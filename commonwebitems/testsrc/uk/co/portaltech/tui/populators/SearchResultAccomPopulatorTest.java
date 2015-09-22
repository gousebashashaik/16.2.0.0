/**
 *
 */
package uk.co.portaltech.tui.populators;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.hamcrest.MatcherAssert.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.annotation.ExpectedException;

import com.tripadvisor.content.api1.Hotel;


import uk.co.portaltech.thirdparty.tripadvisor.client.TripadvisorServiceImpl;
import uk.co.portaltech.travel.enums.LocationType;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.FeatureDescriptorModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.model.ProductRangeModel;
import uk.co.portaltech.travel.model.results.Holiday;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.web.view.data.SearchResultViewData;

/**
 *
 */
@IntegrationTest
public class SearchResultAccomPopulatorTest extends ServicelayerTransactionalTest{

    @Resource
    private SearchResultAccomPopulator searchResultsAccomPopulator;

	@Resource
	TripadvisorServiceImpl tripadvisorServiceImpl;

	@Resource
	private FeatureService featureService;

	@Resource
	private ModelService modelService;

    private SearchResultViewData target;

	private Hotel hotel;

	private LocationModel country, destination, resort;

	private ProductRangeModel productRange;

	private AccommodationModel accommodation;

	private CatalogModel catalogModel;

	private CatalogVersionModel catalogVersion;

	private static final String country1 = "ESP";

	private static final String destination1 = "Benidorm";

	private static final String resort1 = "000234";

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		initMocks(this);

		target = new SearchResultViewData();
		catalogModel = new CatalogModel();
		catalogModel.setId("fc_catalog");
		catalogVersion = new CatalogVersionModel();
		catalogVersion.setVersion("Staged");

		modelService.save(catalogModel);
		catalogVersion.setCatalog(catalogModel);

		modelService.save(catalogVersion);
		modelService.save(LocationType.COUNTRY);
		modelService.save(LocationType.DESTINATION);
		modelService.save(LocationType.RESORT);

		populateMockData();
	}

	/**
	 *
	 */
	private void populateMockData() {
		country = modelService.create(LocationModel.class);
		country.setCode(country1);
		country.setType(LocationType.COUNTRY);
		country.setCatalogVersion(catalogVersion);
		modelService.save(country);

		//create one destination
		destination = modelService.create(LocationModel.class);
		destination.setCode("BND");
		destination.setName(destination1);
		destination.setType(LocationType.DESTINATION);
		destination.setCatalogVersion(catalogVersion);
		modelService.save(destination);

		//create one Resort
		resort = modelService.create(LocationModel.class);
		resort.setCode(resort1);
		resort.setName("Samana");
		resort.setType(LocationType.RESORT);
		resort.setCatalogVersion(catalogVersion);
		modelService.save(resort);

		accommodation = modelService.create(AccommodationModel.class);
		accommodation.setCode("000111");
		accommodation.setCatalogVersion(catalogVersion);
		Collection<CategoryModel> superCats = new ArrayList<CategoryModel>();
		superCats.add(country);
		superCats.add(destination);
		superCats.add(resort);
		accommodation.setSupercategories(superCats);
		MediaModel thumbNailImage = new MediaModel();
		thumbNailImage.setCatalogVersion(catalogVersion);
		thumbNailImage.setCode("img");
		thumbNailImage.setURL("thumbnailImage.jpg");
		accommodation.setThumbnail(thumbNailImage);
		accommodation.setName("Riu Palace");
		modelService.save(accommodation);
	}

	@Test
	public void testPopulateCategoryContent() {
		searchResultsAccomPopulator.populateCategoryContent(target, accommodation.getSupercategories(), new HashMap<String, String>());
			assertThat(target.getAccommodation().getLocation().getDestination().getCode(), is("BND"));
		assertThat(target.getAccommodation().getLocation().getDestination().getName(), is("Benidorm"));

		assertThat(target.getAccommodation().getLocation().getResort().getCode(), is("000234"));
		assertThat(target.getAccommodation().getLocation().getResort().getName(), is("Samana"));

	}

	@SuppressWarnings("boxing")
	@Test
	@ExpectedException(UnknownIdentifierException.class)
	public void testWithoutStrapline()
	{
		//Create on Product Range
		productRange = modelService.create(ProductRangeModel.class);
		productRange.setCode("SPL");
		productRange.setName("SplashWorld");
		productRange.setCatalogVersion(catalogVersion);
		modelService.save(productRange);

		FeatureDescriptorModel featureDesc = new FeatureDescriptorModel();
		//featureDesc.setLocaleProvider(new LocaleProvider)
		featureDesc.setCode("strapline");
		featureDesc.setCatalogVersion(catalogVersion);
		
		Set<FeatureDescriptorModel> featureSet = new HashSet<FeatureDescriptorModel>();
		featureSet.add(featureDesc);
		productRange.setFeatureDescriptors(featureSet);
		modelService.save(featureDesc);

		Collection<CategoryModel> superCats = new ArrayList<CategoryModel>();
		superCats.add(country);
		superCats.add(destination);
		superCats.add(resort);
		superCats.add(productRange);
		accommodation.setSupercategories(superCats);
		modelService.save(accommodation);

		searchResultsAccomPopulator.populateCategoryContent(target, accommodation.getSupercategories(), new HashMap<String, String>());

		assertThat(target.getAccommodation().getDifferentiatedType(), is("SplashWorld"));
		assertThat(target.getAccommodation().isDifferentiatedProduct(), is(true));
		assertThat(target.getAccommodation().getDifferentiatedCode(), is("SPL"));


	}

	@SuppressWarnings("boxing")
	@Test
	public void testPopulateCategoryContentForProductRangeWithStrapline() {
		
		
		
	}

	@Test
	@ExpectedException(UnknownIdentifierException.class)
	public void testImageAndAccomName() {

		FeatureDescriptorModel featureDesc = new FeatureDescriptorModel();
		featureDesc.setCode("usps");
		featureDesc.setCatalogVersion(catalogVersion);
		Set<FeatureDescriptorModel> featureSet = new HashSet<FeatureDescriptorModel>();
		featureSet.add(featureDesc);
		accommodation.setFeatureDescriptors(featureSet);
		modelService.save(featureDesc);
		modelService.save(accommodation);

		searchResultsAccomPopulator.populate(accommodation, target);
		Assert.assertNotNull(target.getAccommodation().getImageUrl());
		Assert.assertNotNull(target.getAccommodation().getName());
		assertThat(target.getAccommodation().getImageUrl(), is("thumbnailImage.jpg"));
		assertThat(target.getAccommodation().getName(), is("Riu Palace"));

		
		
	}

	/**
	 * Test method
	 */
	@SuppressWarnings("boxing")
	@Test
	public void testGetTripAdvisorRating() {

		 Mockito.when(tripadvisorServiceImpl.getHotel("010725",0)).thenReturn(populateHotel() );

		 assertThat(target.getAccommodation().getTripAdvisorReviewCount(), is(500));
	}

	/**
	 * Test method
	 */
	@SuppressWarnings("boxing")
	Hotel populateHotel(){
		hotel = new Hotel();
		hotel.setTotalUserReviewCount(500);
		return hotel;
	}

}
