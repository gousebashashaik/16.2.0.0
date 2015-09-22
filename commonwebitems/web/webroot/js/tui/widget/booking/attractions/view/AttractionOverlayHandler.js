/*define('tui/widget/booking/attractions/view/AttractionOverlayHandler', [
  'dojo',
  'dojo/query',
  'dojo/dom-class'
], function(dojo, query, domClass) {

dojo.declare('tui.widget.booking.attractions.view.AttractionOverlayHandler', [tui.widget._TuiBaseWidget], {


	controller:  null,

	postCreate: function() {
		var widget = this;
	 	var widgetDom = widget.domNode;

	 		var radioButtons= query('input[type=radio]',widgetDom);
	 		_.each(radioButtons, function(radioButton) {
	 			dojo.connect(radioButton,'click', function(e){
				var items = query('.theme-park-heading', widgetDom);
					_.each(items, function(item) {
						domClass.remove(item, 'clicked-radio');
					});
					var checkedRadioButtons= query('input[type=radio]:checked',widgetDom);
					_.each(checkedRadioButtons, function(checkedRadioButton) {
						var checkedParent = dojo.query(checkedRadioButton).parents(".theme-park-heading");
						domClass.add(checkedParent[0], 'clicked-radio');
					});
	 			});
	 		});

	 	},

	});

	return tui.widget.booking.attractions.view.AttractionOverlayHandler;
});*/
define('tui/widget/booking/attractions/view/AttractionOverlayHandler', [
  'dojo',
  'dojo/query',
  'dojo/dom-class'
], function(dojo, query, domClass) {

dojo.declare('tui.widget.booking.attractions.view.AttractionOverlayHandler', [tui.widget._TuiBaseWidget], {


	controller:  null,

	postCreate: function() {
		alert("test");
		var widget = this;
	 	var widgetDom = widget.domNode;

	 		var radioButtons= query('input[type=radio]',widgetDom);
	 		_.each(radioButtons, function(radioButton) {
	 			dojo.connect(radioButton,'click', function(e){
				var items = query('.theme-park-heading', widgetDom);
					_.each(items, function(item) {
						domClass.remove(item, 'clicked-radio');
					});
					var checkedRadioButtons= query('input[type=radio]:checked',widgetDom);
					_.each(checkedRadioButtons, function(checkedRadioButton) {
						var checkedParent = dojo.query(checkedRadioButton).parents(".theme-park-heading");
						domClass.add(checkedParent[0], 'clicked-radio');
					});
	 			});
	 		});

	 	}

	});

	return tui.widget.booking.attractions.view.AttractionOverlayHandler;
});