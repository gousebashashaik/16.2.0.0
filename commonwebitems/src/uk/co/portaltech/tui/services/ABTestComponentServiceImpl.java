package uk.co.portaltech.tui.services;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.cms2.model.contents.components.SimpleCMSComponentModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.jfree.util.Log;

import uk.co.portaltech.tui.components.model.ABTestComponentModel;
import uk.co.portaltech.tui.model.ABTestModel;
import uk.co.portaltech.tui.model.VariantGroupModel;
import uk.co.tui.web.common.constants.CommonwebitemsConstants;
import uk.co.tui.web.common.enums.ABTestComponentScopes;


/**
 * @author s.consolino
 *
 */
public class ABTestComponentServiceImpl implements ABTestComponentService
{

    @Resource
    private FlexibleSearchService flexibleSearchService;

    @Resource
    private SessionService sessionService;

    @Resource
    private CatalogVersionService catalogVersionService;

    private static final String AB_TEST_COMP_PK = "abTestComponentPK";
    private static final String ACTIVE_CATALOG_VERSIONS = "activeCatalogVersions";

    private static final String SELECT_ABTEST_COMPONENT = "SELECT {relation.target} FROM {ABTestComponentSimpleCMSComponentRelation AS relation} "
            + "WHERE {relation.source} = ?abTestComponentPK " + "ORDER BY {relation.target}";

    private static final String SELECT_VARIANT_GROUP_BY_VARIANT_NAME = "SELECT {pk} FROM {VariantGroup} WHERE {VariantGroup.VARIANTCODE} = ?variantName and {catalogVersion} IN (?activeCatalogVersions)";

    // YTODO Optimize Query
    private static final String SELECT_SIMPLE_CMS_COMPONENT_BY_ABTESTCOMPONENT_VARIANTGROUP = "select {pk} from {SimpleCMSComponent AS simplecmscomp}"
            + ",{ABTestComponentSimpleCMSComponentRelation as "
            + "absimplerelation},{VariantGroupSimpleCMSComponentRelation AS vgsimplerelation}"
            + "where {simplecmscomp.pk} = {absimplerelation.target} and"
            + "{absimplerelation.source} = ?abTestComponentPK and"
            + "{vgsimplerelation.source}=?variantGroupPK and" + "{absimplerelation.target}={vgsimplerelation.target}";

    private static final String SELECT_VARIANT_GROUP_BY_COMPONENT = "select distinct {abtvgrel.target}, {abtcp.abtest} "
            + "from {ABTestComponent as abtcp}, {ABTestVariantGroupRelation as abtvgrel} "
            + "where {abtcp.uid} = ?abTestComponentPK " + "and {abtcp.abtest} = {abtvgrel.source}";

    private static final String SELECT_VARIANT_GROUP_BY_ABTEST_ABTESTCOMPONENT = "select distinct {abtcp.abtest}, "
            + "{abtvgrel.target} from {ABTestComponent as abtcp}, {ABTestVariantGroupRelation as abtvgrel} "
            + "where {abtcp.uid} = ?abTestComponentPK and {abtcp.abtest} in ({{select {pk} from {abtest} "
            + "where {testcode} in (?testCodes) and c}})";

    private static final String GET_ALL_VARIANT_FOR_TEST = "select {vg.pk} from {VariantGroup as vg}, {ABTest as ab}, "
            + "{ABTestVariantGroupRelation as abtvg} where  {ab.testCode} = ?test and {ab.pk} = "
            + "{abtvg.source} and {vg.pk}={abtvg.target}  and {vg.catalogVersion} IN (?activeCatalogVersions)";

    private static final String SELECT_VARIANT_GROUP_PERCENTAGE_BY_CODE = "select {vg.pk} from {VariantGroup as vg} "
            + "where {vg.variantCode}=?code and {catalogVersion} IN (?activeCatalogVersions)";

    /*
     * (non-Javadoc)
     *
     * @see
     * uk.co.portaltech.tui.services.ABTestComponentService#getRandomCMSComponent(uk.co.portaltech.tui.components.model
     * .ABTestComponentModel)
     */
    @Override
    public SimpleCMSComponentModel getRandomCMSComponent(final ABTestComponentModel abTestComponent)
    {
        // Retrieves the list of CMS components that belong to the A/B Test component in inupt
        final List<SimpleCMSComponentModel> simpleCMSComponentList = getSimpleCMSComponentsList(abTestComponent);
        return getRandomCMSComponet(abTestComponent, simpleCMSComponentList);
    }

    private List<SimpleCMSComponentModel> getSimpleCMSComponentsList(final ABTestComponentModel abTestComponent)
    {
        final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(SELECT_ABTEST_COMPONENT);
        flexibleSearchQuery.addQueryParameter(AB_TEST_COMP_PK, abTestComponent.getPk().toString());
        final SearchResult<SimpleCMSComponentModel> searchResult = flexibleSearchService
                .<SimpleCMSComponentModel> search(flexibleSearchQuery);
        return searchResult.getResult();
    }

    private int getRandFromSession(final ABTestComponentModel abTestComponent,
            final List<SimpleCMSComponentModel> simpleCMSComponentList)
    {
        final String key = abTestComponent.getPk().toString() + "_ABTestRand";
        Integer rand = (Integer) sessionService.getAttribute(key);
        if (rand == null)
        {
            rand = getRandomFromPercentage(simpleCMSComponentList);
            sessionService.setAttribute(key, rand);
        }
        return rand.intValue();
    }

    private Integer getRandomFromPercentage(final List<SimpleCMSComponentModel> simpleCMSComponentList)
    {
        int random = RandomUtils.nextInt(simpleCMSComponentList.size());
        int total = 0;
        // Calculation of the total weight of the percentage values for each A/B test variation
        for (final SimpleCMSComponentModel simpleCMSComponentModel : simpleCMSComponentList)
        {
            if (simpleCMSComponentModel.getAbTestPercentage() != null)
            {
                total += simpleCMSComponentModel.getAbTestPercentage().intValue();
            }
        }
        // If the percentage values for A/B test variation are setted
        if (total > 0)
        {
            int percentage = 0;
            // Calculation of a random number between 1 and the total weight of the percentage values
            final int randomPercentage = RandomUtils.nextInt(total) + 1;
            for (int index = 0; index < simpleCMSComponentList.size(); index++)
            {
                final SimpleCMSComponentModel simpleCMSComponentModel = simpleCMSComponentList.get(index);
                // If the percentage value for this A/B test variation is setted and greater than zero
                if (simpleCMSComponentModel.getAbTestPercentage() != null
                        && simpleCMSComponentModel.getAbTestPercentage().intValue() > 0)
                {
                    percentage += simpleCMSComponentModel.getAbTestPercentage().intValue();
                    if (randomPercentage <= percentage)
                    {
                        random = index;
                        break;
                    }
                }
            }
        }
        return Integer.valueOf(random);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * uk.co.portaltech.tui.services.ABTestComponentService#getRandomCMSComponentFromVariantGroup(uk.co.portaltech.tui
     * .components.model.ABTestComponentModel, java.lang.String)
     */
    @Override
    public SimpleCMSComponentModel getRandomCMSComponentFromVariantGroup(final ABTestComponentModel abTestComponent,
            final String variantCode)
    {
        final VariantGroupModel variantGroupModel = getVariantGroupByName(variantCode);
        if (variantGroupModel != null)
        {
            final List<SimpleCMSComponentModel> variantCMSComponents = getSimpleCMSComponentsList(abTestComponent, variantGroupModel);

            return checkVariantCMScComponent(variantCMSComponents, abTestComponent);

        }
        return null;
    }


    public SimpleCMSComponentModel checkVariantCMScComponent(final List<SimpleCMSComponentModel> variantCMSComponents,
            final ABTestComponentModel abTestComponent)
    {
        if (CollectionUtils.isNotEmpty(variantCMSComponents))
        {
            if (variantCMSComponents.size() > 1)
            {
                return getRandomCMSComponet(abTestComponent, variantCMSComponents);
            }
            else
            {
                return variantCMSComponents.get(0);
            }
        }
        return null;
    }

    /**
     * @param abTestComponent
     * @param simpleCMSComponentList
     * @return index of random CMS Component.
     */
    private SimpleCMSComponentModel getRandomCMSComponet(final ABTestComponentModel abTestComponent,
            final List<SimpleCMSComponentModel> simpleCMSComponentList)
    {
        int rand = 0;
        if (simpleCMSComponentList != null && !simpleCMSComponentList.isEmpty())
        {
            String scope = null;
            // Verifies the scope of the A/B Test component in inupt
            final ABTestComponentScopes abTestComponentScopes = abTestComponent.getScope();
            if (abTestComponentScopes != null)
            {
                scope = abTestComponentScopes.toString();
            }
            if (ABTestComponentScopes.REQUEST.toString().equals(scope))
            {
                // If the A/B Test component in inupt is request based, then calculates a pseudorandom number between 0
                // (inclusive) and the number of its components (exclusive)
                rand = RandomUtils.nextInt(simpleCMSComponentList.size());
            }
            else if (ABTestComponentScopes.SESSION.toString().equals(scope))
            {
                // If the A/B Test component in inupt is session based, before checks if was previously calculated and
                // stored in the session a pseudorandom number between 0 (inclusive) and the number of its components
                // (exclusive), and if it exists, returns it, otherwise calculates it.
                rand = getRandFromSession(abTestComponent, simpleCMSComponentList);
            }
            else
            {
                Log.warn("Unkown A/B Test Component scope: [" + scope + "]");
            }
            // Gets the i-th component from the list of components that belong to the A/B Test component in inupt with
            // index equals at the pseudorandom number calculated.
            return simpleCMSComponentList.get(rand);
        }
        return null;
    }

    /**
     * @param abTestComponent
     * @param variantGroupModel
     */
    private List<SimpleCMSComponentModel> getSimpleCMSComponentsList(final ABTestComponentModel abTestComponent,
            final VariantGroupModel variantGroupModel)
    {
        final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(
                SELECT_SIMPLE_CMS_COMPONENT_BY_ABTESTCOMPONENT_VARIANTGROUP);
        flexibleSearchQuery.addQueryParameter(AB_TEST_COMP_PK, abTestComponent.getPk().toString());
        flexibleSearchQuery.addQueryParameter("variantGroupPK", variantGroupModel.getPk().toString());
        final SearchResult<SimpleCMSComponentModel> searchResult = flexibleSearchService
                .<SimpleCMSComponentModel> search(flexibleSearchQuery);
        return searchResult.getResult();

    }

    /**
     * @return VariantGroupModel list that match flexi search query.
     */
    private Map<VariantGroupModel, ABTestModel> getVariantGroupByComponent(final ABTestComponentModel abTestComponent)
    {
        final Map<VariantGroupModel, ABTestModel> resultMap = new HashMap<VariantGroupModel, ABTestModel>();

        final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(SELECT_VARIANT_GROUP_BY_COMPONENT);
        flexibleSearchQuery.addQueryParameter(AB_TEST_COMP_PK, abTestComponent.getUid());
        flexibleSearchQuery.setResultClassList(Arrays.asList(VariantGroupModel.class, ABTestModel.class));

        final List result = flexibleSearchService.search(flexibleSearchQuery).getResult();
        for (int i = 0; i < result.size(); i++)
        {
            final List row = (List) result.get(i);
            resultMap.put((VariantGroupModel) row.get(0), (ABTestModel) row.get(1));

        }
        return resultMap;
    }

    /**
     * @param variantCode
     * @return VariantGroupModel list that match flexi search query.
     */
    private VariantGroupModel getVariantGroupByName(final String variantCode)
    {
        FlexibleSearchQuery flexibleSearchQuery = null;
        if (variantCode != null)
        {
            flexibleSearchQuery = new FlexibleSearchQuery(SELECT_VARIANT_GROUP_BY_VARIANT_NAME);
            flexibleSearchQuery.addQueryParameter("variantName", variantCode);
            flexibleSearchQuery.addQueryParameter(ACTIVE_CATALOG_VERSIONS, catalogVersionService.getSessionCatalogVersions());
        }
        return flexibleSearchService.<VariantGroupModel> searchUnique(flexibleSearchQuery);
    }

    /*
     * (non-Javadoc)
     *
     * @see uk.co.portaltech.tui.services.ABTestComponentService#getVariant(uk.co.portaltech.tui.components.model.
     * ABTestComponentModel, java.util.Set)
     */
    @Override
    public Map<ABTestModel, VariantGroupModel> getVariant(final ABTestComponentModel abTestComponent, final Set<String> testNames)
    {
        FlexibleSearchQuery flexibleSearchQuery = null;
        flexibleSearchQuery = new FlexibleSearchQuery(SELECT_VARIANT_GROUP_BY_ABTEST_ABTESTCOMPONENT);
        flexibleSearchQuery.addQueryParameter(AB_TEST_COMP_PK, abTestComponent.getPk().toString());
        flexibleSearchQuery.addQueryParameter("testCodes", testNames);
        flexibleSearchQuery.addQueryParameter(ACTIVE_CATALOG_VERSIONS, catalogVersionService.getSessionCatalogVersions());
        flexibleSearchQuery.setResultClassList(Arrays.asList(VariantGroupModel.class, ABTestModel.class));

        final List<?> searchResult = flexibleSearchService.searchUnique(flexibleSearchQuery);
        final Map<ABTestModel, VariantGroupModel> resultMap = new HashMap<ABTestModel, VariantGroupModel>();
        resultMap.put((ABTestModel) searchResult.get(0), (VariantGroupModel) searchResult.get(1));
        return resultMap;
    }





    /*
     * (non-Javadoc)
     *
     * @see uk.co.portaltech.tui.services.ABTestComponentService#getRandomVariantGroup()
     */
    @Override
    public Map<ABTestModel, VariantGroupModel> getRandomVariantGroup(final ABTestComponentModel abTestComponent)
    {
        final Map<VariantGroupModel, ABTestModel> abTestVariantGroupModelMap = getVariantGroupByComponent(abTestComponent);
        final Map<ABTestModel, VariantGroupModel> abTestVariantGroupMap = new HashMap<ABTestModel, VariantGroupModel>();

        if (abTestVariantGroupModelMap != null && !abTestVariantGroupModelMap.isEmpty())
        {
            final VariantGroupModel variantGroupModel = getRandomFromVariantPercentage(new ArrayList<VariantGroupModel>(
                    abTestVariantGroupModelMap.keySet()));
            abTestVariantGroupMap.put(abTestVariantGroupModelMap.get(variantGroupModel), variantGroupModel);
        }

        return abTestVariantGroupMap;
    }

    /**
     *
     * @param variantGroupList
     * @return integer random value.
     */
    private VariantGroupModel getRandomFromVariantPercentage(final List<VariantGroupModel> variantGroupList)
    {
        int random = RandomUtils.nextInt(variantGroupList.size());
        int total = 0;
        // Calculation of the total weight of the percentage values for each A/B test variation
        for (final VariantGroupModel variantGroupModel : variantGroupList)
        {
            if (variantGroupModel.getVariantPercentage() != null)
            {
                total += variantGroupModel.getVariantPercentage().intValue();
            }
        }
        // If the percentage values for A/B test variation are setted
        if (total > 0)
        {
            int percentage = 0;
            // Calculation of a random number between 1 and the total weight of the percentage values
            final int randomPercentage = RandomUtils.nextInt(total) + 1;
            for (int index = 0; index < variantGroupList.size(); index++)
            {
                final VariantGroupModel variantGroupModel = variantGroupList.get(index);
                // If the percentage value for this A/B test variation is setted and greater than zero
                if (variantGroupModel.getVariantPercentage() != null && variantGroupModel.getVariantPercentage().intValue() > 0)
                {
                    // If the percentage of the variant is set to 100, there is no need to randomly pick the variant.
                    if (variantGroupModel.getVariantPercentage().intValue() == CommonwebitemsConstants.ONE_ZERO_ZERO)
                    {
                        return variantGroupModel;
                    }
                    percentage += variantGroupModel.getVariantPercentage().intValue();
                    if (randomPercentage <= percentage)
                    {
                        random = index;
                        break;
                    }
                }
            }
        }
        return variantGroupList.get(random);
    }

    @Override
    public String getVariantCodeForNewUser(final String testCode)
    {
        final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(GET_ALL_VARIANT_FOR_TEST);
        flexibleSearchQuery.addQueryParameter("test", testCode);
        flexibleSearchQuery.addQueryParameter(ACTIVE_CATALOG_VERSIONS, catalogVersionService.getSessionCatalogVersions());
        final SearchResult<VariantGroupModel> search = flexibleSearchService.search(flexibleSearchQuery);
        final VariantGroupModel variantGroupModel = getRandomFromVariantPercentage(search.getResult());
        return variantGroupModel.getVariantCode();
    }


    @Override
    public int getVariantPercentageByCode(final String variantCode)
    {
        final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(SELECT_VARIANT_GROUP_PERCENTAGE_BY_CODE);
        flexibleSearchQuery.addQueryParameter("code", variantCode);
        flexibleSearchQuery.addQueryParameter(ACTIVE_CATALOG_VERSIONS, catalogVersionService.getSessionCatalogVersions());
        final SearchResult<VariantGroupModel> search = flexibleSearchService.search(flexibleSearchQuery);
        if (search.getResult() == null || search.getResult().isEmpty())
        {
            return 0;
        }
        return search.getResult().get(0).getVariantPercentage().intValue();
    }





}