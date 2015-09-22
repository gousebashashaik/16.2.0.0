define('tui/widget/booking/insurance/InsuranceModel', [
  'dojo',
  'dojo/query',
  'dojo/dom-class'
], function (dojo, query, domClass) {

  function InsuranceModel() {
    this.insuranceJsonData = {};
    console.log(this.insuranceJsonData,"insuranceJsonData");
  }

  return new InsuranceModel();
});