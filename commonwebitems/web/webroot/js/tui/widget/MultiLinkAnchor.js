define("tui/widget/MultiLinkAnchor", ["dojo", "tui/widget/_TuiBaseWidget"], function (dojo) {

    dojo.declare("tui.widget.MultiLinkAnchor", [tui.widget._TuiBaseWidget], {
        newWindowLink:null,

        postCreate:function () {
            var anchorLink = this;
            anchorLink.inherited(arguments);

            anchorLink.connect(anchorLink.domNode, "onclick", function(e){
                window.open(anchorLink.newWindowLink);
                location.href = anchorLink.domNode.href;
            });
        }
    });

    return tui.widget.MultiLinkAnchor;
})
