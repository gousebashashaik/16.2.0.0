define("tui/flights/timetable/TimeTableFlyToAutoComplete", [
  "dojo",
  "dojo/io-query",
  "tui/widget/_TuiBaseWidget",
  "tui/widget/mixins/AutoCompleteable",
  "tui/flights/widget/MonthPullDown"
], function(dojo,ioQuery) {

  dojo.declare("tui.flights.timetable.TimeTableFlyToAutoComplete", [tui.widget._TuiBaseWidget, tui.widget.mixins.AutoCompleteable,tui.flights.widget.MonthPullDown], {

	// ---------------------------------------------------------------- autoComplete props

	  targetURL:"",

	  titleProp:'name',

	  // data property field for airport code.
	  valueProp:'id',

	  // searchProperty for key search.
	  searchProperty:'searchKey',

	  maxHeight : 310,

	  searchQuery: true,
    // ---------------------------------------------------------------- autoComplete methods
	  //ws/timetabledeparture?from=&to=&when=&q=**&start=0&count=Infinity
    postCreate: function() {
      var _this = this;
      _this.autoCompleteableInit();
      _this.inherited(arguments);
    },

	onType: function(element, event) {
		  var _this = this;
	      if(dojo.trim(element.value).length >= _this.charNo) {
	    	  _this.targetURL = dojoConfig.paths.webRoot + '/ws/flytoautosuggest';
	      }else if(element.value.length == 0) {
	    	  _this.unSelect();
	    	  _this.renderMonthPullDown();
	      }
	      _this.inherited(arguments);
	},
	onResults: function (data) {
		var _this = this;
		if(_this.multiField && !_this.multiField.isActive() ) return;
		 data = _this.getCustomizedData(data);
		 dojo.removeClass(_this.domNode, "autoLoading");
		 _this.inherited(arguments);
	},

	getCustomizedData: function(data){
		var _this = this,dataObj;
		dataObj = data;
		dojo.forEach(dataObj,function(item){
			if(item.error !== "nomatch") {
				item.name += " (" + item.id + ")";
				if(item.countryCode != "GBR"){
					item.name += ", " + item.countryName
				}
				if(!item.available){
					item.name += " - No flights"
					item.disabled = true;
				}
			}
		})
		return dataObj;
	},
	createQuery: function (element, event) {
	    var _this = this,whenCal,flyFromVal;
	    /*whenCal = dijit.byId("whenCal").getValue();
	    flyFromVal = dijit.byId("timeTableFlyFrom").getValue();*/

	    if (_this.searchQuery) {
	      var query = "?"+ _this.getQueryObj(element);
	      return query;
	    }
	    return null;
	},

	createListElement: function () {
		// summary:
		//      Extends createListElement method, and adds a css class called "autocomplete" to listElement.
		var _this = this;
		_this.inherited(arguments);
		dojo.addClass(_this.listElement, "ms timetable");
	},


	onNoResults: function (listElementUL, data) {
		var _this = this;
		var dataObj = [{"name":"No matches found","id":"","disabled":true,"countryName":"","group":[],"children":[],"synonym":"","countryCode":"","error":"nomatch"}];
		_this.onResults(dataObj);
	},

	createQueryObject: function (element, event) {
	      var autoCompleteable = this;
	     // var searchRegExp = new RegExp([element.value].join(""), "i");
	      var queryObj = {};
	      queryObj[autoCompleteable.searchProperty] = dojo.trim(element.value);
	      return queryObj;

	 },
	 getQueryObj : function(elem){
		 var _this = this,whenCal=[],flyFromVal,selectedDate,firstAndLastDate=[],searchProperty;
		 whenCal = dijit.byId("whenTimeTable").getSelectedData().value !== "" ? dijit.byId("whenTimeTable").getSelectedData().value.split("/") : [];
		 if(whenCal.length>0){
			 selectedDate = new Date(parseInt(whenCal[1]),parseInt(whenCal[0])-1);
			 firstAndLastDate = _this.getFirstAndLastDate(selectedDate);
		 }
		 flyFromVal = dijit.byId("timeTableFlyFrom").getValue();
		 searchProperty =  _this.searchProperty;
		  return ioQuery.objectToQuery({
				 "startDate" : firstAndLastDate[0] == undefined ? "" : firstAndLastDate[0],
				 "endDate" : firstAndLastDate[1] == undefined ? "" : firstAndLastDate[1],
				 "from[]"   : flyFromVal,
				 "searchKey" : elem == undefined ? "" :  elem.value
		 });
	 },
	 onHideList: function(){
			var _this = this;
			_this.clearHighlight();
	 },
	 query: function () {
			// summary:
			//      Extends query to adding loading class.
			var _this = this;
			dojo.addClass(_this.domNode, "autoLoading");
			_this.inherited(arguments);
	},
	getFirstAndLastDate: function(date){
		var _this = this,startAndEndDate=[],startDate,endDate;
		startDate = dojo.date.locale.format(date, {selector: "date",datePattern: "yyy-MM-dd"});
		endDate = dojo.date.locale.format(new Date(date.setDate(dojo.date.getDaysInMonth(date))), {selector: "date",datePattern: "yyy-MM-dd"});

		return [startDate,endDate];
	}

});

  return tui.flights.timetable.TimeTableFlyToAutoComplete;
});
