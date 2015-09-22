define("tui/cruise/deck/view/DeckTitle", [
    'dojo',
    'tui/widget/_TuiBaseWidget'], function (dojo) {
    dojo.declare("tui.cruise.deck.view.DeckTitle", [tui.widget._TuiBaseWidget], {

        data: null,

        postCreate: function () {
            var deckTitle = this;
            deckTitle.inherited(arguments);
            dojo.subscribe("tui/widget/popup/cruise/DeckPopup/deckTitle", function (deckData) {
                deckTitle.domNode.innerHTML = deckData.deckData.title;
            });
        }
    });

    return tui.cruise.deck.view.DeckTitle;
});
