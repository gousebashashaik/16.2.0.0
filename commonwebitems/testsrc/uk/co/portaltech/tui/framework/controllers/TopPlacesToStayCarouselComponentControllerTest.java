/**
 * 
 */
package uk.co.portaltech.tui.framework.controllers;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.tui.components.model.TopPlacesCarouselModel;
import uk.co.portaltech.tui.components.model.TopPlacesToStayCarouselComponentModel;
import uk.co.portaltech.tui.exception.NoSuchComponentException;
import uk.co.portaltech.tui.facades.ComponentFacade;
import uk.co.portaltech.tui.resolvers.TUIUrlResolver;
import uk.co.portaltech.tui.web.view.data.AccommodationCarouselViewData;
import uk.co.portaltech.tui.web.view.data.LocationCarouselViewData;
import uk.co.portaltech.tui.web.view.data.LocationData;
import uk.co.portaltech.tui.web.view.data.wrapper.TopPlacesWrapper;
import uk.co.tui.async.logging.TUILogUtils;

import com.enterprisedt.util.debug.Logger;


/**
 * @author gopinath.n
 * 
 */
public class TopPlacesToStayCarouselComponentControllerTest
{

	/**
	 * @throws java.lang.Exception
	 */


	@Mock
	private ComponentFacade componentFacade;

	private TopPlacesToStayCarouselComponentModel component;

	@Mock
	private CategoryModel categoryModel;

	@Mock
	private TopPlacesWrapper wrapper;

	/*
	 * @Mock private ViewSelectorUtil selector;
	 */

	@Mock
	private TUIUrlResolver<CategoryModel> tuiCategoryModelUrlResolver;


	@Mock
	private TUIUrlResolver<ProductModel> tuiProductModelUrlResolver;

	@Mock
	private LocationData locationData;

	@Mock
	private AccommodationCarouselViewData accommodationsData;

	@Mock
	private LocationCarouselViewData locationsData;

	private TopPlacesCarouselModel topPlacesCarouselModel;

	private List<TopPlacesCarouselModel> topPlacesCarouselModelList;

	
	private final TUILogUtils LOG = new TUILogUtils("TopPlacesToStayCarouselComponentControllerTest");


	private static final String PLACESTOGO = "places-to-go";
	private static final String VIEW_AllURL = "";


	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		locationData = new LocationData();
		topPlacesCarouselModel = new TopPlacesCarouselModel();
		component = new TopPlacesToStayCarouselComponentModel();
		dummyDataForTopPlacesCarouselModelComponent();
		dummyDataforTopPlaces();
	}

	private void dummyDataForTopPlacesCarouselModelComponent()
	{
		topPlacesCarouselModel.setUid("WF_COM_052-1");
		topPlacesCarouselModel.setName("Top x Places to Stay Carousel");
		
		

		component.setName("Top x Places to Stay Carousel");
		component.setOnlyOneRestrictionMustApply(true);
		component.setUid("WF_COM_052-1");
		

	}

	private void dummyDataforTopPlaces()
	{
		locationData.setCode("ENDECA_TOP_PLACES_TO_STAY");
		locationData.setUrl("http://www.firstchoice.co.uk/");
		locationData.setName("Top x Places to Stay Carousel");
		locationData.setPriceFrom("5000");
		wrapper.setLocation(locationData);

	}


	@Test
	public void testViewTabbedComponent()
	{
		final String componentUID = "WF_COM_052-1";
		try
		{
			Mockito.when(componentFacade.getComponent(componentUID)).thenReturn(component);
		}
		catch (final NoSuchComponentException e)
		{
			LOG.error("No such component available");
			e.printStackTrace();
		}
		Mockito.when(
				componentFacade.getTopPlacesToStayData(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
						Mockito.any(TopPlacesToStayCarouselComponentModel.class))).thenReturn(wrapper);
		final String viewAllURL = "";

		tuiCategoryModelUrlResolver.setOverrideSubPageType(PLACESTOGO);
		

		tuiProductModelUrlResolver.setOverrideSubPageType(PLACESTOGO);
		Assert.assertEquals("WF_COM_052-1", component.getUid());
		Assert.assertEquals("Top x Places to Stay Carousel", component.getName());

	}
}
