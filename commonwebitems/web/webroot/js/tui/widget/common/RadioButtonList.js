define('tui/widget/common/RadioButtonList', [
  'dojo',
  'dojo/query',
  'dojo/on',
  'dojo/dom-class',
  'dojo/dom-attr',
  'dojo/_base/connect',
  'dojo/NodeList-traverse',
  'tui/widget/mobile/Widget'], function (dojo, query, on, domClass, domAttr,connect){


  function Radio(dom){
    var radio = this;
    var radioSpan = dojo.query('.radio', dom)[0];

    this.dom = function (){
      return dom;
    }

    this.check = function (){
      domClass.add(dom, 'included');
      domClass.add(radioSpan, 'active');
      connect.publish('tui/widget/accordion=resize');
    }

    this.unCheck = function (){
      domClass.remove(dom, 'included');
      domClass.remove(radioSpan, 'active');
    }

    this.isChecked = function (){
      return domClass.contains(dom, 'included') && domClass.contains(radioSpan, 'active');
    }

  }

  dojo.declare('tui.widget.common.RadioButtonList', [tui.widget.mobile.Widget], {

    onCheck: function(value) {

    },

    postCreate: function (){
      var widget = this;
      widget.radios = _.map(query('li.radio-list', widget.domNode), function (radio){
        return new Radio(radio);
      });

      _.each(widget.radios, function (radio){
        on(radio.dom(), 'click', function (e){
          dojo.stopEvent(e);
          radio.check();
          widget.onCheck(domAttr.get(radio.dom(), 'data-value'));
          _.each(_.without(widget.radios, radio), function (item){
            item.isChecked()? item.unCheck() : '';
          })
        })
      })
    }

  });
});
