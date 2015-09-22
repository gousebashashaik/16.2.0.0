define("tui/widget/WeatherChart", ["dojo",
  "dijit/_Widget",
  "dojox/charting/Chart2D",
  "tui/widget/themes/TUITheme",
  "dojo/NodeList-manipulate",
  "dojo/html",
  "dojo/query",
	"tui/widget/_TuiBaseWidget",
	"tui/widget/mixins/ChartingTooltip"], function (dojo) {

  dojo.declare("tui.widget.WeatherChart", [tui.widget._TuiBaseWidget], {

    // ----------------------------------------------------------------------------- properties
    data: [],

    chart: null,

    monthNames: [
      'January',
      'February',
      'March',
      'April',
      'May',
      'June',
      'July',
      'August',
      'September',
      'October',
      'November',
      'December'
    ],

    rainfallMax: null,

    rainfallTickSteps: null,
    // ----------------------------------------------------------------------------- singleton
    postMixInProperties: function () {

      var weatherChart = this,
          dt = null,
          prop = ["month", "temperature", "rainfall"];

      dojo.query(".weather-data tbody tr").forEach(function (node, index) {
        dt = {};
        dojo.query("td", node).forEach(function (td, i) {
          dt[prop[i]] = (i == 0) ? dojo.query(td).text() : parseInt(dojo.query(td).text(), 10);
        });
        dt.name = weatherChart.monthNames[index];
        weatherChart.data.push(dt)
      });
    },

    postCreate: function () {

      var weatherChart = this,
          getMonths = function (month) {
            if (month > 12 || month < 1) {
              return false;
            }
            return weatherChart.data[month - 1].month.substring(0, 1);
          };

      weatherChart.inherited(arguments);
      weatherChart.calculateRainfallMax();
      weatherChart.rainfallTickSteps = weatherChart.calculateRainfallTickSteps(weatherChart.rainfallMax);

      weatherChart.chart = new dojox.charting.Chart2D(weatherChart.domNode);
      weatherChart.chart.setTheme(dojox.charting.themes.tuiTheme.green);
      weatherChart.chart.addAxis("x", {labelFunc: getMonths, majorTickStep: 1, majorTick: {length: 0}, minorTickStep: 0});
      weatherChart.chart.addAxis("yt", {vertical: true, leftBottom: true, majorTickStep: 10, includeZero: true, minorTickStep: 0, majorTick: {length: 0}, stroke: "#fff"});
      weatherChart.chart.addAxis('yr', {vertical: true, leftBottom: false, majorTickStep: weatherChart.rainfallTickSteps, includeZero: true, minorTickStep: 0, majorTick: {length: 0}, stroke: '#fff', max: weatherChart.rainfallMax});
      weatherChart.chart.addPlot("rainfall", {type: "Default", vAxis: "yr", tension: 9});
      weatherChart.chart.addPlot("temperature", {type: "Columns", vAxis: "yt", gap: 3, animate: true });
      weatherChart.chart.addPlot("grid", {type: "Grid", vAxis: "yt", vMajorLines: false });
      weatherChart.chart.addSeries("Temp", weatherChart.getData("temperature"), {plot: "temperature"});
      weatherChart.chart.monthData = weatherChart.data;

			var tip = new tui.widget.mixins.ChartingTooltip(weatherChart.chart, "temperature");

      weatherChart.chart.render();

      weatherChart.chart.hoverEvent = weatherChart.addRolloverEvent();

      var checkTmpl = '<input type="checkbox" id="rf_check"> <label for="rf_check">Show average monthly rainfall (mm)</a>';
      dojo.place(checkTmpl, weatherChart.domNode, "after");

      var checkBox = dojo.byId('rf_check');
      dojo.connect(checkBox, 'onclick', function () {
        if (this.checked) {
          weatherChart.chart.hoverEvent.remove();
          weatherChart.addRainfall();
          weatherChart.chart.hoverEvent = weatherChart.addRolloverEvent();
          dojo.addClass(document.body, "addrain");
        } else {
          weatherChart.removeRainfall();
          dojo.removeClass(document.body, "addrain");
        }
      });

      // Tag elements
      weatherChart.tagElement(checkBox, 'rainfallCheck');
    },

    removeRainfall: function () {
      var weatherChart = this;
      weatherChart.chart.removeSeries("Rain");
    },

    addRainfall: function () {
      var weatherChart = this;
      weatherChart.chart.addPlot("temperature", {type: "Columns", vAxis: "yt", gap: 3, animate: false })
      weatherChart.chart.addAxis('yr', {vertical: true, leftBottom: false, majorTickStep: weatherChart.rainfallTickSteps, includeZero: true, minorTickStep: 0, majorTick: {length: 0}, stroke: '#fff', max: weatherChart.rainfallMax});
      weatherChart.chart.addSeries("Rain", weatherChart.getData("rainfall"), {plot: "rainfall"});
      var tip = new tui.widget.mixins.ChartingTooltip(weatherChart.chart, "temperature");
      weatherChart.chart.render();

    },

    getData: function (t) {
      var weatherChart = this, a = [];
      for (var month in weatherChart.data) {
        a.push(weatherChart.data[month][t]);
      }
      return a;
    },

    addRolloverEvent: function () {
      var weatherChart = this;
      var t = weatherChart.chart.connectToPlot("temperature", function (event) {
        var shape = event.shape,
            type = event.type,
            index = event.index,
            children = event.eventMask.parent.children,
            originalFill = shape.fillStyle,
            hoverFill = "#fde2ad",
            i, node;

        if (type === 'onmouseover') {
          for (i = 0; i < children.length; i++) {
            node = children[i];
            if (node !== shape) {
              node.setFill(hoverFill);
              node.setStroke(hoverFill);
            }
          }
        } else if (type === 'onmouseout') {
          for (i = 0; i < children.length; i++) {
            node = children[i];
            node.setFill(originalFill);
            node.setStroke(originalFill);
          }
        }
      });
      return t;
    },

    calculateRainfallMax: function () {
      var weatherChart = this, max = 0;
      for (var month in weatherChart.data) {
        max = Math.max(max, weatherChart.data[month].rainfall);
      }
      max = Math.ceil(max / 10) * 10;
      weatherChart.rainfallMax = (max < 100) ? 100 : max;
    },
    calculateRainfallTickSteps: function (max) {
      var weatherChart = this;
      if (max >= 200) {
        return Math.floor(max / 10);
      }
      return 25;
    }
  });
  return tui.widget.WeatherChart;
});
