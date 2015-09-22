define("tui/mixins/MethodSubscribable", ["dojo","tui/Tui"], function (dojo, tui) {

	dojo.declare("tui.mixins.MethodSubscribable", null, {

		subscribableMethods: null,

		subscribeMethods: function () {
			var methodSubscribable = this;
			if (methodSubscribable.subscribableMethods) {
				dojo.forEach(methodSubscribable.subscribableMethods, function (methodName, index) {
					var subscribeName = methodSubscribable.declaredClass + "." + methodName;
					if (methodSubscribable[methodName]) {
						if (methodSubscribable.subscribe){
							methodSubscribable.subscribe(subscribeName, methodSubscribable[methodName]);
						} else{
							dojo.subscribe(subscribeName, methodSubscribable[methodName]);
						}
					}
				})
			}
		}
	});

	return tui.mixins.MethodSubscribable;
});