define("tui/filterIGPanel/view/BestFor",
    ["dojo",
        "tui/filterIGPanel/view/OptionsFilter"], function (dojo) {

        dojo.declare("tui.filterIGPanel.view.BestFor", [tui.filterIGPanel.view.OptionsFilter], {

            dataPath : 'bestFor',

            draw: function () {
                var hotel = this;
                hotel.inherited(arguments);
                hotel.defineNumber();
                _.each(dijit.findWidgets(hotel.domNode), function(w){
                    w.tag = hotel.tagMappingTable.table[hotel.declaredClass];
                    dojo.setAttr(w.domNode, 'analytics-id', w.tag);
                    dojo.setAttr(w.domNode, 'analytics-instance', hotel.number);
                    dojo.setAttr(w.domNode, 'analytics-text', w.model.name);
                })
            }

        })

        return tui.filterIGPanel.view.BestFor;
    })