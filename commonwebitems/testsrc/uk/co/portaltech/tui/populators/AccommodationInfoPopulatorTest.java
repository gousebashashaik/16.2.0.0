/**
 * 
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.media.MediaContainerModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.enums.BrandType;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.model.TransferTimeModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.resolvers.TUIUrlResolver;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;


/**
 * @author venkatasuresh.t
 * 
 */
public class AccommodationInfoPopulatorTest
{
	@InjectMocks
	AccommodationInfoPopulator accommodationInfoPopulator = new AccommodationInfoPopulator();

	@Mock
	private FeatureService featureService;

	@Mock
	private Map<String, List<Object>> results;

	@Mock
	private AccommodationModel sourceModel;

	@Mock
	private Collection<TransferTimeModel> transferTimes;

	@Mock
	private MediaPopulator heroImagesPopulator;

	@Mock
	private CategoryModel categoryModel;

	@Mock
	private TUIUrlResolver<AccommodationModel> tuiProductUrlResolver;

	AccommodationModel source;
	AccommodationViewData targetData;

	/**
	 * @throws java.lang.Exception
	 */

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		source = new AccommodationModel();
		targetData = new AccommodationViewData();
		final List<MediaContainerModel> galleryimages = new ArrayList<MediaContainerModel>();
		final MediaContainerModel mediaContainerModel = new MediaContainerModel();
		galleryimages.add(mediaContainerModel);

		source.setBrands(Arrays.asList(BrandType.FC));
		source.setCode("");
		source.setGalleryImages(galleryimages);
		final CategoryModel categoryModel = new CategoryModel();
		final Collection<CategoryModel> categoryModelList = new ArrayList<CategoryModel>();
		categoryModelList.add(categoryModel);
		source.setSupercategories(categoryModelList);
	}

	/**
	 * Test method for
	 * {@link uk.co.portaltech.tui.populators.AccommodationInfoPopulator#populate(uk.co.portaltech.travel.model.AccommodationModel, uk.co.portaltech.tui.web.view.data.AccommodationViewData)}
	 * .
	 */
	@Test
	public void testPopulate()
	{
		final List<MediaContainerModel> galleryimages = new ArrayList<MediaContainerModel>();
		final MediaContainerModel mediaContainerModel = new MediaContainerModel();
		galleryimages.add(mediaContainerModel);

		Mockito.when(
				featureService.getValuesForFeatures(Mockito.anyCollection(), Mockito.any(LocationModel.class),
						Mockito.any(Date.class), Mockito.anyString())).thenReturn(results);
		Mockito.when(sourceModel.getTransferTimes()).thenReturn(transferTimes);
		Mockito.doNothing().when(tuiProductUrlResolver).setOverrideSubPageType(Mockito.anyString());


		accommodationInfoPopulator.populate(source, targetData);

		Assert.assertNotNull(source);
		Assert.assertNotNull(targetData);

	}

}
