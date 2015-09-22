/**
 *
 */
package uk.co.portaltech.tui.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import uk.co.portaltech.travel.cruise.services.CruiseAreaService;
import uk.co.portaltech.travel.cruise.services.ItineraryService;
import uk.co.portaltech.travel.cruise.services.location.MainStreamLocationService;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.services.config.TUIConfigService;
import uk.co.portaltech.tui.services.pojo.IntrasiteNavigation;
import uk.co.tui.cr.components.model.CruiseHeaderNavigationComponentModel;
import uk.co.tui.cruise.mainstream.data.CruiseAreaData;
import uk.co.tui.cruise.mainstream.data.LocationData;


/**
 * @author akshatha.bb
 *
 */
public class CRIntrasiteNavigationServiceTest
{
	@InjectMocks
	private final CRIntrasiteNavigationService crIntrasiteNavigationService = new CRIntrasiteNavigationService();

	@Mock
	private MainStreamLocationService mainStreamLocationService;

	@Mock
	private CMSSiteService cmsSiteService;

	@Mock
	private TUIConfigService tuiConfigService;

	@Mock
	private ItineraryService itineraryService;

	@Mock
	private CruiseAreaService cruiseAreaService;

	@Spy
	private final CruiseHeaderNavigationComponentModel componentModel = new CruiseHeaderNavigationComponentModel();

	private final Map<String, String> tuiConfigModelsMap = new LinkedHashMap<String, String>();

	private final CatalogVersionModel catalogVersionModel = new CatalogVersionModel();

	private final List<String> pocCodes = new ArrayList<String>();

	private Boolean flag = false;


	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		pocCodes.add("pocCodeOne");
		componentModel.setPocCodes(pocCodes);
	}


	@Test
	public void getIntraSiteNavigationTest()
	{
		mockObjects();
		final IntrasiteNavigation result = crIntrasiteNavigationService.getIntraSiteNavigation(componentModel);
		assertEquals("CR", result.getSite());

	}

	@Test
	public void getIntraSiteNavigationTestTwo()
	{
		mockObjects();
		componentModel.getPocCodes().clear();
		final IntrasiteNavigation result = crIntrasiteNavigationService.getIntraSiteNavigation(componentModel);
		assertEquals("CR", result.getSite());

	}

	@Test
	public void getIntraSiteNavigationTestThree()
	{
		flag = true;
		mockObjects();
		final IntrasiteNavigation result = crIntrasiteNavigationService.getIntraSiteNavigation(componentModel);
		assertEquals("CR", result.getSite());

	}



	private void mockObjects()
	{
		tuiConfigModelsMap.put("tuiConfigOne", "valueOne");
		when(tuiConfigService.getConfigValuesMap(Mockito.anyList())).thenReturn(tuiConfigModelsMap);
		when(cmsSiteService.getCurrentCatalogVersion()).thenReturn(catalogVersionModel);
		doReturn(flag).when(itineraryService).isValidPortOfCall(Mockito.anyString(), Mockito.any(CatalogVersionModel.class));
		final Map<CruiseAreaData, Set<LocationData>> cruiseAreaMap = null;
		when(cruiseAreaService.getAllCRAreaWithCountries(Mockito.any(CatalogVersionModel.class))).thenReturn(cruiseAreaMap);
		final List<LocationModel> locationModelMap = null;
		when(mainStreamLocationService.getAllPOCByCodes(Mockito.anyList(), Mockito.any(CatalogVersionModel.class))).thenReturn(
				locationModelMap);

	}

}
