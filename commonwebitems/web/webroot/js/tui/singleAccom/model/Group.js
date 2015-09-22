define("tui/singleAccom/model/Group", [], function () {

    return function Group(key, name, type, members, lowPrice, dateRange, airportList) {
        this.key = key;
        this.name = name;
        this.type = type;
        this.members = members;
        lowPrice ? this.lowPrices = lowPrice : null;
        dateRange ? this.dateRange = dateRange : null;
        airportList ? this.airportList = airportList : null;
    }
});