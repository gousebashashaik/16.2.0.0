define("tui/villaAvailability/nls/I18nable", [
    "dojo",
    "dojo/i18n",
    "dojo/i18n!./I18n"
], function (dojo, i18n) {

    dojo.declare("tui.villaAvailability.nls.I18nable", null, {

        // ----------------------------------------------------------------------------- properties

        localeStrings: null,

        // ----------------------------------------------------------------------------- methods

        initLocale: function () {
            // summary:
            //		Initialises localisation
            var messageable = this;
            messageable.localeStrings = i18n.getLocalization("tui.villaAvailability", "I18n");
        }
    });

    return tui.villaAvailability.nls.I18nable;
});