define("tui/utils/tests/TuiUnderscoreTest", ["dojo", "doh", "tui/utils/TuiUnderscore"], function (dojo, doh, _) {
    doh.register("underscore JS extension tests", [
        {
            name:"should properly mock a function",
            runTest:function () {
                var obj = {key: "value", f: function() {return "original value";}};

                _.mock(obj, "f", function() {return "mocked value";});

                doh.assertEqual(obj.f(), "mocked value");
            }
        },
        {
            name:"should properly unmock all functions in an object",
            runTest:function () {
                var obj = {key: "value", f: function() {return "original value";}};
                _.mock(obj, "f", function() {return "mocked value";});

                _.unmockAll(obj)

                doh.assertEqual(obj.f(), "original value");
            }
        },
        {
            name:"should properly do a 'repeatedly' on passed list",
            runTest:function () {
                //var obj = {key: "value", f: function() {return "original value";}};
                //_.mock(obj, "f", function() {return "mocked value";});
                _.repeatedly(3, function(i){console.log(i);});


                //doh.assertEqual(obj.f(), "original value");
            }
        }
    ]);

});
