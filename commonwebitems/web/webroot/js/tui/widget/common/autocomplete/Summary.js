define('tui/widget/common/autocomplete/Summary', [
  'dojo',
  'dojo/on',
  'dojo/dom-class'], function(dojo, on, domClass) {

  function Summary(dom, onClick, entityStore) {
    var summary = this;
    summary.dom = dom;

    summary.entities = entityStore.query();

    summary.entities.observe(function() {
      var entities = entityStore.query();
      _.isEmpty(entities) ? summary.clear() : summary.update(entities[0].name + (entities.length > 1 ? ' +' + _.dec(entities.length) + ' more' : ''));
    });

    on(dom, 'click', function(e) {
      dojo.stopEvent(e);
      onClick();
    });


    return summary;
  }

  Summary.prototype.update = function(text) {
    var summary = this;
    summary.dom.innerHTML = text;
    domClass.remove(summary.dom, 'hide');
  };

  Summary.prototype.clear = function() {
    var summary = this;
    summary.dom.innerHTML = '';
    domClass.add(summary.dom, 'hide');
  };

  Summary.prototype.hide = function() {
    var summary = this;
    domClass.add(summary.dom, 'hide');
  };

  Summary.prototype.show = function() {
    var summary = this;
    !_.isEmpty(summary.dom.innerHTML) ? domClass.remove(summary.dom, 'hide') : null;
  };

  return Summary;
});


