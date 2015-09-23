define("tui/flights/status/FlightNumber",[
"dojo",
"dojo/_base/declare",
"dojo/has",
"dojo/on",
'dojo/query',
"dijit/registry",
"dojo/dom",
"dojo/_base/lang",
"dojo/text!tui/flights/status/templates/flightStatusResults.html",
"tui/widget/mixins/Templatable",
"tui/widget/_TuiBaseWidget",
"tui/search/nls/Searchi18nable"],function(dojo,declare,has,on,query,registry,dom,lang,flightStatusResultsTmpl,Templatable,
		  _TuiBaseWidget, Searchi18nable){
	declare("tui.flights.status.FlightNumber",[Templatable,_TuiBaseWidget,Searchi18nable],{
		/*****************properties**********************/
		today : "",
		tomday : "",
		/*****************methods**********************/
		postCreate: function(){
			var FlightNumber = this;
			console.log("flight number  postcreate loaded");
			FlightNumber.getDateformats();
			//FlightNumber.attachEvents();


		},
		attachEvents:function(){
			var FlightNumber = this;
			var flightNumberNode=dijit.byId("flightNumber").getLabel().replace(" ","")
		},
		searchTypes: function(targetId){
			var FlightNumber = this;
			var flvalue=dijit.byId("flightNumber").getLabel().replace(" ","");
			url="searchByFlightNumber/"+flvalue;
			FlightNumber.callJson(url,targetId);
		},
		//makes an Ajax call for results
    	callJson: function(url,fieldId){
    			var FlightNumber = this;
        		url= url,
        		fieldId=fieldId,
    			dojo.xhrGet({
    		        url: url,
    		        fieldId:fieldId,
    		        handleAs: "json",
    	            load: function (jsonData) {
    	            	FlightNumber.handleJsonObj(url, fieldId, jsonData);
    	            },
    	            error: function () {
    	            	console.log("error in ajax call");
    	            }
    			});
    		},
    	handleJsonObj:function(url, fieldId, obj){
    		var FlightNumber = this;
    		FlightNumber.call(url, fieldId, obj);
    	},
    	call: function (url, fieldId, jsonData) {
		   var FlightNumber = this;
    		flvalue = dijit.byId("flightNumber").getLabel().replace(" ","");
		    var flightNumberSpaced;
		   /* if (flvalue.indexOf("TOM") == -1) {
		        flvalue = "TOM" + flvalue;
		    }*/

		    FlightNumber.afterResults(url, fieldId, jsonData);
		 },
		 //converts the response data into required format if needed
		 formatFlightData : function(Data, fieldId){
			 var FlightNumber=this, isDepartureTimeBold = false, isArrivalTimeBold = false,flightStatus="",flightStatusInfo = "";
			 isDepartureTimeBold = (fieldId == "allDeps" || fieldId == "flyFromStat" || fieldId == "flightNumber") ? true : false;
			 isArrivalTimeBold = (fieldId == "allArvs" || fieldId == "flyToStat") ? true : false;

			 return _.map(lang.clone(Data), function (flight, index) {

					flight.flightnr = flight.flightnr.split('TOM').join('TOM ');
					flight.schdeptm = FlightNumber.addColons(flight.schdeptm);
					flight.scharrtm = FlightNumber.addColons(flight.scharrtm);
					flight.expdeptm = FlightNumber.addColons(flight.expdeptm);
					flight.exparrtm = FlightNumber.addColons(flight.exparrtm);
					if(fieldId == "allArvs" || fieldId == "flyToStat"){
						flightStatusInfo=FlightNumber.getFlightStatus(flight.arrstatus);
						flightStatus = FlightNumber.initCapString(flight.arrstatus);
					}else if(fieldId == "allDeps" || fieldId == "flyFromStat" || fieldId == "flightNumber"){
						flightStatusInfo=FlightNumber.getFlightStatus(flight.depstatus);
						flightStatus = FlightNumber.initCapString(flight.depstatus);
					}
					flight.imgClass = flightStatusInfo.imgClass;
					flight.statusColor = flightStatusInfo.statusColor;
					flight.showEstimatedDepTime = flightStatusInfo.showEstimatedDepTime;
					flight.showEstimatedArrTime = flightStatusInfo.showEstimatedArrTime;
					flight.flightStatus = flightStatus;
					flight.isDepartureTimeBold = isDepartureTimeBold;
					flight.isArrivalTimeBold = isArrivalTimeBold;
					return flight;

				});

		 },
		 //generate the today and tommorrow date in to yymmdd format
		 getDateformats :  function(){
			var FlightNumber=this, today,tomday;
				today = dojo.date.locale.format(new Date(), {
						selector: "date",
						datePattern: "yyMMdd"
		        	});
				FlightNumber.today = parseInt(today);


				tomday = dojo.date.locale.format(new Date(new Date().getTime() + 86400000), {
					selector: "date",
					datePattern: "yyMMdd"
	        	});
				FlightNumber.tomday = parseInt(tomday);
		},
		//convert the date obj in to WEEKDAY DATETH MONTH YEAR
		flightTitleObject : function(dt){
			var FlightNumber=this,dateObj, flightTitle,splitData,
				options = { weekday: "long", year: "numeric", month: "long",  day: "numeric" };

				dateObj = dt.toLocaleDateString("en-us", options);
				splitData = dateObj.replace(/[,]/gi,'');
				splitData = splitData.split(" ");

				flightTitle = splitData[0] +" "+ splitData[2] +" "+ splitData[1] +" "+ splitData[3];

				return flightTitle.toUpperCase();
		},
		//determines class name based on flight status
		getFlightStatus : function(flightStatus){
			var FlightNumber=this,
				imgClass = "",
				statusColor = "displayGreen",
				fltStatus = {},
				flightStatus = FlightNumber.getCapString(flightStatus),
				showEstimatedArrTime = false,
				showEstimatedDepTime = false;
	            if(flightStatus === "DELAYED" || flightStatus === "EXPECTED" ){
	            	imgClass = "delayed";
	            	statusColor = "displayRed";
	            	showEstimatedDepTime = true;
	            	showEstimatedArrTime = true;
	            }
	            else if(flightStatus === "ASSCHEDULED" || flightStatus === "ONTIME"){
	            	imgClass = "asScheduled";
	            }
	            else if(flightStatus === "LANDED"){
	            	imgClass = "landed";
	            }else{
	            	imgClass = "normal";
	            }
		            return fltStatus = {
								imgClass : imgClass,
								statusColor : statusColor,
								showEstimatedDepTime : showEstimatedDepTime,
								showEstimatedArrTime : showEstimatedArrTime
							};
		},

		//renders results
		 afterResults: function(url, fieldId, jsonData){
			 var FlightNumber=this;
			 //	dojo.byId("loading-flight-results").innerHTML ="";
			 var    todayFlights =jsonData.todaysFlightStatusData ,
			 		tomorrowFlights= jsonData.tomorrowsFlightStatusData,
			 		flightResultHeading = "",
			 		flightNumberDomNode = dojo.query("#flightNumber");
		        	if ((todayFlights == null || todayFlights.length == 0) && (tomorrowFlights == null || tomorrowFlights.length == 0)) {
		            	dojo.style(dojo.byId("errorpopup"), {"display": "inline-block"});
	            		//dojo.addClass(dojo.byId("readOnlyTom"),"error");
		            	//flightNumberDomNode.removeClass("yellow-border").addClass("error");
	            		dojo.style(dojo.query(".statusResultsContainer")[0], {"display": "none"});
	            		dojo.forEach(dojo.query(".sometext"), function(e){
		                	dojo.style(e, {"display":"none"});
		                });
	            	}
		        	else{
		        		dojo.style(dojo.query(".statusResultsContainer")[0], {"display": "block"});
	            		dojo.forEach(dojo.query(".sometext"), function(e){
		                	dojo.style(e, {"display":"block"});
		                });
	       	        	dojo.style(dojo.byId("errorpopup"), {"display": "none"});
	            		var todaysFlightStatusData = FlightNumber.formatFlightData(todayFlights, fieldId),
	            			tomorrowsFlightStatusData = FlightNumber.formatFlightData(tomorrowFlights, fieldId),
	            			flightData=[],dayData = {},title,dayData,isArrival = false;
	            			if(todaysFlightStatusData.length > 0){

		       			 		title = FlightNumber.flightTitleObject(new Date());
		       			 		dayData={
		       			 				title : title,
		       			 				flightStatusData : todaysFlightStatusData
		       			 		};
		       			 		flightData.push(dayData);
	       			 		}
	            			if(tomorrowsFlightStatusData.length > 0){
		       			 		title = FlightNumber.flightTitleObject(dojo.date.add( new Date(),"day",1));

		       			 		dayData={
		       			 				title : title,
		       			 				flightStatusData : tomorrowsFlightStatusData
		       			 		};
		       			 		flightData.push(dayData);
	            			}
	                        if(url == "allDepartures" || url.split("?")[0] == "searchByDeparture"){
	    	                	if(url == "allDepartures"){
	    	                		flightResultHeading = "All Departures";
	    	                	}
	    	                	else{
	    	                		var airportFromName = dijit.byId("flyFromStat").getLabel().split(",")[0];
	       			 				flightResultHeading = "Flights Departing from "+airportFromName;
	    	                	}


	    	                }
	    	                else if(url == "allArrivals" || url.split("?")[0] == "searchByArrival"){
	    	                	if(url == "allArrivals"){
	    	                		flightResultHeading = "All Arrivals";
	    	                	}
	    	                	else{
	    	                		var airportToName = (dijit.byId("flyToStat").getLabel()).split(",")[0];
	    	                		flightResultHeading = "Flights Arriving To "+airportToName;
	    	                	}
	    	                	isArrival =true;

	    	                }

	    	                else if(url.split("?")[0] == "searchByAll"){
	       			 				var airportFromName = dijit.byId("flyFromStat").getLabel().split(",")[0];
	       			 				var airportToName = dijit.byId("flyToStat").getLabel().split(",")[0];
	       			 				if(dijit.byId("flyToStat").getLabel() == "" && dijit.byId("flyFromStat").getLabel() != "" ){
	       			 				flightResultHeading = "Flights Departing from "+airportFromName;
	       			 				}
	       			 				if(dijit.byId("flyFromStat").getLabel() == "" && dijit.byId("flyToStat").getLabel() != ""){
	       			 				flightResultHeading = "Flights Arriving To "+airportToName;
	       			 				}
	       			 				else if(dijit.byId("flyFromStat").getLabel() != "" && dijit.byId("flyToStat").getLabel() != ""){
	       			 				flightResultHeading = "Flights Departing from "+airportFromName+" to "+airportToName;
	       			 				}
	       			 		}
	    	                else{
	       			 			var flightNumer = dijit.byId("flightNumber").getLabel();
	       			 			flightResultHeading = "Flight "+flightNumer;
	       			 		}
						var data = {
								flightData : flightData,
								flightResultHeading:flightResultHeading,
								isArrival : isArrival
								};
						html = dojo.trim(tui.dtl.Tmpl.createTmpl(data, flightStatusResultsTmpl));
						console.log(html)
						targetNode = dojo.query(".statusResultsContainer")[0];
						dojo.html.set(targetNode,html,{
							parseContent: true
						});
	         	}
		        	dojo.byId("extracttm").innerHTML = jsonData.currentFeedtime;
		 },//after results
		 //converts the time  format from hhmm to hh:mm
		 addColons : function(str) {
			    var result = '';
			    if(str.length > 0) {
			       result= str.substring(0,2)+ ":" + str.substring(2,4)
			    }
			    return result;
			},
			//converts the string format from xxx-xxx to XXXXXX
			getCapString: function(string){
				var words = string.split("-"),
					str = "";
				for(var i = 0;i < words.length; i++){
					str+= words[i];
				}
				return str.toUpperCase();
			},
			 initCapString: function(string){
				 var words = string.split("-");
				var str1 = "";
				var str = "";
				for(var i = 0;i < words.length; i++){
					str = words[i].toLowerCase();
					str1+= str[0].toUpperCase() + str.slice(1)+" ";
				}
				return str1 = str1==="Expected " ? "Delayed ": str1;
			}
	});
	return tui.flights.status.FlightNumber;


});