// # SessionStorage
// ## polyfill module
//
// The module defines a simple polyfill for sessionStorage on unsupported browsers.
// @source https://gist.github.com/remy/350433

define("tui/utils/SessionStorage", ["dojo"], function (dojo) {

    function SessionStorage(repo) {

        this.clear = function () {
            repo.clear();
        };

        this.getItem = function (key) {
            if(repo.getItem(key) && repo.getItem(key) !== "") {
                return dojo.fromJson(repo.getItem(key));
            }
            return null;
        };

        this.setItem = function (key, val) {
            return repo.setItem(key, dojo.toJson(val));
        };

        this.removeItem = function (key) {
            repo.removeItem(key);
        };

    }

    return window.sessionStorage ? new SessionStorage(window.sessionStorage) : new SessionStorage({

        clear: function () {
            window.name = {};
        },

        getItem: function (key) {
            return window.name ? window.name[key] : null;
        },

        removeItem: function (key) {
            delete window.name[key];
        },

        setItem: function (key, value) {
            window.name[key] = value;
        }
    });

});