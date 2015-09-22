define("tui/searchBPanel/store/DestinationMultiFieldStore", [
	"dojo",
    'tui/searchPanel/config/SearchConfig',
	"tui/searchBPanel/store/SearchMultiFieldStore"
], function (dojo, searchConfig) {

	dojo.declare("tui.searchBPanel.store.DestinationMultiFieldStore", [tui.searchBPanel.store.SearchMultiFieldStore], {

        // ----------------------------------------------------------------------------- properties

        selectedSize: 0,

		// ----------------------------------------------------------------------------- methods

		put: function (object, options) {
			var destinationMultiFieldStore = this;
			if (!object.isInstanceOf(tui.searchBPanel.model.DestinationModel)) {
				throw "Object needs to be an instance of tui.searchBPanel.model.DestinationModel";
			}
            var index = destinationMultiFieldStore.inherited(arguments);
            destinationMultiFieldStore.setSelectedSize();
            return index;
        },

        add: function (object, options) {
            var destinationMultiFieldStore = this;
            var index = destinationMultiFieldStore.inherited(arguments);
            destinationMultiFieldStore.setSelectedSize();
            return index;
		},

        remove: function (id) {
			var destinationMultiFieldStore = this;
            var index = destinationMultiFieldStore.inherited(arguments);
            destinationMultiFieldStore.setSelectedSize();
            return index;
        },

        setSelectedSize: function () {
            var destinationMultiFieldStore = this;
            var items = [];
            _.forEach(destinationMultiFieldStore.data, function (item) {
                if (item.children && item.children.length > 0) {
                    _.forEach(item.children, function (child) {
                        items.push(child);
                    });
                } else {
                    items.push(item.id);
                }
            });
            destinationMultiFieldStore.selectedSize = _.uniq(items).length;
        },

        checkSelectedSize: function (destinationModel) {
            var destinationMultiFieldStore = this;

            var size = destinationMultiFieldStore.selectedSize;
            var count = 0;
            if (destinationModel.children && destinationModel.children.length > 0) {
                //return destinationModel.children.length + size;
                _.forEach(destinationModel.children, function (id, index) {
                    if (!destinationMultiFieldStore.get(id) && !destinationMultiFieldStore.isItemInGroup(id)) {
                        count++;
                    }
                });
                return size + count;
            }
            return size + 1;
		},

        isItemInGroup: function (id) {
            var destinationMultiFieldStore = this;

            var store = destinationMultiFieldStore.query(function (item) {
                if (!item.children) {
                    return false;
                }
                return (_.indexOf(item.children, id) > -1);
	      });
            return (store.length > 0);
        },

        isChildrenInGroup: function (children) {
            var destinationMultiFieldStore = this;

            var store = destinationMultiFieldStore.query(function (item) {
                if (!item.children) {
                    return false;
                }
                return _.intersection(item.children, children).length;
            });
            return (store.length > 0);
		},

		summariseCount: function (itemsToIgnore) {
			var destinationMultiFieldStore = this;
            var count = 0;
            var i = 0;
			itemsToIgnore = itemsToIgnore || 1;

			if (destinationMultiFieldStore.data.length <= 1) return 0;

            // get itemsToIgnore items, if has children get children count
            _.some(destinationMultiFieldStore.data, function (item) {
                if (i > (itemsToIgnore - 1)) {
                    return false;
	    }
                if (item.children && item.children.length > 0) {
                    count += item.children.length;
                } else {
                    count++;
                }
                i++;
            });

            // return size of store minus count of itemsToIgnore items
            return destinationMultiFieldStore.selectedSize - count;
	    }
	});

	return tui.searchBPanel.store.DestinationMultiFieldStore;
});
