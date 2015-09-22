define("tui/searchPanel/view/cruise/CruiseQuickSearch", [
  "dojo",
  "dojo/_base/declare",
  "dojo/text!tui/searchPanel/view/cruise/templates/cruiseQSNoResultsTmpl.html", 
  "dojo/text!tui/searchPanel/view/cruise/templates/cruiseQSResultIsTmpl.html",
  "tui/dtl/Tmpl",
  "tui/searchPanel/view/QuickSearch" ], function(dojo, declare, noresultTmpl, resultItemTmpl) {

  // can you pass me the id for results
  return declare("tui.searchPanel.view.cruise.CruiseQuickSearch", [tui.searchPanel.view.QuickSearch], {

	  // ---------------------------------------------------------------- quickSearch methods

    liContentTemplate: resultItemTmpl,
    
    quickSearchTmpl: noresultTmpl,
    
    analyticText:"pocQuickSearchBox",
    
    postCreate: function() {
    	var autoComplete = this;
    	autoComplete.inherited(arguments);    	
    	},
    
    onElementListSelection: function(selectedData, listData) {
    	var quickSearch = this, 
     	  goButton = dojo.query("a.cruiseGoButton", document.body)[0];
    	if(!goButton) {
    		window.location.href = selectedData.value;
    		return;
    	}
    	quickSearch.connect(goButton, "onclick", function () {  
    		window.location.href = selectedData.value;
    	});
    }

  });

});