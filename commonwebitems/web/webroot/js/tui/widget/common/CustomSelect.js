define('tui/widget/common/CustomSelect', [
  'dojo',
  'dojo/dom-class',
  'dojo/on',
  'dojo/dom-attr',
  'tui/widget/mobile/Widget'], function (dojo, domClass, on, domAttr) {


  dojo.declare('tui.widget.common.CustomSelect', [tui.widget.mobile.Widget], {

    _deselectOption: function (option) {
      domClass.remove(option, 'active');
    },

    _label : function () {
      return dojo.query('.text', this.domNode)[0];
    },

    _selectOption: function (option) {
      var widget = this;
      domClass.add(option, 'active');
      setTimeout(function() {
        widget.close();
            },1000);
    },

    toggle: function () {
      var widget = this;
      domClass.toggle(this.domNode, 'active');
      widget.isOpen() ? widget.onOpen() : '';
    },

    isOpen:function () {
      return domClass.contains(this.domNode, 'active');
    },

    onOpen:function () {
      var widget = this;
      var element = dojo.query('.scroll', widget.domNode)[0];
      new IScroll(element, dojo.fromJson(domAttr.get(element, 'data-scroll-options') || {}));
    },

    close: function () {
      domClass.remove(this.domNode, 'active');
    },

    onSelect: function (value) {

    },

    updateLabel : function (label) { 
            this._label().innerHTML = label;
    },

    selectValue: function (value) {

    },

    options: function () {
      return  dojo.query('div > ul > li', this.domNode);
    },


    onBlur: function () {
      var widget = this;
      widget.close();
      widget.inherited(arguments)
    },


    postCreate: function () {
      var widget = this;
      var containers = dojo.query('.text, .arrow', widget.domNode);
      _.each(containers, function (element) {
        on(element, 'click', function (e) {
          dojo.stopEvent(e);
          widget.toggle();
        });
      })

      _.each(widget.options(), function (option) {
        on(option, 'click', function (e) {
                    dojo.stopEvent(e);
                    domClass.remove(dojo.byId('loading-results'), 'hide');
          _.each(_.without(widget.options(), option), function (otherOption) {
            widget._deselectOption(otherOption);
          })
                    domClass.add(option, 'active');
                    // delaying the triggering of handler to improve responsiveness
                    setTimeout(function() {
          widget.onSelect(domAttr.get(option, 'data-select-value') || '');
                    },500);

        });
      });

      widget.inherited(arguments);
    }
  });
});
