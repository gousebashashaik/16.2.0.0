define("tui/widget/popup/cruise/DeckPopup", [
    'dojo',
    'dojo/query',
    "dojo/on",
    "dojo/parser",
    'dijit/registry',
    'dojo/text!tui/widget/popup/cruise/templates/deckPopupTmpl.html',
    'tui/widget/popup/Popup'], function (dojo, query, on, parser, registry, tmpl, popup) {

    dojo.declare("tui.widget.popup.cruise.DeckPopup", [tui.widget.popup.DynamicPopup], {

        includeScroll: true,

        openOnStart: false,

        resizeListener: false,

        subscribableMethods: ["open", "close", "resize"],

        widgetId:'deckOverlay',

        data: null,

        modal: true,

        scrollToNode: null,

        touchSupport: false,


        onClose:function(){
            var deckPopup = this;
            var deckDropDown = _.find(registry.findWidgets(deckPopup.popupDomNode), function(num){
                return  num.id.indexOf("DeckSelectOption") != -1;
            });
            deckDropDown.hideList();
            deckPopup.touchSupport && dojo.window.scrollIntoView(deckPopup.scrollToNode);
        }

    });
    return tui.widget.popup.cruise.DeckPopup;
});
