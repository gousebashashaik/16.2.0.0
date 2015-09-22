define ("tui/widget/SwitchButton", ["dojo", 
									"tui/widget/_TuiBaseWidget",
									"dojo/fx"], function(dojo){
	
	dojo.declare("tui.widget.SwitchButton", [tui.widget._TuiBaseWidget], {
		
		_checkbox: null,
		
		_text: null,
		
		_state: "on",
		
		// ---------------------------------------------------------------- switchbutton method
		
		postCreate: function(){
			var switchButton = this;
			switchButton._checkbox = dojo.query("input[type='checkbox']", switchButton.domNode)[0];
			switchButton.setState("on");
			switchButton._text = dojo.query(".text", switchButton.domNode)[0];
			switchButton.connect(switchButton.domNode, "onclick", function(event){
				dojo.stopEvent(event)
				switchButton.buttonClick();
			})	
			switchButton.inherited(arguments);
		},
		
		buttonClick: function(){
			var switchButton = this;
			var state = (!switchButton._checkbox.checked) ? "on" : "off";
			switchButton.setState(state);
			switchButton.animateButtonState();
		},
		
		getValue: function(){
			var switchButton = this;
			return switchButton._checkbox.value;
		},
		
		setValue: function(value){
			var switchButton = this;
			switchButton._checkbox.value = value;
		},
		
		getState: function(){
			var switchButton = this;
			return switchButton._state;
		},
		
		setState: function(state){
			var switchButton = this;
			switchButton._checkbox.checked = (state === "on") ? true : false;
			switchButton._state = state;
		},
		
		animateButtonState: function(){
			var switchButton = this;
			var end = (switchButton._state === "on") ? 0 : -38;
			dojo.animateProperty({
   				node: switchButton._text,
   				properties: {
    				left: {end: end} 
    			}, onEnd: function(){
    				switchButton.onbuttonclick(switchButton.getValue(), 
    										   switchButton.getState(), 
    										   switchButton);
    			}
			}).play();
		},
		
		// ---------------------------------------------------------------- switchbutton event
		
		onbuttonclick: function(value, state, switchButton){
			
		}
	})
	
	return tui.widget.SwitchButton;
})