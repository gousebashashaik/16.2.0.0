define("tui/widget/booking/flightoptions/view/FlightOptionsView", [
  "dojo",
  "dojo/_base/declare",
  "dojo/dom-construct",
  "dijit/registry",
  "dojo/dom-attr",
  "dojo/store/Observable",
  "dojo/on",
  "dojo/dom",
  "dojo/query",
  "dojo/topic",
  "dojo/dom-style",
  "dojo/_base/lang",
  "dojo/_base/array",
  "dojo/parser",
  "dojo/keys",
  "dojo/has",
  "dojo/text!tui/widget/booking/flightoptions/view/templates/FlightOptionsView.html",
  "dojo/_base/connect",
  "tui/widget/booking/flightoptions/view/FlightOptionsSummaryPanel",
  "tui/widget/booking/flightoptions/view/AlternateflightsOverlay",
  "tui/searchResults/view/InfoPopup",
  "dojo/dom-class",
  "dojo/string",
  "dojo/date/locale",
  "tui/widget/booking/flightoptions/view/FlightOptionDatepicker",
  "tui/search/nls/Searchi18nable",
  "tui/widget/mixins/Templatable",
  "tui/widget/_TuiBaseWidget",
  "tui/widget/popup/Tooltips"],
    function (dojo, declare, domConstruct, registry, domAttr, Observable, on, dom, query, topic, domStyle, lang, array,
              parser, keys,has, flightOptionsViewTmpl, connect, FlightOptionsSummaryPanel, AlternateflightsOverlay, Popup,domClass) {

    return declare("tui.widget.booking.flightoptions.view.FlightOptionsView", [
          tui.widget._TuiBaseWidget,
          tui.widget.mixins.Templatable,
          tui.search.nls.Searchi18nable], {

      // ----------------------------------------------------------------------------- properties
      customDatesOptionView: null,
      tmpl: flightOptionsViewTmpl,
      templateView: "initialize",
      duration: null,
      maxDuration: 0,
      durationMaxNightsMsg: "",
      selectYourOwnMsg: "",
      moreChoices: [],
      airportChoices: [],
      customTab: null,
      errorMessage: null,
      model: null,
      durationData: null,
      airportData: null,
      selectedDuration: null,
      selectedAirport: null,
      dynamicselectedAirport: null,
      currentAirport: null,
      calendarData: null,
      alternateflightsOverlaywidget: null,
      months: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
      dataPath: "jsonData",
      // info popup displayed on load (if not cancelled)
      infoPopup: null,
      // duration for which to show the info popup
      infoPopupDuration: 10000,
      subscribableMethods: ["displayInfoPopup"],
      calendarStartDate: null,
      calendarEndDate: null,
      calendarAvail: [],
      priceMap: [],
      Flight_MoreAirports_Link: null,
      Flight_MoreDurations_Link: null,
      flightViewDataRef:"",

      generateRequest: function (field) {
        var request = {'duration': this.model.activeDuration};
        if (field === 'duration') {
          request["searchRequestType"] = "Duration";
        }
        return request;
      },

      clearErrors: function () {
        if (this.errorMessage.length > 0) {
          _.forEach(this.errorMessage, function (item) {
            dojo.destroy(query(item, this.domNode)[0]);
          });
        }
      },

      getMaxDuration: function () {
        var flightDurationView = this;
        return flightDurationView.maxDuration;
      },


      refresh: function (response) {
        if (this.isSelectedFlight) {
          if(!_.isUndefined(this.updateSummaryPanel) && this.updateSummaryPanel){}
          else{
             var controller = dijit.registry.byId("controllerWidget").publishToViews("flights", response);
          }
          domConstruct.destroy(this.dateTeaserButton);
            domConstruct.destroy(this.durationTeaserButton);
            domConstruct.destroy(this.airportTeaserButton);
            this.model = response;
          this.alternateflightsOverlaywidget.close();
          return;
        }
        var airportArry = [];
        var moreAirports = [];
        var calendermap = [];
        var calenderAvailarry = [];
        var calenderStr = [];
        var priceStr = [];
        this.model = response;
        this.durationData = [];
        this.moreChoices = [];
        this.airportChoices = [];
        this.airportData = [];
        _.forEach(this.model.calendarViewData.availability, function (item, i) {

          calenderStr.push(item.displayDate);
          priceStr.push(item.price);

        });

        this.calendarAvail = calenderStr;
        this.priceMap = priceStr;
        this.defaultDisplayTabs = query(".duration-standard", this.domNode);
        this.moreChoicesOptions = query(".preset", this.domNode);
        this.customChoice = query(".customize", this.domNode);

        durationDatamap = dojo.map(this.model.calendarViewData.duration, lang.hitch(this, function (durationItem) {
          if (durationItem.shouldDisplay == false) {
            this.moreChoices.push(durationItem.duration);
          }
          else {
            this.durationData.push(durationItem.duration);
          }

        }));
        var durationFilteredArr = array.filter(this.model.calendarViewData.duration, lang.hitch(this, function (durationItem) {

          if (durationItem.selected == true) {

            this.selectedDuration = durationItem.duration;
            this.setActiveTab(durationItem.duration);
          }

        }));

        airportDatamap = dojo.map(this.model.calendarViewData.departureAirport, lang.hitch(this, function (airportItem) {
          if (airportItem.shouldDisplay == false) {
            this.airportChoices.push(airportItem);
          }
          else {
            this.airportData.push(airportItem);
          }

        }));


        var airportFilteredArr = array.filter(this.model.calendarViewData.departureAirport, lang.hitch(this, function (airportItem) {
          if (airportItem.selected == true) {
            this.dynamicselectedAirport = airportItem;
            this.selectedAirport = airportItem.airportName;
            this.currentAirport = airportItem.airportCode;
            this.addCustomTab(airportItem.airportCode);
          }
        }));

        this.inherited(arguments);

        var holder=query('.holder')[0];
        var durationTextBox=query('.durationTextBox')[0];
        if(holder){
        on(query('.holder')[0], "click", lang.hitch(this, this.iePlaceHolders));
        }
        if(durationTextBox){
        on(query('.durationTextBox')[0], "click", lang.hitch(this, this.iePlaceHolders));
        }


        var tabStandard = query(".tab-standard", this.domNode);
        for(var index=0; index<tabStandard.length; index++){
        	this.tagElement(tabStandard[index], tabStandard[index].id);
        }
        var subTab = query(".sub-tab", this.domNode);
        for(var index1=0; index1<subTab.length; index1++){
        	this.tagElement(subTab[index1], subTab[index1].id);
        }

       	this.tagElements(query(".check-box", this.domNode), "calSelection");
      },


      changeFlightsClicked: function (data) {
        this.model = data;
        this.openOverlay(false);
      },

      handleNoResults: function (name, oldDuration, newDuration) {
        var flightDurationView = this;
        if (name === 'duration') {
          if (flightDurationView.errorMessage.length > 0) {
            _.forEach(flightDurationView.errorMessage, function (item) {
              dojo.destroy(query(item, flightDurationView.domNode)[0]);
            });
          }
          var html = flightDurationView.renderTemplate("error", {"message": flightDurationView.searchMessaging.errors.durationNoResults});
          flightDurationView.errorMessage.push(dojo.place(html, query(".customize", flightDurationView.domNode)[0], "last"));

          if (flightDurationView.isCustomDuration(newDuration)) {
            if (!flightDurationView.isCustomDuration(oldDuration) && flightDurationView.customTab) {
              flightDurationView.removeCustomTab();
            }
            flightDurationView.resetCustomDuration();
          } else {
            topic.publish("tui/searchResults/view/SearchResultsComponent/showNoResultsPopup");
          }
          //reset to old duration
          flightDurationView.model.activeDuration = oldDuration;
          flightDurationView.setActiveTab(oldDuration);
        }
      },

      // ----------------------------------------------------------------------------- flightDurationView methods

      postCreate: function () {
        // summary:
        //      Sets default values for class properties.

        var flightDurationView = this;
        this.flightOptionsSummaryPanel = null;
        this.mediator = dijit.registry.byId('mediator');
        this.model = this.mediator.registerController(this);
        this.isSelectedFlight = false;

        this.dateTeaserButton = dom.byId("dateTeaserButton");
        this.durationTeaserButton = dom.byId("durationTeaserButton");
        this.airportTeaserButton = dom.byId("airportTeaserButton");
        this.viewallButton = dom.byId("viewall");


        topic.subscribe("flightoptions.selecteddate.display", lang.hitch(this, this.getFlightDetailsForSelectedDate));
        topic.subscribe("flightoptions.alternateflightsOverlay.close", lang.hitch(this, this.closeOverlay));
        topic.subscribe("flightoptions.summarypanel.button.clicked", lang.hitch(this, this.summaryPanelButtonClicked));
        topic.subscribe("flightOptions.overlay.show", lang.hitch(this, this.changeFlightsClicked));
        topic.subscribe("flightOptions.flightSummaryPlane", lang.hitch(this, this.setFlightSwitcher));
        topic.subscribe("flightoptions.alternateflights.close", lang.hitch(this, this.closePopup));

        flightDurationView.errorMessage = [];

        this.alternateflightsOverlaywidget = new AlternateflightsOverlay({widgetId: 'altflight-overlay', modal: true});

        // attach event listeners
        this.attachDurationViewEventListeners();


        this.initSearchMessaging();


        this.durationMaxNightsMsg = lang.replace(
            flightDurationView.searchMessaging.errors.durationMaxNights, {MAX_NIGHTS: flightDurationView.getMaxDuration()}
        );
        this.selectYourOwnMsg = lang.replace(
            flightDurationView.searchMessaging.holidayDuration.selectYourOwn, {MAX_NIGHTS: flightDurationView.getMaxDuration()}
        );


        this.inherited(arguments);


      },

      closePopup : function () {
    	  this.alternateflightsOverlaywidget.close();
      },

      closeOverlay: function () {
        delete this.selectedDate;
      },

      summaryPanelButtonClicked: function (packageId) {
        this.isSelectedFlight = true;
        this.mediator.fire(null, null, null, null, packageId);

      },

      setFlightSwitcher: function() {

  		var getFlightDiv = query('.flight-options-section', this.domNode);

  		_.each(getFlightDiv,  lang.hitch(this, function(item){

  			var buttons = query('.button', item);
  		_.each(buttons, lang.hitch(this, function(button) {
  			domClass.remove(button, 'selected');
  			on(button, "click", lang.hitch(this, this.summaryPanelButtonClicked));

  			}));
  		}));
  	 },

      displayInfoPopup: function (cookieCheck) {
        var flightDurationView = this;
        // don't show if only one duration
        if (flightDurationView.model.length === 1) return;

        if (!cookieCheck) {
          flightDurationView.helpPopup = flightDurationView.createInfoPopup();
        } else {
          flightDurationView.helpPopup = cookie('durationInfoPopup') ? null : flightDurationView.createInfoPopup();
        }

        if (flightDurationView.helpPopup) flightDurationView.helpPopup.open();
      },

      createInfoPopup: function () {
        var flightDurationView = this;

        var activeTab = query(".duration-standard", flightDurationView.domNode).filter(function (node) {
          return dojo.hasClass(node, "active")
        })[0];

        return flightDurationView.helpPopup || new Popup({
          floatWhere: "position-top-center",
          heading: lang.replace(
              flightDurationView.searchMessaging.holidayDuration.popupHeading, {DURATION: flightDurationView.model.activeDuration}
          ),
          message: flightDurationView.searchMessaging.holidayDuration.popupMessage,
          removeText: flightDurationView.searchMessaging.holidayDuration.dontShow,
          elementRelativeTo: activeTab,
          showDuration: flightDurationView.infoPopupDuration,
          posOffset: {
            left: 0,
            top: -18
          }
        }, null);
      },

      removeDurations: function () {
        // summary:
        //      Removes all duration options
        var flightDurationView = this;
        dojo.destroy(query("ul", flightDurationView.domNode)[0]);
      },

      iePlaceHolders: function () {
    	  domStyle.set(query('.holder')[0], "display", "none");
    	  dom.byId("durationTextBox").focus();
        },

      attachDurationViewEventListeners: function () {
        // summary:
        //      Adds event listeners to view
        var flightDurationView = this;


        on(this.viewallButton, "click", lang.hitch(this, function () {
          this.openOverlay(false);
        }));
        if(!_.isUndefined(this.updateSummaryPanel) && this.updateSummaryPanel){
           console.log('Todo for Cruise Summary panel');
        }else{
           on(this.dateTeaserButton, "click", lang.hitch(this, this.openOverlay, true, "DATE"));
           on(this.durationTeaserButton, "click", lang.hitch(this, this.openOverlay, true, "DURATION"));
           on(this.airportTeaserButton, "click", lang.hitch(this, this.openOverlay, true, "AIRPORT"));
        }

        // standard tabs listener
        on(this.domNode, on.selector(".duration-standard", "click, keyup"), lang.hitch(this, function (event) {


          if (event.keyCode && event.keyCode !== keys.ENTER) return;

          // close infoPopup if open
          topic.publish("tui.searchResults.view.InfoPopup.close");

          // ignore if already active
          if (dojo.hasClass(event.target.parentNode, "active")) return;
          this.defaultDisplayTabs.removeClass("active");
          this.setDuration(event.target.parentNode, this.currentAirport);
        }));

        on(this.domNode, on.selector(".airport-standard", "click, keyup"), lang.hitch(this, function (event) {
          //dojo.stopEvent(event);
          var currentTab = this;
          if (event.keyCode && event.keyCode !== keys.ENTER) return;
          // ignore if already active
          if (dojo.hasClass(event.target, "active")) return;
          this.defaultDisplayTabs.removeClass("active");
          this.setAirportDuration(this.selectedDuration, event.target.parentNode);
        }));

        on(dojo.body(), "click", lang.hitch(this, function (event) {
          this.closeMoreOptionsList();
        }));

        // more options listener
        on(this.domNode, on.selector(".custom-dates", "click, keyup"), lang.hitch(this, function (event) {
          dojo.stopEvent(event);
          if (event.keyCode && event.keyCode !== keys.ENTER) return;

          this.openMoreOptionsList(event);
        }));

        // listen to preset options
        on(this.domNode, on.selector(".preset", "click, keyup"), lang.hitch(this, function (event) {

          if (event.keyCode && event.keyCode !== keys.ENTER) {
            return;
          }
          if(this.defaultDisplayTabs){
          this.defaultDisplayTabs.removeClass("active");
          this.setDuration(event.target, this.currentAirport);
          this.closeMoreOptionsList();
          }
        }));

        on(flightDurationView.domNode, on.selector(".airportpreset", "click, keyup"), lang.hitch(this, function (event) {
          if (event.keyCode && event.keyCode !== keys.ENTER) {
            return;
          }
          if(this.defaultDisplayTabs){
          this.defaultDisplayTabs.removeClass("active");
          }
          if(this.selectedDuration){
          this.setAirportDuration(this.selectedDuration, event.target);
          this.closeMoreOptionsList();
          }
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

        on(query('.customize input', this.domNode), 'keypress', lang.hitch(this, function (event) {
          if (event.keyCode === keys.ENTER) {
            this.setCustomDuration(this.domNode);
          }
        }));


      },

      addLoading: function(){
          dojo.addClass(dom.byId("top"), 'updating');
          dojo.addClass(dom.byId("main"), 'updating');
          dojo.addClass(dom.byId("right"), 'updating');
      },

      openOverlay: function (isTeaser, teaserType) {

        var departureAirportCode, duration, date;
        this.addLoading();
        this.isTeaser = isTeaser;
        this.isSelectedFlight = false;

        var listOfFlightViewData =  this.model.packageViewData.flightViewData;
        var refIndexValue = this.flightIndexVal;
        this.flightViewDataRef = _.find(listOfFlightViewData, function(item,index){ if(index == refIndexValue) return item });


        if (isTeaser) {
          if (teaserType == "AIRPORT") {
            departureAirportCode = this.model.airportTeaserViewData.airportCode;
            duration = this.flightViewDataRef.duration;
            date = this.flightViewDataRef.outboundSectors[0].schedule.departureDateInMiliSeconds;
          } else if (teaserType == "DURATION") {
            departureAirportCode = this.flightViewDataRef.outboundSectors[0].departureAirport.code;
            duration = this.model.durationTeaserViewData.duration;
            date = this.flightViewDataRef.outboundSectors[0].schedule.departureDateInMiliSeconds;
          } else if (teaserType == "DATE") {
            departureAirportCode = this.flightViewDataRef.outboundSectors[0].departureAirport.code;
            duration = this.flightViewDataRef.duration;
            date = this.model.dateTeaserViewData.date;
          }

        } else {
          departureAirportCode = this.flightViewDataRef.outboundSectors[0].departureAirport.code;
          duration = this.flightViewDataRef.duration;
          date = this.flightViewDataRef.outboundSectors[0].schedule.departureDateInMiliSeconds;
        }
        this.actualDate = new Date(this.flightViewDataRef.outboundSectors[0].schedule.departureDateInMiliSeconds);
        if (teaserType == "DATE") {
          this.selectedDate = new Date(this.model.dateTeaserViewData.date);
        } else {
          this.selectedDate = new Date(this.flightViewDataRef.outboundSectors[0].schedule.departureDateInMiliSeconds);
        }
        this.mediator.fire(duration, departureAirportCode, this.getSelectedFilters(), date, null);

      },

      openMoreOptionsList: function (event) {
        var flightDurationView = this;

        // close list if open and either list element or "more options text" or "arrow" are clicked
        var moreOptionsTargets = [event.target.parentElement];

        dojo.forEach(query("span", event.target.parentElement), function (item) {

          moreOptionsTargets.push(item);
        });
        if (dojo.hasClass(event.target.parentElement, "active") && (dojo.indexOf(moreOptionsTargets, event.target) > -1)) {

          flightDurationView.closeMoreOptionsList();
          return;
        }
        if(dojo.indexOf(moreOptionsTargets, event.target) == 1){
        	flightDurationView.closeMoreOptionsList();
        	}
        dojo.addClass(event.target.parentElement, "active");
      },

      closeMoreOptionsList: function () {
        query(".custom-dates").removeClass("active");
        if(has("ie") == 8 || has("ie") == 9){
         var input = query("input", this.domNode)[0];
         if(input){
          var holderVal = domAttr.get(input, "value");
          if(holderVal == ""){
        domStyle.set(query('.holder')[0], "display", "block");
          }
        }
        }
      },

      setDuration: function (node, currentAirport) {
        var tabDuration = domAttr.get(node, "data-dojo-value");
        var newDuration = parseInt(tabDuration, 10);
        this.model.activeDuration = newDuration;
        var date = this.flightViewDataRef.outboundSectors[0].schedule.departureDateInMiliSeconds;
        this.mediator.fire(tabDuration, currentAirport, this.getSelectedFilters(), date, null);
      },

      setAirportDuration: function (duration, node) {

        var airportCode = domAttr.get(node, "data-dojo-value");
        var date = this.flightViewDataRef.outboundSectors[0].schedule.departureDateInMiliSeconds;
        this.mediator.fire(duration, airportCode, this.getSelectedFilters(), date, null);
      },

      resetCustomDuration: function () {
        var flightDurationView = this;
        var input = query('input', flightDurationView.domNode)[0]
        input ? input.value = '' : null;
      },

      showError: function (message) {
        this.clearErrors();
        var durationErrorDiv = "<p class='message error'>" + message + "</p>";
        this.errorMessage.push(domConstruct.place(durationErrorDiv, query(".customize", this.domNode)[0], "last"));
      },

      getSelectedFilters: function () {
        var selectedFilters = [];
        query("input[type='checkbox']", this.domNode).forEach(function (node) {
          if (node.checked) {
            selectedFilters.push(node.id);
          }
        });

        return selectedFilters;


      },

      setCustomDuration: function (node) {
        // summary:
        //      Sets duration of user-input custom duration to searchResultsModel

        var input = query("input", node)[0];
        var placeholder = domAttr.get(input, "placeholder");

        if (!this.testNumber(input.value)) {
          this.showError(this.searchMessaging.errors.durationInputError)
          return;
        }
        if (parseInt(input.value, 10) > this.getMaxDuration()) {
          this.showError(this.durationMaxNightsMsg)
          return;
        }
        if (input.value !== placeholder || input.value !== "") {
          var newDuration = parseInt(input.value, 10);
          var date = this.flightViewDataRef.outboundSectors[0].schedule.departureDateInMiliSeconds;
          this.mediator.fire(newDuration, this.currentAirport, this.getSelectedFilters(), date, null);
        }
      },

      setActiveTab: function (value) {
        // summary:
        //      Deactivates all tabs and sets tab with currently selected value to active

        dojo.forEach(this.defaultDisplayTabs, function (tab) {
          if (parseInt(domAttr.get(tab, "data-dojo-value"), 10) === value) {
            domClass.add(tab, "active");
          }
        });

      },

      isEmpty : function(obj) {
			for(var key in obj) {
				if(obj.hasOwnProperty(key))
					return false;
			}
			return true;
		},


      addCustomTab: function (value) {
//           Adds a custom tab when user selects duration from "more options" menu
        if (this.checkboxHandlers) {
          this.checkboxHandlers.remove();
        }
        if (!this.selectedDate) {
          this.selectedDate = this.actualDate;
        }
        if (this.summaryButtonHandlers) {
          this.summaryButtonHandlers.remove();
        }
        if (this.model.calendarViewData.startDate === null || this.model.calendarViewData.endDate === null) {
          this.isCalendarDisplay = false;
        } else if(this.model.calendarViewData.startDate === this.model.calendarViewData.endDate) {
        	if(this.isEmpty(this.model.calendarViewData.availability)){
        		this.isCalendarDisplay = false;
        	}else{
        		this.isCalendarDisplay = true;
        	}

        } else {
          this.isCalendarDisplay = true;
        }

        this.Flight_MoreAirports_Link = this.model.flightOptionsStaticContentViewData.flightContentMap.Flight_MoreAirports_Link;
        this.Flight_MoreDurations_Link = this.model.flightOptionsStaticContentViewData.flightContentMap.Flight_MoreDurations_Link;

        var html = this.renderTemplate("initialize", { value: value });
        domConstruct.place(html, this.domNode, 'only');
        parser.parse(this.domNode);
        this.checkboxHandlers = query("input[type='checkbox']", this.domNode).forEach(lang.hitch(this, function (node) {


        	 on(node, "click", lang.hitch(this, function(){

        		 if(node.id != "DAYTIME_FLIGHTS_ONLY" && node.checked == true && node.id != "DREAM_LINER") {
                	 query("input[type='checkbox']", this.domNode).forEach(function (checkboxNode) {

                		 if (checkboxNode.checked) {
                           	if(checkboxNode.id != "DAYTIME_FLIGHTS_ONLY" && checkboxNode.id != "DREAM_LINER"){
                           		checkboxNode.checked = false;
                           	}
                           }
                	 });
                	 node.checked = true;
                 }

        		 var date = this.actualDate;
        		 if (this.selectedDate) {
        	            date = this.selectedDate;
        	          }
        	          var selectDateStr = this.months[date.getMonth()] +
        	              " " + date.getDate() +
        	              ", " + date.getFullYear() +
        	              " 12:00:00 AM";

        	          this.mediator.fire(this.selectedDuration, this.currentAirport, this.getSelectedFilters(), selectDateStr, null);

        	 }));

        }));
        if (this.isCalendarDisplay) {

          this.getFlightDetailsForSelectedDate(this.selectedDate.getFullYear(),
              this.selectedDate.getMonth(),
              this.selectedDate.getDate(),
              true);

        } else {
          if (this.flightOptionsSummaryPanel !== null) {
            this.flightOptionsSummaryPanel.destroy();
            this.flightOptionsSummaryPanel = null
          }
          var durationNode=query(".change-duration-section", this.domNode);
          if(durationNode != ""){
          domConstruct.empty(query(".change-duration-section", this.domNode)[0]);
          var errorDiv = "<p>No Results found</p>";
          this.errorMessage.push(domConstruct.place(errorDiv, query(".change-duration-section", this.domNode)[0], "last"));
          }
        }

        this.alternateflightsOverlaywidget.open();
      },
      getFlightDetailsForSelectedDate: function (year, month, day, isUSerClick) {
        var summaryPanelModelObj = null;
        if (year !== null) {
          if (isUSerClick) {
            this.selectedDate = new Date(year, month, day);
            dateInFormat = dojo.date.locale.format(this.selectedDate, {
              selector: "date",
              datePattern: "dd-MM-yyyy"
            });
            _.forEach(this.model.calendarViewData.availability, function (item, i) {
              if (item.displayDate === dateInFormat) {
                summaryPanelModelObj = item;
                return;
              }
            });
          } else {
            _.forEach(this.model.calendarViewData.availability, function (item, i) {
            	var monthVal = year.month.toString().length === 1 ? "0" + year.month : year.month;
            	var tempDate = year.day+"-"+monthVal+"-"+year.year;
              if (item.selected) {
                summaryPanelModelObj = item;
                return;
              }else if(tempDate == item.displayDate){
            	  summaryPanelModelObj = item;
                  return;
              }

            });

          }
        }
        if (this.flightOptionsSummaryPanel !== null) {
            this.flightOptionsSummaryPanel.destroy();
            this.flightOptionsSummaryPanel = null
          }
          if (summaryPanelModelObj !== null) {
            this.flightOptionsSummaryPanel = new FlightOptionsSummaryPanel({
              "model": summaryPanelModelObj,
              "flightIndexValue": 0
            });
            domConstruct.place(this.flightOptionsSummaryPanel.domNode, query(".overlay-divider", this.domNode)[0], 'after');
          } else {
            this.flightOptionsSummaryPanel = new FlightOptionsSummaryPanel({
              "errorMsg": "Select a departure date to view flights"
            });

            domConstruct.place(this.flightOptionsSummaryPanel.domNode, query(".overlay-divider", this.domNode)[0], "after");
          }
      },

      updateCustomTab: function (value) {
        // summary:
        //      Updates custom tab with new selected value
        var flightDurationView = this;
        var a = query("a", flightDurationView.customTab)[0];
        domAttr.set(flightDurationView.customTab, "data-dojo-value", value);
        a.innerHTML = value + " " + (parseInt(value, 10) > 1 ? flightDurationView.searchMessaging.nights : flightDurationView.searchMessaging.night);
      },

      removeCustomTab: function () {
        // summary:
        //      Removes custom tab
        this.destroy(this.customTab);
      },

      renderTemplate: function (/*String*/msgType, /*Object?*/data) {
        // summary:
        //      Renders a template
        var renderData = data ? dojo.mixin(this, data) : this;

        return this.renderTmpl(null, renderData);
      },

      testNumber: function (value) {
        // summary:
        //      Tests if given value is a number
        //      Returns: boolean
        return parseInt(value, 10) === Number(value) && parseInt(value, 10) > 0;
      }

    });
  });