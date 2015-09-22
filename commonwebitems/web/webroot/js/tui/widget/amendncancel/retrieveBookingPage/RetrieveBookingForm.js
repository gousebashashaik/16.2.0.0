define('tui/widget/amendncancel/retrieveBookingPage/RetrieveBookingForm', [
  "dojo/_base/declare",
  "dojo/parser",
  "dojo/on",
  "dojo/dom",
  "dojo/dom-class",
  "dojo/query",
  "dojo/dom-construct",
  "tui/widget/_TuiBaseWidget",
  "dojox/dtl/_Templated",
  "tui/widget/mixins/Templatable",
  "dojo/text!tui/widget/amendncancel/retrieveBookingPage/templates/RetrieveBookingForm.html",
  "tui/widget/mixins/MethodSubscribable",
  "dojo/dom-style",
  "dojo/date",
  "tui/widget/amendncancel/AmendDatePicker",
  "tui/widget/common/ClickToolTip",
  "tui/widget/amendncancel/refnoPopup",
  "tui/widget/amendncancel/retrieveBookingPage/errorTooltip",
  "tui/widget/amendncancel/retrieveBookingPage/splitter/Splitter",
  "tui/widget/form/ValidationTextBox"], function(declare, parser, on, dom, domClass, query, domConstruct, _TuiBaseWidget, _Templated,
		Templatable, formTemplate, MethodSubscribable,domStyle, domDate) {

	return declare('tui.widget.amendncancel.retrieveBookingPage.RetrieveBookingForm', [_TuiBaseWidget, _Templated,
		Templatable], {

	   //Templated Mixin attributes
	   templateString: formTemplate,

	   //To get widget instances in the template
	   widgetsInTemplate:true,

	   //MethodSubscribable attribute
	   subscribableMethods:["checkDate","showHideValidationError","showHideDateValidationError","addOnFocusEvent", "showHideBookingNotFoundError","addOnKeyPressEvent"],

	   hybrisUrl: "displaybooking",
	   iscapeDeeplinkUrl: null,

	   iscapeSwitchdate: "",

	   //iscapeSwitchdate in Date format
	   switchDate: null,

	   jsonData: null,
	   //Initialized with empty to handle redirection from Mails (Query Params)
	   bookingSearchCriteria: { departureDate: '', bookingRefereneceId: '' , secPassengerName: '' },
	   staticDataMap: null,

	   //Query Params values
	   departureDate: "",
	   bookingRefereneceId: "",
	   sec_passengerName:"",

	   splitterInstance: null,
	   datePickerInstance: null,
	   errTooltipInstance: null,

	   postMixInProperties: function(){
		   widget = this;

		   if( widget.jsonData.bookingSearchCriteria != null)
		   {
			   widget.bookingSearchCriteria  = widget.jsonData.bookingSearchCriteria;
		   }
		   widget.staticDataMap =	widget.jsonData.manageHomePageContentViewData.manageHomePageContentMap;

		   // Pre-population from Query Params
		   if(widget.bookingRefereneceId != "" || widget.sec_passengerName != "")
		   {
			   widget.bookingSearchCriteria.bookingRefereneceId = widget.bookingRefereneceId;
			   widget.bookingSearchCriteria.secPassengerName = widget.sec_passengerName;
		   }
      },

	   postCreate:function(){
			this.inherited(arguments);
			widget = this;

			widget.resetOnbackButtonPress();
			widget.registerWidgetInstances();
		
			widget.errTooltipInstance.registerForm(widget);
			

			// Pre-population from Query Params
		    if(widget.departureDate != "")
		    {
			    var date = widget.departureDate.split('-');
			    widget.bookingSearchCriteria.departureDate = new Date(date[2], (date[1] - 1), date[0]);
			    widget.prePopulateDate();
		    }

			if(widget.jsonData != null && widget.jsonData.error != null && widget.jsonData.error.error){
				widget.showHideBookingNotFoundError(false);
				widget.prePopulateDate();
			}

			//store in date format
			var date = widget.iscapeSwitchdate.split('-');
			var actualdate = new Date(date[2], date[1], date[0]);
			widget.switchDate = domDate.add(actualdate, "month", -1);


			var form = dom.byId("login-form");

			//set form action to https url given in jsondata
			if(dom.byId("login-form") != undefined){

				if(widget.jsonData.pageResponse){
					if(widget.jsonData.pageResponse.targetPageUrl){
						widget.hybrisUrl = widget.jsonData.pageResponse.targetPageUrl;
					}
				}
				form.action = widget.hybrisUrl;
			}
			on(form, "submit", function(evt){
			   evt.preventDefault();
			   if(widget.validate(true)){
					form.submit();
			   }
			   else{
					widget.errTooltipInstance.showHidePopUp(false);
					widget.showHideValidationError(false);
			   }
			});

			widget.addOnFocusEvent();
			widget.addOnKeyPressEvent();
	   },
	   //fix given for back button press on ipad
       resetOnbackButtonPress: function(){
			window.onpageshow = function(event) {
				if (event.persisted) {
					window.location.reload()
				}
			};
	   },
		//To add events whenever Splitter changes its template
	   addOnFocusEvent:function(){
			on(query('.textfield, .dijitInputInner'), "focus" ,function(evt){
				widget.showHideValidationError(true);
				widget.showHideBookingNotFoundError(true);
			});
	   },

		addOnKeyPressEvent:function() {
			query('.dijitInputInner').forEach(function(node){
				on(node, "keypress" ,function(evt){
					var placeholder = query(evt.currentTarget).siblings(".dijitPlaceHolder");
					if(!domStyle.get(placeholder,"display")) {
						placeholder.style("display", "none");
					}
				});
			});
	   },
	   //Get all the Widget instances in the template
	   registerWidgetInstances:function(){
			widget.datePickerInstance = widget[widget._attachPoints[0]];
			widget.splitterInstance = widget[widget._attachPoints[1]];
			widget.errTooltipInstance = widget[widget._attachPoints[3]];
	   },

	   showHideValidationError:function(isValid){
			var errorDom = query('.error-message-section.miss-text')[0];
			if(isValid){
				if(!domClass.contains(errorDom, "hide")){
					domClass.add(errorDom, "hide");
				}
			}else{
				if(domClass.contains(errorDom, "hide")){
					domClass.remove( errorDom, "hide");
//					widget.showHideBookingNotFoundError(true);
				}
			}
	   },

	   showHideBookingNotFoundError:function(isValid){
			var errorDom = query('.error-message-section.result-not-found')[0];
			if(isValid){
				if(!domClass.contains(errorDom, "hide")){
					domClass.add(errorDom, "hide");
				}
			}else{
				if(domClass.contains(errorDom, "hide")){
					domClass.remove( errorDom, "hide");
				}
			}
	   },

	   showHideDateValidationError:function(isValid){
		   var validateIcon = query('.date-picker > .validate-icon')[0];
		   var errorMsg = query('.date-picker > .err-txt')[0];
			if(isValid){
				if(domClass.contains(validateIcon, "fail")){
					domClass.remove(validateIcon , "fail" );
				}
				if(!domClass.contains(validateIcon, "success")){
					domClass.add(validateIcon , "success" );
				}
				if(domClass.contains(errorMsg, "show")){
					domClass.remove( errorMsg, "show" );
				}
			}else{
				if(!domClass.contains(validateIcon, "fail")){
					domClass.add(validateIcon , "fail" );
				}
				if(!domClass.contains(errorMsg, "show")){
					domClass.add( errorMsg, "show" );
				}
			}
	   },

	   renderWidget: function () {
    	var widget = this;
    	var html = widget.renderTmpl(widget.templateString, widget);
		domConstruct.place(html, this.domNode, "only");
		parser.parse(this);
	   },

	   prePopulateDate:function(){
			widget = this;
			var tmp = new Date(widget.bookingSearchCriteria.departureDate);
			widget.datePickerInstance.clearDateField(tmp);
	   },

	   checkDate:function(){
			var selectedDate = new Date(dom.byId("date").value);
			var retrieveForm = dom.byId("login-form");
			var channel = "";
			if(selectedDate < widget.switchDate){
				//Re-order the form if its a TRACS booking
				retrieveForm.action = this.iscapeDeeplinkUrl;
				channel = "iscape";
				dom.byId('sec_departureDay').value = selectedDate.getDate();
				dom.byId('sec_departureMonth').value = (selectedDate.getMonth() + 1) + " " + selectedDate.getFullYear();
			}
			else{
				if(this.jsonData.pageResponse){
					if(this.jsonData.pageResponse.targetPageUrl){
						this.hybrisUrl = this.jsonData.pageResponse.targetPageUrl;
					}
				}
				retrieveForm.action = this.hybrisUrl;
				channel = "hybris";
			}
			widget.splitterInstance.splitBookingReference(channel);
	   },

	   // showErrorMsgs is used to render/not-render validation msgs
	   validate:function(showErrorMsgs){
			valid = true;
			var validatableWidgets = new Array();
			_.each(widget[widget._attachPoints[1]].widgetAttachPoints , function(object){
				validatableWidgets.push(object);
			});
			validatableWidgets.push(widget[widget._attachPoints[2]]);

			//Individually validate validation text boxes
			for (var index = 0; index < validatableWidgets.length; index++) {
				if(validatableWidgets[index].fieldIdentifier == "splitted") {
					valid = validatableWidgets[index].validate();
				}
				else {
					if (validatableWidgets[index].state && validatableWidgets[index].required) {
						validatableWidgets[index]._hasBeenBlurred = showErrorMsgs;
						valid = validatableWidgets[index].validate(false);
					}
				}
		     }

			 var selectedDate = dom.byId("date").value;
			 if( showErrorMsgs && ( selectedDate === "" || selectedDate === "Select a date" ) ){
				widget.showHideDateValidationError(false);
				valid = false;
			 }
		     return valid;
	   }

	});
	return tui.widget.amendncancel.retrieveBookingPage.RetrieveBookingForm;
});