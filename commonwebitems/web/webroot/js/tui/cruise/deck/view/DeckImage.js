define("tui/cruise/deck/view/DeckImage", [
    'dojo',
    'dojo/text!tui/cruise/deck/view/templates/deckImage.html',
    'tui/widget/_TuiBaseWidget',
    'tui/widget/mixins/Templatable'], function (dojo, tmpl) {
    dojo.declare("tui.cruise.deck.view.DeckImage", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable], {

        data: null,

        tmpl:tmpl,

        postCreate: function () {
            var deckImage = this;
            deckImage.inherited(arguments);
            dojo.subscribe("tui/widget/popup/cruise/DeckPopup/openPopup", function (deckData) {
            	deckFacilities.updateTemplate(deckData);
            });
        },
        
        updateTemplate : function (deckData) {
      	  var deckFacilities = this;
      	 var html = deckImage.renderTmpl(null, deckData);
         deckImage.domNode.innerHTML = html;
      }
    });

    return tui.cruise.deck.view.DeckImage;
});
