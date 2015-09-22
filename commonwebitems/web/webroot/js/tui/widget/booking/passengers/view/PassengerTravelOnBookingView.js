define("tui/widget/booking/passengers/view/PassengerTravelOnBookingView", [
  "dojo/dom",
  "dojo/_base/xhr",
  "dojo/_base/declare",
  "dojo/query",
  "dojo/dom-attr",
  "dojo/dom-construct",
  "dojo/dom-class",
  "dojo/Evented",
  "dojo/topic",
  "dojo/_base/lang",
  "dojo/dom-style",
  "tui/widget/_TuiBaseWidget",
  "dojox/dtl/_Templated",
  "tui/widget/mixins/Templatable",
  "dojo/text!tui/widget/booking/passengers/view/templates/PassengerTravelOnBookingTmpl.html",
  "tui/widget/booking/passengers/view/PassengerTravelOnBookingOverlay",
  "tui/widget/booking/constants/BookflowUrl",
  "dojo/on",
  "dojo/_base/json",
  "dojo/date",
  "dojo/_base/array"
  ], function (dom,xhr,declare, query, domAttr, domConstruct, domClass, Evented, topic, lang, domStyle,
               _TuiBaseWidget, dtlTemplate, Templatable,PassengerTravelOnBookingTmpl,PassengerTravelOnBookingOverlay,BookflowUrl, on, jsonUtil,date, arrayUtils) {

	return declare('tui.widget.booking.passengers.view.PassengerTravelOnBookingView', [_TuiBaseWidget, dtlTemplate, Templatable, Evented], {

    tmpl: PassengerTravelOnBookingTmpl,
    templateString: "",
    widgetsInTemplate: true,


      buildRendering: function () {
    	if(this.jsonData.customerSignInViewData.signInAttempted ==  false){
    		if(this.jsonData.customerSignInViewData.customerLoggedIn){
    		  this.templateString = this.renderTmpl(this.tmpl, this);
    	        delete this._templateCache[this.templateString];
    	        this.inherited(arguments);
    		}
    	}    	      		
      },

      postCreate: function () {
    	  this.controller = dijit.registry.byId("controllerWidget");	
          this.attachEvents();
      },
      attachEvents:function()
      {
    	  if(this.notTravelling){
    	  on(this.notTravelling, 'click', lang.hitch(this, this.handleSubmitButton,"no"));
    	  }
    	  if(this.travelOnBooking){
		  on(this.travelOnBooking, 'click', lang.hitch(this, this.handleSubmitButton,"yes"));
    	  }
      },
      
      handleSubmitButton:function(travel){ 
    	  
    	  this.emailId=this.jsonData.customerSignInViewData.emailId;
    	  if(travel=="yes"){
    		  domClass.remove(this.clickedYes,'disNone');
    		  domClass.add(this.clickedNo,'disNone');
    		  this.loginComp=query(".loginComp")[0];
        	  domClass.add(this.loginComp,'disNone');
			  this.generateRequest("travellingPassengerSelection", BookflowUrl.travellingPassengerSelection,
                   {emailId:this.emailId});
    	  }
    	  else
    		  {
    		  this.generateRequest("nonTravellingPassengerSelection", BookflowUrl.nonTravellingPassengerSelection,
                      {emailId:this.emailId});
    		  domClass.add(this.clickedYes,'disNone');
    		  domClass.remove(this.clickedNo,'disNone');
    		  }
      },
      afterSucces:function(response){
    	  topic.publish("Adult.details", response);
    	  
    	  
      },
      generateRequest: function(field,value,contentValue)
      {
     	 var widget =this;
     	 console.log("ajax call");
     	 console.log(value);
     	dojo.addClass(dom.byId("main"), 'updating');
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
     		 console.log(field);
     		 console.log(response);
    		 dojo.removeClass(dom.byId("main"), 'updating');
     		widget.afterSucces(response);
     		

     	 });
      }

      
	});
});