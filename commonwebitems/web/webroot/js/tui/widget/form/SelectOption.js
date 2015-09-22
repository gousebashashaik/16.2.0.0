define("tui/widget/form/SelectOption", [
    "dojo",
    "dojo/on",
    "dojo/text!tui/widget/form/selectOption/template/SelectOption.html",
    "dojox/dtl",
    "dojox/dtl/Context",
    "dojox/dtl/tag/logic",
    "dojox/dtl/_DomTemplated",
    "dojo/NodeList-traverse",
    "tui/widget/_TuiBaseWidget",
    "tui/widget/mixins/Listable",
    "dojo/html",
    "dojo/query",
    "dojo/NodeList-dom",
    "dojo/dom-construct"], function (dojo, on, selectOptionTmpl) {

    dojo.declare("tui.widget.form.SelectOption", [tui.widget._TuiBaseWidget, tui.widget.mixins.Listable, dojox.dtl._DomTemplated], {

        // Reference to the HTML select node, which associated with SelectOption widget.
        selectNode: null,

        arrow: false,

        // Reference to the dom node for selectOption. When selected displays associated list.
        selectDropdown: null,

        // Reference to the dom node for label text element
        selectDropdownLabel: null,

        // Property which determines, if the selectOption list should be the same width
        // as selectOption dom node.
        fixedWidth: false,

        // Property which determines, if key down selects an item from list.
        selectOnKeyUpDown: true,

        subscribableMethods: ["resize", "hideList"],

        showOnClick: true,

        // ---------------------------------------------------------------- _DomTemplated properties

        // Template html used for select option.
        templateString: selectOptionTmpl,

        // ---------------------------------------------------------------- tui.widget.mixins.Listable properties

        // Property name from data for label.
        titleProp: "text",

        // Property for current selected list data.
        listData: null,

        // Property which determines the max height for list.
        maxHeight: 280,

        // ----------------------------------------------------------------  tui.widget.mixins.FloatPosition

        // Determines the offset position for selectoption list.
        posOffset: {top: -3, left: 2},

        // ---------------------------------------------------------------- tui.widget.mixins.Listable methods

        createListElement: function () {
            // summary:
            //   Creates UL dom node, where the list will be inserted.
            //    Overridden for Listable class.
            var selectOption = this;
            dojo.place(selectOption.selectNode, selectOption.domNode, "last");
            selectOption.createOptions();
            selectOption.listElement = dojo.create("div", {
                "class": "dropdownlist"
            }, document.body, "last");
            if(selectOption.arrow){
	            dojo.create("span", {
	                "class": "arrow"
	            }, selectOption.listElement, "first");
            }
            selectOption.listElementUL = dojo.place("<ul></ul>", selectOption.listElement, 'last');
            dojo.setStyle(selectOption.listElement, {"display": "none"});
        },
        
        unSelect: function () {
            // summary:
            //   un-selects select option, and set it back to its default state, which
            //   is the first element in list.
            var selectOption = this;
            selectOption.listIndex = 0;
            selectOption.setHighlight();
            selectOption.setSelectedData();
        },

        // ---------------------------------------------------------------- tui.widget.form.SelectOption methods

        postMixInProperties: function () {
            // summary:
            //   Setting default values for selectOption.
            var selectOption = this;
            selectOption.listData = [];
            selectOption.selectNode = selectOption.srcNodeRef;
            selectOption._selindex = selectOption.selectNode.selectedIndex;
            selectOption._buildListDataFromOption();
            selectOption.inherited(arguments);
        },

        createOptions: function () {
            var selectOption = this;
            if (!selectOption.listData) return;
            var option;
            dojo.empty(selectOption.selectNode);
            _.forEach(selectOption.listData, function (item) {
                option = dojo.create("option", null, selectOption.selectNode, "last");
                option.text = item.text;
                option.value = item.value;
            });

            // try/catch is just for good 'ol IE browsers if index is -1
            try {
                selectOption.selectNode.options[selectOption._selindex].selected = true;
            }
            catch (e) {
                selectOption.selectNode.options[0].selected = true;
            }
        },

        _buildListDataFromOption: function () {
            // summary:
            //   Creates selectOption data list, from given HTML option values.
            var selectOption = this;
            var options = dojo.query("option", selectOption.selectNode);
            options.forEach(function (item) {
                selectOption.listData.push({
                    text: item.text,
                    value: item.value,
                    disabled: item.disabled
                });
            });
        },

        buildListData: function (dataresults) {
            var selectOption = this;
            selectOption.listData.length = 0;
            selectOption.parseJsonData(dataresults);
            selectOption.createOptions();
            selectOption.renderList();
        },

        parseJsonData: function (jsonData) {
            var selectOption = this;
            jsonData.forEach(function (item) {
                selectOption.listData.push({
                    text: item.text,
                    value: item.value,
                    disabled: !!item.disabled
                });
            });
        },

        onDownArrowKey: function (event, domNode) {
            var selectOption = this;
            selectOption.inherited(arguments);
            if (selectOption.selectOnKeyUpDown) {
                selectOption.setSelectedData();
            }
        },

        onUpArrowKey: function (event, domNode) {
            var selectOption = this;
            selectOption.selectDropdown.focus();
            selectOption.inherited(arguments);
            if (selectOption.selectOnKeyUpDown) {
                selectOption.setSelectedData();
            }
        },

        postCreate: function () {
            var selectOption = this;
            selectOption.listableInit();

            dojo.connect(selectOption.selectDropdown, "onclick", function (event) {
                //dojo.stopEvent(event);
                dojo.publish("tui/widget/form/SelectOption/onclick", [selectOption, event]);
                dojo.addClass(selectOption.selectDropdown, "open");
                selectOption.selectDropdown.focus();
                selectOption.onSelectOptionsClick(event, this);

                if (selectOption.listShowing) {
                    selectOption.hideList();
                    return;
                }
                if(dojoConfig.site == 'flights'){
                	selectOption.fixedWidth = true;
                }
                if (selectOption.fixedWidth) {
                    var width = dojo.style(selectOption.selectDropdown, "width");
                    dojo.style(selectOption.listElement, "width", [width-2, "px"].join(""));
                }
                if(selectOption.showOnClick){
                selectOption.showList();
                }
				//Hide dropdown list on scrolling
				var thisId = "#"+selectOption.id;
				var list = dojo.query(thisId).parents();				
				list.forEach(function(node){ 					
					if(dojo.getStyle(node,'overflow').indexOf("auto") != -1){
						on(node, 'scroll',function(){ 
							 dojo.removeClass(selectOption.selectDropdown, "focus");
							 selectOption.hideList();
						});
					}
				});
            });

            // add/remove focus class on focus/blur
            on(selectOption.selectDropdown, "focus", function (event) {
                dojo.addClass(selectOption.selectDropdown, "focus");
            });

            on(selectOption.selectDropdown, "blur", function () {
                dojo.removeClass(selectOption.selectDropdown, "focus");
            });

            selectOption.subscribe("tui/widget/form/SelectOption/onclick", function (selectOptionObj) {
                var selectOption = this;
                if (selectOption === selectOptionObj) return;
                selectOption.hideList();
            });

            selectOption.subscribe("tui.searchBPanel.view.DropDownSelector", function (canPositionDropDown) {
                var selectOption = this;
                selectOption.posElement(selectOption.listElement);
            });


            dojo.connect(document.body, "onclick", function (event) {
                if (document.activeElement === selectOption.selectDropdown || !selectOption.listShowing) {
                    return;
                }
                selectOption.hideList();
            });

            selectOption.attachListKeyEvent(selectOption.selectDropdown);
            selectOption.renderList();

            selectOption.inherited(arguments);
            dojo.attr(selectOption.selectDropdown, "tabindex", dojo.attr(selectOption.selectNode, "tabindex") || 0);
            dojo.attr(selectOption.domNode, "tabindex", -1);
            dojo.attr(selectOption.selectNode, "tabindex", -1);
            selectOption.tagElement(selectOption.selectDropdown, 'geoSelection');
            dojo.subscribe("tui:channel=modalOpening", function () {
                if (selectOption.listShowing) selectOption.hideList();
            });
        },

        hideList: function () {
            var selectOption = this;
            dojo.removeClass(selectOption.selectDropdown, "open");
            selectOption.inherited(arguments);
        },

        onBeforeRender: function () {
            var selectOption = this;
            dojo.query(selectOption.listElementUL).orphan();
            dojo.query(selectOption.listElement).orphan();
            selectOption.createListElement();
        },

        renderList: function () {
            var selectOption = this;
            selectOption.inherited(arguments);
            selectOption.setSelectedIndex(selectOption.selectNode.selectedIndex);
        },

        resize: function () {
            var selectOption = this;
            if (selectOption.listShowing) {
                selectOption.onRepositionList(selectOption.listElement);
            }
        },

        onChange: function (name, oldValue, newvalue) {
            var selectOption = this;
            dojo.html.set(selectOption.selectDropdownLabel, newvalue.key);
            for (var i = 0; i < selectOption.selectNode.options.length; i++) {
                if (selectOption.selectNode.options[i].value === newvalue.value) {
                    selectOption.selectNode.options[i].selected = true;
                }
            }
        },

        onBeforePosition: function (listPos, selectPos, windowPos, listNode, selectNode) {
            var selectOption = this;
            var relativeSelectPos = dojo.position(selectNode, false);

            if ((relativeSelectPos.y + selectPos.h + listPos.h) > windowPos.h && (listPos.h < relativeSelectPos.y)) {
                selectOption.floatWhere = tui.widget.mixins.FloatPosition.TOP_LEFT;
                return;
            }
            selectOption.floatWhere = tui.widget.mixins.FloatPosition.BOTTOM_LEFT;
        },

        appendOption: function (label, value, index) {
            var selectOption = this;
            var option = dojo.create("option", {text: label, value: value});

            try {
                selectOption.selectNode.add(option, selectOption.selectNode.options[index]);
            } catch (e) {
                selectOption.selectNode.add(option, index);
            }

            selectOption.listData.splice(index, 0, {
                text: label,
                value: value
            });

            selectOption.renderList();
        },

        removeOption: function (index) {
            var selectOption = this;
            selectOption.selectNode.remove(index);

            selectOption.listData.splice(index, 1);
            selectOption.renderList();
        },

        onSelectOptionsClick: function (event, selectOptions) {}
    });

    return tui.widget.form.SelectOption;
});
