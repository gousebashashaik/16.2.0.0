define("tui/widget/booking/infantoptions/view/CrecheSpaceView", [
  "dojo/_base/declare",
  "dojo/query",
  "dojo/dom",
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
  "dojo/text!tui/widget/booking/infantoptions/view/templates/CrecheSpaceView.html",
  "tui/widget/booking/constants/BookflowUrl",
  "tui/widget/booking/bookflowMsgs/nls/Bookflowi18nable",
  "dojo/on",
  "dojo/_base/json"

], function (declare, query, dom, domAttr, domConstruct, domClass, Evented, topic, lang, domStyle, _TuiBaseWidget,
             dtlTemplate, Templatable, CrecheSpaceView,BookflowUrl,Bookflowi18nable, on, jsonUtil) {

  return declare('tui.widget.booking.infantoptions.view.CrecheSpaceView',
          [_TuiBaseWidget, dtlTemplate, Templatable, Evented, Bookflowi18nable], {

      tmpl: CrecheSpaceView,
      templateString: "",
      widgetsInTemplate: true,
      totalPrice: 0,
      successAjax: false,

      selectedRadioButtons: [],

      buildRendering: function () {
        this.templateString = this.renderTmpl(this.tmpl, this);
        delete this._templateCache[this.templateString];
        this.inherited(arguments);
      },

      postCreate: function () {
        this.controller = dijit.registry.byId("controllerWidget");
        this.radioButtons = query('input[type=radio]', this.domNode);
        this.initBookflowMessaging();
        this.attachEvents();
        this.handleRadioButtons();
        this.inherited(arguments);
      },

      attachEvents: function () {

        _.each(this.radioButtons, lang.hitch(this, function (radioButton) {
          on(radioButton, 'click', lang.hitch(this, this.handleRadioButtons));
        }));

        on(this.addButton, 'click', lang.hitch(this, this.handleAddButton));
      },

      handleRadioButtons: function () {

        this.totalPrice = 0;
        this.selectedRadioButtons = [];
        _.each(this.radioButtons, lang.hitch(this, function (radioButton) {
        	if(dojo.byId(radioButton.parentElement.id)){
        		dojo.byId(radioButton.parentElement.id).className= 'custom-radio';
        	}

          if (radioButton.checked) {
        	  if(dojo.byId(radioButton.parentElement.id)){
        		  dojo.byId(radioButton.parentElement.id).className= 'custom-radio selected';
        	  }
            this.totalPrice = this.totalPrice + parseInt(radioButton.value, 10);
            this.selectedRadioButtons.push(radioButton);
          }
        }));

        domAttr.set(this.totalCalculatedPrice, "innerHTML", dojoConfig.currency + this.totalPrice.toFixed(2));
      },

      handleAddButton: function () {
        var responseObj = [],
          url = this.url,
           requestData= {};
        var errorMgs = dom.byId("error-mgs");
        _.each(this.selectedRadioButtons, lang.hitch(this, function (radioButton) {
          var passengerId = domAttr.get(radioButton, "name").replace("creche", ""),
            code = domAttr.get(radioButton, "id").replace("creche", ""),
            index=code.indexOf("|"),
            extraCode = code.slice(0, index),
            quantity = 0,
            selected = true;

          if (extraCode === "None") {
          	 extraCode="Non";
              selected = false;
            }
          responseObj.push({
            "passengerId": passengerId,
            "extraCode": extraCode,
            "quantity": quantity,
            "selected": selected,
            "extraCategoryCode":this.crecheOptionsData.extraFacilityCategoryCode
          });
        }));
        for(var i=0; i<responseObj.length ; i++){

          	 if(responseObj[i].selected){
          		 this.successAjax = true;
          		 break;

          	 }else{
          		 this.successAjax = false;
          		 continue;
          	 }


           }

        	if( this.jsonData.summaryViewData.currentPage == 'cruiseoptions'){

        		var jsonReqString = this.bookflowMessaging[dojoConfig.site].PrebookInfantEquipment.stageSchoolRef.crechStageSchool;
        	}else{
        		var jsonReqString = this.bookflowMessaging[dojoConfig.site].PrebookInfantEquipment.crechAndStage;
        	}

        	   if(this.successAjax){
		            requestData[jsonReqString] =jsonUtil.toJson(responseObj);
		            this.controller.generateRequest("creche", url, requestData);
		            this.overlayRef.close();
        	   }
        	   else{
        		    domClass.remove(errorMgs, "disNone");
          			domAttr.set(errorMgs, "innerHTML", "Please select the child who will be taking part");

        	   }

      }

    });
});