/**
 *
 */
package uk.co.portaltech.tui.populators;

import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import uk.co.portaltech.travel.enums.SearchResultType;
import uk.co.portaltech.tui.resolvers.CruiseProductUrlResolver;
import uk.co.portaltech.tui.services.pojo.CruiseNavigation;
import uk.co.portaltech.tui.services.pojo.NavigationComponent;
import uk.co.portaltech.tui.web.view.data.GlobalNavigationComponentViewData;
import uk.co.tui.cruise.mainstream.data.CruiseAreaData;
import uk.co.tui.cruise.mainstream.data.LocationData;


/**
 * @author akshatha.bb
 *
 */
public class CRdestinationNavigationPopulatorTest
{

	@InjectMocks
	private final CRdestinationNavigationPopulator cRdestinationNavigationPopulator = new CRdestinationNavigationPopulator();

	@Mock
	private CruiseProductUrlResolver cruiseProductUrlResolver;

	@Spy
	private final NavigationComponent navigationComponent = new NavigationComponent();

	@Spy
	private final GlobalNavigationComponentViewData globalNavigationComponentViewData = new GlobalNavigationComponentViewData();

	private final CruiseNavigation cruiseNavigation = new CruiseNavigation();

	private final Map<String, String> megaMenuConfigValues = new HashMap<String, String>();

	private final Set<LocationData> locationSet = new HashSet<LocationData>();

	private final Map<CruiseAreaData, Set<LocationData>> entry = new HashMap<CruiseAreaData, Set<LocationData>>();

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		megaMenuConfigValues.put("keyOne", "valueOne");
		cruiseNavigation.setMegaMenuConfigValues(megaMenuConfigValues);

		when(cruiseProductUrlResolver.resolve(Mockito.anyString(), Mockito.any(SearchResultType.class))).thenReturn("url");


	}

	@Test
	public void populateTest()
	{
		inputTestOne();
		cRdestinationNavigationPopulator.populate(navigationComponent, globalNavigationComponentViewData);

	}


	private void inputTestOne()
	{

		final LocationData locOne = new LocationData();
		locOne.setName("Spain");
		locOne.setCode("CR1");
		final LocationData locTwo = new LocationData();
		locTwo.setName("Italy");
		locTwo.setCode("CR2");
		final LocationData locThree = new LocationData();
		locThree.setName("Spain");
		locThree.setCode("CR3");
		locationSet.add(locOne);
		locationSet.add(locTwo);
		locationSet.add(locThree);
		final CruiseAreaData cruiseAreaOne = new CruiseAreaData();
		cruiseAreaOne.setCode("SPA");
		cruiseAreaOne.setName("Europe");
		entry.put(cruiseAreaOne, locationSet);
		cruiseNavigation.setCruiseAreaMap(entry);
		navigationComponent.setIntraSiteNavigation(cruiseNavigation);
	}


}
