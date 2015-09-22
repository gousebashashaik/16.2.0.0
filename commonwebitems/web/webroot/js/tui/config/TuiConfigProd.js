// This file is now inlined in header.
//From Common web
// When you change it, remember to update the JSP as well.

// This value is updated at build time.
var buildVersion = '12345';

// adding js class to HTML.
document.getElementsByTagName('html')[0].className += ' js ';

var dojoConfig = {
  async: true,
  waitSeconds: 5,
  site: tuiSiteName,
  prodDebug: true,
  devDebug: false,
  styleSwitch: true,
  context: '',
  currency:currency,
  paths: {
    tui: tuiCdnPath + '/js/minified/' + buildVersion + '/tui',
    google: tuiCdnPath + '/js/minified/' + buildVersion + '/google',
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
    require(['tui/tui-default'], function() {
      require(dojoConfig.moduleNames, function(dojo) {
        tui.init();
      });
    });
  }
};
