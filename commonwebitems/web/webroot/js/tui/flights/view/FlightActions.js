define("tui/flights/view/FlightActions", [
	"dojo",
	'dojo/_base/lang',
	'dojo/dom-construct',
	"tui/flights/widget/FlightTimetableSearchBox",
	'dojo/dom-class',
	'dojo/dom-style',
	'dojo/dom-attr',
	'dojo/_base/window',
	'dojo/query',
	'tui/flights/view/FlightScheduler',
	"dojo/_base/array",
	"dojo/NodeList-traverse",
	'dojo/domReady!'], function (dojo, lang, domConstruct, flightTimetableSearchBox, domClass, domStyle, domAttr, win, query, flightScheduler,array) {

	dojo.declare("tui.flights.view.FlightActions",[tui.flights.view.FlightScheduler], {

		constructor: function(){
			var flightActions = this;

		},

		hover: function(evt){
			/* hover over scheduler */
			var flightActions = this;
			query(".spacer-center table.weekClass tbody.schdbody td.schdtd").on("mouseover, mouseout,click", function(evt){
				var cell = query(this),
					departureDate = cell.children(".schdt").children(".date")[0],
					departureTimeArray = cell.children(".flight-schds").children(".schd-time"),
					leftMostCell = cell.siblings()[0],
					topMostCell = cell.parents(".schdbody").siblings().children().children();
				if(evt.type === "mouseover"){
						if(departureTimeArray.length>0){
							dojo.addClass(cell[0],"active");
							dojo.addClass(leftMostCell,"leftMostCell-active");
							dojo.addClass(topMostCell[this.cellIndex],"topMostCell-active");
						}
				}else if(evt.type === "mouseout"){
						if(departureTimeArray.length>0){
							dojo.removeClass(cell[0],"active");
							dojo.removeClass(leftMostCell,"leftMostCell-active");
							dojo.removeClass(topMostCell[this.cellIndex],"topMostCell-active");
						}
				}
			});
		},

		showflightSchedules: function(mon, yr){
			var flightActions = this;
			Scheduler  = new flightScheduler(mon,yr);
			Scheduler.checkAvailableDates(mon, yr);
			Scheduler.generateHTML();
			query(".spacer-center")[0].innerHTML = "";
			query(".spacer-center")[0].innerHTML = Scheduler.getHTML();
			dojo.parser.parse(query(".spacer-center")[0]);
			flightActions.showSearchBox();
			if(!dojo.isIE){
				flightActions.hover();
			}

		},
		showSearchBox: function(){
			var flightActions = this;
			console.log("flightActions")
			query(".weekClass tbody td").on("click", function(){
				if(query(".flight-schds",this).length == 0)return;
				var cell = query(this),
					departureDate = cell.children(".schdt").children(".date")[0],
					departureTimeArray = cell.children(".flight-schds").children(".schd-time"),
					leftMostCell = cell.siblings()[0],
					topMostCell = cell.parents(".schdbody").siblings().children().children();
				//remove previous classes
				query(".click-active").removeClass("click-active");
				query(".leftMostCell-click-active").removeClass("leftMostCell-click-active");
				query(".topMostCell-click-active").removeClass("topMostCell-click-active");
				if(departureTimeArray.length>0){
					dojo.addClass(cell[0],"click-active");
					dojo.addClass(leftMostCell,"leftMostCell-click-active");
					dojo.addClass(topMostCell[cell[0].cellIndex],"topMostCell-click-active");
				}
				var monthSelec = query("div.timeTableMonthSelector div.month-sel-active")[0];
				  var curMon = parseInt(domAttr.get(monthSelec, "data-cur-mon"));
				  var curYear = parseInt(domAttr.get(monthSelec, "data-cur-yr"));
				  var curDay = parseInt(query("input[type='hidden']",this)[0].value);
				  var selectedDepartureDate = new Date(curYear,curMon,curDay);
				  dojo.global.selectedDepartureDate = selectedDepartureDate;
				var fstb = new flightTimetableSearchBox({
					elm: this,
					selectedDepartureDate : selectedDepartureDate
				});

	  		});
		}
	});
	return tui.flights.view.FlightActions;
});