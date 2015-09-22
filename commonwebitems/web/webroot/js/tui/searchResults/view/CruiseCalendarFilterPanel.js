define("tui/searchResults/view/CruiseCalendarFilterPanel", [
  "dojo/_base/declare",
  "dojo/_base/connect",
  "dojo/_base/lang",
  "dojo/parser",
  "dojo/aspect",
  "dojo/topic",
  "dojo/on",
  "dojo/_base/xhr",
  "dojo/mouse",
  "dojo/query",
  "dojo/dom-style",
  "dojo/dom-attr",
  "dojo/dom-construct",
  "dojo/dom-class",
  "dojo/dom-geometry",
  "tui/widget/_TuiBaseWidget",
  "tui/search/nls/Searchi18nable"], function (declare, connect, lang, parser, aspect, topic, on, xhr, mouse, query,
                                              domStyle, domAttr, domConstruct, domClass, domGeom) {




  return declare("tui.searchResults.view.CruiseCalendarFilterPanel", [tui.widget._TuiBaseWidget], {

    // ----------------------------------------------------------------------------- properties

    // ----------------------------------------------------------------------------- holiday rendering methods
	  tag: 370,

	  number: 1,

	 postCreate: function () {
      // initialise search results view
	      var widget = this;

	      query("#monthFilters > li.first", widget.domNode).first().addClass("active");

     query("#monthFilters > li > a", widget.domNode).on("click", function(e){
    	 var urlSplit =  e.target.href.split("#");
    	 query("#monthFilters > li.active").removeClass("active");
    	 query(e.target).parent().addClass("active");
    	 dojo.publish("tui.searchResults.view.CruiseCalendarSearchResultsComponent.disableToggle", dojo.byId(urlSplit[1]));
    	 dojo.publish("tui.searchResults.view.CruiseCalendarSearchResultsComponent.openMonth", dojo.byId(urlSplit[1]));

     });
     	widget.tagElement(query(".jump-to", widget.domNode)[0], "Departure Month Options");
     	widget.tagElement(query("#monthFilters", widget.domNode)[0], "depMonthPicker");

     }





      });
});

