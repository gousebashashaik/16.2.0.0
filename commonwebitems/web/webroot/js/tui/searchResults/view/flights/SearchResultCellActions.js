define("tui/searchResults/view/flights/SearchResultCellActions", [
	  "dojo",
	  "dojo/has",
	  "dojo/on",
	  'dojo/query',
	  "dojo/io-query",
	  "dojo/parser",
	  "dojo/_base/connect",
	  "tui/search/store/SearchPanelMemory"
	  ], function (dojo, has, on, query, ioQuery, parser, connect, SearchPanelMemory) {
	dojo.declare("tui.searchResults.view.flights.SearchResultCellActions", [], {

		/******* Properties *******/

		postCreate:function(){
			var searchResultCellActions = this;
		},

		mouseOverCellhiglight : function(){
			 var searchResultCellActions = this;
			dojo.query(".searchResultTable tr td.flex-grid-cell").on("mouseover,mouseout",function(tdElem){
				var closestTd = dojo.query(tdElem.target).closest("td")[0];
				if(searchResultCellActions.savedSearch.returnDate){
			 		//Get Selected cells of the top rows


			 		dojo.query(closestTd.parentNode).prevAll().children(':nth-child(' + (closestTd.cellIndex+1) + ')').toggleClass("hover");
			 		//Get Selected cells of the left td's
			 		dojo.query(closestTd).prevAll().toggleClass("hover");

			 		if(dojo.query(closestTd).children(".srTdcnt").length !== 0) {
			 			if(!dojo.hasClass(closestTd,"srDefault") && !dojo.hasClass(closestTd,"srClicked"))
			 			dojo.query(closestTd).toggleClass("srBodySelectedDate");
			 		} else {
			 			dojo.query(closestTd).toggleClass("hover");
			 		}
			 		dojo.query(".SRdeptTblHeader").removeClass("hover");
			 		if(closestTd.parentNode.rowIndex !== 1){
			 			dojo.toggleClass(dojo.query(".searchResultTable tr")[closestTd.parentNode.rowIndex+15].children[0],"hover");
			 		}else {
			 			dojo.toggleClass(dojo.query(".searchResultTable tr")[closestTd.parentNode.rowIndex+8].children[1],"hover");
			 		}
				} else {
					if(dojo.query(closestTd).children(".srTdcnt").length !== 0){
						if(!dojo.hasClass(closestTd,"srDefault") && !dojo.hasClass(closestTd,"srClicked") && !dojo.hasClass(closestTd,"blank-cell")  )
						dojo.query(closestTd).toggleClass("srBodySelectedDate");
					}
				}
		 		});
		},

		  highlightselectedCell : function(tdObj){
			  var searchResultCellActions = this;
			  if(dojo.hasClass(tdObj,"SRrettTblHeader") || dojo.hasClass(tdObj,"SRdeptTblHeader") || dojo.hasClass(tdObj,"noResultDiv") || dojo.hasClass(tdObj,"searchResultDeptDatesTd") || dojo.hasClass(tdObj,"caption-left")) return;
			  if(tdObj.children.length < 1) return;

			  if(!dojo.query(".srTdcnt",tdObj) || dojo.hasClass(tdObj, "arrowstyle") || dojo.hasClass(tdObj, "blank-cell")) return;

			  if(dojo.query(".srBodySelectedDate",searchResultCellActions.domNode).length > 0){
				  var selectedCells = dojo.query(".srBodySelectedDate",searchResultCellActions.domNode);
				  for(var i=0; i< selectedCells.length; i++){
					  dojo.query(".srBodySelectedDate",searchResultCellActions.domNode).removeClass("srBodySelectedDate srClicked srDefault");
				  }

			  }


			  dojo.addClass(tdObj,"srBodySelectedDate srClicked");
			  if(searchResultCellActions.savedSearch.returnDate){
				  searchResultCellActions.highlightActiveCells(tdObj);
			  }
		  },


		  highlightActiveCells: function(tdObj){
			var searchResultCellActions = this;
			dojo.query("#searchResultContainer .active",searchResultCellActions.domNode).removeClass("active");

			dojo.query(tdObj.parentNode).prevAll().children(':nth-child(' + (tdObj.cellIndex+1) + ')').addClass("active");
			dojo.query(tdObj).prevAll().addClass("active");

			dojo.query(".SRdeptTblHeader").removeClass("active");

			if(tdObj.parentNode.rowIndex !== 1){
				dojo.toggleClass(dojo.query(".searchResultTable tr")[tdObj.parentNode.rowIndex+15].children[0],"active");
			}else {
				dojo.toggleClass(dojo.query(".searchResultTable tr")[tdObj.parentNode.rowIndex+8].children[1],"active");
			}

		  },

		  highlightMiddleCells: function(){
			  var searchResultCellActions = this,tdObj;

				  if(dojo.query(".srBodySelectedDate",searchResultCellActions.domNode).length === 0 && dojo.query(".noResultDiv",searchResultCellActions.domNode).length === 0){
					  return;
				  } else {
					  	if(dojo.query(".srBodySelectedDate",searchResultCellActions.domNode)[0] !== undefined){
						  tdObj = dojo.query(".srBodySelectedDate",searchResultCellActions.domNode)[0];
				  		} else {
				  			tdObj = dojo.query(".noResultDiv",searchResultCellActions.domNode)[0];
				  		}
				  }
				  if(searchResultCellActions.savedSearch.returnDate){
					  searchResultCellActions.highlightActiveCells(tdObj);
				  }
				  searchResultCellActions.getPreviousFlightDetails(tdObj);

		  },


		    getPreviousSelectedCellID : function(){
				var searchResultCellActions = this,compId=[],Itinerary,properties,
				  	tdObj = dojo.query(".srBodySelectedDate",searchResultCellActions.domNode)[0];
					if(tdObj){

						for(var i=0; i < tdObj.children.length; i++ ){
							var child = tdObj.children[i];
							compId.push(child.id);
						}

						Itinerary = searchResultCellActions.resultsGridStore.get(tdObj.children[0].id);

						if(searchResultCellActions.savedSearch.returnDate){
							properties = {
								 	compId: compId,
								 	selectedDeptDate: Itinerary.outbound.depDate,
							        selectedRetuDate : Itinerary.inbound.depDate
							      };
						}else{
							properties = {
								 	compId: compId,
								 	selectedDeptDate: Itinerary.outbound.depDate
							      	};
						}
						 properties.timestamp = (new Date()).getTime();

						searchResultCellActions.setPreviousCellobject(properties);
					}

			  },

			  highlightpreviousCell : function(){
				var searchResultCellActions = this,localSavedSearch;

					localSavedSearch = searchResultCellActions.getPreviousCellobject();
					if(localSavedSearch){
						dojo.query(".srBodySelectedDate",searchResultCellActions.domNode).removeClass("srBodySelectedDate srDefault");
						if(dojo.query("#"+localSavedSearch.compId[0]).length>0){
							tdObj = dojo.query("#"+localSavedSearch.compId[0])[0];
							dojo.addClass(tdObj.parentNode,"srBodySelectedDate srDefault");

						}
					}

			  }



	});

	return tui.searchResults.view.flights.SearchResultCellActions;
});
