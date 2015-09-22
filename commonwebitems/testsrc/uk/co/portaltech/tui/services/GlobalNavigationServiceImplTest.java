/**
 *
 */
package uk.co.portaltech.tui.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import uk.co.portaltech.tui.services.pojo.GlobalNavigation;
import uk.co.portaltech.tui.services.pojo.IntrasiteNavigation;
import uk.co.tui.cr.components.model.GlobalNavigationComponentModel;


/**
 * @author akshatha.bb
 *
 */
public class GlobalNavigationServiceImplTest
{
	@InjectMocks
	private final GlobalNavigationServiceImpl globalNavigationServiceImpl = new GlobalNavigationServiceImpl();

	@Mock
	private ConfigurationService configurationService;

	@Mock
	private FlexibleSearchService flexibleSearchService;

	@Mock
	private Map<String, IntrasiteNavigationService> intrasiteNavigationService;

	@Mock
	private IntrasiteNavigationService intrasiteNavigationServiceObject;

	@Mock
	private Configuration configuration;

	@Spy
	private final GlobalNavigationComponentModel componentModel = new GlobalNavigationComponentModel();

	@Spy
	private final SimpleCMSComponentModel simpleCMSComponentModel = new SimpleCMSComponentModel();

	@Spy
	private final IntrasiteNavigation intrasiteNavigation = new IntrasiteNavigation();

	private final CatalogVersionModel catalogVersionModel = new CatalogVersionModel();

	private final CatalogModel catalogModel = new CatalogModel();

	private final Map<String, String> globalHeaderNavigationLinks = new HashMap<String, String>();


	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		componentModel.setGlobalNavigation(simpleCMSComponentModel);
		catalogModel.setId("CatalogId");
		catalogVersionModel.setCatalog(catalogModel);
		componentModel.setCatalogVersion(catalogVersionModel);
		globalHeaderNavigationLinks.put("keyOne", "valueOne");
		componentModel.setGlobalHeaderNavigationLinks(globalHeaderNavigationLinks);

	}

	@Test
	public void getGlobalNavigationsTest()
	{
		when(configurationService.getConfiguration()).thenReturn(configuration);
		when(configuration.getString(Mockito.anyString())).thenReturn("a:b,c:d");
		when(flexibleSearchService.searchUnique(Mockito.any(FlexibleSearchQuery.class))).thenReturn(simpleCMSComponentModel);
		mockObject();
		final GlobalNavigation result = globalNavigationServiceImpl.getGlobalNavigations("CR", componentModel);
		assertEquals("valueOne", result.getMainNavigations().get("keyOne"));
	}

	@Test
	public void getGlobalNavigationsTestTwo()
	{
		when(configurationService.getConfiguration()).thenReturn(configuration);
		when(configuration.getString(Mockito.anyString())).thenReturn(null);
		mockObject();
		final GlobalNavigation result = globalNavigationServiceImpl.getGlobalNavigations("CR", componentModel);
		assertEquals("valueOne", result.getMainNavigations().get("keyOne"));
	}

	/**
	 *
	 */
	private void mockObject()
	{

		when(intrasiteNavigationService.get(Mockito.anyString())).thenReturn(intrasiteNavigationServiceObject);
		when(intrasiteNavigationServiceObject.getIntraSiteNavigation(Mockito.any(SimpleCMSComponentModel.class))).thenReturn(
				intrasiteNavigation);
	}
}
