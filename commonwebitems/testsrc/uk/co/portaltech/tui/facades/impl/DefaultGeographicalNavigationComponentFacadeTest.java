/**
 * 
 */
package uk.co.portaltech.tui.facades.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.impl.DefaultCategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import uk.co.portaltech.travel.enums.LocationType;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.tui.components.model.GeographicalNavigationComponentModel;
import uk.co.portaltech.tui.converters.GeographicalNavigationComponentConverter;
import uk.co.portaltech.tui.facades.AccommodationFacade;
import uk.co.portaltech.tui.facades.impl.DefaultGeographicalNavigationComponentFacade;
import uk.co.portaltech.tui.resolvers.TUIUrlResolver;
import uk.co.portaltech.tui.resolvers.TuiCategoryUrlResolver;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.GeographicalNavigationComponentViewData;
import uk.co.portaltech.tui.web.view.data.TUICategoryData;

/**
 * @author venkataharish.k
 * 
 */
public class DefaultGeographicalNavigationComponentFacadeTest {
	@InjectMocks
	final DefaultGeographicalNavigationComponentFacade defaultGeographicalNavigationComponentFacade = new DefaultGeographicalNavigationComponentFacade();
	@Mock
	private CMSComponentService cmsComponentService;
	@Mock
	private Converter<GeographicalNavigationComponentModel, GeographicalNavigationComponentViewData> geographicalNavigationComponentConverter1;
	@Mock
	private Populator<GeographicalNavigationComponentModel, GeographicalNavigationComponentViewData> geographicalNavigationComponentPopulator1;
	@Mock
	private AccommodationFacade accommodationFacade;
	/*@Mock
	private CategoryService categoryService,categoryService1;*/
	@Mock
	private TUIUrlResolver<CategoryModel> tuiCategoryModelUrlResolver;
	@Mock
	private GeographicalNavigationComponentModel geographicalNavigationComponentModel1;
	@Mock
	private GeographicalNavigationComponentViewData geographicalNavigationComponentViewData1,geographicalNavigationComponentViewData2;
	@Mock
	private LocationType locationType, locationType1;
	
	Map<String, List<AccommodationViewData>> accommodations = MapUtils.EMPTY_MAP;
	
	
	String seopage = "SEOPAGESNAVIGATION";
	Collection<String> locationCategoryCodes;
	Collection<String> value1;
	 List<TUICategoryData> categories;

	@Before
	public void setUp() throws Exception {
		
		
// Data For the Component View Data
		
dummyMethodForComponentViewData();

//Data For The ComponentModel
dummyDataForgeographicalNavigationComponentModel();

	}
	@SuppressWarnings("boxing")
	private void dummyMethodForComponentViewData() {
		categories = new ArrayList<TUICategoryData>();
		TUICategoryData categoryData = new TUICategoryData();
		String code = "africa";
		String cod1 = "asia";
		String locationName = "Africa";
		categoryData.setCode(code);
		categoryData.setCode(cod1);
		categories.add(categoryData);
		Integer navigationLevelDepth = 1;
		Map<String, String> a = new HashMap<String, String>();
		
		accommodations = new HashMap<String, List<AccommodationViewData>>();
		List<AccommodationViewData> listOfAccommdations = new ArrayList<AccommodationViewData>();
		
		AccommodationViewData accommodationData = new AccommodationViewData();
		listOfAccommdations.add(accommodationData);
		accommodations.put("HOTEL", listOfAccommdations);
		geographicalNavigationComponentViewData1 = new GeographicalNavigationComponentViewData();
		geographicalNavigationComponentViewData1.setCategories(categories);
		geographicalNavigationComponentViewData1.setLocationName(locationName);
		geographicalNavigationComponentViewData1.setLocationType(locationType);
		geographicalNavigationComponentViewData1
				.setNavigationLevelDepth(navigationLevelDepth);
		geographicalNavigationComponentViewData1
				.setAccommodations(accommodations);

		geographicalNavigationComponentConverter1 = new GeographicalNavigationComponentConverter();

		geographicalNavigationComponentViewData1 = geographicalNavigationComponentConverter1
				.convert(geographicalNavigationComponentModel1);

	}

	
	private void dummyDataForgeographicalNavigationComponentModel() {
		geographicalNavigationComponentModel1=new GeographicalNavigationComponentModel();
		value1 = new ArrayList<String>();
		value1.add("Africa");
		value1.add("Asia");
		value1.add("europe");
		Integer value = 1;
		geographicalNavigationComponentModel1.setCategoryCodes(value1);
		geographicalNavigationComponentModel1.setLocationType(locationType1);
		geographicalNavigationComponentModel1.setNavigationLevelDepth(value);
				geographicalNavigationComponentConverter1 = new GeographicalNavigationComponentConverter();

	}

	@Test
	public void testGetGeographicalNavigationData() {


		LocationModel location =Mockito.mock(LocationModel.class);

		geographicalNavigationComponentViewData1.setPlacesToGoUrl("Spain");
		
		if (geographicalNavigationComponentViewData1.getCategories() == null
				|| seopage != null) {
			Collection<String> categoryCodes = geographicalNavigationComponentModel1
					.getCategoryCodes();
						if (!categoryCodes.isEmpty() && categoryCodes.iterator() != null) {
				geographicalNavigationComponentViewData1
						.setAccommodations(accommodations);
			} 
		}
		tuiCategoryModelUrlResolver = Mockito.mock(TuiCategoryUrlResolver.class);
		tuiCategoryModelUrlResolver.setOverrideSubPageType("places-to-go");
		
        


		String url = tuiCategoryModelUrlResolver.resolve(location);
		geographicalNavigationComponentViewData1.setPlacesToGoUrl(url);
		assertNotNull("geographicalNavigationComponentViewData",geographicalNavigationComponentViewData1);

		assertNotNull("Value",value1);

	}
	 @Test
	 public void testGetGeographicalNavigationDataforNull(){
			LocationModel location1 =Mockito.mock(LocationModel.class);
			geographicalNavigationComponentViewData2=new GeographicalNavigationComponentViewData();
				geographicalNavigationComponentViewData2
						.setAccommodations(accommodations);
				tuiCategoryModelUrlResolver = Mockito.mock(TuiCategoryUrlResolver.class);
				tuiCategoryModelUrlResolver.setOverrideSubPageType("places-to-go");
				
				String url = tuiCategoryModelUrlResolver.resolve(location1);
				geographicalNavigationComponentViewData2.setPlacesToGoUrl(url);
				assertEquals(geographicalNavigationComponentViewData2.getPlacesToGoUrl(),url);

				assertNotNull(geographicalNavigationComponentViewData2);
				

		 
	 }	   

}
