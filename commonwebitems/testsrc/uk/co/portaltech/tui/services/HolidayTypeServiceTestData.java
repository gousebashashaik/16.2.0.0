/**
 * 
 */
package uk.co.portaltech.tui.services;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import uk.co.portaltech.travel.model.unit.Unit;
import uk.co.portaltech.travel.model.unit.Units;
import uk.co.portaltech.tui.web.view.data.DestinationData;
import uk.co.portaltech.tui.web.view.data.wrapper.UnitData;
/**
 * @author laxmibai.p
 *
 */
public final class HolidayTypeServiceTestData {
	
	private static  DestinationData dest1;
	private static DestinationData dest2;
	private static DestinationData dest3;
	private static DestinationData dest4;
	
	private static Unit unit1;
	private static Unit unit2;
	private static Unit unit3;
	private static Unit unit4;
	private static Unit unit5;
	private static Unit unit6;
	
	private HolidayTypeServiceTestData()
	{
		
	}
	
	
	public static List<DestinationData> getDestinationDataList()
	{
		List<DestinationData> destData = new ArrayList<DestinationData>();
		
		List<UnitData> unitDataList=new ArrayList<UnitData>();
		
		UnitData unitData1=new UnitData();
		
		unitData1.setId("02240");
		unitData1.setName("Holiday village");
		unitData1.setType("Normal");
		
		UnitData unitData2=new UnitData();
		
		unitData2.setId("02245");
		unitData2.setName("Premier Families");
		unitData2.setType("Normal");
		
		UnitData unitData3=new UnitData();
		
		unitData3.setId("022487");
		unitData3.setName("Premier");
		unitData3.setType("Normal");
		
		UnitData unitData4=new UnitData();
		
		unitData4.setId("022467");
		unitData4.setName("Splas World");
		unitData4.setType("Normal");
		
		unitDataList.add(unitData1);
		unitDataList.add(unitData2);
		unitDataList.add(unitData3);
		unitDataList.add(unitData4);
		
		dest1 = new DestinationData();
		dest1.setAvailable(false);
		dest1.setChildren(null);
		dest1.setId("FHV");
		dest1.setName("Holiday village");
		dest1.setType(null);
		dest1.setHotels(unitDataList);
		destData.add(dest1);
		
		dest2 = new DestinationData();
		dest2.setAvailable(false);
		dest2.setChildren(null);
		dest2.setId("FPF");
		dest2.setName("Premier Families");
		dest2.setType(null);
		dest2.setHotels(unitDataList);
		destData.add(dest2);
		
		dest3 = new DestinationData();
		dest3.setAvailable(false);
		dest3.setChildren(null);
		dest3.setId("FPR");
		dest3.setName("Premier");
		dest3.setType(null);
		dest3.setHotels(unitDataList);
		destData.add(dest3);
		
		dest4 = new DestinationData();
		dest4.setAvailable(false);
		dest4.setChildren(null);
		dest4.setId("FSP");
		dest4.setName("Splas World");
		dest4.setType(null);
		dest4.setHotels(unitDataList);
		destData.add(dest4);
		return destData;
	}

		
	public static Units getUnitsForRoute()
	{
		Collection<Unit> units = new ArrayList<Unit>();
		
		
		
		unit6 = new Unit();
		unit6.setProductRangeType("Premier");
		units.add(unit6);
		
		
		unit3 = new Unit();
		unit3.setProductRangeType("premier");
		units.add(unit3);
		
		unit4 = new Unit();
		unit4.setProductRangeType("premier");
		units.add(unit4);
		
		unit1 = new Unit();
		unit1.setProductRangeType("premier");
		units.add(unit1);
		Units unit = new Units(null,units,null,null);
		return unit;
		
	}
	public static Collection<Unit> getUnitsWithOutRoute()
	{
		Collection<Unit> units = new ArrayList<Unit>();
		
		unit5 = new Unit();
		unit5.setProductRangeType("Premier Families");
		units.add(unit5);
		unit2 = new Unit();
		unit2.setProductRangeType("Premier Families");
		units.add(unit2);
		return units;
	}
}
