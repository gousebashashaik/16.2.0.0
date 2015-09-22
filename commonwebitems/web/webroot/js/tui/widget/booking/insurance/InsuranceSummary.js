define([
  "dojo/_base/declare",
  // Parent classes
  "dijit/_WidgetBase",
  "tui/widget/mixins/Templatable",
  "dijit/_TemplatedMixin",
  "dojox/dtl/_Templated",
  "tui/widget/popup/DynamicPopup",
  "dojo",
  "dojo/_base/lang",
  "dojo/dom-construct",
  'dojo/query',
  'dojo/dom',
  'dojo/on',
  "dojo/_base/xhr",
  "dojo/dom-class",
  "dojo/dom-style",
  "tui/widget/booking/constants/BookflowUrl",
  "dojo/has",
  "dojo/text!tui/widget/booking/insurance/view/templates/InsuranceSummary.html",
  "tui/widget/booking/insurance/InsuranceModel",
  "tui/widget/booking/insurance/InsuranceInformationOverlay",
  "tui/widget/booking/insurance/InsuranceFamilyAdd",
  "tui/widget/booking/insurance/InsuranceIndAdd"],
  function (declare, _WidgetBase, _TemplatedMixin, Templatable, dtlTemplated, DynamicPopup, dojo, lang,
		  domConstruct, query, dom, on, xhr, domClass,domStyle,BookflowUrl,has,InsuranceSummaryTmpl,InsuranceModel) {
    return declare("tui.widget.booking.insurance.InsuranceSummary",
      [ _WidgetBase, dtlTemplated, Templatable, _TemplatedMixin, DynamicPopup ], {

        tmpl: InsuranceSummaryTmpl,
        templateString: "",
        widgetsInTemplate: true,
        summaryDescription: null,
        summaryPaxComposition: null,
        summaryPrice: null,
        familyCode: null,
        targeturl: BookflowUrl.insuranceremoveurl,
        FrmPrice: null,
        currAppPrice: null,
        addexcessPrice: null,
        addAppPrice: null,
        insViewObject: null,
        RefNode2:null,
        RefNode3:null,
        addExcess:null,
        selectedFlag:null,
        initialflag:null,
        pax:null,
        coveredAllPassenger:null,
        ewflag:null,
	 isIndButtonAdded:false,
        isFamButtonAdded:false,


        postMixInProperties: function () {

          var InsuranceSummary = this;
          InsuranceSummary.summaryDescription = this.description;
          InsuranceSummary.summaryPaxComposition = this.paxComposition;
          InsuranceSummary.summaryPrice = this.price;
          InsuranceSummary.familyCode = this.famCode;
          InsuranceSummary.insViewObject = this.indAddObject;
          InsuranceSummary.FrmPrice = this.FrmPrice2;
          InsuranceSummary.currAppPrice = this.currAppPrice2;
          InsuranceSummary.addexcessPrice = this.addexcessPrice2;
          InsuranceSummary.addAppPrice = this.addAppPrice2;
          InsuranceSummary.RefNode2 = this.buttonRefNode2;
          InsuranceSummary.RefNode3 = this.buttonRefNode3;
          InsuranceSummary.addExcess = this.addExcessFlag;
          InsuranceSummary.ewflag=this.ewflag;
          InsuranceSummary.selectedFlag = this.selectedFlag;
          InsuranceSummary.initialflag=this.initialflag;
          InsuranceSummary.coveredAllPassenger=this.coveredAllPassenger;

          //InsuranceSummary.pax=this.pax;

          //console.log(this.initialflag,"thisthisthisthis");
        },

        buildRendering: function () {

          this.templateString = this.renderTmpl(this.tmpl, this);
          delete this._templateCache[this.templateString];
          this.inherited(arguments);
        },

        postCreate: function () {
          var InsuranceSummary = this;
		console.log(InsuranceSummary.jsonData);
		if(query('.getQuoteBtn')[0] && query('.addedIndBtn')[0] && this.isIndButtonAdded){
		   domStyle.set(query('.getQuoteBtn')[0], "display", "none");
 		   domStyle.set(query('.addedIndBtn')[0], "display", "block");
		   //query(".price-containerInd").style("display", "none");
		   if(query('.getQuoteFamBtn')[0] && query('.addedFamBtn')[0]){
			domStyle.set(query('.getQuoteFamBtn')[0], "display", "block");
 		   	domStyle.set(query('.addedFamBtn')[0], "display", "none");
		   	//query(".price-containerFam").style("display", "block");
		   }
		}

		if(query('.getQuoteFamBtn')[0] && query('.addedFamBtn')[0] && this.isFamButtonAdded){
		   domStyle.set(query('.getQuoteFamBtn')[0], "display", "none");
 		   domStyle.set(query('.addedFamBtn')[0], "display", "block");
		   //query(".price-containerFam").style("display", "none");
 		  var buttonNode=query('.getQuoteBtn')[0];
 		  var indBtnNode=query('.addedIndBtn')[0];
 		  if(buttonNode){
		   domStyle.set(query('.getQuoteBtn')[0], "display", "block");
 		  }
 		 if(indBtnNode){
 		   domStyle.set(query('.addedIndBtn')[0], "display", "none");
 		 }
		   //query(".price-containerInd").style("display", "block");

		}

	  dojo.empty("resultNode");
          domConstruct.place(this.templateString, "resultNode", "only");



          InsuranceSummary.listenEvents();

if( this.overlayObj!=null){
          this.overlayObj.close();
          }
          if(InsuranceSummary.coveredAllPassenger){

        	  domClass.add("coveredAllPassenger", "disNone");

          }else{

        	  dojo.removeClass("coveredAllPassenger", "disNone");
          }
        },
        refresh: function(){

        },

        afterSuccess: function (response) {
        	  var InsuranceSummary = this;
           query(".withoutPrice1").style("display", "none");
           query(".without1").style("display", "none");
           query(".withoutPrice").style("display", "none");
         query(".without").style("display", "none");
         dojo.publish("tui/booking/insurance", true);
         //query(".price-containerInd").style("display", "block");
         //query(".price-containerFam").style("display", "block");
          dojo.forEach(response, function (item, i) {
        	  var excessText = "";
        	  if(InsuranceSummary.addExcess){
        		  excessText="with excess waiver";
        	  }else{
        		  excessText="";
        	  }
            if (item.insRemoved) {

              var fadeoutHtml =
                "<div class="+
              "info - section pax - name"+
              ">"  + InsuranceSummary.summaryDescription + " "  + excessText + " " + "has been removed</div>"+
              "<p><a id='prebook-closeqty'  href='javascript:void(0)'>close x</a></p>";


              domConstruct.place(domConstruct.toDom(fadeoutHtml), "mainSummary", "only");
              on(dom.byId("prebook-closeqty"), "click", lang.hitch(InsuranceSummary, InsuranceSummary.closeFadeout));
              dojo.style("mainSummary",{
                  "backgroundColor": "#FFFF99",
                  "width": "97%",
                  "height":"20px"

              });
              var fadeArgs = {
                node: "mainSummary",
                duration: BookflowUrl.fadeOutDuration,
                onEnd: function () {
                  domConstruct.destroy("mainSummary");
                }
              };
              dojo.fadeOut(fadeArgs).play();
		if(query('.getQuoteBtn')[0] && query('.addedIndBtn')[0]){
			domStyle.set(query('.getQuoteBtn')[0], "display", "block");
 			domStyle.set(query('.addedIndBtn')[0], "display", "none");
 			//query(".price-containerInd").style("display", "block");
		}
		if(query('.getQuoteFamBtn')[0] && query('.addedFamBtn')[0]){
			domStyle.set(query('.getQuoteFamBtn')[0], "display", "block");
 			domStyle.set(query('.addedFamBtn')[0], "display", "none");
			//query(".price-containerFam").style("display", "block");
		}




            }

          });

        },

        closeFadeout: function(){
        	domConstruct.destroy("mainSummary");
        },

        listenEvents: function () {
          var InsuranceSummary = this;

          on(dom.byId("change"), "click", lang.hitch(InsuranceSummary, InsuranceSummary.change));
          on(dom.byId("summaryRemove"), "click", lang.hitch(InsuranceSummary, InsuranceSummary.removeAjaxCall));
        },

        change: function () {
          var InsuranceSummary = this;
          InsuranceSummary.insuranceInformationOverlay = new tui.widget.booking.insurance.InsuranceInformationOverlay({
              widgetId: 'insuranceIndAdd',
              modal: true
           });

          InsuranceSummary.insuranceInformationOverlay.open();
          if (this.buttonRefNode2 != null || (InsuranceSummary.initialflag)) {
            var insuranceFamilyAdd = new tui.widget.booking.insurance.InsuranceFamilyAdd({
              indAddObject: InsuranceSummary.insViewObject,
              code: InsuranceSummary.familyCode,
              resultDomNode: InsuranceSummary.resultDomNode,
              curFrmPrice: InsuranceSummary.FrmPrice,
              currAppPrice: InsuranceSummary.currAppPrice,
              addexcessPrice: InsuranceSummary.addexcessPrice,
              addAppPrice: InsuranceSummary.addAppPrice,
              buttonRefNode: InsuranceSummary.buttonRefNode2,
              indivtitle: InsuranceSummary.summaryDescription,
              selectedFlag :InsuranceSummary.selectedFlag,
              addExcessFlag:InsuranceSummary.addExcess,
              ewflag:InsuranceSummary.ewflag,
              jsonData:this.jsonData,
              insStaticTooltipData:InsuranceModel.insuranceJsonData.extraOptionsStaticContentViewData.insuranceStaticContentViewData.insuranceContentMap.Insurance_ExcessWaiver_ToolTip
             // insuranceRestrictionsToolTip:InsuranceModel.insuranceJsonData.extraOptionsStaticContentViewData.insuranceStaticContentViewData.insuranceContentMap.Insurance_Restrictions_ToolTip

            });

          }
          if ((this.buttonRefNode3 != null) || (!InsuranceSummary.initialflag)) {
            var insuranceIndAdd = new tui.widget.booking.insurance.InsuranceIndAdd({
              indAddObject: InsuranceModel.insuranceJsonData.insuranceContainerViewData,
              familyCode: InsuranceSummary.familyCode,
              resultDomNode: InsuranceSummary.resultDomNode,
              buttonRefNode: InsuranceSummary.buttonRefNode3,
              ewflag:this.ewflag,
              jsonData:this.jsonData,
              insStaticTooltipData:InsuranceModel.insuranceJsonData.extraOptionsStaticContentViewData.insuranceStaticContentViewData.insuranceContentMap.Insurance_ExcessWaiver_ToolTip
            //  insuranceRestrictionsToolTip:InsuranceModel.insuranceJsonData.extraOptionsStaticContentViewData.insuranceStaticContentViewData.insuranceContentMap.Insurance_Restrictions_ToolTip
            });
          }

        },


        removeAjaxCall: function () {

          var InsuranceSummary = this;
          dojo.addClass(dom.byId("top"), 'updating');
      	  dojo.addClass(dom.byId("main"), 'updating');
      	  dojo.addClass(dom.byId("right"), 'updating');

          //console.log(jsonRequestData);
          var results = xhr.post({
            url: this.targeturl,
            content: {
              insuranceCriteria: this.familyCode
            },
            handleAs: "json",
            error: function (err) {
              if (dojoConfig.devDebug) {
                // console.log(this.domNode.removeClass("mask-interactivity"));
              }
              // this.afterFailure();
              dojo.removeClass(dom.byId("top"), 'updating');
              dojo.removeClass(dom.byId("main"), 'updating');
     		  dojo.removeClass(dom.byId("right"), 'updating');
              // this.afterFailure();
            }
          });
          dojo.when(results, function (response) {
        	  InsuranceModel.insuranceJsonData = response;
            InsuranceSummary.afterSuccess(response.insuranceContainerViewData.insViewData);
            InsuranceSummary.controller = dijit.registry.byId("controllerWidget");
      	    dijit.registry.byId("controllerWidget").publishToViews("insurance", response);
      	    dojo.removeClass(dom.byId("top"), 'updating');
 		    dojo.removeClass(dom.byId("main"), 'updating');
 		    dojo.removeClass(dom.byId("right"), 'updating');
          });
        }
      });
  });