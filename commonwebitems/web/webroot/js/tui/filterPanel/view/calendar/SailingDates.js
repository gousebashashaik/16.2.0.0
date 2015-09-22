define("tui/filterPanel/view/calendar/SailingDates", [
    "dojo",
    "dojo/_base/declare",
    'dojo/query',
    "dojo/_base/lang",
    "dojo/dom-attr",
    "dojo/on",
    "dojo/topic",
    "dojo/text!tui/filterPanel/view/templates/sailingDates.html",
    "dojo/dom-construct",
    "dojo/dom-class",
    "dijit/registry",
    "tui/widget/TagMappingTable",
    "dojo/date/locale",
    "tui/utils/JCalendar",
    "tui/filterPanel/view/calendar/CruiseCalendar",
    "dojox/dtl",
    "dojox/dtl/Context",
    "dojox/dtl/tag/logic",
    "tui/widget/expand/Expandable"], function (dojo, declare, query, lang, domAttr, on, topic, tmpl, domConstruct, domClass, registry, tagMappingTable, locale, JCalendar) {

    return declare("tui.filterPanel.view.calendar.SailingDates", [tui.widget._TuiBaseWidget], {

        dataPath: 'deptDate',

        months: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],

        checkBoxes: null,

        checkBoxGroups: null,

        filterMap: null,

        tmpl : tmpl,

        tagMappingTable: tagMappingTable,

        selectedDate:null,

        selected: false,

        flexible: false,

        //Analytic variables
        tag: '087.B',

        number:1,

        buildFilterMap: function (data) {
            var filterMap = {};
            _.each(data.filters, function (filter) {
                _.each(filter.values, function (parent) {
                    filterMap[parent.id] = parent;
                    _.each(parent.children, function (child) {
                        filterMap[child.id] = child;
                    });
                });
            });
            return filterMap;
        },

        generateRequest: function () {
            var widget = this;
            var request = {};

            var filterId = widget.filterId || widget.id;
            request[filterId] = [];
            //var selectedFilterDate = JCalendar.fromDate(new Date(widget.selectedDate));
            //This is modified for 'backend convenience'. To be modified when the Format is changed.
            request[filterId].push({
                'name': filterId,
                'value': widget.selectedDate,
                'parent': filterId || null,
                'categoryCode': null,
                'disabled': null,
                'selected': widget.selected,
                'countShowing': null,
                'noItineraries': null,
                'id': filterId,
                'availableDates': widget.availableValues
            });
            /*
            request[filterId] = {
                value: widget.selectedDate
            };
            */
            return request;
        },

        postCreate: function () {
            var widget = this;
            var data = widget.getParent().registerFilter(widget);
            if( data ){
                try{
                    widget.availableValues = data.availableValues;
                    widget.flexible = data.flexible;
                    widget.draw(data);
                }catch(err){console.log(err);}
                widget.inherited(arguments);
            }
            topic.subscribe("tui.filterpanel.view.calendar.SailingDates", function (message) {
                widget.selectedDate = message;
                widget.selectionUpdated();
            });

            widget.tagElement(widget.domNode, 'depDatePicker');
        },

        draw: function (data) {
            var widget = this, wrap = dojo.query(".item-content-wrap", widget.domNode),
                placeNode = wrap.length ? wrap[0] : widget.domNode;
            widget.inherited(arguments);
            if (data) {

                widget.initialise(data);

                widget.filterMap = widget.buildFilterMap(data);
                var template = new dojox.dtl.Template(widget.tmpl);
                var context = new dojox.dtl.Context(data);
                var html = template.render(context);
                domConstruct.place(html,  widget.domNode, "only");
                dojo.parser.parse(widget.domNode);

                widget.prevButton = dojo.query(".date-prev", widget.domNode)[0];
                widget.nextButton = dojo.query(".date-next", widget.domNode)[0];

                widget.monthYearLabel = dojo.query(".current-monthyear", widget.domNode)[0];
                widget.calendar = registry.byNode(dojo.query(".cruiseCalendar", widget.domNode)[0]);
                widget.calendarComponent =dojo.query(".calendar-component", widget.domNode)[0];
                widget.position = 1;
                widget.updateCalendarState(data);
                widget.updateSelection();
            }
        },

        destroyWidgets: function () {
            var widget = this;
            _.each(dijit.findWidgets(widget.domNode), function (w) {
                w.destroyRecursive(true);
            });
        },

        refresh: function (field, oldValue, newValue, data, isCached, sliderPPChanged, sliderTotalChanged, sailingDateChanged) {
            var widget = this, filterMap, optionClicked;

            if( data && !isCached && !widget.selected){

                if(widget.calendar){
                    widget.destroyCalendar();
                }
                //updating only available values in the refresh call.
                widget.availableValues = data.availableValues;
                if(!widget.selected)
                try{
                    widget.draw(data);
                }catch(err){console.log(err);}
                widget.inherited(arguments);
            }

            if( data &&  isCached ){
            	if(sailingDateChanged){
                	//If sailingDateChanged, availableValues[0] will be the selected date
            		widget.userSelectedDate = data.availableValues[0];
                	widget.selected = true;
                	var mediator = dijit.registry.byId('mediator');

                    //Updating available values with stored values
                    data.availableValues = widget.availableValues =  (mediator.currentRequest.filters.sailingDate[0] ? mediator.currentRequest.filters.sailingDate[0].availableDates : data.availableValues );
                }else{
                	//updating only available values in the refresh call.
                    widget.availableValues = data.availableValues;
                }

            	if(widget.calendar){
                    widget.destroyCalendar();
                }

                try{
                    widget.draw(data);
                }catch(err){console.log(err);}

                if(sailingDateChanged){
                	widget.selectedDate = widget.userSelectedDate;
                }
                widget.prePopulateMonth(data);
            }

            widget.updateSelection();
        },


        handleNoResults: function (field, oldValue) {
            var widget = this;
            var changedCheckBox = _.filter(widget.checkBoxes, function (checkBox) {
                return checkBox.id === oldValue.checkId;
            });
            if (!_.isEmpty(changedCheckBox)) {
                oldValue['value'] ? changedCheckBox[0].select() : changedCheckBox[0].deselect();
            }
        },

        reset: function (data, reset, response) {
            var widget = this;
            // the 'reset' flag is
            // true in case of Cabin option selections and
            // false in case of 'Clear All Filters'
            reset ? null : widget.clear(data);
        },

        clear: function (data) {
            var widget = this;
            if (data) {
                widget.checkBoxes = [];
                widget.destroyWidgets();

                widget.selected = false;
                widget.selectedDate = '';
                widget.userSelectedDate = '';
                widget.availableValues = data.availableValues;
                widget.draw(data);
            }
        },

        selectionUpdated: function (id, oldValue, newValue) {
            var filter = this;
            filter.selected = filter.selectedDate.length > 0;
            filter.userSelectedDate = filter.selectedDate;
            topic.publish("tui/filterPanel/view/FilterController/applyFilter", {
                'oldValue': { 'value': ''},
                'newValue': { 'value': filter.selectedDate}
            });
        },

        addCheckBox: function (checkBox) {
            var widget = this;
            widget.checkBoxes.push(checkBox);
            if (widget.checkBoxGroups[checkBox.filterId]) {
                widget.checkBoxGroups[checkBox.filterId].push(checkBox);
            } else {
                widget.checkBoxGroups[checkBox.filterId] = [checkBox];
            }
            return widget.filterMap[checkBox.id];
        },

        initialise: function(data){
            var widget = this;

            widget.firstDate = dojo.clone(dojo.date.locale.parse(data.minValue, {selector: "date", datePattern: 'yyyy-MM-dd'}));
            widget.endDate = dojo.clone(dojo.date.locale.parse(data.maxValue, {selector: "date", datePattern: 'yyyy-MM-dd'}));
            widget.selectedDate = dojo.date.locale.parse(data.selecteValue, {selector: "date", datePattern: 'yyyy-MM-dd'});
            widget.actualDate = dojo.date.locale.parse(data.selecteValue, {selector: "date", datePattern: 'yyyy-MM-dd'});
            widget.month = widget.selectedDate.getMonth();
            widget.year = widget.selectedDate.getFullYear();
            widget.combineMonthsAndYears();

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
                    label: [this.months[curMonth - 1], ' ', curYear].join(''),
                    labelMonth: this.months[curMonth - 1]
                });
                if (curMonth % 12 !== 0) {
                    curMonth++;
                } else {
                    curMonth = 1;
                    curYear++;
                }
            }
        },

        updateCalendarState: function(data){
            var widget = this;

            widget.attachEvents();

            widget.displayMonthYearIndex = 1;

            domAttr.set(widget.monthYearLabel, "innerHTML",  widget.monthsAndYears[widget.position].label);
            domAttr.set(widget.prevButton, "innerHTML",  widget.monthsAndYears[widget.position-1].labelMonth);
            domAttr.set(widget.nextButton, "innerHTML",  widget.monthsAndYears[widget.position+1].labelMonth);


            widget.daycells = query(".datepicker-day", widget.domNode);
            widget.inherited(arguments);
            widget.prevDatesAvailable() ? widget.enablePrevArrowBtn() : widget.disablePrevArrowBtn();
            widget.nextDatesAvailable() ? widget.enableNextArrowBtn() : widget.disableNextArrowBtn();
            widget.flexible ? null : [widget.hidePrevArrowBtn(),widget.hideNextArrowBtn()];

        },

        prevDatesAvailable: function () {
           var widget = this;
           var month = (widget.monthsAndYears[widget.position-1].month>9) ? '-'+widget.monthsAndYears[widget.position-1].month : '-0' + widget.monthsAndYears[widget.position-1].month;
           var monthYear = widget.monthsAndYears[widget.position-1].year + month;
           var availableMonthYear = _.find(widget.availableValues, function(availableDate){
              return availableDate.indexOf(monthYear) > -1;
           });
           return !_.isUndefined(availableMonthYear)
        },

        nextDatesAvailable: function () {
           var widget = this;
           var month = (widget.monthsAndYears[widget.position+1].month>9) ? '-'+widget.monthsAndYears[widget.position+1].month : '-0' + widget.monthsAndYears[widget.position+1].month;
           var monthYear = widget.monthsAndYears[widget.position+1].year + month;
           var availableMonthYear = _.find(widget.availableValues, function(availableDate){
              return availableDate.indexOf(monthYear) > -1;
           });
           return !_.isUndefined(availableMonthYear)
        },

        enablePrevArrowBtn: function () {
            var widget = this;
            domClass.remove(widget.prevButton.parentElement, "disabled");
        },

        disablePrevArrowBtn: function () {
            var widget = this;
            domClass.add(widget.prevButton.parentElement, "disabled");
        },

        enableNextArrowBtn: function () {
            var widget = this;
            domClass.remove(widget.nextButton.parentElement, "disabled");
        },

        disableNextArrowBtn: function () {
            var widget = this;
            domClass.add(widget.nextButton.parentElement, "disabled");
        },

        hidePrevArrowBtn: function () {
            var widget = this;
            domClass.add(widget.prevButton.parentElement, "inactive");
        },

        showPrevArrowBtn: function () {
            var widget = this;
            domClass.remove(widget.prevButton.parentElement, "inactive");
        },

        hideNextArrowBtn: function () {
            var widget = this;
            domClass.add(widget.nextButton.parentElement, "inactive");
        },

        showNextArrowBtn: function () {
            var widget = this;
            domClass.remove(widget.nextButton.parentElement, "inactive");
        },

        attachEvents : function () {
            var widget = this;
            on(widget.prevButton, "click", lang.hitch(widget, widget.prevButtonClicked));
            on(widget.nextButton, "click", lang.hitch(widget, widget.nextButtonClicked));
        },

        prePopulateMonth: function(data){
        	var widget = this;
            if((!_.isUndefined(widget.userSelectedDate) ) &&  (!_.isUndefined(data.selecteValue)) ){
                var selectedFilterDate =  JCalendar.fromDate(dojo.date.locale.parse(widget.userSelectedDate, {selector: "date", datePattern: 'yyyy-MM-dd'}));
                var defaultFilterDate =  JCalendar.fromDate(dojo.date.locale.parse(data.selecteValue, {selector: "date", datePattern: 'yyyy-MM-dd'}));
                var selectedMonth = selectedFilterDate.month() + 1;
                var defaultMonth = defaultFilterDate.month() + 1;
                for(var i = 0; i < Math.abs( selectedMonth - defaultMonth) ; i++){
                    if( selectedMonth > defaultMonth){
                        widget.nextButtonClicked();
                    }else{
                        widget.prevButtonClicked();
                    }
                }
            }
        },

        prevButtonClicked : function (){
            var widget = this;
            if(domClass.contains(widget.prevButton.parentNode, "disabled")){
               return;
            }

            if(!domClass.contains(widget.prevButton, "disabled")){
                var dateStr = null;
                widget.position--;

                domAttr.set(widget.monthYearLabel, "innerHTML", widget.setMonthAndYearTxt(false));
                if(widget.selectedDate.getMonth() === widget.actualDate.getMonth()){
                    dateStr = widget.actualDate;
                    widget.selectedDate = widget.actualDate;
                }
                widget.renderCalendar();
                topic.publish("flightoptions.selecteddate.display", dateStr);

            }
        },

        nextButtonClicked: function () {
            var widget = this;
            if(domClass.contains(widget.nextButton.parentNode, "disabled")){
                return;
            }
            if(!domClass.contains(widget.nextButton, "disabled")){
                var dateStr = null;
                widget.position++;

                domAttr.set(widget.monthYearLabel, "innerHTML", widget.setMonthAndYearTxt(true));

                if(widget.selectedDate.getMonth() === widget.actualDate.getMonth()){
                    dateStr = widget.actualDate;
                    widget.selectedDate = widget.actualDate;
                }
                widget.renderCalendar();
                topic.publish("flightoptions.selecteddate.display", dateStr);
            }
        },
        renderCalendar : function() {
            var widget = this;
            if(widget.calendar){
                widget.destroyCalendar();
            }


            widget.calendar = new tui.filterPanel.view.calendar.CruiseCalendar (
                {"calStartDate": widget.calStartDate,
                    "calAvail": _.isUndefined(widget.calAvail) ? widget.availableValues : widget.calAvail,
                    "price": widget.price,
                    "selectedDate" : widget.selectedDate,
                    "actualDate" : widget.actualDate
                });
            domConstruct.place(widget.calendar.domNode, widget.calendarComponent, "last");

            widget.updateSelection();
        },

        updateSelection: function(){
           var widget = this;
           var selectedFilterDate =  _.isUndefined(widget.userSelectedDate) || widget.userSelectedDate == '' ? '' : JCalendar.fromDate(dojo.date.locale.parse(widget.userSelectedDate, {selector: "date", datePattern: 'yyyy-MM-dd'}));
           widget.validUserSelectedDate(selectedFilterDate) ? widget.updateUserSelection(selectedFilterDate) : null ;
        },

        updateUserSelection: function(selectedFilterDate) {
           var widget = this;
           var dayCellToBeSelected = _.find(widget.calendar.daycells, function(num){ return 'td'+selectedFilterDate.dayOfMonth() === num.id; });
           dojo.addClass(dayCellToBeSelected, "selected-date");
           widget.calendar.previousCell = dayCellToBeSelected;
        },

        validUserSelectedDate: function(selectedFilterDate){
           var widget = this;

           //&& (widget.monthsAndYears[widget.position].month ===  )
           return (!_.isUndefined(widget.userSelectedDate)) &&
              (widget.userSelectedDate.length > 0)  &&
              (widget.selected) &&
              (selectedFilterDate.month() + 1 == widget.monthsAndYears[widget.position].month);
        },

        destroyCalendar : function() {
            var widget = this;
            widget.calendar.destroyRecursive();
            widget.calendar = null;

        },

        monthDiff: function (date1, date2) {
            // summary:
            // 		returns difference in months between 2 date objects
            var months;
            months = (date2.getFullYear() - date1.getFullYear()) * 12;
            months -= date1.getMonth() +1;
            months += date2.getMonth() +1;
            return months < 0 ? 0 : (months +1);
        },

        setMonthAndYearTxt: function (isNext) {
            var widget = this;

            if(widget.position == 1) {
                widget.restoreFilterHeader();
                widget.showPrevArrowBtn();
                widget.showNextArrowBtn();

                widget.prevDatesAvailable() ? widget.enablePrevArrowBtn() : widget.disablePrevArrowBtn();
                widget.nextDatesAvailable() ? widget.enableNextArrowBtn() : widget.disableNextArrowBtn();
            }
            else{
                if(isNext){
                   widget.disableNextArrowBtn();
                   widget.hideNextArrowBtn();
                   widget.enablePrevArrowBtn();
                   domAttr.set(widget.prevButton, "innerHTML",  widget.monthsAndYears[widget.position-1].labelMonth);
                }
                else{
                   widget.disablePrevArrowBtn();
                   widget.hidePrevArrowBtn();

                    widget.enableNextArrowBtn();
                   domAttr.set(widget.nextButton, "innerHTML",  widget.monthsAndYears[widget.position+1].labelMonth);
                }
            }


            widget.selectedDate = new Date(widget.monthsAndYears[widget.position].year,
                widget.monthsAndYears[widget.position].month - 1,1);
            return widget.monthsAndYears[widget.position].label;

        },

        restoreFilterHeader: function(){
            var widget = this;
            domAttr.set(widget.prevButton, "innerHTML",  widget.monthsAndYears[widget.position-1].labelMonth);
            domAttr.set(widget.nextButton, "innerHTML",  widget.monthsAndYears[widget.position+1].labelMonth);

            widget.enableNextArrowBtn();
            widget.enablePrevArrowBtn();
        }
    });
});