define('tui/widget/amendncancel/retrieveBookingPage/splitter/Splitter', [
  "dojo/_base/declare",
  "dojo/dom",
  "dojo/dom-construct",
  "dojo/parser",
  "tui/widget/_TuiBaseWidget",
  "dojox/dtl/_Templated",
  "tui/widget/mixins/Templatable",
  "dojo/text!tui/widget/amendncancel/retrieveBookingPage/splitter/templates/IscapeSplitter.html",
  "dojo/text!tui/widget/amendncancel/retrieveBookingPage/splitter/templates/HybrisSplitter.html",
  "tui/widget/form/ValidationTextBox",
  "tui/widget/amendncancel/retrieveBookingPage/twoFieldValidate"], function(declare, dom, domConstruct, parser, _TuiBaseWidget, _Templated,
		Templatable, iscapeTemplate, hybrisTemplate) {

	return declare('tui.widget.amendncancel.retrieveBookingPage.splitter.Splitter', [_TuiBaseWidget, _Templated, Templatable], {

	   //Templated Mixin attribute
	   templateString: hybrisTemplate,

	   widgetAttachPoints: [],

	   defaultValue: null,

	   postCreate:function(){
			this.inherited(arguments);
			this.renderWidget();
	   },

	   splitBookingReference:function(channel){
		   var widget = this;
	       if(channel == "hybris" && widget.templateString != hybrisTemplate){
				widget.templateString = hybrisTemplate;
				widget.renderWidget();
		   }else if(channel == "iscape" && widget.templateString != iscapeTemplate){
				widget.templateString = iscapeTemplate;
				widget.renderWidget();
		   }
	   },

	   renderWidget: function () {
    	var widget = this;
    	var html = widget.renderTmpl(widget.templateString, widget);
		domConstruct.place(html, this.domNode, "only");
		widget.widgetAttachPoints = parser.parse(this.domNode);
		dojo.publish("tui.widget.amendncancel.retrieveBookingPage.RetrieveBookingForm.addOnFocusEvent");
		dojo.publish("tui.widget.amendncancel.retrieveBookingPage.RetrieveBookingForm.addOnKeyPressEvent");
	  }
	});
	return tui.widget.amendncancel.retrieveBookingPage.splitter.Splitter;
});