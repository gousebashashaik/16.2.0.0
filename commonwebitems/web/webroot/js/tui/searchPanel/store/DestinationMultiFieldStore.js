define("tui/searchPanel/store/DestinationMultiFieldStore", [
	"dojo",
	"tui/searchPanel/store/SearchMultiFieldStore"
], function (dojo) {

	dojo.declare("tui.searchPanel.store.DestinationMultiFieldStore", [tui.searchPanel.store.SearchMultiFieldStore], {

		// ----------------------------------------------------------------------------- methods

		put: function (object, options) {
			var destinationMultiFieldStore = this;
			if (!object.isInstanceOf(tui.searchPanel.model.DestinationModel)) {
				throw "Object needs to be an instance of tui.searchPanel.model.DestinationModel";
			}

			return destinationMultiFieldStore.inherited(arguments);
		},

		summariseCount: function (itemsToIgnore) {
			var destinationMultiFieldStore = this;
			itemsToIgnore = itemsToIgnore || 1;

			if (destinationMultiFieldStore.data.length <= 1) return 0;

			return destinationMultiFieldStore.data.length - itemsToIgnore;
		},

	    emptyStore: function () {
	      // clear out the store when a new search is performed
	      var destinationMultiFieldStore = this,
	          data = destinationMultiFieldStore.query();
	      _.each(data, function (dataItem) {
	    	  destinationMultiFieldStore.remove(dataItem.id)
	      });
	    }

	});

	return tui.searchPanel.store.DestinationMultiFieldStore;
});
