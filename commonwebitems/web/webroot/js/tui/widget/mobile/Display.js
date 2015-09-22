define('tui/widget/mobile/Display', [
  'dojo'], function(dojo) {

  //order is important here.
  var displays = [
    {
      'name': 'Mobile',
      'min-width': 0,
      'max-width': 579,
      'type' : 'onMobile',
      'handle': function(widget) {
      
      }
    },
    {
      'name': 'MiniTablet',
      'min-width': 580,
      'max-width': 759,
      'type' : 'onMiniTablet',
      'handle': function (widget) {
       
      }
    },
    {
      'name': 'Tablet',
      'min-width': 760,
      'max-width': 959,
      'type' : 'onTablet',
      'handle': function(widget) {
       
      }
    },
    {
      'name': 'Desktop',
      'min-width': 760,
      'max-width': 9999,
      'type' : 'onDesktop',
      'handle': function(widget) {
        
      }
    }
  ];

  function fireSwitch(widget, display) {
    function fire() {
      display.handle(widget);
      widget.currentDisplay = display['name'];
    }

    function fireOnlyWhenSwitch() {
      if (widget.currentDisplay != display['name']) {
        fire();
      }
    }
    widget.continuouslyAdapt ? fire() : fireOnlyWhenSwitch();
  }


  return {
    adapt: function(widget) {

      function adapter() {
        var clientWidth = document.documentElement.clientWidth;
        var display = _.find(displays, function (display) {
          return clientWidth <= display['max-width'] && clientWidth >= display['min-width'] && widget[display['type']];
        });
        if (display) {
          fireSwitch(widget, display);
        }
      }
      //fire once on page load!
      adapter();

      dojo.connect(window, 'onresize', _.throttle(adapter, 1000));
    }
  };
});


