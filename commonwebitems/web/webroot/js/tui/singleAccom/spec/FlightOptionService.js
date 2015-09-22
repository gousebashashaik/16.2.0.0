describe("Flight Option Service Specifications", function () {
    var flightOptionService;

    beforeEach(function () {
        flightOptionService = dojo.require("tui/singleAccom/service/FlightOptionService");
    });

    it("Should be loaded", function () {
        expect(flightOptionService).not.toBe(null);
    });

});

