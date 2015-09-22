/**
 *
 */
package uk.co.portaltech.tui.services;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.converters.BaynoteRESTConverter;
import uk.co.portaltech.tui.thirdparty.baynote.client.Attribute;
import uk.co.portaltech.tui.thirdparty.baynote.client.Guides;
import uk.co.portaltech.tui.thirdparty.baynote.client.Result;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.MediaViewData;
import uk.co.portaltech.tui.web.view.data.ProductRangeViewData;
import uk.co.portaltech.tui.web.view.data.RecommendationsData;


/**
 * @author geethanjali.k
 *
 */
@UnitTest
public class RecommendationsServiceTest
{
	@InjectMocks
	private final RecommendationsService recommendationsService = new RecommendationsService();

	@Mock
	BaynoteRESTConverter baynoteRESTConverter = new BaynoteRESTConverter();

	@Mock
	RecommendationsRESTConnector recommendationsRESTConnector;

	@Mock
	CatalogVersionModel catalogVersion;

	@Mock
	CategoryService categoryService;

	@Mock
	private FeatureService featureService;

	@Mock
	ConfigurationService configurationService;

	@Mock
	Guides guides;

	@Mock
	Result result;

	@Mock
	Attribute attributes;

	@Mock
	ProductRangeViewData productRangeViewData;

	@Mock
	MediaViewData medaViewData;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Test method for
	 * {@link uk.co.portaltech.tui.facades.impl.RecommendationsFacadeImpl#getRecommendations(java.lang.String, java.lang.String, java.util.Map)}
	 * .
	 */
	@Test
	public void testHomeGetRecommendations()
	{
		final String pageType = "HomeRec";
		final String brandtype = "FC";
		final String attributeList = "BRAND,NAME,COUNTRY,RESORT,T_RATING_AF0104,FEATURED_IMAGE,DEEPLINK_URL,HOTEL_ID,PRODUCT_RANGE";
		final Map<String, String> urlmap = new HashMap<String, String>();
		urlmap.put("cn", "tui");
		urlmap.put("cc", "firstchoice");
		urlmap.put("guide", pageType);
		urlmap.put("attrList", attributeList);

		urlmap.put(
				"bnTrail",
				"[%22http://www.firstchoice.co.uk/holiday/bookaccommodation?productCode=029575%22,%22http://www.firstchoice.co.uk/holiday/bookaccommodation?productCode=013885%22]");

		urlmap.put("userId", "6924717449449701377");

		urlmap.put("v", "1");

		final RecommendationsData recommendationsData = new RecommendationsData();

		final List<AccommodationViewData> accomodationDatas = new ArrayList<AccommodationViewData>();
		final AccommodationViewData element = new AccommodationViewData();
		element.setBaynoteHotelId("002644");
		element.setResortName("Puerto de Santiago");
		element.setName("Hotel Barceló Santiago");
		element.setDestinationName("Spain");
		accomodationDatas.add(element);
		recommendationsData.setAccomodationDatas(accomodationDatas);
		final String xmlResponse = "";

		try
		{
			Mockito.when(recommendationsRESTConnector.getRESTResponse(Mockito.anyString(), Mockito.anyString(), Mockito.anyMap()))
					.thenReturn(xmlResponse);
			Mockito.when(baynoteRESTConverter.convert(Mockito.anyString(), Mockito.anyString())).thenReturn(recommendationsData);
		}
		catch (final IOException e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
		}

		final RecommendationsData result = recommendationsService.getRecommendations(pageType, brandtype);

		Assert.assertEquals("002644", result.getAccomodationDatas().get(0).getBaynoteHotelId());
		Assert.assertEquals("Puerto de Santiago", result.getAccomodationDatas().get(0).getResortName());
		Assert.assertEquals("Hotel Barceló Santiago", result.getAccomodationDatas().get(0).getName());
		Assert.assertEquals("Spain", result.getAccomodationDatas().get(0).getDestinationName());

	}

	@Test
	public void testBrowseGetRecommendations()
	{
		final String pageType = "AccomodationRec";
		final String brandtype = "FC";
		final String attributeList = "BRAND,NAME,COUNTRY,RESORT,T_RATING_AF0104,FEATURED_IMAGE,DEEPLINK_URL,HOTEL_ID,PRODUCT_RANGE";
		final Map<String, String> urlmap = new HashMap<String, String>();
		urlmap.put("cn", "tui");
		urlmap.put("cc", "firstchoice");

		urlmap.put("attrList", attributeList);
		urlmap.put("elementIds", "BN_PersonalizedAccomodations,BN_AccomodationRec");
		urlmap.put("bnTrail", "[http://www.firstchoice.co.uk/holiday/bookaccommodation?productCode=029575" + ","
				+ "http://www.firstchoice.co.uk/holiday/bookaccommodation?productCode=013885" + "]");
		urlmap.put("condition", "d%26g%26s");
		urlmap.put("userId", "6924717449449701377");
		urlmap.put("deduplicate", "true");
		urlmap.put("v", "1");

		final RecommendationsData recommendationsData = new RecommendationsData();

		final List<AccommodationViewData> accomodationDatas = new ArrayList<AccommodationViewData>();
		final AccommodationViewData element = new AccommodationViewData();
		element.setBaynoteHotelId("002644");
		element.setResortName("Puerto de Santiago");
		element.setName("Hotel Barceló Santiago");
		element.setDestinationName("Spain");
		final Map<String, String> baynoteTrackingMap = new HashMap<String, String>();
		baynoteTrackingMap.put("baynote_subTitle", "People who looked at this hotel also viewed...");
		element.setBaynoteTrackingMap(baynoteTrackingMap);
		accomodationDatas.add(element);
		recommendationsData.setAccomodationDatas(accomodationDatas);
		final String xmlResponse = "";

		try
		{
			Mockito.when(recommendationsRESTConnector.getRESTResponse(Mockito.anyString(), Mockito.anyString(), Mockito.anyMap()))
					.thenReturn(xmlResponse);
			Mockito.when(baynoteRESTConverter.convert(Mockito.anyString(), Mockito.anyString())).thenReturn(recommendationsData);
		}
		catch (final IOException e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
		}

		final RecommendationsData result = recommendationsService.getRecommendations(pageType, brandtype);

		Assert.assertEquals("002644", result.getAccomodationDatas().get(0).getBaynoteHotelId());
		Assert.assertEquals("Puerto de Santiago", result.getAccomodationDatas().get(0).getResortName());
		Assert.assertEquals("Hotel Barceló Santiago", result.getAccomodationDatas().get(0).getName());
		Assert.assertEquals("Spain", result.getAccomodationDatas().get(0).getDestinationName());
		Assert.assertEquals("People who looked at this hotel also viewed...", result.getAccomodationDatas().get(0)
				.getBaynoteTrackingMap().get("baynote_subTitle"));
	}
}
