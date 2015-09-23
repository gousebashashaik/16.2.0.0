define("tui/flightWhereWeFly/model/WhereWeFlyModel",[
	"dojo",
	"dojo/_base/declare",
	"dojo/Stateful",
	"dojo/store/Memory",
	"tui/search/nls/Searchi18nable",
	"tui/search/base/SearchValidationBase"
	],function(dojo, declare, Stateful , Memory, i18){

	declare("tui.flightWhereWeFly.model.WhereWeFlyModel",[ dojo.Stateful, tui.search.nls.Searchi18nable, tui.search.base.SearchValidationBase, dojo.store.Memory],{

		searchConfig: null,

		constructor: function(){
			var dealsModel = this;
				dealsModel.initSearchMessaging();
		}
	});
	return tui.flightWhereWeFly.model.WhereWeFlyModel;
});