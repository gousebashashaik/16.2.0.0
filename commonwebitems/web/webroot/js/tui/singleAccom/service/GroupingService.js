define("tui/singleAccom/service/GroupingService", [
    "dojo",
    "tui/singleAccom/model/Group"], function (dojo, Group) {

    var groupOperations = {
        'MULTIPLEDATESSINGLEAIRPORT' :  byDepartureDates, // 3
        'SINGLEDATEMULTIPLEAIRPORTS' : byDepartureAirports,  // 4
        'MULTIPLEDATESAIRPORTS' : byAirportAndDates, // 5, 6
        'SINGLEDATEAIRPORTFLIGHT' : byDuration, // 1
        'SINGLEDATEAIRPORTMULTIPLEFLIGHTS' : byDuration // 2
    };

    function getLowPrice (holidays) {
        return {
            "totalParty": _.min((_.pluck(_.pluck(holidays, "price"), "totalParty")).map(Number)),
            "perPerson": _.min((_.pluck(_.pluck(holidays, "price"), "perPerson")).map(Number))
        }
    }

    function getMinDate (dates) {
        var date = new Date(_.min(dates));
        return date.getDate() + "-" + (date.getMonth() + 1) + "-" + date.getFullYear();
    }

    function getMaxDate (dates) {
        var date = new Date(_.max(dates));
        return date.getDate() + "-" + (date.getMonth() + 1) + "-" + date.getFullYear();
    }

    function getDateRange (holidays) {
        var depDates = _.uniq(_.pluck(_.pluck(holidays, "itinerary"), "departureDate")),
            dateObjects = [], firstDate, lastDate, minDate, maxDate, firstFormat = "EEE d";

        _.each(depDates, function(date){
            date = date.split('-');
            date = new Date(date[2], parseInt(date[1],10) - 1, date[0]);
            dateObjects.push(date.getTime())
        });

        minDate = getMinDate(dateObjects);
        maxDate = getMaxDate(dateObjects);

        firstDate = minDate.split('-');
        lastDate = maxDate.split('-');

        if (firstDate[1] !== lastDate[1]) firstFormat += " MMM";
        if (firstDate[2] !== lastDate[2]) firstFormat += " yyyy";

        return {
            firstDate: (minDate !== maxDate) ? _.formatDate(minDate, null, firstFormat) : false,
            lastDate: _.formatDate(maxDate)
        }
    }

    function summarizeList (arr, limit) {
        limit = limit || null;
        var more = limit ? arr.length - limit : null;
        return limit ? _.first(arr, limit).join(',&nbsp;') + (more ? "&hellip; +" + more + " more" : "") : arr.join(',&nbsp;');
    }

    function getAirportList (holidays) {
        var airports = _.uniq(_.pluck(_.pluck(holidays, "itinerary"), "departureAirport"));
        return airports.length > 3 ? summarizeList(airports, 3) : summarizeList(airports);
    }

    function byDepartureDates (holidays, incLowPrice, incDateRange, incAirportList) {
        var groups = _.groupBy(holidays, function (holiday) {
            return holiday.itinerary.departureDate;
        });

        var holidaysByDepartureDates = _.map(groups, function (members, key) {
            return new Group(
                key,
                members[0].itinerary.outbounds[0].schedule.departureDate,
                "byDates",
                members,
                incLowPrice ? getLowPrice(members) : null,
                incDateRange ? getDateRange(members) : null,
                incAirportList ? getAirportList(members) : null
            );
        });

        return {
            'byDates': holidaysByDepartureDates
        }
    }

    function byDepartureAirports (holidays, incLowPrice, incDateRange, incAirportList) {
        var groups = _.groupBy(holidays, function (holiday) {
            return holiday.itinerary.outbounds[0].departureAirportCode;
        });

        var holidaysByDepartureAirports = _.map(groups, function (members, key) {
            return new Group(
                key,
                members[0].itinerary.outbounds[0].departureAirport,
                "byAirports",
                members,
                incLowPrice ? getLowPrice(members) : null,
                incDateRange ? getDateRange(members) : null,
                incAirportList ? getAirportList(members) : null
            );
        });

        return {
            'byAirports': holidaysByDepartureAirports
        }
    }

    function byDuration (holidays) {
        var groups = _.groupBy(holidays, function (holiday) {
            return holiday.duration;
        });

        var holidaysByDuration = _.map(groups, function (members, key) {
            return new Group(key, members[0].duration, "byDuration", members);
        });

        return {
            'byDuration': holidaysByDuration
        }
    }

    function byAirportAndDates (holidays) {
        return dojo.mixin({}, byDepartureDates(holidays, true, false, true), byDepartureAirports(holidays, true, true, false))
    }

    return {

        group : function (holidays, groupType) {
            return groupOperations[groupType](holidays)
        }
    }

})