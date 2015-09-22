define("tui/searchResults/view/nls/SearchResultsMessagable", [
	"dojo",
	"dojo/i18n",
	"dojo/i18n!./SearchResultsMessaging"
], function (dojo, i18n) {

	dojo.declare("tui.searchResults.view.nls.SearchResultsMessagable", null, {

		// ----------------------------------------------------------------------------- properties

		searchResultsMessaging: null,

		// ----------------------------------------------------------------------------- methods

		initSearchResultsMessaging: function () {
			// summary:
			//		Initialises localisation
			var searchResultsMessagable = this;
			searchResultsMessagable.searchResultsMessaging = i18n.getLocalization("tui.searchResults.view", "SearchResultsMessaging");
		}
	});

	return tui.searchResults.view.nls.SearchResultsMessagable;
});