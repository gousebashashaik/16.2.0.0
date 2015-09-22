define('tui/common/TuiUnderscore', [
  'dojo/dom-style',
  'dojo/date/locale'
], function(domStyle, dateLocale) {

  _.mixin(
    {
      negate: function(value) {
        return -1 * value;
      },

      hide: function(element) {
        domStyle.set(element, 'display', 'none');
      },

      show: function(element) {
        if(element) {
          domStyle.set(element, 'display', 'block');
        }
      },

      when: function(cond, fn) {
        return cond ? fn() : null;
      },

      inc: function(x) {
        return x + 1;
      },

      dec: function(x) {
        return x - 1;
      },

      numberBefore: function(str, sub) {
        str = str || '';
        sub = sub || '';
        return str.substr(0, str.indexOf(sub)) || 0;
      },


      pixelValue: function(str) {
        return str && str.indexOf('px') == -1 ? 0 : window.parseInt(this.numberBefore(str, 'px'), 0);
      },

      pixels: function(n) {
        return n + 'px';
      },

      isPositive: function(n) {
        return _.isNumber(n) && n >= 0;
      },

      isNegative: function(n) {
        return !_.isPositive(n);
      },

      singlePixel: function() {
        return 1;
      },

      isNull: function(x) {
        if(typeof x === 'undefined' || x === null) {
          return true;
        }
        return false;
      },

      firstNonEmpty: function() {
        var i, j;
        for(i = 0, j = arguments.length; i < j; i++) {
          if(!(typeof arguments[i] === 'undefined' || arguments[i] === null)) {
            return arguments[i];
          }
        }
        return null;
      },

      zipWith: function(arr1, arr2, fn) {
        return _.map(_.zip(arr1, arr2), function(pair) {
          return fn(pair[0], pair[1]);
        });
      },

      repeat: function(x, n) {
        return _.map(_.range(n), function() {
          return x;
        });
      },

      repeatedly: function(n, fn) {
        return _.map(_.range(n), fn);
      },

      matrix: function(rows, columns /*, default value */) {
        var matrix = [];
        var value = arguments[2] || null;
        _.each(_.range(rows), function() {
          matrix.push(_.repeat(value, columns));
        });
        return matrix;
      },

      highlightWords: function(line, word) {
        var regex = new RegExp('(' + word + ')', 'gi');
        return line.replace(regex, '<strong>$1</strong>');
      },

      capitalize: function(str) {
        return str[0].toUpperCase() + str.slice(1);
      },

      ordinal: function(n) {
        var s = ['th', 'st', 'nd', 'rd'],
          v = n % 100;
        return n + (s[(v - 20) % 10] || s[v] || s[0]);
      },

      not: function(fn) {
        return function() {
          return !fn();
        }
      },

      isBetween: function(x, xs) {
        var sortedList = _.sortBy(xs, _.identity);
        return x >= _.first(sortedList) && x <= _.last(sortedList);
      },

      watch: function(fields, object, fn) {
        _.each(fields, function(field) {
          object.watch(field, function(name, oldValue, newValue) {
            fn(name, oldValue, newValue)
          })
        })
      },

      safeCall: function(fn, context) {
        var self = context || window;
        fn ? fn.call(self) : '';
      },

      safeArray: function(xs) {
        return _.filter(xs, function(x) {
          return !_.isNull(x);
        })
      },

      prefix: function(pre) {
        return function(str) {
          return pre + str;
        }
      },

      repeat: function(x, n) {
        return _.map(_.range(n), function() {
          return x;
        })
      },

      reduceAndExtend: function(xs, length) {
        var arr = [];
        _.reduceRight(xs, function(a,b){
          for(var i = 0; i < length; i++) {
            arr.push(_.extend(a[i],b[i]));
          }
        })
        return arr;
      },

      interpose: function(xs, str) {
        var strs = _.repeat(str, _.compose(_.dec, _.size)(xs));
        return _.map(_.zip(xs, strs),function(x) {
          return x[0] + (x[1] || '')
        }).join('');
      },

      deviceOrientation: function() {
        switch(window.orientation) {
          case 0:
            return 'portrait'
            break;
          case 180:
            return 'portrait'
            break;
          case -90:
            return 'landscape'
            break;
          case 90:
            return 'landscape'
            break;
        }
      },

      formatDate: function(date, inFormat, outFormat) {
        // define defaults
        inFormat = inFormat || "dd-MM-yyyy";
        outFormat = outFormat || "EEE d MMM yyyy";

        var dateObject = dateLocale.parse(date, {
          selector: "date",
          datePattern: inFormat
        });

        // throws error if supplied string doesn't match `inFormat`
        if(!dateObject) {
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
      formatTime: function(time, inFormat, outFormat) {
        // define defaults
        inFormat = inFormat || "HHmm";
        outFormat = outFormat || "HH:mm";

        var timeObject = dateLocale.parse(time, {
          selector: "time",
          timePattern: inFormat
        });

        // throws error if supplied string doesn't match `inFormat`
        if(!timeObject) throw ("Error: The time doesn't match the given format");

        return dateLocale.format(timeObject, {
          selector: "time",
          timePattern: outFormat
        });
      }

    });

  return _;

});
