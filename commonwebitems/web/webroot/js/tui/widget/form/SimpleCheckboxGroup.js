define("tui/widget/form/SimpleCheckboxGroup", [
    "dojo",
    "dojo/on",
    "tui/widget/_TuiBaseWidget",
    "dojo/query",
    "tui/filterPanel/mixin/FilterPanelItem",
    "dojo/NodeList-dom"
], function (dojo, on) {

    dojo.declare("tui.widget.form.SimpleCheckboxGroup", [tui.widget._TuiBaseWidget, tui.filterPanel.mixin.FilterPanelItem], {

        childId: null,

        // ----------------------------------------------------------------------------- properties

        parentSelector: "ul",

        selector: ".check",

        // ----------------------------------------------------------------------------- methods

        addSearchResultWatcher: function (filterPanelModel) {
            var filterPanelItem = this;
            filterPanelModel.watch('filterMap', function (name, oldValue, newValue) {
                var oldFilter = oldValue[filterPanelItem.filterId];
                var newFilter = newValue[filterPanelItem.filterId];
                filterPanelItem.onValueChange(name, oldFilter ? oldFilter[filterPanelItem.childId] : null, newFilter ? newFilter[filterPanelItem.childId] : null);
            })
        },


        postCreate: function () {
            var simpleCheckboxGroup = this;

            simpleCheckboxGroup.childrenCheckboxes = dojo.query(".child-check", simpleCheckboxGroup.domNode)
            simpleCheckboxGroup.inherited(arguments);
            simpleCheckboxGroup.addEventsListeners();
        },

        selectAll: function () {
            var checkBoxGroup = this;
            dojo.setAttr(dojo.query('.parent-check', checkBoxGroup.domNode)[0], 'checked', true);
            _.each(checkBoxGroup.childrenCheckboxes, function (checkbox) {
                dojo.setAttr(checkbox, 'checked', true);
            });
        },

        unselectAll: function () {
            var checkBoxGroup = this;
            dojo.setAttr(dojo.query('.parent-check', checkBoxGroup.domNode)[0], 'checked', false);
            _.each(checkBoxGroup.childrenCheckboxes, function (checkbox) {
                dojo.setAttr(checkbox, 'checked', false);
            });
        },

        addEventsListeners: function () {
            var simpleCheckboxGroup = this;
            on(simpleCheckboxGroup.domNode, on.selector(simpleCheckboxGroup.selector, "click"), function (event) {
                var checkbox = this;
                var answer = checkbox.checked;
                var checkboxes = simpleCheckboxGroup.childrenCheckboxes;
                var parentCheckbox = dojo.query(".parent-check", simpleCheckboxGroup.domNode);

                if (dojo.hasClass(checkbox, "parent-check")) {
                    checkboxes.attr("checked", answer);
                } else {
                    var filteredCheckboxes = _.filter(checkboxes, function (item) {
                        return item.checked === true;
                    });
                    answer = (checkboxes.length === filteredCheckboxes.length);
                    parentCheckbox.attr("checked", answer);
                }

                var childValues = [];
                _.each(checkboxes, function (checkbox) {
                    childValues.push({name: checkbox.value, selected: dojo.attr(checkbox, 'checked')});
                })

                simpleCheckboxGroup.onChange(checkbox, {name: simpleCheckboxGroup.id, selected: dojo.attr(checkbox, 'checked')}, childValues);
            });
        },

        isSelected: function () {
            var simpleCheckboxGroup = this;
            return dojo.attr(dojo.query(".parent-check", simpleCheckboxGroup.domNode)[0], 'checked') || false;
        },

        onChange: function (checkbox, parent, children) {
        }
    });

    return tui.widget.form.SimpleCheckboxGroup;
});
