define("tui/singleAccom/FlightOptionsController", [
    "dojo",
    "tui/singleAccom/service/FlightOptionService",
    "tui/singleAccom/view/FlightOptionViewMapping",
    "tui/singleAccom/view/AirportDateToggle",
    "tui/singleAccom/view/DurationHighlight",
    "tui/singleAccom/view/FlightGroup",
    "tui/singleAccom/view/FlightOptions",
    "dijit/_Widget"], function (dojo, service, viewMappings) {

    function createViewModel (result) {
        return {
            'model': result.data,
            'view': viewMappings ? viewMappings(result.type) : '',
            'showMoreDuration': result.showMoreDuration
        };
    }

    dojo.declare("tui.singleAccom.FlightOptionsController", [dijit._Widget], {

        model: null,

        widgets: [],

        dataPath: 'searchResult',


        postCreate: function () {
            var controller = this;
            controller.model = dijit.registry.byId('mediator').registerController(controller);
            // remove updating class
            dojo.subscribe("tui:channel=lazyload", function () {
              dojo.removeClass(dojo.query(".search-results")[0], 'updating');
            });
        },

        refresh: function (field, oldValue, newValue, response) {
            _.each(this.widgets, function (widget) {
                widget.refresh ? widget.refresh((_.compose(createViewModel, service.process))(response)) : null;
            });
        },

        register: function (widget) {
            var controller = this;
            !_.contains(controller.widgets, widget) ? controller.widgets.push(widget) : null;
            return (_.compose(createViewModel, service.process))(controller.model)

        }
    });

    return tui.singleAccom.FlightOptionsController;
});