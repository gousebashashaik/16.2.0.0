define('tui/singleAccom/service/spec/FlightOptionService', [
  'tui/singleAccom/service/FlightOptionService'
], function(flightOptionService) {

  describe('Flight option service', function() {
    it('should expose an object', function() {
      expect(typeof flightOptionService).toEqual('object');
    });
  });
});
