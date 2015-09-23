define("tui/flights/utils/ReverseAirportsLinkUtil", [
  "dojo",
  "dojo/text!tui/flights/templates/ReverseAirportsLink.html",
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
  "tui/flights/utils/FlightTimeTableValidations",
  "tui/flights/view/FlightActions",
  'tui/flights/view/FlightsMonthBar',
  "tui/flights/widget/MonthPullDown",
  "dijit/registry",
  "tui/widget/_TuiBaseWidget"], function (dojo, reverseLinkTMPL, query, domConstruct, domStyle, on, keys, ready, domClass, domGeom, html, lang, flightTimetableValidations, flightActions, flightMonthBar, monthPullDown,registry) {
  dojo.declare("tui.flights.utils.ReverseAirportsLinkUtil", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable], {

	  tmpl: reverseLinkTMPL,
	  reverseAirportLink : 0,
	  errorMsg : "This field cannot be left blank",

	  constructor: function(){
	  //console.clear();
	  		var reverseAirportsLinkUtil = this;
	  		reverseAirportsLinkUtil.inherited(arguments);
  	  },

  	  postCreate: function(){
  		var reverseAirportsLinkUtil = this;
  		reverseAirportsLinkUtil.inherited(arguments);


  		reverseAirportsLinkUtil.domNode.innerHTML = reverseAirportsLinkUtil.render();

  		query(reverseAirportsLinkUtil.domNode).on("click", function(){

  	/*
  				var flyingFromObj = new flightTimetableValidations({
  					attachId:"#flying-from",
  					dynaId: "flyingFrom",
  					msg: "",
  					headerMsg: flightTimetableViewButton.errorMsg
  				});
  				var flyingToObj = new flightTimetableValidations({
  					attachId:"#flying-to",
  					dynaId: "flyingTo",
  					msg: "",
  					headerMsg: flightTimetableViewButton.errorMsg
  				});
  				var monthPullDownObj = new flightTimetableValidations({
  					attachId:"#whenCal",
  					dynaId: "days-cal-when",
  					msg: "",
  					headerMsg: flightTimetableViewButton.errorMsg
  				});
  				console.log(flyingFromObj);
  				if(flyingFromObj.validated === false || flyingToObj.validated === false || monthPullDownObj.validated === false){
  					return false;
  				}else{

  					flightTimetableViewButton.changeHdrLabels();
  				}*/


  			reverseAirportsLinkUtil.changeHdrLabels();

  		});
  	  },

  	  changeHdrLabels: function(){

  		var toAir, fromAir,fromAirport,toAirport,fromAirportName,toAirportName,mpD;
		 fromAir = query("#from-airport")[0];
		 toAir = query("#to-airport")[0];
		 fromAirportName = fromAir.textContent;
		 toAirportName = toAir.textContent;
 		 query(fromAir).text(toAirportName);
		 query(toAir).text(fromAirportName);

		 flyAiport = fromAirportName.split("(")[1].split(")")[0];
		 toAirport = toAirportName.split("(")[1].split(")")[0];


		 	var mpD = new monthPullDown();
			mpD.generateMonthPullDownData(flyAiport,toAirport);

  		  domStyle.set(query(".flight-results")[0], {
  			  display:"block"
  		  });

  		  query(".timeTableMonthSelector")[0].innerHTML = "";
  		  query(".spacer-center")[0].innerHTML = "";


  		// var fa = new flightActions();
		// fa.showSearchBox();

  		  /*call json here*/
  	  },

  	  callJson: function(){

  	  },

  	  render: function(){
  		  var reverseAirportsLinkUtil = this;
		  var html = reverseAirportsLinkUtil.renderTmpl(reverseAirportsLinkUtil.tmpl);
		  return html;
  	  }
  });

  	return tui.flights.utils.ReverseAirportsLinkUtil;
  });