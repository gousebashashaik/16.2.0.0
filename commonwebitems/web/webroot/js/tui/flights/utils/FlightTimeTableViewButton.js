define("tui/flights/utils/FlightTimeTableViewButton", [
  "dojo",
  "dojo/text!tui/flights/templates/FlightViewButton.html",
  "dojo/query",
  'dojo/dom-construct',
  "dojo/dom-style",
  "dojo/on",
  "dojo/keys",
  "dojo/ready",
  "dojo/dom-class",
  "dojo/dom-geometry",
  'dojo/_base/html',
  "dojo/_base/lang",
  "dijit/registry",
  "tui/flights/utils/FlightTimeTableValidations",
  "tui/flights/view/FlightActions",
  'tui/flights/view/FlightsMonthBar',
  "tui/flights/widget/MonthPullDown",
  "tui/widget/_TuiBaseWidget"], function (dojo, flightViewButtonTMPL, query, domConstruct, domStyle, on, keys, ready, domClass, domGeom, html, lang, registry, flightTimetableValidations, flightActions, flightMonthBar,MonthPullDown) {
  dojo.declare("tui.flights.utils.FlightTimeTableViewButton", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable], {

	  tmpl: flightViewButtonTMPL,

	  errorMsg : "This field cannot be left blank",

	  constructor: function(){
	  		var flightTimetableViewButton = this;
	  		flightTimetableViewButton.inherited(arguments);
  	  },

  	  postCreate: function(){
  		var flightTimetableViewButton = this;
  		flightTimetableViewButton.inherited(arguments);

  		flightTimetableViewButton.domNode.innerHTML = flightTimetableViewButton.render();

  		query(flightTimetableViewButton.domNode).on("click", function(){

  				var flyingFromObj = new flightTimetableValidations({
  					attachId:"timeTableFlyFrom",
  					dynaId: "flyingFrom",
  					msg: "",
  					headerMsg: flightTimetableViewButton.errorMsg
  				});
  				var flyingToObj = new flightTimetableValidations({
  					attachId:"timeTableFlyTo",
  					dynaId: "flyingTo",
  					msg: "",
  					headerMsg: flightTimetableViewButton.errorMsg
  				});
  				var monthPullDownObj = new flightTimetableValidations({
  					attachId:"whenTimeTable",
  					dynaId: "days-cal-when",
  					msg: "",
  					headerMsg: flightTimetableViewButton.errorMsg
  				});
  				if(flyingFromObj.validated === false || flyingToObj.validated === false || monthPullDownObj.validated === false){
  					return false;
  				}else{

  					flightTimetableViewButton.changeHdrLabels();
  				}
  		});
  	  },


  	  changeHdrLabels: function(){
  		  var fromAir = query("#from-airport")[0],
  		      toAir = query("#to-airport")[0],
  		      timeTableFlyFrom = registry.byId("timeTableFlyFrom"),
  		    timeTableFlyTo = registry.byId("timeTableFlyTo");
   		  query(fromAir).text(registry.byId("timeTableFlyFrom").getLabel().split(",")[0]);
 		  query(toAir).text(registry.byId("timeTableFlyTo").getLabel().split(",")[0]);

  		  domStyle.set(query(".flight-results")[0], {
  			  display:"block"
  		  });

		  domStyle.set(query(".footer-flight-details")[0], {
  			  display:"block"
  		  });

		  query(".timeTableMonthSelector")[0].innerHTML = "";
		  query(".spacer-center")[0].innerHTML = "";


  		  var MD = new MonthPullDown();
		  MD.getAvailableMonths();
  		  /*call json here*/
  	  },

  	  callJson: function(){

  	  },

  	  render: function(){
  		  var flightTimetableViewButton = this;
		  var html = flightTimetableViewButton.renderTmpl(flightTimetableViewButton.tmpl);
		  return html;
  	  }
  });

  	return tui.flights.utils.FlightTimeTableViewButton;
  });