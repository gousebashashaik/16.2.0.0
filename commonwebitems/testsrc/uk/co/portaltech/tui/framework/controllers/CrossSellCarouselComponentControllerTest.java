/**
 * 
 */
package uk.co.portaltech.tui.framework.controllers;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.tui.components.model.CrossSellCarouselComponentModel;
import uk.co.portaltech.tui.exception.NoSuchComponentException;
import uk.co.portaltech.tui.facades.ComponentFacade;
import uk.co.portaltech.tui.web.view.data.CrossSellWrapper;
import uk.co.portaltech.tui.web.view.data.LocationCarouselViewData;
import uk.co.tui.async.logging.TUILogUtils;

import com.enterprisedt.util.debug.Logger;


/**
 * @author gopinath.n
 * 
 */
public class CrossSellCarouselComponentControllerTest
{

	private CrossSellCarouselComponentModel crossComponent;

	private List<CrossSellCarouselComponentModel> crossComponentList;

	@Mock
	private ComponentFacade componentFacade;

	@Mock
	private CrossSellWrapper wrapper;

	private LocationCarouselViewData viewData;

	
	private final TUILogUtils LOG = new TUILogUtils("CrossSellCarouselComponentControllerTest");

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		crossComponent = new CrossSellCarouselComponentModel();
		dummyDataforCrossSellCarouselComponent();
		
		

	}

	public void dummyDataforCrossSellCarouselComponent()
	{
		crossComponent.setUid("WF_COM_001-1");
		crossComponent.setName("Cross sell carousel");

	}

	/*
	 * public void dummyDataforCrossSellWrapper() {
	 * 
	 * 
	 * }
	 */

	/*
	 * public void dummyDataforLocationCarouselViewData() { viewData.setCode(""); viewData.setName("");
	 * viewData.setLookUpTypeCode("");
	 * 
	 * }
	 */

	@Test
	public void testViewTabbedComponent()
	{
		final String componentUid = "WF_COM_001-1";
		try
		{
			Mockito.when(componentFacade.getComponent(componentUid)).thenReturn(crossComponent);
		}
		catch (final NoSuchComponentException e)
		{
			LOG.error("No Such component available");
			e.printStackTrace();
		}


		try
		{
			Mockito.when(
					componentFacade.getCrossSellCarousels(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
							Mockito.anyString(), Mockito.anyString())).thenReturn(wrapper);
		}
		catch (final NoSuchComponentException e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertEquals("WF_COM_001-1", crossComponent.getUid());
		Assert.assertEquals("Cross sell carousel", crossComponent.getName());

	}




}
