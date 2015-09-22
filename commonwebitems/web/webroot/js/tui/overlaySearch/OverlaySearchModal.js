define("tui/overlaySearch/OverlaySearchModal", [
  "dojo",
  "dojo/_base/lang",
  "dojo/_base/fx",
  'dojo/_base/connect',
  "dojo/query",
  "dojo/store/Observable",
  "tui/searchPanel/model/AirportModel",
  "tui/searchPanel/store/AirportMultiFieldStore",
  "tui/searchPanel/model/DestinationModel",
  "tui/searchPanel/store/DestinationMultiFieldStore",
  "tui/utils/RequestAnimationFrame",
  "tui/searchGetPrice/view/GetPriceModal"], function (dojo, lang, fx, connect, query, Observable, AirportModel, AirportMultiFieldStore, DestinationModel, DestinationMultiFieldStore) {

  dojo.declare("tui.overlaySearch.OverlaySearchModal", [tui.searchGetPrice.view.GetPriceModal], {

    // ---------------------------------------------------------------- properties
    modal: true,

    data: null,

    subscribableMethods: ["open", "close", "resize"],

    open: function (data) {
      var overlaySearchModal = this;
      overlaySearchModal.data = data || null;
      overlaySearchModal.inherited(arguments);
    },

    initSearchController: function () {
      var overlaySearchModal = this;
      overlaySearchModal.searchPanelModel.resetStores('from');
      overlaySearchModal.searchPanelModel.resetFields({
        'adults': 0,
        'seniors': 0,
        'children': 0
      });

      //populating To
      var to = [], from = [];
      _.each(overlaySearchModal.data.to, function (holiday) {
        to.push(new DestinationModel({
          id: holiday.code,
          name: holiday.name,
          type: holiday.type
        }));
      });

      // retrieve from if available
      // push airports as AirportModel
      _.each(overlaySearchModal.data.from, function (airport) {
        var model = {
          id: airport.id,
          name: airport.name
        };
        _.has(airport, "group") ? model.group = airport.group : null;
        _.has(airport, "synonym") ? model.synonym = airport.synonym : null;
        _.has(airport, "children") ? model.children = airport.children : null;

        from.push(new AirportModel(model));
      });

      overlaySearchModal.searchPanelModel.onRetrieveSavedObject({
        "flexible": _.has(overlaySearchModal.data, "flexibility") ? overlaySearchModal.data.flexibility : overlaySearchModal.searchPanelModel.flexible,
        "flexibleDays": _.has(overlaySearchModal.data, "flexibleDays") ? overlaySearchModal.data.flexibleDays : overlaySearchModal.searchPanelModel.flexibleDays,
        "date": _.has(overlaySearchModal.data, "when") ? overlaySearchModal.searchPanelModel.formatDate(overlaySearchModal.data.when, "dd-MM-yyyy", "EEE d MMMM yyyy") : overlaySearchModal.searchPanelModel.date,
        "adults": _.has(overlaySearchModal.data, "noOfAdults") ? overlaySearchModal.data.noOfAdults : overlaySearchModal.searchPanelModel.adults,
        "seniors": _.has(overlaySearchModal.data, "noOfSeniors") ? overlaySearchModal.data.noOfSeniors : overlaySearchModal.searchPanelModel.seniors,
        "children": _.has(overlaySearchModal.data, "noOfChildren") ? overlaySearchModal.data.noOfChildren : overlaySearchModal.searchPanelModel.children,
        "to": to,
        "from": from
      });

      overlaySearchModal.data = null;

    }

  });
  return tui.overlaySearch.OverlaySearchModal;
});