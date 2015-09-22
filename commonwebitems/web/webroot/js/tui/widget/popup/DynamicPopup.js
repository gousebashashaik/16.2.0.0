define ("tui/widget/popup/DynamicPopup", ["dojo","tui/widget/popup/Popup"], function(dojo){

    dojo.declare("tui.widget.popup.DynamicPopup", [tui.widget.popup.Popup], {
    	
        widgetId: null,

        postCreate: function(){
            var dynamicPopup = this;
            // place popup dom at bottom of the body document, to ensure its not
            // being placed in a relative div.
            var win = dojo.byId(dynamicPopup.widgetId);
            if (!win) return;
            win = dojo.place(win, document.body, "last");
            dynamicPopup.popupDomNode = win;
            dynamicPopup.inherited(arguments);
            dynamicPopup.onAfterTmplRender();
        }
    });

    return tui.widget.popup.DynamicPopup;
})