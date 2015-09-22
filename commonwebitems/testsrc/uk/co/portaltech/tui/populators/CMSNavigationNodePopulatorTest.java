/**
 * 
 */
package uk.co.portaltech.tui.populators;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import uk.co.portaltech.tui.web.view.data.CMSLinkComponentViewData;
import uk.co.portaltech.tui.web.view.data.CMSNavigationNodeViewData;

import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.commerceservices.converter.Populator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author sureshbabu.rn
 *
 */
public class CMSNavigationNodePopulatorTest {

	@InjectMocks
	private CMSNavigationNodePopulator cmsNavigationNodePopulator=new CMSNavigationNodePopulator();
	@Spy
	private CMSNavigationNodePopulator cmsNavigationNodePopulator2=new CMSNavigationNodePopulator();
	
	@Mock
	private Converter<CMSNavigationNodeModel, CMSNavigationNodeViewData>                 cmsNavigationNodeConverter;
	@Mock
	private Converter<CMSLinkComponentModel, CMSLinkComponentViewData>                   cmsLinkComponentConverter;
	@Mock
	private Populator<CMSLinkComponentModel, CMSLinkComponentViewData>                   cmsLinkComponentPopulator;
	
	
	private CMSNavigationNodeModel      sourceModel,cmsNavigationNodeModel,soorceModel1;
	private CMSNavigationNodeViewData   targetData, cmsNavigationNodeViewData,cmsNavigationNodeViewData2,cmsNavigationNodeViewData3;
	private CMSLinkComponentModel cmsLinkComponentModel;
	private List<CMSLinkComponentModel> cmsLinkComponentModelList;
	private List<CMSNavigationNodeModel> cmsNavigationNodeModelList;
	private CMSLinkComponentViewData cmsLinkComponentViewData,cmsLinkComponentViewData2,cmsLinkComponentViewData3;
	
	@SuppressWarnings("deprecation")
	@Before
	public void setUp() throws Exception {
		
		MockitoAnnotations.initMocks(this);
		
		sourceModel=new CMSNavigationNodeModel();
		cmsNavigationNodeModel=new CMSNavigationNodeModel();
		targetData=new CMSNavigationNodeViewData();
		cmsNavigationNodeViewData=new CMSNavigationNodeViewData();
		cmsLinkComponentViewData=new CMSLinkComponentViewData();
		cmsLinkComponentModel=new CMSLinkComponentModel();
		cmsLinkComponentModelList=new ArrayList<CMSLinkComponentModel>();
		cmsNavigationNodeModelList=new ArrayList<CMSNavigationNodeModel>();
		
		cmsLinkComponentModel.setExternal(true);
		cmsLinkComponentModel.setCreationtime(new Date());
		
		cmsNavigationNodeModel.setVisible(true);
		cmsNavigationNodeModel.setTitle("cmsNavigationNodeModel_Title",Locale.UK);
		
		cmsLinkComponentModelList.add(cmsLinkComponentModel);
		cmsNavigationNodeModelList.add(cmsNavigationNodeModel);
		
		sourceModel.setVisible(true);
		sourceModel.setTitle("source", Locale.UK);
		sourceModel.setLinks(cmsLinkComponentModelList);
		sourceModel.setChildren(cmsNavigationNodeModelList);
		sourceModel.setParent(sourceModel);
		
		cmsNavigationNodeViewData.setVisible(true);
		
		cmsLinkComponentViewData.setLinkName("viewdata_link");
		cmsLinkComponentViewData.setUrl("www.first.co.uk");
		cmsLinkComponentViewData2=new CMSLinkComponentViewData();
		
		cmsLinkComponentViewData2.setLinkName(null);
		cmsLinkComponentViewData2.setUrl(null);
		cmsNavigationNodeViewData2=new CMSNavigationNodeViewData();
		cmsNavigationNodeViewData3=new CMSNavigationNodeViewData();
		cmsLinkComponentViewData3=new CMSLinkComponentViewData();
	}

	/**
	 * Test method for {@link uk.co.portaltech.tui.populators.CMSNavigationNodePopulator#populate(de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel, uk.co.portaltech.tui.web.view.data.CMSNavigationNodeViewData)}.
	 */
	@SuppressWarnings("boxing")
	@Test
	public void testPopulate() {
		
		Mockito.when(cmsLinkComponentConverter.convert(Mockito.any(CMSLinkComponentModel.class))).thenReturn(cmsLinkComponentViewData);
	    Mockito.doNothing().when(cmsLinkComponentPopulator).populate(Mockito.any(CMSLinkComponentModel.class), Mockito.any(CMSLinkComponentViewData.class));
	    Mockito.when(cmsNavigationNodeConverter.convert(Mockito.any(CMSNavigationNodeModel.class))).thenReturn(cmsNavigationNodeViewData);
	    Mockito.doNothing().when(cmsNavigationNodePopulator2).populate(Mockito.any(CMSNavigationNodeModel.class), Mockito.any(CMSNavigationNodeViewData.class));
	    
	    
	    assertNotNull(sourceModel);
	    assertNotNull(targetData);
	    
	    cmsNavigationNodePopulator.populate(sourceModel, targetData);
	    
	    assertNotNull(targetData);
	    
	    assertEquals(true, targetData.isVisible());
	    assertEquals(true, targetData.getParent().isVisible());
	    assertEquals(1, targetData.getLinks().size());
	    assertEquals("viewdata_link", targetData.getLinks().get(0).getLinkName());
	    assertEquals("www.first.co.uk", targetData.getLinks().get(0).getUrl());
	    assertEquals(1, targetData.getChildren().size());
	    assertEquals(true, targetData.getChildren().get(0).isVisible());
	}
	@Test
	public void testPopulateNull(){
		soorceModel1=new CMSNavigationNodeModel();
		Mockito.when(cmsLinkComponentConverter.convert(Mockito.any(CMSLinkComponentModel.class))).thenReturn(cmsLinkComponentViewData2);
	    Mockito.doNothing().when(cmsLinkComponentPopulator).populate(Mockito.any(CMSLinkComponentModel.class), Mockito.any(CMSLinkComponentViewData.class));
	    Mockito.when(cmsNavigationNodeConverter.convert(Mockito.any(CMSNavigationNodeModel.class))).thenReturn(cmsNavigationNodeViewData2);
	    Mockito.doNothing().when(cmsNavigationNodePopulator2).populate(Mockito.any(CMSNavigationNodeModel.class), Mockito.any(CMSNavigationNodeViewData.class));
	    
	    
	   
	    cmsNavigationNodePopulator.populate(soorceModel1, targetData);
	    assertNotNull(targetData);
	}
	@Test
	public void testPopulateEmpty(){
		soorceModel1=new CMSNavigationNodeModel();
		Mockito.when(cmsLinkComponentConverter.convert(Mockito.any(CMSLinkComponentModel.class))).thenReturn(cmsLinkComponentViewData3);
	    Mockito.doNothing().when(cmsLinkComponentPopulator).populate(Mockito.any(CMSLinkComponentModel.class), Mockito.any(CMSLinkComponentViewData.class));
	    Mockito.when(cmsNavigationNodeConverter.convert(Mockito.any(CMSNavigationNodeModel.class))).thenReturn(cmsNavigationNodeViewData3);
	    Mockito.doNothing().when(cmsNavigationNodePopulator2).populate(Mockito.any(CMSNavigationNodeModel.class), Mockito.any(CMSNavigationNodeViewData.class));
	    
	    
	   
	    cmsNavigationNodePopulator.populate(soorceModel1, targetData);
	    assertNotNull(targetData);
	}
}
