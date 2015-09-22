define('tui/widget/amendncancel/bookingSearchResultsComponent', [
  "dojo/_base/declare",
  "dojo/on",
  "dojo/dom-attr",
  "dojo/_base/lang",
 "dojo/dom-style",
 "dojo/dom",
  "dojo/dom-class",
  "tui/widget/_TuiBaseWidget",
  "dojox/dtl/_Templated",
  "tui/widget/mixins/Templatable",
  "dojo/text!tui/widget/amendncancel/templates/bookingSearchResultComponent.html",
   "dojo/dom-construct",
   "dojo/json",
   "dojo/query",
   "dojo/_base/xhr",
   "dojo/dom-geometry",
   "tui/mixins/MethodSubscribable",
   "tui/widget/popup/Tooltips",
   "dijit/focus",
   "tui/widget/form/ValidationTextBox"
   
     ], function(declare, on, domAttr , lang, domStyle, dom,  domClass,  _TuiBaseWidget, _Templated, 
           Templatable, template, domConstruct,JSON,query,xhr,domGeom, MethodSubscribable,Tooltips, focusUtil) {

           return declare('tui.widget.amendncancel.bookingSearchResultsComponent', [_TuiBaseWidget, _Templated, Templatable, MethodSubscribable], {	
                                                           	
            templateString: template, 
                                                           		
           widgetsInTemplate:true,
           
         //Properties of MethodSubscribable
     	   subscribableMethods: ["renderBookingResults"],
           
     	   templateview:"",
           bookingsSummary: null,
           searchedDate:null,
           searchedSurName:null,
           
           pages:null,
           maxNoOfPages:null,
           myNode:null,
           maxSize:null,
           currentPage:1,
           first:1,
           last:1,
           
           postCreate: function() {
	           this.inherited(arguments);
	           booking = this; 
	           
           },
           
           renderBookingResults: function(var1){
        	   if(var1.resultsAvailable){
        		   //noOfPages = Math.ceil(var1.totalNoOfResults/var1.offset);
        		   var noOfPages = Math.ceil(var1.totalNoOfResults/var1.offset);
        		   booking.updatePages(noOfPages, var1.maxNoOfPages);
        		   booking.bookingsSummary = var1.bookingsSummary;
        		   
        		   booking.highlight(booking.bookingsSummary);
                   
        		   booking.onePage = noOfPages;
        		   booking.templateview = "results";
            	   //Re render Template
            	   booking.refreshResults();
            	   on(query('.search-submit.bookref'), "click" , booking.getBooking);
            	   dojo.connect(dojo.query('.pagination-area')[0],"onclick", booking.pagination);
            	   //enable rightarrow if more pages
        		   if (noOfPages > 1) {
        			   domClass.remove(dojo.query("#rightarrow .pagination", booking.domNode)[0], "disabled");
        			   domClass.remove(dojo.query("#rightarrow", booking.domNode)[0], "disable");
        			   domClass.add(dojo.query("#rightarrow", booking.domNode)[0], "next");
        		   } 
				   if (var1.totalNoOfResults >1)
				   {
            	   dojo.query('.searchresult-text')[0].innerHTML = var1.totalNoOfResults + "  Search results found"
				   }else{
				   dojo.query('.searchresult-text')[0].innerHTML = var1.totalNoOfResults + "  Search result found"
				   }
            	   dojo.parser.parse(booking.domNode);
        	   }else{
        		   //Error
        		   booking.templateview = "noResults";
        		   booking.searchedDate = dojo.byId("date").value;
        		   booking.searchedSurName = dojo.byId("psurname").value;
        		   booking.refreshResults();
        		   
        		   dojo.connect(dojo.byId("error-new-search"), "onclick", function(evt){
        			   if(dojo.query(".unabletofind-booking")[0]){
        					  dojo.query(".unabletofind-booking")[0].style.display = "none";
        				  }
        			   dojo.addClass(dojo.byId("psurname"), "highlight-border-color");
        			   focusUtil.focus(dojo.byId('psurname'));
        			   dojo.publish("tui.widget.amendncancel.SearchBookingComponentPanel.clearSearchFields");
        		   });
        	   }
        	  
           },
           
           updatePages:function(pages, maxNoOfPages){
        	   first=1;
        	   booking.pages = pages;
        	   booking.maxNoOfPages = [];
        	   var pageLength = 0;
        	   if(pages > maxNoOfPages) {
        		   pageLength = maxNoOfPages;
        	   }
        	   else {
        		   pageLength = pages;
        	   }
        	   for(i=1;i<=pageLength;i++) {
                   booking.maxNoOfPages.push(i);
               }
        	   last = pageLength;
        	   booking.currentPage = 1;
           },
           
           
           updateNextPages:function(pages, maxNoOfPages){
        	   if(parseInt(booking.currentPage) == last) {
        		   booking.pages = pages;
            	   booking.maxNoOfPages = [];
        		   last= last+1; 
        		   first = first+1;
        		   for(i=first;i<=last;i++){   
              		 booking.maxNoOfPages.push(i);
          		   } 
        	   }
        	   booking.currentPage = parseInt(booking.currentPage)+1;
           },
           
           
           updatePrevPages:function(pages, maxNoOfPages){
        	   if(parseInt(booking.currentPage) == first) {
        		   booking.pages = pages;
            	   booking.maxNoOfPages = [];
        		   last= last-1; 
        		   first = first-1;
        		   for(i=first;i<=last;i++){   
              		 booking.maxNoOfPages.push(i);
          		   } 
        	   }
        	   booking.currentPage = parseInt(booking.currentPage)-1;
           },
           
           refreshResults:function(){
        	   html = booking.renderTmpl(booking.templateString,booking);
        	   booking.domNode.innerHTML = html;
           },
           
           pagination:function(evt){
        	   var page = evt.target.innerHTML;
        	   clickEvent = evt.target.parentElement.className;
        	   if(clickEvent === "disable"){
        		   return;
        	   }
        	   /*if(page === "&gt;" && booking.pages[booking.pages.length - 1] > booking.currentPage){
        		   page = parseInt(booking.currentPage) + 1;
        	   }*/
        	   
        	   if(clickEvent === "next"){
        		   page = parseInt(booking.currentPage) + 1;
        		  // var currentPage = parseInt(booking.currentPage);
        		   var noOfPages = Math.ceil(var1.totalNoOfResults/var1.offset);
        		   booking.updateNextPages(noOfPages, var1.maxNoOfPages);
        	   }
        	   
        	   if(clickEvent === "prev"){
        		   page = parseInt(booking.currentPage) - 1;
        		   var noOfPages = Math.ceil(var1.totalNoOfResults/var1.offset);
        		   booking.updatePrevPages(noOfPages, var1.maxNoOfPages);
        	   }
        	   
        	   booking.getNextPage(page);
        	   
        	   
           },
           
           getNextPage:function(page){
        	   var myNode = dojo.byId("content");
        	   var margin = domGeom.getMarginBox(myNode);
				var grayedArea = dojo.query('.amend-loading')[0];
				grayedArea.style.display = "block";
				domGeom.setMarginBox(grayedArea, {w: margin.w, h: margin.h});
        	   var results = xhr.post({
		             url: "search",
		             content: {
		            	 departureDate: dojo.byId("date").value,
		            	 surName: dojo.byId("psurname").value,
		            	 first: page
		             },
		             
		             handleAs: "json",
		             async: true,
		             headers: {Accept: "application/javascript, application/json"},
		             load:function(resp){
		            	 
		            	   dojo.query('.amend-loading')[0].style.display = "none";
		            	   booking.currentPage = page;
		            	   booking.bookingsSummary = resp.bookingsSummary;
		            	   
		            	   booking.highlight(booking.bookingsSummary);
		        		   booking.templateview = "results";	
		        		   booking.refreshResults();
		        		   selectedpage = parseInt(booking.currentPage);
		        		   var countpages = Math.ceil(resp.totalNoOfResults/resp.offset);
		        		   if (selectedpage < countpages) {
		        			   domClass.remove(dojo.query("#rightarrow .pagination", booking.domNode)[0], "disabled");
		        			   domClass.remove(dojo.query("#rightarrow", booking.domNode)[0], "disable");
		        			   domClass.add(dojo.query("#rightarrow", booking.domNode)[0], "next");
		        		   } 
		        		   if (selectedpage != 1) {
		        			   domClass.remove(dojo.query("#leftarrow .pagination", booking.domNode)[0], "disabled");
		        			   domClass.remove(dojo.query("#leftarrow", booking.domNode)[0], "disable");
		        			   domClass.add(dojo.query("#leftarrow", booking.domNode)[0], "prev");
		        		   }
		            	   dojo.connect(dojo.query('.pagination-area')[0],"onclick", booking.pagination);
		            	   dojo.query('.searchresult-text')[0].innerHTML = resp.totalNoOfResults + "  Search result found";
		            	   dojo.parser.parse(booking.domNode);
		            	 },
		             error: function (err) {
		            	 dojo.query('.amend-loading')[0].style.display = "none";
		                 alert(err);
		             }
		         });
           },
           
           getBooking: function(evt){
        	   query('.retrieve-booking .bookingRefereneceId')[0].value = domAttr.get(evt.target, "data-bookref");
        	   query('.retrieve-booking')[0].submit();
           },
         
           //highlights the SurName
           highlight: function(bookingSummary){
        	   _.each(bookingSummary, function(booking) { 
    			   text = "Mr FirstName " + booking.leadPassengerName ;
    			   typedText = dojo.byId("psurname").value;
    			   booking.leadPassengerName = text.replace(new RegExp('(' + typedText + ')', 'gi'), '<strong>$1</strong>')
    		   });
           }
           
       });
  return tui.widget.amendncancel.bookingSearchResultsComponent;
  
});