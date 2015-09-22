define ("tui/widget/customeraccount/AccountMenu", [
													"dojo",	
													"dojo/on",
													"dojo/cookie",
													"dojo/query",
													"dojo/has",
													"dojox/validate/web",
													"tui/validate/check",
													"dojo/_base/array",
													"dojo/dom-style",
													"dijit/focus",
													"dojo/fx",
													"dojo/touch",
													"dojox/gesture/tap",
													"dojo/topic",                                               
													"tui/widget/_TuiBaseWidget", 
													"dojo/NodeList-traverse",
												
													"dojox/dtl", 
													"dojox/dtl/Context", 
													"dojox/dtl/tag/logic",
													"dijit/registry",
													"tui/dtl/Tmpl",
													"dojo/html",
													"dojox/validate/us"
													
													
							    			  ], function(dojo, on, cookie, query, has, validate, check, arrayUtil, domStyle, focusUtil, fx,touch,tap){

		dojo.declare("tui.widget.customeraccount.AccountMenu", [tui.widget._TuiBaseWidget], {
				
		postCreate: function() {
			this.attachMenuEvents();
			this.handleLogoutMessageOnHomepage();
			this.handleBackButtonClick();
		},
		handleBackButtonClick:function(){
		
			window.onpageshow = function(event) {
				if (event.persisted) {
					window.location.reload() 
				}
			};	
			
			if(dojo.byId("CAbackButtonhandling") != undefined ){
				var e=dojo.byId("CAbackButtonhandling");
				if(e.value=="no"){
				e.value="yes";
				}else{
					e.value="no";
					var loc = document.location.toString();
					if(loc.indexOf("login") != -1){
						var url = tuiWebrootPath+"/customer-account/login?cache="+Math.random();
						window.location.assign(url);
						return false;
					}
				}
			}
            
		
		},
		handleLogoutMessageOnHomepage:function(){
		    var localStoragename = tuiSiteName+"_loggedOut";
			dojo.query(".sign-out .CASignOut").on("click", function(e){	
				localStorage.setItem(localStoragename,"1");
			});
			
			var st = localStorage.getItem(localStoragename);
			if(st == 1 || st == "1"){
				dojo.query("#menu .status .dialog").attr("class","dialog show");
				setTimeout(function(){			
				dojo.query("#menu .status .dialog").attr("class","dialog showx");
				localStorage.setItem(localStoragename,"");
				},30000);	
			}
			else{
				dojo.query("#menu .status .dialog").attr("class","dialog showx");
			}
			
					
		},
		startup:function(){
			this.modalInit();
		},
		attachMenuEvents: function(){
			var self = this;
			//Quickfix: below code is applicable for all the forms in customer account
			dojo.query('[type="submit"]').forEach(function(submitbtton){
            dojo.connect(submitbtton,"mousedown,keydown,touch.press,tap",function(e){
            	dojo.stopEvent(e);
            	e.cancelBubble = true;
            	e.stopPropagation();
            	return false;
            });			
			});
			 var cancel=dojo.query('.cancelit .cancel')[0];
            dojo.connect(cancel,"mousedown,keydown,touch.press,tap",function(e){
            	dojo.stopEvent(e);
            	e.cancelBubble = true;
            	e.stopPropagation();
            	return false;
            });
			//Quickfix: ends here
			self.applyDialogActions(".account-icon",".account-bar-submenu");
		},
		applyDialogActions: function(iconlnk,accountinnr){
			var signLink = dojo.query(iconlnk);
			var wipeTarget = dojo.query(accountinnr);
			signLink.on("click", function(e){
				dojo.stopEvent(e);
				dojo.toggleClass(wipeTarget[0],'show');
			});
		},
		modalFix:function(modal){
			var modal=dojo.query('#'+modal);
			modal.style({'position':'fixed','top':'0','margin':'0'});
			modal.children('.window').style({'position':'fixed','top':'0','margin':'0'});
			window.scroll(0,0);
		},
		modalInit:function(){
			var self = this;
			query('.modal').forEach(function(modal){
				if( /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent) ){
					query('input',modal).on('blur',function(){
						self.modalFix(modal.id);
					});
				}
			});
		}
	});
	
	return tui.widget.customeraccount.AccountMenu;
});