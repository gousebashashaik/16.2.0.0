define ("tui/widget/modules/PopupModule", ["dojo",
    "tui/widget/popup/PopupBase",
    "tui/widget/popup/Popup",
    "tui/widget/popup/DynamicPopup",
    "tui/widget/popup/ErrorPopup",
    "tui/widget/popup/OverflowTooltip",
    "tui/widget/popup/Tooltips"], function (dojo) {
	
    return dojo.getObject("tui.widget.modules.PopupModule", true);
})