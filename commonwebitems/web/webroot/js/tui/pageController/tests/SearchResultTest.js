try {
    var userArgs = window.location.search.replace(/[\?&](dojoUrl|testUrl|testModule)=[^&]*/g, "").replace(/^&/, "?");

    doh.registerUrl("tui.pageController.tests.SearchResultTest.js", dojo.moduleUrl("tui", "pageController/tests/SearchResultControllerTest.html" + userArgs), 999999);

} catch (e) {
    doh.debug(e);
}