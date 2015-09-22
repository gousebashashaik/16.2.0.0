/**
 *
 */
package uk.co.portaltech.tui.facades;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import uk.co.portaltech.travel.enums.LocationType;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.tui.web.view.data.CountryViewData;
import uk.co.portaltech.tui.web.view.data.DestinationData;

/**
 * @author sunilkumar.sahu
 *
 */
@IntegrationTest
public class LocationFacadeIntegrationTest extends
		ServicelayerTransactionalTest {

	@Resource
	private LocationFacade locationFacade;

	@Resource
	private ModelService modelService;

	private CatalogModel catalogModel;
	private CatalogVersionModel catalogVersion;

	private LocationModel locationModel, locationModel2, locationModel3;
	private LocationModel region, destination;

	private static final String country1 = "IND";
	private static final String country2 = "TUR";
	private static final String country3 = "BGR";

	private static final String region1 = "Goa";
	private static final String destination1 = "South Goa";
	private List<String> brandList = null;

	@Before
	public void setUp() {
		catalogModel = new CatalogModel();
		catalogModel.setId("fc_catalog");
		catalogVersion = new CatalogVersionModel();
		catalogVersion.setVersion("Staged");

		modelService.save(catalogModel);
		catalogVersion.setCatalog(catalogModel);

		modelService.save(catalogVersion);
		modelService.save(LocationType.COUNTRY);

		locationModel = modelService.create(LocationModel.class);
		locationModel.setCode(country1);
		locationModel.setType(LocationType.COUNTRY);
		locationModel.setCatalogVersion(catalogVersion);

		// create one destination
		destination = modelService.create(LocationModel.class);
		destination.setCode(destination1);
		destination.setType(LocationType.DESTINATION);
		destination.setCatalogVersion(catalogVersion);

		final List<CategoryModel> destModels = new ArrayList<CategoryModel>();
		destModels.add(destination);

		// create one region
		region = modelService.create(LocationModel.class);
		region.setCode(region1);
		region.setType(LocationType.REGION);
		region.setCatalogVersion(catalogVersion);

		// add the created destination list to the region
		region.setCategories(destModels);

		final List<CategoryModel> regionModels = new ArrayList<CategoryModel>();
		regionModels.add(region);

		// add the created region list to the location
		locationModel.setCategories(regionModels);
		brandList = new ArrayList<String>();
		brandList.add("TH");
		brandList.add("TH_FC");

	}

	/*@Test
	public void testgetAllCountries() {
		
		

		locationModel2 = modelService.create(LocationModel.class);
		locationModel2.setCode(country2);
		locationModel2.setType(LocationType.COUNTRY);
		locationModel2.setCatalogVersion(catalogVersion);

		locationModel3 = modelService.create(LocationModel.class);
		locationModel3.setCode(country3);
		locationModel3.setType(LocationType.COUNTRY);
		locationModel3.setCatalogVersion(catalogVersion);

		modelService.save(locationModel);
		modelService.save(locationModel2);
		modelService.save(locationModel3);

		List<DestinationData> locations1 = locationFacade.getAllCountries(brandList);
		assertNotNull(locations1);
		System.out.println("locations1-->> " + locations1);
		System.out.println("locations1.getID()-->> "
				+ locations1.get(0).getId());
		System.out.println("locations1.getName()-->> "
				+ locations1.get(0).getName());
		System.out.println("locations1.getType()-->> "
				+ locations1.get(0).getType());
		System.out.println("locations1.get(0).getName()-->> "
				+ locations1.get(0).getName());
		assertEquals("COUNTRY", locations1.get(0).getType());
		assertEquals("IND", locations1.get(0).getId());
	}*/

	@Test
	public void testgetChildCategories() {

		modelService.save(locationModel);
		CountryViewData locations1 = locationFacade.getChildCategories("IND",brandList);
		assertNotNull(locations1);
		System.out.println("locations1-->> " + locations1);
		System.out.println("locations1.getID()-->> " + locations1.getId());
		System.out.println("locations1.getName()-->> " + locations1.getName());
		System.out.println("locations1.getType()-->> " + locations1.getType());

		assertEquals("IND", locations1.getId());

		assertEquals("Goa", locations1.getChildren().get(0).getId());
		assertEquals("REGION", locations1.getChildren().get(0).getType());

		assertEquals("South Goa", locations1.getChildren().get(0).getChildren()
				.get(0).getId());
		assertEquals("DESTINATION", locations1.getChildren().get(0)
				.getChildren().get(0).getType());

	}

}
