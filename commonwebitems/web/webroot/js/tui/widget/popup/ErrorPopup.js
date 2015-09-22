define ("tui/widget/popup/ErrorPopup", ["dojo", "dojo/text!tui/widget/popup/templates/error.html", "tui/widget/popup/PopupBase"], function(dojo, template){

    /***********************************************************************************/
    /* tui.widget.tui.widget.Popup											   		   */
    /***********************************************************************************/
    dojo.declare("tui.widget.popup.ErrorPopup", [tui.widget.popup.PopupBase], {
        floatWhere: "position-bottom-center",

        tmpl: template,

        errorMessage: ""
    })


    return tui.widget.popup.ErrorPopup;
})