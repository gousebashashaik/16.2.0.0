define('tui/common/Util', ['dojo'], function() {

  function makePositive(input) {
    if (input < 0) {
      return input * -1;
    }
    return input;
  }

  return {

    positive: makePositive,

    isPositive: function(input) {
      return input >= 0;
    },

    negate: function(input) {
      if (this.isPositive(input)) {
        return -1 * input;
      }
      return input;

    },

    diff: function(a, b) {
      return makePositive(makePositive(a) - makePositive(b));
    },

    half: function(input) {
      return Math.ceil(input / 2);
    },

    swipableRatio: function(input) {
      return Math.ceil(input / 18);
    },

    range: function(n) {
      var r = [];
      for (var i = 0; i < n; i++) {
        r.push(i);
      }
      return r;
    },

    filterAndMap: function(xs, filter, mapper) {
      var filtered = _.filter(xs, filter);
      return _.map(filtered, mapper);
    }
  };
});
