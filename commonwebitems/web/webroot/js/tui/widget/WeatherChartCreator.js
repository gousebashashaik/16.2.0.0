define("tui/widget/WeatherChartCreator", [
  "dojo",
  "dijit/registry",
  "tui/utils/Collections",
  "tui/widget/form/SelectOption",
  "tui/widget/WeatherChart"], function (dojo, registry, collections) {

  function WeatherData() {
    this.monthlyIndex = {};

    this.push = function (weather) {
      var weatherData = this, monthData;
      _.each(weather.weatherTypeValueViewDataList, function (value){
        monthData = {
          month: value.month,
          name: value.month
        };
        monthData[weather.weatherType.toLowerCase()] = value.average;
        weatherData.add(value.month, monthData);
      });
    };

    this.add = function (month, data) {
      this.get(month) ? dojo.mixin(this.monthlyIndex[month], data) : this.monthlyIndex[month] = data;
    };

    this.get = function (month) {
      return this.monthlyIndex[month];
    };

    this.consolidated = function () {
      var completeData = [];
      _.each(this.monthlyIndex, function(monthData){
        completeData.push(monthData);
      });
      return completeData;
    };

  }

  dojo.declare("tui.widget.WeatherChartCreator", [tui.widget.form.SelectOption, tui.widget._TuiBaseWidget], {

    prepareChart: function () {

      var weatherChartCreator = this;
      var locationName = weatherChartCreator.getSelectedData().value;
      var jsonData = collections.select(weatherChartCreator.jsonData, function (data) {
        return locationName === data.locationName;
      });

      var consolidatedData = parseWeatherData(jsonData.weatherTypes);

      if (consolidatedData.length) {
        var weatherChart = new tui.widget.WeatherChart({
          postMixInProperties: function () {
            this.data = consolidatedData;
          }
        }, container());
      } else {
        container().innerHTML = "<p>No weather data available.</p>";
      }

      function parseWeatherData() {
        var chartData = new WeatherData();
        _.each(jsonData.weatherTypes, function (weather) {
          chartData.push(weather);
        });
        return chartData.consolidated();
      }

      function destroyWidget(id) {
        var widget = registry.byId(id);
        if (widget) {
          widget.destroyRecursive(true);
        }
        dojo.byId(id).innerHTML = "";
      }

      function clearContainer(domNode, elements) {
        dojo.query(elements.join(", "), domNode).forEach(function (node) {
          dojo.destroy(node);
        });
      }

      function container() {
        destroyWidget('weatherWidget');
        clearContainer(dojo.byId('weatherChartContainer'), ["label", "input"]);
        return dojo.byId('weatherWidget');
      }

    },

    postCreate: function () {
      var weatherChartCreator = this;
      weatherChartCreator.inherited(arguments);

      weatherChartCreator.prepareChart();
      weatherChartCreator.tagElement(weatherChartCreator.domNode, 'geoSelection');

      /*weatherChartCreator.connect(weatherChartCreator.domNode, "onchange", function () {
        weatherChartCreator.prepareChart();
      });*/
    },

    onChange: function () {
      var weatherChartCreator = this;
      weatherChartCreator.inherited(arguments);
      weatherChartCreator.prepareChart();
    }

  });

  return tui.widget.WeatherChartCreator;
})