// #ChartingTooltip
// ##Mixin module
//
// Attaches a tooltip to a `dojox.charting.Chart` object
// used by `tui.widget.WeatherChart` for example.
//
// This is a mixin class, it must be extended
//
// @author: Maurice Morgan.

define("tui/widget/mixins/ChartingTooltip", [
  "dojo",
  "dojo/_base/lang",
  "dojox/charting/action2d/PlotAction",
  "tui/widget/modules/PopupModule"
], function (dojo, lang) {

  return dojo.declare("tui.widget.mixins.ChartingTooltip", [dojox.charting.action2d.PlotAction], {

    /**
     * ###eventType
     * The type of event which will trigger desired behaviour
     * @type {String}
     */
    eventType: 'onmouseover',

    /**
     * ###constructor()
     * Creates the tooltip action and connect it to the plot.
     * @param {Object} chart `dojox.charting.Chart` object
     * @param {String?} plot the plot this action is attached to. If not passed, "default" is assumed
     * @param {dojox.charting.action2d.__TooltipCtorArgs?} kwArgs Optional keyword arguments object for setting parameters
     */
    constructor: function (chart, plot, kwArgs) {
      this.connect();
    },

    /**
     * ###process()
     * Process the action on the given object
     * Displays the relevant tooltip for the object
     * @param {dojox.gfx.Shape} object The object on which to process the highlighting action
     */
    process: function (object) {
      var chartingTooltip = this;

      // exit early if object has no shape attached or event doesn't match configuration
      if (!object.shape || object.type !== chartingTooltip.eventType) {
        return;
      }

      // exit early if tooltip already exists
      if (!object.tooltip) {

        var data = object.chart.monthData[object.index],
            temperature = "Temperature: " + data.temperature + "\u00B0C",
            rainfall = "Rainfall: " + data.rainfall + " mm";

        // configure the tooltip and attach it to the target object
        object.tooltip = new tui.widget.popup.Tooltips({
          month: data.name,
          temperature: temperature,
          rainfall: rainfall,
          tmpl: "<div class='tooltip {{ floatWhere }}'><p class='uppercase'>{{ month }}</p><p class='small'>{{ temperature }}</p>{% if rainfall %}<p class='small rainfall'>{{ rainfall }}</p>{% endif %}<span class='arrow'></span></div>",
          floatWhere: tui.widget.mixins.FloatPosition.TOP_CENTER
        }, object.shape.rawNode);

        // create and display the tooltip
        object.tooltip.createPopup();
        object.tooltip.posElement(object.tooltip.popupDomNode);
        object.tooltip.showWidget(object.tooltip.popupDomNode);

      }
    }
  });
});
