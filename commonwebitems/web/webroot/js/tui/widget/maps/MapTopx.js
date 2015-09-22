define("tui/widget/maps/MapTopx", ["dojo",
	"tui/widget/maps/Mappers",
	"tui/config/TuiConfig",
	"dojo/fx",
	"dojo/_base/fx",
	"dojo/_base/array",
	"tui/widget/maps/InspirationMapBase",
	"tui/utils/ObjectUtils"], function (dojo, mappers, tuiConfig) {

	dojo.declare("tui.widget.maps.MapTopx", [tui.widget.maps.InspirationMapBase], {

		// ---------------------------------------------------------------- InspirationMapBase properties

		mapFilter: false,

		// ---------------------------------------------------------------- mapTopx properties

		mapcatergories: null,

		mapcategoryimages: true,

		headerLinks: null,

		mapInspImagesContainer: null,

		mapInspImages: null,

		tuiConfig: tuiConfig,

		//topxamount: 0,

		showEntitiesTypes: ["locations", "events", "sights", "accommodations", "excursions"],

		// ---------------------------------------------------------------- mapTopx methods

		postMixInProperties: function () {
			var mapTopx = this;
			mapTopx.inherited(arguments);
			mapTopx.mapcatergories = [];
			mapTopx.selectedLocations = [];

			var data = {}, catergories;
			//mapTopx.topxamount = mapTopx.jsonData.visibleItems;

			var mappedData = mappers.topxMapper(mapTopx.jsonData);
			mapTopx.mapcatergories = mappedData.allCategories;
			mapTopx.selectedLocations = mappedData.selectedLocations;
			mapTopx.selectedType = mappedData.selectedCategory;

		},

		postCreate: function () {
			var mapTopx = this;
			mapTopx.inherited(arguments);

			var delayImg = 0;
			var delayLink = 0;
			var anims = [];

			var mapViewport = dojo.query(".map-viewport").addClass("inspirational-map-loading");

			mapTopx.mapInspImagesContainer = dojo.query('.menu-image', mapTopx.markercatergories);
			mapTopx.mapInspImages = dojo.query('img', mapTopx.markercatergories);
			mapTopx.headerLinks = dojo.query("a", mapTopx.markercatergories);

			mapTopx.headerLinks.style("opacity", "0").forEach(function (img) {
        anims.push(mapTopx.fadeIn(img, 800, delayImg));
        delayImg += 100;
      });

			var fadeInspImages = dojo.fx.combine(anims).play();

			var connectInsp = dojo.connect(fadeInspImages, "onEnd", function () {
				mapViewport.removeClass("inspirational-map-loading");
			});

			mapTopx.addEventListeners();
			mapTopx.tagElements(dojo.query('ul.menu li', mapTopx.domNode), function (DOMElement) {
				return DOMElement.textContent || DOMElement.innerText;
			});
		},

		updateLocationFeatureCode: function(){
			var mapTopx = this;
			_.each(mapTopx.jsonData, function(items){
				if(_.isArray(items)){
	                //location.destinationBreadcrumb = filterMap.destinationBreadcrumb
	                _.each(items, function(holidayLocation){
	                    if(holidayLocation.productRanges){
	                    	modifiedCode = mapTopx.tuiConfig[dojoConfig.site].dualBrandConfig.differentiatedCodeLarge[holidayLocation.productRanges.code.toLowerCase().replace(/\s/g,"")];
	                    	if(modifiedCode){
	                    		holidayLocation.productRanges.code = modifiedCode;
	                    	}
	                    }
	                });
				}
			});
		},

		addEventListeners: function () {
			var mapTopx = this;
			var inspAnimRan = false;

			mapTopx.headerLinks.onclick(function (event) {
				dojo.stopEvent(event);
				var anchor = this;

				mapTopx.currentPopup.close();
				mapTopx.headerLinks.removeClass("active");
				dojo.addClass(anchor, "active");

				mapTopx.setSelectedType(anchor.hash.substr(1));
				if(dojoConfig.dualBrandSwitch && mapTopx.tuiConfig[dojoConfig.site]){
					mapTopx.updateLocationFeatureCode();
				}
				var html = mapTopx.renderTemplateView("markerlist");
				var mapScrollPanels = mapTopx.mapSidePanel.getMapTab().getMapScrollPanels();
				mapScrollPanels[0].insertContent(html);
				mapScrollPanels[0].update();
				mapTopx.setMarkers(mapTopx.selectedLocations);
				mapScrollPanels[0].tagAllElements();

				if (!inspAnimRan) {
					var anims = [];
					var thisImage = dojo.query(".menu-image", anchor)[0];
					var index = mapTopx.mapInspImagesContainer.indexOf(thisImage);
					var delays = [
						[0, 100, 200, 300],
						[100, 0, 100, 200],
						[200, 100, 0, 100],
						[300, 200, 100, 0]
					];
					mapTopx.mapInspImagesContainer.forEach(function (img, i) {
						anims.push(mapTopx.slideUp(img, 300, delays[index][i]));
					});

					var slideInspImages = dojo.fx.combine(anims).play();
					dojo.connect(slideInspImages, "onEnd", function () {
						mapTopx.mapInspImagesContainer.remove();
					});

					inspAnimRan = true;
				}

			}).onmouseenter(function (event) {
					dojo.stopEvent(event);
					var anchor = this;
					var image = dojo.query("img", anchor)[0];
					var anims = [];

					mapTopx.mapInspImages.forEach(function (img, i) {
						if (img !== image) {
							anims.push(mapTopx.animProp(img, 300, {opacity: 0.5}));
						}
					});

					dojo.fx.combine(anims).play();

				}).onmouseleave(function (event) {
					dojo.stopEvent(event);
					var anchor = this;
					var image = dojo.query("img", anchor)[0];
					var anims = [];

					mapTopx.mapInspImages.forEach(function (img, i) {
						if (img !== image) {
							anims.push(mapTopx.animProp(img, 300, {opacity: 1}));
						}
					});

					dojo.fx.combine(anims).play();

				});

		},

		fadeIn: function (node, duration, delay) {
			var anim = dojo.fadeIn({
				node: node,
				duration: duration,
				delay: delay
			});
			return anim;
		},

		animProp: function (node, duration, properties, delay) {
			var anim = dojo.animateProperty({
				node: node,
				duration: duration,
				properties: properties,
				delay: delay || 0
			});
			return anim;
		},

		slideUp: function (node, duration, delay) {
			var anim = dojo.fx.wipeOut({
				node: node,
				duration: duration,
				delay: delay
			});
			return anim;
		},

		setSelectedType: function (type) {
			var mapTopx = this;
			mapTopx.selectedType = type;
			var location = mapTopx.jsonData[mapTopx.selectedType];
			mapTopx.selectedLocations = location;
		}
	});

	return tui.widget.maps.MapTopx;
});