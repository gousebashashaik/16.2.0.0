define("tui/flights/view/FlightTinyCalendar", [
	"dojo",
	"dojo/query",
	"dojo/dom-attr",
	"dojo/dom-style",
	"dojo/parser",
	"dojo/dom-construct",
	"dojo/on",
	"tui/flights/store/MonthPullDownStore",
	"dojo/_base/lang",
	"tui/widget/popup/Tooltips",
	"dojo/NodeList-traverse"], function (dojo, query, domAttr, domStyle, parser, domConstruct, on, monthPullStore, lang, tooltips) {

	dojo.declare("tui.flights.view.FlightTinyCalendar", null, {

		availableDates: [],

		cal_days_labels : ['M', 'T', 'W', 'T', 'F', 'S', 'S'],

		cal_months_labels : ['January', 'February', 'March', 'April',
		                     'May', 'June', 'July', 'August', 'September',
		                     'October', 'November', 'December'],

		cal_days_in_month : [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31],

		cal_current_date : new Date(),

		selectedDepartureDate: null,

		constructor: function(month, year, selectedDate, selectedDepartureDate){
			var flightTinyCalendar = this;
			flightTinyCalendar.month = (isNaN(month) || month == null) ? flightTinyCalendar.cal_current_date.getMonth() : month;
			flightTinyCalendar.year  = (isNaN(year) || year == null) ? flightTinyCalendar.cal_current_date.getFullYear() : year;
			flightTinyCalendar.departureDate = parseInt(selectedDate);
			flightTinyCalendar.selectedDepartureDate = selectedDepartureDate;
			flightTinyCalendar.html = '';
		},

		cal_next_date: function(dt){
			return new Date(dt);
		},

		getMonthSliced:function(i){
			var flightTinyCalendar = this;
			var mons = (new Date()).setMonth(new Date().getMonth()+i);
			var mon = lang.trim(flightTinyCalendar.cal_months_labels[flightTinyCalendar.cal_next_date(mons).getMonth()].substring(0,3).toUpperCase());
			var fullMonth = lang.trim(flightTinyCalendar.cal_months_labels[flightTinyCalendar.cal_next_date(mons).getMonth()].toUpperCase());
			return  (new Date(mons).getFullYear() + "@" + mon + "@" +  new Date(mons).getMonth());
		},
		getFullMonths:function(m,yr){
			var flightTinyCalendar = this,
				dt,
				fullMonth;
			dt = new Date(yr,m);
			fullMonth = lang.trim(flightTinyCalendar.cal_months_labels[dt.getMonth()].toUpperCase());
			return  (fullMonth+" "+dt.getFullYear());
		},
		generateHTML: function(){
			var flightTinyCalendar = this;
			if(flightTinyCalendar.availableDates[0] !=undefined){
				 var firstDay = new Date(flightTinyCalendar.year, flightTinyCalendar.month, 1);
				  var startingDay = new Date(firstDay).getDay();
				  if(startingDay === 0){
					  startingDay = 7;
				  }
				  var monthLength = flightTinyCalendar.cal_days_in_month[flightTinyCalendar.month];
				  if (flightTinyCalendar.month == 1) { // February only!
				    if((flightTinyCalendar.year % 4 == 0 && flightTinyCalendar.year % 100 != 0) || flightTinyCalendar.year % 400 == 0){
				      monthLength = 29;
				    }
				  }
				  var monthName = flightTinyCalendar.cal_months_labels[flightTinyCalendar.month]
				  var html = '<table class="flight-tiny-datepicker">';
				  html += '<thead><tr>';
				  for(var i = 0; i <= 6; i++ ){
				    html += '<th>';
				    html += flightTinyCalendar.cal_days_labels[i];
				    html += '</th>';
				  }
				  html += '</tr></thead><tbody><tr>';

				  var day = 1;
				  var monthSelec = query("div.timeTableMonthSelector div.month-sel-active")[0];
				  var curMon = domAttr.get(monthSelec, "data-cur-mon");
				  var curYear = domAttr.get(monthSelec, "data-cur-yr");
				  var selectedRetDate = domAttr.get(dojo.byId("season-len-input"),"data-ret-date");
				  for (var i = 0; i < 9; i++) {
				    for (var j = 0; j <= 6; j++) {
				    	if(selectedRetDate !== null || selectedRetDate !== ""){
				    		selectedRetDate = new Date(selectedRetDate);
				    	}
				    	selectedDt = false;
				    	if(dojo.date.difference(selectedRetDate,date) == -1){
					    	  selectedDt = true;
					    }

				    	if(selectedDt){
				    		html += '<td class="yellowBGClick"><div class="tooltipDiv"></div><div style="position: relative;" class="has-tooltip">';
				    	} else {
				    		html += '<td><div class="tooltipDiv"></div><div style="position: relative;" class="has-tooltip">';
				    	}
				      var date = new Date(day+ " " + dojo.byId("season-len-input").value );
				      var disableDate = true;
				      if(dojo.date.difference(flightTinyCalendar.selectedDepartureDate,date) > 93 || dojo.date.difference(flightTinyCalendar.selectedDepartureDate,date) <= 0){
				    	  disableDate = false;
				      }

				      if (day <= monthLength && (i > 0 || j >= startingDay-1)) {
				    	  var chkday = true;

				    	  _.each(flightTinyCalendar.availableDates, function(txt){
				        	var availDay = "";
					        if(day < 10) availDay = "0" + day;
					        else availDay = day.toString();

					        if(availDay === txt.substring(0,2)){
					        	if((parseInt(curMon) === flightTinyCalendar.month && day === parseInt(flightTinyCalendar.departureDate)) && parseInt(curYear) === flightTinyCalendar.year){
					        		html+="<div class='curDay'></div>";
					        	}else{
					        		if(disableDate){
					        			html += "<div class='available'>" + day.toString() + "</div>";
					        		} else {
					        			html += "<div class='unavailable'>" + day.toString() + "</div>";
					        		}

					        	}
					        	chkday = false;
					        }
				          });

				        if(chkday){
				        	if((parseInt(curMon) === flightTinyCalendar.month && day === parseInt(flightTinyCalendar.departureDate)) && parseInt(curYear) === flightTinyCalendar.year){
				        		html+="<div class='curDay'></div>";
				        	}else{
				        		html += "<div class='unavailable'>" + day + "</div>";
				        	}
				        }

				        day++;
				      }else{
				    	  html += "<div class='disabled'></div>";
				      }
				      html += '</div></td>';
				    }
				    // stop making rows if we've run out of days
				    if (day > monthLength) {
				      break;
				    } else {
				      html += '</tr><tr>';
				    }
				  }


				  html += '</tr></tbody></table>';

			}else{
				html = " <p class = 'invalidMonth'> Sorry, there are no flights in <span class='middle'>" + flightTinyCalendar.getFullMonths(flightTinyCalendar.month, flightTinyCalendar.year)+ "</span>. Please select an alternative month </p>";

			}
			flightTinyCalendar.html = html;
		},
		getHTML:function() {
			var flightTinyCalendar = this;
			return flightTinyCalendar.html;

		},

		checkAvailableDates:function(m, y, selectedDate){
			var flightTinyCalendar = this;

			flightTinyCalendar.availableDates = [];

			var fromAir = query("#from-airport")[0].innerHTML;
			var toAir = query("#to-airport")[0].innerHTML;

			var _targetURL = "";

	  		  if(fromAir.length != 0 && toAir.length != 0){
				  var fromAirValue = fromAir.split("(")[1].split(")")[0];
				  var toAirValue = toAir.split("(")[1].split(")")[0] + ":" + toAir.split("(")[0];
				  _targetURL = "ws/traveldates?from[]="+fromAirValue+"&to[]="+toAirValue+"&multiSelect=";
	  		  }else{
	  			_targetURL = "ws/traveldates/";
	  		  }

			var monthStore = new monthPullStore({
	  			targetURL: _targetURL
	  		});

			flightTinyCalendar.monStore = monthStore.fetchAvailableReturnDates();

			m += 1;
			if(m < 10){
				m = "0" + m;
			}

			m = m.toString();

			_.each(flightTinyCalendar.monStore, function(elm){
					if(m === elm.substring(3,5) && y.toString() === elm.substring(6,10)){
						flightTinyCalendar.availableDates.push(elm);
					}else{
						return;
					}
			});

		},

		attachTooltipEvents: function(){
			var flightTinyCalendar = this;
			if(!dojo.hasClass(query("html")[0],"touch")){
				query("table.flight-tiny-datepicker tr td").on("mouseover,mouseout", function(evt){
						var isUnavailable = dojo.query(this).children().children(".unavailable").length > 0;
						if(evt.type === "mouseover"){
							if(isUnavailable){
								dojo.addClass(this, 'noBG');
							}
							if(!isNaN(parseInt(query(this).text()))){

								if(query(".available",this)[0]){
									dojo.addClass(this, 'yellowBG');
									if(query(".timetable.loaded", this).length > 0) {
										dijit.byNode(query(".timetable.loaded", this)[0]).open();
										return;
									}
									var hovDate = query(".available",this).text(),
										kl,
										currentHoverDate = new Date(hovDate +" " + dojo.byId("season-len-input").value);
										kl = dojo.date.difference(flightTinyCalendar.selectedDepartureDate ,currentHoverDate, "day" )
										new tooltips({
							    			 refId : query("div.tooltipDiv", this)[0],
							    			 text:  kl == 1 ?  kl + " day" : kl + " days",
							    			 floatWhere : 'position-top-center',
							    			 className: "timetable",
							    			 setPosOffset: function(){
							    				  this.posOffset = {top: -10, left: 0};
							    			 }
							    		 },query("div.tooltipDiv", this)[0]).open();
							  }
							}
						}else if (evt.type === "mouseout"){
							if(isUnavailable){
								dojo.removeClass(this, 'noBG');
							}
							dojo.removeClass(this, 'yellowBG');
							//query("div.return-tip").forEach(domConstruct.destroy);
							if(query(".timetable.loaded", this).length > 0){
								dijit.byNode(query(".timetable.loaded", this)[0]).close();
							}
						}
				});
			}
		},

		attachClickEvent:function(){
			var flightTinyCalendar = this,returnDate;
			query("table.flight-tiny-datepicker tr td").on("click", function(evt){
				if(query(".available",this)[0]){
					//if(parseInt(query(this).text()) > parseInt(flightTinyCalendar.departureDate)){
						query(".yellowBGClick",dojo.byId("tiny-search-box")).removeClass("yellowBGClick");
						dojo.addClass(this, 'yellowBGClick');
						returnDate = new Date(flightTinyCalendar.year,flightTinyCalendar.month,parseInt(query(".available",this).text()))
						dojo.attr(dojo.byId("season-len-input"),"data-ret-date",dojo.date.locale.format(returnDate, {selector: "date",datePattern: "yyy-MM-dd"}));
						dojo.query(".dealsPax").remove();
						if(dojo.hasClass(query("html")[0],"touch")){
							query("table.flight-tiny-datepicker tr td").removeClass("yellowBG");
							//query("div.return-tip").forEach(domConstruct.destroy);
							if(query(".timetable.loaded", query("table.flight-tiny-datepicker")[0]).length > 0){

								query(".timetable.loaded", query("table.flight-tiny-datepicker")[0]).forEach(function(item){
									dijit.byNode(item).close();
								})
							}

							if(!isNaN(parseInt(query(this).text()))){

								if(query(".available",this)[0]){
									dojo.addClass(this, 'yellowBG');
									if(query(".timetable.loaded", this).length > 0) {
										dijit.byNode(query(".timetable.loaded", this)[0]).open();
										return;
									}
									var hovDate = query(".available",this).text(),
										kl,
										currentHoverDate = new Date(hovDate +" " + dojo.byId("season-len-input").value);
										kl = dojo.date.difference(flightTinyCalendar.selectedDepartureDate ,currentHoverDate, "day" )
										new tooltips({
							    			 refId : query("div.tooltipDiv", this)[0],
							    			 text:  kl == 1 ?  kl + " day" : kl + " days",
							    			 floatWhere : 'position-top-center',
							    			 className: "timetable",
							    			 setPosOffset: function(){
							    				  this.posOffset = {top: -10, left: 0};
							    			 }
							    		 },query("div.tooltipDiv", this)[0]).open();
							  }
							}

						}
					//}

				}
			})
		},

		getDifferenceDays: function(months, lastYear){
			var flightTinyCalendar = this,
				monthLength =0;

			for(var k =0; k < months; k++ ){
				var monthIndex=0;
				if(lastYear){
					monthIndex = 11 - parseInt(k);
				}
				else{
					monthIndex = parseInt(flightTinyCalendar.month-1 ) - parseInt(k);
				}

				if (monthIndex == 1) { // February only!
					monthLength += flightTinyCalendar.leapYearChecking(monthIndex);
				}else{
					monthLength += flightTinyCalendar.cal_days_in_month[monthIndex];
				}
			}
			return monthLength;

		},
		leapYearChecking: function(month){
			var flightTinyCalendar = this;

			if((flightTinyCalendar.year % 4 == 0 && flightTinyCalendar.year % 100 != 0) || flightTinyCalendar.year % 400 == 0){
			      return 29;
			  }else{
				  return 28;
			  }
		}
	});

	return tui.flights.view.FlightTinyCalendar;
});