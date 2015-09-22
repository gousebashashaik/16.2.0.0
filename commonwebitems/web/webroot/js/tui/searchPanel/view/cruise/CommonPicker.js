define ("tui/searchPanel/view/cruise/CommonPicker", ["dojo",
    "dojo/on",
    "dojo/date/locale",
    "dojo/topic",
    "tui/widget/form/SelectOption",
    "dojo/NodeList-traverse",
    "tui/widget/search/CookieSearchSave"], function(dojo, on){

    dojo.declare("tui.searchPanel.view.cruise.CommonPicker", [tui.widget.form.SelectOption], {

        postCreate: function(){
            var selectOption = this;
            selectOption.listableInit();
            selectOption.attachEvents();

            selectOption.attachListKeyEvent(selectOption.selectDropdown);
            selectOption.updateResponse(0, true);
            selectOption.renderList();

            dojo.attr(selectOption.selectDropdown, "tabindex", dojo.attr(selectOption.selectNode, "tabindex") || 0);
            dojo.attr(selectOption.domNode, "tabindex", -1);
            dojo.attr(selectOption.selectNode, "tabindex", -1);
            selectOption.tagElement(selectOption.domNode, selectOption.analyticText);

            dojo.subscribe("tui:channel=modalOpening", function () {
                if (selectOption.listShowing) selectOption.hideList();
            });
            selectOption.attachUpdateEvent(true);
        },

        attachUpdateEvent: function(pageLoad){
            var selectOption = this;
            dojo.subscribe("tui:channel=updateResponse", function () {
                var selected = selectOption.getSelectedIndex();
                selectOption.store.requestData(selectOption.searchPanelModel).then(function(responseData){
                   _.each(responseData, function(option){
                      (option.disabled) ? selectOption.disableItem(selectOption.getIndexFromValue(option.value)) : selectOption.enableItem(selectOption.getIndexFromValue(option.value));
                   });
                });
            });
        },

        attachEvents: function(){

            var selectOption = this;
            dojo.connect(selectOption.selectDropdown, "onclick", function (event) {

               if (!selectOption.listShowing) {
                   dojo.publish("tui:channel=modalOpening");
               }
               selectOption.removeErrors();

               dojo.stopEvent(event);
               //setTimeout(selectOption.updateResponse(), 450);
               dojo.publish("tui/widget/form/SelectOption/onclick", [selectOption, event]);
               dojo.addClass(selectOption.selectDropdown, "open");
               selectOption.selectDropdown.focus();
               selectOption.onSelectOptionsClick(event, this);
               if (selectOption.listShowing) {
                  selectOption.hideList();
                  return;
               }

               if (selectOption.fixedWidth) {
                  var width = dojo.style(selectOption.selectDropdown, "width");
                  dojo.style(selectOption.listElement, "width", [width, "px"].join(""));
               }
               selectOption.showList();

            });
            
            // add/remove focus class on focus/blur
            on(selectOption.selectDropdown, "focus", function (event) {
                dojo.addClass(selectOption.selectDropdown, "focus");
            });

            on(selectOption.selectDropdown, "blur", function () {
                dojo.removeClass(selectOption.selectDropdown, "focus");
            });

            selectOption.subscribe("tui/widget/form/SelectOption/onclick", function (selectOptionObj) {
                var selectOption = this;
                if (selectOption === selectOptionObj) return;
                selectOption.hideList();
            });

            selectOption.subscribe("tui.searchBPanel.view.DropDownSelector", function (canPositionDropDown) {
                var selectOption = this;
                selectOption.posElement(selectOption.listElement);
            });
            
            dojo.connect(document.body, "onclick", function (event) {
                if (document.activeElement === selectOption.selectDropdown || !selectOption.listShowing) {
                    return;
                }
                selectOption.hideList();
            });
        },

        updateResponse: function (selected, pageLoad) {
           var selectOption = this;
           selectOption.store.requestData(selectOption.searchPanelModel).then(function(responseData){
              selectOption.buildListData(responseData);
              selectOption.setSelectedIndex(selected);
              if(pageLoad){
                 selectOption.populateSavedSearch();
              }
           });
        },

        buildListData: function (results) {
            var selectOption = this;
            selectOption.listData.length = 0;
            selectOption.parseJsonData(results);
            selectOption.createOptions();
            selectOption.renderList();
        },

        parseJsonData: function (results) {
            var selectOption = this;
            selectOption.addDefault ? selectOption.addDefaultOption() : null;
            var list = results.dates || results ;
            _.forEach(list, function (item) {
                selectOption.listData.push({
                    text: item.name,
                    value: item.value,
                    disabled: item.disabled
                });
            });
        },

        addDefaultOption: function(defaultLabel){
            var selectOption = this;
            selectOption.listData.push({
                text: defaultLabel ? defaultLabel : 'Select',
                value: 0,
                disabled: false
            });
        },

        removeErrors: function(){}

    });
    return tui.searchPanel.view.cruise.CommonPicker;
});