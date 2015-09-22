define("tui/flights/view/FlightTinyCalendar", [
	"dojo",
	"dojo/query",
	"dojo/dom-attr",
	"dojo/dom-style",
	"dojo/parser",
	"dojo/dom-construct",
	"dojo/on",
	"tui/flights/store/MonthPullDownStore",
	"dojo/_base/lang"], function (dojo, query, domAttr, domStyle, parser, domConstruct, on, monthPullStore, lang) {

	dojo.declare("tui.flights.view.FlightTinyCalendar", null, {
		
		availableDates: [],
		
		cal_days_labels : ['M', 'T', 'W', 'T', 'F', 'S', 'S'],
		
		cal_months_labels : ['January', 'February', 'March', 'April',
		                     'May', 'June', 'July', 'August', 'September',
		                     'October', 'November', 'December'],
		                     
		cal_days_in_month : [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31],
		
		cal_current_date : new Date(),
		
		constructor: function(month, year, selectedDate){
			var flightTinyCalendar = this;
			flightTinyCalendar.month = (isNaN(month) || month == null) ? flightTinyCalendar.cal_current_date.getMonth() : month;
			flightTinyCalendar.year  = (isNaN(year) || year == null) ? flightTinyCalendar.cal_current_date.getFullYear() : year;
			flightTinyCalendar.departureDate = selectedDate;
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
		getFullMonths:function(i){
			var flightTinyCalendar = this;
			var mons = (new Date()).setMonth(new Date().getMonth()+i);
			var mon = lang.trim(flightTinyCalendar.cal_months_labels[flightTinyCalendar.cal_next_date(mons).getMonth()].substring(0,3).toUpperCase());
			var fullMonth = lang.trim(flightTinyCalendar.cal_months_labels[flightTinyCalendar.cal_next_date(mons).getMonth()]);
			return  (fullMonth+" "+new Date(mons).getFullYear());
		},
		generateHTML: function(){
			var flightTinyCalendar = this;
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
			  var monthSelec = query("div.monthSelector div.month-sel-active")[0];
			  var curMon = domAttr.get(monthSelec, "data-cur-mon");
			  var curYear = domAttr.get(monthSelec, "data-cur-yr");
			  for (var i = 0; i < 9; i++) {
			    for (var j = 0; j <= 6; j++) {
			      html += '<td><div style="position: relative;" class="has-tooltip">';
			      if (day <= monthLength && (i > 0 || j >= startingDay-1)) {
			    	  var chkday = true;
			        
			    	  _.each(flightTinyCalendar.availableDates, function(txt){
			        	var availDay = "";
				        if(day < 10) availDay = "0" + day;
				        else availDay = day.toString();
				        
				        if(availDay === txt.substring(0,2)){
				        	if((parseInt(curMon) === flightTinyCalendar.month && day === parseInt(flightTinyCalendar.departureDate)) && parseInt(curYear) === flightTinyCalendar.year){
				        		html+="<div style='width:30px;height:30px;background:#feb512;'><img src='images/yelloFlight.png' style='position:relative;top:8px;'/></div>";
				        	}else{
				        		html += day.toString().fontcolor("#000");
				        	}
				        	chkday = false;
				        }
			          });
			        
			        if(chkday){
			        	if((parseInt(curMon) === flightTinyCalendar.month && day === parseInt(flightTinyCalendar.departureDate)) && parseInt(curYear) === flightTinyCalendar.year){
			        		html+="<div style='width:30px;height:30px;background:#feb512;'><img src='images/yelloFlight.png' style='position:relative;top:8px;'/></div>";
			        	}else{
			        		html += day;
			        	}
			        }
			        
			        day++;
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
				  _targetURL = "ws/traveldates?from[]="+fromAirValue+"&multiSelect=";
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
			query("table.flight-tiny-datepicker tr td").on("mouseover,mouseout", function(evt){
				if(evt.type === "mouseover"){
					dojo.addClass(this, 'yellowBG');
				}else {
					dojo.removeClass(this, 'yellowBG');
				}
				
			});
			
			query("table.flight-tiny-datepicker tr td").on("click", function(evt){
					if(evt.type === "click"){
						query("div.return-tip").forEach(domConstruct.destroy);
						if(!isNaN(parseInt(query(this).text()))){
							
							if(query("font", this)[0]){
								var hovDate = query("font", this).text(),
									kl,
									monthLength=0,
									differenceMonths=0,
									showFlag = false;
								
									
								if( (dojo.global.monLastIndx !== '' && dojo.global.yearLastIndx !== '') && parseInt(flightTinyCalendar.year) === parseInt(dojo.global.yearLastIndx) && ((parseInt(flightTinyCalendar.month) > parseInt(dojo.global.monLastIndx)) || ((parseInt(flightTinyCalendar.month) === parseInt(dojo.global.monLastIndx)) && (parseInt(query(this).text()) > parseInt(flightTinyCalendar.departureDate))))) {
									
									showFlag=true;
									differenceMonths = parseInt(flightTinyCalendar.month) - parseInt(dojo.global.monLastIndx);	
									
									if(differenceMonths > 1){											
										monthLength += flightTinyCalendar.getDifferenceDays(differenceMonths,false);	
										
									}else if(differenceMonths === 0){										
										monthLength=0;
										
									}else{										
										if (dojo.global.monLastIndx == 1) { // February only!
												monthLength += flightTinyCalendar.leapYearChecking(dojo.global.monLastIndx);
										  }else{
											  	monthLength += flightTinyCalendar.cal_days_in_month[dojo.global.monLastIndx ];
										  }
									}		
									 
									kl = parseInt(hovDate) + parseInt(monthLength) -  parseInt(flightTinyCalendar.departureDate);
									
									 
								}else if(parseInt(flightTinyCalendar.year) > parseInt(dojo.global.yearLastIndx)){
									
									showFlag=true;
									monthLength += flightTinyCalendar.getDifferenceDays(flightTinyCalendar.month, false);	
									differenceMonths = 12 - parseInt(dojo.global.monLastIndx) ;	
									monthLength += flightTinyCalendar.getDifferenceDays(differenceMonths, true);
									
									kl = parseInt(hovDate) + parseInt(monthLength) -  parseInt(flightTinyCalendar.departureDate);
							
								}else if(parseInt(query(this).text()) > parseInt(flightTinyCalendar.departureDate)) {
									showFlag=true;
									kl = parseInt(hovDate) - parseInt(flightTinyCalendar.departureDate);
								   
								   
								}
								
								if(showFlag){
									query("div.has-tooltip", this)[0].innerHTML = "";
									query("div.has-tooltip", this)[0].innerHTML = "<font color='#000'>" + hovDate + "</font><div class='return-tip position-top-center'><p id='return-dates-tip'>" + kl + " days</p><span class='arrow'></span></div>";
							}
						  }	
						}
					}else{
						query("div.return-tip").forEach(domConstruct.destroy);
					}
			});
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