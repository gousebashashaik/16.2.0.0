define("tui/showIGPackages/Mediator", [
    "dojo",
    "dojo/_base/xhr",
    "tui/utils/SessionStorage",
    "dojo/_base/connect",
    "tui/searchResults/Mediator",
    "tui/widget/_TuiBaseWidget"], function (dojo, xhr, sessionStore, connect) {

    function doWhen(condition, f) {
        return condition ? f() : null;
    }
    /*
     * TODO: 
     * Filter panel configurations and code refactor
     * */
    dojo.declare("tui.showIGPackages.Mediator", [ tui.searchResults.Mediator], {

        controllers: [],

        targetUrl: dojoConfig.paths.webRoot + "/inventory/customize",

        doAjaxRequest: true,

        model: null,
        
        flag: true,

        currentRequest: null,

        currentResponse: null,

        searchControllerKlassIds : ["searchController","searchBController","getPriceController", "getPriceBController"],

        constructor: function () {
            var mediator = this;
            connect.subscribe("tui/showIGPackages/Mediator/saveState", function () {
                var responseToSave = dojo.clone(mediator.currentResponse || mediator.model);
                var requestToSave = dojo.clone(mediator.currentRequest || mediator.model.merchandiserRequest);

                // if available dates empty, reload from model, ideally at server side but forced to do client side
                if(mediator.currentResponse && !mediator.currentResponse.merchandiserResult.availableDates.availableValues.length) {
                    responseToSave.merchandiserResult.availableDates = mediator.model.merchandiserResult.availableDates
                }

                // also save original date in merchandiserRequest > have to do this for both response and request <rolleyes>
                requestToSave.when = mediator.model.merchandiserRequest.when;
                requestToSave.until = mediator.model.merchandiserRequest.until;
                requestToSave.flexibility = mediator.model.merchandiserRequest.flexibility;
                
                responseToSave.merchandiserRequest.when = mediator.model.merchandiserRequest.when;
                responseToSave.merchandiserRequest.until = mediator.model.merchandiserRequest.until;
                responseToSave.merchandiserRequest.flexibility = mediator.model.merchandiserRequest.flexibility;

                //filter selections should be retained, ideally at server side but forced to do client side
               // responseToSave['merchandiserResult']['filterPanel'] = mediator.model['merchandiserResult']['filterPanel'];

                sessionStore.setItem('searchResultsState', {'criteria': requestToSave, 'response': responseToSave});
            });

            var savedState = sessionStore.getItem('searchResultsState');
            if (savedState) {
                mediator.currentRequest = savedState.criteria;
                mediator.currentResponse = savedState.response;
                sessionStore.removeItem('searchResultsState');
            }
        },
        
        beforeRequest: function () {
            var mediator = this;
            if(mediator.targetUrl == dojoConfig.paths.webRoot+"/deals/customize"){
                var resultsNode = dojo.query(".result-view")[0];
            }else{
            	var resultsNode = dojo.query(".search-results")[0];
            }
            dojo.addClass(resultsNode, 'updating');
            mediator.doAjaxRequest = false;
        },

        generateRequest: function (field) {
            var mediator = this;
            var request = {};
            
             if(mediator.targetUrl == dojoConfig.paths.webRoot+"/deals/customize"){
            	 request['pageLabel'] = mediator.model.dealsCollectionResult.merchandiserRequest.pageLabel;
            }else{
            	  request['pageLabel'] = mediator.model.merchandiserRequest.pageLabel;
            	  request['inventoryLabel'] = mediator.model.merchandiserRequest.inventoryLabel;            	  
            }
             if(mediator.targetUrl == dojoConfig.paths.webRoot+"/inventory/customize"){
               //removing the searchControllers/get-price controllers if any from mediator.controllers
               if(mediator.flag){
            	   mediator.controllers = _.filter(mediator.controllers,function(controller){
            		   return _.indexOf(mediator.searchControllerKlassIds, dojo.attr(controller.domNode,"data-klass-id")) == -1;
            	   });
                  mediator.flag = false;
               }
             }
           
            _.each(mediator.controllers, function (controller) {
                dojo.mixin(request, controller.generateRequest ? controller.generateRequest(field) : {});
            });
            return request;
        },
        
        afterSuccess: function () {
            var mediator = this;
            if(mediator.targetUrl == dojoConfig.paths.webRoot+"/deals/customize"){
            	 dojo.removeClass(dojo.query(".result-view")[0], 'updating');
            }else{
            	 dojo.removeClass(dojo.query(".search-results")[0], 'updating');
            }
            mediator.doAjaxRequest = true;
        },

        afterFailure: function () {
            var mediator = this;
            
            if(mediator.targetUrl == dojoConfig.paths.webRoot+"/deals/customize"){
           	 dojo.removeClass(dojo.query(".result-view")[0], 'updating');
           }else{
           	 dojo.removeClass(dojo.query(".search-results")[0], 'updating');
           }
            mediator.doAjaxRequest = true;
            dojo.publish("tui.searchResults.view.SearchResultsComponent.renderAjaxErrorPopup")
        },

        
        fire: function (field, oldValue, newValue) {
            var mediator = this;
            doWhen(mediator.doAjaxRequest, function () {
                mediator.beforeRequest();

                var request = mediator.generateRequest(field);
                var results = xhr.post({
                    url: mediator.targetUrl,
                    content: {searchCriteria: dojo.toJson(request)},
                    handleAs: "json",
                    error: function (err) {
                        if (dojoConfig.devDebug) {
                            console.error(err);
                        }
                        mediator.afterFailure();
                    }
                });

                dojo.when(results, function (response) {
                	if(response.dealsCollectionResult != undefined) {
                		
                       	 _.forEach(response.dealsCollectionResult.tabbedDealsList, function(response){
                       		 mediator.count = response.merchandiserResult.endecaResultsCount;
                       	 });
                       	 
                       	 if (mediator.count === 0) {
                             mediator.handleNoResults(field, oldValue, newValue);
                         } else {
                            // mediator.adaptResponse(mediator.currentResponse || mediator.model, response, field);
                             mediator.currentRequest = request;
                             mediator.currentResponse = response;
                             //duration is a new search so reset the model.
                             if (field === 'duration') {
                                 mediator.model = response;
                             }
                             mediator.publishToControllers(field, oldValue, newValue, response);
                         }
                	}else {
                		
                		 if (response.merchandiserResult.endecaResultsCount === 0) {
                             mediator.handleNoResults(field, oldValue, newValue);
                         } else {
                            // mediator.adaptResponse(mediator.currentResponse || mediator.model, response, field);
                             mediator.currentRequest = request;
                             mediator.currentResponse = response;
                             //duration is a new search so reset the model.
                             if (field === 'duration') {
                                 mediator.model = response;
                             }
                             mediator.publishToControllers(field, oldValue, newValue, response);
                         }
                	}
                   
                    mediator.afterSuccess();
                });

            });

        }
        
    });

    return tui.showIGPackages.Mediator;
});