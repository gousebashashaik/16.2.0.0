define("tui/bookflow/Sticky", [
  "dojo",
  "dojo/query",
  'dojo/dom',
  "dojo/text!tui/widget/Templates/PriceBreakdownSummaryPanelComponent.html",
  "dojo/dom-construct",
  "tui/widget/booking/constants/TotalPrice",
  "dojo/dom-attr",
  "tui/widget/_TuiBaseWidget",
  "tui/widgetFx/Transitiable",
  "tui/widgetFx/WipeTransitions"

  ], function(dojo,query,dom,PriceBreakdownSummaryPanelComponent,domConstruct,TotalPrice,domAttr) {

  dojo.declare("tui.bookflow.Sticky", [tui.widget._TuiBaseWidget], {

	  tmpl: PriceBreakdownSummaryPanelComponent,
	  displayDiscount: true,
    postCreate: function() {
      var sticky = this;

	  handler = function(){
      var stickyDomPos = sticky.domNode.offsetTop,
          stickOffPos = (dojo.query(".priceBrk")[0].offsetTop) + 350,
          footerOffPos=(dojo.query("#inner-footer")[0].offsetTop)-200,
          pagePos = window.pageYOffset ? window.pageYOffset : document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop;
      if (pagePos > stickOffPos) {
        dojo.addClass(sticky.domNode, "stick");
       if (pagePos >= footerOffPos) {
        	 dojo.removeClass(sticky.domNode, "stick");
          }
      }
      else if (pagePos <= stickOffPos) {
        dojo.removeClass(sticky.domNode, "stick");
      }
    }
	if (document.addEventListener) {

		document.addEventListener("touchmove", handler, true);
		}
		else {
		document.attachEvent("touchmove", handler, true);
		}

	  window.onscroll =  handler;




	 	var widgetDom = sticky.domNode;
	 	console.log(sticky.jsonData);


	 	var controller=null;
	 	console.log(dijit.registry.byId("controllerWidget"));
	 	sticky.controller= dijit.registry.byId("controllerWidget");
	 	var testVar=  sticky.controller.registerView(sticky)
	 	console.log(sticky.controller);


	 	var template = new dojox.dtl.Template(sticky.tmpl);
	      var context = new dojox.dtl.Context(sticky);
	      var html = template.render(context);

	      domConstruct.place(html, sticky.domNode, 'only');
	      dojo.parser.parse(sticky.domNode);
          sticky.inherited(arguments);
          if(dom.byId('priceBreakDown')){
        	  domAttr.set(dom.byId('priceBreakDown'), "innerHTML", TotalPrice.updatetotalprice(this.jsonData.packageViewData.currencyAppendedRoundUpTotalCost,  'priceBreakPoundSize' , 'priceBreakNumber', 'priceBreakDecimal'));
          }


	   },
    refresh : function(field,response)
	{
	 var widget= this;

		console.log(field);
		console.log(response);
		widget.jsonData = response;
		console.log(response);
		console.log(widget.jsonData );
		console.log(widget);


		  var template = new dojox.dtl.Template(widget.tmpl);
	      var context = new dojox.dtl.Context(widget);
	      var html = template.render(context);

	      domConstruct.place(html, widget.domNode, 'only');
	      dojo.parser.parse(widget.domNode);
	      if(dom.byId('priceBreakDown')){
	    	  domAttr.set(dom.byId('priceBreakDown'), "innerHTML", TotalPrice.updatetotalprice(this.jsonData.packageViewData.currencyAppendedRoundUpTotalCost,  'priceBreakPoundSize' , 'priceBreakNumber', 'priceBreakDecimal'));
	      }


	}

  });
  return tui.bookflow.Sticky;

});