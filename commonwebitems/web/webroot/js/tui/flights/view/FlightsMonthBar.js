define("tui/flights/view/FlightsMonthBar", [
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
	'tui/flights/view/FlightActions',
	'tui/flights/view/FlightScheduler',
	'dojo/domReady!'], function (dojo, lang, domGeom, domConstruct, domClass, domStyle, domAttr, connect, win, query, html, flightActions, flightScheduler) {

	dojo.declare("tui.flights.view.FlightsMonthBar", [tui.flights.view.FlightActions], {
		
		seasonLength: 18,
		
		constructor: function(){
			var flightsMonthBar = this;
			
		},
		
		generateMonths: function(){
			var flightsMonthBar = this;
			
			pulldownData = dojo.global.monthPullDown;
			
			for(var i=0; i < flightsMonthBar.seasonLength; i++){
				var arr = [];
				
				var flightstr = (new flightScheduler()).getMonthSliced(i);
				arr = flightstr.split("@");

				var selectedMon = dojo.byId("ftselectedMonth");
				
				var selArr = selectedMon.value.split(":");
				
				var tempA = Number(arr[2]);
				tempA += 1;
				
				if(tempA < 10) tempA = "0" + tempA;
				
				tempA = tempA.toString();
				
				var activeKlass = "";
				
				if(pulldownData[i].available === true) activeKlass = "month-activable";
				if(pulldownData[i].available === false)activeKlass = "month-inactivable";
				if(selArr[0] === tempA && selArr[1] === arr[0])activeKlass = "month-sel-active";
				
				
				var monthSel = dojo.create("div", { 
					innerHTML:arr[1],
					"data-cur-yr":arr[0],
					"data-cur-mon":arr[2],
					"data-cur-idx":i,
					"class": activeKlass
				});
				
				domConstruct.place(monthSel,query(".monthSelector")[0],"last");
				if(parseInt(arr[2]) === 0){
					var elm = html.coords(monthSel);
					flightsMonthBar.createNextYr(arr[0], elm);
				}
			}
			
			flightsMonthBar.onMonthClick();
			flightsMonthBar.createYr();
			
			var flightactns = new flightActions();
			flightactns.showflightSchedules((Number(selArr[0]))-1, Number(selArr[1]));
			
		},
		
		onMonthClick: function(){
			var flightsMonthBar = this,
				flightactns = new flightActions(),
				monthsSelector = query(".monthSelector div");
			monthsSelector.on("click", function(){
				var mon = parseInt(domAttr.get(this,"data-cur-mon"));
				var yr = parseInt(domAttr.get(this,"data-cur-yr"));
				
				if(domClass.contains(this, "month-activable") || domClass.contains(this, "selective")){
					
					monthsSelector.forEach(function(tag){
						domClass.remove(tag, "month-sel-active");
						domClass.add(tag, "month-activable");
					});
					
					domClass.remove(this, "selective");
					domClass.remove(this, "month-activable");
					domClass.add(this, "month-sel-active");
					
					flightactns.showflightSchedules(mon,yr);
				}
				
				fa = new flightActions();
		  		fa.showSearchBox();
				
			});
			
			flightsMonthBar.onMonthHover();
		},
		
		onMonthHover: function(){
			var flightsMonthBar = this;
			query(".monthSelector div").on("div:mouseover, div:mouseout", function(evt){
				var m = parseInt(domAttr.get(this, "data-cur-mon"));
				if(evt.type === "mouseover"){
					
					if(!domClass.contains(this, "month-sel-active")){
						if(domClass.contains(this, "month-activable")) domClass.remove(this, "month-activable");
						
						domStyle.set(this, {
							"background": "#b5d4f0",
							"color": "#fff",
							"cursor": "pointer",
							"backgroundImage": "url(images/flight-blue.png)",
							"backgroundRepeat": "no-repeat",
							"backgroundPosition": "18px 27px"
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
			var yr = dojo.create("span", { 
				innerHTML:"|" + yr
			});
			domStyle.set(yr, {
				"position":"absolute",
				"top":"-13.796875px",//"145.796875px",
				"left":pos+"px"//"108.578125px"
			});
			domConstruct.place(yr,query(".monthSelector")[0],"last");
		}
		
	});
	
	return tui.flights.view.FlightsMonthBar;
});
