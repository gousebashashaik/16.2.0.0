define("tui/searchPanel/view/flights/ArrivalFlightSearchMonthBar", [
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

	dojo.declare("tui.searchPanel.view.flights.ArrivalFlightSearchMonthBar", null, {

		seasonLength: null,

		seasonStartDate:null,

		cal_months_labels : ['January', 'February', 'March', 'April',
		                     'May', 'June', 'July', 'August', 'September',
		                     'October', 'November', 'December'],

		constructor: function(){
			var arrivalFlightSearchMonthBar = this;

		},

		generateMonths: function(seasonStartDate, seasonLength){
			var arrivalFlightSearchMonthBar = this;
			arrivalFlightSearchMonthBar.seasonLength = seasonLength;
			arrivalFlightSearchMonthBar.seasonStartDate = arrivalFlightSearchMonthBar.setDateFormat(seasonStartDate);
			//Needs to disable the month if it is exceeding 93 days
			pulldownData = arrivalFlightSearchMonthBar.validatefor93Days(dojo.global.arrivalAvailableMonths);
			var monthSelc = dojo.create("div", {
				"class":"monthSelector"
			});
			domConstruct.place(monthSelc,document.body,"last");

			/*generate previous Season dates*/
			//arrivalFlightSearchMonthBar.getPreviousSeasonDate();

			/*generate months ahead..*/

			arrivalFlightSearchMonthBar.createPreviousYr(arrivalFlightSearchMonthBar.seasonStartDate);
			for(var i=0; i < arrivalFlightSearchMonthBar.seasonLength; i++) {
				var arr = [];

				var flightstr = arrivalFlightSearchMonthBar.getMonthSliced(i,  new Date());
				arr = flightstr.split("@");
				var activeKlass = "return-month-activable";
				if(pulldownData[i] !== undefined){
					if(pulldownData[i].available === true) activeKlass = "return-month-activable";
					if(pulldownData[i].available === false)activeKlass = "month-inactivable";
				}

				arrivalFlightSearchMonthBar.generateMonthCell(flightstr,activeKlass);

			}
			arrivalFlightSearchMonthBar.onMonthClick();

		},
		validatefor93Days: function(monthData){
			try {
				var arrivalFlightSearchMonthBar = this,lastMonthToEnable,selectedDepDate;
				if(dijit.byId("when") === undefined) throw "id with 'when' dom node is not created";
				selectedDepDate = new Date(dijit.byId("when").searchPanelModel.date);
				lastMonthToEnable = dojo.date.add(selectedDepDate,"day",93);
				dojo.forEach(monthData, function(item){
					//set the date of a item to 1st and compare as a date
					var monthDate = new Date("01 " + item.month);

					if(dojo.date.compare(monthDate,lastMonthToEnable) > 0){
						item.available = false;
					}
				});
			return monthData;
			} catch (err){
				console.error(err.name +" "+ err.message);
			}
		},

		getPreviousSeasonDate: function(){
			var arrivalFlightSearchMonthBar = this,
				currentDate = new Date();
				previousMonths = dojo.date.difference(arrivalFlightSearchMonthBar.seasonStartDate, currentDate,"month");
				//previousMonths = arrivalFlightSearchMonthBar.getMonthsDifference(seasonStartDate, currentDate),
				arr = [],
				activeKlass ="month-inactivable";
				//arrivalFlightSearchMonthBar.seasonLength = arrivalFlightSearchMonthBar.seasonLength - previousMonths;

				for(var i=0; i < previousMonths; i++){
					var flightstr = arrivalFlightSearchMonthBar.getMonthSliced(i, arrivalFlightSearchMonthBar.seasonStartDate);
					arrivalFlightSearchMonthBar.generateMonthCell(flightstr,activeKlass);
				}


		},

		generateMonthCell : function(flightstr,activeKlass){
			var arrivalFlightSearchMonthBar = this,
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
				arrivalFlightSearchMonthBar.createNextYr(arr[0], elm);
			}

		},

		 setDateFormat : function(dateObj){
		    	var searchController = this,newDate,
		    		parts = dateObj.split('-');
		    		newDate =  new Date(parts[0],parts[1]-1,parts[2]);
		    		return newDate;

		    },

		getMonthsDifference : function(d1, d2) {
		    var months;
		    months = (d2.getFullYear() - d1.getFullYear()) * 12;
		    months -= d1.getMonth() + 1;
		    months += d2.getMonth() + 1;
		    return months <= 0 ? 0 : months;
		},


		onMonthClick: function(){
			var arrivalFlightSearchMonthBar = this,
			monthsSelector = query(".monthSelector div");
			monthsSelector.on("click", function(){
				var mon = parseInt(domAttr.get(this,"data-cur-mon"));
				var yr = parseInt(domAttr.get(this,"data-cur-yr"));

				if(domClass.contains(this, "return-month-activable") || domClass.contains(this, "selective")) {
					monthsSelector.forEach(function(tag) {
						domClass.remove(tag, "month-sel-active");
						domClass.add(tag, "return-month-activable");
					});
					domClass.remove(this, "selective");
					domClass.remove(this, "return-month-activable");
					domClass.add(this, "month-sel-active");

					dojo.publish("tui/widget/datepicker/ArrivalMonthBarSelectOption/onchange", [mon, yr]);
				}
			});
			arrivalFlightSearchMonthBar.onMonthHover();
		},

		onMonthHover: function(){
			var arrivalFlightSearchMonthBar = this;
			query(".monthSelector div").on("mouseover, mouseout", function(evt){
				var m = parseInt(domAttr.get(this, "data-cur-mon"));
				if(evt.type === "mouseover"){

					if(!domClass.contains(this, "month-sel-active")){
						if(domClass.contains(this, "return-month-activable")) domClass.remove(this, "return-month-activable");

						domStyle.set(this, {
							"background": "#fcb712",
							"color": "#fff",
							"cursor": "pointer",
							"backgroundImage": "url(images/date_planes_1.png)",
							"backgroundRepeat": "no-repeat",
							"backgroundPosition": "17px 27px"
						});

						if(!domClass.contains(this, "month-inactivable"))domClass.add(this, "selective");
					}

				}else{
					if(domClass.contains(this, "month-sel-active")){
						domAttr.remove(this, "style");
					}else{
						domClass.remove(this, "selective");
						domClass.add(this, "return-month-activable");
					}
				}
			});
		},

		createPreviousYr:function(seasonStartDate){
			var currentYr = seasonStartDate.getFullYear();
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
				"class":"spanyear",
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
			var arrivalFlightSearchMonthBar = this;
			var dt = new Date(currentDate);
			dt.setDate(1);
			var mons = (dt).setMonth(dt.getMonth()+i);
			var mon = lang.trim(arrivalFlightSearchMonthBar.cal_months_labels[arrivalFlightSearchMonthBar.cal_next_date(mons).getMonth()].substring(0,3).toUpperCase());
			var fullMonth = lang.trim(arrivalFlightSearchMonthBar.cal_months_labels[arrivalFlightSearchMonthBar.cal_next_date(mons).getMonth()].toUpperCase());
			return  (new Date(mons).getFullYear() + "@" + mon + "@" +  new Date(mons).getMonth());
		},

		cal_next_date: function(dt){
			return new Date(dt);
		}
	});

	return tui.searchPanel.view.flights.ArrivalFlightSearchMonthBar;
});
