// # TuiUnderscore
// ## Mixin module
//
// Miscellaneous helper methods, mixed into LoDash for easy global access
//
// @extends LoDash
define("tui/utils/TuiUnderscore", ["dojo/date/locale"], function (dateLocale) {
  var MOCKS_VARIABLE_NAME = "tuiMocks";

  _.mixin({

    /**
     * ###mock()
     * mock/stub functions
     *
     * @param obj
     * @param methName
     * @param fn
     * @returns {Array} reference to mocked method
     */
    mock: function (obj, methName, fn) {
      var orig = obj[methName];
      var handle = [obj, methName, orig];
      obj[methName] = fn;
      if (!obj[MOCKS_VARIABLE_NAME]) {
        obj[MOCKS_VARIABLE_NAME] = [];
      }
      obj[MOCKS_VARIABLE_NAME].push(handle);
      return handle;
    },

    /**
     * ###unmock()
     * unmocks specific mocked method
     *
     * @param handle reference to mocked method
     */
    unmock: function (handle) {
      handle[0][handle[1]] = handle[2];
    },

    /**
     * ###unmockAll()
     * unmocks all mocked methods
     *
     * @param obj
     */
    unmockAll: function (obj) {
      _.each(obj[MOCKS_VARIABLE_NAME], function (handle) {
        _.unmock(handle);
      });
    },

    /**
     * ###valid()
     * tests if arguments are valid ie `!null` and `!undefined`
     */
    valid: function () {
      if (_.any(arguments, function (arg) {
        return arg == null || typeof(arg) === 'undefined';
      })) {
        throw ("invalid parameter passed to : " + arguments.callee);
      }
    },

    /**
     * ###debug()
     * logs errors to console if in 'dev' environment
     * optionally throws an exception
     *
     * @param {String} message this will be logged to the console or used as the message for exception thrown
     * @param {Boolean?} throwError whether or not to throw an exception or simply log the error
     */
    debug: function (message, throwError) {
      if (dojoConfig.devDebug) {
        if (throwError) {
          throw message;
        } else {
          console.debug(message);
        }
      }
    },

    /**
     * ###formatDate()
     * formats given date string using formats provided (or defaults)
     *
     * @param {String} date string representing a date
     * @param {String?} inFormat format of given date string, defaults to `dd-MM-yyyy`
     * @param {String?} outFormat format of output string, defaults to `EEE d MMM yyyy`
     * @returns {String} string representing given date in the output format
     */
    formatDate: function (date, inFormat, outFormat) {
      // define defaults
      inFormat = inFormat || "dd-MM-yyyy";
      outFormat = outFormat || "EEE d MMM yyyy";

      var dateObject = dateLocale.parse(date, {
        selector: "date",
        datePattern: inFormat
      });

      // throws error if supplied string doesn't match `inFormat`
      if (!dateObject) {
        throw ("Error: The date doesn't match the given format");
      }

      return dateLocale.format(dateObject, {
        selector: "date",
        datePattern: outFormat
      });
    },

    /**
     * ###formatTime()
     * formats given time string using formats provided
     *
     * @param {String} time string representing a time value
     * @param {String?} inFormat format of given time string, defaults to HHmm
     * @param {String?} outFormat format of output string, defaults to HH:mm
     * @returns {String} string representing given time in the output format
     */
    formatTime: function (time, inFormat, outFormat) {
      // define defaults
      inFormat = inFormat || "HHmm";
      outFormat = outFormat || "HH:mm";

      var timeObject = dateLocale.parse(time, {
        selector: "time",
        timePattern: inFormat
      });

      // throws error if supplied string doesn't match `inFormat`
      if (!timeObject) throw ("Error: The time doesn't match the given format");

      return dateLocale.format(timeObject, {
        selector: "time",
        timePattern: outFormat
      });
    },

    /**
     * ###negate()
     * returns negative representation of supplied number
     *
     * @param {number} value
     * @returns {number}
     */
    negate: function (value) {
      return -1 * value;
    },

    /**
     * ###when()
     * executes supplied method if condition is met
     *
     * @param {*} cond condition to test against
     * @param {Function} fn method to execute
     * @returns {Function|null}
     */
    when: function (cond, fn) {
      return cond ? fn() : null;
    },

    /**
     * ###inc()
     * increments supplied number by 1
     *
     * @param {number|string} x number to increment
     * @returns {number|string}
     */
    inc: function (x) {
      return x + 1;
    },

    /**
     * ###dec()
     * reduces supplied number by 1
     *
     * @param {number|string} x number to reduce
     * @returns {number|string}
     */
    dec: function (x) {
      return x - 1;
    },

    /**
     * ###numberBefore()
     * given a string containing a number and a suffix (eg `22px`)
     * returns only the number
     *
     * @param {string} str complete string to parse
     * @param {string} sub string to remove from `str`
     * @returns {string|number}
     */
    numberBefore: function (str, sub) {
      str = str || '';
      sub = sub || '';
      return str.substr(0, str.indexOf(sub)) || 0;
    },

    /**
     * ###pixelValue()
     * given a pixel-value string (eg `25px`) returns the number (eg `25`)
     *
     * @param str
     * @returns {number}
     */
    pixelValue: function (str) {
      return str && str.indexOf('px') == -1 ? 0 : parseInt(_.numberBefore(str, 'px'), 0);
    },

    /**
     * ###pixels()
     * given a number returns it as a pixel-value string
     *
     * @param {number|string} n
     * @returns {string}
     */
    pixels: function (n) {
      return n + 'px';
    },

    /**
     * ###isPositive()
     * determines if a number is of type Number and is positive
     *
     * @param n number to test
     * @returns {boolean}
     */
    isPositive: function (n) {
      return _.isNumber(n) && n >= 0;
    },

    /**
     * ###isNegative()
     * determines if a number is of type Number and is negative
     *
     * @param n number to test
     * @returns {boolean}
     */
    isNegative: function (n) {
      return !_.isPositive(n);
    },

    /**
     * ###isNull()
     * determines if a given value is `undefined` or `null`
     *
     * @param x
     * @returns {boolean}
     */
    isNull: function (x) {
      return (typeof x === 'undefined' || x === null);
    },

    /**
     * ###capitalize()
     * given a string (eg `transition`) returns capitalised string (eg `Transition`)
     *
     * @param str
     * @returns {string}
     */
    capitalize: function (str) {
      return str.charAt(0).toUpperCase() + str.slice(1);
    },

    /**
     * ###repeat()
     * returns an array of length n with value x repeated
     *
     * @param x
     * @param {number} n number representing size of desired array
     * @returns {Array}
     */
    repeat: function(x, n) {
      return _.map(_.range(n), function() {
        return x;
      });
    },

    /**
     * ###interpose()
     * given an array of values and a delimiter, joins the values into one string separated by delimiter
     *
     * @param xs
     * @param {string} str delimiter
     * @returns {string}
     */
    interpose: function (xs, str) {
      var strs = _.repeat(str, _.compose(_.dec, _.size)(xs));
      return _.map(_.zip(xs, strs),function (x) {
        return x[0] + (x[1] || '');
      }).join('');
    },

    /**
     * ###wrapWords()
     *  wraps given text in `line` (eg `Spain`) with `<strong></strong>` by default.
     *  optionally provide `pref` and `suff` to change this
     *
     *  eg given `Spa Hotel, Spain` as `line` and `spa` as word
     *  returns `<strong>Spa</strong> Hotel, <strong>Spa</strong>in`
     *
     * @param {string} line a string of text
     * @param {string} word the word to wrap
     * @param {string} pref html tag or string to insert before word
     * @param {string} suff html tag or string to insert after word
     * @returns {string}
     */
    wrapWords: function(line, word, pref, suff) {
      pref = pref || '<strong>';
      suff = suff || '</strong>';
      var regex = new RegExp('(' + word + ')', 'gi');
      return line.replace(regex, pref + '$1' + suff);
    },

    /**
     * ###toDash()
     * Converts strings from camelCase to hyphenated equivalent
     * @param {String} str the string to hyphenate
     * @returns {String}
     */
    toDash: function (str) {
      return str.replace(/([A-Z])/g, function($1) {
        return '-' + $1.toLowerCase();
      });
    },

    /**
     * ###makeArray()
     * Converts an element or nodeList into an array
     * @param {Object} obj the element/nodelist to convert
     * @returns {Array}
     */
    makeArray: function (obj) {
      var ary = [];
      if (_.isArray(obj)) {
        // use object if already an array
        ary = obj;
      } else if (obj && typeof obj.length === 'number') {
        // convert nodeList to array
        for (var i = 0, len = obj.length; i < len; i++) {
          ary.push(obj[i]);
        }
      } else {
        // array of single index
        ary.push(obj);
      }
      return ary;
    },

    /**
     * ###mapcat()
     * Maps a collection and concat the mapped elements
     * @param {Array} xs the element/nodelist to map
     * @param {Function} fn the mapping function
     * @returns {Array}
     */
    mapcat: function (xs, fn) {
      return _.flatten(_.map(xs, fn));
    },

    /**
     * ###repeatedly()
     * Maps a collection and concat the mapped elements
     * @param {Array} xs the element/nodelist to map
     * @param {Function} fn the mapping function
     * @returns {Array}
     */
    repeatedly: function(n, fn){
      return _.map(_.range(n), fn);
    }

  });

  return _;

});