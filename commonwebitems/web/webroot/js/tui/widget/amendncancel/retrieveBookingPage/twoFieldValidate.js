define("tui/widget/amendncancel/retrieveBookingPage/twoFieldValidate", [
    "dojo",
	"dojo/dom-construct",
	"dojo/dom-class",
	"dojo/query",
	'dojo/on',
	"dojo/Stateful",
	"dojo/_base/declare",
	"dojo/text!tui/widget/amendncancel/retrieveBookingPage/templates/twoFieldValidate.html",
	"tui/widget/mixins/Templatable",
	"tui/widget/_TuiBaseWidget"], function (dojo, domConstruct, domClass, query, on, stateful, declare, twoFieldValidateTmpl){
	
	var fieldClass = declare([stateful], {
		shopVal: null,
		visionVal:null,
		
		//getters
		_shopValGetter: function(){
		  return this.shopVal;
		},
		_visionValGetter: function(){
		  return this.visionVal;
		},
		
		//setters
		_shopValSetter: function(fieldShopVal){
		  this.shopVal = fieldShopVal;
		},
		_visionValSetter: function(fieldVisionVal){
		  this.visionVal = fieldVisionVal;
		}
	});

    dojo.declare('tui.widget.amendncancel.retrieveBookingPage.twoFieldValidate', [tui.widget.mixins.Templatable, tui.widget._TuiBaseWidget], {
	
		twoFieldValidateTmpl: twoFieldValidateTmpl,
		
		reqMsg:"Please enter your booking reference",
		
		errMsg:"Please enter your correct booking reference",
		
		validationErrorMsg: "",
		
		required:true,
		
		validationRule:/^[0-9]+$/,
		
		baseDomNode:null,
		
		fieldObj : null,
		
		isLeftField : null,
		
		isRightField : null,
		
		placeholderShop:'2060',
		
		placeholderVision:'123456',
		
		fieldIdentifier: 'splitted',
		
		shopField: function() {
			return query(".shop", widget.domNode)[0];
		},
		
		visionField: function() {
			return query(".vision", widget.domNode)[0];
		},
		
		errDomNode: function() {
			return query(".validation-wrap", widget.baseDomNode)[0];
		},
		
		errorField: function() {
			return query(".invalid-copy", widget.baseDomNode)[0];
		},
		
		validatefield: function(val, field, iskeyup) {
			var widget = this;
			widget.validationErrorMsg ="";
			var status = false;
			
			//required case
			if(widget.required && !iskeyup) {
				status = (val !== "");
				if(!status) {
					widget.validationErrorMsg = widget.reqMsg;
					widget.showValMsg(status, field, iskeyup);
					return status;
				}
			}
			
			//invalid case
			status = (widget.validationRule.test(val));
			if(!status) {
				widget.validationErrorMsg = widget.errMsg;
			}
			widget.showValMsg(status, field, iskeyup);
			return status;
		},
		
		validate: function() {
			var widget = this;
			var flag_shop = widget.validatefield(widget.shopField().value,"shop", false);
			var flag_vision = widget.validatefield(widget.visionField().value, "vision", false);
			return flag_shop && flag_vision; 
		},
		
		attachEvents: function() {
			var widget = this;
			
			//Shop field
			on(widget.shopField(), "blur", function() {
				widget.fieldObj.set('shopVal', widget.shopField().value, false);
				if(widget.shopField().value == "" || widget.shopField().value == null){
					if(widget.validationErrorMsg !== widget.reqMsg){
						domClass.remove(query(".placeholder", widget.domNode)[0], "hide");
					}
				}else{
					domClass.add(query(".placeholder", widget.domNode)[0], "hide");
				}
			});
			on(widget.shopField(), "keyup", function() {
				widget.fieldObj.set('shopVal', widget.shopField().value, false);
				//widget.validatefield(widget.shopField().value,"shop", false);
			});
			on(widget.shopField(), "focus", function(e) {
				domClass.add(query(".placeholder", widget.domNode)[0], "hide");
			});
			on(query(".placeholder", widget.domNode)[0], "click", function(){
				domClass.add(query(".placeholder", widget.domNode)[0], "hide");
				widget.shopField().focus();
			});
			
			
			//Vision field
			on(widget.visionField(), "blur", function() {
				widget.fieldObj.set('visionVal', widget.visionField().value, false);
				if(widget.visionField().value == "" || widget.visionField().value == null){
					if(widget.validationErrorMsg !== widget.reqMsg){
						domClass.remove(query(".placeholder", widget.domNode)[1], "hide");
					}
				}else{
					domClass.add(query(".placeholder", widget.domNode)[1], "hide");
				}
			});
			on(widget.visionField(), "keyup", function() {
				widget.fieldObj.set('visionVal', widget.visionField().value, false);
			});
			on(widget.visionField(), "focus", function(e) {
				domClass.add(query(".placeholder", widget.domNode)[1], "hide");
			});
			on(query(".placeholder", widget.domNode)[1], "click", function(){
				domClass.add(query(".placeholder", widget.domNode)[1], "hide");
				widget.visionField().focus();
			});
		},
		
		showValMsg: function(valState, field, iskeyup) {
			var widget = this;
			
			if(valState===true) {
				if(!iskeyup) {
					//console.log("success "+field);
					if(domClass.contains(widget.errDomNode(), field+'-invalid')) {
						domClass.remove(widget.errDomNode(), field+'-invalid');
					}
					domClass.add(widget.errDomNode(), field+'-valid');
				}
			}
			else {
				//console.log("error");
				if(domClass.contains(widget.errDomNode(), field+'-valid')) {
					domClass.remove(widget.errDomNode(), field+'-valid');
				}
				domClass.add(widget.errDomNode(), field+'-invalid');
				widget.errorField().innerHTML=widget.validationErrorMsg;
				//if(field == "shop"){
					domClass.add(query(".placeholder", widget.domNode)[0], "hide");
				//}
				//if(field == "vision"){
					domClass.add(query(".placeholder", widget.domNode)[1], "hide");
				//}
				return;
			}
		},
		
		reset: function(fieldClassName) {
			var widget = this;
			//console.log("reset");
			if(domClass.contains(widget.errDomNode(), fieldClassName+"-valid")) {
				domClass.remove(widget.errDomNode(), fieldClassName+"-valid");
			}
			if(domClass.contains(widget.errDomNode(), fieldClassName+"-invalid")) {
				domClass.remove(widget.errDomNode(), fieldClassName+"-invalid");
			}
		},
		
		renderTemplate: function() {
			var widget = this;
			var html = widget.renderTmpl(widget.twoFieldValidateTmpl, widget);
			return domConstruct.place(html, widget.domNode, "only");
		},
		
		createFieldObj: function(initialValue) {
			var fieldObj = new fieldClass({value: initialValue});
			return fieldObj;
		},
		
		postCreate: function() {
			var widget = this;
			widget.baseDomNode = widget.renderTemplate();
			widget.fieldObj = widget.createFieldObj("");
			widget.attachEvents();
			widget.fieldObj.watch("shopVal", function(name, oldValue, newValue){
				widget.validatefield(widget.fieldObj.shopVal,"shop", false);
			});
			widget.fieldObj.watch("visionVal", function(name, oldValue, newValue){
				if(oldValue !== null){
					widget.validatefield(widget.fieldObj.visionVal, "vision", false);
				}
			});
		}
	});
	
	return tui.widget.amendncancel.retrieveBookingPage.twoFieldValidate;
});