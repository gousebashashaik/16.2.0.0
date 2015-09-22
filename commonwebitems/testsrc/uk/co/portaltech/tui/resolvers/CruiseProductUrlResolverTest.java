/**
 *
 */
package uk.co.portaltech.tui.resolvers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.servicelayer.type.TypeService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import uk.co.portaltech.travel.enums.SearchResultType;
import uk.co.portaltech.tui.facades.CrdToUrlMappingFacade;
import uk.co.portaltech.tui.services.TuiUtilityService;


/**
 * @author akshatha.bb
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(PK.class)
public class CruiseProductUrlResolverTest
{
	@InjectMocks
	private final CruiseProductUrlResolver cruiseProductUrlResolver = new CruiseProductUrlResolver();

	@Mock
	private CrdToUrlMappingFacade crdToUrlMapFacade;

	@Mock
	private TuiUtilityService tuiUtilityService;

	@Mock
	private TypeService typeService;

	private SearchResultType searchType;

	@Spy
	private final EnumerationValueModel enumerationValueModel = new EnumerationValueModel();

	private PK pkValue;


	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		cruiseProductUrlResolver.setOverrideSubPageType("OVERVIEW");
	}

	@Test
	public void resolveTest()
	{
		mockObjects();
		when(pkValue.toString()).thenReturn("pkValue");
		when(crdToUrlMapFacade.getUrlForCRD(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn("url");
		when(tuiUtilityService.getSiteBrand()).thenReturn("CR");
		final String result = cruiseProductUrlResolver.resolve("code", searchType.LOCATION);
		assertEquals("url", result);

	}

	@Test
	public void resolveTestTwo()
	{
		cruiseProductUrlResolver.setOverrideSubPageType("LOCATION");
		mockObjects();
		when(pkValue.toString()).thenReturn("pkValue");
		when(crdToUrlMapFacade.getUrlForCRD(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn("url");
		when(tuiUtilityService.getSiteBrand()).thenReturn("BR");
		final String result = cruiseProductUrlResolver.resolve("code", searchType.ACCOMMODATION);
		assertEquals("", result);

	}

	@Test
	public void resolveTestThree()
	{
		cruiseProductUrlResolver.setOverrideSubPageType("");
		mockObjects();
		when(pkValue.toString()).thenReturn("pkValue");
		when(crdToUrlMapFacade.getUrlForCRD(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn("url");
		when(tuiUtilityService.getSiteBrand()).thenReturn("CR");
		final String result = cruiseProductUrlResolver.resolve("code", searchType.CRUISEAREA);
		assertEquals("url", result);

	}


	@Test
	public void resolveTestFour()
	{
		cruiseProductUrlResolver.setOverrideSubPageType("LOCATION");
		mockObjects();
		when(pkValue.toString()).thenReturn("pkValue");
		when(crdToUrlMapFacade.getUrlForCRD(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn("url");
		when(tuiUtilityService.getSiteBrand()).thenReturn("CR");
		final String result = cruiseProductUrlResolver.resolve("code", searchType.CRUISEANDSTAY);
		assertEquals("urlLOCATION/", result);

	}

	@Test
	public void resolveTestFive()
	{
		mockObjects();
		when(pkValue.toString()).thenReturn("");
		when(crdToUrlMapFacade.getUrlForCRD(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn("");
		when(tuiUtilityService.getSiteBrand()).thenReturn("CR");
		final String result = cruiseProductUrlResolver.resolve("code", searchType.CRUISEANDSTAY);
		assertEquals("", result);

	}

	@Test
	public void setterGetterTest()
	{
		cruiseProductUrlResolver.setContext("context");
		cruiseProductUrlResolver.setDefaultSubPageType("defaultSubPageType");
		cruiseProductUrlResolver.setCrdToUrlMapFacade(crdToUrlMapFacade);
		cruiseProductUrlResolver.setPattern("pattern");
		cruiseProductUrlResolver.setTuiUtilityService(tuiUtilityService);
		final String context = cruiseProductUrlResolver.getContext();
		final String defaultSubPageType = cruiseProductUrlResolver.getDefaultSubPageType();
		final CrdToUrlMappingFacade crdToUrlMappingFacade = cruiseProductUrlResolver.getCrdToUrlMapFacade();
		final TuiUtilityService tuiUtilityService = cruiseProductUrlResolver.getTuiUtilityService();
		final String pattern = cruiseProductUrlResolver.getPattern();
		final String overrideSubPageType = cruiseProductUrlResolver.getOverrideSubPageType();
		assertEquals("pattern", pattern);

	}


	private void mockObjects()
	{
		pkValue = mock(PK.class);
		when(typeService.getEnumerationValue(Mockito.anyString(), Mockito.anyString())).thenReturn(enumerationValueModel);
		when(enumerationValueModel.getPk()).thenReturn(pkValue);



	}
}
