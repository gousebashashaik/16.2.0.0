define('tui/widget/customeraccount/mybookings/DateService', [
  'dojo',
  "dojo/dom-attr",
  'dojo/dom-class'
], function(criteria, domAttr, domClass) {

  function DateService() {
  }

  DateService.prototype.availableDates = function(airports, destinations) {
        /* taking selected value of the airports(where from) -start */
        var whereFrom = "";
        if (airports.length != 0) {
    	airports = JSON.parse(JSON.stringify(airports));
    	var myAirports = [];
    	var i = 0;
    	airports.forEach(function(object) {
    		myAirports[i] = object.id;
		 		i++;
    	});
       	whereFrom = myAirports.join('|');
        }
        /* taking selected value of the airports(where from) -end */
        /* taking selected value of the destinations(where to) -start */
	   var whereTo = "";
       if (destinations.length != 0) {
    	   destinations = JSON.parse(JSON.stringify(destinations));
         var myDestination =[];
    	 var j = 0;
    	 destinations.forEach(function(object) {
    		myDestination[j] = object.id +":" +object.name;
		 		j++;
    	  });
    	 whereTo = myDestination.join('|');
       }
       var finder = dojo.byId('finder').value;
       if(finder == "checkprice")
		{  
   	   /*fetching value of selected accommodation name(where to) -start */
          var title = dojo.query('.row.check-price-title h3')[0];
          if(title !=null)
       	   {
       	   myDestination = domAttr.get(title, "data-whereto");
       	    }
		}	    
       /* taking selected value of the destinations(where to) -end */
 // make an ajax request with the selected airports & destinations and get the dates
    var xhrArgs = {
      url: '/holiday/**/ws/traveldates',
      content: {
        'from[]': myAirports,
        'to[]': myDestination
      },
      sync: true,
      handleAs: 'json',
      timeout: 0
    };
    var datesFormat = [];

    var deferred = dojo.xhrGet(xhrArgs);
    var dates = null;
    deferred.then(function(response) {
    dates = deferred.results[0];
    });
    if(dates.length == 0) {
   		var error_header ="";
		var error_from = "";
		var error_to = "";
		var error_temp = dojo.query('.row.error-msg')[0];
		/* Conditional statement added to find weather  get-price or holiday-finder loaded */
		var finder = dojo.byId('finder').value;
		if(finder == "holidayfinder")
			{				 
			dojo.byId("holidaySearch").disabled= true;
			 dojo.byId("adults").disabled= true;
			 dojo.byId("children").disabled= true;		
				 error_header = dojo.byId('eHeader');
				 error_from = dojo.byId('clearFrom');
				 error_to = dojo.byId('clearTo');
			}
		else if(finder == "checkprice")
			{
			 dojo.byId("priceSearch").disabled= true;
			 dojo.byId("priceadults").disabled= true;
			 dojo.byId("pricechildren").disabled= true;
			 	error_temp = dojo.query('#errorGetprice')[0];
				error_header = dojo.byId('eHeaderGetprice');
				 //error_from = dojo.byId('clearFromGetprice');
				 error_to = dojo.byId('clearToGetprice');
			}
		var error = dojo.query('.row.error-msg')[0];
		error_header.innerHTML = 'No available dates for selected destinations and airports';
        domClass.add(error, 'show');
	  }
    else {
        return _.map(dates, function(date) {
    		var dateValues = date.split('-');    
           return JCalendar.fromDate(new Date(dateValues[2], _.dec(dateValues[1]), dateValues[0]));
    });
    }

  };

  return new DateService();
});
