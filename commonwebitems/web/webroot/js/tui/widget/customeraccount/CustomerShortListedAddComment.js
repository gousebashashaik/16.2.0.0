define ("tui/widget/customeraccount/CustomerShortListedAddComment", 
		["dojo",
		 "dojo/on",
		 "dojo/cookie",
		 "dojo/query",
		 "dojo/has",
		 "dojox/validate/web",
		 "tui/validate/check",
		 "dojo/_base/array",
		 "dojo/dom-style",
		 "dijit/focus",
		 "dojo/dom-construct",
		 "dojo/topic",                                               
		 "tui/widget/_TuiBaseWidget", 
		 "dojo/NodeList-traverse",
		
		 "dojox/dtl", 
		 "dojox/dtl/Context", 
		 "dojox/dtl/tag/logic",
		 "dijit/registry",
		 "tui/dtl/Tmpl",
		 "dojo/html",													
		 "dojox/validate/us",
		 "dojo/dom-geometry",
		 "dojo/dom",
		 "tui/widget/customeraccount/ErrorHandling"	 
		],
		function(dojo, on, cookie, query, has, validate,check, arrayUtil, domStyle, focusUtil, domConstruct,domGeom,dom){
        
		dojo.declare("tui.widget.customeraccount.CustomerShortListedAddComment", [tui.widget._TuiBaseWidget, tui.widget.customeraccount.ErrorHandling], {
			/* Component custom attributes */
			commentTextArea: null,
			      countSpan: null,
			  maxLength: 500, // Default max length
	    wishlistEnrtyId: null,
		buttonText:null,
		className:null,
		    constructor: function() {
				// add here anything that will be executed in the widget initialization.
			},
			removeClassName:function(elem, name){
						var remClass = elem.className;
						var re = new RegExp('(^| )' + name + '( |$)');
						remClass = remClass.replace(re, '$1');
						remClass = remClass.replace(/ $/, '');
						elem.className = remClass;
			},
			manageWindow: function(type){
				var self = this;
				var someNode = dojo.query(".modal").attr("class");
				var root = document.getElementsByTagName( 'html' )[0];
				
				if(type === "SHOW"){
					
					var node = dojo.byId("custNewComment_"+self.wishlistEnrtyId);
					var spanInfo = dojo.position(node, true);
					
					if(dojo.byId("customer-Comment") != undefined){
						dojo.addClass("customer-Comment", "show");
					}
					dojo.addClass(root, "modal-open");
				}
				else if(type === "HIDE"){					
					if(dojo.byId("customer-Comment") != undefined){
						dojo.removeClass("customer-Comment", "show");
						dojo.addClass("customer-Comment", "hide");
					}
					dojo.removeClass(root, "modal-open");
				}
				
			},
			postCreate: function() {
				this.commentTextArea = dojo.byId("comment");
				this.countSpan = dojo.byId("textCharCount");
				this.attachCustomEvents();
			},
			attachCustomEvents: function(){
				var self = this;
				dojo.query(".title .close").connect("onclick", function(){ console.log("called");
					self.manageWindow("HIDE");
				});
				
				
				
				dojo.query(".customer-comments").connect("onclick", function(evt){ 
					self.wishlistEnrtyId = dojo.query(this).attr("name");
					
					dojo.query('[name='+self.wishlistEnrtyId+']').forEach(function(commentNode){
						if(dojo.trim(commentNode.innerHTML)=="Add a comment"){
							self.commentTextArea.value = "";
							self.handleButtonDisableEnable(self.commentTextArea);
							self.check_length(self.commentTextArea);
						}else{
							var commenttext=query('#'+commentNode.id).next()[0];
							self.handleButtonDisableEnable(commenttext);
							self.check_length(commenttext);
						}
						
						if(dojo.trim(commentNode.innerHTML)!="Add a comment"){
							var enteredValue=query('#'+commentNode.id).next()[0].innerHTML;
							if(enteredValue != undefined){
								self.commentTextArea.value = enteredValue;
							}
							self.handleButtonDisableEnable(self.commentTextArea);
						}
					});
					
					/*if(dojo.byId("custNewComment_"+self.wishlistEnrtyId).innerHTML == "Add a comment"){
						self.commentTextArea.value = "";
						self.handleButtonDisableEnable(self.commentTextArea);
						self.check_length(self.commentTextArea);
					}
					else{
						var commenttext = dojo.byId("_custNewComment_"+self.wishlistEnrtyId);
						self.handleButtonDisableEnable(commenttext);
						self.check_length(commenttext);
					}*/
										
					
					//retain the entered value in text area
					
					/*if(dojo.byId("custNewComment_"+self.wishlistEnrtyId).innerHTML != "Add a comment"){
						var enteredValue = dojo.byId("_custNewComment_"+self.wishlistEnrtyId).innerHTML;
						
						if(enteredValue != undefined){
						self.commentTextArea.value = enteredValue;
						}
						self.handleButtonDisableEnable(self.commentTextArea);
					}*/
					self.manageWindow("SHOW");
					evt.preventDefault(); // To stop scrolling up after user clicks on hyperlink.
				});
				
				// Attaching onkeydown and onkeyup events to the textarea		
				dojo.connect(self.commentTextArea, "onkeydown", function (evt) {
					self.check_length(this);
					
				});	
				dojo.connect(self.commentTextArea, "onkeyup", function (evt) {
					self.check_length(this);
					self.handleButtonDisableEnable(this);
				});
				dojo.query(".comment-btn").connect("onclick", function(evt){
					dojo.attr(dojo.byId("custCommentForm"), "action", self.saveNoteURL);
					
                    /*					
					var res = self.handleSessionTimeOut();
					if(!res){
					return false;
					}
					*/
					dojo.xhr.post({
						form: "custCommentForm",
						content: { "comments": self.commentTextArea.value, "wishlistEnrtyId": self.wishlistEnrtyId },
						timeout: 3000,
				        load: function(data){
							
				        	var resJons = dojo.fromJson(data);
				            if(resJons.Status == "true"){
				            	var txt = self.commentTextArea.value;
				            	//manju
								console.log("button:"+self.buttonText);
								if(self.buttonText){								
									txt = self.buttonText;
									dojo.byId("custNewComment_"+self.wishlistEnrtyId).innerHTML = txt;
									dojo.byId("text_"+self.wishlistEnrtyId).innerHTML = self.commentTextArea.value;
									dojo.byId("custNewComment_"+self.wishlistEnrtyId).className = self.className;
								}
								else{
									txt = '<i class="bubble"></i>View/edit comment';
									//dojo.byId("custNewComment_"+self.wishlistEnrtyId).innerHTML = txt;
									dojo.query('[name='+self.wishlistEnrtyId+']').forEach(function(commentNode){
										commentNode.innerHTML=txt;
									});
				            	}
								
								dojo.query('[name='+self.wishlistEnrtyId+']').forEach(function(commentNode){
									query('#'+commentNode.id).next()[0].innerHTML = self.commentTextArea.value;
								});
								
				            	//dojo.byId("_custNewComment_"+self.wishlistEnrtyId).innerHTML = self.commentTextArea.value;
				            	
				            	//handle buttons enable/disable
				            	self.handleButtonDisableEnable(self.commentTextArea);
				            	self.check_length(self.commentTextArea);
				            	
				            }
				            self.manageWindow("HIDE");
				        },
						error: function(errors) {
							console.log("inside errors");
							self.handleBackendError(errors);
							
						}	
				    });
				});
				dojo.query(".deleteCMT").connect("onclick", function(evt){
					dojo.attr(dojo.byId("custCommentForm"), "action", self.deleteNoteURL);
					
					/*
					var res = self.handleSessionTimeOut();
					if(!res){
					return false;
					}
					*/
					dojo.xhr.post({
						form: "custCommentForm",
						content: { "comments": self.commentTextArea.value, "wishlistEnrtyId": self.wishlistEnrtyId},
						timeout: 3000,
				        load: function(data){
							
				        	if(data == "true"){
				        		dojo.query('[name='+self.wishlistEnrtyId+']').forEach(function(commentNode){
				        			commentNode.innerHTML='Add a comment';
				        			query('#'+commentNode.id).next()[0].innerHTML="";
				        		});
				        		
				        		//dojo.byId("custNewComment_"+self.wishlistEnrtyId).innerHTML = 'Add a comment';
				        		//dojo.byId("_custNewComment_"+self.wishlistEnrtyId).innerHTML = "";
				        		self.commentTextArea.value = "";
				        		self.handleButtonDisableEnable(self.commentTextArea);
				        		self.check_length(self.commentTextArea);
								if(self.buttonText){
									dojo.byId("text_"+self.wishlistEnrtyId).innerHTML = "";
									dojo.byId("custNewComment_"+self.wishlistEnrtyId).className = "add-comment customer-comments";
								}
				        	}
				        	self.manageWindow("HIDE");
				        },
						error: function(errors) {
							console.log("inside errors");
							self.handleBackendError(errors);
							
						}
				    });
				});
			},
			
			handleButtonDisableEnable: function(txtArea){
				
				var txt = txtArea.value;
				if(txt == undefined){
					var txt = txtArea.innerHTML;
				}
				var len = parseInt(txt.length, 10);		
				
				if(len > 0){ 
            		if(dojo.byId("deleteCMT") != undefined && dojo.byId("deleteCMT__Disabled") != undefined ){
            			dojo.byId("deleteCMT__Disabled").style.display = "none";
	            		dojo.byId("deleteCMT").style.display = "block";	 
            		}
            	}
            	else{ 
            		if(dojo.byId("deleteCMT") != undefined && dojo.byId("deleteCMT__Disabled") != undefined ){ 
	            		dojo.byId("deleteCMT__Disabled").style.display = "block";
	            		dojo.byId("deleteCMT").style.display = "none";
            		}
            	}
				
			},
			check_length: function(txtArea){
				var txt = txtArea.value;
				if(txt == undefined){
					var txt = txtArea.innerHTML;
				}
				var len = ( this.maxLength);
				
				if ( txt.length > len ) // Reached the Maximum length so trim the textarea
					txtArea.value = txt.substring(0, len);
				else
					this.countSpan.innerHTML = txt.length;
			}
		});
		return tui.widget.customeraccount.CustomerShortListedAddComment;
});