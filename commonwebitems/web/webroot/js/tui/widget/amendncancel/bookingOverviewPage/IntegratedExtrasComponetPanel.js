define("tui/widget/amendncancel/bookingOverviewPage/IntegratedExtrasComponetPanel", [
  "dojo/_base/declare",
  "dojo/dom-construct",
  "dojo/parser",
  "dojo/text!tui/widget/amendncancel/bookingOverviewPage/templates/IntegratedExtrasComponetPanelTmpl.html",
  "tui/widget/_TuiBaseWidget",
  "dojox/dtl/_Templated",
  "tui/widget/mixins/Templatable",
  "tui/widget/expand/Expandable",
  "tui/widget/expand/subExpandable",
  "tui/widget/common/ClickToolTip"
  ], function (declare,domConstruct,parser,IntegratedExtrasComponetPanel, _TuiBaseWidget, dtlTemplate, Templatable) {

  return declare("tui.widget.amendncancel.bookingOverviewPage.IntegratedExtrasComponetPanel",
    [_TuiBaseWidget, dtlTemplate, Templatable], {

      tmpl: IntegratedExtrasComponetPanel,
      templateString: "",
      widgetsInTemplate: true,
      accommodationData:null,
	  extraFacilityData:null,
	  staticData:null,
	  groups:["INSURANCE","PACKAGE","ACCOMMODATION"],
         
      buildRendering: function () {
        this.templateString = this.renderTmpl(this.tmpl, this);
        delete this._templateCache[this.templateString];
        this.inherited(arguments);
      },
      
      postMixInProperties: function(){   	  
		  
		  var len = this.jsonData.packageViewData.extraFacilityCategoryViewData.length;
		  for(var i=0; i < len; i++){
			var group = "";
			group = this.jsonData.packageViewData.extraFacilityCategoryViewData[i].extraFacilityGroup;
			if(dojo.indexOf(this.groups, group) != -1){
			this.jsonData.packageViewData.extraFacilityCategoryViewData[i].extraFacilityGroup = "integratedGroup";
			}
		  }
		  this.extraFacilityData = this.jsonData.packageViewData.extraFacilityCategoryViewData;
		 
		  this.staticData = this.jsonData.manageBookingSummaryContentViewData.manageBookingSummaryContentMap;     	  
      },

      postCreate: function () {
           this.inherited(arguments);
      }

    });
});