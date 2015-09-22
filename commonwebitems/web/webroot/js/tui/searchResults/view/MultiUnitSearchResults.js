define("tui/searchResults/view/MultiUnitSearchResults", [
  "dojo/_base/declare",
  "dojo/text!tui/searchResults/view/templates/MultiUnitSearchResultItemTmpl.html",
  "tui/searchResults/view/SearchResultsComponent"], function (declare, resultTmpl1 ) {

  // ----------------------------------------------------------------------------- utility methods

	return declare("tui.searchResults.view.MultiUnitSearchResults", [tui.searchResults.view.SearchResultsComponent], {
 
    // ----------------------------------------------------------------------------- properties

    tmpl: resultTmpl1,
 
	postCreate: function () {
	      // initialise search results view
	      var resultsView = this;
	      
	      resultsView.inherited(arguments);
	      
	      
	      
	}
	
	});
});

