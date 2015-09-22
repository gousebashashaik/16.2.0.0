define("tui/widget/booking/transferoptions/view/FlightsOnlyCoachTransfersView", [
  "dojo/_base/declare",
  "dijit/_WidgetBase",
  "tui/widget/mixins/Templatable",
  "dojox/dtl/_Templated",
  "dojo/parser",
  "dojo/on",
  "dojo/query",
  "dojo/dom-attr",
  "dojo/dom-class",
  "dojo/dom-style",
  "dojo/_base/lang",
  "dojo/dom",
  "dijit/registry",
  "dojo/dom-construct",
  "dojo/text!tui/widget/booking/transferoptions/view/Templates/FlightsOnlyCoachTransfers.html",
  "tui/widget/booking/constants/BookflowUrl",
  "dojo/topic",
  "dojo/html",
  "dojox/dtl",
  "dojox/dtl/Context",
  "tui/widget/_TuiBaseWidget"
], function (declare, _WidgetBase, Templatable, dtlTemplated, parser, on, query,domAttr, domClass, domStyle, lang, dom, registry,domConstruct, FlightsOnlyCoachTransfers,BookflowUrl, topic, html) {

  return declare("tui.widget.booking.transferoptions.view.FlightsOnlyCoachTransfersView", [tui.widget._TuiBaseWidget, _WidgetBase, dtlTemplated, Templatable], {

    tmpl: FlightsOnlyCoachTransfers,
    perSeatPrice :"",
    totalPrice : " ",
    transferCode:"",
    itemId : " ",
    selectTrue:false,
	transferFlag:false,
	flightIndexVal:0,
	postMixInProperties:function(){
	var widget = this;
	 var widgetDom = widget.domNode;
	if(!_.isEmpty(widget.jsonData.extraFacilityViewDataContainer.transferOptions.extraFacilityViewData)){
      var transferData =widget.jsonData.extraFacilityViewDataContainer.transferOptions.extraFacilityViewData;
      for(i=0;i<transferData.length;i++){
    	  if(transferData[i].selected == true){
    		  widget.transferDescription = transferData[i].description;
    		  widget.transferFlag = true;
    		  break;
    	  }
    	  else{
    		  widget.transferFlag = false;
    	  }
      }
    }

	this.inboundFlag = true;
	this.controller = dijit.registry.byId("controllerWidget");
    this.flightViewData = this.controller.refData(this.jsonData.packageViewData.flightViewData, this.flightIndexVal);
    if (_.isEmpty(this.flightViewData.inboundSectors)){
    	this.inboundFlag = false;
    }

	},

    buildRendering: function () {
      this.templateString = this.renderTmpl(this.tmpl, this);
      delete this._templateCache[this.templateString];
      this.inherited(arguments);
    },

    postCreate: function () {
      var widget = this;
      var widgetDom = widget.domNode;
      widget.inherited(arguments);
      var selectButton = query('.withoutPrice',widget.domNode);
      widget.controller = dijit.registry.byId("controllerWidget");
      var testvar = widget.controller.registerView(widget);
      parser.parse(widget.domNode);
	   if(widget.removeDisplay){
        	  var displayButton = query(".button-cont", widget.domNode);
              domClass.remove(displayButton[0],"disNone");
          }
      widget.handleDropDown();
    },


    handleDropDown : function(){
    	var widget = this;

    	  var childWidgets = registry.findWidgets(this.domNode);
    	  var displayButton = query(".button-cont", widget.domNode);
        _.each(childWidgets, lang.hitch(this, function(childWidget) {
      	  if(childWidget instanceof tui.widget.form.SelectOption){
  			  on(childWidget, "change", function () {
      			 var key = childWidget.getSelectedData().value;
      			  this.selectTrue = true;
                  var transferArray = widget.jsonData.extraFacilityViewDataContainer.transferOptions.extraFacilityViewData;
                   for(var i=0;i<transferArray.length;i++){
                  	 if(key==transferArray[i].description){
                  		 widget.perSeatPrice =transferArray[i].currencyAppendedPerPersonPrice;
                  		 widget.totalPrice = transferArray[i].currencyAppendedPrice;
                  		 widget.itemId = transferArray[i].code;
                  		 domClass.remove(displayButton[0],"disNone");
                  		 widget.withPriceDiv = query(".withPrice",widget.domNode);
                  		 widget.perSeatPriceDiv = query(".priceDisplay",widget.domNode);
                 		domAttr.set(widget.withPriceDiv[0], "innerHTML",widget.totalPrice);
                 		domAttr.set(widget.perSeatPriceDiv[0], "innerHTML", widget.perSeatPrice + " per seat");

                  		 break;
                  	 }else{
                  		 domClass.add(displayButton[0],"disNone");
                  	 }

                   }
                   });


       }


        }));
        widget.extraSelected = true;
        if(this.selectDisplay){
        on(this.selectDisplay,"click",lang.hitch(widget,widget.handleResortSelection,widget.itemId,widget.selectDisplay,widget.extraSelected));
        }
        if(this.removeDisplay){
        on(this.removeDisplay,"click",lang.hitch(widget,widget.handleResortSelection,widget.itemId,widget.removeDisplay,false));
        }
    },

    handleResortSelection : function(itemId,selectDisplay,extraSelected){
    	var widget = this;
    	 var extraCategory = widget.jsonData.extraFacilityViewDataContainer.transferOptions.extraFacilityCategoryCode;
   	  widget.generateRequest(widget.itemId,extraCategory,extraSelected);
      var dropDownDis = query(".afterSelecting", widget.domNode);
   	   domClass.add(dropDownDis[0],"disabled");
   	  if(widget.removeDisplay){
   		 var displayButton = query(".button-cont", widget.domNode);
   		domClass.add(displayButton[0],"disNone");
   	  }


    },


    generateRequest: function (itemId,extraCategory,extraSelected) {
     var field = "transfers";
      var widget = this;
      var request = {extraCategory: extraCategory,transferCode: itemId,extraSelected: extraSelected};
      var url = BookflowUrl.transfertoggleurl;
      widget.controller.generateRequest(field, url, request);
    },

    refresh: function(field, response){
    	var widget = this;
    	  var widgetDom = widget.domNode;
    	  widget.jsonData = response;
    	  console.log(widget.jsonData);
    		if(!_.isEmpty( widget.jsonData.extraFacilityViewDataContainer.transferOptions.extraFacilityViewData)){
                var transferData = widget.jsonData.extraFacilityViewDataContainer.transferOptions.extraFacilityViewData;
                for(i=0;i<transferData.length;i++){
              	  if(transferData[i].selected == true){
              		  widget.transferTaxiPrice = transferData[i].currencyAppendedPerTaxiPrice;
              		  widget.transferPrice =  transferData[i].currencyAppendedPrice;
              		  widget.transferDescription = transferData[i].description;
              		  widget.transferFlag = true;
              		  break;
              	  }
              	  else{
              		  widget.transferFlag = false;
              	  }
                }
              }
    	  widget.model = response.extraFacilityViewDataContainer;
    	  widget.destroyRecursive();
          var node = domConstruct.create("div", null, dom.byId("your-transfer-section"));
          widget.create({
            "jsonData": response,
            "transitionType": 'WipeInOut'
          }, node);

         if(widget.removeDisplay){
        	  var displayButton = query(".button-cont", widget.domNode);
              domClass.remove(displayButton[0],"disNone");
          }





    }

  });
});