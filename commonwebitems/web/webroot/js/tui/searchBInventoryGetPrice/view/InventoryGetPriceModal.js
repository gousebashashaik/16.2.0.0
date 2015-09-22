define("tui/searchBInventoryGetPrice/view/InventoryGetPriceModal", [
    "dojo",
    "dojo/store/Observable",
    "tui/searchBPanel/model/DestinationModel",
    "tui/searchBPanel/store/DestinationMultiFieldStore",
    "tui/searchBGetPrice/view/GetPriceModal"], function (dojo, Observable, DestinationModel, DestinationMultiFieldStore) {

    dojo.declare("tui.searchBInventoryGetPrice.view.InventoryGetPriceModal", [tui.searchBGetPrice.view.GetPriceModal], {

        // ---------------------------------------------------------------- properties

        searchPanelModel: null,

        mediator: null,

        jsonData: null,

        subscribableMethods: ["open", "close", "resize"],

        // ---------------------------------------------------------------- methods

        open: function () {
            // summary:
            //		Overides the default open method from popupbase, ensuring that modal DOM container
            var invGetPriceModal = this;

            invGetPriceModal.populateDestinations();
            invGetPriceModal.inherited(arguments);
        },

        populateDestinations: function () {

        	var invGetPriceModal = this;

        	var invControllers = invGetPriceModal.mediator.controllers;
			_.forEach(invControllers, function (invController) {
				if(invController["data-klass-id"] == "showIGPackagesComponent"){
					invGetPriceModal.jsonData = invController.holidays;
				}
			});
			var to = [];
			_.forEach(invGetPriceModal.jsonData, function (holiday) {
				to.push(new Object({
					id: holiday.accommodation.code,
					name: holiday.accommodation.name,
					type: holiday.accommodation.accomType
				}));
			});
			invGetPriceModal.searchPanelModel.to = new Observable(new DestinationMultiFieldStore());
			invGetPriceModal.searchPanelModel.to.setStorageData(to, DestinationModel);
        }

    });

    return tui.searchBInventoryGetPrice.view.InventoryGetPriceModal;
});