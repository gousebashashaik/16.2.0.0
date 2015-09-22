define ("tui/widget/customeraccount/mybookings/linkMyBooking", [
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
													"dojo/dom-construct",
													"dojo/_base/xhr",
													"dojo/text!tui/widget/customeraccount/mybookings/templates/bookingListData.html",
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
													"tui/widget/mixins/Templatable"													
													
							    			  ], function(dojo,on,  cookie, query, has, validate,check, arrayUtil, domStyle, focusUtil, domConstruct,xhr, bookingTmpl){
        
		dojo.declare("tui.widget.customeraccount.mybookings.linkMyBooking", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable], {		
					
		postCreate: function() {
			var textNode = this;
			textNode.onSubmitEvent();
			textNode.linkPanelAccordian();
			textNode.removeErrorMessage();
			textNode.inherited(arguments);
		},
		
		onSubmitEvent: function() {
			var textNode = this;
			query("#myBookings").on("click", function(e){
				dojo.stopEvent(e);
				var bookId, bookYear;
				bookId = dojo.query("#book-id")[0].submitflag;
				bookYear = dojo.query("#book-year")[0].submitflag;
				if(bookId && bookYear) {
					textNode.getResponseData();						
				} else {
					textNode.showErrorMessage("All fields are mandatory");
				}
       	    });
		},
				
		linkPanelAccordian: function() {
			var widget = this;	
			var linkPanel = dojo.query('.another-booked')[0];	
			
			query(".caret.booked").on("click", function(e) {
				dojo.stopEvent(e);
				if(dojo.hasClass(linkPanel, "open")) {
					dojo.removeClass(linkPanel, "open");
				} else {
					dojo.addClass(linkPanel, "open");
				}
       	    });			
		},
		
		getResponseData: function() {
			var widget = this;
			var hide = { display:"none" };
			var show = { display:"block" };	
			var sortUrl	= tuiWebrootPath+"/customerBookings/linkmyBookings";
			var form = dojo.byId("getBookings");
			var querystr = "departureDate=" + dojo.byId("departure").value + "&bookingref=" + dojo.byId("book-id").value + "&shopRef=" + dojo.byId("book-year").value;
						
			dojo.removeClass(dojo.byId("hello"), "hide");
			dojo.setAttr(dojo.byId("hello"), "style", show);	
			
			xhr.post({				
				
				url: sortUrl +"?"+ querystr,
				preventCache: true,
				
				form: dojo.byId(form.id),
				
				load: function(response) {
					var bookingJson = JSON.parse(response);
					if(bookingJson.accessError == "") {
						if(bookingJson.previousCustomerBookingsViewData != "") {
							widget.bindLinkBooking(bookingJson.previousCustomerBookingsViewData, "previous");
						}
						if(bookingJson.currentCustomerBookingsViewData != "") {
							widget.bindLinkBooking(bookingJson.currentCustomerBookingsViewData, "current");
						}
					} else {
						widget.showErrorMessage(bookingJson.accessError);
					}
				},					
				error: function(errors) {
					searchResultsComponent.handleBackendError(errors);
				},					
				handle: function(response) {
					
					hasBeenSent = true;
					
					if(response){
						response = dojo.fromJson(response);
						if(dojo.byId("results") != undefined){
							wipeTarget = dojo.byId("results");
							wipeTarget.innerHTML = "";
							wipeTarget.style.display = 'none';							
							searchResultsComponent.renderSavedHolidaysList(response);						
							fx.wipeIn({ node: wipeTarget }).play();
							if(dojo.byId("shortlist") != undefined){				
								searchResultsComponent.hideLoader("shortlistresultsAvailable");
								searchResultsComponent.hideLoader("noLongerAvailableHolidays");
							}
						}
					}
				}
			});
			dojo.setAttr(dojo.byId("hello"), "style", hide);
			dojo.addClass(dojo.byId("hello"), "hide");			
		},
		
		bindLinkBooking: function(jsondata, bookingType) {
			var widget = this;	
			
			var bookingTemplate = dojo.query('.bookings.' + bookingType + '-bookings')[0];
			
			if(bookingTemplate != undefined ){
				console.log("bookingTemplate:",bookingTemplate);
				var html = widget.renderTmpl(bookingTmpl, {booking: jsondata});
				if (html) {
					//dojo.place(html, bookingTemplate.domNode, "last");
					//dojo.parser.parse(bookingTemplate.domNode); 
                    bookingTemplate.innerHTML += html;
				}
			}			
		},
		
		removeErrorMessage: function() { 
        	var textNode = this;
			var errorMessage = dojo.query("span.error");
			errorMessage[0].innerHTML = "";
	   	},
		
    	showErrorMessage:function(message) { 
    		var textNode = this; 
			var errorMessage = dojo.query("span.error");
			errorMessage[0].innerHTML = message;
	   	}
		
	
	});	
	return tui.widget.customeraccount.mybookings.linkMyBooking;
});