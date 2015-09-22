define('tui/widget/common/autocomplete/AddButton', [
  'dojo',
  'dojo/on',
  'dojo/dom-class'], function(dojo, on, domClass) {

  function AddButton(dom, onClick, entityStore) {
    var button = this;
    button.dom = dom;

    button.entities = entityStore.query();

    button.entities.observe(function() {
      entityStore.query().length > 0 ? button.show() : button.hide();
    });

    on(dom, 'click', function(e) {
      dojo.stopEvent(e);
      onClick();
    });


    return button;
  }

  AddButton.prototype.show = function() {
    var button = this;
    domClass.add(button.dom, 'active');

  };

  AddButton.prototype.hide = function() {
    var button = this;
    domClass.remove(button.dom, 'active');

  };

  return AddButton;
});


