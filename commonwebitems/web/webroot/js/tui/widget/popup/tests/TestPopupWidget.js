define("tui/widget/popup/tests/TestPopupWidget", ["dojo", "dojo/text!tui/widget/popup/tests/TestTemplate.html", "tui/widget/popup/Popup"], function (dojo, tmplFile) {

    dojo.declare("tui.widget.popup.tests.TestPopupWidget", [tui.widget.popup.Popup], {
        tmpl:tmplFile
    });

    return tui.widget.popup.tests.TestPopupWidget;
})