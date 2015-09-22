define('tui/filterBPanel/view/sliders/SlidingSelect', [
  'dojo',
  'dojo/query',
  'dojo/dom-class',
  'dojo/dom-style',
  'dojo/on',
  'dojo/dom-attr',
  "dojo/topic",
  'tui/widget/_TuiBaseWidget'], function(dojo, query, domClass, domStyle, on, domAttr, topic) {

  dojo.declare('tui.filterBPanel.view.sliders.SlidingSelect', [tui.widget._TuiBaseWidget], {

    continuouslyAdapt: true,

    containerSelector: '.filter-slider',

    current: null,

    valueTmpl: null,

    steps: null,

    full: 100,

    stepWidth: null,

    slider: null,

    offset: 0,

    eventAttached : false,

    onDesktop: function() {
      var slider = this;
      slider.stepWidth = slider.trackLists()[0].offsetWidth;
      slider.updateSlider(slider.current);
    },

    optionsContainer: function() {
      var slider = this;
      return query(slider['containerSelector'], slider.domNode)[0];
    },

    trackLists: function() {
      return query('ul > li',  this.optionsContainer());
    },

    updateSliderOffset: function() {
      var slider = this;
      //if(slider.offset === 0) return;
      var offset = slider.offset * slider.stepWidth;
      domStyle.set(slider.slider, 'right', offset + 'px');
    },

    setRange: function(range) {
      // summary:
      // Sets slider ranges, mix max and steps.
      // description:
      // Works out the slider range, min, max steps values.
      var slider = this;
      slider.min = 0;
      slider.max = slider.full;
      // if a range is specified use values.
      if(range) {
        slider.min = range[0] || slider.min;
        slider.max = range[1] || slider.max;
      }
      slider.step = slider.step || [slider.min, slider.max];
      slider.range = Math.abs(slider.max - slider.min);
      slider.steps = 4;
      slider.stepSize = slider.range / (slider.steps);
    },

    renderValue: function(value) {
      var slider = this;
      return dojo.string.substitute(slider.valueTmpl, {
        value: value
      });
    },

    setMarkers: function() {
      var slider = this, value, marker = null;
      slider.offset = 0;
      for(var i = 0; i <= slider.steps; i++) {
        marker = slider.trackLists()[i];
        if((i + 1) < slider.allowedMin) {
          domClass.add(marker, 'disabled');
        } else if((i + 1) > slider.allowedMax) {
          domClass.add(marker, 'disabled');
          slider.offset += 1;
        }
        else {
        	domClass.contains(marker, 'disabled') ? domClass.remove(marker, 'disabled') : ''
        }
        value = i + 1;
        domAttr.set(marker, 'data-value', value);
      }
    },

    setLimit: function(limit) {
      var slider = this;
      if(limit) {
        slider.allowedMax = slider.limit[1];
        slider.allowedMin = slider.limit[0];
      }
    },

    setTrack: function() {
      var slider = this;
      switch(slider.trackType) {
        case 'maxRange':
          slider.updateSlider(slider.allowedMax - 1);
          break;
        case 'minRange':
          slider.updateSlider(slider.allowedMin - 1);
          break;
        default :
          break;
      }
    },

    drawSlider: function(options, refresh) {
      var slider = this, ref, pill = query('ul > li', slider.domNode)[0];
      typeof refresh === 'undefined' ? ref = true : ref = refresh;
      if(options !== null) {
        dojo.mixin(slider, options)
      }
      slider.slider = query('span.fill', slider.optionsContainer())[0];
      slider.setRange(slider.range);
      slider.setLimit(slider.limit);
      if(slider.code !='budgetTotal'){
        slider.setMarkers();
        slider.updateSliderOffset();
      }
      slider.eventAttached ? "" : slider.attachTrackEvents();
    },

    updateSlider: function(i) {
      var slider = this,
      	  width = 0,
      	  pill = query('ul > li', slider.domNode)[0],
      	  right = 0;
      slider.current = i;
      slider.stepWidth = slider.trackLists()[0].offsetWidth || slider.stepWidth;
      if(slider.trackType === "maxRange") {
        //TODO: add the midrange functionality to max range slider as well!!
        width = (i + 1) * 20;
        domStyle.set(slider.slider, 'width', width + '%');
        _.each(slider.trackLists(), function(pill, index) {
          if(!domClass.contains(pill, 'disabled')) {
        	  if(index > i) {
        		  domClass.add(pill, 'selectable');
        	  } else {
               domClass.remove(pill, 'selectable');
        	  }
          }
        });
		  slider.updateState(i);
      }

      if(slider.trackType === "minRange") {
/*        if(slider.offset === 0) {
          width = i === 0 ? ((slider.steps + 1) * slider.stepWidth - 1) : ((slider.steps - i) + 1) * slider.stepWidth - 1;
        } else {
          width = i === 0 ? ((slider.steps + 1) * slider.stepWidth - 1) : ((slider.steps - (i + slider.offset)) + 1) * slider.stepWidth - 1;
        }*/
        width = ((slider.steps - (i + slider.offset)) + 1) * slider.stepWidth - 1; // DE40919
    	//width = ((slider.allowedMax - slider.allowedMin + 1) * slider.stepWidth - 1)
        domStyle.set(slider.slider, 'width', width + 'px');
        right = (slider.max - slider.allowedMax) * slider.stepWidth;
        domStyle.set(slider.slider, 'right', right + 'px');
        _.each(slider.trackLists(), function(pill, index) {
          if(!domClass.contains(pill, 'disabled')) {
        	  if(index < i) {
        		  domClass.add(pill, 'selectable');
        	  } else {
               domClass.remove(pill, 'selectable');
        	  }
          }
        });
        slider.updateState(i);
      }
      //TODO : add functionality for other track types
    },

    resetSlider: function(model) {
      var widget = this;
        var pill = dojo.query('ul > li', widget.domNode)[0];
        _.each(widget.trackLists(), function(pill, i) {
              if(domClass.contains(pill, 'disabled')) {
                return;
              }
                widget.updateSlider(i);
                widget.updateState(i+1);
          });
        if(widget.id == "tripadvisorrating" || widget.id == "fcRating"){
        	 widget.updateSlider((widget.model.limit[0] - 1));
        	 widget.updateState((widget.model.limit[0] - 1));
        }
    },

    attachTrackEvents: function() {
      var slider = this,
      	  previousSelection = 0,
      	  pill = query('ul > li', slider.domNode)[0];

      if(!slider.eventAttached) {
      _.each(slider.trackLists(), function(pill, selectedIndex) {
        on(pill, 'click', function(e) {
          if(domClass.contains(pill, 'disabled')) {
            return;
          }
          dojo.stopEvent(e);
          previousSelection = slider.values && slider.values.length ? slider.values[0] : slider.max;
          slider.setPreviousSuccessfulSelection(previousSelection); // This is to restore previous state during oops scenario
          slider.offset = slider.max - slider.allowedMax;
          slider.updateSlider(selectedIndex);
          slider.updateState(selectedIndex);
          if(domAttr.get(pill, 'data-value') != null)
          slider.onSelect(domAttr.get(pill, 'data-value'), selectedIndex);
          else
            slider.onSelect(domAttr.get(pill, 'data-value-total'), selectedIndex);
        })
      });
       slider.eventAttached = true;
      }

    },

    updateState: function(selectedIndex){},

    postCreate: function() {
      var slider = this;
      slider.inherited(arguments);
    }
  });
});

