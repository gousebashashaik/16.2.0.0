define("tui/filterPanel/view/sliders/RatingTapping", [
    "dojo",
    'dojo/dom-attr',
    "tui/filterPanel/view/sliders/TappingFilter"], function (dojo, domAttr) {

    dojo.declare("tui.filterPanel.view.sliders.RatingTapping", [tui.filterPanel.view.sliders.TappingFilter], {

        valueTmpl: "${value}",

        dataPath: 'rating.filters',

            setData: function () {
              var widget = this, pill = dojo.query('ul > li', widget.domNode)[0];
              widget.controller = widget.getParent();
              var data = widget.controller.registerFilter(widget);
              widget.model = data[0] || data[1];
              widget.drawIfData(widget.model);

              if(widget.model) {
                var lastSelection = _.filter(widget.trackLists(), function(trackList) {
                    if(domAttr.get(trackList, 'data-value') == widget.model.max) {
                        return trackList
                }
                });
                widget.current = _.indexOf(widget.trackLists(), _.first(lastSelection));
            }
              widget.stepWidth = widget.trackLists()[0].offsetWidth;
              widget.updateSliderOffset();
              widget.updateSlider((widget.model.limit[0] - 1));
           //Tagging widget with Analytics
          	widget.tagElements(dojo.query('div', widget.domNode), function(DOMElement) {
                return "fcRating-slider-left-handle";
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

        renderValue: function (value) {
          var slider = this;
          return dojo.string.substitute(slider.valueTmpl, {
            value: value
          });
        },

        clear: function (data) {
          var slider = this;
          slider.isChanged = false;
          slider.setData();
        }

    });

    return tui.filterPanel.view.sliders.RatingTapping;
});