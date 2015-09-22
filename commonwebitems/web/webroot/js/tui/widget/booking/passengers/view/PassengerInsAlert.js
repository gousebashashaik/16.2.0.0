define("tui/widget/booking/passengers/view/PassengerInsAlert", [
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
  "dojo/text!tui/widget/booking/passengers/view/templates/PassengerInsAlert.html",
  "tui/widget/booking/constants/BookflowUrl",
  "dojo/on",
  "dojo/_base/json",
  "tui/widget/form/SelectOption"


], function (declare, query, domAttr, domConstruct, domClass, Evented, topic, lang, domStyle, _TuiBaseWidget,
             dtlTemplate, Templatable, PassengerInsAlert,BookflowUrl, on, jsonUtil) {

  return declare('tui.widget.booking.passengers.view.PassengerInsAlert',
      [_TuiBaseWidget, dtlTemplate, Templatable, Evented], {

    tmpl: PassengerInsAlert,

    buildRendering: function () {
    	this.templateString = this.renderTmpl(this.tmpl, this);
		  delete this._templateCache[this.templateString];
		  this.inherited(arguments);
    },

    postCreate: function () {
      this.inherited(arguments);
  	var a= query(".button",this.domNode)[0];
   	
   	
   	for (var i =0; i<=this.jsonData.summaryViewData.summaryPanelComponentViewData.length; i++){
   		 
   		if(this.jsonData.summaryViewData.summaryPanelComponentViewData[i].name == "EXTRAS")
   			{
   			this.extrasURL = this.jsonData.summaryViewData.summaryPanelComponentViewData[i].summaryPanelUrlsViewData.url;
   			break;
   			}
   		
   		}
   	domAttr.set(a,"href",this.extrasURL);
   	
    }

  });
});