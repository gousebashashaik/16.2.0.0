define("tui/widget/amendncancel/AmendDatePicker", [
"dojo",
"tui/widget/datepicker/DatePicker",
"dojo/text!tui/widget/amendncancel/templates/AmendDatePicker.html",
"dojo/dom-construct",
"dojo/dom-attr",
"dojo/query",
"dojo/dom-style",
"dojo/dom-class",
"tui/mixins/MethodSubscribable",
"tui/utils/JCalendar"], function (dojo, datePicker, template, domConstruct, domAttr, query, domStyle, domClass, MethodSubscribable) {

	/*
	 * This is a Date Picker which displays 2-month view
	*/
  dojo.declare("tui.widget.amendncancel.AmendDatePicker", [datePicker, MethodSubscribable], {

	  //elementRelativeTo:null,

	  //Properties of Templatable
	  tmpl: template,

	  //Properties of DatePicker widget
	  parentWidget: null,
	  //This property is to store StartDate(Default Date) in Date format
	  now:null,
	  //Calendar starts from this date (specify in dd-mm-yyyy format)
	  startDate:null,

	  //Properties of MethodSubscribable
	  subscribableMethods: ["clearDateField"],

	  //Custom Properties of this widget

	  //If null, no restriction on the calendar(startDate is a must to specif this)
	  seasonLength:null,

	  //EndDate of Calendar decided based on season length (can be overridden for a custom end date)
	  endDate:null,

	  datePickerShowDate2:null,
	  totalDays2:null,
	  day2:null,
	  month2:null,
	  year2:null,
	  predates2:null,
	  monthdates2:null,
	  monthdayamount2:null,
	  enddates2:null,
	  daycells2:null,
	  nextMonthAndYearTxt:null,
	  monthAndYearTxt:null,

	  disablePastDate:false,

	  //OverRided functions of DatePicker

	  postMixInProperties: function () {
	      // summary:
	      // Called before the AmendDatePicker widget is created. Setting values to default states.
		  amendDatePicker = this;
          amendDatePicker.inherited(arguments);
		  //amendDatePicker.elementRelativeTo = this.domNode;
	    },

		postCreate: function(){
			amendDatePicker = this;

			//If startDate is not passed, Past dates are not disabled
			if(amendDatePicker.startDate != null  || amendDatePicker.seasonLength != null){
				amendDatePicker.disablePastDate = true;
			}

			amendDatePicker.inherited(arguments);
			amendDatePicker.parentWidget = amendDatePicker.getParent();

			//End date is calculated on season length attribute
			amendDatePicker.nextMonthAndYearTxt = [amendDatePicker.months[amendDatePicker.month2], ' ', amendDatePicker.year2].join('');
			amendDatePicker.monthAndYearTxt = [amendDatePicker.months[amendDatePicker.month], ' ', amendDatePicker.year].join('');

			endDate = amendDatePicker.monthsAndYears[amendDatePicker.monthsAndYears.length - 1].value.split("/");
			amendDatePicker.endDate = new Date(endDate[1],endDate[0],"");

			//Overriding Selected date to be today's date
			amendDatePicker.selectedDate = new Date();
			amendDatePicker.setup();

			dojo.setAttr(amendDatePicker.domNode, "value", "Select a date");
		},
		onCalendarFocus: function () {
		      var datePicker = this;
		      if (!datePicker.datepickerDOM) {
		        datePicker.renderDatePicker();
		      }
		      datePicker.domNode.placeholder = "";

		      var showDate = dojo.date.locale.parse(datePicker.getFieldValue(), {
		        selector: "date",
		        datePattern: datePicker.datePattern
		      });

		      if (showDate) {
		        showDate.setDate(1);
		        if (datePicker.datePickerShowDate.getTime() !== showDate.getTime()) {
		          datePicker.datePickerShowDate = showDate;
		          datePicker.setDateSelectOption();
		        }
		      }
			  dojo.publish("tui.widget.amendncancel.retrieveBookingPage.RetrieveBookingForm.showHideValidationError",true);
			  dojo.publish("tui.widget.amendncancel.retrieveBookingPage.RetrieveBookingForm.showHideBookingNotFoundError",true);

		      datePicker.open();
		    },

		onAfterTmplRender: function (datepickerHTML) {
			var amendDatePicker = this;

		      if (amendDatePicker.templateview === "datepicker") {
		    	  amendDatePicker.datepickerDOM = dojo.place(dojo.trim(datepickerHTML), document.body);
		        dojo.parser.parse(amendDatePicker.datepickerDOM);
		        dojo.connect(amendDatePicker.datepickerDOM, "onclick", function (event) {
		          dojo.stopEvent(event);
		          amendDatePicker.clearDatePickerTimer();
		        });
		      } else {
		        dojo.place(dojo.trim(datepickerHTML), amendDatePicker.datepickerDOM);
		      }

		      amendDatePicker.enableArrowBtn();
		      amendDatePicker.addDatePickerEventListener();

              //Should highlight today's date(datepicker.noe contains calendar start date)
			  var today = new Date();
		      if (amendDatePicker.datePickerShowDate.getMonth() === today.getMonth() &&
		    		  amendDatePicker.datePickerShowDate.getFullYear() === today.getFullYear()) {
		        var index = today.getDate();
		        amendDatePicker.setClassOnDate(index - 1, "today");
		      }
			  else if(amendDatePicker.datePickerShowDate2.getMonth() === today.getMonth() &&
		    		  amendDatePicker.datePickerShowDate2.getFullYear() === today.getFullYear()) {
		        var index = today.getDate();
		        amendDatePicker.setClassOnDate(index - 1, "today", false, "secondMonth");
			 }

		      if (amendDatePicker.selectedDate) {
		        if (amendDatePicker.datePickerShowDate.getMonth() === amendDatePicker.selectedDate.getMonth() &&
		        		amendDatePicker.datePickerShowDate.getFullYear() === amendDatePicker.selectedDate.getFullYear()) {
		          var index = amendDatePicker.selectedDate.getDate();
		          amendDatePicker.setClassOnDate(index - 1, "selected");
		        }
				else if(amendDatePicker.datePickerShowDate2.getMonth() === amendDatePicker.selectedDate.getMonth() &&
		        		amendDatePicker.datePickerShowDate2.getFullYear() === amendDatePicker.selectedDate.getFullYear()){
				  var index = amendDatePicker.selectedDate.getDate();
		          amendDatePicker.setClassOnDate(index - 1, "selected", false, "secondMonth");
				}
		      }

		      var disableDate = amendDatePicker.now;
		      amendDatePicker.disableUnavailableDates(disableDate, amendDatePicker.disablePastDate);
		      if (amendDatePicker.templateview === "datepicker") {
		        var customDropDown = dojo.query(".custom-dropdown", amendDatePicker.datepickerDOM);
		        customDropDown.forEach(function (element, index) {
		          var selectWidget = registry.getEnclosingWidget(element);
		          amendDatePicker.dateSelectOption[index] = selectWidget;
		        });
		      }

			//Disabling dates after end date
		    if(amendDatePicker.seasonLength){
		    	if(amendDatePicker.year2 == amendDatePicker.endDate.getFullYear() && amendDatePicker.month2 - amendDatePicker.endDate.getMonth() >= 0){
					var rightArrow = dojo.query(".date-next", amendDatePicker.datepickerDOM)[0];
				    dojo.addClass(rightArrow, "disabled");
					if(amendDatePicker.month2 - amendDatePicker.endDate.getMonth() != 0){
						for (var i = 0; i < amendDatePicker.daycells2.length; i++) {
							amendDatePicker.setClassOnDate(i, "disabled", false, "secondMonth");
							amendDatePicker.setClassOnDate(i, "unavailable past", false, "secondMonth");
					      }
					}
				}
		    }
		},

		setup: function (newdate) {
		      var datePicker = this;

		      var viewdate = datePicker.selectedDate || dojo.clone(datePicker.now);

			  datePicker.datePickerShowDate = newdate || new Date(viewdate.getFullYear(), viewdate.getMonth(), 1);

		      //First Month
		      datePicker.totalDays = [31, datePicker.isLeapYear(datePicker.datePickerShowDate.getFullYear()) ? 29 :
		          28 , 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
		      datePicker.day =
		          (datePicker.datePickerShowDate.getDay() === 0) ? 7 : datePicker.datePickerShowDate.getDay();
		      datePicker.month = datePicker.datePickerShowDate.getMonth();
		      datePicker.year = datePicker.datePickerShowDate.getFullYear();
		      datePicker.monthTxt = datePicker.months[datePicker.month];
		      datePicker.years = [];
		      for (var i = 0; i < datePicker.yearRange; i++) {
		        datePicker.years[i] = datePicker.now.getFullYear() + i;
		      }
		      datePicker.predates = dojo.clone(datePicker.dates);
		      datePicker.predates.splice((datePicker.day - 1), 31 - (datePicker.day - 1));
		      datePicker.monthdayamount = datePicker.totalDays[datePicker.datePickerShowDate.getMonth()];
		      datePicker.monthdates = [];
		      var newrow = datePicker.day;
		      for (var i = 0; i < datePicker.monthdayamount; i++) {
		        var monthdates = {
		          date: i + 1,
		          newrow: false
		        };
		        if (newrow > 6) {
		          newrow = 0;
		          monthdates.newrow = true;
		        }
		        datePicker.monthdates.push(monthdates);
		        newrow++;
		      }
		      datePicker.datePickerShowDate.setDate(datePicker.monthdayamount);
		      var endDate = (datePicker.datePickerShowDate.getDay() === 0) ? datePicker.datePickerShowDate.getDay() :
		          7 - datePicker.datePickerShowDate.getDay();
		      datePicker.enddates = dojo.clone(datePicker.dates);
		      datePicker.enddates.splice(endDate, 31 - endDate);
		      datePicker.datePickerShowDate.setDate(1);
		      datePicker.monthAndYearTxt = [datePicker.months[datePicker.datePickerShowDate.getMonth()], ' ',datePicker.datePickerShowDate.getFullYear()].join('');

		      //SecondMonth
		      varDate = newdate || viewdate;
		      if(varDate.getMonth() == 11){
		    	  nextMonth = 0;
		    	  nextMonthYear = varDate.getFullYear() + 1;
		      }
		      else{
		    	  nextMonth = varDate.getMonth() + 1;
		    	  nextMonthYear = varDate.getFullYear();
		      }
		      datePicker.datePickerShowDate2 = new Date(nextMonthYear, nextMonth, 1);
		      datePicker.totalDays2 = [31, datePicker.isLeapYear(datePicker.datePickerShowDate2.getFullYear()) ? 29 :
		          28 , 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];

		      datePicker.day2 =
		          (datePicker.datePickerShowDate2.getDay() === 0) ? 7 : datePicker.datePickerShowDate2.getDay();
		      datePicker.month2 = datePicker.datePickerShowDate2.getMonth();
		      datePicker.year2 = datePicker.datePickerShowDate2.getFullYear();

		      datePicker.predates2 = dojo.clone(datePicker.dates);
		      datePicker.predates2.splice((datePicker.day2 - 1), 31 - (datePicker.day2 - 1));
		      datePicker.monthdayamount2 = datePicker.totalDays[datePicker.datePickerShowDate2.getMonth()];
		      datePicker.monthdates2 = [];
		      var newrow = datePicker.day2;
		      for (var i = 0; i < datePicker.monthdayamount2; i++) {
		        var monthdates2 = {
		          date: i + 1,
		          newrow: false
		        };
		        if (newrow > 6) {
		          newrow = 0;
		          monthdates2.newrow = true;
		        }
		        datePicker.monthdates2.push(monthdates2);
		        newrow++;
		      }
		      datePicker.datePickerShowDate2.setDate(datePicker.monthdayamount2);
		      var endDate = (datePicker.datePickerShowDate2.getDay() === 0) ? datePicker.datePickerShowDate2.getDay() :
		          7 - datePicker.datePickerShowDate2.getDay();
		      datePicker.enddates2 = dojo.clone(datePicker.dates);
		      datePicker.enddates2.splice(endDate, 31 - endDate);
		      datePicker.datePickerShowDate2.setDate(1);
		      datePicker.nextMonthAndYearTxt = [datePicker.months[datePicker.datePickerShowDate2.getMonth()], ' ',datePicker.datePickerShowDate2.getFullYear()].join('');
		    },

		    addDatePickerEventListener: function () {
		        var datePicker = this;

		        //First Month DateCells
		        datePicker.daycells = dojo.query(".month1", datePicker.datepickerDOM);
		        datePicker.daycells.forEach(function (element, index) {
		          datePicker.datePickerConnects.push(dojo.connect(element, "onclick", function (event) {
		            dojo.stopEvent(event);
		            var cell = this;
		            if (dojo.hasClass(cell, "disabled")) {
		              datePicker.clearDatePickerTimer();
		              return;
		            }
		            dojo.query("a", element)[0].focus();
		            datePicker.selectedDate = datePicker.selectedDate || dojo.clone(datePicker.now);
		            datePicker.setClassOnDate(index, "selected", undefined, "firstMonth");
		            datePicker.selectedDate = new Date(datePicker.year,datePicker.month,index + 1);
		            datePicker.setFieldValue(datePicker.selectedDate);

		            if (datePicker.closeOnSelection) {
		              datePicker.close();
		            }
		            datePicker.checkSelectedDate();
		          }));
		        });

		      //Second Month DateCells
		        datePicker.daycells2 = dojo.query(".month2", datePicker.datepickerDOM);
		        datePicker.daycells2.forEach(function (element, index) {
		          datePicker.datePickerConnects.push(dojo.connect(element, "onclick", function (event) {
		            dojo.stopEvent(event);
		            var cell = this;
		            if (dojo.hasClass(cell, "disabled")) {
		              datePicker.clearDatePickerTimer();
		              return;
		            }
		            dojo.query("a", element)[0].focus();
		            datePicker.selectedDate = datePicker.selectedDate || dojo.clone(datePicker.now);
		            datePicker.setClassOnDate(index, "selected",undefined, "secondMonth");
		            datePicker.selectedDate = new Date(datePicker.year2,datePicker.month2,index + 1);
		            datePicker.setFieldValue(datePicker.selectedDate);

		            if (datePicker.closeOnSelection) {
		              datePicker.close();
		            }
		            datePicker.checkSelectedDate();
		          }));
		        });

		        if (datePicker.templateview === "datepicker") {
		          datePicker.connect(datePicker.datepickerDOM, "onclick", function (event) {
		            dojo.stopEvent(event);
		          });

		          var leftArrow = dojo.query(".date-prev", datePicker.datepickerDOM)[0];
		          datePicker.connect(leftArrow, "onclick", function (event) {
		            dojo.stopEvent(event);
		            datePicker.clearDatePickerTimer();
		            if (dojo.hasClass(leftArrow, "disabled")) {
		              return;
		            }
		            //Special Case if season length is defined
		            if(datePicker.seasonLength == null || datePicker.datePickerShowDate.getFullYear() > datePicker.now.getFullYear() || (datePicker.datePickerShowDate.getMonth() - datePicker.now.getMonth()) > 1){
		            	datePicker.datePickerShowDate.setMonth(datePicker.datePickerShowDate.getMonth() - 2);
		            }
		            else{
		            	datePicker.datePickerShowDate.setMonth(datePicker.datePickerShowDate.getMonth() - 1);
		            }
		            datePicker.setDateSelectOption();
		          });

		          var rightArrow = dojo.query(".date-next", datePicker.datepickerDOM)[0];
		          datePicker.connect(rightArrow, "onclick", function (event) {
		            dojo.stopEvent(event);
		            datePicker.clearDatePickerTimer();
		            if (dojo.hasClass(rightArrow, "disabled")) {
		              return;
		            }
		          //Special Case if season length is defined
		            if(datePicker.seasonLength == null || datePicker.endDate.getFullYear() > datePicker.datePickerShowDate2.getFullYear() || (datePicker.endDate.getMonth() - datePicker.datePickerShowDate2.getMonth()) > 1){
		            	datePicker.datePickerShowDate.setMonth(datePicker.datePickerShowDate.getMonth() + 2);
		            }
		            else{
		            	datePicker.datePickerShowDate.setMonth(datePicker.datePickerShowDate.getMonth() + 1);
		            }
		            datePicker.setDateSelectOption();
		          });
		        }
		      },

		      //Here variable month(selects one of the 2 displayed Months)
		      setClassOnDate: function (index, classname, remove, month) {
		          var datePicker = this;
		          if(month === "secondMonth"){
		        	  daycells = datePicker.daycells2;
		          }else{
		        	  daycells = datePicker.daycells;
		          }

		          if (daycells.length === 0) return;
		          remove = (remove === undefined) ? true : remove;
		          if (remove) {
		            dojo.query([".", classname].join(""), datePicker.datepickerDOM).removeClass(classname);
		          }
		          dojo.addClass(daycells[index], classname);
		        },

		        disableDatesByIndex: function (index) {
		            // summary:
		            // 	 Disables datepicker date based on the given index number.
		        	//This has been already handled (OverRiding DatePicker functionality)
		          },

		      disablePastDatesByIndex: function (index) {
		          var datePicker = this;
		          var leftArrow = dojo.query(".date-prev", datePicker.datepickerDOM)[0];
		          dojo.addClass(leftArrow, "disabled");
		          var disableIndex = index || 0;

		          for (var i = 0; i < datePicker.daycells.length; i++) {
		            if ((i + 1) < disableIndex) {
		              datePicker.setClassOnDate(i, "disabled", false);
		              datePicker.setClassOnDate(i, "unavailable past", false);
		            }
		          }
		        },

		        disableUnavailableDates: function (date, past) {
		            // summary:
		            // 		Determines if datepicker needs to be disable
		            //		past or dates in the future, from the given date value.
		            var datePicker = this;
		            if (datePicker.datePickerShowDate.getMonth() === date.getMonth() &&
		                datePicker.datePickerShowDate.getFullYear() === date.getFullYear()) {
		              var index = date.getDate();
		              if (!past) {
		                datePicker.disableDatesByIndex(index);
		              } else {
		                datePicker.disablePastDatesByIndex(index);
		              }
		            } else if (!past && datePicker.datePickerShowDate > date) {
		              datePicker.disableDatesByIndex();
		            } else if (past && datePicker.datePickerShowDate < date) {
		              // TODO: using disablePastDatesByIndex() ??
		              datePicker.disableDatesByIndex();
		            }

		            //Special Case for last day of Month
		            if(datePicker.datePickerShowDate2.getMonth() === date.getMonth() &&
			                datePicker.datePickerShowDate2.getFullYear() === date.getFullYear()){
		            	if(past){
		            		datePicker.disablePastDatesByIndex(datePicker.daycells.length + 1);
		            	}
		            }
		          },

				setDateSelectOption: function () {
				      var datePicker = this;
				      datePicker.redrawDatePicker();
				},

			    deleteDatePicker: function () {
			      var datePicker = this;
			      for (var i = 0; i < datePicker.datePickerConnects; i++) {
			        dojo.disconnect(datePicker.datePickerConnects[i]);
			      }
			      datePicker.datePickerConnects.length = 0;
			      datePicker.daycells.length = 0;
			      dojo.query(".month-indicator",datePicker.datepickerDOM).remove();
			      dojo.query(".datepickertable",datePicker.datepickerDOM).remove();
			    },

			    //Custom Methods of AmendDatePicker
			    //To reset selected date to the start date
			    clearDateField: function(date){
			    	if(!date){
			    		date = amendDatePicker.now;
			    		amendDatePicker.selectedDate = date;
				    	amendDatePicker.datePickerShowDate = new Date(date.getFullYear(),date.getMonth(),1);
				    	amendDatePicker.setDateSelectOption();
				    	domClass.add(dojo.byId("date"), "selected");
			    	}else{
			    		amendDatePicker.selectedDate = date;
				    	amendDatePicker.setFieldValue(amendDatePicker.selectedDate);
				    	domClass.add(dojo.byId("date"), "selected");
			    	}

			    },

			    checkSelectedDate: function () {
					domClass.add(dojo.byId("date"), "selected");
			    	dojo.publish("tui.widget.amendncancel.retrieveBookingPage.RetrieveBookingForm.checkDate");
					dojo.publish("tui.widget.amendncancel.retrieveBookingPage.RetrieveBookingForm.showHideDateValidationError", true);
			    	 /*if(amendDatePicker.domNode != "Select a date"){
			    		 if(dojo.query(".errClass",datePicker.datepickerDOM)[0] != null) {
							  dojo.query(".errClass",datePicker.datepickerDOM)[0].innerHTML = "";
			    		 }
			    		 var img_node = dojo.query('#dateImg')[0];
						  img_node.style.display = "block";
						  dojo.style(dojo.byId('date'),"color","#000000");
						  var cuttoffdate = dojo.query ('#cuttoffdate');
						  if (cuttoffdate && cuttoffdate.length > 0){
						  var newDate = new Date(dojo.byId("cuttoffdate").value); // US12076 - Calendar pre/post departure date search field entry display
						  var startDate = new Date(dojo.byId("date").value);
							var errorDate = null;
							if(startDate < newDate){
								errorDate = domConstruct.toDom("<tr><td><div class='error-notation error-message-text'>You\'ll not be able to access bookings<br>made before "+dojo.byId("cuttoffdate").value+".Please go<br>to BRAC to retrieve the details.</div></td></tr>");
								domConstruct.place(errorDate,  "errDate");
								if(dojo.hasClass(img_node, "image-success")){
									dojo.removeClass(img_node, "image-success");
								}
								dojo.addClass(img_node, "image-failure");
								 query(".image-failure").style({
									    marginTop: "-95px",
									    marginLeft: "174px"
								 });
							}else{
								if(dojo.hasClass(img_node, "image-failure")){
									dojo.removeClass(img_node, "image-failure");
								}
								dojo.addClass(img_node, "image-success");
						  query(".image-success").style({
							    marginTop: "-31px",
							    marginLeft: "166px"
							  });
							}
		               }
			    	 }*/
			    }
  	});

  return tui.widget.amendncancel.AmendDatePicker;

  });

