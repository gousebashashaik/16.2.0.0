define('tui/widget/popup/cruise/BookInterimOverlay', [
    'dojo',
    'dojo/text!tui/widget/popup/cruise/templates/book-interim-overlayTmpl.html',
    "dojo/query",
    "dojo/parser",
    "dojo/dom-style",
    'tui/widget/popup/Popup'], function(dojo, bookInterimOverlay, query, parser, domStyle) {

    dojo.declare('tui.widget.popup.cruise.BookInterimOverlay', [tui.widget.popup.Popup], {

        // ----------------------------------------------------------------------------- properties

        tmpl: bookInterimOverlay,

        jsonData:null,

        subscribableMethods: ["open", "close", "resize"],

        modal:true,

        includeScroll: true


        // ----------------------------------------------------------------------------- singleton


        //no  methods yet...


    });

    return tui.widget.popup.cruise.BookInterimOverlay;
});
