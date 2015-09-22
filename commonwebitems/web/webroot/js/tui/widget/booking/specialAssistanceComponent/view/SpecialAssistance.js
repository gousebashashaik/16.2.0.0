define("tui/widget/booking/specialAssistanceComponent/view/SpecialAssistance", [
  "dojo/_base/declare",
  "dojo",
  "dojo/dom-construct",
  "dojo/parser",
  "dojo/text!tui/widget/booking/specialAssistanceComponent/view/templates/SpecialAssistanceTmpl.html",
  "dojo/html",
  "dojo/Evented",
  "tui/widget/_TuiBaseWidget",
  "dojox/dtl/_Templated",
  "tui/widget/mixins/Templatable",
  "tui/widget/expand/Expandable",
  "tui/widget/booking/bookflowMsgs/nls/Bookflowi18nable"
  ], function (declare,dojo,domConstruct,parser,SpecialAssistanceTmpl, html, Evented, _TuiBaseWidget, dtlTemplate, Templatable, Expandable) {

  return declare("tui.widget.booking.specialAssistanceComponent.view.SpecialAssistance",
    [_TuiBaseWidget, dtlTemplate, Templatable, Evented,tui.widget.booking.bookflowMsgs.nls.Bookflowi18nable], {

      tmpl: SpecialAssistanceTmpl,
      templateString: "",
      widgetsInTemplate: true,
      thirrdPartyFlight: false,

      postMixInProperties: function () {
    	  this.sitename = dojoConfig.site;
      },

      buildRendering: function () {
    	this.thirrdPartyFlight = false;//this.jsonData.packageViewData.multicomThirdPartyFlight;
		this.initBookflowMessaging();
		var siteObj = this.bookflowMessaging[dojoConfig.site];
		this.indexValue = 1;
		this.getCurrentPage	= this.jsonData.summaryViewData.currentPage;


		if(this.getCurrentPage != "flightoptions"){
			this.sectionTitle = siteObj[this.compType].title;
			this.sectionContent1 = '';
			this.sectionContent2 =this.jsonData.roomOptionsContentViewData.roomContentViewData.contentMap.cruise_special_assistance_displayName;
			this.sectionContent3 =this.jsonData.roomOptionsContentViewData.roomContentViewData.contentMap.cruise_special_assistance_intro1;
		}else{
			this.sectionTitle = siteObj[this.compType].title;
			this.sectionContent1 = siteObj[this.compType].description1;
			this.sectionContent2 = siteObj[this.compType].description2;
			this.sectionContent3= siteObj[this.compType].description3;
		}
		this.templateString = this.renderTmpl(this.tmpl, this);
        delete this._templateCache[this.templateString];
        this.inherited(arguments);

      },

      postCreate: function () {

      }

    });
});