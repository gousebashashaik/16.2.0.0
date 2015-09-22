define("tui/singleAccom/Controller", [
    "dojo",
    "tui/singleAccom/service/GroupingService",
    "dojo/text!tui/singleAccom/view/templates/multiDatemultiAirport.html",
    "dijit/_Widget"], function (dojo, GroupingService, multiDatemultiAirportTemplate) {

    dojo.declare("tui.singleAccom.Controller", [dijit._Widget], {

        store: null,

        jsonData: null,

        widgets: [],

        services: null,

        viewMappings: null,

        postCreate: function () {
            var controller = this;
            controller.jsonData = searchResponse;
            controller.viewMappings = {
                'flightOptions': function (model) {
                    return multiDatemultiAirportTemplate;
                }
            };

            controller.services = {
                'flightOptions': GroupingService(this.jsonData.searchResult['holidays'])
            }
        },


        register: function (widget, dataPath) {
            var controller = this;
            var model = dojo.getObject(dataPath || widget.dataPath || '', false, controller.jsonData);
            controller.widgets.push(widget);

            return {
                'model': controller.groupingService.byDepartureAirports(),
                'view': controller.viewMappings[widget.widgetName] ? controller.viewMappings[widget.widgetName](model) : ''
            };
        }
    });

    return tui.singleAccom.Controller;
});