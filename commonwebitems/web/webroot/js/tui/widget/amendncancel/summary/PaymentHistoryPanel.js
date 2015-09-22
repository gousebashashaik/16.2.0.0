define("tui/widget/amendncancel/summary/PaymentHistoryPanel", [
  "dojo/_base/declare",
  "dojo/query",
  "dojo/dom-class",
  "dojo/text!tui/widget/amendncancel/summary/templates/PaymentHistoryPanel.html",
  "dojo/dom-construct",
  "dojo/parser",
  "tui/widget/mixins/Templatable"
], function (declare, query, domClass, PaymentHistoryPanel, domConstruct, parser, Templatable) {
  return declare("tui.widget.amendncancel.summary.PaymentHistoryPanel", [tui.widget._TuiBaseWidget,Templatable], {
    tmpl: PaymentHistoryPanel,
    jsonData:null,
	bookingData:null,
	staticData:null,
	
    postMixInProperties: function(){
	  
	  this.bookingData = this.jsonData.packageViewData.bookingDetails;	  
	  this.staticData = this.jsonData.manageBookingSummaryContentViewData.manageBookingSummaryContentMap;     	  
    },
    postCreate: function () {      
      this.renderWidget();
      this.inherited(arguments);
    },
    
    renderWidget: function () {
    	var widget = this;
		
    	var html = widget.renderTmpl(widget.tmpl, widget);
      domConstruct.place(html, this.domNode, "only");
      parser.parse(this.domNode);
    }

  });
});