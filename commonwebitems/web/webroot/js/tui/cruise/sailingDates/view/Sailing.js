define("tui/cruise/sailingDates/view/Sailing", [
    'dojo',
    'tui/widget/popup/Tooltips',
    'tui/widget/_TuiBaseWidget'], function (dojo) {
    return dojo.declare("tui.cruise.sailingDates.view.Sailing", [tui.widget._TuiBaseWidget,  tui.widget.popup.Tooltips], {

        data:null,

        text: null,

        floatWhere:null,

        floatWhere:'position-left-center',

        // ----------------------------------------------------------------------------- methods
        postCreate : function () {
            var sailing = this;
            console.log(sailing);
        }

    });
});