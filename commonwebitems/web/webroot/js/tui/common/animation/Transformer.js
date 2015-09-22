define('tui/common/animation/Transformer', [
    'tui/utils/DetectFeature',
    'tui/common/animation/DOM'
], function(feature, DOM) {

    var is3dSupported = feature.is3dSupported();

    var _transitionPrefix = feature.isTransitionSupported();

    var _transformLabel = feature.isTransformSupported();

    function Transformer(element, options) {
        this.element = element;
        this.options = options || {};
    }

    Transformer.prototype.transformXValue = function() {
        var transformer = this;
        var transVals = transformer.transformValues();
        if (transVals) {
            return _.pixelValue(transVals.x);
        }
        return 0;
    };

    Transformer.prototype.transform = function(x, y, z, speed, timingFn, endHandler) {
        var transformer = this;
        var style = {};
        style[_transitionPrefix + 'Property'] = 'all';
        style[_transitionPrefix + 'Delay'] = '0';
        if (speed === 0) {
            style[_transitionPrefix + 'Duration'] = '';
            style[_transitionPrefix + 'TimingFunction'] = '';
        } else {
            style[_transitionPrefix + 'Duration'] = speed + 'ms';
            style[_transitionPrefix + 'TimingFunction'] = timingFn;
        }
        var transformValues = [_.pixels(x), _.pixels(y)].join(',');
        style[_transformLabel] = is3dSupported ? 'translate3d(' + transformValues + ', ' + _.pixels(z) +')' : 'translate(' + transformValues + ')';

        if (speed && endHandler) {
            setTimeout(function() {
                endHandler();
            }, speed + 10);
        }

        DOM.applyStyles(transformer.element, style);
        return transformer;
    };

    Transformer.prototype.clear = function() {
        var transformer = this;
        var element = transformer.element;
        var style = {};
        var transVals = this.transformValues();
        if (transVals) {
            style[_transitionPrefix + 'Property'] = '';
            style[_transitionPrefix + 'Duration'] = '';
            style[_transitionPrefix + 'TimingFunction'] = '';
            style[_transitionPrefix + 'Delay'] = '';
            style[_transformLabel] = '';
            DOM.applyStyles(element, style);
        }
    };

    Transformer.prototype.transformValues = function() {
        var transformer = this;
        var element = transformer.element;
        //var transform = _.firstNonEmpty(element.style.webkitTransform, element.style.MozTransform, element.style.transform);
        var transform = element.style[_transformLabel];
        if (transform || transform === '') {
            var transformMatch, transformExploded;
            transformMatch = is3dSupported ? transform.match(/translate3d\((.*?)\)/) : transform.match(/translate\((.*?)\)/);
            if (transformMatch) {
                transformExploded = transformMatch[1].split(', ');
                return {x: transformExploded[0], y: transformExploded[0]};
            }
        }
        return null;
    };

    Transformer.prototype.freezeX = function() {
        var transformer = this;
        var style = {};
        var transVals = this.transformValues();
        if (transVals) {
            var domX = transformer.element.offsetLeft;
            style[_transitionPrefix + 'Property'] = '';
            style[_transitionPrefix + 'Duration'] = '';
            style[_transitionPrefix + 'TimingFunction'] = '';
            style[_transitionPrefix + 'Delay'] = '';
            style['left'] = _.pixels((domX + _.pixelValue(transVals.x)));
            style[_transformLabel] = '';
            DOM.applyStyles(transformer.element, style);
        }
    };

    Transformer.prototype.freezeY = function() {
        var transformer = this;
        var style = {};
        var transVals = this.transformValues();
        if (transVals) {
            var domY = _.pixelValue(DOM.getStyle(transformer.element, 'top'));
            style[_transitionPrefix + 'Property'] = '';
            style[_transitionPrefix + 'Duration'] = '';
            style[_transitionPrefix + 'TimingFunction'] = '';
            style[_transitionPrefix + 'Delay'] = '';
            style['top'] = _.pixels((domY + _.pixelValue(transVals.y)));
            style[_transformLabel] = '';
            DOM.applyStyles(transformer.element, style);
        }
    };

    return {

        init: function(element, options) {
            return new Transformer(element, options || {});
        }

    };
});
