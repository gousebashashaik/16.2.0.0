define("tui/searchBResults/service/RoomGroupingService", ["dojo"], function (dojo) {


    function maxValue(object, attribute) {
        return _.max(_.pluck(object, attribute))
    }

    function minValue(object, attribute) {
        return _.min(_.pluck(object, attribute))
    }

    function hasParam(roomGroup, searchParams) {
        return _.size(_.find(roomGroup, searchParams)) > 0;
    }

    function flattenGroupedRooms(roomGroup) {
        roomGroup = dojo.clone(roomGroup);

        return {
            availability: !hasParam(roomGroup, {availability: false}),
            boardBasisCode: roomGroup[0].boardBasisCode,
            boardType: roomGroup[0].boardType,
            occupancy: roomGroup[0].occupancy,
            price: roomGroup[0].price,
            roomCode: roomGroup[0].roomCode,
            roomCount: _.size(roomGroup) > 1 ? _.size(roomGroup) : false,
            roomType: roomGroup[0].roomType,
            sellingout: minValue(roomGroup, "sellingout")
        };
    }

    return {
        groupRooms: function (rooms, groupAttribute) {
            var roomsData = dojo.clone(rooms),
                roomGroups,
                groupedRooms = [];

            roomGroups = _.groupBy(roomsData, function(room){
                return room[groupAttribute];
            });

            _.each(roomGroups, function (roomGroup){
                groupedRooms.push(flattenGroupedRooms(roomGroup));
            });

            return groupedRooms;
        }
    }

});