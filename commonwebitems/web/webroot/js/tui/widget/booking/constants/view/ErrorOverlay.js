define("tui/widget/booking/constants/view/ErrorOverlay", [
  "dojo/_base/declare",
  "tui/widget/popup/DynamicPopup",
  "dojo/dom",
  "dojo/topic",
  "dojo/dom-class",
  "dojo/query"
], function (declare, dynamicPopup,dom,topic,domclass,query) {

  return declare("tui.widget.booking.constants.view.ErrorOverlay", [dynamicPopup], {

		    stopDefaultOnCancel: false,
		    refWidget:null,
		      open: function () {
		     // window.scrollTo(0, 0);
		      this.inherited(arguments);
			  var priceBreakDown = dom.byId("priceBrk");
		      var priceBreakdownSticky = query(".noAccrodDef",priceBreakDown)[0];
		      domclass.remove(priceBreakdownSticky,"stick");
		    },

		    close: function () {

		      this.inherited(arguments);
		      try{
		    	  var priceBreakDown = dom.byId("priceBrk");
		    	  var priceBreakdownSticky = query(".noAccrodDef",priceBreakDown)[0];
		    	  domclass.remove(priceBreakdownSticky,"stick");
		      }catch(e){ }

		    }

  		});
});