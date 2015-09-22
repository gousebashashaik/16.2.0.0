define("tui/widget/popup/PopupTrigger", [
    "dojo",
    "dojo/on",
    "tui/widget/_TuiBaseWidget"], function (dojo, on) {

    dojo.declare("tui.widget.popup.PopupTrigger", [tui.widget._TuiBaseWidget], {

        targetPopup: null,

        postCreate: function () {
            var popupTrigger = this;
            on(popupTrigger.domNode, "click", function(event){
               if (popupTrigger.targetPopup) {
                   dojo.publish(popupTrigger.targetPopup + ".open");
               }
            });
        }

    });

    return tui.widget.popup.PopupTrigger;
});