define('tui/widget/booking/insurance/InsuranceSelectOption', [
  'dojo',
  'dojo/query',
  'dojo/dom-class',
  'tui/widget/form/SelectOption', "tui/widget/booking/insurance/InsuranceModel"], function (dojo, query, domClass, selectoption, InsuranceModel) {

  dojo.declare('tui.widget.booking.insurance.InsuranceSelectOption', [tui.widget.form.SelectOption], {
    onChange: function (name, oldValue, newvalue) {
      console.log(newvalue.value)
    }
  });
  return tui.widget.booking.insurance.InsuranceSelectOption;
});