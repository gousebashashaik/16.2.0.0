define("tui/widget/amendncancel/summary/PaymentDuePanel", [
    "dojo/_base/declare",
    "dojo/query",
    'dojo/dom',
    "dojo/dom-class",
    "dojo/text!tui/widget/amendncancel/summary/templates/PaymentDuePanel.html",
    "dojo/dom-construct",
    "dojo/_base/xhr",
    "dojo/dom-attr",
    "dojo/parser",
    "dojo/dom-style",
    "tui/widget/booking/constants/TotalPrice",
    "tui/widget/mixins/Templatable",
	"tui/widget/amendncancel/tooltipInline"

], function(declare, query, dom, domClass, PaymentDuePanel, domConstruct, xhr, domAttr, parser, domStyle, TotalPrice, Templatable) {
    return declare("tui.widget.amendncancel.summary.PaymentDuePanel", [tui.widget._TuiBaseWidget, Templatable], {
        tmpl: PaymentDuePanel,
        /*jsonData:null,
		bookingData:null,
		staticData:null,
		
        postMixInProperties: function() {
            var amtString = this.jsonData.packageViewData.bookingDetails.amountDue.amount.toString();
            if (amtString.indexOf(".") != -1) {
                var amountString = amtString.split(".");
                this.jsonData.packageViewData.bookingDetails.amountDue.amountNumber = Math.abs(parseInt(amountString[0], 10));
                this.jsonData.packageViewData.bookingDetails.amountDue.amountDecimal = parseInt(amountString[1], 10);
            } else {
                this.jsonData.packageViewData.bookingDetails.amountDue.amountNumber = Math.abs(this.jsonData.packageViewData.bookingDetails.amountDue.amount);
            }
            this.bookingData = this.jsonData.packageViewData.bookingDetails;

            this.staticData = this.jsonData.manageBookingSummaryContentViewData.manageBookingSummaryContentMap;
        },*/
        postCreate: function() {
            this.renderWidget();
            this.inherited(arguments);
            domAttr.set(dom.byId('newpricedue'), "innerHTML", TotalPrice.updatetotalprice(this.jsonData.packageViewData.bookingDetails.currencyAppendedAmountDue,'poundSize','number','decimal'));
        },

        renderWidget: function() {
            var widget = this;
            var html = widget.renderTmpl(widget.tmpl, widget);
            domConstruct.place(html, this.domNode, "only");
            parser.parse(this.domNode);
        }

    });
});