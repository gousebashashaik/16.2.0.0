define("tui/searchGetPrice/view/GetPriceModal", [
    "dojo",
    "dojo/_base/lang",
    "dojo/_base/fx",
    "tui/utils/RequestAnimationFrame",
    "tui/widget/popup/DynamicPopup"], function (dojo, lang, fx) {

    dojo.declare("tui.searchGetPrice.view.GetPriceModal", [tui.widget.popup.DynamicPopup], {

        // ---------------------------------------------------------------- properties

        jsonData: null,

        includeScroll: true,

        openOnStart: false,

        resizeListener: false,

        timer: null,

        subscribableMethods: ["open", "close", "resize", "positionPopUp"],

        searchPanelModel: null,

        // ---------------------------------------------------------------- methods

        onAfterTmplRender: function () {
            var getPriceModal = this;
            getPriceModal.inherited(arguments);

			if((dojoConfig.site == "thomson") || (dojoConfig.site == "firstchoice")){
				if(location.href.indexOf("\&PassToAccom=true") !== -1 ){
				getPriceModal.openOnStart=true;
				if (getPriceModal.openOnStart) getPriceModal.open();
				}
				else{
					if (getPriceModal.openOnStart){ getPriceModal.open();}
				}
			}
			else{
            if (getPriceModal.openOnStart) getPriceModal.open();
			}
        },

        close: function () {
            // summary:
            //		Overides the default close method from popupbase, ensuring that modal DOM container
            //		if configured is closed on close.
            var getPriceModal = this;
            getPriceModal.publishMessage("searchPanel/searchOpening");
            getPriceModal.inherited(arguments);
            dojo.publish("tui:channel=getPriceClosing");
        },

        open: function(sailingDate) {
            var getPriceModal = this;
            getPriceModal.inherited(arguments);
        },

        onOpen: function () {
            // summary:
            //		Override default method, fired when popup opens
            var getPriceModal = this;
            getPriceModal.publishMessage("searchPanel/searchOpening");
            if(dojo.hasClass(getPriceModal.popupDomNode, "searching")) dojo.removeClass(getPriceModal.popupDomNode, "searching");
            getPriceModal.initSearchController();
        },

        initSearchController: function() {
          var getPriceModal = this;
          if(!getPriceModal.searchPanelModel) return;
          getPriceModal.searchPanelModel.resetStores('from');
          getPriceModal.searchPanelModel.resetFields({
            'adults': getPriceModal.searchPanelModel.searchConfig.MIN_ADULTS_NUMBER,
            'children': 0,
            'date': ''
          });
        },

        resize: function (w) {
            // summary:
            //		Override default method to animate width of popup
            var getPriceModal = this;
            if (getPriceModal.resizeListener) {
                getPriceModal.animateWidth(w);
            }
        },

        positionPopUp : function(){
        	var getPriceModal = this;
        	getPriceModal.posElement(getPriceModal.popupDomNode);
        },

        animateWidth: function (w) {
            var getPriceModal = this;
            fx.animateProperty({
                node: getPriceModal.popupDomNode,
                properties: {
                    'minWidth': w
                },
                duration:280,
                onAnimate: function(){
                    getPriceModal.posElement(getPriceModal.popupDomNode);
                    dojo.publish("tui.searchPanel.view.SearchDatePicker.resize");
                    dojo.publish("tui.searchPanel.view.HowLongOptions.resize");
                    dojo.publish("tui.searchPanel.view.AdultsSelectOption.resize");
                    dojo.publish("tui.searchPanel.view.SeniorsSelectOption.resize");
                    dojo.publish("tui.searchPanel.view.ChildSelectOption.resize");

                    dojo.publish("tui.searchPanel.view.cruise.DatePicker.resize");
                    dojo.publish("tui.searchPanel.view.cruise.DurationPicker.resize");
                    dojo.publish("tui.widget.form.SelectOption.resize");
                },
                onEnd: function () {
                  dojo.publish("tui.searchPanel.view.SearchDatePicker.resize");
                  dojo.publish("tui.searchPanel.view.HowLongOptions.resize");
                  dojo.publish("tui.searchPanel.view.AdultsSelectOption.resize");
                  dojo.publish("tui.searchPanel.view.SeniorsSelectOption.resize");
                  dojo.publish("tui.searchPanel.view.ChildSelectOption.resize");

                  dojo.publish("tui.searchPanel.view.cruise.DatePicker.resize");
                  dojo.publish("tui.searchPanel.view.cruise.DurationPicker.resize");
                  dojo.publish("tui.widget.form.SelectOption.resize");
                }
            }).play();
        }

    });

    return tui.searchGetPrice.view.GetPriceModal;
});