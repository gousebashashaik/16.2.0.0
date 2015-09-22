define('tui/widget/booking/PassengersOptionController', [
  'dojo',
  'dojo/query',
  'dojo/dom-class',
  "dojo/_base/xhr",
  "dojo/dom",
  "dojo/ready",
  "dojo/on",
  "tui/widget/_TuiBaseWidget"], function(dojo, query, domClass,xhr,dom,ready,on) {

	function doWhen(condition, f) {
        return condition ? f() : null;
    }
    ready(function(){

        dojo.removeClass(dom.byId("top"), 'updating');
        dojo.removeClass(dom.byId("main"), 'updating');
        dojo.removeClass(dom.byId("right"), 'updating');

    });

dojo.declare('tui.widget.booking.PassengersOptionController', [tui.widget._TuiBaseWidget], {


	 views:[],




	 postCreate: function() {
		 on(window, 'pageshow', function(e) {
             if(e.persisted) {
               // do if page is cached
           	  window.location.href = "passengerdetails"
             }
           });
		 var widget=this;
		 console.log("hey");
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
             error: function (err) {
                 if (dojoConfig.devDebug) {
                     console.error(err);
                 }

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

    	 });
     },
     refData: function(listOfData , indexValue){

    	 return _.find(listOfData, function(item,index){ if(index == indexValue) return item });

     }



})

	return tui.widget.booking.PassengersOptionController;
});