try {
    var userArgs = window.location.search.replace(/[\?&](dojoUrl|testUrl|testModule)=[^&]*/g, "").replace(/^&/, "?");

    doh.registerUrl("tui.widget.maps.tests.MapTopx", dojo.moduleUrl("tui", "widget/maps/tests/MapTest.html" + userArgs), 999999);

} catch (e) {
    doh.debug(e);
}



