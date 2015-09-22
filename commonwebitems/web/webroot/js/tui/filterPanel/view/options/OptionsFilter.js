define("tui/filterPanel/view/options/OptionsFilter", [
  "dojo",
  "dojo/_base/declare",
  "dojo/topic",
  "dojo/text!tui/filterPanel/view/templates/checkBoxFilters.html",
  "dojo/dom-construct",
  "dojo/dom-class",
  'tui/widget/TagMappingTable',
  "tui/config/TuiConfig",
  "tui/filterPanel/view/options/CheckBox",
  "dojox/dtl",
  "dojox/dtl/Context",
  "dojox/dtl/tag/logic",
  "tui/widget/expand/Expandable"], function (dojo, declare, topic, tmpl, domConstruct, domClass, tagMappingTable, tuiConfig) {

  return declare("tui.filterPanel.view.options.OptionsFilter", [tui.widget._TuiBaseWidget], {

    dataPath: null,

    checkBoxes: null,

    checkBoxGroups: null,

    filterMap: null,

    tmpl : tmpl,

    tagMappingTable: tagMappingTable,

	tuiConfig : tuiConfig,

    buildFilterMap: function (data) {
      var widget = this,
	      filterMap = {},
		  labelValue = "";
      _.each(data.filters, function (filter) {
        _.each(filter.values, function (parent) {
		   parent.labelName = parent.name;
              if(widget.tuiConfig[dojoConfig.site] && dojoConfig.dualBrandSwitch){
	          	  labelValue = widget.tuiConfig[dojoConfig.site].dualBrandConfig.optionLabel[parent.name.toLowerCase().replace(/\s/g,"")];
	          	  if(labelValue){
	          		parent.labelName = labelValue.toUpperCase();
	          	  }
           }
          filterMap[parent.id] = parent;
          _.each(parent.children, function (child) {
				child.labelName = child.name;
            	  if(widget.tuiConfig[dojoConfig.site] && dojoConfig.dualBrandSwitch){
	            	  labelValue = widget.tuiConfig[dojoConfig.site].dualBrandConfig.optionLabel[child.name.toLowerCase().replace(/\s/g,"")];
	            	  if(labelValue){
	            		  child.labelName = labelValue.toUpperCase();
	            	 }
            	 }
            filterMap[child.id] = child;
          });
        });
      });
      return filterMap;
    },

    generateRequest: function () {
      var widget = this;
      var request = {};
      _.each(widget.checkBoxes, function (checkBox) {
        request[checkBox['filterId']] = request[checkBox['filterId']] || [];
        if (/*checkBox.isSelected() &&*/ !checkBox.isParent()) {
          request[checkBox['filterId']].push({
            'name': checkBox.name,
            'value': checkBox.getValue(),
            'parent': checkBox.parentId || null,
            'categoryCode': checkBox.categoryCode || null,
            'disabled': !checkBox.isEnabled(),
            'selected': checkBox.isSelected(),
            'countShowing': checkBox.isCountShowing(),
            'noAccommodations': checkBox.getLabelCounts(),
            'id': checkBox.id
          });
        }
      });
      /*var filteredRequest = {};
      _.each(request, function (value, key) {
        if (!_.isEmpty(value)) {
          filteredRequest[key] = value;
        }
      });
      return filteredRequest;*/
      return request;
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
        var template = new dojox.dtl.Template(widget.tmpl);
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

    refresh: function (field, oldValue, newValue, data, isCached) {
      var widget = this, filterMap, optionClicked;
      //if(isCached) return;
      if (data && data.filters) {
        filterMap = widget.buildFilterMap(data);
        if (_.has(newValue, "checkId")) {
          // if newValue has checkId > option filter fired the request, choose which controllers to send repaint call to
          // (every one except that which fired the request)
          _.each(widget.checkBoxGroups, function (checkBoxes) {
            // is checkbox inside this group?
            optionClicked = _.filter(checkBoxes, function (checkBox) {
              return checkBox.id === newValue.checkId;
            });
            var anySelected = _.any(checkBoxes, function (checkBox) {
              return checkBox.isSelected();
            });
            // not in the group so refresh
            _.each(checkBoxes, function (checkBox) {
              checkBox.refresh(filterMap[checkBox.id], anySelected, !optionClicked.length);
            });
          });
        } else {
          // repaint all option filters
          _.each(widget.checkBoxGroups, function (checkBoxes) {
            var anySelected = _.any(checkBoxes, function (checkBox) {
              return checkBox.isSelected();
            });
            _.each(checkBoxes, function (checkBox) {
              checkBox.refresh(filterMap[checkBox.id], anySelected, true);
            });
          });
        }
      }
    },

    refreshCheckboxes: function (data) {
      var widget = this;
      if (data && data.filters) {
        var filterMap = widget.buildFilterMap(data);
        _.each(widget.checkBoxGroups, function (checkBoxes, groupId) {
          var anySelected = _.any(checkBoxes, function (checkBox) {
            return checkBox.isSelected();
          });
          _.each(checkBoxes, function (checkBox) {
            checkBox.refresh(filterMap[checkBox.id], anySelected);
          });
        });
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
      if (data && data.filters) {
        widget.checkBoxes = [];
        widget.destroyWidgets();
        widget.draw(data);
      }
    },

    selectionUpdated: function (id, oldValue, newValue) {
      var filter = this;
      topic.publish("tui/filterPanel/view/FilterController/applyFilter", {
        'oldValue': {'id': filter.id, 'checkId': id, 'value': oldValue},
        'newValue': {'id': filter.id, 'checkId': id, 'value': newValue}
      });
    },

    addCheckBox: function (checkBox) {
      var widget = this;
      widget.checkBoxes.push(checkBox);
      if (widget.checkBoxGroups[checkBox.filterId]) {
        widget.checkBoxGroups[checkBox.filterId].push(checkBox);
      } else {
        widget.checkBoxGroups[checkBox.filterId] = [checkBox];
      }
      return widget.filterMap[checkBox.id];
    }
  });
});