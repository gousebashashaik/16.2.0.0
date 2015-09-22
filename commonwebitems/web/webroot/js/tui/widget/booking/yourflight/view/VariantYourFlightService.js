define("tui/widget/booking/yourflight/view/VariantYourFlightService", [
    "dojo",
    "dojo/text!tui/widget/booking/yourflight/view/Templates/YourFlightTmpl.html",
    "dojo/text!tui/widget/booking/yourflight/view/Templates/FlightsOnlyTmpl.html",
    "dojo/text!tui/widget/booking/yourflight/view/Templates/CruiseAndStayTmpl.html",
    "dojo/text!tui/widget/booking/yourflight/view/Templates/BackToBackCruise.html"
   
    
    ], function (dojo,YourFlightTmpl,FlightsOnlyTmpl,CruiseAndStayTmpl,BackToBackCruise) {

    var variantYourFlightMapping = {
      'FC': YourFlightTmpl,
      'INCLUSIVE': YourFlightTmpl,
      'FNC': YourFlightTmpl,
      'CNS': CruiseAndStayTmpl,
      'SNC':CruiseAndStayTmpl,
      'BTB':BackToBackCruise,
      'FO':FlightsOnlyTmpl
    };

    return function (templateName) {
        
        return variantYourFlightMapping[templateName];
    }
})