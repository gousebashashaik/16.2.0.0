define ("tui/widget/search/SearchLoadingPopup", ["dojo",
                                            "dojo/text!tui/widget/search/templates/SearchLoadingPopup.html",
                                            "dojo/html",
                                            "tui/widget/popup/Popup"], function(dojo, searchLoadingPopupTmpl, html){

    dojo.declare("tui.widget.search.SearchLoadingPopup", [tui.widget.popup.Popup], {

        tmpl: searchLoadingPopupTmpl,

        modal: true,

        postCreate: function(){
            var searchLoadingPopup = this;
            searchLoadingPopup.inherited(arguments);
        },

        addEventModalListener: function () {}

    });

    return tui.widget.search.SearchLoadingPopup;
})