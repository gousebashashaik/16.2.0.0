define("tui/singleAccom/view/FlightOptions", [
  "dojo",
  "dojo/mouse",
  "dojo/dom-construct",
  "dojo/text!tui/singleAccom/view/templates/flightDetails.html",
  "dojo/text!tui/singleAccom/view/templates/uspDetails.html",
  "dojo/dom-attr",
  "dojo/on",
  "tui/singleAccom/FlightOptionsController",
  "tui/singleAccom/view/AirportDateToggle",
  "tui/singleAccom/view/FlightGroup",
  "tui/widget/_TuiBaseWidget",
  "tui/widget/mixins/Templatable",
  "tui/singleAccom/view/DurationHighlight",
  "tui/search/nls/Searchi18nable"], function (dojo, mouse, domConstruct, flightDetailsTmpl,uspDetailsTmpl, domAttr, on) {

  dojo.declare("tui.singleAccom.view.FlightOptions", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable, tui.search.nls.Searchi18nable], {

    dataPath: 'searchResult.holidays',

    widgetName: 'flightOptions',

    shortlistStore: null,

    shortlistDisabled: false,

    priceView: null,

    componentId: null,

    stepNumber: 0,
    
    currency :  dojoConfig.currency,

    postCreate: function () {
      var widget = this;
      widget.initSearchMessaging();
      var controller = dijit.registry.byId('flightOptionsController');
     var mediator = dijit.registry.byId('mediator');
      
      var model = controller.register(widget);
      widget.aniteSwitch=controller.model.aniteSwitch;
      widget.shortlistEnabled=controller.model.shortlistEnabled;
      model.siteBrand=controller.model.siteBrand;
      widget.siteName=mediator.model.siteName;
      
      //var aboveUSP= widget.siteName=="firstchoice"?true:false;
      
      widget.moredetails=widget.searchMessaging[dojoConfig.site].moredetails;
      widget.shortlist=widget.searchMessaging[dojoConfig.site].shortlist;
      
      if(widget.displayDiscount){
    	  widget.displayDiscount = JSON.parse(widget.displayDiscount);
      }
      
      if((_.isEmpty(widget.displayDiscount)) && (dojoConfig.site == "falcon")){    	  
    	  widget.displayDiscount=true;
      }
      
      
      widget.currency = currency;
      
      widget.draw(model);
      widget.attachShortListEvents();
      widget.inherited(arguments);

      var saveAnchors = dojo.query('.shortlist', widget.domNode);
      var continueAnchors = dojo.query('.url', widget.domNode);
      _.each(saveAnchors, function (saveAnchor, ind) {
        widget.tagElement(saveAnchor, "SASsaveShortlist");
        domAttr.set(saveAnchor, "analytics-instance", ind + 1);
      });
      _.each(continueAnchors, function (continueAnchor, ind) {
        widget.tagElement(continueAnchor, "SAScontinue");
       // var ind = Number( query(".result-view .plist").indexOf(holidayItem) );
	      domAttr.set(continueAnchor, "analytics-instance", ind + 1);
      });
      var moreDurationsNode = dojo.query('.alternate-durations a', widget.domNode);
      if(moreDurationsNode.length) {
        widget.tagElement(moreDurationsNode[0], "seeDurations");
      }
      
      	// cache analytics values
      	widget.analyticsData = {
        tag: widget.tag,
        number: widget.number,
        analyticsText: {
          singleAccoShortlistTxt: "SASsaveShortlist",
          singleAccoContinue: "SAScontinue"
        }
      };
    },

    refresh: function (modelView) {
      var widget = this;
      widget.destroyWidgets();
      widget.draw(modelView);
      widget.refreshShortlistedPackages();
      var saveAnchors = dojo.query('.shortlist', widget.domNode);
      var continueAnchors = dojo.query('.url', widget.domNode);
      _.each(saveAnchors, function (saveAnchor, ind) {
        widget.tagElement(saveAnchor, "SASsaveShortlist");
        domAttr.set(saveAnchor, "analytics-instance", ind + 1);
      });
      _.each(continueAnchors, function (continueAnchor, ind) {
        widget.tagElement(continueAnchor, "SAScontinue");
       // var ind = Number( query(".result-view .plist").indexOf(holidayItem) );
	      domAttr.set(continueAnchor, "analytics-instance", ind + 1);
      });
    },

    destroyWidgets: function () {
      var widget = this;
      _.each(dijit.findWidgets(widget.domNode), function (w) {
        w.destroyRecursive(true);
      });
      dojo.empty(widget.domNode);
    },

    draw: function (modelView) {
      var widget = this;
      
      // logic for assigning finPos
      var airportCount = 0;
      var dateCount = 0;
      var aboveUSP= widget.siteName == "firstchoice"?true:false;
      _.each(modelView.model.byDates, function(date, ind) {
    	  _.each(date.members, function(holiday, i) {
    		  dateCount++;
    		  holiday.finPos = dateCount;
    	  });
      });
      var html = widget.renderTmpl(modelView.view, {
        'groups': modelView.model,
        'flightDetailsTmpl': flightDetailsTmpl,
        'uspDetailsTmpl':uspDetailsTmpl,
        'aboveUSP':aboveUSP,
        'messages': widget.searchMessaging,
        'showMoreDuration': modelView.showMoreDuration,
        'priceView': widget.priceView,
        'componentId': widget.componentId,
        'stepNumber': widget.stepNumber,
        'aniteSwitch':widget.aniteSwitch,
        'shortlistEnabled':widget.shortlistEnabled,
        'siteBrand':widget.siteBrand,
        'siteName':modelView.siteName,		
		'moredetails':widget.moredetails,
        'shortlist' : widget.shortlist,
        'currency': widget.currency,
'displayDiscount': widget.displayDiscount,
        'saveButtonDisplay' : (widget.siteName === 'falcon') ? false : true
        
      });
      domConstruct.place(html, widget.domNode, "only");
      dojo.parser.parse(widget.domNode);
    },

    attachShortListEvents: function () {
      var flightOptions = this;

      flightOptions.shortlistStore.getObservable().query().observe(function (holidayPackage, remove, add) {
        var action, text,
            buttons = _.filter(dojo.query(".shortlist.button", flightOptions.domNode), function (item) {
              return domAttr.get(item, "data-package-id") === holidayPackage.id;
            });

        if (!buttons.length) return;

        
        if(flightOptions.siteName=="firstchoice" || flightOptions.siteName=="thomson"){
        if (add > -1) {
          action = "addClass";
          text = "shortlisted";
        } else {
          action = "removeClass";
          text = "shortlist";
        }
        }
        else{
        	if (add > -1) {
                action = "addClass";
                text = "saved";
              } else {
                action = "removeClass";
                text = "save";
              }
        }
        	
        _.each(buttons, function (button) {
          var textNode = dojo.query(".text", button)[0];
		  if(flightOptions.siteName=="firstchoice" || flightOptions.siteName=="thomson") dojo[action](button, "shortlisted");
          else dojo[action](button, "saved");
          textNode.innerHTML = text;
        });
      });

      /*_.each(dojo.query('.shortlist', flightOptions.domNode), function (saveButton) {
       dojo.connect(saveButton, 'onclick', function (e) {
       if (flightOptions.shortlistDisabled && !flightOptions.isShortlisted(saveButton)) return;
       flightOptions.updateShortlist(flightOptions.isShortlisted(saveButton), domAttr.get(saveButton, "data-package-id"));
       });
       });*/

      on(flightOptions.domNode, on.selector(".url", "click"), function (event) {
        // Save current page state to SessionStorage
        dojo.publish('tui/searchResults/Mediator/saveState', []);
      });

      on(flightOptions.domNode, ".shortlist:click", function (e) {
        var button = (e.target.tagName.toLowerCase() === 'span' || e.target.tagName.toLowerCase() === 'i') ? dojo.query(e.target).parents('.shortlist')[0] : e.target;
        if (flightOptions.shortlistDisabled && !flightOptions.isShortlisted(button)) return;
        flightOptions.updateShortlist(flightOptions.isShortlisted(button), domAttr.get(button, "data-package-id"));
      });

      // change text on hover/focus
      on(flightOptions.domNode, on.selector(".shortlist", mouse.enter), function (event) {   
        if (flightOptions.isShortlisted(event.target)) dojo.query(".text", event.target).text("remove");  	
      });

      // change text on mouseleave/blur
      on(flightOptions.domNode, on.selector(".shortlist", mouse.leave), function (event) {
    	  if(flightOptions.siteName=="firstchoice" || flightOptions.siteName=="thomson"){
        if (flightOptions.isShortlisted(event.target)) dojo.query(".text", event.target).text("shortlisted");
    	  }
    	  else
    		  if (flightOptions.isShortlisted(event.target)) dojo.query(".text", event.target).text("saved");       
      });

      dojo.subscribe("tui:channel=shortlistStoreDisabled", function (isDisabled) {
        flightOptions.shortlistDisabled = isDisabled;
        var action = isDisabled ? "addClass" : "removeClass";
        _.each(dojo.query(".shortlist", flightOptions.domNode), function (saveButton) {
          if (!flightOptions.isShortlisted(saveButton)) {
            dojo[action](saveButton, "disabled");
          }
        });
      });
    },

    updateShortlist: function (action, packageId) {
      var flightOptions = this;
      action = !action ? "add" : "remove";
      flightOptions.shortlistStore.getObservable().requestData(true, action, packageId)
    },

    refreshShortlistedPackages: function () {
      var flightOptions = this, textNode;
      _.each(dojo.query(".shortlist.button", flightOptions.domNode), function (button) {
        if (flightOptions.isShortlisted(button)) {
          textNode = dojo.query(".text", button)[0];
		  if(flightOptions.siteName=="firstchoice" || flightOptions.siteName=="thomson") {
			dojo.addClass(button, "shortlisted");
            textNode.innerHTML = "shortlisted";
		  }
		  else {
          dojo.addClass(button, "saved");
          textNode.innerHTML = "saved";
        }
        }
      });
    },

    isShortlisted: function (node) {
      var flightOptions = this;
      return flightOptions.shortlistStore.getObservable().query({id: domAttr.get(node, "data-package-id")}).total > 0;
    }
  });

  return tui.singleAccom.view.FlightOptions;
});
