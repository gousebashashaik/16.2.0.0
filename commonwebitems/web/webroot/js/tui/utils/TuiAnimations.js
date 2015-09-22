define("tui/utils/TuiAnimations", [
    "dojo",
    "dojo/_base/fx",
    "dojo/fx",
    "dojo/fx/easing"], function (dojo, baseFx, fx, easing) {

    //  module:
    //    tui/utils/TuiAnimations
    //  summary:
    //     provides common TUI animations.
    return {
        animateTo: function (node, dnode, duration, easingfx, chainfx) {
            chainfx = chainfx || [];

            var srcPos = dojo.position(node, true),
                dstPos = dojo.position(dnode, true);

            chainfx.splice(0, 0,
                baseFx.animateProperty({
                    node: node,
                    duration: duration || 600,
                    properties: {
                        top: {
                            start: srcPos.y,
                            end: dstPos.y
                        },
                        left: {
                            start: srcPos.x,
                            end: dstPos.x
                        }
                    },
                    easing: easingfx || easing.cubicOut
                })
            );

            fx.chain(chainfx).play();
        }
    };
});
