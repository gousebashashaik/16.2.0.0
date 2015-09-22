define("tui/searchPanel/view/flights/OneWayOnly", [
    "dojo",
    "dojo/on",
    "tui/widget/_TuiBaseWidget"],
    function (dojo, on){
	dojo.declare("tui.searchPanel.view.flights.OneWayOnly", [tui.widget._TuiBaseWidget], {
		
		checkbox: null,
		postCreate: function () {
           	var oneWayOnly = this;
           	oneWayOnly.checkbox = dojo.query("[type='checkbox']", oneWayOnly.domNode)[0];
			on(oneWayOnly.checkbox, "click", function (event) {
				oneWayOnly.setOneWayOnly();
			});
			oneWayOnly.inherited(arguments);
			oneWayOnly.tagElement(oneWayOnly.domNode, "Onewayonly");
		} ,
	
		setOneWayOnly: function () {
		var oneWayOnly = this;
		var checked = dojo.attr(oneWayOnly.checkbox, "checked");
		if(checked){
			oneWayOnly.searchPanelModel.returnDate="";
		}
		oneWayOnly.searchPanelModel.set("oneWayOnly", checked);
	},
	
	selectOneWay: function (name, oldAnswer, answer) {
		var oneWayOnly = this;
		if (dojo.attr(oneWayOnly.checkbox, "checked") === answer) {
			return;
		}
		if (answer) {
			dojo.attr(oneWayOnly.checkbox, "checked", "checked");
		} else {
			dojo.removeAttr(oneWayOnly.checkbox, "checked");
		}
	}
});
	
	
return  tui.searchPanel.view.flights.OneWayOnly

});