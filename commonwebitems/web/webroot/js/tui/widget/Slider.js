define ("tui/widget/Slider", ["dojo", 

							  "dojo/html", 
							  "tui/widget/_TuiBaseWidget", 
							  "tui/widgetFx/MoveableLimit", 
							  "tui/widgetFx/HorizontalMover", 
							  "tui/widgetFx/VerticalMover",
							  "tui/widget/mixins/FloatPosition"], function(dojo){
	
	/***********************************************************************************/
   	/* tui.widget.Slider													           */
   	/***********************************************************************************/
   	
	dojo.declare("tui.widget.Slider", [tui.widget._TuiBaseWidget, 
									   tui.widget.mixins.FloatPosition], {
		
		// summary:
		//		Class for creating a Slider. 
		//
		// description:
		//		This class define the behaviour needed to create a slider. 
		//
		// @author: Maurice Morgan.
		
		// Slider direction type.
		type: "Horizontal",
		
		// An array refernce containing tui/widgetFx/MoveableLimit objects. 
		// These object manages the drag movement of the slider handles.
		moveable: [],
		
		// Array containing the DOM elements of slider handles. 
		// Element 0 references left handle, Element 1 references right handle.
		handles: [],
		
		
		currentStepElements: [],
		
		// DOM element of slider track elapse.
		elapse: null,
		
		// Value for offset range.
		offset: 0,
		
		// Value to offset handles. If slider is in Horizontal mode. 
		// This value is used to offset the top, else its used to offset
		// the left. 
		handleOffest: -5,
		
		// Number of steps movement for slide. 
		steps: null,
		
		// Array containing the slider range.
		range: false,
		
		// Array containing current step of slider handles.
		step: null,
		
		// Boolean for enabling handles snaping. 
		snap: false,
		
		// Boolean for displaying slider makers values.
		displayMaker: false,
		
		// Template for value item.
		valueTmpl: "${value}",
		
		showMarkerValue: true,
		
		//---------------------------------------------------------------- slider methods
		
		postMixInProperties: function(){
			
			// summary:
			//		Override postMixInProperties method.
			//
			// description:
			//		Initalises slider properties with default variables. 
			
			var slider = this;
			slider.type = String(slider.type).replace(/\b[a-z]/g, function(match){
				return match.toUpperCase();
			});
			
			slider.handles = [];
			slider.moveable = [];
			slider.currentStepElements = [];
			
			switch (slider.type){
				case 'Vertical':
					slider.axis = 'y';
					slider.property = 'top';
					slider.trackproperty = 'height';
					slider.offsetproperty = 'offsetHeight';
				break;
				case 'Horizontal':
					slider.axis = 'x';
					slider.property = 'left';
					slider.trackproperty = 'width';
					slider.offsetproperty = 'offsetWidth';
				break;
			}
		},
		
		postCreate: function(){
			
			// summary:
			//		Override postCreate method, this is called once widget is added to DOM 
			//		making the domNode variable available.
			//
			// description:
			//		Once slider has been added to the DOM. We position handles, and attach
			//		event listeners.
			
			var slider = this;
			slider.elapse = dojo.query(".elapse", slider.domNode)[0];
			slider.addHandlersEventListeners();
			slider.setSliderDimensions();
			slider.setRange(slider.range);
			slider.attachMoverToHandlers();
			slider.setSliderTrack();
			slider.addTracksEventListeners();
			slider.showMarkers();
		},
		
		
		showMarkers: function() {
			var slider = this;
			slider.marker = dojo.create("ul", null, slider.domNode, "last");
			var li = dojo.create("li", {
				innerHTML : slider.renderValue(slider.min),
				style : {
					"left" : "3px"
				}
			}, slider.marker, "first");
	
			slider.onFirstMarkerItemRender(li, 3, slider.min);

			if (slider.displayMaker) {
				for (var i = 0; i < slider.steps - 1; i++) {
					var pos = (i + 1) * slider.stepWidth;
					var li = dojo.create("li", {
						"class" : (i + 1 === slider.steps) ? "" : "marker",
						"innerHTML" : (i + 1 === slider.steps) ? slider.renderValue(slider.max) : slider.renderMarker((slider.min + 1) + i),
						"style" : {
							"left" : [pos, "px"].join("")
						}
					}, slider.marker, "last");
					slider.onMarkerItemRender(li, pos, (slider.min + 1) + i);
				}
			}
			
			var pos = slider.steps * slider.stepWidth;
			var li = dojo.create("li", {
				innerHTML : slider.renderValue(slider.max),
				style : {
					"left" : [pos, "px"].join("")
				}
			}, slider.marker, "last");
			slider.onLastMarkerItemRender(li, pos, slider.steps);
		},
		
		setSliderTrack: function(){
			var slider = this;
			if (slider.handles.length === 2){
				var hlleft  = dojo.style(slider.handles[0], "left");
				var hrleft  = dojo.style(slider.handles[1], "left");
				dojo.style(slider.elapse, {
					left:[hlleft, "px"].join(""),
					width: [hrleft - hlleft, "px"].join("")
				});
				slider.updateLimit(hlleft, hrleft);
			} else { 
				var left  = dojo.style(slider.handles[0], "left");
				dojo.style(slider.elapse, {
					width: [left, "px"].join("")
				});
			}
		},
		
		updateLimit: function(hlleft, hrleft){
			var slider = this;
			var limit = slider.moveable[0].limit;
			limit[slider.axis] = [limit[slider.axis][0], hrleft];
			limit = slider.moveable[1].limit;
			limit[slider.axis] = [hlleft, limit[slider.axis][1]];
		},
	
		setSliderDimensions: function(){
			// summary:
			//		Set the slider dimensions.
			// description:
			//		Working out the slider dimension, taking the silder handle size and any offset.
			var slider = this;
			slider.half = slider.handles[0][slider.offsetproperty] / 2;
			slider.full = slider.domNode[slider.offsetproperty] - slider.handles[0][slider.offsetproperty] + (slider.offset * 2);
			slider.containerfull = slider.domNode[slider.offsetproperty] - 2;
		},
		
		setRange: function(range){
			// summary:
			//		Sets slider ranges, mix max and steps.
			// description:
			//		Works out the slider range, min, max steps values.  
			var slider = this;
			slider.min = 0;
			slider.max = slider.full;
			// if a range is speficied use values.
			if (range){
				slider.min = range[0] || slider.min;
				slider.max = range[1] || slider.max;
			} 
			//console.log( range[0])
			slider.step = slider.step || [slider.min, slider.max];
			slider.range =  Math.abs(slider.max - slider.min);
			slider.steps = slider.steps || slider.max;
			slider.stepSize =slider.range / slider.steps;
			slider.stepWidth = slider.stepSize * slider.full / slider.range;
			slider.markerWidth = slider.stepSize * slider.containerfull / slider.range;
		},
		
		setSnap: function(options){
			// summary:
			//		Sets slider to snap.
			//
			// description:
			//		We set the slider range, and slider steps using given values passed.
			var slider = this;
			options.gridStep = Math.ceil(slider.stepWidth);
			options.limit[slider.axis][1] = slider.full;
			return slider;
		}, 
		
		setStep: function(step, handlePos){
			// summary:
			//		Sets slider set
			//
			// description:
			//		We set the slider range, and slider steps using given values passed.
			var slider = this;
			if (!((slider.range > 0) ^ (step < slider.min))) step = slider.min;
			if (!((slider.range > 0) ^ (step > slider.max))) step = slider.max;
			var direction = [0, 0];
			var newStep = Math.round(step);
			if (newStep > slider.step[handlePos]){
				direction[handlePos] = 1
			}
			
			if (newStep < slider.step[handlePos]){
				direction[handlePos] = -1
			}
			var lastStep = dojo.clone(slider.step);
			slider.step[handlePos] = newStep;
			slider.setHandlePosition(slider.toPosition(slider.step[handlePos]), handlePos);
			slider.change(handlePos, slider.step[handlePos], lastStep, slider.step, direction);
		},
		
		setHandlePosition: function(/* */ position, /* */ index){
			// summary:
			//		Sets slider handlers position.
			// description:
			//		We 
			var slider = this;
			if (slider.snap) position = slider.toPosition(slider.step[index]);
			dojo.style(slider.handles[index], slider.property, [position, "px"].join(""));
		},
		
		toPosition: function(step){
			var slider = this;
			return (slider.full * Math.abs(slider.min - step)) / (slider.steps * slider.stepSize) - slider.offset;
		},
			
		toStep: function(position){
			var slider = this;
			var step = (position /*+ slider.offset*/) * slider.stepSize / slider.full * slider.steps;
			return slider.steps ? Math.round(step -= step % slider.stepSize) : step;
		},
		
		addHandlersEventListeners: function(){
			var slider = this;
			
			dojo.query(".handle", slider.domNode)
				.forEach(function(element, index){
					dojo.style(element, 'position', 'absolute');
					dojo.attr(element, 'tabindex', 0);	
					dojo.connect(element, "onclick", function(event){
						element.focus();
					})
					
					dojo.connect(element, "onkeydown", function(event){
						if (slider.isDragging) return;
						
						if (event.keyCode === dojo.keys.LEFT_ARROW){
							var step = slider.step[index] - slider.stepSize;
							slider.setStep(step, index);
							slider.setSliderTrack();	
							slider.onEnd(i, slider.step[i],  slider.step);
						}
						
						if (event.keyCode === dojo.keys.RIGHT_ARROW){
							var step = slider.step[index] + slider.stepSize;
							slider.setStep(index, slider.step[index],  slider.step);
							slider.setSliderTrack();				
							slider.onEnd(index, slider.step[index],  slider.step);
						}
					})	
					slider.handles.push(element);
				})
		},
		
		attachMoverToHandlers: function(){
			var slider = this;
			var limit = {};
			limit[slider.axis] = [-slider.offset, slider.full - slider.offset];
			var options = {
				 mover: tui.widgetFx[[slider.type, "Mover"].join("")],
				 host: slider,
				 limit: limit,
				 offset:{t: (slider.axis === 'x') ? slider.handleOffest : 0, 
				 		 l: (slider.axis === 'x') ? 0 : slider.handleOffest}
			}
			if (slider.snap) slider.setSnap(options);
		
			_.forEach(slider.handles, function(handle, i){
				options.handlePos = i;
				slider.moveable[i] = new tui.widgetFx.MoveableLimit(handle, options);
				dojo.connect(slider.moveable[i], "onMoveStop", slider, function(e, leftTop){
					slider.isDragging = false;
					slider.onEnd(i, slider.step[i],  slider.step);
				});
				dojo.connect(slider.moveable[i], "onMoveStart", slider, function(e, leftTop){
					slider.isDragging = true;
				})
				slider.setStep(slider.step[i], i);
			})
		},
		
		addTracksEventListeners: function(){
			var slider = this;
			if (slider.isDragging) return;
			var position = dojo.position(slider.domNode, true)
			var leftTop = position[slider.axis];
			var widthHeight = position[slider.trackproperty[0]];
			slider.connect(slider.domNode, "onmousedown", function(event){
				dojo.stopEvent(event);
				if (slider.isDragging) return;
				var client = ["client", slider.axis.toUpperCase()].join("");
				var pos = event[client] - leftTop;
				pos = (slider.snap) ? (Math.round(pos / Math.ceil(slider.stepWidth)) * Math.ceil(slider.stepWidth)) : pos;
				var index = 0;
				if (slider.handles.length === 2){
					index = (pos > widthHeight / 2) ? 1 : 0;
				}
				pos = (pos > slider.moveable[index].limit[slider.axis][1]) ? 
						slider.moveable[index].limit[slider.axis][1] : pos;						
				slider.updateHandlePosition(pos, index);
				slider.onEnd(index, slider.step[index],  slider.step);
			})
			slider.connect(window, "onresize", function(event){
				dojo.stopEvent(event);
				for (var i = 0; i < slider.handles.length; i++){
					slider.updateSelectedValues(i, slider.step[i]);
				}
			})
		},
		
		updateHandlePosition: function(value, index){
			var slider = this;
			var dir = slider.range[index] < 0 ? -1 : 1;
			var step = Math.round(slider.min + dir * slider.toStep(value));
			slider.setStep(step, index);
			slider.setSliderTrack();
		},
		
		updateSelectedValues: function(handlePos, value){
			var slider = this;
			var handle = slider.handles[handlePos];
			var action = (value === slider.step[1]) ? "addClass" : "removeClass";
			dojo[action](handle, "last");
			if(slider.showMarkerValue){
				dojo.html.set(handle, ["<span>", slider.renderValue(value) ,"</span>" ].join(""));
			}
		},
		
		renderMarker: function(){
			return "|";
		},
		
		renderValue: function(value){
			var slider = this;
			var txt = dojo.string.substitute(slider.valueTmpl, {
				value: value
			})
			return txt;
		},
		
		onFirstMarkerItemRender: function(li){
			
		},
		
		onLastMarkerItemRender: function(li){
			
		},
		
		onMarkerItemRender: function(li){
	
		},
		
		onMoveStop: function(){
			
		},
		
		onEnd: function(handlePos, value, step){

		},
		
		change: function(handlePos, value, oldStep, newStep, dir){
			var slider = this;
			slider.updateSelectedValues(handlePos, value);
			slider.onChange(handlePos, value, oldStep, newStep, dir);
			return slider;
		},
		
		onChange: function(handlePos, value, oldStep, newStep, dir){
			
		},
		
		onMove: function(moveable, leftTop){
			var slider = this;
			var firstChar = slider.property.slice(0,1);
			var position = leftTop[firstChar];
			//console.log(position)
			var dir = slider.range[moveable.handlePos] < 0 ? -1 : 1;
			for (var i = 0; i < slider.handles.length; i++){
				dojo.style(slider.handles[i], "zIndex", 0);
			}
			slider.updateHandlePosition(position, moveable.handlePos);
			dojo.style(slider.handles[moveable.handlePos], "zIndex", 1);
		}
	})
	
	
	
	
	
	
	
	
	
	/***********************************************************************************/
   	/* tui.widget.TimeSlider													       */
   	/***********************************************************************************/
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
	
	return tui.widget.Slider;
})