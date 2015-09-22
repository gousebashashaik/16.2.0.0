define("tui/filterPanel/view/sliders/TaRating", [
  "dojo",
  "dojo/_base/declare",
  "tui/filterPanel/view/sliders/SlidingFilter"], function (dojo, declare) {

  return declare("tui.filterPanel.view.sliders.TaRating", [tui.filterPanel.view.sliders.SlidingFilter], {

    valueTmpl: "${value}",

    dataPath: 'rating.filters',

    setData: function () {
      var widget = this;
      widget.controller = widget.getParent();
      var data = widget.controller.registerFilter(widget);
      widget.model = data[1] || data[0];
      widget.drawIfData(widget.model);
    },

    showMarkers: function () {
      // summary:
      //Creates and displays, ratingSlider marker point based on given range and steps.
      //Markers will only be displayed if "displayMaker" is set to true.
      var ratingSlider = this;
      var pos, li, i;

      ratingSlider.marker = dojo.create("ul", {
        className: "show-values"
      }, ratingSlider.domNode, "last");
      li = dojo.create("li", {
        innerHTML: ratingSlider.renderValue(ratingSlider.min),
        style: {"left": "3px"},
        className: "min"
      }, ratingSlider.marker, "first");

      ratingSlider.onFirstMarkerItemRender(li, 3, ratingSlider.min);

      if (ratingSlider.displayMaker) {
        for (i = 0; i < ratingSlider.steps - 1; i++) {
          pos = (i + 1) * ratingSlider.stepWidth;
          li = dojo.create("li", {
            "class": (i + 1 === ratingSlider.steps) ? "" : "marker",
            "innerHTML": ratingSlider.renderValue((ratingSlider.min + 1) + i),
            "style": {"left": pos + "px"}
          }, ratingSlider.marker, "last");
          ratingSlider.onMarkerItemRender(li, pos, (ratingSlider.min + 1) + i);
        }
      }

      pos = ratingSlider.steps * ratingSlider.stepWidth;
      li = dojo.create("li", {
        innerHTML: ratingSlider.renderValue(ratingSlider.max),
        style: {"left": pos + "px"},
        className: "max"
      }, ratingSlider.marker, "last");
      ratingSlider.onLastMarkerItemRender(li, pos, ratingSlider.steps);
    },

    renderValue: function (value) {
      var slider = this;
      return dojo.string.substitute(slider.valueTmpl, {
        value: value
      });
    },

    clear: function (data) {
      var slider = this;
      slider.model = _.isArray(data) ? (data[1] || data[0]) : slider.model;
      slider.model.values = slider.trackType === 'maxRange' ? [slider.model.range[1]] : [slider.model.range[0]];
      //show the slider temporarily so that offsetwidth can be calcluated
      slider.redraw();
    }

  });
});