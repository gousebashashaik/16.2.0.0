define("tui/searchResults/view/variants/VariantViewMapping", [
    "dojo",
    "dojo/text!tui/searchResults/view/templates/variants/flyCruise.html",
    "dojo/text!tui/searchResults/view/templates/variants/cruiseBrowseCalendar.html",
    "dojo/text!tui/searchResults/view/templates/variants/cruiseBrowseItinerary.html",
    "dojo/text!tui/searchResults/view/templates/variants/cruiseBrowsePocListing.html",
    "dojo/text!tui/searchResults/view/templates/variants/exUk.html",
    "dojo/text!tui/searchResults/view/templates/variants/backToBack.html",
    "dojo/text!tui/searchResults/view/templates/variants/cruiseAndStay.html",
    "dojo/text!tui/searchResults/view/templates/variants/stayAndCruise.html",
    "dojo/text!tui/searchResults/view/templates/variants/cruiseAndStayHotel.html",
    "dojo/text!tui/searchResults/view/templates/variants/cruiseAndStayTracs.html",
	"dojo/text!tui/searchResults/view/templates/variants/stayAndCruiseHotel.html",
    "dojo/text!tui/searchResults/view/templates/variants/stayAndCruiseTracs.html"

    ], function (dojo, flyCruise, cruiseBrowseCalendar, cruiseBrowseItinerary, cruisePoc, exUk, backToBack, cruiseAndStay, stayAndCruise, cruiseAndStayHotel, cruiseAndStayTracs, stayAndCruiseHotel, stayAndCruiseTracs) {

    var mappings = {
        'CRUISE_BROWSE_CALENDAR' : cruiseBrowseCalendar,  //Browse Calendar
        'CRUISE_BROWSE_ITINERARY' : cruiseBrowseItinerary,      //Browse Itinerary
		'CRUISE_BROWSE_ITINERARY_TRACS' : cruiseBrowseItinerary,      //Browse Itinerary
		'CRUISE_BROWSE_ITINERARY_ATCOM' : cruiseBrowseItinerary,      //Browse Itinerary
        'PORT': cruisePoc,
		'FLY_CRUISE_TRACS' :  flyCruise,  //Fly Cruise tracs
		'FLY_CRUISE_ATCOM' :  flyCruise,  //Fly Cruise -Atcom
		'CRUISE_STAY_TRACS' : cruiseAndStayTracs, //Cruise and Stay tracs
		'CRUISE_STAY_ATCOM' : cruiseAndStay, //Cruise and Stay - Atcom
		'STAY_CRUISE_TRACS' : stayAndCruiseTracs, //Stay and Cruise tracs
		'STAY_CRUISE_ATCOM' : stayAndCruise, //Stay and Cruise - Atcom
		'BACK_BACK_TRACS' : backToBack,  //Back to back tracs
		'BACK_BACK_ATCOM' : backToBack,  //Back to back - Atcom	
		'STAY_CRUISE_TRACS_HOTEL' : stayAndCruiseHotel,// - tracs
		'STAY_CRUISE_ATCOM_HOTEL' : stayAndCruiseHotel,// - Atcom
		'CRUISE_STAY_TRACS_HOTEL' : cruiseAndStayHotel,// - tracs
		'CRUISE_STAY_ATCOM_HOTEL' : cruiseAndStayHotel,// - Atcom
        'EX_UK' : exUk //Ex UK - instead of Flight use Port data
    };

    return function (displayType) {
        _.debug('Using Variant Type:'+displayType);
        return mappings[displayType];
    }
})
