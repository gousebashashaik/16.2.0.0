define(["dojo/_base/lang"], function(lang){
	
	var store = lang.getObject("tui.store", true);

	dojo.setObject('StoreManager', {
		stores: {},
		setStore: function(key, store){
			this.stores[key] = store;
		},
		getStore: function(key){
			return this.stores[key]
		},
		isStoreRegistered: function(key){
			return (this.stores[key] !== undefined);
		}
	}, store);
})