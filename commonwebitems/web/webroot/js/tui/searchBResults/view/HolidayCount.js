define("tui/searchBResults/view/HolidayCount", ["dojo"], function (dojo) {

    dojo.declare("tui.searchBResults.view.HolidayCount", [tui.widget._TuiBaseWidget], {

        count: 0,

        dataPath: 'searchResult.endecaResultsCount',

        // ----------------------------------------------------------------------------- methods

        postCreate: function () {
          var widget = this;
          widget.count = dijit.registry.byId('mediator').registerController(this, widget.dataPath);
          widget.render();
        },

        refresh: function (field, oldValue, newValue, count) {
            var widget = this;
            if (field === 'paginate') {
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
            var holidayTxt = (widget.count > 1 || widget.count === 0) ? "Holidays" : "Holiday";
            widget.domNode.innerHTML = "<span class='count'>" + ((widget.count >= 100) ? '100+' : widget.count) + "</span> " + holidayTxt;
        }
    });

    return tui.searchBResults.view.HolidayCount;
});
