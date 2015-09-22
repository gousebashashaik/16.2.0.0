define("tui/searchResults/view/VariantNoResultView", [
  "dojo",
  "dojo/on",
  "dojo/query",
  "tui/utils/AjaxQueue",
  "dojo/text!tui/searchResults/view/templates/noResultsAltVariantSearchTmpl.html",
  "dojo/io-query",
  "tui/searchResults/view/NoResultView",
  "tui/widget/_TuiBaseWidget"], function (dojo, on, query, ajaxQueue, tmpl, ioQuery, tagMappingTable) {

  //var widenRequestTypes = ["WHEREFROM", "WHERETO", "WHEN", "DURATION","STAYDURATION"];//,

  dojo.declare("tui.searchResults.view.VariantNoResultView", [tui.searchResults.view.NoResultView], {

    // ---------------------------------------------------------------- properties

    ajaxUrl: dojoConfig.paths.webRoot + "/widenCriteria",

    linkUrl: dojoConfig.paths.webRoot + "/packages?",

    optionsNode: null,

    listNode: null,

    numRequests: 0,

    requestLimit: 4,

    searchOptions: null,

    stay: false,
	
	widenRequestTypes: ["WHEREFROM", "WHERETO", "WHEN", "DURATION", "STAYDURATION"],

    // ---------------------------------------------------------------- methods

    postCreate: function () {
      var noResultView = this, text, searchRequest = null;
	 widenRequestTypes = noResultView.widenRequestTypes;
     // noResultView.inherited(arguments);
      noResultView.searchOptions = [];
      noResultView.optionsNode = dojo.query(".options", noResultView.domNode)[0];
      noResultView.listNode = dojo.query(".js-widened-criteria", noResultView.domNode)[0];

      searchRequest = dojo.clone(noResultView.jsonData.searchRequest);
      searchRequest.searchRequestType = "noResults";

      var links = dojo.query('.notice a', noResultView.domNode);
      _.each(links, function (link, i) {
    	  switch (i) {
    	  	case 0:
    	  		noResultView.tagElement(link, "airport-list-link");
    	  		break;
    	  	case 1:
    	  		noResultView.tagElement(link, "destination-list-link");
    	  		break;
    	  	case 2:
    	  		noResultView.tagElement(link, "when");
    	  		break;
    	    default:
    	    	break;
    	  }
      });

  	  // handle search panel publishers
      on(noResultView.domNode, on.selector(".notice a", "click"), function (event) {
    		  switch (event.target.className) {
              case "destination-list-link":
                dojo.publish("tui.searchPanel.view.cruise.DestinationGuide.openExpandable");
                break;
              case "airport-list-link":
                dojo.publish("tui.searchPanel.view.cruise.AirportGuide.openExpandable");
                break;
              case "change-date-link":
			  
			  var DatePicker=query(".custom-dropdown.departing a")[0];
				 on.emit(DatePicker, "click", {
				   bubbles: true,
				   cancelable: true
				 });
            }

      });

     // noResultView.stay ? widenRequestTypes.push("STAYDURATION") : null;
      _.each(widenRequestTypes, function (widenType) {
        noResultView.requestWideningCriteria(searchRequest, widenType);
      });

    },

    requestWideningCriteria: function (searchRequest, widenType) {
      var noResultView = this;
      ajaxQueue.send("widenCriteria", {
        url: noResultView.ajaxUrl,
        handleAs: "json",
        error: function (err) {
          noResultView.handleNoResults();
          if (dojoConfig.devDebug) {
            throw "Ajax Error: " + err;
          }
        },
        content: {
          searchCriteria: dojo.toJson(searchRequest),
          widenType: widenType
        },
        load: function (response, options) {
          noResultView.numRequests++;
          noResultView.response =response;
          noResultView.handleResults(response, options);
        },
        widenType: widenType,
        searchRequest: searchRequest
      }, "post");
    },

    handleResults: function (promise, options) {
      var noResultView = this;
      dojo.when(promise, function (response) {
        if (response.displayWidenCriteria) {
          noResultView.renderSearchOption(noResultView.parseData(response, options));
          } else if (noResultView.numRequests === noResultView.requestLimit && !noResultView.searchOptions.length) {
          noResultView.handleNoResults();
        }
      });
    },

    handleNoResults: function () {
      var noResultView = this;
      //dojo.addClass(noResultView.optionsNode, "no-results");
      dojo.removeClass(noResultView.optionsNode, "loading");
    },

    parseData: function (response, options) {
      var noResultView = this, from, to;

      switch (options.widenType) {
        case "WHERETO":
          from = (!response.searchRequest.from.length) ? "Any UK Airport" : response.searchRequest.from[0].name;
          to = response.displayName || 'PLACEHOLDER';
          break;
        case "WHEREFROM":
          from = response.displayName || 'PLACEHOLDER';
          to = (!response.searchRequest.to.length) ? "Anywhere" : response.searchRequest.to[0].name;
          break;
        case "WHEN":
          from = (!response.searchRequest.from.length) ? "Any UK Airport" : response.searchRequest.from[0].name;
          to = (!response.searchRequest.to.length) ? "Anywhere" : response.searchRequest.to[0].name;
          break;
        case "DURATION":
          from = (!response.searchRequest.from.length) ? "Any UK Airport" : response.searchRequest.from[0].name;
          to = (!response.searchRequest.to.length) ? "Anywhere" : response.searchRequest.to[0].name;
          break;
        case "STAYDURATION":
          from = (!response.searchRequest.from.length) ? "Any UK Airport" : response.searchRequest.from[0].name;
          to = (!response.searchRequest.to.length) ? "Anywhere" : response.searchRequest.to[0].name;
          break;
        default:
          break;
      }

      var response =  {
        widenType: options.widenType,
        url: noResultView.generateUrl(response.searchRequest),
        date: response.widenViewData.when,//_.formatDate(response.searchRequest.when, "dd-MM-yyyy", "EEEE dd MMMM yyyy"),
        flexible: response.searchRequest.flexibility,
        flexibleDays: response.searchRequest.flexibleDays,
        flexibleMonths: response.searchRequest.flexibleMonths,
        from: response.widenViewData.from.join(', '),
        to: response.widenViewData.to.join(', '),
        duration:response.widenViewData.duration,
		stay:response.widenViewData.widenStay
      }
      return response;
    },

    generateUrl: function (params) {
      var noResultView = this, queryParams, airports = [], units = [],
          paramsToSend = ["when", "until", "flexibility", "flexibleDays", "noOfAdults", "noOfSeniors",
            "noOfChildren", "duration", "first","flexibleMonths", "addAStay"];

      queryParams = _.pick(params, paramsToSend);

      _.each(params.from, function (airport) {
        airports.push(airport.id+ ":" + airport.type);
      });

      _.each(params.to, function (unit) {
        units.push(unit.id + ":" + unit.type);
      });

      queryParams = dojo.mixin(queryParams, {
        "from[]": airports.join("|"),
        "to[]": units.join("|"),
        "childrenAge": params.childrenAge.join(','),
        "searchRequestType": "ins"
      });

      return noResultView.linkUrl + ioQuery.objectToQuery(queryParams);
    },

    renderSearchOption: function (data) {
      var noResultView = this;
      dojo.removeClass(noResultView.optionsNode, "loading");
      var html = noResultView.renderTmpl(tmpl, data);
      var dom = dojo.place(html, noResultView.listNode, "last");
      noResultView.searchOptions.push(dom);
	  noResultView.tagButtons(query('.cta', dom)[0], data.widenType);
    },

    tagButtons: function (button, widenType) {
    	var noResultView = this, text;
    	switch(widenType) {
    	case "WHEN":
    		text = 'flexibleDepDate';
    		break;
    	case "WHERETO":
    		text = 'flexibleDestination';
    		break;
    	case "WHEREFROM":
    		text = 'flexibleFlight';
    		break;
    	}
    	noResultView.tagElement(button, text);
    }

  });

  return tui.searchResults.view.VariantNoResultView;
});