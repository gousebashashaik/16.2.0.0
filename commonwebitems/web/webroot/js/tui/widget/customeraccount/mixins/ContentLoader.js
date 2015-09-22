define('tui/widget/mixins/ContentLoader', ['dojo', 'dojo/html'], function(dojo, html) {

	dojo.declare('tui.widget.mixins.ContentLoader', null, {

		contentSelector: null,

		contentNode: null,

		insertContent: function(/*string*/ HTMLContent) {
			var contentLoader = this;
			if (!contentLoader.contentNode) {
				contentLoader.contentNode = (contentLoader.contentSelector) ?
						dojo.query(contentLoader.contentSelector, contentLoader.domNode)[0] : contentLoader.domNode;
			}
			html.set(contentLoader.contentNode, HTMLContent);
		}
	});

	return tui.widget.mixins.ContentLoader;
});
