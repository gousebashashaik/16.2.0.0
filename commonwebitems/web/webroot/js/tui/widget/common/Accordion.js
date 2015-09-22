define('tui/widget/common/Accordion', [
  'dojo',
  'dojo/query',
  'dojo/on',
  'dojo/dom-class',
  'dojo/dom-style',
  "dojo/dom-geometry",
  'dojo/_base/connect',
  'dojo/NodeList-traverse',
  'tui/widget/mobile/Widget'], function (dojo, query, on, domClass, domStyle, domGeom, connect){

  function calculateHeight(elements){
    var height = 0;
    _.each(elements, function(element) {
      height += domGeom.position(element).h;
    })
    return height;
  }


  function Item(dom){
    var item = this;
    var content = query('.content', dom)[0];
    var innerContent = query('.content', dom).children();
    var trigger = query('.trigger', dom)[0];
    var height = calculateHeight(innerContent);

    on(trigger, 'click', function (e){
      dojo.stopEvent(e);
      item.toggle();
    })

    this.dom = function (){
      return dom;
    }

    this.content = function (){
      return content;
    }

    this.height = function (){
      return height;
    }

    this.setNewHeight = function () {
      height = calculateHeight(innerContent);
      this.isOpen() ? this.open() : '';
    }

    this.open = function (){
      domStyle.set(content, 'height', _.pixels(height));
      domClass.add(dom, 'open');
    }

    this.close = function (){
      domStyle.set(content, 'height', _.pixels(0));
      domClass.remove(dom, 'open');
    }

    this.isOpen = function (){
      return domClass.contains(dom, 'open');
    }

    this.toggle = function (){
      this.isOpen() ? this.close() : this.open();
    }

  }

  dojo.declare('tui.widget.common.Accordion', [tui.widget.mobile.Widget], {

    onMobile: function (){
      var widget = this;
      _.map(widget.items, function (item){
        item.close();
      });
      window.document.addEventListener('orientationchange', function () {
        _.each(widget.items, function (item){
          item.setNewHeight();
        });
      }, false);
      connect.subscribe('tui/widget/accordion=resize', function (){
        _.each(widget.items, function (item){
          item.setNewHeight();
        });
      })
    },

    onDesktop: function (){
      var widget = this;
      _.map(widget.items, function (item){
        item.open();
      });
      connect.subscribe('tui/widget/accordion=resize', function (){
        _.each(widget.items, function (item){
          item.setNewHeight();
        });
      })
    },

    onMiniTablet: function (){
      this.onDesktop();
    },

    postCreate: function (){
      var widget = this;
      widget.items = _.map(query('.item', this.domNode), function (item){
        return new Item(item);
      })
      widget.inherited(arguments);
    }
  });
});
