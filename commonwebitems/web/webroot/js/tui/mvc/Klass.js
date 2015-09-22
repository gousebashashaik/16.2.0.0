define("tui/mvc/Klass", [
    "dojo",
    "dojo/dom-attr",
    "tui/Tui",
    "dojo/_base/lang",
    "tui/mvc/iocConfig",
    "tui/mvc/Pair" ], function (dojo, domAttr, tui, lang, iocConfig, Pair) {

    // creates an object doh!
    function makeObject(key, val) {
        var obj = {};
        obj[key] = val;
        return obj;
    }

    // pair constructor takes an object and returns an array of key/value pairs
    // present in the object
    function makePairs(object) {
        return _.map(_.pairs(object), function (xs) {
            return new Pair(xs);
        });
    }

    // takes the Type (a class) and unique id (key) and creates an instance of class using the creator
    // and places it back into the Type with the id to allow multiple instances of class
    // returns instantiated class or existing object of Type if already instantiated
    function addToSingleton(Type, key, creator) {
        try {
            !Type['singletons'] ? Type['singletons'] = {} : null;
            var obj = !Type['singletons'][key] ? Type['singletons'][key] = creator() : Type['singletons'][key];
            // todo: remove if no longer required/in use
            tui.addSingleton(key, obj);
            return obj;
        } catch (exp) {
            console.error(Type);
            throw exp;
        }
    }

    // extracts id from domNode's data-klass-id attribute
    function extractId(domNode) {
        var id = domAttr.get(domNode, 'data-klass-id');
        return id ? id : function () {
            throw new Error('id cannot be empty!')
        }();
    }

    // checks if configuration specifies object should be a singleton class
    function isSingleton(config) {
        return config.scope === iocConfig.SCOPES.SINGLETON || config.scope === iocConfig.SCOPES.CONFIG;
    }

    // configures binders (watchers)
    function configureBinders(node) {
        if (!node.binds) {
            return node;
        }
        _.each(node.binds, function (bind) {
            _.each(bind.watch, function (methods, field) {
                var targetObject = lang.getObject(bind.target, false, node);
                _.each(methods, function (method) {
                    targetObject.watch(field, dojo.hitch(node, method));
                });
            });
        });
        return node;
    }

    // wiring references in config
    // takes 'pair' object (key/value) and wires referred classes if necessary
    // calls addToSingleton method if config declares this class to be a singleton
    // returns reference to instantiated class
    function wireReference(pair) {
        var config = iocConfig[pair.refKey()];
        if (!config) {
            throw new Error('no config found for reference: ' + pair.refKey());
        }
        var Type = dojo.require(config.type);

        function creator() {
            return configureBinders(new Type(wire(makePairs(config))));
        }

        return isSingleton(config) ? addToSingleton(Type, pair.refKey(), creator) : creator();
    }

    // wires the class
    function wire(pairs) {
        if (_.isEmpty(pairs)) {
            return {};
        }
        var pair = _.first(pairs);
        var restOfPairs = _.rest(pairs);
        if (pair.isReference()) {
            return lang.mixin(makeObject(pair.key, wireReference(pair)), wire(restOfPairs));
        }
        return lang.mixin(pair.process(), wire(restOfPairs));
    }

    dojo.declare("tui.mvc.Klass", null, {

      /**
       * @param domNode
       */
        domNode: null,

      /**
       * @param iocConfig {object?} allows to specify config object if not available in main config file, use for testing alternate configs
       */
        iocConfig: null,

        constructor: function (options, domNode, id) {
            var klass = this;
            id = id || extractId(domNode);
            var config = klass.iocConfig ? klass.iocConfig[id] : iocConfig[id];

            require([config.type], function (Type) {
                try {
                    function creator() {
                        return configureBinders(new Type(lang.mixin(wire(makePairs(config)), options || {}), domNode))
                    }
                    // destroy config domNode once loaded
                    if (config.scope === 'config') {
                        dojo.destroy(domNode);
                    }
                    klass.obj = isSingleton(config) ? addToSingleton(Type, id, creator) : creator();
                } catch (exp) {
                    console.error('error while wiring : ' + id);
                    throw exp;
                }
            });

        },

        getCreatedInstance: function () {
            // return created instance (for on-the-fly wiring)
            return this.obj;
        }

    });

    tui.mvc.Klass.createInstance = function (id, options) {
        new tui.mvc.Klass(options, null, id);
    };

    return tui.mvc.Klass;

});