define("tui/searchPanel/view/cruise/DestinationAutoComplete", [
  "dojo",
  "dojo/on",
  'dojo/query',
  'dojox/mobile/sniff',
  "tui/searchPanel/model/DestinationModel",
  "dojo/text!tui/searchPanel/view/cruise/templates/NoResultsDestinations.html",
  "dojo/text!tui/searchPanel/view/cruise/templates/ItineraryListing.html",
  "dojo/_base/fx",
  "dojo/fx",
  "tui/utils/TuiAnimations",
  "tui/searchPanel/model/DestinationModel",
  "tui/searchPanel/view/SearchAutoComplete",
  "tui/search/nls/Searchi18nable"
], function (dojo, on, query, sniff, DestinationModel, noResultsTmpl, itineraryTmpl, baseFx, fx, tuiAnimations) {

  dojo.declare("tui.searchPanel.view.cruise.DestinationAutoComplete", [tui.searchPanel.view.SearchAutoComplete,
    tui.search.nls.Searchi18nable], {

    // ----------------------------------------------------------------------------- properties

    // target url for destination service.
    targetURL: dojoConfig.paths.webRoot + '/ws/textBox',

    // data property field for destination name.
    titleProp: 'name',

    // data property field for destination code.
    valueProp: 'id',

    // data property field for destination code.
    typeProp: 'type',

    // searchProperty for key search.
    searchProperty: 'key',

    infoPopupTemplate: noResultsTmpl,

    errorSpan: null,

    quickSearch: null,

    tag:'300',

    // ----------------------------------------------------------------------------- methods

    postCreate: function () {
      var destinationAutocomplete = this;
      destinationAutocomplete.inherited(arguments);
      destinationAutocomplete.quickSearch = query(destinationAutocomplete.domNode).closest(".quick-search")[0];
      destinationAutocomplete.errorSpan = query('.destinationAutoCompleteErrorSpan', destinationAutocomplete.widgetController.domNode)[0];
      destinationAutocomplete.initSearchMessaging();
      destinationAutocomplete.clearToError();
      dojo.subscribe("tui.searchPanel.view.DestinationAutoComplete.clearList", function (component) {
         destinationAutocomplete.hideAutoCompleteList();
         destinationAutocomplete.clearUnselectedEntries();
         destinationAutocomplete.clearToError();
      });
      destinationAutocomplete.tagElement(destinationAutocomplete.domNode, "pocitinerary search");
    },

    clearUnselectedEntries: function(){
       var destinationAutocomplete = this;
       var lis = query('.itinerary-list>ul>li', destinationAutocomplete.widgetController.domNode);
       if(!_.isEmpty(lis)){
           var unselectedLis = _.filter(lis, function(li){
               return !dojo.hasClass(query('label', li)[0], 'selected');
           });
           //remove all unselected Li's...if not destroyed selection is possible on next load
           _.each(unselectedLis, function(li){
               dojo.destroy(li);
           })

       }


    },

    elementListSelection: function () {
       var listable = this;
       var element = (listable.listItems[listable.listIndex]);

       if (element) {
          var elements = dojo.query("a", element);
          if (elements.length > 0) {
             elements[0].focus();
          }
       }
       listable.setSelectedData();
       listable.hideList();
       if ( !listable.isValidMatch( listable.getSelectedData() ) ){
    	   listable.domNode.value = '';
    	   return false;
       }
       listable.onElementListSelection(listable.getSelectedData(), listable.listData);
        var destinationModel =     listable.getDestinationModel(listable.getSelectedData());
        listable.animatePill(listable.getSelectedData().key,element, query('.where-to', listable.widgetController.domNode)[0], function () {
            listable.destinationGuide.updateModel(destinationModel, "add");
        });

        //Predictive text response...
        listable.addPredictiveTextEntry(listable.getSelectedData());
    },

    isValidMatch: function(selectedData){
    	var listable = this;
    	var destinationData = listable.searchPanelModel.to.data;
    	var matchedDestinations = dojo.filter(destinationData, function(item){ return item.id === selectedData.value  });
    	return matchedDestinations.length ? false : true
    },

    addPredictiveTextEntry: function(data){
        var autoComplete = this;
        var uiListElem = query('.itinerary-list>ul', autoComplete.widgetController.domNode)[0];
        var template = new dojox.dtl.Template(itineraryTmpl);
        var context = new dojox.dtl.Context({data:data});
        var html = dojo.trim(template.render(context));
        /*var selectedEntryElem = dojo.create("div").innerHTML =  html;
        selectedEntryElem = selectedEntryElem.firstChild;*/
        if( !query("." + data.value, uiListElem).length ){
	  		dojo.place(html, uiListElem, "first");
  		}

        autoComplete.domNode.value = '';
    },

    getDestinationModel: function (destination) {
          // summary:
          //		Returns attributes for passed destination
          return new DestinationModel({
              name: destination.key,
              id: destination.value,
              type: destination.type
              //multiSelect:  false
          });
    },

    createListData: function (item) {
          var listable = this;
          return {
              key: item[listable.titleProp],
              value: item[listable.valueProp],
              type: item[listable.typeProp],
              listData: item
          };
    },

    animatePill: function (label, element, dstElement, callback) {
       var text = dojo.trim(label.textContent || label.innerText || label);
       var srcPos = dojo.position(element, true) ;
       var pillElement = dojo.create("div", {
          "innerHTML": "<span>" + text + "</span>",
          "class": "flying-pill",
          "style": {"top": srcPos.y + "px", "left": srcPos.x + "px"}
       }, dojo.body(), "last");

       tuiAnimations.animateTo(pillElement, dstElement, null, null, [baseFx.fadeOut({
          node: pillElement,
          duration: 300,
          onEnd: function (node) {
             dojo.destroy(node);

          }
       })]);
       callback();
    },

      createListElement: function () {
      // summary:
      //      Extends createListElement method, and adds a css class called "autocomplete" to listElement.
      var destinationAutocomplete = this;
      destinationAutocomplete.inherited(arguments);
      dojo.addClass(destinationAutocomplete.listElement, "destination-autocomplete");
    },
      createQueryObject: function (element, keycode) {
          // summary:
          //    Create query for airport autocomplete.
          var searchAutoComplete = this;

          var searchquery = searchAutoComplete.searchPanelModel.generateQueryObject();
          var queryObj = {
              'to[]': searchquery.to,
              'from[]': searchquery.from,
              'when': searchquery.date || '',
              'flexible': searchquery.flexible,
              'duration': searchquery.duration || '',
              'addAStay': searchquery.addAStay
          };

          queryObj[searchAutoComplete.searchProperty] = element.value;
          return queryObj;
      },

    onBeforeSetResults: function (dataresults) {
      // summary:
      //		Method parses destination object json, into an
      //		single destination array.
      var destinationAutocomplete = this, data = null, results = dataresults.data;
      destinationAutocomplete.inherited(arguments);

      destinationAutocomplete.error = dataresults;
      if(!results) return [];

      _.each(results, function(destData, destProp){
        data = data || [];
        //TODO: TBC in CommonWeb
        if(destData && _.size(destData) > 0) {
          // add collection title
          data.push(new DestinationModel({
            'id': 'title',
            'name': destinationAutocomplete.searchMessaging.keys[destProp]
          }));

          _.each(destData, function (item) {
            data.push(dojo.mixin(new DestinationModel(), item));
          });
        }

      });

      return data;
    },

    /**
       * ###onType()
       * called when the user is typing (within the configured delay)
       * connect using aspect.after (previously used dojo.connect)
       *
       * called by `onOtherKey`
       * @param {Node} element the autocomplete's input field
       * @param event
    */
      onType: function (element, event) {
        var destinationAutocomplete = this;
        destinationAutocomplete.clearToError();
        destinationAutocomplete.inherited(arguments);
        //remove Error class since we are below the limit.
        if (element.value.length >= destinationAutocomplete.charNo) {
             //
        } else {
           destinationAutocomplete.displayToErrorMessage();
        }
      },

    onNoResults: function (listElementUL) {
      // summary:
      //    Extend default method and publish guide link
      var destinationAutocomplete = this;
      destinationAutocomplete.hideList();
      destinationAutocomplete.addToError();
    },

    addToError: function(message){
       var destinationAutocomplete = this;
       destinationAutocomplete.searchPanelModel.searchErrorMessages.set("to", {errorCode: 'noResults'});
       destinationAutocomplete.errorSpan.innerHTML = destinationAutocomplete.createNoMatchMessage(destinationAutocomplete.error.searchError);
       on(destinationAutocomplete.errorSpan, on.selector('.airport-guide', "click"), function (event) {
            var destination = null;
            // add entry item to whereTo
            _.each(destinationAutocomplete.error.searchError.entry, function (entry) {
                //destination = dojo.mixin(new DestinationModel(), entry);
                var destinationModel =     new DestinationModel({
                    name: entry.name,
                    id: entry.id,
                    type: entry.type
                });
                destinationAutocomplete.destinationGuide.updateModel(destinationModel, "add");
                //Predictive text response...
                destinationAutocomplete.addPredictiveTextEntry({key:destinationModel.name, value:destinationModel.id, type:destinationModel.type});
            });

            // remove all items from whereFrom
            _.each(destinationAutocomplete.searchPanelModel.from.data, function (match) {
                destinationAutocomplete.searchPanelModel.from.remove(match.id);
            });

            // open airport expandable.
            dojo.publish("tui.searchPanel.view.cruise.AirportGuide.openExpandable");

            // clear error which will close popup.
           destinationAutocomplete.searchPanelModel.searchErrorMessages.set("to", {});
       });
    },

    clearToError: function(){
       var destinationAutocomplete = this;
       destinationAutocomplete.errorSpan.innerHTML = '';
       // delete no match error & Empty Destination error if present
       destinationAutocomplete.searchPanelModel.searchErrorMessages.set("to", {});
    },

    displayToErrorMessage: function(name, oldErrorInfo, newErrorInfo){
       var destinationAutocomplete = this;
       var action = (!_.isEmpty(destinationAutocomplete.searchPanelModel.searchErrorMessages.to)) ? "addClass" : "removeClass";
       dojo[action](destinationAutocomplete.quickSearch, "error");
       dojo[action](destinationAutocomplete.errorSpan, "error");
    },

    createNoMatchMessage: function (data) {
          var searchAutoComplete = this;
          var template = new dojox.dtl.Template(searchAutoComplete.infoPopupTemplate);
          var context = new dojox.dtl.Context(data);
          return template.render(context);
    },

    onChange: function (name, oldValue, value) {
      // summary:
      //    If destination guide is selected, we have to let all listens to this channel know.
      var destinationAutocomplete = this;

      if (value === null) {
        return;
      }
      if (value.listData.id === "destinationguide") {
        dojo.publish("tui.searchPanel.view.DestinationGuide.openExpandable");
        return;
      }
      destinationAutocomplete.inherited(arguments);
    },

    onBeforeListItemRender: function (li, item) {
      var destinationAutocomplete = this;
      if (item.id === "destinationguide") {
        dojo.addClass(li, "guide-link destination-guide");
      }
      if (item.id === "title") {
        dojo.addClass(li, "title " + destinationAutocomplete.disableItemClass);
      }
    },

    onDisableSelection: function () {
      dojo.publish('tui.searchPanel.view.DestinationMultiFieldList.onTextboxInputFocus');
    },

    onEnterKey: function (event, domNode) {
      var destinationAutocomplete = this;
      destinationAutocomplete.inherited(arguments);

      // hard-coding focus to next search panel element
      // @TODO: fix this properly as this is a temporary hack for go-live
      if (destinationAutocomplete.widgetController.searchApi === "searchPanel" && dojo.keys.TAB === (event.charCode || event.keyCode)) {
        dojo.stopEvent(event);
        dojo.publish("tui.searchPanel.view.SearchDatePicker.focusCalendar", [destinationAutocomplete.widgetController]);
      }
    },

    attachListKeyEvent: function (domNode) {
        var listable = this;
        //
        if (sniff('touch')) {
	        dojo.connect(domNode, "onkeydown", function (event) {
	          if (listable.domNode.value === "") {
	            listable.unSelect();
	          }
	          listable.listkeydown(event, this);
	        });
        }else{
        	listable.inherited(arguments);
        }
     },

    onAfterRender: function () {
      var destinationAutocomplete = this;
      /*
      var node = dojo.query(".guide-link a", destinationAutocomplete.listNode)[0];
      if (node) {
        destinationAutocomplete.tagElement(node, "See All Destinations");
      }
      */
    }
  });

  return tui.searchPanel.view.cruise.DestinationAutoComplete;
});
