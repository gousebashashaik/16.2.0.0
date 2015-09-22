define("tui/widget/booking/passengers/view/PassengerChildValidationAlertView", [
  "dojo/_base/declare",
  "dojo/query",
  "dojo/dom-attr",
  "dojo/dom-construct",
  "dojo/dom-class",
  "dojo/Evented",
  "dojo/topic",
  "dojo/_base/lang",
  "dojo/dom-style",
  "tui/widget/_TuiBaseWidget",
  "dojox/dtl/_Templated",
  "tui/widget/mixins/Templatable",
  "dojo/text!tui/widget/booking/passengers/view/templates/PassengerChildValidationAlertTmpl.html",
  "tui/widget/booking/constants/BookflowUrl",
  "dojo/on",
  "dojo/_base/json",
  "dojo/dom",
  "tui/widget/form/SelectOption"


], function (declare, query, domAttr, domConstruct, domClass, Evented, topic, lang, domStyle, _TuiBaseWidget,
             dtlTemplate, Templatable, PassengerChildValidationAlertTmpl,BookflowUrl, on, jsonUtil,dom) {

  return declare('tui.widget.booking.passengers.view.PassengerChildValidationAlertView',
      [_TuiBaseWidget, dtlTemplate, Templatable, Evented], {

    tmpl: PassengerChildValidationAlertTmpl,


    buildRendering: function () {
      this.templateString = this.renderTmpl(this.tmpl, this);
	  delete this._templateCache[this.templateString];
	  this.inherited(arguments);
    },

    postCreate: function () {
       	var a= query(".button",this.domNode);

       	for (var i =0; i<=this.jsonData.summaryViewData.summaryPanelComponentViewData.length; i++){

       		if(( this.jsonData.summaryViewData.summaryPanelComponentViewData[i].name == "HOTEL" || this.jsonData.summaryViewData.summaryPanelComponentViewData[i].name == "ITINERARY & SHIP" ))
       			{
       			this.accomUrl = this.jsonData.summaryViewData.summaryPanelComponentViewData[i].summaryPanelUrlsViewData.url;
       			break;
       			}

       	}

    	if(this.jsonData.valStatusFlag == "2" ){

    		var textMessage = query(".font12", this.domNode);
    		domAttr.set(a[0],"href",this.accomUrl);
        domAttr.set(textMessage[0], "innerHTML", "Sorry, now that we've updated the passenger's age" +
            " this holiday is no longer available. " +
            "Please correct the passenger's age in the search criteria to find alternative holiday.");

    	}
    	else if(this.jsonData.valStatusFlag == "1"){

    		var textMessage = query(".font12", this.domNode);
    		domAttr.set(a[0],"href",this.accomUrl);
        domAttr.set(textMessage[0], "innerHTML", "Sorry, now that we've updated the passenger's age" +
            " this holiday extras are no longer valid. " +
            "Please select available extras for the passenger's age.");

    	}
       this.inherited(arguments);
    }

  });
});