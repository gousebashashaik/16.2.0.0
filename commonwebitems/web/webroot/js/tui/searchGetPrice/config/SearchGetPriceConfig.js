define("tui/searchGetPrice/config/SearchGetPriceConfig", ["dojo"], function (dojo) {

	var config = dojo.getObject("tui.searchGetPrice.config", true);

	config.GetPriceControllerConfig =  [{
		id:'searchGetPriceConfig',
		name:'searchConfig',
		ref:'tui.searchPanel.config.SearchConfig',
		props:{
			PERSISTED_SEARCH_PERIOD: 30,
			MAX_ADULTS_NUMBER: 9,
			INFANT_AGE: 2,
			FLEXIBLE_DAYS: 7,
			DATE_PATTERN: 'EEE d MMMM yyyy'
		},
		type:'singleton'
	},{
		id:'getSearchPanelModel',
		name:'searchPanelModel',
		ref:'tui.searchPanel.model.SearchPanelModel',
		props:{
			classConfig:[{
				name:'searchConfig',
				ref:'searchConfig'
			}],
			savedSearch: 'search/main',
			view: 'search'
		},
		type:'singleton'
	},{
		id:'getAirportGuideStore',
		name:'airportGuideStore',
		ref:'tui.searchPanel.store.AirportGuideStore',
		type:'singleton'
	},{
		id:'getDateStore',
		name:'dateStore',
		ref:'tui.searchPanel.store.DateStore',
		type:'singleton'
	}];


	config.AirportMultifieldConfig = [{
		name:'searchPanelModel',
		ref: 'getSearchPanelModel',
		bind: [{
			property: 'searchErrorMessages',
			watch: {'fromTo':'displayFromToError'}
		},{
			property: 'searchErrorMessages',
			watch: {'from':'displayRouteError'}
		}]
	}]

	config.AirportGuideConfig = [{
		name:'searchPanelModel',
		ref: 'getSearchPanelModel'
	},{
		name:'airportGuideStore',
		ref:'getAirportGuideStore'
	}]

	config.DatepickerConfig = [{
		name:'searchPanelModel',
		ref: 'getSearchPanelModel',
		bind: [{
			watch: {
				'date': 'setFormatedDate'
			}
		},{
			property: 'searchErrorMessages',
			watch: {
				'when': 'displayDateErrorMessage'
			}
		}]
	},{
		name:'dateStore',
		ref:"getDateStore"
	}]

	config.FlexibleConfig = [{
		name:'searchPanelModel',
		ref: 'getSearchPanelModel',
		bind: [{
			watch: {
				'flexible': 'select'
			}
		}]
	}]

	config.HowLongConfig = [{
		name:'searchPanelModel',
		ref: 'getSearchPanelModel',
		bind: [{
			watch: {
				'duration': 'updateHowLong'
			}
		}]
	}]

	config.AdultsConfig = [{
		name:'searchPanelModel',
		ref: 'getSearchPanelModel',
		bind: [{
			watch: {
				'adults': 'updateAdultsView'
			}
		}]
	}]

	config.SeniorConfig = [{
		name:'searchPanelModel',
		ref: 'getSearchPanelModel',
		bind: [{
			watch: {
				'seniors': 'updateSeniorView'
			}
		},{
			property: 'searchErrorMessages',
			watch: {
				'partyComp': 'updatePartyCompError'
			}
		}]
	}]

	config.ChildrenConfig = [{
		name:'searchPanelModel',
		ref: 'getSearchPanelModel',
		bind: [{
			watch: {
				'childAges': 'addChildAgeSelector',
				'children':'updateChildrenView'
			}
		}]
	}]

	config.SubmitConfig = [{
		name:'searchPanelModel',
		ref: 'getSearchPanelModel'
	}]

});


