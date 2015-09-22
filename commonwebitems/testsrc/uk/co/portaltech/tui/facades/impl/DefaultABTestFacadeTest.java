/**
 *
 */
package uk.co.portaltech.tui.facades.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.tui.components.model.ABTestComponentModel;
import uk.co.portaltech.tui.facades.impl.DefaultABTestFacade;
import uk.co.portaltech.tui.model.ABTestModel;
import uk.co.portaltech.tui.model.VariantGroupModel;
import uk.co.portaltech.tui.services.ABTestComponentService;
import uk.co.portaltech.tui.web.view.data.ABTestViewData;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.web.common.enums.ABTestComponentScopes;


/**
 * @author sureshbabu.rn
 *
 */
public class DefaultABTestFacadeTest
{

	@InjectMocks
	private final DefaultABTestFacade defaultABTestFacade = new DefaultABTestFacade();

	@Mock
	private CMSComponentService cmsComponentService;

	@Mock
	private ABTestComponentService abTestComponentService;

	@Mock
	private Converter<SimpleCMSComponentModel, ABTestViewData> abTestDataConverter;

	@Mock
	ABTestComponentModel source;

	@Mock
	private Populator<ABTestComponentModel, ABTestViewData> abTestDataPopulatorFromABTestComponent;

	@Mock
	private Populator<SimpleCMSComponentModel, ABTestViewData> abTestDataPopulatorFromCMSComponent;

	@Mock
	
	private final TUILogUtils logger = new TUILogUtils("DefaultABTestFacadeTest");

	@Mock
	private ABTestComponentModel abTestComponent;

	private SimpleCMSComponentModel simpleCMSComponentModel, simpleCMSComponent;
	private AbstractCMSComponentModel abstractCMSComponentModel;

	private List<SimpleCMSComponentModel> simpleCMSComponentList;
	private ABTestViewData abTestViewData;
	private ABTestComponentModel abTestComponentModel;
	private ABTestModel abTestModel;
	private ABTestComponentScopes abTestComponentScopes;
	private Set<String> testNames;
	private Map<ABTestModel, VariantGroupModel> abtestVariantGroup;
	private VariantGroupModel variantGroupModel;

	private final String componentUid = "WF_COM_12131", variantCode = "1234";

	@Before
	public void setUp() throws Exception
	{

		MockitoAnnotations.initMocks(this);

		simpleCMSComponentModel = new SimpleCMSComponentModel();
		simpleCMSComponent = new SimpleCMSComponentModel();
		abTestComponentModel = new ABTestComponentModel();
		abTestViewData = new ABTestViewData();
		simpleCMSComponentList = new ArrayList<SimpleCMSComponentModel>();
		abTestModel = new ABTestModel();
		testNames = new HashSet<String>();
		variantGroupModel = new VariantGroupModel();
		abtestVariantGroup = new HashMap<ABTestModel, VariantGroupModel>();
		abstractCMSComponentModel = new AbstractCMSComponentModel();

		abstractCMSComponentModel.setName("abstract");
		abstractCMSComponentModel.setCreationtime(new Date());
		abstractCMSComponentModel.setUid("WF_COM_12131");
		abstractCMSComponentModel.setVisible(true);

		abtestVariantGroup.put(abTestModel, variantGroupModel);

		variantGroupModel.setVariantCode("1111");
		variantGroupModel.setCreationtime(new Date());

		testNames.add("Thomson");
		testNames.add("TH_FC");
		testNames.add("first choice");

		abTestModel.setTestCode("abtest");

		simpleCMSComponentModel.setCreationtime(new Date());
		simpleCMSComponentModel.setName("simpleCMSComponent");
		simpleCMSComponentModel.setUid("WF_COM_1232");

		simpleCMSComponentList.add(simpleCMSComponentModel);
		abTestComponentModel.setCreationtime(new Date());
		abTestComponentModel.setName("abtestcomponent");
		abTestComponentModel.setSimpleCMSComponent(simpleCMSComponentList);
		abTestComponentModel.setUid("WF_COM_1111");
		abTestComponentModel.setScope(abTestComponentScopes);
		abTestComponentModel.setAbTest(abTestModel);

		abTestViewData.setTestName("abtest");
		abTestViewData.setVariantCode("4523");

	}

	/**
	 * Test method for {@link uk.co.portaltech.tui.facades.impl.DefaultABTestFacade#getABTestData(java.lang.String)} .
	 *
	 * @throws CMSItemNotFoundException
	 */
	@Test
	public void testGetABTestDataString() throws CMSItemNotFoundException
	{

		Mockito.when((ABTestComponentModel) cmsComponentService.getAbstractCMSComponent(componentUid)).thenReturn(
				abTestComponentModel);
		Mockito.when(abTestComponentService.getRandomCMSComponent(abTestComponentModel)).thenReturn(simpleCMSComponentModel);
		Mockito.when(abTestDataConverter.convert(simpleCMSComponentModel)).thenReturn(abTestViewData);

		Mockito.doNothing().when(abTestDataPopulatorFromABTestComponent).populate(abTestComponentModel, abTestViewData);
		Mockito.doNothing().when(abTestDataPopulatorFromCMSComponent).populate(simpleCMSComponentModel, abTestViewData);

		assertNotNull(defaultABTestFacade.getABTestData(componentUid));
		assertThat(defaultABTestFacade.getABTestData(componentUid).getTestName(), is("abtest"));
		assertThat(defaultABTestFacade.getABTestData(componentUid).getVariantCode(), is("4523"));

	}

	/**
	 * Test method for
	 * {@link uk.co.portaltech.tui.facades.impl.DefaultABTestFacade#getABTestData(java.lang.String, java.lang.String)} .
	 *
	 * @throws CMSItemNotFoundException
	 */
	@Test
	public void testGetABTestDataStringString() throws CMSItemNotFoundException
	{

		Mockito.when((ABTestComponentModel) cmsComponentService.getAbstractCMSComponent(componentUid)).thenReturn(
				abTestComponentModel);
		Mockito.when(abTestComponentService.getRandomCMSComponentFromVariantGroup(abTestComponentModel, variantCode)).thenReturn(
				simpleCMSComponentModel);
		Mockito.when(abTestDataConverter.convert(simpleCMSComponentModel)).thenReturn(abTestViewData);

		Mockito.doNothing().when(abTestDataPopulatorFromABTestComponent).populate(abTestComponent, abTestViewData);
		Mockito.doNothing().when(abTestDataPopulatorFromCMSComponent).populate(simpleCMSComponentModel, abTestViewData);
		Mockito.doNothing().when(logger).debug(abTestViewData.toString());

		assertNotNull(defaultABTestFacade.getABTestData(componentUid, variantCode));
		assertThat(defaultABTestFacade.getABTestData(componentUid, variantCode).getTestName(), is("abtest"));
		assertThat(defaultABTestFacade.getABTestData(componentUid, variantCode).getVariantCode(), is("1234"));

	}

	/**
	 * Test method for
	 * {@link uk.co.portaltech.tui.facades.impl.DefaultABTestFacade#getVariant(java.lang.String, java.util.Set)} .
	 *
	 * @throws CMSItemNotFoundException
	 */
	@Test
	public void testGetVariant() throws CMSItemNotFoundException
	{
		Mockito.when((ABTestComponentModel) cmsComponentService.getAbstractCMSComponent(componentUid)).thenReturn(
				abTestComponentModel);
		Mockito.when(abTestComponentService.getVariant(abTestComponentModel, testNames)).thenReturn(abtestVariantGroup);
		Mockito.when(abTestDataConverter.convert(abTestComponentModel)).thenReturn(abTestViewData);

		assertNotNull(defaultABTestFacade.getVariant(componentUid, testNames));
		assertThat(defaultABTestFacade.getVariant(componentUid, testNames).getTestName(), is("abtest"));
		assertThat(defaultABTestFacade.getVariant(componentUid, testNames).getVariantCode(), is("1111"));
	}

}
