define(["dojo/_base/declare","dojo/has",
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
  "dojo/_base/event",
  "dojo/_base/xhr",
  "dojo/dom-class",
  "dojo/dom-attr",
  "tui/widget/booking/insurance/InsuranceInformationOverlay",
  "tui/widget/booking/constants/BookflowUrl",
  "tui/widget/booking/bookflowMsgs/nls/Bookflowi18nable",
  "dojo/text!tui/widget/booking/insurance/view/templates/InsuranceIndAddDetails.html",
  "tui/widget/booking/insurance/InsuranceModel",
  "dojox/dtl",
  "dojox/dtl/Context",
  "tui/widget/form/SelectOption",
  "tui/widget/expand/Expandable",
  "tui/widget/booking/insurance/InsuranceSummary",
  "tui/widget/ScrollPanel",
  "tui/widget/booking/insurance/TravelInsuranceComponent","tui/widget/booking/insurance/InsuranceIndAdd"],
  function (declare, has,_WidgetBase, Templatable, dtlTemplated, dojo, lang, domConstruct, query, dom, on, event, xhr, domClass, domAttr, insuranceInformationOverlay, BookflowUrl,Bookflowi18nable,insuranceIndAddTmpl,InsuranceModel) {
    return declare("tui.widget.booking.insurance.InsuranceIndAddDetails",
      [_WidgetBase, dtlTemplated, Templatable, Bookflowi18nable], {

        tmpl: insuranceIndAddTmpl,
        templateString: "",
        widgetsInTemplate: true,
        targeturl:BookflowUrl.insuranceaddurl ,
        resultDomNode: null,
        description: null,
        addPrice: null,
        addPax: null,
        addExcessFlag: false,
        buttonRefNode3: null,
        insViewObject: null,
        paxComposition1:null,
        excesswaiverDisplay:null,
        insStaticTooltipData:null,
        coveredAllPassenger:null,
        parentDom:null,
        buttonRef:null,
        currency:"",

        postMixInProperties: function () {
          var insuranceIndAddDetails = this;
          insuranceIndAddDetails.currency=dojoConfig.currency;
          insuranceIndAddDetails.buttonRef = this.buttonRef;
          insuranceIndAddDetails.parentDom = this.parentDom;
          insuranceIndAddDetails.values = this.displayArray1;
          insuranceIndAddDetails.currencyAppendedPrice = this.cAPrice;
          insuranceIndAddDetails.appendedPrice = this.aPrice;
          insuranceIndAddDetails.map = this.map;
         // insuranceIndAddDetails.map1 = this.mapobj1;
         // insuranceIndAddDetails.map2 = this.mapobj2;
          insuranceIndAddDetails.familyCode = this.familyCode;
          insuranceIndAddDetails.pax = this.pax;
          console.log(this.pax,"this.paxthis.pax");
          //insuranceIndAddDetails.dd = this.dd;
          insuranceIndAddDetails.resultDomNode = this.resultDomNode;
          insuranceIndAddDetails.addPrice = this.addPrice;
          insuranceIndAddDetails.paxComposition1 = this.addPrice;
          insuranceIndAddDetails.addPax = this.addPax;
          insuranceIndAddDetails.buttonRefNode3 = this.buttonRefNode3;
          insuranceIndAddDetails.insViewObject = this.insViewObject;
          insuranceIndAddDetails.paxComposition1 = parseInt((this.addPrice), 10);
          insuranceIndAddDetails.excesswaiverDisplay = this.excesswaiverDisplayFlag;
          insuranceIndAddDetails.ewFlag=this.ewFlag;
          insuranceIndAddDetails.insStaticTooltipData=this.insStaticTooltipData;

          if (this.excesswaiverDisplay) {
        	  insuranceIndAddDetails.excesswaiverDisplay = true;
        	}else{
        		insuranceIndAddDetails.excesswaiverDisplay = false;
        	}

          insuranceIndAddDetails.initBookflowMessaging();
          insuranceIndAddDetails.setContentValues();

        },

        buildRendering: function () {
          this.templateString = this.renderTmpl(this.tmpl, this);
          delete this._templateCache[this.templateString];
          this.inherited(arguments);
        },

        postCreate: function () {

          // this.listenEvents();
          var insuranceIndAddDetails = this;

          insuranceIndAddDetails.insuranceInformationOverlay = new tui.widget.booking.insurance.InsuranceInformationOverlay({widgetId: 'insuranceIndAdd', modal: true});
          insuranceIndAddDetails.insuranceInformationOverlay.open();

          domConstruct.place(this.domNode, dom.byId("insuranceIndAddOverlay"), "only");
          if(insuranceIndAddDetails.ewFlag){
        	  var inPrice = parseInt((insuranceIndAddDetails.addPrice), 10);
              var inpax = parseInt((insuranceIndAddDetails.addPax), 10);
        	  this.totalValue.innerHTML = inPrice + inpax+".00";
        	  insuranceIndAddDetails.addExcessFlag=true;

          }else{
        	  this.totalValue.innerHTML = parseInt((insuranceIndAddDetails.addPrice), 10)+".00";
              insuranceIndAddDetails.paxComposition1 = parseInt((insuranceIndAddDetails.addPrice), 10);

          }

          insuranceIndAddDetails.listenEvents();
          this.inherited(arguments);
          this.tagElements(query('button.changeBtn', this.domNode),"changeCover");
          if(this.excesswaiverDisplay){
          this.tagElements(query('.excessCheck', this.domNode),"addExcessWaiver");
          }
          this.tagElements(query('.confirmationCheck', this.domNode),"tAndcAgree");
          this.tagElements(query('button.addBtn', this.domNode),"Individual Cover");

        },

        setContentValues: function(){
    		this.contentObject = this.jsonData ;
  		var titleKey = this.bookflowMessaging[dojoConfig.site].Insurance.excessWaiverTitle;
  		var bodyKey = this.bookflowMessaging[dojoConfig.site].Insurance.excessWaiverBody;
  		this.excessWaiverTitle = this.jsonData.extraOptionsContentViewData.extraContentViewData.contentMap[titleKey];
  		this.excessWaiverBody = this.jsonData.extraOptionsContentViewData.extraContentViewData.contentMap[bodyKey];
  	 },

        listenEvents: function () {
          var insuranceIndAddDetails = this;
          on(this.changegq, "click", lang.hitch(insuranceIndAddDetails, insuranceIndAddDetails.getQuote));
          on(this.addGetQ1, "click", lang.hitch(insuranceIndAddDetails, insuranceIndAddDetails.addDetails));
          if(this.excesswaiverDisplay){
          on(this.addExcessWaiver, "change", lang.hitch(insuranceIndAddDetails, insuranceIndAddDetails.addExcessWaiverFunction, this.addExcessWaiver));
          }
          on(this.confirmationCheck, "change", lang.hitch(insuranceIndAddDetails, insuranceIndAddDetails.confirmationCheckFunction,this.confirmationCheck));
          on(this.cancelButton, "click", lang.hitch(insuranceIndAddDetails, insuranceIndAddDetails.cancelOverlay, insuranceIndAddDetails.insuranceInformationOverlay));

        },
        cancelOverlay: function (overlayObj) {
        	overlayObj.close();

        },

        afterSuccess: function (response) {
          var insuranceIndAddDetails = this;

    	  query(".withoutPrice1").style("display", "none");

          /*var controller =*/ dijit.registry.byId("controllerWidget").publishToViews("extraoptions", response);
         /* var index = controller.views.indexOf(this);
          if (index !== -1) {
            controller.views[index] = this;
          } else {
            controller.publishToViews("extraoptions", response);
          }*/
          InsuranceModel.insuranceJsonData = response;
          dojo.forEach(response.insuranceContainerViewData.insViewData, function (item, i) {
            if (item.selected) {
              insuranceIndAddDetails.description = item.description;
              insuranceIndAddDetails.paxDetails = item.paxComposition;
              insuranceIndAddDetails.ewFlag=item.excessWaiverViewData.selected;
              insuranceIndAddDetails.price = item.totalPrice;
            }
          });

          console.log(insuranceIndAddDetails.addExcessFlag,"insuranceIndAddDetails.addExcessFlag");

          	var refNode = dom.byId("insurance_comp");

          	dojo.publish("tui/booking/insurance", false);

          	InsuranceModel.insuranceJsonData = response;
          //var newInstance = new tui.widget.booking.insurance.TravelInsuranceComponent({'jsonData': response});




          var insuranceSummary = new tui.widget.booking.insurance.InsuranceSummary({
            overlayObj: insuranceIndAddDetails.insuranceInformationOverlay,
            resultDomNode: insuranceIndAddDetails.resultDomNode,
            description: insuranceIndAddDetails.description,
            paxComposition: insuranceIndAddDetails.paxComposition1,
            pax:insuranceIndAddDetails.paxDetails,
            price: insuranceIndAddDetails.price,
            famCode: insuranceIndAddDetails.familyCode,
            buttonRefNode3: insuranceIndAddDetails.buttonRefNode3,
            indAddObject: insuranceIndAddDetails.insViewObject,
            addExcessFlag:insuranceIndAddDetails.addExcessFlag,
            ewflag:insuranceIndAddDetails.ewFlag,
            coveredAllPassenger:InsuranceModel.insuranceJsonData.insuranceContainerViewData.coveredAllPassenger,
            jsonData:this.jsonData,
            isIndButtonAdded:'true'

          });

        },
        refresh: function (field, response) {




          },
        getQuote: function () {
          var insuranceIndAddDetails = this;

         // domConstruct.place(insuranceIndAddDetails.parentDom, dojo.byId("insuranceIndAddOverlay"), "only");
         // insuranceIndAddDetails.map1.clear();
         // insuranceIndAddDetails.map2.clear();
       // if(has("ie")){
          var insuranceIndAdd = new tui.widget.booking.insurance.InsuranceIndAdd({
              indAddObject: InsuranceModel.insuranceJsonData.insuranceContainerViewData,
              familyCode: insuranceIndAddDetails.familyCode,
              resultDomNode: insuranceIndAddDetails.resultDomNode,
              buttonRefNode: this.buttonRef,
              ewFlag:insuranceIndAddDetails.ewFlag,
              insStaticTooltipData: insuranceIndAddDetails.insStaticTooltipData,
              jsonData:this.jsonData
            });
        //  }else{
        //	  domConstruct.place(insuranceIndAddDetails.parentDom, dojo.byId("insuranceIndAddOverlay"), "only");
        //  }

        },
        addExcessWaiverFunction: function (checkbox) {
          var insuranceIndAddDetails = this;
          insuranceIndAddDetails.addExcessFlag = checkbox.checked;
          if (checkbox.checked) {
            var price = parseInt((insuranceIndAddDetails.addPrice), 10);
            var pax = parseInt((insuranceIndAddDetails.addPax), 10);
            this.totalValue.innerHTML = price + pax+".00";
            insuranceIndAddDetails.paxComposition1 =  price + pax;
          } else {
            this.totalValue.innerHTML = parseInt((insuranceIndAddDetails.addPrice), 10)+".00";
            insuranceIndAddDetails.paxComposition1 = parseInt((insuranceIndAddDetails.addPrice), 10);
          }

        },

        confirmationCheckFunction: function (obj) {
          var insuranceIndAddDetails = this;

          if (obj.checked) {
        	  domClass.add("impInfoCheckbox_Error35", "disNone");
            } else {


            }
        },

        addDetails: function () {
          var insuranceIndAddDetails = this;
          insuranceIndAddDetails.impInfoCheckBox = dom.byId("impInfoCheckbox_Error35");
          if (this.confirmationCheck.checked == true) {
        	  dojo.addClass(dom.byId("top"), 'updating');
          	  dojo.addClass(dom.byId("main"), 'updating');
          	  dojo.addClass(dom.byId("right"), 'updating');
            var jsonRequestData = {
              "insuranceCode": this.familyCode,
              "excessWaiver": this.addExcessFlag,
              "selected": this.map
            };

            var results = xhr.post({
              url: this.targeturl,

              content: {insuranceCriteria: dojo.toJson(jsonRequestData)},
              handleAs: "json",
              error: function (err) {
                if (dojoConfig.devDebug) {
                  //console.log(this.domNode.removeClass("mask-interactivity"));
                }
                //this.afterFailure();
                dojo.removeClass(dom.byId("top"), 'updating');
       		    dojo.removeClass(dom.byId("main"), 'updating');
       		    dojo.removeClass(dom.byId("right"), 'updating');
              }
            });

            dojo.when(results, function (response) {
              insuranceIndAddDetails.afterSuccess(response);
              dojo.removeClass(dom.byId("top"), 'updating');
     		  dojo.removeClass(dom.byId("main"), 'updating');
     		  dojo.removeClass(dom.byId("right"), 'updating');

            });
          } else {
            domClass.remove(query(".error-notation-insurance", this.impInfoCheckBox.domNode)[0], "disNone");
            domAttr.set(this.impInfoCheckBox.id, "innerHTML", "Please confirm that you have checked the terms and conditions");
          }
        }
      });
  });