define("tui/filterIGPanel/view/Rating", [
    "dojo",
    "tui/filterIGPanel/view/SlidingFilter"], function (dojo) {

    dojo.declare("tui.filterIGPanel.view.Rating", [tui.filterIGPanel.view.SlidingFilter], {

        valueTmpl: "${value}",

        dataPath: 'fcRating',

        showMarkers: function () {
            // summary:
            //		Creates and displays, ratingSlider marker point based on given range and steps.
            //		Markers will only be displayed if "displayMaker" is set to true.
            var ratingSlider = this;
            var pos, li, i;

            ratingSlider.marker = dojo.create("ul", {
                className: "show-values"
            }, ratingSlider.domNode, "last");
            li = dojo.create("li", {
                innerHTML: ratingSlider.renderValue(ratingSlider.min),
                style: {"left": "3px"},
                className: "min"
            }, ratingSlider.marker, "first");

            ratingSlider.onFirstMarkerItemRender(li, 3, ratingSlider.min);

            if (ratingSlider.displayMaker) {
                for (i = 0; i < ratingSlider.steps - 1; i++) {
                    pos = (i + 1) * ratingSlider.stepWidth;
                    li = dojo.create("li", {
                        "class": (i + 1 === ratingSlider.steps) ? "" : "marker",
                        "innerHTML": ratingSlider.renderValue((ratingSlider.min + 1) + i),
                        "style": {"left": pos + "px"}
                    }, ratingSlider.marker, "last");
                    ratingSlider.onMarkerItemRender(li, pos, (ratingSlider.min + 1) + i);
                }
            }

            pos = ratingSlider.steps * ratingSlider.stepWidth;
            li = dojo.create("li", {
                innerHTML: ratingSlider.renderValue(ratingSlider.max),
                style: {"left": pos + "px"},
                className: "max"
            }, ratingSlider.marker, "last");
            ratingSlider.onLastMarkerItemRender(li, pos, ratingSlider.steps);
        }

    });

    return tui.filterIGPanel.view.Rating;
});