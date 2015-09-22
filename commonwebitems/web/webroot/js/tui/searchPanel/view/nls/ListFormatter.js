define('tui/searchPanel/view/nls/ListFormatter', [
  'dojo'
], function(dojo) {

  dojo.declare('tui.searchPanel.view.nls.ListFormatter', null, {

    // -----------------------------------------------------------------------------

    // List formatter for English.
    // Adapted from:
    // * https://github.com/twitter/twitter-cldr-js/blob/master/lib/assets/javascripts/twitter_cldr/en.js

    // ----------------------------------------------------------------------------- properties

    // Default formats for English. i18n is injected from the caller.
    formats: {
      '2':      '{0} and {1}',
      'end':    '{0}, and {1}',
      'middle': '{0}, {1}',
      'start':  '{0}, {1}'
    },

    // ----------------------------------------------------------------------------- methods

    _compose_list: function(list) {
      var listFormatter = this;

      var format_key, i, result, _i, _ref;
      result = listFormatter._compose(listFormatter.formats.end || listFormatter.formats.middle || '', [list[list.length - 2], list[list.length - 1]]);
      if (list.length > 2) {
        for (i = _i = 3, _ref = list.length; 3 <= _ref ? _i <= _ref : _i >= _ref; i = 3 <= _ref ? ++_i : --_i) {
          format_key = i === list.length ? 'start' : 'middle';
          if (listFormatter.formats[format_key] === undefined) {
            format_key = 'middle';
          }
          result = listFormatter._compose(listFormatter.formats[format_key] || '', [list[list.length - i], result]);
        }
      }
      return result;
    },

    _compose: function(format, elements) {
      var element, result;
      elements = (function() {
        var _i, _len, _results;
        _results = [];
        for (_i = 0, _len = elements.length; _i < _len; _i++) {
          element = elements[_i];
          if (element !== undefined) {
            _results.push(element);
          }
        }
        return _results;
      })();
      if (elements.length > 1) {
        result = format.replace(/\{(\d+)\}/g, '$1');
        return result.replace(/(\d+)/g, function(a) {
          return elements[parseInt(a, 10)];
        });
      } else {
        return elements[0] || '';
      }
    },

    format: function(list, formats) {
      var listFormatter = this;

      if (formats !== undefined) {
        listFormatter.formats = formats;
      }

      if (listFormatter.formats[list.length.toString()] !== undefined) {
        return listFormatter._compose(listFormatter.formats[list.length.toString()], list);
      } else {
        return listFormatter._compose_list(list);
      }
    }
  });

  return tui.searchPanel.view.nls.ListFormatter;
});
