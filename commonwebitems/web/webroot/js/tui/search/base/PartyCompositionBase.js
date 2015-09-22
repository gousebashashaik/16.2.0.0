define("tui/search/base/PartyCompositionBase", ["dojo"], function (dojo) {

	dojo.declare("tui.search.base.PartyCompositionBase", [], {

		// ----------------------------------------------------------------------------- properties

		// Defaults for party composition adults.
		adults: 0,

		// Defaults for party composition seniors.
		seniors: 0,

		// Defaults for party composition children.
		children: 0,

		// Defaults for party composition childAges.
		childAges: null


	});
	return tui.search.base.PartyCompositionBase;
});