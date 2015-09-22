define("tui/widget/amendncancel/bookingOverviewPage/PassengerDetailsSectionComponentPanel", [
  "dojo/_base/declare",
  "dojo/dom-construct",
  "dojo/dom-class",
  "dojo/parser",
  "dojo/text!tui/widget/amendncancel/bookingOverviewPage/templates/PassengerDetailsSectionComponentPanel.html",
  "tui/widget/_TuiBaseWidget",
  "dojox/dtl/_Templated",
  "tui/widget/mixins/Templatable",
  "tui/widget/expand/Expandable",
  "tui/widget/expand/subExpandable",
  "tui/widget/amendncancel/ShowMore",
  "tui/widget/common/ClickToolTip",
  "tui/widget/amendncancel/BookingOverviewController",
  "tui/widget/amendncancel/bookingOverviewPage/ModalPopup"
  ], function (declare,domConstruct,domClass,parser,PassengerDetailsSectionTmpl, _TuiBaseWidget, dtlTemplate, Templatable) {

  return declare("tui.widget.amendncancel.bookingOverviewPage.PassengerDetailsSectionComponentPanel",
    [_TuiBaseWidget, dtlTemplate, Templatable], {

      tmpl: PassengerDetailsSectionTmpl,
      templateString: "",
      widgetsInTemplate: true,
      packageData:null,
	  staticData:null,
	  staticDataString:null,
	  paxList1:[],
	  paxList2:[],
	  paxCountFlag:false,
	  controller:null,

      buildRendering: function () {
        this.templateString = this.renderTmpl(this.tmpl, this);
        delete this._templateCache[this.templateString];
        this.inherited(arguments);
      },

      postMixInProperties: function(){
    	  this.packageData = this.jsonData.packageViewData ;
		  this.staticData = this.jsonData.manageBookingSummaryContentViewData.manageBookingSummaryContentMap ;
		  this.staticDataString = dojo.toJson(this.jsonData.manageBookingSummaryContentViewData.manageBookingSummaryContentMap);
		  this.seperatePaxList();
    	  this.inherited(arguments);
      },

      postCreate: function () {
    	    widget=this;
  			widget.controller = dijit.registry.byId("controllerWidget");
  			widget.controller.registerView(widget);
            this.inherited(arguments);
      },

      refresh: function (field, response) {
      	  var widget = this;
		  if (field == "securityQuestions" && response.error == null) {
			  widget.packageData = this.jsonData.packageViewData ;
			  widget.staticData = this.jsonData.manageBookingSummaryContentViewData.manageBookingSummaryContentMap ;
			  widget.staticDataString = JSON.stringify(this.jsonData.manageBookingSummaryContentViewData.manageBookingSummaryContentMap);
			  widget.getLeadPassender(response.address);
			  widget.seperatePaxList();
			  var html = widget.renderTmpl(widget.tmpl, widget);
			  domConstruct.place(html, widget.domNode, "only");
			  parser.parse(widget.domNode);
			}
       },

       getLeadPassender: function(address){
    	   if(address){
    		   for(var i=0; i<= widget.packageData.passenger.length; i++){
    			   console.log(widget.packageData.passenger);
    			   console.log(widget.packageData.passenger[i].lead);
        		   if(widget.packageData.passenger[i].lead){
        			   widget.packageData.passenger[i].address = address;
        			   widget.packageData.showLeadPaxAddress = true;
        			   break;

        		   }
        	   }
    	   }
       },

	   seperatePaxList: function(){
		if( this.packageData.passenger.length > 10 ){
			this.paxCountFlag = true;
			for(i=0;i < this.packageData.passenger.length;i++){
				if( i < 10 ){
					this.paxList1.push(this.packageData.passenger[i]);
				}
				else{
					this.paxList2.push(this.packageData.passenger[i]);
				}
			}
		  }
		  else{
			this.paxCountFlag = false;
			this.paxList1 = this.packageData.passenger;
		  }
	   }

    });
});