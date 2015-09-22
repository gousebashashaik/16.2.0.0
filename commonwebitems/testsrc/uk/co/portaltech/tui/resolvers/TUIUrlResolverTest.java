package uk.co.portaltech.tui.resolvers;


import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import org.junit.Test;

import javax.annotation.Resource;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@IntegrationTest
public class TUIUrlResolverTest extends ServicelayerTransactionalTest {

    @Resource
    private CategoryService categoryService;

    @Resource
    private ProductService productService;

    @Resource
    private TuiCategoryUrlResolver tuiCategoryModelUrlResolver;

    @Resource
    private TuiProductUrlResolver tuiProductUrlResolver;


    protected CatalogVersionModel getImportCatalogVersion() {
        final ConfigurationService configService = Registry.getApplicationContext().getBean(ConfigurationService.class);
        final CatalogVersionService catalogVersionService = Registry.getApplicationContext().getBean(CatalogVersionService.class);
        String catalog = configService.getConfiguration().getString("import_catalog");
        String catalogVersion = configService.getConfiguration().getString("import_catalog.version");

        CatalogVersionModel catalogVersionModel = catalogVersionService.getCatalogVersion(catalog, catalogVersion);

        catalogVersionService.setSessionCatalogVersions(asList(catalogVersionModel));
        return catalogVersionModel;
    }


    @Test
    public void shouldBuildURLForASpecificCountry() {
        CategoryModel country = categoryService.getCategoryForCode(getImportCatalogVersion(), "ESP");

        String url = tuiCategoryModelUrlResolver.resolve(country);

        assertThat(url, is(notNullValue()));
        assertThat(url, is("/holiday/location/overview/Spain-ESP"));

    }


    @Test
    public void shouldBuildURLForASpecificDestination() {
        CategoryModel destination = categoryService.getCategoryForCode(getImportCatalogVersion(), "000329");

        String url = tuiCategoryModelUrlResolver.resolve(destination);

        assertThat(url, is(notNullValue()));
        assertThat(url, is("/holiday/location/overview/Lanzarote-000329"));
    }

    @Test
    public void shouldBuildURLForASpecificHotel() {
        ProductModel product = productService.getProductForCode(getImportCatalogVersion(), "027346");

        String url = tuiProductUrlResolver.resolve(product);

        assertThat(url, is(notNullValue()));
        assertThat(url, is("/holiday/accommodation/overview/Costa-Teguise/Barcelo-Lanzarote-Resort-027346"));
    }

}
