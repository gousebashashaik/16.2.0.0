define('tui/widget/booking/allInclusive', [ 'dojo', 'dojo/query',
		'dojo/dom-class',"dojo/dom-style","dojo/dom","dojo/dom-construct","dojo/text!tui/widget/Templates/allInclusiveTmpl.html","dojo/html","tui/widget/booking/RoomBoard/modal/RoomAndBoard","tui/widget/_TuiBaseWidget"],
		function(dojo, query, domClass,domStyle,dom,domConstruct,allInclusiveTmpl,html,RoomAndBoard) {

	dojo
			.declare('tui.widget.booking.allInclusive',
					[ tui.widget._TuiBaseWidget ],

					{

						tmpl:allInclusiveTmpl,


						controller:  null,
						postCreate : function() {

							var widget = this;
							var widgetDom = widget.domNode;
							RoomAndBoard.refWidget = widget;








						},
				contentRefresh: function(response){
					var widget= this;
					var widgetDom = widget.domNode;
					widget.jsonData = response;
					console.log(widget.jsonData);

					var template = new dojox.dtl.Template(widget.tmpl);
				    var context = new dojox.dtl.Context(widget);
				    var html = template.render(context);

				    domConstruct.place(html, widget.domNode, 'only');
				    dojo.parser.parse(widget.domNode);

				}

					});
	return tui.widget.booking.allInclusive

});