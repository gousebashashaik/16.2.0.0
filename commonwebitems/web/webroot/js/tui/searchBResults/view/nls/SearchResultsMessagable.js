define("tui/searchBResults/view/nls/SearchResultsMessagable", [
	"dojo",
	"dojo/i18n",
	"dojo/i18n!./SearchResultsMessaging"
], function (dojo, i18n) {

	dojo.declare("tui.searchBResults.view.nls.SearchResultsMessagable", null, {

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

	return tui.searchBResults.view.nls.SearchResultsMessagable;
});