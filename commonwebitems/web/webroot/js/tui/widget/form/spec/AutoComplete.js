define('tui/widget/form/spec/AutoComplete', [
  'dojo/on',
  'tui/widget/form/AutoComplete'
], function(on, AutoComplete) {

  describe('AutoComplete', function() {
    var autocomplete1;
    var autocomplete2;

    beforeEach(function() {
      // Load the HTML fixtures.
      loadFixtures('js/tui/widget/form/spec/AutoComplete.html');

      // Instantiate widgets.
      autocomplete1 = new AutoComplete({}, dojo.byId('autocomplete1'));
      autocomplete2 = new AutoComplete({}, dojo.byId('autocomplete2'));
    });

    afterEach(function() {
      // Destroy widgets.
      autocomplete1.destroy(true);
      autocomplete2.destroy(true);
    });

    it('should expose a function and instantiate an object', function() {
      expect(typeof AutoComplete).toEqual('function');
      expect(typeof autocomplete1).toEqual('object');
      expect(typeof autocomplete2).toEqual('object');
    });

    /*it('should display results on 3 or more chars', function() {
     on.emit($autocomplete1, 'click', {bubbles: true});
     //doh.robot.typeKeys("abc", 500, 300);

     expect(dojo.style(autocomplete1.listElement, 'display')).toEqual('block');
     expect(autocomplete1.listShowing).toBeTruthy();
     });*/
  });
});
