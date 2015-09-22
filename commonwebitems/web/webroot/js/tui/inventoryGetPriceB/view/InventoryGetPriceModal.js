define("tui/inventoryGetPriceB/view/InventoryGetPriceModal", [
    "dojo",
    "dojo/store/Observable",
    "tui/searchBPanel/model/DestinationModel",
    "tui/searchBPanel/store/DestinationMultiFieldStore",
    "tui/searchBGetPrice/view/GetPriceModal"], function (dojo, Observable, DestinationModel, DestinationMultiFieldStore) {

    dojo.declare("tui.inventoryGetPriceB.view.InventoryGetPriceModal", [tui.searchBGetPrice.view.GetPriceModal], {

        // ---------------------------------------------------------------- properties
        
        searchPanelModel: null,
        
        mediator: null,
        
        jsonData: null,

        subscribableMethods: ["open", "close", "resize"],

        // ---------------------------------------------------------------- methods

       
        open: function () {
            // summary:
            //		Overides the default close method from popupbase, ensuring that modal DOM container
            //		if configured is closed on close.
            var getPriceModal = this;
            
            var invControllers = getPriceModal.mediator.controllers;
			
			_.forEach(invControllers, function (invController) {
				if(invController["data-klass-id"] == "showIGPackagesComponent"){
					getPriceModal.jsonData = invController.holidays; 
				}
			});
			
			var to = [];
			_.forEach(getPriceModal.jsonData, function (holiday) {
				to.push(new Object({
					id: holiday.accommodation.code,
					name: holiday.accommodation.name,
					type: holiday.accommodation.accomType
				}));
				
			});
			
			getPriceModal.searchPanelModel.to = new Observable(new DestinationMultiFieldStore());
			
			getPriceModal.searchPanelModel.to.setStorageData(to, DestinationModel);
            
            getPriceModal.inherited(arguments);
            
        }

    });

    return tui.inventoryGetPriceB.view.InventoryGetPriceModal;
});