define ("tui/widget/customeraccount/EditNameEmailPopup", [
        													"dojo",											  	
        													"dojo/cookie",
        													"dojo/query",
        													"dojo/has",
        													"dojox/validate/web",
        													"tui/validate/check",
        													"dojo/_base/array",
        													"dojo/dom-style",
        													"dijit/focus",
        													"dojo/text!tui/widget/customeraccount/view/templates/EditName.html",
        													"dojo/text!tui/widget/customeraccount/view/templates/EditEmail.html",
															"dojo/_base/xhr",
															"dojo/dom-form",
        													"dojo/dom-class",
        													"dojo/topic",
        													"tui/widget/_TuiBaseWidget", 
        													"dojo/NodeList-traverse",
        													"dojo/NodeList-manipulate",
        													"dojox/dtl", 
        													"dojox/dtl/Context", 
        													"dojox/dtl/tag/logic",
        													"dijit/registry",
        													"tui/dtl/Tmpl",
        													"dojo/html",
        													"dojox/validate/us",
        													"tui/widget/mixins/Templatable",
															"tui/widget/customeraccount/ErrorHandling"
        													
        													
        							    			  ], function(dojo,cookie, query, has, validate,check, arrayUtil, domStyle, focusUtil, EditNameTmpl, EditEmailTmpl,xhr,domForm,domClass){

		dojo.declare("tui.widget.customeraccount.EditNameEmailPopup", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable, tui.widget.customeraccount.ErrorHandling], {
		tmpl: EditNameTmpl,	
        tuiWebrootPath:tuiWebrootPath,
		successUrl : null,
		commonSignIn:null,
		componentUid:null,
		onAfterTmplRender: function(){
			dojo.removeClass(this.domNode, 'updating');			 
		},
		showHidePopup:function(PopupId, type){
			var root = document.getElementsByTagName( 'html' )[0];
			if(type == "show"){
				this.resetForm();
				dojo.removeClass(PopupId, "hide");
				dojo.addClass(PopupId, "show");
				dojo.addClass(root, "modal-open");
			}
			else{
				dojo.removeClass(PopupId, "show");
				dojo.addClass(PopupId, "hide");
				dojo.removeClass(root, "modal-open");
			}
		},
		postCreate: function() {
			this.loadNameForm();
			this.loadEmailForm();
			this.attachEventListener();
			this.inherited(arguments);
		},
		loadNameForm:function(){
			var html = this.renderTmpl(EditNameTmpl,{tuiWebrootPath:tuiWebrootPath,firstName:this.firstName,familyName:this.familyName,email:this.email,cdmCustomerId:this.cdmCustomerId,componentUid: this.componentUid});           
            if (html) { 
                dojo.place(html,this.domNode,"only");
                //dojo.parser.parse(this.domNode);
            }
		},
		loadEmailForm:function(){
			var html = this.renderTmpl(EditEmailTmpl,{tuiWebrootPath:tuiWebrootPath,firstName:this.firstName,familyName:this.familyName,email:this.email,cdmCustomerId:this.cdmCustomerId});           
            if (html) { 
                dojo.place(html,this.domNode,"last");
                dojo.parser.parse(this.domNode);
            }
		},
		startup: function(){
			var editPop=this;
			query('.editName').onclick(function(e){
				dojo.stopEvent(e);
				editPop.showHidePopup(editNamepop,"show");
			});
			
			query('.editEmail').onclick(function(e){
				dojo.stopEvent(e);
				editPop.showHidePopup(editEmailpop,"show");
			});
			
			query("#editNamepop .cancel, #editNamepop .close").onclick(function(e){
				dojo.stopEvent(e);
				editPop.showHidePopup(editNamepop);
			});
			
			query("#editEmailpop .cancel, #editEmailpop .close").onclick(function(e){
				dojo.stopEvent(e);
				editPop.showHidePopup(editEmailpop);
			});
			
			query("#editnamepopForm").onsubmit(function(e){
				dojo.stopEvent(e);
				var form = this;
                var accountFileds = {
	             		   trim: ["email","password","firstName","lastName"],
	             		   required: ["email","password","firstName","lastName"],
	             		   constraints: {
	             			   email: [validate.isEmailAddress, false, true]
	             		   }
	             } ;				
                var results = validate.check(form, accountFileds);
                if(!results.isSuccessful()){
	        		dojo.query('input',form).forEach(function(inputElem){            		   
	        			  var field = inputElem.id;
	              		  focusUtil.focus(dojo.byId(field));
	              		  dojo.byId(field).blur();
	        		});
	        		//dojo.stopEvent(e);
	        		return false;
                }else{
                	if(editPop.validateForm(form)){
	            		editPop.submitForm(form);
	            	}
                }    
			});
			
			query("#editemailpopForm").onsubmit(function(e){
				dojo.stopEvent(e);
				var form=this;
				var accountFileds = {
	             		   trim: ["newEmailAddress","password","ReEnterEmail"],
	             		   required:["newEmailAddress","password","ReEnterEmail"],
	             		   constraints: {
	             			   email: [validate.isEmailAddress, false, true]
	             		   }
	             } ;
				 var results = validate.check(form, accountFileds);
				 if(!results.isSuccessful()){
		        		dojo.query('input',form).forEach(function(inputElem){            		   
		        			  var field = inputElem.id;
		              		  focusUtil.focus(dojo.byId(field));
		              		  dojo.byId(field).blur();
		        		});
		        		//dojo.stopEvent(e);
		        		return false;
	             }else{
	            	if(editPop.validateForm(form)){
	            		editPop.submitForm(form);
	            	}
	             }
				
			});
		},
		validateForm:function(form){
			var errorFlag=0;
			dojo.query("#"+form.id+" .row").forEach(function(node){
				if(domClass.contains(node, "error")){					
					errorFlag=1;			
				}
			});			
			if(errorFlag){
				return false;
			}
			return true;
		},
		resetForm:function(){
			var editPop=this;
			//reset editnamepopup form
			if(dojo.byId("editNamepop") != undefined){
				var container = dojo.byId("editnamepopForm");
				dojo.query('input', container).forEach(function(inputElem){           		   
					var field = inputElem.id;
					if(inputElem.type != "submit" && inputElem.type != "button"){
						focusUtil.focus(dojo.byId(field));
						if(inputElem.name=="firstName"){
							inputElem.value=editPop.firstName;
						}else if(inputElem.name=="lastName"){
							inputElem.value=editPop.familyName;
						}else if(inputElem.name=="email"){
							inputElem.value=editPop.email;
						}
						else if(inputElem.name=="cdmCustomerId"){
							inputElem.value=editPop.cdmCustomerId;
						}
						else{
							inputElem.value="";
						}
						dojo.byId(field).blur();
					}
				});
			}
			
			//reset editemailpopup form
			if(dojo.byId("editEmailpop") != undefined){
				var container = dojo.byId("editemailpopForm");
				dojo.query('input', container).forEach(function(inputElem){           		   
					var field = inputElem.id;
					if(inputElem.type != "submit" && inputElem.type != "button"){
						focusUtil.focus(dojo.byId(field));
						if(inputElem.name=="firstName"){
							inputElem.value=editPop.firstName;
						}else if(inputElem.name=="lastName"){
							inputElem.value=editPop.familyName;
						}else if(inputElem.name=="email"){
							inputElem.value=editPop.email;
						}
						else if(inputElem.name=="cdmCustomerId"){
							inputElem.value=editPop.cdmCustomerId;
						}
						else{
							inputElem.value="";
						}
						dojo.byId(field).blur();
					}
				});
			}
		},
		submitForm:function(form){
		
		    var editPop = this;
		    var res = editPop.handleSessionTimeOut();
			if(!res){
			return false;
			}
			
			xhr.post({
				url:form.action,
				form:dojo.byId(form.id),
				load:function(response){
					var obj=JSON.parse(response);
					if(obj[0] != undefined){
						var responsecode=obj[0].code;
						switch (responsecode)
						{
							case "PWD":
								var row=dojo.query("#"+form.id).children(".pwd");
								var fieldId=dojo.query("#"+form.id+" input[type='password']").attr("id")[0];
								row.removeClass("valid").addClass("error pwd");
								dojo.byId(fieldId+"Error_2").style.display="inline-block";
								dojo.byId(fieldId+"Error_3").style.display="none";
								dojo.byId(fieldId+"Error_1").innerHTML=obj[0].description;
								dojo.byId(fieldId+"Error_1").style.display="inline-block";
								return false;
								break;
							case "FAILURE":
								window.location = "../manageAccount/customerHome";
								localStorage.setItem("detailsupdate","error");
								break;
							case "SUCCESS":
								window.location = "../manageAccount/customerHome";
								localStorage.setItem("detailsupdate","updated");
								break;
							case "ACCOUNTALREADYEXIST":
								dojo.query("#emailalreadyexist").removeClass("hide").children('p').attr("innerHTML","<i class='caret warning'></i>"+obj[0].description);
								break;
						}
					}
				},
				error:function(errors){
					window.location = "../manageAccount/customerHome";
					localStorage.setItem("detailsupdate","error");
				}
			});
		},
		attachEventListener:function(){
			
		}
	});
	
	return tui.widget.customeraccount.EditNameEmailPopup;
});