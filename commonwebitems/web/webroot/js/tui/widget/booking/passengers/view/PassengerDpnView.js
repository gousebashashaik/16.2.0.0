define("tui/widget/booking/passengers/view/PassengerDpnView", [
  "dojo/_base/declare",
  "dojo/query",
  "dojo/dom-attr",
  "dojo/dom-construct",
  "dojo/dom-class",
  "dojo/Evented",
  "dojo/topic",
  "dojo/_base/lang",
  "dojo/dom-style",
  "tui/widget/booking/constants/BookflowUrl",
  "tui/widget/_TuiBaseWidget",
  "dojox/dtl/_Templated",
  "tui/widget/mixins/Templatable",
  "dojo/text!tui/widget/booking/passengers/view/templates/PassengerDpnView.html",
  "tui/widget/booking/passengers/view/PassengerDpnOverlay",
  "dojo/on",
  "dojo/_base/json",
  "tui/widget/form/SelectOption"


], function (declare, query, domAttr, domConstruct, domClass, Evented, topic, lang, domStyle, BookflowUrl,_TuiBaseWidget, dtlTemplate, Templatable, PassengerDpnView, PassengerDpnOverlay,on, jsonUtil) {

  return declare('tui.widget.booking.passengers.view.PassengerDpnView', [_TuiBaseWidget, dtlTemplate, Templatable, Evented], {

      tmpl: PassengerDpnView,
      templateString: "",
	  widgetsInTemplate : true,
	  templateView:false,

    buildRendering: function () {
    	this.templateString = this.renderTmpl(this.tmpl, this);
		delete this._templateCache[this.templateString];
		this.inherited(arguments);
    },

    postCreate: function () {
      this.controller = dijit.registry.byId("controllerWidget");
      this.controller.registerView(this);
		
      this.inherited(arguments);
      this.attachEvents();
    },

    attachEvents: function () {
    	var widget = this;
    	var widgetDom = widget.domNode;
    	var okButton =  query('.okbutton',widgetDom)[0];
    	 on(okButton, 'click', function(e) {
    		 var responseObj = {};
    	        var requestData;
    	        var url=BookflowUrl.preferences;
    	        var checkBox=query('.checkBoxRef',widgetDom);
    	        for(i=0;i<checkBox.length;i++){
      	          var communicateBy = checkBox[i].name;
      	          var value = checkBox[i].checked; 
      	          responseObj[communicateBy] = value;
    	              console.log(responseObj);
    	              }  
    	        requestData = {"preferences": jsonUtil.toJson(responseObj)};
    	        widget.controller.generateRequest("preferences", url, requestData);  		 
    		 topic.publish("some/PassengerDpnOverlay/closeOverlay");   	 });
    },
    
    refresh : function(field,response){
    	this.passengerDpnOverlay.refWidget.jsonData = response;   	
    }
  });
});