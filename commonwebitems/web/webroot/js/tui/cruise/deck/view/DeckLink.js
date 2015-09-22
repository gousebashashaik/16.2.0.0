define("tui/cruise/deck/view/DeckLink", [
    'dojo',
    'dojo/on',
    'tui/widget/_TuiBaseWidget'], function (dojo) {
    dojo.declare("tui.cruise.deck.view.DeckLink", [tui.widget._TuiBaseWidget], {

        data: null,

        popup: null,

        modal:true,

        postCreate: function () {
            var deckLink = this;
            deckLink.inherited(arguments);
        }
    });

    return tui.cruise.deck.view.DeckLink;
});