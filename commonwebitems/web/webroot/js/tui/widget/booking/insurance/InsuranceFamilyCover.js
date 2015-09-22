define(["dojo/_base/declare",
  "dojo",
  // Parent classes
  "tui/widget/_TuiBaseWidget",
  "tui/widget/mixins/Templatable",
  "dojox/dtl/_Templated",
  // General application modules
  "dojo/_base/lang",
  "dojo/on",
  "dojo/dom-class",
  "dojo/dom-style",
  "dojo/dom",
  "dojo/query",
  "dojo/dom-construct",
  "dojo/_base/connect",
  "dojo/text!tui/widget/booking/insurance/view/templates/InsuranceFamilyCover.html",
  "tui/widget/booking/insurance/InsuranceModel",
  "tui/widget/booking/insurance/InsuranceIndAdd"

],

  function (declare, dojo, _WidgetBase, Templatable, _Templated, lang, on, domClass,domStyle, dom, query, domConstruct, connect, insuranceFamilyCoverTmpl,insuranceModel) {

    return declare("tui.widget.booking.insurance.InsuranceFamilyCover", [_WidgetBase, _Templated, Templatable], {

      /** Widget template HTML string */


      tmpl: insuranceFamilyCoverTmpl,
      templateString: "",
      widgetsInTemplate: true,
      resultDomNode: null,
      selectedFlag:null,
      insStaticTooltipData:null,
      insuranceRestrictionsToolTip:null,
      addExcessFlag:null,
      ewflag:null,


      postMixInProperties: function () {
        var insuranceFamilyCover = this
        insuranceFamilyCover.adtPrice = this.adtPrice;
        insuranceFamilyCover.chdPrice = this.chdPrice;
        insuranceFamilyCover.familyTitle = this.title;
        insuranceFamilyCover.insViewObject = this.insViewObject;
        insuranceFamilyCover.code = this.code;
        insuranceFamilyCover.resultDomNode = this.resultDomNode;
        insuranceFamilyCover.selectedFlag = this.selectedFlag;
        insuranceFamilyCover.addExcessFlag=this.addExcessFlag;
        insuranceFamilyCover.ewflag=this.ewflag;
        insuranceFamilyCover.insStaticTooltipData = this.insStaticTooltipData;
        insuranceFamilyCover.insuranceRestrictionsToolTip = this.insuranceRestrictionsToolTip;
	 insuranceFamilyCover.buttonRefNode= this.familygq
	insuranceFamilyCover.jsonData = this.jsonData
        this.inherited(arguments);
      },

      buildRendering: function () {
        this.templateString = this.renderTmpl(this.tmpl, this);

        delete this._templateCache[this.templateString];
        this.inherited(arguments);
      },

      postCreate: function () {
        var insuranceFamilyCover = this;
	 insuranceFamilyCover.addedIndBtnFunction();
	 if(this.familygq){
           on(this.familygq, "click", lang.hitch(insuranceFamilyCover, insuranceFamilyCover.getQuote,insuranceFamilyCover.code));
	 }

         this.inherited(arguments);
         this.tagElements(query('button.button', this.domNode),this.title);
      },

      addedIndBtnFunction: function(){
	var insuranceFamilyCover = this;
		if(insuranceFamilyCover .selectedFlag){
		domStyle.set(query('.getQuoteBtn', insuranceFamilyCover.domNode)[0], "display", "none");
 		domStyle.set(query('.addedIndBtn', insuranceFamilyCover.domNode)[0], "display", "block");
		//domStyle.set(query(".price-containerInd", insuranceFamilyCover.domNode)[0], "display", "none");
		 _.each(insuranceFamilyCover.jsonData.insuranceContainerViewData.insViewData, lang.hitch(this, function (item) {
			if(item.selected){
				var insuranceSummary = new tui.widget.booking.insurance.InsuranceSummary({
                           		overlayObj: null,
                           		resultDomNode: this.resultDomNode,
                           		description: item.description,
                           		paxComposition: item.totalPrice,
                           		pax:item.paxComposition,
                           		price: item.totalPrice,
                           		famCode: item.code,
                           		buttonRefNode3: null,
                           		buttonRefNode2:null,
                           		FrmPrice2:item.frmPrice,
                           		currAppPrice2: item.excessWaiverViewData.currencyAppendedPrice,
                           		addexcessPrice2: item.excessWaiverViewData.price,
                           		indAddObject: this.jsonData.insuranceContainerViewData,
                           		addAppPrice2: item.frmPrice,
                           		addExcessFlag:item.excessWaiverViewData.selected,
                           		ewflag:item.excessWaiverViewData.selected,
                           		initialflag:item.familyInsPresent,
                           		selectedFlag:item.selected,
                           		coveredAllPassenger:this.jsonData.insuranceContainerViewData.coveredAllPassenger,
                           		jsonData:this.jsonData
 				});
			}
		}));
		}else{
		 domStyle.set(query('.getQuoteBtn', insuranceFamilyCover.domNode)[0], "display", "block");
 		 domStyle.set(query('.addedIndBtn', insuranceFamilyCover.domNode)[0], "display", "none");
		 //domStyle.set(query(".price-containerInd", insuranceFamilyCover.domNode)[0], "display", "block");

		}

     },

      getQuote: function (code) {
    	  var insuranceFamilyCover = this;
        dojo.query("#insuranceInformation").style({
          "display": "none"
        });
        dojo.query(".modal").style({
          "display": "none"
        });
        _.each(insuranceModel.insuranceJsonData.insuranceContainerViewData.insViewData, lang.hitch(this, function (item) {
       	if(!item.familyInsPresent){
        var insuranceIndAdd = new tui.widget.booking.insurance.InsuranceIndAdd({
          indAddObject: insuranceModel.insuranceJsonData.insuranceContainerViewData,
          familyCode: code,
          resultDomNode: this.resultDomNode,
          buttonRefNode: this.familygq,
          addExcessFlag:insuranceFamilyCover.addExcessFlag,
          ewflag:item.excessWaiverViewData.selected,
          insStaticTooltipData:this.insStaticTooltipData,
          jsonData:this.jsonData
        });
    	}
        }));
      }
    });
  });
