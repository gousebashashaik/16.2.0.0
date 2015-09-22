define("tui/filterPanel/view/options/CheckBox", [
  "dojo/_base/declare",
  "dojo/_base/lang",
  "dojo/topic",
  "dojo/on",
  "dojo/query",
  "dojo/dom-attr",
  "dojo/dom-class",
  "dijit/registry",
  "dojo/NodeList-dom",
  "tui/widget/_TuiBaseWidget"
], function (declare, lang, topic, on, query, domAttr, domClass, registry) {

  return declare("tui.filterPanel.view.options.CheckBox", [tui.widget._TuiBaseWidget], {

    parentId: null,

    children: null,

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
      checkBox.label = query('label', checkBox.domNode)[0];
      checkBox.count = checkBox.label ? query('.package-count', checkBox.label)[0] : null;
      checkBox.children = [];
      checkBox.addEventsListeners();
      checkBox.inherited(arguments);
      //register with options filter && get the model
      checkBox.model = checkBox.getParent().addCheckBox(checkBox);
      checkBox.parentId ? (checkBox.parent = registry.byId(checkBox.parentId)).addChild(checkBox) : null;

      topic.subscribe("tui:channel=updateCheckboxState", function(model){
        checkBox.update(model);
      });
    },

    update: function(model) {
      var checkBox = this;
      if(checkBox.id === model.id){
        model.selected ? checkBox.select() : checkBox.deselect();
        model.disabled ? checkBox.disable() : checkBox.enable();
        model.countShowing ? checkBox.showLabelCounts() : checkBox.hideLabelCounts();
        model.noAccommodations ? checkBox.updateCountModel(model) : null;
      }
    },

    updateCount: function () {
      var checkBox = this;
      if (checkBox.count) {
        checkBox.count.innerHTML = lang.replace(checkBox.countTmpl, {
          count: checkBox.model.noAccommodations
        });
      }
    },

    addEventsListeners: function () {
      var checkBox = this;
      var container = checkBox.getParent();
      on(checkBox.inputDom, 'click', function () {
        domAttr.get(checkBox.inputDom, 'checked') ? checkBox.select() : checkBox.deselect();
        switch(domAttr.get(checkBox.inputDom,"value").toLowerCase()){
        	case 'couples': if(_.first(query("#check_CL_SMR input",checkBox.getParent().domNode))){
        						domAttr.set(_.first(query("#check_CL_SMR input",checkBox.getParent().domNode)), "checked", domAttr.get(checkBox.inputDom, 'checked'));
        					}
        					break;
        	case 'sensimar': if(_.first(query("#check_CL_COU input",checkBox.getParent().domNode))){ 
        						domAttr.set(_.first(query("#check_CL_COU input",checkBox.getParent().domNode)), "checked", domAttr.get(checkBox.inputDom, 'checked'));
        					}
        					break;
        	case 'cou': if(_.first(query("#check_DO_SMR input",checkBox.getParent().domNode))){ 
        				_.each(query("#check_DO_SMR input",checkBox.getParent().domNode), function(checkBoxSensimar){
        					domAttr.set(checkBoxSensimar, "checked", domAttr.get(checkBox.inputDom, 'checked'));
        				});
        				}
							break;
        	case 'smr': if(_.first(query("#check_DO_COU input",checkBox.getParent().domNode))){ 
						_.each(query("#check_DO_COU input",checkBox.getParent().domNode), function(checkBoxCouple){
							domAttr.set(checkBoxCouple, "checked", domAttr.get(checkBox.inputDom, 'checked'));
						});
					 }
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
      var checkBox = this;
      domAttr.set(checkBox.inputDom, 'checked', false);
      checkBox.model.selected = false;
      _.each(checkBox.children, function (child) {
        child.isEnabled() ? child.deselect() : null;
      });
    },

    select: function () {
      var checkBox = this;
      domAttr.set(checkBox.inputDom, 'checked', true);
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
    },

    refresh: function (model, groupSelected, mutate) {
      var checkBox = this;

      if (checkBox.filterType === 'OR') {
        !checkBox.isSelected() && groupSelected ? checkBox.hideLabelCounts() : null;
        !groupSelected && model ? [checkBox.updateCountModel(model), checkBox.enable(), checkBox.showLabelCounts()] : null;
        checkBox.isSelected() ? checkBox.showLabelCounts() : null;
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

      var noneSelected = _.every(enabledChildren, function (child) {
        return !child.isSelected();
      });

      noneSelected ? domAttr.set(checkBox.inputDom, 'checked', false) : null;
    },

    isParent: function () {
      return !_.isEmpty(this.children);
    },

    getValue: function () {
      var checkBox = this;
      return checkBox.inputDom.value || '';
    }

  });
});
