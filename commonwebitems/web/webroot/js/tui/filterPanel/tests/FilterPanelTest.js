try {
    var userArgs = window.location.search.replace(/[\?&](dojoUrl|testUrl|testModule)=[^&]*/g, "").replace(/^&/, "?");

    doh.registerUrl("tui.filterPanel.tests.FilterPanelTest", dojo.moduleUrl("tui", "filterPanel/tests/FilterPanelTest.html" + userArgs), 999999);

} catch (e) {
    doh.debug(e);
}



