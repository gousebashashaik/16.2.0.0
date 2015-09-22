/**
 * 
 */
package uk.co.portaltech.tui.framework.controllers;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;

import com.enterprisedt.util.debug.Logger;

import static org.mockito.Mockito.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.tui.components.model.AttractionEditorialComponentModel;
import uk.co.portaltech.tui.exception.NoSuchComponentException;
import uk.co.portaltech.tui.facades.ComponentFacade;
import uk.co.portaltech.tui.facades.AttractionFacade;
import uk.co.portaltech.tui.web.view.data.AttractionViewData;


/**
 * @author arun.y
 *
 */

@UnitTest
public class AttractionEditorialComponentControllerTest {
	
	@Mock
	private ComponentFacade componentFacade;
	
	@Mock
	private AttractionFacade attractionFacade;
	
	private final Logger LOG = Logger.getLogger(AttractionEditorialComponentControllerTest.class);
	
	private CatalogVersionModel catalogVersionModel;
	
	private CatalogModel catalogModel;
	
	private AttractionEditorialComponentModel attractionEditorialComponentModel;
	
	private AttractionViewData attractionViewData;


	@Before
	public void setUp() throws Exception 
	{
		MockitoAnnotations.initMocks(this);
		catalogVersionModel = new CatalogVersionModel();
		catalogModel = new CatalogModel();
		attractionEditorialComponentModel = new AttractionEditorialComponentModel();
		attractionViewData = new AttractionViewData();
		
		catalogModel.setId("fc-catalog");
        catalogVersionModel.setCatalog(catalogModel);
        catalogVersionModel.setVersion("Online");
        
        getAttractionEditorialComponentModelInfo();
        getAttractionViewDataInfo();
	}

	
	public AttractionEditorialComponentModel getAttractionEditorialComponentModelInfo()
	{
		attractionEditorialComponentModel.setUid("WF_COM_026-1");
		attractionEditorialComponentModel.setCatalogVersion(catalogVersionModel);
		attractionEditorialComponentModel.setName("Attraction Editorial Component");
		return attractionEditorialComponentModel;
	}
	
	public AttractionViewData getAttractionViewDataInfo()
	{
		attractionViewData.setUrl("/holiday/attraction/Lloret-de-Mar/Port-Aventura-991414");
		attractionViewData.setCode("991414");
		attractionViewData.setName("Port Aventura");
		return attractionViewData;
	}

	@Test
	public void testViewAttractionEditorialContent() 
	{
		try 
		{
			when(componentFacade.getComponent(attractionEditorialComponentModel.getUid())).thenReturn(attractionEditorialComponentModel);
			when(attractionFacade.getEditorialContent("991414")).thenReturn(attractionViewData);
			assertThat(attractionEditorialComponentModel.getUid(), is("WF_COM_026-1"));
			assertThat(attractionEditorialComponentModel.getName(), is("Attraction Editorial Component"));
			assertThat(attractionViewData, is(notNullValue()));
			assertThat(attractionViewData.getUrl(), is("/holiday/attraction/Lloret-de-Mar/Port-Aventura-991414"));
			assertThat(attractionViewData.getName(), is("Port Aventura"));
			assertThat(attractionViewData.getCode(), is("991414"));
		} 
		catch (NoSuchComponentException e) 
		{
			LOG.error("No such component available");
		}
		

	}

}
