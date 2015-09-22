define("tui/filterBPanel/view/sliders/TaRating", [
  "dojo",
  'dojo/dom-attr',
  'dojo/dom-class',
  "dojo/_base/declare",
  "dojo/topic",
  "tui/filterBPanel/view/sliders/SlidingFilter"], function (dojo, domAttr, domClass, declare, topic) {

  return declare("tui.filterBPanel.view.sliders.TaRating", [tui.filterBPanel.view.sliders.SlidingFilter], {

    valueTmpl: "${value}",

    dataPath: 'rating.filters',

    visibilityKey : 'tripadvisorRatingFilter',

    setData: function () {
      var widget = this, pill = dojo.query('ul > li', widget.domNode)[0];
      widget.controller = widget.getParent();
      var data = widget.model ? [widget.model] : widget.controller.registerFilter(widget);
      widget.model = data[1] || data[0];
      widget.drawIfData(widget.model);
      if(widget.model) {
        var lastSelection = _.filter(widget.trackLists(), function(trackList) {
            if(domAttr.get(trackList, 'data-value') == widget.model.max) {
                return trackList
            }
        });
        widget.current = _.indexOf(widget.trackLists(), _.first(lastSelection));
      //calculation in prototype
      widget.stepWidth = 40;
      //calculation in desktop application
      //widget.stepWidth = widget.trackLists()[0].offsetWidth;
      widget.updateSliderOffset();
      widget.updateSlider((widget.model.limit[0] - 1));
      }

      //Tagging widget with Analytics
    	widget.tagElements(dojo.query('div', widget.domNode), function(DOMElement) {
          return "tripadvisorrating-slider-left-handle";
        });
    	widget.tagElements(dojo.query('li span', widget.domNode), function(DOMElement) {
            return "tripadvisorrating-slider-left-handle";
          });
    },

    updateValue: function() {
      var widget = this;
      //method call when the state is restored.
      var model = dojo.clone(widget.filterModel[widget.exId]);
        if(_.isEqual(widget.originalModel, model)) {
          widget.currentModel = widget.originalModel;
        } else {
      //set the current model as this
      widget.currentModel = model;
        }
      widget.istrackSet = true;
      widget.resetToPreviousSelection();
    },

    resetToPreviousSelection: function() {
      var widget = this;
      if(widget.currentModel) {
        var lastSelection = _.filter(widget.trackLists(), function(trackList) {
          if(domAttr.get(trackList, 'data-value') == widget.currentModel.max) {
            return trackList
        }
        });
        widget.current = _.indexOf(widget.trackLists(), _.first(lastSelection));
        widget.generateRequest(widget.exId, widget.currentModel);
      }
    },

    renderValue: function (value) {
      var slider = this;
      return dojo.string.substitute(slider.valueTmpl, {
        value: value
      });
    },

    clear: function (data) {
      var slider = this,
      	  filterConatiner;
      filterConatiner = _.first(dojo.query(slider.domNode).closest(".item-container"));
      domClass.remove(filterConatiner, "disabled");
	  domClass.remove(slider.domNode, 'hide');
	  domClass.remove(_.first(dojo.query("h3", slider.domNode.parentElement)), "hide");
      slider.isChanged = false;   
      slider.updateState(slider.allowedMin - 1);
      slider.setData();
    },

    updateState: function(selectedIndex){
    	var slider = this;
    	slider.isDefault = true;
    	slider.isChanged = false;
	    if(selectedIndex >= slider.allowedMin) {
				slider.isDefault = false;
				slider.isChanged = true;
	    }
	    topic.publish("tui/filterBPanel/view/FilterController/setFilterSelection", slider.domNode.id);
        topic.publish("tui/filterBPanel/view/filterController/clearAllFilterVisibility");
    },

    reset: function (data) {
        var slider = this;
        slider.isChanged = false;
        if(!data.length) {
          _.debug('Warning! No data returned for ' + slider.declaredClass);
        }
        _.every(data, function(ratingData){
        	if(slider.id === ratingData.id){
        		slider.model = ratingData;
                return false;
        	}
        	return true;
        });
        slider.redraw(slider.model);
        slider.resetSlider(slider.model);
    }

  });
});