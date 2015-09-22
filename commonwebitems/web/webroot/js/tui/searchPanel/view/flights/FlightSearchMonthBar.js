define("tui/searchPanel/view/flights/FlightSearchMonthBar", [
	'dojo',
	'dojo/_base/lang',
	'dojo/dom-geometry',
	'dojo/dom-construct',
	'dojo/dom-class',
	'dojo/dom-style',
	'dojo/dom-attr',
	"dojo/_base/connect",
	'dojo/_base/window',
	'dojo/query',
	'dojo/_base/html',
	'dojo/domReady!'], function (dojo, lang, domGeom, domConstruct, domClass, domStyle, domAttr, connect, win, query, html) {

	dojo.declare("tui.searchPanel.view.flights.FlightSearchMonthBar", null, {
		
		
		
		cal_months_labels : ['January', 'February', 'March', 'April',
		                     'May', 'June', 'July', 'August', 'September',
		                     'October', 'November', 'December'],
		
		constructor: function(){
			var flightSearchMonthBar = this;
					
		},
		
		generateMonths: function(seasonStartDate, seasonLength){
			var flightSearchMonthBar = this;
			flightSearchMonthBar.seasonLength = seasonLength;
			flightSearchMonthBar.seasonStartDate = seasonStartDate;
			pulldownData = dojo.global.availableMonths;
			var monthSelc = dojo.create("div", { 
				"class":"monthSelector"
			});
			domConstruct.place(monthSelc,document.body,"last");
			
			/*generate previous Season dates*/
			flightSearchMonthBar.getPreviousSeasonDate();
			
			/*generate  months ahead..*/
			for(var i=0; i < flightSearchMonthBar.seasonLength; i++) {
				var arr = [];
				
				var flightstr = flightSearchMonthBar.getMonthSliced(i , new Date());
				arr = flightstr.split("@");
				var activeKlass = "month-activable";
				if(pulldownData[i] != undefined) {
					if(pulldownData[i].available === true) activeKlass = "month-activable";
					if(pulldownData[i].available === false)activeKlass = "month-inactivable";
				}
				
				flightSearchMonthBar.generateMonthCell(flightstr,activeKlass);				
			
			}
			flightSearchMonthBar.onMonthClick();
			
		},
		

		getPreviousSeasonDate: function(){
			var flightSearchMonthBar = this,
				seasonStartDate = flightSearchMonthBar.setDateFormat(flightSearchMonthBar.seasonStartDate),
				currentDate = new Date();				
				previousMonths = flightSearchMonthBar.getMonthsDifference(seasonStartDate, currentDate),
				arr = [],
				activeKlass ="month-inactivable";
				//flightSearchMonthBar.seasonLength = flightSearchMonthBar.seasonLength - previousMonths;
				
				for(var i=0; i < previousMonths; i++){	
					
					var flightstr = flightSearchMonthBar.getMonthSliced(i, flightSearchMonthBar.seasonStartDate);						
						flightSearchMonthBar.generateMonthCell(flightstr,activeKlass);
					
				}
				
				flightSearchMonthBar.createPreviousYr(flightSearchMonthBar.seasonStartDate);
		},
		
		generateMonthCell : function(flightstr,activeKlass){
			var flightSearchMonthBar = this,
				arr = flightstr.split("@"),
				monthSel = dojo.create("div", { 
				innerHTML:arr[1],
				"data-cur-yr":arr[0],
				"data-cur-mon":arr[2],
				"class": activeKlass
			});
			
			domConstruct.place(monthSel,query(".monthSelector")[0],"last");
			
			if(parseInt(arr[2]) === 0){
				var elm = html.coords(monthSel);
				flightSearchMonthBar.createNextYr(arr[0], elm);
			}
			
		},
		
		getMonthsDifference : function(d1, d2) {
		    var months;
		    d1 = new Date(d1);
		    months = (d2.getFullYear() - d1.getFullYear()) * 12;
		    months -= d1.getMonth() + 1;
		    months += d2.getMonth() + 1;
		    return months <= 0 ? 0 : months;
		},
		
		onMonthClick: function(){
			var flightSearchMonthBar = this,
			monthsSelector = query(".monthSelector div");
			monthsSelector.on("click", function(){
				var mon = parseInt(domAttr.get(this,"data-cur-mon"));
				var yr = parseInt(domAttr.get(this,"data-cur-yr"));
				
				if(domClass.contains(this, "month-activable") || domClass.contains(this, "selective")) {
					monthsSelector.forEach(function(tag) {
						domClass.remove(tag, "month-sel-active");
						domClass.add(tag, "month-activable");
					});
					domClass.remove(this, "selective");
					domClass.remove(this, "month-activable");
					domClass.add(this, "month-sel-active");
					
					dojo.publish("tui/widget/datepicker/MonthBarSelectOption/onchange", [mon, yr]);
				}
			});
			flightSearchMonthBar.onMonthHover();
		},
		
		onMonthHover: function(){
			var flightSearchMonthBar = this;
			query(".monthSelector div").on("mouseover, mouseout", function(evt){
				var m = parseInt(domAttr.get(this, "data-cur-mon"));
				if(evt.type === "mouseover"){
					
					if(!domClass.contains(this, "month-sel-active")){
						if(domClass.contains(this, "month-activable")) domClass.remove(this, "month-activable");
						
						domStyle.set(this, {
							"background": "#fcb712",
							"color": "#fff",
							"cursor": "pointer",
							"backgroundImage": "url(images/date_planes.png)",
							"backgroundRepeat": "no-repeat",
							"backgroundPosition": "0px 27px"
						});
						
						if(!domClass.contains(this, "month-inactivable"))domClass.add(this, "selective");
					}
					
				}else{
					if(domClass.contains(this, "month-sel-active")){
						domAttr.remove(this, "style");
					}else{
						domClass.remove(this, "selective");
						domClass.add(this, "month-activable");
					}
				}
			});
		},
		setDateFormat : function(dateObj){
			var flightSearchMonthBar = this,newDate,
	    		parts = dateObj.split('-');
	    		newDate = new Date(parts[0],parts[1]-1,parts[2]);
	    		return newDate.valueOf();
	    },
	
		createPreviousYr:function(seasonStartDate){
			var flightSearchMonthBar = this;
			var currentYr = new Date(flightSearchMonthBar.setDateFormat(seasonStartDate)).getFullYear();
			var yr = dojo.create("span", { 
				innerHTML:currentYr
			});
			domStyle.set(yr, {
				"position":"absolute",
				"top":"-13.796875px",//"145.796875px",
				"left":"50px"//"108.578125px"
			});
			domConstruct.place(yr,query(".monthSelector")[0],"first");
		},
		
		createYr:function(){
			var currentYr = (new Date()).getFullYear();
			var yr = dojo.create("span", { 
				innerHTML:currentYr
			});
			domStyle.set(yr, {
				"position":"absolute",
				"top":"-13.796875px",//"145.796875px",
				"left":"50px"//"108.578125px"
			});
			domConstruct.place(yr,query(".monthSelector")[0],"first");
		},
		createNextYr: function(yr, elm){
			var pos = elm.l;
			pos = pos - 17;
			var yr = dojo.create("span", { 
				innerHTML:"|" + yr
			});
			domStyle.set(yr, {
				"position":"absolute",
				"top":"-13.796875px",//"145.796875px",
				"left":pos+"px"//"108.578125px"
			});
			domConstruct.place(yr,query(".monthSelector")[0],"last");
		},
		
		 getMonthSliced:function(i, currentDate){
			var flightSearchMonthBar = this;
			if(typeof currentDate === "string"){
				var dt = new Date(flightSearchMonthBar.setDateFormat(currentDate));
			} else {
				var dt = currentDate;
			}
			
			dt.setDate(1);
			var mons = (dt).setMonth(dt.getMonth()+i);
			var mon = lang.trim(flightSearchMonthBar.cal_months_labels[flightSearchMonthBar.cal_next_date(mons).getMonth()].substring(0,3).toUpperCase());
			var fullMonth = lang.trim(flightSearchMonthBar.cal_months_labels[flightSearchMonthBar.cal_next_date(mons).getMonth()].toUpperCase());
			return  (new Date(mons).getFullYear() + "@" + mon + "@" +  new Date(mons).getMonth());
		}, 
		
		cal_next_date: function(dt){
			return new Date(dt);
		}
	});
	
	return tui.searchPanel.view.flights.FlightSearchMonthBar;
});
