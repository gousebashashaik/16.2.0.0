define('tui/widget/BlogPost', [
	'dojo',
	'dojo/on',
	'dojo/dom-attr',
	'dojo/dom-style',
	'dojo/query'
], function (dojo, on, domAttr, domStyle) {
	dojo.declare('tui.widget.BlogPost', [tui.widget._TuiBaseWidget], {
		Link: null,
		postCreate: function () {
			var blogPost = this;
			blogPost.addEventListeners();
		},
		addEventListeners: function () {
			var blogPost = this;
			var myDiv = dojo.byId("blog");
			dojo.connect(myDiv, "onclick", function () {
				window.open(blogPost.Link);
			});
		}
	});
	return tui.widget.BlogPost;
});

