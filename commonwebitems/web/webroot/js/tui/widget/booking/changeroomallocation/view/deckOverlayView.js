define("tui/widget/booking/changeroomallocation/view/deckOverlayView", [
   "dojo/_base/declare",
   "dojo/dom",
   "dojo/query",
   "dojo/on",
   "dojo/dom-attr",
   "dojo/dom-construct",
   "dojo/dom-class",
   "dojo/dom-style",
   "dijit/registry",
   "tui/cruise/deck/view/DeckFacilities", 
   "tui/cruise/deck/view/DeckInteractiveSVG", 
   "tui/cruise/deck/view/DeckLegend", 
	"tui/cruise/deck/view/DeckCabinOptions",
	"tui/widget/ScrollHorizontal",
   "tui/widget/_TuiBaseWidget",
   "dojox/dtl/_Templated",
   "tui/widget/mixins/Templatable",
   "dojo/topic",
   "dojo/_base/lang",
   "dojo/text!tui/widget/booking/changeroomallocation/view/Templates/deckOverlayViewTmpl.html",
   "tui/widget/booking/constants/BookflowUrl",
   "dojo/_base/json",
   "tui/widget/booking/bookflowMsgs/nls/Bookflowi18nable"

   ], function( declare,dom,query,on,domAttr,domConstruct,domClass,domStyle, registry,deckFacilities,deckInteractiveSVG,deckLegend,deckCabinOptions, scrollHorizontal, _TuiBaseWidget,dtlTemplate, Templatable,topic,lang,
		   deckOverlayViewTmpl,BookflowUrl,jsonUtil) {

	return declare('tui.widget.booking.changeroomallocation.view.deckOverlayView', [_TuiBaseWidget, dtlTemplate, Templatable,tui.widget.booking.bookflowMsgs.nls.Bookflowi18nable], {

		tmpl: deckOverlayViewTmpl,
		templateString: "",
		widgetsInTemplate : true,
		postMixInProperties: function () {
			
			this.inherited(arguments);
		},

		

		buildRendering: function(){
			this.deckString = "DECK "+this.deckNo;
			this.deckClassName="deck"+this.deckNo;
			this.templateString = this.renderTmpl(this.tmpl, this);
			delete this._templateCache[this.templateString];
			this.inherited(arguments);
		},

		postCreate: function() {
			console.log(this.deckResponseData);
			this.deckResponseData.image = {};
			this.deckResponseData.image.mainSrc = this.deckResponseData.gifImageUrl;
			this.deckResponseData.cabinCategories = this.deckResponseData.cabinFacilitiesViewData;
			var childWidgets = registry.findWidgets(this.domNode);
			console.log(childWidgets);
			
			 _.each(childWidgets, lang.hitch(this, function(childWidget) {
					
				 if(childWidget instanceof deckFacilities){
						
					console.log("true");
					var  deckData = { "facilities": this.deckResponseData.facilities };
					childWidget.updateTemplate({"deckData":deckData, "deckNo": this.deckNo, "flag": false});
		        	
						
					}
				 
				 if(childWidget instanceof scrollHorizontal){
					 var deckWidget = registry.findWidgets(childWidget.domNode)[0];
					 
					 if(deckWidget instanceof deckInteractiveSVG){
						 
							var svgUrl = this.deckResponseData.svgImageUrl;
							 this.svgImageFetch(svgUrl, deckWidget, this.deckResponseData);
						 }
					 
				
					 
				 }

				   
				  if(childWidget instanceof deckLegend){
					       	
					 childWidget.updateTemplate({"deckData":this.deckResponseData.cabinFacilitiesViewData, "flag": true});
			       
					 
						
					 }
				   if(childWidget instanceof deckCabinOptions){
					 
					 childWidget.updateTemplate({"deckData":this.deckResponseData, "bindEvent":true, "deckNo": this.deckNo});
					 }
			 }));
			
		},
		
		svgImageFetch:function(svgUrl, childWidget, deckResponse){
			
			
			 var deckSVGController = this, xhrReq;
	        	
	        	var invocation = new XMLHttpRequest();
	            if(invocation) {
	               invocation.open('GET', svgUrl , true);
	               invocation.onreadystatechange = function(){
	                   if (invocation.readyState==4 && invocation.status==200){
	                	   
	                	   childWidget.updateTemplate(deckResponse, this.deckNo, false, invocation.responseText,deckResponse, deckResponse.cabinFacilitiesViewData,[],deckResponse.cabinTypeMap);
	                   }
	               };
	               invocation.send();
	            }
			
		}

		

		

	});
});