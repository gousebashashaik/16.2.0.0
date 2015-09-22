define('tui/widget/booking/insuranceB/InsuranceModel', [
  'dojo',
  'dojo/query',
  'dojo/dom-class'
], function (dojo, query, domClass) {

  function InsuranceModel() {
    this.insuranceJsonData = {};
    this.summaryDisplayed = false;
    console.log(this.insuranceJsonData,"insuranceJsonData");
  }

  return new InsuranceModel();
});