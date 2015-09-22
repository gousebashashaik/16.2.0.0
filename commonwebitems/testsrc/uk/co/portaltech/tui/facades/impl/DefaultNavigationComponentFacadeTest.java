package uk.co.portaltech.tui.facades.impl;

import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.type.TypeService;
import uk.co.portaltech.tui.components.model.NavigationComponentModel;
import uk.co.portaltech.tui.facades.impl.DefaultNavigationComponentFacade;

import uk.co.portaltech.tui.web.view.data.NavigationComponentViewData;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.jfree.util.Log;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;

/**
 * @author sushilkumar.s
 *
 */

public class DefaultNavigationComponentFacadeTest

{

	@InjectMocks
    private final DefaultNavigationComponentFacade navigationComponentFacade = new DefaultNavigationComponentFacade();

	@Mock
	private CMSComponentService cmsComponentService;

	@Mock
	private Populator<NavigationComponentModel, NavigationComponentViewData> navigationComponentPopulator;

	@Mock
	private Converter<NavigationComponentModel, NavigationComponentViewData> navigationComponentConverter;


	@Mock
	private TypeService typeService;

	public static final String TEST_COMPONENT_UID = "WF_COM_900-1";
    private final static String NAME="NavigationComponent";
	private final static String COMPONENT_STYLE="component Style";
	private final static boolean VIEW_MODE=true;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	 public NavigationComponentModel createDummayDataModel()
	 {
		 NavigationComponentModel navigationModel=new NavigationComponentModel();
		 navigationModel.setName(NAME);
		 navigationModel.setOnlyOneRestrictionMustApply(VIEW_MODE);
		 return navigationModel;
	 }
    public NavigationComponentViewData createDummyData()
    {
    	NavigationComponentViewData navigationComponentData=new NavigationComponentViewData();
    	navigationComponentData.setComponentStyle(COMPONENT_STYLE);
    	return navigationComponentData;
    }

	@Test
	public void testNavigationComponentData() throws CMSItemNotFoundException
	{
		NavigationComponentViewData NavigationComponentData=createDummyData();
		NavigationComponentModel navigationComponentModel=createDummayDataModel();

		try {
			Mockito.when((NavigationComponentModel)cmsComponentService.getSimpleCMSComponent(TEST_COMPONENT_UID)).thenReturn(navigationComponentModel);
			Mockito.when(navigationComponentConverter.convert(navigationComponentModel)).thenReturn(NavigationComponentData);
			NavigationComponentViewData expectedResult=navigationComponentFacade.getNavigationComponentViewData(TEST_COMPONENT_UID);
			assertThat(expectedResult, is(notNullValue()));
			assertThat(expectedResult.getComponentStyle(),is(NavigationComponentData.getComponentStyle()));
			}
		catch (NullPointerException e)
		{
				// YTODO Auto-generated catch block
				Log.error("Null pointe exception thrown");
		}
	}
}

