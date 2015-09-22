define("tui/searchBResults/view/variants/VariantViewMapping", [
    "dojo",
    "dojo/text!tui/searchBResults/view/templates/variants/flyCruise.html",
    "dojo/text!tui/searchBResults/view/templates/variants/cruiseBrowseCalendar.html",
    "dojo/text!tui/searchBResults/view/templates/variants/cruiseBrowseItinerary.html",
    "dojo/text!tui/searchBResults/view/templates/variants/cruiseBrowsePocListing.html",
    "dojo/text!tui/searchBResults/view/templates/variants/exUk.html",
    "dojo/text!tui/searchBResults/view/templates/variants/backToBack.html",
    "dojo/text!tui/searchBResults/view/templates/variants/cruiseAndStay.html",
    "dojo/text!tui/searchBResults/view/templates/variants/stayAndCruise.html",
    "dojo/text!tui/searchBResults/view/templates/variants/cruiseAndStayHotel.html",
    "dojo/text!tui/searchBResults/view/templates/variants/cruiseAndStayTracs.html",
    "dojo/text!tui/searchBResults/view/templates/variants/stayAndCruiseTracs.html"

    ], function (dojo, flyCruise, cruiseBrowseCalendar, cruiseBrowseItinerary, cruisePoc, exUk, backToBack, cruiseAndStay, stayAndCruise, cruiseAndStayHotel, cruiseAndStayTracs, stayAndCruiseTracs) {

    var mappings = {

        'CRUISE_BROWSE_CALENDAR' : cruiseBrowseCalendar,  //Browse Calendar
        'CRUISE_BROWSE_ITINERARY' : cruiseBrowseItinerary,      //Browse Itinerary
        'PORT': cruisePoc,
        'FLY_CRUISE' :  flyCruise,  //Fly Cruise
        'EX_UK' : exUk, //Ex UK - instead of Flight use Port data
        'BACK_BACK' : backToBack,  //Back to back sailings
        'CRUISE_STAY_TRACS' : cruiseAndStayTracs, //Cruise and Stay
        'STAY_CRUISE_TRACS' : stayAndCruiseTracs, //Stay and Cruise
        //'STAY_CRUISE_HOTEL' :,// - Atcom
        'CRUISE_STAY' : cruiseAndStay, //Cruise and Stay - Atcom
        'STAY_CRUISE' : stayAndCruise, //Stay and Cruise - Atcom
        'CRUISE_STAY_HOTEL' : cruiseAndStayHotel// - Atcom


    };

    return function (displayType) {
        _.debug('Using Variant Type:'+displayType);
        return mappings[displayType];
    }
})
