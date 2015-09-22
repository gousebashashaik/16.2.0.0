try {
    var userArgs = window.location.search.replace(/[\?&](dojoUrl|testUrl|testModule)=[^&]*/g, "").replace(/^&/, "?");

    doh.registerUrl("tui.searchPanel.tests.searchTest", dojo.moduleUrl("tui", "tui/searchPanel/tests/searchTest.html" + userArgs), 999999);

} catch (e) {
    doh.debug(e);
}