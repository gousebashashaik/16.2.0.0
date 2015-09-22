define("tui/searchResults/view/HolidayCount", ["dojo"], function (dojo) {

    dojo.declare("tui.searchResults.view.HolidayCount", [tui.widget._TuiBaseWidget], {

        count: 0,

        dataPath: 'searchResult.endecaResultsCount',

        one: "Holiday",

        many: "Holidays",

        // ----------------------------------------------------------------------------- methods

        postCreate: function () {
          var widget = this;
          widget.count = dijit.registry.byId('mediator').registerController(this, widget.dataPath);
          widget.render();
        },

        refresh: function (field, oldValue, newValue, count) {
            var widget = this;
            if (field === 'paginate' && dojo.config.site != "cruise") {
                return;
            }
            widget.count = count;
            widget.render();
        },

        /*handleNoResults: function (field, oldValue, newValue, count) {
            var widget = this;
            widget.count = count;
            widget.render();
        },*/

        handleNoResults: function() {},

        generateRequest: function () {},

        render: function () {
            var widget = this;
            var holidayTxt = (widget.count > 1 || widget.count === 0) ? widget.many : widget.one;
            widget.domNode.innerHTML = "<span class='count'>" + ((widget.count >= 100) ? '100+' : widget.count) + "</span> " + holidayTxt;
        }
    });

    return tui.searchResults.view.HolidayCount;
});
