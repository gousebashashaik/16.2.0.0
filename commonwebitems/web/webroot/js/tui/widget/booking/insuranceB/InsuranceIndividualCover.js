define(["dojo/_base/declare",
  // Parent classes
  "tui/widget/_TuiBaseWidget",
  "tui/widget/mixins/Templatable",
  "dojox/dtl/_Templated",
  "dojo",
  "dojo/_base/lang",
  "dojo/dom-construct",
  'dojo/query',
  'dojo/dom',
  'dojo/on',
  "dojo/dom-class",
  "dojo/topic",
  "dojo/dom-style",
  "tui/widget/booking/insuranceB/InsuranceInformationOverlay",
  "dojo/text!tui/widget/booking/insuranceB/view/templates/InsuranceIndividualCover.html",
  "tui/widget/booking/insuranceB/InsuranceModel",
  "dojox/dtl",
  "dojox/dtl/Context",
  "tui/widget/booking/insuranceB/InsuranceFamilyCover",
  "tui/widget/booking/insuranceB/InsuranceFamilyAdd"
  ],
  function (declare, _WidgetBase, Templatable, dtlTemplated, dojo, lang, domConstruct, query, dom, on,domClass, topic,domStyle, insuranceInformationOverlay, individualDetailsTmpl,insuranceModel) {
    return declare("tui.widget.booking.insuranceB.InsuranceIndividualCover",
      [_WidgetBase, dtlTemplated, Templatable], {

        tmpl: individualDetailsTmpl,
        templateString: "",
        widgetsInTemplate: true,
        resultDomNode: null,
        FrmPrice: null,
        currAppPrice: null,
        addexcessPrice: null,
        addAppPrice: null,
        indivtitle:null,
        selectedFlag:null,
        insStaticTooltipData:null,
        insuranceRestrictionsToolTip:null,

        postMixInProperties: function () {
          var insuranceIndividualCover = this;
          insuranceIndividualCover.individualPrice = this.price;
          insuranceIndividualCover.indivtitle = this.title;
          insuranceIndividualCover.insViewObject = this.insViewObject;
          insuranceIndividualCover.code = this.code;
          insuranceIndividualCover.resultDomNode = this.resultDomNode;
          insuranceIndividualCover.FrmPrice = this.curFrmPrice;
          insuranceIndividualCover.currAppPrice = this.currAppPrice;
          insuranceIndividualCover.addexcessPrice = this.addexcessPrice;
          insuranceIndividualCover.addAppPrice = this.addAppPrice;
          insuranceIndividualCover.selectedFlag = this.selectedFlag;
          insuranceIndividualCover.ewflag=this.ewflag;
          insuranceIndividualCover.insStaticTooltipData = this.insStaticTooltipData;
          insuranceIndividualCover.insuranceRestrictionsToolTip = this.insuranceRestrictionsToolTip;
	   insuranceIndividualCover.jsonData = this.jsonData;
          //this.inherited(arguments);
        },

        buildRendering: function () {
          this.templateString = this.renderTmpl(this.tmpl, this);

          delete this._templateCache[this.templateString];
          this.inherited(arguments);
        },

        postCreate: function () {
          var insuranceIndividualCover = this
          console.log(insuranceIndividualCover.insViewObject)
	 insuranceIndividualCover.addedFamBtnFunction();
	  if(insuranceIndividualCover.familygq1){
          on(insuranceIndividualCover.familygq1, "click", lang.hitch(insuranceIndividualCover,
            insuranceIndividualCover.listenEvents, insuranceIndividualCover.insViewObject,
            insuranceIndividualCover.code));
	   }
          console.log(insuranceIndividualCover.selectedFlag,"insuranceIndividualCover.indivtitle");

          //if(insuranceIndividualCover.selectedFlag){
        	 // query(".price-containerFam").style("display", "none");

        	  //dojo.style(this.pricecontainerFam, "display", "none")
          	   //domClass.add(insuranceIndividualCover.familygq1, "buttonAdded");
          	//dojo.style(this.spangq1, "display", "block");
          	//dojo.style(this.maskButton1, "display", "block");
          	//insuranceIndividualCover.familygq1.textContent = "Added";
          //} else{
        	//  query(".price-containerFam").style("display", "block");
        	 // dojo.style(this.pricecontainerFam, "display", "block")
        	//	dojo.style(this.spangq1, "display", "none");
        	//	dojo.style(this.maskButton1, "display", "none");
          //}

        //  this.inherited(arguments);
          this.tagElements(query('button.button', this.domNode),this.title);

          topic.subscribe("tui/widget/booking/insurnaceB/InsuranceIndividualCover/disabledFam", function(){
        	  domClass.add("fam-ins-block","famBlock");
          });
          topic.subscribe("tui/widget/booking/insurnaceB/InsuranceIndividualCover/enableFam", function(){
        	  domClass.remove("fam-ins-block","famBlock");
          });

        },

      addedFamBtnFunction: function(){
	var insuranceFamilyCover = this;
		if(insuranceFamilyCover .selectedFlag){
		domStyle.set(query('.getQuoteFamBtn', insuranceFamilyCover.domNode)[0], "display", "none");
 		domStyle.set(query('.addedFamBtn', insuranceFamilyCover.domNode)[0], "display", "block");
		//domStyle.set(query(".price-containerFam", insuranceFamilyCover.domNode)[0], "display", "none");
		 _.each(insuranceFamilyCover.jsonData.insuranceContainerViewData.insViewData, lang.hitch(this, function (item) {
			if(item.selected){
				var insuranceSummary = new tui.widget.booking.insuranceB.InsuranceSummary({
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
		 domStyle.set(query('.getQuoteFamBtn', insuranceFamilyCover.domNode)[0], "display", "block");
 		 domStyle.set(query('.addedFamBtn', insuranceFamilyCover.domNode)[0], "display", "none");
		 //domStyle.set(query(".price-containerFam", insuranceFamilyCover.domNode)[0], "display", "block");

		}

     },

        listenEvents: function (insViewObject, individualCode, overlayDom) {
          var insuranceIndividualCover = this
          dojo.query("#insuranceInformation").style({
            "display": "none"

          });
          dojo.query(".modal").style({
            "display": "none"

          });
          _.each(insuranceModel.insuranceJsonData.insuranceContainerViewData.insViewData, lang.hitch(this, function (item) {
           if(item.familyInsPresent){
          var insuranceFamilyAdd = new tui.widget.booking.insuranceB.InsuranceFamilyAdd({
            indAddObject: insuranceModel.insuranceJsonData.insuranceContainerViewData,
            code: individualCode,
            resultDomNode: insuranceIndividualCover.resultDomNode,
            curFrmPrice: this.FrmPrice,
            currAppPrice: this.currAppPrice,
            addexcessPrice: this.addexcessPrice,
            addAppPrice: this.addAppPrice,
            buttonRefNode: this.familygq1,
            ewflag:item.excessWaiverViewData.selected,
            indivtitle:this.indivtitle1,
            insStaticTooltipData:this.insStaticTooltipData,
            jsonData:this.jsonData
          });
     		}
          }));
        }
      });
  });