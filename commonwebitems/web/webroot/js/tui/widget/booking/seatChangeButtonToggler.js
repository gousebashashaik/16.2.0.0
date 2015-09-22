define('tui/widget/booking/seatChangeButtonToggler', [ 'dojo', 'dojo/query',
		'dojo/dom-class',"dojo/dom-style","dojo/text!tui/widget/Templates/SeatOptionTmpl.html","dojo/topic",
		  "dojo/dom-construct","tui/widget/_TuiBaseWidget","tui/widget/mixins/Templatable","tui/widget/booking/Expandable" ], function(dojo, query, domClass,domStyle,tryHtml,topic,domConstruct) {

	dojo
			.declare('tui.widget.booking.seatChangeButtonToggler',
					[ tui.widget._TuiBaseWidget , tui.widget.expand.Expandable],

					{

						tmpl: tryHtml,
						controller:  null,


						postCreate : function() {

							var widget = this;
							var widgetDom = widget.domNode;

							var template = new dojox.dtl.Template(widget.tmpl);
						      var context = new dojox.dtl.Context(widget);
						      var html = template.render(context);

						      domConstruct.place(html, widget.domNode, 'only');
						      dojo.parser.parse(widget.domNode);

						      widget.forTransitionEffect();


							console.log(dijit.registry.byId("controllerWidget"));
							widget.controller = dijit.registry.byId("controllerWidget");
							console.log(widget.controller);
							var testvar = widget.controller.registerView(widget);
							console.log(widgetDom);



							widget.forclickevent(widgetDom);
							 dojo.subscribe("tui/widget/booking/displayContent", function(){

						         	console.log("generic function");
						         	widget.displayContent();

						  	    });
								 widget.displayContent();


						},
						generateRequest : function(itemId) {
							console.log("generate request");
							var field = "seats";
							var widget= this;


							var request= {seatExtrasCode: itemId};
							var url = dojoConfig.paths.webRoot+"/book/update/seat" ;
							widget.controller.generateRequest(field,url,request);
							return request;
						},

						refresh : function(field,response)
						{

							console.log(field);
							console.log(response);

								var widget = this;
								var widgetDom = widget.domNode;
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
							      widget.forTransitionEffect();
							      widget.forclickevent(widgetDom);


						},

						forclickevent: function(widgetDom)
						{
							var widget = this;
							var items = query('.item', widgetDom);
							_.each(items, function(item) {
								var buttons = query('.seatingBtn', item);
								_.each(buttons, function(button) {
									dojo.connect(button, 'click', function(e) {

										var itemId = dojo.attr(item, "id");
										console.log("id:", itemId);
										widget.generateRequest(itemId);
									});
								});
							});
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

						},
						  displayContent: function(){

						    	var widget = this;
						    	var widgetDom = widget.domNode;
						    	var items = dojo.query(".open",widgetDom);
						    	var allItems = dojo.query(".item",widgetDom);
						    	_.each(allItems,function(item){
						    		var header = dojo.query(".item-toggle",item)[0];


						    		var thresholdAvailability = dojo.query(".limitedAvailability",item)[0];

						    		if(thresholdAvailability != null){
						    		domClass.remove(thresholdAvailability,"displayNone");
						    		}

						    	})
						    	_.each(items,function(item){
						    		var header = dojo.query(".item-toggle",item)[0]

						    		var thresholdAvailability = dojo.query(".limitedAvailability",item)[0];

						    		if(thresholdAvailability != null){
						    		domClass.add(thresholdAvailability,"displayNone");
						    		}
						    	})
						    }



					});
	return tui.widget.booking.seatChangeButtonToggler;
});