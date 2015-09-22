define('tui/widget/booking/LuggageAllowanceRadioButtonWidget', [
  'dojo',
  'dojo/query',
  'dojo/dom-class'
], function(dojo, query, domClass) {

dojo.declare('tui.widget.booking.LuggageAllowanceRadioButtonWidget', [tui.widget._TuiBaseWidget], {


	controller:  null,

	postCreate: function() {
		var widget = this;
	 	var widgetDom = widget.domNode;


	 			console.log(dijit.registry.byId("controllerWidget"));

	 			widget.controller = dijit.registry.byId("controllerWidget");

	 			console.log(widget.controller);

	 		var testvar = widget.controller.registerView(widget);

	 		var radioButtons= query('input[type=radio]',widgetDom);
	 		console.log(radioButtons);

	 	_.each(radioButtons, function(radioButton) {



	 		dojo.connect(radioButton,'click', function(e){


	 			console.log(radioButton);
	 			console.log(dojo.attr(radioButton, "name"));
	 			console.log(dojo.attr(radioButton, "value"));

	 			var passengerId=dojo.attr(radioButton, "name");
	 			var baggageId= dojo.attr(radioButton, "value");


	 			widget.generateRequest(passengerId,baggageId);

	 		});

	 	});

	 	},
	 	generateRequest : function(pId,bId) {
			console.log("generate request");
			var widget= this;
			var request={};
			var field = "baggage"

			request={passengerId: pId,baggageId: bId };

			var url = dojoConfig.paths.webRoot+"/book/update/baggage" ;
			widget.controller.generateRequest(field,url,request);

			return request;

		},
		refresh : function(field,response)
		{

			console.log(field);
			console.log(response);
		}
	});
	return tui.widget.booking.LuggageAllowanceRadioButtonWidget;
});