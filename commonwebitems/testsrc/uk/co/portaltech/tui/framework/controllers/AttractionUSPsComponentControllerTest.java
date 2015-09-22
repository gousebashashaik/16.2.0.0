/**
 * 
 */
package uk.co.portaltech.tui.framework.controllers;

import org.junit.Before;
import org.junit.Test;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;

import com.enterprisedt.util.debug.Logger;

import static org.mockito.Mockito.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.tui.components.model.AttractionUSPsComponentModel;
import uk.co.portaltech.tui.exception.NoSuchComponentException;
import uk.co.portaltech.tui.facades.ComponentFacade;
import uk.co.portaltech.tui.facades.AttractionFacade;
import uk.co.portaltech.tui.web.view.data.AttractionViewData;
import uk.co.tui.async.logging.TUILogUtils;

/**
 * @author arun.y
 *
 */

@UnitTest
public class AttractionUSPsComponentControllerTest {
	
	@Mock
	private ComponentFacade componentFacade;
	
	@Mock
	private AttractionFacade attractionFacade;
	
	
	private final TUILogUtils LOG = new TUILogUtils("AttractionUSPsComponentControllerTest");
	
	private CatalogVersionModel catalogVersionModel;
	
	private CatalogModel catalogModel;
	
	private AttractionUSPsComponentModel attractionUSPsComponentModel;
	
	private AttractionViewData attractionViewData;


	@Before
	public void setUp() throws Exception 
	{
		MockitoAnnotations.initMocks(this);
		catalogVersionModel = new CatalogVersionModel();
		catalogModel = new CatalogModel();
		attractionUSPsComponentModel = new AttractionUSPsComponentModel();
		attractionViewData = new AttractionViewData();
		
		catalogModel.setId("fc-catalog");
        catalogVersionModel.setCatalog(catalogModel);
        catalogVersionModel.setVersion("Online");
        
        getAttractionUSPsComponentModelInfo();
        getAttractionViewDataInfo();
	}
	
	public AttractionUSPsComponentModel getAttractionUSPsComponentModelInfo()
	{
		attractionUSPsComponentModel.setUid("attractionUsps_comp");
		attractionUSPsComponentModel.setCatalogVersion(catalogVersionModel);
		attractionUSPsComponentModel.setName("Attraction USPs Component");
		attractionUSPsComponentModel.setVisibleItems(Integer.valueOf(10));
		return attractionUSPsComponentModel;
	}
	
	public AttractionViewData getAttractionViewDataInfo()
	{
		attractionViewData.setUrl("/holiday/attraction/Aghios-Ioannis/Aqualand-Ticket-Only-992768");
		attractionViewData.setCode("992768");
		attractionViewData.setName("Aqualand - Ticket Only");
		return attractionViewData;
	}


	@Test
	public void testViewAttractionUSPsComponent() 
	{
		try 
		{
			when(componentFacade.getComponent(attractionUSPsComponentModel.getUid())).thenReturn(attractionUSPsComponentModel);
			when(attractionFacade.getAttractionUspsByCode("992768")).thenReturn(attractionViewData);
			assertThat(attractionUSPsComponentModel.getName(), is("Attraction USPs Component"));
			assertThat(attractionUSPsComponentModel.getUid(), is("attractionUsps_comp"));
			assertThat(attractionUSPsComponentModel.getVisibleItems(), is(Integer.valueOf(10)));
			assertThat(attractionViewData, is(notNullValue()));
			assertThat(attractionViewData.getUrl(), is("/holiday/attraction/Aghios-Ioannis/Aqualand-Ticket-Only-992768"));
			assertThat(attractionViewData.getName(), is("Aqualand - Ticket Only"));
			assertThat(attractionViewData.getCode(), is("992768"));
		} 
		catch (NoSuchComponentException e) 
		{
			LOG.error("No such component available");
		}
	}

}
