define("tui/widget/booking/worldcarefund/view/WorldCareFundHandleCheckbox", [
                                                           "dojo/_base/declare",
                                                           "dijit/_WidgetBase",
															"tui/widget/mixins/Templatable",
															"dojox/dtl/_Templated",
															'dojo',
															"dojo/on",
															'dojo/query',
															'dojo/dom-class',
															"dojo/dom-style",
															"dojo/_base/lang",
															"dojo/dom",
															"dojo/dom-construct",
															"dojo/text!tui/widget/booking/worldcarefund/view/template/WorldCareFundTmpl.html",
															"tui/widget/booking/constants/BookflowUrl",
															"dojo/topic",
															"dojo/html",
															"dojox/dtl",
															"dojox/dtl/Context",
															"tui/widget/_TuiBaseWidget",
					                                        "tui/widget/booking/Expandable"],
    function(declare, _WidgetBase, Templatable,dtlTemplated,dojo,on, query, domClass,domStyle,lang,dom,domConstruct,WorldCareFundTmpl,BookflowUrl,topic,html) {
	return declare("tui.widget.booking.worldcarefund.view.WorldCareFundHandleCheckbox",[tui.widget._TuiBaseWidget,_WidgetBase,dtlTemplated,Templatable], {


		tmpl:WorldCareFundTmpl,
		checkboxId:null,
		tempVar: null,
		ajaxObjArray:[],
		buildRendering: function(){
			this.templateString = this.renderTmpl(this.tmpl, this);
			delete this._templateCache[this.templateString];
			this.inherited(arguments);
		},
		postCreate: function(){
			this.controller = dijit.registry.byId("controllerWidget");
			this.controller.registerView(this);
			dojo.parser.parse(this.domNode);
			this.inherited(arguments);
			this.tagElements(dojo.query('.wcff1', this.domNode),"World care fund donation component");

			 for(var i=0 ;i<jsonData.extraFacilityViewDataContainer.donationOptions.extraFacilityViewData.length;i++)
				 {
				 this.ajaxObjArray.push(jsonData.extraFacilityViewDataContainer.donationOptions.extraFacilityViewData[i].code);
				 }
			 var ajaxObj = this.ajaxObjArray.toString();
			 var extraCategory  = jsonData.extraFacilityViewDataContainer.donationOptions.extraFacilityCategoryCode;

			on(this.worldCareCheckbox,"click",lang.hitch(this, this.handleWorldCareFundCheckbox,this.worldCareCheckbox,ajaxObj,extraCategory));
		},
		handleWorldCareFundCheckbox:function(worldCareCheckbox,ajaxObj,extraCategory){
			var value=worldCareCheckbox.checked;
			this.checkboxId=ajaxObj;
			this.generateRequest(value,extraCategory);
		},
		generateRequest: function(value,extraCategory){
			var field="worldCare";
			if (value == true){
				var requestData={extraCategory:extraCategory,donationFacilityCode:this.checkboxId};
				var url = BookflowUrl.worldcarefundurl;
				this.controller.generateRequest(field,url,requestData);
			}
			else {
				var requestData={extraCategory:extraCategory,donationFacilityCode:""};
				var url = BookflowUrl.worldcarefundurl;
				this.controller.generateRequest(field,url,requestData);
			}
		},
		refresh: function(field, response){
       		console.log(field);
			console.log(response);

		}
	});
});






