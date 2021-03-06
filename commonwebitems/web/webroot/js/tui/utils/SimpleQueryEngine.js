// # SimpleQueryEngine
// ## utility module
//
// The module defines a simple filtering query engine for object stores.
//
// Description:
//
//		The SimpleQueryEngine provides a way of getting a QueryResults through
//		the use of a simple object hash as a filter.  The hash will be used to
//		match properties on data objects with the corresponding value given. In
//		other words, only exact matches will be returned.
//
//		This function can be used as a template for more complex query engines;
//		for example, an engine can be created that accepts an object hash that
//		contains filtering functions, or a string that gets evaluated, etc.
//
//		When creating a new dojo.store, simply set the store's queryEngine
//		field as a reference to this function.
//
// @param {Object} query
//
//		An object hash with fields that may match fields of items in the store.
//		Values in the hash will be compared by normal == operator, but regular expressions
//		or any object that provides a test() method are also supported and can be
// 		used to match strings by more complex expressions
// 		(and then the regex's or object's test() method will be used to match values).
//
//
// @param {dojo.store.util.SimpleQueryEngine.__queryOptions?} options
//
//		An object that contains optional information such as sort, start, and count.
//
// @returns {Function}
//
//		A function that caches the passed query under the field "matches".  See any
//		of the "query" methods on dojo.stores.
//
// @example
//
//		Define a store with a reference to this engine, and set up a query method.
//
//	```var myStore = function(options){
//			//	...more properties here
//			this.queryEngine = dojo.store.util.SimpleQueryEngine;
//			//	define our query method
//			this.query = function(query, options){
//				return dojo.store.util.QueryResults(this.queryEngine(query, options)(this.data));
//			};
//  };```

define(["dojo/_base/array", "dojo/_base/lang"], function (arrayUtil) {
  return function (query, options) {

    // create our matching query function
    switch (typeof query) {
      default:
        throw new Error("Can not query with a " + typeof query);
      case "object":
      case "undefined":
        var queryObject = query;
        query = function (object) {
          for (var key in queryObject) {
            var required = queryObject[key];
            if (required && required.test) {
              if (required.test(object[key])) {
                return true;
              }
            } else if (required.toLowerCase() == object[key].toLowerCase()) {
              return true;
            }
          }
          return false;
        };
        break;
      case "string":
        // named query
        if (!this[query]) {
          throw new Error("No filter function " + query + " was found in store");
        }
        query = this[query];
      // fall through
      case "function":
      // fall through
    }
    function execute(array) {
      // execute the whole query, first we filter
      var results = arrayUtil.filter(array, query);
      // next we sort
      if (options && options.sort) {
        results.sort(function (a, b) {
          for (var sort, i = 0; sort = options.sort[i]; i++) {
            var aValue = a[sort.attribute];
            var bValue = b[sort.attribute];
            if (aValue != bValue) {
              return !!sort.descending == aValue > bValue ? -1 : 1;
            }
          }
          return 0;
        });
      }
      // now we paginate
      if (options && (options.start || options.count)) {
        var total = results.length;
        results = results.slice(options.start || 0, (options.start || 0) + (options.count || Infinity));
        results.total = total;
      }
      return results;
    }

    execute.matches = query;
    return execute;
  };
});