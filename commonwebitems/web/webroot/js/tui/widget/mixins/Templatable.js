// #Templatable
// ##Mixin module
//
// Adds templating functionality to widgets.
// This is a mixin class, it must be extended
//
// @author: Maurice Morgan.

define("tui/widget/mixins/Templatable", [
  "dojo",
  "dojox/dtl",
  "dojox/dtl/Context",
  "dojox/dtl/tag/logic",
  "dojox/dtl/tag/misc",
  "dojox/dtl/tag/loop",
  "dojox/dtl/tag/loader",
  "dojox/dtl/filter/strings",
  "dojox/dtl/filter/logic",
  "dojox/dtl/filter/dates",
  "dojox/dtl/filter/misc",
  "dojox/dtl/filter/lists",
  "tui/utils/TuiUnderscore"], function (dojo) {

  dojo.declare("tui.widget.mixins.Templatable", null, {

    /**
     * ###tmpl
     * Should be either an inline template or a reference to a template
     * loaded with dojo's text! plugin
     */
    tmpl: null,

    /**
     * ###renderTmpl()
     * Template rendering method
     *
     * @param {String?} tmpl Template string. Optionally specify which template to render, defaults to calling Class's tmpl
     * @param {Object?} data Data object the template will reference
     * @returns {string|*} Rendered HTML from template and data
     */
    renderTmpl: function (tmpl, data) {
      var templatable = this;

      tmpl = tmpl || templatable.tmpl;
      data = data || templatable;

      // call hook before rendering allowing manipulation of template and data
      templatable.onBeforeTmplRender(tmpl, data);

      // validate template and data are not null/undefined
      _.valid(tmpl, data);

      // initialise dojox.dtl engine
      var template = new dojox.dtl.Template(tmpl);
      var context = new dojox.dtl.Context(data);

      // trim whitespace around rendered html
      var html = dojo.trim(template.render(context));

      // call hook after rendering if further manipulation needed
      templatable.onAfterTmplRender(html);
      return html;
    },

    /**
     * ###onAfterTmplRender()
     * Event method fired after template has been rendered
     *
     * @param {String} html the rendered HTML
     */
    onAfterTmplRender: function (html) {
    },

    /**
     * ###onBeforeTmplRender
     * Event method fired before template is rendered
     *
     * @param {String} tmpl the template that will be rendered
     * @param {Object} data the data to render
     */
    onBeforeTmplRender: function (tmpl, data) {
    }

  });

  return tui.widget.mixins.Templatable;
});