define('tui/villaAvailability/store/VillaAvailabilityStore', [
  'dojo',
  'tui/villaAvailability/model/VillaDateItemModel',
  'dojo/store/Memory'
], function (dojo, VillaModel) {

  dojo.declare('tui.villaAvailability.store.VillaAvailabilityStore', [dojo.store.Memory], {

    putData: function (object) {
      var store = this;
      var itemExists = store.get(object.id);
      if (itemExists) {
        itemExists.days = itemExists.days.concat(object.days);
        store.remove(object.id);
        store.put(itemExists);
      } else {
        store.put(object);
      }
    },

    put: function (object) {
      var store = this;
      // map days to VillaDateItemModel > fills in defaults and adds required attributes missing from backend data
      object.days = _.map(object.days, function (day) {
        return new VillaModel(day);
      });
      // remove duplicates > handles possibility of duplicate days returned
      object.days = _.uniq(object.days, 'when');
      store.inherited(arguments);
    },

    emptyStore: function () {
      // clear out the store when a new search is performed
      var store = this,
          data = store.query();
      _.each(data, function (dataItem) {
        store.remove(dataItem.id)
      });
    }

  });

  return tui.villaAvailability.store.VillaAvailabilityStore;
});