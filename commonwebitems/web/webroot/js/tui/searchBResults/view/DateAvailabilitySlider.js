define('tui/searchBResults/view/DateAvailabilitySlider', [
    'dojo',
    'dojo/date/locale',
    'dojo/on',
    'dojo/query',
    'dojo/dom-class',
    "dojo/topic",
    'dojo/NodeList-data',
    'tui/widget/form/sliders/Slider'
], function(dojo, locale, on, query, domClass, topic) {

    dojo.declare('tui.searchBResults.view.DateAvailabilitySlider', [tui.widget.form.sliders.Slider], {

        // ----------------------------------------------------------------------------- properties

        showMarkerValue: false,

        displayMaker: true,

        snap: true,

        // Retrieved from searchRequest.
        searchDate: null,

        //FIXME
        sliderDateElements: null,

        // Template for value item.
        valueTmpl: "<div class='day unselectable'><span>${day} <span class='date-slide'>${value}</span><span class='month-name hide'>${month}</span><b></b></div>",


        // Left offset between starting date and departure date (Used in tomorrow scenario).

        flexibleDays: 7,

        offsetStartPos: 0,

        toolTipWidgets: null,

        // Minimum value selectable between 2 handles.
        handleConstraint: 1,

        days: ['sun', 'mon', 'tue', 'wed', 'thu', 'fri', 'sat'],

        months: ['JAN', 'FEB', 'MAR', 'APR', 'MAY', 'JUN', 'JUL', 'AUG', 'SEP', 'OCT', 'NOV', 'DEC'],

        dataPath: 'searchResult.availableDates',

        selection: {},

        initialModel: null,

        value: null,

        animateLeftInterval: null,
        animateRightInterval: null,

        animDuration: null,

        oInitialModel: null,

        // ----------------------------------------------------------------------------- methods

        generateRequest: function(field) {
            var dateSlider = this,
                selection = dateSlider.selection,
                request;
            if (field === 'clearAll' || field === 'duration' || field === 'rooms' || field === 'clearRooms') {
                request = {
                    'when': locale.format(dateSlider.searchDate, {
                        selector: 'date',
                        datePattern: 'dd-MM-yyyy'
                    }),
                    'until': '',
                    'flexibility': true //default to true as date slider will appear only for flexiblity TRUE
                };
            } else {
                request = {
                    'when': selection.when,
                    'until': (selection.when === selection.until) ? '' : selection.until,
                    'flexibility': (selection.when !== selection.until)
                };
            }
            if (field === 'dateSlider') {
                request.searchRequestType = 'Dateslider';
            }
            return request;
        },

        highlightAll: function(){
        	var dateAvailabilitySlider = this;
        	_.each(query("li",dateAvailabilitySlider.domNode), function(li){
        		dojo.addClass(li, 'curr-date-highlight');
        	});
        },

        refresh: function(field, oldSelection, newSelection, response, isCached, sliderPPChanged, sliderTotalChanged, dateSliderChanged, searchResponse) {
            var dateAvailabilitySlider = this;
            if (field === 'duration' || field === 'rooms' || field === 'clearAll' || dateSliderChanged) {
                // duration || rooms is a new search

                // set model to new data
                dateAvailabilitySlider.model = response;

                dateAvailabilitySlider.highlightAll();

                // reset availability classes
                dateAvailabilitySlider.setCalendarItemsClass(response);
                // reset steps
                dateAvailabilitySlider.setStep(dateAvailabilitySlider.min, 0);
                dateAvailabilitySlider.setStep(dateAvailabilitySlider.max, 1);
                // update selection dates (when, until)
                dateAvailabilitySlider.changeSelections(response.minValue, response.maxValue);
                // re-animate handles to full range
                dateAvailabilitySlider.animateSliderHandles();
                dateAvailabilitySlider.setToolTip();
            } else {
            	dateAvailabilitySlider.highlightAll();
                // not a new search so just reload reset availability classes based on original values
                dateAvailabilitySlider.setCalendarItemsClass();
                if (isCached) {
            	    if(!searchResponse.searchRequest.flexibility){
            	    	response.minValue = response.minValue ? response.minValue : searchResponse.searchRequest.when;
            	    	response.maxValue = response.maxValue ? response.maxValue : searchResponse.searchRequest.when;
            	    }
                    var newWhen = dojo.date.locale.parse(response.minValue, {
                        datePattern: 'dd-MM-yyyy',
                        selector: 'date'
                    });
                    var newUntil = dojo.date.locale.parse(response.maxValue, {
                        datePattern: 'dd-MM-yyyy',
                        selector: 'date'
                    });
                    var when = dateAvailabilitySlider.searchDate;
	                if(response.minValue && response.maxValue) {
		                dateAvailabilitySlider.selection.when = response.minValue;
		                dateAvailabilitySlider.selection.until = response.maxValue;
	                }

                    var step = step || [(dateAvailabilitySlider.middleOffset + 1), (dateAvailabilitySlider.middleOffset + 2)];

                    var left = step[0] - parseInt((when - newWhen) / (1000 * 60 * 60 * 24), 10);
                    var right = step[1] + parseInt((newUntil - when) / (1000 * 60 * 60 * 24), 10);
                    var value = [left, right];
                    clearInterval(dateAvailabilitySlider.animateLeftInterval);
                    clearInterval(dateAvailabilitySlider.animateRightInterval);
                    dateAvailabilitySlider.animateSliderHandles(value);
                    dateAvailabilitySlider.setToolTip();
                }

                if (dateAvailabilitySlider.oInitialModel) {
                    dateAvailabilitySlider.model = JSON.parse(JSON.stringify(dateAvailabilitySlider.oInitialModel));
                }

            }
        },

        handleNoResults: function(name, oldResults, newResults) {
            var dateAvailabilitySlider = this;
            if (name === 'dateSlider') {
                // reset steps
                dateAvailabilitySlider.setStep(dateAvailabilitySlider.lastStepOnEnd ? dateAvailabilitySlider.lastStepOnEnd[0] : dateAvailabilitySlider.min, 0);
                dateAvailabilitySlider.setStep(dateAvailabilitySlider.lastStepOnEnd ? dateAvailabilitySlider.lastStepOnEnd[1] : dateAvailabilitySlider.max, 1);
                // update selection dates (when, until)
                dateAvailabilitySlider.changeSelections(oldResults.when, oldResults.until);
            }
        },

        postCreate: function() {
            var dateAvailabilitySlider = this;

            // set required date values
            dateAvailabilitySlider.initSliderDates();

            // draw slider
            dateAvailabilitySlider.inherited(arguments);

            // set classes on markers (availability)
            dateAvailabilitySlider.setCalendarItemsClass();

            //Set Tooltip
            dateAvailabilitySlider.setToolTip();

            // Animate cursors.
            dojo.ready(function() {
                dateAvailabilitySlider.animateSliderHandles();
            });

            // Tagging handles element.
            dateAvailabilitySlider.tagElement(dateAvailabilitySlider.domNode, 'Date Availability Slider');
            dateAvailabilitySlider.tagElement(dateAvailabilitySlider.handles[0], 'left-date-handle');
            dateAvailabilitySlider.tagElement(dateAvailabilitySlider.handles[1], 'right-date-handle');
        },

        changeSelections: function( /*String*/ when, /*String*/ until) {
            // summary:
            //  Changes date selections and returns previous selection
            //  @returns old selection
            var dateSlider = this;
            var oldSelection = dateSlider.selection;
            dateSlider.selection = {};
            dateSlider.selection.when = when;
            dateSlider.selection.until = until;
            return oldSelection;
        },

        initSliderDates: function() {
            var dateAvailabilitySlider = this;

            // temporarily assign required data
            var mediatorData = dijit.registry.byId('mediator').registerController(dateAvailabilitySlider, ['searchResult.availableDates', 'searchRequest']);

            // mediatorData[0] === searchResult.availableDates
            dateAvailabilitySlider.model = mediatorData[0];
            dateAvailabilitySlider.initialModel = dojo.clone(mediatorData[0]);

            // mediatorData[1] === searchRequest
            var origSearch = mediatorData[1];

            dateAvailabilitySlider.flexibleDays = origSearch.flexibleDays > 0 ? origSearch.flexibleDays : dateAvailabilitySlider.flexibleDays;
            if (dateAvailabilitySlider.flexibleDays > 8) {
                dojo.addClass(dateAvailabilitySlider.domNode, 'narrow-variant');
            }

            var searchRequestDate = origSearch.when;

            dateAvailabilitySlider.offsetStartPos = dateAvailabilitySlider.flexibleDays;
            dateAvailabilitySlider.steps = (dateAvailabilitySlider.flexibleDays * 2) + 1;
            dateAvailabilitySlider.range = [1, (dateAvailabilitySlider.flexibleDays * 2) + 2];
            dateAvailabilitySlider.sliderDateElements = [];

            // set initial selections based on searchRequestData
            dateAvailabilitySlider.changeSelections(origSearch.when, origSearch.until);

            dateAvailabilitySlider.searchDate = dojo.date.locale.parse(searchRequestDate, {
                datePattern: 'dd-MM-yyyy',
                selector: 'date'
            });

            if (!dateAvailabilitySlider.searchDate) {
                _.debug(dateAvailabilitySlider.id + ": can't render date slider without a valid search date", true);
                return;
            }

            // Check if today date is included inside the range. Don't show dates in the past.
            var dateRange = new Date();
            dateRange = new Date().setDate(dateRange.getDate() + dateAvailabilitySlider.flexibleDays);

            if (dateRange >= dateAvailabilitySlider.searchDate) {
                var daysOffset = dateAvailabilitySlider.flexibleDays -
                    parseInt((dateRange - dateAvailabilitySlider.searchDate) / (1000 * 60 * 60 * 24), 10);
                dateAvailabilitySlider.offsetStartPos = daysOffset;
                dateAvailabilitySlider.steps = daysOffset + dateAvailabilitySlider.flexibleDays + 1;
                dateAvailabilitySlider.range = [1, (daysOffset + dateAvailabilitySlider.flexibleDays) + 2];
            }

            var tempRange = dateAvailabilitySlider.range[1] - dateAvailabilitySlider.range[0];
            dateAvailabilitySlider.middleOffset = tempRange % 2 === 0 ? tempRange / 2 : (tempRange - 1) / 2;

            //calculating the slider animation duration
            dateAvailabilitySlider.animDuration = 1050 / dateAvailabilitySlider.flexibleDays;
        },

        setCalendarItemsClass: function(model) {
            // summary:
            var dateAvailabilitySlider = this,
                date, node, i, sliderDate, lastOfMonth, lastOfDayMonth, sliderDateElementsLength;

            if (dateAvailabilitySlider.searchDate.getDate() <= dateAvailabilitySlider.flexibleDays) {
                lastOfMonth = new Date(dateAvailabilitySlider.searchDate.getFullYear(), dateAvailabilitySlider.searchDate.getMonth(), 0);
            } else {
                lastOfMonth = new Date(dateAvailabilitySlider.searchDate.getFullYear(), dateAvailabilitySlider.searchDate.getMonth() + 1, 0);
            }

            lastOfDayMonth = lastOfMonth.getDate();
            sliderDateElementsLength = dateAvailabilitySlider.sliderDateElements.length - 1;

            model = model || dateAvailabilitySlider.model;

            _.forEach(dateAvailabilitySlider.sliderDateElements, function(item, index) {
                sliderDate = dateAvailabilitySlider.getDisplayDate(index + 1);

                // check what month information is needed to be shown.
                if (lastOfDayMonth === sliderDate.getDate() && index !== sliderDateElementsLength) {
                    i = {
                        s: index,
                        e: index + 1
                    };
                } else if (dateAvailabilitySlider.middleOffset === index && _.isEmpty(i)) {
                    i = {
                        s: index
                    };
                }

                date = dojo.date.locale.format(sliderDate, {
                    selector: 'date',
                    datePattern: 'dd-MM-yyyy'
                });

                node = dojo.query('div', item)[0];
                dojo.addClass(node, 'unselectable');
                var n = dojo.create('div', {
                    innerHTML: '<p>hi</p>'
                });
                dojo.removeClass(node, 'unselectable-outside');

                if (dojo.indexOf(model.availableValues, date) > -1) {
                    dojo.removeClass(node, 'unselectable');
                    dojo.query(node).data('data-date-value', date);
                } else {
                    query(node).closest(".curr-date-highlight").removeClass("curr-date-highlight");
                }
            });
            // Displays month information, if we are in between two months, then we show both months.
            var end;
            for (var prop in i) {
                end = (prop === 'e') ? 'end' : '';
                dojo.query('.month-name', dateAvailabilitySlider.sliderDateElements[i[prop]]).removeClass('hide').addClass(end);
            }
        },

        animateSliderHandles: function( /*Array?*/ step) {
            var dateAvailabilitySlider = this;
            var end = dojo.clone(step ? step : dateAvailabilitySlider.step);

            step = step ? step : [(dateAvailabilitySlider.middleOffset + 1), (dateAvailabilitySlider.middleOffset + 2)];

            dateAvailabilitySlider.setStep(step[0], 0);
            dateAvailabilitySlider.setStep(step[1], 1);
            dateAvailabilitySlider.setSliderTrack();

            var leftHandleValue = dateAvailabilitySlider.step[0];
            var rightHandleValue = dateAvailabilitySlider.step[1];

            dateAvailabilitySlider.animateLeftInterval = setInterval(function() {
                dateAvailabilitySlider.setStep(leftHandleValue--, 0);
                dateAvailabilitySlider.setSliderTrack();
                dateAvailabilitySlider.setIndicators();
                if (leftHandleValue < end[0]) {
                    clearInterval(dateAvailabilitySlider.animateLeftInterval);
                }
            }, dateAvailabilitySlider.animDuration);

            dateAvailabilitySlider.animateRightInterval = setInterval(function() {
                dateAvailabilitySlider.setStep(rightHandleValue++, 1);
                dateAvailabilitySlider.setSliderTrack();
                dateAvailabilitySlider.setIndicators();
                if (rightHandleValue > end[1]) {
                    clearInterval(dateAvailabilitySlider.animateRightInterval);
                }
            }, dateAvailabilitySlider.animDuration);
        },

        renderMarker: function(value) {
            var dateAvailabilitySlider = this;
            return dateAvailabilitySlider.renderValue(value);
        },

        getDisplayDate: function(index) {
            var dateAvailabilitySlider = this;
            var displayDate = dojo.clone(dateAvailabilitySlider.searchDate);
            displayDate.setDate(dateAvailabilitySlider.searchDate.getDate() + (index - (dateAvailabilitySlider.offsetStartPos + 1)));
            return displayDate;
        },

        renderValue: function(index) {
            // summary:
            //		Override method to render calender item.
            var dateAvailabilitySlider = this;

            //var displayDate = dojo.clone(dateAvailabilitySlider.searchDate);
            // get date, which we need to render, taking into account the required offset start date.
            var displayDate = dateAvailabilitySlider.getDisplayDate(index);

            // create date tmpl to be rendered.
            return dojo.string.substitute(dateAvailabilitySlider.valueTmpl, {
                value: displayDate.getDate(),
                day: dateAvailabilitySlider.days[displayDate.getDay()],
                month: dateAvailabilitySlider.months[displayDate.getMonth()]
            });
        },

        onFirstMarkerItemRender: function(li, posLeft, index) {
            // summary:
            //		Override method to reposition calender item.
            var dateAvailabilitySlider = this;
            dojo.addClass(li, 'marker initialBar');
            dojo.setStyle(li, {
                left: '-1px',
                width: dateAvailabilitySlider.stepWidth + 'px'
            });
            var n = dojo.create('div', {
                innerHTML: '<p>hi</p>'
            });
            var n = dojo.create('div', {
                innerHTML: '<p>hi</p>'
            });
            dojo.addClass(li, 'curr-date-highlight');
            // Add calender item to sliderDateElements array.
            dateAvailabilitySlider.sliderDateElements.push(li);
        },

        onMarkerItemRender: function(li, posLeft, index) {
            // summary:
            //		Override method to reposition calender item.
            var dateAvailabilitySlider = this;
              dojo.setStyle(li, 'width', dateAvailabilitySlider.stepWidth + 'px');
            // Add calender item to sliderDateElements array.

            dateAvailabilitySlider.sliderDateElements.push(li);
            var when = searchResponse.searchRequest.when;
            var date = when && when.split('-');
            // if (date.length === 3 && dojo.query('span.date-slide', li)[0].innerHTML === parseInt(date[0])) {
            dojo.addClass(li, 'curr-date-highlight initialBar');
            //   	}

        },

        onLastMarkerItemRender: function(li, posLeft, index) {
            var availableDatesList = dojo.query('.slider-calendar li').length;
            if (availableDatesList > 8 & availableDatesList <= 16) {
            	dojo.query('.slider-calendar li').addClass('marker-7');
                dojo.query('.slider-calendar li div span').addClass('date-slide-7');
            }
            if (availableDatesList > 16) {
                if (availableDatesList > 24) {
                    dojo.query('.slider-calendar ul li').addClass('marker-25');
                } else {
                    dojo.query('.slider-calendar ul li').addClass('marker-17');
                }
                dojo.setStyle(dojo.byId('dateAvailabilitySlider'), 'height', '64px');
            }

            // summary:
            //		Override method to hide to hide last marker.

            if(availableDatesList){
            	domClass.remove(query('.slider-calendar li')[availableDatesList - 2],"initialBar");
            }

            dojo.style(li, 'display', 'none');
        },

        addTracksEventListeners: function() {
            var dateAvailabilitySlider = this;
            on(dateAvailabilitySlider.domNode, on.selector('.marker', 'click'), function(event) {
                var marker = this,
                    startIndex = 0,
                    handleConstraint = 0,
                    dateitem = dojo.query('.day', marker)[0],
                    markerList = dojo.query('.marker', dateAvailabilitySlider.domNode);

                // ignore click if item is unselectable.
                if (dateAvailabilitySlider.isDragging ||
                    dojo.hasClass(dateitem, 'unselectable') ||
                    dojo.hasClass(dateitem, 'unselectable-outside')) {
                    return;
                }
                if (!dateAvailabilitySlider.oInitialModel) {
                    dateAvailabilitySlider.oInitialModel = JSON.parse(JSON.stringify(dateAvailabilitySlider.model));
                }
                _.each(markerList, function(item, i) {
                    dojo.removeClass(item, 'curr-date-highlight');
                });
                dojo.addClass(marker, 'curr-date-highlight');
                startIndex = dojo.indexOf(dateAvailabilitySlider.sliderDateElements, marker);
                handleConstraint = dateAvailabilitySlider.handleConstraint;
                dateAvailabilitySlider.handleConstraint = 0;
                dateAvailabilitySlider.setStep(startIndex + 1, 0);
                dateAvailabilitySlider.setStep(startIndex + 2, 1);
                dateAvailabilitySlider.handleConstraint = handleConstraint;
                dateAvailabilitySlider.onEnd(null, dateAvailabilitySlider.step[0], dateAvailabilitySlider.step);
                //dateAvailabilitySlider.setSliderTrack();
            });
        },

        findValidPosition: function(step) {
            var dateAvailabilitySlider = this,
                dateItem = null;

            var days = dojo.query('.marker', dateAvailabilitySlider.domNode).filter(function(item) {
                dateItem = dojo.query('.day', item)[0];
                return (!dojo.hasClass(dateItem, 'unselectable') && !dojo.hasClass(dateItem, 'unselectable-outside') && !dojo.hasClass(dateItem, 'outside'));
            });
            //US17667 - Date Slider UI FIx for inactive dates
            if (!days[0]) {
                var inactiveSelectedItm = dojo.query('.day:not(.outside)', dateAvailabilitySlider.domNode).first();
                var inactItmIndx = dojo.indexOf(dateAvailabilitySlider.sliderDateElements, inactiveSelectedItm.parent()[0]);
                if (inactItmIndx == 0) {
                    days = dojo.query('.day:not(.unselectable):not(.unselectable-outside)', dateAvailabilitySlider.domNode).first().parent();
                } else {
                    days = dojo.query('.day:not(.unselectable):not(.unselectable-outside)', dateAvailabilitySlider.domNode).last().parent();
                }
            }
            return [dojo.indexOf(dateAvailabilitySlider.sliderDateElements, days.first()[0]) + 1,
                dojo.indexOf(dateAvailabilitySlider.sliderDateElements, days.last()[0]) + 2
            ];
        },

        onEnd: function(handlePos, value, step) {
            var dateAvailabilitySlider = this;
            var newStep = step;
            //US17667 - Date Slider UI FIx for inactive dates
            if (dateAvailabilitySlider.stepRange > 1 && handlePos !== null) {
                newStep = dateAvailabilitySlider.findValidPosition(handlePos, step);
                if (newStep.join('') !== step.join('')) {
                    dateAvailabilitySlider.setStep(newStep[0], 0);
                    dateAvailabilitySlider.setStep(newStep[1], 1);
                    dateAvailabilitySlider.setSliderTrack();
                }
            }
            // Update the departure date on the model.
            var when = dojo.query('div', dateAvailabilitySlider.sliderDateElements[newStep[0] - 1]).data('data-date-value')[0];
            var until = dojo.query('div', dateAvailabilitySlider.sliderDateElements[newStep[1] - 2]).data('data-date-value')[0];
            var oldSelection = dateAvailabilitySlider.changeSelections(when, until);
            dijit.registry.byId('mediator').fire('dateSlider', oldSelection, dateAvailabilitySlider.selection);
        },

        setDateElementsStyle: function() {
            var dateAvailabilitySlider = this,
                mode = 'outside';

            dojo.forEach(dateAvailabilitySlider.sliderDateElements, function(sliderDateElement, i) {
                var dateElement = dojo.query('.day', sliderDateElement)[0];

                mode = (mode === 'outside' && i + 1 === dateAvailabilitySlider.step[0]) ? 'inside' : mode;
                mode = (mode === 'inside' && i + 1 === dateAvailabilitySlider.step[1]) ? 'outside' : mode;

                if (mode === 'outside') {
                    dojo.addClass(dateElement, 'outside');
                    if (dojo.hasClass(dateElement, 'unselectable')) {
                        dojo.removeClass(dateElement, 'unselectable');
                        dojo.addClass(dateElement, 'unselectable-outside');
                    }
                } else {
                    dojo.removeClass(dateElement, 'outside');
                    if (dojo.hasClass(dateElement, 'unselectable-outside')) {
                        dojo.removeClass(dateElement, 'unselectable-outside');
                        dojo.addClass(dateElement, 'unselectable');
                    }
                }
            });
        },

        setIndicators: function() {
            var wrap = dojo.byId('dateAvailabilitySlider');
            var liLen = dojo.query('ul li', wrap).length;
            var liLeft = null,
            	liWidth = null,
            	liCount = -1;
            if (!liLen) {
                return;
            }
            try {
            liLeft = dojo.style(dojo.query('.day:not(.day.outside):first', wrap).parent()[0], 'left');
            liWidth = dojo.style(dojo.query('.day:not(.day.outside):first', wrap).parent()[0], 'width');
            //var liHeight = dojo.style(dojo.query(".day:not(.day.outside):first", wrap).parent()[0], "height");
            liCount = dojo.query('.day:not(.day.outside)', wrap).length;
            } catch(err){
            	console.log("Error in setting date slider");

            }
            if(liLeft && liWidth && liCount > -1){
            var dateGap = 0;
            if (dojo.isIE === 8) {
                dateGap = (liLen === 16) ? 1 : 6 / liLen;

            }
            var width = ((liCount - 1) * liWidth) + parseInt(((liCount - 1) * dateGap));
            if (!dojo.query('.track .runner-indicator', wrap).length) {
                dojo.place("<div class='runner-indicator'></div>", dojo.query('.track', wrap)[0], 'last');
                if (dojo.query('.slider-calendar li').length > 16) {
                    if (dojo.query('.slider-calendar li').length > 24) {
                        dojo.addClass(wrap, 'sliderSize-25');
                    } else {
                        dojo.addClass(wrap, 'sliderSize-17');
                    }
                }
            }
            dojo.style(dojo.query('.track .runner-indicator', wrap)[0], {
                'left': liLeft + 8 + 'px',
                'width': width - 2 + 'px'
            });
            }
        },

        setToolTip: function() {

            var dateAvailabilitySlider = this,
                message = "",
                date = "",
                toolTipSpan = "";
            var lis = dojo.query('#dateAvailabilitySlider > ul > li');
            if (!dateAvailabilitySlider.toolTipWidgets) {
                dojo.forEach(lis, function(li, listIndex) {
                    toolTipSpan = dojo.query('div.day > span', li);
                    date = toolTipSpan[0].innerHTML.replace(/\<\/span\>/ig, '&nbsp;').replace(/(<([^>]+)>)/ig, '').toLowerCase();
                    if (dojo.query(' > .unselectable-outside', li).length || dojo.query(' > .unselectable', li).length) {
                        message = 'No flights are<br>departing on <br><span style=\"text-transform:capitalize;\">' + date + '</span>';
                    } else {
                        message = 'Return flights<br>departing on<br><span style=\"text-transform:capitalize;\">' + date + '</span>';
                    }

                    var toolTipMsg = "floatWhere:'position-top-center', text:'" + message + "'";
                    dojo.attr(toolTipSpan[0], {
                        'data-dojo-type': 'tui.searchBResults.view.Tooltips',
                        'data-dojo-props': toolTipMsg
                    });
                });
                dateAvailabilitySlider.toolTipWidgets = dojo.parser.parse(dojo.query('#dateAvailabilitySlider > ul')[0]);
            } else {
                dojo.forEach(lis, function(li, listIndex) {
                    if (dateAvailabilitySlider.toolTipWidgets[listIndex]) {
                        toolTipSpan = dojo.query('div.day > span', li);
                        date = toolTipSpan[0].innerHTML.replace(/\<\/span\>/ig, '&nbsp;').replace(/(<([^>]+)>)/ig, '').toLowerCase();
                        if (dateAvailabilitySlider.toolTipWidgets[listIndex].popupDomNode) {
                            if (dojo.query(' > .unselectable-outside', li).length || dojo.query(' > .unselectable', li).length) {
                                query(dateAvailabilitySlider.toolTipWidgets[listIndex].popupDomNode).empty().html('<p class="text"> No flights are<br>departing on <br><span style=\"text-transform:capitalize;\">' + date + '</span></p>');
                            } else {
                                query(dateAvailabilitySlider.toolTipWidgets[listIndex].popupDomNode).empty().html('<p class="text"> Return flights<br>departing on<br><span style=\"text-transform:capitalize;\">' + date + '</span></p>');
                            }
                        } else {
                            if (dojo.query(' > .unselectable-outside', li).length || dojo.query(' > .unselectable', li).length) {
                                dateAvailabilitySlider.toolTipWidgets[listIndex].text = 'No flights are<br>departing on <br><span style=\"text-transform:capitalize;\">' + date + '</span>';
                            } else {
                                dateAvailabilitySlider.toolTipWidgets[listIndex].text = 'Return flights<br>departing on<br><span style=\"text-transform:capitalize;\">' + date + '</span>';
                            }
                        }
                    }
                });
            }
        },

        onChange: function(handlePos, value, laststep, step, direction) {
            var dateAvailabilitySlider = this;
            dateAvailabilitySlider.setDateElementsStyle();
            dateAvailabilitySlider.inherited(arguments);
            dateAvailabilitySlider.setIndicators();
            return dateAvailabilitySlider;
        }
    });

    return tui.searchBResults.view.DateAvailabilitySlider;
});