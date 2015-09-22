define("tui/widget/mixins/Scrollable", ["dojo",
	"dojo/query",
	"tui/widget/ScrollPanel"], function (dojo, query, scrollPanel) {

	dojo.declare("tui.widget.mixins.Scrollable", null, {

    /**
     * ###scrollable
     * Property to determine whether to display scrolling behaviour
     * @type {Boolean}
     */
		scrollable: false,

    /**
     * ###scrollPanel
     * reference to ScrollPanel Class
     * which allows us to use an extended or different class
     * @type {Object}
     */
		scrollPanel: scrollPanel,

    /**
     * ###scrollerSelector
     * CSS Selector used to query the container to which
     * scrollable behaviour will be attached
     * @type {String}
     */
		scrollerSelector: ".scroller",

    /**
     * ###scrollPanels
     * Stored reference to any scroll panels that have been instantiated
     * @type {Array|null}
     */
		scrollPanels: null,

    /**
     * ###addScrollerPanel()
     * Method which attaches a `ScrollPanel`
     * @param {Node} domContainer the DOM Node which will become scrollable
     */
		addScrollerPanel: function (domContainer) {
			var scrollable = this;

      // exit early if already instantiated or widget is not scrollable
      // for example if desired behaviour is to limit the max-height of a
      // target DOM element, `scrollable` would be false until it exceeds this max-height
      // see `tui.widget.form.SelectOption` for example
			if (!scrollable.scrollable || scrollable.scrollPanels) return;

			scrollable.scrollPanels = [];

      // attach to either `scrollerSelector` within the domContainer or the `domContainer` directly
			var scrollersDom = (scrollable.scrollerSelector === "") ? query(domContainer) : query(scrollable.scrollerSelector, domContainer);
			scrollersDom.forEach(function (item) {
				scrollable.scrollPanels.push(new scrollable.scrollPanel({}, item));
			})
		},

    /**
     * ###deleteScrollerPanel()
     * Method removes any `ScrollPanel` widgets which have been created
     * and destroys related DOM elements
     */
		deleteScrollerPanel: function () {
			var scrollable = this;
			if (!scrollable.scrollPanels) return;
			_.forEach(scrollable.scrollPanels, function (item) {
				item.destroyRecursive();
			});
			scrollable.scrollPanels = null;
		}
	});

	return tui.widget.mixins.Scrollable;
});