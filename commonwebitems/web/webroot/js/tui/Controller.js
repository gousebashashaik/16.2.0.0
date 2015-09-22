define("tui/Controller", ["dojo","tui/Tui", "tui/mixins/MethodSubscribable"], function (dojo, tui) {

	dojo.declare("tui.Controller", [tui.mixins.MethodSubscribable], {

		domNode: null,

		constructor:function () {
			var controller = this;

			// Reference to controller domNode
			controller.domNode = arguments[1];
			tui.addController(controller);
			controller.subscribeMethods();
		}
	});

	return tui.Controller;
});