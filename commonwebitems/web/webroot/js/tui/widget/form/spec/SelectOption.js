define('tui/widget/form/spec/SelectOption', [
  'dojo/on',
  'tui/widget/form/SelectOption'
], function(on, SelectOption) {

  describe('SelectOption', function() {
    var selectOption1;
    var selectOption2;
    var selectOption3;

    beforeEach(function() {
      // Load the HTML fixtures.
      loadFixtures('js/tui/widget/form/spec/SelectOption.html');

      // Instantiate widgets.
      selectOption1 = new SelectOption({}, dojo.byId('selectOption1'));
      selectOption2 = new SelectOption({}, dojo.byId('selectOption2'));
      selectOption3 = new SelectOption({}, dojo.byId('selectOption3'));
    });

    afterEach(function() {
      // Destroy widgets.
      selectOption1.destroy(true);
      selectOption2.destroy(true);
      selectOption3.destroy(true);
    });

    it('should expose a function and instantiate an object', function() {
      expect(typeof SelectOption).toEqual('function');
      expect(typeof selectOption1).toEqual('object');
      expect(typeof selectOption2).toEqual('object');
    });

    it('should display list on mouse click', function() {
      on.emit(selectOption1.selectDropdown, 'click', {bubbles: true});

      expect(dojo.style(selectOption1.listElement, 'display')).toEqual('block');
      expect(selectOption1.listShowing).toBeTruthy();
    });

    it('should hide list when handle is clicked', function() {
      on.emit(selectOption1.selectDropdown, 'click', {bubbles: true});
      on.emit(selectOption1.selectDropdown, 'click', {bubbles: true});

      expect(dojo.style(selectOption1.listElement, 'display')).toEqual('none');
      expect(selectOption1.listShowing).toBeFalsy();
    });

    it('should hide list when another element is clicked', function() {
      on.emit(selectOption1.selectDropdown, 'click', {bubbles: true});
      on.emit(selectOption2.selectDropdown, 'click', {bubbles: true});

      expect(dojo.style(selectOption1.listElement, 'display')).toEqual('none');
      expect(selectOption1.listShowing).toBeFalsy();
    });

    it('should change the value to 1 when selecting the second item from list', function() {
      var elementToClick = dojo.query('li', selectOption1.listElement)[1];
      on.emit(selectOption1.selectDropdown, 'click', {bubbles: true});
      on.emit(elementToClick, 'mouseover', {bubbles: true});
      on.emit(elementToClick, 'mousedown', {bubbles: true});
      on.emit(elementToClick, 'mouseup', {bubbles: true});
      on.emit(elementToClick, 'click', {bubbles: true});

      expect(dojo.style(selectOption1.listElement, 'display')).toEqual('none');
      expect(selectOption1.listShowing).toBeFalsy();
      // select data should be 1
      var selectData = selectOption1.getSelectedData();
      expect(parseInt(selectData.value, 10)).toEqual(1);
      expect(dojo.query(selectOption1.selectDropdownLabel).text()).toEqual('1');
    });
  });

  // @todo Continue to port the rest of the tests.

});
