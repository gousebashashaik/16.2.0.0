dojo.provide("tui.tests.module");
//This file loads in all the test definitions.  

try {

        dojo.require("tui.widget.maps.tests.Mappers");
        dojo.require("tui.widget.tests.CookieNotifier");
        dojo.require("tui.widget.popup.tests.DynamicPopupTest");
        dojo.require("tui.utils.tests.TuiUnderscoreTest");
        dojo.require("tui.widget.popup.tests.PopupTest");
        dojo.require("tui.widget.maps.tests.MapTest");
        dojo.require("tui.widget.form.tests.SelectOptionTest");

} catch (e) {
    doh.debug(e);
}

