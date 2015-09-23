define("tui/flights/view/FlightScheduler", [
	"dojo",
	"dojo/query",
	"tui/flights/store/MonthPullDownStore",
	"dojo/_base/lang",
	"dojo/dom-construct",
	"tui/searchResults/view/flights/Tooltips"], function (dojo, query, monthPullStore, lang, domConstruct) {

	dojo.declare("tui.flights.view.FlightScheduler", null, {

		availableDates: [],

		cal_days_labels : ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'],

		cal_days_header_lbls: ['M', 'T', 'W', 'T', 'F', 'S', 'S'],

		cal_months_labels : ['January', 'February', 'March', 'April',
		                     'May', 'June', 'July', 'August', 'September',
		                     'October', 'November', 'December'],

		cal_days_in_month : [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31],

		cal_current_date : new Date(),

		constructor: function(month, year){
			var flightScheduler = this;
			flightScheduler.month = (isNaN(month) || month == null) ? flightScheduler.cal_current_date.getMonth() : month;
			flightScheduler.year  = (isNaN(year) || year == null) ? flightScheduler.cal_current_date.getFullYear() : year;
			flightScheduler.html = '';

		},

		cal_next_date: function(dt){
			return new Date(dt);
		},

		getMonthSliced:function(i){
			var flightScheduler = this;
			var dt = new Date();
			dt.setDate(1);
			var mons = (dt).setMonth(dt.getMonth()+i);
			var mon = lang.trim(flightScheduler.cal_months_labels[flightScheduler.cal_next_date(mons).getMonth()].substring(0,3).toUpperCase());
			var fullMonth = lang.trim(flightScheduler.cal_months_labels[flightScheduler.cal_next_date(mons).getMonth()].toUpperCase());
			return  (new Date(mons).getFullYear() + "@" + mon + "@" +  new Date(mons).getMonth());
		},
		getMonthSlicedIndex:function(i){
			var flightScheduler = this;
			var dt = new Date();
			dt.setDate(1);
			var mons = (dt).setMonth(dt.getMonth()+i);
			var mon = lang.trim(flightScheduler.cal_months_labels[flightScheduler.cal_next_date(mons).getMonth()].substring(0,3).toUpperCase());
			var fullMonth = lang.trim(flightScheduler.cal_months_labels[flightScheduler.cal_next_date(mons).getMonth()].toUpperCase());
			var monInIndex = Number(new Date(mons).getMonth()) + 1;
			if(monInIndex < 10) monInIndex = "0" + monInIndex;
			return  (new Date(mons).getFullYear() + "@" +  monInIndex);
		},
		getMnthYear: function(i){
			var flightScheduler = this;
			var dt = new Date();
			dt.setDate(1);
			var mons = (dt).setMonth(dt.getMonth()+i);
			var mon = lang.trim(flightScheduler.cal_months_labels[flightScheduler.cal_next_date(mons).getMonth()].toUpperCase());
			var fullMonth = lang.trim(flightScheduler.cal_months_labels[flightScheduler.cal_next_date(mons).getMonth()].toUpperCase());
			return  (new Date(mons).getFullYear() + "@" + mon + "@" +  new Date(mons).getMonth());
		},
		getFullMonths:function(i){
			var flightScheduler = this;
			var dt = new Date();
			dt.setDate(1);
			var mons = (dt).setMonth(dt.getMonth()+i);
			var mon = lang.trim(flightScheduler.cal_months_labels[flightScheduler.cal_next_date(mons).getMonth()].substring(0,3).toUpperCase());
			var fullMonth = lang.trim(flightScheduler.cal_months_labels[flightScheduler.cal_next_date(mons).getMonth()]);
			return  (fullMonth+" "+new Date(mons).getFullYear());
		},
		generateHTML: function(){
			var flightScheduler = this;
			  var firstDay = new Date(flightScheduler.year, flightScheduler.month, 1);
			  var startingDay = new Date(firstDay).getDay();
			  if(startingDay === 0){
				  startingDay = 7;
			  }
			  var monthLength = flightScheduler.cal_days_in_month[flightScheduler.month];
			  if (flightScheduler.month == 1) { // February only!
			    if((flightScheduler.year % 4 == 0 && flightScheduler.year % 100 != 0) || flightScheduler.year % 400 == 0){
			      monthLength = 29;
			    }
			  }
			  var monthName = flightScheduler.cal_months_labels[flightScheduler.month]
			  var html = '<table class="weekClass">';
			  html += '<thead class="schdheader"><tr>';
			  for(var i = 0; i <= 6; i++ ){
			    html += '<th class="schdth">';
			    html += flightScheduler.cal_days_labels[i];
			    html += '</th>';
			  }
			  html += '</tr></thead><tbody class="schdbody"><tr>';

			  // fill in the days
			  var day = 1;
			  // this loop is for is weeks (rows)
			  for (var i = 0; i < 9; i++) {
			    // this loop is for weekdays (cells)
			    for (var j = 0; j <= 6; j++) {
			      html += "<td class='schdtd'><div class='schdt'>";
			      if (day <= monthLength && (i > 0 || j >= startingDay-1)) {
			        //html += "<span class='date'>"+ day +"</span>";
			        //html += "<input type='hidden' value='" + day + "' />";
			        //html += '</div>';
			        /*var ad = flightScheduler.availableDates*/
			        var availDates=[];
			        _.each(flightScheduler.availableDates, function(txt,index){

			        	var availDay = "";
				        if(day < 10) availDay = "0" + day;
				        else availDay = day.toString();

				        if(availDay === txt.departureDate.substring(0,2)){
				        		availDates.push(flightScheduler.addColons(txt.departureTime)+ " - " + flightScheduler.addColons(txt.arrivalTime))
				        		//html += "<div class='flight-schds'><span class='schd-time'>" + flightScheduler.addColons(txt.departureTime)+ " - " + flightScheduler.addColons(txt.arrivalTime) + "</span></div>";
				        }

			        });
			        if(availDates.length<=3 && availDates.length!=0){
			        	html += "<span class='date avail'>"+ day +"</span>";
				        html += "<input type='hidden' value='" + day + "' />";
				        html += '</div>';
			        	_.each(availDates,function(item){
			        		html += "<div class='flight-schds'><span class='schd-time'>" + item + "</span></div>";
			        	})

			        } if(availDates.length>3) {
			        	count = availDates.length-1
			        	tooltiptxt = ""
				        	_.each(availDates,function(item,index){
				        		if(index !== 0)
				        		tooltiptxt +=  '<li>'+ item +'<li>'
				        	})
				        html += "<span class='date avail'>"+ day +"</span>";
			        	html += "<input type='hidden' value='" + day + "' />";
			        	html += '</div>';
			        	html += "<div class='flight-schds'><span class='schd-time'>" + availDates[0] + "</span></div>";
			        	var tooltipDom = '<div class="more-tooltip"><span data-dojo-type="tui.searchResults.view.flights.Tooltips" data-dojo-props="list:\''+ tooltiptxt + '\' , width: \'jumbo\',className:\'timetableCal\'">+'+ count +' more flights</span></div>';
			        	html += tooltipDom;
			        } else if(availDates.length===0){
			        	html += "<span class='date'>"+ day +"</span>";
				        html += "<input type='hidden' value='" + day + "' />";
				        html += '</div>';
			        }
			        day++;
			      }
			      html += '</td>';
			    }
			    // stop making rows if we've run out of days
			    if (day > monthLength) {
			      break;
			    } else {
			      html += '</tr><tr>';
			    }
			  }
			  html += '</tr></tbody></table>';
			  flightScheduler.html = html;
		},
		getHTML:function() {
			var flightScheduler = this;
			return flightScheduler.html;

		},
		checkAvailableDates: function(m,y){

			var flightScheduler = this;

			flightScheduler.availableDates = [];

			var fromAir = query("#from-airport")[0].innerHTML;
			var toAir = query("#to-airport")[0].innerHTML;

			var _targetURL = "";

				// called when clicking on "view" button to populate Month Bar and Big calendar
	  		  /*if(fromAir.length != 0 && toAir.length != 0){
				  var fromAirValue = fromAir.split("(")[1].split(")")[0];
				  var toAirValue = toAir.split("(")[1].split(")")[0] + ":" + toAir.split("(")[0];
				  _targetURL = 	+toAirValue+"&from[]="+fromAirValue;
	  		  }else{// to populate the Month Dropdown
	  			_targetURL = "ws/traveldates/";
	  		  }*/



			if(fromAir.length != 0 && toAir.length != 0){
		  		var fromAp = fromAir.split("(")[1].split(")")[0];
		  		var toAP = toAir.split("(")[1].split(")")[0];
		  		var results = dojo.xhr("GET", {
	                url:"ws/timetableresults?departureCode="+fromAp+"&arrivalCode="+toAP,
	                handleAs: "json",
	                sync: true
	            });

				results.then(function(data){
					flightScheduler.availMonStore = data.items;
				});
	  		}

			//flightScheduler.avail = results;//[{"departureTime":"1050","arrivalTime":"1525","departureDate":"25-05-2014","departureAirport":"LGW","arrivalAirport":"SFB"},{"departureTime":"1050","arrivalTime":"1525","departureDate":"26-03-2014","departureAirport":"LGW","arrivalAirport":"SFB"},{"departureTime":"1050","arrivalTime":"1525","departureDate":"27-03-2014","departureAirport":"LGW","arrivalAirport":"SFB"},{"departureTime":"1050","arrivalTime":"1525","departureDate":"28-03-2014","departureAirport":"LGW","arrivalAirport":"SFB"},{"departureTime":"1050","arrivalTime":"1525","departureDate":"29-03-2014","departureAirport":"LGW","arrivalAirport":"SFB"},{"departureTime":"1050","arrivalTime":"1525","departureDate":"30-03-2014","departureAirport":"LGW","arrivalAirport":"SFB"},{"departureTime":"1050","arrivalTime":"1525","departureDate":"31-03-2014","departureAirport":"LGW","arrivalAirport":"SFB"},{"departureTime":"1050","arrivalTime":"1525","departureDate":"02-04-2014","departureAirport":"LGW","arrivalAirport":"SFB"},{"departureTime":"1050","arrivalTime":"1525","departureDate":"09-04-2014","departureAirport":"LGW","arrivalAirport":"SFB"},{"departureTime":"1050","arrivalTime":"1525","departureDate":"16-04-2014","departureAirport":"LGW","arrivalAirport":"SFB"},{"departureTime":"1050","arrivalTime":"1525","departureDate":"23-04-2014","departureAirport":"LGW","arrivalAirport":"SFB"},{"departureTime":"1050","arrivalTime":"1525","departureDate":"30-04-2014","departureAirport":"LGW","arrivalAirport":"SFB"},{"departureTime":"1050","arrivalTime":"1525","departureDate":"25-05-2014","departureAirport":"LGW","arrivalAirport":"SFB"}];


			m += 1;
			if(m < 10){
				m = "0" + m;
			}

			m = m.toString();

			/*_.each(flightScheduler.monStore, function(elm){
					if(m === elm.substring(3,5) && y.toString() === elm.substring(6,10)){
						flightScheduler.availableDates.push(elm);
					}else{
						return;
					}
			});*/

			_.each(flightScheduler.availMonStore, function(elm){
				if(m === elm.departureDate.substring(3,5) && y.toString() === elm.departureDate.substring(6,10)){
					flightScheduler.availableDates.push(elm);
				}else{
					return;
				}
			});

		},

		addColons: function(str){
			 var result = '';
			    while (str.length > 0) {
			        result += str.substring(0, 2) + ':';
			        str = str.substring(2);
			    }
			 return result.substring(0, result.length-1);
		}

	});

	return tui.flights.view.FlightScheduler;
});