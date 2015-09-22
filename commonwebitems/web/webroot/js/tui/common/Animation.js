/**
 * SHOULD BE DELETED! REFER TO tui/common/animation/Animation instead
 */

define('tui/common/Animation', [
  'dojo',
  'dojo/dom-style'
], function (dojo, domStyle) {

  var FRICTION = 0.025;

  var _transitionPrefix = (window.document.documentElement.style.webkitTransition !== undefined) ?
    'webkitTransition' : (window.document.documentElement.style.MozTransition !== undefined) ?
    'MozTransition' : (window.document.documentElement.style.msTransition !== undefined) ?
    'msTransition' : 'transition';

  var _transformLabel = (window.document.documentElement.style.webkitTransform !== undefined) ?
    'webkitTransform' : (window.document.documentElement.style.MozTransition !== undefined) ?
    'MozTransform' : (window.document.documentElement.style.msTransform !== undefined) ?
    'msTransform' : 'transform';

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

  var isNull = function (x) {
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

  var Browser = {
    is3dSupported: function () {
      return document.body.style.WebkitPerspective != null && document.body.style.WebkitPerspective != undefined;
    },


    isTransformSupported: function () {
      return !isNull(document.body.style.WebkitTransform)
        || !isNull(document.body.style.MozTransform)
        || !isNull(window.document.documentElement.style.msTransform)
        || !isNull(document.body.style.transformProperty);
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

  function attachListeners(element, handler) {
    element.addEventListener('webkitTransitionEnd', handler, false);
    element.addEventListener('transitionend', handler, false);
    element.addEventListener('OTransitionEnd', handler, false);
    element.addEventListener('oTransitionEnd', handler, false);
    element.addEventListener('MSTransitionEnd', handler, false);
  }

  function removeListeners(element, listener) {
    element.removeEventListener('webkitTransitionEnd', listener);
    element.removeEventListener('transitionend', listener);
    element.removeEventListener('OTransitionEnd', listener);
    element.removeEventListener('oTransitionEnd', listener);
    element.removeEventListener('MSTransitionEnd', listener);
  }

  var Transformation = {
    applyTransform: function (element, x, y, z, speed, timingFn) {
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
      style[_transformLabel] = 'translateX(' + x + 'px)';
      //Translate 3d was causing problems when there is translate property on css already (webkit translatez for z-index)
      //Browser.is3dSupported() ? 'translate3d(' + transformValues + ', 0px)' : 'translate(' + transformValues + ')';
      applyStyles(element, style);
    },

    clear: function (element) {
      var style = {};
      var transVals = Transformation.transformValues(element);
      if (transVals) {
        style[_transitionPrefix + 'Property'] = '';
        style[_transitionPrefix + 'Duration'] = '';
        style[_transitionPrefix + 'TimingFunction'] = '';
        style[_transitionPrefix + 'Delay'] = '';
        style[_transformLabel] = '';
        applyStyles(element, style);
      }
    },


    move: function (element, speed, x, y, timingFunction) {
      if (Browser.isTransformSupported()) {
        Transformation.applyTransform(element, x, y, 0, speed, timingFunction || 'ease-out');
      } else {
        console.error('CSS Transform not supported!!');
      }
    },

    resetToCurrentPosition: function (element) {
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

    resetTo: function (element, position) {
      var style = {};
      style[_transitionPrefix + 'Property'] = '';
      style[_transitionPrefix + 'Duration'] = '';
      style[_transitionPrefix + 'TimingFunction'] = '';
      style[_transitionPrefix + 'Delay'] = '';
      style['left'] = position + 'px';
      style[_transformLabel] = '';
      applyStyles(element, style);
    },

    transformXValue: function (element) {
      var transVals = Transformation.transformValues(element);
      if (transVals) {
        return pixelValue(transVals.x);
      }
      return 0;
    },

    transformValues: function (element) {
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


  function Animation(element) {
    this.element = element;

  }

  Animation.prototype.elementWidth = function () {
    return width(this.element);
  };


  Animation.prototype.animateHeight = function () {

  };


  Animation.prototype.swipe = function (x, deltaTime, callback, timingFn) {
    var animation = this;
    var swipeSpeed = Math.abs(x) / deltaTime,
      deceleration = 0.0006,
      direction = (_.isPositive(x) ? 1 : -1),
      distance = Math.round((swipeSpeed * swipeSpeed) / (2 * deceleration)) * direction,
      duration = Math.round(Math.abs(x) / swipeSpeed);
    animation.transformX(distance, duration, callback, timingFn || 'cubic-bezier(0.25, 0.46, 0.45, 0.94)');
    if (_.isNegative(direction) && Math.abs(distance) >= animation.elementWidth()) {
      animation.transformX(0, duration, callback, timingFn || 'ease-in');
    }

  };

  Animation.prototype.moveX = function (x, speed, callback, timingFn) {
    var animation = this;
    var listener = (function () {
      removeListeners(animation.element, listener);
      Transformation.resetToCurrentPosition(animation.element);
      callback ? callback() : null;
    }).bind(animation);

    attachListeners(animation.element, listener);
    Transformation.move(animation.element, speed, x, 0, timingFn || 'cubic-bezier(.49, .73, .45, .97)');
    return animation;
  };


  Animation.prototype.transformX = function (x, speed, container, callback, timingFn) {
    var animation = this;
    var listener = (function () {
      removeListeners(animation.element, listener);
      if (container) {
        var parentWidth = width(container);
        var widgetLeftPosition = left(animation.element);
        if (widgetLeftPosition + x <= 0) {
          Transformation.move(animation.element, speed, -1 * widgetLeftPosition, 0, 'cubic-bezier(.49, .73, .45, .97)');
        } else if (widgetLeftPosition + position >= parentWidth) {
          Transformation.move(animation.element, 250, parentWidth - widgetLeftPosition, 0, 'cubic-bezier(.49, .73, .45, .97)');
        }
      }
      callback ? callback() : null;
    }).bind(animation);

    attachListeners(animation.element, listener);
    Transformation.move(animation.element, speed, x, 0, timingFn || 'cubic-bezier(.49, .73, .45, .97)');
    return animation;
  };

  Animation.prototype.fixLeft = function () {
    var animation = this;
    Transformation.resetToCurrentPosition(animation.element);
  };


  return {

    init: function (element) {
      return new Animation(element);
    },

    animateHeight: function (element, h) {
      domStyle.set(element, 'height', h + 'px');
    }

  };
});
