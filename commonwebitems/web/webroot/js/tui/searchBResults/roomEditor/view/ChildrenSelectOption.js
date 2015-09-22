define("tui/searchBResults/roomEditor/view/ChildrenSelectOption", [
  "dojo",
  "dojo/on",
  "dojo/text!tui/searchBResults/roomEditor/view/templates/roomOptionsTmpl.html",
  "tui/searchBResults/roomEditor/view/PartyCompSelectOption",
  "tui/widget/mixins/Templatable"], function (dojo, on, roomOptionsTmpl, SelectOption) {

  dojo.declare("tui.searchBResults.roomEditor.view.ChildrenSelectOption", [tui.searchBResults.roomEditor.view.PartyCompSelectOption, tui.widget.mixins.Templatable], {

    // ----------------------------------------------------------------------------- properties

    roomModel: null,

    unitNode: null,

    popupNode: null,

    childAges: null,

    childAgeSelectOptions: null,

    templateView: null,

    tmpl: roomOptionsTmpl,

    watchers: null,

    // ----------------------------------------------------------------------------- methods

    postCreate: function () {
      // summary:
      //      Sets default values for class properties.
      var childrenSelectOption = this;
      childrenSelectOption.inherited(arguments);
      childrenSelectOption.watchers = [];

      childrenSelectOption.childAges = [];
      childrenSelectOption.childAgeSelectOptions = [];

      childrenSelectOption.initWatchers();

      childrenSelectOption.connect(childrenSelectOption, "onChange", function (name, oldValue, newValue) {
        childrenSelectOption.roomModel.set("children", parseInt(newValue.value, 10));
      });

      // set initial value from model
      childrenSelectOption.setSelectedValue(childrenSelectOption.roomModel.get("children"));

      childrenSelectOption.subscribe("tui/searchBResults/roomEditor/view/RoomsEditor/Errors/Party", function (action) {
        action = action ? "addClass" : "removeClass";
        dojo[action](childrenSelectOption.domNode, "error");
      });
      childrenSelectOption.tagElement(childrenSelectOption.domNode, childrenSelectOption.roomModel.id + "-children");
    },

    initWatchers: function () {
      // summary:
      //      Initialises watcher(s)
      var childrenSelectOption = this;

      // place watchers in array so we can unwatch all on destroy
      childrenSelectOption.watchers.push(
          // children watcher
          childrenSelectOption.roomModel.watch("children", function (name, oldValue, newValue) {
            if (newValue.length === 0) {
              return;
            }
            if (parseInt(childrenSelectOption.getSelectedData().value, 10) !== newValue) {
              childrenSelectOption.setSelectedValue(newValue);
            } else {
              var childAges = dojo.clone(childrenSelectOption.roomModel.childAges);
              var diff = 0;
              oldValue = parseInt(oldValue, 10);
              newValue = parseInt(newValue, 10);

              if (newValue > oldValue) {
                diff = newValue - oldValue;
                for (var i = 0; i < diff; i++) {
                  childAges.push(-1);
                }
              } else {
                diff = oldValue - newValue;
                childAges.splice(-diff, diff);
              }
              childrenSelectOption.roomModel.set("childAges", childAges);
            }
          }),
          // childages watcher
          childrenSelectOption.roomModel.watch("childAges", function (name, oldAges, newAges) {
            if (childrenSelectOption.childAgeSelectOptions.length === newAges.length) {
              return;
            }
            childrenSelectOption.addChildAgeSelectOptions(newAges.length);
          })
      );
    },

    addChildAgeSelectOptions: function (size) {
      // summary:
      //      Adds age selector widget for each child in parent's view
      var childrenSelectOption = this;
      size = parseInt(size, 10);
      childrenSelectOption.removeChildAgeSelectOptions();

      if (size === 0) {
        return;
      }

      childrenSelectOption.childAges = dojo.clone(childrenSelectOption.roomModel.get("childAges"));
      childrenSelectOption.templateView = "childages";

      var html = childrenSelectOption.renderTmpl(null, childrenSelectOption);

      dojo.place(html, dojo.query(dojo.byId(childrenSelectOption.roomModel.id), childrenSelectOption.popupNode)[0], "last");
      _.forEach(dojo.query(".child-age", childrenSelectOption.unitNode), function (item, i) {
        var select = new SelectOption({
          roomModel: childrenSelectOption.roomModel,
          roomsEditorModel: childrenSelectOption.roomsEditorModel
        }, item);

        // set initial child age value
        select.setSelectedValue(childrenSelectOption.childAges[i]);

        // set child age to model onchange
        select.connect(select, "onChange", function (name, oldValue, newValue) {
          var childAges = dojo.clone(childrenSelectOption.roomModel.get("childAges"));
          childAges[i] = parseInt(newValue.value, 10);
          childrenSelectOption.roomModel.set("childAges", childAges);
        });

        // subscribe to child ages errors, allows each room to show error if matches
        select.subscribe("tui/search/model/ChildAges/Errors", function () {
          var action = (parseInt(select.getSelectedData().value, 10) === -1) ? "addClass" : "removeClass";
          dojo[action](select.domNode, "error");
        });

        select.subscribe("tui/searchBResults/roomEditor/view/RoomsEditor/Errors/InfantLimit", function (action) {
          action = action ? "addClass" : "removeClass";
          dojo[action](select.domNode, "error")
        });
        childrenSelectOption.tagElement(select.domNode, childrenSelectOption.roomModel.id + "-childAge-" + (i + 1));

        childrenSelectOption.childAgeSelectOptions.push(select);
      });

      // publish resize request to popup
      dojo.publish("tui.searchResults.roomEditor.view.RoomsPopup.resize");
    },

    removeChildAgeSelectOptions: function () {
      // summary:
      //      Removes all age selector widgets in parent's view
      var childrenSelectOption = this;
      if (childrenSelectOption.childAgeSelectOptions.length <= 0) {
        return;
      }

      _.forEach(childrenSelectOption.childAgeSelectOptions, function (item) {
        // fixme: not removing dropdownlists for some reason
        item.destroyRecursive();
      });
      childrenSelectOption.childAgeSelectOptions.length = 0;
      dojo.query(".child-ages", childrenSelectOption.unitNode).remove();
    },

    destroyRecursive: function () {
      // summary:
      //      removes widget recursively
      var childrenSelectOption = this;
      _.forEach(childrenSelectOption.watchers, function (item) {
        item.unwatch();
      });
      if (childrenSelectOption.childAgeSelectOptions.length > 0) {
        _.forEach(childrenSelectOption.childAgeSelectOptions, function (item) {
          item.destroyRecursive();
        });
      }
      childrenSelectOption.inherited(arguments);
    }

  });

  return tui.searchBResults.roomEditor.view.ChildrenSelectOption;
});