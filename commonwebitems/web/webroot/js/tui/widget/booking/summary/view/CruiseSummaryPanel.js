define("tui/widget/booking/summary/view/CruiseSummaryPanel", [
  "dojo/_base/declare",
  "dojo/query",
  "dojo/dom-class",
  "tui/widget/booking/bookflowMsgs/nls/Bookflowi18nable",
  "dojo/text!tui/widget/booking/summary/view/templates/CruiseSummaryPanel.html",
  "dojo/text!tui/widget/booking/summary/view/templates/CruiseBackToBackSummaryPanel.html",
  "dojo/dom-construct",
  "dojo/parser",
  "tui/widget/mixins/Templatable"
], function (declare, query, domClass, Bookflowi18nable, CruiseSummaryPanel, CruiseBackToBackSummaryPanel, domConstruct, parser, Templatable) {
  return declare("tui.widget.booking.summary.view.CruiseSummaryPanel", [tui.widget._TuiBaseWidget,Templatable,Bookflowi18nable], {
    tmpl: CruiseSummaryPanel,
    controller : null,
    firstCruise:"",
    secondCruise:"",



    postCreate: function () {
      this.controller = dijit.registry.byId("controllerWidget");
      this.initBookflowMessaging();
      this.controller.registerView(this);
      this.packageTypeValue = this.jsonData.packageType;
      this.renderWidget();
      this.inherited(arguments);




    },

    refresh: function (field, response) {
      this.jsonData = response;
      this.renderWidget();
    },

    renderWidget: function () {
    	var widget = this;

    	var packageTypeTempVar = _.isEmpty(this.jsonData.packageType) ? this.packageTypeValue:this.jsonData.packageType;

    	this.accomViewData = this.controller.refData(this.jsonData.packageViewData.accomViewData, this.bookflowMessaging[dojoConfig.site].brandMapping[packageTypeTempVar].cruiseSummary);
    	this.flightViewData = this.controller.refData(this.jsonData.packageViewData.flightViewData, this.flightIndexValue);

    	this.listOfAccomViewData= this.getAllData(this.jsonData.packageViewData.accomViewData, this.bookflowMessaging[dojoConfig.site].brandMapping[packageTypeTempVar].accomIndexVal);


    	 this.imageDispFlag =  true;
    	if (this.jsonData.summaryViewData.currentPage == 'passengerdetails' || this.jsonData.summaryViewData.currentPage == 'confirmation'){

    		this.imageDispFlag = false;

    	}
    	if(this.packageTypeValue == "BTB"){
    		var str=this.accomViewData.accomName;
    		  var accom= str.split("|");
    		  this.firstCruise=accom[0];
      		this.secondCruise=accom[1];
      }

    	var html = widget.renderTmpl(widget.getRenderHtml(packageTypeTempVar), widget);
      domConstruct.place(html, this.domNode, "only");
      parser.parse(this.domNode);
    },

    getAllData: function(listOfData , indexValue){

   	 return  _.filter(listOfData, function(item,index){ return index < indexValue ; })

    },

    getRenderHtml: function(packageType){
    	 var summaryMapping = {

       	      'BTB':CruiseBackToBackSummaryPanel

       	    };

    	 if(_.isUndefined(summaryMapping[packageType])){

    		 return CruiseSummaryPanel

    	 }else{

    		 return summaryMapping[packageType]
    	 }





    }

  });
});