define("tui/mvc/Pair", [], function () {

    var ignoredFields = ['type', 'scope'];

    function makeObject(key, val) {
        var obj = {};
        obj[key] = val;
        return obj;
    }

    function Pair(xs) {
        this.key = xs[0];
        this.value = xs[1];
    }

    Pair.prototype.process = function () {
        var pair = this;
        return pair.isIgnored() ? {} : makeObject(pair.key, pair.value);
    };

    Pair.prototype.isReference = function () {
        var pair = this;
        return typeof pair.value == 'string' && pair.value.indexOf('$') == 0;
    };

    Pair.prototype.isIgnored = function () {
        var pair = this;
        return pair && _.contains(ignoredFields, pair.key);
    };

    Pair.prototype.refKey = function() {
        if (this.isReference()) {
            return this.value.replace('$', '');
        } else {
            return this.key;
        }
    };

    return Pair;
});