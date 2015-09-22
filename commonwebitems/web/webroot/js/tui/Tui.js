define([
	'dojo',
  'dojo/on',
	'dojo/_base/lang',
	'tui/sniffing/form',
	'dojox/mobile/sniff',
	'dojo/query',
  'tui/utils/TuiUnderscore'], function(dojo, on, lang, has, sniff, query) {

	var tui = dojo.getObject('tui', true);

	lang.mixin(tui, {

		version: '0.1',

		appConfig: {
			controllers: {},
			singleton: {}
		},

		addSingleton: function(id, instance) {
			// summary:
			//      adds singleton class instance to app config.
			if (!tui.appConfig.singleton[id]) {
				tui.appConfig.singleton[id] = instance;
			}
			return tui.getSingleton(id);
		},

		getSingleton: function(id) {
			// summary:
			//      get singleton class instance from app config.
			//
			//      return singleton instance.
			return tui.appConfig.singleton[id];
		},

		addController: function(controller) {
			// summary:
			//      adds reference to a controller instance to the appConfig
			//      using class name as the key.
			if (!tui.appConfig.controllers[controller.declaredClass]) {
				tui.appConfig.controllers[controller.declaredClass] = controller;
			} else {
				throw new Error('A controller of type ' + controller.declaredClass + ' already exists');
			}
		},

		getController: function(name) {
			return tui.appConfig.controllers[name];
		},

		init: function(callback) {
			dojo.ready(function() {
				//commenting the line as adding "touch" class is done with mobile detection
				//tui.addTouchSupport();
				dojo.parser.parse();
				tui.ensLinkTacking();
				tui.addClickableEvents();
				tui.polyfills();
				dojo.publish('tui:channel=lazyload');
				tui.oninit();
				if (callback) {
					callback();
				}
			});
		},

		addTouchSupport: function() {
			if (sniff('touch')) {
				dojo.addClass(document.body, 'touch');
			}
			else{
				dojo.addClass(document.body, 'no-touch');
			}
		},

		ensLinkTacking: function() {
			dojo.query('.ensLinkTrack').onclick(function(event) {
				var componentid = dojo.attr(this, 'data-componentId');
				ensLinkTrack(this, componentid);
			});
		},

		polyfills: function() {
			tui.placeHolderPloyfill();
		},

		addClickableEvents: function() {
			dojo.query('.clickable').forEach(function(item, i) {
				var url = dojo.attr(item, 'data-clickable-url');
				dojo.connect(item, 'onclick', function() {
					document.location.href = url;
				});
			});
			// handle events for link(s) with class .history-back and go back in history, optionally items can specify steps using "data-back-steps" attribute
			dojo.query('.history-back').on('click', function(evt) {
				var steps = dojo.attr(evt.target, 'data-back-steps') ? dojo.attr(evt.target, 'data-back-steps') : -1;
				window.history.go(steps);
			});
			// handle events for link(s) with class .history-reload, reloads the page
			dojo.query('.history-reload').on('click', function() {
				window.location.reload();
			});
      // handle print button click
      dojo.query('.print-page').on('click', function(){
        window.print();
      });
		},

		placeHolderPloyfill: function() {
			if (!has('input-attr-placeholder')) {
				var elements = query('[placeholder]');
				_.forEach(elements, function(element) {
					tui.addplaceholderTxt(element);
					dojo.connect(element, 'onfocus', function(event) {
						dojo.stopEvent(event);
						setTimeout(function() {
							tui.removeplaceholderTxt(element);
						}, 100);
					});
					dojo.connect(element, 'onblur', function(event) {
						dojo.stopEvent(event);
						setTimeout(function() {
							tui.addplaceholderTxt(element);
						}, 200);
					});
				});
			}
		},

		removePlaceHolders: function(form) {
			var placeholderfields = query('[placeholder]', form);
			_.forEach(placeholderfields, function(field, i) {
				tui.removeplaceholderTxt(field);
			});
		},

		removeplaceholderTxt: function(field) {
			if (field.value === dojo.attr(field, 'placeholder')) {
				dojo.removeClass(field, 'placehold');
				field.value = '';
			}
		},

		resetPlaceHolders: function(form) {
			if (has('input-attr-placeholder')) return;
			var placeholderfields = query('[placeholder]', form);
			_.forEach(placeholderfields, function(field, i) {
				tui.addplaceholderTxt(field);
			});
		},

		addplaceholderTxt: function(field) {
			if (has('input-attr-placeholder')) return;
			if (field.value === '') {
				dojo.addClass(field, 'placehold');
				field.value = dojo.attr(field, 'placeholder');
			}
		},

		oninit: function() {}
	});

	return tui;
});
