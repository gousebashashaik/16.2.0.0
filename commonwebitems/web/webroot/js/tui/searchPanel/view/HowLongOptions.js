define("tui/searchPanel/view/HowLongOptions", [
	"dojo",
	"tui/widget/form/SelectOption"], function (dojo) {

	dojo.declare("tui.searchPanel.view.HowLongOptions", [tui.widget.form.SelectOption], {

		// ----------------------------------------------------------------------------- methods

		postCreate: function () {
			var howLong = this;
			howLong.inherited(arguments);
			howLong.disableLapLand();
			howLong.addTripClass();
			howLong.addDropdownEventlistener();
			howLong.tagElement(howLong.domNode, "duration");
			howLong.subscribe("tui/searchPanel/searchOpening", function (component) {
				if (component !== howLong && howLong.listShowing) {
					howLong.hideList();
				}
			});
		},
		
		disableLapLand: function(){
			var howLong = this;
			_.each(howLong.listItems, function(itm, ind){
				if( itm.innerHTML == "Day Trip"){
					itm.style.display = "none";
					dojo.addClass(itm, "lapLandDDLi");
					dojo.addClass(dojo.query(itm).parent()[0], "howLongDD");
				}
			});
		},

		addTripClass:function(){
			
			var howLong = this;
			_.each(howLong.listItems, function(itm, ind){
				if( itm.innerHTML.toLowerCase() == "a few days"){
					
					dojo.addClass(itm, "aFewDayTripLi");					
				}
			});
			
		},

		updateHowLong: function (name, oldValue, newValue) {
			// stateful watcher method
			var howLong = this;
			if (parseInt(howLong.getSelectedData().value, 10) !== newValue) {
				howLong.setSelectedValue(newValue);
			}
		},

		addDropdownEventlistener: function () {
			var howLong = this;
			howLong.inherited(arguments);
			howLong.connect(howLong, "onChange", function (name, oldValue, newValue) {
				howLong.searchPanelModel.set("duration", parseInt(newValue.value, 10));
			});
		}
	});

	return tui.searchPanel.view.HowLongOptions;
});