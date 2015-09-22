define("tui/widget/booking/yourflight/view/GlobalYourFlightCompVariant", ["dojo/_base/declare",
  "dijit/_WidgetBase",
  "tui/widget/mixins/Templatable",
  "dojox/dtl/_Templated",
  "dojo/on",
  'dojo/query',
  'dojo/dom-class',
  "dojo/dom-style",
  "dojo/_base/lang",
  "dojo/dom",
  "dojo/dom-construct",
  "dojo/text!tui/widget/booking/yourflight/view/Templates/VariantYourFlightTmpl.html",
  "dojo/topic",
  "dojo/html",
  "dojo/parser",
  "tui/widget/booking/yourflight/view/VariantYourFlightService",
  "tui/widget/booking/bookflowMsgs/nls/Bookflowi18nable",
  "dojox/dtl",
  "dojox/dtl/Context",
  "tui/widget/_TuiBaseWidget",
  "tui/widget/booking/yourflight/view/YourFlights"
  ],
  function (declare, _WidgetBase, Templatable, dtlTemplated, on, query, domClass, domStyle, lang, dom, domConstruct, VariantYourFlightTmpl, topic, html, parser, VariantYourFlightService) {
    return declare("tui.widget.booking.yourflight.view.GlobalYourFlightCompVariant", [tui.widget.booking.yourflight.view.YourFlights,tui.widget.booking.bookflowMsgs.nls.Bookflowi18nable], {

      tmpl: VariantYourFlightTmpl,
      templateString: "",

      postMixInProperties: function () {
    	  this.initBookflowMessaging();
		  this.contentDisplay = this.bookflowMessaging[dojoConfig.site].brandMapping[this.jsonData.packageType].yourFlightContentDisplay;

		  var accomUrlBool = this.bookflowMessaging[dojoConfig.site].brandMapping[this.jsonData.packageType].accomUrl;
		  var urlCheck1 = false;
		  var urlCheck2 = false;
		  if(accomUrlBool){

			  for (var i =0; i<this.jsonData.summaryViewData.summaryPanelComponentViewData.length; i++){
			      	if(this.jsonData.summaryViewData.summaryPanelComponentViewData[i].name == this.bookflowMessaging[dojoConfig.site].brandMapping[this.jsonData.packageType].accomPageUrlText)
			      	{
			      		this.accomUrl = this.jsonData.summaryViewData.summaryPanelComponentViewData[i].summaryPanelUrlsViewData.url;
			      		urlCheck1 = true;


			      	}
			      	if(!_.isEmpty(this.bookflowMessaging[dojoConfig.site].brandMapping[this.jsonData.packageType].accomPageUrlText1)){
			      		if(!urlCheck2){
			      			if(this.jsonData.summaryViewData.summaryPanelComponentViewData[i].name == this.bookflowMessaging[dojoConfig.site].brandMapping[this.jsonData.packageType].accomPageUrlText1)
					      	{
				      			this.contentStayDisplay = this.bookflowMessaging[dojoConfig.site].brandMapping[this.jsonData.packageType].yourFlightContentDisplayStay
				      			this.accomUrl1 = _.isEmpty(this.jsonData.summaryViewData.summaryPanelComponentViewData[i].summaryPanelUrlsViewData)? " ":this.jsonData.summaryViewData.summaryPanelComponentViewData[i].summaryPanelUrlsViewData.url;;

				      			urlCheck2 = true;
					      	}
			      		}else if(urlCheck1 && urlCheck2){
			      			break;
			      		}

			      	}else {
			      		urlCheck2 = true;
			      	}

			      	if(urlCheck1 && urlCheck2){
			      		break;
			      	}else{
			      		continue;
			      	}

			      }

		  }



	 },

     buildRendering: function () {
    	var productType = this.jsonData.packageType;
    	this.inboundFlag = true;
    	this.controller = dijit.registry.byId("controllerWidget");
        this.flightViewData = this.controller.refData(this.jsonData.packageViewData.flightViewData, this.flightIndexVal);
        if (_.isEmpty(this.flightViewData.inboundSectors)){
        	this.inboundFlag = false;
        }




        this.accomIndexValue = this.bookflowMessaging[dojoConfig.site].brandMapping[this.jsonData.packageType].accomIndexVal;


        this.listAccomViewData = this.controller.getAllData(this.jsonData.packageViewData.accomViewData, this.accomIndexValue);

        if(productType == "BTB"){
        	this.accomViewData = this.controller.refData(this.jsonData.packageViewData.accomViewData, this.bookflowMessaging[dojoConfig.site].brandMapping[productType].cruiseSummary);
      		var str=this.accomViewData.accomName;
      		  var accom= str.split("|");
      		  this.firstCruise=accom[0];
        		this.secondCruise=accom[1];
        }



    	/*this.accomViewData = this.controller.refData(this.jsonData.packageViewData.accomViewData, this.accomIndexValue-1);

    	this.listAccomViewData.push(this.accomViewData);*/



    	this.templateVariant = VariantYourFlightService(productType);
        this.templateString = this.renderTmpl(this.tmpl, this);
        delete this._templateCache[this.templateString];
        this.inherited(arguments);
    },

    postCreate: function () {
      this.inherited(arguments);
    }

  });
});