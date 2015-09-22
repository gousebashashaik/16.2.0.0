// #ContentLoader
// ##Mixin module
//
// Provides method for inserting HTML into specified DOM element
// This is a mixin class, it must be extended
//
// @author: Maurice Morgan.

define("tui/widget/mixins/ContentLoader", [
  "dojo",
  "dojo/html"
], function (dojo, html) {

  return dojo.declare("tui.widget.mixins.ContentLoader", null, {

    /**
     * ###contentSelector
     * CSS selector used to query target DOM element where content will be inserted
     * @type {String}
     */
    contentSelector: null,

    /**
     * ###contentNode
     * stored reference to target DOM element
     * @type {Node}
     */
    contentNode: null,

    /**
     * ###insertContent()
     * Inserts `HTMLContent` content into target DOM element
     * @param {String} HTMLContent Content that will be inserted into `contentNode`
     */
    insertContent: function (HTMLContent) {
      var contentLoader = this;
      // query and store a reference to content node if not pre-defined
      if (!contentLoader.contentNode) {
        contentLoader.contentNode = (contentLoader.contentSelector) ?
            dojo.query(contentLoader.contentSelector, contentLoader.domNode)[0] :
            contentLoader.domNode;
      }
      // set the content
      html.set(contentLoader.contentNode, HTMLContent);
    }
  });
});