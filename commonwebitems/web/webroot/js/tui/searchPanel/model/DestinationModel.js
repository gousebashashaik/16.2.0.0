define("tui/searchPanel/model/DestinationModel", [
    "dojo",
    "dojo/Stateful"], function (dojo, stateful) {

    dojo.declare("tui.searchPanel.model.DestinationModel", [dojo.Stateful], {

        // ----------------------------------------------------------------------------- properties

        id: null,

        name: null,

        synonym: null,

        type: null,

        multiSelect: true,

        // ----------------------------------------------------------------------------- methods

        constructor: function () {
            var destinationModel = this;
            destinationModel.synonym = '';
        }
    });

    return tui.searchPanel.model.DestinationModel;
});
