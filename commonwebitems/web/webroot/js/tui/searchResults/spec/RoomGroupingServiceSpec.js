describe('Search Results Room Grouping Service', function () {

  var roomService;

  beforeEach(function () {
    roomService = dojo.require("tui/searchResults/service/RoomGroupingService");
  });

  it("should be loaded", function () {
    expect(roomService).not.toBe(null);
  });

  it('should flatten rooms of the same type into a single room', function () {
    var rooms = [{
      "roomTypeGroup":"Twin Bedroom",
      "roomType":"Deluxe twin room",
      "roomCode":"DG143",
      "sellingout":31,
      "availability":false,
      "boardType":"Full Board",
      "boardBasisCode":"FB",
      "price":{"discount":null},
      "occupancy":{"adults":2,"seniors":0,"children":0,"paxDetail":[{"id":1,"age":30},{"id":3,"age":30}],"infant":0}
    },{
      "roomTypeGroup":"Twin Bedroom",
      "roomType":"Deluxe twin room",
      "roomCode":"DG143",
      "sellingout":31,
      "availability":false,
      "boardType":"Full Board",
      "boardBasisCode":"FB",
      "price":{"discount":null},
      "occupancy":{"adults":2,"seniors":0,"children":0,"paxDetail":[{"id":2,"age":30},{"id":4,"age":30}],"infant":0}
    }];
    var grouped = roomService.groupRooms(rooms, "roomType");

    expect(grouped.length).toBe(1);
    expect(grouped[0].roomType).toBe("Deluxe twin room");

  });

  it('should return lowest availability where one or more of same rooms are selling out', function () {
    var rooms = [{"roomTypeGroup":"Twin Bedroom","roomType":"Twin room with limited sea view","roomCode":"BV141","sellingout":5,"availability":true,"boardType":"Half Board","boardBasisCode":"HB","price":{"discount":null},"occupancy":{"adults":2,"seniors":0,"children":0,"paxDetail":[{"id":1,"age":30},{"id":3,"age":30}],"infant":0}},{"roomTypeGroup":"Twin Bedroom","roomType":"Twin room with limited sea view","roomCode":"BV141","sellingout":2,"availability":true,"boardType":"Half Board","boardBasisCode":"HB","price":{"discount":null},"occupancy":{"adults":2,"seniors":0,"children":0,"paxDetail":[{"id":2,"age":30},{"id":4,"age":30}],"infant":0}}];
    var grouped = roomService.groupRooms(rooms, "roomType");

    expect(grouped.length).toBe(1);
    expect(grouped[0].roomType).toBe("Twin room with limited sea view");
    expect(grouped[0].sellingout).toBe(2);

  });

});