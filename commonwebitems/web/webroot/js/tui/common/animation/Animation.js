define('tui/common/animation/Animation', [
    'tui/common/animation/DOM',
    'tui/common/animation/Transformer',
    'tui/utils/DetectFeature',
    'dojo/dom-style'], function(DOM, Transformer, feature, domStyle) {


    function Animation(element, options) {
        this.element = element;
        this.transformer = Transformer.init(element);
    }

    Animation.prototype.animateHeight = function(h) {

    };

    Animation.prototype.animateWidth = function(w) {

    };

    Animation.prototype.moveX = function(x, speed, timingFn) {
        // same as transformX but updates element's position style to match translated position
        var animation = this;
        animation.transformer.transform(x, 0, 0, speed || 0, timingFn || 'cubic-bezier(.49, .73, .45, .97)', function() {
            animation.transformer.freezeX();
        });
        return animation;
    };

    Animation.prototype.moveY = function(y, speed, timingFn) {
        // same as transformY but updates element's position style to match translated position
        var animation = this;
        animation.transformer.transform(0, y, 0, speed || 0, timingFn || 'cubic-bezier(.49, .73, .45, .97)', function() {
            animation.transformer.freezeY();
        });
        return animation;
    };

    Animation.prototype.transformY = function(y, speed, timingFn) {
        var animation = this;
        animation.transformer.transform(0, y, 0, speed || 0, timingFn || 'cubic-bezier(.49, .73, .45, .97)');
        return animation;
    };

    Animation.prototype.transformX = function(x, speed, timingFn) {
        var animation = this;
        animation.transformer.transform(x, 0, 0, speed, timingFn || 'cubic-bezier(.49, .73, .45, .97)');
        return animation;
    };

    return {

        init: function(element, options) {
            return new Animation(element, options);
        },

        animateHeight: function(element, h) {
            domStyle.set(element, 'height', h + 'px');
        }
    };

});