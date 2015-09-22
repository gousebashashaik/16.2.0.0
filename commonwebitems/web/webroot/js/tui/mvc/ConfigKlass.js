define("tui/mvc/ConfigKlass", ["dojo", "dojo/_base/lang", "tui/mvc/Klass", "tui/mvc/iocConfig"], function (dojo, lang, klass, iocConfig) {

    function isSingleton(configValue) {
        return configValue.type === iocConfig.TYPES.SINGLETON;
    }

    dojo.declare("tui.mvc.ConfigKlass", null, {

        domNode: null,

        wire : function (object) {

        },

        constructor: function (options, domNode) {
            var configValue = iocConfig[klass.extractId(domNode)];

            require([configValue.path], function (ConfigKlass) {
                var instance = new ConfigKlass(lang.mixin(configValue, options));
            });
        }
    });

    return tui.mvc.ConfigKlass;
});