define("tui/searchPanel/view/ChildSelectOption", [
  "dojo",
  "dojo/text!tui/searchPanel/view/templates/ChildAgeSelectOption.html",
  "dojo/text!tui/searchPanel/view/flights/templates/ChildAgeSelectOption.html",
  "tui/widget/mixins/Templatable",
  "tui/searchPanel/view/PartyCompSelectOption",
  "dojo/dom-attr",
  "tui/searchPanel/view/ChildAgesView"], function (dojo, childAgeSelectorTmpl, flightsChildAgeSelectorTmpl, Templatable, SelectOption,domAttr, ChildAgesView) {

  dojo.declare("tui.searchPanel.view.ChildSelectOption", [tui.searchPanel.view.PartyCompSelectOption, tui.widget.mixins.Templatable, tui.search.nls.Searchi18nable], {

    // ----------------------------------------------------------------------------- properties

    childAgesOptions: null,

    childAgesView: null,

    subscribableMethods: ["updateChildAgeValues"],

    // Number of child-age widgets to draw per row
    rowLength: 0,

    // ----------------------------------------------------------------------------- methods

    postCreate: function () {
      var childSelectOption = this;
      childSelectOption.rowLength = childSelectOption.widgetController.searchApi === 'searchPanel' ? 3 : 4;
      childSelectOption.initSearchMessaging();
      childSelectOption.childAgesOptions = [];
      childSelectOption.inherited(arguments);
      //dojo.subscribe("tui:channel=searchLocalStorageRetrieved", function(){
      childSelectOption.addChildDropdownEventlistener();
      //});
      childSelectOption.tagElement(childSelectOption.domNode, "children");
     if(dojoConfig.site === "flights"){
    	 domAttr.set(childSelectOption.domNode, "analytics-id", "COM_FO_SEARCH_PANEL");
    	 childSelectOption.domNode.children[0].setAttribute("analytics-id","COM_FO_SEARCH_PANEL");
     }
     
     
    },

    updateChildrenView: function (name, oldchildValue, newchildValue) {
      var childSelectOption = this;
      if (parseInt(childSelectOption.getSelectedData().value, 10) !== newchildValue) {
        childSelectOption.setSelectedValue(newchildValue);
      } else {
        var childAges = dojo.clone(childSelectOption.searchPanelModel.get("childAges"));
        var diff = 0;
        newchildValue = parseInt(newchildValue, 10);
        oldchildValue = parseInt(oldchildValue, 10);

        if (newchildValue > oldchildValue) {
          diff = newchildValue - oldchildValue;
          for (var i = 0; i < diff; i++) {
            childAges.push(-1);
          }
        } else if (newchildValue < oldchildValue) {
          diff = oldchildValue - newchildValue;
          childAges.splice(-diff, diff);
        } else {
          if(!childAges.length) {
            childAges = [];
            for (var ii = 0; ii < newchildValue; ii++) {
              childAges.push(-1);
            }
          } else {
            if(newchildValue === 0) {
              childAges = [];
            }
          }
        }
        // set model
        childSelectOption.searchPanelModel.set("childAges", childAges);
      }
    },

    addChildDropdownEventlistener: function () {
      var childSelectOption = this;
      childSelectOption.inherited(arguments);
      childSelectOption.connect(childSelectOption, "onChange", function (name, oldvalue, newvalue) {
        childSelectOption.searchPanelModel.set("children", parseInt(newvalue.value, 10));
      });
    },

    /*childSelectOption.searchPanelModel.watch("childAges", function (name, oldChildAges, newChildAges) {

     childSelectOption.addChildAgeSelector(newChildAges.length);
     });*/

    addChildAgeSelector: function (name, oldChildAges, newChildAges) {
      var childSelectOption = this;
      if (childSelectOption.childAgesOptions.length === newChildAges.length) {
        return;
      }
      var size = newChildAges.length;
      var childAges = dojo.clone(childSelectOption.searchPanelModel.get("childAges"));

      // remove old child age widgets
      if (childSelectOption.childAgesOptions.length > 0) {
        _.forEach(childSelectOption.childAgesOptions, function (item) {
          item.destroyRecursive();
        });
        childSelectOption.childAgesOptions.length = 0;
      }

      if (childSelectOption.childAgesView) {
        childSelectOption.childAgesView.watcher.unwatch();
        childSelectOption.childAgesView.destroyRecursive();
      }

      dojo.query(".child-age-label", childSelectOption.widgetController.domNode).remove();
      dojo.query(".child-age-selector", childSelectOption.widgetController.domNode).remove();

      if (size === 0) {
        return;
      }

      // render child ages template
      var childAgesHtml;
      if(dojoConfig.site == 'flights'){
    	  childAgesHtml = childSelectOption.renderTmpl(flightsChildAgeSelectorTmpl, childSelectOption);
      }else{
    	  childAgesHtml = childSelectOption.renderTmpl(childAgeSelectorTmpl, childSelectOption);
      }
      var node = dojo.query(".col.pax", childSelectOption.widgetController.domNode)[0];
      if(node != undefined){
    	  dojo.place(childAgesHtml, node, "last");
      } else {
    	  dojo.place(childAgesHtml, dojo.query(".rooms", childSelectOption.widgetController.domNode)[0], "last");
      }
      
      // create view for child ages error listener
      childSelectOption.childAgesView = new ChildAgesView({
        widgetController: childSelectOption.widgetController,
        searchPanelModel: childSelectOption.searchPanelModel
      }, dojo.query(".child-age-label", childSelectOption.widgetController.domNode)[0], "last");

      var select = null;
      _.forEach(dojo.query(".child-age-selector select", childSelectOption.widgetController.domNode), function (item, i) {
        select = new SelectOption({
          searchPanelModel: childSelectOption.searchPanelModel,
          widgetController: childSelectOption.widgetController
        }, item);

        select.setSelectedValue(childAges[i]);

        select.connect(select, "onChange", function (name, oldvalue, newvalue) {
          var childAges = dojo.clone(childSelectOption.searchPanelModel.get("childAges"));
          childAges[i] = parseInt(newvalue.value, 10);
          childSelectOption.searchPanelModel.set("childAges", childAges);
        });

        childSelectOption.tagElement(select.domNode, "child-age-" + (i + 1));

        childSelectOption.childAgesOptions.push(select);
      });
      if(childSelectOption.widgetController.searchApi === 'getPrice'){
    	  dojo.publish("tui.searchGetPrice.view.GetPriceModal.positionPopUp");
      }
    },
       
    updateChildAgeValues: function () {
      var childSelectOption = this;
      if (!childSelectOption.childAgesOptions.length) return;
      _.each(childSelectOption.childAgesOptions, function (option, i) {
        option.setSelectedValue(option.searchPanelModel.get("childAges")[i]);
      });
    }
  });

  return tui.searchPanel.view.ChildSelectOption;
});
