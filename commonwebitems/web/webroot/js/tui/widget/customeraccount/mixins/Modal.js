define('tui/widget/mixins/Modal', [
  'dojo',
  'dojo/dom-class',
  'dojo/query',
  'dojo/_base/event',
  'dojo/dom-construct',
	'dojo/dom-attr',
  'dojox/dtl',
  'dojox/dtl/Context',
  'dijit/_Widget'], function(dojo, domClass, query, event, domConstruct, domAttr) {


  dojo.declare('tui.widget.mixins.Modal', [dijit._Widget], {

    template: null,

    data: null,

    postCreate: function() {
      var widget = this;
      var modal = query('.modal')[0];
			var html = query('html')[0];
      var window = query('.window', modal)[0];

      dojo.connect(widget.domNode, 'onclick', function(e) {
        event.stop(e);

        var template = new dojox.dtl.Template(widget.template);
        var context = new dojox.dtl.Context(widget);
        domConstruct.place(template.render(context), window, 'only');

        dojo.connect(query('.close', window)[0], 'onclick', function(e) {
          domClass.remove(modal, 'show');
          domClass.remove(html, 'modal-open');
        });
        domClass.add(modal, 'show');
        domClass.add(html, 'modal-open');
				/*
				html.ontouchmove = function(event){
					if(domClass.contains(event.target, 'modal-open')) {
						event.preventDefault();
						event.stopPropagation();
					}
				}
				*/
				document.addEventListener('touchmove', function(e) {
					if (domClass.contains(e.target, 'modal-open')) {
						e.preventDefault();
						e.stopPropagation();
					}
				},false);

      });
      widget.inherited(arguments);
    }

  });
});


