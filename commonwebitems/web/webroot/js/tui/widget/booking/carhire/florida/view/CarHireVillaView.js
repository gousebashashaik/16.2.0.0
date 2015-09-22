define("tui/widget/booking/carhire/florida/view/CarHireVillaView", [
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
  "dojo/text!tui/widget/booking/carhire/florida/view/template/CarHireVillaViewTmpl.html",
  "tui/widget/booking/carhire/florida/model/CarHire",
  "tui/widget/booking/carhire/florida/view/CarHireViewAllOverlay",
  "tui/widget/booking/constants/BookflowUrl",
  "dojo/topic",
  "dojo/html",
  "dojox/dtl",
  "dojox/dtl/Context",
  "tui/widget/_TuiBaseWidget"
], function (declare, _WidgetBase, Templatable, dtlTemplated, parser, on, query, domClass, domStyle, lang, dom, domConstruct, CarHireVillaViewTmpl,CarHireModel,CarHireViewAllOverlay,BookflowUrl, topic, html) {

  return declare("tui.widget.booking.carhire.florida.view.CarHireVillaView", [tui.widget._TuiBaseWidget, _WidgetBase, dtlTemplated, Templatable], {

    tmpl: CarHireVillaViewTmpl,
    preSelect: null,
    carHireSection:null,
    templateString:null,
    carHireModelObj:null,
    preSelect:  null ,
    controller: null,
    checkBoxStatus:null,
    carHireDesc:null,
    commonFlag: false,
    extraAvailabilityFlag: false,
    postMixInProperties: function () {
    	var widget = this;
    	console.log(widget.jsonData);

    	widget.commonFunction();

      },


      commonFunction: function(){

    	  var widget = this;
    	  var widgetDom = widget.domNode;


    	  	CarHireModel.jsonData = dojo.clone(widget.jsonData);
          	CarHireModel.villaCars=[];
        	var cartypeInsurance = "";
        	var insuranceFlag = true;
        	widget.checkBoxStatus = CarHireModel.jsonData.extraFacilityViewDataContainer.transferOptions.noTransferOpted;

        	if(CarHireModel.jsonData.extraFacilityViewDataContainer.carHireExtras != null){
        		widget.extraAvailabilityFlag = true;
        	}

    	  for(i=0;i<CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade.length;i++)
  		{
        if(i<1)  {
  		var carDescription = "Basic Car Hire";//CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityCategoryCode;
  		widget.insuranceFlag = true;
  		var item = {id:"car"+[i+1],description:carDescription,extraFacility:[]}
  		var carDescription="";
  		var carCurrencyAppendedPrice="";
  		var carPerPersonPrice="";
  		var code = "";
      var free= null;
      var selected = null;
      var included = null;


        for(var j=0;j<CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData.length;j++)
  			{

  					widget.cartypeInsurance = CarHireModel.jsonData.extraFacilityViewDataContainer.carHireUpgrade[i].extraFacilityViewData[j].type;
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




  			CarHireModel.villaCars.push(item);
  			console.log(CarHireModel.villaCars);
  			console.log(CarHireModel);
  			widget.carHireModelObj=CarHireModel.villaCars;

  			}

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


    	var items = query(".villacar_type",widgetDom);
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
               var tempCarHireObj = dojo.attr(button, "id");
               var carHireObj = tempCarHireObj.split('|');
               widget.generateRequest(carHireObj[0],carHireObj[1]);

             });
             });

           });

         var items = query(".villacar_type",widgetDom);
         _.each(items, function (item) {

           var selected = query(".selected",item)[0];


           if(selected != null)
           	{

             domClass.add(item, "active");

           	}
         });
         var activeElement = query(".default", widgetDom)[0];

         if(activeElement != null){
        	 widget.preSelect = dojo.attr(activeElement, "name");
         }


         widget.checkBoxDisplay();


    },

    checkBoxDisplay: function () {
    	var widget = this;
        var widgetDom = widget.domNode;
        var items = query(".villacar_type",widgetDom);
        var checkbox = query("input[type=checkbox]", widgetDom)[0];
        var dispIdontNeedTransfer = query(".iDontNeedTransfer",widgetDom)[0];
        var checkBoxId = dojo.attr(checkbox, "id");
        on(checkbox, "click", function () {
          if (this.checked == false) {

                domClass.add(dispIdontNeedTransfer,"disNone");
                widget.generateRequest(widget.preSelect);

          } else {
            _.each(items, function (item) {
              domClass.remove(item, "active");
              domClass.remove(item, "selected");
            });

            domClass.remove(dispIdontNeedTransfer,"disNone");
            widget.generateRequest(checkBoxId);
          }
        });
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