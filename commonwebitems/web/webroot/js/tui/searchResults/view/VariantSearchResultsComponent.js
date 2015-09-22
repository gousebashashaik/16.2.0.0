define("tui/searchResults/view/VariantSearchResultsComponent", [
  "dojo/_base/declare",
  "dojo/_base/connect",
  "dojo/_base/lang",
  "dojo/parser",
  "dojo/aspect",
  "dojo/topic",
  "dojo/on",
  "dojo/_base/xhr",
  "dojo/mouse",
  "dojo/query",
  "dojo/dom-style",
  "dojo/dom-attr",
  "dojo/dom-construct",
  "dojo/dom-class",
  "dojo/dom-geometry",
  "dojo/text!tui/searchResults/view/templates/variantResultItemTmpl.html",
  "dojo/text!tui/searchResults/view/templates/noResultsPopupTmpl.html",
  "dojo/text!tui/searchResults/view/templates/ajaxErrorPopupTmpl.html",
  "dojo/text!tui/searchResults/view/templates/pageLoaderTmpl.html",
  "tui/searchResults/view/SearchResultsPaginator",
  "tui/searchResults/view/NoResultsPopup",
  "tui/searchResults/service/RoomGroupingService",
  "tui/searchResults/view/variants/VariantViewMapping",
  "tui/widget/popup/cruise/FacilityOverlay",
  "tui/searchResults/view/SearchResultsComponent",
  "tui/widget/mixins/Templatable",
  "tui/widget/_TuiBaseWidget",
  "tui/search/nls/Searchi18nable"], function (declare, connect, lang, parser, aspect, topic, on, xhr, mouse, query,
                                              domStyle, domAttr, domConstruct, domClass, domGeom, resultTmpl, noResultsTmpl,
                                              ajaxErrorTmpl, loaderTmpl, Paginator, Modal, roomGrpSrvc, variantMappings, FacilityOverlay) {

  // ----------------------------------------------------------------------------- utility methods


  function brandTypeMap(brandType) {
    var map = {
      'F': 'First Choice',
      'SKY': 'SKYTOURS'
    };
    // map brandtype codes to names
    return (brandType !== 'T') ? map[brandType] : false;
  }
  function createBoardBasis(accommodation){
     return {name: _.first(accommodation.rooms).boardType, available:true, code: _.first(accommodation.rooms).boardBasisCode};
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


  return declare("tui.searchResults.view.VariantSearchResultsComponent", [tui.searchResults.view.SearchResultsComponent], {

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
    disconnHandlr: null,

    // ----------------------------------------------------------------------------- holiday rendering methods
    postCreate: function(){
    	var resultsView = this;
    	dojo.destroy(query(".cruise-search-results-loader")[0]);
    	resultsView.inherited(arguments);
    },

	invokeExcursioinOverlay: function () {
			var resultsView = this, xhrReq,index;

			var excursionUrl = dojoConfig.paths.webRoot + "/excursions";
			dojo.disconnect(resultsView.disconnHandlr);
			//query(resultsView.domNode).on("li.excOverlay:click", function(event){
			resultsView.disconnHandlr = dojo.connect(resultsView.domNode, "onclick", query("li.excOverlay"), function(event){
					excNode = query(event.target).parent()[0];
					hIndex = domAttr.get(excNode,'data-index');
					pCode = domAttr.get(excNode,'data-port-code');
					exCode = domAttr.get(excNode,'data-excursion-code');
					xhrReq = xhr.get({
					url: excursionUrl+"?locCode="+pCode+"&stay=false&excCode="+exCode, //?locCode=003437&excCode=986360",
					 // url: excursionUrl+"?locCode="+mapData.locationCode+"?stay="+mapData.addToStay,
					handleAs: "json",
					load: function (response) {
						var portName = (pocItemLi = query(excNode).closest(".poc-list")[0] )? query("h2.title a", pocItemLi)[0].innerHTML : "";  //holidays[hIndex-1].portDetails.portName;
						resultsView.handleResults(response, excNode, portName);
					 },
					  error: function (err) {
					   _.debug(err);
						console.log("AJAX Error message: "+error);
					  }
					});
			});

		},

    handleResults:function (response, excNode, portName){
			var resultsView = this,
			    excOverlay = new FacilityOverlay({
					componentName: "excursionOverlay",
					portName: portName,
					jsonData: response.excursion[0]
				},excNode);
			excOverlay.open();
		},

    renderSearchResults: function (packages, isPagination) {
          // Render given search results

          if (!packages || !packages.length) {
              _.debug("Error! Holiday package data is missing");
          }
         // _.each(resultsView, function (resultsView) {

          var resultsView = this, html;
          var monthMap = [];
          var holidays = _.map(lang.clone(packages), function (holiday, index) {
              holiday.componentId = resultsView.componentId;

            //variantType added from 'CruiseCalendarSearchResultsComponent.js'
              holiday.variant = !_.isNull(variantMappings(holiday.variantType)) ? variantMappings(holiday.variantType) : _.isNull(variantMappings(holiday.variantNInv)) ? variantMappings(holiday.searchVariant) : variantMappings(holiday.variantNInv);


              if(holiday.searchVariant === 'PORT')
              {
                  holiday.image = _.isEmpty(holiday.portDetails.galleryImages) ? imgPlaceholders('') : holiday.portDetails.galleryImages[0].mainSrc;
                  holiday.portDetails.portCountry =  _.isEmpty(holiday.portDetails.portCountry) ? '': holiday.portDetails.portCountry;
                  holiday.portDetails.portDesc = _.isEmpty(holiday.portDetails.portDesc) ? '': JSON.stringify(holiday.portDetails.portDesc).substring(1,104);

              }
              else
              {
                  if(holiday.variantType === 'CRUISE_BROWSE_CALENDAR'){
                      var date = dojo.date.locale.parse(holiday.sailings[0].durationInfo.departureDate, {datePattern: "dd MMM yyyy", selector: 'date'})
                      var monthYear = dojo.date.locale.format(date,{selector: "date", datePattern: "MMMM yyyy" });
                      if(!_.contains(monthMap, monthYear)){
                          monthMap.push(monthYear);
                          holiday.title = monthYear;
                          holiday.hashTarget = dojo.trim(dojo.date.locale.format(date,{selector: "date", datePattern: "MMM yyyy" })).replace(' ', '-');
                          holiday.titleExist = true;
                      }
                      else
                      {
                          holiday.title = '';
                          holiday.titleExist = false;
                      }
                  }else if(holiday.variantNInv === 'CRUISE_BROWSE_ITINERARY' || holiday.variantNInv === 'CRUISE_BROWSE_ITINERARY_TRACS' || holiday.variantNInv === 'CRUISE_BROWSE_ITINERARY_ATCOM') {
                      holiday.shipName = holiday.sailings[0].shipName.name;
                      holiday.dateRange = _.size(holiday.dateRange) > 1 ? holiday.dateRange[0] + ' - ' + holiday.dateRange[1] : holiday.dateRange[0];
                      holiday.durationRange = _.size(holiday.durationRange) > 1 ? holiday.durationRange[0] + ' -' + holiday.durationRange[1] : holiday.durationRange[0];
                  }
                  holiday.itineraries = holiday.searchVariant === 'BACK_BACK' ? createMultipleItineraries(holiday.itinenaries) : createItineraries(holiday.itinenaries);
                  holiday.boardBasis = ((holiday.searchVariant === 'CRUISE_STAY_HOTEL') || (holiday.searchVariant === 'STAY_CRUISE_HOTEL')) ? createBoardBasis(holiday.accommodation) : {available:false};

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
                      holiday.accommodation.ratings.tripAdvisorRatingKlass = taRatingClass(holiday.accommodation.ratings.tripAdvisorRating);
                  }
                  //TODO: Room Groupings if applicable
                  if((holiday.searchVariant === 'CRUISE_STAY_HOTEL') || (holiday.searchVariant === 'STAY_CRUISE_HOTEL')){
                      holiday.accommodation.rooms = roomGrpSrvc.groupRooms(holiday.accommodation.rooms, "roomType");
                  }else{
                	  holiday.sailings[0].cabins = roomGrpSrvc.groupCabins(holiday.sailings[0].cabins, "roomType");
                  }

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
              holiday = _.isEmpty(holiday.sailings) ? holiday : resultsView.updateTooltip(holiday);
              // todo: remove after data has been fixed
              return holiday;
          });
          if( !holidays.length ) {
        	  query(".results-full", resultsView.domNode)[0].innerHTML = "";
        	  return false;
          }
          resultsView.onBeforePlaceHolidays(isPagination);
          // todo: refactor
          if (resultsView.getView() === 'list') {
              // render template, include holidays and i8n texts
              html = resultsView.renderTmpl(null, lang.mixin(resultsView.analyticsData, {
                  holidays: holidays,
                  messages: resultsView.searchMessaging.searchResults,
                  tracs: (holidays[0].variantNInv.indexOf('TRACS') > -1)
              }));
             // if(holidays[0].searchVariant === 'PORT') {
            //	  resultsView.renderColumnWise(html, isPagination);
            //  }else{
            	//  domConstruct.place(html, resultsView.colNodes.listCol, "last");
             // }
             domConstruct.place(html, resultsView.colNodes.listCol, "last");

          } else {
              resultsView.constructGalleria(holidays);
          }

          isPagination ? resultsView.destroyResultWidgets(true) : null;
          if(resultsView.holidays[0].variantType != 'CRUISE_BROWSE_CALENDAR'){
        	  parser.parse(resultsView.domNode);
             }


		if(holidays[0].searchVariant === 'PORT') {
		  resultsView.invokeExcursioinOverlay(holidays);
		}

          resultsView.onAfterPlaceHolidays(isPagination);

    },

    updateTooltip: function(holiday){

       var searchResultComponent = this;
       var tracs = (holiday.variantNInv.indexOf('TRACS') > -1);
       var islandEscape = (holiday.sailings[0].shipName.name.indexOf('ISLAND')>-1);
       var hotelResult = holiday.variantNInv.indexOf('_HOTEL') > -1 ;

       //Flight timings
       //sailing.sailingAirport.tooltip
       //"Example -
       // Sat 31 Jan 2015: 14:25 > 18:45
       // Sat 7 Feb 2015: 19:50 > 00:05  (+1 day)
       // You can select other flights later, after choosing your cruise options "
       var outboundSchedule = holiday.sailings[0].outbound.schedule;
       var outboundOverlap = (outboundSchedule.overlapDay) ? '  (+1 day)' : ''
       var outBoundText = outboundSchedule.departureDate+': ' + outboundSchedule.departureTime +' > '+ outboundSchedule.arrivalTime + outboundOverlap;
       var inboundSchedule = holiday.sailings[0].inbound.schedule;
       var inboundOverlap = (inboundSchedule.overlapDay) ? '  (+1 day)' : ''
       var inBoundText = inboundSchedule.departureDate+': ' + inboundSchedule.departureTime +' > '+ inboundSchedule.arrivalTime + inboundOverlap;
       var flightText = outBoundText + '<br/>' + inBoundText;
       holiday.sailings[0].sailingAirport.tooltip = (!tracs) ? flightText +"<br/>"+ searchResultComponent.searchMessaging.searchResults.toolTip.atcomFlightMessage : flightText;
	   holiday.sailings[0].sailingAirport.tooltipBrowse = flightText;

       //Cabin friendly title
       if(tracs) {
           holiday.sailings[0].cabinType.cabinFriendlyTitle = searchResultComponent.searchMessaging.searchResults.cabinTypesTooltip;
           _.each(holiday.sailings, function (sailing) {
               _.each(sailing.cabins, function (cabin) {
                   cabin.cabinFriendlyTitleTooltip = searchResultComponent.searchMessaging.searchResults.cabinTypesTooltip;
               });
               //sailing.cabinType.cabinFriendlyTitleTooltip =  sailing.cabinType.cabinFriendlyTitle  + '<br/><br/>' + searchResultComponent.searchMessaging.searchResults.cabinTypesTooltip;
           });
       }
       else
       {
           holiday.sailings[0].cabinType.cabinFriendlyTitle = holiday.sailings[0].cabinType.cabinFriendlyTitle  + '<br/><br/>' + searchResultComponent.searchMessaging.searchResults.cabinTypesTooltip;
           //Multiple Cabin types are possible and they can have dynamic Cabin types
           _.each(holiday.sailings, function(sailing){
               _.each(sailing.cabins, function(cabin){
                   cabin.cabinFriendlyTitleTooltip = cabin.cabinFriendlyTitle + '<br/><br/>' + searchResultComponent.searchMessaging.searchResults.cabinTypesTooltip;
               });
              //sailing.cabinType.cabinFriendlyTitleTooltip =  sailing.cabinType.cabinFriendlyTitle  + '<br/><br/>' + searchResultComponent.searchMessaging.searchResults.cabinTypesTooltip;
           });
       }
       //Limited avail indicator
       //{{sailing.cabinType.tooltipInfo.tooltip}}
       //holiday.sailings[0].cabinType.tooltipInfo.tooltip = searchResultComponent.messages.toolTip.limitedAvailability;

       //{{sailing.boardBasis.additionalText}}


       if (holiday.sailings[0].boardBasis.code === 'FB')
          holiday.sailings[0].boardBasis.additionalText = searchResultComponent.searchMessaging.searchResults.toolTip.additionalTextForDrinks ;
        else
          holiday.sailings[0].boardBasis.additionalText = searchResultComponent.searchMessaging.searchResults.toolTip.additionalText;


       holiday.sailings[0].boardBasis.showTooltip = !tracs && !islandEscape;
       // /{{sailing.boardBasis.tooltip}}
       var difference = parseFloat(holiday.sailings[0].boardBasis.altBoardPrice) - parseFloat(holiday.sailings[0].boardBasis.price)
       var symbol = '+';
       (holiday.sailings[0].boardBasis.code === 'FB') ?  null : symbol = '-' ;
       var altCode = holiday.sailings[0].boardBasis.alternateBoardName;
       var alternateBoardText = _.isNull(holiday.sailings[0].boardBasis.altBoardPrice) ?
    		   '' : ('<br/><span class="fl">'+ altCode +'</span><span class="fr">'+symbol+'&pound;'
    		 + String( Math.round(holiday.sailings[0].boardBasis.altBoardPrice) > 0 ?
    			Math.round(holiday.sailings[0].boardBasis.altBoardPrice) : (Math.round(holiday.sailings[0].boardBasis.altBoardPrice)* -1)) + '&nbsp;&nbsp;</span>' );

       holiday.sailings[0].boardBasis.tooltip = searchResultComponent.searchMessaging.searchResults.toolTip.otherBoardBasisHeading
       + '<br/><span class="fl">' +  holiday.sailings[0].boardBasis.name + '</span><span class="fr">Selected&nbsp;&nbsp;</span>'
       + alternateBoardText + '<br/>'
       + searchResultComponent.searchMessaging.searchResults.toolTip.alternativeBoardBasisSelection;
       holiday.sailings[0].cruiseNStay.tooltip = holiday.sailings[0].cruiseNStay.tooltip;
       if(hotelResult){
          holiday.accommodation.limitedAvailabilityTooltip = tracs ? searchResultComponent.searchMessaging.searchResults.tracsLimitedAvailabilityRoom : searchResultComponent.searchMessaging.searchResults.atComLimitedAvailabilityRoom;
          holiday.boardBasis.tooltip = searchResultComponent.searchMessaging.searchResults.toolTip[holiday.boardBasis.code];
       }
       return holiday;
    },

    onAfterPlaceHolidays: function(isPagination){
    	  var resultsView = this;
    	  resultsView.inherited(arguments);
    	  query("ul.plist li").addClass("search-result-item");
      },

      destroyResultWidgets: function (preserveDom) {
          var resultsView = this;
          // destroy all child widgets (tooltips etc...)
          _.each(dijit.findWidgets(resultsView.domNode), function (w) {
        	  if( w.componentName != "excursionOverlay"){
        		  w.destroyRecursive(preserveDom);
        	  }
          });
        },
      // ----------------------------------------------------------------------------- mediator methods

      generateRequest: function (field) {
          var widget = this;
          //todo: convert to page numbers
          if (field === 'paginate') {
              return {
                  'page': _.inc(widget.currentPage),
                  'searchRequestType': 'paginate'
              };
          }
          return {
              'page': 1
          };
      },

      //Shortlist feature is not implemented yet
      refreshShortlistedPackages: function(){},

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