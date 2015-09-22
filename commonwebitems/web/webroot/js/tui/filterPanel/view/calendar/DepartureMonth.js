define("tui/filterPanel/view/calendar/DepartureMonth", [
    "dojo",
    "dojo/_base/declare",
    'dojo/query',
    "dojo/_base/lang",
    "dojo/dom-attr",
    "dojo/on",
    "dojo/topic",
    "dojo/text!tui/filterPanel/view/templates/sailingDates.html",
    "dojo/dom-construct",
    "dojo/dom-class",
    "dijit/registry",
    "tui/widget/TagMappingTable",
    "dojo/date/locale"], function (dojo, declare, query, lang, domAttr, on, topic, tmpl, domConstruct, domClass, registry, tagMappingTable, locale) {

    return declare("tui.filterPanel.view.calendar.DepartureMonth", [tui.widget._TuiBaseWidget], {

        dataPath: 'deptDate',



        postCreate: function () {
            var widget = this;
            var data = widget.getParent().registerFilter(widget);
            if( data ){
                try{
                    widget.draw(data);
                }catch(err){console.log(err);}
                widget.inherited(arguments);
            }
        },

        generateRequest: function () {
            var widget = this;
            var request = {};
            var filterId = widget.filterId || widget.id;
            request[filterId] = [];
            //This is modified for 'backend convenience'. To be modified when the Format is changed.
            request[filterId].push({
                'name': filterId,
                'value': locale.format(widget.selectedDate, {selector: "date", datePattern: "dd-MM-yyyy"}),
                'parent': filterId || null,
                'categoryCode': null,
                'disabled': null,
                'selected': null,
                'countShowing': null,
                'noItineraries': null,
                'id': filterId
            });
            /*
             request[filterId] = {
             value: widget.selectedDate
             };
             */
            return request;
        },

        draw: function (data) {
            var widget = this, wrap = dojo.query(".item-content-wrap", widget.domNode),
                placeNode = wrap.length ? wrap[0] : widget.domNode;
            widget.inherited(arguments);

        },

        refresh: function (field, oldValue, newValue, data, isCached) {
            var widget = this, filterMap, optionClicked;
            if(isCached) return;

        },

        reset: function (data) {
            var widget = this;
            widget.clear(data);
        },

        clear: function (data) {
            var widget = this;
            if (data && data.filters) {
                widget.checkBoxes = [];
                widget.destroyWidgets();
                widget.draw(data);
            }
        },

        selectionUpdated: function (id, oldValue, newValue) {
            var filter = this;
            topic.publish("tui/filterPanel/view/FilterController/applyFilter", {
                'oldValue': { 'value': ''},
                'newValue': { 'value': filter.selectedDate}
            });
        },

        initialise: function(data){
            var widget = this;
            widget.firstDate = dojo.clone(new Date(data.minValue));
            widget.endDate = dojo.clone(new Date(data.maxValue));
            widget.selectedDate = new Date(data.selecteValue);
            widget.actualDate = new Date(data.selecteValue);
            widget.month = widget.selectedDate.getMonth();
            widget.year = widget.selectedDate.getFullYear();
            widget.combineMonthsAndYears();

        },

        attachEvents : function () {
            var widget = this;
            on(widget.prevButton, "click", lang.hitch(widget, widget.prevButtonClicked));
            on(widget.nextButton, "click", lang.hitch(widget, widget.nextButtonClicked));
        }

    });
});