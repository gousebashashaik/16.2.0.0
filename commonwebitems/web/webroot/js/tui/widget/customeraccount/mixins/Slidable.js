define('tui/widget/mixins/Slidable', [
  'dojo',
  'dojo/query',
  'dijit/_Widget'], function(dojo) {

  var _transitionPrefix = (window.document.documentElement.style.webkitTransition !== undefined) ?
      'webkitTransition' : (window.document.documentElement.style.MozTransition !== undefined) ?
      'MozTransition' : 'transition';

  var _transformLabel = (window.document.documentElement.style.webkitTransform !== undefined) ?
      'webkitTransform' : (window.document.documentElement.style.MozTransition !== undefined) ?
      'MozTransform' : 'transform';

  function numberBefore(str, sub) {
    str = str || '';
    sub = sub || '';
    return str.substr(0, str.indexOf(sub)) || 0;
  }


  function firstNonEmpty() {
    var i, j;
    for (i = 0, j = arguments.length; i < j; i++) {
      if (!(typeof arguments[i] === 'undefined' || arguments[i] === null)) {
        return arguments[i];
      }
    }
    return null;
  }

  var isNull = function(x) {
    if (typeof x === 'undefined' || x === null) {
      return true;
    }
    return false;
  };


  function pixelValue(str) {
    return str && str.indexOf('px') == -1 ? 0 : window.parseInt(numberBefore(str, 'px'), 0);
  }

  function getStyle(el, styleName) {
    var retval = window.getComputedStyle(el, '').getPropertyValue(styleName);
    if (retval === '') {
      retval = el.style[styleName];
    }
    return retval;

  }

  function applyStyles(element, style) {
    if (style instanceof Object) {
      for (var prop in style) {
        element.style[prop] = style[prop];
      }
    }
  }

  function attachTransitionEndHandler(element, handler) {
    element.addEventListener('webkitTransitionEnd', handler, false);
    element.addEventListener('transitionend', handler, false);
    element.addEventListener('OTransitionEnd', handler, false);
  }


  var Browser = {
    is3dSupported: function() {
      return document.body.style.WebkitPerspective != null && document.body.style.WebkitPerspective != undefined;
    },


    isTransformSupported: function() {
      return !isNull(document.body.style.WebkitTransform) || !isNull(document.body.style.MozTransform) || !isNull(document.body.style.transformProperty);
    }
  };

  function width(element) {
    return pixelValue(getStyle(element, 'width'));
  }

  function left(element) {
    return pixelValue(getStyle(element, 'left'));
  }

  function right(element) {
    return pixelValue(getStyle(element, 'right'));
  }

  var Transformation = {
    applyTransform: function(element, x, y, z, speed, timingFn) {
      var style = {};
      style[_transitionPrefix + 'Property'] = 'all';
      style[_transitionPrefix + 'Delay'] = '0';
      if (speed === 0) {
        style[_transitionPrefix + 'Duration'] = '';
        style[_transitionPrefix + 'TimingFunction'] = '';
      }
      else {
        style[_transitionPrefix + 'Duration'] = speed + 'ms';
        style[_transitionPrefix + 'TimingFunction'] = timingFn;
      }
      var transformValues = x + 'px, ' + y + 'px';
      style[_transformLabel] = Browser.is3dSupported() ? 'translate3d(' + transformValues + ', 0px)' : 'translate(' + transformValues + ')';
      applyStyles(element, style);
    },


    move: function(element, speed, x, y, timingFunction) {
      if (Browser.isTransformSupported()) {
        Transformation.applyTransform(element, x, y, 0, speed, timingFunction || 'ease-out');
      } else {
        console.error('CSS Transform not supported!!');
      }
    },

    resetToCurrentPosition: function(element) {
      var style = {};
      var transVals = Transformation.transformValues(element);
      if (transVals) {
        var domX = pixelValue(getStyle(element, 'left'));
        style[_transitionPrefix + 'Property'] = '';
        style[_transitionPrefix + 'Duration'] = '';
        style[_transitionPrefix + 'TimingFunction'] = '';
        style[_transitionPrefix + 'Delay'] = '';
        style['left'] = (domX + pixelValue(transVals.x)) + 'px';
        style[_transformLabel] = '';
        applyStyles(element, style);
      }
    },

    transformXValue: function(element) {
      var transVals = Transformation.transformValues(element);
      if (transVals) {
        return pixelValue(transVals.x);
      }
      return 0;
    },

    transformValues: function(element) {
      var transform3dSupported = Browser.is3dSupported();
      var transform = firstNonEmpty(element.style.webkitTransform, element.style.MozTransform, element.style.transform);
      if (transform || transform === '') {
        var transformMatch, transformExploded;
        transformMatch = transform3dSupported ? transform.match(/translate3d\((.*?)\)/) : transform.match(/translate\((.*?)\)/);
        if (transformMatch) {
          transformExploded = transformMatch[1].split(', ');
          return {x: transformExploded[0], y: transformExploded[0]};
        }
      }
      return null;
    }
  };


  /*
     supports only drag in x direction
     */
  dojo.declare('tui.widget.mixins.Slidable', [dijit._Widget], {

    postCreate: function() {
      var widget = this;
      var hammer = Hammer(widget.domNode);

      hammer.options['drag_lock_to_axis'] = true;
      hammer.options['swipe'] = false;

      var transitionEndHandler = function(event) {
        var newLeftPosition = Transformation.transformXValue(widget.domNode);
        event.stopPropagation();
        var style = {};
        style[_transitionPrefix + 'Property'] = '';
        style[_transitionPrefix + 'Duration'] = '';
        style[_transitionPrefix + 'TimingFunction'] = '';
        style[_transitionPrefix + 'Delay'] = '';
        style['left'] = (newLeftPosition + left(widget.domNode)) + 'px';
        style[_transformLabel] = '';
        applyStyles(widget.domNode, style);
      };

      var position = 0;

      function handler(ev) {
        if (!ev.gesture) {
          return;
        }
        ev.gesture.preventDefault();

        switch (ev.type) {
          case 'touch':
            attachTransitionEndHandler(widget.domNode, transitionEndHandler);
            break;
          case 'dragleft':
          case 'dragright':
            (function() {
              position = (ev.gesture.center.pageX - ev.gesture.startEvent.center.pageX);
              Transformation.move(widget.domNode, 0, position, 0, 'cubic-bezier(.49, .73, .45, .97)');
            })();
            break;
          case 'release':
            (function() {
              var parentWidth = width(widget.domNode.parentNode);
              var widgetLeftPosition = left(widget.domNode);
              if (widgetLeftPosition + position <= 0) {
                Transformation.move(widget.domNode, 250, -1 * widgetLeftPosition, 0, 'cubic-bezier(.49, .73, .45, .97)');
              } else if (widgetLeftPosition + position >= parentWidth) {
                Transformation.move(widget.domNode, 250, parentWidth - widgetLeftPosition, 0, 'cubic-bezier(.49, .73, .45, .97)');
              }
            })();
            break;
        }

      }

      hammer.on('touch dragstart release dragleft dragright swipeleft swiperight', handler);

      widget.inherited(arguments);
    }
  });
});


