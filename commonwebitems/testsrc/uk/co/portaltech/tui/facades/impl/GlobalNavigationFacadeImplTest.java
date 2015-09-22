/**
 *
 */
package uk.co.portaltech.tui.facades.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import de.hybris.platform.commercefacades.converter.ConfigurablePopulator;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import uk.co.portaltech.tui.converters.GlobalNavigationOption;
import uk.co.portaltech.tui.facades.impl.GlobalNavigationFacadeImpl;
import uk.co.portaltech.tui.services.GlobalNavigationService;
import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.portaltech.tui.services.pojo.GlobalNavigation;
import uk.co.portaltech.tui.services.pojo.IntrasiteNavigation;
import uk.co.portaltech.tui.services.pojo.NavigationComponent;
import uk.co.portaltech.tui.web.view.data.GlobalNavigationComponentViewData;
import uk.co.tui.cr.components.model.GlobalNavigationComponentModel;


/**
 * @author akshatha.bb
 *
 */
public class GlobalNavigationFacadeImplTest
{
	@InjectMocks
	private final GlobalNavigationFacadeImpl globalNavigationFacadeImpl = new GlobalNavigationFacadeImpl();

	@Mock
	private TuiUtilityService tuiUtilityService;

	@Mock
	private GlobalNavigationService globalNavigationService;

	@Mock
	private ConfigurablePopulator<NavigationComponent, GlobalNavigationComponentViewData, GlobalNavigationOption> defaultCrGlobalNavigationPopulator;

	@Spy
	private final GlobalNavigationComponentModel globalNavigationComponent = new GlobalNavigationComponentModel();

	private final GlobalNavigation globalNavigation = new GlobalNavigation();

	private final List<NavigationComponent> navigationComponents = new ArrayList<NavigationComponent>();

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		final NavigationComponent navigationComponentOne = new NavigationComponent();
		final IntrasiteNavigation intrasiteNavigation = new IntrasiteNavigation();
		intrasiteNavigation.setSite("CR");
		navigationComponentOne.setIntraSiteNavigation(intrasiteNavigation);
		navigationComponents.add(navigationComponentOne);
		globalNavigation.setNavigationComponents(navigationComponents);
	}

	@Test
	public void getGlobalNavigationViewDataTest()
	{
		mockObjects();
		final GlobalNavigationComponentViewData result = globalNavigationFacadeImpl.getGlobalNavigationViewData(
				globalNavigationComponent, ".pageId");
		assertEquals("CR", result.getSiteBrand());

	}

	@Test
	public void getGlobalNavigationViewDataTestTwo()
	{
		globalNavigation.getNavigationComponents().clear();
		mockObjects();
		final GlobalNavigationComponentViewData result = globalNavigationFacadeImpl.getGlobalNavigationViewData(
				globalNavigationComponent, ".pageId");
		assertEquals("CR", result.getSiteBrand());

	}


	private void mockObjects()
	{
		when(tuiUtilityService.getSiteBrand()).thenReturn("CR");
		when(globalNavigationService.getGlobalNavigations(Mockito.anyString(), Mockito.any(GlobalNavigationComponentModel.class)))
				.thenReturn(globalNavigation);
		doNothing().when(defaultCrGlobalNavigationPopulator).populate(Mockito.any(NavigationComponent.class),
				Mockito.any(GlobalNavigationComponentViewData.class), Mockito.anyCollection());
	}
}
