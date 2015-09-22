define ("tui/widget/TimeSlider", ["dojo", "dojo/html", "tui/widget/Slider", "dojo/date/locale" ], function(dojo){
	
	dojo.declare("tui.widget.TimeSlider", [tui.widget.Slider], {
		
		
		//---------------------------------------------------------------- slider methods
		timePattern: "H:m",
		
		postMixInProperties: function(){
			// summary:
			//		Override postMixInProperties method.
			// description:
			//		Initalises slider properties with default variables. 
			var timeslider = this;
			for (var i = 0; i < timeslider.range.length; i++){				
				var time = dojo.date.locale.parse(timeslider.range[i],{
					selector: "time", 
					timePattern: timeslider.timePattern 
				});
				
				timeslider.range[i] = (time.getHours() * 60) + time.getMinutes();
			}
			timeslider.inherited(arguments);
		},
				
		renderValue: function(value){
			var timeslider = this;
			var abs = (value / 60);
			var hour = parseInt(abs);
			var min = (parseFloat(abs) - parseInt(abs)) * 60;
			min = Math.round(min);
			min = (min <  10)? [0, min].join("") : min;
			return [hour, ":" , min].join("")
		}
	})
	
	return tui.widget.TimeSlider;
})