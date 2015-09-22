define('tui/common/TemplateUtil', [
  'dojox/dtl',
  'dojox/dtl/tag/loop',
  'dojox/dtl/filter/lists',
  'dojox/dtl/tag/misc',
  'dojox/dtl/tag/logic',
  'dojox/dtl/Context'], function() {

  return {
    render: function(template, context) {
      var template = new dojox.dtl.Template(template);
      var context = new dojox.dtl.Context(context);
      return template.render(context);
    }
  };

});
