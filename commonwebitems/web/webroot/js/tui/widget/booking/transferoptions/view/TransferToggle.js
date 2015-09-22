define("tui/widget/booking/transferoptions/view/TransferToggle", [
  "dojo/_base/declare",
  "dijit/_WidgetBase",
  "tui/widget/mixins/Templatable",
  "dojox/dtl/_Templated",
  "dojo/parser",
  "dojo/on",
  "dojo/query",
  "dojo/dom-class",
  "dojo/dom-style",
  "dojo/_base/lang",
  "dojo/dom",
  "dojo/dom-construct",
  "dojo/text!tui/widget/booking/transferoptions/view/Templates/TransferOptionsComponent.html",
  "tui/widget/booking/carhire/florida/model/CarHire",
  "tui/widget/booking/constants/BookflowUrl",
  "dojo/topic",
  "dojo/html",
  "tui/widget/booking/transferoptions/view/TransferOptionsCBOverlay",
  "dojox/dtl",
  "dojox/dtl/Context",
  "tui/widget/_TuiBaseWidget"
], function (declare, _WidgetBase, Templatable, dtlTemplated, parser, on, query, domClass, domStyle, lang, dom, domConstruct, transferOptionsComponent,CarHireModel,BookflowUrl, topic, html, TransferOptionsCBOverlay) {

  return declare("tui.widget.booking.transferoptions.view.TransferToggle", [tui.widget._TuiBaseWidget, _WidgetBase, dtlTemplated, Templatable], {

    tmpl: transferOptionsComponent,
    preSelect: null,
    uniFlag: false,

    postMixInProperties: function () {

    	  this.setDisplayBlock();

      },
    buildRendering: function () {
      this.find3PFCarrier();
      this.build3PFMap(this.jsonData.extraOptionsStaticContentViewData);
      this.templateString = this.renderTmpl(this.tmpl, this);
      delete this._templateCache[this.templateString];
      this.inherited(arguments);
    },

    build3PFMap : function (thirdPartyInfoMap){
  	  var getCarrierCode = "",
  	  widget = this;

  	  if(widget.jsonData.packageViewData.flightViewData.length){
  		  _.each(widget.jsonData.packageViewData.flightViewData, function(outboundCarrier){
  			  getCarrierCode = outboundCarrier.outboundSectors[0].carrierCode;
  			  if(thirdPartyInfoMap.extraContentMap != undefined){
  			  	widget.thirdPartyCoachTransferInfo = widget.get3PFDetailsMap(getCarrierCode);
  			  }
  		});
  	  }
    },

    get3PFDetailsMap : function(carrierCode, thirdPartyFlight){
        var key = this.jsonData.packageViewData.multicomThirdPartyFlightCarrierCode+"_CoachTrasfers_Additional_Description";
        var mixedKey = "MIX_"+this.jsonData.packageViewData.multicomThirdPartyFlightCarrierCode+"_CoachTrasfers_Additional_Description";
        return {
                          "coachTransferDesc": this.jsonData.extraOptionsStaticContentViewData.extraContentMap[key],
                          "mixedCoachTransferDesc": this.jsonData.extraOptionsStaticContentViewData.extraContentMap[mixedKey]
        }
   },

   setDisplayBlock: function(){
  	 this.displayNoTransferSection = true;
       if(this.checkFlag){

      	 this.displayNoTransferSection = this.jsonData.extraFacilityViewDataContainer.transferOptions.displayNoTransferOption;

       }
  },

    find3PFCarrier: function () {
  	  var widget = this;
  	  widget.getinBoundCarrierCode = widget.jsonData.packageViewData.flightViewData[0].inboundSectors[0].carrierCode;
  	  widget.getoutBoundCarrierCode = widget.jsonData.packageViewData.flightViewData[0].outboundSectors[0].carrierCode;
		  widget.thirdPartyFlightSelectionFlag=false;
		  if (widget.jsonData.packageViewData.multicomThirdPartyFlight) {
	    	  if( widget.getinBoundCarrierCode == widget.getoutBoundCarrierCode){
			 		 widget.thirdPartyFlightSelectionFlag = true;
			 		 widget.get3PFCarrierFlag = "single_carrier";
	    	  }
	    	  else {
			 		widget.thirdPartyFlightSelectionFlag = false;
			 		widget.get3PFCarrierFlag = "mixed_carrier";
			  }
         }
    },


    postCreate: function () {
      var widget = this;
      var widgetDom = widget.domNode;
      var controller = null;
      widget.inherited(arguments);
      parser.parse(widget.domNode);
      widget.controller = dijit.registry.byId("controllerWidget");
      var testvar = widget.controller.registerView(widget);
      widget.commonFunction();

      topic.subscribe("tui/widget/booking/transferOptionsCBOverlay/close/modalClick", function(){
    	  var noTransferCheckValue;
    	  if(dom.byId("NOTF")){
    		 noTransferCheckValue = dom.byId("NOTF");
    	 }else if(dom.byId("NOCARHIRETRANSFER")){
    		 noTransferCheckValue = dom.byId("NOCARHIRETRANSFER");
    	 }

    	  if(! _.isUndefined(noTransferCheckValue)){
    		  noTransferCheckValue.checked = false;
    	  }

      });
      var transfers = dojo.query('.item', widgetDom);
      for(var index = 0; index < transfers.length ; index++){
			if(transfers[index].id == "TXX"){
				widget.tagElement(transfers[index],"Return coach transfers");
			}else if(transfers[index].id == "HMZ"){
				widget.tagElement(transfers[index],"Return taxi transfers");
			}else if(transfers[index].id == "ECW"){
					widget.tagElement(transfers[index],"Care hire");
			}else{
				widget.tagElement(transfers[index],"I dont need transfers");
			}
      }

    },
    commonFunction: function(){
    	var widget = this;
    	var widgetDom = widget.domNode;
    	 var items = query(".item ", widgetDom);
         var activeElement = query(".default", widgetDom)[0];

         var extraCategory = widget.jsonData.extraFacilityViewDataContainer.transferOptions.extraFacilityCategoryCode;

         var checkBoxOkButton = dom.byId("checkBoxOk");
         var checkBoxCancelButton = dom.byId("checkBoxCancel");

         var item = query(".active", widgetDom)[0];
         var items = query(".item ", widgetDom);
         widget.uniCount = 0;
         if(item != null){
        	  var activeDomId = dojo.attr(item, "id");
         }

         var carHireFlBlock = dom.byId("carHire_florida")
         if(carHireFlBlock != null){
         if(activeDomId == "HMZ" || activeDomId == "TXX"){
        	 widget.uniCount = 1;
        	  domClass.add(carHireFlBlock, "disNone");
        	  CarHireModel.displayFlag = false;

          }else{
        	  widget.uniCount = 0;
        	  domClass.remove(carHireFlBlock, "disNone");
        	  CarHireModel.displayFlag = true;

          }
         }

         on(checkBoxCancelButton, "click", function (e) {


        	 var item = query(".active", widgetDom)[0];
             if(widget.uniFlag){

            	 var domId = dojo.attr(item, "id");
                 domClass.add(item, "active");
                 var carHireFlBlock = dom.byId("carHire_florida");
                 if(carHireFlBlock != null){

            		 if(domId == "HMZ" || domId == "TXX"){
 	        	  domClass.add(carHireFlBlock, "disNone");

 	          }else{
 	        	  domClass.remove(carHireFlBlock, "disNone");

 	          }
            		 widget.uniCount = 0;
            		 widget.uniFlag = false;

                 //widget.generateRequest(widget.preSelect,extraCategory);

           }
             }else{
            	 var checkbox = query("input[type=checkbox]", widgetDom)[0];
                 checkbox.checked = false;
            	  if (checkbox.checked == false) {

                      var domId = dojo.attr(item, "id");
                        domClass.add(item, "active");
                        var carHireFlBlock = dom.byId("carHire_florida");
                        if(carHireFlBlock != null){

                   		 if(domId == "HMZ" || domId == "TXX"){
        	        	  domClass.add(carHireFlBlock, "disNone");

        	          }else{
        	        	  domClass.remove(carHireFlBlock, "disNone");

        	          }
                        //widget.generateRequest(widget.preSelect,extraCategory);

                  }
                }
             }




         });

         on(checkBoxOkButton, "click", function (e) {
        	 console.log("ok button clicked");

        	 if(widget.uniFlag){
        		 if(widget.transferOptionsCBOverlay != null){
            		 widget.transferOptionsCBOverlay.close();
            		 var carHireFlBlock = dom.byId("carHire_florida");
            		 if(carHireFlBlock != null){
            		 domClass.add(carHireFlBlock, "disNone");
            		 }
            		 _.each(items, function (item) {
                         domClass.remove(item, "active");
                       });
                     domClass.add(widget.item, "active");
            		 widget.generateRequest(widget.itemId,extraCategory);
            	 }

        		 widget.uniFlag = false;


             }else{
            	 if(widget.transferOptionsCBOverlay != null){
            		 widget.transferOptionsCBOverlay.close();
            		 var carHireFlBlock = dom.byId("carHire_florida");
            		 if(carHireFlBlock != null){
            		 domClass.add(carHireFlBlock, "disNone");
            		 }
            		 _.each(items, function (item) {
                     domClass.remove(item, "active");
                   });
            		 widget.generateRequest(widget.checkBoxIdVal,widget.extraCategoryval);
            	 }

             }



         });

         if(activeElement != null){

        	 CarHireModel.preSelectTransfer = dojo.attr(activeElement, "id");
        	 widget.preSelect =  CarHireModel.preSelectTransfer;
         }else{

        	 widget.preSelect =  CarHireModel.preSelectTransfer;

         }
         widget.isChecked = false;
         widget.checkBoxDisplay(extraCategory);
         var dispIdontNeedTransfer = query(".iDNTransferText",widgetDom)[0];
   		 var dispIdontNeedCarHire = query(".iDNCarHireText",widgetDom)[0];

    	 _.each(items, function (item) {
    	        var button = query(".transfer-select", item)[0];
    	        if(button != null){
    	        	 on(button, "click", function (e) {
    	        		 if(widget.jsonData.alamoCarHire){
    	        			 var itemId = dojo.attr(item, "id");
        	        		 if(itemId == "HMZ" || itemId == "TXX"){


       	    	        	  widget.itemId = itemId;
       	    	        	  widget.item = item;
       	    	        	if(widget.uniCount == 0){
   	    	        			widget.uniFlag = true;

   	    	        			domClass.remove(dispIdontNeedCarHire, "disNone");
   	    	        	   		domClass.add(dispIdontNeedTransfer, "disNone");
   	    	        		  widget.transferOptionsCBOverlay = new TransferOptionsCBOverlay({widgetId: "checkBox-Overlay", modal: true});
         	    	          widget.transferOptionsCBOverlay.open();

   	    	        	}else{
   	    	        		widget.generateRequest(itemId,extraCategory);
   	    	        	}
       	    	        	widget.uniCount = widget.uniCount + 1;


       	    	          }else{
       	    	        	widget.uniCount = 0;
       	    	        	   widget.generateRequest(itemId,extraCategory);
       	    	          }
    	        		 }else{
    	        			 _.each(items, function (item) {
    	    	    	            domClass.remove(item, "active");
    	    	    	          });
    	    	    	          domClass.add(item, "active");
    	    	    	          var itemId = dojo.attr(item, "id");
    	    	    	          widget.generateRequest(itemId,extraCategory);
    	    	    	          var noTransferCheckValue ;
    	    	    	          if(dom.byId("NOTF")){
    	    	    	     		 noTransferCheckValue = dom.byId("NOTF");
    	    	    	     	 }else if(dom.byId("NOCARHIRETRANSFER")){
    	    	    	     		 noTransferCheckValue = dom.byId("NOCARHIRETRANSFER");
    	    	    	     	 }
    	    	    	          if(! _.isUndefined(noTransferCheckValue)){
    	    	    	    		  noTransferCheckValue.checked = false;
    	    	    	    	  }
    	        		 }

    	    	          var carHireFlBlock = dom.byId("carHire_florida");

    	    	          if(carHireFlBlock != null){
    	    	        	  if(itemId == "HMZ" || itemId == "TXX"){
    	        	        	  domClass.add(carHireFlBlock, "disNone");
    	        	        	  CarHireModel.displayFlag = false;
    	        	          }else{
    	        	        	  domClass.remove(carHireFlBlock, "disNone");
    	        	        	  CarHireModel.displayFlag = true;
    	        	          }

    	    	          }
    	    	        });
    	        }

    	      });

    },
    generateRequest: function (itemId,extraCategory) {
      var field = "transfers";
      var widget = this;
      var request = {extraCategory: extraCategory,transferCode: itemId};
      var url = BookflowUrl.transfertoggleurl;
      widget.controller.generateRequest(field, url, request);
      return request;
    },
    refresh: function(field, response){
    	if(field == "carHire" || field == "carHireOverlay" || field == "transfers"){

    		var widget = this;
            var widgetDom = widget.domNode;
            widget.jsonData = response;
            var html = widget.renderTmpl(widget.tmpl, widget);
            domConstruct.place(html, widget.domNode, "only");

            widget.setDisplayBlock();

            dojo.parser.parse(widget.domNode);
            widget.commonFunction();
            widget.find3PFCarrier();
            widget.build3PFMap(widget.jsonData.extraOptionsStaticContentViewData);
    	}
    },

    checkBoxDisplay: function (extraCategory) {
      var widget = this;
      var widgetDom = widget.domNode;
      var items = query(".item ", widgetDom);
      var dispIdontNeedTransfer = query(".iDNTransferText",widgetDom)[0];
      var dispIdontNeedCarHire = query(".iDNCarHireText",widgetDom)[0];
      var checkbox = query("input[type=checkbox]", widgetDom)[0];

      if(checkbox !=null){
    	  var checkBoxId = dojo.attr(checkbox, "id");
    	  console.log(checkbox.checked);
    	  if(checkbox.checked == false){
        	  var carHireFlBlock = dom.byId("carHire_florida");
        	  if(carHireFlBlock != null){
        	  	 domClass.remove(carHireFlBlock, "disNone");
        	  }
          }else {
        	  var carHireFlBlock = dom.byId("carHire_florida");
        	  if(carHireFlBlock != null){
        	  	 domClass.add(carHireFlBlock, "disNone");
        	  	 widget.uniCount = 1;
        	  }
          }
          on(checkbox, "click", function () {
            if (this.checked == false) {
            	widget.uniCount = 0;
       		 widget.uniFlag = false;
              _.each(items, function (item) {
                var domId = dojo.attr(item, "id");
                if (widget.preSelect == domId) {
                  domClass.add(item, "active");
                  domClass.add(dispIdontNeedTransfer,"disNone");
                  var carHireFlBlock = dom.byId("carHire_florida");
    if(carHireFlBlock != null){
    	 domClass.remove(carHireFlBlock, "disNone");
    }
                  widget.generateRequest(widget.preSelect,extraCategory);
                }
              });
            } else {
              domClass.remove(dispIdontNeedTransfer,"disNone");
        		domClass.add(dispIdontNeedCarHire, "disNone");

              widget.transferOptionsCBOverlay = new TransferOptionsCBOverlay({widgetId: "checkBox-Overlay", modal: true});
              widget.transferOptionsCBOverlay.open();
              widget.checkBoxIdVal =checkBoxId;
              widget.extraCategoryval =extraCategory;

            }
          });

      }


    }

  });
});