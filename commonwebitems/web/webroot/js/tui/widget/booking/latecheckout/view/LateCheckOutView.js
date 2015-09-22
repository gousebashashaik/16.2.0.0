define("tui/widget/booking/latecheckout/view/LateCheckOutView", [
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
  "dojo/text!tui/widget/booking/latecheckout/view/templates/LateCheckOutView.html",
  "tui/widget/booking/constants/BookflowUrl",
  "dojo/on",
  "dojo/_base/json",
  "tui/widget/form/SelectOption"
], function (declare, query, domAttr, domConstruct, domClass, Evented, topic, lang, domStyle, _TuiBaseWidget, dtlTemplate, Templatable, LateCheckOutView,BookflowUrl, on, jsonUtil) {

  return declare('tui.widget.booking.latecheckout.view.LateCheckOutView', [_TuiBaseWidget, dtlTemplate, Templatable, Evented], {

    tmpl: LateCheckOutView,
    templateString: "",
    widgetsInTemplate: true,
    quantityArrayLCD: [],
    currency:"",

    postMixInProperties: function () {
    	this.currency=dojoConfig.currency;
      this.quantityArrayLCD = [];
      for (var i = 0; i < this.lcdData.length; i++) {
        for (var k = 0, j = 1; j <= this.lcdData[i].quantity; j++, k++) {
          this.quantityArrayLCD[k] = j;
        }
      }
      this.inherited(arguments);
    },

    buildRendering: function () {
      this.templateString = this.renderTmpl(this.tmpl, this);
      delete this._templateCache[this.templateString];
      this.inherited(arguments);
    },

    postCreate: function () {
    	if(this.lcdData[0].selectedQuantity == 0){
      	  this["SelectionBox"].setSelectedValue(this.lcdData[0].quantity);
      	}
      this.controller = dijit.registry.byId("controllerWidget");
      this.attachEvents();
      this.handleSelectionBox();
      this.inherited(arguments);
      this.tagElements(dojo.query('.okbutton',this.domNode),"lateCheckoutRoomSelect");
      this.tagElements(dojo.query('.cancelbutton',this.domNode),"cancelLateCheckoutRooms");
      this.tagElements(dojo.query('.custom-dropdown',this.domNode),"lateCheckoutRoomOption");
    },

    attachEvents: function () {
      on(this.lcdOkButton, "click", lang.hitch(this, this.handleAddButton));
      on(this["SelectionBox"], "change", lang.hitch(this, this.handleSelectionBox));
    },

    handleSelectionBox: function () {
      this.totalPrice = 0;
      var noOfRooms = this["SelectionBox"].getSelectedData().value;
      var perroomprice = this.lcdData[0].currencyAppendedPerPersonPrice;
      var prices = perroomprice.slice(1);

      var priceNumber = parseInt(prices);
      var roomInNumber = parseInt(noOfRooms);

      var priceUpdate = priceNumber * roomInNumber;
      this.totalPrice = priceUpdate;
      domAttr.set(this.totalCalculatedPrice, "innerHTML", this.currency + this.totalPrice.toFixed(2));
    },

    handleAddButton: function () {

      var key = "";
      var quantity = "";
      _.each(this._attachPoints, lang.hitch(this, function (attachPoint) {
        var selectionAttachPoint = this[attachPoint];
        if (selectionAttachPoint instanceof tui.widget.form.SelectOption) {
          key = selectionAttachPoint.id;
          quantity = selectionAttachPoint.getSelectedData().value;
        }

      }));
      var url = BookflowUrl.latecheckouturl;
      var requestData={extraCategory:this.extraCategory,extraCode:key,quantity:quantity};
      this.controller.generateRequest("latecheckout", url, requestData);

    }
  });
});