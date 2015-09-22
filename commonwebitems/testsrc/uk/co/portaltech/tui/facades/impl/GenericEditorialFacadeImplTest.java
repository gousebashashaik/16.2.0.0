/**
 * 
 */
package uk.co.portaltech.tui.facades.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.core.model.ItemModel;

import org.aspectj.lang.annotation.Before;
import org.jfree.util.Log;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.services.AccommodationService;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.facades.impl.GenericEditorialFacadeImpl;
import uk.co.portaltech.tui.web.view.data.FacilityViewData;

/**
 * @author kavita.na
 * 
 */
public class GenericEditorialFacadeImplTest {
	@Mock
	private FeatureService featureService;

	@Mock
	private CategoryService categoryService;

	@Mock
	private AccommodationService accommodationService;

	@Mock
	private CMSSiteService siteService;

	@Mock
	private GenericEditorialFacadeImpl facadeImpl;

	private ItemModel itemModel;
	private AccommodationModel accommodationModel, accommodationModel2;
	private CategoryModel categoryModel1, categoryModel2;
	private static final String FEATURE_CODE = "260";
	private static final String ACCOMMCODE2 = "007854";
	private static final String CATEGORY_CODE = "ca01";
	private static final String CATEGORY_NAME = "country";

	public AccommodationModel getAccommodationModel() {
		final CatalogVersionModel catalog = getCatalogVersion();
		AccommodationModel accom = new AccommodationModel();
		accom.setCode(ACCOMMCODE2);
		accom.setCatalogVersion(catalog);
		
		return accom;
	}

	public CategoryModel getCategories() {
		CategoryModel category = new CategoryModel();
		category.setCode(CATEGORY_CODE);
		category.setName(CATEGORY_NAME);
		return category;
	}

	private CatalogVersionModel getCatalogVersion() {

		final CatalogVersionModel catalog = new CatalogVersionModel();
		catalog.setVersion("stage");
		return catalog;

	}

	@Test
	public void getConfiguredFeatureValueForCode() {

		AccommodationModel model = getAccommodationModel();
		final CatalogVersionModel catalog = getCatalogVersion();
		try {
			Mockito.when(siteService.getCurrentCatalogVersion()).thenReturn(catalog);
			Mockito.when(accommodationService.getAccomodationByCodeAndCatalogVersion(ACCOMMCODE2, catalog, null)).thenReturn(model);
			String generic = facadeImpl.getConfiguredFeatureValueForCode(ACCOMMCODE2, CATEGORY_CODE, FEATURE_CODE);
			assertThat(generic.getClass(),is(GenericEditorialFacadeImplTest.class));

		} catch (NullPointerException e) {
			// YTODO Auto-generated catch block
			Log.error("Null Pointe Exception thrown");
		}
	}
}
