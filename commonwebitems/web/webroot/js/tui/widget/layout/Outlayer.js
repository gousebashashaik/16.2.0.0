// # Outlayer
// ## Outlayer layout class
//
// Outlayer class ported to dojo by Vijay Kumar and Laurent Mignot
//
// @extends: tui.widget._TuiBaseWidget
//
// @source: https://github.com/metafizzy/outlayer

define("tui/widget/layout/Outlayer", [
  "dojo/_base/declare",
  "dojo/_base/lang",
  "dojo/Evented",
  "dojo/query",
  "dojo/dom-style",
  "tui/utils/DetectFeature",
  "tui/utils/GetSize",
  "tui/widget/layout/OutlayerItem",
  "tui/widget/_TuiBaseWidget"
], function (declare, lang, Evented, query, domStyle, detectFeature, getSize, OutlayerItem) {

  "use strict";

  return declare("tui.widget.layout.Outlayer", [Evented], {

    domNode: null,

    itemSelector: null,

    options: {
      containerStyle: {
        position: 'relative'
      },
      isInitLayout: true,
      isOriginLeft: true,
      isOriginTop: true,
      isResizeBound: true,
      // item options
      transitionDuration: '0.4s',
      hiddenStyle: {
        opacity: 0,
        transform: 'scale(0.001)'
      },
      visibleStyle: {
        opacity: 1,
        transform: 'scale(1)'
      }
    },

    transitionProperty: null,

    transformProperty: null,

    supportsCSS3: false,

    is3d: false,

    transitionEndEvent: null,

    prefixableProperties: null,

    vendorProperties: null,

    items: null,

    _isLayoutInited: false,

    constructor: function (options, domNode) {
      var outlayer = this;
      outlayer.domNode = domNode;
      lang.mixin(outlayer.options, options);
      outlayer.init();
    },

    init: function () {
      var outlayer = this;

      outlayer.transitionProperty = detectFeature.isTransitionSupported();
      outlayer.transformProperty = detectFeature.isTransformSupported();
      outlayer.supportsCSS3 = outlayer.transitionProperty && outlayer.transformProperty;
      outlayer.is3d = !!detectFeature.is3dSupported();

      outlayer.transitionEndEvent = {
        WebkitTransition: 'webkitTransitionEnd',
        MozTransition: 'transitionend',
        OTransition: 'otransitionend',
        transition: 'transitionend'
      }[outlayer.transitionProperty];

      outlayer.prefixableProperties = ['transform', 'transition', 'transitionDuration', 'transitionProperty'];

      outlayer.vendorProperties = (function () {
        var cache = {};
        for (var i = 0, len = outlayer.prefixableProperties.length; i < len; i++) {
          var prop = outlayer.prefixableProperties[i];
          var supportedProp = detectFeature.isSupported(prop);
          if (supportedProp && supportedProp !== prop) {
            cache[prop] = supportedProp;
          }
        }
        return cache;
      })();

      // kick it off
      outlayer._create();

      if (outlayer.options.isInitLayout) {
        outlayer.layout();
      }

    },

    _create: function () {
      var outlayer = this;
      outlayer.reloadItems();
      lang.mixin(outlayer.domNode.style, outlayer.options.containerStyle);
      if (outlayer.options.isResizeBound) {
        outlayer.bindResize();
      }
    },

    reloadItems: function () {
      var outlayer = this;
      outlayer.items = outlayer._itemize(query(outlayer.itemSelector, outlayer.domNode));
    },

    _itemize: function (nodeList) {
      var outlayer = this,
          items = [];
      _.each(nodeList, function(domNode){
        items.push(new OutlayerItem({
          transitionProperty: outlayer.transitionProperty,
          transformProperty: outlayer.transformProperty,
          supportsCSS3: outlayer.supportsCSS3,
          is3d: outlayer.is3d,
          transitionEndEvent: outlayer.transitionEndEvent,
          vendorProperties: outlayer.vendorProperties,
          layout: outlayer
        }, domNode));
      });

      return items;
    },

    getItemElements: function () {
      var outlayer = this,
          elems = [];
      _.each(outlayer.items, function(item){
        elems.push(item.domNode);
      });
      return elems;
    },

    layout: function () {
      var outlayer = this;
      outlayer._resetLayout();
      var isInstant = outlayer.options.isLayoutInstant !== undefined ? outlayer.options.isLayoutInstant : !outlayer._isLayoutInited;
      outlayer.layoutItems(outlayer.items, isInstant);

      // flag for initalized
      outlayer._isLayoutInited = true;
    },

    _resetLayout: function () {
      var outlayer = this;
      outlayer.getSize();
    },

    getSize: function () {
      var outlayer = this;
      outlayer.size = getSize(outlayer.domNode);
    },

    _getMeasurement: function (measurement, size) {
      var outlayer = this,
          option = outlayer.options[measurement],
          node;

      if(!option) {
        outlayer[measurement] = 0;
      } else {
        node = option;
        outlayer[measurement] = node ? getSize(node)[size] : option;
      }
    },

    layoutItems: function (items, isInstant) {
      var outlayer = this;
      outlayer._layoutItems(items, isInstant);
      outlayer._postLayout();
    },

    _layoutItems: function(items, isInstant) {
      var outlayer = this,
          queue = [];
      // @TODO: pub/sub
      if(!items || !items.length) {
        outlayer.emit('layoutComplete', [outlayer, items]);
      }

      _.each(items, function (item) {
        queue.push(lang.mixin(outlayer._getItemLayoutPosition(), {
          item: item,
          isInstant: isInstant
        }));
      });

      outlayer._processLayoutQueue(queue);
    },

    _getItemLayoutPosition: function () {
      return {
        x: 0,
        y: 0
      };
    },

    _processLayoutQueue: function (queue) {
      var outlayer = this;
      _.each(queue, function (queueItem) {
        outlayer._positionItem(queueItem.item, queueItem.x, queueItem.y, queueItem.isInstant);
      });
    },

    _positionItem: function (item, x, y, isInstant) {
      if (isInstant) {
        // if not transition, just set CSS
        item.goTo(x, y);
      } else {
        item.moveTo(x, y);
      }
    },

    /**
     * Any logic you want to do after each layout,
     * i.e. size the container
     */
    _postLayout: function () {
      // no-op
    },

    _setContainerMeasure: function (measure, isWidth) {
      var outlayer = this;
      if (measure === undefined) {
        return;
      }

      var elemSize = outlayer.size;
      // add padding and border width if border box
      if (elemSize.isBorderBox) {
        measure += isWidth ? elemSize.paddingLeft + elemSize.paddingRight + elemSize.borderLeftWidth + elemSize.borderRightWidth : elemSize.paddingBottom + elemSize.paddingTop + elemSize.borderTopWidth + elemSize.borderBottomWidth;
      }

      measure = Math.max(measure, 0);
      domStyle.set(outlayer.domNode, (isWidth ? 'width' : 'height'), _.pixels(measure));
    },

    _itemsOn: function (items, eventName, callback) {
      var outlayer = this,
          doneCount = 0,
          count = items.length;

      function tick() {
        doneCount++;
        if (doneCount === count) {
          callback.call(outlayer);
        }
        return true; // bind once
      }

      _.each(items, function(item){
        item.on(eventName, tick);
      });
    }

  });
});