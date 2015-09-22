define("tui/filterPanel/view/sliders/Temperature",
    ["dojo",
        "tui/filterPanel/view/sliders/SlidingFilter"], function (dojo) {



        dojo.declare("tui.filterPanel.view.sliders.Temperature", [tui.filterPanel.view.sliders.SlidingFilter], {

            dataPath : 'temperature.filters'

        })

        return tui.filterPanel.view.sliders.Temperature;
    })