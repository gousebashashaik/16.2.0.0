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
  "tui/widget/_TuiBaseWidget"], function (dojo, reverseLinkTMPL, query, domConstruct, domStyle, on, keys, ready, domClass, domGeom, html, lang, flightTimetableValidations, flightActions, flightMonthBar, monthPullDown) {
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
  			var mpD = new monthPullDown();
  			mpD.generateMonthPullDownData();
  			
  			reverseAirportsLinkUtil.changeHdrLabels();
  			
  		});
  	  },
  	  
  	  changeHdrLabels: function(){
  		  
  		  /*var fromAir = query("#from-airport")[0];
  		  var toAir = query("#to-airport")[0];*/
   			
  		   var toAir;
		  var fromAir;
		  if(this.reverseAirportLink%2 == 0){
			   toAir = query("#from-airport")[0];
			   fromAir = query("#to-airport")[0];
		  }
		  else{
			 fromAir = query("#from-airport")[0];
	  		 toAir = query("#to-airport")[0];
		  }

		  this.reverseAirportLink++;
  		  
  		  query(fromAir).text(""); query(toAir).text("");	
  		  query(fromAir).text(query("#flying-from")[0].value);
  		  query(toAir).text(query("#flying-to")[0].value);
  		  
  		  domStyle.set(query(".flight-results")[0], {
  			  display:"block"
  		  });
  		  
  		  query(".monthSelector")[0].innerHTML = "";
  		  query(".spacer-center")[0].innerHTML = "";
  	
  		  
  		 var fa = new flightActions();
		 fa.showSearchBox();
  		  
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