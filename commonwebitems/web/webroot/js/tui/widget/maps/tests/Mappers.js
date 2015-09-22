//var a='';
//a=a.replace(/"(\w+)"\s*:/g, '$1:');
//console.log(a);

define(["dojo", "doh", "tui/widget/maps/Mappers", "tui/widget/maps/tests/FilterMapJsons"], function (dojo, doh, mapper, jsons) {
    doh.register("map's json object mapping tests", [


    /**,{
     testType: "perf",
     trialDuration: 100,
     trialIterations: 100,
     trialDelay: 100,
     name:"should map/parse a valid json object for filter map efficiently",

     runTest : function() {
     var mappedData = mapper.filterTypeMapper(jsons.validJson);

     with(doh) {
     assertTrue(mappedData);
     }
     }
     }**/
    ]);

});
