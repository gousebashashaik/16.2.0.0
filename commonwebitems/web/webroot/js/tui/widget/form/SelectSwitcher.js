define("tui/widget/form/SelectSwitcher", [
  "dojo",
  "dojo/on",
  "dojo/query",
  "dojo/dom-class",
  "tui/widget/form/SelectOption"], function (dojo, on, query, domClass) {

  dojo.declare("tui.widget.form.SelectSwitcher", [tui.widget.form.SelectOption], {

    onChange: function (name, oldValue, newvalue) {
      var selectSwitcher = this;
      selectSwitcher.inherited(arguments);
      if(oldValue.key === newvalue.key) return;

      domClass.remove(dojo.byId(oldValue.value), 'active');
      domClass.add(dojo.byId(newvalue.value), 'active');
    }

  });

  return tui.widget.form.SelectSwitcher;
});