define('tui/common/Sugar', [], function() {
  Function.prototype.callIf = function(cond) {
    var target = this;
    return function() {
      if (cond) {
        console.log('calling...' + target);

        target.apply(target, arguments);
      }
    }
  };
});
