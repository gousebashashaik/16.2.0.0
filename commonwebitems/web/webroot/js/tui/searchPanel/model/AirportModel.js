define("tui/searchPanel/model/AirportModel", ["dojo", "dojo/Stateful"], function (dojo) {

  dojo.declare("tui.searchPanel.model.AirportModel", [dojo.Stateful], {

	  // ----------------------------------------------------------------------------- properties

    // airport id
    id: null,

    // airport name
    name: null,

    // synonym.
    synonym: null,

    // contains array of airport group children
    children: null,

    // contains group code
    group: null,

    // ----------------------------------------------------------------------------- methods

    constructor:function () {
      var airportModel = this;
      airportModel.synonym = '';
      airportModel.children = [];
      airportModel.group = [];
    }
  });

  return tui.searchPanel.model.AirportModel;
});