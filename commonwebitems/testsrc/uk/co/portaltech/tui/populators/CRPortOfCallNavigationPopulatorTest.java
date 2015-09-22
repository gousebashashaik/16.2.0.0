/**
 *
 */
package uk.co.portaltech.tui.populators;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import uk.co.portaltech.travel.enums.LocationType;
import uk.co.portaltech.travel.enums.SearchResultType;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.tui.resolvers.CruiseProductUrlResolver;
import uk.co.portaltech.tui.services.pojo.CruiseNavigation;
import uk.co.portaltech.tui.services.pojo.NavigationComponent;
import uk.co.portaltech.tui.web.view.data.GlobalNavigationComponentViewData;


/**
 * @author akshatha.bb
 *
 */
public class CRPortOfCallNavigationPopulatorTest
{

	@InjectMocks
	private final CRPortOfCallNavigationPopulator crPortOfCallNavigationPopulator = new CRPortOfCallNavigationPopulator();

	@Mock
	private CruiseProductUrlResolver cruiseProductUrlResolver;

	@Spy
	private final NavigationComponent navigationComponent = new NavigationComponent();

	@Spy
	private final GlobalNavigationComponentViewData globalNavigationComponentViewData = new GlobalNavigationComponentViewData();

	private final CruiseNavigation cruiseNavigation = new CruiseNavigation();

	private final List<LocationModel> locationModels = new ArrayList<LocationModel>();

	private final Map<String, String> megaMenuConfigValues = new HashMap<String, String>();

	@Spy
	private final LocationModel locOne = new LocationModel();

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		cruiseNavigation.setMegaMenuConfigValues(megaMenuConfigValues);
		locOne.setType(LocationType.COUNTRY);
		locationModels.add(locOne);
		cruiseNavigation.setLocationModels(locationModels);
		navigationComponent.setIntraSiteNavigation(cruiseNavigation);

		when(cruiseProductUrlResolver.resolve(Mockito.anyString(), Mockito.any(SearchResultType.class))).thenReturn("url");
	}

	@Test
	public void populateTestOne()
	{
		doReturn("Spain").when(locOne).getName();
		crPortOfCallNavigationPopulator.populate(navigationComponent, globalNavigationComponentViewData);
	}

}
