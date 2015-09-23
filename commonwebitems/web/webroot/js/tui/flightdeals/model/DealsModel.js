define("tui/flightdeals/model/DealsModel",[
	"dojo",
	"dojo/_base/declare",
	"dojo/Stateful",
	"dojo/store/Memory",
	"tui/search/nls/Searchi18nable",
	"tui/search/base/SearchValidationBase"
	],function(dojo, declare, Stateful , Memory, i18){

	declare("tui.flightdeals.model.DealsModel",[ dojo.Stateful, tui.search.nls.Searchi18nable, tui.search.base.SearchValidationBase, dojo.store.Memory],{

		searchConfig: null,

		constructor: function(){
			var dealsModel = this;
				dealsModel.initSearchMessaging();
		}
	});
	return tui.flightdeals.model.DealsModel;
});