define ("tui/villaAvailability/view/VillaAvailabilityPopup", [
    "dojo",
    "tui/widget/popup/DynamicPopup"
], function(dojo){

    dojo.declare("tui.villaAvailability.view.VillaAvailabilityPopup", [tui.widget.popup.DynamicPopup], {

        widgetId: null,

        onOpen: function() {
            var villaAvailabilityPopup = this;
            var relative = dojo.position(villaAvailabilityPopup.elementRelativeTo);
            var popupNode = dojo.position(villaAvailabilityPopup.popupDomNode);
            if((popupNode.w / 2) > relative.x) {
                villaAvailabilityPopup.posOffset = {top: -8, left: 24};
                villaAvailabilityPopup.posElement(villaAvailabilityPopup.popupDomNode);
            }
        }
    });

    return tui.villaAvailability.view.VillaAvailabilityPopup;
});