define("tui/widget/maps/cruise/CruiseBrowseResults", [
   "dojo",
	"dojo/_base/declare",
	"dojo/text!tui/searchResults/view/templates/browseResults/cruiseBrowseResultsTmpl.html",
	"dojo/text!tui/searchResults/view/templates/browseResults/browseFilterResultsTmpl.html",
	"dojo/text!tui/searchResults/view/templates/browseResults/cruiseRegionNameTmpl.html",
	"dojo/text!tui/searchResults/view/templates/browseResults/browseResultsCountTmpl.html",
	"dojo/text!tui/searchResults/view/templates/browseResults/topxPortsTmpl.html",
	"dojo/query",
	"dojo/on",
	"dojo/dom-class",
	"dojo/dom-attr",
	"dojo/dom-style",
	"dojo/fx",
	"dojo/mouse",
	"dojo/_base/xhr",
	"dojo/parser",
	"tui/searchResults/VariantMediator",
	"tui/mvc/Klass",
	"dojo/topic",
	"dojo/hash",
	"tui/searchResults/view/Tooltips",
	"tui/widget/popup/PopupTrigger",
	"tui/filterPanel/view/FilterController",
	"tui/filterPanel/view/calendar/SailingDates",
	"tui/searchResults/view/VariantSearchResultsComponent",
	"tui/searchResults/view/SortResultsSelectOption",
	"tui/widget/maps/cruise/CruiseAreasMapScroller",
	"tui/filterPanel/view/options/CruiseOptionsFilter",
	"tui/widget/_TuiBaseWidget",
   "tui/widget/mixins/Templatable"
], function (dojo, declare, cruiseResultsTmpl, filterResultsTmpl, regionNameTmpl, resultsCountTmpl, topXTmpl, query, on, domClass, domAttr, domStyle, fx, mouse, xhr, parser, VariantMediator, Klass, topic, hash) {
	return declare("tui.widget.maps.cruise.CruiseBrowseResults", [tui.widget._TuiBaseWidget , tui.widget.mixins.Templatable], {

		tmpl: cruiseResultsTmpl,
		filterResultsTmpl: filterResultsTmpl,
		regionNameTmpl: regionNameTmpl,
		resultsCountTmpl: resultsCountTmpl,
		topXTmpl: topXTmpl,
		parseFlag: true,
		parentWdgt: null,
		crCode: null,
		timer: [],

		postCreate: function(){
			var crResults = this, urlParams;
			crResults.parentWdgt = crResults.getParent();
			crResults.parentWdgt.crResults = crResults;
			crResults.inherited(arguments);
			crResults.addEventHandler();
			crResults.crResultsNode =  query(".cruiseArea-SearchResults", document)[0];
			crResults.crTopXNode =  query(".cruiseTopx", document)[0];
			//Subscription to update the browse results Endeca count
			topic.subscribe("tui/widget/maps/cruise/CruiseBrowseResults/updateCount", function(response){
				if (crResults.data.destLandingMap) {
					crResults.renderTemplate(crResults.resultsCountTmpl, {"jsonData": response}, query("div.count-list", crResults.crResultsNode)[0]);
				}
			});
			//replacing URL code with selected region
			urlParams = crResults.selectedRegion;
			if (urlParams){
				// Cruise region preselected - invoking toggle events
				crResults.preSelectRegion(urlParams);
				return true;
			}

			if (!crResults.data.destLandingMap){
				//cruise ports of call landing page scenario
				//onload AJAX for browser results data, with "000" as a dummy code
				crResults.invokeAjaxService("000","All Ports of call");
			}else {
				// cruise destination landing page scenario
				//onload AJAX for Top Ports of calls data, with "001" as a dummy code
				crResults.invokeAjaxService("001","All Destinations");
			}

		},
		addEventHandler: function () {
			var crResults = this;
			on(crResults.parentWdgt.domNode, on.selector("span.area-name", "click"), function (e) {
				var crCode = domAttr.get(this, "data-region-code");
				var crName = ( crName = domAttr.get(this, "data-region-Name") ) ? crName : "" ;
				crResults.invokeAjaxService(crCode, crName);
			});
		},
		preSelectRegion: function (urlParams){
			var crResults = this;
			/**
			 * crResults.parentWdgt.getParent() -- CruiseAreasMapController
			 * crResults.parentWdgt -- CruiseMapMenuExpandable
			 * Invokes Toggle events on page load
			 */
			var index =  crResults.fetchIndex(urlParams);
			crResults.parentWdgt.getParent().cruiseAreaMap.cruiseAreaIndex = index;
			var parent = query(".item", crResults.parentWdgt.domNode)[index+1];
			crResults.parentWdgt.invokeToggleEvent(parent);
			var crName = domAttr.get(query("span.area-name", parent)[0], "data-region-Name")
			crResults.invokeAjaxService(urlParams, crName);
		},
		fetchIndex: function (code){
			var crResults = this, index;
			_.each(crResults.data.destinationViewDatas, function(item, i){
				if (code === item.cruiseAreaCode) {
					index = i
				}
			});
			return index;
		},
		invokeAjaxService: function (code, crName) {
			var crResults = this,
				dynURL = crResults.data.destLandingMap ? dojoConfig.paths.webRoot + "/ws/itineraries?code="+code : dojoConfig.paths.webRoot + "/ws/ports?code="+code;
			//if(!code || code === "001"){
			// destroying the widgets in case of "All Destinations" scenario (i.e. No region selected)
			//crResults.destroyWidgets();
			//return;
			//}

			var loader = query(".cruise-map-loader.updating")[0];
			var resultsNode = query(".search-results", crResults.crResultsNode)[0];
			if(loader){
				dojo.addClass(loader, "search-results");
				dojo.removeClass(loader, "hide");
			}
			if( resultsNode ){
				dojo.addClass(resultsNode, "updating");
			}
			crResults.crCode  = code;
			var xhrReq = xhr.get({
				url: dynURL,
				handleAs: "json",
				load: function (response, options) {
					if(loader){
						dojo.setStyle(loader, {marginTop: code == "001" ? 0 : "-100px"});
						dojo.removeClass(loader, "search-results");
						dojo.addClass(loader, "hide");
					}
					crResults.destroyWidgets();
					crResults.handleResults(response, crName);
				},
				error: function (err) {
					crResults.handleError(err);
				}
			});
		},

		destroyWidgets: function () {
			var crResults = this;
			domStyle.set(crResults.crResultsNode, "display", "none");
			_.each(dijit.findWidgets(crResults.crResultsNode), function(widget){
				if( widget.id === "filtersController") {
					_.each( widget.filters, function(filter){
						filter.clearDestroy();
						query("ul.checks", filter.domNode)[0].innerHTML = '';
						query(filter.domNode).parent().addClass("hide");
					});
				}
			});
		},

		handleError: function(err) {
			var crResults = this;
			crResults.destroyWidgets();
			console.log("AJAX ERROR or Data missing--"+err);
		},
		handleResults: function(response, crName) {
			var crResults = this;
			var selectedRegion = _.find(crResults.data.destinationViewDatas, function(data){ return data.cruiseAreaCode == crResults.crCode; });
			var	contextData =  {
				"jsonData": response,
				"crName": crName,
				"hide": crResults.crCode === "001",
				"editorial": _.isUndefined(selectedRegion) ? 'nothing to display' : selectedRegion.editorialDescription,
				destinationsFlag: crResults.data.destLandingMap,
				regionNameTmpl: crResults.regionNameTmpl,
				filterResultsTmpl: crResults.filterResultsTmpl,
				resultsCountTmpl : crResults.resultsCountTmpl,
				topXTmpl: crResults.topXTmpl,
				cdnDomain: dojoConfig.paths.cdnDomain
			};

			domStyle.set(crResults.crResultsNode, "display", (!crResults.crCode || crResults.crCode === "001")? "none" : "block");
			// parse check - it will parse the widgets only once, for the first time of loading the widgets.
			if(crResults.parseFlag ) {
				if(crResults.crCode === "001"){
					//"001" - code for All Destinations
					// Destinations landing page ON-LOAD Scenario
					// Condition to render TOPX component, and ignore the rest
					crResults.renderTemplate(crResults.topXTmpl, contextData, crResults.crTopXNode);
					crResults.updateHeaderAndDescription(contextData);
					return true;
				}
				// parsing the widget only once
				crResults.updateHeaderAndDescription(contextData);
				crResults.renderTmpl(crResults.filterResultsTmpl,contextData);
				crResults.renderTemplate(crResults.tmpl, contextData, crResults.crResultsNode);
				crResults.commonTemplates(contextData);
				//Creating Mediator Instance
				crResults.mediatorInstance(response);
				parser.parse(crResults.crResultsNode);
				crResults.parseFlag = false;
			}
			else {
				//crResults.renderTemplate(crResults.regionNameTmpl, contextData, query("div.crName", crResults.crResultsNode)[0]);
				crResults.updateHeaderAndDescription(contextData);
				crResults.renderTmpl(crResults.filterResultsTmpl,contextData);
				crResults.commonTemplates(contextData);
				//Publish the updated data to each widgets
				crResults.mediator.model = 	response;
				crResults.mediator.publishToControllers("clearFilters", null, null, response, false);
			}
			//LazyLoad channel is not working/subscribed properly, so added this fix to remove the loader
			crResults.timer.push(setTimeout (function(){
				var searchNode = query(".search-results", document)[0];
				dojo.publish('tui/filterPanel/view/FilterController/defaultOpen');
				if(domClass.contains(searchNode, "updating")){
					domClass['remove']( searchNode, 'updating');
				}
			}, 600));

			var filterDomNode = query(".cruise-Filter-container .filter-section")[0];
			var sortNode = query(".results-container .sorted")[0];
			if(response.searchResult.holidays.length <= 1){
				// no results scenario
				filterDomNode && domStyle.set(filterDomNode, {visibility: "hidden"});
				sortNode && domStyle.set(sortNode, {visibility: "hidden"});
				//return true;
			}else{
				filterDomNode && domStyle.set(filterDomNode, {visibility: "visible"});
				sortNode && domStyle.set(sortNode, {visibility: "visible"});
			}
		},
		commonTemplates : function (contextData){
			var crResults = this;
			if(crResults.data.destLandingMap){
				//TOPX template - updates topx data on successful response.
				crResults.renderTemplate(crResults.topXTmpl, contextData, crResults.crTopXNode);
				//Endeca count template - updates count on successful response.
				crResults.renderTemplate( crResults.resultsCountTmpl, contextData, query("div.count-list", crResults.crResultsNode)[0]);
			}


		},

		updateHeaderAndDescription: function(contextData){
			var crResults = this;
			//Cruise region Name template - updates name on successful response.
			var crNameNode = query("div.crName", document)[0];
			if(!_.isUndefined(crNameNode)){
				/*			if(domClass.contains(crNameNode, "hide")){
				 domClass['remove']( crNameNode, 'hide');
				 }*/
				contextData.hide ? domClass['add']( crNameNode, 'hide') : domClass['remove']( crNameNode, 'hide');
				query("h2", crNameNode)[0].innerHTML = contextData.crName + ' CRUISES';
				query("p", crNameNode)[0].innerHTML = contextData.editorial;
			}
		},

		renderTemplate : function (currentTmpl, tmplData, resultsNode) {
			var crResults = this;
			var HTML = crResults.renderTmpl(currentTmpl, tmplData);
			dojo.place(HTML, resultsNode, "only");
		},
		mediatorInstance : function (response) {
			var crResults = this;
			if(!crResults.mediator) {
				crResults.mediator = new VariantMediator({
					model: response,
					saveStateKey: "searchResults",
					pageId: "searchResults"
				}, query("#mediator", document)[0]);
			}
		}
	});
});