define("tui/filterIGPanel/view/OptionsFilter",
    ["dojo",
        "dojo/text!tui/filterIGPanel/view/templates/checkBoxFilters.html",
        "dojo/dom-construct",
        "dojo/_base/connect",
        'tui/widget/TagMappingTable',
        "tui/filterPanel/view/options/CheckBox",
        "dojox/dtl",
        "dojox/dtl/Context",
        "dojox/dtl/tag/logic",
        "tui/widget/expand/Expandable"], function (dojo, tmpl, domConstruct, connect, tagMappingTable) {


        dojo.declare("tui.filterIGPanel.view.OptionsFilter", [tui.widget._TuiBaseWidget], {

            dataPath: null,

            checkBoxes: null,

            checkBoxGroups: null,

            filterMap: null,

            tagMappingTable: tagMappingTable,

            buildFilterMap: function (data) {
                var filterMap = {};
               /* _.each(data.filters, function (filter) {*/
                    _.each(data.values, function (parent) {
                        filterMap[parent.id] = parent;
                        _.each(parent.children, function (child) {
                            filterMap[child.id] = child;
                        });
                    });
               /* });*/
                return filterMap;
            },

            generateRequest: function () {
                var widget = this;
                var request = {};
                _.each(widget.checkBoxes, function (checkBox) {
                    request[checkBox['filterId']] = request[checkBox['filterId']] || [];
                    if (checkBox.isSelected() && !checkBox.isParent()) {
                        request[checkBox['filterId']].push({
                            'name': checkBox.name,
                            'value': checkBox.getValue(),
                            'parent': checkBox.parentId || null, 'categoryCode': checkBox.categoryCode || null
                        });
                    }
                });
                var filteredRequest = {};
                _.each(request, function (value, key) {
                    if (!_.isEmpty(value)) {
                        filteredRequest[key] = value;
                    }
                });
                return filteredRequest;
            },

            postCreate: function () {
                var widget = this;
                var data = widget.getParent().registerFilter(widget);
                widget.checkBoxes = [];
                widget.checkBoxGroups = {};
                widget.draw(data);
                widget.inherited(arguments);
            },

            draw: function (data) {
                var widget = this, wrap = dojo.query(".item-content-wrap", widget.domNode),
                placeNode = wrap.length ? wrap[0] : widget.domNode;

                widget.inherited(arguments);
                if (data) {
                    widget.filterMap = widget.buildFilterMap(data);
                    //TODO
                    var filters = [];
                    filters.push(data);
                    var data = {filters: filters};
                    var template = new dojox.dtl.Template(tmpl);
                    var context = new dojox.dtl.Context(data);
                    var html = template.render(context);
                    domConstruct.place(html, placeNode, "only");
                    dojo.parser.parse(widget.domNode);
                }
            },

            destroyWidgets: function () {
                var widget = this;
                _.each(dijit.findWidgets(widget.domNode), function (w) {
                    w.destroyRecursive(true);
                });
            },

            refresh: function (field, oldValue, newValue, data) {
                var widget = this, filterMap, optionClicked;
                if (data ) { //&& data.filters
                    filterMap = widget.buildFilterMap(data);
                    if (_.has(newValue, "checkId")) {
                        // if newValue has checkId > option filter fired the request, choose which controllers to send repaint call to
                        // (every one except that which fired the request)
                        _.each(widget.checkBoxGroups, function (checkBoxes) {
                            // is checkbox inside this group?
                            optionClicked = _.filter(checkBoxes, function (checkBox) {
                                return checkBox.id === newValue.checkId
                            });
                            var anySelected = _.any(checkBoxes, function (checkBox) {
                                return checkBox.isSelected();
                            });
                            // not in the group so refresh
                            _.each(checkBoxes, function (checkBox) {
                                checkBox.refresh(filterMap[checkBox.id], anySelected, !optionClicked.length)
                            });
                        });
                    } else {
                        // repaint all option filters
                        _.each(widget.checkBoxGroups, function (checkBoxes) {
                            var anySelected = _.any(checkBoxes, function (checkBox) {
                                return checkBox.isSelected();
                            });
                            _.each(checkBoxes, function (checkBox) {
                                checkBox.refresh(filterMap[checkBox.id], anySelected, true)
                            })
                        })
                    }
                }
            },

            refreshCheckboxes: function (data) {
                var widget = this;
                if (data) { // && data.filters
                    var filterMap = widget.buildFilterMap(data);
                    /*_.each(widget.checkBoxes, function (checkBox) {
                     checkBox.refresh(filterMap[checkBox.id])
                     //filterMap[checkBox.id] ? checkBox.refresh(filterMap[checkBox.id], 'enable') : checkBox.refresh(filterMap[checkBox.id], 'disable');
                     //filterMap[checkBox.id] ? checkBox.enable() : checkBox.disable();
                     })*/
                    _.each(widget.checkBoxGroups, function (checkBoxes, groupId) {
                        var anySelected = _.any(checkBoxes, function (checkBox) {
                            return checkBox.isSelected();
                        });
                        _.each(checkBoxes, function (checkBox) {
                            checkBox.refresh(filterMap[checkBox.id], anySelected)
                        })
                    })
                }
            },

            handleNoResults: function (field, oldValue) {
                var widget = this;
                var changedCheckBox = _.filter(widget.checkBoxes, function (checkBox) {
                    return checkBox.id === oldValue.checkId;
                });
                if (!_.isEmpty(changedCheckBox)) {
                    oldValue['value'] ? changedCheckBox[0].select() : changedCheckBox[0].deselect();
                }
            },

            reset: function (data) {
                var widget = this;
                widget.clear(data);
            },

            clear: function (data) {
                var widget = this;
                if (data) { // && data.filters
                    widget.checkBoxes = [];
                    widget.destroyWidgets();
                    widget.draw(data);
                }
            },

            selectionUpdated: function (id, oldValue, newValue) {
                var filter = this;
                connect.publish("tui/filterIGPanel/controller/FilterController/applyFilter",
                    {'oldValue': {'id': filter.id, 'checkId': id, 'value': oldValue},
                        'newValue': {'id': filter.id, 'checkId': id, 'value': newValue}});
            },

            analyticsData: function () {
                var widget = this;
                var selections = {};
                var values = [];

                function nonEmptySelections() {
                    var nonEmptySelections = {}
                    _.each(selections, function (selection, key) {
                        if (!_.isEmpty(selection)) {
                            nonEmptySelections[key] = selection;
                        }
                    })
                    return nonEmptySelections
                }

                _.each(widget.checkBoxes, function (checkBox) {
                    selections[checkBox['filterId']] = selections[checkBox['filterId']] || []
                    if (_.isEmpty(checkBox.children) && checkBox.isSelected()) {
                        selections[checkBox['filterId']].push(checkBox.getValue());
                    }
                });
                _.each(nonEmptySelections(), function (selection, key) {
                    values.push(key + '=' + selection.join('|'));
                });
                return values.join('&');
            },

            addCheckBox: function (checkBox) {
                var widget = this;
                widget.checkBoxes.push(checkBox);
                if (widget.checkBoxGroups[checkBox.filterId]) {
                    widget.checkBoxGroups[checkBox.filterId].push(checkBox)
                } else {
                    widget.checkBoxGroups[checkBox.filterId] = [checkBox]
                }
                return widget.filterMap[checkBox.id];
            }
        })
        return tui.filterIGPanel.view.OptionsFilter;
    })