define("tui/search/nls/Searchi18nable", [
	"dojo",
	"dojo/i18n",
	"dojo/i18n!./Searchi18n"
], function (dojo, i18n) {

	dojo.declare("tui.search.nls.Searchi18nable", null, {

		// ----------------------------------------------------------------------------- properties

		searchMessaging: null,

		// ----------------------------------------------------------------------------- methods

		initSearchMessaging: function () {
			// summary:
			//		Initialises localisation
			var searchMessagable = this;
			searchMessagable.searchMessaging = i18n.getLocalization("tui.search", "Searchi18n");
		}
	});

	return tui.search.nls.Searchi18nable;
});
