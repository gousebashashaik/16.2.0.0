
// This value is updated at build time.
var buildVersion = '12345';

var profile = (function () {
    var testResourceRe = /[tT]ests?$/;

    return {
        resourceTags: {
            test: function (filename, mid) {
                return testResourceRe.test(mid) ||
          mid === 'tui/utils/tests/TuiUnderscoreTest' ||
          mid === 'tui/tests/module' ||
          mid === 'tui/widget/popup/tests/DynamicPopupTest' ||
          mid === 'tui/widget/maps/tests/Mappers' ||
          mid === 'tui/widget/form/tests/AutoCompleteTest' ||
          mid === 'tui/widget/searchPanel/tests/searchTest';
            },

            amd: function (filename, mid) {
                return !testResourceRe.test(mid) && /\.js$/.test(filename);
            }
        },

        basePath: '.',
        localeList: 'en',
        layerOptimize: 'closure',
        optimize: 'closure',
    stripConsole: 'none',
        releaseDir: 'minified/' + buildVersion,
        hasReport: true,
        staticHasFeatures: {
      'dojo-firebug': 0
        },

        packages: [
            {
        name: 'dojo',
        location: 'dojo'
            },
            {
        name: 'dojox',
        location: 'dojox'
            },
            {
        name: 'dijit',
        location: 'dijit'
            },
            {
        name: 'tui',
        location: 'tui'
            },
            {
        name: 'google',
        location: 'google'
            }
        ],

        layers: {
            'tui/tui-default': {
                include: [
                    'tui/Tui',
                    'dojo/parser',
                    'dojo/dom-attr',
                    'dojo/NodeList-traverse',
                    'dojo/selector/acme',

                    'dojox/dtl',
                    'dojox/dtl/Context',
                    'dojox/dtl/tag/logic',
                    'dojox/dtl/filter/strings',
                    'dojox/dtl/_DomTemplated',
                    'dojox/uuid/generateRandomUuid',
                    'tui/widget/MultiLinkAnchor',
                    'tui/widget/homepage/MegaMenu',
                    'tui/widget/DarkSitePopup',
                    'tui/widget/LazyLoadImage',
                    'tui/widget/popup/DynamicPopup',
                    'tui/widget/popup/ErrorPopup',
                    'tui/widget/popup/LightboxPopup',
                    'tui/widget/popup/PopupTrigger',
                    'tui/widget/popup/Tooltips',
                    'tui/widget/CookieNotifier',
                    // 'tui/widget/faqs',

                    'tui/searchPanel/view/QuickSearch',
                    'tui/widget/Tabs',
                    'tui/widget/Pills',
                    'tui/widget/media/HeroCarousel',
                    'tui/widget/maps/MapTopx',
                    'tui/widget/maps/FilterMap',

                    'tui/widget/WeatherChart',


                    'tui/widget/media/VideoPopup',
                    'tui/widget/media/MediaPopup',
                    'tui/widget/media/MapPopup',
                    'tui/widget/media/DreamlinerPopup',

                    "tui/search/controller/SearchController",

                    'tui/searchResults/view/NoResultView',
                    'tui/showIGPackages/Mediator',
                    'tui/singleAccom/FlightOptionsController',
                    'tui/filterPanel/view/sliders/TotalBudget',
                    'tui/filterPanel/view/sliders/PerPersonBudget',
                    'tui/filterPanel/view/options/Flight',
                    'tui/searchResults/Mediator',
                    'tui/filterPanel/view/FilterController',
                    'tui/widget/expand/Expandable',
                    'tui/mvc/Klass',
                    'tui/mvc/iocConfig',
                    'tui/mvc/Pair'
                ]
            },
            'tui/tui-cruise': {
                include: [
                    'tui/Tui',
                    'dojo/parser',
                    'dojo/dom-attr',
                    'dojo/NodeList-traverse',
                    'dojo/selector/acme',

                    'dojox/dtl',
                    'dojox/dtl/Context',
                    'dojox/dtl/tag/logic',
                    'dojox/dtl/filter/strings',
                    'dojox/dtl/_DomTemplated',

                    'tui/widget/MultiLinkAnchor',
                    'tui/widget/popup/DynamicPopup',
                    'tui/widget/CookieNotifier',
                    'tui/widget/homepage/MegaMenu',
                    'tui/widget/popup/Tooltips',
                    'tui/searchResults/view/Tooltips',

                    //'tui/widget/faqs',
                    'tui/widget/DarkSitePopup',
                    'tui/widget/LazyLoadImage',
                    'tui/widget/Tabs',
                    'tui/widget/Pills',
                    'tui/widget/expand/cruise/CruiseExpandable',
                    'tui/widget/media/HeroCarousel',

                    "tui/search/controller/cruise/SearchController",
                    "tui/searchPanel/view/cruise/AirportGuide",
                    "tui/searchPanel/view/cruise/DestinationGuide",
                    "tui/searchPanel/view/cruise/CruiseAndStayPicker",
                    "tui/searchPanel/view/cruise/CruiseSubmitButton",

                    'tui/searchResults/view/VariantNoResultView',
                    'tui/searchResults/VariantMediator',
                    'tui/searchResults/VariantMediator',
                    'tui/searchResults/view/VariantSearchResultsComponent',
                    'tui/filterPanel/view/FilterController',
                    'tui/mvc/Klass',
                    'tui/mvc/iocConfig',
                    'tui/mvc/Pair'
                ]
            },
            'tui/tui-bookflow-default': {
                include: [
                    'tui/Tui',
                    'dojo/parser',
                    'dojo/dom-attr',
                    'dojo/NodeList-traverse',
                    'dojo/selector/acme',
                    'tui/widget/MultiLinkAnchor',

                    'tui/widget/popup/Tooltips',

                    'tui/widget/homepage/MegaMenu',
                    'tui/widget/booking/alertMessage/view/AlertsMessage',
                    'tui/widget/expand/Expandable',
                    'tui/widget/form/SelectOption',

                    "tui/widget/popup/DynamicPopup",

                    "tui/widget/CookieNotifier",

                    'tui/widget/booking/summary/view/TotalPriceSummaryPanel',
                    'tui/widget/booking/summary/view/FlightSummaryPanel',
                    'tui/widget/booking/summary/view/RoomSummaryPanel',
                    'tui/widget/booking/summary/view/ExtrasSummaryPanel',
                    'tui/bookflow/Sticky',


                    'dijit/form/Form',
                    'dijit/form/Button',


                    'dojox/mvc/_DataBindingMixin',
                    'tui/widget/_TuiBaseWidget',
                    'dojox/dtl/_Templated',
                    'tui/widget/mixins/Templatable',


                    'tui/widget/Tabs'
                ]
            }
        },
        prefixes: []
    };
})();
