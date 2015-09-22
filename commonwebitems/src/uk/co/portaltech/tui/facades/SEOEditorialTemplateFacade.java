/**
 *
 */
package uk.co.portaltech.tui.facades;

import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.model.SEOEditorialTemplateModel;
import uk.co.portaltech.tui.web.view.data.SEOEditorialTemplateViewData;

/**
 * @author l.furrer
 *
 */
public interface SEOEditorialTemplateFacade {

    /**
     * This method parses all the fields of a template and looks for '[location]', where location is 'country', 'region'
     * 'destination' and 'resort'<br/>
     * It substitutes them with the approriate location name
     * @param seoTemplate
     *  the {@link SEOEditorialTemplateModel} to which resolve names for.
     * @return
     *  the modified {@link SEOEditorialTemplateModel}
     */
     SEOEditorialTemplateViewData resolveLocationNameForTemplate(SEOEditorialTemplateModel seoTemplate, LocationModel location,String siteUid);

}
