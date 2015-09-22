define('tui/widget/booking/ExtrasOptionController', [
  'dojo',
  'dojo/query',
  'dojo/dom-class',
  "dojo/_base/xhr",
  "dojo/dom",
  "dojo/dom-construct",
  "dojo/_base/array",
  "dojo/on",
   "dojo/ready",
   "dojo/has",
   "tui/widget/_TuiBaseWidget"], function(dojo, query, domClass,xhr,dom,domconstruct,arrayUtil,on, ready, has) {

    ready(function(){

        dojo.removeClass(dom.byId("top"), 'updating');
        dojo.removeClass(dom.byId("main"), 'updating');
        dojo.removeClass(dom.byId("right"), 'updating');

    });

dojo.declare('tui.widget.booking.ExtrasOptionController', [tui.widget._TuiBaseWidget], {


	 views:[],


	controller:null,

	nextPageUrl: null,

    displayInsuranceUpSell: false,

	 postCreate: function() {
		 on(window, 'pageshow', function(e) {
             if(e.persisted) {
               // do if page is cached
           	  window.location.href = "extraoptions"
             }
           });
		 var widget=this,
       continueBooking = dojo.query(".wcf-center .ensLinkTrack");
         var insuranceChckBox = dojo.query(".msgBox .new-ins-arrangement");
         var familyInsurance = dom.byId("travel-ins");
        if(widget.displayInsuranceUpSell) {
          if(has("ie") == 8 || insuranceChckBox.length == 0 || !familyInsurance) {
            on(continueBooking, "click", function(){
              window.location.href= widget.nextPageUrl;
            });
          }
          else {
            on(continueBooking, "click", function(){
              if(familyInsurance){
                 dojo.publish("tui/booking/insurance/openInsurancePopUp");
              }
              else {
                window.location.href= widget.nextPageUrl;
              }
            });
          }
        }
        else {
          on(continueBooking, "click", function(){
            window.location.href= widget.nextPageUrl;
          });
        }
        console.log("hey");
	 },

 	 registerView: function (view) {
 		console.log("registring");
 		var widget = this;
    	 var index = arrayUtil.indexOf(widget.views,view);
    	 if (index !== -1) {
    	 widget.views[index] = view;
    	 } else {
 		widget.views.push(view);
    	 }
 		console.log(widget.views);
 		return widget;
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

     }





})

	return tui.widget.booking.ExtrasOptionController;
});