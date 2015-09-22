define("tui/singleAccom/service/FlightOptionService", [
    "dojo",
    "tui/singleAccom/service/GroupingService",
    "tui/searchResults/service/RoomGroupingService"], function (dojo, groupingService, roomGroupingService) {


    function formatDateAndTime(leg) {
        leg.schedule.departureDate = _.formatDate(leg.schedule.departureDate);
        leg.schedule.arrivalDate = _.formatDate(leg.schedule.arrivalDate);

        leg.schedule.departureTime = _.formatTime(leg.schedule.departureTime);
        leg.schedule.arrivalTime = _.formatTime(leg.schedule.arrivalTime);
    }

    function formatRoomsAndDates(holidays) {
        _.each(holidays, function (holiday, i) {

            _.each(holiday.itinerary.outbounds, function (outbound) {
                formatDateAndTime(outbound);
            });

            _.each(holiday.itinerary.inbounds, function (inbound) {
                formatDateAndTime(inbound);
            });

            holiday.accommodation.rooms = roomGroupingService.groupRooms(holiday.accommodation.rooms, "roomType");
            holiday.finPos = i + 1 ;
            holiday.airportFinPos = i + 1 ;
            // due to django limitations have to check deposits here
            holiday.showDeposits = holiday.price.lowDepositExists || holiday.price.depositExists;

            //if Discounts are less then 1 or 0 hide the discount component
            	holiday.price.discountPPFlag = parseInt(holiday.price.discountPP) > 0 ? true : false;
                holiday.price.discountFlag =parseInt(holiday.price.discount)  > 0 ? true : false;

        });
        return holidays;
    }

    return {
        process: function (searchResponse) {
            //Logic here

            var wireFrameType = searchResponse['singleAccomViewData']['wireframeType'];
            //var groups = groupingService[searchResponse['singleAccomViewData']['wireframeType']](formatDates(searchResponse['holidays']))
            //var groups = groupingService[methodCall](formatDates(searchResponse['holidays']))
            var groups = groupingService.group(formatRoomsAndDates(dojo.clone(searchResponse['holidays'])), wireFrameType);
            return {
                'data': groups,
                'type': wireFrameType,
                'showMoreDuration': searchResponse.durationSelection.defaultDisplay.length > 1
            };
        }
    };

});