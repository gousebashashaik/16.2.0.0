define('tui/widget/mobile/Select', [
  'dojo',
  'dojo/dom-class',
  'dojo/query',
  'dijit/_Widget'], function(dojo, domClass, query) {

  query('.select').forEach(function(selectDiv) {
    var target = query('span', selectDiv)[0];
    var select = query('select', selectDiv)[0];
    if (target && select) {
    dojo.connect(select, 'onchange', function(e) {
      target.innerHTML = e.target.value;
    });
    }
  });


});


