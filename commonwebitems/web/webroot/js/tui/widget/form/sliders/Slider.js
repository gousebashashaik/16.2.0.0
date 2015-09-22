define("tui/widget/form/sliders/Slider", [
    "dojo",
    "tui/widget/form/sliders/SliderMoveable",
    "tui/widgetFx/HorizontalMover",
    "tui/widgetFx/VerticalMover",
    "dojo/html",
    "tui/widget/_TuiBaseWidget",
    "tui/widget/mixins/FloatPosition"], function (dojo, SliderMoveable) {

    dojo.declare("tui.widget.form.sliders.Slider", [tui.widget._TuiBaseWidget, tui.widget.mixins.FloatPosition], {

        // ----------------------------------------------------------------------------- properties

        // description:
        //		This class define the behaviour needed to create a slider.
        // @author: Maurice Morgan.

        // Slider direction type.
        type: "Horizontal",

        // An array reference containing tui/widgetFx/MoveableLimit objects.
        // These object manages the drag movement of the slider handles.
        moveable: [],

        // Array containing the DOM elements of slider handles.
        // Element 0 references left handle, Element 1 references right handle.
        handles: [],

        //
        currentStepElements: [],

        //
        axis: 'x',

        //
        property: 'left',

        //
        trackproperty: 'width',

        trackType: "maxRange",

        //
        offsetproperty: 'offsetWidth',

        // DOM element of slider track elapse.
        elapse: null,

        track: null,

        // Value for offset range.
        offset: 0,

        // Value to offset handles. If slider is in Horizontal mode.
        // This value is used to offset the top, else its used to offset
        // the left.
        handleOffest: -5,

        // Number of steps movement for slide.
        steps: null,

        // The number of step in the slider.
        range: false,

        // Array containing current step of slider handles.
        step: null,

        // Boolean for enabling handles snaping.
        snap: true,

        // Boolean for displaying slider makers values.
        displayMaker: false,

        // Template for value item.
        valueTmpl: "${value}",

        // Boolean for displaying marker value.
        showMarkerValue: true,

        handleConstraint: null,

        limit: null,

        direction: null,

        fixedFraction: 2,

        seperator: '|',
		
		startPos: '3px',
		

        // ----------------------------------------------------------------------------- methods

        resetState: function () {
            // summary:
            //		Override postMixInProperties method.
            //
            // description:
            //		Initialises slider properties with default variables.
            var slider = this;


            // Put the first letter to uppercase if necessary. Required?
            slider.type = ('' + slider.type).replace(/^[a-z]/g, function (match) {
                return match.toUpperCase();
            });

            slider.moveable = [];
            slider.handles = [];
            slider.currentStepElements = [];
            if (slider.limit == null) {
                slider.limit = [];
            }
            slider.direction = [0, 0];


            if (slider.type === 'Vertical') {
                slider.axis = 'y';
                slider.property = 'top';
                slider.trackproperty = 'height';
                slider.offsetproperty = 'offsetHeight';
            } else {
                // Horizontal (default value)
                slider.axis = 'x';
                slider.property = 'left';
                slider.trackproperty = 'width';
                slider.offsetproperty = 'offsetWidth';
            }


        },

        postMixInProperties: function () {
            var slider = this;
            slider.resetState();
            slider.inherited(arguments);
        },

        postCreate: function () {
            // summary:
            //		Override postCreate method, this is called once widget is added to DOM
            //		making the domNode variable available.
            //
            // description:
            //		Once slider has been added to the DOM. We position handles, and attach
            //		event listeners.
            var widget = this;

            widget.inherited(arguments);
            widget.drawSlider();

        },

        drawSlider: function (options) {
            var slider = this;
            slider.resetState();
            slider.clearMarkers();
            if (options) {
                dojo.mixin(slider, options);
            }
            slider.elapse = dojo.query(".elapse", slider.domNode)[0];
            slider.track = dojo.query(".track", slider.domNode)[0];
            slider.handles = [];
            slider.addHandlersEventListeners();
            if (slider.handleConstraint === null) {
                slider.handleConstraint = (slider.handles.length > 1) ? 1 : 0;
            }
            if (slider.handles.length > 1) {
                slider.offset = 7;
                slider.trackType = "range"
            }
            if (slider.trackType === "minRange") {
                slider.offset = 7;
            }
            slider.setSliderDimensions();
            slider.setRange(slider.range);
            slider.updateStepRange();
            slider.attachMoverToHandlers();
            slider.setSliderTrack();
            slider.showMarkers();
            slider.findLimitMarkers();
            slider.addTracksEventListeners();
        },

        clearMarkers: function () {
            var slider = this;
            if (slider.domNode) {
                _.each(dojo.query('ul', slider.domNode), function (marker) {
                    dojo.destroy(marker);
                });
            }
        },

        showMarkers: function () {
            // summary:
            //		Creates and displays, slider marker point based on given range and steps.
            //		Markers will only be displayed if "displayMaker" is set to true.
            var slider = this;
            var pos, li, i;

            slider.marker = dojo.create("ul", null, slider.domNode, "last");
            li = dojo.create("li", {
                innerHTML: slider.renderValue(slider.min),
                style: {"left": slider.startPos}
            }, slider.marker, "first");

            slider.onFirstMarkerItemRender(li, 3, slider.min);

            if (slider.displayMaker) {
                for (i = 0; i < slider.steps - 1; i++) {
                    pos = (i + 1) * slider.stepWidth;
                    li = dojo.create("li", {
                        "class": (i + 1 === slider.steps) ? "" : "marker",
                        "innerHTML": (i + 1 === slider.steps) ? slider.renderValue(slider.max) :
                                     slider.renderMarker((slider.min + 1) + i),
                        "style": {"left": pos + "px"}
                    }, slider.marker, "last");
                    slider.onMarkerItemRender(li, pos, (slider.min + 1) + i);
                }
            }

            pos = slider.steps * slider.stepWidth;
            li = dojo.create("li", {
                innerHTML: slider.renderValue(slider.max),
                style: {"left": pos + "px"}
            }, slider.marker, "last");
            slider.onLastMarkerItemRender(li, pos, slider.steps);
        },

        setSliderTrack: function () {
            // summary:
            //		Set the slider track based on the step array.
            var slider = this;
            if (slider.trackType === "range" || slider.trackType === "minRange") {
                var hlleft = slider.toPosition(slider.step[0]) + slider.offset - slider.offset * 2;
                var hrleft = slider.step[1] ? slider.toPosition(slider.step[1]) + slider.offset :
                             slider.toPosition(slider.max) + slider.offset * 2;
                dojo.style(slider.elapse, {left: hlleft + "px", width: Math.abs((hrleft - hlleft)) + "px"});
            } else {
                var left = slider.toPosition(slider.step[0]);
                dojo.style(slider.elapse, {width: left + "px"});
            }
        },

        setLimit: function (limit) {
            var slider = this;
            slider.limit = limit;
            slider.findLimitMarkers();
        },

        findLimitMarkers: function () {
            var slider = this;
            if (slider.limit && slider.limit.length > 0 &&
                slider.limit.join("") != [slider.min, slider.max].join("")) {
                var markers = dojo.query(".marker", slider.domNode);
                markers.children("span").remove();
                slider.disableLimitMarker(markers[Math.ceil((slider.limit[0] / slider.stepSize)) - 2]);
                slider.disableLimitMarker(markers[Math.ceil((slider.limit[1] / slider.stepSize)) - 2], "end");
            }
        },

        disableLimitMarker: function (marker, classname) {
            var slider = this;
            if (!marker) return;
            classname = classname || "";
            dojo.place("<span class='disable' + classname></span>", marker, "first");
        },

        setSliderDimensions: function () {
            // summary:
            //		Set the slider dimensions.
            // description:
            //		Working out the slider dimension, taking the silder handle size and any offset.
            var slider = this;
            slider.half = slider.handles[0][slider.offsetproperty] / 2;
            slider.full = slider.domNode[slider.offsetproperty] - slider.handles[0][slider.offsetproperty]
            slider.containerfull = slider.domNode[slider.offsetproperty] - 2;
        },

        setRange: function (range) {
            // summary:
            //		Sets slider ranges, mix max and steps.
            // description:
            //		Works out the slider range, min, max steps values.
            var slider = this;
            slider.min = 0;
            slider.max = slider.full;

            // if a range is speficied use values.
            if (range) {
                slider.min = range[0] || slider.min;
                slider.max = range[1] || slider.max;
            }

            slider.step = slider.step || [slider.min, slider.max];
            slider.range = Math.abs(slider.max - slider.min);
            slider.steps = slider.steps || slider.max;
            slider.stepSize = slider.range / slider.steps;
            slider.stepWidth = slider.stepSize * slider.full / slider.range;
            slider.markerWidth = slider.stepSize * slider.containerfull / slider.range;
        },

        setStepsByValues: function (values) {
            var slider = this;
            _.each(values, function (value, position) {
                slider.setStep(value, position);
            });
        },

        setSteps: function (steps) {
            var slider = this;
            _.each(steps, function (step, position) {
                slider.setStep(step, position);
            });
        },

        setStep: function (step, handlePos) {
            var slider = this;

            if (step < (slider.limit[0] || slider.min)) step = (slider.limit[0] || slider.min);
            if (step > (slider.limit[1] || slider.max)) step = (slider.limit[1] || slider.max);

            var newStep = step;
            (newStep >= slider.step[handlePos]) ? slider.direction[handlePos] = 1 : slider.direction[handlePos] = -1;

            slider.lastStep = dojo.clone(slider.step);

            var offset = (handlePos === 0) ? slider.offset - slider.offset * 2 : slider.offset;

            if (slider.isHandleConstraint(handlePos, slider.direction[handlePos])) {
                slider.setHandlePosition(slider.toPosition(slider.lastStep[handlePos]) + offset, handlePos);
            } else {
                slider.step[handlePos] = newStep;
                slider.setHandlePosition(slider.toPosition(slider.step[handlePos]) + offset, handlePos);
                slider.change(handlePos, slider.step[handlePos], slider.lastStep, slider.step, slider.direction);
            }

            slider.updateStepRange();

            slider.setSliderTrack();
        },

        updateStepRange: function () {
            var slider = this;
            slider.stepRange = Math.abs(slider.step[0] - (slider.step[1] || slider.max));
        },

        isHandleConstraint: function (handlePos, dir) {
            var slider = this;
            var constraint = (slider.stepRange === (slider.handleConstraint * slider.stepSize));
            if ((constraint && handlePos === 0 && dir === 1) || (constraint && handlePos === 1 && dir === -1)) {
                return true;
            }
            // default false
            return false;
        },

        setHandlePosition: function (/* */position, /* */index) {
            // summary:
            //		Sets slider handlers position.
            var slider = this;
            dojo.style(slider.handles[index], slider.property, position + "px");
        },

        toPosition: function (step) {
            var slider = this;
            return (slider.full * Math.abs(slider.min - step)) / (slider.steps * slider.stepSize);
        },

        toStep: function (position, dir) {
            // summary:
            //		Work out the steps based on the given pixel position.
            var slider = this;
            var step = (position * slider.stepSize / slider.full) * slider.steps;
            if(!dir) {
                return slider.snap ? (step -= step % slider.stepSize) : step;
            }
            return slider.snap ? Math.ceil(step) : step;
        },

        addHandlersEventListeners: function () {
            // summary:
            //		Add keyEvent listener to slider handles to allow slider selection via
            //		arrow keys.
            var slider = this;
            dojo.query(".handle", slider.domNode).forEach(function (element, index) {
                dojo.style(element, 'position', 'absolute');
                dojo.attr(element, 'tabindex', 0);
                dojo.connect(element, "onclick", function (event) {
                    element.focus();
                });
                dojo.connect(element, "onkeydown", function (event) {
                    if (slider.isDragging) return;
                    if ((event.keyCode === dojo.keys.LEFT_ARROW) ||
                        (event.keyCode === dojo.keys.RIGHT_ARROW)) {
                        var direction = event.keyCode === dojo.keys.LEFT_ARROW ? -1 : 1,
                            pos = (dojo.style(element, "left")),
                            amount = (slider.snap) ? slider.stepWidth : 1;

                        pos = (direction === 1) ? Math.ceil(pos + amount) : Math.ceil(pos - amount);

                        dojo.style(element, "left", (pos) + "px");

                        slider.updateHandlePosition(pos, index, direction);
                    }
                    if (event.keyCode === dojo.keys.ENTER) {
                        slider.onEnd(index, Math.ceil(slider.step[index]), slider.step);
                    }
                });
                slider.handles.push(element);
            });
        },

        attachMoverToHandlers: function () {
            var slider = this;
            var options = {
                mover: tui.widgetFx[slider.type + "Mover"],
                host: slider,
                offset: {
                    t: (slider.axis === 'x') ? slider.handleOffest : 0,
                    l: (slider.axis === 'x') ? 0 : slider.handleOffest
                }
            };

            _.forEach(slider.handles, function (handle, i) {
                options.handlePos = i;

                slider.moveable[i] = new SliderMoveable(handle, options);

                dojo.connect(slider.moveable[i], "onMoveStop", slider, function (mover) {
                    slider.isDragging = false;
                    slider.onEnd(i, Math.ceil(slider.step[i]), slider.step);
                });

                dojo.connect(slider.moveable[i], "onMoveStart", slider, function (e, leftTop) {
                    slider.lastStepOnEnd = dojo.clone(slider.step);
                    slider.isDragging = true;
                });

                slider.setStep(slider.step[i], i);
            });
        },

        addTracksEventListeners: function () {
            var slider = this;
            if (slider.isDragging) {
                return;
            }
            var position = dojo.position(slider.domNode, true);
            var leftTop = position[slider.axis];
            var widthHeight = position[slider.trackproperty[0]];

            slider.connect(slider.domNode, "onmousedown", function (event) {
                dojo.stopEvent(event);
                if (slider.isDragging) {
                    return;
                }
                var index = 0;
                var client = "client" + slider.axis.toUpperCase();
                var pos = event[client] - leftTop;

                pos = (slider.snap) ? (Math.round(pos / Math.ceil(slider.stepWidth)) * Math.ceil(slider.stepWidth)) : pos;
                if (slider.handles.length === 2) {
                    index = (pos > widthHeight / 2) ? 1 : 0;
                }
                slider.updateHandlePosition(pos, index);
                slider.onEnd(index, slider.step[index], slider.step);
            });

            slider.connect(window, "onresize", function (event) {
                dojo.stopEvent(event);
                for (var i = 0; i < slider.handles.length; i++) {
                    slider.updateSelectedValues(i, slider.step[i]);
                }
            });
        },

        onSliderClick: function () {

        },

        updateHandlePositions: function (positions) {
            var slider = this;
            _.each(positions, function (position, index) {
                slider.updateHandlePosition(position, index);
            })
        },

        updateHandlePosition: function (value, index, dir) {
            var slider = this;
            dir = dir || null;
			var step = slider.min + slider.toStep(value, dir);
            slider.setStep(step, index);
        },

        updateSelectedValues: function (handlePos, value) {
            var slider = this;
            var handle = slider.handles[handlePos];
            var action = (value === slider.step[1]) ? "addClass" : "removeClass";
            dojo[action](handle, "last");
            if (slider.showMarkerValue) {
                dojo.html.set(dojo.query(".slider-value", handle)[0], slider.renderValue(value));
            }
        },

        renderMarker: function () {
            var slider = this;
            return slider.seperator;
        },

        renderValue: function (value) {
            var slider = this;
            return dojo.string.substitute(slider.valueTmpl, {
                value: Math.ceil(value)
            });
        },

        onFirstMarkerItemRender: function (li) {},

        onLastMarkerItemRender: function (li) {},

        onMarkerItemRender: function (li) {},

        onMoveStop: function (event, mover, leftTop) {},

        onEnd: function (handlePos, value, step) {},

        change: function (handlePos, value, oldStep, newStep, dir) {
            var slider = this;
            slider.updateSelectedValues(handlePos, value);
            slider.onChange(handlePos, value, oldStep, newStep, dir);
            return slider;
        },

        onChange: function (handlePos, value, oldStep, newStep, dir) {},

        onMove: function (moveable, leftTop) {
            var slider = this;
            var firstChar = slider.property.slice(0, 1);
            var position = leftTop[firstChar];
            /*for (var i = 0, length = slider.handles.length; i < length; i++) {
             dojo.style(slider.handles[i], "zIndex", 100);
             }*/
            slider.updateHandlePosition(position, moveable.handlePos);
            //dojo.style(slider.handles[moveable.handlePos], "zIndex", 101);
        }
    });

    return tui.widget.form.sliders.Slider;
});
