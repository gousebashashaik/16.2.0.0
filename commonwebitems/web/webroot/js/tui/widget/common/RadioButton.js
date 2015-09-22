define('tui/widget/common/RadioButton', [
  'dojo',
  'dojo/query',
  'dojo/on',
  'dojo/dom-class',
  'dojo/NodeList-traverse',
  'tui/widget/mobile/Widget'], function (dojo, query, on, domClass, domStyle, domAttr) {


  function Radio(dom) {
    var radio = this;
    var radioSpan = dojo.query('.radio', dom)[0];

    this.dom = function () {
      return dom;
    }

    this.check = function () {
      domClass.add(dom, 'included');
      domClass.add(radioSpan, 'active');
    }

    this.unCheck = function () {
      domClass.remove(dom, 'included');
      domClass.remove(radioSpan, 'active');
    }

    this.isChecked = function () {
      return domClass.contains(dom, 'included') && domClass.contains(radioSpan, 'active');
    }

  }

  dojo.declare('tui.widget.common.RadioButton', [tui.widget.mobile.Widget], {

    postCreate: function () {
      var widget = this;
      widget.radios = _.map(query('li', this.domNode), function (radio) {
        return new Radio(radio);
      })

      function performCheck(event, liDom, li) {
        if (event.target == liDom || event.target.parentNode == liDom) {
          li.isChecked() ? '' : li.check();
        }
        else {
          li.unCheck();
        }
      }

      _.each(widget.radios, function (radio) {
        on(radio.dom(), 'click', function (e) {
          dojo.stopEvent(e);
          _.each(widget.radios, function (item) {
            performCheck(e, item.dom(), item);
          })
        })
      })

    }

  });
});
