define("tui/widget/booking/passengers/view/PassengerDetailsView", [
  "dojo/_base/declare",
  "dojo/dom",
  "dojo/query",
  "dojo/dom-attr",
  "dojo/dom-construct",
  "dojo/dom-class",
  "dojo/Evented",
  "dojo/topic",
  "dojo/_base/lang",
  "dojo/_base/array",
  "dojo/dom-style",
  "dojo/keys",
  "dojox/mvc/_DataBindingMixin",
  "tui/widget/_TuiBaseWidget",
  "dojox/dtl/_Templated",
  "tui/widget/mixins/Templatable",
  "dojo/html",
  "dojo/text!tui/widget/booking/passengers/view/templates/PassengerDetailView.html",
  "tui/widget/booking/passengers/validator/PassengerDetailValidator",
  "tui/widget/booking/passengers/view/PassengerDpnOverlay",
  "tui/widget/booking/passengers/view/PassengerDpnView",
  "tui/widget/booking/passengers/view/PassengerInsAlertOverlay",
  "tui/widget/booking/passengers/view/PassengerInsAlert",
  "tui/widget/booking/passengers/view/PassengerChildAgeView",
  "tui/widget/booking/passengers/view/PassengerChildAgeOverlay",
  "tui/widget/booking/passengers/PassengerUtils",
  "dojo/on",
  "dojo/has",
  "dojox/mvc",
  "tui/widget/booking/constants/BookflowUrl",
  "tui/widget/form/ValidationTextBox",
  "dojox/validate",
  "tui/widget/popup/Tooltips",
  "tui/widget/form/SelectOption",
  "dijit/form/CheckBox",
  "dijit/form/TextBox",
  "dojox/mvc/StatefulModel",
  "dojox/mvc/Output",
   "dijit/focus"
], function (declare, dom, query, domAttr, domConstruct, domClass, Evented, topic,  lang, arrayUtils, domStyle,keys,
             _DataBindingMixin, _TuiBaseWidget, dtlTemplate, Templatable, htmlUtils, PassengerDetailView,
             PassengerDetailValidator, PassengerDpnOverlay, PassengerDpnView, PassengerInsAlertOverlay,
             PassengerInsAlert,PassengerChildAgeView,PassengerChildAgeOverlay, PassengerUtils, on,has, mvc,BookflowUrl,ValidationTextBox,focusUtil) {

  return declare('tui.widget.booking.passengers.view.PassengerDetailsView',
          [_TuiBaseWidget, dtlTemplate, Templatable, _DataBindingMixin, Evented], {
    tmpl: PassengerDetailView,
    templateString: "",
    widgetsInTemplate: true,
    validator: null,
    passengerModel: null,
    insDOBCounter : 0,
    controller : null,
    surnCheckBox: [],
  infantNotYetFlag:false,
    leadAdultBox:[],
    i:0,
    res:[],
    falconFlag:false,
    flightsFlag:false,


    postMixInProperties: function () {
      passengerDetailModel = mvc.newStatefulModel({
        data: {
          title: this.paxInfoFormBean.title,
          gender: this.paxInfoFormBean.gender,
          firstName: this.paxInfoFormBean.firstName,
          lastName: this.paxInfoFormBean.lastName,
          leadPassenger: this.passengerDetailsFormBean.leadPassenger,
          personType: this.paxInfoFormBean.personType,
          age: this.paxInfoFormBean.age,
          day: this.paxInfoFormBean.day,
          month: this.paxInfoFormBean.month,
          year: this.paxInfoFormBean.year,
          dob: null,
          checkBox:this.paxInfoFormBean.checkBox,
          address1: this.passengerDetailsFormBean.address1,
          address2: this.passengerDetailsFormBean.address2,
          houseNum: this.passengerDetailsFormBean.houseNum,
          town: this.passengerDetailsFormBean.town,
          county: this.passengerDetailsFormBean.county,
          country:this.passengerDetailsFormBean.country,
          postCode: this.passengerDetailsFormBean.postCode,
          telephoneNum: this.passengerDetailsFormBean.telephoneNum,
          email: this.passengerDetailsFormBean.email,
          confirmationEmail: this.passengerDetailsFormBean.confirmationEmail,
          roomAllocated: false,
          insuranceSelected : false
        }
      });
      this.passengerModel = passengerDetailModel;
      if(dojoConfig.site=="falcon"){
    	  this.falconFlag=true;
      }
      if(dojoConfig.site== "flights"){
    	  this.flightsFlag=true;
      }

    },

    buildRendering: function () {
      this.templateString = this.renderTmpl(this.tmpl, this);
      this.inherited(arguments);
    },

    postCreate: function () {
    	if ((this.index === 0 && this.falconFlag) || (this.index === 0 && this.flightsFlag)) {
         this.handleCountrySelection(false);
        }

      var passengerDetails = this;
     this.controller = dijit.registry.byId("controllerWidget");
     // this.controller.registerView(this);
      this.validator = new PassengerDetailValidator();
      if(dojoConfig.site=="falcon"){
    	  var countryNode=query(".countrySelect",this.domNode)[0];
    	  console.log(countryNode);
    	  //domClass.remove(countryNode,'disNone');
    	  if(countryNode){
    	  domStyle.set(countryNode,"display","block");
    	  }
      }
      this.attachEvents();

      if (this.paxInfoFormBean != "" ) {
       if( !this.paxInfoFormBean.lastName && this.index !== 0) {
         if(this.surnameCheckbox.domNode) {
          this.surnameCheckbox.set("checked", "true");
        }
       }
      }
      if(this.passengerModel.get("personType")) {
        this.passengerModel.set("personType", this.passengerObj.type);
      }
      if(this.insDObBlock){
        domStyle.set(this.insDObBlock, "display", "none");
      }

                if (this.titleSelect.getSelectedData().value !== "DR" &&
                    this.titleSelect.getSelectedData().value !== "PROF" &&
                    this.titleSelect.getSelectedData().value !== "REV") {
      domStyle.set(this.genderSelect.domNode, "display", "none");
                }


      if (this.emailConfirmationTextBox) {
        this.emailConfirmationTextBox.validator = this.validator.checkConfirmationEmail;
      }

      if (this.dobTextBox) {
        this.dobTextBox.validator = this.validator.checkDOBDate;
        this.dobTextBox.age = this.passengerObj.age;
        this.dobTextBox.returnDate = this.calculateReturnDate();

      }

      if(this.insDOBTextBox) {
        this.insDOBTextBox.isDisplayed = false;
        this.insDOBTextBox.validator = this.validator.checkDOBRange;
        this.insDOBTextBox.returnDate = this.calculateReturnDate();
        this.handleInsuranceSelect();
      }
      if (this.year.value !== "") {

        if(this.insDOBTextBox){
          this.passengerModel.set("dob", this.setDobValue(this.insDOBTextBox));
        }else if (this.dobTextBox) {
          this.passengerModel.set("dob", this.setDobValue(this.dobTextBox));
        }

      }


  	if ((this.index === 0 && this.falconFlag) || (this.index === 0 && this.flightsFlag)) {
      	  on(this.countryDropdown, "change", lang.hitch(this, this.handleCountrySelection,true));
      }


      if(this.infantNotBornCheckBox && this.passengerObj.age == 0) {
          this.handleInfantNotBornCheckbox();
       }



     /* if(this.passengerObj.type != 'ADULT'){
    	  this.setDateValue();
      }*/



      this.inherited(arguments);
      var passengerDropDown = query('.selectDropDown', this.domNode);
  	  if(this.passengerObj.type == 'ADULT'){
		    this.tagElements(passengerDropDown,"Adult"+this.passengerObj.passengerCount+"-title");
      }else{
        this.tagElements(passengerDropDown,"Child"+this.passengerObj.passengerCount+"-title");
      }

      var passengerFirst = query('.firstName', this.domNode);
  	  if(this.passengerObj.type == 'ADULT'){
		    this.tagElements(passengerFirst,"Adult"+this.passengerObj.passengerCount+"-firstName");
      }else{
        this.tagElements(passengerFirst,"Child"+this.passengerObj.passengerCount+"-firstName");
      }

      var passengerSurn = query('.surnName', this.domNode);
      if(this.passengerObj.type == 'ADULT'){
    		  this.tagElements(passengerSurn,"Adult"+this.passengerObj.passengerCount+"-surnName");
    	  }else{
    		  this.tagElements(passengerSurn,"Child"+this.passengerObj.passengerCount+"-surnName");
    	  }

      var surnameCheckBox = query('.surname-checkBox', this.domNode);
      if(this.passengerObj.type == 'ADULT'){
		    this.tagElements(surnameCheckBox,"Adult2-firstName");
      }else{
        this.tagElements(surnameCheckBox,"Child"+this.passengerObj.passengerCount+"-firstName");
      }
      if(this.index !== 0){
    	  this.surnCheckBox.push(surnameCheckBox[0]);
    	  if (this.paxInfoFormBean != "" ) {
	    	  _.each(this.surnCheckBox, lang.hitch(this, function (surnCheckBoxs, index) {
	  			  domAttr.remove(surnCheckBoxs, "disabled");
	      	  }));
    	  }
      }
      this.tagElements(query('.address', this.domNode),"Adult1-address");
      this.tagElements(query('.telephone', this.domNode),"Adult1-telephone");
      this.tagElements(query('.confireEmail', this.domNode),"Adult1-confireEmail");
      this.tagElements(query('.emails', this.domNode),"Adult1-emails");
      var dob = query('.dobText', this.domNode)
      if(this.passengerObj.type == 'CHILD'){
        this.tagElements(dob,"Child"+this.passengerObj.passengerCount+"-dateOfBirth");
      }
      this.tagElements(query('span.button', this.domNode),"Adult1-findButton");
      this.tagElements(query('a.enterMyAddr', this.domNode),"Adult1-enterMyAddress");
      if(this.index == 0){
      topic.subscribe("Adult.details", lang.hitch(this, this.leadAdultPassenger));
      }


    },

    leadAdultPassenger : function(response){
	 /* console.log(response);
	  this.res=response;
	  console.log(this.res);
	   if(response!=""){
		   if( response.paxInfoFormBean[0].title!=""){

			   for(var j= 0; j< this.titleSelect.selectNode.options.length; j++){

				if(this.titleSelect.selectNode.options[j].text == response.paxInfoFormBean[0].title) {
					this.titleSelect.selectNode.options[j].selected = true;
					this.titleSelect.setSelectedIndex(j);
					htmlUtils.set(this.titleSelect.selectDropdownLabel,response.paxInfoFormBean[0].title)
					this.handleGenderSelect();
					break;
				}
			   }

			   this.selectValidate(this.titleSelect, this.titleSelectDiv);
		  		if(this.titleSelect.isValid()){
		  			this.validateDetails(this.i);
		  		}
			   }
		   if( response.paxInfoFormBean[0].firstName!=""){
	  		this.firstNameBox.set("value", response.paxInfoFormBean[0].firstName);
	  		if(this.firstNameBox.isValid()){
	  			this.validateDetails(this.i);
				this.inherited(arguments);
	  		}
		   }

		   if( response.paxInfoFormBean[0].lastName!=""){
		  		this.surnameBox.set("value", response.paxInfoFormBean[0].lastName);
		  		if(this.surnameBox.isValid()){
		  			this.validateDetails(this.i);
		  		}
			   }
		   if( response.houseNum!=""){
		  		this.houseNumTextBox.set("value",response.houseNum);
		  		if(this.houseNumTextBox.isValid()){
		  			this.validateDetails(this.i);
		  		}
			   }
		   if( response.address1!=""){
		  		this.street1TextBox.set("value", response.address1);
		  		if(this.street1TextBox.isValid()){
		  			this.validateDetails(this.i);
		  		}
			   }
		   if( response.address2!=""){
		  		this.street2TextBox.set("value", response.address2);
		  		if(this.street2TextBox.isValid()){
		  			this.validateDetails(this.i);
		  		}
			   }
		   if( response.town!=""){
		  		this.townTextBox.set("value", response.town);
		  		if(this.townTextBox.isValid()){
		  			this.validateDetails(this.i);
		  		}
			   }
		   if( response.county!=""){
		  		this.countyTextBox.set("value", response.county);
		  		if(this.countyTextBox.isValid()){
		  			this.validateDetails(this.i);
		  		}
			   }
		   if( response.postCode!=""){
		  		this.postcodeTextBox.set("value", response.postCode);
		  		if(this.postcodeTextBox.isValid()){
		  			this.validateDetails(this.i);
		  		}
			   }
		   if( response.telephoneNum!=""){
		  		this.telephoneTextBox.set("value", response.telephoneNum);
		  		if(this.telephoneTextBox.isValid()){
		  			this.validateDetails(this.i);
		  		}
			   }
		   if( response.email!=""){
		  		this.emailTextBox.set("value", response.email);
		  		if(this.emailTextBox.isValid()){
		  			this.validateDetails(this.i);
		  		}
			   }
		   if( response.confirmationEmail!=""){
		  		this.emailConfirmationTextBox.set("value", response.confirmationEmail);
		  		if(this.emailConfirmationTextBox.isValid()){
		  			this.validateDetails(this.i);
		  			this.validateDetails(this.i);
		  		}
			   }
	   }*/
    },
    validateDetails:function(index){
    	this.innerErrorBox = query(".dijitValidationInner", this.domNode)[index];
    	domClass.remove(this.innerErrorBox.parentElement, "dijitValidationContainer");
		domClass.add(this.innerErrorBox, "validStateTextBox");
		domStyle.set(this.innerErrorBox, "display", "block");
		this.i++;
    },

    attachEvents: function () {
    	if(this.jsonData.customerSignInViewData.signInAttempted){
    		this.res=this.passengerDetailsFormBean;
    	}

      on(this.titleSelect, "change", lang.hitch(this, this.handleGenderSelect));
      on(this.genderSelect, "change", lang.hitch(this, this.selectValidate, this.genderSelect, this.genderSelectDiv));
      on(this.titleSelect, "blur", lang.hitch(this, function(){
     	 //this.fadeOutMessage(getLCRRemoveText);
        if(!this.titleSelect.listShowing) {
          this.selectValidate(this.titleSelect, this.titleSelectDiv);
        }


      }));
      on(this.genderSelect, "blur", lang.hitch(this, function(){
     	 this.fadeOutMessage(getLCRRemoveText);
        if(!this.genderSelect.listShowing) {
          this.selectValidate(this.genderSelect, this.genderSelectDiv);
        }
      }));
      if(this.insuranceSelect){
        on(this.insuranceSelect, "change", lang.hitch(this, this.handleInsuranceSelect));
      }else if(this.insuranceChildSelect){
        on(this.insuranceChildSelect, "change", lang.hitch(this, this.removeErrorStylesForInsuranceSelects, this.insuranceChildSelect));
      }

      if (this.dobTextBox) {
  	    this.dobHandler = on.pausable(this.dobTextBox, "change", lang.hitch(this, this.setDateValue, this.dobTextBox, this["infant"+this.index]));
  	  on(this.dobTextBox, "keydown", function(evt){
  		 console.log(evt.keyCode)
  		if (evt.keyCode == 9){
  			this.focus();
  			evt.stopPropagation();

  		}

  	  });
      }

      if(this.insDOBTextBox) {
       on(this.insDOBTextBox, "change", lang.hitch(this, this.setDateValue, this.insDOBTextBox, this["infant"+this.index]));
      }
      if(this.infantNotBornCheckBox && this.passengerObj.age == 0) {
    	  on(this.infantNotBornCheckBox, "click", lang.hitch(this, this.handleInfantNotBornCheckbox));
      }
      if(this.dpnOverlay) {
    	  on(this.dpnOverlay, "click", lang.hitch(this, this.handleDpnOverlay));
      }


      if (this.index != 0) {
        on(this.surnameCheckbox, "click", lang.hitch(this, this.handleSurnameCheckBox));
        on(this.surnameBox, "blur", lang.hitch(this, this.handleRemaing));
      } else {
        //on(this.addressManually, "click", lang.hitch(this, this.handleAddressWidgets));
      }

    var firstNameFlag = false, surnameFlag = false;

      if(this.index == 0){
    	 console.log(this.passengerDetailsFormBean);
     	 on(this.surnameBox, "blur", lang.hitch(this, this.handleSurnameText));
       	/* on(this.firstNameBox, "change", lang.hitch(this, function(){
       		 console.log(this.res.paxInfoFormBean[0].firstName);
    		 if(this.res.paxInfoFormBean[0].firstName != this.firstNameBox.get("value") && this.res.paxInfoFormBean[0].firstName != ""){
    			 this.leadAdultBox.push("firstName");
    			 this.getParent().isDetailChanged = true;
    			 getLCRRemoveText = query(".detailsUpdatedMessage")[0]
     			this.fadeOutMessage(getLCRRemoveText);
    		 }else{
    			 this.leadAdultBox.pop();
    		 }
    	 }));
    	 on(this.surnameBox, "change", lang.hitch(this, function(){
    		 if(this.res.paxInfoFormBean[0].lastName != this.surnameBox.get("value") && this.res.paxInfoFormBean[0].lastName != ""){
    			 this.leadAdultBox.push("surName");
    			 this.getParent().isDetailChanged = true;
    			 getLCRRemoveText = query(".detailsUpdatedMessage")[0]
      			this.fadeOutMessage(getLCRRemoveText);
    		 }else{
    			 this.leadAdultBox.pop();
    		 }
    	 }));
    	 on(this.houseNumTextBox, "change", lang.hitch(this, function(){
    		 if(this.res.houseNum != this.houseNumTextBox.get("value") && this.res.paxInfoFormBean[0].houseNum != ""){
    			 this.leadAdultBox.push("housenumber");
    			 this.getParent().isDetailChanged = true;
    			 getLCRRemoveText = query(".detailsUpdatedMessage")[0]
      			this.fadeOutMessage(getLCRRemoveText);
    		 }else{
    			 this.leadAdultBox.pop();
      }
    	 }));

    	 on(this.telephoneTextBox, "change", lang.hitch(this, function(){
    		 if(this.res.telephoneNum != this.telephoneTextBox.get("value")  && this.res.paxInfoFormBean[0].telephoneNum != ""){
    			 this.leadAdultBox.push("telephone");
    			 this.getParent().isDetailChanged = true;
    			 getLCRRemoveText = query(".detailsUpdatedMessage")[0]
      			this.fadeOutMessage(getLCRRemoveText);
    		 }else{
    			 this.leadAdultBox.pop();
    		 }
    	 }));
    	 on(this.emailTextBox, "change", lang.hitch(this, function(){
    		 if(this.res.email != this.emailTextBox.get("value")  && this.res.paxInfoFormBean[0].email != ""){
    			 this.leadAdultBox.push("email");
    			 this.getParent().isDetailChanged = true;
    			 getLCRRemoveText = query(".detailsUpdatedMessage")[0]
      			this.fadeOutMessage(getLCRRemoveText);
    		 }else{
    			 this.leadAdultBox.pop();
    		 }
    	 }));
    	 on(this.emailConfirmationTextBox, "change", lang.hitch(this, function(){
    		 if(this.res.confirmationEmail != this.emailConfirmationTextBox.get("value")  && this.res.paxInfoFormBean[0].confirmationEmail != ""){
    			 this.leadAdultBox.push("emailConfirmation");
    			 this.getParent().isDetailChanged = true;
    			 getLCRRemoveText = query(".detailsUpdatedMessage")[0]
      			this.fadeOutMessage(getLCRRemoveText);
    		 }else{
    			 this.leadAdultBox.pop();
    		 }
    	 }));
    	 on(this.postcodeTextBox, "change", lang.hitch(this, function(){
    		 if(this.res.postCode != this.postcodeTextBox.get("value")  && this.res.paxInfoFormBean[0].postCode != ""){
    			 this.leadAdultBox.push("postcode");
    			 this.getParent().isDetailChanged = true;
    			 getLCRRemoveText = query(".detailsUpdatedMessage")[0]
      			this.fadeOutMessage(getLCRRemoveText);
    		 }else{
    			 this.leadAdultBox.pop();
    		 }
    	 }));
    	 on(this.townTextBox, "change", lang.hitch(this, function(){
    		 if(this.res.town != this.townTextBox.get("value")  && this.res.paxInfoFormBean[0].town != ""){
    			 this.leadAdultBox.push("town");
    			 this.getParent().isDetailChanged = true;
    			 getLCRRemoveText = query(".detailsUpdatedMessage")[0]
      			this.fadeOutMessage(getLCRRemoveText);
    		 }else{
    			 this.leadAdultBox.pop();
    		 }
    	 }));
    	 on(this.countyTextBox, "change", lang.hitch(this, function(){
    		 if(this.res.county != this.countyTextBox.get("value")  && this.res.paxInfoFormBean[0].county != ""){
    			 this.leadAdultBox.push("country");
    			 this.getParent().isDetailChanged = true;
    			 getLCRRemoveText = query(".detailsUpdatedMessage")[0]
      			this.fadeOutMessage(getLCRRemoveText);
    		 }else{
    			 this.leadAdultBox.pop();
    		 }
    	 }));*/
      }


/*      if(this.index == 1){
      	  on(this.firstNameBox, "focus", lang.hitch(this, function(){
      		   if( this.getParent().isDetailChanged){
      		  if(!this.isEmpty(this.leadAdultBox)){
      			getLCRRemoveText = query(".detailsUpdatedMessage")[0]
    			this.fadeOutMessage(getLCRRemoveText);
      		  }
       }

     	 }));
      	  on(this.surnameBox, "focus", lang.hitch(this, function(){
      		if(this.getParent().isDetailChanged){
        		if(!this.isEmpty(this.leadAdultBox)){
        			getLCRRemoveText = query(".detailsUpdatedMessage")[0]
        			this.fadeOutMessage(getLCRRemoveText);
        		}
        		  }
       	 }));

      	on(this.houseNumTextBox, "focus", lang.hitch(this, function(){
      		if(this.getParent().isDetailChanged){
        		if(!this.isEmpty(this.leadAdultBox)){
        			getLCRRemoveText = query(".detailsUpdatedMessage")[0]
        			this.fadeOutMessage(getLCRRemoveText);
        		}
        		  }
       	 }));

      	on(this.telephoneTextBox, "focus", lang.hitch(this, function(){
      		if(this.getParent().isDetailChanged){
        		if(!this.isEmpty(this.leadAdultBox)){
        			getLCRRemoveText = query(".detailsUpdatedMessage")[0]
        			this.fadeOutMessage(getLCRRemoveText);
        		}
        		  }
       	 }));

      	on(this.emailTextBox, "focus", lang.hitch(this, function(){
      		if(this.getParent().isDetailChanged){
        		if(!this.isEmpty(this.leadAdultBox)){
        			getLCRRemoveText = query(".detailsUpdatedMessage")[0]
        			this.fadeOutMessage(getLCRRemoveText);
        		}
        		  }
       	 }));

      	on(this.emailConfirmationTextBox, "focus", lang.hitch(this, function(){
      		if(this.getParent().isDetailChanged){
        		if(!this.isEmpty(this.leadAdultBox)){
        			getLCRRemoveText = query(".detailsUpdatedMessage")[0]
        			this.fadeOutMessage(getLCRRemoveText);
        		}
        		  }
       	 }));

      	on(this.postcodeTextBox, "focus", lang.hitch(this, function(){
      		if(this.getParent().isDetailChanged){
        		if(!this.isEmpty(this.leadAdultBox)){
        			getLCRRemoveText = query(".detailsUpdatedMessage")[0]
        			this.fadeOutMessage(getLCRRemoveText);
        		}
        		  }
       	 }));

      	on(this.townTextBox, "focus", lang.hitch(this, function(){
      		if(this.getParent().isDetailChanged){
        		if(!this.isEmpty(this.leadAdultBox)){
        			getLCRRemoveText = query(".detailsUpdatedMessage")[0]
        			this.fadeOutMessage(getLCRRemoveText);
        		}
        		  }
       	 }));

      	on(this.countyTextBox, "focus", lang.hitch(this, function(){
      		if(this.getParent().isDetailChanged){
        		if(!this.isEmpty(this.leadAdultBox)){
        			getLCRRemoveText = query(".detailsUpdatedMessage")[0]
        			this.fadeOutMessage(getLCRRemoveText);
        		  }
      		}
       	 }));

      	on(this.titleSelect, "focus", lang.hitch(this, function(){
      		if(this.getParent().isDetailChanged){
        		if(!this.isEmpty(this.leadAdultBox)){
        			getLCRRemoveText = query(".detailsUpdatedMessage")[0]
        			this.fadeOutMessage(getLCRRemoveText);
        		}
       }
       	 }));

      	on(this.genderSelect, "focus", lang.hitch(this, function(){
      		if(this.getParent().isDetailChanged){
        		if(!this.isEmpty(this.leadAdultBox)){
        			getLCRRemoveText = query(".detailsUpdatedMessage")[0]
        			this.fadeOutMessage(getLCRRemoveText);
      }
      }
       	 }));


      }*/
      if(this.passengerObj.age == 0 && this.passengerObj.type == 'INFANT'){
    	  domStyle.set(this["infant"+this.index], "display", "block");
       }
    },
    isEmpty : function(obj) {
		for(var key in obj) {
			if(obj.hasOwnProperty(key))
				return false;
		}
		return true;
	},
	handleCountrySelection :function(flag) {
		if( this.countryDropdown.getSelectedData().value != "GB" && this.jsonData.packageType == "FO"){
			this.postcodeTextBox.regExp = '.*';
	  		this.postcodeTextBox.required = false;// non uk for Flights Only
	  		this.postcodeTextBox._computePartialRE();

	  		this.telephoneTextBox.regExp ='^([0-9]{1,11})$';
	  		this.telephoneTextBox._computePartialRE();

	  		if(flag){
	  			this.postcodeTextBox.reset();
	  			this.telephoneTextBox.reset();

	  		}
  	    }
		else if(this.countryDropdown.getSelectedData().value == "IE"){
			this.postcodeTextBox.regExp = '^[a-zA-Z0-9]+(?:\ [a-zA-Z0-9]+)*$';
	  		this.postcodeTextBox.required = false;// non uk
	  		this.postcodeTextBox._computePartialRE();

	  		this.telephoneTextBox.regExp ='^(0([1-9]{1}[0-4,6-9]{1}[0-9]{1}[0-9]{3,7})|0([1-9]{1}[5]{1}[0-2,4-9]{1}[0-9]{3,7})|[1-9]{1}[0-4,6-9]{1}[0-9]{1}[0-9]{3,7}|[1-9]{1}[5]{1}[0-2,4-9]{1}[0-9]{3,7})$';
	  		this.telephoneTextBox._computePartialRE();

	  		this.townTextBox.regExp='^[a-zA-Z]+(([\ ]{1,1}[0-9]{1,1}[0-9a-zA-Z]{0,1}?)?)$';
	  		this.townTextBox._computePartialRE();

	  		this.countyTextBox.regExp='^([a-zA-Z]?[a-zA-Z]*[a-zA-Z])$';
	  		this.countyTextBox._computePartialRE();

	  		if(flag){
	  			this.postcodeTextBox.reset();
	  			this.telephoneTextBox.reset();
	  			this.townTextBox.reset();
	  			this.countyTextBox.reset();
	  		}
  	    }else{
	  		this.postcodeTextBox.regExp = '^([A-Pa-pR-UWYZr-uwyz0-9][A-Ha-hK-Yk-y0-9][AEHMNPRTUVXYaehmnprtuvxy0-9]?[ABEHMNPRVWXYabehmnprvwxy0-9]?[ \\s]{0,1}[0-9][ABD-HJLN-UW-Zabd-hjln-uw-z]{2}|GIR 0AA)$';
	  		this.postcodeTextBox.required = true;
	  		this.postcodeTextBox._computePartialRE();

	  		this.telephoneTextBox.regExp = '^\\s*(0([4]{1}[0-3,5-9]{1}[0-9]{1}[0-9]{7}|[1-3,5-9]{1}[0-9]{1}[0-9]{8})|[1-9]{1}[0-9]{9})\\s*$';
	  		this.telephoneTextBox._computePartialRE();

	  		this.townTextBox.regExp='^([a-zA-Z_\/.,-]?[a-zA-Z_ \/.,-]*[a-zA-Z_\/.,-])$';
	  		this.townTextBox._computePartialRE();


            if(this.jsonData.packageType == "FO"){
            	this.countyTextBox.regExp='^([a-zA-Z]?[a-zA-Z]*[a-zA-Z])$';
    	  		this.countyTextBox._computePartialRE();
            }
            else{
            	this.countyTextBox.regExp='^([a-zA-Z0-9]?[a-zA-Z0-9]*[a-zA-Z0-9])$';
    	  		this.countyTextBox._computePartialRE();
            }



	  		if(flag){
	  			this.postcodeTextBox.reset();
	  			this.telephoneTextBox.reset();
	  			this.townTextBox.reset();
	  			this.countyTextBox.reset();
	  		}
  	   }
	},

	fadeOutMessage : function(getLCRRemoveText){
		this.getParent().isDetailChanged=false;
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

    removeErrorStylesForInsuranceSelects : function(selectBox) {
      var innerBox = query("input", this.selectInsBlock)[0];
      domClass.remove(query("a", selectBox.domNode)[0], "selectOptionError");
      domClass.remove(innerBox, "selectOptionErrorInnerBox");
      domClass.add(innerBox, "selectOptionValidInnerBox");
      PassengerUtils.displayMessagesForSelect("", selectBox, this.selectInsBlock);
      domStyle.set(this.selectInsBlock, "display", "inline");
    },

    handleInsuranceSelect : function(){
      this.removeErrorStylesForInsuranceSelects(this.insuranceSelect);
      if (this.insuranceSelect.getSelectedData().value === "") {
        domStyle.set(this.insDObBlock, "display", "none");
        this.insDOBTextBox.isDisplayed = false;
        this.insDOBTextBox.set("value", "");
      } else {
        domStyle.set(this.insDObBlock, "display", "block");
        this.insDOBTextBox.isDisplayed = true;
        arrayUtils.forEach(this.insSelectionDataList, lang.hitch(this, function(entry, i){
          if(entry.value === this.insuranceSelect.getSelectedData().value) {
            this.insDOBTextBox.minAge = entry.minAge;
            this.insDOBTextBox.maxAge = entry.maxAge;
          }
        }));

      }
    },

    selectValidate: function (selectBox, divBlock) {


    	if (domClass.contains(dom.byId(dojoConfig.site), "modalmode")){
    		var tempNode = query(".childvalidation-modal-window", dom.byId(dojoConfig.site))[0];
    		if (tempNode.style.display == "block"){
    			console.log(tempNode);
  			}

    	}else{

    		 var innerBox = query("input", divBlock)[0];
    	      this.passengerModel.set("gender", this.genderSelect.getSelectedData().value);
    	      this.passengerModel.set("title", this.titleSelect.getSelectedData().value);
    	      domAttr.set(innerBox, "value", "");
    	      if (selectBox.selectNode.options[0].selected === true) {
    	        domClass.remove(innerBox, "selectOptionValidInnerBox");
    	        domClass.add(query("a", selectBox.domNode)[0], "selectOptionError");
    	        domClass.add(innerBox, "selectOptionErrorInnerBox");
    	        PassengerUtils.displayMessagesForSelect(selectBox.message, selectBox, this.selectBlock);
    	        domStyle.set(divBlock, "display", "inline");
    	      } else {
    	        domClass.remove(query("a", selectBox.domNode)[0], "selectOptionError");
    	        domClass.remove(innerBox, "selectOptionErrorInnerBox");
    	        domClass.add(innerBox, "selectOptionValidInnerBox");
    	        PassengerUtils.displayMessagesForSelect("", selectBox, this.selectBlock);
    	        domStyle.set(divBlock, "display", "inline");
    	      }
    	}

    },

    handleGenderSelect: function () {

      if (this.titleSelect.getSelectedData().value === "MISS" ||
          this.titleSelect.getSelectedData().value === "MRS" ||
          this.titleSelect.getSelectedData().value === "MS") {
        htmlUtils.set(this.genderSelect.selectDropdownLabel, "Female");
        this.genderSelect.selectNode.options[2].selected = true;
        domStyle.set(this.genderSelectDiv, "display", "none");
        domStyle.set(this.genderSelect.domNode, "display", "none");
        PassengerUtils.displayMessagesForSelect("", this.genderSelect, this.selectBlock);
      } else if (this.titleSelect.getSelectedData().value === "MR" || this.titleSelect.getSelectedData().value === "MSTR" || this.titleSelect.selectNode.value === "MR") {
        htmlUtils.set(this.genderSelect.selectDropdownLabel, "Male");
        this.genderSelect.selectNode.options[1].selected = true;
        domStyle.set(this.genderSelectDiv, "display", "none");
        domStyle.set(this.genderSelect.domNode, "display", "none");
        PassengerUtils.displayMessagesForSelect("", this.genderSelect, this.selectBlock);
      } else if (this.titleSelect.getSelectedData().value === "") {
        htmlUtils.set(this.genderSelect.selectDropdownLabel, "Gender");
        this.genderSelect.selectNode.options[0].selected = true;
        domClass.remove(query("a", this.genderSelect.domNode)[0], "selectOptionError");
        domStyle.set(this.genderSelectDiv, "display", "none");
        domStyle.set(this.genderSelect.domNode, "display", "none");
        PassengerUtils.displayMessagesForSelect("", this.genderSelect,this.selectBlock);
      } else {
        htmlUtils.set(this.genderSelect.selectDropdownLabel, "Gender");
        this.genderSelect.selectNode.options[0].selected = true;
        domStyle.set(this.genderSelect.domNode, "display", "inline");
      }
      this.passengerModel.set("title", this.titleSelect.getSelectedData().value);
      this.passengerModel.set("gender", this.genderSelect.getSelectedData().value);
      this.selectValidate(this.titleSelect, this.titleSelectDiv);
      _.each(query(".selectOptionValidInnerBox", this.domNode), lang.hitch(this, function (errorNotation){
			domStyle.set(errorNotation, "display", "block");
		}));
    _.each(query(".selectOptionErrorInnerBox", this.domNode), lang.hitch(this, function (errorNotation){
			domStyle.set(errorNotation, "display", "block");
		}));
    },

    handleAddressWidgets: function () {

      domStyle.set("form-address-wrap", "display", "block");
      domStyle.set(this.street1TextBox.domNode, "display", "block");
      domStyle.set(this.street2TextBox.domNode, "display", "block");
      domStyle.set(this.townTextBox.domNode, "display", "block");
      domStyle.set(this.countyTextBox.domNode, "display", "block");
      domStyle.set(this.countryDropdown.domNode, "display", "block");
    },

    handleSurnameCheckBox: function () {
      var leadPassengerSurname = dijit.byId(query("input[name='paxInfoFormBean[0].lastName']")[0]);
      var booleanVal = domClass.contains(leadPassengerSurname.parentNode.parentNode, "dijitValidationTextBoxError")
      if (this.surnameCheckbox.checked && leadPassengerSurname.value && !booleanVal) {
        domAttr.set(this.surnameBox.id, "readonly", "readonly");
        this.surnameBox.set("value", leadPassengerSurname.value);
        var resetCat = query(".dijitReset", this.surnameBox.domNode)[0];
    	var resetCatVal = query(".dijitReset .dijitInputField", this.surnameBox.domNode)[0];
    	domClass.remove(resetCat,"dijitValidationContainer")
    	domClass.add(resetCatVal, "validStateTextBox");

        /*domStyle.set(this.tickImg, "display", "block");
        domClass.remove(this.tickImg, "selectOptionErrorInnerBox");*/
      } else {
        domAttr.remove(this.surnameBox.id, "readonly");
        this.surnameBox.set("value", "");
        //this.surnameBox.set("value", leadPassengerSurname.value);

        if (this.surnameBox.isValid() == false){

        	this.removeTick(this.surnameBox);


        }
        /*domStyle.set(this.tickImg, "display", "none");
        domClass.add(this.tickImg, "selectOptionErrorInnerBox");*/
      }
    },
    handleRemaing: function(){
    	var leadPassengerSurname = dijit.byId(query("input[name='paxInfoFormBean[0].lastName']")[0]);

    if(this.surnameBox.value == leadPassengerSurname.value && this.surnameBox.value != ""){
	this.surnameCheckbox.checked = true;

   }else{
	this.surnameCheckbox.checked = false;
      }
    },

    removeTick: function(objRef){
    	var resetCat = query(".dijitReset", objRef.domNode)[0];
    	var resetCatVal = query(".dijitReset .dijitInputField", objRef.domNode)[0];
    	domClass.add(resetCat,"dijitValidationContainer")
    	domClass.remove(resetCatVal, "validStateTextBox");
    },

    handleSurnameText : function(){
   	 var leadPassengerSurname = dijit.byId(query("input[name='paxInfoFormBean[0].lastName']")[0]);
    var formParentNode = dom.byId("passengerDetailsPanel");
     var subSections = query(".sub-section", formParentNode);


   	_.each(this.surnCheckBox, lang.hitch(this, function (surnCheckBoxs, index) {
   		if(domClass.contains(leadPassengerSurname.parentNode.parentNode, "dijitValidationTextBoxError")){

   	   	  domAttr.set(surnCheckBoxs, "disabled", " ");

   	   	}else{
   	   		domAttr.remove(surnCheckBoxs, "disabled");
   	   	}

   	_.each(subSections, function (subSection) {
   	  var passengerForm = query(".passenger-detail-form", subSection)[0];
   		var passengerdetailView = dijit.byId(passengerForm.id);
   		if(passengerdetailView.surnameCheckbox && passengerdetailView.surnameCheckbox.checked == true){

   			if(domClass.contains(leadPassengerSurname.parentNode.parentNode, "dijitValidationTextBoxError")){

   				domAttr.remove(passengerdetailView.surnameBox.id, "readonly");
   				passengerdetailView.surnameBox.set("value", leadPassengerSurname.value);
   				passengerdetailView.removeTick(passengerdetailView.surnameBox);

   		        domStyle.set(passengerdetailView.tickImg, "display", "none");
   		        domClass.add(passengerdetailView.tickImg, "selectOptionErrorInnerBox");
   		        domAttr.remove(passengerdetailView.surnameCheckbox, "disabled");

      	   	}else{
      	   	domAttr.set(passengerdetailView.surnameBox.id, "readonly", "readonly");
   			passengerdetailView.surnameBox.set("value", leadPassengerSurname.value);

      	   	}


   		}

   	});



       	  }));
      },
   confirmationText : function(flags){

	  /* if(this.jsonData.confiremationText == this.confirmationText.get("value")){
		   flags = true;
	   }*/

	   if(flags){

		   domStyle.set(this.detailsUpdatedMessage, "display", "block");
	   }

	   },

    handleInfantNotBornCheckbox: function () {
    	var infantSelectBox = query(".infantSelect", this.domNode)[0];
    	var infantTitle = query("span.infantTitle", this.domNode)[0];
    	var surnameCheckBox = query('.surname-checkBox', this.domNode);

    	if(this.infantNotBornCheckBox.checked){
    		 this.dobHandler.pause();
                    this.infantNotYetFlag = true;
    		 domStyle.set(this.infantNotBornWarp, "display", "none");
    		 domStyle.set(this.infantNotBornMessage, "display", "block");
    		 domStyle.set(infantSelectBox, "display", "none");
    		 domStyle.set(infantTitle, "display", "none");
    		 this.firstNameBox.set("value",  this.infantNotYetBornData.firstName);
    		 this.surnameBox.set("value", this.infantNotYetBornData.surName);
    		 this.dobTextBox.set("value", "");
    		 htmlUtils.set(this.titleSelect.selectDropdownLabel, "select");
    		 htmlUtils.set(this.genderSelect.selectDropdownLabel, "select");
    		 this.genderSelect.selectNode.options[1].selected = true;
    	     this.titleSelect.selectNode.options[1].selected = true;
    		 this.day.value = this.infantNotYetBornData.day;
    		 this.month.value= this.infantNotYetBornData.month;
    		 this.year.value = this.infantNotYetBornData.year;
    		 this.infantNotYetBornFlag.value = "true";
    		 _.each(query(".selectOptionValidInnerBox", this.domNode), lang.hitch(this, function (errorNotation){
   	   			domStyle.set(errorNotation, "display", "none");
   	   		}));
    		if(dojo.byId(infantSelectBox.id+"_Error") != null){
    		 domStyle.set(infantSelectBox.id+"_Error", "display", "none");
    		 domStyle.set(this.titleSelectDiv, "display", "none");
			}
			if(!this.isEmpty(query(".selectOptionErrorInnerBox", this.domNode))){
    		 domStyle.set(query(".selectOptionErrorInnerBox", this.domNode)[0], "display", "none");
			 }
    		 if(!this.isEmpty(query(".error-notation", this.domNode))){
    		 domStyle.set(query(".error-notation", this.domNode), "display", "none");
			 }
    	}else {
    		this.dobHandler.resume();
			domStyle.set(this.infantNotBornWarp, "display", "block");
			domStyle.set(this.infantNotBornMessage, "display", "none");
			domStyle.set(infantSelectBox, "display", "block");
	    	domStyle.set(infantTitle, "display", "block");
	    	//htmlUtils.set(this.titleSelect.selectDropdownLabel, "select");
	    	htmlUtils.set(this.genderSelect.selectDropdownLabel, "select");
	    	this.genderSelect.selectNode.options[0].selected = false;
	    	this.titleSelect.selectNode.options[0].selected = false;
			this.firstNameBox.set("value", "");
   		  	this.surnameBox.set("value", "");
   		  	this.infantNotYetBornFlag.value = "false";
   		  	this.surnameCheckbox.checked = false;
	   		_.each(query(".error-notation", this.domNode), lang.hitch(this, function (errorNotation){
	   			domStyle.set(errorNotation, "display", "none");
	   		}));
	   		_.each(query(".dijitValidationInner", this.domNode), lang.hitch(this, function (errorNotation){
	   			domStyle.set(errorNotation, "display", "none");
	   		}));
	   		_.each(query(".selectOptionValidInnerBox", this.domNode), lang.hitch(this, function (errorNotation){
	   			domStyle.set(errorNotation, "display", "none");
	   		}));
	   		_.each(query(".selectOptionErrorInnerBox", this.domNode), lang.hitch(this, function (errorNotation){
	   			domStyle.set(errorNotation, "display", "none");
	   		}));
	   		_.each(query("a.dropdown", this.domNode), lang.hitch(this, function (errorNotation){
	   			domClass.remove(errorNotation, "selectOptionError");
	   		}));
    	}

    },
	isEmpty : function(obj) {
			if(has("ie") == 8){
				if(obj === undefined || obj.length === 0){
					return true;
				}else{
					return false;
				}
			}else{
				if(obj.length !== 0){
					for(var key in obj) {
						if(obj.hasOwnProperty(key))
							return false;
					}
				}
				return true;
			}
		},

    handleDpnOverlay: function () {
    	 if (this.passengerDpnView != null) {
           this.passengerDpnView.destroyRecursive();
           this.passengerDpnView = null;
           this.passengerDpnOverlay.destroyRecursive();
           this.passengerDpnOverlay = null;
         }
         this.passengerDpnView = new PassengerDpnView({
        	 "jsonData": this.jsonData,
             "id": "dpn-overlay"
         });
         domConstruct.place(this.passengerDpnView.domNode, this.domNode, "last");
         this.passengerDpnOverlay = new PassengerDpnOverlay({widgetId: this.passengerDpnView.id, modal: true});
         this.passengerDpnView.passengerDpnOverlay = this.passengerDpnOverlay;
         this.passengerDpnOverlay.refWidget = this;
         this.passengerDpnOverlay.open();

    },

    showInsAlertOverlay: function () {
    	 if (this.passengerInsAlert != null) {
             this.passengerInsAlert.destroyRecursive();
             this.passengerInsAlert = null;
             this.passengerInsAlertOverlay.destroyRecursive();
             this.passengerInsAlertOverlay = null;
         }
         this.passengerInsAlert = new PassengerInsAlert({
            "id": this.id+"_ins_overlay",
            "jsonData": this.jsonData
         });
         domConstruct.place(this.passengerInsAlert.domNode, this.domNode, "last");
         this.passengerInsAlertOverlay = new PassengerInsAlertOverlay({widgetId: this.id+"_ins_overlay", modal: true});
         this.passengerInsAlertOverlay.open();

    },


    setDateValue: function (dobTextBox, infantNotYet) {
      if (dobTextBox.isValid()) {
        var dobDate = PassengerUtils.getDateInCorrectFormat(dobTextBox.value);
        this.year.value = dobDate.getFullYear();
        this.month.value = dobDate.getMonth() + 1;
        this.day.value = dobDate.getDate();
      } else {
        if(!dobTextBox.ageValid){

        	if(this.type === "ADULT" ){
            this.insDOBCounter++;
            if(this.insDOBCounter >= 2){
              this.showInsAlertOverlay();
            }
          }

          else if(this.type == "CHILD" || this.type == "INFANT"){
        	  var dobDate = PassengerUtils.getDateInCorrectFormat(dobTextBox.value);
              this.year.value = dobDate.getFullYear();
              this.month.value = dobDate.getMonth() + 1;
              this.day.value = dobDate.getDate();
            if (this.passengerChildAgeView != null) {
              var index = arrayUtils.indexOf(this.controller.views,this.passengerChildAgeView);
              if (index !== -1) {
                this.controller.views. splice(index, 1);
              }
              this.passengerChildAgeView.destroyRecursive();
              this.passengerChildAgeView = null;
              this.passengerChildAgeOverlay.destroyRecursive();
              this.passengerChildAgeOverlay = null;
            }

            document.getElementById(dobTextBox.id).blur();
            this.passengerChildAgeView = new PassengerChildAgeView({
              "id": "childAge_Overlay"+this.passengerObj.identifier,
              "childId":this.passengerObj.identifier,
              "childage":this.passengerObj.age,
              "dobTextBoxValue":dobTextBox.value,
              "infantNotYet":infantNotYet
            });
            domConstruct.place(this.passengerChildAgeView.domNode, this.domNode, "first");
            this.passengerChildAgeOverlay = new PassengerChildAgeOverlay({widgetId: this.passengerChildAgeView.id, modal: true});
            this.passengerChildAgeView.passengerChildAgeOverlay = this.passengerChildAgeOverlay;
            this.passengerChildAgeOverlay.refWidget = this;
            this.passengerChildAgeOverlay.open();

        }

      }
      }
    },

    setDobValue: function (dobTextBox) {
      var dayValue = this.day.value.toString().length === 1 ? "0" + this.day.value : this.day.value;
      var monthValue = this.month.value.toString().length === 1 ? "0" + this.month.value : this.month.value;
      var dobStr = dayValue + "/" + monthValue + "/" + this.year.value;
      dobTextBox.set("value", dobStr);
    },


    calculateReturnDate: function () {
   	 this.fVD =this.controller.refData(this.jsonData.packageViewData.flightViewData, 0);

    	 if ( ! _.isEmpty(this.fVD.inboundSectors)){
      return new Date(this.Schedule.departureDateInMiliSeconds);
    	 }
    	 else{
    		  return new Date(this.Schedule.departureDateInMiliSeconds);
    	 }

    },

    isInfantNotYetValidateMessage : function(errorNotation){
    	if(this.type === "INFANT" && this.age.value === "0"){
    		if(this.dobTextBox.value === "" || this.firstNameBox.value === ""  || this.surnameBox.value === "" || this.titleSelect.selectDropdownLabel.innerHTML === "select" ){
    			return true;
    		}
    	}

    	return false;
    },

    validateAndDisplayMessages: function () {
      var valid = true;
      if (this.index === 0) {
        //this.handleAddressWidgets();
      }

      if (this.titleSelect.getSelectedData().value === "") {
        this.selectValidate(this.titleSelect, this.titleSelectDiv);
        valid = false;
      } else if (this.genderSelect.selectNode.options[0].selected === true) {
        this.selectValidate(this.genderSelect, this.genderSelectDiv);
        valid = false;
      }


      for (var index = 0; index < this._attachPoints.length; index++) {
        if (this[this._attachPoints[index]].state && this[this._attachPoints[index]].state === "Incomplete") {
          if (this[this._attachPoints[index]].required) {
            if(this.insDOBTextBox === this[this._attachPoints[index]] && !this.insDOBTextBox.isDisplayed){
               continue;
            } else {
              this[this._attachPoints[index]]._hasBeenBlurred = true;
              this[this._attachPoints[index]].validate(false);
            }
          }
          valid = false;
        }
      }

      if(this.infantNotBornCheckBox){
     	   _.each(query(".error-notation", this.dompNode), lang.hitch(this, function (errorNotation){
     		   if(this.isInfantNotYetValidateMessage()){
     			   domStyle.set(errorNotation, "display", "block");
     		   }

     		}));
     		_.each(query(".dijitValidationInner", this.domNode), lang.hitch(this, function (errorNotation){
     			if(this.isInfantNotYetValidateMessage()){
     				if(errorNotation.parentElement.parentElement.id.replace("widget_","") == this.dobTextBox.id && this.dobTextBox.value === "" ){
     					domClass.remove(errorNotation, 'validStateTextBox');
         				domStyle.set(errorNotation, "display", "block");
     	    		}
     	    		if(errorNotation.parentElement.parentElement.id.replace("widget_","") == this.firstNameBox.id && this.firstNameBox.value === "" ){
     	    			domClass.remove(errorNotation, 'validStateTextBox');
         				domStyle.set(errorNotation, "display", "block");
     	    		}
     	    		if(errorNotation.parentElement.parentElement.id.replace("widget_","") == this.surnameBox.id && this.surnameBox.value === "" ){
     	    			domClass.remove(errorNotation, 'validStateTextBox');
         				domStyle.set(errorNotation, "display", "block");
     	    		}

     			}
     		}));
     		_.each(query(".selectOptionValidInnerBox", this.domNode), lang.hitch(this, function (errorNotation){
     			if(this.isInfantNotYetValidateMessage()){
     				if(this.titleSelect.selectDropdownLabel.innerHTML === "select" ){
     					domClass.add(errorNotation, 'selectOptionErrorInnerBox');
     					domClass.remove(errorNotation, 'selectOptionValidInnerBox');
     					PassengerUtils.displayMessagesForSelect("Please select a title", this.titleSelect, this.selectBlock);
     					domStyle.set(errorNotation, "display", "block");
     				}
     			}
     		}));
     		_.each(query(".selectOptionErrorInnerBox", this.domNode), lang.hitch(this, function (errorNotation){
     			domStyle.set(errorNotation, "display", "block");
     		}));
     		_.each(query("a.dropdown", this.domNode), lang.hitch(this, function (errorNotation){
     			if(this.isInfantNotYetValidateMessage()){
     				if(this.titleSelect.selectDropdownLabel.innerHTML === "select" ){
     					domClass.add(errorNotation, "selectOptionError");
     				}
     			}
  	   		}));
        }

      return valid;
    },

    isValid: function () {
      if(this.infantNotBornCheckBox && this.infantNotBornCheckBox.checked)  {
        return true;
      }
      var valid = this.validateAndDisplayMessages();
      if (!valid) return false;
      for (var index = 0; index < this._attachPoints.length; index++) {
        if (this[this._attachPoints[index]].state && this[this._attachPoints[index]].state === "Error") {
          if(this.insDOBTextBox === this[this._attachPoints[index]] && !this.insDOBTextBox.isDisplayed ) {
              continue;
          } else {
            return false;
          }

        }
      }
      return true;

    }



  });
});