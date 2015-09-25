define("tui/flights/utils/FlightTimeTableValidations", [
	"dojo",
	"dojo/query",
	"tui/flights/widget/ErrorTooltip",
	"dojo/_base/lang"], function (dojo, query, errorTooltip, lang) {

	dojo.declare("tui.flights.utils.FlightTimeTableValidations", null, {

		validateFlag: false,

		attachId: "",

		dynaId: "",

		msg: "",

		headerMsg: "",

		constructor:function(params){
			var flightTimetableValidations = this;
			flightTimetableValidations.attachId = params.attachId;
			flightTimetableValidations.dynaId = params.dynaId;
			flightTimetableValidations.msg = params.msg;
			flightTimetableValidations.headerMsg = params.headerMsg;
			flightTimetableValidations.validateEmptyFields();
		},
		validateEmptyFields: function(){
			var flightTimetableValidations = this, queryElement,queryValue;
			queryElement = dijit.byId(flightTimetableValidations.attachId);
			if(flightTimetableValidations.attachId == "whenTimeTable"){
				queryValue = queryElement.getSelectedData().value;
			} else {
				queryValue = queryElement.getValue();
			}
			flightTimetableValidations.validated = true;
			if(lang.trim(queryValue) === ""){
				try{
					flightTimetableValidations.throwErrorTooltip();
					flightTimetableValidations.validated = false;
				}catch(e){}
			}else{
				return;
			}

		},
		throwErrorTooltip: function(){
			var flightTimetableValidations = this;
			try{
				  new errorTooltip({
		  			  connectId: flightTimetableValidations.attachId,
		  			  errorMsg: flightTimetableValidations.msg,
		  			  errorHeaderMsg: flightTimetableValidations.headerMsg,
		  			  id: flightTimetableValidations.dynaId
		  		  });
			}catch(e){}

		}
	});

	return tui.flights.utils.FlightTimeTableValidations;
});
