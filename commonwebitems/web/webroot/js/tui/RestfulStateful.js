define("tui/RestfulStateful", [
	"dojo",
	"dojo/Stateful"], function (dojo) {

	// module: tui/RestfulStateful
	//
	// summary:
	//      Extends Stateful class with RESTful methods blah blah blah

	dojo.declare("tui.RestfulStateful", [dojo.Stateful], {

		// ----------------------------------------------------------------------------- methods

		set: function(name, value, responseSet, params) {
			var restfulStateful = this;
			var args = arguments;
			if(responseSet) {
				var searchModelCopy = dojo.clone(restfulStateful);
				searchModelCopy[name] = value;
				//restfulStateful.inherited(args);
				/*dojo.when(restfulStateful.doQuery(params), function(response){
				 dojo.publish(restfulStateful.declaredClass + "channel:modelUpdated", [response]);
				 restfulStateful.inherited(args);
				 restfulStateful.onResponseSet(name, value, response);
				 })*/
			} else {
				restfulStateful.inherited(arguments);
			}
		},

		doQuery: function(params) {
			var restfulStateful = this;
			var targetUrl = params.url || restfulStateful.targetUrl;
			return dojo.xhr("GET", {
				url:targetUrl + "?" + (params.query),
				handleAs:"json"
			});
		},

		onResponseSet: function(name, value, response){}

	});

	return tui.RestfulStateful;
});