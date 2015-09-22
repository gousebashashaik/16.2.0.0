define("tui/filterPanel/view/options/CruiseOptionsFilter", [
  "dojo",
  "dojo/_base/declare",
  "dojo/topic",
  "dojo/text!tui/filterPanel/view/templates/cruiseCheckBoxFilters.html",
  "dojo/dom-construct",
  "dojo/dom-class",
  "tui/widget/TagMappingTable",
  "dojo/dom-attr",
  "tui/filterPanel/view/options/CruiseCheckBox",
  "dojox/dtl",
  "dojox/dtl/Context",
  "dojox/dtl/tag/logic",
  "tui/widget/expand/Expandable",
  "tui/search/nls/Searchi18nable"], function (dojo, declare, topic, tmpl, domConstruct, domClass, tagMappingTable, domAttr) {

  return declare("tui.filterPanel.view.options.CruiseOptionsFilter", [tui.widget._TuiBaseWidget, tui.search.nls.Searchi18nable], {

    dataPath: null,

    checkBoxes: null,

    checkBoxGroups: null,

    filterMap: null,

    tmpl : tmpl,

    tagMappingTable: tagMappingTable,

    buildFilterMap: function (data) {
      var filterMap = {};
      _.each(data.filters, function (filter) {
        _.each(filter.values, function (parent) {
          filterMap[parent.id] = parent;
          _.each(parent.children, function (child) {
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
            'noItineraries': checkBox.getLabelCounts(),
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

      // initialise internationalisation
      widget.initSearchMessaging();

      var data = widget.getParent().registerFilter(widget);
      if( data ){

	      widget.checkBoxes = [];
	      widget.checkBoxGroups = {};
	      widget.draw(data);
	      widget.inherited(arguments);
      }
    },

    draw: function (data) {
      var widget = this, wrap = dojo.query(".item-content-wrap", widget.domNode),
          placeNode = wrap.length ? wrap[0] : widget.domNode;

      widget.inherited(arguments);
      if (data) {

		//All inclusive tooltip is upadated as no content exist
        if(data.name === 'ALL INCLUSIVE'){
           _.isEmpty(data.filters[0].values) ? null :
   		   data.filters[0].values[0].tooltip = widget.searchMessaging.searchResults.toolTip.allInclusive;
		}

        widget.filterMap = widget.buildFilterMap(data);
        var template = new dojox.dtl.Template(widget.tmpl);
        var context = new dojox.dtl.Context(data);
        var html = template.render(context);
        domConstruct.place(  html, placeNode, "only");
        if( !data.filters[0].values.length ){
        	dojo.query(widget.domNode).parent().addClass("hide");
        }else{
        	dojo.parser.parse(widget.domNode);
        	dojo.query(widget.domNode).parent().removeClass("hide");
        }
        var filterCntrlNode = widget.getParent().domNode;
    	if( dojo.query(".item.hide", widget.getParent().domNode).length === 3 ){
    		dojo.query(filterCntrlNode).siblings(".filter-head").addClass("hidden");
    		dojo.query(".clear-arrow", filterCntrlNode).addClass("hide");
    	}else{
    		 dojo.query(filterCntrlNode).siblings(".filter-head").removeClass("hidden");
    		dojo.query(".clear-arrow", filterCntrlNode).removeClass("hide");
    	}

        widget.defineNumber();
        _.each(dijit.findWidgets(widget.domNode), function (w) {
          w.tag = widget.tagMappingTable.table[widget.declaredClass];
          domAttr.set(w.domNode, 'analytics-id', w.tag);
          domAttr.set(w.domNode, 'analytics-instance', widget.number);
          domAttr.set(w.domNode, 'analytics-text', w.model.name + (w.filterId == 'cruiseStayDestinations' ? " Stay option" : "" ));
        });
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
      //in Cruise...the data sent back from backend is having empty values and not
      //empty data.filter
      if (data && data.filters && !_.isEmpty(data.filters[0].values)) {
        widget.checkBoxes = [];
        widget.destroyWidgets();
        widget.draw(data);
      }
    },

    clearDestroy: function () {
        var widget = this;
          widget.checkBoxes = [];
          widget.destroyWidgets();
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