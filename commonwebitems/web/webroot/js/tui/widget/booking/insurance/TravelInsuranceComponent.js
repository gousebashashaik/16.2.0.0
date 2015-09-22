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
  "dojo/dom-style",
  "dojo/text!tui/widget/booking/insurance/view/templates/TravelInsuranceComponent.html",
  "dojo/Evented",
  "tui/widget/booking/insurance/InsuranceInformationOverlay",
  "dojo/parser",
  "dojo/_base/array",
  "tui/widget/booking/insurance/InsuranceModel",
  "tui/widget/booking/insurance/InsuranceIndividualCover",
  "tui/widget/booking/insurance/InsuranceFamilyCover",
  "tui/widget/booking/insurance/InsuranceSummary",
  "tui/widget/booking/bookflowMsgs/nls/Bookflowi18nable",
  "tui/widget/expand/Expandable","tui/widget/popup/Tooltips"],
  function (declare, _TuiBaseWidget, Templatable, dtlTemplatable, dojo, lang, domConstruct, query,
		  dom, on,domClass,domStyle, travelInsuranceComponentTmpl, Evented, insuranceInformationOverlay,
		  parser, array,InsuranceModel,InsuranceIndividualCover,InsuranceFamilyCover,InsuranceSummary,Bookflowi18nable) {


    return declare("tui.widget.booking.insurance.TravelInsuranceComponent", [_TuiBaseWidget, dtlTemplatable, Templatable,Bookflowi18nable], {

      tmpl: travelInsuranceComponentTmpl,
      templateString: "",
      widgetsInTemplate: true,
      familyflag: null,
      indivflag: null,
      indivPrice: null,
      familyAdtPrice: null,
      familyChdPrice: null,
      familyTitle: null,
      indivTitle: null,
      insuranceLength: null,
      code: null,
      resultDomNode: null,
      currFrmPrice: null,
      currencyAppPrice: null,
      excessWaiverPrice: null,
      addPrice: null,
      insViewRootData:null,
      insStaticTooltipData:null,
      selectedFlag:null,
      clickEvents:[],
      insurance_ContainerViewData:null,
      insuranceRestrictionsToolTip:null,
      coveredAllPassenger:null,
      ewFlag:null,
      currency:"",
      policyDetailsLink:"",


      postMixInProperties: function () {
        var trInComponent = this;
        this.inherited(arguments);
        this.initBookflowMessaging();
        trInComponent.resultDomNode = this.resultDomNode;
        this.currency=dojoConfig.currency;
       this.policyDetailsLink= this.bookflowMessaging[dojoConfig.site].insurancepolicyDetailsurl;
      },

      buildRendering: function () {
        this.templateString = this.renderTmpl(this.tmpl, this);
        delete this._templateCache[this.templateString];
        this.inherited(arguments);
      },

      removeallevents : function(){
    	  this.clickEvents = [];
      },

      postCreate: function () {
        var trInComponent = this;
        console.log("insurance");
        console.log(trInComponent.jsonData);
        InsuranceModel.insuranceJsonData = trInComponent.jsonData;
        dojo.subscribe("tui/booking/insurance", function(){
        	trInComponent.removeallevents();
 	    });


        var flag = "";
        trInComponent.inherited(arguments);
        trInComponent.resultDomNode = this.resultDomNode;
        trInComponent.insuranceLength = this.jsonData.insuranceContainerViewData.insViewData.length;
        trInComponent.insViewRootData = this.jsonData.insuranceContainerViewData;
        trInComponent.insStaticTooltipData = this.jsonData.extraOptionsStaticContentViewData.insuranceStaticContentViewData.insuranceContentMap.Insurance_ExcessWaiver_ToolTip;
        trInComponent.insuranceRestrictionsToolTip = this.jsonData.extraOptionsStaticContentViewData.insuranceStaticContentViewData.insuranceContentMap.Insurance_Restrictions_ToolTip;

		var changedArray = array.map(trInComponent.insViewRootData.insViewData, function (item) {
          if (item.familyInsPresent) {
            trInComponent.flag = "FMLY";
            trInComponent.familyflag = true;
          } else {
            trInComponent.indivflag = true;
            trInComponent.flag = "PERS";
          }

          if (trInComponent.flag == "FMLY") {
            trInComponent.indivPrice = item.frmPrice;
            trInComponent.familyTitle = item.description;
            trInComponent.code = item.code;
            trInComponent.currFrmPrice = item.frmPrice;
            trInComponent.currencyAppPrice = item.excessWaiverViewData.currencyAppendedPrice;
            trInComponent.excessWaiverPrice = item.excessWaiverViewData.price;
            trInComponent.addPrice = item.frmPrice;
            trInComponent.selectedFlag = item.selected;

            var individualObject = new tui.widget.booking.insurance.InsuranceIndividualCover({
              price: item.frmPrice,
              title: item.description,
              insViewObject: trInComponent.insViewRootData,
              code: item.code,
              resultDomNode: trInComponent.resultDomNode,
              curFrmPrice: trInComponent.currFrmPrice,
              currAppPrice: trInComponent.currencyAppPrice,
              addexcessPrice: trInComponent.excessWaiverPrice,
              addAppPrice: trInComponent.addPrice,
              selectedFlag:trInComponent.selectedFlag,
              addExcessFlag:item.excessWaiverViewData.selected,
              ewflag:item.excessWaiverViewData.selected,
              insStaticTooltipData:trInComponent.insStaticTooltipData,
              insuranceRestrictionsToolTip:InsuranceModel.insuranceJsonData.extraOptionsStaticContentViewData.insuranceStaticContentViewData.insuranceContentMap.Insurance_Restrictions_ToolTip_FA,
              jsonData:this.jsonData
            })
            domConstruct.place(individualObject.domNode, "individual", "only");
          }

          if (trInComponent.flag == "PERS") {
            trInComponent.familyAdtPrice = item.frmAdtPrice;
            trInComponent.familyChdPrice = item.frmChdPrice;
            trInComponent.indivTitle = item.description;
            trInComponent.selectedFlag = item.selected;

            var familyObject = new tui.widget.booking.insurance.InsuranceFamilyCover({
              adtPrice: item.frmAdtPrice,
              chdPrice: item.frmChdPrice,
              title: item.description,
              insViewObject: trInComponent.insViewRootData,
              code: item.code,
              resultDomNode: trInComponent.resultDomNode,
              selectedFlag:trInComponent.selectedFlag,
              addExcessFlag:item.excessWaiverViewData.selected,
              ewflag:item.excessWaiverViewData.selected,
              insStaticTooltipData:trInComponent.insStaticTooltipData,
              insuranceRestrictionsToolTip:InsuranceModel.insuranceJsonData.extraOptionsStaticContentViewData.insuranceStaticContentViewData.insuranceContentMap.Insurance_Restrictions_ToolTip_IN,
              jsonData:this.jsonData

            })
            domConstruct.place(familyObject.domNode, "family", "only");
          }
        });

        trInComponent.listenEvents(  trInComponent.familyflag, trInComponent.indivflag,
                                       trInComponent.indivPrice, trInComponent.familyAdtPrice,
                                       trInComponent.familyChdPrice, trInComponent.familyTitle,
                                       trInComponent.indivTitle,trInComponent.jsonData.insuranceContainerViewData,
                                       trInComponent.code );

      },

      refresh: function (field, response) {
    	  var trInComponent = this;
    	  trInComponent.jsonData = response;
     },

     listenEvents: function (familyflag, indivflag, indivPrice, familyAdtPrice, familyChdPrice, familyTitle, indivTitle, insuranceContainerViewData, code) {
        var trInComponent = this;

		this.clickEvents.push(on(trInComponent.Seewhatcovered, "click",
          lang.hitch(trInComponent, trInComponent.whatCovered,
            familyflag, indivflag, indivPrice, familyAdtPrice,
            familyChdPrice, familyTitle, indivTitle,
            InsuranceModel.insuranceJsonData.insuranceContainerViewData, code)));

		if (familyflag) {
        	this.clickEvents.push(on(dojo.query("#indivi"), "click",
            lang.hitch(trInComponent, trInComponent.whatCovered,
            familyflag, indivflag, indivPrice, familyAdtPrice,
              familyChdPrice, familyTitle, indivTitle,
              insuranceContainerViewData, code)));
        }
        if (indivflag) {
        	this.clickEvents.push(on(dojo.query("#fmd"), "click",
            lang.hitch(trInComponent, trInComponent.whatCovered,
              familyflag, indivflag, indivPrice, familyAdtPrice,
              familyChdPrice, familyTitle, indivTitle,
              insuranceContainerViewData, code)));
        }


      },

      whatCovered: function (familyflag, indivflag, indivPrice, familyAdtPrice, familyChdPrice, familyTitle, indivTitle, insuranceContainerViewData, code) {

        var trInComponent = this;

        var insuranceInformationOverlay1 = new tui.widget.booking.insurance.InsuranceInformationOverlay({
        	widgetId: "insuranceInformation", modal: true});

       insuranceInformationOverlay1.open();


        var insViewRootData = InsuranceModel.insuranceJsonData.insuranceContainerViewData;
        //var  insStaticTooltipData = InsuranceModel.insuranceJsonData.extraOptionsStaticContentViewData.insuranceStaticContentViewData.insuranceContentMap.Insurance_ExcessWaiver_ToolTip
        var flag = "";
        domConstruct.empty("WhatCoveredfamily");
        domConstruct.empty("whatCoveredindividual");
	console.log(InsuranceModel.insuranceJsonData.insuranceContainerViewData.insViewData);
        var changedArray = array.map(InsuranceModel.insuranceJsonData.insuranceContainerViewData.insViewData, function (item) {
		console.log(item);
          if (item.familyInsPresent) {
            trInComponent.flag = "FMLY";
            trInComponent.familyflag = true;

          } else {
            trInComponent.indivflag = true;

            trInComponent.flag = "PERS";
          }

          if (trInComponent.flag == "FMLY") {

            trInComponent.indivPrice = item.frmPrice;
            trInComponent.familyTitle = item.description;
            trInComponent.code = item.code;
            trInComponent.currFrmPrice = item.frmPrice;
            trInComponent.currencyAppPrice = item.excessWaiverViewData.currencyAppendedPrice;
            trInComponent.excessWaiverPrice = item.excessWaiverViewData.price;
            trInComponent.addPrice = item.frmPrice;
            trInComponent.selectedFlag = item.selected;
            var individualObject = new tui.widget.booking.insurance.InsuranceIndividualCover({
            	 price: item.frmPrice,
                 title: item.description,
                 insViewObject: InsuranceModel.insuranceJsonData.insuranceContainerViewData,
                 code: item.code,
                 resultDomNode: trInComponent.resultDomNode,
                 curFrmPrice: trInComponent.currFrmPrice,
                 currAppPrice: trInComponent.currencyAppPrice,
                 addexcessPrice: trInComponent.excessWaiverPrice,
                 addAppPrice: trInComponent.addPrice,
                 selectedFlag:trInComponent.selectedFlag,
                 ewflag:item.excessWaiverViewData.selected,
                 insStaticTooltipData:InsuranceModel.insuranceJsonData.extraOptionsStaticContentViewData.insuranceStaticContentViewData.insuranceContentMap.Insurance_ExcessWaiver_ToolTip,
                 insuranceRestrictionsToolTip:InsuranceModel.insuranceJsonData.extraOptionsStaticContentViewData.insuranceStaticContentViewData.insuranceContentMap.Insurance_Restrictions_ToolTip_FA,
                 jsonData:this.jsonData
            })
            domConstruct.place(individualObject.domNode, "whatCoveredindividual", "only");
            query(".moreDetails").style("display","none");
		if(item.selected){
        	     domStyle.set(query('.getQuoteFamBtn', individualObject.domNode)[0], "display", "none");

        	     //domStyle.set(query(".price-containerFam", individualObject.domNode)[0], "display", "none");
 	     	     domStyle.set(query('.addedFamBtn', individualObject.domNode)[0], "display", "block");
		}else{
	            domStyle.set(query('.getQuoteFamBtn', individualObject.domNode)[0], "display", "block");
	            //domStyle.set(query(".price-containerFam", individualObject.domNode)[0], "display", "block");
 	     	     domStyle.set(query('.addedFamBtn', individualObject.domNode)[0], "display", "none");

		}

          }
          if (trInComponent.flag === "PERS") {

            trInComponent.familyAdtPrice = item.frmAdtPrice;
            trInComponent.familyChdPrice = item.frmChdPrice;
            trInComponent.indivTitle = item.description;
            trInComponent.selectedFlag = item.selected;

            /*	 var familyObject  = new tui.widget.booking.insurance.InsuranceFamilyCover({adtPrice:item.frmAdtPrice,
             chdPrice:item.frmChdPrice,title:item.description,insViewObject:insViewRootData,code:item.code})*/
            // domConstruct.place(familyObject.domNode, "WhatCoveredfamily","only");
            var familyObject = new tui.widget.booking.insurance.InsuranceFamilyCover({
            	adtPrice: item.frmAdtPrice,
                chdPrice: item.frmChdPrice,
                title: item.description,
                insViewObject: InsuranceModel.insuranceJsonData.insuranceContainerViewData,
                code: item.code,
                resultDomNode: trInComponent.resultDomNode,
                selectedFlag:trInComponent.selectedFlag,
                ewflag:item.excessWaiverViewData.selected,
                insStaticTooltipData:InsuranceModel.insuranceJsonData.extraOptionsStaticContentViewData.insuranceStaticContentViewData.insuranceContentMap.Insurance_ExcessWaiver_ToolTip,
                insuranceRestrictionsToolTip:InsuranceModel.insuranceJsonData.extraOptionsStaticContentViewData.insuranceStaticContentViewData.insuranceContentMap.Insurance_Restrictions_ToolTip_IN,
                jsonData:this.jsonData
            });
            domConstruct.place(familyObject.domNode, "WhatCoveredfamily", "only");
            query(".moreDetails").style("display","none");
		if(item.selected){
        	     domStyle.set(query('.getQuoteBtn', familyObject.domNode)[0], "display", "none");
        	     //domStyle.set(query(".price-containerInd", familyObject.domNode)[0], "display", "none");
 	     	     domStyle.set(query('.addedIndBtn', familyObject.domNode)[0], "display", "block");

		}else{
		     domStyle.set(query('.getQuoteBtn', familyObject.domNode)[0], "display", "block");
		   //domStyle.set(query(".price-containerInd", familyObject.domNode)[0], "display", "block");
 	     	     domStyle.set(query('.addedIndBtn', familyObject.domNode)[0], "display", "none");

		}

          }
        });
      }
    });
  });