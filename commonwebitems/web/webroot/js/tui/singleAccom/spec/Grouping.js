describe("Single Accommodation results page holiday grouping", function () {
    var service;

    beforeEach(function () {
        service = dojo.require("tui/singleAccom/service/GroupingService");
    });

    it("should be loaded", function () {
        expect(service).not.toBe(null);
    });

    it("should Group Holidays by departure dates", function () {
        var holidays = [
            {
                "itinerary": {
                    "departureDate": "11-06-2013",
                    "outbounds": [
                        {
                            "departureAirport": "London Gatwick",
                            "departureAirportCode": "LGW",
                            "schedule": {
                                "departureDate": "11-06-2013"
                            }
                        }
                    ]
                },
                "packageId": "Q007662",
                "duration": 7
            },
            {
                "itinerary": {
                    "departureDate": "06-06-2013",
                    "outbounds": [
                        {
                            "departureAirport": "London Gatwick",
                            "departureAirportCode": "LGW",
                            "schedule": {
                                "departureDate": "06-06-2013"
                            }
                        }
                    ]
                },
                "packageId": "Q020233"
            },
            {
                "itinerary": {
                    "departureDate": "11-06-2013",
                    "departureAirport": "London Luton",
                    "dreamlinerLogo": false,
                    "outbounds": [
                        {
                            "departureAirport": "London Gatwick",
                            "departureAirportCode": "LTN",
                            "schedule": {
                                "departureDate": "11-06-2013"
                            }
                        }
                    ]
                },
                "packageId": "Q026787"
            }
        ];

        var groups = service.group(holidays,'MULTIPLEDATESSINGLEAIRPORT')['byDates'];
        expect(groups.length).toEqual(2);

        var june11Group = _.filter(groups, function (group) {
            return group.key === '11-06-2013';
        })[0];

        var june6Group = _.filter(groups, function (group) {
            return group.key === '06-06-2013';
        })[0];

        expect(june11Group.members.length).toEqual(2);
        expect(june6Group.members.length).toEqual(1);
    });

    it("should group holidays by departure Airports", function () {
        var holidays = [
            {
                "itinerary": {
                    "departureDate": "11-06-2013",
                    "outbounds": [
                        {
                            "departureAirport": "London Gatwick",
                            "departureAirportCode": "LGW",
                            "schedule": {
                                "departureDate": "11-06-2013"
                            }
                        }
                    ]
                },
                "packageId": "Q007662",
                "duration": 7
            },
            {
                "itinerary": {
                    "departureDate": "06-06-2013",
                    "outbounds": [
                        {
                            "departureAirport": "London Gatwick",
                            "departureAirportCode": "LGW",
                            "schedule": {
                                "departureDate": "06-06-2013"
                            }
                        }
                    ]
                },
                "packageId": "Q020233"
            },
            {
                "itinerary": {
                    "departureDate": "11-06-2013",
                    "departureAirport": "London Luton",
                    "dreamlinerLogo": false,
                    "outbounds": [
                        {
                            "departureAirport": "London Gatwick",
                            "departureAirportCode": "LTN",
                            "schedule": {
                                "departureDate": "11-06-2013"
                            }
                        }
                    ]
                },
                "packageId": "Q026787"
            }
        ];

        var groups = service.group(holidays, 'SINGLEDATEMULTIPLEAIRPORTS')['byAirports'];
        expect(groups.length).toEqual(2);

        var gatWickGroup = _.filter(groups, function (group) {
            return group.key === 'LGW';
        })[0];

        expect(gatWickGroup.members.length).toEqual(2);
    });

    it("should group holidays by departure dates & departure Airports", function () {
        var holidays = [
            {
                "price": {
                  "perPerson" : "572.0",
                  "totalParty" : "1144.0"
                },
                "itinerary": {
                    "departureDate": "11-06-2013",
                    "outbounds": [
                        {
                            "departureAirport": "London Gatwick",
                            "departureAirportCode": "LGW",
                            "schedule": {
                                "departureDate": "11-06-2013"
                            }
                        }
                    ]
                },
                "packageId": "Q007662",
                "duration": 7
            },
            {
                "price": {
                  "perPerson" : "482.0",
                  "totalParty" : "964.0"
                },
                "itinerary": {
                    "departureDate": "06-06-2013",
                    "outbounds": [
                        {
                            "departureAirport": "London Gatwick",
                            "departureAirportCode": "LGW",
                            "schedule": {
                                "departureDate": "06-06-2013"
                            }
                        }
                    ]
                },
                "packageId": "Q020233"
            },
            {
              "price": {
                "perPerson" : "435.0",
                "totalParty" : "770.0"
              },
                "itinerary": {
                    "departureDate": "06-06-2013",
                    "departureAirport": "London Luton",
                    "dreamlinerLogo": false,
                    "outbounds": [
                        {
                            "departureAirport": "London Luton",
                            "departureAirportCode": "LTN",
                            "schedule": {
                                "departureDate": "06-06-2013"
                            }
                        }
                    ]
                },
                "packageId": "Q026787"
            }
        ];

        var groups = service.group(holidays, 'MULTIPLEDATESAIRPORTS');
        var groupsByAirport = groups['byAirports'];
        var groupsByDates = groups['byDates'];
        expect(groupsByAirport.length).toEqual(2);
        expect(groupsByDates.length).toEqual(2);

        var gatWickGroup = _.filter(groupsByAirport, function (group) {
            return group.key === 'LGW';
        })[0];

        var lutonGroup = _.filter(groupsByAirport, function (group) {
            return group.key === 'LTN';
        })[0];

        var june11Group = _.filter(groupsByDates, function(group) {
            return group.key === '11-06-2013';
        })[0];

        var june6Group = _.filter(groupsByDates, function(group) {
            return group.key === '06-06-2013';
        })[0];

        expect(gatWickGroup.members.length).toEqual(2);
        expect(lutonGroup.members.length).toEqual(1);

        expect(june11Group.members.length).toEqual(1);
        expect(june6Group.members.length).toEqual(2);
    });
});