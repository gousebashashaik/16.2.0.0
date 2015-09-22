define("tui/widget/booking/passengers/view/DiscountCodeView", [
  "dojo",
  "dojo/query",
  "dojo/dom-class",
  "dojo/_base/xhr",
  "dojo/dom",
  "dojo/dom-style",
  "dojo/dom-attr",
  "dojo/on",
  "dojo/dom-construct",
  "dojo/_base/lang",
  "dojo/text!tui/widget/booking/passengers/view/templates/DiscountView.html",
  "tui/widget/booking/constants/BookflowUrl",
  "tui/widget/_TuiBaseWidget",
  "dojox/dtl/_Templated",
  "tui/widget/mixins/Templatable",
  "dojo/Evented",
  "tui/widget/popup/Tooltips",
  "tui/widget/expand/Expandable",
  "tui/widget/form/ValidationTextBox",
  "tui/widget/booking/passengers/view/DiscountTextBoxValidation"
],

    function (dojo, query, domClass, xhr, dom, domStyle, domAttr, on, domConstruct, lang,
              DiscountViewTmpl, BookflowUrl, _TuiBaseWidget, dtlTemplate, Templatable, Evented) {
      return dojo.declare('tui.widget.booking.passengers.view.DiscountCodeView',
          [_TuiBaseWidget, dtlTemplate, Templatable, Evented], {

            TARGET_URL: BookflowUrl.passengerdetailsurl,
            CHANGE_URL : BookflowUrl.promoCodeRemoveUrl,
            tmpl: DiscountViewTmpl,
            templateString: "",
            widgetsInTemplate: true,
            promotional: null,
            applicable: null,
            controller : null,
            emptyInvalidString: true,
            SUCCESS_MSG : "A discount of &pound;<span class='dis-rate'>{0}</span> has been applied to your holiday. The total price is now &pound;<span class='dis-rate'>{1}</span>",
            SUCCESS_MSG_FALCON : "A discount of &euro;<span class='dis-rate'>{0}</span> has been applied to your holiday. The total price is now &euro;<span class='dis-rate'>{1}</span>",
            SUCCESS_MSG_FLIGHTS_ONLY : "A discount of &pound;<span class='dis-rate'>{0}</span> has been applied to your flight. The total price is now &pound;<span class='dis-rate'>{1}</span>",
            buildRendering: function () {
              this.templateString = this.renderTmpl(this.tmpl, this);
              this.inherited(arguments);
            },

            postCreate: function () {
              this.controller = dijit.registry.byId("controllerWidget");
              this.registerEvents();
              domStyle.set(this.successMsgBlock, "display", "none");
               /*this.discountTextBox.validator = lang.hitch(this, function () {
                var value =  this.discountTextBox.get("value");

                console.log('...'+value);

                if(this.emptyInvalidString){
                  return false;
                }else {
                  return true;
                }

              });*/

            },

            registerEvents: function () {
              on(this.discountButton, "click", lang.hitch(this, this.submitEvent));
              on(this.changeButton, "click", lang.hitch(this, this.changeEvent));
              on(this.discountTextBox, "change", lang.hitch(this, function(){
                this.emptyInvalidString = false;
                this.discountTextBox.validate();
              }));
              on(this.discountTextBox, "blur", lang.hitch(this, function(){
                domClass.add(this.discountTextBox.innerErrorBox.parentElement, "dijitValidationContainer");
				domClass.add(query('.psgr-textfield')[0], "dijitNoTextBoxError");
                domStyle.set(query('.error-notation')[0], "display", "none");
              }));
            },

            afterFailure: function () {
              domClass.remove(this.domNode, "updating");
            },

            afterSuccess: function (response) {
              domClass.remove(this.domNode, "updating");
              this.controller.publishToViews("extraoptions", response);
              if (response.promotionalCodeViewData.applicable) {
                this.emptyInvalidString = false;
                var successmsg;
                if(dojoConfig.site === 'falcon'){
                	successmsg = this.SUCCESS_MSG_FALCON.replace("{0}",response.promotionalCodeViewData.promotionalDiscount.toFixed(2));
                	successmsg = successmsg.replace("{1}",response.promotionalCodeViewData.totalPrice.toFixed(2));
                }
                else if(dojoConfig.site == 'flights'){
                	successmsg = this.SUCCESS_MSG_FLIGHTS_ONLY.replace("{0}",response.promotionalCodeViewData.promotionalDiscount.toFixed(2));
                	successmsg = successmsg.replace("{1}",response.promotionalCodeViewData.totalPrice.toFixed(2));
                }
                else{
                	successmsg = this.SUCCESS_MSG.replace("{0}",response.promotionalCodeViewData.promotionalDiscount.toFixed(2));
                	successmsg = successmsg.replace("{1}",response.promotionalCodeViewData.totalPrice.toFixed(2));
                }

                successmsg = successmsg.replace("{1}",response.promotionalCodeViewData.totalPrice.toFixed(2));
                domStyle.set(this.discountBlock, "display", "none");
                domStyle.set(this.successMsgBlock, "display", "block");
                domAttr.set(this.successMsgDiv, "innerHTML",successmsg);
              } else {
                this.emptyInvalidString = true;
                this.discountTextBox.validate();
                this.discountTextBox.displayMessage(response.promotionalCodeViewData.promotionalCodeFailueMessage);
              }
            },

            submitEvent: function () {
              var requestParam = this.discountTextBox.get("value");
              if (this.discountTextBox.isValid() && requestParam != "") {
            	  this.emptyInvalidString = true;
            	  domClass.add(this.domNode, "updating");
            	  var results = xhr.get({
                  url: this.TARGET_URL,
                  content: {"promocode": requestParam},
                  handleAs: "json",
                  preventCache: true,
                  error: lang.hitch(this, function (err) {
                    this.afterFailure();
                  }) ,
                  load:  lang.hitch(this,function(response){
                    this.afterSuccess(response);
                  })
                });
              } else if(requestParam == ""){
            	  this.emptyInvalidString= false;
            	  this.discountTextBox.validate();
            	  this.discountTextBox.displayMessage(this.discountTextBox.missingMessage);
              }else{
            	  this.emptyInvalidString= false;
              }
            },

            changeEvent : function() {

              var requestParam = this.discountTextBox.get("value");
              if (requestParam != "") {
                this.emptyInvalidString = true;
                domClass.add(this.domNode, "mask-interactivity");
                var results = xhr.get({
                  url: this.CHANGE_URL,
                  content: {},
                  handleAs: "json",
                  preventCache: true,
                  error: lang.hitch(this, function (err) {
                    this.afterFailure();
                  }) ,
                  load:  lang.hitch(this,function(response){
                	this.emptyInvalidString= false;
                    this.controller.publishToViews("extraoptions", response);
                    this.discountTextBox.set("value","");
                    domStyle.set(this.discountBlock, "display", "block");
                    domStyle.set(this.successMsgBlock, "display", "none");
                    domClass.remove(this.domNode, "mask-interactivity")
                  })
                });
              } else {
                this.emptyInvalidString= false;
                this.discountTextBox.validate();
                this.discountTextBox.displayMessage(this.discountTextBox.missingMessage);
              }
            }

    })

});