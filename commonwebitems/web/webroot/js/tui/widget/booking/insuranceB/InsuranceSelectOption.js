define('tui/widget/booking/insuranceB/InsuranceSelectOption', [
  'dojo',
  'dojo/query',
  'dojo/dom-class',
  'tui/widget/form/SelectOption', "tui/widget/booking/insuranceB/InsuranceModel"], function (dojo, query, domClass, selectoption, InsuranceModel) {

  dojo.declare('tui.widget.booking.insuranceB.InsuranceSelectOption', [tui.widget.form.SelectOption], {
    onChange: function (name, oldValue, newvalue) {
      console.log(newvalue.value)
    }
  });
  return tui.widget.booking.insuranceB.InsuranceSelectOption;
});