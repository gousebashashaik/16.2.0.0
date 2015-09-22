var mobileConfig = {
    STANDARD_MOBILE_WIDTH: 562
};

var dojoConfig = {
    async: true,
    waitSeconds: 5,
    parseOnLoad: true,
    /*    paths: {
     tui: '/js/minified/tui',
     google: '/js/minified/google'
     },*/

    paths: {
        tui: '/js/tui',
        google: '/js/google'
    },

    init: function () {
        //TODO:load components by page
        require(["dojo",
            "tui/common/Sugar",
            "dojo/parser",
            "dojo/dom-attr",
            "dojo/NodeList-traverse",
            "tui/MobileInitializer",
            "tui/mobile",
            "tui/widget/mobile/DomRearranger",
            "tui/common/SwipeUtil",
            "tui/widget/thingstodo/ThingsToDo",
            "tui/widget/gallery/ImageGallery",
            "tui/widget/showmore/ShowMore",
            "tui/widget/mobile/TextRegion",
            "tui/widget/mobile/TabHeader",
            "tui/widget/weather/WeatherReport",
            "tui/widget/carousel/PageIndicator",
            "tui/widget/maps/FilterPanel",
            "tui/widget/maps/MapFilter",
            "tui/widget/maps/PlacesToGoMap",
            "tui/widget/maps/ThingsToDoMap",
            "tui/widget/topx/TopEntities",
            "tui/widget/Tabs",
            "tui/widget/deals/LatestDeals",
            "tui/widget/video/VideoPopup",
            "tui/widget/gallery/GalleryPopup",
            "tui/widget/mobile/Select",
            "tui/widget/maps/PlacesToGoStatic",
            "tui/widget/maps/PlacesToGoExplore",
            "tui/widget/maps/PlacesToGoThumbnail",
            "tui/widget/maps/TopxMap",
            "tui/widget/maps/AccomThumbnail",
            "tui/common/ListMonad",
            "tui/widget/breadcrumb/BreadCrumb",
            "tui/widget/ExpandableParagraph",
            "tui/widget/destinations/SimilarDestinations",
            "tui/widget/ExpandableList",
      "tui/widget/maps/Zoomer"
    ], function(dojo, sugar, parser, domAttr, nodeTraverse, mobileInitializer, mobile) {
            dojo.ready(function () {
                mobileInitializer.init();
                mobile.init();
      });
        });
    }
};
