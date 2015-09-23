define("tui/searchPanel/view/flights/AirportListGrouping", [
	"dojo",
	"dojo/io-query",
	"tui/search/store/SearchPanelMemory"
	], function (dojo, ioQuery) {

	dojo.declare("tui.searchPanel.view.flights.AirportListGrouping", [tui.search.store.SearchPanelMemory], {

		overseasairportList:[],

		ukairportList : [],



		airportMap : function(responseData){
			var airportMap= this,
				airportSwapList=[];

		        airportSwapList = airportMap.getAirportSwapList(responseData);

		        airportMap.overseasairportList = airportMap.getOverseasairport(airportSwapList);

		        airportMap.ukairportList = airportMap.getUkAirports(airportSwapList);

		        airportMap.searchPanelAirportlist(airportMap.ukairportList , airportMap.overseasairportList);

		    return airportMap;
		},


		getAirportSwapList : function(responseData){
			var airportMap= this;
				airportsListKeys = Object.keys(responseData),
				airportSwapList=[],
				airportTempList=[];

	        _.each(airportsListKeys, function(countryKey){
	        		airportTempList = responseData[countryKey];
		    		_.each(airportTempList, function(airport){
		        			airportSwapList.push(airport);
		    		});
	        });
	        return airportSwapList;
		},

		getOverseasairport : function(airportList){
			var airportMap= this,
				overseasairportList=[];

				_.each(airportList, function(airport){
					if(airport.countryCode !== "GBR"){
				    		overseasairportList.push(airport);
				    	}
				    });

					overseasairportList = airportMap.setAirportGroupFlag(overseasairportList);
					overseasairportList = airportMap.sortByName(overseasairportList);

			return overseasairportList;
		},

		getUkAirports : function(airportList){
			var airportMap= this,
				UkairportList=[];

				_.each(airportList, function(airport,i){
					if(airport.countryCode == "GBR"){
						if(airport.group.length < 1){
							airport.group[0] ='OT';
						}
							UkairportList.push(airport);
						}
					});

				return UkairportList;
		},


		setUkAirportsGroupCodes : function(airportList){
				var airportMap= this,
					UkairportList=[];

			 _.each(airportList, function(airport,i){
                	if(airport.countryCode == "GBR"){

                			airportList[i].group = [];
                    		if(airport.id === 'LGW'){
                    			airportList[i].group[0] ='LN';
                    		}
                    		if(airport.id === 'LTN'){
                    			airportList[i].group[0] ='LN';
                    		}
                    		if(airport.id === 'STN'){
                    			airportList[i].group[0] ='LN';
                    		}

                    		if(airport.id === 'SEN'){
                    			airportList[i].group[0] ='SE';
                			}
                    		if(airport.id === 'NWI'){
                    			airportList[i].group[0] ='SE';
                			}
                    		if(airport.id === 'SOU'){
                    			airportList[i].group[0] ='SE';
                			}

                    		if(airport.id === 'BOH'){
                    			airportList[i].group[0] ='SW';
                			}
                    		if(airport.id === 'BRS'){
                    			airportList[i].group[0] ='SW';
                			}
                    		if(airport.id === 'CWL'){
                    			airportList[i].group[0] ='SW';
                			}
                    		if(airport.id === 'EXT'){
                    			airportList[i].group[0] ='SW';
                			}

                    		if(airport.id === 'BHX'){
                    			airportList[i].group[0] ='MD';
                			}
                    		if(airport.id === 'EMA'){
                    			airportList[i].group[0] ='MD';
                			}

                    		if(airport.id === 'DSA'){
                    			airportList[i].group[0] ='NE';
                			}
                    		if(airport.id === 'NME'){
                    			airportList[i].group[0] ='NE';
                			}
                    		if(airport.id === 'HUY'){
                    			airportList[i].group[0] ='NE';
                			}
                    		if(airport.id === 'LBA'){
                    			airportList[i].group[0] ='NE';
                			}
                    		if(airport.id === 'NCL'){
                    			airportList[i].group[0] ='NE';
                			}

                    		if(airport.id === 'MAN'){
                    			airportList[i].group[0] ='NW';
                			}

                    		if(airport.id === 'ABZ'){
                    			airportList[i].group[0] ='SC';
                			}
                    		if(airport.id === 'EDI'){
                    			airportList[i].group[0] ='SC';
                			}
                    		if(airport.id === 'GLA'){
                    			airportList[i].group[0] ='SC';
                			}

                    		if(airport.id === 'BFS'){
                    			airportList[i].group[0]  ='NI';
                			}

	                		if(airport.id === 'BHD'){
	                			airportList[i].group[0] ='NI';
	            			}
	                		if(airport.id === 'LDY'){
	                			airportList[i].group[0] ='NI';
	            			}
	                		if(airport.id === 'LPL'){
	                			airportList[i].group[0] ='NW';
	            			}

	                		UkairportList.push(airport);
                	}
                });

             return UkairportList;
		},

		setUKAiportGroupNames : function(airportList){
			var airportMap= this,
				UkairportList=[],
				counrtyGP=['LN','SE','SW','MD','NE','NW','SC','NI','OT'],
				counrtyGPNames=['London','South East','South West', 'Midlands','North East', 'North West' , 'Scotland', 'Northern Ireland','Others'];

			  _.each(counrtyGP, function(tempGP, i){
	             	_.each(airportList, function(airport ,j){
	             		if(airport.group[0] === tempGP){
	               			airport.countryName = counrtyGPNames[i];
	               			UkairportList.push(airport);
	             		}
	             	});
	             });

			  UkairportList = airportMap.setAirportGroupFlag(UkairportList);

			  return UkairportList
			},


		setAirportGroupFlag : function(airportList){
			var airportMap= this,
				airportSwapList=[],
				checkCName = "";

    		_.each(airportList, function(airport){
    			if(airport.countryName == checkCName){
        			airport.cFlag = false;
        		}else{
        			airport.cFlag = true;
        		}
        		checkCName = airport.countryName;
        		airportSwapList.push(airport);
    		});

    		return airportSwapList;
		},


		searchPanelAirportlist : function(ukairportList , overseasairportList){
			var airportMap= this,
				tempUKAiportList = [],
				tempOverseasAirportList=[];

				ukairportList = airportMap.setUKAiportGroupNames(ukairportList);
				ukairportList = airportMap.sortByName(ukairportList);

				_.each(ukairportList, function(airport ,i){
					airport.nextCol = false;
					if(airport.id =="BFS" || airport.id =="SOU"){
						airport.nextCol = true;
					}
					tempUKAiportList.push(airport);
				});

				airportMap.ukairportList = tempUKAiportList;


				_.each(overseasairportList, function(airport){
						airport.nextCol = false;
						if(airport.id =="RVN" || airport.id =="MRU"){
								airport.nextCol = true;
							}
							tempOverseasAirportList.push(airport);
				    });

				airportMap.airportList = tempOverseasAirportList;

		},


		getDealsUKAiportList : function(airportList){
			var airportMap= this;
				dealsUKAiportList = [];

				airportList = airportMap.getAirportSwapList(airportList);
				airportList = airportMap.setUkAirportsGroupCodes(airportList);
				airportList = airportMap.setUKAiportGroupNames(airportList);

            	_.each(airportList, function(airport ,i){
              			airport.nextCol = false;
              			airport.ids = airport.id + "-deals"
              			airport.countryNameId = airport.countryName + "-deals";
              			if(airport.id =="EXT" || airport.id =="MAN"){
              				airport.nextCol = true;
              			}
              			if(airport.countryName == "NORTH WEST" || airport.countryName == "NORTHERN IRELAND"){
              				airport.showAny = false;
              			} else {
              				airport.showAny = true;
              			}
              			dealsUKAiportList.push(airport);
            	});

            	return dealsUKAiportList;
		},


		getDealsOverseasAirportList : function(airportList){
			var airportMap= this,
				dealsOverseasAirportList=[];

				airportList = airportMap.getAirportSwapList(airportList);
				airportList = airportMap.getOverseasairport(airportList);


			_.each(airportList, function(airport ,i){
				airport.nextCol = false;
				airport.countryCodeLabel = airport.countryCode + "-deals"
        		airport.ids = airport.id + "-deals"

        		if(airport.id =="RVN" || airport.id =="MRU"){
    				airport.nextCol = true;
    			}
        		dealsOverseasAirportList.push(airport);
			});

			return dealsOverseasAirportList;

		},


		getGroupedOverseasAirport : function(airportList){
			var airportMap= this,
				overseasairportList=[],
				countries=[],
				allOverseasAirports=[],
				allOverseasCountries=[];;

				airportList = airportMap.getAirportSwapList(airportList);

				overseasairportList = airportMap.getOverseasairport(airportList);

		        countries = airportMap.getCountryNames(overseasairportList);

		        allOverseasAirports = airportMap.getAllAirports(null , overseasairportList );

		        allOverseasCountries = airportMap.setAirportMap(allOverseasAirports ,countries);

		        return allOverseasCountries;
		},


		getAvailableGroupedOverseasAirport : function(airportList){
			var airportMap= this,
				overseasairportList=[],
				countries=[],
				allOverseasAirports=[],
				allOverseasCountries=[];;

				airportList = airportMap.getAirportSwapList(airportList);

				airportList = airportMap.getAvailableAirport(airportList);

				overseasairportList = airportMap.getOverseasairport(airportList);

		        countries = airportMap.getCountryNames(overseasairportList);

		        allOverseasAirports = airportMap.getAllAirports(null , overseasairportList );

		        allOverseasCountries = airportMap.setAirportMap(allOverseasAirports ,countries);

		        return allOverseasCountries;
		},

		getGroupedUkAirport : function(airportList){
			var airportMap= this,
				airportSwapList=[],
				ukairportList=[],
				overseasairportList=[],
				countryName="",
				countries=[],
				allUKAirports=[],
				allCountries=[];;

				airportList = airportMap.getAirportSwapList(airportList);

		        ukairportList = airportMap.getUkAirports(airportList);

		        ukairportList = airportMap.setUkAirportsGroupCodes(ukairportList);

		        ukairportList = airportMap.setUKAiportGroupNames(ukairportList);

		        countries = airportMap.getCountryNames(ukairportList);

		        allUKAirports = airportMap.getAllAirports(ukairportList, null);

		        allUKCountries = airportMap.setAirportMap(allUKAirports ,countries);

		        return allUKCountries;

		},


		getAvailableGroupedUKAirport : function(airportList){
			var airportMap= this,
				airportSwapList=[],
				ukairportList=[],
				overseasairportList=[],
				countryName="",
				countries=[],
				allUKAirports=[],
				allCountries=[];;

				airportList = airportMap.getAirportSwapList(airportList);

		        ukairportList = airportMap.getUkAirports(airportList);

		        ukairportList = airportMap.getAvailableAirport(ukairportList);

		        ukairportList = airportMap.setUkAirportsGroupCodes(ukairportList);

		        ukairportList = airportMap.setUKAiportGroupNames(ukairportList);

		        countries = airportMap.getCountryNames(ukairportList);

		        allUKAirports = airportMap.getAllAirports(ukairportList, null);

		        allUKCountries = airportMap.setAirportMap(allUKAirports ,countries);

		        return allUKCountries;

		},

		airportGroupMapping : function(airportList){
			var airportMap= this,
				airportSwapList=[],
				ukairportList=[],
				overseasairportList=[],
				countryName="",
				countries=[],
				allAirports=[],
				allCountries=[];;

				airportList = airportMap.getAirportSwapList(airportList);

				overseasairportList = airportMap.getOverseasairport(airportList);

		        ukairportList = airportMap.getUkAirports(airportList);

		        ukairportList = airportMap.setUkAirportsGroupCodes(ukairportList);

		        ukairportList = airportMap.setUKAiportGroupNames(ukairportList);

		        countries = airportMap.getCountryNames(ukairportList);

		        countries = airportMap.getCountryNames(overseasairportList);

		        allAirports = airportMap.getAllAirports(ukairportList , overseasairportList);

		        allCountries = airportMap.setAirportMap(allAirports ,countries);

		        return allCountries;

		},

		getCountryNames : function(airportList  ){
			var airportMap= this,
				countries=[];

			_.each(airportList, function(airport ,i){
	        		countryName = airport.countryName;
        			data = {
        					"countryName" : countryName,
        					"airports":[]
        			};
        			countries.push(data);
	        });

			countries = 	_.uniq(countries,"countryName");
			return countries;
		},

		getAllAirports : function(ukairportList , overseasairportList){
			var airportMap= this,
				allAirports=[];

			if(ukairportList){
				_.each(ukairportList, function(airport ,i){
					if(airport.available){
						allAirports.push(airport);
					}

		        });
			}

			if(overseasairportList){
				_.each(overseasairportList, function(airport ,i){
					if(airport.available){
						allAirports.push(airport);
					}
				});
			}

			return allAirports;
		},

		setAirportMap : function(allAirports ,countries){
			var airportMap= this;

			  _.each(countries, function(country, i){
					_.each(allAirports, function(airport, j){
						if(airport.countryName === country.countryName ){
							countries[i].airports.push(airport);
						}
					})
				});

			  return countries;

		},

		sortByName :  function(array){
		    return _.sortBy(array, "countryName");
		},

		getAllContinetDetails: function(){
			var airportMap= this,
				continent;
				 return continent={
		        			"name"		:	"All Regions",
		        			"longitude"	:	-12.0,
		        			"latitude"	:	10.0,
		        			"code"		:	"allregions"
		        		}
		},

		getContinentDetails : function(responseData){
			var airportMap= this,
				continentTempList=[],
				continentList=[],
				continent="",
				continentListKeys = Object.keys(responseData);


			_.each(continentListKeys, function(continent,i){
					continentTempList = responseData[continent];
					continent={
	        			"name"		:	continentTempList.name,
	        			"longitude"	:	continentTempList.longitude,
	        			"latitude"	:	continentTempList.latitude,
	        			"code"		:	continentTempList.code

					}
        		continentList.push(continent);
        	});

			continentList =	_.sortBy(continentList, function(o) { return o.name; });

			//All Regions Continet for map
			continentList.unshift(airportMap.getAllContinetDetails());
        	return continentList;
		},

		getAllContinents : function(responseData){
			var airportMap= this,
				continentTempList=[],
				continentList=[],
				airportData=[],
				countries=[],
				allCountries=[],
				continentListKeys = Object.keys(responseData);

				continentList.push(airportMap.getAllContinetDetails());

			 _.each(continentListKeys, function(continent){
				 	continentTempList = responseData[continent];
				 	continentTempList.countries = airportMap.getCountries(continentTempList.countries);
				 	continentTempList.countries = airportMap.checkCountryAvailable(continentTempList.countries);
				 	continentList.push(continentTempList);
			 	});

			 _.each(continentList, function(continent){
				 _.each(continent.countries, function(countries){
				 		allCountries.push(countries);
				 	});
				 	continent.countries = _.sortBy(continent.countries, function(o) { return o.name; });
			 	});

			 	allCountries = _.sortBy(allCountries, function(o) { return o.name;});
			 	continentList[0].countries =allCountries;
			 	return continentList;
		},

		getCountries : function(responseData){
			var airportMap= this;
				airportsListKeys = Object.keys(responseData),
				airportSwapList=[],
				airportTempList=[];

				_.each(airportsListKeys, function(countryKey){
						airportSwapList = responseData[countryKey];
						airportSwapList.airports = airportMap.getAirports(airportSwapList.airports);
						airportTempList.push(airportSwapList);
				});

				return airportTempList;
		},

		getAirports : function(responseData){
			var airportMap= this;
				airportsListKeys = Object.keys(responseData),
				airportList=[];

				_.each(airportsListKeys, function(airports){
					airportList.push(responseData[airports]);
				});

				return airportList;

		},

		checkCountryAvailable : function(countries){
			var airportMap= this,count;

			_.each(countries, function(country){
					count =0;
				_.each(country.airports, function(airport){
						if(airport.available){
							count++;
						}
				});
				if(count>0){
					country.available=true;
				}else{
					country.available=false;
				}

			});
			return countries

		},

		getMapCountryNames : function(airportList  ){
			var airportMap= this,
				countries=[];

				_.each(airportList, function(airport){
	        		countryName = airport.countryName;
	        			data = {
	        					"countryName" : countryName,
	        					"airports":[]
	        			};
	        			countries.push(data);
				});

				countries = _.uniq(countries,"countryName");
				return countries;
		},

		setGoogleMapAirportMap : function(allAirports ,countries){
			var airportMap= this;

			  _.each(countries, function(country, i){
					_.each(allAirports, function(airport, j){
						if(airport.country.countryName === country.countryName ){
							countries[i].airports.push(airport);
						}
					})
				});

			  return countries;

		},
		getMapOverseasAirports : function(airportList){
			var airportMap= this,
				overseasairportList=[];

			_.each(airportList, function(airport){
				if(airport.country.countryCode !== "GBR"){
			    		overseasairportList.push(airport);
			    	}
			  });

			return overseasairportList;
		},

		getAvailableAirport : function(airports){
			var airportMap= this,airportList=[];

			_.each(airports, function(airport){
					if(airport.available){
						airportList.push(airport);
					}
				})

				return airportList;

		}


		});

	return tui.searchPanel.view.flights.AirportListGrouping;
});