define(["dojo", "dojo/_base/lang", "tui/Tui", "tui/widget/search/ErrorSearchPopup"], function (dojo, lang, tui) {

	tui.errorPopups = {};

	tui.init = function (callback) {
		dojo.ready(function () {
			tui.addTouchSupport();
			dojo.parser.parse();
			dojo.publish("tui/lazyload");
			dojo.publish("tui/pageController");
			tui.ensLinkTacking();
			tui.addClickableEvents();
			tui.polyfills();
			dojo.publish("tui:channel=lazyload");
			tui.oninit();
			if (callback) {
				callback();
			}
		})
	};

	tui.getFormElementByName = function (name, from) {
		var fields = tui.getFormElementsByName(name, from);
		return (fields.length > 0) ? fields[0] : null;
	};

	tui.getFormElementsByName = function (name, from) {
		return dojo.query(["[name='" + name + "']"].join(""), from || document.body);
	};

	tui.setFormElementByName = function (name, from, values) {
		var fields = tui.getFormElementsByName(name, from);
		fields.forEach(function (field, i) {
			field.value = (dojo.isArray(values)) ? values[i] : values;
		});
		return fields;
	};


	tui.showDefaultSearchErrorPopup = function (element, messageOption, tmplPath) {

		tui.removeErrorPopup(element);

		var options = {
			elementRelativeTo: element
		};

		if (tmplPath) options.tmplPath = tmplPath;

		if (lang.isString(messageOption)) {
			options.errorMessage = messageOption;
		} else {
			lang.mixin(options, messageOption)
		}


		var error = new tui.widget.search.ErrorSearchPopup(options, null);
		error.open();

		dojo.addClass(element, "error");
		tui.errorPopups[element.id] = error;

		error._errorOnFocusFn = dojo.connect(element, "onfocus", function (event) {
			tui.removeErrorPopup(element);
		});
		return error;
	};

	tui.removeErrorPopup = function (element) {
		var popuperror = tui.errorPopups[element.id];

		if (popuperror) {
			dojo.removeClass(element, "error");
			dojo.disconnect(popuperror._errorOnFocusFn);
			delete tui.errorPopups[element.id];
			popuperror.close();
			popuperror.destroy();
		}
	};

	tui.removeAllErrorPopups = function () {
		for (prop in tui.errorPopups) {
			tui.removeErrorPopup(dojo.byId(prop));
		}
	};

	tui.widget.search.AirportGuideExpandable.prototype.showWidget = function (/*Node?*/ element) {
		var _tuiBaseWidget = this;
		var elementtoshow = (element || _tuiBaseWidget.domNode);
		dojo.setStyle(elementtoshow, "display", "block");
	};
	tui.widget.search.AirportGuideExpandable.prototype.isShowing = function (/*Node?*/ element) {
		var _tuiBaseWidget = this;
		element = element || _tuiBaseWidget.domNode;
		return (dojo.style(element, "display") === "block");
	};
	tui.widget.search.AirportGuideExpandable.prototype.hideWidget = function (/*Node?*/ element) {
		var _tuiBaseWidget = this;
		var elementtoshow = (element || _tuiBaseWidget.domNode);
		dojo.setStyle(elementtoshow, "display", "none");
	};

	tui.widget.search.DestinationGuideExpandable.prototype.showWidget = function (/*Node?*/ element) {
		var _tuiBaseWidget = this;
		var elementtoshow = (element || _tuiBaseWidget.domNode);
		dojo.setStyle(elementtoshow, "display", "block");
	};
	tui.widget.search.DestinationGuideExpandable.prototype.isShowing = function (/*Node?*/ element) {
		var _tuiBaseWidget = this;
		element = element || _tuiBaseWidget.domNode;
		return (dojo.style(element, "display") === "block");
	};

	tui.widget.search.DestinationGuideExpandable.prototype.hideWidget = function (/*Node?*/ element) {
		var _tuiBaseWidget = this;
		var elementtoshow = (element || _tuiBaseWidget.domNode);
		dojo.setStyle(elementtoshow, "display", "none");
	};


});