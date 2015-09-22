define("tui/searchBResults/view/VariantSearchResultsComponent", [
  "dojo/_base/declare",
  "dojo/_base/connect",
  "dojo/_base/lang",
  "dojo/parser",
  "dojo/aspect",
  "dojo/topic",
  "dojo/on",
  "dojo/mouse",
  "dojo/query",
  "dojo/dom-style",
  "dojo/dom-attr",
  "dojo/dom-construct",
  "dojo/dom-class",
  "dojo/dom-geometry",
  "dojo/text!tui/searchBResults/view/templates/variantResultItemTmpl.html",
  "dojo/text!tui/searchBResults/view/templates/noResultsPopupTmpl.html",
  "dojo/text!tui/searchBResults/view/templates/ajaxErrorPopupTmpl.html",
  "dojo/text!tui/searchBResults/view/templates/pageLoaderTmpl.html",
  "tui/searchBResults/view/SearchResultsPaginator",
  "tui/searchBResults/view/NoResultsPopup",
  "tui/searchBResults/service/RoomGroupingService",
  "tui/searchBResults/view/variants/VariantViewMapping",
  "tui/searchBResults/view/SearchResultsComponent",
  "tui/widget/mixins/Templatable",
  "tui/widget/_TuiBaseWidget",
  "tui/search/nls/Searchi18nable"], function (declare, connect, lang, parser, aspect, topic, on, mouse, query,
                                              domStyle, domAttr, domConstruct, domClass, domGeom, resultTmpl, noResultsTmpl,
                                              ajaxErrorTmpl, loaderTmpl, Paginator, Modal, roomGrpSrvc, variantMappings) {

  // ----------------------------------------------------------------------------- utility methods


  function brandTypeMap(brandType) {
    var map = {
      'F': 'First Choice',
      'SKY': 'SKYTOURS'
    };
    // map brandtype codes to names
    return (brandType !== 'T') ? map[brandType] : false;
  }

  function createItineraries(itineraries) {
      var itinerary = {
          name:'',
          left: [],
          right: [],
          image:''
      };
      // Itinerary Name
      itinerary.name = itineraries[0].name;

      //Itinerary Location list
      visitingList(itineraries[0].itineraryLegs, itinerary);

      //Itinerary Image
      addImage(itineraries, itinerary);
      return itinerary;
  }

  function createMultipleItineraries(itineraries) {
      var itinerary = {
          name:[],
          left: [],
          right: [],
          image:''
      };

      // Itinerary Name
      itinerary.name = _.pluck(itineraries, 'name');

      //Itinerary Location list
      visitingList(_.flatten(_.pluck(itineraries, 'itineraryLegs')), itinerary);

      //Itinerary Image
      addImage(itineraries, itinerary);
      return itinerary;
  }

  function addImage(itineraries, itinerary){
      itinerary.image = _.isEmpty(itineraries[0].itineraryImageUrl) ? imgPlaceholders(itineraries[0].itineraryImageUrl) : itineraries[0].itineraryImageUrl;
  }

  function visitingList(itineraryLegs, itinerary){
      var total = _.size(itineraryLegs)/2;
      _.each(itineraryLegs, function (leg, index) {
          if(index > total)
              itinerary.right.push(leg);
          else
              itinerary.left.push(leg);
      });
  }

  function addStay(holiday) {
     var itineraries = holiday.itinenaries;
     var stay = {
         image: '',
         description: '',
         name: ''
     };
     stay.image = _.isEmpty(itineraries[0].addToStayImageUrl) ? imgPlaceholders(itineraries[0].addToStayImageUrl) : itineraries[0].addToStayImageUrl;
     stay.description = itineraries[0].addToStayDescription;
     stay.name =  itineraries[0].addToStayName;

     return stay;
  }

  function taRatingClass(rating) {
    // Add TripAdvisor rating class based on passed rating
    return "rating" + rating * 10;
  }

  function tuiRatingClass(rating) {
    if(rating && rating.match(/\//)){
      return false;
    }
    return rating % 1 === 0.5 ? rating - (rating % 1) + 'plus' : rating;
  }

  function imgPlaceholders(imageUrl) {
    // Replaces imageUrl with placeholder image if empty
    return dojoConfig.paths.webRoot + "/images/" + dojoConfig.site + "/default-large.png";
  }

  function formatDates(itinerary) {
    // Loop over itinerary and re-format date strings
    // Format overall departure date
    itinerary.departureDate = _.formatDate(itinerary.departureDate);

    // format outbound dates
    if (!itinerary.outbounds.length || !itinerary.inbounds.length) {
      return itinerary;
    }

    itinerary.outbounds = _.map(itinerary.outbounds, function (outbound) {
      outbound.schedule.departureDate = _.formatDate(outbound.schedule.departureDate);
      outbound.schedule.departureTime = _.formatTime(outbound.schedule.departureTime);
      outbound.schedule.arrivalDate = _.formatDate(outbound.schedule.arrivalDate);
      outbound.schedule.arrivalTime = _.formatTime(outbound.schedule.arrivalTime);
      return outbound;
    });

    // format inbound dates
    itinerary.inbounds = _.map(itinerary.inbounds, function (inbound) {
      inbound.schedule.departureDate = _.formatDate(inbound.schedule.departureDate);
      inbound.schedule.departureTime = _.formatTime(inbound.schedule.departureTime);
      inbound.schedule.arrivalDate = _.formatDate(inbound.schedule.arrivalDate);
      inbound.schedule.arrivalTime = _.formatTime(inbound.schedule.arrivalTime);
      return inbound;
    });

    return itinerary;
  }

  function priceDiffFormat(boardTypes) {
    var symbol, template = '{symbol}{currency}{value}';
    return _.map(boardTypes, function (boardType) {
      if (boardType.priceDiffrence) {
        symbol = boardType.priceDiffrence.charAt(0);
        boardType.priceDiffrence = lang.replace(template, {
          'symbol': symbol,
          'currency': '&pound;',
          'value': boardType.priceDiffrence.replace(symbol, '')
        });
      }
      return boardType;
    });
  }


  return declare("tui.searchBResults.view.VariantSearchResultsComponent", [tui.searchBResults.view.SearchResultsComponent], {

    // ----------------------------------------------------------------------------- properties

    holidays: null,

    holidayNodes: null,

    colNodes: null,

    totalHolidaysAvailable: 0,

    tmpl: resultTmpl,

    paginator: null,

    pageLoaderNode: null,

    noResultsPopup: null,

    ajaxErrorPopup: null,

    currentPage: 1,

    shortlistDisabled: false,

    componentId: null,

    subscribableMethods: ["renderAjaxErrorPopup", "renderNoResultsPopup", "setView", "updatePage"],

    viewMode: null,

    defaultGalleryHeight: 345,

    galleryOpenTimer: null,

    analyticsData: null,

    // ----------------------------------------------------------------------------- holiday rendering methods

      renderSearchResults: function (packages, isPagination) {
          // Render given search results

          if (!packages || !packages.length) {
              _.debug("Error! Holiday package data is missing");
          }

          var resultsView = this, html;
          var holidays = _.map(lang.clone(packages), function (holiday, index) {
              holiday.componentId = resultsView.componentId;
              holiday.variant = variantMappings(holiday.searchVariant);

              if(holiday.searchVariant === 'PORT')
              {
                  holiday.image = _.isEmpty(holiday.portDetails.galleryImages) ? imgPlaceholders('') : holiday.portDetails.galleryImages[0].mainSrc;
                  holiday.portDetails.portCountry =  _.isEmpty(holiday.portDetails.portCountry) ? '': holiday.portDetails.portCountry;
                  holiday.portDetails.portDesc = _.isEmpty(holiday.portDetails.portDesc) ? '': holiday.portDetails.portDesc;
              }
              else
              {
                  holiday.itineraries = holiday.searchVariant === 'BACK_BACK' ? createMultipleItineraries(holiday.itinenaries) : createItineraries(holiday.itinenaries);
                  holiday.stayApplicable = holiday.searchVariant.indexOf('STAY')>-1;
                  if(holiday.stayApplicable)
                      holiday.stay = addStay(holiday);

                  //TODO: Correct Dates used in templates
                  //holiday.itinerary = formatDates(holiday.itinerary);
                  //DE13065 fix
                  if( holiday.accommodation.featureCodesAndValues.strapline && holiday.accommodation.featureCodesAndValues.strapline.length)
                  {
                      holiday.accommodation.featureCodesAndValues.strapline[0] = holiday.accommodation.featureCodesAndValues.strapline[0].replace(/\u0027ll/g, "\\'");
                  }
                  !holiday.accommodation.imageUrl ? holiday.accommodation.imageUrl = imgPlaceholders(holiday.accommodation.imageUrl) : null;
                  if (holiday.accommodation.ratings.tripAdvisorRating) {
                      holiday.accommodation.ratings.tripAdvisorRatingKlass = resultsView.taRatingClass(holiday.accommodation.ratings.tripAdvisorRating);
                  }
                  //TODO: Room Groupings if applicable
                  //holiday.accommodation.rooms = roomGrpSrvc.groupRooms(holiday.accommodation.rooms, "roomType");

                  // map holiday brandType
                  holiday.brandType = brandTypeMap(holiday.brandType);

                  // map board-type price-diffs
                  holiday.alternateBoard = priceDiffFormat(holiday.alternateBoard);

                  // due to django limitations have to check deposits here
                  //TODO: Deposit coding
                  //holiday.lowDepositExists = holiday.price.lowDepositExists || holiday.price.depositExists;

                  if (!isPagination) {
                      holiday.finPos = index + 1;
                      holiday.uiPos = index + 1;
                  }

                  holiday.accommodation.ratings.officialRating = tuiRatingClass(holiday.accommodation.ratings.officialRating);

              }
              // todo: remove after data has been fixed
              return holiday;
          });
           resultsView.onBeforePlaceHolidays(isPagination);
          // todo: refactor
          if (resultsView.getView() === 'list') {
              // render template, include holidays and i8n texts
              html = resultsView.renderTmpl(null, lang.mixin(resultsView.analyticsData, {
                  holidays: holidays,
                  messages: resultsView.searchMessaging.searchResults
              }));
              domConstruct.place(html, resultsView.colNodes.listCol, "last");
          } else {
              resultsView.constructGalleria(holidays);
          }

          isPagination ? resultsView.destroyResultWidgets(true) : null;
          parser.parse(resultsView.domNode);
          resultsView.onAfterPlaceHolidays(isPagination);

      },

      // ----------------------------------------------------------------------------- mediator methods
      generateRequest: function (field) {
          var widget = this;
          //todo: convert to page numbers
          if (field === 'paginate') {
              return {
                  'page': _.inc(widget.currentPage),
                  //'searchRequestType': 'paginate'
              };
          }
          return {
              'page': 1
          };
      },
      constructGalleria: function(holidays){
          var resultsView = this;
          // sort holidays and then render each column
          var htmlOdd, htmlEven, sortedHolidays = {
              oddCol: [],
              evenCol: []
          };
          _.each(holidays, function (holiday) {
              if (parseInt(holiday.uiPos, 10) % 2 !== 0) {
                  sortedHolidays.oddCol.push(holiday);
              } else {
                  sortedHolidays.evenCol.push(holiday);
              }
          });
          htmlOdd = resultsView.renderTmpl(null, lang.mixin(resultsView.analyticsData, {
              holidays: sortedHolidays.oddCol,
              messages: resultsView.searchMessaging.searchResults
          }));
          htmlEven = resultsView.renderTmpl(null, lang.mixin(resultsView.analyticsData, {
              holidays: sortedHolidays.evenCol,
              messages: resultsView.searchMessaging.searchResults
          }));
          domConstruct.place(htmlOdd, resultsView.colNodes.oddCol, "last");
          domConstruct.place(htmlEven, resultsView.colNodes.evenCol, "last");
      },

      observeShortlist: function () {}

      });
});