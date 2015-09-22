define("tui/widget/amendncancel/bookingOverviewPage/FlightDetailsSectionComponentPanel", [
  "dojo/_base/declare",
  "dojo/dom-construct",
  "dojo/parser",
  "dojo/text!tui/widget/amendncancel/bookingOverviewPage/templates/FlightDetailsSectionComponentPanel.html",
  "tui/widget/_TuiBaseWidget",
  "dojox/dtl/_Templated",
  "tui/widget/mixins/Templatable",
  "tui/widget/expand/Expandable",
  "tui/widget/expand/subExpandable",
  "tui/widget/common/ClickToolTip"
  ], function (declare,domConstruct,parser,FlightDetailsSectionTmpl, _TuiBaseWidget, dtlTemplate, Templatable) {

  return declare("tui.widget.amendncancel.bookingOverviewPage.FlightDetailsSectionComponentPanel",
    [_TuiBaseWidget, dtlTemplate, Templatable], {

      tmpl: FlightDetailsSectionTmpl,
      templateString: "",
      widgetsInTemplate: true,
      flightData:null,	  
	  staticData:null,
	  packageData:null,
	  bookingRefereneceId:null,
         
      buildRendering: function () {
        this.templateString = this.renderTmpl(this.tmpl, this);
        delete this._templateCache[this.templateString];
        this.inherited(arguments);
      },
      
      postMixInProperties: function(){
    	  this.flightData = this.jsonData.packageViewData.flightViewData ;
		  this.packageData = this.jsonData.packageViewData ;
		  this.staticData = this.jsonData.manageBookingSummaryContentViewData.manageBookingSummaryContentMap;        	  
      },

      postCreate: function () {
           this.inherited(arguments);
      }

    });
});