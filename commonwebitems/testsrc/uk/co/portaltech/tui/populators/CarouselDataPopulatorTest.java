/**
 * 
 */
package uk.co.portaltech.tui.populators;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.enums.AccommodationType;
import uk.co.portaltech.travel.enums.ArticleType;
import uk.co.portaltech.travel.enums.BrandType;
import uk.co.portaltech.travel.enums.FeatureType;
import uk.co.portaltech.travel.enums.RoomType;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.ArticleModel;
import uk.co.portaltech.travel.model.BoardBasisContentModel;
import uk.co.portaltech.travel.model.FacilityModel;
import uk.co.portaltech.travel.model.FeatureDescriptorModel;
import uk.co.portaltech.travel.model.FeatureValueSetModel;
import uk.co.portaltech.travel.model.RoomModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.MediaViewData;
import uk.co.travel.daos.util.BrandUtils;


/**
 * @author sureshbabu.rn
 * 
 */
public class CarouselDataPopulatorTest
{
	@InjectMocks
	private final CarouselDataPopulator carouselDataPopulator = new CarouselDataPopulator();
	@Mock
	private FeatureService featureService;
	private AccommodationModel accommodation;
	@Mock
	private AccommodationModel accommodationModel;
	@Mock
	private BrandType brandType;
	
	@Mock
	private BrandUtils brandUtils;
	
	@Mock
	private MediaFormatModel formatModel;
	private Collection<AccommodationModel> accommodations;
	private List<Object> list;
	private Collection<MediaModel> medias;
	private final String DESCRIPTION = "From its atmospheric old town to the leafy boulevards of its modern shopping district, Palma is one of those got-it-all cities. Today, you’ll get to know Majorca’s capital from the comfort of our sightseeing bus. Best of all, you can hop on and off to explore the bits you fancy on foot. You’ll see the city’s impressive Gothic cathedral, which casts a shadow over the harbour. You’ll tick off the stylish waterfront, with its glossy yachts and open-air restaurants. And you’ll check out the main shopping boulevards, where the local ladies do lunch in designer sunglasses. The city’s showpiece, though, is its cobbled Medieval quarter. Hop off the bus here to discover tiny treasure-trove jewellery and antique shops, and sunny squares where tapas joints advertise their specials on outdoor chalk boards. When you need to refuel, stop for a coffee and an ensaimada – a delicious local pastry – at one of the cubbyhole patisseries.";

	@SuppressWarnings("boxing")
	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);

		accommodation = new AccommodationModel();
		medias = new ArrayList<MediaModel>();

		final MediaModel mediaModel = new MediaModel();
		mediaModel.setCode("002240");
		mediaModel.setDescription(DESCRIPTION);
		mediaModel.setURL("/holiday/accommodation/location/Benidorm/Flamingo-Benidorm-002240");
		mediaModel.setAltText("AltText");
		mediaModel.setMime("MP4V-ES");
		mediaModel.setMediaFormat(formatModel);
		medias.add(mediaModel);

		final Set<FeatureDescriptorModel> descriptorModels = new HashSet<FeatureDescriptorModel>();
		final List<FeatureValueSetModel> featureValueSetModels = new ArrayList<FeatureValueSetModel>();

		final FeatureValueSetModel featureValueSetModel = new FeatureValueSetModel();

		featureValueSetModel.setCode("02240");
		featureValueSetModel.setCatalogVersion(getCatalogVersion());

		featureValueSetModels.add(featureValueSetModel);

		final FeatureDescriptorModel descriptorModel = new FeatureDescriptorModel();
		descriptorModel.setCode("02240");
		descriptorModel.setType(FeatureType.STRING);

		descriptorModels.add(descriptorModel);

		final Collection<FacilityModel> facilityModels = new ArrayList<FacilityModel>();

		final FacilityModel facilityModel = new FacilityModel();
		final Collection<MediaContainerModel> containerModels = new ArrayList<MediaContainerModel>();

		final MediaContainerModel containerModel = new MediaContainerModel();

		containerModel.setQualifier("Qualifier");
		containerModel.setMediaType("MIME");
		containerModel.setBrands(Arrays.asList( BrandType.FC,BrandType.TH));
		containerModels.add(containerModel);

		facilityModel.setCode("02240");
		facilityModel.setGalleryImages(containerModels);

		final Collection<RoomModel> roomModels = new ArrayList<RoomModel>();

		final RoomModel model = new RoomModel();

		model.setRoomType(RoomType.APARTMENT);
		model.setRoomTypeCode("Room type code");

		roomModels.add(model);

		final List<ArticleModel> articleModels = new ArrayList<ArticleModel>();

		final ArticleModel articleModel = new ArticleModel();

		articleModel.setBrands(Arrays.asList( BrandType.FC,BrandType.TH));
		articleModel.setContent("Content");
		articleModel.setSource("Source");
		articleModel.setTitle("Title");
		articleModel.setType(ArticleType.INTERNAL);
		articleModel.setUrl("www.firstchioce.com");

		articleModels.add(articleModel);

		final Collection<BoardBasisContentModel> basisContentModels = new ArrayList<BoardBasisContentModel>();

		final BoardBasisContentModel basisContentModel = new BoardBasisContentModel();

		basisContentModel.setName("board Basis Content ");
		basisContentModel.setFeatureDescriptors(descriptorModels);
		basisContentModel.setFeatureValueSets(featureValueSetModels);
		basisContentModel.setAccommodation(accommodation);
		basisContentModels.add(basisContentModel);
		accommodation.setCode("002440");
		accommodation.setBrands(Arrays.asList( BrandType.FC,BrandType.TH));
		accommodation.setFeatureDescriptors(descriptorModels);
		accommodation.setFeatureValueSets(featureValueSetModels);
		accommodation.setRatingsBar("1");
		accommodation.setType(AccommodationType.HOTEL);
		accommodation.setFacilities(facilityModels);
		accommodation.setReviewRating(new Double(12.00));
		accommodation.setReviewsCount(40);
		accommodation.setRooms(roomModels);
		accommodation.setBoardBasisContents(basisContentModels);
		accommodation.setArticles(articleModels);

		accommodations = new ArrayList<AccommodationModel>();
		accommodations.add(accommodation);
		list = new ArrayList<Object>();
		list.add(accommodation);

	}

	private CatalogVersionModel getCatalogVersion()
	{

		final CatalogVersionModel catalog = new CatalogVersionModel();
		catalog.setVersion("online");
		return catalog;
	}

	@SuppressWarnings("boxing")
	@Test
	public void testConvertAll()
	{
		final List<AccommodationViewData> expectedResult = carouselDataPopulator.convertAll(accommodations);

		assertNotNull(expectedResult);

		assertThat(expectedResult.size(), is(1));


	}

	@SuppressWarnings("boxing")
	@Test
	public void testConvertAllNull()
	{

		final Collection<AccommodationModel> accommodationList = null;
		final List<AccommodationViewData> expectedResult = carouselDataPopulator.convertAll(accommodationList);

		assertThat(expectedResult.size(), is(0));

	}

	@SuppressWarnings("boxing")
	@Test
	public void testConvert()
	{

		assertNotNull(accommodation);
		Mockito.when(accommodationModel.getBrands()).thenReturn(Arrays.asList(BrandType.FC, BrandType.TH));
		Mockito.when(brandType.getCode()).thenReturn(BrandType.TH.getCode());
		Mockito.when(brandUtils.getFeatureServiceBrand(Mockito.anyList())).thenReturn(BrandType.TH.getCode());
		Mockito.when(
				featureService.getFeatureValues("strapline", accommodation, new Date(),brandUtils.getFeatureServiceBrand( accommodation.getBrands())))
				.thenReturn(list);
		Mockito.when(featureService.getFeatureValues("name", accommodation, new Date(),brandUtils.getFeatureServiceBrand( accommodation.getBrands())))
				.thenReturn(list);

		final AccommodationViewData expectedResult = carouselDataPopulator.convert(accommodation);

		assertNotNull(expectedResult);

		final Map<String, List<Object>> map = expectedResult.getFeatureCodesAndValues();

		final Set<Entry<String, List<Object>>> accommodationModels = map.entrySet();

		final Iterator iterator = accommodationModels.iterator();

		while (iterator.hasNext())
		{
			final Map.Entry<String, List<Object>> keyVal = (Map.Entry<String, List<Object>>) iterator.next();

			final List<Object> featureList = keyVal.getValue();
			final Iterator iterator2 = featureList.iterator();

			while (iterator2.hasNext())
			{

				final Object object = iterator2.next();
				final AccommodationModel model = (AccommodationModel) object;
				assertThat(model.getCode(), is("002440"));
				assertThat(model.getRooms().size(), is(1));
				assertThat(model.getArticles().size(), is(1));
				assertThat(model.getBoardBasisContents().size(), is(1));
				assertThat(model.getFeatureValueSets().size(), is(1));
				assertThat(model.getRatingsBar(), is("1"));
				assertThat(model.getType(), is(AccommodationType.HOTEL));
				assertThat(model.getReviewsCount(), is(40));

			}

		}

	}

	@SuppressWarnings("boxing")
	@Test
	public void testConvertNull()
	{

		final AccommodationModel model = null;
		final AccommodationViewData expectedResult = carouselDataPopulator.convert(model);

		assertNull(expectedResult);
	}

	@Test
	public void testConvertAllMedia()
	{

		final List<MediaViewData> expectedResult = carouselDataPopulator.convertAllMedia(medias);

		assertNotNull(expectedResult);
		final Iterator<MediaViewData> iterator = expectedResult.iterator();
		while (iterator.hasNext())
		{
			final MediaViewData mediaViewData = iterator.next();
			assertThat(mediaViewData.getDescription(), is(DESCRIPTION));
			assertThat(mediaViewData.getMainSrc(), is("/holiday/accommodation/location/Benidorm/Flamingo-Benidorm-002240"));

		}

	}

	@Test
	public void testConvertMedia()
	{
		final MediaModel mediaModel = new MediaModel();
		mediaModel.setDescription(DESCRIPTION);
		mediaModel.setURL("/holiday/accommodation/location/Benidorm/Flamingo-Benidorm-002240");

		final MediaViewData expectedResult = carouselDataPopulator.convertMedia(mediaModel);
		assertNotNull(expectedResult);
		assertThat(expectedResult.getDescription(), is(DESCRIPTION));
		assertThat(expectedResult.getMainSrc(), is("/holiday/accommodation/location/Benidorm/Flamingo-Benidorm-002240"));
	}

	@Test
	public void testConvertMediaNull()
	{
		final MediaModel model = null;
		final MediaViewData expectedResult = carouselDataPopulator.convertMedia(model);

		assertNull(expectedResult);
	}

}
