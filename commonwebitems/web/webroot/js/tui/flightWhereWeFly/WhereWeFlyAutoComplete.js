define("tui/flightWhereWeFly/WhereWeFlyAutoComplete", [
  "dojo",
  "tui/searchPanel/view/ErrorPopup",
  "tui/searchPanel/view/flights/AirportListGrouping",
  "tui/widget/_TuiBaseWidget"
], function(dojo,ErrorPopup) {

  dojo.declare("tui.flightWhereWeFly.WhereWeFlyAutoComplete", [tui.widget._TuiBaseWidget, tui.widget.mixins.AutoCompleteable], {

	// ---------------------------------------------------------------- autoComplete props

	  targetURL:"",

	  titleProp:'name',

	  // data property field for airport code.
	  valueProp:'id',

	  // searchProperty for key search.
	  searchProperty:'key',

	  searchQuery: true,
    // ---------------------------------------------------------------- autoComplete methods

    postCreate: function() {
      var _this = this;
      _this.autoCompleteableInit();
      _this.inherited(arguments);
    },

	onType: function(element, event) {
		  var _this = this;
	      if(element.value.length >= _this.charNo) {
	    	  _this.targetURL = dojoConfig.paths.webRoot + '/ws/whereweflyFilter';
	      }else if(element.value.length == 0) {
	    	  _this.unSelect();
	      }
	      _this.inherited(arguments);
	},
	onResults: function (data) {
		var _this = this;
		if(_this.multiField && !_this.multiField.isActive() ) return;
		 data = _this.getCustomizedData(data);
		 dojo.removeClass(_this.domNode, "autoLoading");
		 if(data === false) return;
		 _this.inherited(arguments);

	},

	onChange : function(){
		var _this = this,url,ajaxCall,seasonsList,selectedVal;
		 if(this.getSelectedData() !== null){
			 dojo.query(".clear-filter").removeClass("disabled");
			 whereWeFlyController = dijit.byId("foWhereWeFlyController");
			 whereWeFlyController.getValidAirportData();

		 }
		 _this.inherited(arguments);
		 selectedVal = _this.getSelectedData() == null ? "" : _this.getSelectedData().value;
		 url = dojoConfig.paths.webRoot+"/ws/whereweflyseasons?from[]=" + selectedVal;
		 ajaxCall = dijit.byId("foWhereWeFlyController").mapPanelModel.doXhrGet(url);
		 ajaxCall.then(function(data){
			 dijit.byId("foWhereWeFlyController").seasonsList = data;
			 dijit.byId("foWhereWeFlyController").renderSeasonsList(true);
		 })
	},
	getCustomizedData: function(data){
		var _this = this,dataObj=[],ukAirports=[];
		if(data === null || data.length === 0){
			var dataObj = [{"name":"No matches found","id":"","disabled":true,"countryName":"","group":[],"children":[],"synonym":"","countryCode":""}];
			return dataObj;
		};
		//dataObj = _this.getAirportSwapList(data);
		dojo.forEach(data,function(item){
			if(item.countryCode == "GBR"){
				item.name += " (" + item.id + ")";
				if(!item.available){
					item.name += " - No flights"
					item.disabled = true;
				}
				dataObj.push(item);
			}
		});
		return dataObj;

	},


	createListElement: function () {
		// summary:
		//      Extends createListElement method, and adds a css class called "autocomplete" to listElement.
		var _this = this;
		_this.inherited(arguments);
		dojo.addClass(_this.listElement, "ms where-we-fly");
	},

	onNoResults: function (listElementUL, data) {
		var _this = this;
		var dataObj = [{"name":"No matches found","id":"","available":false,"countryName":"","group":[],"children":[],"synonym":"","countryCode":""}];
		_this.onResults(dataObj);
	},

	createQueryObject: function (element, event) {
		 var _this = this;
	     // var searchRegExp = new RegExp([element.value].join(""), "i");
	      var queryObj = {};
	      queryObj[_this.searchProperty] = dojo.trim(element.value);
	      return queryObj;
	 },
	 createQuery: function (element, event) {
		 var _this = this,startDate="",endDate="";
		 	if(_this.getSeasons().length>0){
		 		startDate = _this.getSeasons()[0];
		 		endDate = _this.getSeasons()[1];
		 	}
		    if (_this.searchQuery) {
		      var query = "?startDate=" + startDate + "&endDate="+ endDate  +"&"+_this.searchProperty + "=" + element.value;//["?", autoCompleteable.searchQuery, "=", element.value].join("");
		      return query;
		    }
		    return null;
	},

	getSeasons: function(){
		var _this = this,season,seasonValue=[];
		season = dijit.byId("seasonSelect");
		if(season.getSelectedIndex() !== 0 && season.getSelectedIndex() !== -1){
			seasonValue =  season.getSelectedData().value.split(",");
		}
		return seasonValue;
	},
	 query: function () {
			// summary:
			//      Extends query to adding loading class.
			var _this = this;
			dojo.addClass(_this.domNode, "autoLoading");
			_this.inherited(arguments);
	},
	onHideList: function(){
		var _this = this;
		_this.clearHighlight();
	}
});

  return tui.flightWhereWeFly.WhereWeFlyAutoComplete;
});
