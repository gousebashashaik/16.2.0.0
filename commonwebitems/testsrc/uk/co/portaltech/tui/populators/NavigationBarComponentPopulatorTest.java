/**
 * 
 */
package uk.co.portaltech.tui.populators;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.junit.Assert.fail;

import de.hybris.platform.acceleratorcms.enums.NavigationBarMenuLayout;
import de.hybris.platform.acceleratorcms.model.components.NavigationBarComponentModel;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.commerceservices.converter.Populator;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.tui.web.view.data.CMSLinkComponentViewData;
import uk.co.portaltech.tui.web.view.data.CMSNavigationNodeViewData;
import uk.co.portaltech.tui.web.view.data.NavigationBarComponentViewData;

/**
 * @author sureshbabu.rn
 *
 */
public class NavigationBarComponentPopulatorTest {

	@InjectMocks
	private NavigationBarComponentPopulator navigationBarComponentPopulator=new NavigationBarComponentPopulator();
	
	@Mock
	private Converter<CMSNavigationNodeModel, CMSNavigationNodeViewData>                 cmsNavigationNodeConverter;
	@Mock
	private Populator<CMSNavigationNodeModel, CMSNavigationNodeViewData>                 cmsNavigationNodePopulator;
	@Mock
	private Converter<CMSLinkComponentModel, CMSLinkComponentViewData>                   cmsLinkComponentConverter;
	@Mock
	private Populator<CMSLinkComponentModel, CMSLinkComponentViewData>                   cmsLinkComponentPopulator;
	@Mock
	private NavigationBarMenuLayout navigationBarMenuLayout;
	
	private NavigationBarComponentModel sourceModel;
	private NavigationBarComponentViewData targetData;
	
	private CMSNavigationNodeModel navigationNodeModel;
	private CMSLinkComponentModel cmsLinkComponentModel;
	private CMSLinkComponentViewData cMSLinkComponentViewData;
	private CMSNavigationNodeViewData cMSNavigationNodeViewData;
	@SuppressWarnings("boxing")
	@Before
	public void setUp() throws Exception {
		
		MockitoAnnotations.initMocks(this);
		
		sourceModel=new NavigationBarComponentModel();
		targetData=new NavigationBarComponentViewData();
		navigationNodeModel=new CMSNavigationNodeModel();
		cmsLinkComponentModel=new CMSLinkComponentModel();
		
		navigationNodeModel.setName("node");
		navigationNodeModel.setCreationtime(new Date());
		
		cmsLinkComponentModel.setName("link");
		cmsLinkComponentModel.setCreationtime(new Date());
		cmsLinkComponentModel.setUrl("www.tui.com");
		
		sourceModel.setStyleClass("style");
		sourceModel.setWrapAfter(12);
		sourceModel.setDropDownLayout(navigationBarMenuLayout);
		sourceModel.setName("sample");
		sourceModel.setNavigationNode(navigationNodeModel);
		sourceModel.setLink(cmsLinkComponentModel);
		
		
		
	}

	/**
	 * Test method for {@link uk.co.portaltech.tui.populators.NavigationBarComponentPopulator#populate(de.hybris.platform.acceleratorcms.model.components.NavigationBarComponentModel, uk.co.portaltech.tui.web.view.data.NavigationBarComponentViewData)}.
	 */
	@SuppressWarnings("boxing")
	@Test
	public void testPopulate() {
		
		Mockito.when(cmsNavigationNodeConverter.convert(navigationNodeModel)).thenReturn(cMSNavigationNodeViewData);
		Mockito.when(cmsLinkComponentConverter.convert(cmsLinkComponentModel)).thenReturn(cMSLinkComponentViewData);
		
		assertNotNull(sourceModel);
		
		navigationBarComponentPopulator.populate(sourceModel, targetData);
		
		assertNotNull(sourceModel);
		assertNotNull(targetData);
		
		
		assertThat(targetData.getStyleClass(), is("style"));
		assertThat(targetData.getWrapAfter(), is(12));
		assertThat(targetData.getName(), is("sample"));
		assertThat(targetData.getDropDownLayout(), is(navigationBarMenuLayout));
		assertThat(targetData.getNavigationNode(), is(cMSNavigationNodeViewData));
		assertThat(targetData.getLink(), is(cMSLinkComponentViewData));
		
		
	}

}
