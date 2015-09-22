define("tui/searchPanel/store/flights/ArrivalAirportMultiFieldStore", [
    "dojo",
    'tui/searchPanel/config/SearchConfig',
    "tui/searchPanel/store/SearchMultiFieldStore"
], function (dojo, searchConfig) {

    dojo.declare("tui.searchPanel.store.flights.ArrivalAirportMultiFieldStore", [tui.searchPanel.store.SearchMultiFieldStore], {

        // ----------------------------------------------------------------------------- properties

        selectedSize: 0,

        // ----------------------------------------------------------------------------- methods

        put: function (object, options) {
            var arrivalAirportMultiFieldStore = this;
            if (!object.isInstanceOf(tui.searchPanel.model.AirportModel)) {
                throw "Object needs to be an instance of tui.searchPanel.model.flights.AirportModel";
            }
            var index = arrivalAirportMultiFieldStore.inherited(arguments);
            arrivalAirportMultiFieldStore.setSelectedSize();
            return index;
        },

        add: function (object, options) {
            var arrivalAirportMultiFieldStore = this;
            var index = arrivalAirportMultiFieldStore.inherited(arguments);
            arrivalAirportMultiFieldStore.setSelectedSize();
            return index;
        },

        remove: function (id) {
            var arrivalAirportMultiFieldStore = this;

            var index = arrivalAirportMultiFieldStore.inherited(arguments);
            arrivalAirportMultiFieldStore.setSelectedSize();
            return index;
        },

        setSelectedSize: function () {
            var arrivalAirportMultiFieldStore = this;
            var items = [];
            dojo.forEach(arrivalAirportMultiFieldStore.data, function (item) {
                if (item.children && item.children.length > 0) {
                    dojo.forEach(item.children, function (child) {
                        items.push(child);
                    });
                } else {
                    items.push(item.id);
                }
            });
            arrivalAirportMultiFieldStore.selectedSize = _.uniq(items).length;
        },

        checkSelectedSize: function (airportModel) {
            var arrivalAirportMultiFieldStore = this;
            var size = arrivalAirportMultiFieldStore.selectedSize;
            var count = 0;
            if (airportModel.children && airportModel.children.length > 0) {
                //return airportModel.children.length + size;
                dojo.forEach(airportModel.children, function (id, index) {
                    if (!arrivalAirportMultiFieldStore.get(id) && !arrivalAirportMultiFieldStore.isItemInGroup(id)) {
                        count++;
                    }
                });
                return size + count;
            }
            return size + 1;
        },

        isItemInGroup: function (id) {
            var arrivalAirportMultiFieldStore = this;

            var store = arrivalAirportMultiFieldStore.query(function (item) {
                if (!item.children) {
                    return false;
                }
                return (dojo.indexOf(item.children, id) > -1);
            });
            return (store.length > 0);
        },

        isChildrenInGroup: function (children) {
            var arrivalAirportMultiFieldStore = this;

            var store = arrivalAirportMultiFieldStore.query(function (item) {
                if (!item.children) {
                    return false;
                }
                return _.intersection(item.children, children).length;
            });
            return (store.length > 0);
        },

        summariseCount: function (itemsToIgnore) {
            var arrivalAirportMultiFieldStore = this;
            var count = 0;
            var i = 0;
            itemsToIgnore = itemsToIgnore || 1;

            if (arrivalAirportMultiFieldStore.data.length <= 1) return 0;

            // get itemsToIgnore items, if has children get children count
            dojo.some(arrivalAirportMultiFieldStore.data, function (item) {
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
            return arrivalAirportMultiFieldStore.selectedSize - count;
        }
    });

    return tui.searchPanel.store.flights.ArrivalAirportMultiFieldStore;
});
