try {
    var userArgs = window.location.search.replace(/[\?&](dojoUrl|testUrl|testModule)=[^&]*/g, "").replace(/^&/, "?");

    doh.registerUrl("tui.widget.form.tests.AutoCompleteTest", dojo.moduleUrl("tui", "widget/form/tests/AutoCompleteTest.html" + userArgs), 999999);

} catch (e) {
    doh.debug(e);
}