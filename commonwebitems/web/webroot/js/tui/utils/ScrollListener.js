define("tui/utils/ScrollListener", ["dojo", "dojo/on"], function (dojo, on) {

	dojo.declare("tui.utils.ScrollListener", null, {

		area: 50,

		mode: "vertical",

		domNode: null,

		scrollConnect: null,

		attachOnLoad: true,

		constructor: function (args) {
			var scrollListener = this;
			dojo.safeMixin(scrollListener, args);
			if(scrollListener.attachOnLoad) {
				scrollListener.attach();
			}
		},

		attach: function () {
			var scrollListener = this;
			scrollListener.scrollConnect = on(scrollListener.domNode || window, "scroll", function () {
				scrollListener.scroll()
			});
			return scrollListener;
		},

		detach: function () {
			var scrollListener = this;
			if(scrollListener.scrollConnect) {
				scrollListener.scrollConnect.remove();
				scrollListener.scrollConnect = null;
				return scrollListener;
			}
			return null;
		},

		scroll: function () {
			var scrollListener = this,
					viewport = (scrollListener.domNode) ? dojo.marginBox(scrollListener.domNode) : dojo.window.getBox(),
					scrollHeight,
					scrollWidth;

			var scrollPos = 0;
			if (scrollListener.mode === "vertical") {
				scrollPos = viewport.t + viewport.h;
				scrollHeight = (scrollListener.domNode) ? scrollListener.domNode : dojo.body().scrollHeight;
				if (scrollPos < scrollHeight - scrollListener.area) {
					return;
				}
			} else {
				scrollPos = viewport.l + viewport.w;
				scrollWidth = (scrollListener.domNode) ? scrollListener.domNode : dojo.body().scrollWidth;
				if (scrollPos < scrollWidth - scrollListener.area) {
					return;
				}
			}

			scrollListener.onScroll(scrollPos, scrollListener);
		},

		onScroll: function (scrollPos, scrollListener) {}

	});

	return tui.utils.ScrollListener;
});