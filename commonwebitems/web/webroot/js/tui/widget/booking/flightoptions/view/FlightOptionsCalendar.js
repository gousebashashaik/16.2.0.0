define("tui/widget/booking/flightoptions/view/FlightOptionsCalendar", [
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
    "dojo/text!tui/widget/booking/flightoptions/view/templates/FlightOptionsCalendar.html",
    "tui/widget/_TuiBaseWidget",
    "dojox/dtl/_Templated",
    "tui/widget/mixins/Templatable",
    "dojo/Evented"
], function (topic, declare, query, lang, domAttr, dateUtils, domClass, arrayUtils, on, domConstruct, flightOptionsCalendarTmpl, _TuiBaseWidget, dtlTemplate, Templatable, Evented) {

    return declare("tui.widget.booking.flightoptions.view.FlightOptionsCalendar", [_TuiBaseWidget, dtlTemplate, Templatable,  Evented], {

        days: ["M", "T", "W", "T", "F", "S", "S"],
        dates: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31],
        months: ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"],
        tmpl: flightOptionsCalendarTmpl,
        templateString: "",
    	widgetsInTemplate : true,


        postMixInProperties: function () {
            
        	this.firstDate = dojo.clone(new Date(parseInt(this.calStartDate,10)));
        	this.selectedDate = new Date(this.selectedDate);
        	this.actualDate = new Date(this.actualDate);
        	this.priceResults = this.price.split(',');
         	this.availDatesArray = this.calAvail.split(",");
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
        	if(this.selectedDate.getMonth() === this.actualDate.getMonth()){
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
            		this.availCellClicked(this.availDateCells[0]);
            		
            	
        	}
        	
        	this.inherited(arguments);
            this.tagElements(query(".datepickertable", this.domNode), "calPicker");
        },
        
        attachEvents : function () {
        	arrayUtils.forEach(this.availDateCells, lang.hitch(this, function(cell){
        		on(cell.parentNode, "click", lang.hitch(this, this.availCellClicked, cell));
        	}))
        },
        
        availCellClicked : function(cell) {
        	if(this.previousCell){
        		domClass.remove(this.previousCell, "selected-date");
        	}
        	 
        	domClass.add(cell.parentElement, "selected-date");
        	this.previousCell = cell.parentElement;
        	topic.publish("flightoptions.selecteddate.display", this.year, this.month, parseInt(cell.id,10), true);
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
            for (var i = 0; i < this.monthdayamount; i++) {
                var monthdates = {
                    date: i + 1,
                    newrow: false,
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
