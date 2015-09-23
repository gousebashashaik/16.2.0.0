define("tui/flights/status/FlyToAutoComplete", [
  "dojo",
  "tui/widget/_TuiBaseWidget",
  "tui/widget/mixins/AutoCompleteable"
], function(dojo) {

  dojo.declare("tui.flights.status.FlyToAutoComplete", [tui.widget._TuiBaseWidget, tui.widget.mixins.AutoCompleteable], {

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

    postCreate: function() {
      var _this = this;
      _this.autoCompleteableInit();
      _this.inherited(arguments);
    },

	onType: function(element, event) {
		  var _this = this;
	      if(dojo.trim(element.value).length >= _this.charNo) {
	    	  _this.targetURL = dojoConfig.paths.webRoot + '/airportsAutoSuggest';
	      }else if(element.value.length == 0) {
	    	  _this.unSelect();
	      }
	      _this.inherited(arguments);
	},
	onResults: function (data) {
		var _this = this;
		if(_this.multiField && !_this.multiField.isActive() ) return;
		 data = _this.getCustomizedData(data);
		 _this.inherited(arguments);
	},

	getCustomizedData: function(data){
		var _this = this,dataObj=[],obj={};
		if(data.ukAirports && data.ukAirports.length > 0){
			obj.name = "UK airports";
			obj.disabled = true;
			dataObj.push(obj);

			dojo.forEach(data.ukAirports,function(item){
				item.name += " ("+ item.id +")"
				/*if(!item.available){
					item.name += " - <i>No Flights<i>"
					item.disabled = true;
				}*/
				dataObj.push(item);
			});
		}
		if(data.osAirports && data.osAirports.length > 0){
			obj = {};
			obj.name = "Overseas airports";
			obj.disabled = true;
			dataObj.push(obj);

			dojo.forEach(data.osAirports,function(item){
				item.name += " ("+ item.id +"), " + item.countryName;
				/*if(!item.available){
					item.name += " - <i>No Flights<i>"
					item.disabled = true;
				}*/
				dataObj.push(item);
			});
		}
		return dataObj;

	},
	createQuery: function (element, event) {
	    var autoCompleteable = this,
	    flightNumVal = dijit.byId("flightNumber").getLabel().replace(" ","");
	    flyFromVal = dijit.byId("flyFromStat").getValue();

	    if (autoCompleteable.searchQuery) {
	      var query = "?depAirPortCode="+ flyFromVal + "&flightNumber=" + flightNumVal + "&"+autoCompleteable.searchProperty + "=" + element.value +"&event=arrival";//["?", autoCompleteable.searchQuery, "=", element.value].join("");
	      return query;
	    }
	    return null;
	},

	createListElement: function () {
		// summary:
		//      Extends createListElement method, and adds a css class called "autocomplete" to listElement.
		var _this = this;
		_this.inherited(arguments);
		dojo.addClass(_this.listElement, "ms status");
		//on show of the auto complete list remvoing if any error popup is there

		dojo.byId("errorpopupArr").style.display = "none";
		dojo.query(_this.domNode.parentNode).removeClass("error");
		dojo.query("input",_this.domNode.parentNode).removeClass("error");
	},


	onNoResults: function (listElementUL, data) {
		var _this = this;
		dojo.byId("errorpopupArr").style.display = "inline-block";
		dojo.query(".focusNode",_this.domNode.parentNode.parentNode).addClass("error");
		dojo.query(_this.domNode).addClass("error");
	},

	createQueryObject: function (element, event) {
	      var autoCompleteable = this;
	     // var searchRegExp = new RegExp([element.value].join(""), "i");
	      var queryObj = {};
	      queryObj[autoCompleteable.searchProperty] = dojo.trim(element.value);
	      return queryObj;
	 },
	 onHideList: function(){
 		var _this = this;
 		_this.clearHighlight();
 	}


});

  return tui.flights.status.FlyToAutoComplete;
});
