define("tui/widget/popup/tests/DynamicPopupTest", ["dojo", "doh", "tui/widget/popup/DynamicPopup", "dojo/dom-construct", "tui/utils/TuiUnderscore"], function (dojo, doh, popup, domConstruct, under) {
    popup = popup.call();

    doh.register("tests for the dynamic popup", [
        {
            name:"should safely return if 'widgetId' is not supplied",
            runTest:function () {
                try {
                    popup.postCreate();
                    doh.assertTrue(true);
                } catch (e) {
                    //you shouldn't come here.

                }

            }
        },

        {
            name:"should trigger onAfterTmplRender event",
            setUp:function () {
                this.mocked = popup;

                with (_) {
                    mock(popup, "onAfterTmplRender", function () {
                        doh.assertTrue(true);
                    });

                    mock(dojo, "byId", function () {
                        return domConstruct.create("div");
                    });
                }
            },

            runTest:function () {
                popup.postCreate();

            },

            tearDown:function () {
                with (_) {
                    unmockAll(this.mocked);
                }
            }
        },

        {
            name:"should place the popup dom id as the last element in the body",
            setUp:function () {
                this.mocked = popup;

                with (_) {
                    mock(dojo, "byId", function () {
                        return domConstruct.create("div", {id:"testId"});
                    });
                }
            },

            runTest:function () {
                popup.postCreate();

                _.unmockAll(this.mocked);
                doh.assertTrue(typeof(dojo.byId("testId")) != "undefined");
            }
        }
    ]);

});

