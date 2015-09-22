/**
 *
 */
package uk.co.portaltech.tui.facades.impl;

import static org.junit.Assert.assertEquals;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.tui.components.model.PromoImageComponentModel;
import uk.co.portaltech.tui.facades.impl.DefaultLocationFacade;
import uk.co.portaltech.tui.facades.impl.DefaultPromoImageFacade;
import uk.co.portaltech.tui.web.view.data.LocationData;
import uk.co.portaltech.tui.web.view.data.PromoImageComponentData;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.web.common.enums.PositionType;
import uk.co.tui.web.common.enums.Width;


/**
 * @author veena.pn
 * 
 */
public class DefaultPromoImageFacadeTest
{


	@Mock
	private DefaultLocationFacade locationFacade;


	@InjectMocks
	private final DefaultPromoImageFacade promoImageFacade = new DefaultPromoImageFacade();


	@Mock
	private CMSSiteService siteService;


	@Mock
	private FlexibleSearchService flexibleSearchService;

	@Mock
	private CMSComponentService cmsComponentService;


	public static final String TEST_COMPONENT_UID = "WF_COM_900-1";
	public static final int VisibleItems = Integer.parseInt("5");
	List<String> locationcodes = new ArrayList<String>();

	
	private final TUILogUtils LOG = new TUILogUtils("DefaultPromoImageFacadeTest");


	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
	}

	private PromoImageComponentModel getDummyComponentModelWithEmptyComponent()
	{
		final PromoImageComponentModel heroImage = new PromoImageComponentModel();
		heroImage.setUid("");
		return heroImage;
	}


	@SuppressWarnings("boxing")
	private PromoImageComponentModel getDummyComponentModel()
	{
		final PromoImageComponentModel promoImage = new PromoImageComponentModel();
		promoImage.setUid(TEST_COMPONENT_UID);
		promoImage.setVisibleItems(VisibleItems);
		promoImage.setPromoImageDescription1("Short Description1");
		promoImage.setPromoImageDescription2("Short Description2");
		promoImage.setTitle("TRUE VIEW");
		promoImage.setPosition(PositionType.HOMEPAGE_FIRST);
		promoImage.setWidth(Width.HOMEPAGE_HALF);


		return promoImage;

	}



	private PromoImageComponentData getEmptyPromoImageComponentData()
	{
		final List<LocationData> locationdata = new ArrayList<LocationData>();
		final PromoImageComponentData promoImageComponentData = new PromoImageComponentData();
		promoImageComponentData.setLocationViewDataList(locationdata);
		return promoImageComponentData;
	}


	@SuppressWarnings("boxing")
	private PromoImageComponentData getPromoImageComponentData()
	{

		final PromoImageComponentData promoImageComponentData = new PromoImageComponentData();
		final List<LocationData> locationViewDataList = new ArrayList<LocationData>();
		promoImageComponentData.setVisibleItems(5);
		promoImageComponentData.setPromoImageDescription1("Short Description1");
		promoImageComponentData.setPromoImageDescription2("Short Description2");
		promoImageComponentData.setPromoImageTitle("TRUE VIEW");
		promoImageComponentData.setPromoImageUrl("testurl");
		locationViewDataList.add(getLocationData1());
		return promoImageComponentData;
	}

	private LocationData getLocationData1()
	{
		final LocationData locData1 = new LocationData();
		locData1.setCode("001544");
		locData1.setUrl("url1");

		return locData1;

	}

	private LocationData getLocationData2()
	{
		final LocationData locData2 = new LocationData();
		locData2.setCode("001696");
		locData2.setUrl("url2");

		return locData2;

	}

	private LocationData getLocationData3()
	{
		final LocationData locData3 = new LocationData();
		locData3.setCode("000457");
		locData3.setUrl("url3");

		return locData3;

	}


	private LocationData getLocationData4()
	{
		final LocationData locData4 = new LocationData();
		locData4.setCode("000338");
		locData4.setUrl("url4");

		return locData4;

	}

	private LocationData getLocationData5()
	{
		final LocationData locData5 = new LocationData();
		locData5.setCode("001965");
		locData5.setUrl("url5");
		return locData5;

	}



	@Test
	public void testPromoImageWithoutComponentModel()
	{
		try
		{
			Mockito.when(cmsComponentService.getAbstractCMSComponent("")).thenReturn(getDummyComponentModelWithEmptyComponent());
			final PromoImageComponentData viewData = promoImageFacade.getLocationData(getDummyComponentModelWithEmptyComponent());
			final PromoImageComponentData promoImageViewdata = getEmptyPromoImageComponentData();
			assertEquals(viewData.getVisibleItems(), promoImageViewdata.getVisibleItems());

		}
		catch (final CMSItemNotFoundException e)
		{
			LOG.error(" Could not find component with the name  component");

		}

	}


	@Test
	public void testPromoImageWithComponentModel()
	{
		try
		{
			Mockito.when(cmsComponentService.getAbstractCMSComponent(TEST_COMPONENT_UID)).thenReturn(getDummyComponentModel());

			Mockito.when(locationFacade.getLocationData("001544")).thenReturn(getLocationData1());
			Mockito.when(locationFacade.getLocationData("001696")).thenReturn(getLocationData2());
			Mockito.when(locationFacade.getLocationData("000457")).thenReturn(getLocationData3());
			Mockito.when(locationFacade.getLocationData("000338")).thenReturn(getLocationData4());
			Mockito.when(locationFacade.getLocationData("001965")).thenReturn(getLocationData5());

			final PromoImageComponentData viewData = promoImageFacade.getLocationData(getDummyComponentModel());
			final PromoImageComponentData promoImageViewdata = getPromoImageComponentData();
			assertEquals(viewData.getVisibleItems(), promoImageViewdata.getVisibleItems());
			assertEquals(viewData.getPromoImageDescription1(), promoImageViewdata.getPromoImageDescription1());
			assertEquals(viewData.getPromoImageDescription2(), promoImageViewdata.getPromoImageDescription2());
			assertEquals(viewData.getPromoImageTitle(), promoImageViewdata.getPromoImageTitle());


		}
		catch (final CMSItemNotFoundException e)
		{
			LOG.error(" Could not find component with the name  component");

		}

	}
}
