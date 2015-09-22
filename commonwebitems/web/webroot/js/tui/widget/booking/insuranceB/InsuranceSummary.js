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
  "dojo/topic",
  "dojo/text!tui/widget/booking/insuranceB/view/templates/InsuranceSummary.html",
  "tui/widget/booking/insuranceB/InsuranceModel",
  "tui/widget/booking/insuranceB/InsuranceInformationOverlay",
  "tui/widget/booking/insuranceB/InsuranceFamilyAdd",
  "tui/widget/booking/insuranceB/InsuranceIndAdd"],
  function (declare, _WidgetBase, _TemplatedMixin, Templatable, dtlTemplated, DynamicPopup, dojo, lang,
		  domConstruct, query, dom, on, xhr, domClass,domStyle,BookflowUrl,has, topic, InsuranceSummaryTmpl,InsuranceModel) {
    return declare("tui.widget.booking.insuranceB.InsuranceSummary",
      [ _WidgetBase, dtlTemplated, Templatable, _TemplatedMixin, DynamicPopup ], {

        tmpl: InsuranceSummaryTmpl,
        templateString: "",
        widgetsInTemplate: true,
        summaryDescription: null,
        summaryPaxComposition: null,
        summaryPrice: null,
        familyCode: null,
        targeturl: BookflowUrl.insuranceBremoveurl,
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
        basicPriceValue: null,
        price:0,

        postMixInProperties: function () {

            var InsuranceSummary = this;
            InsuranceSummary.summaryDescription = this.summaryDescription;
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
            InsuranceSummary.basicPriceValue = this.getBasicPrice;

            //InsuranceSummary.pax=this.pax;

            console.log('..sum..'+this.summaryDescription);
          },

        buildRendering: function () {

          this.templateString = this.renderTmpl(this.tmpl, this);
          delete this._templateCache[this.templateString];
          this.inherited(arguments);
        },

        postCreate: function () {
          var InsuranceSummary = this;
          console.log('.....summary js ....'+InsuranceSummary.jsonData);
          console.log(InsuranceSummary.jsonData);
          dojo.empty("resultNode");
          domConstruct.place(this.templateString, "resultNode", "only");
          InsuranceSummary.listenEvents();
          InsuranceModel.summaryDisplayed = true;
        },
        refresh: function(){
        	 console.log('refresh called 9');
        },

        afterSuccess: function (response) {
        	var InsuranceSummary = this;

        	console.log(InsuranceSummary.getBasicPrice+'...sumarry panel after sucess');

        	query(".withoutPrice1").style("display", "none");
        	query(".without1").style("display", "none");
        	query(".withoutPrice").style("display", "none");
        	query(".without").style("display", "none");
        	dojo.publish("tui/booking/insurance", true);
        	//query(".price-containerInd").style("display", "block");
        	//query(".price-containerFam").style("display", "block");
  		    topic.publish("tui/booking/insuranceB/enableAdequateInsuranceCheck");
        	topic.publish("tui/booking/insuranceB/enableAdequateInsuranceCheck");
        	dojo.forEach(response, function (item, i) {
        		var excessText = "";
        		if(InsuranceSummary.addExcess){
        			excessText="with excess waiver";
        		}else{
        			excessText="";
        		}
        		if (item.insRemoved) {
        			InsuranceModel.summaryDisplayed = false;
        			if(item.familyInsPresent){
        				topic.publish("tui/widget/booking/insurnaceB/isuranceFamilyCover/enableInd");
        			} else {
        	            topic.publish("tui/widget/booking/insurnaceB/InsuranceIndividualCover/enableFam");
        			}
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
                // Fix DE-44205
                	if(!InsuranceModel.summaryDisplayed){
                		domConstruct.destroy("mainSummary");
                	}
                }
              };
            dojo.fadeOut(fadeArgs).play();


			domClass.remove("collapsed-view", "disNone");
			domClass.add("expand-view", "disNone");
			domStyle.set(query('.updatedIns')[0], "display", "block");
			domClass.remove("fam-ins","bbnone");
			//InsuranceSummary.familyconfirmation.checked=false;
			console.log('.basic value..'+InsuranceSummary.currencyAppPrice);

			if( dojo.style("addedBtn", "display")==="none" ){
			}else{
				dom.byId("collapseFamPrice").innerHTML = InsuranceSummary.basicPriceValue;

			}
			domStyle.set(query('.addedFamBtn')[0], "display", "none");
			domClass.remove("addedBtn","hideadded");

            }

          });

        },

        closeFadeout: function(){
        	domConstruct.destroy("mainSummary");
        },

        listenEvents: function () {
          var InsuranceSummary = this;

          //on(dom.byId("change"), "click", lang.hitch(InsuranceSummary, InsuranceSummary.change));
          on(dom.byId("summaryRemove"), "click", lang.hitch(InsuranceSummary, InsuranceSummary.removeAjaxCall));
        },



        removeAjaxCall: function () {

          var InsuranceSummary = this;
          dojo.addClass(dom.byId("top"), 'updating');
      	  dojo.addClass(dom.byId("main"), 'updating');
      	  dojo.addClass(dom.byId("right"), 'updating');


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
 		    topic.publish("tui/widget/booking/insuranceB/remove");
          });
        }
      });
  });