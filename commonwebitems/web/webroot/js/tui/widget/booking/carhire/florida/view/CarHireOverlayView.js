define("tui/widget/booking/carhire/florida/view/CarHireOverlayView", [
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
  "dojo/topic",
  "dojo/text!tui/widget/booking/carhire/florida/view/template/CarHireOverlayView.html",
  "tui/widget/booking/carhire/florida/model/CarHire",
  "tui/widget/booking/constants/BookflowUrl",
  "dojo/html",
  "dojox/dtl",
  "dojox/dtl/Context",
  "tui/widget/_TuiBaseWidget"
], function (declare, _WidgetBase, Templatable, dtlTemplated, parser, on, query, domClass, domStyle, lang, dom, domConstruct, topic, CarHireFloridaView,CarHireModel,BookflowUrl, html) {

  return declare("tui.widget.booking.carhire.florida.view.CarHireOverlayView", [tui.widget._TuiBaseWidget, _WidgetBase, dtlTemplated, Templatable], {

    tmpl: CarHireFloridaView,
    preSelect: null,
    carHireSection:null,
    templateString:null,
    carHireOverlayModelObj:null,
    controller: null,
    postMixInProperties: function () {

    	var widget = this;
    	console.log(widget.jsonData);


    	widget.commonFunction();



      },
      commonFunction: function(){
    	  var widget = this;
    	  CarHireModel.jsonData = dojo.clone(widget.jsonData);
          CarHireModel.altCars=[];
        	var cartypeInsurance = "";
        	var insuranceFlag = true;

        for(i=0;i<CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade.length;i++)
        		{
        		var carDescription = CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityCategoryCode;
        	    var dymDescription = CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].carHireContentMap[carDescription+'_displayName'];
        		var carEquivalent = CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].carHireContentMap[carDescription+'_extra_detail_1'];
        		widget.insuranceFlag = true;
        		var item = {id:"car"+[i+1],description:dymDescription,equivalent:carEquivalent,extraFacility:[],uspContent:CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].carHireContentMap[carDescription+'_usp'],extraFalicityCode:carDescription};
        	/*	for(var k=1; k<=3; k++){
            		var usp = CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].carHireContentMap[carDescription+'_usp'+k];
            		item.uspContent.push(usp);
            	}*/
        		var carDescription="";
        		var carCurrencyAppendedPrice="";
        		var carPerPersonPrice="";
        		var code = "";
        		 var free= null;
        	        var selected = null;
        	        var included = null;
        		for(var j=0;j<CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData.length;j++)
        			{

        				if(CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData.length == 3)
        				{
        					if(CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].type == "BASIC")
    						{
        					widget.cartypeInsurance = "basic";
        					carDescription=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].description;
        					carCurrencyAppendedPrice=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].currencyAppendedPrice;
        					carPerPersonPrice=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].currencyAppendedPricePerNight;
        					code=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].code;
        					free=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].free;
        		              selected=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].selected;
        		              included=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].included;
        		              var carExtraFacility ={cartypeInsurance:widget.cartypeInsurance,
        		                                    carDescription:carDescription,
        		                                    code:code,
        		                                    carCurrencyAppendedPrice:carCurrencyAppendedPrice,
        		                                    carPerPersonPrice:carPerPersonPrice,
        		                                    free:free,
        		                                    selected:selected,
        		                                    included:included};
        		    					item.extraFacility.push(carExtraFacility);
    						}else if(CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].type == "FULLY INC")
    						{
        					widget.cartypeInsurance = "Fully Inclusive";
        					carDescription=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].description;
        					carCurrencyAppendedPrice=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].currencyAppendedPrice;
        					carPerPersonPrice=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].currencyAppendedPricePerNight;
        					code=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].code;
        					free=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].free;
        		              selected=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].selected;
        		              included=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].included;
        		              var carExtraFacility ={cartypeInsurance:widget.cartypeInsurance,
        		                                    carDescription:carDescription,
        		                                    code:code,
        		                                    carCurrencyAppendedPrice:carCurrencyAppendedPrice,
        		                                    carPerPersonPrice:carPerPersonPrice,
        		                                    free:free,
        		                                    selected:selected,
        		                                    included:included};
        		    					item.extraFacility.push(carExtraFacility);
    						}else if(CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].type == "GOLD INS")
    						{
        					widget.cartypeInsurance = "Gold Package";
        					carDescription=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].description;
        					carCurrencyAppendedPrice=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].currencyAppendedPrice;
        					carPerPersonPrice=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].currencyAppendedPricePerNight;
        					code=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].code;
        					free=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].free;
        		              selected=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].selected;
        		              included=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].included;
        		              var carExtraFacility ={cartypeInsurance:widget.cartypeInsurance,
        		                                    carDescription:carDescription,
        		                                    code:code,
        		                                    carCurrencyAppendedPrice:carCurrencyAppendedPrice,
        		                                    carPerPersonPrice:carPerPersonPrice,
        		                                    free:free,
        		                                    selected:selected,
        		                                    included:included};
        		    					item.extraFacility.push(carExtraFacility);
    						}



        				}else if(CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData.length == 2){

        					widget.refArray = _.pluck(CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData, 'type');

        					var basicInsu = _.contains(widget.refArray, "BASIC");
        					var FullInsu = _.contains(widget.refArray, "FULLY INC");
        					var goldInsu = _.contains(widget.refArray, "GOLD INS");

        					if(!basicInsu && j == 0)
    						{
    						widget.cartypeInsurance = "nobasic";
    						carDescription="Basic Package";
    						var carExtraFacility ={cartypeInsurance:widget.cartypeInsurance,carDescription:carDescription};
    						item.extraFacility.push(carExtraFacility);
    						}

    							if(CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].type == "BASIC")
        						{
            					widget.cartypeInsurance = "basic";
            					carDescription=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].description;
            					carCurrencyAppendedPrice=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].currencyAppendedPrice;
            					carPerPersonPrice=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].currencyAppendedPricePerNight;
            					code=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].code;
                      free=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].free;
                      selected=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].selected;
                      included=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].included;
                      var carExtraFacility ={cartypeInsurance:widget.cartypeInsurance,
                                            carDescription:carDescription,
                                            code:code,
                                            carCurrencyAppendedPrice:carCurrencyAppendedPrice,
                                            carPerPersonPrice:carPerPersonPrice,
                                            free:free,
                                            selected:selected,
                                            included:included};
            					item.extraFacility.push(carExtraFacility);

            						if(!FullInsu){
            							widget.cartypeInsurance = "nobasic";
                						carDescription = "Full Inclusive Package"
                						var carExtraFacility ={cartypeInsurance:widget.cartypeInsurance,carDescription:carDescription};
                						item.extraFacility.push(carExtraFacility);
            							}

        							}



        							if(CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].type == "FULLY INC")
            						{

                					widget.cartypeInsurance = "FullyInclusive";
                					carDescription=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].description;
                					carCurrencyAppendedPrice=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].currencyAppendedPrice;
                					carPerPersonPrice=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].currencyAppendedPricePerNight;
                					code=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].code;
                          free=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].free;
                          selected=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].selected;
                          included=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].included;
                          var carExtraFacility ={cartypeInsurance:widget.cartypeInsurance,
                            carDescription:carDescription,
                            code:code,
                            carCurrencyAppendedPrice:carCurrencyAppendedPrice,
                            carPerPersonPrice:carPerPersonPrice,
                            free:free,
                            selected:selected,
                            included:included};
                          item.extraFacility.push(carExtraFacility);
                          if(!goldInsu){
                        	  widget.cartypeInsurance = "nobasic";
      						carDescription="GoldPackage";
      						var carExtraFacility ={cartypeInsurance:widget.cartypeInsurance,carDescription:carDescription};
      						item.extraFacility.push(carExtraFacility);
								}
            						}



        							if(CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].type == "GOLD INS")
            						{

        								{
        		        					widget.cartypeInsurance = "GoldPackage";
        		        					carDescription=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].description;
        		        					carCurrencyAppendedPrice=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].currencyAppendedPrice;
        		        					carPerPersonPrice=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].currencyAppendedPricePerNight;
        		        					code=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].code;
        		                  free=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].free;
        		                  selected=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].selected;
        		                  included=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].included;
        		                  var carExtraFacility ={cartypeInsurance:widget.cartypeInsurance,
        		                    carDescription:carDescription,
        		                    code:code,
        		                    carCurrencyAppendedPrice:carCurrencyAppendedPrice,
        		                    carPerPersonPrice:carPerPersonPrice,
        		                    free:free,
        		                    selected:selected,
        		                    included:included};
        		                  item.extraFacility.push(carExtraFacility);
        		    						}
            						}



        				}else if (CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData.length == 1)
        					{
        					if(CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].type == "FULLY INC")
        						{
        						widget.cartypeInsurance = "nobasic";
        						carDescription="Basic Package";
        						var carExtraFacility ={cartypeInsurance:widget.cartypeInsurance,carDescription:carDescription};
        						item.extraFacility.push(carExtraFacility);

        						widget.cartypeInsurance = "Fully Inclusive";
        						carDescription=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].description;
            					carCurrencyAppendedPrice=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].currencyAppendedPrice;
            					carPerPersonPrice=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].currencyAppendedPricePerNight;
            					code=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].code;
            					free=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].free;
            		              selected=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].selected;
            		              included=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].included;
            		              var carExtraFacility ={cartypeInsurance:widget.cartypeInsurance,
            		                                    carDescription:carDescription,
            		                                    code:code,
            		                                    carCurrencyAppendedPrice:carCurrencyAppendedPrice,
            		                                    carPerPersonPrice:carPerPersonPrice,
            		                                    free:free,
            		                                    selected:selected,
            		                                    included:included};
            		    					item.extraFacility.push(carExtraFacility);

        						widget.cartypeInsurance = "nobasic";
        						carDescription="Gold Package";
        						var carExtraFacility ={cartypeInsurance:widget.cartypeInsurance,carDescription:carDescription};
        						item.extraFacility.push(carExtraFacility);

        						}
        					if(CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].type == "GOLD INS")
    						{
        						widget.cartypeInsurance = "nobasic";
        						carDescription="Basic Package";
        						var carExtraFacility ={cartypeInsurance:widget.cartypeInsurance,carDescription:carDescription};
        						item.extraFacility.push(carExtraFacility);

        						widget.cartypeInsurance = "nobasic";
        						carDescription = "Full Inclusive Package"
        						var carExtraFacility ={cartypeInsurance:widget.cartypeInsurance,carDescription:carDescription};
        						item.extraFacility.push(carExtraFacility);

        						widget.cartypeInsurance = "Gold Package";
        						carDescription=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].description;
            					carCurrencyAppendedPrice=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].currencyAppendedPrice;
            					carPerPersonPrice=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].currencyAppendedPricePerNight;
            					code=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].code;
            					free=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].free;
            		              selected=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].selected;
            		              included=CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].included;
            		              var carExtraFacility ={cartypeInsurance:widget.cartypeInsurance,
            		                                    carDescription:carDescription,
            		                                    code:code,
            		                                    carCurrencyAppendedPrice:carCurrencyAppendedPrice,
            		                                    carPerPersonPrice:carPerPersonPrice,
            		                                    free:free,
            		                                    selected:selected,
            		                                    included:included};
            		    					item.extraFacility.push(carExtraFacility);

    						}

        					}


        			}

        			CarHireModel.altCars.push(item);
        			console.log(CarHireModel.altCars);
        			console.log(CarHireModel);
        			widget.carHireModelObj=CarHireModel.altCars;


        		}


      },

      buildRendering: function () {
    	  console.log(this);
    	  console.log(this.carHireModelObj);
        this.templateString = this.renderTmpl(this.tmpl, this);
        delete this._templateCache[this.templateString];
        this.inherited(arguments);


      },

    postCreate: function () {
    	var widget= this;
    	var widgetDom = widget.domNode;
    	widget.controller = dijit.registry.byId("controllerWidget");
        var testvar = widget.controller.registerView(widget);
        dojo.parser.parse(widget.domNode);
        widget.clickEvents(widgetDom);


    },

    clickEvents: function(widgetDom)
    {
    	var widget = this;
    	 var items = query(".cartype",widgetDom);
         _.each(items, function (item) {

           var buttons = query(".button", item);
           _.each(buttons,function(button){
             on(button, "click", function (e) {
               _.each(items, function (item) {
                 domClass.remove(item, "active");
               });
               var buttons = query(".button", widgetDom);
               _.each(buttons,function(button){
                 domClass.remove(button, "selected");
               });
               domClass.add(item, "active");
               domClass.add(button, "selected");


               topic.publish("tui/booking/carhireclose");
               var tempCarHireObj = dojo.attr(button, "id");
               var carHireObj = tempCarHireObj.split('|');
               widget.generateRequest(carHireObj[0],carHireObj[1]);



             });
           });

         });


         _.each(items, function (item) {

             var selected = query(".selected",item)[0];
             if(selected != null)
             	{
             domClass.add(item, "active");
             	}

           });

    },

    generateRequest: function (itemId,extraCategory) {

    	console.log(itemId);

        var field = "carHireOverlay";
        var widget = this;
        var request = {extraCategory:extraCategory,carhirecode: itemId};
        var url = BookflowUrl.carhireurl;
        widget.controller.generateRequest(field, url, request);
        return request;

    },
    refresh: function(field,response){

    	if(field == "carHire" || field == "carHireOverlay" || field == "transfers"){

    		var widget = this;
            var widgetDom = widget.domNode;
            widget.jsonData = response;

            widget.commonFunction();

            var html = widget.renderTmpl(widget.tmpl, widget);
            domConstruct.place(html, widget.domNode, "only");
            dojo.parser.parse(widget.domNode);

            widget.clickEvents(widgetDom);

    	}
    },
    checkBoxDisplay: function () {

    }

  });
});