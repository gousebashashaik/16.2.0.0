/**
 * 
 */
package uk.co.portaltech.tui.populators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.session.SessionService;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mvel2.ast.AssertNode;

import uk.co.portaltech.travel.enums.BrandType;
import uk.co.portaltech.travel.model.BenefitModel;
import uk.co.portaltech.travel.model.ProductRangeCategoryModel;
import uk.co.portaltech.travel.model.ProductRangeModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.travel.services.ProductRangeService;
import uk.co.portaltech.tui.brand.BrandDetails;
import uk.co.portaltech.tui.resolvers.TUIUrlResolver;
import uk.co.portaltech.tui.web.view.data.MediaViewData;
import uk.co.portaltech.tui.web.view.data.ProductRangeCategoryViewData;

import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;


/**
 * @author vinodkumar.g
 *
 */
public class ProductRangeCategoryCarouselOverviewPopulatorTest {

	@InjectMocks
	ProductRangeCategoryCarouselOverviewPopulator productRangeCategoryCarouselOverviewPopulator=new ProductRangeCategoryCarouselOverviewPopulator();
	
	@Mock
    private MediaDataPopulator                        mediaDataPopoulator;
	@Mock
    private TUIUrlResolver<ProductRangeModel> tuiCategoryModelUrlResolver;
	@Mock
    private ProductRangeService                       productRangeService;
	@Mock
    private FeatureService                            featureService;
    @Mock
	private SessionService                            sessionService;
    @Mock
    private CMSSiteService                    siteService;

	
	ProductRangeCategoryModel source;
	ProductRangeCategoryViewData target;
	
	
	@Before
	public void setUp() throws Exception {
		
		MockitoAnnotations.initMocks(this);
		
		source=new ProductRangeCategoryModel();
		target=new ProductRangeCategoryViewData();
		
		CategoryModel categoryModel=new ProductRangeModel();
		MediaModel mediaModel=new MediaModel(); 
		
		categoryModel.setCode("code");
		categoryModel.setPicture(mediaModel);
		
		List<CategoryModel> categoryModels=new ArrayList<CategoryModel>();
		categoryModels.add(categoryModel);
		
		source.setCategories(categoryModels);
	}

	/**
	 * Test method for {@link uk.co.portaltech.tui.populators.ProductRangeCategoryCarouselOverviewPopulator#populate(uk.co.portaltech.travel.model.ProductRangeCategoryModel, uk.co.portaltech.tui.web.view.data.ProductRangeCategoryViewData)}.
	 */
	@Test
	public void testPopulate() {
		
		
		List<BenefitModel> benefitModels=new ArrayList<BenefitModel>();
		BenefitModel benefitModel=new BenefitModel();
		benefitModel.setBrands(Arrays.asList( BrandType.FC));

		benefitModels.add(benefitModel);
		
		List<Object> features=new ArrayList<Object>();
		features.add("feature");
		
		BrandDetails brandDetails=new BrandDetails();
		List<String> relevantBrands=new ArrayList<String>();
		relevantBrands.add("tui");
		brandDetails.setRelevantBrands(relevantBrands);
		CatalogVersionModel catalogVersionModel=new CatalogVersionModel();
		
		
		Mockito.doNothing().when(mediaDataPopoulator).populate(Mockito.any(MediaModel.class), Mockito.any(MediaViewData.class));
		Mockito.when(tuiCategoryModelUrlResolver.resolve(Mockito.any(ProductRangeModel.class))).thenReturn("www.tui.com");
		Mockito.when(productRangeService.getProductRangeBenefits(Mockito.anyString(), Mockito.any(CatalogVersionModel.class), Mockito.anyList())).thenReturn(benefitModels);
	    Mockito.when(featureService.getFeatureValues(Mockito.anyString(), Mockito.any(ItemModel.class), Mockito.any(Date.class), Mockito.anyString())).thenReturn(features);
	    Mockito.when(sessionService.getAttribute(Mockito.anyString())).thenReturn(brandDetails);
	    Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(catalogVersionModel);
	    
        productRangeCategoryCarouselOverviewPopulator.populate(source, target);
        
        assertNotNull(source);
        assertNotNull(target);
        assertEquals(1, target.getProductRanges().size());
        assertEquals(1, target.getProductRanges().get(0).getBenefits().size());
        assertEquals("feature", target.getProductRanges().get(0).getBenefits().get(0).getName());
        assertEquals("feature", target.getProductRanges().get(0).getBenefits().get(0).getDescription());
	    
	}

}
