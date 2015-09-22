define("tui/widget/booking/flightoptions/view/FlightOptionDatepicker", [
    "dojo/topic",
    "dojo/_base/declare",
    "dojo/query",
    "dojo/_base/lang",
    "dojo/dom-attr",
    "dojo/date",
    "dojo/dom-class",
    "dojo/dom-style",
    "dojo/_base/array",
    "dojo/on",
    "dojo/dom-construct",
    "dojo/text!tui/widget/booking/flightoptions/view/templates/FlightOptionDatepicker.html",
    "tui/widget/_TuiBaseWidget",
    "dojox/dtl/_Templated",
    "tui/widget/mixins/Templatable",
    "dojo/Evented",
    "tui/widget/booking/flightoptions/view/FlightOptionsCalendar"
], function (topic, declare, query, lang, domAttr, dateUtils, domClass, domStyle, arrayUtils, on,
		domConstruct, searchDatePickerTmpl, _TuiBaseWidget, dtlTemplate, Templatable, Evented, FlightOptionsCalendar) {

    return declare("tui.widget.booking.flightoptions.view.FlightOptionDatepicker", [_TuiBaseWidget, dtlTemplate, Templatable,  Evented], {


    	 days: ["M", "T", "W", "T", "F", "S", "S"],
         dates: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31],
         months: ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"],

        totalDays: null,

        datepickerDOM: null,

        displayMonthYearIndex: 0,

        day: null,

        month: 0,

        year: null,

        years: null,

        daycells: [],

        datePattern: "yyyy/mm/dd",

        monthsAndYears: [],

        seasonLength: 18,

        tmpl: searchDatePickerTmpl,

        firstDate: null,

        endDate: null,

        availDates:[],

        templateString: "",

    	widgetsInTemplate : true,

        postMixInProperties: function () {
            this.firstDate = dojo.clone(new Date(parseInt(this.calStartDate,10)));
            this.endDate = dojo.clone(new Date(parseInt(this.calEndDate,10)));
            this.selectedDate = new Date(this.selectedDate);
            this.actualDate = new Date(this.actualDate);
            this.month = this.selectedDate.getMonth();
            this.year = this.selectedDate.getFullYear();
            this.combineMonthsAndYears();
            this.inherited(arguments);
        },

    	buildRendering: function(){
    		this.templateString = this.renderTmpl(this.tmpl, this);
    		delete this._templateCache[this.templateString];
    		this.inherited(arguments);
    	},


        postCreate: function () {
        	this.attachEvents();
        	this.disablePrevArrowBtn();
        	this.displayMonthYearIndex =0;

        	if(this.firstDate.getMonth() == this.month){
        		this.displayMonthYearIndex = 0;
        	}else if(this.firstDate.getMonth() > this.month){
        		this.displayMonthYearIndex = (this.month +12) - this.firstDate.getMonth();
        	}else {
        		this.displayMonthYearIndex = this.month - this.firstDate.getMonth();
        	}

        	if(this.monthsAndYears.length === 1){
        		this.disablePrevArrowBtn();
        		this.disableNextArrowBtn();
        	}else {
        		if(this.displayMonthYearIndex === 0){
        			this.disablePrevArrowBtn();
            	}else {
            		this.enablePrevArrowBtn();
            	}
        		if(this.displayMonthYearIndex === (this.monthsAndYears.length-1)){
        			this.disableNextArrowBtn();
        		}else {
        			this.enableNextArrowBtn();
        		}
        	}
        	if(this.monthsAndYears.length > this.displayMonthYearIndex){
           domAttr.set(this.monthYearLabel, "innerHTML",  this.monthsAndYears[this.displayMonthYearIndex].label);
        	}else{
        		domAttr.set(this.monthYearLabel, "innerHTML",  this.monthsAndYears[0].label);
        	}
           this.daycells = query(".datepicker-day", this.domNode);
           this.inherited(arguments);
           this.tagElements(query(".alt-flight-calendar", this.domNode), "calPicker");
        },


        attachEvents : function () {
        	on(this.prevButton, "click", lang.hitch(this, this.prevButtonClicked));
        	on(this.nextButton, "click", lang.hitch(this, this.nextButtonClicked));
        },

        prevButtonClicked : function (){
        	if(!domClass.contains(this.prevButton, "disabled")){
        		var dateStr = null;
        		domAttr.set(this.monthYearLabel, "innerHTML", this.setMonthAndYearTxt(false));
        		if(this.selectedDate.getMonth() === this.actualDate.getMonth()){
        			dateStr = this.actualDate;
        			this.selectedDate = this.actualDate;
        		}
        		this.renderCalendar();
        		topic.publish("flightoptions.selecteddate.display", dateStr);

        	}
        },

        nextButtonClicked: function () {
        	if(!domClass.contains(this.nextButton, "disabled")){
        		var dateStr = null;
        		domAttr.set(this.monthYearLabel, "innerHTML", this.setMonthAndYearTxt(true));

        		if(this.selectedDate.getMonth() === this.actualDate.getMonth()){
        			dateStr = this.actualDate;
        			this.selectedDate = this.actualDate;
        		}
        		this.renderCalendar();
        		topic.publish("flightoptions.selecteddate.display", dateStr);
        	}
        },

        renderCalendar : function() {
        	if(this.calendar){
        		this.destroyCalendar();
        	}


			this.calendar = new FlightOptionsCalendar(
					{"calStartDate": this.calStartDate,
					 "calAvail": this.calAvail,
					 "price": this.price,
					 "selectedDate" : this.selectedDate,
					 "actualDate" : this.actualDate
					});
        	domConstruct.place(this.calendar.domNode, this.domNode, "last");
        },

        destroyCalendar : function() {
        	this.calendar.destroyRecursive();
        	this.calendar = null;

        },
        setMonthAndYearTxt: function (isNext) {
        	var nextIndex = 0;
        	if(isNext){
        		nextIndex = this.displayMonthYearIndex + 1;
        		if(nextIndex  < this.monthsAndYears.length){
        			this.displayMonthYearIndex++;
        			this.enablePrevArrowBtn();
        			if(this.displayMonthYearIndex == (this.monthsAndYears.length -1)){
        				this.disableNextArrowBtn();
        			}
        		}
        	}else {
        		nextIndex = this.displayMonthYearIndex - 1;
        		if(nextIndex >= 0 ){
            		this.displayMonthYearIndex--;
            		this.enableNextArrowBtn();
            		if(this.displayMonthYearIndex == 0){
        				this.disablePrevArrowBtn();
        			}
            	}
        	}
        	this.selectedDate = new Date(this.monthsAndYears[this.displayMonthYearIndex].year,
					this.monthsAndYears[this.displayMonthYearIndex].month - 1,1);
        	return this.monthsAndYears[this.displayMonthYearIndex].label;

        },


        combineMonthsAndYears: function () {

        	var curMonth = this.firstDate.getMonth() + 1,
             	curYear = this.firstDate.getFullYear(),
             	numberOfMonths = this.monthDiff(this.firstDate, this.endDate);
        	var curMonthYearValue = [curMonth, '/', curYear].join("");
            this.monthsAndYears =[];
            for (var i = 0; i < numberOfMonths; i++) {
                this.monthsAndYears.push({
                    year:  curYear,
                    month: curMonth,
                    label: [this.months[curMonth - 1], ' ', curYear].join('')
                });
                if (curMonth % 12 !== 0) {
                    curMonth++;
                } else {
                    curMonth = 1;
                    curYear++;
                }
            }
        },

        enablePrevArrowBtn: function () {
        	domClass.remove(this.prevButton.parentElement, "disabled");
        },

        disablePrevArrowBtn: function () {
            domClass.add(this.prevButton.parentElement, "disabled");
        },

        enableNextArrowBtn: function () {
        	domClass.remove(this.nextButton.parentElement, "disabled");
        },

        disableNextArrowBtn: function () {
        	domClass.add(this.nextButton.parentElement, "disabled");
        },


        formatDate: function (date) {
            // summary:
            //		Formats date object to match JSON format
            var searchDatePicker = this;
            return dojo.date.locale.format(date, {
                selector: "date",
                datePattern: "dd-MM-yyyy"
            });
        },

        monthDiff: function (date1, date2) {
            // summary:
            // 		returns difference in months between 2 date objects
            var months;
            months = (date2.getFullYear() - date1.getFullYear()) * 12;
            months -= date1.getMonth() +1;
            months += date2.getMonth() +1;
            return months < 0 ? 0 : (months +1);
        }
    });
});
