define("tui/filterIGPanel/view/Destination",
    ["dojo",
        "tui/filterIGPanel/view/OptionsFilter"], function (dojo) {


        dojo.declare("tui.filterIGPanel.view.Destination", [tui.filterIGPanel.view.OptionsFilter], {

            dataPath: 'destination',

            draw: function () {
                var destination = this;
                destination.inherited(arguments);
                destination.defineNumber();
                _.each(dijit.findWidgets(destination.domNode), function(w){
                    w.tag = destination.tagMappingTable.table[destination.declaredClass];
                    dojo.setAttr(w.domNode, 'analytics-id', w.tag);
                    dojo.setAttr(w.domNode, 'analytics-instance', destination.number);
                    dojo.setAttr(w.domNode, 'analytics-text', w.model.name);
                })
            }

        })
        return tui.filterIGPanel.view.Destination;
    })