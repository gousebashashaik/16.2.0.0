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
  "dojo/fx",
  "dojo/dom-class",
  "dojo/dom-attr",
  "dojo/dom-style",
  "dojo/topic",
  "tui/widget/booking/constants/BookflowUrl",
  "dojo/text!tui/widget/booking/insuranceB/view/templates/TravelInsuranceComponent.html",
  "dojo/Evented",
  "tui/widget/booking/insuranceB/InsuranceInformationOverlay",
  "dojo/parser",
  "dojo/_base/array",
  "tui/widget/booking/insuranceB/InsuranceModel",
  "dojo/_base/xhr", "dojox/dtl", "dojox/dtl/Context",
  "tui/widget/booking/insuranceB/InsuranceIndividualCover",
  "tui/widget/booking/insuranceB/InsuranceFamilyCover",
  "tui/widget/booking/insuranceB/InsuranceSummary",
  "tui/widget/expand/Expandable","tui/widget/popup/Tooltips"],
  function (declare, _TuiBaseWidget, Templatable, dtlTemplatable, dojo, lang, domConstruct, query,
		  dom, on,coreFx,domClass,domAttr,domStyle, topic,BookflowUrl,travelInsuranceComponentTmpl, Evented, insuranceInformationOverlay,
		  parser, array,InsuranceModel,xhr) {


    return declare("tui.widget.booking.insuranceB.TravelInsuranceComponent", [_TuiBaseWidget, dtlTemplatable, Templatable], {

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
      expanditemSelector:'famExpandviewBtn',
      itemSelector : "famCollapsedviewBtn",
      targetSelector:"famins_section",
      targetUrl: BookflowUrl.insuranceBaddurl,
      confirmationFlag: false,
      addExcessFlag: false,
      isFamilyPackageExist : false,
      excessWaiverFlag : true,


      postMixInProperties: function () {
        var trInComponent = this;
        this.inherited(arguments);
        trInComponent.resultDomNode = this.resultDomNode;
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

        var flag = ""
        trInComponent.inherited(arguments);
        trInComponent.resultDomNode = this.resultDomNode;
        trInComponent.insuranceLength = this.jsonData.insuranceContainerViewData.insViewData.length;
        trInComponent.insViewRootData = this.jsonData.insuranceContainerViewData;
        trInComponent.insStaticTooltipData = this.jsonData.extraOptionsStaticContentViewData.insuranceStaticContentViewData.insuranceContentMap.Insurance_ExcessWaiver_ToolTip;
        trInComponent.insuranceRestrictionsToolTip = this.jsonData.extraOptionsStaticContentViewData.insuranceStaticContentViewData.insuranceContentMap.Insurance_Restrictions_ToolTip;
        trInComponent.confirmValidationMsg = this.jsonData.extraOptionsStaticContentViewData.insuranceStaticContentViewData.insuranceContentMap.Extra_Insurance_PleaseConfirmValidationMsg;
        trInComponent.isFamilyPackageExist = trInComponent.insViewRootData.insViewData.length === 2 ? true : false;

        var changedArray = array.map(trInComponent.insViewRootData.insViewData, function (item) {
          if (item.familyInsPresent) {
            trInComponent.flag = "FMLY";
            trInComponent.familyflag = true;
          } else {
            trInComponent.indivflag = true;
            trInComponent.flag = "PERS";
          }

          /* Insurance B Start*/
    	  domClass.remove("fam-ins", "hide");
    	  domClass.remove("ind-ins", "individual-only");
    	  domClass.remove("ind-ins", "individual-family");
    	  domClass.remove("fam-ins", "individual-family");

          if(trInComponent.insuranceLength==1){
        	  domClass.add("fam-ins", "hide");
        	  domClass.add("ind-ins", "individual-only");
        	  domClass.add("famins_section", "disNone");
          }else{
        	  domClass.add("ind-ins", "individual-family");
        	  domClass.add("fam-ins", "family-only");

          }

          /* Insurance B End*/

          if (trInComponent.flag == "FMLY") {
            trInComponent.indivPrice = item.roundedFrmPrice;
            trInComponent.familyTitle = item.description;
            trInComponent.code = item.code;
            trInComponent.currFrmPrice = item.roundedFrmPrice;
            trInComponent.currencyAppPrice = item.excessWaiverViewData.currencyAppendedPrice;
            trInComponent.excessWaiverPrice = item.excessWaiverViewData.price;
            trInComponent.addPrice = item.roundedFrmPrice;
            trInComponent.selectedFlag = item.selected;
            trInComponent.currencyExcessWaiver = item.currencyAppendedInsWithExcessWaiver;
            trInComponent.famInsprice = item.roundedFrmPrice;
            console.log('..'+trInComponent.indivPrice);

            var individualObject = new tui.widget.booking.insuranceB.InsuranceIndividualCover({
              titleName:trInComponent.jsonData.extraOptionsContentViewData.extraContentViewData.contentMap.fam_ins_info_one_displayName,
              price: item.roundedFrmPrice,
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
            domConstruct.place(individualObject.domNode, "fam-ins", "only");

            /*Version-B Insu related code - start */
            trInComponent.showDetailsClicked();
            trInComponent.showFamilyInsDetails();
            /*Version-B insu related code - end */



          }

          if (trInComponent.flag == "PERS") {
            trInComponent.familyAdtPrice = item.frmAdtPrice;
            trInComponent.familyChdPrice = item.frmChdPrice;
            trInComponent.indivTitle = item.description;
            trInComponent.selectedFlag = item.selected;
            trInComponent.frmSeniorPrice = item.frmSeniorPrice;


            var familyObject = new tui.widget.booking.insuranceB.InsuranceFamilyCover({
              adtPrice: item.frmAdtPrice,
              chdPrice: item.frmChdPrice,
              senPrice: item.frmSeniorPrice,
              totalPrice : item.roundedTotalPrice,
              totalPriceWithEw: item.currencyAppendedInsWithExcessWaiver,
              totalPriceSummary: item.totalPrice,
              adultPrice : parseInt(item.frmAdtPrice.split(currency)[1]),
              childPrice: parseInt(item.frmChdPrice.split(currency)[1]),
              seniorPrice: parseInt(item.frmSeniorPrice.split(currency)[1]),
              superSeniorPrice : parseInt(item.frmSuperSeniorPrice.split(currency)[1]),
              title: item.description,
              insViewObject: trInComponent.insViewRootData,
              code: item.code,
              resultDomNode: trInComponent.resultDomNode,
              selectedFlag:trInComponent.selectedFlag,
              addExcessFlag:item.excessWaiverViewData.selected,
              excessWaiverPrice: parseFloat(item.excessWaiverViewData.excessWaiverprice),
              ewflag:item.excessWaiverViewData.selected,
              insStaticTooltipData:trInComponent.insStaticTooltipData,
              insuranceRestrictionsToolTip:InsuranceModel.insuranceJsonData.extraOptionsStaticContentViewData.insuranceStaticContentViewData.insuranceContentMap.Insurance_Restrictions_ToolTip_IN,
              jsonData:this.jsonData,
              currency:currency,
              selected: item.selected,
              familyPackagePresent : trInComponent.isFamilyPackageExist

            })
           domConstruct.place(familyObject.domNode, "ind-ins", "only");
          }
        });

        if(trInComponent.isFamilyPackageExist){
	        // Disabled Fam. insurance section if Ind. insurance added vise versa
	    	  if( dojo.style("addedBtn", "display")==="block" ){
	    		  domClass.add("ind-ins-block","indBlock");
	    	  }

	    	  if( dojo.style("addedIndBtn", "display")==="block" ){
	    		  domClass.add("fam-ins-block","famBlock");
	    	  }
        }


        trInComponent.listenEvents(  trInComponent.familyflag, trInComponent.indivflag,
                                       trInComponent.indivPrice, trInComponent.familyAdtPrice,
                                       trInComponent.familyChdPrice, trInComponent.familyTitle,
                                       trInComponent.indivTitle,trInComponent.jsonData.insuranceContainerViewData,
                                       trInComponent.code );




      },

      /*Version-B Insu related code - start */

      showDetailsClicked:function(){
    	  console.log('...clicked show details');
    	  var travelInsComponent = this;
    	  travelInsComponent.CollapsedView();
    	  //insuranceIndividualCover.individualPrice = this.price;
      },

      CollapsedView : function(){

    	  var travelInsComponent = this;
    	  var getFamilInsSection = dojo.byId(travelInsComponent.targetSelector);
    	  if(getFamilInsSection){
    		  domClass.add("famins_section", "disNone");
    		  domClass.add("expand-view", "disNone");

              if( dojo.style("addedBtn", "display")==="none" ){
            	//  domStyle.set(query('.updatedIns')[0], "display", "block");
              }else{
            	  domStyle.set(query('.updatedIns')[0], "display", "none");
              }

    	  }

    	  dom.byId(travelInsComponent.itemSelector).innerHTML = travelInsComponent.defaultTxt;
    	  dom.byId(travelInsComponent.expanditemSelector).innerHTML =travelInsComponent.expandTxt;

    	  travelInsComponent.toogleInsSection();
    	  //travelInsComponent.confirmationCheckFunction();
		  if(travelInsComponent.familygq1){
			  on(travelInsComponent.familygq1, "click", lang.hitch(travelInsComponent, travelInsComponent.addFamilyIns,false));

		  }
		  if(travelInsComponent.familyexcesAdd){
			  on(travelInsComponent.familyexcesAdd, "click", lang.hitch(travelInsComponent, travelInsComponent.addFamilyIns,true));

		  }
    	  on(this.familyconfirmation, "click", lang.hitch(travelInsComponent, travelInsComponent.confirmationCheckFunction, this.familyconfirmation));


      },

      confirmationCheckFunction: function (obj) {
          var InsuranceFamilyAdd = this;

          console.log('.check box checked..'+obj.checked);
          obj.blur();
          obj.focus();

          if (obj.checked) {
            InsuranceFamilyAdd.confirmationFlag = obj.checked;
            domClass.add("impCheckbox_Error", "disNone");
          } else {
            InsuranceFamilyAdd.confirmationFlag = obj.checked;
          }
      },


      // Displays added button with insurance summary and an option to remove cover
      showFamilyInsDetails : function() {

    	  var InsuranceFamilyAdd = this;

    	  if( dojo.style("addedBtn", "display")==="none" ){
          	//  domStyle.set(query('.updatedIns')[0], "display", "block");
    	  }else{

    		  dojo.forEach(InsuranceModel.insuranceJsonData.insuranceContainerViewData.insViewData,
    		            function (item, i) {
    		              console.log(InsuranceFamilyAdd.buttonRefNode2, 'item.selected');
    		              if (item.selected) {
    		                InsuranceFamilyAdd.description = item.description;
    		                InsuranceFamilyAdd.paxDetails = item.paxComposition;
    		                InsuranceFamilyAdd.price = item.totalPrice;
    		                InsuranceFamilyAdd.initialflag=item.selected;
    		                InsuranceFamilyAdd.ewflag=item.excessWaiverViewData.selected;
    		                InsuranceFamilyAdd.currencyAppened = item.excessWaiverViewData.currencyAppendedPrice;
    		                InsuranceFamilyAdd.individualPrice = item.roundedTotalPrice;
    		                InsuranceFamilyAdd.basicPrice = item.price;
    		                InsuranceFamilyAdd.storebasicPrice = item.roundedFrmPrice;

    		              }

    		            });
    		          console.log(InsuranceFamilyAdd.price+'.. total price get..');
    		          dom.byId("collapseFamPrice").innerHTML = InsuranceFamilyAdd.price;

    		  setTimeout(function(){
    		  topic.publish("tui/booking/insuranceB/disableAdequateInsuranceCheck");
    	        }, 1500);

    		  var insuranceSummary = new tui.widget.booking.insuranceB.InsuranceSummary({
        		  jsonData : this.jsonData,
              	titleName:InsuranceFamilyAdd.jsonData.extraOptionsContentViewData.extraContentViewData.contentMap.fam_ins_info_one_displayName,
              	  getBasicPrice : InsuranceFamilyAdd.storebasicPrice,
                  indAddObject: InsuranceFamilyAdd.insViewObject1,
                  overlayObj: InsuranceFamilyAdd.insuranceInformationOverlay,
                  resultDomNode: InsuranceFamilyAdd.resultDomNode,
                  summaryDescription: InsuranceFamilyAdd.description,
                  paxComposition: InsuranceFamilyAdd.paxComposition1,
                  pax:InsuranceFamilyAdd.paxDetails,
                  price: InsuranceFamilyAdd.price,
                  famCode: InsuranceFamilyAdd.code,
                  buttonRefNode2: InsuranceFamilyAdd.buttonRefNode2,
                  FrmPrice2: InsuranceFamilyAdd.FrmPrice,
                  currAppPrice2: InsuranceFamilyAdd.currencyAppened,
                  addexcessPrice2: InsuranceFamilyAdd.addexcessPrice,
                  addAppPrice2: InsuranceFamilyAdd.addAppPrice,
                  addExcessFlag:InsuranceFamilyAdd.addExcessFlag,
                  selectedFlag:InsuranceFamilyAdd.selectedFlag,
                  initialflag:InsuranceFamilyAdd.initialflag,
                  ewflag:InsuranceFamilyAdd.ewflag,
                  coveredAllPassenger:InsuranceModel.insuranceJsonData.insuranceContainerViewData.coveredAllPassenger,
                  isFamButtonAdded:'true'
                });



          }


      },

      addFamilyIns: function (excessFlag) {

    	  console.log('get excess flag'+excessFlag);
    	  var InsuranceFamilyAdd = this;
    	  InsuranceFamilyAdd.impInfoCheckBox = dom.byId("impCheckbox_Error");
    	  // InsuranceFamilyAdd.insuranceInformationOverlay.open();
    	  console.log('..confirm check is...'+InsuranceFamilyAdd.confirmationFlag+'....excess waiver '+excessFlag);

    	  if (InsuranceFamilyAdd.confirmationFlag) {

    		  dojo.addClass(dom.byId("top"), 'updating');
         	  dojo.addClass(dom.byId("main"), 'updating');
         	  dojo.addClass(dom.byId("right"), 'updating');

         	  var jsonRequestData = {
         			  "insuranceCode": InsuranceFamilyAdd.code,
         			  "excessWaiver": excessFlag,
         			  "selected": {}
         	  }
         	  var results = xhr.post({
	             url: this.targetUrl,
	             content: {
	               insuranceCriteria: dojo
	               .toJson(jsonRequestData)
	             },
	             handleAs: "json",
	             error: function (err) {
	            	 if (dojoConfig.devDebug) {

	            	 }
	            	 dojo.removeClass(dom.byId("top"), 'updating');
	            	 dojo.removeClass(dom.byId("main"), 'updating');
	            	 dojo.removeClass(dom.byId("right"), 'updating');
	             }
         	  });

         	  dojo.when(results, function (response) {
         		  InsuranceFamilyAdd.afterSuccess(response);
         		  dojo.removeClass(dom.byId("top"), 'updating');
         		  dojo.removeClass(dom.byId("main"), 'updating');
         		  dojo.removeClass(dom.byId("right"), 'updating');
         	  });
    	  }else{

    		  console.log(this.jsonData.extraOptionsStaticContentViewData.insuranceStaticContentViewData.insuranceContentMap.Insurance_Restrictions_ToolTip+'..onditions');
    		  domClass.remove("impCheckbox_Error", "disNone");
    		  domClass.remove(query(".error-notation-insurance", InsuranceFamilyAdd.impInfoCheckBox.domNode)[0], "disNone");
    		  domAttr.set(InsuranceFamilyAdd.impInfoCheckBox.id, "innerHTML", "Please confirm that you have checked the terms and conditions");

         }

      },


      afterSuccess: function (response) {

    	  var InsuranceFamilyAdd = this;
    	  InsuranceFamilyAdd.jsonData = response;

    	  console.log('..in travel js after sucess method calling.');
          dojo.publish("tui/booking/insurance");
          console.log(dojo.query(".moreDetails"));

          domClass.add("expand-view", "disNone");
          domClass.remove("collapsed-view", "disNone");
          domStyle.set(query('.addedFamBtn')[0], "display", "block");
          InsuranceFamilyAdd.FOwipeOut();
          domStyle.set(query('.updatedIns')[0], "display", "none");
          domClass.remove("ind-ins","Insopacity");
          //dojo.query("#changeButton").attr("disabled", false);
          domClass.remove("fam-ins","bbnone");
          domClass.add("addedBtn","hideadded");
          domClass.remove("fam-ins","add-height");
          topic.publish("tui/widget/booking/insurnaceB/isuranceFamilyCover/disabledInd");
		  topic.publish("tui/booking/insuranceB/disableAdequateInsuranceCheck");

          dijit.registry.byId("controllerWidget").publishToViews("extraoptions", response);
          InsuranceModel.insuranceJsonData = response;


          dojo.forEach(response.insuranceContainerViewData.insViewData,
            function (item, i) {
              console.log(InsuranceFamilyAdd.buttonRefNode2, 'item.selected');
              if (item.selected) {
                InsuranceFamilyAdd.description = item.description;
                InsuranceFamilyAdd.paxDetails = item.paxComposition;
                InsuranceFamilyAdd.price = item.totalPrice;
                InsuranceFamilyAdd.initialflag=item.selected;
                InsuranceFamilyAdd.ewflag=item.excessWaiverViewData.selected;
                InsuranceFamilyAdd.currencyAppened = item.excessWaiverViewData.currencyAppendedPrice;
                InsuranceFamilyAdd.individualPrice = item.roundedTotalPrice;
                InsuranceFamilyAdd.basicPrice = item.price;
                InsuranceFamilyAdd.storebasicPrice = item.roundedFrmPrice;

              }

            });
          console.log(InsuranceFamilyAdd.price+'.. total price get..');
          dom.byId("collapseFamPrice").innerHTML = InsuranceFamilyAdd.price;
          console.log(InsuranceFamilyAdd.price+'. total price..');
          var insuranceSummary = new tui.widget.booking.insuranceB.InsuranceSummary({
        	titleName:this.jsonData.extraOptionsContentViewData.extraContentViewData.contentMap.fam_ins_info_one_displayName,
        	getBasicPrice : InsuranceFamilyAdd.storebasicPrice,
            indAddObject: InsuranceFamilyAdd.insViewObject1,
            overlayObj: InsuranceFamilyAdd.insuranceInformationOverlay,
            resultDomNode: InsuranceFamilyAdd.resultDomNode,
            summaryDescription: InsuranceFamilyAdd.description,
            paxComposition: InsuranceFamilyAdd.paxComposition1,
            pax:InsuranceFamilyAdd.paxDetails,
            price: InsuranceFamilyAdd.price,
            famCode: InsuranceFamilyAdd.code,
            buttonRefNode2: InsuranceFamilyAdd.buttonRefNode2,
            FrmPrice2: InsuranceFamilyAdd.FrmPrice,
            currAppPrice2: InsuranceFamilyAdd.currencyAppened,
            addexcessPrice2: InsuranceFamilyAdd.addexcessPrice,
            addAppPrice2: InsuranceFamilyAdd.addAppPrice,
            addExcessFlag:InsuranceFamilyAdd.addExcessFlag,
            selectedFlag:InsuranceFamilyAdd.selectedFlag,
            initialflag:InsuranceFamilyAdd.initialflag,
            ewflag:InsuranceFamilyAdd.ewflag,
            coveredAllPassenger:InsuranceModel.insuranceJsonData.insuranceContainerViewData.coveredAllPassenger,
            jsonData:this.jsonData,
            isFamButtonAdded:'true'
          });
        },


      toogleInsSection : function(){

    	  var travelInsComponent = this;
    	  var getclassName = dojo.byId(travelInsComponent.itemSelector);
    	  var getclassNameexpandview = dojo.byId(travelInsComponent.expanditemSelector);
    	  //on(this.familygQAdd, "click", lang.hitch(travelInsComponent, travelInsComponent.getQuote));
    	  console.log('..'+this.indivPrice+'..get basic price..');
    	 // travelInsComponent.getFaminsPrice = this.indivPrice;
    	  dom.byId("getFaminsPrice").innerHTML = travelInsComponent.famInsprice;
    	  dom.byId("getFamExcessPrice").innerHTML = travelInsComponent.currencyExcessWaiver;
    	  dojo.connect(getclassName, "onclick", function(evt){
    		  if(travelInsComponent.individualPrice){
    			  travelInsComponent.excessWaiverFlag = false;
    		  }
      		  console.log('..re.click show details'+'..'+travelInsComponent.individualPrice);
      		  console.log(travelInsComponent.get_mod+'..value.');
      		  travelInsComponent.FOwipeIn();
      	  });
    	  dojo.connect(getclassNameexpandview, "onclick", function(evt){
    		  travelInsComponent.FOwipeOut();
    	  });
      },


      FOwipeIn : function(){

    	  var travelInsComponent = this;
    	  console.log('..'+this.indivPrice+'..get basic price..'+travelInsComponent.indivPrice);
    	  domClass.add("famins_section", "wipeinexpand");
    	  domClass.remove("famins_section", "disNone");
    	  domClass.add("collapsed-view", "disNone");
    	  domClass.remove("expand-view", "disNone");
    	  //domClass.add("ind-ins","Insopacity");
    	  //dojo.query("#changeButton").attr("disabled", true);

    	  topic.publish("tui/widget/booking/insurnaceB/isuranceFamilyCover/disabledInd");

    	  domClass.add("fam-ins","bbnone");
    	  domClass.add("fam-ins","add-height");
    	  coreFx.wipeIn({
    		  node: travelInsComponent.targetSelector,
    	      onEnd: function(){
    	      }
    	  }).play();
      },

      FOwipeOut : function(){

    	  var travelInsComponent = this;
    	  var getclassName = dojo.byId(travelInsComponent.itemSelector);
    	  topic.publish("tui/widget/booking/insurnaceB/isuranceFamilyCover/enableInd");
		  topic.publish("tui/booking/insuranceB/enableAdequateInsuranceCheck");
    	  coreFx.wipeOut({
    		  node: travelInsComponent.targetSelector,
    		  onEnd: function(){
    			  domClass.remove("fam-ins","bbnone");
    			  domClass.remove("fam-ins","add-height");
    			domClass.remove("collapsed-view", "disNone");
    	       	domClass.add("expand-view", "disNone");
    	       	//domClass.remove("ind-ins","Insopacity");
           	//	dojo.query("#changeButton").attr("disabled", false);
    		  }
   	    }).play();
     	var getclassName = query(travelInsComponent.itemSelector);

     },  /*Version-B insu related code - end */

     refresh: function (field, response) {
    	  var trInComponent = this;
    	  trInComponent.jsonData = response;

    	  console.log('refresh -10');
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

        var insuranceInformationOverlay1 = new tui.widget.booking.insuranceB.InsuranceInformationOverlay({
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

            trInComponent.indivPrice = item.roundedFrmPrice;
            trInComponent.familyTitle = item.description;
            trInComponent.code = item.code;
            trInComponent.currFrmPrice = item.roundedFrmPrice;
            trInComponent.currencyAppPrice = item.excessWaiverViewData.currencyAppendedPrice;
            trInComponent.excessWaiverPrice = item.excessWaiverViewData.price;
            trInComponent.addPrice = item.roundedFrmPrice;
            trInComponent.selectedFlag = item.selected;
            var individualObject = new tui.widget.booking.insuranceB.InsuranceIndividualCover({
            	 price: item.roundedFrmPrice,
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

            /*	 var familyObject  = new tui.widget.booking.insurance.InsuranceFamilyCover({adtPrice:item.frmAdtPrice,
             chdPrice:item.frmChdPrice,title:item.description,insViewObject:insViewRootData,code:item.code})*/
            // domConstruct.place(familyObject.domNode, "WhatCoveredfamily","only");
            var familyObject = new tui.widget.booking.insuranceB.InsuranceFamilyCover({
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