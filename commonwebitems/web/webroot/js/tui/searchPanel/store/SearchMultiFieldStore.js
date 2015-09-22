define("tui/searchPanel/store/SearchMultiFieldStore", [
	"dojo",
	"dojo/store/Memory"], function (dojo) {

	dojo.declare("tui.searchPanel.store.SearchMultiFieldStore", [dojo.store.Memory], {

		// ----------------------------------------------------------------------------- properties

		storeSummary: null,

		// ----------------------------------------------------------------------------- methods
		
		getStorageData: function(fields, queryOption) {
			var searchMultiFieldStore = this;
			var dataFromStorage = [];
			_.forEach(searchMultiFieldStore.query(queryOption), function(item){
				var data = {};
				_.forEach(fields, function(field) {
					data[field] = item[field];
				});
				dataFromStorage.push(data);
			});
			return dataFromStorage;
		},
		
		setStorageData: function(items, instanceType, options) {
			var searchMultiFieldStore = this;
			_.forEach(items, function(item) {
				var instance = dojo.mixin(new instanceType(options), item);
				searchMultiFieldStore.put(instance);
			});
		},

    emptyStore: function () {
      // clear out the store when a new search is performed
      var searchMultiFieldStore = this,
          data = searchMultiFieldStore.query();
      _.each(data, function (dataItem) {
        searchMultiFieldStore.remove(dataItem.id);
      });
    }
	});
   
		return tui.searchPanel.store.SearchMultiFieldStore;
});
