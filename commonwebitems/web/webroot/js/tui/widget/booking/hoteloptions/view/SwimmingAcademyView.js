define("tui/widget/booking/hoteloptions/view/SwimmingAcademyView", [
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
  "dojo/text!tui/widget/booking/hoteloptions/view/templates/SwimmingAcademyView.html",
  "tui/widget/booking/constants/BookflowUrl",
  "dojo/on",
  "dojo/_base/json",
  "tui/widget/form/SelectOption"


], function (declare, query, dom, domAttr, domConstruct, domClass, Evented, topic, lang, domStyle, _TuiBaseWidget,
             dtlTemplate, Templatable, SwimmingAcademyView,BookflowUrl, on, jsonUtil) {

  return declare('tui.widget.booking.hoteloptions.view.SwimmingAcademyView',
    [_TuiBaseWidget, dtlTemplate, Templatable, Evented], {

      tmpl: SwimmingAcademyView,
      templateString: "",
      widgetsInTemplate: true,
      extrasMap: {},
      totalPrice: 0,
      selectedRadioButtons: [],
      successAjax: false,

    buildRendering: function () {
      this.templateString = this.renderTmpl(this.tmpl, this);
      delete this._templateCache[this.templateString];
      this.inherited(arguments);
    },

    postCreate: function () {
      this.controller = dijit.registry.byId("controllerWidget");
      this.radioButtons = query('input[type=checkbox]', this.domNode);
      this.attachEvents();
      this.handleRadioButtons();
      this.inherited(arguments);
    },

    attachEvents: function () {
      for (var index = 0; index < this.swimOptionsData.extrasToPassengerMapping.length; index++) {
        var swimAcademy = this.swimOptionsData.extrasToPassengerMapping[index];
        if (swimAcademy.passenger.age > 2 && swimAcademy.passenger.age < 17) {
          if (!swimAcademy.passenger.isStageSelected) {
        _.each(this.radioButtons, lang.hitch(this, function (radioButton) {
          on(radioButton, 'click', lang.hitch(this, this.handleRadioButtons));
        }));
          }
            }
          }

        on(this.addButton, 'click', lang.hitch(this, this.handleAddButton));
    },

      handleRadioButtons: function () {

      this.totalPrice = 0;
          this.selectedRadioButtons = [];
          _.each(this.radioButtons, lang.hitch(this, function (radioButton) {
            if (radioButton.checked) {
              this.totalPrice = this.totalPrice + parseInt(radioButton.value, 10);
              this.selectedRadioButtons.push(radioButton);
      }
          }));

      domAttr.set(this.totalCalculatedPrice, "innerHTML", dojoConfig.currency + this.totalPrice.toFixed(2));

    },

    handleAddButton: function () {
      var responseObj = [],
        url =  BookflowUrl.swimacademyurl;
      var widget = this;
      var errorMgs = dom.byId("error-mgs_swim");
      _.each(this.radioButtons, lang.hitch(this, function (radioButton) {

    	  var quantity=0;
    	  var selected = false;
              var passengerId = domAttr.get(radioButton, "name").replace("swim", "");
               var code = domAttr.get(radioButton, "id").replace("swim", "");
                var extraCode = code.substr(0, code.length-2);
                if(radioButton.checked){
                quantity = 1;
                selected = true;
        }

              responseObj.push({
                "passengerId": passengerId,
                "extraCode": extraCode,
                "quantity": quantity,
                "selected": selected,
                "extraCategoryCode":this.swimOptionsData.extraFacilityCategoryCode
              });

      }));


     for(var i=0; i<responseObj.length ; i++){

    	 if(responseObj[i].quantity == 0){
    		 widget.successAjax = false;
    		 continue;
    	 }else{
    		 widget.successAjax = true;
    		 break;
    	 }


     }

      if(widget.successAjax){
      var requestData = {"swimExtra": jsonUtil.toJson(responseObj)};
      this.controller.generateRequest("swim", url, requestData);
         //value passed from component js
          widget.overlayRef.close();
      }else{
    	  	domClass.remove(errorMgs, "disNone");
			domAttr.set(errorMgs, "innerHTML", "Please select the child who will be taking part");
      }


    }
  });
});