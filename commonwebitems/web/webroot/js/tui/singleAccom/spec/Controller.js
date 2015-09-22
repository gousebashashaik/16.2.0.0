define('tui/singleAccom/spec/Controller', [
  'tui/singleAccom/Controller'
], function(controller) {

  xdescribe('Single accommodation', function() {
    var controller;

    beforeEach(function() {
      controller = new controller();
    });

    it('should expose a function', function() {
      expect(typeof controller).toEqual('function');
    });

    it('should get all holidays', function() {
      expect(_.difference(controller.holidays().all(), searchResponse.searchResult.holidays)).toEqual([]);
    });

    it('should group holidays by departure dates', function() {
      var groups = controller.holidays().byDepartureDates();
      expect(groups.length).toEqual(2);

      var july11Group = _.filter(groups, function(group) {
        return group.key() === '11-06-2013';
      })[0];

      expect(july11Group.members().length).toEqual(2);
    });

    it('should group holidays by departure Airports', function() {
      var groups = controller.holidays().byDepartureAirports();
      expect(groups.length).toEqual(2);

      var gatWickGroup = _.filter(groups, function(group) {
        return group.key() === 'LGW';
      })[0];

      expect(gatWickGroup.members().length).toEqual(2);
    });
  });

  var searchResponse = {
    searchResult: {
      holidays: [
        {
          'accommodation': {
            'code': '009765',
            'name': 'Hotel Flamingo Oasis',
            'imageUrl': 'http://newmedia.thomson.co.uk/live/vol/0/05b04f3da1644c57a130227569812325da400645/232x130/web/EUROPE___MEDITERRANEAN_SPAIN_CON_ESP__COSTA_BLANCA_BENIDORM_RES_000349__FLAMINGO_OASIS.jpg',
            'differentiatedProduct': false,
            'location': {
              'destination': {
                'code': '000347',
                'name': 'Costa Blanca'
              },
              'resort': {
                'code': '000349',
                'name': 'Benidorm'
              }
            },
            'ratings': {
              'officialRating': '4',
              'tripAdvisorRating': ''
            },
            'rooms': [
              {
                'roomType': 'Double room',
                'roomCode': 'JU173',
                'sellingout': 5,
                'availability': false,
                'boardType': 'All Inclusive',
                'price': {

                },
                'occupancy': {
                  'adults': 2,
                  'children': 0,
                  'infant': 0
                }
              }
            ],
            'tripAdvisorReviewCount': 0,
            'commercialPriority': 0,
            'featureCodesAndValues': {
              'usps': [
                'Close to the beach',
                'Refurbished rooms',
                'Evening entertainment'
              ]
            },
            'url': '/holiday/bookaccommodation/overview/Benidorm/Hotel-Flamingo-Oasis-009765?productCode=009765&noOfAdults=2&noOfChildren=0&childrenAge=&duration=0&flexibleDays=7&airports[]=LGW&flexibility=true&noOfSeniors=0&when=12-06-2013&units[]=ESP:COUNTRY&packageId=Q007662'
          },
          'price': {
            'perPerson': '611.0',
            'totalParty': '1222.0',
            'discount': '90.0',
            'depositAmount': '1222.0',
            'lowDepositExists': false
          },
          'itinerary': {
            'departureDate': '11-06-2013',
            'departureAirport': 'London Gatwick',
            'dreamlinerLogo': false,
            'outbounds': [
              {
                'departureAirport': 'London Gatwick',
                'departureAirportCode': 'LGW',
                'arrivalAirport': 'Alicante',
                'arrivalAirportCode': 'ALC',
                'dreamLinerIndicator': false,
                'haulType': 'SH',
                'schedule': {
                  'departureDate': '11-06-2013',
                  'departureTime': '1750',
                  'departureDateTimeInMilli': 1370905200000,
                  'arrivalDate': '11-06-2013',
                  'arrivalTime': '2120',
                  'arrivalDateTimeInMilli': 1370905200000,
                  'overlapDay': false
                },
                'carrier': {
                }
              }
            ],
            'inbounds': [
              {
                'departureAirport': 'Alicante',
                'departureAirportCode': 'ALC',
                'arrivalAirport': 'London Gatwick',
                'arrivalAirportCode': 'LGW',
                'dreamLinerIndicator': false,
                'haulType': 'SH',
                'schedule': {
                  'departureDate': '18-06-2013',
                  'departureTime': '2225',
                  'departureDateTimeInMilli': 1371510000000,
                  'arrivalDate': '19-06-2013',
                  'arrivalTime': '0005',
                  'arrivalDateTimeInMilli': 1371596400000,
                  'overlapDay': true
                },
                'carrier': {
                }
              }
            ]
          },
          'packageId': 'Q007662',
          'tracsUnitCode': 'Q007662-QFF',
          'coachTransfer': false,
          'duration': 7,
          'productCode': 'Q',
          'subProductCode': 'F',
          'index': 1
        },
        {
          'accommodation': {
            'code': '027942',
            'name': 'Holiday Village Costa del Sol',
            'imageUrl': 'http://newmedia.thomson.co.uk/live/vol/0/1be3458d46df81b0bd95c2b546757933a6030ea7/232x130/web/EUROPE___MEDITERRANEAN_SPAIN_CON_ESP__COSTA_DEL_SOL_DES_000365__BENALMADENA_POLYNESIA_HOTEL_ACC_033828_.jpg',
            'differentiatedProduct': true,
            'differentiatedType': 'Holiday Village',
            'differentiatedCode': 'FHV',
            'location': {
              'destination': {
                'code': '000365',
                'name': 'Costa del Sol'
              },
              'resort': {
                'code': '000366',
                'name': 'Benalmadena'
              }
            },
            'ratings': {
              'officialRating': '4',
              'tripAdvisorRating': ''
            },
            'rooms': [
              {
                'roomType': '1-bedroom apartment',
                'roomCode': 'XB175',
                'sellingout': 43,
                'availability': false,
                'boardType': 'All Inclusive',
                'price': {
                },
                'occupancy': {
                  'adults': 2,
                  'children': 0,
                  'infant': 0
                }
              }
            ],
            'tripAdvisorReviewCount': 0,
            'commercialPriority': 0,
            'featureCodesAndValues': {
              'usps': [
                'Huge pool with pirate ship',
                'Beach club with wave pool',
                'Select apartments available'
              ],
              'strapline': [
                'The ultimate family holiday'
              ]
            },
            'url': '/holiday/bookaccommodation/overview/Benalmadena/Holiday-Village-Costa-del-Sol-027942?productCode=027942&noOfAdults=2&noOfChildren=0&childrenAge=&duration=0&flexibleDays=7&airports[]=LGW&flexibility=true&noOfSeniors=0&when=12-06-2013&units[]=ESP:COUNTRY&packageId=Q020233'
          },
          'price': {
            'perPerson': '743.0',
            'totalParty': '1486.0',
            'discount': '112.0',
            'depositAmount': '1486.0',
            'lowDepositExists': false
          },
          'itinerary': {
            'departureDate': '06-06-2013',
            'departureAirport': 'London Gatwick',
            'dreamlinerLogo': false,
            'outbounds': [
              {
                'departureAirport': 'London Gatwick',
                'departureAirportCode': 'LGW',
                'arrivalAirport': 'Malaga Pablo Ruiz Picasso',
                'arrivalAirportCode': 'AGP',
                'dreamLinerIndicator': false,
                'haulType': 'SH',
                'schedule': {
                  'departureDate': '06-06-2013',
                  'departureTime': '0600',
                  'departureDateTimeInMilli': 1370473200000,
                  'arrivalDate': '06-06-2013',
                  'arrivalTime': '0950',
                  'arrivalDateTimeInMilli': 1370473200000,
                  'overlapDay': false
                },
                'carrier': {
                }
              }
            ],
            'inbounds': [
              {
                'departureAirport': 'Malaga Pablo Ruiz Picasso',
                'departureAirportCode': 'AGP',
                'arrivalAirport': 'London Gatwick',
                'arrivalAirportCode': 'LGW',
                'dreamLinerIndicator': false,
                'haulType': 'SH',
                'schedule': {
                  'departureDate': '13-06-2013',
                  'departureTime': '1055',
                  'departureDateTimeInMilli': 1371078000000,
                  'arrivalDate': '13-06-2013',
                  'arrivalTime': '1240',
                  'arrivalDateTimeInMilli': 1371078000000,
                  'overlapDay': false
                },
                'carrier': {
                }
              }
            ]
          },
          'packageId': 'Q020233',
          'tracsUnitCode': 'Q020233-Q33',
          'coachTransfer': false,
          'duration': 7,
          'productCode': 'Q',
          'subProductCode': '3',
          'index': 2
        },
        {
          'accommodation': {
            'code': '046925',
            'name': 'Holiday Village Majorca',
            'imageUrl': 'http://newmedia.thomson.co.uk/live/vol/0/3c51cdf842c3d6d8740919ba0fb59e6555b75ff7/232x130/web/EUROPE___MEDITERRANEAN_SPAIN_CON_ESP__MAJORCA_CALA_MILLOR_HOLIDAY_VILLAGE_MAJORCA.jpg',
            'differentiatedProduct': true,
            'differentiatedType': 'Holiday Village',
            'differentiatedCode': 'FHV',
            'location': {
              'destination': {
                'code': '000122',
                'name': 'Majorca'
              },
              'resort': {
                'code': '000263',
                'name': 'Cala Millor'
              }
            },
            'ratings': {
              'officialRating': '5',
              'tripAdvisorRating': ''
            },
            'rooms': [
              {
                'roomType': 'Twin room',
                'roomCode': 'AV175',
                'sellingout': 1,
                'availability': true,
                'boardType': 'All Inclusive',
                'price': {
                },
                'occupancy': {
                  'adults': 2,
                  'children': 0,
                  'infant': 0
                }
              }
            ],
            'tripAdvisorReviewCount': 0,
            'commercialPriority': 0,
            'featureCodesAndValues': {
              'usps': [
                'Swim-up rooms',
                '6 pools',
                'Themed restaurants'
              ],
              'strapline': [
                'The ultimate family holiday'
              ]
            },
            'url': '/holiday/bookaccommodation/overview/Cala-Millor/Holiday-Village-Majorca-046925?productCode=046925&noOfAdults=2&noOfChildren=0&childrenAge=&duration=0&flexibleDays=7&airports[]=LGW&flexibility=true&noOfSeniors=0&when=12-06-2013&units[]=ESP:COUNTRY&packageId=Q026787'
          },
          'price': {
            'perPerson': '873.0',
            'totalParty': '1746.0',
            'discount': '132.0',
            'depositAmount': '1746.0',
            'lowDepositExists': false
          },
          'itinerary': {
            'departureDate': '11-06-2013',
            'departureAirport': 'London Luton',
            'dreamlinerLogo': false,
            'outbounds': [
              {
                'departureAirport': 'London Gatwick',
                'departureAirportCode': 'LTN',
                'arrivalAirport': 'Palma de Mallorca',
                'arrivalAirportCode': 'PMI',
                'dreamLinerIndicator': false,
                'haulType': 'SH',
                'schedule': {
                  'departureDate': '11-06-2013',
                  'departureTime': '0615',
                  'departureDateTimeInMilli': 1370559600000,
                  'arrivalDate': '07-06-2013',
                  'arrivalTime': '0930',
                  'arrivalDateTimeInMilli': 1370559600000,
                  'overlapDay': false
                },
                'carrier': {
                }
              }
            ],
            'inbounds': [
              {
                'departureAirport': 'Palma de Mallorca',
                'departureAirportCode': 'PMI',
                'arrivalAirport': 'London Gatwick',
                'arrivalAirportCode': 'LGW',
                'dreamLinerIndicator': false,
                'haulType': 'SH',
                'schedule': {
                  'departureDate': '14-06-2013',
                  'departureTime': '1045',
                  'departureDateTimeInMilli': 1371164400000,
                  'arrivalDate': '14-06-2013',
                  'arrivalTime': '1210',
                  'arrivalDateTimeInMilli': 1371164400000,
                  'overlapDay': false
                },
                'carrier': {
                }
              }
            ]
          },
          'packageId': 'Q026787',
          'tracsUnitCode': 'Q026787-Q33',
          'coachTransfer': false,
          'duration': 7,
          'productCode': 'Q',
          'subProductCode': '3',
          'index': 3
        }
      ]
    }
  };
});
