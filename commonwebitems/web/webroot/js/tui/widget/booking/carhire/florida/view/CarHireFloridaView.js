define("tui/widget/booking/carhire/florida/view/CarHireFloridaView", [
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
  "dojo/text!tui/widget/booking/carhire/florida/view/template/CarHireFloridaView.html",
  "tui/widget/booking/carhire/florida/model/CarHire",
  "tui/widget/booking/carhire/florida/view/CarHireViewAllOverlay",
  "tui/widget/booking/carhire/florida/view/CarHireTermsAndConditionView",
  "tui/widget/booking/carhire/florida/view/CarHireTermsAndConditionOverlay",
  "tui/widget/booking/constants/BookflowUrl",
  "dojo/topic",
  "dojo/html",
  "dojox/dtl",
  "dojox/dtl/Context",
  "tui/widget/_TuiBaseWidget"
], function (declare, _WidgetBase, Templatable, dtlTemplated, parser, on, query, domClass, domStyle, lang, dom, domConstruct, CarHireFloridaView,CarHireModel,CarHireViewAllOverlay,CarHireTermsAndConditionView,CarHireTermsAndConditionOverlay, BookflowUrl, topic, html) {

  return declare("tui.widget.booking.carhire.florida.view.CarHireFloridaView", [tui.widget._TuiBaseWidget, _WidgetBase, dtlTemplated, Templatable], {

    tmpl: CarHireFloridaView,
    preSelect: null,
    carHireSection: null,
    templateString: null,
    carHireModelObj: null,
    preSelect:  null,
    controller: null,
    checkBoxStatus:null,
    carHireDesc:null,
    commonFlag: false,
    displayFlag: null,
    extraAvailabilityFlag: false,
    basicFlag:false,
    refArray: [],
    carhireUpgradesAvailable:false,

    postMixInProperties: function () {
    	var widget = this;
    	console.log(widget.jsonData);
    	widget.commonFunction();

      },


      commonFunction: function(){
    	  var widget = this;
       	  var widgetDom = widget.domNode;

       	  widget.displayFlag = CarHireModel.displayFlag;
    	  CarHireModel.jsonData = dojo.clone(widget.jsonData);
          CarHireModel.cars=[];
          var cartypeInsurance = "";
          var insuranceFlag = true;


          if (CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade.length > 2){
        	  widget.carhireUpgradesAvailable = true;
          }else{
        	  widget.carhireUpgradesAvailable = false;
          }

          widget.checkBoxStatus = CarHireModel.jsonData.extraFacilityViewDataContainer.transferOptions.noTransferOpted;

          if(CarHireModel.jsonData.extraFacilityViewDataContainer.carHireExtras != null){
        	widget.extraAvailabilityFlag = true;
          }

          for(i=0;i<CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade.length;i++){
        	  if(i<2){
        		for(var j=0;j<CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData.length;j++){
        			if(CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].selected == true){
        				widget.commonFlag = true;

        				if (widget.commonFlag ==  true)
        					{
        					if(CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].type == "GOLD INS")
            					{
        						var tempVar = CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].currencyAppendedPricePerNight ;

        						var res = tempVar.slice(1);

        						var selected = CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].selected;

        						widget.carHireDesc = {desc:res,selected:selected};
        						widget.commonFlag= false;
        						break;

            					}else{
            						continue;
            					}
        					}
        			}else{

        				if (widget.commonFlag ==  true)
        					{
        					if(CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].type == "GOLD INS")
            					{
        						var tempVar = CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].currencyAppendedPricePerNight ;

        						var res = tempVar.slice(1);

        						var selected = CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].selected;

        						widget.carHireDesc = {desc:res,selected:selected};
        						widget.commonFlag= false;
            					break;

            					}else{
            						continue;
            					}
        					}else{
        						continue;
        					}

        			}
        			break;

    			}
        	  }else{
                  break;

                }

    		}
        	for(i=0;i<CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade.length;i++)
    		{
        		if(i<2){

        		}
    		}
        for(i=0;i<CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade.length;i++)
        		{
              if(i<2)  {
        		var carDescription = CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityCategoryCode;
        		var dymDescription = CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].carHireContentMap[carDescription+'_displayName'];
        		var carEquivalent = CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].carHireContentMap[carDescription+'_extra_detail_1'];
        		widget.insuranceFlag = true;
        		if(carDescription == 'BASIC'){
        			widget.basicFlag = true;
        		}
        		var item = {id:"car"+[i+1],description:dymDescription,equivalent:carEquivalent,extraFacility:[],uspContent:CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].carHireContentMap[carDescription+'_usp'],extraFalicityCode:carDescription}
        		/*for(var k=1; k<=3; k++){
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
    						}else if(CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].type == "GOLD INS")
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

        						widget.cartypeInsurance = "nobasic";
        						carDescription="GoldPackage";
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


        			}

        			CarHireModel.cars.push(item);
        			console.log(CarHireModel.cars);
        			console.log(CarHireModel);
        			widget.carHireModelObj=CarHireModel.cars;

              }else{

                break;
              }
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

    clickEvents: function(widgetDom){

    	var widget= this;


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

               var highLightNodes = query(".carhire_active_cls",widgetDom);

               _.each(highLightNodes,function(highLightNode){
                 domClass.remove(highLightNode, "carhire_active_cls");

               });
               var tempCarHireObj = dojo.attr(button, "id");
               var carHireObj = tempCarHireObj.split('|');
               widget.generateRequest(carHireObj[0],carHireObj[1]);

             });
             });

           });

   var items = query(".cartype",widgetDom);
   _.each(items, function (item) {

     var selected = query(".selected",item)[0];


     if(selected != null)
     	{
         var insuarnceTypeName = dojo.attr(selected, "name");
         var contentBlocks = query(insuarnceTypeName,widgetDom);
         _.each(contentBlocks,function(contentBlock){
           domClass.add(contentBlock, "carhire_active_cls");

         });
       domClass.add(item, "active");

     	}

   });

   var activeElement = query(".default", widgetDom)[0];


   if(activeElement != null){
	   widget.preSelect = dojo.attr(activeElement, "name");
   }


   widget.checkBoxDisplay();
    console.log(widget.viewAll_Overlay);
    if (widget.viewAll_Overlay) {
        on(widget.viewAll_Overlay, "click", lang.hitch(this, widget.CarHireOverlayInfo));
      }

    on(query(".termsAndConditions")[0], "click", lang.hitch(this, widget.carHireTermsandConditions));

    },

    checkBoxDisplay: function () {
      var widget = this;
      var widgetDom = widget.domNode;
      var items = query(".cartype",widgetDom);
      var dispIdontNeedTransfer = query(".iDontNeedTransfer",widgetDom)[0];

    },
    CarHireOverlayInfo: function(){
    	var widget = this;
    	if (widget.CarHireViewAllOverlay != null) {
            widget.CarHireViewAllOverlay.destroyRecursive();
            widget.CarHireViewAllOverlay = null;
          }
    	widget.CarHireViewAllOverlay = new CarHireViewAllOverlay({widgetId: "carHireViewInfoOverlay", modal: true});
    	widget.CarHireViewAllOverlay.open();
    },

    carHireTermsandConditions: function(){
    	 if (this.carHireTermsAndConditionView != null) {
             this.carHireTermsAndConditionView.destroyRecursive();
             this.carHireTermsAndConditionView = null;
             this.carHireTermsAndConditionOverlay.destroyRecursive();
             this.carHireTermsAndConditionOverlay = null;
           }
           this.carHireTermsAndConditionView = new CarHireTermsAndConditionView({
               "jsonData": this.jsonData,
               "id": "carHire-overlay"
           });
           domConstruct.place(this.carHireTermsAndConditionView.domNode, this.domNode, "last");
           this.carHireTermsAndConditionOverlay = new CarHireTermsAndConditionOverlay({widgetId: this.carHireTermsAndConditionView.id, modal: true});
           this.carHireTermsAndConditionOverlay.open();
    },

    generateRequest: function (itemId,extraCategory) {

    	console.log(itemId);

      var field = "carHire";
      var widget = this;
      var request = {extraCategory:extraCategory,carhirecode: itemId};
      var url = BookflowUrl.carhireurl;
      widget.controller.generateRequest(field, url, request);
      return request;

    },
    refresh: function(field, response){



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
    }



  });
});