define("tui/filterIGPanel/view/Collection",
["dojo",
    "tui/filterIGPanel/view/OptionsFilter"], function (dojo) {

    dojo.declare("tui.filterIGPanel.view.Collection", [tui.filterIGPanel.view.OptionsFilter], {

        dataPath : 'collection',

        draw: function () {
            var general = this;
            general.inherited(arguments);
            general.defineNumber();
            _.each(dijit.findWidgets(general.domNode), function(w){
                w.tag = general.tagMappingTable.table[general.declaredClass];
                dojo.setAttr(w.domNode, 'analytics-id', w.tag);
                dojo.setAttr(w.domNode, 'analytics-instance', general.number);
                dojo.setAttr(w.domNode, 'analytics-text', w.model.name);
            })
        }

    });

    return tui.filterIGPanel.view.Collection;
});