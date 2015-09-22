define("tui/searchResults/view/NoResultView", [
  "dojo",
  "dojo/on",
  "dojo/query",
  "tui/utils/AjaxQueue",
  "dojo/text!tui/searchResults/view/templates/noResultsAltSearchTmpl.html",
  "dojo/io-query",
  "tui/widget/mixins/Templatable",
  "tui/widget/_TuiBaseWidget"], function (dojo, on, query, ajaxQueue, tmpl, ioQuery, tagMappingTable) {

  var widenRequestTypes = ["WHEN", "WHERETO", "WHEREFROM"];

  dojo.declare("tui.searchResults.view.NoResultView", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable], {

    // ---------------------------------------------------------------- properties

    ajaxUrl: dojoConfig.paths.webRoot + "/widenCriteria",

    linkUrl: dojoConfig.paths.webRoot + "/packages?",

    optionsNode: null,

    listNode: null,

    numRequests: 0,

    requestLimit: 3,

    searchOptions: null,

    // ---------------------------------------------------------------- methods

    postCreate: function () {
      var noResultView = this, text, searchRequest = null;
      noResultView.inherited(arguments);
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
    	  if(noResultView.abTestCode == "destinationGuideB"){
    		  switch (event.target.className) {
              case "destination-list-link":
                dojo.publish("tui.searchBPanel.view.DestinationGuide.openExpandable");
                break;
              case "airport-list-link":
                dojo.publish("tui.searchBPanel.view.AirportGuide.openExpandable");
                break;
              case "change-date-link":
                dojo.publish("tui.searchBPanel.view.SearchDatePicker.focusCalendar");
                break;
            }
    	  }
    	  else {
    		  switch (event.target.className) {
              case "destination-list-link":
                dojo.publish("tui.searchPanel.view.DestinationGuide.openExpandable");
                break;
              case "airport-list-link":
                dojo.publish("tui.searchPanel.view.AirportGuide.openExpandable");
                break;
              case "change-date-link":
                dojo.publish("tui.searchPanel.view.SearchDatePicker.focusCalendar");
                break;
            }
    	  }

      });

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
      dojo.addClass(noResultView.optionsNode, "no-results");
      dojo.removeClass(noResultView.optionsNode, "loading");
    },

    parseData: function (response, options) {
      var noResultView = this, from, to;
      var anyAirportText = (dojoConfig.site === "falcon") ? "Any ROI Airport" : "Any UK Airport"
      switch (options.widenType) {
        case "WHERETO":
          from = (!response.searchRequest.airports.length) ? anyAirportText : response.searchRequest.airports[0].name;
          to = response.displayName || 'PLACEHOLDER';
          break;
        case "WHEREFROM":
          from = response.displayName || 'PLACEHOLDER';
          to = (!response.searchRequest.units.length) ? "Anywhere" : response.searchRequest.units[0].name;
          break;
        case "WHEN":
          from = (!response.searchRequest.airports.length) ? anyAirportText : response.searchRequest.airports[0].name;
          to = (!response.searchRequest.units.length) ? "Anywhere" : response.searchRequest.units[0].name;
          break;
      }

      return {
        widenType: options.widenType,
        url: noResultView.generateUrl(response.searchRequest),
        date: _.formatDate(response.searchRequest.departureDate, "dd-MM-yyyy", "EEEE dd MMMM yyyy"),
        flexible: response.searchRequest.flexibility,
        flexibleDays: response.searchRequest.flexibleDays,
        from: from,
        to: to
      }
    },

    generateUrl: function (params) {
      var noResultView = this, queryParams, airports = [], units = [],
          paramsToSend = ["when", "until", "flexibility", "flexibleDays", "noOfAdults", "noOfSeniors",
            "noOfChildren", "duration", "first"];

      queryParams = _.pick(params, paramsToSend);

      _.each(params.airports, function (airport) {
        airports.push(airport.id);
      });

      var multiSelect = true;
      _.each(params.units, function (unit) {
        units.push(unit.id + ":" + unit.type);
        multiSelect = unit.multiSelect;
      });

      queryParams = dojo.mixin(queryParams, {
        "airports[]": airports.join("|"),
        "units[]": units.join("|"),
        "childrenAge": params.childrenAge.join(','),
        "searchRequestType": "ins",
        "multiSelect": multiSelect
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

  return tui.searchResults.view.NoResultView;
});