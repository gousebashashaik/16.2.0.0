define("tui/summaryPanel/view/AddStayTeaser", [
    "dojo",
    "dojo/on",
    "dojo/text!tui/summaryPanel/view/templates/addStayTeaser.html",
    "tui/widget/mixins/Templatable"
], function (dojo, on, pricePanelTmpl, Templatable) {

    return dojo.declare("tui.summaryPanel.view.AddStayTeaser", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable], {

        tmpl: pricePanelTmpl,

        data: null,

        tag:'365',
        number:'1',

        postCreate: function () {
            var addAStay = this;
            console.log('Add a Stay teaser View');
            addAStay.renderContent();
			 if(dojo.query(".stay-teaser .addAStayFooter")[0] ){
           	dojo.query(".stay-teaser #stayTeaserTitle")[0].innerText = "ADD a "+ addAStay.data.stayTeaser.duration +" night stay in";
           }
            dojo.subscribe("tui/summaryPanel/controller/updateSummaryPanel", function (data) {
               dojo.disconnect(addAStay.addButton);
               addAStay.domNode.innerHTML = '';
               addAStay.data = data;
               addAStay.renderContent();
            });
        },

        renderContent: function(){
            var addAStay = this;
            var tracs = (addAStay.data.selectedResult.variantNInv.indexOf('TRACS') > -1);
            if(addAStay.data.stayTeaser.available){
            	var footerStayTeaser = dojo.query(".stay-teaser .addAStayFooter")[0];
            	var contxt = addAStay.fetchData(addAStay.data);
            	contxt.footerStayTeaserFlag = !!footerStayTeaser;
                var html = addAStay.renderTmpl(addAStay.tmpl, contxt );
                dojo.place(html, addAStay.domNode, "last");
                footerStayTeaser && dojo.place( dojo.query(".addAStayFooter", addAStay.domNode)[0], footerStayTeaser, "replace");
                addAStay.addButton = dojo.query(".addStay").on(("click"), function (event) {
                    addAStay.data.requestData.searchRequest.addAStay = addAStay.data.stayTeaser.duration + ':'+addAStay.data.requestData.searchRequest.addAStay;
                    dojo.publish("tui/summaryPanel/controller/addStay",tracs ? addAStay.data.requestData : addAStay.data.stayTeaser.pkgRequestupdated);
                    window.location = "#summary-panel-nav";
                });
                var button = dojo.query('.button', addAStay.domNode)[0];
                addAStay.tagElement(button, 'addStay');
                footerStayTeaser &&  addAStay.tagElement( dojo.query('.button', footerStayTeaser)[0], 'addStay');
            }
        },

        fetchData: function(data){
            return {
                available: data.stayTeaser.available,
                amount: Math.round(data.stayTeaser.ppPrice) > 0 ? Math.round(data.stayTeaser.ppPrice) : (Math.round(data.stayTeaser.ppPrice)* -1),
                stayDuration:data.stayTeaser.duration,
                location:data.stayTeaser.location
            };
        }

    });
});