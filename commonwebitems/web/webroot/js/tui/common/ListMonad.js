/*
    Usage : ListMonad.pipe(data, [function1, function2, function3]);

 */
define('tui/common/ListMonad', [], function() {

  var INSTANCE = new ListMonad();
  function ListMonad() {

    function unit(x) {
      return [x];
    }

    function bind(x, f) {
      var result = [];
      for (var i = 0; i < x.length; i++) {
        result = result.concat(f(x[i]));
      }
      return result;
    }

    ListMonad.prototype.pipe = function(x, fns) {
      var u = x.length ? x : unit(x);
      for (var i = 0; i < fns.length; i++) {
        u = bind(u, fns[i]);
      }
      return u;
    };

  }


  return INSTANCE;
});

