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
  "dojo/_base/array",
  "dojo/dom-attr",
  "dojo/dom-class",
  "tui/widget/booking/insurance/InsuranceInformationOverlay",
  "tui/widget/booking/constants/BookflowUrl",
  "dojo/text!tui/widget/booking/insurance/view/templates/InsuranceIndAdd.html",
  "dojo/_base/xhr",
  "dijit/Dialog", "dijit/popup",
  "dojox/dtl",
  "dojox/dtl/Context",
  "tui/widget/form/SelectOption",
  "tui/widget/expand/Expandable",
  "tui/widget/booking/insurance/InsuranceIndAddDetails",
  "dojox/collections/ArrayList",
  "dojox/mvc",
  "dojox/mvc/StatefulModel",
  "dojox/mvc/Output",
  "dijit/form/Select","dijit/form/Textarea"],
  function (declare, _WidgetBase, Templatable, dtlTemplated, dojo, lang, domConstruct, query, dom, on, array,
		  domAttr, domClass, insuranceInformationOverlay,BookflowUrl, insuranceIndAddTmpl, xhr,TooltipDialog,popup) {
    return declare("tui.widget.booking.insurance.InsuranceIndAdd", [_WidgetBase, dtlTemplated, Templatable], {

      tmpl: insuranceIndAddTmpl,
      templateString: "",
      widgetsInTemplate: true,
      mapObject: [],
      targetUrl:BookflowUrl.insuranceselecturl ,
      insuranceModel: null,
      counter: 0,
      currencyAppendedPrice: null,
      appendedPrice: null,
      paxComposition: null,
      resultDomNode: null,
      addPrice: null,
      addPaxComposition: null,
      buttonRefNode3: null,
      insViewObject: null,
      flag: null,
      mapObject2:null,
      checkboxAttachPoints: [],
      selectionBoxAttachPoints:[],
      insStaticTooltipData:null,
      addExcessFlag:null,
      ewFlag:null,

      postMixInProperties: function () {
        var insuranceIndAdd = this;
        insuranceIndAdd.insViewObject = insuranceIndAdd.indAddObject;
        insuranceIndAdd.mapNewArray = new dojox.collections.ArrayList([]);
        insuranceIndAdd.displayArray = new dojox.collections.ArrayList([]);
        insuranceIndAdd.familyCode1 = this.familyCode;
        insuranceIndAdd.counter = insuranceIndAdd.insViewObject.insPasViewData.length;
        insuranceIndAdd.resultDomNode = this.resultDomNode;
        insuranceIndAdd.buttonRefNode3 = this.buttonRefNode;
        insuranceIndAdd.flag=true;
        insuranceIndAdd.addExcessFlag=this.addExcessFlag;
        insuranceIndAdd.ewFlag=this.ewflag;
        insuranceIndAdd.insStaticTooltipData=this.insStaticTooltipData;

      },

      buildRendering: function () {
        this.templateString = this.renderTmpl(this.tmpl, this);
        delete this._templateCache[this.templateString];
        this.inherited(arguments);
      },

      postCreate: function () {
        var insuranceIndAdd = this;
        insuranceIndAdd.insuranceInformationOverlay = new tui.widget.booking.insurance.InsuranceInformationOverlay({widgetId: 'insuranceIndAdd', modal: true});
        insuranceIndAdd.insuranceInformationOverlay.open();
        domConstruct.place(this.domNode, dom.byId("insuranceIndAddOverlay"), "only");
        insuranceIndAdd.listenEvents();
        insuranceIndAdd.flag=false;
        this.inherited(arguments);
        var insuranceCheckBox = query('.passengerCB', this.domNode);
        var i = 1, j = 1;
        for(var index =0; index < insuranceCheckBox.length; index++){
			if(insuranceCheckBox[index].value == 'Adult'){
				this.tagElement(insuranceCheckBox[index],"adult"+i+"Check");
				i++;
			}else{
				this.tagElement(insuranceCheckBox[index],"child"+j+"Check");
				j++;
			}
		}
        var insuranceSelect = query('.infant-count', this.domNode);
        for(var index1 =0; index1 < insuranceSelect.length; index1++){
			this.tagElement(insuranceSelect[index1],"adult"+(index1 +1)+"Option");

        }


      },

      listenEvents: function () {
        var insuranceIndAdd = this;
        var arraylength = insuranceIndAdd.insViewObject.insPasViewData.length;
        for (i = 1; i <= insuranceIndAdd.insViewObject.insPasViewData.length; i++) {

        	  if(!insuranceIndAdd.insViewObject.insPasViewData[i-1].child){
         if(this["checkboxId" + i]){
        on(this["checkboxId" + i], "change", lang.hitch(insuranceIndAdd,
            insuranceIndAdd.enableDisableSelect, this["selectDropdown" + i], this["checkboxId" + i],
            insuranceIndAdd.mapNewArray, insuranceIndAdd.displayArray, insuranceIndAdd.counter));
         }
         if(this["selectDropdown" + i]){
          on(this["selectDropdown" + i],"change",lang.hitch(insuranceIndAdd,this.onSelect,
        		  this["selectDropdown" + i],this["checkboxId" + i],i));
         }
          if(!this["checkboxId" + i].checked){
        	  query(this["selectDropdown" + i].listElementUL).query("li").addClass("disabled");
              //query(this["selectDropdown" + i].listElementUL).query("li").removeClass("active");
          }
        	  }
        }
        on(this.insuranceIndAddgq, "click", lang.hitch(insuranceIndAdd, insuranceIndAdd.getQuote));
        on(this.cancelBtn, "click", lang.hitch(insuranceIndAdd, insuranceIndAdd.cancelOverlay, insuranceIndAdd.insuranceInformationOverlay));
      },
      cancelOverlay: function (overlayObj) {
      	overlayObj.close();

      },
      onSelect: function (selectDropdownObj,checkboxObj) {

    	  var selectedAges = selectDropdownObj.getSelectedData().value;

          this.impInfoCheckBox = dom.byId("impInfoCheckbox_Error34");
          if(selectedAges == "NOT APPLICABLE"){

        	  console.log(checkboxObj.checked,"checked...");
        	//  domAttr.remove(checkboxObj,"checked");
        	  checkboxObj.checked=false;
        	  var n = dojo.create("div", null, dom.byId("impInfoCheckbox_Error34"), "first");
        	  domAttr.set(n, "id", "age85");
        	  domAttr.set(this.impInfoCheckBox.id, "innerHTML", this.insViewObject.overAgeMessage);
    	   domClass.remove(query(".error-notation-insurance", this.impInfoCheckBox.domNode)[0], "disNone");



    	   query(selectDropdownObj.listElementUL).query("li").addClass("disabled");
           //query(selectDropdownObj.listElementUL).query("li").removeClass("active");

          }else{
        		domClass.add(query(".error-notation-insurance", this.impInfoCheckBox.domNode)[0], "disNone");

          }


      },

      afterSuccess: function (insViewData,displayArray,response,requestObj) {

        var insuranceIndAdd = this;
        console.log(query(".withoutPrice1").style("display", "none"));
        query("#insuracecontent div").removeClass("mask-interactivity");
        domConstruct.place(this.domNode, dom.byId("insuranceIndAddOverlay"), "only");

        dojo.forEach(insViewData, function (item, i) {
          console.log(item.familyInsPresent)
          if (!item.familyInsPresent) {
            console.log(item.frmPrice, item.excessWaiverViewData.price, "asasa");
            this.currencyAppendedPrice = item.excessWaiverViewData.currencyAppendedPrice;
            this.appendedPrice = item.currencyAppendedFrmPrice;
            this.paxComposition = item.paxComposition;
            this.addPrice = item.frmPrice;
            this.addPaxComposition = item.excessWaiverViewData.price;
            this.excesswaiverDisplay = item.excessWaiverViewData.currencyAppendedPrice;
			insuranceIndAdd.ewFlag = item.excessWaiverViewData.selected;
          }

        });

        var InsuranceIndAddDetailsObj = new tui.widget.booking.insurance.InsuranceIndAddDetails({
          buttonRef: this.buttonRefNode3,
          parentDom: this.domNode,
          familyCode: this.familyCode,
          displayArray1: displayArray,
          cAPrice: currencyAppendedPrice,
          aPrice: appendedPrice,
          map: requestObj,
          pax: paxComposition,
          resultDomNode: this.resultDomNode,
          addPrice: addPrice,
          addPax: addPaxComposition,
          buttonRefNode3: this.buttonRefNode3,
          excesswaiverDisplayFlag:excesswaiverDisplay,
          ewFlag:insuranceIndAdd.ewFlag,
          insStaticTooltipData:this.insStaticTooltipData,
          insViewObject: this.insViewObject,
          jsonData:this.jsonData});
      },

      afterFailure: function () {
        query("#insuracecontent div").removeClass("mask-interactivity");
      },

      enableDisableSelect: function (selectDropdownRefobj, checkboxRefobj, mapNewArray, displayArray, j) {
    	  var insuranceIndAdd = this;
    	  this.impInfoCheckBox = dom.byId("impInfoCheckbox_Error34");
    	  domClass.add(query(".error-notation-insurance", this.impInfoCheckBox.domNode)[0], "disNone");
        if (checkboxRefobj.checked == false) {


          query(selectDropdownRefobj.listElementUL).query("li").addClass("disabled");
          //query(selectDropdownRefobj.listElementUL).query("li").removeClass("active");

        }


        if (checkboxRefobj.checked == true) {
        	if (selectDropdownRefobj.selectNode.value == "NOT APPLICABLE"){
        		selectDropdownRefobj.renderList();
        	}
        		checkboxRefobj.checked=true;
                query(selectDropdownRefobj.listElementUL).query("li").removeClass("disabled");
                //query(selectDropdownRefobj.listElementUL).query("li").addClass("active");


        }

      },
      getQuote: function () {

        var insuranceIndAdd = this,
        	requestObj = {};
        var mf=[];
        var unchekedArray=[];


       // console.log(checkboxRefobj.checked,mapNewArray.toArray().length,this.mapObject2,"checkboxRefobj.checked");

         for(var index=0; index < this.insViewObject.insPasViewData.length ; index++){
        	 if(this["checkboxId" + this.insViewObject.insPasViewData[index].id].checked){

        		 var lf= ""+this.insViewObject.insPasViewData[index].id;
        		 if(!this.insViewObject.insPasViewData[index].child){

        	//	if(this["selectDropdown"  + this.insViewObject.insPasViewData[index].id].getSelectedData().key==="Age 18-64"){
        			mf.push(this.insViewObject.insPasViewData[index].name+" ("+ this["selectDropdown"  + this.insViewObject.insPasViewData[index].id].getSelectedData().key+")");
        		////}
        		//if(this["selectDropdown"  + this.insViewObject.insPasViewData[index].id].getSelectedData().key==="Age 65-74"){
        		//	mf.push(this.insViewObject.insPasViewData[index].name+" ("+ this["selectDropdown"  + this.insViewObject.insPasViewData[index].id].getSelectedData().key+")");
        		//}
        		//if(this["selectDropdown"  + this.insViewObject.insPasViewData[index].id].getSelectedData().key==="Age 75-85"){
        		//	mf.push(this.insViewObject.insPasViewData[index].name+" ("+this["selectDropdown"  + this.insViewObject.insPasViewData[index].id].getSelectedData().key+")");
        		//}


        	     //mf.push(this["selectDropdown"  + this.insViewObject.insPasViewData[index].id].getSelectedData().key);
        		 requestObj[""+lf] = this["selectDropdown"  + this.insViewObject.insPasViewData[index].id].getSelectedData().value;

        		 }else{
        			// console.log(this["label"+this.insViewObject.insPasViewData[index].id].value)
                     if(jsonData.packageType == "FO"){
                    	 mf.push(this.insViewObject.insPasViewData[index].name + " (Age 02-15)");
            			 requestObj[""+lf] = domAttr.get(this["label"+this.insViewObject.insPasViewData[index].id], "value");
                     }
                     else{
        			 mf.push(this.insViewObject.insPasViewData[index].name + " (Age 02-17)");
        			 requestObj[""+lf] = domAttr.get(this["label"+this.insViewObject.insPasViewData[index].id], "value");
                     }
        		 }

        	 }else{
        		 if(!this.insViewObject.insPasViewData[index].child){
        		 unchekedArray.push(this["selectDropdown"  + this.insViewObject.insPasViewData[index].id].getSelectedData().key)
        		 }else{
        			 unchekedArray.push(domAttr.get(this["label"+this.insViewObject.insPasViewData[index].id], "value"))
        		 }
        	 }



         }
      console.log(requestObj,"requestObj...");
         var jsonRequestData = {
                 "insuranceCode": this.familyCode1,
                // "excessWaiver": false,
                // "tAndcAccepted": false,
                 "selected": requestObj
               };
         insuranceIndAdd.impInfoCheckBox = dom.byId("impInfoCheckbox_Error34");
        if(unchekedArray.length==this.insViewObject.insPasViewData.length){

        	 domAttr.set(insuranceIndAdd.impInfoCheckBox.id, "innerHTML", "Please Select atleast one Passenger");
        	 domClass.remove(query(".error-notation-insurance", this.impInfoCheckBox.domNode)[0], "disNone");
        }else{
        	domClass.add(query(".error-notation-insurance", this.impInfoCheckBox.domNode)[0], "disNone");
         var results = xhr.post({
             url: this.targetUrl,
             content: {insuranceCriteria: dojo.toJson(jsonRequestData)},
             handleAs: "json",
             error: function (err) {
               if (dojoConfig.devDebug) {
               }
             }
           });

           dojo.when(results, function (response) {

             insuranceIndAdd.afterSuccess(response.insuranceContainerViewData.insViewData,mf,response,requestObj);
           });
        }
        }

    });


  });