/**
 * 
 */
package uk.co.portaltech.tui.populators;

import static org.mockito.Mockito.mock;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.ProductRangeModel;
import uk.co.portaltech.travel.model.ProductUspModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.resolvers.TUIUrlResolver;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.MediaViewData;
import uk.co.portaltech.tui.web.view.data.ProductRangeViewData;
import uk.co.tui.async.logging.TUILogUtils;



/**
 * @author gopinath.n
 * 
 */
public class AccommodationProductRangePopulatorTest
{

	
	private final TUILogUtils LOGGER = new TUILogUtils("");

	@InjectMocks
	private final AccommodationProductRangePopulator accommodation = new AccommodationProductRangePopulator();

	@Mock
	private Converter<ProductRangeModel, ProductRangeViewData> productRangeConverter;

	@Mock
	private TUIUrlResolver<CategoryModel> tuiCategoryModelUrlResolver;

	@Mock
	private CategoryModel categoryModel;

	@Mock
	private MediaModel mediaModel;

	@Mock
	private FeatureService featureService;
	
	@Mock
	private ProductRangeModel productRangeModelMock;
	
	@Mock
	private TypeService typeService;
	
	private ProductUspModel productUspModel;
	private List<ProductUspModel> productUspModelList;
	private ProductRangeModel productRangeModel;
	private ProductRangeViewData productRangeViewData;
	private Populator<List<ProductUspModel>, 								ProductRangeViewData> productRangeUspsPopulator;
	private AccommodationModel source;
	private AccommodationViewData target;
	private AccommodationModel model;
	private AccommodationViewData viewData;
	private MediaViewData mediaViewData;
	private List<ProductRangeModel> productRangeModelList;
	private List<MediaViewData> mediaViewDataList;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		source = mock(AccommodationModel.class);

		productRangeModel = new ProductRangeModel();
		productRangeViewData = new ProductRangeViewData();
		mediaViewData = new MediaViewData();
		productUspModel = new ProductUspModel();
		target = new AccommodationViewData();

		target.settRating("4");
		target.setStayPeriod("7 days");
		target.setAccommodationType("AccommodationType");
		target.setFacilitiesUrl("/holiday/accommodation/facilities/Playa-Blanca/Holiday-Village-Flamingo-Beach-039425");
		productRangeModel.setCode("WF_COM_051-1");
		productRangeModel.setCommercialPriority(4);
		productRangeModel.setProductUsps(productUspModelList);
		mediaViewData.setCode("media1");
		mediaViewData.setCode("media2");
		mediaViewData.setCode("media3");
		mediaViewData.setCode("media4");
		mediaViewData.setCode("media5");
		mediaViewData.setCode("media6");
		mediaViewData.setCode("media7");
		mediaViewData.setCode("media8");
		mediaViewData.setCode("media9");
		mediaViewData.setCode("media10");
		mediaViewData.setCode("media11");
		mediaViewData.setCode("media12");
		mediaViewData.setMime("image/gif");
		mediaViewData.setDescription("description");
		mediaViewData.setMainSrc("mainSrc");
		mediaViewData.setAltText("altText");
		mediaViewData.setSize("658x370");
		productRangeViewData.setPictureUrl("/holiday/images/insp-product-wintersun4.jpg");
		productRangeViewData.setPictureUrl("/holiday/images/insp-product-premier.jpg");
		productRangeViewData.setPictureUrl("/holiday/images/insp-product-premier-families.jpg");
		productRangeViewData.setPictureUrl("/holiday/images/insp-product-beaches.jpg");



	}

	@Test
	public void testPopulate()
 {

		ProductRangeModel productRangeModel = new ProductRangeModel();
		Mockito.when(productRangeModelMock.getCode()).thenReturn("WF_COM_051-1");
		Mockito.when(productRangeModelMock.getProductUsps()).thenReturn(
				productUspModelList);
		Mockito.when(
				featureService.getValuesForFeatures(
						Arrays.asList(new String[] { "strapline", "name" }),
						productRangeModel, new Date(), "")).thenReturn(null);

		accommodation.populate(source, target);

		Assert.assertNotNull(source);
		Assert.assertNotNull(target);
		Assert.assertEquals(target.getStayPeriod(), "7 days");
		Assert.assertEquals(
				target.getFacilitiesUrl(),
				"/holiday/accommodation/facilities/Playa-Blanca/Holiday-Village-Flamingo-Beach-039425");
		Assert.assertEquals(target.getAccommodationType(), "AccommodationType");

	}

	
}
