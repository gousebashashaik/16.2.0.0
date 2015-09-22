define({

  SCOPES: {
    SINGLETON: "singleton",
    CONFIG: "config",
    PROTOTYPE: "prototype"
  },

  /***********************************************
   *
   *   MAIN SEARCH
   *
   ************************************************/
  searchConfig: {
    type: "tui/searchPanel/config/SearchConfig",
    PERSISTED_SEARCH_PERIOD: 30,
    MAX_ADULTS_NUMBER: 9,
    MIN_ADULTS_NUMBER: 2,
    INFANT_AGE: 2,
    FLEXIBLE_DAYS: 7,
    DATE_PATTERN: "EEE d MMMM yyyy",
    PAGE: 0,
    HAS_SENIORS: false,
    scope: "config"
  },

  searchController: {
    type: "tui/search/controller/SearchController",
    searchConfig: "$searchConfig",
    searchPanelModel: "$searchPanelModel",
    airportGuideStore: "$airportGuideStore",
    destinationGuideStore: "$destinationGuideStore",
    dateStore: "$dateStore",
    searchApi: "searchPanel",
    scope: "singleton"
  },

  searchPanelModel: {
    type: "tui/searchPanel/model/SearchPanelModel",
    savedSearch: dojoConfig.site + "/search/main",
    view: "search",
    searchConfig: "$searchConfig",
    scope: "singleton"
  },

  airportGuideStore: {
    type: "tui/searchPanel/store/AirportGuideStore",
    targetURL: dojoConfig.paths.webRoot + "/ws/airportguide",
    scope: "singleton"
  },

  destinationGuideStore: {
    type: "tui/searchPanel/store/DestinationGuideStore",
    targetURL: dojoConfig.paths.webRoot + '/ws/suggestions',
    targetURLForCountries: dojoConfig.paths.webRoot + '/ws/destinationguide',
    scope: "singleton"
  },

  dateStore: {
    type: "tui/searchPanel/store/DateStore",
    targetURL: dojoConfig.paths.webRoot + "/ws/traveldates",
    scope: "singleton"
  },

  airportGuide: {
    type: "tui/searchPanel/view/AirportGuide",
    searchPanelModel: "$searchPanelModel",
    airportGuideStore: "$airportGuideStore",
    widgetController: "$searchController",
    binds: [
      {
        target: "searchPanelModel.searchErrorMessages",
        watch: {
          fromTo: ["displayFromToError"],
          from: ["displayRouteError"]
        }
      }
    ]
  },

  airportMultiFieldList: {
    type: "tui/searchPanel/view/AirportMultiFieldList",
    searchPanelModel: "$searchPanelModel",
    widgetController: "$searchController"
  },

  destinationMultiFieldList: {
    type: "tui/searchPanel/view/DestinationMultiFieldList",
    searchPanelModel: "$searchPanelModel",
    widgetController: "$searchController",
    binds: [
      {
        target: "searchPanelModel.searchErrorMessages",
        watch: {
          fromTo: ["toggleEmptyAlert"],
          to: ["displayToErrorMessage"]
        }
      }
    ]
  },

  destinationGuide: {
    type: "tui/searchPanel/view/DestinationGuide",
    searchPanelModel: "$searchPanelModel",
    destinationGuideStore: "$destinationGuideStore",
    widgetController: "$searchController",
    binds: [
      {
        target: "searchPanelModel",
        watch: {
          date: ["updateGuideTitle"],
          flexible: ["updateGuideTitle"]
        }
      }
    ]
  },

  searchDatePicker: {
    type: "tui/searchPanel/view/SearchDatePicker",
    searchPanelModel: "$searchPanelModel",
    widgetController: "$searchController",
    searchConfig: "$searchConfig",
    dateStore: "$dateStore",
    binds: [
      {
        target: "searchPanelModel",
        watch: {
          date: ["setFormatedDate"]
        }
      },
      {
        target: "searchPanelModel.searchErrorMessages",
        watch: {
          when: ["displayDateErrorMessage"]
        }
      }
    ]
  },

  flexibleView: {
    type: "tui/searchPanel/view/FlexibleView",
    searchPanelModel: "$searchPanelModel",
    widgetController: "$searchController",
    binds: [
      {
        target: "searchPanelModel",
        watch: {
          flexible: ["select"]
        }
      }
    ]
  },

  toolTips: {
    type: "tui/widget/popup/Tooltips",
    widgetController: "$searchController"
  },

  howLong: {
        type: "tui/searchPanel/view/HowLongOptions",
        searchPanelModel: "$searchPanelModel",
        widgetController: "$searchController",
        tag: '300',
        binds: [
            {
                target: "searchPanelModel",
                watch: {
                    duration: ["updateHowLong"]
                }
            }
        ]
    },

  adultSelectionOption: {
    type: "tui/searchPanel/view/AdultsSelectOption",
    searchPanelModel: "$searchPanelModel",
    widgetController: "$searchController",
    binds: [
      {
        target: "searchPanelModel",
        watch: {
          adults: ["updateAdultsView"]
        }
      },
      {
        target: "searchPanelModel.searchErrorMessages",
        watch: {
          partyComp: ["updatePartyCompError"]
        }
      }
    ]
  },

  seniorsSelectOption: {
    type: "tui/searchPanel/view/SeniorsSelectOption",
    searchPanelModel: "$searchPanelModel",
    widgetController: "$searchController",
    binds: [
      {
        target: "searchPanelModel",
        watch: {
          seniors: ["updateSeniorView"]
        }
      },
      {
        target: "searchPanelModel.searchErrorMessages",
        watch: {
          partyComp: ["updatePartyCompError"]
        }
      }
    ]
  },

  childSelectOption: {
    type: "tui/searchPanel/view/ChildSelectOption",
    searchPanelModel: "$searchPanelModel",
    widgetController: "$searchController",
    binds: [
      {
        target: "searchPanelModel",
        watch: {
          childAges: ["addChildAgeSelector"],
          children: ["updateChildrenView"]
        }
      }
    ]
  },

  submitButton: {
    type: "tui/searchPanel/view/SubmitButton",
    searchPanelModel: "$searchPanelModel",
    widgetController: "$searchController",
    formSelector: ".search-form"
  },

  searchSummary: {
    type: "tui/searchPanel/view/SearchSummary",
    widgetController: "$searchController",
    searchPanelModel: "$searchPanelModel",
    searchConfig: "$searchConfig"
  },


  /***********************************************
  *
  *   NEW MAIN SEARCH
  *
  ************************************************/

 searchBController: {
   type: "tui/searchB/controller/SearchController",
   searchConfig: "$searchConfig",
   searchPanelModel: "$searchBPanelModel",
   airportGuideStore: "$searchBAirportGuideStore",
   destinationGuideStore: "$searchBDestinationGuideStore",
   dateStore: "$searchBDateStore",
   searchApi: "searchPanel",
   scope: "singleton"
 },

 searchBPanelModel: {
   type: "tui/searchBPanel/model/SearchPanelModel",
   savedSearch: dojoConfig.site + "/search/main",
   view: "search",
   searchConfig: "$searchConfig",
   searchApi: "searchPanel",
   scope: "singleton"
 },

 searchBAirportGuideStore: {
   type: "tui/searchBPanel/store/AirportGuideStore",
   targetURL: dojoConfig.paths.webRoot + "/ws/airportguide",
   scope: "singleton"
 },

 searchBDestinationGuideStore: {
   type: "tui/searchBPanel/store/DestinationGuideStore",
	targetURL: dojoConfig.paths.webRoot + '/ws/suggestionsforcontinents',
	targetURLForMostPopular: dojoConfig.paths.webRoot + '/ws/getMostPopularData',
	targetURLForCountries: dojoConfig.paths.webRoot + '/ws/getRegionDestinationForCountry',
	targetURLForCollections: dojoConfig.paths.webRoot + '/ws/suggestionsforcollections',
   scope: "singleton"
 },

 searchBDateStore: {
   type: "tui/searchBPanel/store/DateStore",
   targetURL: dojoConfig.paths.webRoot + "/ws/traveldates",
   scope: "singleton"
 },

 searchBAirportGuide: {
   type: "tui/searchBPanel/view/AirportGuide",
   searchPanelModel: "$searchBPanelModel",
   airportGuideStore: "$searchBAirportGuideStore",
   widgetController: "$searchBController",
   binds: [
           {
             target: "searchPanelModel.searchErrorMessages",
             watch: {
               fromTo: ["displayFromToError"],
               from: ["displayRouteError"]
             }
           }
         ]
 },

 searchBAirportMultiFieldList: {
   type: "tui/searchBPanel/view/AirportMultiFieldList",
   searchPanelModel: "$searchBPanelModel",
   widgetController: "$searchBController"

 },

 searchBDestinationMultiFieldList: {
   type: "tui/searchBPanel/view/DestinationMultiFieldList",
   searchPanelModel: "$searchBPanelModel",
   widgetController: "$searchBController",
   binds: [
     {
       target: "searchPanelModel.searchErrorMessages",
       watch: {
         fromTo: ["toggleEmptyAlert"],
         to: ["displayToErrorMessage"]
       }
     }
   ]
 },

 searchBDestinationGuide: {
   type: "tui/searchBPanel/view/DestinationGuide",
   searchPanelModel: "$searchBPanelModel",
   destinationGuideStore: "$searchBDestinationGuideStore",
   widgetController: "$searchBController",
   binds: [
     {
        	   target: "searchPanelModel.searchErrorMessages",
       watch: {
                 fromTo: ["toggleEmptyAlert"]
       }
     }
   ]
 },

 searchBDatePicker: {
   type: "tui/searchBPanel/view/SearchDatePicker",
   searchPanelModel: "$searchBPanelModel",
   widgetController: "$searchBController",
   searchConfig: "$searchConfig",
   dateStore: "$searchBDateStore",
   binds: [
     {
       target: "searchPanelModel",
       watch: {
         date: ["setFormatedDate"]
       }
     },
     {
       target: "searchPanelModel.searchErrorMessages",
       watch: {
         when: ["displayDateErrorMessage"]
       }
     }
   ]
 },

 searchBFlexibleView: {
   type: "tui/searchBPanel/view/FlexibleView",
   searchPanelModel: "$searchBPanelModel",
   widgetController: "$searchBController",
   binds: [
     {
       target: "searchPanelModel",
       watch: {
         flexible: ["select"]
       }
     }
   ]
 },

 searchBToolTips: {
   type: "tui/widget/popup/Tooltips",
   widgetController: "$searchBController"
 },

 searchBHowLong: {
       type: "tui/searchBPanel/view/HowLongOptions",
       searchPanelModel: "$searchBPanelModel",
       widgetController: "$searchBController",
       tag: '300',
       binds: [
           {
               target: "searchPanelModel",
               watch: {
                   duration: ["updateHowLong"]
               }
           }
       ]
   },

   searchBAdultSelectionOption: {
   type: "tui/searchBPanel/view/AdultsSelectOption",
   searchPanelModel: "$searchBPanelModel",
   widgetController: "$searchBController",
   binds: [
     {
       target: "searchPanelModel",
       watch: {
         adults: ["updateAdultsView"]
       }
     },
     {
       target: "searchPanelModel.searchErrorMessages",
       watch: {
         partyComp: ["updatePartyCompError"]
       }
     }
   ]
 },

 searchBSeniorsSelectOption: {
   type: "tui/searchBPanel/view/SeniorsSelectOption",
   searchPanelModel: "$searchBPanelModel",
   widgetController: "$searchBController",
   binds: [
     {
       target: "searchPanelModel",
       watch: {
         seniors: ["updateSeniorView"]
       }
     },
     {
       target: "searchPanelModel.searchErrorMessages",
       watch: {
         partyComp: ["updatePartyCompError"]
       }
     }
   ]
 },

 searchBChildSelectOption: {
   type: "tui/searchBPanel/view/ChildSelectOption",
   searchPanelModel: "$searchBPanelModel",
   widgetController: "$searchBController",
   binds: [
     {
       target: "searchPanelModel",
       watch: {
         childAges: ["addChildAgeSelector"],
         children: ["updateChildrenView"]
       }
     }
   ]
 },

 searchBSubmitButton: {
   type: "tui/searchBPanel/view/SubmitButton",
   searchPanelModel: "$searchBPanelModel",
   widgetController: "$searchBController",
   formSelector: ".search-form"
 },

 searchBSummary: {
   type: "tui/searchBPanel/view/SearchSummary",
   widgetController: "$searchBController",
   searchPanelModel: "$searchBPanelModel",
   searchConfig: "$searchConfig"
 },

  /***********************************************
   *
   *   GET PRICE SEARCH
   *
   ************************************************/

  getPriceConfig: {
    type: "tui/searchPanel/config/SearchConfig",
    PERSISTED_SEARCH_PERIOD: 30,
    MIN_ADULTS_NUMBER: 2,
    MAX_ADULTS_NUMBER: 9,
    INFANT_AGE: 2,
    FLEXIBLE_DAYS: 3,
    DATE_PATTERN: "EEE d MMMM yyyy",
    PAGE: 0,
    HAS_SENIORS: false,
    tag: '364',
    number: 1,
    scope: "config"
  },

  getPriceController: {
    type: "tui/search/controller/SearchController",
    searchConfig: "$getPriceConfig",
    searchPanelModel: "$getPriceModel",
    airportGuideStore: "$airportGuideStore",
    destinationGuideStore: "$destinationGuideStore",
    dateStore: "$dateStore",
    searchApi: "getPrice",
    tag: '364',
    number: 1,
    scope: "singleton"
  },

  getPriceModal: {
    type: 'tui/searchGetPrice/view/GetPriceModal',
    searchPanelModel: '$getPriceModel',
    scope: 'singleton'
  },

  getPriceModel: {
    type: "tui/searchPanel/model/SearchPanelModel",
    savedSearch: dojoConfig.site + "/search/main",
    view: "search",
    searchConfig: "$getPriceConfig",
    scope: "singleton"
  },

  getPriceAirportGuide: {
    type: "tui/searchPanel/view/AirportGuide",
    searchPanelModel: "$getPriceModel",
    widgetController: "$getPriceController",
    airportGuideStore: "$airportGuideStore",
    expandableProp: "expand-horizontal",
    targetSelector: ".wrapper",
    tag: '364',
    number: 1,
    columns: 2
  },

  getPriceAirportMultiFieldList: {
    type: "tui/searchPanel/view/AirportMultiFieldList",
    searchPanelModel: "$getPriceModel",
    widgetController: "$getPriceController",
    tag: '364',
    number: 1,
    binds: [
      {
        target: "searchPanelModel.searchErrorMessages",
        watch: {
          fromTo: ["displayFromToError"],
          from: ["displayRouteError"]
        }
      }
    ]
  },

  getPriceDestinationMultiFieldList: {
    type: "tui/searchPanel/view/DestinationMultiFieldList",
    searchPanelModel: "$getPriceModel",
    widgetController: "$getPriceController",
    tag: '364',
    number: 1,
    binds: [
      {
        target: "searchPanelModel.searchErrorMessages",
        watch: {
          fromTo: ["toggleEmptyAlert"],
          to: ["displayToErrorMessage"]
        }
      }
    ]
  },

  getPriceDestinationGuide: {
    type: "tui/searchPanel/view/DestinationGuide",
    searchPanelModel: "$getPriceModel",
    widgetController: "$getPriceController",
    destinationGuideStore: "$destinationGuideStore",
    tag: '364',
    number: 1,
    binds: [
      {
        target: "searchPanelModel",
        watch: {
          date: ["updateGuideTitle"],
          flexible: ["updateGuideTitle"]
        }
      }
    ]
  },

  getPriceSearchDatePicker: {
    type: "tui/searchPanel/view/SearchDatePicker",
    searchPanelModel: "$getPriceModel",
    widgetController: "$getPriceController",
    searchConfig: "$getPriceConfig",
    dateStore: "$dateStore",
    tag: '364',
    number: 1,
    binds: [
      {
        target: "searchPanelModel",
        watch: {
          date: ["setFormatedDate"]
        }
      },
      {
        target: "searchPanelModel.searchErrorMessages",
        watch: {
          when: ["displayDateErrorMessage"]
        }
      }
    ]
  },

  getPriceFlexibleView: {
    type: "tui/searchPanel/view/FlexibleView",
    searchPanelModel: "$getPriceModel",
    widgetController: "$getPriceController",
    tag: '364',
    number: 1,
    binds: [
      {
        target: "searchPanelModel",
        watch: {
          flexible: ["select"]
        }
      }
    ]
  },

  getPricehowLong: {
        type: "tui/searchPanel/view/HowLongOptions",
        searchPanelModel: "$getPriceModel",
        widgetController: "$getPriceController",
        tag: '364',
        binds: [
            {
                target: "searchPanelModel",
                watch: {
                    duration: ["updateHowLong"]
                }
            }
        ]
    },


  getPriceAdultSelectOption: {
    type: "tui/searchPanel/view/AdultsSelectOption",
    searchPanelModel: "$getPriceModel",
    widgetController: "$getPriceController",
    tag: '364',
    number: 1,
    binds: [
      {
        target: "searchPanelModel",
        watch: {
          adults: ["updateAdultsView"]
        }
      },
      {
        target: "searchPanelModel.searchErrorMessages",
        watch: {
          partyComp: ["updatePartyCompError"]
        }
      }
    ]
  },

  getPriceSeniorsSelectOption: {
    type: "tui/searchPanel/view/SeniorsSelectOption",
    searchPanelModel: "$getPriceModel",
    widgetController: "$getPriceController",
    tag: '364',
    number: 1,
    binds: [
      {
        target: "searchPanelModel",
        watch: {
          seniors: ["updateSeniorView"]
        }
      },
      {
        target: "searchPanelModel.searchErrorMessages",
        watch: {
          partyComp: ["updatePartyCompError"]
        }
      }
    ]
  },

  getPriceChildSelectOption: {
    type: "tui/searchPanel/view/ChildSelectOption",
    searchPanelModel: "$getPriceModel",
    widgetController: "$getPriceController",
    tag: '364',
    number: 1,
    binds: [
      {
        target: "searchPanelModel",
        watch: {
          childAges: ["addChildAgeSelector"],
          children: ["updateChildrenView"]
        }
      }
    ]
  },

  getPriceSubmitButton: {
    type: "tui/searchPanel/view/SubmitButton",
    searchPanelModel: "$getPriceModel",
    widgetController: "$getPriceController",
    tag: '364',
    number: 1,
    formSelector: ".get-search-form"
  },



  /***********************************************
   *
  *  NEW GET PRICE SEARCH
  *
  ************************************************/

 getPriceBConfig: {
   type: "tui/searchPanel/config/SearchConfig",
   PERSISTED_SEARCH_PERIOD: 30,
   MIN_ADULTS_NUMBER: 2,
   MAX_ADULTS_NUMBER: 9,
   INFANT_AGE: 2,
   FLEXIBLE_DAYS: 3,
   DATE_PATTERN: "EEE d MMMM yyyy",
   PAGE: 0,
   HAS_SENIORS: false,
   tag: '364ab',
   number: 1,
   scope: "config"
 },

 getPriceBController: {
   type: "tui/searchB/controller/SearchController",
   searchConfig: "$getPriceBConfig",
   searchPanelModel: "$getPriceBModel",
   airportGuideStore: "$searchBAirportGuideStore",
   destinationGuideStore: "$searchBDestinationGuideStore",
   dateStore: "$searchBDateStore",
   searchApi: "getPrice",
   tag: '364ab',
   number: 1,
   scope: "singleton"
 },

 getPriceBModal: {
   type: 'tui/searchBGetPrice/view/GetPriceModal',
   searchPanelModel: '$getPriceBModel',
   scope: 'singleton'
 },

 getPriceBModel: {
   type: "tui/searchBPanel/model/SearchPanelModel",
   savedSearch: dojoConfig.site + "/search/main",
   view: "search",
   searchConfig: "$getPriceConfig",
   scope: "singleton"
 },

 getPriceBAirportGuide: {
   type: "tui/searchBPanel/view/AirportGuide",
   searchPanelModel: "$getPriceBModel",
   widgetController: "$getPriceBController",
   airportGuideStore: "$searchBAirportGuideStore",
   expandableProp: "expand-horizontal",
   targetSelector: ".wrapper",
   tag: '364ab',
   number: 1,
   columns: 2,
   binds: [
           {
             target: "searchPanelModel.searchErrorMessages",
             watch: {
               fromTo: ["displayFromToError"],
               from: ["displayRouteError"]
             }
           }
         ]
 },

 getPriceBAirportMultiFieldList: {
   type: "tui/searchBPanel/view/AirportMultiFieldList",
   searchPanelModel: "$getPriceBModel",
   widgetController: "$getPriceBController",
   tag: '364ab',
   number: 1
 },

 getPriceBDestinationMultiFieldList: {
   type: "tui/searchBPanel/view/DestinationMultiFieldList",
   searchPanelModel: "$getPriceBModel",
   widgetController: "$getPriceBController",
   tag: '364ab',
   number: 1,
   binds: [
     {
       target: "searchPanelModel.searchErrorMessages",
       watch: {
         fromTo: ["toggleEmptyAlert"],
         to: ["displayToErrorMessage"]
       }
     }
   ]
 },

 getPriceBDestinationGuide: {
   type: "tui/searchBPanel/view/DestinationGuide",
   searchPanelModel: "$getPriceBModel",
   widgetController: "$getPriceBController",
   destinationGuideStore: "$searchBDestinationGuideStore",
   tag: '364ab',
   number: 1,
   binds: [
     {
       target: "searchPanelModel",
       watch: {
         date: ["updateGuideTitle"],
         flexible: ["updateGuideTitle"]
       }
     }
   ]
 },

 getPriceBSearchDatePicker: {
   type: "tui/searchBPanel/view/SearchDatePicker",
   searchPanelModel: "$getPriceBModel",
   widgetController: "$getPriceBController",
   searchConfig: "$getPriceConfig",
   dateStore: "$searchBDateStore",
   tag: '364ab',
   number: 1,
   binds: [
     {
       target: "searchPanelModel",
       watch: {
         date: ["setFormatedDate"]
       }
     },
     {
       target: "searchPanelModel.searchErrorMessages",
       watch: {
         when: ["displayDateErrorMessage"]
       }
     }
   ]
 },

 getPriceBFlexibleView: {
   type: "tui/searchBPanel/view/FlexibleView",
   searchPanelModel: "$getPriceBModel",
   widgetController: "$getPriceBController",
   tag: '364ab',
   number: 1,
   binds: [
     {
       target: "searchPanelModel",
       watch: {
         flexible: ["select"]
       }
     }
   ]
 },

 getPriceBhowLong: {
       type: "tui/searchBPanel/view/HowLongOptions",
       searchPanelModel: "$getPriceBModel",
       widgetController: "$getPriceBController",
       tag: '364ab',
       binds: [
           {
               target: "searchPanelModel",
               watch: {
                   duration: ["updateHowLong"]
               }
           }
       ]
   },


 getPriceBAdultSelectOption: {
   type: "tui/searchBPanel/view/AdultsSelectOption",
   searchPanelModel: "$getPriceBModel",
   widgetController: "$getPriceBController",
   tag: '364ab',
   number: 1,
   binds: [
     {
       target: "searchPanelModel",
       watch: {
         adults: ["updateAdultsView"]
       }
     },
     {
       target: "searchPanelModel.searchErrorMessages",
       watch: {
         partyComp: ["updatePartyCompError"]
       }
     }
   ]
 },

 getPriceBSeniorsSelectOption: {
   type: "tui/searchBPanel/view/SeniorsSelectOption",
   searchPanelModel: "$getPriceBModel",
   widgetController: "$getPriceBController",
   tag: '364ab',
   number: 1,
   binds: [
     {
       target: "searchPanelModel",
       watch: {
         seniors: ["updateSeniorView"]
       }
     },
     {
       target: "searchPanelModel.searchErrorMessages",
       watch: {
         partyComp: ["updatePartyCompError"]
       }
     }
   ]
 },

 getPriceBChildSelectOption: {
   type: "tui/searchBPanel/view/ChildSelectOption",
   searchPanelModel: "$getPriceBModel",
   widgetController: "$getPriceBController",
   tag: '364ab',
   number: 1,
   binds: [
     {
       target: "searchPanelModel",
       watch: {
         childAges: ["addChildAgeSelector"],
         children: ["updateChildrenView"]
       }
     }
   ]
 },

 getPriceBSubmitButton: {
   type: "tui/searchBPanel/view/SubmitButton",
   searchPanelModel: "$getPriceBModel",
   widgetController: "$getPriceBController",
   tag: '364ab',
   number: 1,
   formSelector: ".get-search-form"
 },

  /***********************************************
   *
	*   SMERCH OVERLAY
	*
	************************************************/

	smerchOverlayController: {
	    type: "tui/smerchOverlayPanel/controller/SearchController",
	    searchConfig: "$getPriceConfig",
	    searchPanelModel: "$smerchOverlayModel",
	    airportGuideStore: "$smerchOverlayAirportGuideStore",
	    destinationGuideStore: "$destinationGuideStore",
	    dateStore: "$smerchOverlayDateStore",
	    searchApi: "getPrice",
	    searchtype:"smerch",
	    scope: "singleton"
	},

	smerchOverlayModel: {
	    type: "tui/searchPanel/model/SearchPanelModel",
	    savedSearch: "search/mainfc",
	    view: "search",
	    searchConfig: "$searchConfig",
	    searchType:"smerch",
	    scope: "singleton"
	},

	smerchOverlayDateStore: {
	    type: "tui/smerchOverlayPanel/store/DateStore",
	    targetURL: dojoConfig.paths.webRoot + "/ws/traveldatesForSmerch",
	    scope: "singleton"
	},

	smerchOverlayAirportGuideStore: {
	    type: "tui/smerchOverlayPanel/store/AirportGuideStore",
	    targetURL: dojoConfig.paths.webRoot + "/ws/airportguideforsmerch",
	    scope: "singleton"
	  },

	smerchOverlayAirportGuide: {
	    type: "tui/searchPanel/view/AirportGuide",
	    searchPanelModel: "$smerchOverlayModel",
	    widgetController: "$smerchOverlayController",
	    airportGuideStore: "$smerchOverlayAirportGuideStore",
	    expandableProp: "expand-horizontal",
	    targetSelector: ".wrapper",
	    columns: 2,
	    binds: [
	            {
	                target: "searchPanelModel.searchErrorMessages",
	                watch: {
	                    fromTo: ["displayFromToError"],
	                    from: ["displayRouteError"]
	                }
	            }
	        ]
	},

	smerchOverlayDestinationMultiFieldList: {
	    type: "tui/smerchOverlayPanel/view/DestinationMultiFieldList",
	    searchPanelModel: "$smerchOverlayModel",
	    widgetController: "$smerchOverlayController",
	    binds: [
	        {
	            target: "searchPanelModel.searchErrorMessages",
	            watch: {
	                fromTo: ["toggleEmptyAlert"],
	                to: ["displayToErrorMessage"]
	            }
	        }
	    ]
	},

	smerchOverlayDestinationGuide: {
	    type: "tui/searchPanel/view/DestinationGuide",
	    searchPanelModel: "$smerchOverlayModel",
	    widgetController: "$smerchOverlayController",
	    destinationGuideStore: "$destinationGuideStore",
	    binds: [
	        {
	            target: "searchPanelModel",
	            watch: {
	                date: ["updateGuideTitle"],
	                flexible: ["updateGuideTitle"]
	            }
	        }
	    ]
	},

	smerchOverlaySearchDatePicker: {
	    type: "tui/smerchOverlayPanel/view/SearchDatePicker",
	    searchPanelModel: "$smerchOverlayModel",
	    widgetController: "$smerchOverlayController",
	    searchConfig: "$getPriceConfig",
	    dateStore: "$smerchOverlayDateStore",
	    binds: [
	        {
	            target: "searchPanelModel.searchErrorMessages",
	            watch: {
	                when: ["displayDateErrorMessage"]
	            }
	        }
	    ]
	},

	smerchOverlayhowLong: {
	    type: "tui/searchPanel/view/HowLongOptions",
	    searchPanelModel: "$smerchOverlayModel",
	    widgetController: "$smerchOverlayController",
	    binds: [
	        {
	            target: "searchPanelModel",
	            watch: {
	                duration: ["updateHowLong"]
	            }
	        }
	    ]
	},

	smerchOverlayAdultSelectOption: {
	    type: "tui/searchPanel/view/AdultsSelectOption",
	    searchPanelModel: "$smerchOverlayModel",
	    widgetController: "$smerchOverlayController",
	    binds: [
	        {
	            target: "searchPanelModel",
	            watch: {
	                adults: ["updateAdultsView"]
	            }
	        },
	        {
	            target: "searchPanelModel.searchErrorMessages",
	            watch: {
	                partyComp: ["updatePartyCompError"]
	            }
	        }
	    ]
	},

	smerchOverlaySeniorsSelectOption: {
	    type: "tui/searchPanel/view/SeniorsSelectOption",
	    searchPanelModel: "$smerchOverlayModel",
	    widgetController: "$smerchOverlayController",
	    binds: [
	        {
	            target: "searchPanelModel",
	            watch: {
	                seniors: ["updateSeniorView"]
	            }
	        },
	        {
	            target: "searchPanelModel.searchErrorMessages",
	            watch: {
	                partyComp: ["updatePartyCompError"]
	            }
	        }
	    ]
	},

	smerchOverlayChildSelectOption: {
	    type: "tui/searchPanel/view/ChildSelectOption",
	    searchPanelModel: "$smerchOverlayModel",
	    widgetController: "$smerchOverlayController",
	    binds: [
	        {
	            target: "searchPanelModel",
	            watch: {
	                childAges: ["addChildAgeSelector"],
	                children: ["updateChildrenView"]
	            }
	        }
	    ]
	},

	smerchOverlaySubmitButton: {
	    type: "tui/searchPanel/view/SubmitButton",
	    searchPanelModel: "$smerchOverlayModel",
	    widgetController: "$smerchOverlayController",
	    formSelector: ".get-search-form"
	},

	/***********************************************
	*
	*   SMERCH RESULTS PAGE
	*
	************************************************/

	smerchDateSlider: {
	    type: "tui/smerchResults/view/smerchDateAvailabilitySlider",
	    searchConfig: "$searchConfig"
	  },

  /***********************************************
   *
   *   WHO IS GOING SEARCH
   *
   ************************************************/

  wigConfig: {
    type: "tui/searchPanel/config/SearchConfig",
    PERSISTED_SEARCH_PERIOD: 30,
    MAX_ADULTS_NUMBER: 9,
    INFANT_AGE: 2,
    FLEXIBLE_DAYS: 7,
    DATE_PATTERN: "EEE d MMMM yyyy",
    PAGE: 0,
    tag: '366',
    number: 1,
    scope: "config"
  },

  wigController: {
    type: "tui/search/controller/SearchController",
    searchConfig: "$wigConfig",
    searchPanelModel: "$wigModel",
    airportGuideStore: "$airportGuideStore",
    destinationGuideStore: "$destinationGuideStore",
    dateStore: "$dateStore",
    searchApi: "getPrice",
    tag: '366',
    number: 1,
    scope: "singleton"
  },

  wigModel: {
    type: "tui/searchPanel/model/SearchPanelModel",
    savedSearch: dojoConfig.site + "/search/main",
    view: "search",
    searchConfig: "$wigConfig",
    scope: "singleton"
  },

  wigAirportGuide: {
    type: "tui/searchPanel/view/AirportGuide",
    searchPanelModel: "$wigModel",
    widgetController: "$wigController",
    airportGuideStore: "$airportGuideStore",
    expandableProp: "expand-horizontal",
    targetSelector: ".wrapper",
    tag: '366',
    number: 1,
    columns: 2
  },

  wigAirportMultiFieldList: {
    type: "tui/searchPanel/view/AirportMultiFieldList",
    searchPanelModel: "$wigModel",
    widgetController: "$wigController",
    tag: '366',
    number: 1,
    binds: [
      {
        target: "searchPanelModel.searchErrorMessages",
        watch: {
          fromTo: ["displayFromToError"],
          from: ["displayRouteError"]
        }
      }
    ]
  },

  wigDestinationMultiFieldList: {
    type: "tui/searchPanel/view/DestinationMultiFieldList",
    searchPanelModel: "$wigModel",
    widgetController: "$wigController",
    tag: '366',
    number: 1,
    binds: [
      {
        target: "searchPanelModel.searchErrorMessages",
        watch: {
          fromTo: ["toggleEmptyAlert"],
          to: ["displayToErrorMessage"]
        }
      }
    ]
  },

  wigDestinationGuide: {
    type: "tui/searchPanel/view/DestinationGuide",
    searchPanelModel: "$wigModel",
    widgetController: "$wigController",
    destinationGuideStore: "$destinationGuideStore",
    tag: '366',
    number: 1,
    binds: [
      {
        target: "searchPanelModel",
        watch: {
          date: ["updateGuideTitle"],
          flexible: ["updateGuideTitle"]
        }
      }
    ]
  },

  wigSearchDatePicker: {
    type: "tui/searchPanel/view/SearchDatePicker",
    searchPanelModel: "$wigModel",
    widgetController: "$wigController",
    searchConfig: "$wigConfig",
    dateStore: "$dateStore",
    tag: '366',
    number: 1,
    binds: [
      {
        target: "searchPanelModel",
        watch: {
          date: ["setFormatedDate"]
        }
      },
      {
        target: "searchPanelModel.searchErrorMessages",
        watch: {
          when: ["displayDateErrorMessage"]
        }
      }
    ]
  },

  wigFlexibleView: {
    type: "tui/searchPanel/view/FlexibleView",
    searchPanelModel: "$wigModel",
    widgetController: "$wigController",
    tag: '366',
    number: 1,
    binds: [
      {
        target: "searchPanelModel",
        watch: {
          flexible: ["select"]
        }
      }
    ]
  },

  wigHowLong: {
    type: "tui/searchPanel/view/HowLongOptions",
    searchPanelModel: "$wigModel",
    widgetController: "$wigController",
    tag: '366',
    number: 1,
    binds: [
      {
        target: "searchPanelModel",
        watch: {
          duration: ["updateHowLong"]
        }
      }
    ]
  },

  wigAdultSelectOption: {
    type: "tui/searchPanel/view/AdultsSelectOption",
    searchPanelModel: "$wigModel",
    widgetController: "$wigController",
    tag: '366',
    number: 1,
    binds: [
      {
        target: "searchPanelModel",
        watch: {
          adults: ["updateAdultsView"]
        }
      },
      {
        target: "searchPanelModel.searchErrorMessages",
        watch: {
          partyComp: ["updatePartyCompError"]
        }
      }
    ]
  },

  wigSeniorsSelectOption: {
    type: "tui/searchPanel/view/SeniorsSelectOption",
    searchPanelModel: "$wigModel",
    widgetController: "$wigController",
    tag: '366',
    number: 1,
    binds: [
      {
        target: "searchPanelModel",
        watch: {
          seniors: ["updateSeniorView"]
        }
      },
      {
        target: "searchPanelModel.searchErrorMessages",
        watch: {
          partyComp: ["updatePartyCompError"]
        }
      }
    ]
  },

  wigChildSelectOption: {
    type: "tui/searchPanel/view/ChildSelectOption",
    searchPanelModel: "$wigModel",
    widgetController: "$wigController",
    tag: '366',
    number: 1,
    binds: [
      {
        target: "searchPanelModel",
        watch: {
          childAges: ["addChildAgeSelector"],
          children: ["updateChildrenView"]
        }
      }
    ]
  },

  wigSubmitButton: {
    type: "tui/searchPanel/view/SubmitButton",
    searchPanelModel: "$wigModel",
    widgetController: "$wigController",
    tag: '366',
    number: 1,
    formSelector: ".get-search-form"
  },

  /***********************************************
   *
  *   WHO IS GOING SEARCH -- varientB
  *
  ************************************************/

  wigControllerB: {
	    type: "tui/searchB/controller/SearchController",
	    searchConfig: "$wigConfig",
	    searchPanelModel: "$wigModelB",
	    airportGuideStore: "$searchBAirportGuideStore",
	    destinationGuideStore: "$searchBDestinationGuideStore",
	    dateStore: "$searchBDateStore",
	    searchApi: "getPrice",
	    tag: '366',
	    number: 1,
	    scope: "singleton"
	  },

	  wigModelB: {
	    type: "tui/searchBPanel/model/SearchPanelModel",
	    savedSearch: dojoConfig.site + "/search/main",
	    view: "search",
	    searchConfig: "$wigConfig",
	    scope: "singleton"
	  },

	  wigAirportGuideB: {
	    type: "tui/searchBPanel/view/AirportGuide",
	    searchPanelModel: "$wigModelB",
	    widgetController: "$wigControllerB",
	    airportGuideStore: "$searchBAirportGuideStore",
	    expandableProp: "expand-horizontal",
	    targetSelector: ".wrapper",
	    tag: '366',
	    number: 1,
	    columns: 2
	  },

	  wigAirportMultiFieldListB: {
	    type: "tui/searchBPanel/view/AirportMultiFieldList",
	    searchPanelModel: "$wigModelB",
	    widgetController: "$wigControllerB",
	    tag: '366',
	    number: 1,
	    binds: [
	      {
	        target: "searchPanelModel.searchErrorMessages",
	        watch: {
	          fromTo: ["displayFromToError"],
	          from: ["displayRouteError"]
	        }
	      }
	    ]
	  },

	  wigDestinationMultiFieldListB: {
	    type: "tui/searchBPanel/view/DestinationMultiFieldList",
	    searchPanelModel: "$wigModelB",
	    widgetController: "$wigControllerB",
	    tag: '366',
	    number: 1,
	    binds: [
	      {
	        target: "searchPanelModel.searchErrorMessages",
	        watch: {
	          fromTo: ["toggleEmptyAlert"],
	          to: ["displayToErrorMessage"]
	        }
	      }
	    ]
	  },

	  wigDestinationGuideB: {
	    type: "tui/searchBPanel/view/DestinationGuide",
	    searchPanelModel: "$wigModelB",
	    widgetController: "$wigControllerB",
	    destinationGuideStore: "$searchBDestinationGuideStore",
	    tag: '366',
	    number: 1,
	    binds: [
	      {
	        target: "searchPanelModel",
	        watch: {
	          date: ["updateGuideTitle"],
	          flexible: ["updateGuideTitle"]
	        }
	      }
	    ]
	  },

	  wigSearchDatePickerB: {
	    type: "tui/searchBPanel/view/SearchDatePicker",
	    searchPanelModel: "$wigModelB",
	    widgetController: "$wigControllerB",
	    searchConfig: "$wigConfig",
	    dateStore: "$searchBDateStore",
	    tag: '366',
	    number: 1,
	    binds: [
	      {
	        target: "searchPanelModel",
	        watch: {
	          date: ["setFormatedDate"]
	        }
	      },
	      {
	        target: "searchPanelModel.searchErrorMessages",
	        watch: {
	          when: ["displayDateErrorMessage"]
	        }
	      }
	    ]
	  },

	  wigFlexibleViewB: {
	    type: "tui/searchBPanel/view/FlexibleView",
	    searchPanelModel: "$wigModelB",
	    widgetController: "$wigControllerB",
	    tag: '366',
	    number: 1,
	    binds: [
	      {
	        target: "searchPanelModel",
	        watch: {
	          flexible: ["select"]
	        }
	      }
	    ]
	  },

	  wigHowLongB: {
	    type: "tui/searchBPanel/view/HowLongOptions",
	    searchPanelModel: "$wigModelB",
	    widgetController: "$wigControllerB",
	    tag: '366',
	    number: 1,
	    binds: [
	      {
	        target: "searchPanelModel",
	        watch: {
	          duration: ["updateHowLong"]
	        }
	      }
	    ]
	  },

	  wigAdultSelectOptionB: {
	    type: "tui/searchBPanel/view/AdultsSelectOption",
	    searchPanelModel: "$wigModelB",
	    widgetController: "$wigControllerB",
	    tag: '366',
	    number: 1,
	    binds: [
	      {
	        target: "searchPanelModel",
	        watch: {
	          adults: ["updateAdultsView"]
	        }
	      },
	      {
	        target: "searchPanelModel.searchErrorMessages",
	        watch: {
	          partyComp: ["updatePartyCompError"]
	        }
	      }
	    ]
	  },

	  wigSeniorsSelectOptionB: {
	    type: "tui/searchBPanel/view/SeniorsSelectOption",
	    searchPanelModel: "$wigModelB",
	    widgetController: "$wigControllerB",
	    tag: '366',
	    number: 1,
	    binds: [
	      {
	        target: "searchPanelModel",
	        watch: {
	          seniors: ["updateSeniorView"]
	        }
	      },
	      {
	        target: "searchPanelModel.searchErrorMessages",
	        watch: {
	          partyComp: ["updatePartyCompError"]
	        }
	      }
	    ]
	  },

	  wigChildSelectOptionB: {
	    type: "tui/searchBPanel/view/ChildSelectOption",
	    searchPanelModel: "$wigModelB",
	    widgetController: "$wigControllerB",
	    tag: '366',
	    number: 1,
	    binds: [
	      {
	        target: "searchPanelModel",
	        watch: {
	          childAges: ["addChildAgeSelector"],
	          children: ["updateChildrenView"]
	        }
	      }
	    ]
	  },

	  wigSubmitButtonB: {
	    type: "tui/searchBPanel/view/SubmitButton",
	    searchPanelModel: "$wigModelB",
	    widgetController: "$wigControllerB",
	    tag: '366',
	    number: 1,
	    formSelector: ".get-search-form"
	  },

  /***********************************************
   *
   *   SEARCH RESULTS
   *
   ************************************************/

  holidayDuration: {
    type: "tui/searchResults/view/HolidayDurationView"
  },

  holidayCount: {
    type: "tui/searchResults/view/HolidayCount"
  },

  sortResults: {
    type: "tui/searchResults/view/SortResultsSelectOption"
  },

  dateSlider: {
    type: "tui/searchResults/view/DateAvailabilitySlider",
    searchConfig: "$searchConfig"
  },


  multiUnitSearchResults: {
    type: "tui/searchResults/view/MultiUnitSearchResults",
    tag: "308",
    shortlistStore: "$shortlistStore"
  },

  searchResultsComponent: {
    type: "tui/searchResults/view/SearchResultsComponent",
    shortlistStore: "$shortlistStore"
  },

  searchResultsComponentThFc: {
	    type: "tui/searchResults/view/SearchResultsComponentThFc",
	    shortlistStore: "$shortlistStore"
	  },


    /***********************************************
   *
   *   SEARCH RESULTS -- VARIANT B
   *
   ************************************************/

  holidayDurationB: {
    type: "tui/searchBResults/view/HolidayDurationView"
  },

  holidayCountB: {
    type: "tui/searchBResults/view/HolidayCount"
  },

  sortResultsB: {
    type: "tui/searchBResults/view/SortResultsSelectOption"
  },

  multiUnitSearchBResults: {
    type: "tui/searchBResults/view/MultiUnitSearchResults",
    tag: "308B",
    shortlistStore: "$shortlistStore"
  },

//used when variant B of main search panel is loaded
  dateSliderB: {
      type: "tui/searchBResults/view/DateAvailabilitySlider",
      searchPanelModel: "$searchBPanelModel",
      searchConfig: "$searchConfig"
  },


  searchBResultsComponent: {
    type: "tui/searchBResults/view/SearchResultsComponent",
    tag: "308B",
    shortlistStore: "$shortlistStore"
  },



  cruiseSearchResultsComponent: {
        type: "tui/searchResults/view/VariantSearchResultsComponent"
        //shortlistStore: "$shortlistStore"
    },

  /***********************************************
   *
   *   ROOMS EDITOR(S)
   *
   ************************************************/

  roomsPopup: {
    type: "tui/searchResults/roomEditor/view/RoomsPopup",
    scope: "singleton"
  },

  roomsEditor: {
    type: "tui/searchResults/roomEditor/view/RoomsEditor",
    roomsEditorModel: "$roomsEditorModel",
    binds: [
      {
        target: "roomsEditorModel.searchErrorMessages",
        watch: {
          partyComp: ["displayErrorMessages", "removeErrorMessages"],
          partyChildAges: ["displayErrorMessages", "removeErrorMessages"],
          roomOccupancy: ["displayErrorMessages", "removeErrorMessages"]
        }
      }
    ],
    scope: "singleton"
  },

  roomsEditorModel: {
    type: "tui/searchResults/roomEditor/model/RoomsEditorModel",
    searchConfig: "$searchConfig",
    scope: "singleton"
  },

  roomsSummary: {
    type: "tui/searchResults/roomEditor/view/RoomsSummary",
    scope: "singleton"
  },

  /***********************************************
   *
   *   ROOMS EDITOR(S) -- Variant B
   *
   ************************************************/

  roomsPopupB: {
    type: "tui/searchBResults/roomEditor/view/RoomsPopup",
    scope: "singleton"
  },

  roomsEditorB: {
    type: "tui/searchBResults/roomEditor/view/RoomsEditor",
    roomsEditorModel: "$roomsEditorModelB",
    binds: [
      {
        target: "roomsEditorModel.searchErrorMessages",
        watch: {
          partyComp: ["displayErrorMessages", "removeErrorMessages"],
          partyChildAges: ["displayErrorMessages", "removeErrorMessages"],
          roomOccupancy: ["displayErrorMessages", "removeErrorMessages"]
        }
      }
    ],
    scope: "singleton"
  },

  roomsEditorModelB: {
    type: "tui/searchBResults/roomEditor/model/RoomsEditorModel",
    searchConfig: "$searchConfig",
    scope: "singleton"
  },

  roomsSummaryB: {
    type: "tui/searchBResults/roomEditor/view/RoomsSummary",
    scope: "singleton"
  },

  /***********************************************
  *
  *  Alternate room options -- book flow room options page
  *
  ************************************************/


  roomOptionsButtonToggler: {
	  type: "tui/widget/booking/changeroomallocation/view/RoomOptionsButtonToggler",
	  maxRoomsToDisplay: 6
  },

  /***********************************************
   *
   *   SHORTLIST
   *
   ************************************************/

  shortlistStore: {
    type: "tui/shortlist/store/ShortlistStore",
    maxItems: 10,
    targetUrls: {
      "add": dojoConfig.paths.webRoot + "/ws/shortlist/add",
      "remove": dojoConfig.paths.webRoot + "/ws/shortlist/remove",
      "list": dojoConfig.paths.webRoot + "/ws/shortlist/list"
    },
    scope: "singleton"
  },

  shortlistSummary: {
    type: "tui/shortlist/view/ShortlistSummary",
    shortlistStore: "$shortlistStore"
  },

  shortlistButton: {
    type: "tui/shortlist/view/ShortlistButton",
    shortlistStore: "$shortlistStore"
  },

  /***********************************************
   *
   *  FILTER PANEL WIRING
   *
   ************************************************/

  clearFilters: {
    type: 'tui/searchResults/view/ClearFilters'
  },

  filterController: {
    type: 'tui/filterPanel/view/FilterController',
    scope: "singleton"
  },

  cruiseBrowserFilter : {
	  type: 'tui/filterPanel/view/FilterController'
  },

  budgetToggle: {
    type: "tui/filterPanel/view/BudgetToggle"
    //searchResultsModel: "$searchResultsModel"
  },
/***********************************************
   *
   *  FILTER PANEL WIRING -- VARIANT B
   *
   ************************************************/

  clearFiltersB: {
    type: 'tui/searchBResults/view/ClearFilters'
  },

  filterControllerB: {
    type: 'tui/filterBPanel/view/FilterController',
    scope: "singleton"
  },

  budgetToggleB: {
    type: "tui/filterBPanel/view/BudgetToggle"
    //searchResultsModel: "$searchResultsModel"
  },

  /***********************************************
   *
   *  Flight Options
   *
   ************************************************/
  flightOptions: {
    type: "tui/singleAccom/view/FlightOptions",
    shortlistStore: "$shortlistStore"
  },

  /***********************************************
   *
   *   DealsGrp Show Packages
   *
   ************************************************/

  showDealsPackagesComponent: {
    type: "tui/showDealsPackages/view/ShowDealsPackagesComponent"
  },

  /***********************************************
   *
   *   InvGrp Filter Packages
   *
   ************************************************/

  clearIGFilters: {
    type: 'tui/showIGPackages/view/ClearFilters'
  },

  filterIGController: {
    type: 'tui/filterIGPanel/controller/FilterController'
  },

  /***********************************************
   *
   *   InvGrp Show Packages
   *
   ************************************************/

  showIGPackagesComponent: {
    type: "tui/showIGPackages/view/ShowIGPackagesComponent",
    scope: "singleton"
  },

  invGetPriceSubmitButton: {
    type: "tui/searchPanel/view/SubmitButton",
    searchPanelModel: "$getPriceModel",
    widgetController: "$getPriceController",
    formSelector: ".get-search-form"
    // mediator: "$getInvMediator"
    // packagesView: "$showIGPackagesComponent"
  },

  invGetPriceOverlayButton: {
    type: "tui/inventoryGetPrice/view/InventoryGetPriceModal",
    searchPanelModel: "$getPriceModel",
    scope: "singleton",
    mediator: "$getInvMediator"
  },
  getInvMediator: {
    type: "tui/showIGPackages/Mediator"
  },


  /***********************************************
  *
  *   InvGrp Show Packages -- Variant B
  *
  ************************************************/

 invGetPriceBSubmitButton: {
   type: "tui/searchBPanel/view/SubmitButton",
   searchPanelModel: "$getPriceBModel",
   widgetController: "$getPriceBController",
   formSelector: ".get-search-form"
   // mediator: "$getInvMediator"
   // packagesView: "$showIGPackagesComponent"
 },

 invGetPriceBOverlayButton: {
   type: "tui/inventoryGetPriceB/view/InventoryGetPriceModal",
   searchPanelModel: "$getPriceBModel",
   scope: "singleton",
   mediator: "$getInvMediator"
 },
  /***************************
   * Villa availability
   *
   ***************************/
  overlaySearchModal: {
    type: "tui/overlaySearch/OverlaySearchModal",
    searchConfig: "$getPriceConfig",
    searchPanelModel: "$getPriceModel",
    airportGuideStore: "$airportGuideStore",
    destinationGuideStore: "$destinationGuideStore",
    dateStore: "$dateStore",
    searchApi: "getPrice",
    widgetId: 'get-price-modal',
    tag: '364',
    number: 1,
    scope: "singleton"
  },

  availabilityFinderA:{
	  type:"tui/overlaySearch/AvailabilityFinder",
	 searchModal:"$overlaySearchModal",
	 searchConfig:"$getPriceConfig"
	 },
  villaAvailability: {
    type: "tui/villaAvailability/view/VillaAvailability",
    overlaySearch: "$overlaySearchModal",
    scope: "singleton"
  },
  /***************************
   * Villa availability -- varientB
   *
   ***************************/
  overlaySearchModalB: {
	    type: "tui/overlaySearchB/OverlaySearchModal",
	    searchConfig: "$getPriceConfig",
	    searchPanelModel: "$getPriceBModel",
	    airportGuideStore: "$searchBAirportGuideStore",
	    destinationGuideStore: "$searchBDestinationGuideStore",
	    dateStore: "$searchBDateStore",
	    searchApi: "getPrice",
	    widgetId: 'get-priceB-modal',
	    tag: '364',
	    number: 1,
	    scope: "singleton"
	  },

	  availabilityFinderB:{
		  type:"tui/overlaySearchB/AvailabilityFinder",
		 searchModal:"$overlaySearchModalB",
		 searchConfig:"$getPriceConfig"
		 },

	  villaAvailabilityB: {
	    type: "tui/villaAvailabilityB/view/VillaAvailability",
	    overlaySearch: "$overlaySearchModalB",
	    scope: "singleton"
  },
  deckPopup: {
      type: "tui/widget/popup/cruise/DeckPopup",
      scope: "singleton"
  },

  deckController: {
      type: "tui/cruise/deck/controller/DeckController",
      popup: "$deckPopup",
      scope: "prototype"
  },


    /*==========================Cruise Search Components==========================================*/
    cruiseSearchConfig: {
        type: "tui/searchPanel/config/SearchConfig",
        PERSISTED_SEARCH_PERIOD: 30,
        MAX_ADULTS_NUMBER: 9,
        MIN_ADULTS_NUMBER: 2,
        INFANT_AGE: 2,
        FLEXIBLE_DAYS: 7,
        DATE_PATTERN: "EEE d MMMM yyyy",
        PAGE: 1,
        HAS_SENIORS: false,
        scope: "config"
    },

    cruiseAirportGuideStore: {
        type: "tui/searchPanel/store/cruise/AirportGuideStore",
        targetURL: dojoConfig.paths.webRoot + "/ws/whereFrom",
        scope: "singleton"
    },

    cruiseDestinationGuideStore: {
        type: "tui/searchPanel/store/cruise/DestinationGuideStore",
        targetURL: dojoConfig.paths.webRoot + '/ws/whereto',
        targetURLForCountries: dojoConfig.paths.webRoot + '/ws/destinationguide',
        scope: "singleton"
    },

    cruiseDateStore: {
        type: "tui/searchPanel/store/cruise/DateStore",
        targetURL: dojoConfig.paths.webRoot + "/ws/when",
        scope: "singleton"
    },

    cruiseDurationStore: {
        type: "tui/searchPanel/store/cruise/DurationStore",
        targetURL: dojoConfig.paths.webRoot + "/ws/when",
        scope: "singleton"
    },

    casStore: {
        type: "tui/searchPanel/store/cruise/CruiseAndStayStore",
        targetURL: dojoConfig.paths.webRoot + "/ws/addAStay",
        scope: "singleton"
    },

    cruiseSearchPanelModel: {
        type: "tui/searchPanel/model/cruise/CruiseSearchPanelModel",
        savedSearch: dojoConfig.site + "/search/main",
        view: "search",
        searchConfig: "$cruiseSearchConfig",
        scope: "singleton"
    },

    cruiseSearchController: {
        type: "tui/search/controller/cruise/SearchController",
        searchConfig: "$cruiseSearchConfig",
        searchPanelModel: "$cruiseSearchPanelModel",
        airportGuideStore: "$cruiseAirportGuideStore",
        destinationGuideStore: "$cruiseDestinationGuideStore",
        dateStore: "$cruiseDateStore",
        searchApi: "searchPanel",
        scope: "singleton"
    },


    cruiseAutoComplete: {
        type: "tui/searchPanel/view/cruise/DestinationAutoComplete",
        searchPanelModel: "$cruiseSearchPanelModel",
        widgetController: "$cruiseSearchController",
        destinationGuide: "$cruiseDestinationGuide",
        binds: [
            {
                target: "searchPanelModel.searchErrorMessages",
                watch: {
                    to: ["displayToErrorMessage"]
                }
            }
        ]
    },

    cruiseAirportMultiFieldList: {
        type: "tui/searchPanel/view/cruise/AirportMultiFieldList",
        searchPanelModel: "$cruiseSearchPanelModel",
        guide: "$cruiseAirportGuide",
        binds: [
            {
                target: "searchPanelModel.searchErrorMessages",
                watch: {
                    fromTo: ["displayFromToError"],
                    from: ["displayRouteError"]
                }
            }]
    },


    cruiseFlexibleView: {
        type: "tui/searchPanel/view/FlexibleView",
        searchPanelModel: "$cruiseSearchPanelModel",
        widgetController: "$cruiseSearchController",
        number: 1,
        binds: [
            {
                target: "searchPanelModel",
                watch: {
                    flexible: ["select"]
                }
            }
        ]
    },

    cruiseAirportGuide: {
        type: "tui/searchPanel/view/cruise/AirportGuide",
        searchPanelModel: "$cruiseSearchPanelModel",
        airportGuideStore: "$cruiseAirportGuideStore",
        widgetController: "$cruiseSearchController",
        binds: [
            {
                target: "searchPanelModel.searchErrorMessages",
                watch: {
                    fromTo: ["displayFromToError"],
                    from: ["displayRouteError"]
                }
            }]
    },

    cruiseDestinationGuide: {
        type: "tui/searchPanel/view/cruise/DestinationGuide",
        searchPanelModel: "$cruiseSearchPanelModel",
        destinationGuideStore: "$cruiseDestinationGuideStore",
        widgetController: "$cruiseSearchController",
        binds: [
            {
                target: "searchPanelModel.searchErrorMessages",
                watch: {
                    fromTo: ["toggleEmptyAlert"],
                    to: ["displayToErrorMessage"]
                }
            }
        ],
        scope: "singleton"
    },

    cruiseDatePicker: {
        type: "tui/searchPanel/view/cruise/DatePicker",
        searchPanelModel: "$cruiseSearchPanelModel",
        searchConfig: "$cruiseSearchConfig",
        store: "$cruiseDateStore",
        tag: '300',
        number: 1,
        addDefault:true,
        arrow:false,
        binds: [
            {
                target: "searchPanelModel.searchErrorMessages",
                watch: {
                    when: ["displayDateErrorMessage"]
                }
            }
        ]
    },

    cruiseAndStayPicker: {
        type: "tui/searchPanel/view/cruise/CruiseAndStayPicker",
        searchPanelModel: "$cruiseSearchPanelModel",
        tag: '300',
        cruiseAndStayPickerStore: "$casStore",
        widgetController: "$cruiseSearchController"
    },


    cruiseAdultSelectionOption: {
        type: "tui/searchPanel/view/AdultsSelectOption",
        searchPanelModel: "$cruiseSearchPanelModel",
        arrow:false,
        widgetController: "$cruiseSearchController",
        binds: [
            {
                target: "searchPanelModel",
                watch: {
                    adults: ["updateAdultsView"]
                }
            },
            {
                target: "searchPanelModel.searchErrorMessages",
                watch: {
                    partyComp: ["updatePartyCompError"]
                }
            }
        ]
    },

    cruiseChildSelectOption: {
        type: "tui/searchPanel/view/ChildSelectOption",
        searchPanelModel: "$cruiseSearchPanelModel",
        widgetController: "$cruiseSearchController",
        arrow:false,
        binds: [
            {
                target: "searchPanelModel",
                watch: {
                    childAges: ["addChildAgeSelector"],
                    children: ["updateChildrenView"]
  }
            }
        ]
    },


    cruiseSubmitButton: {
        type: "tui/searchPanel/view/cruise/CruiseSubmitButton",
        searchPanelModel: "$cruiseSearchPanelModel",
        widgetController: "$cruiseSearchController",
        formSelector: ".search-form"
    },

    cruiseSearchSummary: {
        type: "tui/searchPanel/view/cruise/CruiseSearchSummary",
        widgetController: "$cruiseSearchController",
        searchPanelModel: "$cruiseSearchPanelModel",
        searchConfig: "$cruiseSearchConfig"
    },

    durationPicker: {
        type: "tui/searchPanel/view/cruise/DurationPicker",
        searchPanelModel: "$cruiseSearchPanelModel",
        tag: '300',
        number: 1,
        store: "$cruiseDurationStore",
        targetURL: dojoConfig.paths.webRoot + "/ws/durations",
        addDefault:true,
        arrow:false,
        binds: [
                {
                    target: "searchPanelModel.searchErrorMessages",
                    watch: {
                        duration: ["displayDurationErrorMessage"]
                    }
                }
       ]
    },


    /*==========================END of Cruise Search components===========================================*/

    /*========================== Cruise GET Price widgets =================================*/

    cruiseGetPriceConfig: {
        type: "tui/searchPanel/config/SearchConfig",
        PERSISTED_SEARCH_PERIOD: 30,
        MIN_ADULTS_NUMBER: 2,
        MAX_ADULTS_NUMBER: 9,
        INFANT_AGE: 2,
        FLEXIBLE_DAYS: 7,
        DATE_PATTERN: "EEE d MMMM yyyy",
        PAGE: 0,
        HAS_SENIORS: false,
        tag: '364',
        number: 1,
        scope: "config"
    },

    cruiseGetPriceController: {
        type: "tui/search/controller/cruise/SearchController",
        searchConfig: "$cruiseGetPriceConfig",
        searchPanelModel: "$cruiseGetPriceSearchPanelModel",
        widgetController: "$cruiseChangePaxController",
        searchApi: "getPrice",
        scope: "singleton"
    },

    cruiseGetPriceSearchPanelModel: {
        type: "tui/searchPanel/model/cruise/CruiseSearchPanelModel",
        savedSearch: dojoConfig.site + "/search/main",
        view: "search",
        searchConfig: "$cruiseGetPriceConfig",
        scope: "singleton"
    },

    cruiseGetPriceModal: {
        type: 'tui/searchGetPrice/view/cruise/GetPriceModal',
        searchPanelModel: '$cruiseGetPriceSearchPanelModel',
        scope: 'singleton'
    },

    cruiseGetPriceAirportGuide: {
        type: "tui/searchPanel/view/cruise/AirportGuide",
        searchPanelModel: "$cruiseGetPriceSearchPanelModel",
        widgetController: "$cruiseGetPriceController",
        airportGuideStore: "$cruiseAirportGuideStore",
        expandableProp: "expand-horizontal",
        targetSelector: ".cruise-wraper",
        tag: '364',
        number: 1,
        columns: 2,
        binds: [
            {
                target: "searchPanelModel.searchErrorMessages",
                watch: {
                    fromTo: ["displayFromToError"],
                    from: ["displayRouteError"]
                }
            }]
    },

    cruiseGetPriceAirportMultiFieldList: {
        type: "tui/searchPanel/view/cruise/AirportMultiFieldList",
        searchPanelModel: "$cruiseGetPriceSearchPanelModel",
        guide: "$cruiseGetPriceAirportGuide",
        widgetController: "$cruiseGetPriceController",
        tag: '364',
        number: 1,
        binds: [
            {
                target: "searchPanelModel.searchErrorMessages",
                watch: {
                    fromTo: ["displayFromToError"],
                    from: ["displayRouteError"]
                }
            }
        ]
    },

    cruiseGetPriceDestinationMultiFieldList: {
        type: "tui/searchPanel/view/cruise/DestinationMultiFieldList",
        searchPanelModel: "$cruiseGetPriceSearchPanelModel",
        widgetController: "$cruiseGetPriceController",
        tag: '364',
        number: 1,
        binds: [
            {
                target: "searchPanelModel.searchErrorMessages",
                watch: {
                    fromTo: ["toggleEmptyAlert"],
                    to: ["displayToErrorMessage"]
                }
            }
        ]
    },

    cruiseGetPriceDestinationGuide: {
        type: "tui/searchPanel/view/cruise/DestinationGuide",
        searchPanelModel: "$cruiseGetPriceSearchPanelModel",
        widgetController: "$cruiseGetPriceController",
        destinationGuideStore: "$cruiseDestinationGuideStore",
        tag: '364',
        number: 1,
        binds: [
            {
                target: "searchPanelModel",
                watch: {
                    date: ["updateGuideTitle"],
                    flexible: ["updateGuideTitle"]
                }
            }
        ]
    },

    cruiseGetPriceSearchDatePicker: {
        type: "tui/searchPanel/view/cruise/DatePicker",
        searchPanelModel: "$cruiseGetPriceSearchPanelModel",
        widgetController: "$cruiseGetPriceController",
        searchConfig: "$cruiseGetPriceConfig",
        store: "$cruiseDateStore",
        addDefault:true,
        tag: '364',
        number: 1,
        binds: [
            {
                target: "searchPanelModel.searchErrorMessages",
                watch: {
                    when: ["displayDateErrorMessage"]
                }
            }
        ]
    },
    cruiseGetPriceStayPicker: {
        type: "tui/searchPanel/view/cruise/CruiseAndStayPicker",
        searchPanelModel: "$cruiseGetPriceSearchPanelModel",
        widgetController: "$cruiseGetPriceController",
        cruiseAndStayPickerStore: "$casStore",
        expandableProp:"expand-horizontal",
        tag: '364',
        number: 1
    },

    cruiseGetPriceFlexibleView: {
        type: "tui/searchPanel/view/FlexibleView",
        searchPanelModel: "$cruiseGetPriceSearchPanelModel",
        widgetController: "$cruiseGetPriceController",
        tag: '364',
        number: 1,
        binds: [
            {
                target: "searchPanelModel",
                watch: {
                    flexible: ["select"]
                }
            }
        ]
    },

    cruiseGetPriceDurationPicker: {
        type: "tui/searchPanel/view/cruise/DurationPicker",
        searchPanelModel: "$cruiseGetPriceSearchPanelModel",
        tag: '364',
        number: 1,
        store: "$cruiseDurationStore",
        targetURL: dojoConfig.paths.webRoot + "/ws/durations",
        addDefault:true,
        binds: [
                {
                    target: "searchPanelModel.searchErrorMessages",
                    watch: {
                        duration: ["displayDurationErrorMessage"]
                    }
                }
        ]
    },

    cruiseGetPriceAdultSelectOption: {
        type: "tui/searchPanel/view/AdultsSelectOption",
        searchPanelModel: "$cruiseGetPriceSearchPanelModel",
        widgetController: "$cruiseGetPriceController",
        tag: '364',
        number: 1,
        binds: [
            {
                target: "searchPanelModel",
                watch: {
                    adults: ["updateAdultsView"]
                }
            },
            {
                target: "searchPanelModel.searchErrorMessages",
                watch: {
                    partyComp: ["updatePartyCompError"]
                }
            }
        ]
    },



    cruiseGetPriceChildSelectOption: {
        type: "tui/searchPanel/view/ChildSelectOption",
        searchPanelModel: "$cruiseGetPriceSearchPanelModel",
        widgetController: "$cruiseGetPriceController",
        tag: '364',
        number: 1,
        binds: [
            {
                target: "searchPanelModel",
                watch: {
                    childAges: ["addChildAgeSelector"],
                    children: ["updateChildrenView"]
                }
            }
        ]
    },

    cruiseGetPriceSubmitButton: {
        type: "tui/searchPanel/view/cruise/CruiseSubmitButton",
        searchPanelModel: "$cruiseGetPriceSearchPanelModel",
        widgetController: "$cruiseGetPriceController",
        tag: '364',
        number: 1,
        formSelector: ".get-search-form"
    },
    /*========================== Cruise GET Price widgets =================================*/


    /*========================== Cruise Pax change widgets =================================*/

    cruiseChangePaxController: {
        type: "tui/search/controller/cruise/SearchController",
        searchConfig: "$cruiseGetPriceConfig",
        searchPanelModel: "$cruiseGetPriceSearchPanelModel",
        airportGuideStore: "$cruiseAirportGuideStore",
        destinationGuideStore: "$cruiseDestinationGuideStore",
        dateStore: "$cruiseDateStore",
        searchApi: "getPrice",
        tag: '365',
        number: 1,
        scope: "singleton"
    },

    cruiseChangePaxAdultSelectOption: {
        type: "tui/searchPanel/view/AdultsSelectOption",
        searchPanelModel: "$cruiseGetPriceSearchPanelModel",
        widgetController: "$cruiseChangePaxController",
        tag: '365',
        number: 1,
        binds: [
            {
                target: "searchPanelModel",
                watch: {
                    adults: ["updateAdultsView"]
                }
            },
            {
                target: "searchPanelModel.searchErrorMessages",
                watch: {
                    partyComp: ["updatePartyCompError"]
                }
            }
        ]
    },

    cruiseChangePaxChildSelectOption: {
        type: "tui/searchPanel/view/ChildSelectOption",
        searchPanelModel: "$cruiseGetPriceSearchPanelModel",
        widgetController: "$cruiseChangePaxController",
        tag: '365',
        number: 1,
        binds: [
            {
                target: "searchPanelModel",
                watch: {
                    childAges: ["addChildAgeSelector"],
                    children: ["updateChildrenView"]
                }
            }
        ]
    },

    itineryPaxSubmitButton: {
	    type: "tui/searchPanel/view/ChangePaxSubmitButton",
	    searchPanelModel: "$cruiseGetPriceSearchPanelModel",
	    widgetController: "$cruiseChangePaxController",
	    tag: '365',
        number: 1
	  },



    /*========================== Cruise Pax change widgets =================================*/




	/*========================== Flights Search components START =================================*/

	foSearchController: {
		type: "tui/search/controller/flights/SearchController",
		searchConfig: "$searchConfig",
		searchPanelModel: "$foSearchPanelModel",
		airportGuideStore: "$foAirportGuideStore",
		arrivalAirportGuideStore: "$foArrivalAirportGuideStore",
		destinationGuideStore: "$foDestinationGuideStore",
		dateStore: "$dateStore",
		returnDateStore: "$foReturnDateStore",
		searchApi: "searchPanel",
		scope: "singleton"
	},

	foDestinationGuideStore: {
	    type: "tui/searchPanel/store/DestinationGuideStore",
	    targetURL: dojoConfig.paths.webRoot + '/ws/suggestions',
	    targetURLForCountries: dojoConfig.paths.webRoot + '/ws/destinationguide',
	    scope: "singleton"
	},

	foSearchPanelModel: {
		type: "tui/searchPanel/model/flights/SearchPanelModel",
		savedSearch: dojoConfig.site + "/search/main",
		view: "search",
		searchConfig: "$searchConfig",
		scope: "singleton"
	},

	foAirportGuideStore: {
	    type: "tui/searchPanel/store/AirportGuideStore",
	    targetURL: dojoConfig.paths.webRoot + "/ws/departureairport",
	    scope: "singleton"
  	},

	foArrivalAirportGuideStore: {
		type: "tui/searchPanel/store/flights/ArrivalAirportGuideStore",
		targetURL: dojoConfig.paths.webRoot + "/ws/arrivalairport",
	    scope: "singleton"
	},

	foReturnDateStore: {
		type: "tui/searchPanel/store/flights/ReturnDateStore",
		targetURL: dojoConfig.paths.webRoot + "/ws/travelreturndates",
		scope: "singleton"
	},

	foAdultSelectionOption: {
		type: "tui/searchPanel/view/flights/AdultsSelectOption",
		searchPanelModel: "$foSearchPanelModel",
		widgetController: "$foSearchController",
		 maxHeight: 240,
		binds: [
		  {
			target: "searchPanelModel",
			watch: {
			  adults: ["updateAdultsView"]
			}
		  },
		  {
			target: "searchPanelModel.searchErrorMessages",
			watch: {
			  partyComp: ["updatePartyCompError"]
			}
		  }
		]
	},

	foAirportGuide: {
		type: "tui/searchPanel/view/flights/AirportGuide",
		searchPanelModel: "$foSearchPanelModel",
		airportGuideStore: "$foAirportGuideStore",
		widgetController: "$foSearchController"
	},

	foAirportListOverlay: {
		type: "tui/searchPanel/view/flights/AirportListOverlay",
		searchPanelModel: "$foSearchPanelModel",
		airportGuide: "$foAirportGuide",
		widgetController: "$foSearchController",
		airportGuideStore: "$foAirportGuideStore",
		binds: [
				{
				  target: "searchPanelModel.searchErrorMessages",
				  watch: {
					  fromTo: ["displayFromToError"]
				  }
				}
			  ]
	},

	foArrivalAirportListOverlay: {
		type: "tui/searchPanel/view/flights/ArrivalAirportListOverlay",
		searchPanelModel: "$foSearchPanelModel",
		arrivalAirportGuide: "$foArrivalAirportGuide",
		widgetController: "$foSearchController",
		arrivalAirportGuideStore: "$foArrivalAirportGuideStore",
		binds: [
				{
				  target: "searchPanelModel.searchErrorMessages",
				  watch: {
					  emptyTo:["displayToError"]
				  }
				}
			  ]
	},


	foAirportMultiFieldList: {
		type: "tui/searchPanel/view/flights/AirportMultiFieldList",
		searchPanelModel: "$foSearchPanelModel",
		widgetController: "$foSearchController",
		binds: [
			{
			  target: "searchPanelModel.searchErrorMessages",
			  watch: {
				from: ["displayRouteError"],
				invalidDepartureAirportCombination: ["displayInvalidDepartureAirportCombinationError"],
				invalidDepartureandArrivalAirportCombination: ["displayInvalidDepartureandArrivalAirportCombinationError"]
			  }
			}
		  ]
    },

	foArrivalAirportGuide: {
		type: "tui/searchPanel/view/flights/ArrivalAirportGuide",
		searchPanelModel: "$foSearchPanelModel",
		arrivalAirportGuideStore: "$foArrivalAirportGuideStore",
		widgetController: "$foSearchController"
	},


	 foArrivalAirportMultiFieldList: {
		type: "tui/searchPanel/view/flights/ArrivalAirportMultiFieldList",
	    searchPanelModel: "$foSearchPanelModel",
	    widgetController: "$foSearchController",
	   binds: [
	        {
	          target: "searchPanelModel.searchErrorMessages",
	          watch: {
	            to: ["displayRouteError"],
	            invalidArrivalAirportCombination: ["displayInvalidArrivalAirportCombinationError"],
	            invalidFromAndToFlyingCombination: ["displayInvalidFromAndToFlyingCombinationError"]
	          }
	        }
	      ]
	    },



	foArrivalSearchDatePicker: {
		type: "tui/searchPanel/view/flights/ArrivalSearchDatePicker",
		searchPanelModel: "$foSearchPanelModel",
		widgetController: "$foSearchController",
		searchConfig: "$searchConfig",
		returnDateStore: "$foReturnDateStore",
		binds: [
		  {
			target: "searchPanelModel",
			watch: {
			  returnDate: ["setReturnFormatedDate"]
			}
		  },
		  {
			target: "searchPanelModel.searchErrorMessages",
			watch: {
				returnTravel: ["displayReturnDateErrorMessage"]
			}
		  }
		]
	},

	foDestinationGuide: {
		type: "tui/searchPanel/view/flights/DestinationGuide",
		searchPanelModel: "$foSearchPanelModel",
		destinationGuideStore: "$foDestinationGuideStore",
		widgetController: "$foSearchController",
		binds: [
		  {
			target: "searchPanelModel",
			watch: {
			  date: ["updateGuideTitle"],
			  flexible: ["updateGuideTitle"]
			}
		  }
		]
	},

	foDestinationMultiFieldList: {
		type: "tui/searchPanel/view/flights/DestinationMultiFieldList",
		searchPanelModel: "$foSearchPanelModel",
		widgetController: "$foSearchController",
		binds: [
		  {
			target: "searchPanelModel.searchErrorMessages",
			watch: {
			  fromTo: ["toggleEmptyAlert"],
			  to: ["displayToErrorMessage"]
			}
		  }
		]
	},

	foOneWayOnly: {
	    type: "tui/searchPanel/view/flights/OneWayOnly",
	    searchPanelModel: "$foSearchPanelModel",
	    widgetController: "$foSearchController",
	    binds: [
	            {
	              target: "searchPanelModel",
	              watch: {
	            	  oneWayOnly: ["selectOneWay"]
	              }
	            }
	          ]
	  },

	foSearchDatePicker: {
		type: "tui/searchPanel/view/flights/SearchDatePicker",
		searchPanelModel: "$foSearchPanelModel",
		widgetController: "$foSearchController",
		searchConfig: "$searchConfig",
		dateStore: "$dateStore",
		binds: [
		  {
			target: "searchPanelModel",
			watch: {
			  date: ["setFormatedDate"]
			}
		  },
		  {
			target: "searchPanelModel.searchErrorMessages",
			watch: {
			  when: ["displayDateErrorMessage"]
			}
		  }
		]
	},

	foSeniorsSelectOption: {
		type: "tui/searchPanel/view/flights/SeniorsSelectOption",
		searchPanelModel: "$foSearchPanelModel",
		widgetController: "$foSearchController",
		binds: [
		  {
			target: "searchPanelModel",
			watch: {
			  seniors: ["updateSeniorView"]
			}
		  },
		  {
			target: "searchPanelModel.searchErrorMessages",
			watch: {
			  partyComp: ["updatePartyCompError"]
			}
		  }
		]
	},

	foSubmitButton: {
		type: "tui/searchPanel/view/flights/SubmitButton",
		searchPanelModel: "$foSearchPanelModel",
		widgetController: "$foSearchController",
		formSelector: ".search-form"
   },

  foChildSelectOption: {
	    type: "tui/searchPanel/view/ChildSelectOption",
	    searchPanelModel: "$foSearchPanelModel",
	    widgetController: "$foSearchController",
	    maxHeight: 240,
	    binds: [
	      {
	        target: "searchPanelModel",
	        watch: {
	          childAges: ["addChildAgeSelector"],
	          children: ["updateChildrenView"]
	        }
	      }
	    ]
  },

  foFlexibleView: {
	    type: "tui/searchPanel/view/FlexibleView",
	    searchPanelModel: "$foSearchPanelModel",
	    widgetController: "$foSearchController",
	    binds: [
	      {
	        target: "searchPanelModel",
	        watch: {
	          flexible: ["select"]
	        }
	      }
	    ]
  },

  foSearchResultController :{
	  type: "tui/searchResults/view/flights/FlightSearchResultController",
	  searchPanelModel: "$foSearchPanelModel"
  },
  foNoSearchResultController :{
	  type: "tui/searchResults/view/flights/NoSearchResultsController",
	  searchPanelModel: "$foSearchPanelModel"
  },
  foDealsController :{
	  type: "tui/flightdeals/FlightDealsController",
	  searchPanelModel: "$foSearchPanelModel"
  }



	/*========================== Flights Search components END ====================================*/


});