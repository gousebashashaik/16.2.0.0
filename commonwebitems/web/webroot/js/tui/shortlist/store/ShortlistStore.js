define("tui/shortlist/store/ShortlistStore", [
	"dojo",
	"dojo/_base/xhr",
	"dojo/store/Observable",
	"tui/utils/AjaxQueue",
	"dojo/store/Memory"], function (dojo, xhr, Observable, ajaxQueue) {

	var requestParams = {
		add: ["packageId", "searchCriteria"],
		list: [],
		remove: ["packageId"]
	};

	dojo.declare("tui.shortlist.store.ShortlistStore", [dojo.store.Memory], {

		// ----------------------------------------------------------------------------- properties

		targetUrls: null,

		maxItems: 0,

		// ----------------------------------------------------------------------------- methods

		requestData: function (/*Boolean*/queue, /*String?*/ reqType, /*String?*/ packageId) {
            // summary:
            //		Ajax request handling
            var shortlistStore = this, searchCriteria;

            // TODO: disable adding if maxItems reached

            if(dijit.registry.byId('mediator')) {
                searchCriteria = dojo.clone(dijit.registry.byId('mediator').model.searchRequest);
            } else if(shortlistStore.jsonData != null){
                searchCriteria = {
                    when: shortlistStore.jsonData.searchRequestData.when,
                    until: shortlistStore.jsonData.searchRequestData.until,
                    flexibility: shortlistStore.jsonData.searchRequestData.flexibility,
                    flexibleDays: shortlistStore.jsonData.searchRequestData.flexibleDays,
                    noOfAdults: shortlistStore.jsonData.searchRequestData.noOfAdults,
                    noOfSeniors: shortlistStore.jsonData.searchRequestData.noOfSeniors,
                    noOfChildren: shortlistStore.jsonData.searchRequestData.noOfChildren,
                    childrenAge: shortlistStore.jsonData.searchRequestData.childrenAge,
                    duration: shortlistStore.jsonData.searchRequestData.duration,
                    first: shortlistStore.jsonData.searchRequestData.first,
                    airports: shortlistStore.jsonData.searchRequestData.airports,
                    units: shortlistStore.jsonData.searchRequestData.units,
                    searchRequestType: ""
                };
            }

			if(shortlistStore.disabled && reqType === "add") return;

			// Handle required params for request content depending on request type
			reqType = reqType || "list";

			// determine param values based on request type
			var reqContent = reqType === "list" ? "" : {};
			_.forEach(requestParams[reqType], function(param){
				reqContent[param] = (param === "packageId") ? packageId : dojo.toJson(searchCriteria);
			});

			if(reqType == "add") shortlistStore.targetUrl = dojoConfig.paths.webRoot+"/SearchShortlist/shortlist/add";
			else if(reqType == "remove") shortlistStore.targetUrl = dojoConfig.paths.webRoot+"/SearchShortlist/shortlist/remove";
			else shortlistStore.targetUrl = dojoConfig.paths.webRoot+"/SearchShortlist/shortlist/list";
			
			// ajax request defaults
			var requestTemplate = {
				url:shortlistStore.targetUrl,
				handleAs: "json",
				error: function(err){
					if(dojoConfig.devDebug){
						throw "Ajax Error: " + err;
					}
				},
				reqType: reqType,
				packageId: packageId
			};

			// list requests shouldn't be queued
			if(!queue) {
				var results = xhr.post(dojo.mixin(requestTemplate, {content: reqContent}));
				shortlistStore.updateStore(results, {reqType: reqType, packageId: packageId});
			} else {
				// add load method
				ajaxQueue.send("shortlist", dojo.mixin(requestTemplate, {
					content: reqContent,
					load: function(response, options) {
						shortlistStore.updateStore(response, options);
					}
				}), "post");
			}
		},

		updateStore: function (promise, options) {
			// summary:
			//		Callback method for ajax response
			var shortlistStore = this;

			// handle response based on request type
			dojo.when(promise, function(response){
				switch(options.reqType) {
					case "list":
						_.forEach(response, function(packageId){
							shortlistStore.put({"id": packageId, "package": packageId});
						});
						break;
					case "add":
						if(response) {
							shortlistStore.put({"id": options.packageId, "package": options.packageId});
						}
						break;
					case "remove":
						if(response) {
							shortlistStore.remove(options.packageId);
						}
						break;
				}

				dojo.publish("tui:channel=shortlistStoreDisabled", shortlistStore.getCount() >= shortlistStore.maxItems);

				if(shortlistStore.getCount() >= shortlistStore.maxItems && options.reqType !== "remove") {
					ajaxQueue.clearQueue("shortlist");
				}
			});
		},

		getCount: function () {
			// summary:
			//		Return size of shortlistStore
			var shortlistStore = this;
			return shortlistStore.query().total;
		},

		getObservable: function () {
			// summary:
			//		Returns ShortlistStore instance instantiated as dojo/store/Observable
			var shortlistStore = this;
			if (!shortlistStore.observabable) {
				shortlistStore.observabable = new Observable(shortlistStore);
			}
			return shortlistStore.observabable;
		}

	});

	return tui.shortlist.store.ShortlistStore;
});