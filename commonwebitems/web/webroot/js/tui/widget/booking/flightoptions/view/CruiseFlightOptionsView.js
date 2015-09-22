define("tui/widget/booking/flightoptions/view/CruiseFlightOptionsView", [
  "dojo/_base/declare",
  "dojo/query",
  "dojo/dom",
  "dojo/dom-attr",
  "dojo/dom-construct",
  "dojo/dom-class",
  "dojo/Evented",
  "dojo/topic",
  "dojo/parser",
  "dojo/keys",
  "dojo/_base/lang",
  "dojo/_base/array",
  "dojo/dom-style",
  "tui/widget/_TuiBaseWidget",
  "tui/widget/booking/bookflowMsgs/nls/Bookflowi18nable",
  "dojox/dtl/_Templated",
  "tui/widget/mixins/Templatable",
  "dojo/text!tui/widget/booking/flightoptions/view/templates/CruiseFlightOptionsViewTmpl.html",
  "dojo/text!tui/widget/booking/flightoptions/view/templates/ContentTmp.html",
  "tui/widget/booking/flightoptions/view/FlightOptionsView",
  "tui/widget/booking/flightoptions/view/CruiseFlightAlternativeView",
  "tui/widget/booking/constants/BookflowUrl",
  "dojo/on",
  "dojo/_base/json",
  "tui/widget/form/SelectOption"


], function (declare, query, dom,domAttr, domConstruct, domClass, Evented, topic, parser,keys,lang, array,domStyle, _TuiBaseWidget,Bookflowi18nable, dtlTemplate, Templatable, CruiseFlightOptionsViewTmpl,ContentTmp,FlightOptionsView,CruiseFlightAlternativeView,BookflowUrl, on, jsonUtil) {

  return declare('tui.widget.booking.flightoptions.view.CruiseFlightOptionsView', [_TuiBaseWidget, dtlTemplate, Templatable, Evented,FlightOptionsView,Bookflowi18nable], {

    tmpl: CruiseFlightOptionsViewTmpl,
    contentTmpl: ContentTmp,
    templateString: "",
    widgetsInTemplate: true,
    childAgeFlag: false,
    childMealAvailable: false,
    adultorseniorFlag: "",
    Flight_MoreAirports_Link: null,
    airportChoices: [],
    airportData: null,
    selectedAirport: null,
    currentAirport: null,

    postCreate: function(){
    	this.initBookflowMessaging();
    	//only for cruise bookflow
    	var checkRefNode = dom.byId("contentSectionBlock");

    	if(checkRefNode != null){
    		this.setContentForFlight();
    	}



    	this.inherited(arguments);

    },

    refresh: function (response) {
    	var widget = this;

    	widget.cruiseFlightAlternativeView = null;
    	 if (widget.isSelectedFlight) {
             if(!_.isUndefined(widget.updateSummaryPanel) && widget.updateSummaryPanel){
                 dojo.publish("tui/summaryPanel/controller/alternateFlightAdded", response);
             }else {
                 var controller = dijit.registry.byId("controllerWidget").publishToViews("flights", response);
             }
             domConstruct.destroy(widget.dateTeaserButton);
             domConstruct.destroy(widget.durationTeaserButton);
             domConstruct.destroy(widget.airportTeaserButton);
             widget.model = response;

             widget.alternateflightsOverlaywidget.close();
             return;
           }
    	widget.model = response;
    	this.cruiseFlightOptionsView = null;
    	 this.defaultDisplayTabs = query(".duration-standard", this.domNode);
    	this.airportChoices = [];
    	this.airportData = [];
    	var flightDurationView = this;
    	selectedDuration: null,


    	this.Flight_MoreAirports_Link = widget.model.flightOptionsStaticContentViewData.flightContentMap.Flight_MoreAirports_Link;

    	 var durationFilteredArr = array.filter(this.model.calendarViewData.duration, lang.hitch(this, function (durationItem) {

             if (durationItem.selected == true) {

               this.selectedDuration = durationItem.duration;
               this.setActiveTab(durationItem.duration);
             }

         }));

        airportDatamap = dojo.map(widget.model.calendarViewData.departureAirport, lang.hitch(this, function (airportItem) {

            if (airportItem.shouldDisplay == false) {
              this.airportChoices.push(airportItem);
            }
            else {
              this.airportData.push(airportItem);
            }

        }));


        var airportFilteredArr = array.filter(this.model.calendarViewData.departureAirport, lang.hitch(this, function (airportItem) {
            if (airportItem.selected == true) {
              this.selectedAirport = airportItem.airportName;
              this.currentAirport = airportItem.airportCode;
              if(!_.isEmpty(airportItem.flightViewData)){
            	  this.depatureDate = airportItem.flightViewData[0].flightViewData[0].outboundSectors[0].schedule.departureDate;
                  this.arrivalDate = airportItem.flightViewData[0].flightViewData[0].inboundSectors[0].schedule.departureDate;
              }
            }
        }));

        // more options listener
        on(this.domNode, on.selector(".custom-dates", "click, keyup"), lang.hitch(this, function (event) {
          dojo.stopEvent(event);
          if (event.keyCode && event.keyCode !== keys.ENTER) return;
        }));

        on(flightDurationView.domNode, on.selector(".airportpreset", "click, keyup"), lang.hitch(this, function (event) {

            if (event.keyCode && event.keyCode !== keys.ENTER) {
              return;
            }

            	this.setAirportDuration(this.selectedDuration, event.target);

        }));

        on(this.domNode, on.selector(".airport-standard", "click, keyup"), lang.hitch(this, function (event) {
            console.log('Tab clicked');
            var currentTab = this;
            if (event.keyCode && event.keyCode !== keys.ENTER) return;
            // ignore if already active
            if (dojo.hasClass(event.target, "active")) return;
            this.setAirportDuration(this.selectedDuration, event.target.parentNode);

        }));

        // listen to custom option
        on(flightDurationView.domNode, on.selector('.customize > a', "click, keyup"), lang.hitch(this, function (event) {

          this.setCustomDuration(this.domNode);
        }));

        on(query('.customize > a', this.domNode), 'keypress', lang.hitch(this, function (event) {
          if (event.keyCode === keys.ENTER || event.keyCode === keys.TAB) {
            this.setCustomDuration(this.domNode);
          }
        }));

        widget.jsonData = response;
        var html = widget.renderTmpl(widget.tmpl, widget);
        domConstruct.place(html, widget.domNode, "only");

        if (widget.cruiseFlightAlternativeView !== null) {
        	widget.cruiseFlightAlternativeView.destroy();
        	widget.cruiseFlightAlternativeView = null
          }

        widget.cruiseFlightAlternativeView = new CruiseFlightAlternativeView({
              "model": response.calendarViewData.departureAirport

        });

        domConstruct.place(widget.cruiseFlightAlternativeView.domNode, query(".alternate-flights-section", widget.domNode)[0], 'only');
        //parser.parse(widget.domNode);
        this.attachEvents();
        this.setCruiseFlightOptionSwitcher();
        this.alternateflightsOverlaywidget.open();
    },

    attachEvents : function () {

    	this.summaryButtonHandlers =  on(query(".button", this.domNode), "click", lang.hitch(this, function(event){
    		if(event.target.value){
      		this.summaryButtonHandlers.remove();
      		topic.publish("flightoptions.summarypanel.button.clicked", event.target.value);
    		}
    		else{
    			 this.alternateflightsOverlaywidget.close();
    		}
    	}));

    	var summaryPanelModelObj = null;

    	on(query(".moreairportpreset", this.domNode),"click", lang.hitch(this, function (tab) {
    		_.forEach(this.model.calendarViewData.departureAirport, function (item, i) {
    			summaryPanelModelObj = item;

	        });

	        this.cruiseFlightAlternativeView = new CruiseFlightAlternativeView({
	        	"model": summaryPanelModelObj,
	        	"flightIndexValue": 0
	        });
	        domConstruct.place(this.cruiseFlightAlternativeView.domNode, query(".alternate-flights-section", this.domNode)[0], 'only');
    	}));
    },

    setCruiseFlightOptionSwitcher: function(){
    	var getFlightDiv = query('.flight-options-section', this.domNode);
		_.each(getFlightDiv,  function(item){
			var buttons = query('.button', item);
			_.each(buttons, function(button) {
				dojo.connect(button, 'click', function(e) {
					_.each(getFlightDiv,  function(item){
						var buttons = query('.button', item);
						_.each(buttons, function(button){
							domClass.remove(button, 'selected');
						});
						domClass.remove(item, 'highlighted-div');
					});
					domClass.add(button, 'selected');
					domClass.add(item, 'highlighted-div');
				});
			});
		});
    },

    setContentForFlight: function(){
    	console.log("set data");
    	this.toggleDisplayFlag = true;
    	var roomIndexVal = this.bookflowMessaging[dojoConfig.site].brandMapping[this.jsonData.packageType].roomSectionMetaData.roomSummary;
    	var cruiseIndexVal = this.bookflowMessaging[dojoConfig.site].brandMapping[this.jsonData.packageType].cruiseSummary;
    	this.roomViewData = _.find(this.jsonData.packageViewData.accomViewData, function(item,index){ if(index == roomIndexVal) return item })
    	this.cruiseViewData = _.find(this.jsonData.packageViewData.accomViewData, function(item,index){ if(index == cruiseIndexVal) return item })

    	this.multipleRoomOrCabin = false

    	if(this.cruiseViewData.roomViewData.length > 1){
    		this.multipleRoomOrCabin = true;
    		this.roomOrCabinCount = this.cruiseViewData.roomViewData.length;
    	}


    	if(!_.isEmpty(this.roomViewData)){
    		this.toggleDisplayFlag = false;
    	}

    	var html = this.renderTmpl(this.contentTmpl, this);
    	var destoryNode = query('.text',dom.byId("contentSectionBlock"))[0];

    	domConstruct.destroy(destoryNode);
    	domConstruct.place(html, dom.byId("contentSectionBlock"), "first");



    },

    addLoading: function(){
       var flightOptionView = this;
       if(!_.isUndefined(flightOptionView.updateSummaryPanel) && flightOptionView.updateSummaryPanel){
          console.log('Todo for Cruise Summary panel');
       }else{
          this.inherited(arguments);
       }
    }


  });
});