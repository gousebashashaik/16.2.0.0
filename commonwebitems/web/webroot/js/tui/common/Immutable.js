function immute(args, obj) {
  var argumentValues = args;
  var immutable = obj;
  var functionArgumentString = args.callee.toString().match(/\(.*?\)/)[0];
  var argumentNames = functionArgumentString.replace(/[()\s]/g, '').split(',');
  for (var i = 0; i < argumentNames.length; i++) {
    immutable[argumentNames[i]] =
        function() {
      var index = i;
      return function() {
        return argumentValues[index];
      }
    }();
  }
  return immutable;
}
