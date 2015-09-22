define("tui/widget/booking/yourseats/view/seatChangeButtonToggler", ["dojo/_base/declare",
  "dijit/_WidgetBase",
  "tui/widget/mixins/Templatable",
  "dojox/dtl/_Templated",
  "dojo/on",
  "dojo/query",
  "dojo/dom-class",
  "dojo/dom-style",
  "dojo/_base/lang",
  "dojo/dom",
  "dojo/dom-construct",
  "dojo/text!tui/widget/booking/yourseats/view/Templates/SeatOptionTmpl.html",
  "tui/widget/booking/yourseats/view/TermsAndConditionsView",
  "tui/widget/booking/yourseats/view/TermsAndConditionsOverlay",
  "tui/widget/booking/constants/BookflowUrl",
  "dojo/topic",
  "dojo/parser",
  "dojo/html",
  "dojo/dom-attr",
  "tui/widget/booking/constants/TotalPrice",
  "dojo/dom-attr",
  "dojox/dtl",
  "dojox/dtl/Context",
  "tui/widget/_TuiBaseWidget",
  "tui/widget/booking/Expandable"
  ],
  function (declare, _WidgetBase, Templatable, dtlTemplated,  on, query, domClass, domStyle, lang, dom, domConstruct, SeatOptionTmpl,TermsAndConditionsView,TermsAndConditionsOverlay,BookflowUrl, topic, parser, html, domattr,TotalPrice,domAttr) {
    return declare("tui.widget.booking.yourseats.view.seatChangeButtonToggler", [tui.widget._TuiBaseWidget,  _WidgetBase, dtlTemplated, Templatable], {

      tmpl: SeatOptionTmpl,
      templateString: "",
      thirdPartySeatContent:"",
	  thirdPartyFlightSelectionFlag:"",
	  thirdPartyFlightName:'',
	  get3PFCarrierFlag:'',
	  currency:'',


      postMixInProperties: function () {
    	  this.thirdPartySeatContentFun();
       },

      buildRendering: function () {
        this.currency=dojoConfig.currency;
		this.find3PFCarrier();
    	this.build3PFMap(this.jsonData.thirdPartyInfoMap);
        this.templateString = this.renderTmpl(this.tmpl, this);
        delete this._templateCache[this.templateString];
        this.inherited(arguments);

      },

      build3PFMap : function (thirdPartyInfoMap){
    	  var getCarrierCode = "",
    	  widget = this;

    	  if(widget.jsonData.packageViewData.flightViewData.length){
    		  _.each(widget.jsonData.packageViewData.flightViewData, function(outboundCarrier){
    			  getCarrierCode = outboundCarrier.outboundSectors[0].carrierCode;
    			  if(thirdPartyInfoMap != null) {
	    			  if(thirdPartyInfoMap[getCarrierCode]){
	    			  	widget.thirdPartyInfo = widget.get3PFDetailsMap(getCarrierCode,thirdPartyInfoMap[getCarrierCode]);
	    			  }
    			  }
    		});
    	  }
      },

      get3PFDetailsMap : function(carrierCode, thirdPartyFlight){
    	  var key = "DESK_"+this.jsonData.packageViewData.multicomThirdPartyFlightCarrierCode+"_Seats_Description";
    	  var title = this.jsonData.packageViewData.multicomThirdPartyFlightCarrierCode+"_Flight_YourSeats_CompHeader";
    	  return {
				"uspKeys": this.jsonData.flightOptionsStaticContentViewData.flightContentMap[key],
				"thirdPartyTitle": jsonData.flightOptionsStaticContentViewData.flightContentMap[title]
    	  }
      },

	  find3PFCarrier: function () {
    	  var widget = this;
    	  widget.getinboundThirdParty = widget.jsonData.packageViewData.flightViewData[0].inboundThirdParty;
    	  widget.getoutBoundThirdParty = widget.jsonData.packageViewData.flightViewData[0].outBoundThirdParty;
		  widget.thirdPartyFlightSelectionFlag=false;

		  if (widget.jsonData.packageViewData.multicomThirdPartyFlight) {
	    	  if(( widget.getinboundThirdParty==true || widget.getoutBoundThirdParty==true )){
			 		widget.thirdPartyFlightSelectionFlag = false;
			 		widget.get3PFCarrierFlag = "mixed_carrier";
			  }
	    	  if( widget.getinboundThirdParty==true && widget.getoutBoundThirdParty==true){
			 		 widget.thirdPartyFlightSelectionFlag = true;
			 		 widget.get3PFCarrierFlag = "single_carrier";
	    	  }
		  }

      },

      postCreate: function () {
        var widget = this;
        var widgetDom = widget.domNode;
        parser.parse(widget.domNode);
        widget.inherited(arguments);



        var seatingBtn = query('.seatingBtn', widgetDom);
        for(var index = 0; index < seatingBtn.length ; index++){
        	widget.tagElement(seatingBtn[index],seatingBtn[index].id);
        }


        widget.controller = dijit.registry.byId("controllerWidget");

        var testvar = widget.controller.registerView(widget);
        widget.forclickevent(widgetDom);
        //widget.forTransitionEffect();
        topic.subscribe("tui/widget/booking/displayContent", function () {
          widget.displayContent();
        });
        widget.displayContent();
      },


      handleTermsAndConditionsButton: function (data) {

          if (this.termsAndConditionsView && this.termsAndConditionsView !== null) {
            this.termsAndConditionsView.destroyRecursive();
            this.termsAndConditionsView = null;
            this.termsAndConditionsOverlay.destroyRecursive();
            this.termsAndConditionsOverlay = null;
          }
          this.termsAndConditionsView = new TermsAndConditionsView({

            "termsAndConditionsData": data,
            "id": "terms",
            "jsonData":this.jsonData,
            "overlayRef": null
          });
          domConstruct.place(this.termsAndConditionsView.domNode, this.domNode, "last");
          this.termsAndConditionsOverlay = new TermsAndConditionsOverlay({"widgetId": this.termsAndConditionsView.id, modal: true});
          this.termsAndConditionsView.refWidget = this.termsAndConditionsOverlay;
          this.termsAndConditionsOverlay.open();
        },
      generateRequest: function (itemId) {
        var field = "seats";
        var widget = this;
        var request = {seatExtrasCode: itemId};
        var url = BookflowUrl.seatchangebuttontogglerurl;
        widget.controller.generateRequest(field, url, request);
        return request;
      },

      refresh: function (field, response) {
        var widget = this;
        var widgetDom = widget.domNode;
        widget.jsonData = response;
        widget.thirdPartySeatContentFun();
		widget.find3PFCarrier();
        widget.build3PFMap(widget.jsonData.thirdPartyInfoMap);
        var html = widget.renderTmpl(widget.tmpl, widget);
        domConstruct.place(html, widget.domNode, "only");
        parser.parse(widget.domNode);
        //widget.forTransitionEffect();
        widget.forclickevent(widgetDom);

      },

      forclickevent: function (widgetDom) {
        var widget = this;
        var items = query(".item", widgetDom);
        var seatButtonAttachPoints = query("button[data-dojo-attach-point$='seatButton']", widgetDom);
        _.each(seatButtonAttachPoints, function (seatButton) {
          on(seatButton, "click", lang.hitch(widget, widget.handleSeatButtonClick, seatButton));
        });
        widget.displayContent();

        var selectSeat = query("a[data-dojo-attach-point$='termsandconditionsSelectYourSeats']", widgetDom)[0];
        if (selectSeat) {
        	var selectYourSeatToolTip = query("a[data-dojo-attach-point$='termsandconditionsSelectYourSeats']", widgetDom)[0];
            on(selectYourSeatToolTip, "click", lang.hitch(widget, widget.handleTermsAndConditionsButton,widget.jsonData.flightOptionsStaticContentViewData.flightContentMap.SelectYourSeat_ToolTip));
          }
        var extraSpaceSlect = query("a[data-dojo-attach-point$='termsandconditionsExtraSpace']", widgetDom)[0];
        if (extraSpaceSlect) {
        	var extraSpaceToolTip = query("a[data-dojo-attach-point$='termsandconditionsExtraSpace']", widgetDom)[0];
            on(extraSpaceToolTip, "click", lang.hitch(widget, widget.handleTermsAndConditionsButton,widget.jsonData.flightOptionsStaticContentViewData.flightContentMap.SeatsWithExtraSpace_ToolTip));
          }


      },
      handleSeatButtonClick: function(seatButton){
    	  var widget = this;
    	  var itemId = domattr.get(seatButton, "name");
              widget.generateRequest(itemId);
      },

      thirdPartySeatContentFun: function () {
    	  var key = this.jsonData.packageViewData.multicomThirdPartyFlightCarrierCode+"_Seats_Description";
    	  this.thirdPartySeatContent = this.jsonData.flightOptionsStaticContentViewData.flightContentMap[key];
		  //var yourSeats = this.jsonData.flightOptionsStaticContentViewData.flightContentMap.Flight_YourSeats_CompHeader;
		  var flightCode = this.jsonData.packageViewData.multicomThirdPartyFlightCarrierCode;
		  this.thirdPartyFlightName = this.jsonData.packageViewData.summaryPanelStaticContentViewData.summaryContentMap[flightCode+"_Description"];
        },


      displayContent: function () {

        var widget = this;
        var widgetDom = widget.domNode;
        var items = query(".open", widgetDom);
        var allItems = query(".item", widgetDom);
        _.each(allItems, function (item) {
          var header = query(".item-toggle", item)[0];


          var thresholdAvailability = query(".limitedAvailability", item)[0];

          if (thresholdAvailability != null) {
            domClass.remove(thresholdAvailability, "displayNone");
          }

        });
        _.each(items, function (item) {
          var header = query(".item-toggle", item)[0];

          var thresholdAvailability = query(".limitedAvailability", item)[0];

          if (thresholdAvailability != null) {
            domClass.add(thresholdAvailability, "displayNone");
          }
        });
      }
    });

  });