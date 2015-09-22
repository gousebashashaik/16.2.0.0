define("tui/showIGPackages/view/ClearFilters", [
        "dojo",
        "dojo/on",
        "dojo/_base/connect"], function (dojo, on, connect) {

        dojo.declare("tui.showIGPackages.view.ClearFilters", [tui.widget._TuiBaseWidget], {


            constructor:function () {

            },

            postCreate: function () {
                var widget = this;
                widget.inherited(arguments);
                on(widget.domNode, "click", function () {
                    connect.publish("tui/filterIGPanel/controller/FilterController/clearFilters", [{
                        id : 'clear'
                    }]);
                });
                widget.tagElement(widget.domNode, "Clear all filters");
            }

        });

        return tui.showIGPackages.view.ClearFilters;
    });