define('tui/common/MobileWidget', [
  'dojo',
  'dojox/dtl',
  'dojox/dtl/Context',
  'dojo/_base/fx',
  'dojo/query',
  'dijit/_Widget'], function(dojo, tmpl) {


  dojo.declare('tui.common.MobileWidget', [dijit._Widget], {

    templatePath: null,

    data: null,

    postCreate: function() {
      var widget = this;
      window.onresize = widget.onResize.bind(widget);
      widget.render();

    },

    onResize: function() {
      var widget = this;
      widget.render();
    },

    render: function() {
      var widget = this;
      var xhrArgs = {
        url: widget.templatePath + findResolution(),
        handleAs: 'text',
        load: function(tmpl) {
          var template = new dojox.dtl.Template(tmpl);
          var context = new dojox.dtl.Context(widget.data);
          var html = template.render(context);
          widget.domNode.innerHTML = html;

        },
        error: function(error) {
          console.log('error while fetching tempalte!');
        }
      };
      dojo.xhrGet(xhrArgs);
    }

  });
});


function findResolution() {
  var width = window.innerWidth, height = window.innerHeight;
  if (width < 420) {
    return 'template_420x.html';
  }
  else {
    return 'template_default.html';
  }
}
