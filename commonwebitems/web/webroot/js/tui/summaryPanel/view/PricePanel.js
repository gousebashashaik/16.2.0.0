define("tui/summaryPanel/view/PricePanel", [
    "dojo",
    "dojo/number",
    "dojo/parser",
    "dojo/text!tui/summaryPanel/view/templates/pricePanel.html",
    "tui/widget/mixins/Templatable",
    "tui/search/nls/Searchi18nable"
], function (dojo,number, parser, pricePanelTmpl, Templatable) {

    return dojo.declare("tui.summaryPanel.view.PricePanel", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable, tui.search.nls.Searchi18nable], {

        tmpl: pricePanelTmpl,
        
        currency :  dojoConfig.currency,

        tag:'365',
        number:'1',
        
        data: null,

        postCreate: function () {
           var pricePanel = this;
           pricePanel.initSearchMessaging();
           pricePanel.currency = currency;
           console.log('Summary panel View');
           dojo.removeClass(pricePanel.domNode, "loading");
           pricePanel.renderContent();

           dojo.subscribe("tui/summaryPanel/controller/updateSummaryPanel", function (data) {
               pricePanel.domNode.innerHTML = '';
               pricePanel.data = data;
               pricePanel.renderContent();
           });
        },

        renderContent: function(){
           var pricePanel = this;
           var html = pricePanel.renderTmpl(pricePanel.tmpl, pricePanel.fetchData(pricePanel.data));
           var dom = dojo.place(html, pricePanel.domNode, "last");
           parser.parse(pricePanel.domNode);
           pricePanel.tagElement(dojo.query('.change-pax', pricePanel.domNode)[0], 'changeParty');
        },

        fetchData: function(data){
           var pricePanel = this;
           var selectedResult = _.first(data.selectedResult.sailings);
           var price = selectedResult.price;
           var depositWithCreditCardPay = number.format(data.selectedResult.sailings[0].priceToolTipViewData.depositWithCreditCardPay,{
               places: 2
             });
           var totalCostwithCreditCardPay = number.format(data.selectedResult.sailings[0].priceToolTipViewData.totalCostwithCreditCardPay,{
        	   places: 2
           });;
           return {
               total: Math.round(price.total),
               pp: Math.round(price.perPerson),
               discount: Math.round(price.discount),
               lowDeposit:Math.round(price.depositAmountPP),
               depositExists: price.depositExists,
			   lowDepositExists: price.lowDepositExists,
               adults: data.requestData.searchRequest.noOfAdults, //data.requestData.searchRequest
               showChild: data.requestData.searchRequest.noOfChildren>0,//data.requestData.searchRequest
               children: data.requestData.searchRequest.noOfChildren,//data.requestData.searchRequest
               summary: pricePanel.summary,
			   hideLink: pricePanel.accomDetails,
               cruiseandstay: ((data.selectedResult.variantNInv === 'CRUISE_STAY_TRACS') ||
                               (data.selectedResult.variantNInv === 'STAY_CRUISE_TRACS') ||
                               (data.selectedResult.variantNInv === 'STAY_CRUISE_ATCOM') ||
                               (data.selectedResult.variantNInv === 'CRUISE_STAY_ATCOM') ||
                               (data.selectedResult.variantNInv === 'CRUISE_STAY_TRACS_HOTEL') ||
							   (data.selectedResult.variantNInv === 'CRUISE_STAY_ATCOM_HOTEL') ||
                               (data.selectedResult.variantNInv === 'STAY_CRUISE_TRACS_HOTEL') ||
							   (data.selectedResult.variantNInv === 'STAY_CRUISE_ATCOM_HOTEL')
               ),
               stayandcruise: ((data.selectedResult.variantNInv === 'STAY_CRUISE_ATCOM') ||
                               (data.selectedResult.variantNInv === 'STAY_CRUISE_TRACS') ||
                               (data.selectedResult.variantNInv === 'STAY_CRUISE_TRACS_HOTEL') ||
							   (data.selectedResult.variantNInv === 'STAY_CRUISE_ATCOM_HOTEL')
               ),
               scTooltip: pricePanel.searchMessaging.summaryPanel.scTooltip,
               csTooltip: pricePanel.searchMessaging.summaryPanel.csTooltip,
               
               ccChargesText: pricePanel.searchMessaging.summaryPanel.ccChargesText+"<ul class=\"price-breakdown\"><li>Deposit:<span>£"+depositWithCreditCardPay+"</span></li><li>Total cost:<span>£"+totalCostwithCreditCardPay+"</span></li></ul>",
               summaryPrice : "<p></p><p>Price breakdown</p><ul class=\"price-breakdown\"><li>Package price <span>£" + data.selectedResult.sailings[0].priceToolTipViewData.packagePrice + "</span></li><li>World care fund <span>£" + data.selectedResult.sailings[0].priceToolTipViewData.worldCareFund + "</span></li><li>Sub Total <span>£" + data.selectedResult.sailings[0].priceToolTipViewData.subTotal +"</span></li><li>Discount <span>–£" + data.selectedResult.sailings[0].priceToolTipViewData.discount + "</span></li><li ><br/>Total: <span>£" + data.selectedResult.sailings[0].priceToolTipViewData.totalPrice+" </span></li></ul><p></p>"
              // summaryPrice :  <p>Price breakdown</p><ul class="price-breakdown"><li>Package price <span>&pound;<fmt:formatNumber type="number" maxFractionDigits="2" value="${bt.packagePrice}"/></span></li><c:if test="${packageData.worldCare}"><li>World care fund <span>&pound;<fmt:formatNumber type="number" maxFractionDigits="2" value="${accommSummaryViewData.worldCareFunds}"/></span></li></c:if><li>Sub Total <span>&pound;<fmt:formatNumber type="number" maxFractionDigits="2" value="${bt.subTotal}"/></span></li><c:if test="${displayInsuranceUpSell == 'true'}"><li>Discount <span>&ndash;&pound;<fmt:formatNumber type="number" maxFractionDigits="2" value="${packageData.price.discount}" /></span></li></c:if><li class="total-price">Total: <span>&pound;<fmt:formatNumber type="number" maxFractionDigits="2" value="${bt.total}" /></span></li></ul>
           };
        }

    });
});