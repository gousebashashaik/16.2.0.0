// adding js class to HTML.
//From Common web
document.getElementsByTagName('html')[0].className += ' js ';

// method to avoid ie7 throwing console.* errors
if (!window.console) {
  (function() {
    var names = ['log', 'debug', 'info', 'warn', 'error'],
      i, l = names.length;
    window.console = {};
    for (i = 0; i < l; i++) {
      window.console[names[i]] = function() {
      };
    }
  }());
}

var dojoConfig = {
  async: true,
  waitSeconds: 5,
  site: tuiSiteName,
  prodDebug: false,
  devDebug: true,
  styleSwitch: true,
  context: '',
  currency:currency,
  paths: {
        tui: tuiWebrootPath + '/js/tui',
        google: tuiWebrootPath + '/js/google',
        webRoot: tuiWebrootPath,
		cdnDomain: tuiCdnPath
  },
  moduleNames: ['dojo', 'tui/Tui', 'dojo/parser', 'dojo/dom-attr', 'dojo/NodeList-traverse', 'dojo/selector/acme'],
  addModuleName: function() {
    var index;
    var moduleName;
    for (var i = 0; i < arguments.length; i++) {
      index = -1;
      moduleName = arguments[i];
      if (dojoConfig.moduleNames.indexOf) {
        index = dojoConfig.moduleNames.indexOf(moduleName);
      } else {
        for (var j = 0; j < dojoConfig.moduleNames.length; j++) {
          if (dojoConfig.moduleNames[j] === moduleName) {
            index = j;
            break;
          }
        }
      }
      if (index === -1) {
        dojoConfig.moduleNames.push(moduleName);
      }
    }
  },
  init: function() {
    require(dojoConfig.moduleNames, function(dojo) {
      tui.init();
    });
  }
};
