define("tui/summaryPanel/view/TraverseRepoPanel", [
    "dojo",
    "dojo/text!tui/summaryPanel/view/templates/traverseRepoPanel.html",
    "tui/widget/mixins/Templatable"
], function (dojo, traverseRepoPanel, Templatable) {

    return dojo.declare("tui.summaryPanel.view.TraverseRepoPanel", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable], {

        tmpl: traverseRepoPanel,

        data: null,

        tag:'365',
        number:'1',

        postCreate: function () {
            var traverseRepoPanel = this;
            console.log('Traverse Repo panel View');
            dojo.removeClass(traverseRepoPanel.domNode, "loading");
            traverseRepoPanel.renderContent();

            dojo.subscribe("tui/summaryPanel/controller/updateSummaryPanel", function (data) {
               traverseRepoPanel.domNode.innerHTML = '';
               traverseRepoPanel.data = data;
               traverseRepoPanel.renderContent();
            });
        },

        renderContent: function(){
            var traverseRepoPanel = this;
            var differentiatedProduct  = traverseRepoPanel.data.selectedResult.sailings[0].differentiatedProduct && traverseRepoPanel.data.selectedResult.sailings[0].differentiatedProduct.differentiatedProduct ? traverseRepoPanel.data.selectedResult.sailings[0].differentiatedProduct : null;
            var html = traverseRepoPanel.renderTmpl(traverseRepoPanel.tmpl, {
                repoMsg:false,
                noBookflow: false,
                //nextPageUrl: traverseRepoPanel.data.pageResponse.targetPageUrl,
                nextPageUrl : traverseRepoPanel.data.nextPageInfo.nextPageUrl,
                prevPageUrl:traverseRepoPanel.data.overviewUrl,
                summary: traverseRepoPanel.summary,
                showItinerary: traverseRepoPanel.showItinerary,
				
				itinerary: {
                    name:traverseRepoPanel.data.selectedResult.itinenaries[0].name,
                    ship:traverseRepoPanel.data.selectedResult.sailings[0].shipName.name,
                    duration: traverseRepoPanel.data.packageInfo.cruiseDuration,
                    diffProd: differentiatedProduct,
                    productIdentifierCode: _.isNull(differentiatedProduct) ? '' : differentiatedProduct.code,
                    productIdentifierTooltip: _.isNull(differentiatedProduct) ? '' : differentiatedProduct.featureCodesAndValues.strapline[0]
                },
				
                buttonText: ((traverseRepoPanel.data.selectedResult.variantNInv === 'CRUISE_STAY_TRACS') || (traverseRepoPanel.data.selectedResult.variantNInv === 'STAY_CRUISE_TRACS') || ((traverseRepoPanel.data.selectedResult.variantNInv === 'STAY_CRUISE_ATCOM') || (traverseRepoPanel.data.selectedResult.variantNInv === 'CRUISE_STAY_ATCOM'))) ? 'Choose Hotels':'Cruise Options'
            });
			
            var dom = dojo.place(html, traverseRepoPanel.domNode, "last");
            dojo.parser.parse(traverseRepoPanel.domNode);
            var button = dojo.query('.button', traverseRepoPanel.domNode)[0];
            traverseRepoPanel.tagElement(button, button.innerText);

        }
    });
});