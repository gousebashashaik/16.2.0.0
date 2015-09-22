define("tui/filterPanel/view/calendar/CruiseCalendar", [
    "dojo/topic",
    "dojo/_base/declare",
    "dojo/query",
    "dojo/_base/lang",
    "dojo/dom-attr",
    "dojo/date",
    "dojo/dom-class",
    "dojo/_base/array",
    "dojo/on",
    "dojo/dom-construct",
    "dojo/text!tui/filterPanel/view/templates/CruiseCalendar.html",
    "tui/widget/_TuiBaseWidget",
    "dojox/dtl/_Templated",
    "tui/widget/mixins/Templatable",
    "dojo/Evented"
], function (topic, declare, query, lang, domAttr, dateUtils, domClass, arrayUtils, on, domConstruct, flightOptionsCalendarTmpl, _TuiBaseWidget, dtlTemplate, Templatable, Evented) {

    return declare("tui.filterPanel.view.calendar.CruiseCalendar", [_TuiBaseWidget, dtlTemplate, Templatable,  Evented], {

        days: ["M", "T", "W", "T", "F", "S", "S"],
        dates: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31],
        months: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
        tmpl: flightOptionsCalendarTmpl,
        templateString: "",
    	widgetsInTemplate : true,


        postMixInProperties: function () {

        	this.firstDate = _.isDate(this.calStartDate) ? this.calStartDate : dojo.clone(dojo.date.locale.parse(this.calStartDate, {selector: "date", datePattern: 'yyyy-MM-dd'}));
        	this.selectedDate = _.isDate(this.selectedDate) ? this.selectedDate : dojo.date.locale.parse(this.selectedDate, {selector: "date", datePattern: 'yyyy-MM-dd'});
        	this.actualDate = _.isDate(this.actualDate) ? this.actualDate : dojo.date.locale.parse(this.actualDate, {selector: "date", datePattern: 'yyyy-MM-dd'});
            this.availDatesArray = _.isString(this.calAvail) ? this.calAvail.split(",") : this.calAvail;
        	this.renderHTMLSetup();
            this.inherited(arguments);
        },
        
    	buildRendering: function(){
    		this.templateString = this.renderTmpl(this.tmpl, this);
    		delete this._templateCache[this.templateString];
    		this.inherited(arguments);
    	},
    	
       
        postCreate: function () {
        	this.daycells = query(".datepicker-day", this.domNode);
        	this.availDateCells = query("td>a", this.domNode);
        	this.attachEvents();
        	query(".selected-date", this.domNode).forEach(function(node){
        		domClass.remove(node, "selected-date");
        	});
        	this.disablePastAndUnavailableDates();
        	var node = query("td[id=td"+this.actualDate.getDate()+"]", this.domNode)[0];
        	this.previousCell = query("td[id=td"+this.selectedDate.getDate()+"]", this.domNode)[0];
        	/*if(this.selectedDate.getMonth() === this.actualDate.getMonth()){
        		if(this.actualDate.getDate() === this.selectedDate.getDate()){
        			domClass.add(this.previousCell, "previous-date");
        		}else {
        			domClass.add(this.previousCell, "selected-date");
        			domClass.add(node, "previous-date");
        		}
        		
            	on(node, "click", lang.hitch(this,function(){
            		if(this.previousCell){
                		domClass.remove(this.previousCell, "selected-date");
                	}
            		topic.publish("flightoptions.selecteddate.display", this.year,this.month, this.actualDate.getDate(), true);
            	}));
        	}else {
        		domClass.add(this.previousCell, "selected-date");
        	}*/
        	
        	this.inherited(arguments);
            this.tagElements(query(".datepickertable", this.domNode), "calPicker");
        },
        
        attachEvents : function () {
        	arrayUtils.forEach(this.availDateCells, lang.hitch(this, function(cell){
        		on(cell.parentNode, "click", lang.hitch(this, this.availCellClicked, cell));
        	}))
        },
        
        availCellClicked : function(cell) {
            if(domClass.contains(cell.parentElement, "selected-date")){
               domClass.remove(cell.parentElement, "selected-date");
               topic.publish("tui.filterpanel.view.calendar.SailingDates", '');
            }else{
                if(this.previousCell){
                    domClass.remove(this.previousCell, "selected-date");
                }

                domClass.add(cell.parentElement, "selected-date");
                this.previousCell = cell.parentElement;
                topic.publish("tui.filterpanel.view.calendar.SailingDates", this.year+'-'+ (this.month+1) +'-'+ parseInt(cell.id,10));
            }
        },
        
        renderHTMLSetup: function () {

            var viewdate = this.selectedDate || dojo.clone(this.firstDate);
            this.datePickerShowDate = new Date(viewdate.getFullYear(), viewdate.getMonth(), 1);
            this.day = (this.datePickerShowDate.getDay() === 0) ? 7 : this.datePickerShowDate.getDay();
            this.totalDays = [31, this.isLeapYear(this.datePickerShowDate.getFullYear()) ? 29 :
                28 , 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
            this.month = this.datePickerShowDate.getMonth();
            this.year = this.datePickerShowDate.getFullYear();
            this.predates = dojo.clone(this.dates);
            this.predates.splice((this.day - 1), 31 - (this.day - 1));
            this.monthdayamount = this.totalDays[this.datePickerShowDate.getMonth()];
            this.monthdates = [];
            var newrow = this.day;

            var availableDates = _.map(this.availDatesArray, function(date){
                return dojo.clone(dojo.date.locale.parse(date, {selector: "date", datePattern: 'yyyy-MM-dd'}))
            });

            for (var i = 0; i < this.monthdayamount; i++) {
                var date = new Date(this.year, this.month, i+1);
                var monthdates = {
                    date: i + 1,
                    newrow: false,
                    available: ! _.isUndefined(_.find(availableDates, function(availabeDate){
                        return (availabeDate.getFullYear() ==  date.getFullYear()) &&
                            (availabeDate.getMonth() === date.getMonth()) &&
                            (availabeDate.getDate() === date.getDate());
                    })) ,
                    price: this.getPrice(i+1, this.month+1, this.year)
                };
                if (newrow > 6) {
                    newrow = 0;
                    monthdates.newrow = true;
                }
                this.monthdates.push(monthdates);
                newrow++;
            }
            this.datePickerShowDate.setDate(this.monthdayamount);
            var endDate = (this.datePickerShowDate.getDay() === 0) ? this.datePickerShowDate.getDay() :
                          7 - this.datePickerShowDate.getDay();
            this.enddates = dojo.clone(this.dates);
            this.enddates.splice(endDate, 31 - endDate);
            this.datePickerShowDate.setDate(1);
        },
        
        getPrice : function(day, month, year){
        	var dayValue = day.toString().length === 1 ? "0"+day : day;
    		var monthValue = month.toString().length === 1 ? "0"+month : month;
        	var index = arrayUtils.indexOf(this.availDatesArray, dayValue+"-"+monthValue+"-" + year);
        	if(index !== -1) {
        		return this.priceResults[index];
        	}
        	
        	return null;
        },
       
        isLeapYear: function (year) {
            return ((year % 100 !== 0) && (year % 4 === 0) || (year % 400 === 0));
        },
        
        disablePastAndUnavailableDates : function () {

            var totalDays = this.totalDays[this.datePickerShowDate.getMonth()];
            var showDate = dojo.clone(this.datePickerShowDate);
            var now = new Date();
            var today = new Date(now.getFullYear(), now.getMonth(), now.getDate());

            for (var i = showDate.getDate(); i <= totalDays; i++) {
                if (dojo.indexOf(this.availDatesArray, this.formatDate(showDate)) === -1) {
                    domClass.add(this.daycells[showDate.getDate() - 1], "unavailable");
                    domClass.add(this.daycells[showDate.getDate() - 1], "disabled");
                }
                showDate.setDate(showDate.getDate() + 1);
            }
        },

        formatDate: function (date) {
            // summary:
            //		Formats date object to match JSON format
            return dojo.date.locale.format(date, {
                selector: "date",
                datePattern: "dd-MM-yyyy"
            });
        }
    });
});
