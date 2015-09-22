define('tui/widget/TagMappingTable', [ 'dojo' ], function (dojo) {

  dojo.declare('tui.widget.TagMappingTable', null, {

    // ----------------------------------------------------------------------------- properties

    table: {
      'tui.widget.carousels.Carousel': '001', // WF_COM_001
      'tui.widget.carousels.CSSCarousel': '001', // WF_COM_001
      'tui.widget.maps.MapTopx': '002', // WF_COM_002
      // 2 components with the same tag, not ideal, but it works:
      'tui.widget.WeatherChartCreator': '003', // WF_COM_003
      'tui.widget.WeatherChart': '003', // WF_COM_003
      'tui.widget.media.HeroCarousel': '007', // WF_COM_007
      'tui.widget.media.MediaPopup': '007', //
      'tui.widget.media.VideoPopup': '007', //
      'tui.widget.media.Touristboard': '128', // WF_COM_???
      'tui.widget.maps.MapScrollPanel': '057', // WF_COM_057
      'tui.widget.maps.LocationMap': '131', // WF_COM_???
      'tui.widget.pills.PillsSwitcher': '050', // WF_COM_050
      'tui.widget.search.QuickSearch': '074', // WF_COM_074
      'tui.widget.booking.GetPriceModal': '129', // WF_COM_???
      'tui.widget.TripAdvisorExpandable' : '040', // WF_COM_040
      'tui.widget.expand.LinkExpandable' : '040', // WF_COM_040
      'tui.widget.TripAdvisorSmoothScroll' : '035', // WF_COM_035
      'tui.widget.SocialMedia': '999',
      'tui.widget.interactiveItinerary.ItineraryCarousel': '358',
      'tui.widget.interactiveItinerary.ItineraryExpandable': '358',

      'tui.widget.taggable.breadcrumb': '051', // WF_COM_051
      'tui.widget.maps.MapScrollPanel': '002', // WF_COM_057
      'tui.widget.taggable.locationEditorial': '050', // WF_COM_050
      'tui.widget.pills.PillsSwitcher': '050', // WF_COM_050
      'tui.widget.taggable.TopPlaces': '118', // WF_COM_???
      'tui.widget.taggable.GeographicalNavigation': '018', // WF_COM_018
      'tui.widget.taggable.SEOLinks': '080', // WF_COM_080
      'tui.widget.taggable.AttractionResultPane': '023', // WF_COM_023
      'tui.widget.taggable.PlacesToStay': '011.1', // WF_COM_011.1
      'tui.widget.taggable.Deals': '015', // WF_COM_015
      'tui.widget.taggable.FacilitiesHighlights': '068', // WF_COM_068
      'tui.widget.taggable.AccomodationRoomHighlights': '039.1', // WF_COM_039.1
      'tui.widget.taggable.AccomodationRoomResultPane': '046', // WF_COM_046
      'tui.searchPanel.view.QuickSearch': '074', // WF_COM_074
      'tui.searchPanel.view.cruise.CruiseQuickSearch': '174', // WF_COM_174
      'tui.widget.media.Touristboard': '128', // WF_COM_???
      'tui.widget.booking.GetPriceModal': '129', // WF_COM_???
      'tui.widget.TripAdvisorExpandable': '130', // WF_COM_???
      'tui.widget.maps.LocationMap': '131', // WF_COM_???
      'tui.widget.taggable.Navigation': '132', // WF_COM_006
      'tui.widget.taggable.ThumbnailMap': '016', // WF_COM_016
      'tui.widget.taggable.PassportAndVisaEditorial': '064', // WF_COM_064
      'tui.widget.taggable.AccommodationDisclaimer': '064', // WF_COM_064
      'tui.widget.taggable.NavigationForSeoPages': '139', // WF_COM_???
      'tui.widget.taggable.PriceAndAvailability': '027', // WF_COM_027
      'tui.widget.taggable.LocationPanelAnalytics' : '037', // WF_COM_037
     'tui.widget.TripAdvisorSmoothScroll' : '035', // WF_COM_035
     'tui.widget.TripAdvisorExpandable' : '040', // WF_COM_040
     'tui.widget.expand.LinkExpandable' : '040', // WF_COM_040
      'tui.widget.expand.Expandable': '040', // WF_COM_040
      'tui.widget.taggable.GSASearch': '300', // GSA Search box
      'tui.widget.taggable.INVProductPromoComponent':'904-2', //Product promo compnent -WP_COM_904-2
      //Social media links (homepage)
      'tui.widget.SocialMedia': '999',
      'tui.widget.search.QuickSearch': '074',//WF_COM_074
      'tui.widget.media.Touristboard': '335',//WF_COM_335
      'tui.widget.taggable.LaplandDaytripTaggable':'359', // WF_COM_359

      //BrowseFlow widgets tagging codes
      'tui.widget.taggable.ItineraryTaggable':'355', // WF_COM_355
      'tui.widget.taggable.AddAStayItinerary':'357', // WF_COM_357
      'tui.widget.taggable.NeedToKnowTaggable':'351-3', // WF_COM_351-3
      'tui.widget.taggable.ProductCrossSellTaggable':'001.2', // WF_COM_001.2
      'tui.widget.taggable.TwoCentreSelectorTaggable':'354', // WF_COM_354
      'tui.widget.taggable.FeatureListTaggable':'350-1', // WF_COM_350-1
      'tui.widget.taggable.PlacesOfInterestTaggable':'350-2', // WF_COM_350-2
      'tui.widget.taggable.NileCruisePlacesToStayTaggable':'011-3', // WF_COM_011-3
      'tui.widget.taggable.UpsellToHolidayTaggable':'356', // WF_COM_356
      'tui.widget.taggable.AllInOnePackageTaggable':'362', // WF_COM_362

      // Search Panel
      'tui.searchPanel.view.AdultsSelectOption': '300', // WF_COM_300
      'tui.searchPanel.view.AirportGuide': '300', // WF_COM_300
      'tui.searchPanel.view.AirportMultiFieldList': '300', // WF_COM_300
      'tui.searchPanel.view.AirportAutoComplete': '300', // WF_COM_300
      'tui.searchPanel.view.ChildSelectOption': '300', // WF_COM_300
      'tui.searchPanel.view.ChildAgesView': '300', // WF_COM_300
      'tui.searchPanel.view.DestinationGuide': '300', // WF_COM_300
      'tui.searchPanel.view.DestinationGuideSelectOption': '300', // WF_COM_300
      'tui.searchPanel.view.DestinationMultiFieldList': '300', // WF_COM_300
      'tui.searchPanel.view.DestinationAutoComplete': '300', // WF_COM_300
      'tui.searchPanel.view.FlexibleView': '300', // WF_COM_300
      'tui.searchPanel.view.SearchDatePicker': '300', // WF_COM_300
      'tui.searchPanel.view.SearchSummary': '300', // WF_COM_300
      'tui.searchPanel.view.SeniorsSelectOption': '300', // WF_COM_300
      'tui.searchPanel.view.SubmitButton': '300', // WF_COM_300
      'tui.searchPanel.view.ErrorPopup': '300', // WF_COM_300

      // Search panel variant B

      'tui.searchBPanel.view.AdultsSelectOption': '300', // WF_COM_300
      'tui.searchBPanel.view.AirportGuide': '300ab', // WF_COM_300
      'tui.searchBPanel.view.AirportMultiFieldList': '300', // WF_COM_300
      'tui.searchBPanel.view.AirportAutoComplete': '300', // WF_COM_300
      'tui.searchBPanel.view.ChildSelectOption': '300', // WF_COM_300
      'tui.searchBPanel.view.ChildAgesView': '300', // WF_COM_300
      'tui.searchBPanel.view.DestinationGuide': '300ab', // WF_COM_300
      'tui.searchBPanel.view.DestinationGuideSelectOption': '300ab', // WF_COM_300
      'tui.searchBPanel.view.DestinationGuideSearchBox': '300ab', // WF_COM_300
      'tui.searchBPanel.view.DestinationMultiFieldList': '300ab', // WF_COM_300
      'tui.searchBPanel.view.DestinationAutoComplete': '300ab', // WF_COM_300
      'tui.searchBPanel.view.FlexibleView': '300', // WF_COM_300
      'tui.searchBPanel.view.SearchDatePicker': '300', // WF_COM_300
      'tui.searchBPanel.view.SearchSummary': '300', // WF_COM_300
      'tui.searchBPanel.view.SeniorsSelectOption': '300', // WF_COM_300
      'tui.searchBPanel.view.SubmitButton': '300', // WF_COM_300
      'tui.searchBPanel.view.ErrorPopup': '300', // WF_COM_300

      'tui.searchPanel.view.cruise.AirportGuide': '300', // WF_COM_300
      'tui.searchPanel.view.cruise.DestinationGuide': '300', // WF_COM_300
      'tui.searchPanel.view.cruise.DatePicker': '300', // WF_COM_300
      'tui.searchPanel.view.cruise.DurationPicker': '300', // WF_COM_300
      'tui.searchPanel.view.cruise.CruiseSubmitButton': '300', // WF_COM_300

      // Search results
      'tui.searchResults.view.DateAvailabilitySlider': '363',
      'tui.searchResults.view.HolidayDurationView': '310', // WF_COM_310
      'tui.searchResults.view.SearchResultsComponent': '308',
      'tui.searchResults.view.SearchResultsComponentThFc': '308',
      'tui.widget.taggable.GalleryViewTwinMediaAnalytics': '305',
      'tui.searchResults.view.SortResultsSelectOption': '092',
      'tui.searchResults.view.NoResultsPopup': '319',
      'tui.searchResults.view.ViewModeToggle': '306',
      'tui.searchResults.view.NoResultView': '317',

      // SearchB results
      'tui.searchBResults.view.DateAvailabilitySlider': '363',
      'tui.searchBResults.view.HolidayDurationView': '310', // WF_COM_310
      'tui.searchBResults.view.SearchResultsComponent': '308',
      'tui.widget.taggable.GalleryViewTwinMediaAnalytics': '305',
      'tui.searchBResults.view.SortResultsSelectOption': '092',
      'tui.searchBResults.view.NoResultsPopup': '319',
      'tui.searchBResults.view.ViewModeToggle': '306',
      'tui.searchBResults.view.NoResultView': '317',

      //Back to search component
      'tui.widget.taggable.BackToSearch':'006', //

      // Single Accom results
      'tui.singleAccom.view.FlightOptions': '309',
      'tui.singleAccom.view.FlightGroup': '309',
      'tui.singleAccom.view.DurationHighlight': '309',
      'tui.singleAccom.view.AirportDateToggle': '309',

    //Back to search component
      'tui.widget.taggable.BackToSearch':'006', // WF_COM_006


      // Shortlist
      //'tui.shortlist.view.ShortlistSummary' : '401',

      // Filter panel
      'tui.filterPanel.view.FilterController': '087.B',
      'tui.filterPanel.view.sliders.TotalBudget': '087.B',
      'tui.filterPanel.view.sliders.PerPersonBudget': '087.B',
      'tui.filterPanel.view.sliders.TotalBudgetTapping': '087.B',
      'tui.filterPanel.view.sliders.PerPersonBudgetTapping': '087.B',
      'tui.filterPanel.view.sliders.Rating': '087.B',
      'tui.filterPanel.view.sliders.TaRating': '087.B',
      'tui.filterPanel.view.sliders.RatingTapping': '087.B',
      'tui.filterPanel.view.sliders.TaRatingTapping': '087.B',
      'tui.filterPanel.view.options.General': '087.B',
      'tui.filterPanel.view.options.Flight': '087.B',
      'tui.filterPanel.view.options.Hotel': '087.B',
      'tui.filterPanel.view.options.Destination': '087.B',
      'tui.searchResults.view.ClearFilters': '087.B',
      'tui.filterPanel.view.BudgetToggle': '087.B',
 // Filter Bpanel
      'tui.filterBPanel.view.FilterController': '087.B',
      'tui.filterBPanel.view.sliders.TotalBudget': '087.B',
      'tui.filterBPanel.view.sliders.PerPersonBudget': '087.B',
      'tui.filterBPanel.view.sliders.Rating': '087.B',
      'tui.filterBPanel.view.sliders.TaRating': '087.B',
      'tui.filterBPanel.view.options.General': '087.B',
      'tui.filterBPanel.view.options.Flight': '087.B',
      'tui.filterBPanel.view.options.Hotel': '087.B',
      'tui.filterBPanel.view.options.Destination': '087.B',
      'tui.searchBResults.view.ClearFilters': '087.B',
      'tui.filterBPanel.view.BudgetToggle': '087.B',
      'tui.filterBPanel.view.options.BoardBasis':'087.B',

      'tui.filterPanel.view.options.CruiseOptionsFilter': '087.B',

        // INV Search results
      'tui.showIGPackages.view.ShowIGPackagesComponent': '001.3',
      // INV Filter panel
      'tui.filterIGPanel.view.Rating': '087.B',
      'tui.filterIGPanel.view.TaRating': '087.B',
      'tui.filterIGPanel.view.Destination': '087.B',
      'tui.filterIGPanel.controller.FilterController': '087.B',
      'tui.filterIGPanel.view.AccomidationOptions': '087.B',
      'tui.filterIGPanel.view.BestFor': '087.B',
      'tui.showIGPackages.view.ClearFilters': '087.B',
      'tui.filterIGPanel.view.Collection':'087.B',

      // Rooms Editor
      'tui.searchResults.roomEditor.view.RoomsPopup': '087.B',
      'tui.searchResults.roomEditor.view.RoomsEditor': '087.B',
      'tui.searchResults.roomEditor.view.RoomsSummary': '087.B',
      'tui.searchResults.roomEditor.view.AdultsSelectOption': '087.B',
      'tui.searchResults.roomEditor.view.ChildrenSelectOption': '087.B',
      'tui.searchResults.roomEditor.view.SeniorsSelectOption': '087.B',
      'tui.searchResults.roomEditor.view.PartyCompSelectOption': '087.B',

      // Rooms Editor Search Bpanel
      'tui.searchBResults.roomEditor.view.RoomsPopup': '087.B',
      'tui.searchBResults.roomEditor.view.RoomsEditor': '087.B',
      'tui.searchBResults.roomEditor.view.RoomsSummary': '087.B',
      'tui.searchBResults.roomEditor.view.AdultsSelectOption': '087.B',
      'tui.searchBResults.roomEditor.view.ChildrenSelectOption': '087.B',
      'tui.searchBResults.roomEditor.view.SeniorsSelectOption': '087.B',
      'tui.searchBResults.roomEditor.view.PartyCompSelectOption': '087.B',

      //Accommodation Summary
      'tui.widget.taggable.AccommodationSummary': '365',

      // Villa Availability
      'tui.widget.carousels.VillaAvailability': '344',
      'tui.widget.carousels.DynamicCarousel': '344',

      // Bookflow (accom overview)
      'tui.widget.popup.LightboxPopup': '367',

	  // BookFlow widgets tagging codes Room Options page
      'tui.widget.booking.latecheckout.view.LateCheckoutDeal': 'ROOMS_LATE_CHK',
      'tui.widget.booking.latecheckout.view.LateCheckOutView': 'ROOMS_LATE_CHK',
      'tui.widget.booking.changeroomallocation.view.ChangeRoom': 'ROOMS_NUM_SEL',
      'tui.widget.booking.RoomBoard.view.RoomBoardBasisButtonToggler': 'ROOMS_BOARD',
      'tui.widget.booking.changeroomallocation.view.RoomTypeAllocation': 'ROOMS_NUM_SEL',
      'tui.widget.booking.changeroomallocation.view.AlternativeRoom': 'ROOMS_SEL',
      'tui.widget.booking.changeroomallocation.view.ChangeRoomAllocation': 'ROOMS_NUM_SEL',
      'tui.widget.booking.changeroomallocation.view.AlternativeRoom': 'ROOMS_SEL',

      // BookFlow widgets tagging codes summary panel
      'tui.widget.booking.summary.view.ExtrasSummaryPanel': 'BOOK_SUM_PANEL',
      'tui.widget.booking.summary.view.FlightSummaryPanel': 'BOOK_SUM_PANEL',
      'tui.widget.booking.summary.view.TotalPriceSummaryPanel': 'BOOK_SUM_PANEL',
      'tui.widget.booking.summary.view.RoomSummaryPanel': 'BOOK_SUM_PANEL',
      'tui.widget.booking.alertMessage.view.AlertsMessage': 'BOOK_ALERTS',

      // BookFlow widgets tagging codes Flight Options page
      'tui.widget.booking.yourflight.view.YourFlights' : 'FLIGHTS_CUR',
      'tui.widget.booking.flightoptions.FlightOptionsMediator': 'FLIGHTS_ALT',
      'tui.widget.booking.yourseats.view.seatChangeButtonToggler' : 'FLIGHTS_SEATS_COMP',
      'tui.widget.booking.yourluggage.view.LuggageAllowanceComponent' : 'FLIGHTS_LUG',
      'tui.widget.booking.inflightmeals.view.InflightMeal' : 'FLIGHTS_MEALS',
      'tui.widget.booking.inflightmeals.view.InflightMealView' : 'FLIGHTS_MEALS_SEL',
      'tui.widget.booking.flightoptions.view.FlightOptionsView' : 'FLIGHTS_ALT_CAL',
      'tui.widget.booking.yourluggage.view.LuggageAllowanceComponent' : 'FLIGHTS_LUG',
      'tui.widget.booking.flightoptions.view.FlightOptionsCalendar' : 'FLIGHTS_ALT_CAL',
      'tui.widget.booking.flightoptions.view.FlightOptionsSummaryPanel' : 'FLIGHTS_ALT_CAL',
      'tui.widget.booking.flightoptions.view.FlightOptionDatepicker' : 'FLIGHTS_ALT_CAL',

      // BookFlow widgets tagging codes Extras page
      'tui.widget.booking.hoteloptions.view.HotelFacilitiesComponentPanel' : 'EXTRAS_ACT_KIDS',
      'tui.widget.booking.infantoptions.view.PrebookInfantEquipment' : 'EXTRAS_ACT_INFT',
      'tui.widget.booking.transferoptions.view.TransferToggle' : 'EXTRAS_TRANSFERS',
      'tui.widget.booking.excursion.view.ExcursionsOptionsComponent' : 'EXTRAS_EXC',
      'tui.widget.booking.attractions.view.AttractionOptionsComponentPanel' : 'EXTRAS_ATTRACTS',
      'tui.widget.booking.attractions.view.AttractionView' : 'EXTRAS_ATTRACT_ADD',
      'tui.widget.booking.insurance.InsuranceFamilyCover' : 'EXTRAS_INSURE',
      'tui.widget.booking.insurance.InsuranceIndAdd': 'EXTRAS_INSURE_ADD_IND',
      'tui.widget.booking.insurance.InsuranceIndAddDetails': 'EXTRAS_INSURE_ADD_IND',
      'tui.widget.booking.excursion.view.ExcursionsOptionsView' : 'EXTRAS_EXC_ADD',
      'tui.widget.booking.worldcarefund.view.WorldCareFundHandleCheckbox': 'EXTRAS_WORLD_CARE',

      // BookFlow widgets tagging codes passengers page
      'tui.widget.booking.passengers.view.PassengerDetailsView' : 'PAS_DETAILS_SIGNED_IN',
      'tui.widget.booking.passengers.view.DiscountCode': 'PAS_DISCOUNT',
      'tui.widget.booking.passengers.view.PassengerDetailsView': 'PAS_DETAILS',
      
      // Flights widgets tagging codes  	
	'tui.searchPanel.view.flights.AirportMultiFieldList' : 'COM_FO_SEARCH_PANEL',
	'tui.searchPanel.view.flights.DestinationMultiFieldList' : 'COM_FO_SEARCH_PANEL',
	'tui.searchPanel.view.flights.ArrivalAirportMultiFieldList' : 'COM_FO_SEARCH_PANEL',
	'tui.searchPanel.view.flights.AirportGuide': 'COM_FO_SEARCH_PANEL',
	'tui.searchPanel.view.flights.AirportListOverlay': 'COM_FO_SEARCH_PANEL',
	'tui.searchPanel.view.flights.DestinationGuide': 'COM_FO_SEARCH_PANEL',
	'tui.searchPanel.view.flights.ArrivalAirportGuide': 'COM_FO_SEARCH_PANEL',
	'tui.searchPanel.view.flights.ArrivalAirportListOverlay': 'COM_FO_SEARCH_PANEL',
	'tui.searchPanel.view.flights.AdultsSelectOption': 'COM_FO_SEARCH_PANEL',
	'tui.searchPanel.view.flights.SeniorsSelectOption': 'COM_FO_SEARCH_PANEL',
	'tui.searchPanel.view.flights.SearchDatePicker': 'COM_FO_SEARCH_PANEL',
	'tui.searchPanel.view.flights.ArrivalSearchDatePicker': 'COM_FO_SEARCH_PANEL',
	'tui.searchPanel.view.flights.SubmitButton': 'COM_FO_SEARCH_PANEL',
	'tui.searchPanel.view.flights.OneWayOnly': 'COM_FO_SEARCH_PANEL',
	'tui.searchPanel.view.flights.ArrivalFlightSearchMonthBar' : 'COM_FO_SEARCH_PANEL',
	'tui.searchResults.view.flights.FlightSearchResultController' : ' COM_FO_SEARCH_RESULTS_PRICE_GRID',
	'tui.searchResults.view.flights.NoSearchResultsController' : 'COM_FO_NO_RESULTS_PAGE',
	'tui.searchResults.view.PriceToggle' : ' COM_FO_SEARCH_RESULTS_INDIVIDUAL'	


    }
  });

  // ----------------------------------------------------------------------------- singleton

  // Singleton class instance.
  var tagMappingTable = null;

  // Singleton method.
  tui.widget.TagMappingTable.getInstance = function () {
    if (!tagMappingTable) {
      tagMappingTable = new tui.widget.TagMappingTable();
    }
    return tagMappingTable;
  };

  return tui.widget.TagMappingTable.getInstance();
});
