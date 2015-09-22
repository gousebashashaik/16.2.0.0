define("tui/searchPanel/config/SearchConfig", [
	"dojo",
    "dojo/Stateful"
], function (dojo) {

	dojo.declare("tui.searchPanel.config.SearchConfig", [dojo.Stateful], {

		// ----------------------------------------------------------------------------- properties

		// The number of days the search criteria will be remembered on the browser.
		PERSISTED_SEARCH_PERIOD: 30,

		// Maximum number of adults + seniors + children over INFANT_AGE than can be selected.
		MAX_ADULTS_NUMBER: 9,

		// The age below or equal to which a child is considered an infant.
		INFANT_AGE: 1,

		// Number of days the user is flexible around departure date.
		FLEXIBLE_DAYS: 3,

		SEASON_LENGTH: 18,

		// date pattern for datepicker
		DATE_PATTERN: "EEE d MMMM yyyy"
	});


	return tui.searchPanel.config.SearchConfig;
});
