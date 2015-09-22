define("tui/filterBPanel/view/options/BestFor",
    ["dojo",
        "tui/filterBPanel/view/options/OptionsFilter"], function (dojo) {


        dojo.declare("tui.filterBPanel.view.options.BestFor", [tui.filterBPanel.view.options.OptionsFilter], {
            dataPath : 'bestFor',
            visibilityKey : 'bestforFilter'

        })
        return tui.filterBPanel.view.options.Destination;
    })