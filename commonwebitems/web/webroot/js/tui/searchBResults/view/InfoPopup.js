define("tui/searchBResults/view/InfoPopup", [
    "dojo",
    "dojo/on",
    "dojo/cookie",
    "tui/widget/popup/ErrorPopup",
    "dojo/text!tui/searchBResults/view/templates/infoPopupTmpl.html"], function (dojo, on, cookie, ErrorPopup, Tmpl) {

    dojo.declare("tui.searchBResults.view.InfoPopup", [tui.widget.popup.ErrorPopup], {

        // ----------------------------------------------------------------------------- properties

        tmpl: Tmpl,

        arrow: true,

        closeSelector: ".remove",

        closeTimer: null,

        heading: null,

        message: null,

        removeText: null,

        removeListener: null,

        showDuration: 0,

        subscribableMethods: ["reposition", "close", "resize", "open"],

        // ----------------------------------------------------------------------------- methods

        reposition: function () {
            var infoPopup = this;
            infoPopup.posOffset.top = -2;
            //(!infoPopup.isShowing(infoPopup.popupDomNode)) ? infoPopup.open() : null;
            infoPopup.posElement(infoPopup.popupDomNode);
        },

        onAfterTmplRender: function () {
            var infoPopup = this;
            infoPopup.inherited(arguments);

            // add remove listener > set cookie
            infoPopup.removeListener = on.once(infoPopup.popupDomNode, ".remove:click", function(){
                cookie('durationInfoPopup', false, {expires:1 });
                infoPopup.removeListener.remove();
                infoPopup.close();
            });

            // clear popup after specified duration
            infoPopup.closeTimer = setTimeout(function(){
                infoPopup.close();
            }, infoPopup.showDuration);

            // IE 9 and below need position changed when search displays summary instead of search-panel
            if(dojo.isIE <= 9) {
                infoPopup.reposition();
            }
        },

        close: function () {
            var infoPopup = this;
            infoPopup.inherited(arguments);
            infoPopup.destroyRecursive();
        },

        destroyRecursive: function () {
            var infoPopup = this;
            infoPopup.isShowing(infoPopup.popupDomNode) ? infoPopup.close(): null;
            infoPopup.closeTimer ? clearTimeout(infoPopup.closeTimer) : null;
            infoPopup.removeListener.remove();
            infoPopup.inherited(arguments);
        }

    });


    return tui.searchBResults.view.InfoPopup;
});