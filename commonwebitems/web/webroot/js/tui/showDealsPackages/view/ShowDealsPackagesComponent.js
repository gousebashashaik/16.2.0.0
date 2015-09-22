define("tui/showDealsPackages/view/ShowDealsPackagesComponent", [
    "dojo",
    "dojo/on",
    "dojo/date/locale",
    "dojo/mouse",
    "dojo/query",
    "dojo/dom-style",
    "dojo/dom-attr",
    "dojo/dom-construct",
    "dojo/text!tui/showDealsPackages/view/templates/Tmpl_uncategorised.html",
    "dojo/text!tui/showDealsPackages/view/templates/Tmpl_categorised.html",
    "dojo/text!tui/searchResults/view/templates/pageLoaderTmpl.html",
    "dojo/text!tui/searchResults/view/templates/noResultsPopupTmpl.html",
    "dojo/text!tui/searchResults/view/templates/ajaxErrorPopupTmpl.html",
    "tui/searchResults/view/SearchResultsPaginator",
    "tui/mvc/Klass",
    "tui/searchResults/view/NoResultsPopup",
    "dojo/_base/connect",
    "dojo/dom-class",
    'dojo/_base/array',
    "tui/config/TuiConfig",
    "dojox/dtl",
    "dojox/dtl/Context",
   /* "tui/widget/mixins/Templatable",*/
    "tui/widget/_TuiBaseWidget",
    "tui/search/nls/Searchi18nable"], function (dojo, on, dateLocale, mouse, query, domStyle, domAttr, domConstruct,Tmpl_uncategorised,Tmpl_categorised,loaderTmpl,noResultsTmpl,ajaxErrorTmpl,SearchResultsPaginator, Klass, Modal, connect, domClass, arrayUtil, TuiConfig) {
	 /*
     * TODO: 
     * Pagination, initMessaging, event handling and re-work on logic
     * */
    function increment (x) {
        return x + 1;
    }

    function getHref (target) {
        return (target.tagName.toLowerCase() === "img") ? query(target).parents(".url")[0].href : target.href;
    }
    dojo.declare("tui.showDealsPackages.view.ShowDealsPackagesComponent",[tui.widget._TuiBaseWidget,
        tui.widget.mixins.Templatable, tui.search.nls.Searchi18nable], {
		//properties
		dataPath: 'dealsCollectionResult',
	    holidays: null,
	    paginator: null,
	    pageLoaderNode: null,
	    noResultsPopup: null,
        ajaxErrorPopup: null,
	    totalHolidaysAvailable: 0,
	    tmpl1: Tmpl_uncategorised,
	    tmpl2:Tmpl_categorised,
	    currentPage: 1,
	    pageLabel:null,
	    componentId: null,
	    categorizedPage: false,
	    tuiConfig: TuiConfig,
	    
	    postCreate: function () {
	        var showDealsPackagesComponent = this;
	        var mediator = dijit.registry.byId('mediator');
	        showDealsPackagesComponent.categorizedPage = mediator.model.dealsCollectionResult.categorizedPage;
	        showDealsPackagesComponent.pageLabel = mediator.model.dealsCollectionResult.merchandiserRequest.pageLabel;
	        var model = mediator.registerController(showDealsPackagesComponent, this.dataPath);
	        showDealsPackagesComponent.holidays = model.tabbedDealsList[0].merchandiserResult.holidays;
	        showDealsPackagesComponent.totalHolidaysAvailable = model.tabbedDealsList[0].merchandiserResult.endecaResultsCount;
	        showDealsPackagesComponent.initSearchMessaging();
	        // initialise pagination
	        showDealsPackagesComponent.paginator = new SearchResultsPaginator({area: 240, attachOnLoad: false}, null);
           
            dojo.connect(showDealsPackagesComponent.paginator, "onScroll", function (scrollPos, scrollListener) {
                // detach listener > prevent from re-firing
            	showDealsPackagesComponent.paginator.detach();
                if (showDealsPackagesComponent.hasMoreResults()) {
                    mediator.fire('paginate', showDealsPackagesComponent.currentPage, increment(showDealsPackagesComponent.currentPage));
                    showDealsPackagesComponent.toggleLoaderNode(true);
                }
            });
            
            connect.subscribe("tui/searchResults/view/SearchResultsComponent/showNoResultsPopup", function () {
            	showDealsPackagesComponent.renderNoResultsPopup();
            });
            
            // add event delegation methods (buttons)
            showDealsPackagesComponent.delegateEvents();

	        showDealsPackagesComponent.renderSearchResults(); 
	        
	        showDealsPackagesComponent.inherited(arguments);
	    },
	    
	    // ----------------------------------------------------------------------------- result item button methods
       renderSearchResults: function () {
            var showDealsPackagesComponent = this;
            var holidays = showDealsPackagesComponent.holidays;
            if (!holidays || !holidays.length) return;

            var newHolidays = [], html;

           _.forEach(holidays, function (item, i) {
                var holiday = dojo.clone(item);
                holiday.componentId = showDealsPackagesComponent.componentId;
               holiday.itinerary = showDealsPackagesComponent.formatItineraryDates(holiday.itinerary);
                holiday.accommodation.imageUrl = showDealsPackagesComponent.addImagePlaceholders(holiday.accommodation.imageUrl);
               if (holiday.accommodation.ratings.officialRating) {
                    holiday.accommodation.ratings.tripAdvisorRatingKlass = showDealsPackagesComponent.addTripAdvisorRatingKlass(holiday.accommodation.ratings.officialRating);
                }
               holiday.finPos = i + 1;
               holiday.packageId = showDealsPackagesComponent.componentId;
               holiday.accommodation.differentiatedModifiedCode = holiday.accommodation.differentiatedCode
               if(dojoConfig.dualBrandSwitch && showDealsPackagesComponent.tuiConfig[dojoConfig.site] && holiday.accommodation.differentiatedCode) {
                   var modifiedCode = showDealsPackagesComponent.tuiConfig[dojoConfig.site].dualBrandConfig.differentiatedCodeLarge[holiday.accommodation.differentiatedCode.toLowerCase().replace(/\s/g,"")];
                   if(modifiedCode){
                 	  holiday.accommodation.differentiatedModifiedCode = modifiedCode;
                   }
               }
                newHolidays.push(holiday);
            });
			
           
           if (showDealsPackagesComponent.categorizedPage) {
				var template = new dojox.dtl.Template(showDealsPackagesComponent.tmpl1);
			}
			else {
				var template = new dojox.dtl.Template(showDealsPackagesComponent.tmpl2);
			}
           
           var data = {holidays: newHolidays, currency:dojoConfig.currency, searchResults: showDealsPackagesComponent.searchMessaging.searchResults};
			var context = new dojox.dtl.Context(data);
			var html = dojo.trim(template.render(context));

			 // render template, include holidays and i8n texts
            /*html = showDealsPackagesComponent.renderTmpl(null, {
                holidays: newHolidays,
                searchResults: showDealsPackagesComponent.searchMessaging.searchResults
            });*/
            showDealsPackagesComponent.onBeforePlaceHolidays();
            if (html) {
                dojo.place(html, showDealsPackagesComponent.domNode, "last");
                showDealsPackagesComponent.destroyResultWidgets(true);
                dojo.parser.parse(showDealsPackagesComponent.domNode);
            }
            showDealsPackagesComponent.onAfterPlaceHolidays();
        },
        renderMoreHolidays: function (holidays) {
            // summary:
            //		Renders given search results
            var showDealsPackagesComponent = this;
            if (!holidays || !holidays.length) return;
            var newHolidays = [], html;

            // destroy clone node
           // showDealsPackagesComponent.destroyClone();

            _.forEach(holidays, function (item, i) {
                var holiday = dojo.clone(item);
                holiday.componentId = showDealsPackagesComponent.componentId;
               holiday.itinerary = showDealsPackagesComponent.formatItineraryDates(holiday.itinerary);
                holiday.accommodation.imageUrl = showDealsPackagesComponent.addImagePlaceholders(holiday.accommodation.imageUrl);
               if (holiday.accommodation.ratings.officialRating) {
                    holiday.accommodation.ratings.tripAdvisorRatingKlass = showDealsPackagesComponent.addTripAdvisorRatingKlass(holiday.accommodation.ratings.officialRating);
                }
               holiday.finPos = i + 1;
               holiday.packageId = showDealsPackagesComponent.componentId;
               holiday.accommodation.differentiatedModifiedCode = holiday.accommodation.differentiatedCode
               if(dojoConfig.dualBrandSwitch && showDealsPackagesComponent.tuiConfig[dojoConfig.site] && holiday.accommodation.differentiatedCode) {
                   var modifiedCode = showDealsPackagesComponent.tuiConfig[dojoConfig.site].dualBrandConfig.differentiatedCodeLarge[holiday.accommodation.differentiatedCode.toLowerCase().replace(/\s/g,"")];
                   if(modifiedCode){
                 	  holiday.accommodation.differentiatedModifiedCode = modifiedCode;
                   }
               }
                newHolidays.push(holiday);
            });
			if (showDealsPackagesComponent.categorizedPage) {
				var template = new dojox.dtl.Template(showDealsPackagesComponent.tmpl1);
			}
			else {
				var template = new dojox.dtl.Template(showDealsPackagesComponent.tmpl2);
			}
            
            var data = {holidays: newHolidays,  currency:dojoConfig.currency, searchResults: showDealsPackagesComponent.searchMessaging.searchResults};
			var context = new dojox.dtl.Context(data);
			var html = dojo.trim(template.render(context));

           /* html = showDealsPackagesComponent.renderTmpl(null, {
                holidays: newHolidays,
                searchResults: showDealsPackagesComponent.searchMessaging.searchResults
            });*/

            if (html) {
                dojo.place(html, showDealsPackagesComponent.domNode, "only");
                showDealsPackagesComponent.destroyResultWidgets(true);
                dojo.parser.parse(showDealsPackagesComponent.domNode);
            }

            showDealsPackagesComponent.onAfterPlaceHolidays();
        },
        onBeforePlaceHolidays: function () {
            var showDealsPackagesComponent = this;
           // showDealsPackagesComponent.destroyClone();
            // destroy all child widgets
            showDealsPackagesComponent.destroyResultWidgets();
            // empty container
            dojo.empty(showDealsPackagesComponent.domNode);
        },
        onAfterPlaceHolidays: function () {
            // summary:
            //		Runs after holidays have been rendered, after they are placed in the dom
            var showDealsPackagesComponent = this;
            // re-attach pagination listener
            showDealsPackagesComponent.paginator.attach();
            showDealsPackagesComponent.toggleLoaderNode(false);
        },
        hasMoreResults: function () {
            return this.totalHolidaysAvailable > this.holidays.length
        },
        
        //pagination methods
        toggleLoaderNode: function (action) {
            // summary:
            //		Adds page loading indicator to search results dom node
            var showDealsPackagesComponent= this, html;
            action = action ? "removeClass" : "addClass";
            if(!showDealsPackagesComponent.pageLoaderNode) {
                html = showDealsPackagesComponent.renderTmpl(loaderTmpl, {});
                showDealsPackagesComponent.pageLoaderNode = dojo.place(html, dojo.query('.result-view .product-list .viewport')[0], "last");
            }
            dojo[action](showDealsPackagesComponent.pageLoaderNode, "hide");
        },
        
        renderAjaxErrorPopup: function () {
            var showDealsPackagesComponent = this;
            if (!showDealsPackagesComponent.ajaxErrorPopup) {
                dojo.place(showDealsPackagesComponent.renderTmpl(ajaxErrorTmpl, showDealsPackagesComponent.searchMessaging.ajaxError), document.body, "last");
                showDealsPackagesComponent.ajaxErrorPopup = new Modal({
                    widgetId: 'ajaxError',
                    stopDefaultOnCancel: false,
                    reloadOnClose: true
                });
            }
            showDealsPackagesComponent.ajaxErrorPopup.open();
        },
	    renderNoResultsPopup: function () {
            var showDealsPackagesComponent = this;
            if (!showDealsPackagesComponent.noResultsPopup) {
                dojo.place(showDealsPackagesComponent.renderTmpl(noResultsTmpl, showDealsPackagesComponent.searchMessaging.filterNoResults), document.body, "last");
                showDealsPackagesComponent.noResultsPopup = new Modal();
            }
            showDealsPackagesComponent.noResultsPopup.open();
        },
        delegateEvents: function () {
            // summary:
            //		delegate result item button events
            var showDealsPackagesComponent = this;

            // add finPos and analytics data to img, h4 and cta urls
            on(showDealsPackagesComponent.domNode, on.selector(".url", "click"), function (event) {
                // Save current page state to SessionStorage
                connect.publish('tui/showIGPackages/Mediator/saveState', []);
            });

        },
        
        getFinPos: function (id) {
            //		Determine final position of package in results list (index + 1)
            var showDealsPackagesComponent = this;
            var finPos = null;

            query(".result-item", showDealsPackagesComponent.domNode).forEach(function (item, i) {
                if (domAttr.get(item, "data-package-id") === id) {
                    finPos = i + 1;
                }
            });

            return finPos ? finPos : 0;
        },
        destroyResultWidgets: function (preserveDom) {
            var showDealsPackagesComponent = this;
            // destroy all child widgets
            _.forEach(dijit.findWidgets(showDealsPackagesComponent.domNode), function(w) {
                w.destroyRecursive(preserveDom);
            });
        },
        /*destroyClone: function () {
            var showDealsPackagesComponent = this;
            if(showDealsPackagesComponent.clone) {
                if (dojo.hasClass(showDealsPackagesComponent.clone, "special")) {
                    var source = _.filter(dojo.query(".result-item", showDealsPackagesComponent.domNode), function(item){
                        return domAttr.get(item, "data-package-id") === domAttr.get(showDealsPackagesComponent.domNode, "data-package-id");
                    })[0];

                    if(source) {
                        dojo.addClass(dojo.query(".product", source), "special");
                    }
                }
                dojo.destroy(showDealsPackagesComponent.clone);
                showDealsPackagesComponent.cloneListener.remove();
                showDealsPackagesComponent.cloneListener = null;
                showDealsPackagesComponent.clone = null;
            }
        },*/
        
        formatItineraryDates: function (itinerary) {
            // summary:
            //		loops over itinerary and re-formats date strings
            var showDealsPackagesComponent = this;

            // format overall departure date
            // format overall departure date
            if(itinerary.departureDate!= null){
            	itinerary.departureDate = _.formatDate(itinerary.departureDate);
            }

            // format outbound dates
            if (itinerary.outbounds.length === 0) {
                return itinerary;
            }
            var newOutBound = [];
            var newInBound = [];
            _.forEach(itinerary.outbounds, function (outbound) {
                outbound.schedule.departureDate = _.formatDate(outbound.schedule.departureDate);
                outbound.schedule.departureTime = _.formatTime(outbound.schedule.departureTime);
                outbound.schedule.arrivalDate = _.formatDate(outbound.schedule.arrivalDate);
                outbound.schedule.arrivalTime = _.formatTime(outbound.schedule.arrivalTime);
                newOutBound.push(outbound);
            });
            itinerary.outbounds = newOutBound;

            _.forEach(itinerary.inbounds, function (inbound) {
                inbound.schedule.departureDate = _.formatDate(inbound.schedule.departureDate);
                inbound.schedule.departureTime = _.formatTime(inbound.schedule.departureTime);
                inbound.schedule.arrivalDate = _.formatDate(inbound.schedule.arrivalDate);
                inbound.schedule.arrivalTime = _.formatTime(inbound.schedule.arrivalTime);
                newInBound.push(inbound);
            });
            itinerary.inbounds = newInBound;

            return itinerary;
        },
        addTripAdvisorRatingKlass: function (rating) {
            return "rating" + rating * 10;
        },

        addImagePlaceholders: function (imageUrl) {
            if (imageUrl) { return imageUrl; }
            return "../images/default-small.png";
        },  
        
        generateRequest: function (field) {
            var widget = this;
            //todo: convert to page numbers
            if (field === 'paginate') {
                return {'first': increment(widget.currentPage), 'searchRequestType' : 'paginate', 'pageLabel':widget.pageLabel};
            }
            return {'first': 1};
        },
       
        refresh: function (field, oldValue, newValue, response) {
        	var response = response.tabbedDealsList[0].merchandiserResult;
            var showDealsPackagesComponent = this, oldLength = showDealsPackagesComponent.holidays.length;
            switch (field) {
                case 'paginate' :
                    response.holidays = _.map(response.holidays, function(holiday){
                        holiday.finPos = ++oldLength;
                        return holiday;
                    });
                    showDealsPackagesComponent.holidays = showDealsPackagesComponent.holidays.concat(response.holidays || []);
                    showDealsPackagesComponent.currentPage = newValue;
                    showDealsPackagesComponent.renderMoreHolidays(showDealsPackagesComponent.holidays);
                    break;
                default :
                	showDealsPackagesComponent.currentPage = 1;
                showDealsPackagesComponent.totalHolidaysAvailable = response.endecaResultsCount;
                showDealsPackagesComponent.holidays = response.holidays;
                showDealsPackagesComponent.renderSearchResults();
                    break;
            }
        },

        clear: function () {},

        handleNoResults: function (name) {
            var showDealsPackagesComponent = this;
            if (name && (name !== 'duration' && name !== 'rooms')) {
            	showDealsPackagesComponent.renderNoResultsPopup();
            }
        }
    });
    return tui.showDealsPackages.view.ShowDealsPackagesComponent;
});
