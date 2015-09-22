define ("tui/widget/ResultPane", [ "dojo", 
								   "tui/widget/_TuiBaseWidget",
								   "tui/utils/ScrollListener", 
								   "tui/widget/carousels/Carousel", 
								   "dojo/NodeList-traverse"], function(dojo){
	
	dojo.declare("tui.widget.ResultPane", [tui.widget._TuiBaseWidget], {
		
		loading: false,
		
		resultsInsertNo: 0, 
		
		scrollpagable: true,
		
		scrollPage: null,
		
		postCreate: function(){
			var resultPane = this;
			resultPane.inherited(arguments);
			if (resultPane.scrollpagable){
				resultPane.scrollPage = new tui.utils.ScrollListener();
				resultPane.addScrollEvents();
			}
		},
		
		addScrollEvents: function(){
			var resultPane = this;
			resultPane.connect(resultPane.scrollPage, "onScroll", function(currentPos, scrollPage){
				scrollPage.detach();
				resultPane.loadPage(scrollPage);
			})
		},
				
		loadPage: function(scrollPage){
			
		}
	})
	
	return tui.widget.ResultPane;
})