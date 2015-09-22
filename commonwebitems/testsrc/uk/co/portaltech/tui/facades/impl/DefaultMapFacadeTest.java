/**
 *
 */
package uk.co.portaltech.tui.facades.impl;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commercefacades.converter.ConfigurablePopulator;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.services.AccommodationService;
import uk.co.portaltech.tui.components.model.InspirationMapComponentModel;
import uk.co.portaltech.tui.converters.LocationConverter;
import uk.co.portaltech.tui.converters.LocationOption;
import uk.co.portaltech.tui.facades.impl.DefaultMapFacade;
import uk.co.portaltech.tui.model.InspirationMapTabModel;
import uk.co.portaltech.tui.populators.AccommodationInteractiveMapPopulator;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.InspirationMapComponentData;
import uk.co.portaltech.tui.web.view.data.InspirationMapViewData;
import uk.co.portaltech.tui.web.view.data.LocationData;
import uk.co.portaltech.tui.web.view.data.MapDataWrapper;
import uk.co.portaltech.tui.web.view.data.MarkerMapViewData;

/**
 * @author venkataharish.k
 * 
 */

public class DefaultMapFacadeTest {

	@InjectMocks
	private final DefaultMapFacade abc = new DefaultMapFacade();
	@Mock
	private CMSSiteService siteService;
	@Mock
	private AccommodationService accommodationService;
	@Mock
	private AccommodationInteractiveMapPopulator accommodationInteractiveMapPopulator;
	@Mock
	private AccommodationModel accommodationModel;
	@Mock
	private CatalogVersionModel model;
	@Mock
	MapDataWrapper mapData;
	@Mock
	private CategoryService categoryService;
	@Mock
	private LocationData locationdata;
	@Mock
	private LocationModel locationmodel;
	@Mock
	private LocationOption locationoption;
	@Mock
	private LocationConverter locationConverter;
	@Mock
	private ConfigurablePopulator<LocationModel, LocationData, LocationOption> defaultLocationConfiguredPopulator;
	@Mock
	private InspirationMapComponentModel inspirationMapComponentModel;
	@Mock
	private InspirationMapComponentData inspirationMapComponentData;
	@Mock
	private InspirationMapTabModel inspirationMapTabModel;
	@Mock
	private MarkerMapViewData markerMap;
	@Mock
	private InspirationMapViewData inspirationMapViewDataLocation2,
			inspirationMapViewDataAccom1, inspirationMapViewDataAccom2,
			inspirationMapViewData1, inspirationMapViewData2,
			inspirationMapViewData3, inspirationMapViewDataLocation;
	@Mock
	private Converter<List<LocationModel>, InspirationMapViewData> inspirationMapConverter;
	@Mock
	private Populator<List<LocationModel>, InspirationMapViewData> inspirationMapPopulator;
	@Mock
	private Converter<List<AccommodationModel>, InspirationMapViewData> inspirationMapConverterForAccom;
	@Mock
	private Populator<List<AccommodationModel>, InspirationMapViewData> inspirationMapPopulatorForAccom;
	@Mock
	private MediaModel mediamodel;
	private List<MarkerMapViewData> markerMapViewDataList3;
	private List<LocationModel> locationModelList;
	private List<AccommodationModel> accomLocationList;
	private List<InspirationMapViewData> inspirationMapViewDataList;
	@Mock
	private CategoryModel categorymodel;
	private static final String ACCOMMODATIONCODE = "004097";
	private static final String LOCATIONCODE = "ABW";
	private static final String COUNTRY = "spain";
	private static final String CODE = "123";

	@Before
	public void setUp() throws Exception {

		MockitoAnnotations.initMocks(this);

		CatalogVersionModel model = new CatalogVersionModel();
		CatalogModel catalog = new CatalogModel();
		catalog.setId("fc-catalog");
		model.setCatalog(catalog);
		model.setVersion("Online");
		AccommodationModel accommodationModel = new AccommodationModel();
		accommodationModel.setChangeDescription("abc");
		Locale loc = new Locale("eng");
		accommodationModel.setName("accom", loc);
		mapData.setLocationType("destination");

		locationdata.getLocationType();

		List<AccommodationViewData> accommodations = new ArrayList<AccommodationViewData>();
		locationdata.setAccommodations(accommodations);
		locationdata.getSubLocations();
		mapData.setLocations(locationdata.getSubLocations());
		inspirationMapComponentModel.getVisibleItems();
		//Dummy data for merging
		dummyDataFortestmerge();
		//Dummy Data for MapData
		dummyDataForInspirationMapData();
		//Dummy Data For Sorting
		dummyDataforSorting();
		//Dummy Data For Tabs
		dummyDataForTabs();
		//Dummy Data For Inspiration.
		dummyDataForGetInspiration();
	}

	public void dummyDataforSorting() {
		Map<String, MarkerMapViewData> markerMap = new HashMap<String, MarkerMapViewData>();
		inspirationMapViewData3 = new InspirationMapViewData();
		markerMapViewDataList3 = new ArrayList<MarkerMapViewData>();
		inspirationMapViewData3
				.setMarkerMapViewDataList(markerMapViewDataList3);
		List<MarkerMapViewData> tempMarkerMap = inspirationMapViewData3
				.getMarkerMapViewDataList();
		for (int i = 0; i < tempMarkerMap.size(); i++) {
			markerMap.put(tempMarkerMap.get(i).getCode(), tempMarkerMap.get(i));
		}
		List<String> value2 = new ArrayList<String>();
		value2.add(LOCATIONCODE);
		inspirationMapTabModel.setLocationCodes(value2);

		List<InspirationMapViewData> inspirationMapViewDataList = new ArrayList<InspirationMapViewData>();
		inspirationMapViewDataList.add(inspirationMapViewData3);
	}

	public MapDataWrapper setDummyLocationType() {

		MapDataWrapper mapData = new MapDataWrapper();
		mapData.setLocationType("accommodation");
		return mapData;
	}

	public void dummyDataForGetInspiration() {
		inspirationMapViewDataList = new ArrayList<InspirationMapViewData>();
		InspirationMapViewData mapviewdata = new InspirationMapViewData();
		mapviewdata.setTitle(COUNTRY);
		inspirationMapViewDataList.add(mapviewdata);
		accomLocationList = new ArrayList<AccommodationModel>();
		List<LocationModel> locationModelList = new ArrayList<LocationModel>();
		LocationModel loc = new LocationModel();
		loc.setCatalogVersion(model);
		locationModelList.add(loc);
		AccommodationModel acc = new AccommodationModel();
		acc.setCatalogVersion(model);
		accomLocationList.add(acc);
	}

	public void dummyDataForTabs() {
		InspirationMapComponentModel inspirationMapComponentModel3 = new InspirationMapComponentModel();
		List<InspirationMapTabModel> value = new ArrayList<InspirationMapTabModel>();

		InspirationMapTabModel tabmodel = new InspirationMapTabModel();
		InspirationMapTabModel tabmodel2 = new InspirationMapTabModel();
		InspirationMapTabModel tabmodel3 = new InspirationMapTabModel();
		InspirationMapTabModel tabmodel4 = new InspirationMapTabModel();
		tabmodel.setCatalogVersion(model);
		tabmodel.setName(COUNTRY);
		tabmodel.setCode(ACCOMMODATIONCODE);
		tabmodel.setTitle(LOCATIONCODE);
		String country2 = "india";
		String code2 = "321";
		String Loc1 = "for winter sun";
		String Loc2 = "for families";
		String Loc3 = "for beaches";
		String Loc4 = "for couples";
		tabmodel.setTitle(Loc1);
		tabmodel2.setTitle(Loc2);
		tabmodel4.setTitle(Loc4);
		tabmodel2.setName(country2);
		tabmodel3.setTitle(Loc3);
		tabmodel.setCode(code2);
		value.add(tabmodel);
		value.add(tabmodel2);
		value.add(tabmodel3);
		value.add(tabmodel4);
		inspirationMapComponentModel3.setTabs(value);

		List<CategoryModel> cmodel = new ArrayList<CategoryModel>();
		categorymodel.setSupercategories(cmodel);
		locationmodel.setSupercategories(cmodel);
		String Locationcode3 = "CUB";
		List<String> list = new ArrayList<String>();
		list.add(Locationcode3);
	}

	public void dummyDataForInspirationMapData() {
		InspirationMapViewData inspirationMapViewDataLocation = new InspirationMapViewData();
		inspirationMapViewDataAccom2 = new InspirationMapViewData();
		InspirationMapViewData inspirationMapViewData2 = new InspirationMapViewData();
		List<MarkerMapViewData> markerMapViewDataList = new ArrayList<MarkerMapViewData>();
		String name = "cuba";
		MarkerMapViewData mapview = new MarkerMapViewData();
		mapview.setName(name);
		mapview.setCode(LOCATIONCODE);
		markerMapViewDataList.add(mapview);
		inspirationMapViewDataLocation
				.setMarkerMapViewDataList(markerMapViewDataList);
		inspirationMapViewDataAccom2
				.setMarkerMapViewDataList(markerMapViewDataList);

		if (CollectionUtils.isNotEmpty(inspirationMapViewDataAccom2
				.getMarkerMapViewDataList())
				&& CollectionUtils.isNotEmpty(inspirationMapViewDataLocation
						.getMarkerMapViewDataList())) {

			List<MarkerMapViewData> inspirationMergedList = (List<MarkerMapViewData>) CollectionUtils
					.union(inspirationMapViewDataAccom2
							.getMarkerMapViewDataList(),
							inspirationMapViewDataLocation
									.getMarkerMapViewDataList());
			inspirationMapViewData2
					.setMarkerMapViewDataList(inspirationMergedList);
			inspirationMapViewData2.setMarkupListCount(inspirationMergedList
					.size());
		} else {
			if (CollectionUtils.isNotEmpty(inspirationMapViewDataAccom2
					.getMarkerMapViewDataList())) {
				inspirationMapViewData2
						.setMarkerMapViewDataList(inspirationMapViewDataAccom2
								.getMarkerMapViewDataList());
				inspirationMapViewData2
						.setMarkupListCount(inspirationMapViewDataAccom2
								.getMarkerMapViewDataList().size());
			} else if (CollectionUtils
					.isNotEmpty(inspirationMapViewDataLocation
							.getMarkerMapViewDataList())) {
				inspirationMapViewData2
						.setMarkerMapViewDataList(inspirationMapViewDataLocation
								.getMarkerMapViewDataList());
				inspirationMapViewData2
						.setMarkupListCount(inspirationMapViewDataLocation
								.getMarkerMapViewDataList().size());
			}
		}
	}

	public void dummyDataFortestmerge() {
		inspirationMapViewDataLocation2 = new InspirationMapViewData();
		inspirationMapViewDataAccom1 = new InspirationMapViewData();
		List<MarkerMapViewData> markerMapViewDataList1 = new ArrayList<MarkerMapViewData>();
		inspirationMapViewDataLocation2
				.setMarkerMapViewDataList(markerMapViewDataList1);
		inspirationMapViewDataAccom1
				.setMarkerMapViewDataList(markerMapViewDataList1);
	}

	@Test
	public void testGetInteractiveMapForAccommodation() {

		when(siteService.getCurrentCatalogVersion()).thenReturn(model, model);
		when(
				accommodationService.getAccomodationByCodeAndCatalogVersion(
						ACCOMMODATIONCODE, model, null)).thenReturn(
				accommodationModel, accommodationModel);
		accommodationInteractiveMapPopulator.populate(accommodationModel,
				mapData);
		String expected = "accommodation";
		assertNotNull("mapdata", mapData);
		MapDataWrapper map = setDummyLocationType();
		

		assertEquals("map details", expected, map.getLocationType());
	}

	@SuppressWarnings("boxing")
	@Test
	public void testgetInteractiveMapForLocation() {
		MapDataWrapper mapData = new MapDataWrapper();
		CategoryModel category2 = categoryService
				.getCategoryForCode(LOCATIONCODE);
		when(categoryService.getCategoryForCode(LOCATIONCODE)).thenReturn(
				category2);
		LocationData locationdata = new LocationData();

		locationdata.setCode(ACCOMMODATIONCODE);
		Collection<CategoryData> supercategories = new ArrayList<CategoryData>();
		supercategories.add(locationdata);
		locationdata.setSupercategories(supercategories);
		assertEquals("getting location code",
				categoryService.getCategoryForCode(LOCATIONCODE), category2);
		locationdata.setName(COUNTRY);
		locationdata.setSupercategories(supercategories);
		List<LocationData> list = new ArrayList<LocationData>();
		assertNotNull("sublocations", locationdata.getSubLocations());
		mapData.setLocations(list);
		mapData.setTopLocationName(COUNTRY);
		mapData.setSuperCategoryNames(locationdata.getSuperCategoryNames());
		assertNotNull("locationdata", locationdata);
	}

	@Test
	public void testgetInspirationMap() {
		

		when(inspirationMapTabModel.getAccomodations()).thenReturn(
				accomLocationList);
		locationModelList = new ArrayList<LocationModel>();  
		inspirationMapViewDataAccom1 = new InspirationMapViewData();
		inspirationMapViewData1 = new InspirationMapViewData();
		inspirationMapPopulator.populate(locationModelList,
				inspirationMapViewDataLocation);
		assertNotNull("locationlist", accomLocationList);
		when(inspirationMapConverter.convert(locationModelList)).thenReturn(
				inspirationMapViewDataLocation);
		when(inspirationMapConverterForAccom.convert(accomLocationList))
				.thenReturn(inspirationMapViewDataAccom1);
		List<MarkerMapViewData> markerMapViewDataList = new ArrayList<MarkerMapViewData>();
		MarkerMapViewData marker = new MarkerMapViewData();
		marker.setCode(CODE);
		markerMapViewDataList.add(marker);
		inspirationMapViewDataAccom1
				.setMarkerMapViewDataList(markerMapViewDataList);
		inspirationMapViewDataLocation
				.setMarkerMapViewDataList(markerMapViewDataList);
		assertNotNull(inspirationMapViewDataAccom1.getMarkerMapViewDataList());
		assertNotNull(inspirationMapViewDataLocation.getMarkerMapViewDataList());
		List<MarkerMapViewData> inspirationMergedList = new ArrayList<MarkerMapViewData>();
		inspirationMergedList.add(marker);
		inspirationMapViewData1.setMarkerMapViewDataList(inspirationMergedList);
		inspirationMapViewData1
				.setMarkupListCount(inspirationMergedList.size());
		assertNotNull("inspiratinmapviewdata", inspirationMapViewData1);
		List<MarkerMapViewData> tempMarkerMap = inspirationMapViewData1
				.getMarkerMapViewDataList();
		tempMarkerMap.add(marker);
		assertNotNull("tempMarkerMap", tempMarkerMap);
		Map<String, MarkerMapViewData> markerMap = new HashMap<String, MarkerMapViewData>();
		inspirationMapTabModel.setCode(CODE);
		Iterator<String> itr = inspirationMapTabModel.getLocationCodes()
				.iterator();
		assertNotNull(itr);
		String code = itr.toString();
		List<MarkerMapViewData> inspirationMapOderedList = new ArrayList<MarkerMapViewData>();
		inspirationMapOderedList.add(markerMap.get(CODE));
		assertNotNull(inspirationMapOderedList);
		inspirationMapOderedList.add(markerMap.get(code.substring(2)));
		assertNotNull(inspirationMapOderedList);
		int visibleItems = (inspirationMapComponentModel.getVisibleItems() != null ? inspirationMapComponentModel
				.getVisibleItems().intValue() : 0);
		inspirationMapOderedList = inspirationMapOderedList.subList(0,
				visibleItems);
		assertNotNull(inspirationMapOderedList);
		inspirationMapViewData1
				.setMarkerMapViewDataList(inspirationMapOderedList);
		inspirationMapTabModel.setTitle(LOCATIONCODE);
		mediamodel.setMime("text");
		
		mediamodel.setURL("http://www.thomson.co.uk");
		inspirationMapViewData1.setTitle(inspirationMapTabModel.getTitle());
		assertNotNull(inspirationMapViewData1);
		inspirationMapComponentData
				.setInspirationMapViewDataList(inspirationMapViewDataList);
		assertNotNull(inspirationMapComponentData);
	}

	@SuppressWarnings("boxing")
	@Test
	public void testforVisibleitems() {
		int visibleItems = inspirationMapComponentModel.getVisibleItems()
				.intValue();
		InspirationMapComponentModel inspirationMapComponentModel3 = new InspirationMapComponentModel();
		inspirationMapComponentModel3.setVisibleItems(15);
		assertNotNull(inspirationMapComponentModel3);
		assertNotNull(visibleItems);

	}

	@SuppressWarnings("boxing")
	@Test
	public void testforvisibleItemsnull() {
		inspirationMapComponentModel.setVisibleItems(0);
		int visibleItems = inspirationMapComponentModel.getVisibleItems()
				.intValue();
		assertNotNull(visibleItems);
	}

	@SuppressWarnings("boxing")
	@Test
	public void testforvisibleitemsfornegative() {
		InspirationMapComponentModel inspirationMapComponentModel2 = new InspirationMapComponentModel();
		inspirationMapComponentModel2.setVisibleItems(0);
		int visibleItems = inspirationMapComponentModel.getVisibleItems();
		assertEquals(0, visibleItems);
	}

	@Test
	public void testforTabs() {

		List<AccommodationModel> accomLocationList1 = inspirationMapTabModel
				.getAccomodations();

		if (CollectionUtils.isNotEmpty(accomLocationList1)) {
			inspirationMapViewDataAccom2 = inspirationMapConverterForAccom
					.convert(accomLocationList1);
			inspirationMapPopulatorForAccom.populate(accomLocationList1,
					inspirationMapViewDataAccom2);
		}
		if (CollectionUtils.isNotEmpty(locationModelList)) {
			inspirationMapViewDataLocation2 = inspirationMapConverter
					.convert(locationModelList);
			inspirationMapPopulator.populate(locationModelList,
					inspirationMapViewDataLocation2);
		}
		assertNotNull(inspirationMapViewData2);

	}

	@Test
	public void testgetInteractiveMapForAccommodationnegative() {
		String accommodationCode2 = "001603";
		CatalogVersionModel model2 = new CatalogVersionModel();
		CatalogModel catalog2 = new CatalogModel();
		catalog2.setId("fc_catalog");
		model2.setCatalog(catalog2);
		model2.setVersion("staged");
		when(siteService.getCurrentCatalogVersion()).thenReturn(model2);
		AccommodationModel accommodationModel2 = accommodationService
				.getAccomodationByCodeAndCatalogVersion(accommodationCode2,
						siteService.getCurrentCatalogVersion(), null);
		assertNull(accommodationModel2);
		when(
				accommodationService.getAccomodationByCodeAndCatalogVersion(
						accommodationCode2, model2, null)).thenReturn(
				accommodationModel2, accommodationModel2);
		MapDataWrapper target = new MapDataWrapper();
		target.setTopLocationName(COUNTRY);
		accommodationInteractiveMapPopulator.populate(accommodationModel2,
				target);
		target.setLocationType("accommodation");
		target = setDummyLocationType();
		assertNotNull(target);
	}

	@Test
	public void testmergeInspirationMapDataforflase() {

		assertNotNull(inspirationMapViewData1);

	}
}
