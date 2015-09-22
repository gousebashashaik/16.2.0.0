define("tui/searchResults/view/CruiseCalendarSearchResultsComponent", [
  "dojo/_base/declare",
  "dojo/_base/connect",
  "dojo/_base/lang",
  "dojo/parser",
  "dojo/aspect",
  "dojo/topic",
  "dojo/on",
  "dojo/_base/xhr",
  "dojo/mouse",
  "dojo/query",
  "dojo/dom-style",
  "dojo/dom-attr",
  "dojo/dom-construct",
  "dojo/dom-class",
  "dojo/dom-geometry",
  "dojo/text!tui/searchResults/view/templates/variantResultItemTmpl.html",
  "dojo/text!tui/searchResults/view/templates/noResultsPopupTmpl.html",
  "dojo/text!tui/searchResults/view/templates/ajaxErrorPopupTmpl.html",
  "dojo/text!tui/searchResults/view/templates/pageLoaderTmpl.html",
  "tui/searchResults/view/SearchResultsPaginator",
  "tui/searchResults/view/NoResultsPopup",
  "tui/searchResults/service/RoomGroupingService",
  "tui/searchResults/view/variants/VariantViewMapping",
  "tui/widget/popup/cruise/FacilityOverlay",
  "tui/searchResults/view/VariantSearchResultsComponent",
  "tui/widget/mixins/Templatable",
  "tui/widget/_TuiBaseWidget",
  "tui/search/nls/Searchi18nable"], function (declare, connect, lang, parser, aspect, topic, on, xhr, mouse, query,
                                              domStyle, domAttr, domConstruct, domClass, domGeom, resultTmpl, noResultsTmpl,
                                              ajaxErrorTmpl, loaderTmpl, Paginator, Modal, roomGrpSrvc, variantMappings, FacilityOverlay) {




  return declare("tui.searchResults.view.CruiseCalendarSearchResultsComponent", [tui.searchResults.view.VariantSearchResultsComponent], {

    // ----------------------------------------------------------------------------- properties

	  calenderDates: null,

	  CONF_MAX_OPENABLES: 6,

	  subscribableMethods: ["openMonth", "disableToggle"],

	  openedList: [],

    // ----------------------------------------------------------------------------- holiday rendering methods


	 postCreate: function () {
      // initialise search results view
      var resultsView = this,
          mediator = dijit.registry.byId('mediator'),
          model = mediator.registerController(resultsView, 'searchResult');
      resultsView.calenderDates = mediator.registerController(resultsView, 'calendarDates');
	  resultsView.inherited(arguments);

	  resultsView.siteName=mediator.originalModel.siteName;
	  resultsView.renderCruiseCalender(model);

	  resultsView.createMonthPlaceHolder();
	  parser.parse(resultsView.domNode);

     },

     renderCruiseCalender: function(model, clickedLi){
    	 var resultsView = this, noResultFlag = false;
    	 if(!_.size(model)){

    		 return;
    	 }
    	 var currItineryLen = query(" > li", resultsView.colNodes.listCol).length;
    	 _.each(model, function(date, index){

    		 if( noResultFlag || !date.holidays || !date.holidays.length  ){
    			 noResultFlag = true;

    		 }else{
    			 _.each(date.holidays, function(holiday, ind){
    				 holiday.variantType = "CRUISE_BROWSE_CALENDAR";
    				 if( !clickedLi && ind === 0 ){
    					 var date = dojo.date.locale.parse(holiday.sailings[0].durationInfo.departureDate, {datePattern: "dd MMM yyyy", selector: 'date'})
    					 var hashTarget = dojo.trim(dojo.date.locale.format(date,{selector: "date", datePattern: "MMM yyyy" })).replace(' ', '-');
    					 resultsView.openedList.push(hashTarget);
    				 }
    			 });
	 			resultsView.holidays = date.holidays;
	 			resultsView.totalHolidaysAvailable = date.endecaResultsCount;
	 			resultsView.renderSearchResults(date.holidays);
    		 }
    	 });

    	 if( !noResultFlag && clickedLi && _.size(model) === 1){

    		 var nodelist = query(" > li", resultsView.colNodes.listCol);
    		 var target = "#" + query(" > a ", clickedLi)[0].name;

    		 dojo.setStyle(clickedLi, {display: "none"});
    		 dojo.setAttr(clickedLi, "id", "destroyWidt");
    		 _.each(nodelist, function(itm, indx){
    			 if( indx > (currItineryLen -1 )){
    				 parser.parse(itm);
    				 domConstruct.place(itm, clickedLi, "after");
    				 clickedLi = itm;
    			 }
    		 });
    		 dojo.destroy("destroyWidt");
    		 location.href = target;
    	 }else if( noResultFlag ){
    		 domConstruct.destroy( query("img", clickedLi)[0] );
    		 domConstruct.place("<div class='image-with-content' align='center' >No sailings found for this month</div>", clickedLi, "last");
    	 }

    	 on.emit(window, "onscroll", {
				bubbles: true,
				cancelable: true
			});
     },

     onBeforePlaceHolidays: function (isPagination) {
         // Runs before holiday packages are placed in the dom
         var resultsView = this;
         //parser.parse(resultsView.domNode);
         if (!isPagination) {
           // destroy all child widgets
           resultsView.destroyResultWidgets();
         }

       },

       onAfterPlaceHolidays: function (isPagination) {
    	      // Runs after holidays have been rendered, after they are placed in the dom
    	      var resultsView = this;

    	      // re-attach pagination listener
    	//      resultsView.paginator.attach();
    	      resultsView.toggleLoaderNode(false);

    	      /* Shortlist not implemented */
    	      //resultsView.refreshShortlistedPackages();

    	      holidayPackages = query('.search-result-item', resultsView.domNode);
    	      var view = resultsView.getView();

	  		 query(".list-view-buttons", resultsView.domNode).removeClass("hide").style("display", "block");
	  		 query(".gallery-view-buttons", resultsView.domNode).addClass("hide").style("display", "none");

	  		 var clickedItem = sessionStorage.getItem("thClickedElement");
    	      if(clickedItem) {
    	    	  resultsView.scrollToPosition(clickedItem);
    	      }
    	      query("ul.plist li").addClass("search-result-item");
    	      domClass.remove(query(".search-results")[0], 'updating');


       },

       createMonthPlaceHolder: function(){
    	   var resultsView = this;
    	   var tmplPlaceHolderStr = '';
    	   resultsView.calenderDates.splice(0,3);
    	   _.each(resultsView.calenderDates, function(date, ind){
    		   var dateSplit = date.split("-");
    		   parsedDate = new Date(dateSplit[2], Number(dateSplit[1])-1, dateSplit[0]);
    		   var date1 = dojo.date.locale.format(parsedDate, {selector:"date", datePattern:"MMMM yyyy" });
    		   var month = dojo.date.locale.format(parsedDate, {selector:"date", datePattern:"MMM" });
    		   tmplPlaceHolderStr += '<li class="search-listing close" data-dojo-date="' + date + '"><a name="' + month +  '-'  + dateSplit[2] + '"></a>' +
					'<div class="month-title uc " id="' + month +'-'+ dateSplit[2] + '" style="float:left;width:100%;background:#fcb712;padding:2px 0;text-align:center;font-size:16.5px;color:#fff;  cursor: pointer;   margin-bottom: 2%;  line-height: 28px; font-weight: bold;">'+
						date1 + ' <i class="icon icon-arrow"></i>'+
						'</div></li>';

    	   });

    	   domConstruct.place(tmplPlaceHolderStr, resultsView.colNodes.listCol, "last");

       },

       initPaginator: function(mediator){
    	   var resultsView = this;

    	   resultsView.inherited(arguments);

    	   on(resultsView.domNode, "click", function(e){

    		   resultsView.openMonth(e.target);

    	   });
       },

       openMonth: function(monthNode){
    	   var resultsView = this;
    	   if( domClass.contains(monthNode, "month-title" ) &&  !domClass.contains(monthNode, "loading" ) ){
			   var li = query(monthNode).parent()[0];
			   if( ! query(".image-with-content", li).length ){
				   dojo.addClass(monthNode, "loading");
				   resultsView.fireRequest(li);
			   }
			   if( !domClass.contains( monthNode, "disable-toggle" ) ){
				   var monthDivId = resultsView.applyMonthCloseRule(monthNode.id);
				   if( monthDivId ){
					   if( monthNode != dojo.byId(monthDivId)){
						   var monthDiv = dojo.byId(monthDivId);
						   var action = domClass.contains(monthDiv, "open")? 'remove' : 'add';
		    			   domClass[ action ](monthDiv, "open");
						   resultsView.onMonthToggle(monthDiv );
					   }
				   }
				   var action = domClass.contains(monthNode, "open")? 'remove' : 'add';
				   domClass[ action ](monthNode, "open");
				   resultsView.onMonthToggle(monthNode);
			   }
			   domClass.remove( monthNode, "disable-toggle" );
		   }
    	   dojo.publish("tui.searchResults.view.CruiseCalendarSearchResultsComponent.openMonth");
       },

       applyMonthCloseRule: function(month){
    	   var resultsView = this, indx;
    	   if( (indx = _.indexOf(resultsView.openedList, month)) !== -1 ){
    		   resultsView.openedList.splice(indx,1)
    	   }else{
    		   resultsView.openedList.push(month);
    		   if( resultsView.openedList.length > resultsView.CONF_MAX_OPENABLES  ){
    			   return resultsView.openedList.shift();
    		   }

    	   }
    	   return null;
       },

       onMonthToggle: function(monthDiv){
    	   var li = query(monthDiv).parent()[0];
		   do{ //console.log(li);
			   var action = domClass.contains(li, "close") ? 'remove' : 'add' ;
			   domClass[ action ](li, "close");
			   //domClass[ action == "add" ?   'add' : 'remove' ](query(".month-title", li)[0], "open");

			   li = query(li).next()[0];

		   }
		   while( li && !query(".month-title", li).length)
       },

       disableToggle: function(monthDiv){
    	   var resultsView = this;
    	   if( domClass.contains( monthDiv, "open" ) ){
    		   domClass.add( monthDiv, "disable-toggle" );
    	   }

       },

       fireRequest: function(liObj){
    	   var resultsView = this;
    	   domConstruct.place(' <img src="/' + dojo.config.site + '/images/loading-3-anim-transparent.gif" width="40" style="margin-left: 350px;"/>', liObj, "last");
    	   var xhrReq = dojo.xhr.post({
    	          url: dojoConfig.paths.webRoot +  "/ws/calendar",
    	          content: {selectedDate: dojo.getAttr(liObj, "data-dojo-date") },
    	          handleAs: "json",
    	          load: function (response, options) {
    	        	  dojo.removeClass(query(".month-title", options.args.clickedLi)[0], "loading");
    	        	  var clickedLi = options.args.clickedLi;
    	        	  location.href = "#" + query(" > a ", clickedLi)[0].name
    	            resultsView.renderCruiseCalender(response.searchResult, clickedLi);
    	          },
    	          error: function (err) {
    	            console.log("AJAX failed!");
    	          },
    	          clickedLi: liObj
    	        });
       },

       destroyResultWidgets: function (preserveDom) {
	      var resultsView = this;
	   }




      });
});

