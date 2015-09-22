/**
 * 
 */
package uk.co.portaltech.tui.services;


import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.enums.AccommodationType;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.NStateItineraryModel;
import uk.co.portaltech.travel.model.ItineraryLegItemModel;
import uk.co.portaltech.travel.services.AccommodationService;
import uk.co.portaltech.tui.model.ItineraryLeg;
import uk.co.portaltech.tui.resolvers.TUIUrlResolver;
import uk.co.portaltech.tui.web.view.data.ItineraryViewData;
import uk.co.portaltech.tui.web.view.data.wrapper.TwoCentreSelectorViewData;


/**
 * @author narendra.bm
 * 
 */
@UnitTest
public class LinkedItemServiceImplTest
{

	@InjectMocks
	private final LinkedItemServiceImpl linkedItemServiceImpl = new LinkedItemServiceImpl();

	@Mock
	private CMSSiteService siteService;

	@Mock
	private AccommodationService accommodationService;

	@Mock
	private CMSSiteService cmsSiteService;

	@Mock
	private TuiUtilityService tuiUtilityService;

	@Mock
	private TUIUrlResolver<ProductModel> tuiProductUrlResolver;

	@Mock
	private Converter<ItemModel, ItineraryLeg> itineraryConverter;

	@Mock
	private Populator<ItemModel, ItineraryLeg> itineraryPopulator;

	private final String accommodationCode = "accomCode";

	AccommodationModel accommodationModel1 = mock(AccommodationModel.class);
	AccommodationModel accommodationModel2 = mock(AccommodationModel.class);
	AccommodationModel accommodationModel3 = mock(AccommodationModel.class);

	ItineraryLegItemModel iLeg1 = mock(ItineraryLegItemModel.class);
	ItineraryLegItemModel iLeg2 = mock(ItineraryLegItemModel.class);
	ItineraryLegItemModel iLeg3 = mock(ItineraryLegItemModel.class);

	List<ItineraryLegItemModel> itineraryLegModels = mock(List.class);
	List<AccommodationModel> accommodations = mock(List.class);

	/**
	 * @throws java.lang.Exception
	 */
	@SuppressWarnings("boxing")
	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);

		final CatalogVersionModel catalog = getCatalogVersion();

		final AccommodationType accomType1 = mock(AccommodationType.class);
		final AccommodationType accomType2 = mock(AccommodationType.class);

		final AccommodationModel accommodationModel1 = mock(AccommodationModel.class);


		when(accommodationModel2.getCode()).thenReturn("1234");
		when(accommodationModel2.getName()).thenReturn("abc");
		when(tuiProductUrlResolver.resolve(accommodationModel2)).thenReturn("http://www.thomson.co.uk/abc");
		when(accommodationModel2.getType()).thenReturn(accomType1);
		when(accommodationModel2.getType().getCode()).thenReturn("SHIP");

		when(accommodationModel3.getCode()).thenReturn("5678");
		when(accommodationModel3.getName()).thenReturn("xyz");
		when(tuiProductUrlResolver.resolve(accommodationModel3)).thenReturn("http://www.thomson.co.uk/xyz");
		when(accommodationModel3.getType()).thenReturn(accomType2);
		when(accommodationModel3.getType().getCode()).thenReturn("HOTEL");

		final NStateItineraryModel itineraryModel = mock(NStateItineraryModel.class);

		when(tuiUtilityService.getSiteReleventBrandPks()).thenReturn(Arrays.asList("TH"));

		Mockito.when(accommodationService.getAccomodationByCodeAndCatalogVersion(accommodationCode, catalog, Arrays.asList("TH")))
				.thenReturn(accommodationModel1);

		when(accommodationService.getAccommodationAttractionExcursionbyCode("1234", cmsSiteService.getCurrentCatalogVersion()))
				.thenReturn(accommodationModel2);
		when(accommodationService.getAccommodationAttractionExcursionbyCode("5678", cmsSiteService.getCurrentCatalogVersion()))
				.thenReturn(accommodationModel3);

		accommodations.add(accommodationModel2);
		accommodations.add(accommodationModel3);



		final Iterator<AccommodationModel> mockIter1 = Mockito.mock(Iterator.class);
		when(accommodations.iterator()).thenReturn(mockIter1);
		when(mockIter1.hasNext()).thenReturn(true, true, false);
		when(mockIter1.next()).thenReturn(accommodationModel2).thenReturn(accommodationModel3);

		when(iLeg1.getDuration()).thenReturn("3 Night(s)");
		when(iLeg1.getArrivalDay()).thenReturn("1");
		when(iLeg1.getItineraryLegCode()).thenReturn("001");
		when(iLeg1.getItineraryLegType()).thenReturn("ACCOMMODATION");

		when(iLeg2.getDuration()).thenReturn("4 Night(s)");
		when(iLeg2.getArrivalDay()).thenReturn("4");
		when(iLeg2.getItineraryLegCode()).thenReturn("002");
		when(iLeg2.getItineraryLegType()).thenReturn("LOCATION");

		when(iLeg3.getDuration()).thenReturn("6 Night(s)");
		when(iLeg3.getArrivalDay()).thenReturn("8");
		when(iLeg3.getItineraryLegCode()).thenReturn("003");
		when(iLeg3.getItineraryLegType()).thenReturn("ATTRACTION");

		itineraryLegModels.add(iLeg1);
		itineraryLegModels.add(iLeg2);
		itineraryLegModels.add(iLeg3);

		final Iterator<ItineraryLegItemModel> mockIter2 = Mockito.mock(Iterator.class);
		when(mockIter2.hasNext()).thenReturn(true, true, true, false);
		when(mockIter2.next()).thenReturn(iLeg1).thenReturn(iLeg2).thenReturn(iLeg3).thenReturn(iLeg1).thenReturn(iLeg2)
				.thenReturn(iLeg3);

		final Iterator<ItineraryLegItemModel> mockIter = Mockito.mock(Iterator.class);
		when(itineraryLegModels.iterator()).thenReturn(mockIter).thenReturn(mockIter2);
		when(mockIter.hasNext()).thenReturn(true, true, true, false);
		when(mockIter.next()).thenReturn(iLeg1).thenReturn(iLeg2).thenReturn(iLeg3).thenReturn(iLeg1).thenReturn(iLeg2)
				.thenReturn(iLeg3);

		when(accommodationModel1.getItinerary()).thenReturn(itineraryModel);
		when(accommodationModel1.getItinerary().getItineraryLegs()).thenReturn(itineraryLegModels);
	}

	/**
	 * Test method for
	 * {@link uk.co.portaltech.tui.services.LinkedItemServiceImpl#getTwoCentreSelectorData(java.lang.String)}.
	 */
	@Test
	public void testGetTwoCentreSelectorData()
	{

		final TwoCentreSelectorViewData viewData = linkedItemServiceImpl.getTwoCentreSelectorData("accomCode");
		assertThat(viewData, is(notNullValue()));
		assertEquals(2, viewData.getItineraryLegs().size());
		assertEquals("1234", viewData.getItineraryLegs().get(0).getCode());
		assertEquals("9", viewData.getItineraryLegs().get(0).getDuration());
		assertEquals("5678", viewData.getItineraryLegs().get(1).getCode());
		assertEquals("4", viewData.getItineraryLegs().get(1).getDuration());
	}

	@Test
	public void testGetItinerary()
	{
		final ItineraryLeg itineraryLeg1 = new ItineraryLeg();
		when(itineraryConverter.convert(accommodationModel2)).thenReturn(itineraryLeg1);
		doNothing().when(itineraryPopulator).populate(accommodationModel2, itineraryLeg1);

		final ItineraryLeg itineraryLeg2 = new ItineraryLeg();
		when(itineraryConverter.convert(accommodationModel3)).thenReturn(itineraryLeg2);
		doNothing().when(itineraryPopulator).populate(accommodationModel3, itineraryLeg2);

		final ItineraryViewData viewData = linkedItemServiceImpl.getItinerary("accomCode");
		assertThat(viewData, is(notNullValue()));
		final InOrder inOrder1 = inOrder(itineraryConverter);
		inOrder1.verify(itineraryConverter).convert(accommodationModel2);
		inOrder1.verify(itineraryConverter).convert(accommodationModel3);

		final InOrder inOrder = inOrder(itineraryPopulator);
		inOrder.verify(itineraryPopulator).populate(accommodationModel2, itineraryLeg1);
		inOrder.verify(itineraryPopulator).populate(accommodationModel3, itineraryLeg2);
	}

	@Test
	public void testGetLinkedItemsOfAccommodationType()
	{
		final List<AccommodationModel> accommodations = linkedItemServiceImpl.getLinkedItemsOfAccommodationType("accomCode");
		assertThat(accommodations, is(notNullValue()));
		assertEquals(2, accommodations.size());
		assertEquals("1234", accommodations.get(0).getCode());
		assertEquals("5678", accommodations.get(1).getCode());
	}

	private CatalogVersionModel getCatalogVersion()
	{
		final CatalogVersionModel version = Mockito.mock(CatalogVersionModel.class);
		final PK pK = PK.fromLong(1);
		Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(version);
		Mockito.when(cmsSiteService.getCurrentCatalogVersion()).thenReturn(version);
		Mockito.when(version.getPk()).thenReturn(pK);
		return version;
	}

}
