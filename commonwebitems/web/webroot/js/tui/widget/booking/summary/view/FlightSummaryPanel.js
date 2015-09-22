define("tui/widget/booking/summary/view/FlightSummaryPanel", [
  "dojo/_base/declare",
  "dojo/query",
  "dojo/dom-class",
  "dojo/text!tui/widget/booking/summary/view/templates/FlightsSummaryPanel.html",
  "dojo/dom-construct",
  "dojo/parser",
  "tui/widget/mixins/Templatable"

], function (declare, query, domClass, FlightsSummaryPanel, domConstruct, parser, Templatable) {

  return declare("tui.widget.booking.summary.view.FlightSummaryPanel", [tui.widget._TuiBaseWidget,Templatable], {

    tmpl: FlightsSummaryPanel,
    controller: null,
    displayFlag: true,

    postCreate: function () {
      this.controller = dijit.registry.byId("controllerWidget");
      this.controller.registerView(this);
      this.flightViewData = this.controller.refData(this.jsonData.packageViewData.flightViewData, this.flightIndexVal);
      this.renderWidget();
      this.inherited(arguments);
      this.tagElements(query('a.changeFlight'),"changeFlights");
      this.tagElements(query('.summaryAccordSwitch'),"Flights");
    },

    refresh: function (field, response) {
      this.jsonData = response;

      this.flightViewData = this.controller.refData(this.jsonData.packageViewData.flightViewData, this.flightIndexVal);
      this.renderWidget();
    },

    renderWidget: function () {
    	var widget = this;
    var itemBag = _.filter(widget.jsonData.packageViewData.extraFacilityCategoryViewData, function(item,index){ if(item.extraFacilityCategoryCode == "BAG") return item })


    	console.log(itemBag);

    	this.displayBagOpt = true;

	    if(_.isEmpty(itemBag)){

					if(!_.isUndefined(jsonData.extraFacilityViewDataContainer) ){

						if(!_.isEmpty(jsonData.extraFacilityViewDataContainer.baggageOptions.extraFacilityViewData)){
							if(jsonData.packageViewData.paxViewData.noOfInfants !=0){
				    		widget.displayFlag = false;
				    		widget.strinDescrfipt = jsonData.extraFacilityViewDataContainer.baggageOptions.extraFacilityViewData[0].infantBaggageWeightDescription;
					    	widget.infantCount = jsonData.packageViewData.paxViewData.noOfInfants;
					    	widget.variableCount = jsonData.extraFacilityViewDataContainer.baggageOptions.extraFacilityViewData[0].infantBaggageWeightSelection;
							}

				    	}else{
				    		widget.displayFlag = true;

				    	}

					}else{
						this.displayFlag = true
					}

	    }
	    else{
	    	this.displayFlag = true
	    }

if(! _.isEmpty(itemBag)){
    	for(var i=0; i<itemBag[0].extraFacilityViewData.length; i++){

    		if(itemBag[0].extraFacilityViewData[i].weightCode == 0){
    			this.displayBagOpt = false;
    			break;
    		}
    		else{
    			continue;
    		}

    	}
}

    	this.siteName = dojoConfig.site;
    	var html = widget.renderTmpl(widget.tmpl, widget);
      domConstruct.place(html, this.domNode, "only");
      parser.parse(this.domNode);
    }

  });
});