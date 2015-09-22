/**
 * 
 */
package uk.co.portaltech.tui.framework.controllers;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.tui.components.model.SocialMediaLinksComponentModel;
import uk.co.portaltech.tui.exception.NoSuchComponentException;
import uk.co.portaltech.tui.facades.ComponentFacade;
import uk.co.portaltech.tui.facades.SocialMediaFacade;
import uk.co.portaltech.tui.web.view.data.SocialMediaLinksComponentData;
import uk.co.portaltech.tui.web.view.data.SocialMediaViewData;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.web.common.enums.PositionType;

import com.enterprisedt.util.debug.Logger;


/**
 * @author arun.y
 * 
 */
public class SocialMediaLinksComponentControllerTest
{

	@Mock
	private ComponentFacade componentFacade;

	@Mock
	private SocialMediaFacade socialMediaFacade;

	private SocialMediaLinksComponentModel socialMediaLinksModel;
	private SocialMediaViewData socialMediaViewData;
	private List<SocialMediaViewData> socialMediaViewDataList;
	private SocialMediaLinksComponentData socialMediaLinksComponentData;

	
	private final TUILogUtils LOG = new TUILogUtils("SocialMediaLinksComponentControllerTest");

	@Before
	public void setUp() throws Exception
	{

		MockitoAnnotations.initMocks(this);
		socialMediaLinksModel = new SocialMediaLinksComponentModel();
		socialMediaViewData = new SocialMediaViewData();
		socialMediaViewDataList = new ArrayList<SocialMediaViewData>();
		socialMediaLinksComponentData = new SocialMediaLinksComponentData();
		testDummyDataForSocialMedia();
	}

	public void testDummyDataForSocialMedia()
	{
		socialMediaLinksModel.setUid("WP_COM_901.1");
		socialMediaLinksModel.setPosition(PositionType.HOMEPAGE_MIDDLE);
		socialMediaLinksModel.setTitle("Social Media Title");

		socialMediaViewData.setColor("#2c4788");
		socialMediaViewData.setIconUrl("/holiday/images/facebook.png");
		socialMediaViewData.setMediaText("Facebook");
		socialMediaViewData.setMediaUrl("http://www.facebook.com");
		socialMediaViewData.setOnHoverColor("#566CA0");
		socialMediaViewData.setOnHoverText("like");
		socialMediaViewDataList.add(socialMediaViewData);
		socialMediaLinksComponentData.setSocialMediaViewDataList(socialMediaViewDataList);
	}


	@Test
	public void testViewSocialMediaLinksComponent()
	{
		final String componentUID = "WP_COM_901.1";
		final String url = "http://www.firstchoice.co.uk";
		try
		{
			when((SocialMediaLinksComponentModel) componentFacade.getComponent(componentUID)).thenReturn(socialMediaLinksModel);
			when(socialMediaFacade.getSocialMedia(socialMediaLinksModel, url)).thenReturn(socialMediaLinksComponentData);
		}
		catch (final NoSuchComponentException e)
		{
			LOG.error("No such component available");
		}
		Assert.assertEquals("Social Media Title", socialMediaLinksModel.getTitle());
		Assert.assertEquals(socialMediaViewDataList, socialMediaLinksComponentData.getSocialMediaViewDataList());
	}

}
