define("tui/widget/form/AutoComplete", [
  "dojo",
  "tui/widget/_TuiBaseWidget",
  "tui/widget/mixins/AutoCompleteable"
], function(dojo) {

  dojo.declare("tui.widget.form.AutoComplete", [tui.widget._TuiBaseWidget, tui.widget.mixins.AutoCompleteable], {

    // ---------------------------------------------------------------- autoComplete methods

    postCreate: function() {
      var autoComplete = this;
      autoComplete.autoCompleteableInit();

      autoComplete.inherited(arguments);
    }
  });

  return tui.widget.form.AutoComplete;
});
