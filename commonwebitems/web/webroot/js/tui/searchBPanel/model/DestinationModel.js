define("tui/searchBPanel/model/DestinationModel", [
    "dojo",
    "dojo/Stateful"], function (dojo, stateful) {

    dojo.declare("tui.searchBPanel.model.DestinationModel", [dojo.Stateful], {

        // ----------------------------------------------------------------------------- properties

        id: null,

        name: null,

        synonym: null,

        type: null,

        multiSelect: true,

		// contains array of airport group children
		children: null,

		// contains group code
		group: null,
		
        fewDaysFlag: false,

        availability: true,
		
        // ----------------------------------------------------------------------------- methods

        constructor: function () {
            var destinationModel = this;
            destinationModel.synonym = '';
			destinationModel.children = [];
			destinationModel.group = [];			
        }
    });

    return tui.searchBPanel.model.DestinationModel;
});
