define("tui/searchResults/VariantMediator", [
  "dojo/_base/declare",
  "dojo/_base/lang",
  "dojo/_base/xhr",
  "dojo/_base/Deferred",
  "dojo/topic",
  "dojo/json",
  "dojo/on",
  "dojo/query",
  "dojo/dom-class",
  "dojo/hash",
  "dojox/encoding/digests/SHA1",
  "tui/utils/SessionStorage",
  "tui/searchResults/service/VariantResultsMappingService",
  "tui/searchResults/Mediator",
  "tui/widget/_TuiBaseWidget"], function (declare, lang, xhr, Deferred, topic, JSON, on, query, domClass,
                                          hash, sha1, sessionStore, resultsMappingService) {

  var TARGET_URL = dojoConfig.paths.webRoot + "/packages/customize";

  function doWhen(condition, f) {
    return condition ? f() : null;
  }

  function generateHash() {
    return sha1(window.location + new Date().valueOf());
  }

  return declare("tui.searchResults.VariantMediator", [tui.searchResults.Mediator], {

    controllers: [],

    //targetUrl: dojoConfig.paths.webRoot + "/packages/customize",

    doAjaxRequest: true,

    model: null,

    originalModel: null,

    currentRequest: null,

    currentResponse: null,

    saveStateKey: null,

    pageId: null,

    currentHash: null,

    resultsNode: null,

    adaptRequest: function(request) {
      var mediator = this;
      // todo : update serescomp with page #;
      //request.offset = request.page > 1 ? (request.page - 1) * mediator.offset : mediator.offset;
      request.page = (request.searchRequestType === 'paginate') ? 2 : 1;

      return request;
    },


    generateRequest: function (field) {
       var mediator = this, searchRequest = lang.clone(mediator.model.searchRequest);
       var hashGen = hash(generateHash());
       var request = {
          from: searchRequest.from,
          to: searchRequest.to,
          duration: searchRequest.duration,
          when: searchRequest.when,
          until: searchRequest.until,
          flexibility: searchRequest.flexibility,
          flexibleDays: searchRequest.flexibleDays,
          addAStay: searchRequest.addAStay,
          noOfAdults: searchRequest.noOfAdults,
          noOfSeniors: searchRequest.noOfSeniors,
          noOfChildren: searchRequest.noOfChildren,
          childrenAge: searchRequest.childrenAge,
          page: searchRequest.page,
          searchCriteriaType: searchRequest.searchCriteriaType,
          hashValue: hashGen
       };
       _.each(mediator.controllers, function (controller) {
          lang.mixin(request, controller.generateRequest ? controller.generateRequest(field) : {});
       });
       if(mediator.pageId === 'flightOptions') {
          request.flightOptions = true;
          request.packageId = searchRequest.packageId;
          request.selectedBoardBasis = searchRequest.selectedBoardBasis;
       }
       return request;
    },

    fire: function (field, oldValue, newValue, cachedRequest) {
      var mediator = this, hashFlag = true, cachedRequest = cachedRequest || false;

      _.each(mediator.controllers, function(cntr){
    	  if( cntr["data-klass-id"] == "cruiseBrowserFilter"){ hashFlag = false; }
      });
      hashFlag && (!hash()) ? hash(generateHash()) : null;

      doWhen(mediator.doAjaxRequest, function () {
        mediator.beforeRequest();
        var request = cachedRequest ? mediator.currentRequest : mediator.generateRequest(field);
        if( cachedRequest && field == 'paginate' ){ newValue =  2; }
        // update searchResultsPage # if using cachedRequest
        cachedRequest ? topic.publish('tui.searchResults.view.SearchResultsComponent.updatePage', request.page) : null;
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
    },

    handleResults: function (promise, options) {
      var mediator = this;

      Deferred.when(promise, function (response) {
        var field = options.args.field,
            oldValue = options.args.oldValue,
            newValue = options.args.newValue,
            cached = options.args.isCached;
        if (response.searchResult.endecaResultsCount === 0) {
          mediator.handleNoResults(field, oldValue, newValue);
          mediator.afterSuccess();
          mediator.doAjaxRequest = false;
          setTimeout(function(){
        	  mediator.doAjaxRequest = true;
          }, 1000);
          topic.publish("tui/widget/maps/cruise/CruiseBrowseResults/updateCount", mediator.model);
        } else {
          topic.publish("tui/widget/maps/cruise/CruiseBrowseResults/updateCount", response);
          response = resultsMappingService.mapResponse(lang.clone(mediator.model), lang.clone(response), field, newValue);

          mediator.currentRequest = mediator.adaptRequest(lang.clone(options.args.req));
          mediator.saveCriteria(mediator.currentRequest, 'criteria', field, oldValue, newValue);

          if (response.searchRequest != null && response.searchRequest.filters != null) {
         	 if(response.searchRequest.filters.budgetpp != null && response.searchRequest.filters.budgetpp.changed) mediator.sliderPPChanged = true;
              if(response.searchRequest.filters.budgettotal != null && response.searchRequest.filters.budgettotal.changed) mediator.sliderTotalChanged = true;
              if(response.searchRequest.filters.sailingDate != null && response.searchRequest.filters.sailingDate[0] != null ) mediator.sailingDateChanged = response.searchRequest.filters.sailingDate[0].selected;
              mediator.afterSuccess();
         }

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
          mediator.publishToControllers(field, oldValue, newValue, response, cached,mediator.sliderPPChanged,mediator.sliderTotalChanged, mediator.sailingDateChanged);
          mediator.afterSuccess();
        }

      });
    },



    beforeRequest: function () {
      var mediator = this;
      domClass.add(mediator.resultsNode, 'updating');
      mediator.doAjaxRequest = false;
    },

    afterSuccess: function () {
      var mediator = this;
      domClass.remove(mediator.resultsNode, 'updating');
      mediator.doAjaxRequest = true;
    },

    afterFailure: function () {
      var mediator = this;
      domClass.remove(mediator.resultsNode, 'updating');
      mediator.doAjaxRequest = true;
      topic.publish("tui.searchResults.view.SearchResultsComponent.renderAjaxErrorPopup");
    }

  });
});