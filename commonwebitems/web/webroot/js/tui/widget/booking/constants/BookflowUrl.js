define("tui/widget/booking/constants/BookflowUrl", [ "dojo",
		"dojo/_base/declare"], function(dojo, declare) {

	var BookflowUrl =  declare('tui.widget.booking.constants.BookflowUrl', [], {

		latecheckouturl : dojoConfig.paths.webRoot+'/book/update/latecheckout',
		worldcarefundurl: dojoConfig.paths.webRoot+'/book/update/charity',
		seatchangebuttontogglerurl : dojoConfig.paths.webRoot+"/book/update/seat",
		luggageallowanceurl : dojoConfig.paths.webRoot+"/book/update/baggage",
		transfertoggleurl :dojoConfig.paths.webRoot+"/book/update/transfer",
		inflightmealurl :dojoConfig.paths.webRoot+'/book/update/meal',
		crechespaceurl :dojoConfig.paths.webRoot+"/book/update/creche",
		socceracademyurl :dojoConfig.paths.webRoot+"/book/update/soccer",
		stageacademyurl: dojoConfig.paths.webRoot+"/book/update/stage",
		stageschoolurl: dojoConfig.paths.webRoot+"/book/update/stageSchool",
		swimacademyurl: dojoConfig.paths.webRoot+"/book/update/swim",
		infantoptionurl: dojoConfig.paths.webRoot+'/book/update/infants',
		classActurl: dojoConfig.paths.webRoot+'/book/update/classAct',
		hoteloptionremoveurl: dojoConfig.paths.webRoot+"/book/remove/kidsactivity",
		finishingtouchesremoveurl: dojoConfig.paths.webRoot+"/book/remove/finishingtouches",
		passengerdetailsurl : dojoConfig.paths.webRoot+"/book/promocode",
		promoCodeRemoveUrl: dojoConfig.paths.webRoot+"/book/promocode/change",
		insuranceaddurl : dojoConfig.paths.webRoot+"/book/add/insurance",
		insuranceBaddurl : dojoConfig.paths.webRoot+"/book/addIns/insurance",
		insuranceselecturl : dojoConfig.paths.webRoot+"/book/select/insurance",
		insuranceremoveurl:dojoConfig.paths.webRoot+"/book/remove/insurance",
		insuranceBremoveurl:dojoConfig.paths.webRoot+"/book/removeIns/insurance",
		alternativeflighturl :dojoConfig.paths.webRoot+"/book/alternativeflight",
		roombuttontogglerurl : dojoConfig.paths.webRoot+"/book/changeRoom",
		changeBoardBasisurl: dojoConfig.paths.webRoot+"/book/changeBoardBasis",
		changeCabinBoardBasisurl: dojoConfig.paths.webRoot+"/book/cabin/changeBoardBasis",
		changeroomsurl :dojoConfig.paths.webRoot+"/book/changeRoomAllocation",
		cruisecabinsurl: dojoConfig.paths.webRoot+'/book/changeCabinAllocation',
		cruiseDeckPlan: dojoConfig.paths.webRoot+'/book/deckPlan',
		excursionurl:dojoConfig.paths.webRoot+"/book/update/excursion",
		attractionurl: dojoConfig.paths.webRoot +"/book/update/attraction",
		carhireurl : dojoConfig.paths.webRoot+"/book/update/carhireupgrade",
		passengerchildurl:dojoConfig.paths.webRoot+"/book/paxpartycheck",
        saveholidayurl:dojoConfig.paths.webRoot+"/book/saveholiday",
	    removeHolidayUrl: dojoConfig.paths.webRoot+"/book/removeholiday",
	    cabinbuttontogglerurl : dojoConfig.paths.webRoot+"/book/changeCabin",
	    fadeOutDuration: 5000,
	    preferences:dojoConfig.paths.webRoot+"/book/communicationpreferences",
	    	logInRequest:dojoConfig.paths.webRoot+"/book/customerLogin/logInRequest",
			forgotPassword:dojoConfig.paths.webRoot+"/book/customerLogin/forgetPasswordRequest",
			travellingPassengerSelection:dojoConfig.paths.webRoot+"/book/customerLogin/travellingPassengerSelection",
			nonTravellingPassengerSelection:dojoConfig.paths.webRoot+"/book/customerLogin/nonTravellingPassengerSelection"

	});

	return new BookflowUrl();
});