/**
 * 
 */
package uk.co.portaltech.tui.facades.impl;

import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.model.BenefitModel;
import uk.co.portaltech.travel.model.ProductRangeModel;
import uk.co.portaltech.travel.model.ProductUspModel;
import uk.co.portaltech.travel.services.ProductRangeService;
import uk.co.portaltech.tui.facades.impl.ProductRangeFacadeImpl;
import uk.co.portaltech.tui.web.view.data.BenefitViewData;
import uk.co.portaltech.tui.web.view.data.ProductRangeCategoryViewData;
import uk.co.portaltech.tui.web.view.data.ProductRangeCollectionViewData;
import uk.co.portaltech.tui.web.view.data.ProductRangeViewData;
import uk.co.portaltech.tui.web.view.data.ProductUspViewData;

/**
 * @author arun.y
 *
 */

@UnitTest
public class ProductRangeFacadeImplTest {
	
	@InjectMocks
	private ProductRangeFacadeImpl productRangeFacadeImpl = new ProductRangeFacadeImpl();
	
	@Mock
	private ProductRangeService productRangeService;
	
	@Mock
	private CMSSiteService siteService;
	
	@Mock
	private Converter<BenefitModel, BenefitViewData> defaultBenefitConverter;
	
	@Mock
	private Converter<Collection<CategoryModel>, Map<String, ProductRangeCategoryViewData>> defaultProductRangeCollectionConverter;
	
	@Mock
	private CategoryService categoryService;
	
	@Mock
	private Converter<ProductRangeModel, ProductRangeViewData> productRangeConverter;
	
	@Mock
	private Converter<ProductRangeModel, ProductRangeCollectionViewData> defaultProductRangeProductConverter;
	
	@Mock
	private Map<String, ProductRangeCategoryViewData> productRangeMap;
	
	private CatalogVersionModel catalogVersionModel;
	private BenefitModel benefitModel;
	private List<BenefitModel> benefitModelList;
	private BenefitViewData benefitViewData;
	private List<BenefitViewData> benefitViewDataList;
	private CatalogModel catalogModel;
	private Collection<CategoryModel> categoryModelCollection;
	private Map<String, ProductRangeCategoryViewData> productRangeCollection;
	private ProductRangeCategoryViewData productRangeCategoryViewData;
	private ProductRangeCollectionViewData productRangeCollectionViewData;
	private List<ProductRangeCollectionViewData> productRangeCollectionViewDataList;
	private ProductRangeModel productRangeModel;
	private ProductRangeViewData productRangeViewData;
	private List<String> uspList;
	private List<ProductUspViewData>  productUspViewDataList;
	private ProductUspViewData productUspViewData;
	private List<ProductUspModel> productUspList;

	@Before
	public void setUp() throws Exception 
	{
		MockitoAnnotations.initMocks(this);
		catalogVersionModel = new CatalogVersionModel();
		catalogModel = new CatalogModel();
		benefitModel = new BenefitModel();
		benefitModelList = new ArrayList<BenefitModel>();
		benefitViewData = new BenefitViewData();
    	benefitViewDataList = new ArrayList<BenefitViewData>();
    	productRangeCollection = new HashMap<String, ProductRangeCategoryViewData>();
    	productRangeCategoryViewData = new ProductRangeCategoryViewData();
    	productRangeCollectionViewData = new ProductRangeCollectionViewData();
    	productRangeCollectionViewDataList = new ArrayList<ProductRangeCollectionViewData>();
    	categoryModelCollection = new ArrayList<CategoryModel>();
    	productRangeModel = new ProductRangeModel();
    	productRangeViewData = new ProductRangeViewData();
    	uspList = new ArrayList<String>();
    	productUspViewDataList = new ArrayList<ProductUspViewData>();
    	productUspViewData = new ProductUspViewData();
    	productUspList = new ArrayList<ProductUspModel>();
	}
	
	private void dummyValueForCatalogVersionModel()
	{
		catalogModel.setId("FC_Online");
		catalogVersionModel.setCatalog(catalogModel);
		catalogVersionModel.setVersion("Online");
		catalogModel.setActiveCatalogVersion(catalogVersionModel);
	}
	
	private void dummyValueForBenefitViewData()
	{
		benefitModel.setDescription("Test Data of BenefitModel");
		benefitModelList.add(benefitModel);
		benefitViewData.setDescription("Test Data of BenefitViewData");
		benefitViewData.setName("FirstChoice");
		benefitViewData.setThunbnailUrl("www.firstchoice.co.uk");
		benefitViewDataList.add(benefitViewData);
	}
	
	private void dummyValueForProductRangeViewData()
	{
		productRangeViewData.setPictureUrl("www.firstchoice.co.uk");
		String usp1 = "Smart bedrooms";
		String usp2 = "Top dining scenes";
		uspList.add(usp1);
		uspList.add(usp2);
		productRangeViewData.setUsps(uspList);
		productUspViewData.setCode("FSP");
		productUspViewData.setDescription("Test Data of ProductUspViewData");
		productUspViewData.setName("FirstChoice");
		productUspViewData.setPictureUrl("www.firstchoice.co.uk");
		productUspViewData.setThumbnailUrl("www.firstchoice.co.uk");
		productUspViewDataList.add(productUspViewData);		
		productRangeViewData.setProductUsps(productUspViewDataList);
	}
	
	private void dummyValueForProductRangeModel()
	{
		productRangeModel.setCommercialPriority(Integer.valueOf(1));
	}
	
	private void dummyValueForProductRangeCollectionViewData()
	{
		productRangeCollectionViewData.setProductDescription("Splash World Holidays");
		productRangeCollectionViewData.setProductName("Splashworld");
		productRangeCollectionViewData.setProductURL("www.firstchoice.co.uk");
	}
	
	private void dummyValueForProductRangeCategoryViewData() 
	{
		productRangeCollectionViewDataList.add(productRangeCollectionViewData);
		productRangeCategoryViewData.setPrCategoryDescription("Test Data of Holiday Village");
		productRangeCategoryViewData.setPrCategoryName("Holiday Village");
		productRangeCategoryViewData.setPrCollection(productRangeCollectionViewDataList);
	}
	
	private void dummyValueForProductRangeCollection()
	{
		productRangeCollection.put("FHV", productRangeCategoryViewData);
	}

	@Test
	public void testGetProductRangeBenefitsData() 
	{
		dummyValueForCatalogVersionModel();	
		dummyValueForBenefitViewData();
		String productRangeCode = "FHV";
		when(siteService.getCurrentCatalogVersion()).thenReturn(catalogVersionModel);
		when(productRangeService.getProductRangeBenefits(productRangeCode, catalogVersionModel, null)).thenReturn(benefitModelList);
		when(defaultBenefitConverter.convert(benefitModel)).thenReturn(benefitViewData);
		Assert.assertEquals("Test Data of BenefitViewData",benefitViewData.getDescription());
		Assert.assertEquals("FirstChoice",benefitViewData.getName());
		Assert.assertEquals("www.firstchoice.co.uk",benefitViewData.getThunbnailUrl());
	}

	@Test
	public void testGetAllProductRanges() 
	{
		dummyValueForProductRangeCollectionViewData();
		dummyValueForProductRangeCategoryViewData();
		dummyValueForProductRangeCollection();
		when(productRangeService.getAllProductRangeCollection(null)).thenReturn(categoryModelCollection);
		when(defaultProductRangeCollectionConverter.convert(categoryModelCollection, productRangeCollection)).thenReturn(productRangeCollection);
		Assert.assertEquals(productRangeCategoryViewData, productRangeCollection.get("FHV"));
	}
	
	@Test
	public void testGetProductRangeHighlights() 
	{		
		dummyValueForProductRangeViewData();
		dummyValueForProductRangeModel();
		String productRangeCode = "FPR";
		when((ProductRangeModel) categoryService.getCategoryForCode(productRangeCode)).thenReturn(productRangeModel);
		when(productRangeConverter.convert(productRangeModel)).thenReturn(productRangeViewData);
		Assert.assertEquals(Integer.valueOf(1), productRangeModel.getCommercialPriority());
		Assert.assertEquals(uspList, productRangeViewData.getUsps());
		Assert.assertEquals(productUspViewDataList, productRangeViewData.getProductUsps());
		Assert.assertEquals("www.firstchoice.co.uk", productRangeViewData.getPictureUrl());
	}
     
	@Test
	public void testGetProduct() 
	{
		dummyValueForCatalogVersionModel();
		dummyValueForProductRangeModel();
		dummyValueForProductRangeCollectionViewData();
		String productRangeCode = "FSP";
		when(siteService.getCurrentCatalogVersion()).thenReturn(catalogVersionModel);
		when(productRangeService.getProductForProductRange(productRangeCode, catalogVersionModel, null)).thenReturn(productRangeModel);
		when(defaultProductRangeProductConverter.convert(productRangeModel)).thenReturn(productRangeCollectionViewData);
		Assert.assertEquals(Integer.valueOf(1), productRangeModel.getCommercialPriority());
		Assert.assertEquals("Splashworld", productRangeCollectionViewData.getProductName());
		Assert.assertEquals("Splash World Holidays", productRangeCollectionViewData.getProductDescription());
	}
	
	@Test
	public void testGetProductRange()
	{
		dummyValueForProductRangeCollectionViewData();
		dummyValueForProductRangeCategoryViewData();
		String productRangeCategoryCode = "Premier"; 
		when(productRangeMap.get(productRangeCategoryCode)).thenReturn(productRangeCategoryViewData);
		Assert.assertEquals("Test Data of Holiday Village", productRangeCategoryViewData.getPrCategoryDescription());
		Assert.assertEquals("Holiday Village", productRangeCategoryViewData.getPrCategoryName());
		Assert.assertEquals(productRangeCollectionViewDataList, productRangeCategoryViewData.getPrCollection());		
	}

	@Test
	public void testGetProductRangeUsps() 
	{
		dummyValueForProductRangeModel();
		dummyValueForCatalogVersionModel();
		dummyValueForProductRangeViewData();
		String productRangeCode = "FPF";
		when((ProductRangeModel) categoryService.getCategoryForCode(productRangeCode)).thenReturn(productRangeModel);
		when(siteService.getCurrentCatalogVersion()).thenReturn(catalogVersionModel);
		when(productRangeService.getProductRangeUsps(productRangeCode, siteService.getCurrentCatalogVersion(), null)).thenReturn(productUspList);
		when(productRangeConverter.convert(productRangeModel)).thenReturn(productRangeViewData);
		Assert.assertEquals(Integer.valueOf(1), productRangeModel.getCommercialPriority());
		Assert.assertEquals("Online", catalogVersionModel.getVersion());
		Assert.assertEquals(uspList, productRangeViewData.getUsps());
		Assert.assertEquals(productUspViewDataList, productRangeViewData.getProductUsps());
	}
	
	@Test
	public void testGetProductRangeByCode() 
	{
		dummyValueForProductRangeModel();
		dummyValueForProductRangeViewData();
		String productRangeCode = "FHV";
		when((ProductRangeModel) categoryService.getCategoryForCode(productRangeCode)).thenReturn(productRangeModel);
		when(productRangeConverter.convert(productRangeModel)).thenReturn(productRangeViewData);
		Assert.assertEquals(Integer.valueOf(1), productRangeModel.getCommercialPriority());
		Assert.assertEquals(uspList, productRangeViewData.getUsps());
		Assert.assertEquals(productUspViewDataList, productRangeViewData.getProductUsps());
		Assert.assertEquals("www.firstchoice.co.uk", productRangeViewData.getPictureUrl());
	}	
}
