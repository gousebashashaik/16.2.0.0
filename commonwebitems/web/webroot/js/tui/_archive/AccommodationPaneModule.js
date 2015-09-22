define ("tui/widget/AccommodationPaneModule", [ "../.", "dojo/text!tui/widget/Templates/AccommodationPaneTmpl.html",
												"tui/widget/ResultPane", 
												"dojox/dtl", 
												"dojox/dtl/Context", 
												"dojox/dtl/tag/logic"], function(dojo, accommodationPaneTmpl){
	
	dojo.declare("tui.widget.RoomResultPane", [tui.widget.ResultPane], {
		
		carouselContainerSelector: ".carousels-container",
						
		postCreate: function(){
			var roomResultPane = this;
			roomResultPane.inherited(arguments);
			dojo.query(roomResultPane.carouselContainerSelector, roomResultPane.domNode).addClass(roomResultPane.loadedSelector);
		},
				
		loadPage: function(scrollPage){
			var roomResultPane = this;
			roomResultPane.resultsInsertNo = dojo.query(".room-result", roomResultPane.domNode).length + 1
			roomResultPane.resultsInsertNo = (roomResultPane.resultsInsertNo < 10) ? 
							[0, roomResultPane.resultsInsertNo].join("") : roomResultPane.resultsInsertNo;
			var roomInsert = ['roompanel_', roomResultPane.resultsInsertNo].join("");
			var template = new dojox.dtl.Template(accommodationPaneTmpl);
			var context  = new dojox.dtl.Context({resultsInsertNo: roomResultPane.resultsInsertNo});
			var accomodationHTML = template.render(context);
			dojo.place(accomodationHTML, roomResultPane.domNode, "last");
			dojo.query(roomResultPane.carouselContainerSelector, roomResultPane.roomInsert).addClass(roomResultPane.loadedSelector);
			dojo.parser.parse(roomInsert);
			scrollPage.attach();
		}
	})
		
	return tui.widget.AccommodationPaneModule;
})