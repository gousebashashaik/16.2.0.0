define("tui/cruise/sailingDates/view/SailingDuration", [
    "dojo",
    'dojo/_base/declare',
    "dojo/on",
    "tui/widget/form/SelectOption"], function (dojo, declare, on) {
    return declare("tui.cruise.sailingDates.view.SailingDuration", [tui.widget.form.SelectOption], {
    	
    	 // ----------------------------------------------------------------------------- methods
         postCreate: function () {
    		var selectOption = this;
            selectOption.inherited(arguments);
            selectOption.listableInit();
            selectOption.buildListData(selectOption.jsonData);
            dojo.connect(selectOption.selectDropdown, "onclick", function (event) {
                //dojo.stopEvent(event);
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
            on(selectOption.selectDropdown, "focus", function () {
                dojo.addClass(selectOption.selectDropdown, "focus");
            });

            on(selectOption.selectDropdown, "blur", function () {
                dojo.removeClass(selectOption.selectDropdown, "focus");
            });

            selectOption.subscribe("tui/widget/form/SelectOption/onclick", function (selectOptionObj) {
                var selectOption = this;
                if (selectOption === selectOptionObj) { return true; }
                selectOption.hideList();
            });


            dojo.connect(document.body, "onclick", function () {
                if (document.activeElement === selectOption.selectDropdown || !selectOption.listShowing) {
                    return;
                }
                selectOption.hideList();
            });

            selectOption.attachListKeyEvent(selectOption.selectDropdown);
            selectOption.renderList();

            selectOption.inherited(arguments);
            dojo.attr(selectOption.selectDropdown, "tabindex", dojo.attr(selectOption.selectNode, "tabindex") || 0);
            dojo.attr(selectOption.domNode, "tabindex", -1);
            dojo.attr(selectOption.selectNode, "tabindex", -1);
            dojo.subscribe("tui:channel=modalOpening", function () {
                if (selectOption.listShowing) {
                	selectOption.hideList();
                }
            });
        },

        buildListData: function (dataresults) {
            var selectOption = this;
            selectOption.listData.length = 0;
            selectOption.parseJsonData(dataresults);
            selectOption.createOptions();
            selectOption.renderList();
        },

        parseJsonData: function (jsonData) {
            var selectOption = this;
            _.each(jsonData.durationFilterDatas, function (item,i) {
                selectOption.listData.push({
                    text: item.filterName,
                    value: item.filterName,
                    index:i,
                    disabled: false
                });

            });
        },

        onChange: function (name, oldValue, newvalue) {
            var selectOption = this, i;
            dojo.html.set(selectOption.selectDropdownLabel, newvalue.key);
            for ( i = 0; i < selectOption.selectNode.options.length; i =i+1) {
                if (selectOption.selectNode.options[i].value === newvalue.value) {
                    selectOption.selectNode.options[i].selected = true;
                }
            }
            var urlParams = document.URL.split("?")[1], shipCode;
            if(!urlParams){
            	shipCode = "";
            }else {
            	shipCode = urlParams.split("=")[1];
            }
            dojo.publish("tui/cruise/sailingDates/view/SailingDatesComponent/updateSailingList",[ newvalue, shipCode] );
        }
    });
}); 