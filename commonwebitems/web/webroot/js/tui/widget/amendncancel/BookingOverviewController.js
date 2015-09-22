define('tui/widget/amendncancel/BookingOverviewController', [
    'dojo',
    'dojo/query',
    'dojo/dom-class',
    "dojo/_base/xhr",
    "dojo/dom",
    "dojo/ready",
    "dojo/dom-construct",
    "tui/widget/_TuiBaseWidget",
    "tui/widget/_TuiBaseWidget"
], function(dojo, query, domClass, xhr, dom, ready, domconstruct) {

    dojo.declare('tui.widget.amendncancel.BookingOverviewController', [tui.widget._TuiBaseWidget], {


        // Widgets registered
        views: [],

        registerView: function(view) {
            console.log("registring");
            var widget = this;
            widget.views.push(view);
            //console.log(widget.views);
        },

        publishToViews: function(field, response) {
            var widget = this;
            _.each(widget.views, function(view) {
                view.refresh(field, dojo.getObject(view.dataPath || '', false, response));
            });
        },
        generateRequest: function(field, value, contentValue) {
            var widget = this;

            console.log("ajax call");
            //console.log(value);

            var results = xhr.post({
                url: value,
                content: contentValue,
                handleAs: "json",
                headers: {
                    Accept: "application/javascript, application/json"
                },
                error: function(jxhr, err) {
                    if (dojoConfig.devDebug) {
                        //console.error(jxhr);
                    }
                    //console.log(err.xhr.responseText);
                    widget.afterFailure(err.xhr.responseText);
                }
            });
            dojo.when(results, function(response) {
                if (response) {
                    widget.publishToViews(field, response);
                }
            });
        },
        afterFailure: function(html) {
            //console.log(err.xhr.responseText);
        },
        refData: function(listOfData, indexValue) {
            return _.find(listOfData, function(item, index) {
                if (index == indexValue) return item
            });
        }

    })

    return tui.widget.amendncancel.BookingOverviewController;
});