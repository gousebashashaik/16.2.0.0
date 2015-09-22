define("tui/singleAccom/view/FlightOptionViewMapping", [
    "dojo",
    "dojo/text!tui/singleAccom/view/templates/multiDatemultiAirport.html",
    "dojo/text!tui/singleAccom/view/templates/singleAirportMultiDate.html",
    "dojo/text!tui/singleAccom/view/templates/singleDateMultiAirport.html",
    "dojo/text!tui/singleAccom/view/templates/singleAirportSingleDate.html",
    "dojo/text!tui/singleAccom/view/templates/singleAirportSingleDateMultipleFlight.html"

    ], function (dojo, multiDatemultiAirport, singleAirportMultiDate, singleDateMultiAirport, singleAirportSingleDate, singleAirportSingleDateMultipleFlight) {

    var mappings = {

        'MULTIPLEDATESAIRPORTS' :  multiDatemultiAirport,  //5,6
        'MULTIPLEDATESSINGLEAIRPORT' : singleAirportMultiDate,  //3
        'SINGLEDATEAIRPORTFLIGHT' : singleAirportSingleDate,      //1
        'SINGLEDATEMULTIPLEAIRPORTS' : singleDateMultiAirport, //4
        'SINGLEDATEAIRPORTMULTIPLEFLIGHTS' : singleAirportSingleDateMultipleFlight  //2

    };

    return function (displayType) {
        _.debug('Using Display Type:'+displayType);
        return mappings[displayType];
    }
})
