define("tui/widget/form/MultiFieldList", ["dojo",
    "dojo/text!tui/widget/form/multiField/template/multiFieldTmpl.html",
    "dojo/on",
    "tui/widget/form/MultiFieldTextbox",
    "tui/widget/_TuiBaseWidget",
    "dijit/_TemplatedMixin",
    "tui/widget/form/AutoComplete",
    "dojo/NodeList-traverse",
    "tui/widget/Growable",
    "dojo/html",
    "tui/widget/mixins/Templatable",
    "dojox/string/BidiComplex",
    "tui/widget/mixins/Scrollable",
    "tui/utils/CancelBlurEventListener"], function (dojo, multiFieldTmpl, on) {

    dojo.declare("tui.widget.form.MultiFieldList", [tui.widget._TuiBaseWidget, dijit._TemplatedMixin, tui.widget.mixins.Scrollable, tui.utils.CancelBlurEventListener], {

        // ---------------------------------------------------------------- MultiFieldList Properties
        templateString: multiFieldTmpl,

        autocomplete: tui.widget.form.AutoComplete,

        growable: true,

        growableInput: null,

        textboxInput: null,

        textboxes: null,

        textboxvalues: null,

        autocompleteProps: null,

        allowDuplicate: false,

        activeTextboxClass: "selected",

        activeClass: "active",

        isDisplay: false,

        defaultPlaceholderTxt: "Any..",

        itemsSelected: " items selected",

        maxTextboxes: -1,

        maxHeight: -1,

        saveInputValue: false,

        duration: 500,

        blurTimeout: null,

        blurTimeoutDuration: 100,

        subscribableMethods: ["onTextboxInputBlur"],

        hasException: false,

        // ---------------------------------------------------------------- MultiFieldList methods

        postCreate: function () {
            // summary:
            //		Method called after widget creation, to initalise defaults values.
            var multiFieldList = this;

            // initialise default values.
            multiFieldList.textboxes = [];
            multiFieldList.textboxvalues = [];
            multiFieldList.autocompleteProps = {};
            multiFieldList.subscribeTochannel();
            multiFieldList.updatePlaceholder();

            // add growable behaviour to input field if required.
            if (multiFieldList.growable) {
                multiFieldList.growableInput = tui.widget.Growable(multiFieldList.textboxInput);
            }

            multiFieldList.attachTextboxlistEventListeners();
            multiFieldList.createAutocomplete();
            multiFieldList.inherited(arguments);

            dojo.attr(multiFieldList.textboxInput, "tabindex", dojo.attr(multiFieldList.domNode, "tabindex"));
        },

        cancelBlur: function () {
            var multiFieldList = this;
            multiFieldList.inherited(arguments);
            multiFieldList.textboxInput.focus();
        },

        subscribeTochannel: function () {
            // summary:
            //		subscribe to the textbox channel and listen to changes to add and remove.
            var multiFieldList = this;

            multiFieldList.subscribe("tui/widget/Textbox/onclick", function (editTextField, textbox) {
                if (editTextField == multiFieldList.editTextField) {
                    multiFieldList.toggleHighlight(textbox);
                    multiFieldList.onTextboxInputFocus();
                }
            });

            multiFieldList.subscribe("tui/widget/Textbox/onremove", function (editTextField, textbox) {
                if (editTextField == multiFieldList.editTextField) {
                    multiFieldList.removeDataItem({
                        key: textbox.getLabel(),
                        value: textbox.getValue()
                    });
                    multiFieldList.onTextboxInputFocus();
					//dualBranding
					if(dojoConfig.site == "thomson"){
						if(textbox.name == "SMR"){
							textbox.name = "COU";
							textbox.label = "COUPLES";
							textbox.value = "COU";
							
							multiFieldList.removeDataItem({
								key: textbox.getLabel(),
								value: textbox.getValue()
							});
							multiFieldList.onTextboxInputFocus();multiFieldList.removeDataItem({
								key: textbox.getLabel(),
								value: textbox.getValue()
							});
							multiFieldList.onTextboxInputFocus();
						}
						if(textbox.name == "COU" ){
							textbox.name = "SMR";
							textbox.label = "COUPLES/SENSIMAR";
							textbox.value = "SMR";
							
							multiFieldList.removeDataItem({
								key: textbox.getLabel(),
								value: textbox.getValue()
							});
							multiFieldList.onTextboxInputFocus();multiFieldList.removeDataItem({
								key: textbox.getLabel(),
								value: textbox.getValue()
							});
							multiFieldList.onTextboxInputFocus();
						}
					}
                }
            });
        },

        createAutocomplete: function () {
            // summary:
            //		Creates autocomplete for multifieldList.
            var multiFieldList = this;
            multiFieldList.autocompleteProps.elementRelativeTo = multiFieldList.domNode;
            multiFieldList.autocompleteProps.duration = multiFieldList.duration;
            multiFieldList.autocompleteProps.onElementListSelection = function (selectedData, listData) {
                if (selectedData) {
                    multiFieldList.onElementListSelection(selectedData, listData, multiFieldList.autocomplete);
                }
            };
            multiFieldList.autocomplete = new multiFieldList.autocomplete(multiFieldList.autocompleteProps, multiFieldList.textboxInput);
        },

        toggleHighlight: function (textbox) {
            var multiFieldList = this;
            var toggleAction = (dojo.hasClass(textbox.domNode, multiFieldList.activeTextboxClass)) ? "removeClass" : "addClass";
            dojo[toggleAction](textbox.domNode, multiFieldList.activeTextboxClass);
        },

        removeHighlight: function () {
            var multiFieldList = this;
            dojo.query('.' + multiFieldList.activeTextboxClass, multiFieldList.domNode).removeClass(multiFieldList.activeTextboxClass);
        },

        makeMultiFieldActive: function () {
            var multiFieldList = this;
            dojo.removeClass(multiFieldList.domNode, "in" + multiFieldList.activeClass);
            dojo.addClass(multiFieldList.domNode, multiFieldList.activeClass);
            multiFieldList.showTextBoxes();
        },

        isActive: function () {
            var multiFieldList = this;
            return dojo.hasClass(multiFieldList.domNode, multiFieldList.activeClass);
        },

        attachTextboxlistEventListeners: function () {
            // summary:
            //		Add eventlistener to DOM elements.
            var multiFieldList = this;

            on(multiFieldList.domNode, "focus, click", function () {
                // clear timeout if present (prevents input blur)
                //if(multiFieldList.blurTimeout) clearTimeout(multiFieldList.blurTimeout);
                multiFieldList.onTextboxInputFocus();
            });

            multiFieldList.connect(document.body, "onclick", function (event) {
                // hides autocompletes if not currently being selected, and is showing.
                if (document.activeElement === multiFieldList.domNode || document.activeElement === multiFieldList.autocomplete.domNode) return;
                multiFieldList.autocomplete.hideAutoCompleteList();
                multiFieldList.onTextboxInputBlur();
            });

            /*on(multiFieldList.textboxInput, "blur", function () {
                // set timeout on blur in case target === multiFieldList
                multiFieldList.blurTimeout = setTimeout(function(){
                    multiFieldList.onTextboxInputBlur();
                }, multiFieldList.blurTimeoutDuration)
            });*/

            // keyboard eventlisteners.
            on(multiFieldList.textboxInput, "keydown", function (event) {
                var position;
                dojo.publish(multiFieldList.declaredClass + ".closeLimitPopup");

                switch (event.keyCode) {
                    case dojo.keys.BACKSPACE:
                        position = dojox.string.BidiComplex._getCaretPos(event, this);
                        keyboardDeleteTextbox(position);
                        break;

                    case dojo.keys.DELETE:
                        position = dojox.string.BidiComplex._getCaretPos(event, this);
                        keyboardDeleteTextbox(position);
                        break;

                    default:
                        if (multiFieldList.maxTextboxes === multiFieldList.textboxes.length) {
                            //dojo.stopEvent(event);
                            multiFieldList.onTextboxLimit(event);
                        }
                }
            });

            on(multiFieldList.textboxInput, "keyup", function (event) {
                multiFieldList.displayMultifieldInfo();
            });

            function keyboardDeleteTextbox(position) {
                // inner function for deleting textbox by keyboard press.
                if (position[0] === 0 && multiFieldList.textboxes.length > 0) {
                    var deleted = 0;
                    // delete selected textbox(es)
                    _.forEach(multiFieldList.textboxes, function (textbox) {
                        if (dojo.hasClass(textbox.domNode, multiFieldList.activeTextboxClass)) {
                            multiFieldList.removeDataItem({
                                key: textbox.getLabel(),
                                value: textbox.getValue()
                            });
                            deleted++;
                        }
                    });
                    // or just the last item
                    if (deleted === 0) {
                        multiFieldList.textboxInput.focus();
                        multiFieldList.removeHighlight();
                        var textbox = multiFieldList.textboxes[multiFieldList.textboxes.length - 1]
                        multiFieldList.removeDataItem({
                            key: textbox.getLabel(),
                            value: textbox.getValue()
                        });
                    }
                }
            }

        },

        onTextboxInputFocus: function () {
            // summary:
            //		Activates field and focuses input element
            var multiFieldList = this;
            multiFieldList.makeMultiFieldActive();
            multiFieldList.textboxInput.focus();
        },

        onTextboxInputBlur: function () {
            // summary:
            //		Deactivates field, hides textboxes
            var multiFieldList = this;
            multiFieldList.textboxInput.blur();
            dojo.removeClass(multiFieldList.domNode, multiFieldList.activeClass);
            dojo.addClass(multiFieldList.domNode, "in" + multiFieldList.activeClass);
            multiFieldList.performAfterBlur(function () {
                multiFieldList.resetTextboxInput();
                multiFieldList.hideTextBoxes();
                multiFieldList.displayMultifieldInfo();
            }, 150);
        },

        displayMultifieldInfo: function () {
            var multiFieldList = this;
            if (multiFieldList.textboxes.length === 1 && multiFieldList.textboxInput.value === "") {
                dojo.style(multiFieldList.multifieldInfo, "display", "block");
                dojo.addClass(multiFieldList.textboxes[0].domNode, "truncate");
                return;
            }
            if (multiFieldList.textboxes[0]) {
                dojo.removeClass(multiFieldList.textboxes[0].domNode, "truncate");
            }
            multiFieldList.hideMultifieldInfo();

        },

        hideMultifieldInfo: function () {
            var multiFieldList = this;
            dojo.style(multiFieldList.multifieldInfo, "display", "none");
        },

        removeAll: function () {
            // summary:
            //		Removes all textboxes in list, reseting all values but to default.
            var multiFieldList = this;
            for (var i = 0; i < multiFieldList.textboxes.length; i++) {
                var textbox = multiFieldList.textboxes[i];
                textbox.remove();
                multiFieldList.onRemoveTextbox(textbox);
            }
            multiFieldList.textboxes.length = 0;
            multiFieldList.textboxvalues.length = 0;
            multiFieldList.updatePlaceholder();
            multiFieldList.hideTextBoxes();
        },

        removeTextbox: function (selectedData) {
            // summary:
            //		Removes textbox by given key/value pair data.
            var multiFieldList = this;
            for (var i = 0; i < multiFieldList.textboxes.length; i++) {
                var textbox = multiFieldList.textboxes[i];
                if (selectedData.value === textbox.value) {
                    textbox.remove();
                    multiFieldList.textboxes.splice(i, 1);
                    multiFieldList.textboxvalues.splice(i, 1);
                    multiFieldList.onRemoveTextbox(textbox, selectedData);

                    (multiFieldList.textboxes.length === 1 || dojo.hasClass(multiFieldList.domNode, "active")) ?
                    multiFieldList.showTextBoxes() : multiFieldList.hideTextBoxes();
                    multiFieldList.updatePlaceholder();
                    break;
                }
            }

            if (multiFieldList.scrollable && multiFieldList.scrollPanels) {
                var height = dojo.style(multiFieldList.scrollerpane, "height");
                if (height < multiFieldList.maxHeight) {
                    multiFieldList.removeScrollerPanel();
                } else {
                    multiFieldList.scrollPanels[0].update();
                }
                multiFieldList.scrollPanels[0].updateScrollerPosition(100);
            }
        },

        addNewTextbox: function (selectedData) {
            // summary:
            //		Adds textbox by given key/value pair data.
            var multiFieldList = this;
            if (!multiFieldList.isValidMatch(selectedData)) {
                return;
            }

            var textbox = new tui.widget.form.MultiFieldTextbox({
                label: selectedData.key,
                value: selectedData.value,
                name: selectedData.value,
                display: multiFieldList.isDisplay
            });

            textbox.create(multiFieldList.editTextField);

            multiFieldList.textboxes.push(textbox);
            multiFieldList.textboxvalues.push(textbox.getValue());
            multiFieldList.resetTextboxInput();
            multiFieldList.onAddNewTextbox(textbox, selectedData);
            (multiFieldList.textboxes.length === 1 || dojo.hasClass(multiFieldList.domNode, "active")) ?
                multiFieldList.showTextBoxes() : multiFieldList.hideTextBoxes();
            multiFieldList.updatePlaceholder();
            multiFieldList.attachScrollerPanel();
            multiFieldList.displayMultifieldInfo();

        },

        removeScrollerPanel: function () {
            var multiFieldList = this;

            if (!multiFieldList.scrollable || !multiFieldList.scrollPanels) {
                return;
            }

            dojo.style(multiFieldList.scrollerpane, "height", "auto");
            dojo.query(".viewport", multiFieldList.scrollerpane).style("height", "auto");
            dojo.query(".track", multiFieldList.scrollerpane).style("display", "none");
        },

        attachScrollerPanel: function () {
            var multiFieldList = this;
            var height = dojo.style(multiFieldList.scrollerpane, "height");
            if (multiFieldList.scrollable && height >= multiFieldList.maxHeight) {
                dojo.style(multiFieldList.scrollerpane, "height", [multiFieldList.maxHeight, "px"].join(""));
                if (!multiFieldList.scrollPanels) {
                    multiFieldList.addScrollerPanel(multiFieldList.domNode);
                    // once scroll panel is created add attach on move listener
                    /*multiFieldList.connect(multiFieldList.scrollPanels[0], "onMove", function (e) {
                        console.log(e)
                        //multiFieldList.onTextboxInputFocus();
                    });*/
                } else {
                    dojo.query(".track", multiFieldList.scrollerpane).style("display", "block");
                    multiFieldList.scrollPanels[0].update();
                }
                multiFieldList.scrollPanels[0].updateScrollerPosition(100);
            }
        },

        updatePlaceholder: function () {
            var multiFieldList = this;
            var text = (multiFieldList.textboxes.length > 0) ?
                       multiFieldList.textboxes.length + multiFieldList.itemsSelected :
                       multiFieldList.defaultPlaceholderTxt;
            dojo.html.set(multiFieldList.placeholder, text + '<span></span>');
            if (multiFieldList.textboxes.length > 1) {
                dojo.addClass(multiFieldList.placeholder, 'arrow');
            }
            if (multiFieldList.textboxes.length <= 1 && dojo.hasClass(multiFieldList.placeholder, 'arrow')) {
                dojo.removeClass(multiFieldList.placeholder, 'arrow');
            }
        },

        isValidMatch: function (selectedData) {
            var multiFieldList = this;
            if (!multiFieldList.allowDuplicate) {
                var index = _.indexOf(multiFieldList.textboxvalues, selectedData.value);
                if (index > -1) {
                    multiFieldList.resetTextboxInput();
                    return false;
                }
            }
            return true;
        },

        resetTextboxInput: function () {
            var multiFieldList = this;
            if (!multiFieldList.saveInputValue) {
                multiFieldList.textboxInput.value = "";
                if (multiFieldList.growable) {
                    multiFieldList.growableInput.reset();
                }
            }
        },

        setDataItem: function (selectedData) {
            var multiFieldList = this;
            multiFieldList.addNewTextbox(selectedData);
        },

        removeDataItem: function (selectedData) {
            var multiFieldList = this;
            multiFieldList.removeTextbox(selectedData);
        },

        showTextBoxes: function () {
            var multiFieldList = this;
            multiFieldList.isDisplay = true;
            dojo.style(multiFieldList.editTextField, "display", "block");
            for (var i = 0; i < multiFieldList.textboxes.length; i++) {
                dojo.style(multiFieldList.textboxes[i].domNode, "display", "block");
            }
            dojo.style(multiFieldList.placeholder, "display", "none");
            multiFieldList.attachScrollerPanel();
        },

        hideTextBoxes: function () {
            var multiFieldList = this;
            multiFieldList.removeHighlight();
            if (multiFieldList.textboxes.length > 1 || multiFieldList.textboxes.length === 0) {
                multiFieldList.isDisplay = false;
                dojo.style(multiFieldList.editTextField, "display", "none");
                for (var i = 0; i < multiFieldList.textboxes.length; i++) {
                    dojo.style(multiFieldList.textboxes[i].domNode, "display", "none");
                }

                dojo.style(multiFieldList.placeholder, "display", "block");
                multiFieldList.removeScrollerPanel();
            }
        },

        unselect: function () {
            var multiFieldList = this;
            if (!multiFieldList.saveInputValue) {
                multiFieldList.textboxInput.value = "";
            }
            multiFieldList.textboxInput.blur();
            multiFieldList.updatePlaceholder();
            multiFieldList.hideTextBoxes();
        },

        /*focusTextbox: function () {
            var multiFieldList = this;
            multiFieldList.onTextboxInputFocus();
        },*/

        // ---------------------------------------------------------------- event methods
        onTextboxLimit: function (event) {},

        onAddNewTextbox: function (textbox, selectedData) {},

        onRemoveTextbox: function (textbox, selectedData) {
            var multiFieldList = this;
            multiFieldList.displayMultifieldInfo();
        },

        onElementListSelection: function (selectedData, listData, autocomplete) {
            var multiFieldList = this;
            autocomplete.hideList();
            if (!multiFieldList.isValidMatch(selectedData)) {
                multiFieldList.onTextboxInputFocus();
                return;
            }
            multiFieldList.setDataItem(selectedData);
            multiFieldList.onTextboxInputBlur();
        }
    });

    return tui.widget.form.MultiFieldList;
});