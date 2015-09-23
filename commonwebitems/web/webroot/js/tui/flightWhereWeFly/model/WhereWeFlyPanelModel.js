define("tui/flightWhereWeFly/model/WhereWeFlyPanelModel",[
	"dojo/_base/declare",
	"dojo/query",
	"dojo/dom",
	"dojo/Stateful",
	"dojo/store/Memory",
	"dojo/store/Observable",
	"tui/utils/LocalStorage",
	 "dijit/registry",
	 "dojo/cookie",
	 "tui/flightWhereWeFly/model/WhereWeFlyModel"
	],function(declare, query, dom, Stateful, Memory, Observable, localStorage, registry , cookie ){

	declare("tui.flightWhereWeFly.model.WhereWeFlyPanelModel",[tui.flightWhereWeFly.model.WhereWeFlyModel ],{

	    from: [],

	    to: null,

	    date: null,

	    returnDate :  null,

		constructor: function (props) {
			 	var whereWeFlyPanelModel = this;
			 		dojo.mixin(whereWeFlyPanelModel, props);
		},


	    doXhrPost: function(url, json){
	    	var whereWeFlyPanelModel = this,jsonData=null,serviceCall;
	    		if(json){
	    			jsonData = whereWeFlyPanelModel.createQueryObjForServerCall();
	    		}

	    		serviceCall = dojo.xhrPost({
				url: url,
				handleAs: "json",
				content: {
					"jsonData":jsonData
				}
			});

	    	return serviceCall;
	    },
	    doXhrGet: function(url){
	    	var whereWeFlyPanelModel = this,serviceCall=null;
	    	serviceCall = dojo.xhrGet({
				url: url,
				handleAs: "json"
			});

	    	return serviceCall;
	    },
	    itemList : function(airport){
	    	var whereWeFlyPanelModel = this,
	    		airportID = [];

	    	_.each(airport, function (item) {
	    		airportID.push(item.id);
	          });

	    	return airportID.join(",");
	    },


	    reqDateFormat : function(dateObj){
			var searchController = this,
				dt = new Date(dateObj);

			return dojo.date.locale.format(dt, {
		          selector: "date",
		          datePattern: "dd-MM-yyyy"
		        });
		},


	    createQueryObjForServerCall: function(){
	    	var whereWeFlyPanelModel = this;

		    	return dojo.toJson({
				});

	},

	parserJsonData: function(){
		var whereWeFlyPanelModel = this;

		return properties  ={
    		"fromAirports"			: (whereWeFlyPanelModel.from) ? whereWeFlyPanelModel.itemList(whereWeFlyPanelModel.from) : "",
			"toAirports" 			: (whereWeFlyPanelModel.to) ? whereWeFlyPanelModel.to : "",
			"when"					: (whereWeFlyPanelModel.date) ? whereWeFlyPanelModel.reqDateFormat(whereWeFlyPanelModel.date) : "",
			"returnDate"			: (whereWeFlyPanelModel.returnDate) ? whereWeFlyPanelModel.reqDateFormat(whereWeFlyPanelModel.returnDate) : ""
		};
	}




	});
	return tui.flightWhereWeFly.model.WhereWeFlyPanelModel;
});