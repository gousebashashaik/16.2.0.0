package uk.co.portaltech.tui.web.view.data;

import uk.co.portaltech.commons.Collections;
import uk.co.portaltech.travel.model.airport.Airport;
import uk.co.portaltech.travel.model.airport.AirportGuide;

import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;

import static uk.co.portaltech.commons.Collections.map;

public class AirportGuideData {

    private List<AirportData> airports;
    private List<AirportData> airportGroups;

    public static AirportGuideData toAirportGuideData(AirportGuide guide) {
        AirportGuideData airportGuideData = new AirportGuideData();

        airportGuideData.setAirports(map(guide.airports(), new Collections.MapFn<Airport, AirportData>() {
            public AirportData call(Airport airport) {
                return AirportData.toAirportData(airport);
            }
        }));

        if(guide.groupAirports() != null){
            airportGuideData.setAirportGroups(map(guide.groupAirports(), new Collections.MapFn<Airport, AirportData>() {
              public AirportData call(Airport airport) {
                  return AirportData.toAirportData(airport);
              }
          }));
        }

        return airportGuideData;
    }

    public static AirportGuideData toAirportGuideData(AirportGuide guide, String brand) {
       AirportGuideData airportGuideData = new AirportGuideData();

       airportGuideData.setAirports(map(guide.airports(), new Collections.MapFn<Airport, AirportData>() {
           public AirportData call(Airport airport) {
               return AirportData.toAirportData(airport);
           }
       }));

       return airportGuideData;
   }

    public List<AirportData> getAirportGroups() {

        java.util.Collections.sort(airportGroups, new Comparator() {
            @Override
            public int compare(Object lhs, Object rhs) {
                AirportData airport1 = (AirportData)lhs;
                AirportData airport2 = (AirportData)rhs;
                return airport1.getName().compareToIgnoreCase(airport2.getName());
            }
        });

        return airportGroups;
    }

    public List<AirportData> getAirports() {

        java.util.Collections.sort(airports, new Comparator() {
            @Override
            public int compare(Object lhs, Object rhs) {
                AirportData airport1 = (AirportData)lhs;
                AirportData airport2 = (AirportData)rhs;
                return airport1.getName().compareToIgnoreCase(airport2.getName());
            }
        });

        return airports;
    }

    public void setAirports(List<AirportData> airports) {
        this.airports = new ArrayList<AirportData>();
        this.airports.addAll(airports);
    }

    public void setAirportGroups(List<AirportData> airportGroups) {
        this.airportGroups = new ArrayList<AirportData>();
        this.airportGroups.addAll(airportGroups);
    }
}
