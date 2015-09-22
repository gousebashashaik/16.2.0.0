define(["dojo/_base/declare",
  // Parent classes
  "dijit/_WidgetBase",
  "tui/widget/mixins/Templatable",
  "dojox/dtl/_Templated",
  "dojo",
  "dojo/_base/lang",
  "dojo/dom-construct",
  'dojo/query',
  'dojo/dom',
  'dojo/on',
  "dojo/_base/event",
  "dojo/dom-class",
  "dojo/dom-attr",
  "tui/widget/booking/insuranceB/InsuranceInformationOverlay",
  "tui/widget/booking/constants/BookflowUrl",
  "tui/widget/booking/insuranceB/InsuranceModel",
  "dojo/text!tui/widget/booking/insuranceB/view/templates/InsuranceFamAdd.html",
  "dojo/_base/xhr", "dojox/dtl", "dojox/dtl/Context",
  "tui/widget/form/SelectOption", "tui/widget/expand/Expandable",
  "tui/widget/booking/insuranceB/InsuranceIndAddDetails",
  "dojox/collections/ArrayList",
  "tui/widget/booking/insuranceB/InsuranceSummary" ],
  function (declare, _WidgetBase, Templatable, dtlTemplated, dojo, lang, domConstruct, query, dom, on, event, domClass, domAttr, insuranceInformationOverlay, BookflowUrl,InsuranceModel,InsuranceFamAddTmpl, xhr) {
    return declare("tui.widget.booking.insuranceB.InsuranceFamilyAdd", [ _WidgetBase, dtlTemplated, Templatable ], {

      tmpl: InsuranceFamAddTmpl,
      templateString: "",
      widgetsInTemplate: true,
      mapObject: [],
      targetUrl: BookflowUrl.insuranceaddurl,
      resultDomNode: null,
      insuranceInformationOverlay: null,
      addExcessFlag: false,
      confirmationFlag: false,
      code: null,
      FrmPrice: null,
      currAppPrice: null,
      addexcessPrice: null,
      addAppPrice: null,
      buttonRefNode2: null,
      insViewObject1: null,
      paxComposition1:null,
      indivtitle1:null,
      selectedFlag:null,
      initialflag:null,
      insStaticTooltipData:null,
      coveredAllPassenger:null,
      currencyAppened:null,

      postMixInProperties: function () {
        var InsuranceFamilyAdd = this;
        InsuranceFamilyAdd.code = this.code;
        InsuranceFamilyAdd.resultDomNode = this.resultDomNode;
        InsuranceFamilyAdd.FrmPrice = this.curFrmPrice;
        InsuranceFamilyAdd.currAppPrice = this.currAppPrice;
        InsuranceFamilyAdd.addexcessPrice = this.addexcessPrice;
        InsuranceFamilyAdd.addAppPrice = this.addAppPrice;
        InsuranceFamilyAdd.paxComposition1 =this.addAppPrice;
        InsuranceFamilyAdd.buttonRefNode2 = this.buttonRefNode;
        InsuranceFamilyAdd.insViewObject1 = this.indAddObject;
        InsuranceFamilyAdd.paxComposition1 = parseInt((this.addAppPrice), 10);
       // InsuranceFamilyAdd.indivtitle1 = this.indivtitle;
        InsuranceFamilyAdd.selectedFlag = this.selectedFlag;
        InsuranceFamilyAdd.ewflag=this.ewflag;
        InsuranceFamilyAdd.insStaticTooltipData = this.insStaticTooltipData;
       // console.log(InsuranceFamilyAdd.indivtitle1,"InsuranceFamilyAdd.indivtitle1");

      },

      buildRendering: function () {
        this.templateString = this.renderTmpl(this.tmpl, this);
        delete this._templateCache[this.templateString];
        this.inherited(arguments);
      },

      postCreate: function () {

        var InsuranceFamilyAdd = this;
        InsuranceFamilyAdd.insuranceInformationOverlay = new tui.widget.booking.insuranceB.InsuranceInformationOverlay({
            widgetId: 'insuranceIndAdd',
            modal: true
         });
        InsuranceFamilyAdd.insuranceInformationOverlay.open();
        domConstruct.place(this.domNode, dom.byId("insuranceIndAddOverlay"), "only");
        if(InsuranceFamilyAdd.ewflag){

            var inPrice = parseInt((InsuranceFamilyAdd.addAppPrice), 10);
            var inpax = parseInt((InsuranceFamilyAdd.addexcessPrice), 10);
      	    this.totalValue.innerHTML = inPrice + inpax+".00";

        }else{
      	  this.totalValue.innerHTML = parseInt((InsuranceFamilyAdd.addAppPrice), 10)+".00";
      	  InsuranceFamilyAdd.paxComposition1 = parseInt((InsuranceFamilyAdd.addAppPrice), 10);

        }

        InsuranceFamilyAdd.listenEvents();

      },

      listenEvents: function () {
        var InsuranceFamilyAdd = this;
        on(this.familygQAdd, "click", lang.hitch(InsuranceFamilyAdd, InsuranceFamilyAdd.getQuote));
        on(this.familyCheckboxid, "change", lang.hitch(InsuranceFamilyAdd, InsuranceFamilyAdd.addExcessWaiverFam, this.familyCheckboxid));
        on(this.familyconfirmation, "change", lang.hitch(InsuranceFamilyAdd, InsuranceFamilyAdd.confirmationCheckFunction, this.familyconfirmation));

      },
      refresh: function (field, response) {




      },

      afterSuccess: function (response) {
        var InsuranceFamilyAdd = this;

        dojo.publish("tui/booking/insurance");
       query(".withoutPrice").style("display", "none");
       console.log(dojo.query(".moreDetails"));
       dijit.registry.byId("controllerWidget").publishToViews("extraoptions", response);
        /*var controller = dijit.registry.byId("controllerWidget")//.publishToViews("extraoptions", response);
        var index = controller.views.indexOf(this);
        if (index !== -1) {
          controller.views[index] = this;
        } else {
          controller.publishToViews("extraoptions", response);
        }*/
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

            }

          });
        console.log(InsuranceFamilyAdd.paxComposition1,'InsuranceFamilyAdd.paxComposition1')
        var insuranceSummary = new tui.widget.booking.insuranceB.InsuranceSummary({
          indAddObject: InsuranceFamilyAdd.insViewObject1,
          overlayObj: InsuranceFamilyAdd.insuranceInformationOverlay,
          resultDomNode: InsuranceFamilyAdd.resultDomNode,
          description: InsuranceFamilyAdd.description,
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

      addExcessWaiverFam: function (obj) {

        var InsuranceFamilyAdd = this;
        if (obj.checked) {
          InsuranceFamilyAdd.addExcessFlag = obj.checked;
          var price = parseInt((InsuranceFamilyAdd.addAppPrice), 10);
          if(_.isEmpty(InsuranceFamilyAdd.addexcessPrice)){
        	  var pax = 0;
          }else{
        	  var pax = parseInt((InsuranceFamilyAdd.addexcessPrice), 10);
          }

          this.totalValue.innerHTML = price + pax+".00";
          InsuranceFamilyAdd.paxComposition1 = price + pax;
        } else {
          InsuranceFamilyAdd.addExcessFlag = obj.checked;
          this.totalValue.innerHTML = parseInt((InsuranceFamilyAdd.addAppPrice), 10)+".00";
          InsuranceFamilyAdd.paxComposition1 = parseInt((InsuranceFamilyAdd.addAppPrice), 10);

        }
      },

      confirmationCheckFunction: function (obj) {
        var InsuranceFamilyAdd = this;
        if (obj.checked) {
          InsuranceFamilyAdd.confirmationFlag = obj.checked;
          domClass.add("impInfoCheckbox_Error22", "disNone");
        } else {
          InsuranceFamilyAdd.confirmationFlag = obj.checked;

        }
      },

      getQuote: function () {

        var InsuranceFamilyAdd = this;
        InsuranceFamilyAdd.impInfoCheckBox = dom.byId("impInfoCheckbox_Error22");
       // InsuranceFamilyAdd.insuranceInformationOverlay.open();
        if (InsuranceFamilyAdd.confirmationFlag) {
          InsuranceFamilyAdd.insuranceInformationOverlay.close();
          dojo.addClass(dom.byId("top"), 'updating');
      	  dojo.addClass(dom.byId("main"), 'updating');
      	  dojo.addClass(dom.byId("right"), 'updating');
          var jsonRequestData = {
            "insuranceCode": InsuranceFamilyAdd.code,
            "excessWaiver": InsuranceFamilyAdd.addExcessFlag,
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
        } else {
          domClass.remove(query(".error-notation-insurance", InsuranceFamilyAdd.impInfoCheckBox.domNode)[0], "disNone");
          domAttr.set(InsuranceFamilyAdd.impInfoCheckBox.id, "innerHTML", "Please confirm that you have checked the terms and conditions");
        }
      }
    });
  });