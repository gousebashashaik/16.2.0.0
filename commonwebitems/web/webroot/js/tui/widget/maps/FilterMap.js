define("tui/widget/maps/FilterMap", ["dojo",
    "tui/widget/maps/Mappers",
    "dojo/html",
    "tui/config/TuiConfig",
    "tui/widget/maps/MapSwitchButton",
    "tui/widget/maps/InspirationMapBase",
    "tui/utils/ObjectUtils"], function (dojo, mappers, html, tuiConfig) {

    dojo.declare("tui.widget.maps.FilterMap", [tui.widget.maps.InspirationMapBase], {

        // ---------------------------------------------------------------- InspirationMapBase properties

        mapFilter: true,

        mapFilterTypes: null,

        showEntitiesTypes: null,

        pageType: null,

        tuiConfig: tuiConfig,

        // ---------------------------------------------------------------- mapTopx methods

        postMixInProperties: function () {
            var filterMap = this;
            filterMap.selectedLocations = [];
            filterMap.mapFilterTypes = [];
            filterMap.showEntitiesTypes = ["locations", "accommodations", "hotels", "villas", "events", "sights", "excursions"];
            filterMap.inherited(arguments);
            filterMap.parseJsonData();
        },

        parseJsonData: function () {
            var filterMap = this;
            var locations = [],
            	modifiedCode = "";
            filterMap.destinationBreadcrumb = mappers.locationNameMapper(filterMap.jsonData);
            _.each(filterMap.showEntitiesTypes, function (type) {
                var location = mappers.locationsMapper(filterMap.jsonData, type);
                //location.destinationBreadcrumb = filterMap.destinationBreadcrumb
                _.each(location, function(holidayLocation){
                    if(holidayLocation.productRanges && dojoConfig.dualBrandSwitch && filterMap.tuiConfig[dojoConfig.site]){
                    	modifiedCode = filterMap.tuiConfig[dojoConfig.site].dualBrandConfig.differentiatedCodeLarge[holidayLocation.productRanges.code.toLowerCase().replace(/\s/g,"")];
                    	if(modifiedCode){
                    		holidayLocation.productRanges.code = modifiedCode;
                    	}
                    }
                });
                locations.push(location);
            });
            filterMap.selectedLocations = _.flatten(locations);
            filterMap.mapFilterTypes = mappers.filterTypeMapper(filterMap.jsonData);
            filterMap.mapLevel = mappers.filterLocationTypeMapper(filterMap.jsonData);
        },

        subscribeToChannels: function () {
            var filterMap = this;
            filterMap.subscribe("tui/widget/maps/MapSwitchButton/onbuttonclick", function (mapWidget, state, value) {
                if (filterMap === mapWidget) {
                    var display = (state === "on") ? true : false;
                    filterMap.displayMarkers(value, display);
                }
            });

            filterMap.subscribe("tui/widget/maps/MapTab/afterShowTab", function () {
                filterMap.renumberMarkerList();
            });
        },

        postCreate: function () {
            var filterMap = this;
            filterMap.inherited(arguments);
            filterMap.subscribeToChannels();
            filterMap.setMarkers(filterMap.selectedLocations);

            if(filterMap.mapLevel === "accommodation" && (filterMap.pageType === "bookflow" ||  filterMap.pageType === "cruise")) {
                filterMap.addLightboxTrigger();
            }
        },

        onBeforeDisplayMarkers: function (marker, type, display) {
            var filterMap = this;
            var listItem = dojo.query([".marker_", marker.entitydata.id].join(""), filterMap.domNode)[0];
            var displayMaker = (display) ? "showWidget" : "hideWidget";
            if (filterMap.currentPopup.currentMarker === marker && !display) {
                filterMap.unselectListItem();
                filterMap.currentPopup.close();
            }
            filterMap[displayMaker](listItem);
        },

        renumberMarkerList: function () {
            var filterMap = this;
            var no = 1;
            dojo.query(".result-list li", filterMap.domNode).forEach(function (item) {
                if (dojo.style(item, "display") != "none") {
                    var countItem = dojo.query(".count", item)[0];
                    html.set(countItem, [no , ""].join(""));
                    dojo.attr(item, "idx", [no , ""].join(""));
                    no++;
                }
            });
            var marker = filterMap.currentPopup.currentMarker;
            var listItem = dojo.query((".marker_" + marker.entitydata.id), filterMap.domNode)[0];
            var index = dojo.getAttr(listItem, 'idx');
            dojo.query('#mapnode span.count').forEach(function (node) {
                node.innerHTML = index;
            });
        },

        onAfterSetMarker: function (markers) {
            var filterMap = this,
                triggerMarker = null;

            if (filterMap.mapLevel === "accommodation") {
                // compare page code with markers, get current marker and trigger click event
                triggerMarker = _.find(markers, function (marker) {
                    return marker.entitydata.code === filterMap.locationCode;
                });
                if (triggerMarker) {
                    google.maps.event.trigger(triggerMarker, "click");
                }
            }
        },

        addLightboxTrigger: function() {
            var filterMap = this;
            filterMap.lightboxPopup = dijit.registry.byId('attractionLightboxPopup');

            google.maps.event.addListener(filterMap.currentPopup, "linkclick", function(e) {
                var href = dojo.attr(e.target, "href");
                if( filterMap.pageType == "cruise" &&  !/.+cruiseattraction.+/.test(href) ) { return; }
                // return early if accommodation
                // TODO: need a better way to test this
                if ((/.+bookaccommodation.+/.test(href)) || (/.+stayAccommodation.+/.test(href))) return;
                // else load lightbox
                dojo.stopEvent(e);
                if(filterMap.lightboxPopup) {
                   if(filterMap.pageType === "bookflow"){
                      filterMap.lightboxPopup.requestData(href.replace("attraction", "bookattraction"));
                   }else{
                      filterMap.lightboxPopup.requestData(href);
                   }

                }
            });
        }

    });

    return tui.widget.maps.FilterMap;
});