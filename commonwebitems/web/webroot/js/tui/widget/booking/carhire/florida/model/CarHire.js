define("tui/widget/booking/carhire/florida/model/CarHire", ["dojo"], function (dojo) {

		function carHireModel () {
			var carHireModel = this;
			carHireModel.jsonData={};
			carHireModel.cars=[];
			carHireModel.altCars=[];
			carHireModel.flagStatus=false;
			
			carHireModel.villaCars=[];
			carHireModel.VillaAltCars=[];
			carHireModel.VillaCarFlagStatus=[];
			
			carHireModel.displayFlag= true;
			
			
			carHireModel.preSelectTransfer = null;
		}

	return new carHireModel();
});