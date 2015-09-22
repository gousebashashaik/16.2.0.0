define("tui/searchBPanel/view/HowLongOptions", [
	"dojo",
	"dojo/on",
	"tui/widget/form/SelectOption"], function (dojo, on) {

	dojo.declare("tui.searchBPanel.view.HowLongOptions", [tui.widget.form.SelectOption], {

		// ----------------------------------------------------------------------------- methods

		postCreate: function () {
			var howLong = this;
			howLong.inherited(arguments);
			dojo.addClass(howLong.listElement, howLong.domNode.id+"_HLDD");
			howLong.disableLapLand();
			howLong.addTripClass();
			if( howLong.domNode.id == "get-totalDuration"){
				howLong.dayTripFlag = true;
			}
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
			//howLong.daytripFlipper(newValue);
		},
		
		//Howlong option flipper in Daytrip page  
		daytripFlipper: function(newValue){
			var howLong = this;
			if( !howLong.dayTripFlag ){
				return;
			}
			howLong.dayTripFlag = false;		 
			if(howLong.widgetController.locationData && howLong.widgetController.locationData.units && howLong.widgetController.locationData.units[0].multiSelect.toString() == "false"){
	    		dojo.query("ul.howLongDD li.lapLandDDLi", howLong.listElement).style("display", "block");
	    		dojo.query("ul.howLongDD li.lapLandDDLi", howLong.listElement).addClass("active");
	    		on.emit(dojo.query("ul.howLongDD li.lapLandDDLi", howLong.listElement)[0], "mouseover", {
						bubbles: true,
						cancelable: true
					});
				on.emit(dojo.query("ul.howLongDD li.lapLandDDLi", howLong.listElement)[0], "click", {
						bubbles: true,
						cancelable: true
					});
			}
			
			if( dojo.query("div#get-totalDuration .dropdown .value")[0] ){
				
				var txt = dojo.query("div#get-totalDuration .dropdown .value").text();
				if( txt == "Day Trip"){
					dojo.query("ul.howLongDD li.lapLandDDLi", howLong.listElement).style("display", "block");
				}
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

	return tui.searchBPanel.view.HowLongOptions;
});