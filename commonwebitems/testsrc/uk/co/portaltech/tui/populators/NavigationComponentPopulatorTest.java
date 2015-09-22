/**
 * 
 */
package uk.co.portaltech.tui.populators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.platform.acceleratorcms.model.components.NavigationBarComponentModel;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.tui.components.model.NavigationComponentModel;
import uk.co.portaltech.tui.web.view.data.NavigationBarComponentViewData;
import uk.co.portaltech.tui.web.view.data.NavigationComponentViewData;
import uk.co.tui.web.common.enums.NavigationViewMode;


/**
 * @author vinodkumar.g
 * 
 */
public class NavigationComponentPopulatorTest
{

	@InjectMocks
	NavigationComponentPopulator navigationComponentPopulator = new NavigationComponentPopulator();

	@Mock
	private Converter<NavigationBarComponentModel, NavigationBarComponentViewData> navigationBarComponentConverter;

	@Mock
	private Populator<NavigationBarComponentModel, NavigationBarComponentViewData> navigationBarComponentPopulator;

	NavigationComponentModel sourceModel;
	NavigationComponentViewData targetData;


	@Before
	public void setUp() throws Exception
	{

		MockitoAnnotations.initMocks(this);

		sourceModel = new NavigationComponentModel();
		targetData = new NavigationComponentViewData();
		final NavigationBarComponentModel navigationBarComponentModel = new NavigationBarComponentModel();
		final List<NavigationBarComponentModel> navigationBarComponentModelList = new ArrayList<NavigationBarComponentModel>();


		navigationBarComponentModelList.add(navigationBarComponentModel);


		sourceModel.setNavigationBarComponents(navigationBarComponentModelList);
		sourceModel.setNavigationViewMode(NavigationViewMode.ACCOMMODATIONNAVIGATION);

	}

	/**
	 * Test method for
	 * {@link uk.co.portaltech.tui.populators.NavigationComponentPopulator#populate(uk.co.portaltech.tui.components.model.NavigationComponentModel, uk.co.portaltech.tui.web.view.data.NavigationComponentViewData)}
	 * .
	 */
	@Test
	public void testPopulate()
	{

		final NavigationBarComponentViewData navigationBarComponentViewData = new NavigationBarComponentViewData();

		Mockito.when(navigationBarComponentConverter.convert(Mockito.any(NavigationBarComponentModel.class))).thenReturn(
				navigationBarComponentViewData);
		Mockito.doNothing().when(navigationBarComponentPopulator)
				.populate(Mockito.any(NavigationBarComponentModel.class), Mockito.any(NavigationBarComponentViewData.class));

		assertNotNull(sourceModel);
		assertNotNull(targetData);

		navigationComponentPopulator.populate(sourceModel, targetData);

		assertEquals(NavigationViewMode.ACCOMMODATIONNAVIGATION, targetData.getNavigationViewMode());
		assertEquals(1, targetData.getNavigationBarComponents().size());

	}



}
