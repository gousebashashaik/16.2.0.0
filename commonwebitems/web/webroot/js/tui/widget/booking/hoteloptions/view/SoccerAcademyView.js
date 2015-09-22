define("tui/widget/booking/hoteloptions/view/SoccerAcademyView", [
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
  "dojo/text!tui/widget/booking/hoteloptions/view/templates/SoccerAcademyView.html",
  "tui/widget/booking/constants/BookflowUrl",
  "dojo/on",
  "dojo/_base/json"

], function (declare, query, dom, domAttr, domConstruct, domClass, Evented, topic, lang, domStyle, _TuiBaseWidget,
             dtlTemplate, Templatable, SoccerAcademyView,BookflowUrl, on, jsonUtil) {

  return declare('tui.widget.booking.hoteloptions.view.SoccerAcademyView',
                    [_TuiBaseWidget, dtlTemplate, Templatable, Evented], {

      tmpl: SoccerAcademyView,
      templateString: "",
      widgetsInTemplate: true,
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
        this.radioButtons = query('input[type=radio]', this.domNode);
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
          if (radioButton.checked) {
            this.totalPrice = this.totalPrice + parseInt(radioButton.value, 10);
            this.selectedRadioButtons.push(radioButton);
          }
        }));

        domAttr.set(this.totalCalculatedPrice, "innerHTML", dojoConfig.currency + this.totalPrice.toFixed(2));

      },

      handleAddButton: function () {
        var responseObj = [],
          url =BookflowUrl.socceracademyurl ,
          requestData;
        var widget = this;
        var errorMgs = dom.byId("error-mgs-soccer");
        _.each(this.selectedRadioButtons, lang.hitch(this, function (radioButton) {
          var passengerId = domAttr.get(radioButton, "name").replace("soccer", ""),
            code = domAttr.get(radioButton, "id").replace("soccer", ""),
            extraCode = code.substr(0, code.length-2),
            quantity = 0,
            selected = true;
          if (extraCode === "None") {
        	  extraCode = "Non";
             selected = false;
          }
          responseObj.push({
            "passengerId": passengerId,
            "extraCode": extraCode,
            "quantity": quantity,
            "selected": selected,
            "extraCategoryCode":this.soccerOptionsData.extraFacilityCategoryCode
          });
        }));

        for(var i=0; i<responseObj.length ; i++){

       	 if(responseObj[i].selected){
       		 widget.successAjax = true;
       		 break;

       	 }else{
       		 widget.successAjax = false;
       		 continue;
       	 }


        }

         if(widget.successAjax){
        requestData = {"soccerExtra": jsonUtil.toJson(responseObj)};
        this.controller.generateRequest("soccer", url, requestData);
            //value passed from component js
             widget.overlayRef.close();
         }else{
       	  	domClass.remove(errorMgs, "disNone");
   			domAttr.set(errorMgs, "innerHTML", "Please select the child who will be taking part");
         }


      }
    });
});