define("tui/cruise/deck/view/DeckFacilities", [
    'dojo',
    'dojo/text!tui/cruise/deck/view/templates/deckFacilities.html',
    'tui/widget/_TuiBaseWidget',
    'tui/widget/mixins/Templatable'], function (dojo, tmpl) {
    dojo.declare("tui.cruise.deck.view.DeckFacilities", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable], {

        data: null,

        tmpl:tmpl,

        postCreate: function () {
            var deckFacilities = this;
            if(deckFacilities.getParent() != null) {
            	deckFacilities.getParent().getParent().deckFacilityWgts.push(deckFacilities);
            } 
            deckFacilities.inherited(arguments);
            dojo.subscribe("tui/widget/popup/cruise/DeckPopup/deckFacility", function (deckData) {
            	deckFacilities.updateTemplate(deckData);
            });
        },
        
        updateTemplate : function (deckData) {
        	  var deckFacilities = this;
        	  if(!_.isEmpty(deckData.deckData)){
        		  var html = deckFacilities.renderTmpl(null, {"deckData":deckData.deckData, "deckNo":deckData.deckNo,"flag": deckData.flag });
        		  deckFacilities.domNode.innerHTML = html;
        	  }else {
        		  deckFacilities.domNode.innerHTML = "";
	    	  }
        }
    });

    return tui.cruise.deck.view.DeckFacilities;
});
