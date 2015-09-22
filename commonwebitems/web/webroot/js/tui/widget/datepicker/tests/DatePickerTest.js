try {
	var userArgs = window.location.search.replace(/[\?&](dojoUrl|testUrl|testModule)=[^&]*/g, "").replace(/^&/, "?");

	doh.registerUrl("tui.widget.datepicker.tests.DatePickerTest", dojo.moduleUrl("tui", "widget/datepicker/tests/DatePickerTest.html" + userArgs), 999999);

} catch (e) {
	doh.debug(e);
}