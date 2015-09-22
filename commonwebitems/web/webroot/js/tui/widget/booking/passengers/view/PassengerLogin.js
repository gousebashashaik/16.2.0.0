define("tui/widget/booking/passengers/view/PassengerLogin", [
"dojo/dom",
"dojo/_base/xhr",
  "dojo/_base/declare",
  "dojo/text!tui/widget/booking/passengers/view/templates/PassengerLoginTmpl.html",
  "tui/widget/_TuiBaseWidget",
  "dojox/dtl/_Templated",
  "tui/widget/mixins/Templatable",
  "dojo/query",
  "dojo/dom-attr",
  "dojo/dom-style",
  "dojo/dom-construct",
  "dojo/dom-class",
  "dojo/Evented",
  "dojo/topic",
  "dojo/_base/lang",
  "dojo/dom-style",
  "dojo/on",
  "tui/widget/booking/passengers/view/ForgotPasswordOverlay",
  "tui/widget/booking/passengers/view/ForgotPasswordView",
  "tui/widget/booking/passengers/view/PassengerTravelOnBookingView",
  "tui/widget/booking/passengers/view/PassengerTravelOnBookingOverlay",
  "tui/widget/booking/constants/BookflowUrl"
], 
  function(dom,xhr,declare,PassengerLoginTmpl,_TuiBaseWidget, dtlTemplate,Templatable,query,domAttr,domStyle,domConstruct,domClass,event,topic,lang,style,on,ForgotPasswordOverlay,ForgotPasswordView,PassengerTravelOnBookingView,PassengerTravelOnBookingViewOverlay,BookflowUrl) {
 return declare("tui.widget.booking.passengers.view.PassengerLogin", [_TuiBaseWidget, dtlTemplate, Templatable], {
 	 tmpl: PassengerLoginTmpl, 
		templateString: "",
		widgetsInTemplate : true,
		 buildRendering: function () {
			 if(this.jsonData.customerSignInViewData.signInAttempted ==  false){
			    this.templateString = this.renderTmpl(this.tmpl, this);
		        delete this._templateCache[this.templateString];
		        this.inherited(arguments);
			 }
			
		      },		      
		 postCreate: function () {			 
		    	  this.controller = dijit.registry.byId("controllerWidget");
		    	 // this.controller.registerView(this);
		    	  this.radioButtons = query('input[type=radio]', this.domNode);
		    	  this.attachEvents();
		    	  if(this.jsonData.customerSignInViewData.signInAttempted ==  false){
		    	 if(this.jsonData.customerSignInViewData.customerLoggedIn == true){
		    	 this.onLoadFunction();
		    	 }		    	
		    	  }
		      },		      
		  attachEvents : function(){
			  _.each(this.radioButtons, lang.hitch(this, function (radioButton) {
		          on(radioButton, 'click', lang.hitch(this, this.handleRadioButtons,radioButton));
		          this.handleRadioButtons(radioButton);
		        })); 		  
			  var findbutton=query('.button',this.domNode);
			  if(findbutton){
			  on(query('.button',this.domNode), 'click', lang.hitch(this, this.handleDetailsButton));
			  }
			  if(this.forgotpwd){
			  on(this.forgotpwd, 'click', lang.hitch(this, this.forgotOverlay,true));
			  }
		  },		  
		  handleDetailsButton: function (){
			 this.emailId=this.passEmail.get("value");
			 this.paswordValue=this.passPassword.get("value");
			 //this.enterDetails=this.
			 
			 if(this.emailId == "" || this.paswordValue == ""){
				 domStyle.set(this.enterDetails, "display", "block");				 
			 }else{
				 domStyle.set(this.enterDetails, "display", "none");
				 dojo.addClass(dom.byId("main"), 'updating');
			  this.generateRequest("logInRequest", BookflowUrl.logInRequest,
	                {emailId:this.emailId,
					 password:this.paswordValue});
				  
			 }			 
		  },
		      handleRadioButtons: function (radioButton){
		    	  this.getDetails = query('.loginForm',this.domNode)[0];
		    	     if (radioButton.checked && radioButton.id == "signup") {
		               	   domClass.remove(this.getDetails,'disNone');
		         		  this.passPassword.set("value", '');
		         		  this.passEmail.set("value", '');
		         		 _.each(query(".error-notation"), lang.hitch(this, function (errorNotation){
			     	   			domStyle.set(errorNotation, "display", "none");
			     	   		}));
			     	   		_.each(query(".dijitValidationInner"), lang.hitch(this, function (errorNotation){
			     	   			domStyle.set(errorNotation, "display", "none");
			     	   		}));
		       
		         		 
		               }
		              else {
		            	  domClass.add(this.getDetails,'disNone');
			              }
		    	  },		    	 
		    	  forgotOverlay: function (flag){
		    		  domStyle.set(this.enterDetails, "display", "none");
		    		  this.passPassword.set("value", null);
		    		  if (this.forgotPasswordView && this.forgotPasswordView != null) {
		    	           this.forgotPasswordView.destroyRecursive();
		    	           this.forgotPasswordView = null;
		    	           this.forgotPasswordOverlay.destroyRecursive();
		    	           this.forgotPasswordOverlay = null;
		    	         }
		    		  
		    	         this.forgotPasswordView = new ForgotPasswordView({
		    	             "jsonData":this.jsonData,
		    	             "id": "forgot-overlay",
		    	             "flag":flag
		    	         });
		    	         domConstruct.place(this.forgotPasswordView.domNode, this.domNode, "last");
		    	         this.forgotPasswordOverlay = new ForgotPasswordOverlay({widgetId: this.forgotPasswordView.id, modal: true});
		    	         this.forgotPasswordView.forgotPasswordOverlay = this.forgotPasswordOverlay;
		    	         this.forgotPasswordOverlay.open();		    		  
		    	  },
		    	  onLoadFunction: function (){
		    		 //this.jsonData.customerSignInViewData.customerLoggedIn = false;		    	    	 
		    		  if(this.jsonData.customerSignInViewData.customerLoggedIn){
		    		  if (this.passengerTravelOnBookingView && this.passengerTravelOnBookingView != null) {
		    	           this.passengerTravelOnBookingView.destroyRecursive();
		    	           this.passengerTravelOnBookingView = null;
		    	           this.passengerTravelOnBookingViewOverlay.destroyRecursive();
		    	           this.passengerTravelOnBookingViewOverlay = null;
		    	         }
		    	         this.passengerTravelOnBookingView = new PassengerTravelOnBookingView({
		    	             "jsonData":this.jsonData,
		    	             "id": "travelonbooking-overlay"
				    	         });
		    	         domConstruct.place(this.passengerTravelOnBookingView.domNode, this.domNode, "last");
		    	         this.passengerTravelOnBookingViewOverlay = new PassengerTravelOnBookingViewOverlay({widgetId: this.passengerTravelOnBookingView.id, modal: true});
		    	         this.passengerTravelOnBookingView.passengerTravelOnBookingViewOverlay = this.passengerTravelOnBookingViewOverlay;
		    	         this.passengerTravelOnBookingViewOverlay.open();
		    		  }else{
		    			 this.enterDetails.innerHTML =  this.jsonData.customerSignInViewData.alert.messageText;
		    			 domStyle.set(this.enterDetails, "display", "block");


		    		  }
		    	  },
		    	  generateRequest: function(field,value,contentValue)
		          {
		         	 var widget =this;
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
		         		 dojo.removeClass(dom.byId("main"), 'updating');
		         		 widget.jsonData = response;
		         		widget.onLoadFunction();
		         	 });
		          }	      
 	});
});