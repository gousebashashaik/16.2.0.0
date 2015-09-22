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
  "tui/flights/utils/FlightTimeTableValidations",
  "tui/flights/view/FlightActions",
  'tui/flights/view/FlightsMonthBar',
  "tui/widget/_TuiBaseWidget"], function (dojo, flightViewButtonTMPL, query, domConstruct, domStyle, on, keys, ready, domClass, domGeom, html, lang, flightTimetableValidations, flightActions, flightMonthBar) {
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
  				if(flyingFromObj.validated === false || flyingToObj.validated === false || monthPullDownObj.validated === false){
  					return false;
  				}else{
  					
  					flightTimetableViewButton.changeHdrLabels();
  				}
  		});
  	  },
  
	  
  	  changeHdrLabels: function(){
  		  var fromAir = query("#from-airport")[0];
  		  var toAir = query("#to-airport")[0];
   		  query(fromAir).text(""); query(toAir).text("");	
  		  query(fromAir).text(query("#flying-from")[0].value);
  		  query(toAir).text(query("#flying-to")[0].value);
  		  
  		  domStyle.set(query(".flight-results")[0], {
  			  display:"block"
  		  });
  		  
		  domStyle.set(query(".footer-flight-details")[0], {
  			  display:"block"
  		  });  		  
		  
		  query(".monthSelector")[0].innerHTML = "";
		  query(".spacer-center")[0].innerHTML = "";
		  
  		  var fb = new flightMonthBar();
  		  fb.generateMonths();
  		  
  		  //..flight timetable search box..
  		  fa = new flightActions();
  		  fa.showSearchBox();
  		  
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