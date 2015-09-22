define("tui/store/PostLoadable", [
  "dojo",
  "dojo/_base/connect",
  "dojo/store/Cache",
  "dojo/store/JsonRest",
  "dojo/store/Memory",
  "tui/store/StoreManager"], function(dojo, connect){
	
	dojo.declare("tui.store.PostLoadable", null , {
		
		// summary:
		//		Mixin class for widgets with Post load behaviour. 
		//
		// description:
		//		Once inital widget have been render to view. 
		//		We make a background call to our service, for given json.
		//
		// @author: Maurice Morgan.
		
		targetURL: "",
		
		idProperty: "id",
		
		store: null,
		
		//---------------------------------------------------------------- methods
		
		attachPostLoadEventListener: function(){
			// summary:
			//		Subscribes objects to channels by targetURL.
			//		Add kicks off the post loading for data.
			// description:
			//		If a targetURL is specified we will attempt to make a call
			//		for a post load JSON response. Subscribing to its channel for the results.
			var postLoadable = this;
			if (postLoadable.targetURL !== ""){
				// Subscribing a channel using the targetURL. If post load is successful we will save the store data.
				var postSuccessChannel = dojo.subscribe(postLoadable.targetURL, function(objFire, data, store){
					dojo.unsubscribe(postSuccessChannel);
					dojo.unsubscribe(postUnsuccessChannel);
					postLoadable.store = store;
					postLoadable.postLoaded();
				});
				
				// Subscribing a channel using the targetURL. If post load is unsuccessful.
				var postUnsuccessChannel = dojo.subscribe([postLoadable.targetURL,  "-unsuccess"].join(""), function(objFire, data, store){
					dojo.unsubscribe(postSuccessChannel);
					dojo.unsubscribe(postUnsuccessChannel);
					postLoadable.postLoadUnsuccessful();
				});
				
				if(!postLoadable.postLoad()){
					postLoadable.postLoaded()
				}
			} 
		},
		
		postLoad: function(){
			// summary:
			//		Loads given json data from target URL.
			// description:
			var postLoadable = this;
			
			if (!tui.store.StoreManager.isStoreRegistered(postLoadable.targetURL)){
				tui.store.StoreManager.setStore(postLoadable.targetURL, null);
				
				/*var memoryStore = new dojo.store.Memory({
					idProperty: postLoadable.idProperty
				});
				var jsonRestStore = new dojo.store.JsonRest({
					idProperty: postLoadable.idProperty,
					target: postLoadable.targetURL
				});
				postLoadable.store = new dojo.store.Cache(jsonRestStore, memoryStore);
				var resultFromQuery = postLoadable.store.query();
				resultFromQuery.then(function(data){
					tui.store.StoreManager.setStore(postLoadable.targetURL, postLoadable.store);
					dojo.publish(postLoadable.targetURL, [postLoadable, data, postLoadable.store]);
				})*/
				
				dojo.xhrGet({
					url: postLoadable.targetURL,
					handleAs: "json",
					load: function(data){
            			var memoryStore = new dojo.store.Memory({
							idProperty: postLoadable.idProperty,
							data: data
						})
						tui.store.StoreManager.setStore(postLoadable.targetURL, postLoadable.store);
						dojo.publish(postLoadable.targetURL, [postLoadable, data, memoryStore]);
        			},
        			error: function(){
        				dojo.publish([postLoadable.targetURL,  "-unsuccess"].join(""));
        			}
				});
				return true;
			}
			else if (tui.store.StoreManager.getStore(postLoadable.targetURL) === null){
				return true;
			}
			else if (tui.store.StoreManager.getStore(postLoadable.targetURL) !== null){
				tui.store.StoreManager.setStore(postLoadable.targetURL, postLoadable.store);
				return false;
			}
		},
		
		postLoaded: function(){
			
		},
		
		postLoadUnsuccessful: function(){

		},
		
		getStore: function(){
			var postLoadable = this;
			return postLoadable.store;
		}
		
	})
	
	return tui.store.PostLoadable;
})