define("tui/widget/booking/passengers/view/ForgotPasswordView", [
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
  "dojo/text!tui/widget/booking/passengers/view/templates/ForgotPasswordTmpl.html",
  "dojo/on",
  "dojo/_base/json",
  "dojo/date",
  "dojo/_base/array",
  "tui/widget/booking/constants/BookflowUrl"
  ], function (dom,xhr,declare, query, domAttr, domConstruct, domClass, Evented, topic, lang, domStyle,
               _TuiBaseWidget, dtlTemplate, Templatable, ForgetPasswordTmpl, on, jsonUtil,date, arrayUtils,BookflowUrl) {

	return declare('tui.widget.booking.passengers.view.ForgotPasswordView', [_TuiBaseWidget, dtlTemplate, Templatable, Evented], {

    tmpl: ForgetPasswordTmpl,
    templateString: "",
    widgetsInTemplate: true,
    submittedEmail:false,
    success_msg:"sazgdsdhj",
    alertmsg:"",


      buildRendering: function () {
        this.templateString = this.renderTmpl(this.tmpl, this);
        delete this._templateCache[this.templateString];
        this.inherited(arguments);
      },

      postCreate: function () {
    	  var widget=this.domNode;
    	  this.controller = dijit.registry.byId("controllerWidget");
    	 // this.controller.registerView(this);
    	  this.attachEvents();
    	  this.forgotPass=query(".forgotPassword",this.domNode);
    	  this.sumbitEmail=query(".submitEmail",this.domNode);
      },
      
      attachEvents:function()
      {
    	 /* _.each(this.forgotPass, lang.hitch(this, function (forgotPass) {
	          on(forgotPass, 'click', lang.hitch(this, this.handleSubmitButton,forgotPass));
	          this.handleSubmitButton(forgotPass);
	        })); 
    	  on(this.forgotPass, 'click', lang.hitch(this,this.forgotPass,forgotPass));*/
    	  
    	  on(this.submitDetails, 'click', lang.hitch(this,this.handleSubmitButton));
      },
      
      handleSubmitButton:function(sumbitEmail){
    	  this.emailIdValue=this.passEmail.get("value");
    	  if(this.emailIdValue !== ""){
		  this.generateRequest("forgetRequest", BookflowUrl.forgotPassword,{emailId: this.emailIdValue});	                 
    	  }
    	  else{
    		  this.passEmail.validate();
              this.passEmail.displayMessage(this.passEmail.missingMessage);              
    	  }
      },
      afterSuccess:function(successStringVal){
    	  domStyle.set(this.successMsgBlock, "display", "block");
    	  if(successStringVal == ""){
              domStyle.set(this.headerRemove,"display", "none");
              domStyle.set(this.buttonRemove,"display", "none");
              getLCRRemoveText = query(".mail")[0]
   			this.fadeOutMessage(getLCRRemoveText);
              
           
    	  }else{
    		  domStyle.set(this.forgotPasswordBlock, "display", "none");
             this.passEmail.innerHTML = this.emailIdValue; 
             domStyle.set(this.headerRemove,"display", "block");
             domStyle.set(this.buttonRemove,"display", "block");
    	  }
         /*
          domAttr.set(this.successMsgDiv, "innerHTML",success_msg);*/
      },
      
  	fadeOutMessage : function(getLCRRemoveText){
		domStyle.set(getLCRRemoveText, "display", "block");
		var fadeArgs = {
                node: getLCRRemoveText,
                duration: BookflowUrl.fadeOutDuration,
                onEnd: function () {
                	getLCRRemoveText.style.display = "none";
                }
              };
              dojo.fadeOut(fadeArgs).play();
		
	},
      refresh: function (field, response) {
			this.jsonData = response;
			this.renderWidget();
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
      		dojo.removeClass(dom.byId("main"), 'updating');
     		widget.jsonData = response;
     		widget.messageText.innerHTML =  response.customerSignInViewData.alert.messageText;
     		var successString = response.customerSignInViewData.alert.messageHeader;
     		 widget.afterSuccess(successString); 
     	 });
      }

  
	});
});