define('tui/common/animation/DOM', [], function() {

    return {

        getStyle: function(el, styleName) {
            var retval = window.getComputedStyle(el, '').getPropertyValue(styleName);
            if (retval === '') {
                retval = el.style[styleName];
            }
            return retval;
        },

        applyStyles: function(element, style) {
            if (style instanceof Object) {
                _.each(style, function(propVal, propKey){
                    element.style[propKey] = propVal;
                });
            }
        },

        width: function(element) {
            return _.pixelValue(this.getStyle(element, 'width'));
        },

        left: function(element) {
            return _.pixelValue(this.getStyle(element, 'left'));
        },

        right: function(element) {
            return _.pixelValue(this.getStyle(element, 'right'));
        }


    };
});