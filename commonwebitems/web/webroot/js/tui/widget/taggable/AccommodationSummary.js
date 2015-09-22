define('tui/widget/taggable/AccommodationSummary', [
    'dojo',
    'dojo/query',
    'tui/widget/mixins/Taggable'
], function(dojo, query, taggable) {

    dojo.declare('tui.widget.taggable.AccommodationSummary', [tui.widget._TuiBaseWidget], {

        // ----------------------------------------------------------------------------- methods

        postCreate: function() {
            var accommodationSummary = this;
            accommodationSummary.inherited(arguments);
            var ctaBtns = query('.cta-buttons', accommodationSummary.domNode)[0];
            accommodationSummary.tagElement(query('.cta', ctaBtns)[0], 'bookpackageNow');
            accommodationSummary.tagElement(query('.shortlist', ctaBtns)[0], 'packagesavetoShortlist');
            accommodationSummary.tagElement(query('.search-alt', accommodationSummary.domNode)[0], 'searchAgain');
        }
    });

    return tui.widget.taggable.AccommodationSummary;
});
