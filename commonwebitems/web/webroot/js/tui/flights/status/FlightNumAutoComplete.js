define("tui/flights/status/FlightNumAutoComplete", [
  "dojo",
  "tui/widget/_TuiBaseWidget",
  "tui/widget/mixins/AutoCompleteable"
], function(dojo) {

  dojo.declare("tui.flights.status.FlightNumAutoComplete", [tui.widget._TuiBaseWidget, tui.widget.mixins.AutoCompleteable], {

	// ---------------------------------------------------------------- autoComplete props

	  targetURL:"",

	  titleProp:'flightnr',

	  // data property field for airport code.
	  valueProp:'id',

	  // searchProperty for key search.
	  searchProperty:'searchKey',

	  charNo: 4,

	  searchQuery: true,
    // ---------------------------------------------------------------- autoComplete methods

    postCreate: function() {
      var _this = this;
      _this.autoCompleteableInit();
      _this.inherited(arguments);
    },

    isScrollable: function () {
        var listable = this;
        if (!listable.scrollable) {
          return;
        }
        // cache existing display and opacity attributes
        var defaultDisplay = dojo.style(listable.listElement, "display");
        var defaultOpacity = dojo.style(listable.listElement, "opacity");

        // hide it temporarily
        dojo.setStyle(listable.listElement, {
          "opacity": 0,
          "display": "block"
        });

        // calculate the height
        var height = dojo.style(listable.listElement, "height");

        // if it's > maxHeight
        // set the maxHeight height on it
        if (height > listable.maxHeight) {
          dojo.style(listable.listElement, "height", listable.maxHeight + "px");
          dojo.addClass(listable.listElement, "scroller");
          // hook for widgets to do stuff before the scrollPanel is attached
          listable.onBeforeAddScrollerPanel();
          // fire Scrollable method
          listable.addScrollerPanel(listable.listElement);
          if(listable.listElementUL.childNodes.length===9){
          	dojo.style(listable.listElement, "height", 252 + "px");
          }
        }

        // and show the element again
        dojo.setStyle(listable.listElement, {
          "opacity": defaultOpacity,
          "display": defaultDisplay
        });
      },

	onType: function(element, event) {
		  var _this = this,value;
		  value = dojo.trim(element.value);
		  if(value.toUpperCase().indexOf("TOM") != -1){
			  value = dojo.trim(element.value.substring(0,3)) + dojo.trim(element.value.substring(3));
      		}

		  // only fire a query if user has typed at least `charNo` characters
	      // else hide the list if showing
	      if (value.length >= _this.charNo) {
	    	_this.targetURL = dojoConfig.paths.webRoot + '/flightNumberAutoSuggestData';
	    	var queryObj = _this.createQueryObject(element, event);
	        var query = _this.createQuery(element, event);

	        _this.query(queryObj, {
	          searchQuery: query
	        });
	      } else {
	    	  if(value.length == 0) {
		    	  _this.unSelect();
		      }
	    	  _this.hideList();
	      }

	      /*if(value.length >= _this.charNo) {
	    	  _this.targetURL = dojoConfig.paths.webRoot + '/flightNumberAutoSuggestData';
	      } else if(element.value.length == 0) {
	    	  _this.unSelect();
	      }
	      _this.inherited(arguments);*/
	},
	onResults: function (data) {
		var _this = this;
		if(_this.multiField && !_this.multiField.isActive() ) return;
		 data = _this.getCustomizedData(data);
		 _this.inherited(arguments);
	},

	getCustomizedData: function(data){
		var _this = this
		return data.items;

	},
	createQuery: function (element, event) {
	    var autoCompleteable = this,
	    flyFromVal = dijit.byId("flyFromStat").getValue(),
	    flyToVal = dijit.byId("flyToStat").getValue(),
	    value = dojo.trim(element.value.substring(0,3)) + dojo.trim(element.value.substring(3));

	    if (autoCompleteable.searchQuery) {
	      var query = "?arrAirPortCode="+ flyToVal + "&depAirPortCode=" + flyFromVal +"&"+autoCompleteable.searchProperty + "=" + value;//["?", autoCompleteable.searchQuery, "=", element.value].join("");
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

		dojo.byId("errorpopup").style.display = "none";
		dojo.query(_this.domNode.parentNode).removeClass("error");
		dojo.query("input",_this.domNode.parentNode).removeClass("error");
	},


	onNoResults: function (listElementUL, data) {
		var _this = this;
		dojo.byId("errorpopup").style.display = "inline-block";
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

  return tui.flights.status.FlightNumAutoComplete;
});
