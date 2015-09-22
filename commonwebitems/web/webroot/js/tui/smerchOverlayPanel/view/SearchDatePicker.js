define("tui/smerchOverlayPanel/view/SearchDatePicker", [
    "dojo",
    "dojo/store/Observable",
    "dojo/dom-attr",
    "dojo/text!tui/smerchOverlayPanel/view/templates/monthOptions.html",
    "tui/smerchOverlayPanel/store/DateStore",
    "tui/widget/form/SelectOption",
    "dojo/string",
    "tui/widget/mixins/Templatable",
    "tui/search/nls/Searchi18nable",
    "tui/searchPanel/view/SearchErrorMessaging",
    "tui/widget/form/SelectOption"], function (dojo, Observable, domAttr,monthOptionsTmpl, DateStore, SelectOption) {

    function splitDestinationQuery(query) {
      return query.split(':')[0];
    }

    dojo.declare("tui.smerchOverlayPanel.view.SearchDatePicker", [
        tui.widget.form.SelectOption,
        tui.widget.mixins.Templatable,
        tui.search.nls.Searchi18nable,
        tui.searchPanel.view.SearchErrorMessaging], {

        // ----------------------------------------------------------------------------- properties


        datePattern: null,

        monthsAndYears: [],

        monthAndYearTxt: null,

        seasonLength: 18,

        tmpl: monthOptionsTmpl,

        dateStore: null,

        skipMonth: true,
        
        todayDate:null,
        
        firstAvailableMonth: null,         

        startDate: null,

        endDate: null,

        dateRangePreDefined: false,

        calClass: '',
        
        months: ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"],
        
        years:[],

        defaultDaysAhead: 1,        

        subscribableMethods: ["onCalendarFocus", "resize", "focusCalendar"],

        // ----------------------------------------------------------------------------- methods

        postMixInProperties: function () {
            // summary:
            //		Called before the widget is created. Setting values to default states.
            var searchDatePicker = this;  
            searchDatePicker.seasonLength = searchDatePicker.searchConfig.SEASON_LENGTH;
            searchDatePicker.todayDate = new Date();   
            var date =  searchDatePicker.todayDate;           
                       
            searchDatePicker.startDate = searchDatePicker.startDate ? searchDatePicker.startDate.split('-') : null;
            if(searchDatePicker.startDate == null || searchDatePicker.startDate.length <= 1) {
            	searchDatePicker.startDate = new Date(date.getFullYear(), date.getMonth(), date.getDate());
            }
            else{
            	searchDatePicker.startDate = new Date(searchDatePicker.startDate[2], searchDatePicker.startDate[1] - 1, searchDatePicker.startDate[0]);
            	searchDatePicker.dateRangePreDefined = true;
            }
            
            searchDatePicker.endDate = searchDatePicker.endDate ? searchDatePicker.endDate.split('-') : null;
            if(searchDatePicker.endDate == null || searchDatePicker.endDate.length <= 1) {
            	searchDatePicker.endDate = new Date(date.getFullYear(), date.getMonth() + searchDatePicker.seasonLength - 1  , date.getDate());
            }
            else{
            	searchDatePicker.endDate = new Date(searchDatePicker.endDate[2], searchDatePicker.endDate[1] - 1 , searchDatePicker.endDate[0]);
            	searchDatePicker.dateRangePreDefined = true;
            }
            
            searchDatePicker.genarateMonthYearTxt();
            searchDatePicker.defaultSelectedMonth(searchDatePicker.monthsAndYears);//upadtes which month has to selected default on page load
            var html = searchDatePicker.renderTmpl(searchDatePicker.tmpl,searchDatePicker);            
            dojo.place(html, dojo.byId("get-when"), 'last');
          
                              
            searchDatePicker.inherited(arguments);
        },
        
        genarateMonthYearTxt: function(){
        	var searchDatePicker = this;
        	var startDateMonth = searchDatePicker.startDate.getMonth(),
        		startDateYear = searchDatePicker.startDate.getFullYear(),
        		endDateMonth = searchDatePicker.endDate.getMonth(),
        		enddateYear = searchDatePicker.endDate.getFullYear();
        		
        	var yearRange = enddateYear-startDateYear;
            for(i=0; i<=yearRange; i++){
            	searchDatePicker.years.push(startDateYear+i);
            };
            
            var totalMonths = ((yearRange + 1) * 12) - ((startDateMonth - 1)+(12-(endDateMonth))) || searchDatePicker.seasonLength;
            var month=startDateMonth,year=startDateYear;
            for (var i = 0; i < totalMonths; i++) {           	
            	searchDatePicker.monthsAndYears.push({
                    value: [month + 1, '/', year].join(''),
                    label: [searchDatePicker.months[month], ' ', searchDatePicker.years[year - startDateYear]].join('')                    
                });
                if (month % 11 !== 0 || month == 0) {
                	month++;
                } else {
                	month = 0;
                    year++;
                }
            }
        },
        
        defaultSelectedMonth:function(monthsAndYears){
        	var searchDatePicker = this;
        	var firstAvailableMonthFound = false
        	_.each(searchDatePicker.monthsAndYears,function(monthOption, index){       	
    		if(monthOption.value == searchDatePicker.firstAvailableMonth){
    			searchDatePicker.monthsAndYears[index].defaultMonth = true;
    		}
        	else{
        		searchDatePicker.monthsAndYears[index].defaultMonth = false;
        	}
        	});
        },

        updateMonthAvailability: function(response){
        	var searchDatePicker = this;        	
        	_.each(dojo.query("li",searchDatePicker.listElement),function(monthOption){
        		dojo.removeClass(monthOption,"disabled");
    			if(_.indexOf(response,dojo.attr(monthOption, "dataValue")) == -1){
    				dojo.addClass(monthOption,"disabled");
    			}   			
    		});        	
        },
        
        fireRequest: function(searchPanelModel, startDate, endDate){
        	var searchDatePicker = this;
        	dojo.when(searchDatePicker.dateStore.requestData(searchPanelModel,startDate,endDate), function (response) {
        		searchDatePicker.updateMonthAvailability(response);        		   
        		setTimeout(function(){
        			dojo.removeClass(searchDatePicker.domNode, "loading"); 
        			searchDatePicker.showList();
                }, "10");		
            });
        	
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

        postCreate: function () {
            var searchDatePicker = this;          
            searchDatePicker.datePattern = searchDatePicker.searchConfig.DATE_PATTERN; 
            searchDatePicker.searchPanelModel.flexible = true;            
            searchDatePicker.initSearchMessaging();
            if(searchDatePicker.firstAvailableMonth){
            	searchDatePicker.updateModel(searchDatePicker.firstAvailableMonth);
            }
            
            dojo.connect(searchDatePicker.selectDropdown, "onclick", function (event) {
            	
            	if(searchDatePicker.listShowing){
           		 searchDatePicker.hideList();
           		 return;
            	};
            	//add class to show loader
            	dojo.addClass(searchDatePicker.domNode, "loading");  

            	// setting showOnClick false to not to show the drop down until the response is received 
            	searchDatePicker.showOnClick = false;
            	if(searchDatePicker.dateRangePreDefined){
            		searchDatePicker.fireRequest(searchDatePicker.searchPanelModel, searchDatePicker.formatDate(searchDatePicker.startDate), searchDatePicker.formatDate(searchDatePicker.endDate));
            	}else{
            		searchDatePicker.fireRequest(searchDatePicker.searchPanelModel);
            	}
            });
                       
            searchDatePicker.inherited(arguments);  	         	       
            searchDatePicker.connect(searchDatePicker.domNode, "onclick", function () {
                var dateError = dojo.clone(searchDatePicker.searchPanelModel.searchErrorMessages.get("when"));
                if (dateError.emptyDate) {
                    delete dateError.emptyDate;
                    searchDatePicker.searchPanelModel.searchErrorMessages.set("when", dateError);
                }
                // publish opening event for widgets that need to close
                searchDatePicker.publishMessage("searchBPanel/searchOpening");
            });

            dojo.subscribe("tui/searchPanel/view/AutoComplete/datePicker", function () {
                searchDatePicker.setFieldValue(searchDatePicker.searchMessaging.date.placeholder);
                searchDatePicker.onCalendarFocus();
            });

            dojo.subscribe("tui/searchPanel/searchOpening", function (component) {
                if (!searchDatePicker.selectDropdown) return;
                if (component !== searchDatePicker && searchDatePicker.listShowing) {                    
                    searchDatePicker.hideList();
                }
            });           
            searchDatePicker.tagElement(searchDatePicker.domNode, "when");
        },
        
        displayDateErrorMessage: function (name, oldError, newError) {
            // summary:
            //		Watcher method, displays/removes error message when error messaging model is updated
            var searchDatePicker = this;
            searchDatePicker.validateErrorMessage(newError.emptyDate, {
                errorMessage: newError.emptyDate,
                arrow: true,
                field: "when",
                key: "emptyDate"
            });
        },
              
        onChange: function (name, oldValue, newvalue) {
            var searchDatePicker = this;
            searchDatePicker.inherited(arguments);
            if(oldValue == newvalue){
            	return;
            };
            
            searchDatePicker.updateModel(newvalue.value);                      
        },
        
        updateModel: function(selectedMonth){
        	var searchDatePicker = this;
        	var selectedMonthYear = selectedMonth.split('/');
        	var monthStartDate , monthEndDate;
        	var selectedMonthFirstDate = new Date(selectedMonthYear[1], selectedMonthYear[0]- 1, 1);
        	
        	//Updating Start Date
        	
        	if(selectedMonthYear[0]- 1 != searchDatePicker.startDate.getMonth())
        	 {
        	 if(searchDatePicker.todayDate < selectedMonthFirstDate){
        		 monthStartDate = selectedMonthFirstDate;
        	  }
        	  else{
        		 monthStartDate = new Date(selectedMonthYear[1], selectedMonthYear[0]- 1, searchDatePicker.todayDate.getDate() + 1);
        	  }
        	 }else{
        		 if(searchDatePicker.todayDate < searchDatePicker.startDate){
            		 monthStartDate = searchDatePicker.startDate;
            	  }
            	  else{
            		 monthStartDate = new Date(selectedMonthYear[1], selectedMonthYear[0]- 1, searchDatePicker.todayDate.getDate() + 1);
            	  }
        	 }
        	
        	//Updating End Date
        	 
        	 if(selectedMonthYear[0]- 1 != searchDatePicker.endDate.getMonth())
        	 {
        		 monthEndDate = new Date(selectedMonthYear[1], selectedMonthYear[0], 0);
        	 }else{
        		 monthEndDate = searchDatePicker.endDate;
        	 }
                       
             searchDatePicker.searchPanelModel.date = searchDatePicker.configFormat(monthStartDate);
             searchDatePicker.searchPanelModel.until = searchDatePicker.formatDate(monthEndDate);
            
        },
        
        configFormat: function(date){
        	var searchDatePicker = this;        	
        	return dojo.date.locale.format(date, {
                selector: "date",
                datePattern: searchDatePicker.datePattern
            });       	
        },

        focusCalendar: function () {
            var searchDatePicker = this;
            searchDatePicker.domNode.focus();
            searchDatePicker.onCalendarFocus();
        },

        onCalendarFocus: function (searchApi) {
        	
        	var searchDatePicker = this; 
        	dojo.addClass(searchDatePicker.domNode, "loading");
        	if(searchDatePicker.dateRangePreDefined){
        		searchDatePicker.fireRequest(searchDatePicker.searchPanelModel, searchDatePicker.formatDate(searchDatePicker.startDate), searchDatePicker.formatDate(searchDatePicker.endDate));
        	}else{
        		searchDatePicker.fireRequest(searchDatePicker.searchPanelModel);
        	};            
        }       
    });

    return tui.smerchOverlayPanel.view.SearchDatePicker;
});
