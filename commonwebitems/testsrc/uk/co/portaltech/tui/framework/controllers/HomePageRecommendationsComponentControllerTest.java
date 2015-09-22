/**
 * 
 */
package uk.co.portaltech.tui.framework.controllers;

import de.hybris.bootstrap.annotations.UnitTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.tui.components.model.CrossSellCarouselComponentModel;
import uk.co.portaltech.tui.components.model.HomePageRecommendationsComponentModel;
import uk.co.portaltech.tui.exception.NoSuchComponentException;
import uk.co.portaltech.tui.facades.ComponentFacade;
import uk.co.portaltech.tui.facades.RecommendationsFacade;
import uk.co.portaltech.tui.web.view.data.AccommodationCarouselViewData;
import uk.co.portaltech.tui.web.view.data.CrossSellWrapper;
import uk.co.portaltech.tui.web.view.data.LocationCarouselViewData;
import uk.co.portaltech.tui.web.view.data.RecommendationsData;

import com.enterprisedt.util.debug.Logger;

/**
 * @author geethanjali.k
 *
 */
@UnitTest
public class HomePageRecommendationsComponentControllerTest
{
	private HomePageRecommendationsComponentModel homepageRecComponent;

	@Mock
	private ComponentFacade componentFacade;
	
	@Mock
	private RecommendationsFacade recommendationsFacade;

	private RecommendationsData recommendationsData;

	private final Logger LOG = Logger.getLogger(HomePageRecommendationsComponentControllerTest.class);

	String pageType="HomeRec";
	String brandtype="firstchoice";
	String attributeList="BRAND,NAME,COUNTRY,RESORT,T_RATING_AF0104,FEATURED_IMAGE,DEEPLINK_URL,HOTEL_ID,PRODUCT_RANGE";
	Map<String,String> urlmap= new HashMap<String,String>();
	
	@Before
	public void setUp()
	{
		
		MockitoAnnotations.initMocks(this);
		homepageRecComponent = new HomePageRecommendationsComponentModel();
		urlmap.put("cn","tui");
  	   urlmap.put("cc","firstchoice");
 	   urlmap.put("guide",pageType);
 	   urlmap.put("attrList",attributeList);
 	   urlmap.put("bnTrail", "[%22http://www.firstchoice.co.uk/holiday/bookaccommodation?productCode=029575%22,%22http://www.firstchoice.co.uk/holiday/bookaccommodation?productCode=013885%22]");
 	   urlmap.put("userId","6924717449449701377");
 	   urlmap.put("v","1");
		dummyDataforHomePageRecommmendationsComponent();
		
		
	}

	public void dummyDataforHomePageRecommmendationsComponent()
	{
		System.out.println("dummyDataforHomePageRecommmendationsComponent s");
		homepageRecComponent.setUid("WF_COM_449");
		homepageRecComponent.setName("Recommendations Component");
		System.out.println("dummyDataforHomePageRecommmendationsComponent e");
	}

	
	@Test
	public void testViewHomepageRecommendationsComponent()
	{
		
		final String componentUid = "WF_COM_449";
		try
		{
			Mockito.when(componentFacade.getComponent(componentUid)).thenReturn(homepageRecComponent);
		}
		catch (final NoSuchComponentException e)
		{
			LOG.error("No Such component available");
			e.printStackTrace();
		}

		/*Mockito.when(
				recommendationsFacade.getRecommendations(pageType,brandtype,urlmap)).thenReturn(recommendationsData);*/
		Assert.assertEquals("WF_COM_449", homepageRecComponent.getUid());
		Assert.assertEquals("Recommendations Component", homepageRecComponent.getName());
		

	}



}
