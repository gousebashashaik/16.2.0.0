define('tui/villaAvailability/model/VillaDateItemModel', ['dojo'], function (dojo) {

    var formats = {
        'dateFormat': 'dd-MM-yyyy',
        'labelFormat': 'EEE dd'
    };

    function VillaDateItemModel (params) {
        // establish model defaults
        this.when = params.when || null;
        this.label = params.label || params.when ? _.formatDate(params.when, formats.dateFormat, formats.labelFormat) : null;
        this.price = params.price ? parseInt(params.price.toFixed(), 10) : 0;
        this.availability = this.price > 0;
        this.freeChildPlace = params.freeChildPlace || false;
        this.duration = params.duration || 0;
        this.airportCode = params.airportCode || '';
        this.airportName = params.airportName || '';
    }

    return VillaDateItemModel;
});