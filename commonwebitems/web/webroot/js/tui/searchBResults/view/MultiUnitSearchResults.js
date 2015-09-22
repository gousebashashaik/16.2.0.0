define("tui/searchBResults/view/MultiUnitSearchResults", [
  "dojo/_base/declare",
  "dojo/text!tui/searchBResults/view/templates/MultiUnitSearchResultItemTmpl.html",
  "tui/searchBResults/view/SearchResultsComponent"], function (declare, resultTmpl1 ) {

  // ----------------------------------------------------------------------------- utility methods

	return declare("tui.searchBResults.view.MultiUnitSearchResults", [tui.searchBResults.view.SearchResultsComponent], {
 
    // ----------------------------------------------------------------------------- properties

    tmpl: resultTmpl1,
 
	postCreate: function () {
	      // initialise search results view
	      var resultsView = this;
	      
	      resultsView.inherited(arguments);
	      
	      
	      
	}
	
	});
});

