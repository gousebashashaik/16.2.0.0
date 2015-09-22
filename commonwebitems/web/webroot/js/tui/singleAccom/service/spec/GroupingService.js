define('tui/singleAccom/service/spec/GroupingService', [
  'tui/singleAccom/service/GroupingService'
], function(groupingService) {

  describe('Holiday grouping service', function() {
    it('should expose an object', function() {
      expect(typeof groupingService).toEqual('object');
    });

    it('should group holidays by departure dates', function() {
      var holidays = [
        {
          'itinerary': {
            'departureDate': '11-06-2013',
            'outbounds': [
              {
                'departureAirport': 'London Gatwick',
                'departureAirportCode': 'LGW',
                'schedule': {
                  'departureDate': '11-06-2013'
                }
              }
            ]
          },
          'packageId': 'Q007662',
          'duration': 7
        },
        {
          'itinerary': {
            'departureDate': '06-06-2013',
            'outbounds': [
              {
                'departureAirport': 'London Gatwick',
                'departureAirportCode': 'LGW',
                'schedule': {
                  'departureDate': '06-06-2013'
                }
              }
            ]
          },
          'packageId': 'Q020233'
        },
        {
          'itinerary': {
            'departureDate': '11-06-2013',
            'departureAirport': 'London Luton',
            'dreamlinerLogo': false,
            'outbounds': [
              {
                'departureAirport': 'London Gatwick',
                'departureAirportCode': 'LTN',
                'schedule': {
                  'departureDate': '11-06-2013'
                }
              }
            ]
          },
          'packageId': 'Q026787'
        }
      ];

      var groups = groupingService.byDepartureDates(holidays)['byDates'];
      expect(groups.length).toEqual(2);

      var june11Group = _.filter(groups, function(group) {
        return group.key === '11-06-2013';
      })[0];

      var june6Group = _.filter(groups, function(group) {
        return group.key === '06-06-2013';
      })[0];

      expect(june11Group.members.length).toEqual(2);
      expect(june6Group.members.length).toEqual(1);
    });

    it('should group holidays by departure Airports', function() {
      var holidays = [
        {
          'itinerary': {
            'departureDate': '11-06-2013',
            'outbounds': [
              {
                'departureAirport': 'London Gatwick',
                'departureAirportCode': 'LGW',
                'schedule': {
                  'departureDate': '11-06-2013'
                }
              }
            ]
          },
          'packageId': 'Q007662',
          'duration': 7
        },
        {
          'itinerary': {
            'departureDate': '06-06-2013',
            'outbounds': [
              {
                'departureAirport': 'London Gatwick',
                'departureAirportCode': 'LGW',
                'schedule': {
                  'departureDate': '06-06-2013'
                }
              }
            ]
          },
          'packageId': 'Q020233'
        },
        {
          'itinerary': {
            'departureDate': '11-06-2013',
            'departureAirport': 'London Luton',
            'dreamlinerLogo': false,
            'outbounds': [
              {
                'departureAirport': 'London Gatwick',
                'departureAirportCode': 'LTN',
                'schedule': {
                  'departureDate': '11-06-2013'
                }
              }
            ]
          },
          'packageId': 'Q026787'
        }
      ];

      var groups = groupingService.byDepartureAirports(holidays)['byAirports'];
      expect(groups.length).toEqual(2);

      var gatWickGroup = _.filter(groups, function(group) {
        return group.key === 'LGW';
      })[0];

      expect(gatWickGroup.members.length).toEqual(2);
    });

    it('should group holidays by departure dates & departure airports', function() {
      var holidays = [
        {
          'itinerary': {
            'departureDate': '11-06-2013',
            'outbounds': [
              {
                'departureAirport': 'London Gatwick',
                'departureAirportCode': 'LGW',
                'schedule': {
                  'departureDate': '11-06-2013'
                }
              }
            ]
          },
          'packageId': 'Q007662',
          'duration': 7
        },
        {
          'itinerary': {
            'departureDate': '06-06-2013',
            'outbounds': [
              {
                'departureAirport': 'London Gatwick',
                'departureAirportCode': 'LGW',
                'schedule': {
                  'departureDate': '06-06-2013'
                }
              }
            ]
          },
          'packageId': 'Q020233'
        },
        {
          'itinerary': {
            'departureDate': '06-06-2013',
            'departureAirport': 'London Luton',
            'dreamlinerLogo': false,
            'outbounds': [
              {
                'departureAirport': 'London Luton',
                'departureAirportCode': 'LTN',
                'schedule': {
                  'departureDate': '06-06-2013'
                }
              }
            ]
          },
          'packageId': 'Q026787'
        }
      ];

      var groups = groupingService.byAirportAndDates(holidays);
      var groupsByAirport = groups['byAirports'];
      var groupsByDates = groups['byDates'];
      expect(groupsByAirport.length).toEqual(2);
      expect(groupsByDates.length).toEqual(2);

      var gatWickGroup = _.filter(groupsByAirport, function(group) {
        return group.key === 'LGW';
      })[0];

      var lutonGroup = _.filter(groupsByAirport, function(group) {
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
});
