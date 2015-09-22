define("tui/searchBResults/Mediator", [
  "dojo/_base/declare",
  "dojo/_base/lang",
  "dojo/_base/xhr",
  "dojo/_base/Deferred",
  "dojo/topic",
  "dojo/json",
  "dojo/on",
  "dojo/query",
  "dojo/dom-class",
  "dojo/dom-style",
  "dojo/hash",
  "dojox/encoding/digests/SHA1",
  "tui/utils/SessionStorage",
  "tui/searchBResults/service/ResultsMappingService",
  "tui/widget/_TuiBaseWidget",
  "tui/filterBPanel/view/ScrollControls"], function (declare, lang, xhr, Deferred, topic, JSON, on, query, domClass, domStyle,
                                          hash, sha1, sessionStore, resultsMappingService) {

  var TARGET_URL = dojoConfig.paths.webRoot + "/packages/customize";

  function doWhen(condition, f) {
    return condition ? f() : null;
  }

  function generateHash() {
    return sha1(window.location + new Date().valueOf());
  }

  return declare("tui.searchBResults.Mediator", [tui.widget._TuiBaseWidget, tui.filterBPanel.view.ScrollControls], {

    controllers: [],

    //targetUrl: dojoConfig.paths.webRoot + "/packages/customize",

    doAjaxRequest: true,

    model: null,

    originalModel: null,

    currentRequest: null,

    currentResponse: null,

    saveStateKey: null,

    saveStateKeyListName: null,

    pageId: null,

    currentHash: null,

    resultsNode: null,
    
    stickyFilter:null,

    sliderPPChanged: false,
    
    sliderTotalChanged: false,
    
    dateSliderChanged: false,

    holidayCountDisplay: false,

    saveKeyList: [],

    retrievedKeyList: [],

    lastRequestedSortBy: "",

    registerControls: function(controls){
      var widget = this;
      widget.controls = controls;
    },

    postCreate: function () {
      var mediator = this;
      mediator.originalModel = lang.clone(mediator.model);
      mediator.resultsNode = query(".search-results")[0];
      mediator.stickyFilter = query("#sticky-filter")[0];

      // restore state on load if hash present in url
      topic.subscribe("tui:channel=lazyload", function (){
    	  if(mediator.originalModel){
	    	  if(mediator.originalModel.searchRequest){
		    	  if(mediator.originalModel.searchRequest.sortBy != null){
		    		  topic.publish('tui.searchBResults.view.SortResultsSelectOption.updateValue', mediator.originalModel.searchRequest.sortBy);
		    	  }   
	    	  }
	      }
        doWhen(hash(), function(){
          mediator.restoreState(hash());
        });
        
        //updating the spinner position which comes on to the page when any filtering is done.
        mediator.updateSpinnerPosition();
        on(window, 'scroll', function(){
        	mediator.updateSpinnerPosition();
        });
      });

      // restore state on hashChange
      topic.subscribe("/dojo/hashchange", function(hashValue) {
        // but not if we're waiting on an AJAX response
        doWhen(mediator.doAjaxRequest, function(){
          hashValue ? mediator.restoreState(hashValue) : mediator.reset();
        });
      });
    },

    reset: function () {
      var mediator = this;
      mediator.model = lang.clone(mediator.originalModel);
      try {
        if(sessionStore.getItem(mediator.saveStateKey)) {
          sessionStore.removeItem(mediator.saveStateKey);
        }
      } catch (e) {
        _.debug("No state in secure mode");
      }

      if (_.has(mediator.model.searchResult, "filterPanel")) {
        mediator.model.searchResult.filterPanel.repaint = true;
      }
      mediator.publishToControllers('clearAll', null, null, mediator.model);
    },

    saveCriteria: function (data, key, field, oldValue, newValue){
      var mediator = this,
      	  savedState,
      	  saveKey = hash();
      try {
    	mediator.saveStateKeyListName = mediator.saveStateKey+"List";
        savedState = sessionStore.getItem(mediator.saveStateKey);
      } catch(e){
        _.debug("Can't restore state in secure mode");
      }
      if(field === "paginate"){
         data.first = newValue;
      }
      if(savedState) {
        if(savedState[saveKey]) {
          savedState[saveKey][key] = data;
        } else {
          savedState[saveKey] = {};
          savedState[saveKey][key] = data;
        }
      } else {
        savedState = {};
        savedState[saveKey] = {};
        savedState[saveKey][key] = data;
      }

      field ? savedState[saveKey].field = field : null;
      oldValue ? savedState[saveKey].oldValue = oldValue : null;
      newValue ? savedState[saveKey].newValue = newValue : null;

      try {
    	mediator.saveKeyList.push(saveKey);
    	sessionStore.setItem(mediator.saveStateKeyListName, mediator.saveKeyList);
        sessionStore.setItem(mediator.saveStateKey, savedState);
      } catch (e) {
        _.debug("Can't save state in secure mode");
      }
    },

    hasAppliedFilter: function(savedStateList){
         var mediator = this,
         	 savedList = [];
         if(savedStateList && savedStateList.length){
        	 savedList = savedStateList.slice(0);
	         while(savedList.length){
	        	 savedState = sessionStore.getItem(mediator.saveStateKey);
	        	 savedData = savedState[savedList.pop()];
	        	 switch(savedData.field.toLowerCase()){
	        		 case "applyfilters":
	        		 case "dateslider":
	        		 case "clearfilters":
	        		 case "duration":
	        		 case "rooms":
	        		 case "clearrooms":
	        		 case "clearall": return true;
	        		 	 break;
	        	 }
	         }
         }
         return false;
    },

    restoreState: function (key) {
      var mediator = this, savedState, savedData, savedStateList;
      try {
    	mediator.saveStateKeyListName = mediator.saveStateKey+"List";
        savedState = sessionStore.getItem(mediator.saveStateKey);
        savedStateList = sessionStore.getItem(mediator.saveStateKeyListName);
        mediator.retrievedKeyList.push(savedStateList.pop());
        sessionStore.setItem(mediator.saveStateKeyListName, savedStateList);
      } catch(e){
        _.debug("Can't restore state in secure mode");
      }
      if(!savedState || !savedState[key]) return;
      savedData = savedState[key];
	  switch(savedData.field){
    	  case "paginate": mediator.restoreState(savedStateList.pop());
    	  				   return;
    		  break;
    	  case "sortBy": mediator.lastRequestedSortBy = savedData.criteria.sortBy;
    	  				if(mediator.hasAppliedFilter(savedStateList)){
    	  					mediator.restoreState(savedStateList.pop());
    	  				   return;
    	  				}
    	  				topic.publish("tui:channel=lazyloadfilter");
    	  	  break;
    	  default:  topic.publish("tui:channel=lazyloadfilter");
    	  			savedData.criteria.sortBy = mediator.lastRequestedSortBy ? mediator.lastRequestedSortBy : savedData.criteria.sortBy;
    	  			mediator.lastRequestedSortBy = "";
  			  break;
	  }
      try {
        sessionStore.setItem(mediator.saveStateKey, savedState);
      } catch (e) {
        _.debug("Can't save state in secure mode");
      }

      mediator.currentRequest = savedData.criteria;
      var hasFilters = _.has(savedData.criteria, "filters") && _.size(savedData.criteria['filters']) > 0;

      // tell sort results to update
      topic.publish('tui.searchBResults.view.SortResultsSelectOption.updateValue', savedData.criteria.sortBy);

      // update rooms summary
      var rooms = _.map(lang.clone(savedData.criteria.rooms), function(room){
        room.id = "Room " + room.id;
        return room;
      });
      _.size(rooms) > 0 ? topic.publish("tui.searchBResults.roomEditor.view.RoomsSummary.renderSummary", rooms) : null;
      // tell checkboxes to update selected state
      doWhen(hasFilters, function(){
        _.each(savedData.criteria.filters, function(filterGroup){
          if(_.isArray(filterGroup)){
            _.each(filterGroup, function(filter){
              topic.publish("tui:channel=updateCheckboxState/"+filter.id, filter);
            });
          } else {
        	  topic.publish("tui:channel=updateSlider/"+filterGroup.code, filterGroup);
          }
        });
      });

      mediator.fire(savedData.field, savedData.oldValue, savedData.newValue, true);

      // sliders update
      /*doWhen(hasFilters, function(){
        _.each(savedData.criteria.filters, function(filterGroup){
          if(!_.isArray(filterGroup) && filterGroup.changed){
            topic.publish("tui:channel=updateSliderPos", [filterGroup.code, filterGroup.min, filterGroup.max, filterGroup.maxValue]);
          }
        });
      });*/
    },

    adaptRequest: function(request) {
      var mediator = this;
      // todo : update serescomp with page #;
      request.offset = request.first > 1 ? (request.first - 1) * mediator.offset : mediator.offset;
      request.first = (request.searchRequestType === 'paginate') ? 2 : 1;

      return request;
    },

    adaptSaveData: function () {
      var mediator = this,
          responseToSave = lang.clone(mediator.currentResponse || mediator.model);

      // if available dates empty, reload from model, ideally at server side but forced to do client side
      if (mediator.currentResponse && !mediator.currentResponse.searchResult.availableDates.availableValues.length) {
        responseToSave.searchResult.availableDates = mediator.model.searchResult.availableDates;
      }

      mediator.saveCriteria(lang.clone(responseToSave), 'response');
    },

    registerController: function (controller, dataPath) {
      var mediator = this;
      mediator.controllers.push(controller);
      //Take from session store if its history back button
      var response = mediator.currentResponse || mediator.model;
      if (_.isArray(dataPath)) {
        return _.map(dataPath, function (path) {
          return lang.getObject(path || '', false, response);
        });
      } else {
        return lang.getObject(dataPath || controller.dataPath || '', false, response);
      }
    },

    publishToControllers: function (field, oldValue, newValue, response, isCached, sliderPPChanged, sliderTotalChanged, dateSliderChanged) {
      var mediator = this;
      _.each(mediator.controllers, function (controller) {
        controller.refresh(field, oldValue, newValue, lang.getObject(controller.dataPath || '', false, response), isCached, sliderPPChanged, sliderTotalChanged, dateSliderChanged, response);
      });
    },

    handleNoResults: function (field, oldValue, newValue) {
      var mediator = this, savedState, saveKey = hash();
      try {
        savedState = sessionStore.getItem(mediator.saveStateKey);
      } catch(e){
        _.debug("Can't restore state in secure mode");
      }
      if(savedState && savedState[saveKey]){
        delete savedState[saveKey];
        try {
          sessionStore.setItem(mediator.saveStateKey, savedState);
        } catch (e) {
          _.debug("Can't save state in secure mode");
        }
      }
      mediator.doAjaxRequest = false;
      hash('');
      mediator.doAjaxRequest = true;
      _.each(mediator.controllers, function (controller) {
        controller.handleNoResults ? controller.handleNoResults(field, oldValue, newValue) : null;
      });
    },

    generateRequest: function (field) {
      var mediator = this, searchRequest = lang.clone(mediator.model.searchRequest);
      var hashGen = hash(generateHash());
      var request = {
        airports: searchRequest.airports,
        units: searchRequest.units,
        duration: searchRequest.duration,
        when: searchRequest.when,
        durationSelection: mediator.model.searchResult.durationSelection,
        availableDates: mediator.model.searchResult.availableDates,
        backToDestinationOptionData: mediator.model.searchResult.filterPanel.destinationOptions,
        until: searchRequest.until,
        flexibility: searchRequest.flexibility,
        flexibleDays: searchRequest.flexibleDays,
        noOfAdults: searchRequest.noOfAdults,
        noOfSeniors: searchRequest.noOfSeniors,
        noOfChildren: searchRequest.noOfChildren,
        childrenAge: searchRequest.childrenAge,
        first: searchRequest.page,
        priceView: searchRequest.priceView,
        hashValue: hashGen
      };
      _.each(mediator.controllers, function (controller) {
        lang.mixin(request, controller.generateRequest ? controller.generateRequest(field) : {});
      });
      
      //Updating the searchType separately with the value from actual search request criteria to prevent getting this attribute over ridden from search panel controller loaded on the page 
      request.searchType = searchRequest.searchType;
      
      if(mediator.pageId === 'flightOptions') {
        request.flightOptions = true;
        request.packageId = searchRequest.packageId;
        request.selectedBoardBasis = searchRequest.selectedBoardBasis;
      }
      return request;
    },

    lastSuccessfulRequest: function () {
      return this.currentRequest || this.model.searchRequest;
    },

    fire: function (field, oldValue, newValue, cachedRequest) {
      var mediator = this, cachedRequest = cachedRequest || false, request = null;

      !hash() ? hash(generateHash()) : null;

      doWhen(mediator.doAjaxRequest, function () {
        mediator.beforeRequest();
        if(cachedRequest && mediator.pageId != 'singleAccommodationResults') {
        	mediator.model.searchRequest = mediator.currentRequest ;
        }
        if(cachedRequest){
        	window.location.href = window.location.href.replace(/(.+?#).+?(=.*)/g,"$1"+mediator.currentRequest.hashValue+"$2")
        	request = mediator.currentRequest;
        } else {
        	request = mediator.generateRequest(field);
        }
        // DE29458
        /* if(request.searchRequestType !== 'Dateslider' && mediator.currentRequest != null) {
        	request.when = mediator.currentRequest.when;
            request.until = mediator.currentRequest.until;
            if(request.searchRequestType !== 'Duration') request.flexibility = mediator.currentRequest.flexibility;
        } */
        /*if(request.first > 1 && cachedRequest) {
        	request.offset = request.first * mediator.offset;
        	request.first = 1;
        }*/  
        // if duration or date slider search , then holiday count should be shown
        if(request.searchRequestType == 'Duration' || request.searchRequestType == 'Dateslider') mediator.holidayCountDisplay = true;
        else mediator.holidayCountDisplay = false;
        // update searchResultsPage # if using cachedRequest
        cachedRequest ? topic.publish('tui.searchBResults.view.SearchResultsComponent.updatePage', request.first) : null;
        var xhrReq = xhr.post({
          url: TARGET_URL,
          content: {searchCriteria: JSON.stringify(request)},
          handleAs: "json",
          load: function (response, options) {
            mediator.handleResults(response, options);
          },
          error: function (err) {
            _.debug(err);
            mediator.afterFailure();
          },
          field: field,
          oldValue: oldValue,
          newValue: newValue,
          req: request,
          isCached: cachedRequest
        });
      });
      //window.scrollTo(mediator.originalStickyPos.x, mediator.originalStickyPos.y);
    },

    handleResults: function (promise, options) {
      var mediator = this;

      Deferred.when(promise, function (response) {
        var field = options.args.field,
            oldValue = options.args.oldValue,
            newValue = options.args.newValue,
            cached = options.args.isCached;
        if(cached && response.searchRequest.duration != null && response.searchRequest.duration < 50 && response.searchResult.durationSelection != null && response.searchResult.endecaResultsCount != 0){
        	if(response.searchResult.durationSelection.activeDuration != response.searchRequest.duration) {
            response.searchResult.durationSelection.activeDuration = response.searchRequest.duration;
                if(response.searchRequest.searchRequestType != 'Dateslider') mediator.dateSliderChanged = true;
        	}
            mediator.afterSuccess();
        }

        switch(response.searchRequest.searchRequestType){
              case "Dateslider":mediator.dateSliderChanged = false;
        	mediator.afterSuccess();
    					  break;
              case "Filter": topic.publish("tui/widget/sticky/reposition");
              			  break;
        }
        if (response.searchRequest != null && response.searchRequest.filters != null) {
        	 if(response.searchRequest.filters.budgetpp != null && response.searchRequest.filters.budgetpp.changed) mediator.sliderPPChanged = true;
             if(response.searchRequest.filters.budgettotal != null && response.searchRequest.filters.budgettotal.changed) mediator.sliderTotalChanged = true;
             mediator.afterSuccess();
        }
        if (!cached && _.has(response.searchResult, "filterPanel") && response.searchRequest.searchRequestType == 'Sort') {
            response.searchResult.filterPanel.repaint = false;
            mediator.afterSuccess();
          }
        if (response.searchResult.endecaResultsCount === 0) {
          mediator.handleNoResults(field, oldValue, newValue);
          mediator.afterSuccess();
          mediator.doAjaxRequest = false;
          setTimeout(function(){
        	  mediator.doAjaxRequest = true;
          }, 1000);
        }
        else {
          response = resultsMappingService.mapResponse(lang.clone(mediator.model), lang.clone(response), field, newValue);

          mediator.currentRequest = mediator.adaptRequest(lang.clone(options.args.req));
          mediator.saveCriteria(mediator.currentRequest, 'criteria', field, oldValue, newValue);

          mediator.currentResponse = lang.clone(response);
          //mediator.adaptSaveData();

          //duration & rooms is a new search so reset the model.
          if (field === 'duration' || field === 'rooms') {
            mediator.model = response;
          }
          // pagination > concat holidays
          if (field === 'paginate') {
            // TODO: look at this, seems like it would break pagination
            var existingHolidays = lang.clone(mediator.model.searchResult.holidays);
            mediator.currentResponse.searchResult.holidays = existingHolidays.concat(response.searchResult.holidays || []);
          }
          mediator.publishToControllers(field, oldValue, newValue, response, cached, mediator.sliderPPChanged, mediator.sliderTotalChanged, mediator.dateSliderChanged);
          mediator.afterSuccess();
        }

      });
      if(mediator.holidayCountDisplay) {
        var holidayNode = {
          node: "count-container",
          duration: 3000
        }
        setTimeout(function() {
          dojo.fadeOut(holidayNode).play();
        }, 3000);
        setTimeout(function() {
        	dojo.setStyle(dojo.query(".count-container")[0], 'display', 'none');  
          }, 6000);
      }
    },

    beforeRequest: function () {
      var mediator = this;
      domClass.add(mediator.resultsNode, 'updating');
      if(domClass.contains(mediator.stickyFilter, "fix-me")){
    	  domClass.add(mediator.stickyFilter, 'updating'); 
      }            
      mediator.doAjaxRequest = false;
    },

    afterSuccess: function () {
      var mediator = this;
      domClass.remove(mediator.stickyFilter, 'updating');
      domClass.remove(mediator.resultsNode, 'updating');
      if(mediator.holidayCountDisplay){
    	  dojo.setStyle(dojo.query(".count-container")[0], 'opacity', 1);
    	  dojo.setStyle(dojo.query(".count-container")[0], 'display', 'block');      	  
      }
      mediator.doAjaxRequest = true;
      sessionStore.removeItem("thClickedElement");
    },

    afterFailure: function () {
      var mediator = this;
      domClass.remove(mediator.stickyFilter, 'updating');
      domClass.remove(mediator.resultsNode, 'updating');
      mediator.doAjaxRequest = true;
      topic.publish("tui.searchBResults.view.SearchResultsComponent.renderAjaxErrorPopup");
    },
    
    updateSpinnerPosition: function() {
    	var mediator = this;
    	var searchResultsContainerDom = dojo.query(".search-results")[0];
    	var interactiveMask = dojo.query(".mask-interactivity.full")[0];    	
    	var	 searchResultsContainer = searchResultsContainerDom.getBoundingClientRect();
    	
        if(searchResultsContainer.top <= 0 && searchResultsContainer.bottom > window.innerHeight)
        	domClass.add(interactiveMask,"fixed");    	        	    	          	            
        else
        	domClass.remove(interactiveMask,"fixed");  
        
        if(searchResultsContainer.bottom <= window.innerHeight){
        	domClass.add(mediator.stickyFilter,"bottom");
        	domClass.add(interactiveMask,"bottom");         	        	           
        }        	
        else{        	
        	domClass.remove(mediator.stickyFilter,"bottom");
        	domClass.remove(interactiveMask,"bottom");  
        }
        	
    }

  });
});