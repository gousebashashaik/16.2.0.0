 define("tui/widget/booking/passengers/view/PassengerChildAgeView", [
  "dojo/_base/declare",
  "dojo/query",
  "dojo/dom-attr",
  "dojo/dom-construct",
  "dojo/dom-class",
  "dojo/Evented",
  "dojo/topic",
  "dojo/_base/lang",
  "dojo/dom-style",
  "dojo/_base/xhr",
  "tui/widget/_TuiBaseWidget",
  "dojox/dtl/_Templated",
  "tui/widget/mixins/Templatable",
  "dojo/text!tui/widget/booking/passengers/view/templates/PassengerChildAgeTmpl.html",
  "tui/widget/booking/passengers/view/PassengerChildValidationAlertView",
  "tui/widget/booking/passengers/view/PassengerChildValidationAlertOverlay",
  "tui/widget/booking/constants/BookflowUrl",
  "dojo/on",
  "dojo/_base/json",
  "dojo/date",
  "dojo/_base/array",
  "dojo/dom"
  ], function (declare, query, domAttr, domConstruct, domClass, Evented, topic, lang, domStyle,xhr,
               _TuiBaseWidget, dtlTemplate, Templatable,PassengerChildAgeTmpl,PassengerChildValidationAlertView,
               PassengerChildValidationAlertOverlay,BookflowUrl, on, jsonUtil,date, arrayUtils,dom) {

	return declare('tui.widget.booking.passengers.view.PassengerChildAgeView', [_TuiBaseWidget, dtlTemplate, Templatable, Evented], {

    tmpl: PassengerChildAgeTmpl,
    templateString: "",
    widgetsInTemplate: true,


      buildRendering: function () {
        this.templateString = this.renderTmpl(this.tmpl, this);
        delete this._templateCache[this.templateString];
        this.inherited(arguments);
      },

      postCreate: function () {
    	  this.controller = dijit.registry.byId("controllerWidget");
    	  this.controller.registerView(this);
          this.attachEvents();
      },

      attachEvents: function () {
          on(this.childOkButton, "click", lang.hitch(this, this.handleAddButton));
      },

      handleAddButton: function () {

	      var widget =this;
	      var field = "paxpartycheck";
	      var value = BookflowUrl.passengerchildurl;
	      var contentValue = {id: this.childId, dob : this.dobTextBoxValue};
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
	    		 widget.redirectAccomPage(field,response);
	    		 dojo.removeClass(dom.byId("top"), 'updating');
	    		 dojo.removeClass(dom.byId("main"), 'updating');
	    		 dojo.removeClass(dom.byId("right"), 'updating');

	    	 });


      },
      redirectAccomPage: function(field, response){

    	  if(response.packageType == "FO"){
    		  this.accomUrl = response.summaryViewData.homePageURL;
    	  }


    	  else{
		for (var i =0; i< response.summaryViewData.summaryPanelComponentViewData.length; i++){

       		if(response.summaryViewData.summaryPanelComponentViewData[i].name == "HOTEL")
       			{
       			this.accomUrl = response.summaryViewData.summaryPanelComponentViewData[i].summaryPanelUrlsViewData.url+"&PassToAccom=true";
       			break;
       			}else if(response.summaryViewData.summaryPanelComponentViewData[i].name == "ITINERARY & SHIP"){
       				this.accomUrl = response.summaryViewData.summaryPanelComponentViewData[i].summaryPanelUrlsViewData.url;
       			}
          	}
    	  }


 		//var accomUrl=response.bookAccomPageUrl+"&PassToAccom=true";
 		 console.log(this.accomUrl);
 		  window.location.href=this.accomUrl;
      },
    refresh: function (field, response) {
	   if(field != "preferences" && field != "extraoptions"  ){
		   this.jsonData = response;
		    this.model =   this.jsonData.valStatusFlag;
		    if(this.jsonData.valStatusFlag == "0" ){
	        arrayUtils.forEach(this.jsonData.packageViewData.passenger,lang.hitch(this, function(item, index) {
	          if(item.identifier === this.childId) {
	            this.passengerChildAgeOverlay.refWidget.passengerObj = item;
	            this.passengerChildAgeOverlay.refWidget.passengerObj.age = item.age;
	            this.passengerChildAgeOverlay.refWidget.dobTextBox.age = item.age;
	            this.passengerChildAgeOverlay.refWidget.dobTextBox.validate();
				this.passengerChildAgeOverlay.refWidget.dobTextBox.focus();
	            this.passengerChildAgeOverlay.refWidget.age.value = item.age;
	            this.infantNotYetCheckBOX(this.passengerChildAgeOverlay.refWidget.passengerObj.age);
	            domAttr.set(query(".childAge", this.passengerChildAgeOverlay.refWidget.domNode.parentNode)[0],
	                  "innerHTML", "(Age "+item.age+")");
	            domAttr.set(query(".passenger-name", this.passengerChildAgeOverlay.refWidget.domNode.parentNode)[0],
	                    "innerHTML", item.passengerLabel);
            //this.passengerChildAgeOverlay.refWidget = this;
	            this.passengerChildAgeOverlay.close();

	          }
	        }));
		    } else {
		      	this.passengerChildValidationAlertView = new PassengerChildValidationAlertView({
		            "id": "ChildAge_Alert",
		            "jsonData":  this.jsonData
		         });
		      	domConstruct.place(this.passengerChildValidationAlertView.domNode, this.passengerChildAgeOverlay.refWidget.domNode, "first");
	            this.passengerChildValidationAlertOverlay = new PassengerChildValidationAlertOverlay({widgetId: this.passengerChildValidationAlertView.id, modal: true, opacity: 0.4});
	            this.passengerChildValidationAlertOverlay.refWidget = this;
	            this.passengerChildAgeOverlay.close();
	            this.passengerChildValidationAlertOverlay.open();
	      }
	   }

    },

    infantNotYetCheckBOX: function(childAge){
    	if(childAge == 0){
    		domStyle.set(this.infantNotYet, "display", "block");
    		 on(this.passengerChildAgeOverlay.refWidget.infantNotBornCheckBox, "click", lang.hitch(this.passengerChildAgeOverlay.refWidget, this.passengerChildAgeOverlay.refWidget.handleInfantNotBornCheckbox));
    	}else{
    		domStyle.set(this.infantNotYet, "display", "none");
    	}
    }



	});
});