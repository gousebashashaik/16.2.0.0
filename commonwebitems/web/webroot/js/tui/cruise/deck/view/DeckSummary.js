define("tui/cruise/deck/view/DeckSummary", [
    'dojo',
    'tui/widget/_TuiBaseWidget'], function (dojo) {
    dojo.declare("tui.cruise.deck.view.DeckSummary", [tui.widget._TuiBaseWidget], {

        data: null,

        postCreate: function () {
            var deckSummary = this;
            deckSummary.inherited(arguments);
            dojo.subscribe("tui/widget/popup/cruise/DeckPopup/deckSummary", function (deckData) {
                deckSummary.domNode.innerHTML = deckData.deckData.description;
            });
        }
    });

    return tui.cruise.deck.view.DeckSummary;
});
