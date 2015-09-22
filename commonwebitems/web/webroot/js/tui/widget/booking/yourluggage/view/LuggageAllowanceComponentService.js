define("tui/widget/booking/yourluggage/view/LuggageAllowanceComponentService", [
    "dojo",
    "dojo/text!tui/widget/booking/yourluggage/view/Templates/FlightLuggageAllowanceComponent.html",
    "dojo/text!tui/widget/booking/yourluggage/view/Templates/LuggageAllowanceComponent.html"
    ], function (dojo,FlightLuggageAllowanceComponent,LuggageAllowanceComponent) {

    var LuggageAllowanceComponentMapping = {
      'FC':LuggageAllowanceComponent,
      'INCLUSIVE':LuggageAllowanceComponent,
      'FNC':LuggageAllowanceComponent,
      'FO':FlightLuggageAllowanceComponent
    };

    return function (templateName) {
        
        return LuggageAllowanceComponentMapping[templateName];
    }
})