define("tui/showIGPackages/view/ShowIGPackagesComponent", [
  "dojo",
  "dojo/on",
  "dojo/date/locale",
  "dojo/mouse",
  "dojo/query",
  "dojo/dom-style",
  "dojo/dom-attr",
  "dojo/_base/lang",
  "dojo/dom-construct",
  "dojo/text!tui/showIGPackages/view/templates/showIGPackagesTmpl.html",
  "dojo/text!tui/searchResults/view/templates/pageLoaderTmpl.html",
  "dojo/text!tui/searchResults/view/templates/noResultsPopupTmpl.html",
  "dojo/text!tui/searchResults/view/templates/ajaxErrorPopupTmpl.html",
  "tui/searchResults/view/SearchResultsPaginator",
  "tui/mvc/Klass",
  "tui/searchResults/view/NoResultsPopup",
  "dojo/_base/connect",
  "dojo/dom-class",
  'dojo/_base/array',
  "tui/config/TuiConfig",
  "dojox/dtl",
  "dojox/dtl/Context",
  "tui/widget/mixins/Templatable",
  "tui/widget/_TuiBaseWidget",
  "tui/search/nls/Searchi18nable"], function (dojo, on, dateLocale, mouse, query, domStyle, domAttr, lang, domConstruct, showIGPackagesTmpl,loaderTmpl,noResultsTmpl,ajaxErrorTmpl,SearchResultsPaginator, Klass, Modal, connect, domClass, arrayUtil, TuiConfig) {
  /*
   * TODO:
   * Pagination, initMessaging, event handling and re-work on logic
   * */
  function increment (x) {
    return x + 1;
  }

  function getHref (target) {
    return (target.tagName.toLowerCase() === "img") ? query(target).parents(".url")[0].href : target.href;
  }
  dojo.declare("tui.showIGPackages.view.ShowIGPackagesComponent",[tui.widget._TuiBaseWidget,
    tui.widget.mixins.Templatable, tui.search.nls.Searchi18nable], {
    //properties
    dataPath: 'merchandiserResult',
    holidays: null,
    paginator: null,
    pageLoaderNode: null,
    noResultsPopup: null,
    ajaxErrorPopup: null,
    totalHolidaysAvailable: 0,
    tmpl: showIGPackagesTmpl,
    currentPage: 1,
    //pageLabel:null,
    componentId: null,
    analyticsData:null,
    tuiConfig: TuiConfig,
   
    postCreate: function () {
      var showIGPackagesComponent = this;
      var mediator = dijit.registry.byId('mediator');
      // showIGPackagesComponent.pageLabel = mediator.model.merchandiserRequest.pageLabel;
      var model = mediator.registerController(showIGPackagesComponent, this.dataPath);
      showIGPackagesComponent.holidays = model.holidays;     
      showIGPackagesComponent.totalHolidaysAvailable = model.endecaResultsCount;
      showIGPackagesComponent.initSearchMessaging();
      // initialise pagination
      showIGPackagesComponent.paginator = new SearchResultsPaginator({area: 240, attachOnLoad: false}, null);

      dojo.connect(showIGPackagesComponent.paginator, "onScroll", function (scrollPos, scrollListener) {
        // detach listener > prevent from re-firing
        showIGPackagesComponent.paginator.detach();
        if (showIGPackagesComponent.hasMoreResults()) {
          mediator.fire('paginate', showIGPackagesComponent.currentPage, increment(showIGPackagesComponent.currentPage));
          showIGPackagesComponent.toggleLoaderNode(true);
        }
      });

      connect.subscribe("tui/searchResults/view/SearchResultsComponent/showNoResultsPopup", function () {
        showIGPackagesComponent.renderNoResultsPopup();
      });

      // add event delegation methods (buttons)
      showIGPackagesComponent.delegateEvents();

      showIGPackagesComponent.inherited(arguments);
      // cache analytics values
      showIGPackagesComponent.analyticsData = {
        tag: showIGPackagesComponent.tag,
        number: showIGPackagesComponent.number,
        analyticsText: {
          url: "moreDetails",
          viewButton: "View Details"
        }
      };
      showIGPackagesComponent.renderSearchResults();
    },

    // ----------------------------------------------------------------------------- result item button methods
    renderSearchResults: function () {
      var showIGPackagesComponent = this;
      var holidays = showIGPackagesComponent.holidays;
      if (!holidays || !holidays.length) return;

      var newHolidays = [], html;

      _.forEach(holidays, function (item, i) {
        var holiday = dojo.clone(item);
        holiday.componentId = showIGPackagesComponent.componentId;
        holiday.itinerary = showIGPackagesComponent.formatItineraryDates(holiday.itinerary);
        holiday.accommodation.imageUrl = showIGPackagesComponent.addImagePlaceholders(holiday.accommodation.imageUrl);
        if (holiday.accommodation.ratings.officialRating) {
          holiday.accommodation.ratings.tripAdvisorRatingKlass = showIGPackagesComponent.addTripAdvisorRatingKlass(holiday.accommodation.ratings.officialRating);
        }
        holiday.finPos = i + 1;
        holiday.accommodation.differentiatedModifiedCode = holiday.accommodation.differentiatedCode;
        if(dojoConfig.dualBrandSwitch  && showIGPackagesComponent.tuiConfig[dojoConfig.site] && holiday.accommodation.differentiatedCode) {
            var modifiedCode = showIGPackagesComponent.tuiConfig[dojoConfig.site].dualBrandConfig.differentiatedCodeLarge[holiday.accommodation.differentiatedCode.toLowerCase().replace(/\s/g,"")];
            if(modifiedCode){
          	  holiday.accommodation.differentiatedModifiedCode = modifiedCode;
            }
        }
        newHolidays.push(holiday);
      });
      // render template, include holidays and i8n texts
      html = showIGPackagesComponent.renderTmpl(null, lang.mixin(showIGPackagesComponent.analyticsData,{
        holidays: newHolidays,
        searchResults: showIGPackagesComponent.searchMessaging.searchResults,
        currency : dojoConfig.currency
      }));
      showIGPackagesComponent.onBeforePlaceHolidays();
      if (html) {
        dojo.place(html, showIGPackagesComponent.domNode, "last");
        showIGPackagesComponent.destroyResultWidgets(true);
        dojo.parser.parse(showIGPackagesComponent.domNode);
      }
      showIGPackagesComponent.onAfterPlaceHolidays();
    },
    renderMoreHolidays: function (holidays) {
      // summary:
      //		Renders given search results
      var showIGPackagesComponent = this;
      if (!holidays || !holidays.length) return;
      var newHolidays = [], html;

      _.forEach(holidays, function (item, i) {
        var holiday = dojo.clone(item);
        holiday.componentId = showIGPackagesComponent.componentId;
        holiday.itinerary = showIGPackagesComponent.formatItineraryDates(holiday.itinerary);
        holiday.accommodation.imageUrl = showIGPackagesComponent.addImagePlaceholders(holiday.accommodation.imageUrl);
        if (holiday.accommodation.ratings.officialRating) {
          holiday.accommodation.ratings.tripAdvisorRatingKlass = showIGPackagesComponent.addTripAdvisorRatingKlass(holiday.accommodation.ratings.officialRating);
        }
        holiday.finPos = i + 1;
        holiday.accommodation.differentiatedModifiedCode = holiday.accommodation.differentiatedCode;
        if(dojoConfig.dualBrandSwitch  && showIGPackagesComponent.tuiConfig[dojoConfig.site] && holiday.accommodation.differentiatedCode) {
            var modifiedCode = showIGPackagesComponent.tuiConfig[dojoConfig.site].dualBrandConfig.differentiatedCodeLarge[holiday.accommodation.differentiatedCode.toLowerCase().replace(/\s/g,"")];
            if(modifiedCode){
          	  holiday.accommodation.differentiatedModifiedCode = modifiedCode;
            }
        }
        newHolidays.push(holiday);
      });

      // render template, include holidays and i8n texts
      html = showIGPackagesComponent.renderTmpl(null, lang.mixin(showIGPackagesComponent.analyticsData,{
        holidays: newHolidays,
        searchResults: showIGPackagesComponent.searchMessaging.searchResults
      }));
      if (html) {
        dojo.place(html, showIGPackagesComponent.domNode, "only");
        showIGPackagesComponent.destroyResultWidgets(true);
        dojo.parser.parse(showIGPackagesComponent.domNode);
      }

      showIGPackagesComponent.onAfterPlaceHolidays();
    },
    onBeforePlaceHolidays: function () {
      var showIGPackagesComponent = this;
      // destroy all child widgets
      showIGPackagesComponent.destroyResultWidgets();
      // empty container
      dojo.empty(showIGPackagesComponent.domNode);
    },
    onAfterPlaceHolidays: function () {
      // summary:
      //		Runs after holidays have been rendered, after they are placed in the dom
      var showIGPackagesComponent = this;
      // re-attach pagination listener
      showIGPackagesComponent.paginator.attach();
      showIGPackagesComponent.toggleLoaderNode(false);
    },
    hasMoreResults: function () {
      return this.totalHolidaysAvailable > this.holidays.length
    },

    //pagination methods
    toggleLoaderNode: function (action) {
      // summary:
      //		Adds page loading indicator to search results dom node
      var showIGPackagesComponent= this, html;
      action = action ? "removeClass" : "addClass";
      if(!showIGPackagesComponent.pageLoaderNode) {
        html = showIGPackagesComponent.renderTmpl(loaderTmpl, {});
        showIGPackagesComponent.pageLoaderNode = dojo.place(html, dojo.query('.result-view .product-list .viewport')[0], "last");
      }
      dojo[action](showIGPackagesComponent.pageLoaderNode, "hide");
    },

    renderAjaxErrorPopup: function () {
      var showIGPackagesComponent = this;
      if (!showIGPackagesComponent.ajaxErrorPopup) {
        dojo.place(showIGPackagesComponent.renderTmpl(ajaxErrorTmpl, showIGPackagesComponent.searchMessaging.ajaxError), document.body, "last");
        showIGPackagesComponent.ajaxErrorPopup = new Modal({
          widgetId: 'ajaxError',
          stopDefaultOnCancel: false,
          reloadOnClose: true
        });
      }
      showIGPackagesComponent.ajaxErrorPopup.open();
    },
    renderNoResultsPopup: function () {
      var showIGPackagesComponent = this;
      if (!showIGPackagesComponent.noResultsPopup) {
        dojo.place(showIGPackagesComponent.renderTmpl(noResultsTmpl, showIGPackagesComponent.searchMessaging.filterNoResults), document.body, "last");
        showIGPackagesComponent.noResultsPopup = new Modal();
      }
      showIGPackagesComponent.noResultsPopup.open();
    },
    delegateEvents: function () {
      // summary:
      //		delegate result item button events
      var showIGPackagesComponent = this;

      // add finPos and analytics data to img, h4 and cta urls
      on(showIGPackagesComponent.domNode, on.selector(".url", "click"), function (event) {
        // Save current page state to SessionStorage
        connect.publish('tui/showIGPackages/Mediator/saveState', []);
      });

    },

    getFinPos: function (id) {
      //		Determine final position of package in results list (index + 1)
      var showIGPackagesComponent = this;
      var finPos = null;

      query(".search-result-item", showIGPackagesComponent.domNode).forEach(function (item, i) {
        if (domAttr.get(item, "data-package-id") === id) {
          finPos = i + 1;
        }
      });

      return finPos ? finPos : 0;
    },
    destroyResultWidgets: function (preserveDom) {
      var showIGPackagesComponent = this;
      // destroy all child widgets
      _.forEach(dijit.findWidgets(showIGPackagesComponent.domNode), function(w) {
        w.destroyRecursive(preserveDom);
      });
    },

    formatItineraryDates: function (itinerary) {
      // summary:
      //		loops over itinerary and re-formats date strings
      var showIGPackagesComponent = this;

      // format overall departure date
      // format overall departure date
      if(itinerary.departureDate!= null){
        itinerary.departureDate = _.formatDate(itinerary.departureDate);
      }

      // format outbound dates
      if (itinerary.outbounds.length === 0) {
        return itinerary;
      }
      var newOutBound = [];
      var newInBound = [];
      _.forEach(itinerary.outbounds, function (outbound) {
        outbound.schedule.departureDate = _.formatDate(outbound.schedule.departureDate);
        outbound.schedule.departureTime = _.formatTime(outbound.schedule.departureTime);
        outbound.schedule.arrivalDate = _.formatDate(outbound.schedule.arrivalDate);
        outbound.schedule.arrivalTime = _.formatTime(outbound.schedule.arrivalTime);
        newOutBound.push(outbound);
      });
      itinerary.outbounds = newOutBound;

      _.forEach(itinerary.inbounds, function (inbound) {
        inbound.schedule.departureDate = _.formatDate(inbound.schedule.departureDate);
        inbound.schedule.departureTime = _.formatTime(inbound.schedule.departureTime);
        inbound.schedule.arrivalDate = _.formatDate(inbound.schedule.arrivalDate);
        inbound.schedule.arrivalTime = _.formatTime(inbound.schedule.arrivalTime);
        newInBound.push(inbound);
      });
      itinerary.inbounds = newInBound;

      return itinerary;
    },
    addTripAdvisorRatingKlass: function (rating) {
      return "rating" + rating * 10;
    },

    addImagePlaceholders: function (imageUrl) {
      if (imageUrl) { return imageUrl; }
      return "../images/thomson/default-large.png";
    },

    generateRequest: function (field) {
      var widget = this;
      //todo: convert to page numbers
      if (field === 'paginate') {
        return {'first': increment(widget.currentPage), 'searchRequestType' : 'paginate'};//, 'pageLabel':widget.pageLabel
      }
      return {'first': 1};
    },

    refresh: function (field, oldValue, newValue, response) {
      var showIGPackagesComponent = this, oldLength = showIGPackagesComponent.holidays.length;
      switch (field) {
        case 'paginate' :
          response.holidays = _.map(response.holidays, function(holiday){
            holiday.finPos = ++oldLength;
            return holiday;
          });
          showIGPackagesComponent.holidays = showIGPackagesComponent.holidays.concat(response.holidays || []);
          showIGPackagesComponent.currentPage = newValue;
          showIGPackagesComponent.renderMoreHolidays(showIGPackagesComponent.holidays);
          break;
        default :
          showIGPackagesComponent.currentPage = 1;
          showIGPackagesComponent.totalHolidaysAvailable = response.endecaResultsCount;
          showIGPackagesComponent.holidays = response.holidays;
          showIGPackagesComponent.renderSearchResults();
          break;
      }
    },

    clear: function () {},

    handleNoResults: function (name) {
      var showIGPackagesComponent = this;
      if (name && (name !== 'duration' && name !== 'rooms')) {
        showIGPackagesComponent.renderNoResultsPopup();
      }
    }
  });
  return tui.showIGPackages.view.ShowIGPackagesComponent;
});
