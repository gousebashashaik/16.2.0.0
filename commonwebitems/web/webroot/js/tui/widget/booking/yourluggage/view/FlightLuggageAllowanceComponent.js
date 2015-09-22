define("tui/widget/booking/yourluggage/view/FlightLuggageAllowanceComponent", ["dojo/_base/declare",
  "dijit/_WidgetBase",
  "tui/widget/mixins/Templatable",
  "dojox/dtl/_Templated",
  "dojo/parser",
  "dojo/on",
  "dojo/query",
  "dojo/dom-attr",
  "dojo/dom-class",
  "dojo/_base/lang",
  "dojo/dom",
  "dojo/dom-construct",
  "dojo/text!tui/widget/booking/yourluggage/view/Templates/FlightLuggageAllowanceComponent.html",
  "tui/widget/booking/yourluggage/view/LuggageAllowanceComponentService",
  "tui/widget/booking/constants/BookflowUrl",
  "dojo/html",
  "tui/widget/booking/constants/TotalPrice",
  "tui/widget/booking/bookflowMsgs/nls/Bookflowi18nable",
  "dojox/dtl",
  "dojox/dtl/Context",
  "tui/widget/_TuiBaseWidget"
  ],
  function (declare, _WidgetBase, Templatable, dtlTemplated, parser, on, query,domAttr, domClass,  lang, dom, domConstruct, FlightLuggageAllowanceComponent,LuggageAllowanceComponentService,BookflowUrl, html,TotalPrice,Bookflowi18nable) {
    return declare("tui.widget.booking.yourluggage.view.FlightLuggageAllowanceComponent", [tui.widget._TuiBaseWidget, _WidgetBase, dtlTemplated, Templatable,Bookflowi18nable], {

      tmpl: FlightLuggageAllowanceComponent,
      templateString: "",
      thirdPartyLuggageContent:"",
      thirdPartyHandLuggage:"",
      restrictionString:"",

      postMixInProperties: function () {
    	  this.initBookflowMessaging();
    	  this.thirdPartyLuggageContentFun();
    	 var restrictionLink= this.bookflowMessaging[dojoConfig.site].restrictionLink;
    	  this.restrictionString=this.jsonData.flightOptionsStaticContentViewData.flightContentMap[restrictionLink];



      },

      buildRendering: function () {
    		var productType = this.jsonData.packageType;
    		this.controller = dijit.registry.byId("controllerWidget");
    		  this.templateVariant = LuggageAllowanceComponentService(productType);
        this.templateString = this.renderTmpl(this.tmpl, this);
        delete this._templateCache[this.templateString];
        this.inherited(arguments);




      },

      postCreate: function () {
        var widget = this;
        var widgetDom = widget.domNode;
        parser.parse(widget.domNode);
        widget.labelDisplay();
        var controller = null;
        widget.controller = dijit.registry.byId("controllerWidget");
        var testvar = widget.controller.registerView(widget);
        widget.attachEvent();
        this.inherited(arguments);
        this.tagElements(query('.luggage-select-checkbox', this.domNode),"standandSelection");

      },
      attachEvent: function () {
        var widget = this;
        var widgetDom = widget.domNode;
        var radioButtonAttachPoints = query("input[data-dojo-attach-point$='radioButton']", widgetDom);
        _.each(radioButtonAttachPoints, function (radioButton) {
          on(radioButton, "click", lang.hitch(widget, widget.handleRadioButtonClick, radioButton));
        });
        var selectButtons = query("button.button", widgetDom);
        _.each(selectButtons, function (selectButton) {
          on(selectButton, "click", lang.hitch(widget, widget.handleSelectAllRadioButtons, selectButton));
        });
      },
      handleRadioButtonClick: function (radioButton) {
        var widget = this;
        var passengerId = domAttr.get(radioButton, "name");
        var baggageId = domAttr.get(radioButton, "value");
        var ajaxObj = widget.ajaxObjGenerate(radioButton);
        widget.generateRequest(ajaxObj);
      },

      handleSelectAllRadioButtons: function (selectButton) {
    	  var widget = this;
    	  var widgetDom = widget.domNode;
          var passengerId = "all";
          var selectButtonbaggageId = selectButton.id;


          var radioButtonAttachPoints = query("input[data-dojo-attach-point$='radioButton']", widgetDom);
      	var responseObj = [];
      	var ajaxObj = {};
      	_.each(radioButtonAttachPoints, function (radioButton) {
            var radioButtonbaggageId = domAttr.get(radioButton, "value");
      		if(selectButtonbaggageId == radioButtonbaggageId){

      			var passengerId = domAttr.get(radioButton, "name");
                  var baggageId = domAttr.get(radioButton, "value");
                  if(!responseObj[baggageId])
                  {
                	  var temparray = [];
                	  temparray.push(passengerId);
                      responseObj[baggageId] = temparray;
                  }
                  else{
                	  responseObj[baggageId].push(passengerId);
                  }
      		}
          });


      	 //responseObj[baggageId] = _.without(responseObj[baggageId], 1);

      	for(var key in responseObj){
        	  var tempArray = responseObj[key];
        	ajaxObj[key] = tempArray.toString();

          }



          widget.generateRequest(ajaxObj);
        },

        ajaxObjGenerate: function(radioButton){

        	var widget = this;
        	var widgetDom = widget.domNode;
        	var radioButtonAttachPoints = query("input[data-dojo-attach-point$='radioButton']", widgetDom);
        	var responseObj = [];
        	var ajaxObj = {};
        	var clickedRadionButton = radioButton;
        	_.each(radioButtonAttachPoints, function (radioButton) {

        		if(radioButton.checked == true && radioButton.value != "doubleAmt"){

        			var passengerId = domAttr.get(radioButton, "name");
                    var baggageId = domAttr.get(radioButton, "value");





                    if(!responseObj[baggageId])
                    {
                  	  var temparray = [];
                  	  temparray.push(passengerId);
                        responseObj[baggageId] = temparray;
                    }
                    else{
                  	  responseObj[baggageId].push(passengerId);
                    }

                    if( domClass.contains(radioButton,"checked")){

                    	if(clickedRadionButton == radioButton){
                    		 responseObj[baggageId] = _.without(responseObj[baggageId],passengerId);

                    		 if(_.isEmpty(responseObj[baggageId])){
                    			 delete responseObj[baggageId];
                    		 }


                    	}


                    }


        		}

            });

        	for(var key in responseObj){
          	  var tempArray = responseObj[key];
          	ajaxObj[key] = tempArray.toString();

            }
        	return ajaxObj;
        },
      generateRequest: function (ajaxObj) {
        var widget = this;
        var request = {};
        var field = "baggage";
        var request = {selected: dojo.toJson(ajaxObj)};
        var url =BookflowUrl.luggageallowanceurl;
        widget.controller.generateRequest(field, url, request);
        return request;
      },

      refresh: function (field, response) {
        var widget = this;
        widget.jsonData = response;
        widget.thirdPartyLuggageContentFun();
        var html = widget.renderTmpl(widget.tmpl, widget);
        domConstruct.place(html, widget.domNode, "only");
        parser.parse(widget.domNode);
        widget.labelDisplay();
        widget.attachEvent();
      },

      thirdPartyLuggageContentFun: function () {
    	  var key = this.jsonData.packageViewData.multicomThirdPartyFlightCarrierCode+"_Luggage_Description";
    	  var handKey = this.jsonData.packageViewData.multicomThirdPartyFlightCarrierCode+"_Handluggage_Description";
    	  this.thirdPartyLuggageContent = this.jsonData.flightOptionsStaticContentViewData.flightContentMap[key];
    	  this.thirdPartyHandLuggage = this.jsonData.flightOptionsStaticContentViewData.flightContentMap[handKey];
		  var restrictionLink= this.bookflowMessaging[dojoConfig.site].restrictionLink;
    	  this.restrictionString=this.jsonData.flightOptionsStaticContentViewData.flightContentMap[restrictionLink];
      },

      labelDisplay: function () {
        var widget = this;
        var widgetDom = widget.domNode;
        widget.baggageCount = widget.jsonData.extraFacilityViewDataContainer.baggageOptions.extraFacilityViewData.length;
        if (widget.jsonData.baggageSectionDisplayed == true) {
          if (jsonData.packageViewData.paxViewData.noOfInfants > 0) {
            widget.baggageCount = widget.baggageCount + 1;
          }
          if(widget.baggageCount === 1){
        	  widget.baggageCount = widget.baggageCount + 1;
          }
		   var bagWeight = _.filter(widget.jsonData.extraFacilityViewDataContainer.baggageOptions.extraFacilityViewData, function(extra){ return extra.weightCode == 0; });
		   if(!_.isEmpty(bagWeight)){
			widget.baggageCount = 2;
		   }
          var bagCount = "with-" + widget.baggageCount + "-BagExtras";
          domClass.add(dom.byId("luggage-allowance"), bagCount);
        } else {
          if (jsonData.packageViewData.paxViewData.noOfInfants > 0) {
        	var bagCount = "with-" + 2 + "-BagExtras";
            domClass.add(dom.byId("luggage-allowance"), bagCount);
          } else {

          }
        }


      }
    });

  });