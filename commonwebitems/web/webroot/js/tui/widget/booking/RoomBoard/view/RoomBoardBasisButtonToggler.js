define('tui/widget/booking/RoomBoard/view/RoomBoardBasisButtonToggler', [
  'dojo',
  'dojo/query',
  'dojo/dom-class',
  "tui/widget/booking/constants/BookflowUrl",
  "tui/widget/booking/bookflowMsgs/nls/Bookflowi18nable",
  "tui/widget/expand/Expandable"
 
], function(dojo, query, domClass, BookflowUrl, Bookflowi18nable) {

dojo.declare('tui.widget.booking.RoomBoard.view.RoomBoardBasisButtonToggler', [tui.widget._TuiBaseWidget,tui.widget.expand.Expandable,Bookflowi18nable], {
	 postCreate: function() {

		 var widget = this;
		 var widgetDom = widget.domNode;
		 var controller=null;
		 widget.initBookflowMessaging();
			
		 widget.urlString = this.bookflowMessaging[dojoConfig.site].roomBoard;
		 console.log(dijit.registry.byId("controllerWidget"));
		 widget.controller= dijit.registry.byId("controllerWidget");
		 var testVar= widget.controller.registerView(widget)
		 console.log(widget.controller);

		 widget.forTransitionEffect();
		 this.inherited(arguments);
		 var buttonDom=dojo.query('button.seatingBtn',widgetDom);
		 for(var index = 0; index < buttonDom.length ; index++){
			 widget.tagElement(buttonDom[index],buttonDom[index].value);
		 }
		 var items = query('.boardBasis', widgetDom);
			_.each(items, function(item) {
				var buttons = query('.seatingBtn', item);
				_.each(buttons, function(button) {
					 dojo.connect(button, 'click', function(e) {
						_.each(items, function(item) {
							var buttons = query('.seatingBtn',item);
							_.each(buttons, function(button) {
								domClass.remove(button,'select-seat');
							});
							domClass.remove(item,'selectedSection');
						});
						domClass.add(button, 'select-seat');
						domClass.add(item, 'selectedSection');
						 var boardBasisId = dojo.attr(button, "id");
						widget.generateRequest(boardBasisId);
					});
				});
			});
		 },
		 generateRequest: function (boardBasisId) {
		        var field = "boardBasis";
		        var widget = this;
		        boardBasisId = boardBasisId.split('|');
		        var boardBasisCode = boardBasisId[0];
		        var request = {boardBasisCode: boardBasisCode};
		        var url = BookflowUrl[widget.urlString[boardBasisId[1]]];
		        widget.controller.generateRequest(field, url, request);
		        return request;
		      },
		      refresh: function(){


		      },
		 forTransitionEffect: function()
			{
				var widget= this;
				widget.transitionOptions.domNode = widget.domNode;
				widget.transition = widget.addTransition();

				console.log(widget.jsonData);

			      // Tagging particular element.
			      if(widget.autoTag) {
			    	  widget.tagElements(dojo.query(widget.targetSelector, widget.domNode), 'toggle');
			      }

			}
	});
	return tui.widget.booking.RoomBoard.view.RoomBoardBasisButtonToggler
});