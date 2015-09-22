// # JsonCache
// ## Mixin module
//
// This module provides a JSON memory cache updated by AJAX requests,
// to which watchers can be attached to respond to changes in the cache.
// This is a mixin class, it must be extended
//
// @borrows `dojo.store.Memory`
// @author: Maurice Morgan.

define("tui/store/JsonCache", [
  "dojo",
  "dojo/store/Memory",
  "dojo/io-query",
  "tui/utils/SimpleQueryEngine"], function (dojo, Memory, ioQuery) {

  return dojo.declare("tui.store.JsonCache", null, {

    // ---------------------------------------------------------------- properties

    /**
     * ###targetURL
     * URL that will be used to send server requests (ajax)
     * @type {String}
     */
    targetURL: null,

    /**
     * ###_cacheStore
     * Reference to the memory cache
     * @private
     * @type {dojo.store.Memory|null}
     */
    _cacheStore: null,

    /**
     * ###accepts
     * Property defines accepts headers, limiting the type
     * of response that will be accepted
     * @type {String}
     */
    accepts: "application/javascript, application/json",

    /**
     * ###cacheonly
     * Determine if query should only be applied to results in the
     * cache or if a server request should be attempted
     * @type {Boolean|null}
     */
    cacheonly: null,

    /**
     * ###resultsToCache
     * Indicates whether or not results will be cached
     * @type {Boolean}
     */
    resultsToCache: true,

    /**
     * ###currentRequest
     * Stored reference to current request object
     * @type {Object|null}
     */
    currentRequest: null,

    // ---------------------------------------------------------------- dojo.store.Memory properties

    /**
     * ###idProperty
     * Indicates the property to use as the identity property.
     * The values of this property should be unique.
     * @type {String}
     */
    idProperty: "id",

    /**
     * ###queryEngine
     * Defines the query engine to use for querying the data store
     * @type {Function}
     */
    queryEngine: null,

    // ---------------------------------------------------------------- methods

    /**
     * ###setupCacheStore()
     * Initialises the memory store
     */
    setupCacheStore: function () {
      var jsonCache = this;
      var options = {
        idProperty: jsonCache.idProperty
      };
      if (jsonCache.queryEngine) {
        options.queryEngine = jsonCache.queryEngine;
      }
      jsonCache._cacheStore = new Memory(options);
    },

    /**
     * ###getData()
     * Returns data from the memory store
     * @returns {data|*}
     */
    getData: function () {
      var jsonCache = this;
      return jsonCache._cacheStore.data;
    },

    /**
     * ###setData()
     * Sets data to the memory store
     * @param data
     */
    setData: function (data) {
      var jsonCache = this;
      jsonCache._cacheStore.setData(data);
    },

    /**
     * ###query()
     * Queries the memory store, optionally sends a request to the server for data
     * @param query
     * @param options
     * @param cacheonly
     * @returns {*|Function|null|Promise}
     */
    query: function (query, options, cacheonly) {
      var jsonCache = this;
      // exit early if a targetURL has not been defined
      if (!jsonCache.targetURL) return;

      var fromCache = true;
      cacheonly = jsonCache.cacheonly || cacheonly;
      options = options || {};

      // first we query the memory store
      var results = jsonCache._cacheStore.query(query, options);

      // if no results and `cacheonly` is false
      // create a request string
      if (results.length === 0 && (!cacheonly)) {
        fromCache = false;
        // if regular expression just give me the data
        for (var prop in query) {
          if (query[prop].test) {
            var parseString = /\/([^}]+)\//;
            var answer = query[prop].toString().match(parseString);
            if (answer.length > 0) {
              query[prop] = answer[1];
            }
          }
        }

        // create the request object
        if (options.searchQuery) {
          query = options.searchQuery;
        } else if (query && typeof query == "object") {
          query = ioQuery.objectToQuery(query);
          query = query ? "?" + query : "";
        }
        results = jsonCache.requestJson(query);
      }

      // return the promise
      return dojo.when(results, function (dataresults) {
        if (!fromCache && dataresults) {
          dataresults = jsonCache.onBeforeSetResults(dataresults) || dataresults;
          if (jsonCache.resultsToCache) {
            jsonCache.setData(dataresults);
          }
        }
        jsonCache.onResults(dataresults);
      });
    },

    /**
     * ###requestJson()
     * Fires a GET request to the server with the given query
     * @param {String} query the query string sent to the targetURL
     * @returns {Object|null|currentRequest|*}
     */
    requestJson: function (query) {
      var jsonCache = this;
      // apply accept headers
      var headers = {Accept: jsonCache.accepts};
      // if existing request hasn't yet returned cancel it early to avoid
      // older requests replacing more recent requests
      if (jsonCache.currentRequest) {
        jsonCache.currentRequest.cancel();
      }
      // save a reference to the current request
      jsonCache.currentRequest = dojo.xhr("GET", {
        url: jsonCache.targetURL + (query || ""),
        handleAs: "json",
        headers: headers
      });
      // and return it
      return jsonCache.currentRequest;
    },

    // ---------------------------------------------------------------- jsonCache event

    /**
     * ###onBeforeSetResults()
     * event method fired before results have been set into the memory store
     * to allow manipulation of the results
     * @param {Object} dataresults data returned from the server
     * @returns {null|dataresults}
     */
    onBeforeSetResults: function (dataresults) {
      return null;
    },

    /**
     * ###onResults
     * event method fired once results have been set to the memory store
     * @param {Object} dataresults data returned from the server
     */
    onResults: function (dataresults) {
    }
  });
});




