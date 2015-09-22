define("tui/widget/amendncancel/bookingOverviewPage/AccommodationDetailsSectionComponentPanel", [
  "dojo/_base/declare",
  "dojo/dom-construct",
  "dojo/parser",
  "dojo/text!tui/widget/amendncancel/bookingOverviewPage/templates/AccommodationDetailsSectionTmpl.html",
  "tui/widget/_TuiBaseWidget",
  "dojox/dtl/_Templated",
  "tui/widget/mixins/Templatable",
  "tui/widget/expand/Expandable",
  "tui/widget/expand/subExpandable",
  "tui/widget/common/ClickToolTip"
  ], function (declare,domConstruct,parser,AccommodationDetailsSectionTmpl, _TuiBaseWidget, dtlTemplate, Templatable) {

  return declare("tui.widget.amendncancel.bookingOverviewPage.AccommodationDetailsSectionComponentPanel",
    [_TuiBaseWidget, dtlTemplate, Templatable], {

      tmpl: AccommodationDetailsSectionTmpl,
      templateString: "",
      widgetsInTemplate: true,
      accommodationData:null,
	  extraFacilityCategories:null,
	  staticData:null,
	  showAccoExtrasHeading:null,
         
      buildRendering: function () {
        this.templateString = this.renderTmpl(this.tmpl, this);
        delete this._templateCache[this.templateString];
        this.inherited(arguments);
      },
      
      postMixInProperties: function(){
    	  this.accommodationData = this.jsonData.packageViewData.availableAccommodationViewData;		  	  
		  this.extraFacilityCategories = this.jsonData.packageViewData.extraFacilityCategoryViewData;
		  this.staticData = this.jsonData.manageBookingSummaryContentViewData.manageBookingSummaryContentMap;
		  this.showAccoExtrasHeading = this.isAccomExtrasOrSecialReqPresent();	     	  
      },
	  isAccomExtrasOrSecialReqPresent: function(){
		 if(this.accommodationData.specialReqest.length){
			return true;
		 }
		 if(this.extraFacilityCategories.length){
			for(var i=0; i< this.extraFacilityCategories.length; i++){
				if(this.extraFacilityCategories[i].extraFacilityGroup =='ACCOMMODATION'){
					return true;
				}
			}
		 }		 
		return false;
	  },
	
      postCreate: function () {
           this.inherited(arguments);
      }

    });
});