define("tui/widget/booking/infantoptions/view/InfantSelectOptionView", [
   "dojo/_base/declare",
   "dojo/dom",
   "dojo/query",
   "dojo/on",
   "dojo/dom-attr",
   "dojo/dom-construct",
   "dojo/dom-class",
   "dojo/dom-style",
   "tui/widget/_TuiBaseWidget",
   "dojox/dtl/_Templated",
   "tui/widget/mixins/Templatable",
   "dojo/topic",
   "dojo/_base/lang",
   "dojo/text!tui/widget/booking/infantoptions/view/templates/InfantSelectOptionsView.html",
   "tui/widget/booking/constants/BookflowUrl",
   "dojo/_base/json",
   "tui/widget/booking/bookflowMsgs/nls/Bookflowi18nable",
   "tui/widget/form/SelectOption"

   ], function( declare,dom,query,on,domAttr,domConstruct,domClass,domStyle, _TuiBaseWidget,dtlTemplate, Templatable,topic,lang,
		   InfantSelectOptionsView,BookflowUrl,jsonUtil) {

	return declare('tui.widget.booking.infantoptions.view.InfantSelectOptionView', [_TuiBaseWidget, dtlTemplate, Templatable,tui.widget.booking.bookflowMsgs.nls.Bookflowi18nable], {

		tmpl: InfantSelectOptionsView,
		templateString: "",
		widgetsInTemplate : true,
		classActFlag:false,

		postMixInProperties: function () {
			  if(this.jsonData.extraFacilityViewDataContainer.workshopOptions && this.jsonData.extraFacilityViewDataContainer.workshopOptions.available == true){
		    	  this.classActFlag=true;
		      }
			var infantData = this.infantSelectOptionData.extraFacilityViewData;
				for ( var index = 0; index < infantData.length; index++) {
					var extrasStr = "";
					for (var num = 0; num <= infantData[index].quantity; num++) {
						extrasStr = extrasStr + num;
				}
					infantData[index].quantityArray = extrasStr;


			var infantOption = this.infantSelectOptionData.extraFacilityViewData[index];

			infantOption["contentValue"] = this.contentObject(this.infantSelectOptionData.extraFacilityViewData[index].code);
			}
			this.inherited(arguments);
		},
		contentObject : function(code){

			var contentObject ={

			};
			this.initBookflowMessaging();
			var equipmentKey = this.bookflowMessaging["InfantFacilities"];
			var equipment=equipmentKey[code];
			if(code === "LEN"){
				 contentObject["displayName"] = _.isUndefined(this.contentObj.contentMap[equipment+"_Key_Value_One_Title"]) ? " " : this.contentObj.contentMap[equipment+"_Key_Value_One_Title"];
				 contentObject["strapLine"] = _.isUndefined(this.contentObj.contentMap[equipment+"_Key_Value_One_Body"]) ? " " : this.contentObj.contentMap[equipment+"_Key_Value_One_Body"];
			}else{
				 contentObject["displayName"] = _.isUndefined(this.contentObj.contentMap[equipment+"_displayName"]) ? " " : this.contentObj.contentMap[equipment+"_displayName"];
				 contentObject["strapLine"] = _.isUndefined(this.contentObj.contentMap[equipment+"_StrapLine"]) ? _.isUndefined(this.contentObj.contentMap[equipment+"_Key_Value_One_Body"])? this.contentObj.contentMap[equipment+"_Intro"]:this.contentObj.contentMap[equipment+"_Key_Value_One_Body"] : this.contentObj.contentMap[equipment+"_StrapLine"];
				 contentObject["imageUrl"] = _.isUndefined(this.contentObj.contentMap[equipment+"_Key_Value_One_small"]) ? " " : this.contentObj.contentMap[equipment+"_Key_Value_One_small"];
			}

		return contentObject;
		},

		buildRendering: function(){
			this.templateString = this.renderTmpl(this.tmpl, this);
			delete this._templateCache[this.templateString];
			this.inherited(arguments);
		},

		postCreate: function() {
			this.controller = dijit.registry.byId("controllerWidget");
			this.selectionAttachPoints = query('select[data-dojo-attach-point$="SelectionBox"]', this.domNode);
			this.attachEvents();
			this.handleSelectionBox();
			this.inherited(arguments);
			this.tagElements(dojo.query('.select-infant-equipment', this.domNode),"Pre-book infant equipment");
			this.tagElements(dojo.query('.button', this.domNode),"Pre-book infant equipment");
		},

		attachEvents : function(){
			for(var index= 0;  index < this.infantSelectOptionData.extraFacilityViewData.length; index++) {
				var infantOptions = this.infantSelectOptionData.extraFacilityViewData[index];
					on(this[infantOptions.code+"SelectionBox"], "change", lang.hitch(this, this.handleSelectionBox, this[infantOptions.code+"SelectionBox"]));
			}
			on(this.infantAddButton, "click", lang.hitch(this, this.handleAddButton));
		},

		handleSelectionBox: function (selectionBox) {
			this.totalPrice = 0;
			this.impInfoCheckBox = dom.byId("infant-add-button");
			for(var index= 0;  index < this.infantSelectOptionData.extraFacilityViewData.length; index++) {
				var infantOptions = this.infantSelectOptionData.extraFacilityViewData[index];
				var noOfItems = this[infantOptions.code+"SelectionBox"].getSelectedData().value;
				var prices=infantOptions.currencyAppendedPerPersonPrice.slice(1);
				var priceNumber= parseInt(prices);
				this.totalPrice = this.totalPrice + (noOfItems * priceNumber );
			}
			domAttr.set(this.totalInfantPrice, "innerHTML", dojoConfig.currency +  this.totalPrice.toFixed(2));
		},

		handleAddButton : function() {
			var responseObj =[];
			_.each(this._attachPoints, lang.hitch(this, function(attachPoint){
				var selectionAttachPoint= this[attachPoint];
				if(selectionAttachPoint instanceof tui.widget.form.SelectOption){
					var keyValue = selectionAttachPoint.dojoAttachPoint;
					var key=keyValue.slice(0,3);
					var value = selectionAttachPoint.getSelectedData().value;
					responseObj.push({
				           	"extraCode": key,
				            "quantity": value,
				            "extraCategoryCode":this.infantSelectOptionData.extraFacilityCategoryCode
				          });
				}

			}));
			var url = this.url;
			var requestData={"equipments":jsonUtil.toJson(responseObj)};
			this.controller.generateRequest("infant",url,requestData);

		}

	});
});