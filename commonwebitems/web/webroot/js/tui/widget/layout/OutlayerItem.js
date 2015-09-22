// # Outlayer
// ## Outlayer item class
//
// Outlayer item class ported to dojo by Laurent Mignot
//
// @source: https://github.com/metafizzy/outlayer

define("tui/widget/layout/OutlayerItem", [
  "dojo/_base/declare",
  "dojo/_base/lang",
  "dojo/Evented",
  "dojo/on",
  "dojo/dom-style",
  "tui/utils/DetectFeature",
  "tui/utils/GetSize",
  "tui/utils/GetStyleProperty",
  "tui/widget/_TuiBaseWidget"
], function (declare, lang, Evented, on, domStyle, domGeom, detectFeature, getSize, getStyleProperty) {

  // @TODO: pubsub

  "use strict";

  var defView = document.defaultView;

  var getStyle = defView && defView.getComputedStyle ?
      function(elem) {
        return defView.getComputedStyle(elem, null);
      } :
      function(elem) {
        return elem.currentStyle;
      };

  return declare("tui.widget.layout.OutlayerItem", [tui.widget._TuiBaseWidget, Evented], {

    layout: null,

    position: null,

    transitionProperty: null,

    transformProperty: null,

    supportsCSS3: false,

    is3d: false,

    transitionEndEvent: null,

    //prefixableProperties: null,

    vendorProperties: null,

    size: null,

    isTransitioning: false,

    itemTransitionProperties: null,

    dashedVendorProperties: null,

    __transition: null,

    postCreate: function () {
      var outlayerItem = this;
      outlayerItem.inherited(arguments);
      outlayerItem.position = {
        x:0,
        y:0
      };
      outlayerItem.createItem();
    },

    createItem: function () {
      var outlayerItem = this;
      outlayerItem.__transition = {
        ingProperties: {},
        clean: {},
        onEnd: {}
      };
      domStyle.set(outlayerItem.domNode, "position", "absolute");
    },

    handleEvent: function (event) {
      var outlayerItem = this;
      var method = 'on' + event.type;
      if (outlayerItem[method]) {
        outlayerItem[method](event);
      }
    },

    getSize: function () {
      var outlayerItem = this;
      outlayerItem.size = getSize(outlayerItem.domNode);
    },

    setCSS: function (style) {
      var outlayerItem = this,
          supStyleName;
      _.each(style, function(styleProp, styleName){
        supStyleName = outlayerItem.vendorProperties[styleName] || styleName;
        domStyle.set(outlayerItem.domNode, supStyleName, styleProp);
      });
    },

    getPosition: function () {
      var outlayerItem = this;
      var style = getStyle(outlayerItem.domNode);
      var layoutOptions = outlayerItem.layout.options;
      var isOriginLeft = layoutOptions.isOriginLeft;
      var isOriginTop = layoutOptions.isOriginTop;
      var x = parseInt(style[isOriginLeft ? 'left' : 'right'], 10);
      var y = parseInt(style[isOriginTop ? 'top' : 'bottom'], 10);

      // clean up 'auto' or other non-integer values
      x = isNaN(x) ? 0 : x;
      y = isNaN(y) ? 0 : y;
      // remove padding from measurement
      var layoutSize = outlayerItem.layout.size;
      x -= isOriginLeft ? layoutSize.paddingLeft : layoutSize.paddingRight;
      y -= isOriginTop ? layoutSize.paddingTop : layoutSize.paddingBottom;

      outlayerItem.position.x = x;
      outlayerItem.position.y = y;
    },

    layoutPosition: function () {
      var outlayerItem = this;
      var layoutSize = outlayerItem.layout.size;
      var layoutOptions = outlayerItem.layout.options;
      var style = {};

      if (layoutOptions.isOriginLeft) {
        style.left = (outlayerItem.position.x + layoutSize.paddingLeft) + 'px';
        // reset other property
        style.right = '';
      } else {
        style.right = (outlayerItem.position.x + layoutSize.paddingRight) + 'px';
        style.left = '';
      }

      if (layoutOptions.isOriginTop) {
        style.top = (outlayerItem.position.y + layoutSize.paddingTop) + 'px';
        style.bottom = '';
      } else {
        style.bottom = (outlayerItem.position.y + layoutSize.paddingBottom) + 'px';
        style.top = '';
      }

      outlayerItem.setCSS(style);
      outlayerItem.emit('layout', [outlayerItem]);
    },

    translate: function (x, y) {
      var outlayerItem = this;
      return outlayerItem.is3d ?
        'translate3d(' + _.pixels(x) + ', ' + _.pixels(y) + ', 0)' :
        'translate(' + x + 'px, ' + y + 'px)';
    },

    _transitionTo: function (x, y) {
      var outlayerItem = this;
      outlayerItem.getPosition();
      // get current x & y from top/left
      var curX = outlayerItem.position.x;
      var curY = outlayerItem.position.y;

      var compareX = parseInt(x, 10);
      var compareY = parseInt(y, 10);
      var didNotMove = compareX === outlayerItem.position.x && compareY === outlayerItem.position.y;

      // save end position
      outlayerItem.setPosition(x, y);

      // if did not move and not transitioning, just go to layout
      if (didNotMove && !outlayerItem.isTransitioning) {
        outlayerItem.layoutPosition();
        return;
      }

      var transX = x - curX;
      var transY = y - curY;
      var transitionStyle = {};
      // flip cooridinates if origin on right or bottom
      var layoutOptions = outlayerItem.layout.options;
      transX = layoutOptions.isOriginLeft ? transX : -transX;
      transY = layoutOptions.isOriginTop ? transY : -transY;
      transitionStyle.transform = outlayerItem.translate(transX, transY);

      outlayerItem.transition({
        to: transitionStyle,
        onTransitionEnd: {
          transform: outlayerItem.layoutPosition
        },
        isCleaning: true
      });
    },

    _goTo: function (x, y) {
      var outlayerItem = this;
      outlayerItem.setPosition(x, y);
      outlayerItem.layoutPosition();
    },

    moveTo: function (x, y) {
      var outlayerItem = this;
      return outlayerItem.supportsCSS3 ? outlayerItem._transitionTo(x, y) : outlayerItem._goTo(x, y);
    },

    setPosition: function (x, y) {
      var outlayerItem = this;
      outlayerItem.position.x = parseInt(x, 10);
      outlayerItem.position.y = parseInt(y, 10);
    },

    _nonTransition: function (args) {
      var outlayerItem = this;
      outlayerItem.setCSS(args.to);
      if (args.isCleaning) {
        outlayerItem._removeStyles(args.to);
      }
      _.each(args.onTransitionEnd, function (value, prop){
        args.onTransitionEnd[prop].call(outlayerItem);
      });
    },

    _transition: function (args) {
      var outlayerItem = this;
      if (!parseFloat(outlayerItem.layout.options.transitionDuration)) {
        outlayerItem._nonTransition(args);
        return;
      }
      var _transition = outlayerItem.__transition;

      // keep track of onTransitionEnd callback by css property
      _.each(args.onTransitionEnd, function (value, prop) {
        _transition.onEnd[prop] = args.onTransitionEnd[prop];
      });

      _.each(args.to, function (value, prop) {
        _transition.ingProperties[prop] = true;
        if(args.isCleaning) {
          _transition.clean[prop] = true;
        }
      });

      // set from styles
      if (args.from) {
        outlayerItem.setCSS(args.from);
        // force redraw. http://blog.alexmaccaw.com/css-transitions
        var h = outlayerItem.domNode.offsetHeight;
        // hack for JSHint to hush about unused var
        h = null;
      }
      // enable transition
      outlayerItem.enableTransition(args.to);
      // set styles that are transitioning
      outlayerItem.setCSS(args.to);

      outlayerItem.isTransitioning = true;
    },

    enableTransition: function () {
      var outlayerItem = this;
      outlayerItem.itemTransitionProperties = outlayerItem.transformProperty &&
          _.toDash(outlayerItem.transformProperty + ',opacity');
      if(outlayerItem.isTransitioning) return;
      outlayerItem.setCSS({
        transitionProperty: outlayerItem.itemTransitionProperties,
        transitionDuration: outlayerItem.layout.options.transitionDuration
      });
      outlayerItem.on(outlayerItem.domNode, outlayerItem.transitionEndEvent, outlayerItem);
    },

    transition: function () {
      var outlayerItem = this;
      return outlayerItem.transitionProperty ? outlayerItem._transition.apply(arguments) : outlayerItem._nonTransition.apply(arguments);
    },

    onwebkitTransitionEnd: function (event) {
      var outlayerItem = this;
      outlayerItem.ontransitionend(event);
    },

    onotransitionend: function (event) {
      var outlayerItem = this;
      outlayerItem.ontransitionend(event);
    },

    ontransitionend: function (event) {
      var outlayerItem = this;
      if (event.target !== outlayerItem.domNode) return;

      var _transition = outlayerItem.__transition;
      // get property name of transitioned property, convert to prefix-free
      var propertyName = outlayerItem.dashedVendorProperties[event.propertyName] || event.propertyName;

      delete _transition.ingProperties[propertyName];

      if (_.isEmpty(_transition.ingProperties)) {
        // all properties have completed transitioning
        outlayerItem.disableTransition();
      }
      // clean style
      if (propertyName in _transition.clean) {
        // clean up style
        outlayerItem.domNode.style[event.propertyName] = '';
        delete _transition.clean[propertyName];
      }
      // trigger onTransitionEnd callback
      if (propertyName in _transition.onEnd) {
        var onTransitionEnd = _transition.onEnd[propertyName];
        onTransitionEnd.call(outlayerItem);
        delete _transition.onEnd[propertyName];
      }

      outlayerItem.emit('transitionEnd', [outlayerItem]);
    },

    disableTransition: function () {
      var outlayerItem = this;
      outlayerItem.removeTransitionStyles();
      outlayerItem.off(outlayerItem.domNode, outlayerItem.transitionEndEvent, outlayerItem);
      outlayerItem.isTransitioning = false;
    },

    _removeStyles: function (style) {
      var outlayerItem = this,
          cleanStyle = {};
      _.each(style, function(propValue, prop){
        cleanStyle[prop] = '';
      });
      outlayerItem.setCSS(cleanStyle);
    },

    removeTransitionStyles: function () {
      var outlayerItem = this;
      outlayerItem.setCSS({
        transitionProperty: '',
        transitionDuration: ''
      });
    },

    removeElem: function () {
      var outlayerItem = this;
      outlayerItem.domNode.parentNode.removeChild(outlayerItem.domNode);
      outlayerItem.emit('remove', [outlayerItem]);
    },

    remove: function () {
      var outlayerItem = this;
      // just remove element if no transition support or no transition
      if (!outlayerItem.transitionProperty || !parseFloat(this.layout.options.transitionDuration)) {
        outlayerItem.removeElem();
        return;
      }
      // bind once
      outlayerItem.on('transitionEnd', function() {
        outlayerItem.removeElem();
        return false;
      });
      outlayerItem.hide();
    },

    reveal: function () {
      var outlayerItem = this;
      delete outlayerItem.isHidden;
      // remove display: none
      outlayerItem.setCSS({
        display: ''
      });

      var options = this.layout.options;
      outlayerItem.transition({
        from: options.hiddenStyle,
        to: options.visibleStyle,
        isCleaning: true
      });
    },

    hide: function () {
      var outlayerItem = this;
      outlayerItem.isHidden = true;
      // remove display: none
      outlayerItem.setCSS({
        display: ''
      });

      var options = outlayerItem.layout.options;
      outlayerItem.transition({
        from: options.visibleStyle,
        to: options.hiddenStyle,
        // keep hidden stuff hidden
        isCleaning: true,
        onTransitionEnd: {
          opacity: function() {
            // check if still hidden
            // during transition, item may have been un-hidden
            if (outlayerItem.isHidden) {
              outlayerItem.setCSS({
                display: 'none'
              });
            }
          }
        }
      });
    },

    destroy: function () {
      var outlayerItem = this;
      outlayerItem.setCSS({
        position: '',
        left: '',
        right: '',
        top: '',
        bottom: '',
        transition: '',
        transform: ''
      });
    }

  });
});