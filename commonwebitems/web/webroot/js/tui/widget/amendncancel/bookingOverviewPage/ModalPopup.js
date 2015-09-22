define("tui/widget/amendncancel/bookingOverviewPage/ModalPopup", [
    "dojo",
    "dojo/on",
    "dojo/query",
    "dojo/dom-construct",
    "dojo/parser",
    "dojo/keys",
    "dojo/_base/lang",
    "dojo/_base/fx",
    "dojo/text!tui/widget/amendncancel/bookingOverviewPage/templates/SecurityQuestionsPopup.html",
    "dojo/_base/json",
    "tui/widget/popup/Popup",
    "tui/widget/form/ValidationTextBox",
    "dijit/form/Form"
], function(dojo, on, query, domConstruct, parser, keys, lang, fx, tmpl, jsonUtil) {

    dojo.declare("tui.widget.amendncancel.bookingOverviewPage.ModalPopup", [tui.widget.popup.Popup], {


        // ---------------------------------------------------------------- tui.widget.popup.Popup properties

        // DOM eventType for displaying popup.
        eventType: "click",

        // Boolean for enabling and disabling popup modal.
        modal: true,

        content: null,

        securityAnswered: false,

        tmpl: tmpl,

        resizeListener: false,

        contentWidth: 0,

        templateString: "",

        contentHeight: 0,

        widgetId: null,

        initialLoad: true,

        controller: null,

        subscribableMethods: ["open", "close", "resize"],

		staticData:null,

        // ---------------------------------------------------------------- tui.widget.popup.PopupBase methods

        postCreate: function() {
            var widget = this;
            var controller = null;
            widget.controller = dijit.registry.byId("controllerWidget");
            widget.controller.registerView(widget);
            widget.content = "<h1>Popup content from template</h1>";
            widget.inherited(arguments);

        },

        onAfterTmplRender: function() {

            var popup = this;
            popup.inherited(arguments);
            dojo.parser.parse(popup.popupDomNode);
            var retrieveForm = dijit.byId("securityQuestionsForm");
            dojo.forEach(dojo.query('INPUT[type=text]', popup.popupDomNode), function(textField) {
                on(textField, "keypress", function(evt) {
                    dojo.addClass(dojo.query('.miss-text')[0], "hide");
                    dojo.addClass(dojo.query('.error-message-section')[0], "hide");
                });
            });

            dojo.connect(dojo.query('INPUT[name=submitButton]', popup.popupDomNode)[0], "onclick", function(evt) {
				var node = dojo.query(".ac-sec-overlay")[0];
				dojo.addClass(node, "updating");
                if (retrieveForm.validate()) {
                    dojo.addClass(dojo.query('.miss-text')[0], "hide");
                    dojo.stopEvent(evt);
                    var url = "validateLeadPaxDetails";

                    var leadPaxShortAddress = dojo.query('INPUT[name=leadPaxShortAddress]', popup.popupDomNode)[0].value;
                    var contactNumber = dojo.query('INPUT[name=contactNumber]', popup.popupDomNode)[0].value;
                    var reqObj = {
                        "leadPaxShortAddress": leadPaxShortAddress,
                        "contactNumber": contactNumber
                    };
                    this.controller = dijit.registry.byId("controllerWidget");
                    this.controller.generateRequest("securityQuestions", url, reqObj);


                } else {
                    dojo.removeClass(dojo.query('.miss-text')[0], "hide");
                    dojo.addClass(dojo.query('.error-message-section')[0], "hide");
                    if(node != undefined){
					dojo.removeClass(node, "updating");
                    }
                }
            });
        },

        open: function() {
            var popup = this;		
            if (popup.modal) {
                if (popup.securityAnswered == "false") {
                    var args = arguments;
                    query("html").addClass("modalmode");
                    popup.displayModal(true, popup.opacity, function() {
                        popup.inherited(args);
                    }, popup);
                    dojo.publish("tui:channel=modalOpening");
                    return;
                }
            }			
        },
		
		close: function () {
			var popup = this;
			if (popup.modal) {
				query("html").removeClass("modalmode");
			}
			popup.inherited(arguments);
			if (popup.modal) {
				popup.displayModal(false, 0, null, popup);
				this.resetForm();
			}
		},
		
		resetForm: function(){
			var popup = this;			
			if(dijit.byId("securityQuestionsForm")){
				dijit.byId("securityQuestionsForm").reset();
			}
			if(!dojo.hasClass(query('.miss-text')[0], "hide")){
				dojo.addClass(query('.miss-text')[0], "hide");
			}
			if(!dojo.hasClass(query('.error-message-section')[0], "hide")){
				dojo.addClass(query('.error-message-section')[0], "hide");
			}		
		},
		
        createPopup: function() {
            var popup = this;
            if (!popup.widgetId) {
                popup.renderTmpl();
            } else {
                var preRenderedPopup = dojo.byId(popup.widgetId);
                if (preRenderedPopup) {
                    popup.popupDomNode = dojo.place(preRenderedPopup, document.body, "last");
                    popup.onAfterTmplRender();
                }
            }
        },

        refresh: function(field, response) {
            var popup = this;
            if (field == "securityQuestions") {
                if (response.error != null && response.error.error) {
                    dojo.removeClass(dojo.query('.error-message-section')[0], "hide");
                    dojo.removeClass(dojo.query(".ac-sec-overlay")[0], "updating");
                } else if (response.address) {
                    popup.close();
                    popup.securityAnswered = "true";

                }
            }

        }
    });
    return tui.widget.amendncancel.bookingOverviewPage.ModalPopup;
});