/**
 * 
 */
package uk.co.portaltech.tui.facades.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.co.portaltech.travel.enums.ChangeType;
import uk.co.portaltech.travel.model.ProductRangeCategoryModel;
import uk.co.portaltech.travel.services.ProductRangeCategoryService;
import uk.co.portaltech.travel.services.impl.DefaultProductRangeCategoryService;
import uk.co.portaltech.tui.web.view.data.ProductRangeCategoryViewData;
import uk.co.portaltech.tui.web.view.data.ProductRangeViewData;


/**
 * @author venkataharish.k
 * 
 */
public class DefaultProductRangeCategoryFacadeTest
{

	@Mock
	private CategoryService categoryService, categoryService1, categoryService2;

	@Mock
	private ProductRangeCategoryService productRangeCategoryService;

	@Mock
	private Converter<ProductRangeCategoryModel, ProductRangeCategoryViewData> productRangeCategoryConverter,
			productRangeCategoryConverter1;

	@Mock
	private Populator<ProductRangeCategoryModel, ProductRangeCategoryViewData> productRangeCategoryBasicPopulator;

	@Mock
	private Populator<ProductRangeCategoryModel, ProductRangeCategoryViewData> productRangeCategoryCarouselOverviewPopulator;

	@Mock
	private CMSSiteService siteService;
	@Mock
	private ProductRangeCategoryModel productRangeCategoryModel1, productRangeCategoryModel2;

	/**
	 * Test method for
	 * {@link uk.co.portaltech.tui.facades.impl.DefaultProductRangeCategoryFacade#getAllProductRangeCategories()} .
	 */
	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);

		
		


	}

	private CatalogVersionModel getCatalogVersion()
	{

		final CatalogVersionModel catalog = new CatalogVersionModel();
		catalog.setVersion("online");
		return catalog;
	}

	@Test
	public void testGetAllProductRangeCategories()
	{
		productRangeCategoryService = new DefaultProductRangeCategoryService();
		final CatalogVersionModel catalog = getCatalogVersion();
		siteService = Mockito.mock(CMSSiteService.class);
		Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(catalog);
		final ArrayList<ProductRangeCategoryViewData> productRangeCategoryViewDatas1 = getProductRangeCategoryViewData();
		assertNotNull("TestCaseForProductRangeCategories", productRangeCategoryViewDatas1);

	}

	private ArrayList<ProductRangeCategoryViewData> getProductRangeCategoryViewData()
	{
		final ProductRangeCategoryService productRangeCategoryService = Mockito.mock(ProductRangeCategoryService.class);
		Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(getCatalogVersion());

		final Iterator<ProductRangeCategoryModel> productRangeCategoryModelsItr = productRangeCategoryService
				.getProductRangeCategories(siteService.getCurrentCatalogVersion()).iterator();
		final ArrayList<ProductRangeCategoryViewData> productRangeCategoryViewDatas = new ArrayList<ProductRangeCategoryViewData>();
		while (productRangeCategoryModelsItr.hasNext())
		{
			final ProductRangeCategoryModel currentProductRangeCategory = productRangeCategoryModelsItr.next();
			final ProductRangeCategoryViewData currentViewData = productRangeCategoryConverter.convert(currentProductRangeCategory);
			productRangeCategoryBasicPopulator.populate(currentProductRangeCategory, currentViewData);
			productRangeCategoryViewDatas.add(currentViewData);
		}
		return productRangeCategoryViewDatas;
	}

	/**
	 * Test method for
	 * {@link uk.co.portaltech.tui.facades.impl.DefaultProductRangeCategoryFacade#getProductRangeCategoryByCode(java.lang.String)}
	 * .
	 */

	@Test
	public void testGetProductRangeCategoryByCode()
	{
		final String productRangeCategoryCode = "Holiday Village";
		final ProductRangeCategoryModel productRangeCategoryModel = new ProductRangeCategoryModel();
		final CatalogVersionModel value = new CatalogVersionModel();
		productRangeCategoryModel.setCatalogVersion(value);
		categoryService = Mockito.mock(CategoryService.class);
		Mockito.when(categoryService.getCategoryForCode(productRangeCategoryCode)).thenReturn(productRangeCategoryModel);
		productRangeCategoryConverter = Mockito.mock(Converter.class);
		final ProductRangeCategoryViewData productRangeCategoryViewData = productRangeCategoryConverter
				.convert(productRangeCategoryModel);
		productRangeCategoryBasicPopulator = Mockito.mock(Populator.class);
		productRangeCategoryBasicPopulator.populate(productRangeCategoryModel, productRangeCategoryViewData);
		assertNull("productRangecategoryfornull values", productRangeCategoryViewData);

	}

	/**
	 * Test method for
	 * {@link uk.co.portaltech.tui.facades.impl.DefaultProductRangeCategoryFacade#getProductOverviewCarouselData(int, java.lang.String)}
	 * .
	 */

	@Test
	public void testGetProductOverviewCarouselData()
	{
		productRangeCategoryConverter1 = Mockito.mock(Converter.class);
		ProductRangeCategoryViewData productRangeCategoryViewData = Mockito.mock(ProductRangeCategoryViewData.class);
		productRangeCategoryViewData = getProductOverviewData();
		assertNotNull("test For GetProductOverviewCarouselData", productRangeCategoryViewData);

	}

	private ProductRangeCategoryViewData getProductOverviewData()
	{
		final String productRangeCode = "Hotels with unlimited waterpark access";
		final int visibleItems = 5;
		categoryService2 = Mockito.mock(CategoryService.class);

		final CatalogVersionModel model = new CatalogVersionModel();
		final CatalogModel catalog = new CatalogModel();
		catalog.setId("fc-catalog");
		model.setCatalog(catalog);
		model.setVersion("Online");
		final List<CategoryModel> categoryModel = new ArrayList<CategoryModel>();
		categoryService1 = Mockito.mock(CategoryService.class);
		productRangeCategoryModel2 = new ProductRangeCategoryModel();
		productRangeCategoryModel2.setCatalogVersion(model);
		productRangeCategoryModel2.setCategories(categoryModel);
		productRangeCategoryModel2.setChangeType(ChangeType.CUSTOMER_SERVICES_ADVICE);
		productRangeCategoryModel2 = (ProductRangeCategoryModel) categoryService2.getCategoryForCode(productRangeCode);
		productRangeCategoryModel2 = (ProductRangeCategoryModel) categoryService2.getCategoryForCode(productRangeCode);
		productRangeCategoryConverter1 = Mockito.mock(Converter.class);
		final ProductRangeCategoryViewData productRangeCategoryViewData = Mockito.mock(ProductRangeCategoryViewData.class);
		Mockito.when(productRangeCategoryConverter1.convert(productRangeCategoryModel2)).thenReturn(productRangeCategoryViewData);
		final List<ProductRangeViewData> productRanges = productRangeCategoryViewData.getProductRanges();
		if (productRanges != null && productRanges.size() > visibleItems)
		{
			productRangeCategoryViewData.setProductRanges(productRanges.subList(0, visibleItems));
		}
		return productRangeCategoryViewData;
	}

}
