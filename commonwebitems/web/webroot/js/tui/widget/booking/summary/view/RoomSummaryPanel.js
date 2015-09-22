define("tui/widget/booking/summary/view/RoomSummaryPanel", [
  "dojo/_base/declare",
  "dojo/query",
  "dojo/dom-class",
  "dojo/dom-attr",
  "tui/widget/booking/bookflowMsgs/nls/Bookflowi18nable",
  "dojo/text!tui/widget/booking/summary/view/templates/RoomSummaryPanel.html",
  "dojo/dom-construct",
  "dojo/parser",
  "tui/widget/mixins/Templatable"
], function (declare, query, domClass, domAttr, Bookflowi18nable,RoomSummaryPanel, domConstruct, parser, Templatable) {
  return declare("tui.widget.booking.summary.view.RoomSummaryPanel", [tui.widget._TuiBaseWidget,Templatable,Bookflowi18nable], {
    tmpl: RoomSummaryPanel,
    controller : null,

    postCreate: function () {
      this.controller = dijit.registry.byId("controllerWidget");
      this.controller.registerView(this);
      this.initBookflowMessaging();

      this.packageTypeValue = this.jsonData.packageType;
      this.renderWidget();
      this.inherited(arguments);
      this.tagElements(query('a.changeRoom'),"changeRoomBoard");
      this.tagElements(query('.summaryAccordSwitch'),"RoomandBoard");
    },

    refresh: function (field, response) {
      this.jsonData = response;

      this.renderWidget();
      if(field == 'boardBasis'){
    	 this.updateAccomURL(response);
      }
    },

    updateAccomURL:function(response){
    	var backToHotelLinks = query("#indicators .indicator.first a, .back-to-review");
    	if(backToHotelLinks.length != 0){
    		_.each(backToHotelLinks,function(link){
    			domAttr.set(link, 'href',response.summaryViewData.summaryPanelComponentViewData[0].summaryPanelUrlsViewData.url);
    		});
    	}
    },

    renderWidget: function () {
    	var widget = this;
    	var packageTypeTempVar = _.isEmpty(this.jsonData.packageType) ? this.packageTypeValue:this.jsonData.packageType;
    	this.accomViewData = this.controller.refData(this.jsonData.packageViewData.accomViewData, this.bookflowMessaging[dojoConfig.site].brandMapping[packageTypeTempVar].roomSectionMetaData.roomSummary);


        this.imageDisplRefMeta = this.bookflowMessaging[dojoConfig.site].brandMapping[packageTypeTempVar].roomSectionMetaData.roomImageDisp;


        if(!_.isEmpty(this.accomViewData.featureCodesAndValues)){
        	this.ratingCount = _.first(this.accomViewData.featureCodesAndValues.tRating)
        }



    	this.imageDispFlag =  true;
    	if (this.jsonData.summaryViewData.currentPage == 'passengerdetails' || this.jsonData.summaryViewData.currentPage == 'confirmation'){

    		this.imageDispFlag = false;

    	}


    	var html = widget.renderTmpl(widget.tmpl, widget);
      domConstruct.place(html, this.domNode, "only");
      parser.parse(this.domNode);
    }

  });
});