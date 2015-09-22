define('tui/widget/taggable/PassportAndVisaEditorial', [
  'dojo',
  'tui/widget/mixins/Taggable'
], function(dojo, taggable) {

  dojo.declare('tui.widget.taggable.PassportAndVisaEditorial', [tui.widget._TuiBaseWidget], {

    // ----------------------------------------------------------------------------- methods

    postCreate: function() {
      var passportAndVisaEditorial = this;

      passportAndVisaEditorial.inherited(arguments);
    }
  });

  return tui.widget.taggable.PassportAndVisaEditorial;
});
