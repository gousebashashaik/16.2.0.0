
define('tui/widget/booking/passengers/view/PassengerDetailsComponentPanel', [
  "dojo/_base/declare",
  "dojo/query",
  "dojo/_base/array",
  "dojo/topic",
  "dojo/dom",
  "dojo/on",
  "dojo/dom-attr",
  "dojo/_base/lang",
  "dojo/dom-style",
  "dojo/dom-class",
  "tui/widget/_TuiBaseWidget",
  "dojox/dtl/_Templated",
  "tui/widget/mixins/Templatable",
  "dojo/text!tui/widget/booking/passengers/view/templates/PassengerDetailsComponentPanel.html",
  "dojo/dom-construct",
  "dojo/store/Memory",
  "tui/widget/booking/passengers/PassengerUtils",
  "tui/widget/booking/passengers/view/PassengerDetailsView",
  "tui/widget/booking/passengers/view/PassengerRoomAllocationView",
  "tui/widget/booking/passengers/view/PassengerActivityView",
  "tui/widget/booking/passengers/view/PassengerExcursionView",
  "tui/widget/booking/passengers/view/PassengerTravelInsuranceView"
], function(declare, query, arrayUtils, topic,dom,on, domAttr, lang, domStyle,  domClass,  _TuiBaseWidget,dtlTemplate,
		Templatable, PassengerDetailsComponentPanel,domConstruct, Memory, PassengerUtils) {

	return declare('tui.widget.booking.passengers.view.PassengerDetailsComponentPanel', [_TuiBaseWidget, dtlTemplate, Templatable], {

		tmpl: PassengerDetailsComponentPanel,
		templateString: "",
		widgetsInTemplate : true,
		passengerStore : null,
    insuranceCountMap : [],
    passenengerSelectionBoxes: [],
    insuranceExists : false,

	postMixInProperties: function () {

      if(this.jsonData.passengerInsuranceViewData && this.jsonData.passengerInsuranceViewData.passengerDataList){
        this.insuranceExists = true;
        this.jsonData.passengerInsuranceViewData.insSelectionData = [];
        this.jsonData.passengerInsuranceViewData.insChildSelectionData = {};
        for ( var index = 0; index < this.jsonData.passengerInsuranceViewData.passengerDataList.length; index++) {
          var paxInsData =this.jsonData.passengerInsuranceViewData.passengerDataList[index];
          var insStr = "";
          for (var num = 1; num <= paxInsData.noOfPaxsInsSelected; num++) {
            insStr = insStr + num;
          }
          paxInsData.insSelectionData = insStr;
          if(paxInsData.ageCode !== "CHILD") {
            this.jsonData.passengerInsuranceViewData.insSelectionData.push({
              "label" : paxInsData.displayText,
              "value" : paxInsData.ageCode,
              "minAge" : paxInsData.minAge,
              "maxAge" :  paxInsData.maxAge
            });
          }else {
            this.jsonData.passengerInsuranceViewData.insChildSelectionData = {
              "label" : paxInsData.displayText,
              "value" : paxInsData.ageCode,
              "noOfPaxsInsSelected" : paxInsData.noOfPaxsInsSelected
            };
          }
          this.insuranceCountMap.push({
            "key" : paxInsData.ageCode,
            "totalCount" :  paxInsData.noOfPaxsInsSelected,
            "selectedArray" : []
          });
        }
      }
    },

    buildRendering: function(){
    	this.inboundFlag = true;
    	// need to refer in jsp for flight index value
    	 this.flightViewData = dijit.registry.byId("controllerWidget").refData(this.jsonData.packageViewData.flightViewData, 0);
         if (_.isEmpty(this.flightViewData.inboundSectors)){
         	this.inboundFlag = false;
         }
		this.templateString = this.renderTmpl(this.tmpl, this);
		this.inherited(arguments);
	},

		postCreate: function() {
			this.expandleView = query("div[data-dojo-attach-point='impInfoExpandable']")[0] ;
		 	this.inherited(arguments);
		 	this.errorDiv = null;
		 	this.submitButtonDomNode = dom.byId("passengerDetailsSubmitButton");
		 	this.buttonTooltip = dom.byId("buttonTooltip");
		 	this.errorDivStr = "<div class='error-message-section oops-error'> "+
		    "<h3 class='error-message-text'>Oops, some details haven't been entered or are invalid</h3>" +
		    "<h5 class='error-message-subText'>Missing and invalid details are highlighted above</h5>" +
		    "</div>";
		 	var passengerDetailsData = [];
		 	this.impInfoCheckbox = dom.byId("impInfoCheckbox");
		 	this.impInfoCheckboxLabel = query("label", this.impInfoCheckbox.parentElement)[0];
      if(this.expandleView){
        this.impInfoDivView = dijit.byId(this.expandleView.id);
      }

		 	for(var index = 0; index <jsonData.packageViewData.passenger.length; index++){
		 		passengerDetailsData.push({"id" : index, "passengerData" : this["passengerDetailsView"+index].passengerModel});
        var selectionBox = this["passengerDetailsView"+index].insuranceSelect,
            selectInsBlock = this["passengerDetailsView"+index].selectInsBlock;
        if(!selectionBox) {
          selectionBox = this["passengerDetailsView"+index].insuranceChildSelect;
        }
        if(selectionBox && selectInsBlock) {
          this.passenengerSelectionBoxes.push({
            selectBox: selectionBox,
            divBlock : selectInsBlock
          });
        }

		 	}

		 		if(this.jsonData.packageViewData.memoViewData.available == true){
		 		if(this.impInfoCheckbox.checked == true ){
		 			this.impInfoCheckbox.checked = false;
		 		}
		 	}

		 	on(this.impInfoCheckbox, "click", lang.hitch(this, this.handleImpInfoCheckBox));
		 	this.passengerStore = new Memory({data: passengerDetailsData});
		 	if(this.errors) {
		 		arrayUtils.forEach(this.errors, lang.hitch(this, function(errorObj) {
		 			var fieldName = errorObj.field;
		 			if(errorObj.field.indexOf("year") !== -1 ) {
		 				fieldName = fieldName.replace("year", "dob");
		 			}
		 			var domObj = query("input[name='"+fieldName+"']")[0];
		 			if(domObj){
		 				var widget = dijit.byId(domObj.id);
			 			if(widget && widget.serverValidator){
			 				widget.serverValidator(errorObj.defaultMessage);
			 			}
		 			}

		 		}));
		 	}
		},

    validateInsurance : function() {
      var emptySelectionList = [],
          valid = true;
      _.each(this.insuranceCountMap , lang.hitch(this, function (item) {
        item.selectedArray =[];
      }));
      _.each(this.passenengerSelectionBoxes , lang.hitch(this, function (obj, index) {
        if(obj.selectBox.getSelectedData().value === ""){
          emptySelectionList.push(obj.selectBox);
        }else {
          _.each(this.insuranceCountMap , lang.hitch(this, function (item) {
            if(item.key === obj.selectBox.getSelectedData().value) {
              item.selectedArray.push(obj.selectBox);
            }
          }));
        }
      }));
      _.each(this.insuranceCountMap , lang.hitch(this, function (item) {
        if(item.selectedArray.length !== parseInt(item.totalCount,10)) {
          valid = false;
        }
      }));
      if(!valid){
        _.each(this.passenengerSelectionBoxes , lang.hitch(this, function (obj, index) {
          var innerBox = query("input", obj.divBlock)[0];
          domClass.remove(innerBox, "selectOptionValidInnerBox");
          domClass.add(query("a", obj.selectBox.domNode)[0], "selectOptionError");
          domClass.add(innerBox, "selectOptionErrorInnerBox");
          domAttr.set(innerBox, "value", "");
          PassengerUtils.displayMessagesForSelect(obj.selectBox.message, obj.selectBox, obj.divBlock);
          domStyle.set(obj.divBlock, "display", "inline");
        }));

      } else {
        _.each(this.passenengerSelectionBoxes , lang.hitch(this, function (obj, index) {
          var innerBox = query("input", obj.divBlock)[0];
          domClass.remove(query("a", obj.selectBox.domNode)[0], "selectOptionError");
          domClass.remove(innerBox, "selectOptionErrorInnerBox");
          domClass.add(innerBox, "selectOptionValidInnerBox");
          PassengerUtils.displayMessagesForSelect("", obj.selectBox, obj.divBlock);
          domStyle.set(obj.divBlock, "display", "inline");
        }));
      }
      return valid;
    },

		showinvalidTooltip : function() {

		},

		handleImpInfoCheckBox : function () {
			var impinfo=query(".checkedconfirmed");
			var showbt=query(".showbutton");
			if(this.impInfoCheckbox.checked){
				this.displayMessagesForCheckBox("");
				//domClass.remove(query(this.impInfoDivView.itemSelector,this.impInfoDivView.domNode)[0], "open");

				 _.each(showbt, lang.hitch(this, function (showbts) {
			          domClass.remove(showbts,'disNone');

			        }));
				 _.each(impinfo, lang.hitch(this, function (impinfos) {
			          domClass.remove(impinfos,'disNone');
			        }));

				//this.impInfoDivView.transition.animating = false;
			}else {
				 _.each(impinfo, lang.hitch(this, function (impinfos) {
			       domClass.add(impinfos,'disNone');

			   }));
				 _.each(showbt, lang.hitch(this, function (showbts) {
              domClass.add(showbts,'disNone');
              domAttr.set(showbt, "innerHTML", "Show");
			    }));

				this.displayMessagesForCheckBox("Please confirm that you have read the important information");

			}
		},


		displayMessagesForCheckBox: function(message) {
			if(message){
				if(this.impInfoCheckbox.errorTextBoxNode && this.impInfoCheckbox.errorTextBoxNode !== null ){
					domAttr.set(this.impInfoCheckbox.id+"_Error", "innerHTML", message);
				}else {
					this.impInfoCheckbox.errorTextBoxNode = "<div id= '"+this.impInfoCheckbox.id+"_Error' class='error-notation'>"+message+"</div>";
					domConstruct.place(this.impInfoCheckbox.errorTextBoxNode, this.impInfoCheckboxLabel,"after");
				}
				domClass.add(this.impInfoCheckboxLabel, "error-border");
			}else{
				if(this.impInfoCheckbox.errorTextBoxNode && this.impInfoCheckbox.errorTextBoxNode !== null){
					domConstruct.destroy(this.impInfoCheckbox.id+"_Error");
					this.impInfoCheckbox.errorTextBoxNode = null;
				}
				domClass.remove(this.impInfoCheckboxLabel, "error-border");
			}
		},

    isPassengerDetailsValid : function(){
      var valid = true;
      for(var index = 0; index < jsonData.packageViewData.passenger.length; index++){
        if(this["passengerDetailsView"+index].isValid() === false){
          valid = false;
        }
      }
      return valid;
    },


		isValid: function( ) {
			var valid = true,
          passengerViewValidation = true,
          insuranceValidation = true;
			var mouseOverEvent = null;
			if(this.impInfoCheckbox && !this.impInfoCheckbox.checked) {
				this.handleImpInfoCheckBox();
				valid = false;
			}
      passengerViewValidation = this.isPassengerDetailsValid();
      if(!passengerViewValidation){
        valid = passengerViewValidation;
      }
      insuranceValidation = this.validateInsurance();
      if(this.insuranceExists && !insuranceValidation){
        valid = insuranceValidation;
      }
			if(!valid){
				if(!query(".oops-error")[0]){
					this.errorDivStr = "<div class='error-message-section oops-error'> "+
					"<h3 class='error-message-text'>Oops, some details haven't been entered or are invalid</h3>" +
					"<h5 class='error-message-subText'>Missing and invalid details are highlighted above</h5>" +
					"</div>";
					this.errorDiv = domConstruct.place(this.errorDivStr, query("div[data-dojo-attach-point='impInfoExpandable']")[0],"after");

			}
				/*start - While sumbit FORM - if empty field is there we showing section if its not expand */
				this.impExpandleView = query("div[data-dojo-attach-point='impInfoExpandable']")[0] ;
				this.passengerExpandleView = query("div[data-dojo-attach-point='passengerInfoExpandable']")[0] ;

			    if(this.impExpandleView){
			    	this.impInfoDivView = dijit.byId(this.impExpandleView.id);
				}
			    if(this.passengerExpandleView){
			        this.passengerInfoDivView = dijit.byId(this.passengerExpandleView.id);
			    }
				domClass.add(query(this.impInfoDivView.itemSelector,this.impInfoDivView.domNode)[0], "open");
				domStyle.set(query(this.impInfoDivView.itemContentSelector,this.impInfoDivView.domNode)[0], "display", "block");

				domClass.add(query(this.passengerInfoDivView.itemSelector,this.passengerInfoDivView.domNode)[0], "open");
				domStyle.set(query(this.passengerInfoDivView.itemContentSelector,this.passengerInfoDivView.domNode)[0], "display", "block");
				/* end - While sumbit FORM - if empty field is there we showing section if its not expand */

			}else {
				if(query(".oops-error")[0]){
					domConstruct.destroy(query(".oops-error")[0]);
					this.mouseOverEvent.remove();
				}
			}

		    _.each(query(".selectOptionErrorInnerBox", this.domNode), lang.hitch(this, function (errorNotation){
					domStyle.set(errorNotation, "display", "block");
				}));

			return valid;

		}


	});
});