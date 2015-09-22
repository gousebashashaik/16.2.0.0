try {
    var userArgs = window.location.search.replace(/[\?&](dojoUrl|testUrl|testModule)=[^&]*/g, "").replace(/^&/, "?");

    doh.registerUrl("tui.widget.form.tests.SelectOptionTest", dojo.moduleUrl("tui", "widget/form/tests/SelectOptionTest.html" + userArgs), 999999);

} catch (e) {
    doh.debug(e);
}