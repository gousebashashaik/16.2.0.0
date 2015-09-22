define('tui/widget/booking/AttractionDetailWidget', [ 'dojo', 'dojo/query',
		'dojo/dom-class',"dojo/dom-style","dojo/dom","dojo/dom-construct","dojo/text!tui/widget/Templates/AttractionOptionsComponentTmpl.html","dojo/html","tui/widget/_TuiBaseWidget","tui/widget/expand/Expandable" ],
		function(dojo, query, domClass,domStyle,dom,domConstruct,AttractionOptionsComponentTmpl,html) {

	dojo.declare('tui.widget.booking.AttractionDetailWidget',
					[ tui.widget._TuiBaseWidget , tui.widget.expand.Expandable],

					{

						tmpl:AttractionOptionsComponentTmpl,


						controller:  null,
						postCreate : function() {

							var widget = this;
							var widgetDom = widget.domNode;
							console.log(widget.jsonData);

							//console.log(jsonData.extraFacilityViewDataContainer.mealOptions.extraFacilityViewData.length)



							  var template = new dojox.dtl.Template(widget.tmpl);
						      var context = new dojox.dtl.Context(widget);
						      var html = template.render(context);

						      domConstruct.place(html, widget.domNode, 'only');
						      dojo.parser.parse(widget.domNode);



							widget.transitionOptions.domNode = widget.domNode;
							widget.transition = widget.addTransition();
						      // Tagging particular element.
						      if(widget.autoTag) {
						    	  widget.tagElements(dojo.query(widget.targetSelector, widget.domNode), 'toggle');
						      }

							console.log(dijit.registry.byId("controllerWidget"));
							widget.controller = dijit.registry.byId("controllerWidget");
							console.log(widget.controller);
							var testvar = widget.controller.registerView(widget);
							console.log(widgetDom);


						},
						refresh : function(field,response)
						{
							var widget = this;

							console.log(field);
							console.log(response);
							widget.jsonData = response;
							console.log(response);
							console.log(widget.jsonData );
							console.log(widget);


							  var template = new dojox.dtl.Template(widget.tmpl);
						      var context = new dojox.dtl.Context(widget);
						      var html = template.render(context);

						      domConstruct.place(html, widget.domNode, 'only');
						      dojo.parser.parse(widget.domNode);

						}
					});
	return tui.widget.booking.AttractionDetailWidget;

});