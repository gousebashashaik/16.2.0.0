define("tui/utils/HasCssProperty", ["dojo"], function (dojo) {
	//  module:
	//    tui/utils/HasCssProperty
	//  summary:
	//    The allows to feature detect css properties
	//    e.g test("transitionProperty") to test for css transitions
	//  source:
	//    http://perfectionkills.com/feature-testing-css-properties/

		var capitalize = function (string) {
			// summary:
			//      helper method, capitalises string
			return string.charAt(0).toUpperCase() + string.slice(1);
		};

		return {

			test: function (propName, element) {
				var prefixes = ["Moz", "Webkit", "O", "Ms"];
				element = element || document.documentElement;
				var style = element.style,
					prefixed;

				// test standard property first
				if (typeof style[propName] === "string") {
					return propName;
				}

				// capitalize
				propName = capitalize(propName);

				// test vendor specific properties
				for (var i = 0, len = prefixes.length; i < len; i++) {
					prefixed = prefixes[i] + propName;
					if (typeof style[ prefixed ] === "string") {
						return prefixed;
					}
				}
      				return null;
			}
		};
});