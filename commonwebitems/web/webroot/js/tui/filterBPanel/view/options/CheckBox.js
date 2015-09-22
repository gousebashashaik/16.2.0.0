define("tui/filterBPanel/view/options/CheckBox", [
  "dojo/_base/declare",
  "dojo/_base/lang",
  "dojo/topic",
  "dojo/on",
  "dojo/query",
  "dojo/dom-attr",
  "dojo/dom-class",
  "dijit/registry",
  "dojo/html",
  "dojo/NodeList-dom",
  "tui/widget/_TuiBaseWidget"
], function (declare, lang, topic, on, query, domAttr, domClass, registry, html) {

  return declare("tui.filterBPanel.view.options.CheckBox", [tui.widget._TuiBaseWidget], {

    itemSelector: ".item-container",

    parentId: null,

    children: null,

    domNode: null,

    model: null,

    label: null,

    parent: null,

    inputDom: null,

    selected: false,

    disabled: false,

    changed: false,

    countTmpl: "({count})",

    postCreate: function () {
      var checkBox = this;
      checkBox.inputDom = query('input', checkBox.domNode)[0];
      checkBox.spanDom = query('span', checkBox.domNode)[0];
      checkBox.label = query('label', checkBox.domNode)[0];
      checkBox.count = checkBox.label ? query('.package-count', checkBox.label)[0] : null;
      checkBox.children = [];
      checkBox.addEventsListeners();
      checkBox.inherited(arguments);
      //register with options filter && get the model
      checkBox.model = checkBox.getParent().addCheckBox(checkBox);
      checkBox.parentId ? (checkBox.parent = registry.byId(checkBox.parentId)).addChild(checkBox) : null;

      topic.subscribe("tui:channel=updateCheckboxState/"+checkBox.id, function(model){
        checkBox.update(model);
      });
    },

    update: function(model) {
      var checkBox = this;
        model.selected ? checkBox.select() : checkBox.deselect();
        model.disabled ? checkBox.disable() : checkBox.enable();
        model.countShowing ? checkBox.showLabelCounts() : checkBox.hideLabelCounts();
        model.noAccommodations ? checkBox.updateCountModel(model) : null;
    },

    updateCount: function () {
      var checkBox = this;
      if (checkBox.count) {
      var replaceValue = lang.replace(checkBox.countTmpl, {
          count: checkBox.model.noAccommodations
        });
      	html.set(checkBox.count,replaceValue);
      }
    },

    addEventsListeners: function () {
      var checkBox = this;
      var container = checkBox.getParent();
      on(checkBox.inputDom, 'click', function () {
    	  var operation = "";
        domAttr.get(checkBox.inputDom, 'checked') ? checkBox.select() : checkBox.deselect();
        operation = domAttr.get(checkBox.inputDom, 'checked') ? "add" : "remove";
        switch(domAttr.get(checkBox.inputDom,"value").toLowerCase()){
	    	case 'couples': _.every(checkBox.getParent().checkBoxes, function(checkBoxCollection){
	    						if(checkBoxCollection.inputDom.id === "field_CL_SMR"){
	    							domAttr.get(checkBox.inputDom, 'checked') ? checkBoxCollection.select() : checkBoxCollection.deselect();
	    							return false;
	    						}
	    						return true;
	    					});
	    					break;
	    	case 'sensimar': _.every(checkBox.getParent().checkBoxes, function(checkBoxCollection){
								if(checkBoxCollection.inputDom.id === "field_CL_COU"){
									domAttr.get(checkBox.inputDom, 'checked') ? checkBoxCollection.select() : checkBoxCollection.deselect();
									return false;
								}
								return true;
							});
							break;
	    	case 'cou': _.every(checkBox.getParent().checkBoxes, function(checkBoxDestination){
							if(checkBoxDestination.inputDom.id === "field_DO_SMR"){
								domAttr.get(checkBox.inputDom, 'checked') ? checkBoxDestination.select() : checkBoxDestination.deselect();
								_.each(checkBoxDestination.children, function(checkBoxChild){
									domAttr.get(checkBox.inputDom, 'checked') ? checkBoxChild.select() : checkBoxChild.deselect();
								});
								return false;
							}
							return true;
						});
						break;
	    	case 'smr': _.every(checkBox.getParent().checkBoxes, function(checkBoxDestination){
						if(checkBoxDestination.inputDom.id === "field_DO_COU"){
							domAttr.get(checkBox.inputDom, 'checked') ? checkBoxDestination.select() : checkBoxDestination.deselect();
							_.each(checkBoxDestination.children, function(checkBoxChild){
								domAttr.get(checkBox.inputDom, 'checked') ? checkBoxChild.select() : checkBoxChild.deselect();
							});
							return false;
							}
						return true;
						});
					break;
        }
        checkBox.parent ? checkBox.parent.updateSelection() : null;
        container.selectionUpdated(checkBox.id, !domAttr.get(checkBox.inputDom, 'checked'), domAttr.get(checkBox.inputDom, 'checked'));
      });
    },

    addChild: function (child) {
      this.children.push(child);
    },

    deselect: function () {
      var checkBox = this,
      //itemEle = query(checkBox.inputDom).parents(checkBox.itemSelector)[0],
      itemContentEle = query(checkBox.inputDom).parents('.item-content-wrap')[0],
      spanEle = query('span.checkbox', itemContentEle),
      //parentEle = query(itemEle).parents('.horizontal-filter-panel')[0],
      //clearEle = query(".clear-shortlist", parentEle)[0],
      checkBoxSelected = false;
      domAttr.set(checkBox.inputDom, 'checked', false);
      domClass.remove(checkBox.spanDom, 'selected');
      checkBox.model.selected = false;
      _.each(checkBox.children, function (child) {
        child.isEnabled() ? child.deselect() : null;
      });
      _.each(spanEle, function (span) {
        selectedSpanEle = query('span.selected', span)[0];
        if(selectedSpanEle != undefined) {
            checkBoxSelected = true;
        }
      });
      if(checkBoxSelected) {
        checkBox.addSelect();
      }
      else {
        checkBox.removeSelect();
      }
    },

    addSelect: function () {
      var checkBox = this;
      checkBox.updateState(true);
    },

    removeSelect: function () {
		var checkBox = this;
		checkBox.updateState(false);
    },

    select: function () {
      var checkBox = this;
      /*parentEle = query(itemEle).parents('.horizontal-filter-panel')[0];
      clearEle = query(".clear-shortlist", parentEle)[0];*/
      domAttr.set(checkBox.inputDom, 'checked', true);
       domClass.add(checkBox.spanDom, 'selected');
      checkBox.addSelect();
      checkBox.model.selected = true;
      _.each(checkBox.children, function (child) {
        child.isEnabled() ? child.select() : null;
      });
    },

    isSelected: function () {
      var checkBox = this;
      return checkBox.isEnabled() && domAttr.get(checkBox.inputDom, 'checked');
    },

    isCountShowing: function () {
      var checkBox = this;
      return checkBox.count ? true : false;
    },

    getLabelCounts: function () {
      var checkBox = this;
      var countSub = 0;
      if(checkBox.count) {
          var countLength = checkBox.count.innerHTML.length;
          countSub = checkBox.count.innerHTML.substring(1,countLength-1);
          return parseInt(countSub, 10);
      }
      else return 0;
    },

    hideLabelCounts: function () {
      var checkBox = this;
      checkBox.count ? domClass.add(checkBox.count, "hide") : null;
    },

    showLabelCounts: function () {
      var checkBox = this;
      checkBox.count ? domClass.remove(checkBox.count, "hide") : null;
    },

    updateCountModel: function (model) {
      var checkBox = this;
      checkBox.model.noAccommodations = model.noAccommodations || checkBox.model.noAccommodations;
      checkBox.updateCount();
    },

    resetLabel: function () {
      var checkBox = this;
      checkBox.label ? checkBox.label.innerHTML = checkBox.model.name : null;
    },

    disable: function () {
      var checkBox = this;
      domAttr.set(checkBox.inputDom, 'disabled', 'true');
      domClass.add(checkBox.spanDom, 'disabled');
      checkBox.label ? domClass.add(checkBox.label, "disabled") : null;
      if (!_.isEmpty(checkBox.children)) {
        _.each(checkBox.children, function (child) {
          child.disable();
        });
      }
    },

    isEnabled: function () {
      return domAttr.get(this.inputDom, 'disabled') ? false : true;
    },

    enable: function () {
      var checkBox = this;
      checkBox.label ? domClass.remove(checkBox.label, "disabled") : null;
      domAttr.remove(checkBox.inputDom, 'disabled');
      domClass.remove(checkBox.spanDom, 'disabled');
    },

    refresh: function (model, groupSelected, mutate) {
      var checkBox = this;

      //show the the clear all filters option when any of the check box group is selected
      //This code is commented due to ClearAll Filters link is coming when we click on dates on dateslider
//      if(groupSelected) domClass.remove(query(".horizontal-filter-panel .clear-shortlist .shortlisted-h a")[0], 'hide');

      if (checkBox.filterType === 'OR') {
        !checkBox.isSelected() && groupSelected ? checkBox.hideLabelCounts() : null;
        !groupSelected && model ? [checkBox.updateCountModel(model), checkBox.enable(), checkBox.showLabelCounts()] : null;
        checkBox.isSelected() ? checkBox.showLabelCounts() : null;
        /*if(checkBox.isSelected() || groupSelected) {
          if(query(checkBox.inputDom).parents(checkBox.itemSelector)[0] != undefined) {
            var itemEle = query(checkBox.inputDom).parents(checkBox.itemSelector)[0],
            parentEle = query(itemEle).parents('.horizontal-filter-panel')[0],
            clearEle = query(".clear-shortlist", parentEle)[0];
              checkBox.addSelect(clearEle);
          }
        }*/
        if (mutate) {
          model ? [checkBox.updateCountModel(model), checkBox.enable(), checkBox.showLabelCounts()] : !checkBox.isSelected() ? checkBox.disable() : null;
        }
      } else {
        model ? [checkBox.updateCountModel(model), checkBox.enable()] : checkBox.disable();
      }
    },

    updateSelection: function () {
      var checkBox = this;
      var enabledChildren = _.filter(checkBox.children, function (child) {
        return child.isEnabled();
      });
      var allChildrenSelected = _.every(enabledChildren, function (child) {
        return child.isSelected();
      });

      allChildrenSelected ? domAttr.set(checkBox.inputDom, 'checked', true) :
          domAttr.set(checkBox.inputDom, 'checked', false);
      allChildrenSelected ? dojo.addClass(checkBox.spanDom, "selected") :
    	  dojo.removeClass(checkBox.spanDom, "selected");

      var noneSelected = _.every(enabledChildren, function (child) {
        return !child.isSelected();
      });

      noneSelected ? domAttr.set(checkBox.inputDom, 'checked', false) : null;
    },

    isParent: function () {
      return !_.isEmpty(this.children);
    },

    updateState: function(checkBoxState){
    	var checkBox = this;
    	checkBox.getParent().updateState(checkBoxState);
    },

    getValue: function () {
      var checkBox = this;
      return checkBox.inputDom.value || '';
    }

  });
});
