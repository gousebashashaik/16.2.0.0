define("tui/searchPanel/view/cruise/CruiseSearchSummary", [
  "dojo/_base/declare",
  "dojo/on",
  "dojo/dom-attr",
  "dojo/query",
  "tui/searchPanel/view/SearchSummary"], function (declare, on, domAttr, query) {

   return declare("tui.searchPanel.view.cruise.CruiseSearchSummary", [tui.searchPanel.view.SearchSummary], {

    // ----------------------------------------------------------------------------- methods

	   handleSummaryData: function () {
	      // summary:
	      //		Collect data from model and insert to html
	      var searchSummary = this;
	
	      // set text on targets
	      searchSummary.whereFromNode.innerHTML = searchSummary.formatMultifieldSummary("from");
	     query(".where-from-summary-print", searchSummary.domNode)[0].innerHTML = searchSummary.getPrintSummary("from");
	      
	      searchSummary.whereToNode.innerHTML = searchSummary.formatMultifieldSummary("to");
	      query(".where-to-summary-print", searchSummary.domNode)[0].innerHTML = searchSummary.getPrintSummary("to");
	      // show flexible notifier with date if flexible === true
	     query(".when-summary", searchSummary.domNode).text(searchSummary.searchPanelModel.get("date") +
	      (searchSummary.searchPanelModel.flexible ? (" (+/- " + searchSummary.searchPanelModel.flexibleDays + " days)") : ""));
	      
	     query(".cruise-duration-summary", searchSummary.domNode).text(searchSummary.searchPanelModel.get("duration"));
	     query(".cruise-stay-summary", searchSummary.domNode).text(searchSummary.searchPanelModel.get("addAStay"));
	      
	     query(".total-adults", searchSummary.domNode).text(searchSummary.searchPanelModel.get("adults"));
	     query(".total-seniors", searchSummary.domNode).text(searchSummary.searchPanelModel.get("seniors"));
	     query(".total-children", searchSummary.domNode).text(searchSummary.searchPanelModel.get("children"));
	
	      var whereFromCount =query('.more-count', searchSummary.whereFromNode)[0],
	          whereToCount =query('.more-count', searchSummary.whereToNode)[0];
	      whereFromCount ? searchSummary.tagElement(whereFromCount, 'Airport From X More') : null;
	      whereToCount ? searchSummary.tagElement(whereToCount, 'Destination To X More') : null;
	   }
  });
});