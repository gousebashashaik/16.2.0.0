define('tui/widget/booking/FlightOptionController', [
  'dojo',
  'dojo/query',
  'dojo/dom-class',
  "dojo/_base/xhr",
  "dojo/dom",
  "dojo/ready",
  "dojo/dom-construct",
  "dojo/on",
  "tui/widget/_TuiBaseWidget"], function(dojo, query, domClass,xhr,dom,ready,domconstruct,on) {

	function doWhen(condition, f) {
        return condition ? f() : null;
    }
    ready(function(){

        dojo.removeClass(dom.byId("top"), 'updating');
        dojo.removeClass(dom.byId("main"), 'updating');
        dojo.removeClass(dom.byId("right"), 'updating');

        });


dojo.declare('tui.widget.booking.FlightOptionController', [tui.widget._TuiBaseWidget], {


	 views:[],


	 postCreate: function() {
		 on(window, 'pageshow', function(e) {
             if(e.persisted) {
               // do if page is cached
           	  window.location.href = "flightoptions"
             }
           });


	 },

 	 registerView: function (view) {
 		console.log("registring");
 		var widget = this;
 		widget.views.push(view);
 		console.log(widget.views);


     },

     publishToViews: function (field,response) {
    	 var widget = this;
         _.each(widget.views, function (view) {
        	 view.refresh(field, dojo.getObject(view.dataPath || '', false, response));
         });

     },

     generateRequest: function(field,value,contentValue)
     {
    	 var widget =this;
    	 dojo.addClass(dom.byId("top"), 'updating');
    	 dojo.addClass(dom.byId("main"), 'updating');
    	 dojo.addClass(dom.byId("right"), 'updating');
    	 console.log("ajax call");
    	 console.log(value);

    	 var results = xhr.post({
             url: value,
             content: contentValue,
             handleAs: "json",
             headers: {Accept: "application/javascript, application/json"},
             error: function (jxhr,err) {
                 if (dojoConfig.devDebug) {
                     console.error(jxhr);
                 }
                 console.log(err.xhr.responseText);
                 widget.afterFailure(err.xhr.responseText);

             }
         });


    	 dojo.when(results,function(response){

    		 console.log("i am here")
    		 console.log(field);
    		 console.log(response);
    		 widget.publishToViews(field,response);
    		 dojo.removeClass(dom.byId("top"), 'updating');
    		 dojo.removeClass(dom.byId("main"), 'updating');
    		 dojo.removeClass(dom.byId("right"), 'updating');
             if(response.extraFacilityViewDataContainer.seatOptions.extraFacilityViewData[0].code == "DEF_SEAT" && response.extraFacilityViewDataContainer.seatOptions.extraFacilityViewData[0].selected) {
                dojo.removeClass(dom.byId("standardSeat"), "displayNone");
            }
            else {
                dojo.addClass(dom.byId("standardSeat"), "displayNone");
            }

    	 });
     },
     afterFailure: function(html)
     {
    	 var extraOptionController = this;
    	 dojo.removeClass(dom.byId("top"), 'updating');
		 dojo.removeClass(dom.byId("main"), 'updating');
		 dojo.removeClass(dom.byId("right"), 'updating');
        console.log(html);
        console.log(document.body);
        domconstruct.place(html, document.body, "only");
        dojo.parser.parse(document.body);
     },
     refData: function(listOfData , indexValue){

    	 return _.find(listOfData, function(item,index){ if(index == indexValue) return item });

     },

     getAllData: function(listOfData , indexValue){

    	 return  _.filter(listOfData, function(item,index){ return index < indexValue ; })

     }

})

	return tui.widget.booking.FlightOptionController;
});