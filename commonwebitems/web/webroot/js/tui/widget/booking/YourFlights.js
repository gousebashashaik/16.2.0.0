define('tui/widget/booking/YourFlights', [
  'dojo',
  "dojo/on",
  "dojo/topic",
  'dojo/query',
  "dojo/_base/lang",
  'dojo/dom-class',
  "dojo/text!tui/widget/Templates/YourFlightTmpl.html",
  "dojo/dom-construct",
  "tui/widget/booking/alternateflightsOverlay"
  
], function(dojo, on, topic, query, lang, domClass,YourFlightTmpl,domConstruct) {

dojo.declare('tui.widget.booking.YourFlights', [tui.widget._TuiBaseWidget], {	
	
	tmpl: YourFlightTmpl, 
	
	 
	 postCreate: function() {		
		
		var widget = this;
	 	var widgetDom = widget.domNode;
	 
	 	var controller=null;
	 	console.log(dijit.registry.byId("controllerWidget"));
	 	widget.controller= dijit.registry.byId("controllerWidget").registerView(widget);
	 	console.log(widget.controller);
	 	
	 	
	 	var template = new dojox.dtl.Template(widget.tmpl);
	      var context = new dojox.dtl.Context(widget);
	      var html = template.render(context);

	      domConstruct.place(html, widget.domNode, 'only');
	      dojo.parser.parse(widget.domNode);
	      this.clickhandler= on(dojo.byId("changeFlights"), "click", lang.hitch(this, function(){
	    	  topic.publish("flightOptions.overlay.show", this.jsonData);
	      }));
	 	
	 },
	 refresh : function(field,response)
		{
			

			 console.log(field);
			 console.log(response);
			 var widget = this;
			 widget.jsonData = response;
			 console.log(response);
			 console.log(widget.jsonData);
			 console.log(widget);
			 topic.subscribe("flightoptions.alternateflightsOverlay.getData", lang.hitch(this, function(){
				 dijit.byId("duration-tab").model = this.jsonData;
			 }));
             var template = new dojox.dtl.Template(widget.tmpl);
			 var context = new dojox.dtl.Context(widget);
			 var html = template.render(context);
             domConstruct.place(html, widget.domNode,'only');
			 dojo.parser.parse(widget.domNode);
			 this.clickhandler.remove();
			 this.clickhandler=on(dojo.byId("changeFlights"), "click", lang.hitch(this, function(){
		    	 topic.publish("flightOptions.overlay.show", this.jsonData);
		      }));
		}
	 	
	 	
	});
	return tui.widget.booking.YourFlights;
});