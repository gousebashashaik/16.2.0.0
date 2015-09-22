define("tui/pageController/SearchResultsController", ["dojo", "dojo/Stateful"], function (dojo, stateful) {

    dojo.declare("tui.pageController.SearchResultsController", null, {
		constructor: function(){
    		new dojo.Stateful({
				whereFrom: "", 
				whereTo: "",
				when: ""
			});
  		}
    });
    

    return tui.pageController.SearchResultsController;
})