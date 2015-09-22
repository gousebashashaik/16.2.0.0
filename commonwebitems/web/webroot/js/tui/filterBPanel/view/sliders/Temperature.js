define("tui/filterBPanel/view/sliders/Temperature",
    ["dojo",
        "tui/filterBPanel/view/sliders/SlidingFilter"], function (dojo) {



        dojo.declare("tui.filterBPanel.view.sliders.Temperature", [tui.filterBPanel.view.sliders.SlidingFilter], {

            dataPath : 'temperature.filters'

        })

        return tui.filterBPanel.view.sliders.Temperature;
    })