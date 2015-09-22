/**
 *
 */
package uk.co.portaltech.tui.breadcrumb.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import uk.co.portaltech.tui.breadcrumb.BreadCrumb;
import uk.co.portaltech.tui.components.model.BookflowProgressIndicatorComponentModel;

/**
 * @author sunilkumar.sahu
 *
 */
@IntegrationTest
public class BookflowProgressIndicatorIntegrationTest extends ServicelayerTransactionalTest {

	@Resource
	private BreadcrumbBuilder defaultBreadcrumbBuilder;
	@Resource
	private ModelService modelService;

	private CatalogModel catalogModel;
	private CatalogVersionModel catalogVersion;
	private BookflowProgressIndicatorComponentModel indicatorModel;

	private String hotel = "HOTEL";
	private String flights = "FLIGHTS";
	private String rooms = "ROOMS";
	private String extras = "EXTRAS";
	private String checkout = "CHECKOUT";
	private static final int INDICATORCOUNT = 5;
	private static final int ZERO = 0;
	private static final int ONE = 1;
	private static final int TWO = 2;
	private static final int THREE = 3;
	private static final int FOUR = 4;


	@Before
	public void setUp()
	{
				catalogModel = modelService.create(CatalogModel.class);
				catalogModel.setId("fc_catalog");
				catalogVersion = modelService.create(CatalogVersionModel.class);
				catalogVersion.setVersion("Staged");
				catalogVersion.setCatalog(catalogModel);

				modelService.save(catalogModel);
				modelService.save(catalogVersion);

				List<String> pages = new ArrayList<String>();
				pages.add(hotel);
				pages.add(flights);
				pages.add(rooms);
				pages.add(extras);
				pages.add(checkout);

				indicatorModel =  modelService.create(BookflowProgressIndicatorComponentModel.class);
				indicatorModel.setIndicatorCount(INDICATORCOUNT);
				indicatorModel.setPages(pages);
				indicatorModel.setUid("I001");
				indicatorModel.setCatalogVersion(catalogVersion);
	}

	@Test
	public void testgetBookflowProgressIndicators()
	{
		modelService.save(indicatorModel);
		List<BreadCrumb> breadCrumbList = defaultBreadcrumbBuilder.getBookflowProgressIndicators(indicatorModel);

		assertNotNull(breadCrumbList);
		assertEquals(hotel,breadCrumbList.get(ZERO).getName());
		assertEquals(flights,breadCrumbList.get(ONE).getName());
		assertEquals(rooms,breadCrumbList.get(TWO).getName());
		assertEquals(extras,breadCrumbList.get(THREE).getName());
		assertEquals(checkout,breadCrumbList.get(FOUR).getName());
		assertEquals(INDICATORCOUNT, breadCrumbList.size());
	}

	@Test
	public void testgetBookflowProgressIndicatorsForDifferentPages()
	{
		//only 4 pages
		List<String> pages = new ArrayList<String>();
		pages.add(hotel);
		pages.add(flights);
		pages.add(rooms);
		pages.add(extras);

		indicatorModel.setPages(pages);
		modelService.save(indicatorModel);
		List<BreadCrumb> breadCrumbList = defaultBreadcrumbBuilder.getBookflowProgressIndicators(indicatorModel);

		assertEquals(hotel,breadCrumbList.get(ZERO).getName());
		assertEquals(flights,breadCrumbList.get(ONE).getName());
		assertEquals(rooms,breadCrumbList.get(TWO).getName());
		assertEquals(extras,breadCrumbList.get(THREE).getName());

		assertEquals(INDICATORCOUNT-1, breadCrumbList.size());
	}

	@Test
	public void testgetBookflowProgressIndicatorsForNoPages()
	{
		//only 0 pages
		List<String> pages = new ArrayList<String>();

		indicatorModel.setPages(pages);
		modelService.save(indicatorModel);
		List<BreadCrumb> breadCrumbList = defaultBreadcrumbBuilder.getBookflowProgressIndicators(indicatorModel);
		//empty breadcrumblist
		assertEquals(0, breadCrumbList.size());
	}

}
