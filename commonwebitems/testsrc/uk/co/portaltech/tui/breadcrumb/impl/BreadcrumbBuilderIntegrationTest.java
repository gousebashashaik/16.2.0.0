/**
 *
 */
package uk.co.portaltech.tui.breadcrumb.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import uk.co.portaltech.travel.enums.LocationType;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.tui.breadcrumb.BreadCrumb;
import uk.co.portaltech.tui.services.DurationHaulTypeService;


/**
 * @author sunilkumar.sahu
 *
 */
@IntegrationTest
public class BreadcrumbBuilderIntegrationTest  extends ServicelayerTransactionalTest {


	@InjectMocks
	private BreadcrumbBuilder defaultBreadcrumbBuilder=new BreadcrumbBuilder();
	
	private ModelService modelService;

	private ProductModel prodModel;
	private CatalogModel catalogModel;
	private CatalogVersionModel catalogVersion;
	private LocationModel countryModel;
	private LocationModel regionModel;
	private LocationModel destinationModel;
	private LocationModel resortModel;

	private List<CategoryModel> countryCategoryModels = new ArrayList<CategoryModel>();
	private List<CategoryModel> regionCategoryModels = new ArrayList<CategoryModel>();
	private List<CategoryModel> destinationCategoryModels = new ArrayList<CategoryModel>();
	private List<CategoryModel> resortCategoryModels = new ArrayList<CategoryModel>();


	@Before
	public void setUp()
	{
				catalogModel = new CatalogModel();
				catalogModel.setId("fc_catalog");
				catalogVersion = new CatalogVersionModel();
				catalogVersion.setVersion("Staged");
				catalogVersion.setCatalog(catalogModel);




				countryModel = new LocationModel();
				countryModel.setCatalogVersion(catalogVersion);
				countryModel.setCode("country1");
				countryModel.setName("Spain",Locale.UK);
				countryModel.setType(LocationType.COUNTRY);
				countryCategoryModels.add(countryModel);

				regionModel = new LocationModel();
				regionModel.setCatalogVersion(catalogVersion);
				regionModel.setCode("region1");
				regionModel.setName("Canary Island",Locale.UK);
				regionModel.setType(LocationType.REGION);
				regionCategoryModels.add(regionModel);

				destinationModel =new LocationModel();
				destinationModel.setCatalogVersion(catalogVersion);
				destinationModel.setCode("d001");
				destinationModel.setName("Lanzarote",Locale.UK);
				destinationModel.setType(LocationType.DESTINATION);
				destinationCategoryModels.add(destinationModel);

				resortModel =new LocationModel();
				resortModel.setCatalogVersion(catalogVersion);
				resortModel.setCode("resort1");
				resortModel.setName("Playa Blanca",Locale.UK);
				resortModel.setType(LocationType.RESORT);
				resortCategoryModels.add(resortModel);
	}

	/**
	 * product hierarchy
	 * Country-->Region--------->Destination-->Resort---------->Product
	 * Spain---->Canary Island-->Lanzarote---->Playa Blanca---->Product
	 */
	@Test
	public void testgetBreadcrumbsInNormalScenario()
	{
		destinationModel.setCategories(resortCategoryModels);
		regionModel.setCategories(destinationCategoryModels);
		countryModel.setCategories(regionCategoryModels);

		/*modelService.save(countryModel);
		modelService.save(regionModel);
		modelService.save(destinationModel);
		modelService.save(resortModel);*/

		prodModel = new ProductModel();
		prodModel.setCatalogVersion(catalogVersion);
		prodModel.setCode("prod001");
		prodModel.setSupercategories(resortCategoryModels);


		List<BreadCrumb> breadCrumbList = defaultBreadcrumbBuilder.getBookflowAccommPageBreadcrumbs(prodModel);

		assertNotNull(breadCrumbList);
		
	}

	/**
	 * product hierarchy
	 * Country-->Region--------->Resort---------->Product
	 * Spain---->Canary Island-->Playa Blanca---->Product
	 */
	@Test
	public void testgetBreadcrumbsIfResortsUnderRegions()
	{
		regionModel.setCategories(resortCategoryModels);
		countryModel.setCategories(regionCategoryModels);
/*
		modelService.save(countryModel);
		modelService.save(regionModel);
		modelService.save(resortModel);*/

		prodModel = new ProductModel();
		prodModel.setCatalogVersion(catalogVersion);
		prodModel.setCode("prod001");
		prodModel.setSupercategories(resortCategoryModels);


		List<BreadCrumb> breadCrumbList = defaultBreadcrumbBuilder.getBookflowAccommPageBreadcrumbs(prodModel);

		assertNotNull(breadCrumbList);
		
	}

	/**
	 * product hierarchy
	 * Country-->Resort---------->Product
	 * Spain---->Playa Blanca---->Product
	 */
	@Test
	public void testgetBreadcrumbsIfResortsUnderCountry()
	{
		countryModel.setCategories(resortCategoryModels);
//



		prodModel = new ProductModel();
		prodModel.setCatalogVersion(catalogVersion);
		prodModel.setCode("prod001");
		prodModel.setSupercategories(resortCategoryModels);


		List<BreadCrumb> breadCrumbList = defaultBreadcrumbBuilder.getBookflowAccommPageBreadcrumbs(prodModel);

		assertNotNull(breadCrumbList);
		
	}

	/**
	 * product hierarchy
	 * Country-->Region--------->Destination-->Product
	 * Spain---->Canary Island-->Lanzarote---->Product
	 */
	@Test
	public void testgetBreadcrumbsIfProductUnderDestination()
	{
		regionModel.setCategories(destinationCategoryModels);
		countryModel.setCategories(regionCategoryModels);




		prodModel = new ProductModel();
		prodModel.setCatalogVersion(catalogVersion);
		prodModel.setCode("prod001");
		prodModel.setSupercategories(destinationCategoryModels);


		List<BreadCrumb> breadCrumbList = defaultBreadcrumbBuilder.getBookflowAccommPageBreadcrumbs(prodModel);

		assertNotNull(breadCrumbList);
		
	}

	/**
	 * product hierarchy
	 * Country-->Destination-->Product
	 * Spain---->Lanzarote---->Product
	 */
	@Test
	public void testgetBreadcrumbsIfProductUnderDestinationWithoutRegion()
	{
		countryModel.setCategories(destinationCategoryModels);



		prodModel = new ProductModel();
		prodModel.setCatalogVersion(catalogVersion);
		prodModel.setCode("prod001");
		prodModel.setSupercategories(destinationCategoryModels);


		List<BreadCrumb> breadCrumbList = defaultBreadcrumbBuilder.getBookflowAccommPageBreadcrumbs(prodModel);

		assertNotNull(breadCrumbList);
		
	}
	//===========================================================================================
	/**
	 * product hierarchy
	 * Country-->Region--------->Destination-->Resort---------->Product
	 * Spain---->Canary Island-->Lanzarote---->Playa Blanca---->Product
	 */
	@Test
	public void testgetLocationMapInNormalScenario()
	{
		destinationModel.setCategories(resortCategoryModels);
		regionModel.setCategories(destinationCategoryModels);
		countryModel.setCategories(regionCategoryModels);

		/*modelService.save(countryModel);
		modelService.save(regionModel);
		modelService.save(destinationModel);
		modelService.save(resortModel);
*/
		prodModel = new ProductModel();
		prodModel.setCatalogVersion(catalogVersion);
		prodModel.setCode("prod001");
		prodModel.setSupercategories(resortCategoryModels);


		Map<String, String> locationMap = defaultBreadcrumbBuilder.getLocationMap(prodModel);

		assertNotNull(locationMap);
		
	}

	/**
	 * product hierarchy
	 * Country-->Destination-->Product
	 * Spain---->Lanzarote---->Product
	 */
	@Test
	public void testgetLocationMapIfProductUnderDestinationWithoutRegion()
	{
		countryModel.setCategories(destinationCategoryModels);



		prodModel = new ProductModel();
		prodModel.setCatalogVersion(catalogVersion);
		prodModel.setCode("prod001");
		prodModel.setSupercategories(destinationCategoryModels);


		Map<String, String> locationMap = defaultBreadcrumbBuilder.getLocationMap(prodModel);

		assertNotNull(locationMap);
		assertEquals("Lanzarote", locationMap.get("destination"));
		assertEquals("Spain", locationMap.get("country"));
	}

	/**
	 * product hierarchy
	 * Country-->Region--------->Destination-->Product
	 * Spain---->Canary Island-->Lanzarote---->Product
	 */
	@Test
	public void testgetLocationMapIfProductUnderDestination()
	{
		regionModel.setCategories(destinationCategoryModels);
		countryModel.setCategories(regionCategoryModels);

		/*modelService.save(countryModel);
		modelService.save(regionModel);
*/
		prodModel = new ProductModel();
		prodModel.setCatalogVersion(catalogVersion);
		prodModel.setCode("prod001");
		prodModel.setSupercategories(destinationCategoryModels);


		Map<String, String> locationMap = defaultBreadcrumbBuilder.getLocationMap(prodModel);

		assertNotNull(locationMap);
		
	}

	/**
	 * product hierarchy
	 * Country-->Region--------->Resort---------->Product
	 * Spain---->Canary Island-->Playa Blanca---->Product
	 */
	@Test
	public void testgetLocationMapIfResortsUnderRegions()
	{
		regionModel.setCategories(resortCategoryModels);
		countryModel.setCategories(regionCategoryModels);
/*
		modelService.save(countryModel);
		modelService.save(regionModel);
		modelService.save(resortModel);*/

		prodModel = new ProductModel();
		prodModel.setCatalogVersion(catalogVersion);
		prodModel.setCode("prod001");
		prodModel.setSupercategories(resortCategoryModels);


		Map<String, String> locationMap = defaultBreadcrumbBuilder.getLocationMap(prodModel);

		assertNotNull(locationMap);
		
	}

	/**
	 * product hierarchy
	 * Country-->Resort---------->Product
	 * Spain---->Playa Blanca---->Product
	 */
	@Test
	public void testgetLocationMapIfResortsUnderCountry()
	{
		countryModel.setCategories(resortCategoryModels);

		/*modelService.save(countryModel);
		modelService.save(resortModel);*/

		prodModel =new ProductModel();
		prodModel.setCatalogVersion(catalogVersion);
		prodModel.setCode("prod001");
		prodModel.setSupercategories(resortCategoryModels);


		Map<String, String> locationMap = defaultBreadcrumbBuilder.getLocationMap(prodModel);

		assertNotNull(locationMap);


	}
}
