define("tui/cruise/deck/view/DeckLegend", [
    'dojo',
    'dojo/text!tui/cruise/deck/view/templates/deckLegend.html', 
    "tui/cruise/deck/controller/DeckSVGCabinColors",
    'tui/widget/_TuiBaseWidget',
    'tui/widget/mixins/Templatable'], function (dojo, tmpl, SVGCabinColors) {
    dojo.declare("tui.cruise.deck.view.DeckLegend", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable], {

        data: null,

        tmpl:tmpl,

        postCreate: function () {
            var deckLegend = this;
            if(deckLegend.getParent() != null) {
            	deckLegend.getParent().getParent().deckLegendWgts.push(deckLegend);
            } 
            deckLegend.inherited(arguments);

            dojo.subscribe("tui/widget/popup/cruise/DeckPopup/deckLegend", function (deckData) {
            	deckLegend.updateTemplate(deckData);
            });
        },
    
	    updateTemplate : function (deckData) {
	    	var deckLegend = this, legends = {};
        	_.each(deckData.deckData, function(data){
                legends[data.name] = SVGCabinColors["cabinColors"][data.typeCode];
        	});
        	var html = deckLegend.renderTmpl(null, {"deckData":legends, "flag": deckData.flag });
    	  	deckLegend.domNode.innerHTML = html;
	    }
    });

    return tui.cruise.deck.view.DeckLegend;
});
