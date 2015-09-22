try {
    var userArgs = window.location.search.replace(/[\?&](dojoUrl|testUrl|testModule)=[^&]*/g, "").replace(/^&/, "?");

    doh.registerUrl("tui.widget.popup.tests.PopupTest", dojo.moduleUrl("tui", "widget/popup/tests/PopupTest.html" + userArgs), 999999);

} catch (e) {
    doh.debug(e);
}



