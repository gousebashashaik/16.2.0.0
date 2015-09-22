define("tui/summaryPanel/view/CruisePricePanel", [
    "tui/summaryPanel/view/PricePanel"
    ], function () {

    return dojo.declare("tui.summaryPanel.view.CruisePricePanel", [tui.summaryPanel.view.PricePanel], {

        postCreate: function () {
           var pricePanel = this;
           pricePanel.inherited(arguments);
        },

        fetchData: function(data){
           var pricePanel = this;
           var selectedResult = _.first(data.selectedResult.sailings);
           var price = selectedResult.price;
           return {
               total: Math.round(price.total),
               pp: Math.round(price.perPerson),
               discount: Math.round(price.discount),
               lowDeposit:Math.round(price.depositAmountPP),
               lowDepositExists: Math.round(price.depositAmountPP)>0 ,
               adults: data.requestData.searchRequest.noOfAdults, 
               showChild: data.requestData.searchRequest.noOfChildren>0,
               children: data.requestData.searchRequest.noOfChildren,
               summary: pricePanel.summary,
               cruiseandstay: ((data.selectedResult.variantNInv === 'CRUISE_STAY_TRACS') || (data.selectedResult.variantNInv === 'STAY_CRUISE_TRACS') || ((data.selectedResult.variantNInv === 'STAY_CRUISE_ATCOM') || (data.selectedResult.variantNInv === 'CRUISE_STAY_ATCOM'))),
               stayandcruise: ((data.selectedResult.variantNInv === 'STAY_CRUISE_ATCOM') || (data.selectedResult.variantNInv === 'STAY_CRUISE_TRACS'))
           };
        }

    });
});