define("tui/mvc/Controller", ["dojo","tui/Tui"], function (dojo, tui) {

	dojo.declare("tui.mvc.Controller", null, {

		// reference to dom element bounded by controller.
		domNode: null,

		constructor:function (options, domNode) {
			// summary:
			//		controller added to tui app config, on class creation.

			var controller = this;

			// set options
			dojo.mixin(controller, options);

			// set domNode reference.
			controller.domNode = domNode;
		}
	});

	return tui.mvc.Controller;
});