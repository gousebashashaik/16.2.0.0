// #Loadable
// ##Mixin module
//
// Provides method for adding a class into specified DOM element
// This is a mixin class, it must be extended
//

define("tui/widget/mixins/Loadable", [
    "dojo"
], function (dojo) {

    return dojo.declare("tui.widget.mixins.Loadable", null, {

        /**
         * ###loading()
         * Adds class 'loading' to the current Dom Node
         * @param {}
         */
        loading: function () {
            var loadable = this;
            loadable.beforeLoading();
            dojo.addClass(loadable.domNode, "loading");
            loadable.afterLoading();
        },

        /**
         * ###loaded()
         * Removes the class 'loading' from the current Dom Node
         * @param {}
         */
        loaded: function () {
            var loadable = this;
            loadable.beforeLoaded();
            dojo.removeClass(loadable.domNode, "loading");
            loadable.afterLoaded();
        },

        beforeLoading: function(){},

        afterLoading: function(){},

        beforeLoaded: function(){},

        afterLoaded: function(){}
    });
});