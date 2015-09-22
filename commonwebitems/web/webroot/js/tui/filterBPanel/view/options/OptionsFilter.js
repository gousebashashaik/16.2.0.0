define("tui/filterBPanel/view/options/OptionsFilter", [
  "dojo",
  "dojo/_base/declare",
  "dojo/topic",
  "dojo/text!tui/filterBPanel/view/templates/checkBoxFilters.html",
  "dojo/dom-construct",
  "dojo/dom-class",
  "dojo/_base/lang",
  'tui/widget/TagMappingTable',
  "tui/config/TuiConfig",
  "tui/filterBPanel/view/options/CheckBox",
  "dojox/dtl",
  "dojox/dtl/filter/integers",
  "dojox/dtl/Context",
  "dojox/dtl/tag/logic",
  "tui/widget/expand/Expandable"], function (dojo, declare, topic, tmpl, domConstruct, domClass, lang, tagMappingTable, tuiConfig) {

  return declare("tui.filterBPanel.view.options.OptionsFilter", [tui.widget._TuiBaseWidget], {

    dataPath: null,

    checkBoxes: null,

    checkBoxGroups: null,

    filterMap: null,

    tmpl : tmpl,

    tagMappingTable: tagMappingTable,

    isDefault: true,

    isHidden: false,

    tuiConfig : tuiConfig,

    buildFilterMap: function (data) {
      var widget = this,
      	  labelValue = "",
      	  filterMap = {};
    //checking for group of filters or single filter data
      if(data.filters){
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
          }
      else{
        _.each(data.values, function (parent) {
            parent.labelName = parent.name;
            if(widget.tuiConfig[dojoConfig.site] && dojoConfig.dualBrandSwitch){
	    	  labelValue = widget.tuiConfig[dojoConfig.site].dualBrandConfig.optionLabel[parent.name.toLowerCase().replace(/\s/g,"")];
	    	  if(labelValue){
	    		parent.labelName = labelValue;
	    	  }
            }
          filterMap[parent.id] = parent;
          _.each(parent.children, function (child) {
        	  child.labelName = child.name;
        	  if(widget.tuiConfig[dojoConfig.site] && dojoConfig.dualBrandSwitch){
	        	  labelValue = widget.tuiConfig[dojoConfig.site].dualBrandConfig.optionLabel[child.name.toLowerCase().replace(/\s/g,"")];
	        	  if(labelValue){
	        		  child.labelName = labelValue;
	        	  }
        	  }
            filterMap[child.id] = child;
          });
        });
      }
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
	      widget.NoOfItems = 10;
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
      if (data) {
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
      if (data) {
        widget.checkBoxes = [];
        widget.destroyWidgets();
        widget.draw(data);
      }
      widget.isDefault = true;
      topic.publish("tui/filterBPanel/view/FilterController/setFilterSelection", widget.domNode.id);
  	  topic.publish("tui/filterBPanel/view/filterController/clearAllFilterVisibility");
    },

    selectionUpdated: function (id, oldValue, newValue) {
      var filter = this;
      topic.publish("tui/filterBPanel/view/FilterController/applyFilter", {
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
    },

    updateState: function (checkBoxState){
    	var widget = this;
    	widget.isDefault = !checkBoxState;
    	topic.publish("tui/filterBPanel/view/FilterController/setFilterSelection", widget.domNode.id);
    	topic.publish("tui/filterBPanel/view/filterController/clearAllFilterVisibility");
    }

  });
});