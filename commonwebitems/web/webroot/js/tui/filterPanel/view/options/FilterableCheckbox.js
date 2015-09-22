define("tui/filterPanel/view/options/FilterableCheckbox", [
    "dojo",
    "dojo/dom-class",
    "dojo/_base/connect",
    "tui/utils/ObjectUtils",
    "tui/widget/form/SimpleCheckboxGroup"
], function (dojo, domClass, connect, utils) {

    dojo.declare("tui.filterPanel.view.options.FilterableCheckbox", [tui.widget.form.SimpleCheckboxGroup], {

        filterId:null,

        checkBoxId:null,

        onChange: function (checkbox, parent, children) {
            var widget = this;
            widget.getParent().optionSelected(widget.filterId, parent, children);
            connect.publish("tui/filterPanel/view/FilterController/applyFilter", [{
                id : widget.id
            }]);
        },


        postCreate: function () {
            var widget = this;
            widget.inherited(arguments);
        }
    });

    return tui.filterPanel.view.options.FilterableCheckbox;
});
