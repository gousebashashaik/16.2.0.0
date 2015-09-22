/**
 *
 */
package uk.co.portaltech.tui.services.common;

import de.hybris.platform.core.Registry;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.feeds.model.cronjobs.TripAdvisorCronJobModel;



/**
 * @author omonikhide
 *
 */
public class TripAdvisorJobPerformable extends AbstractJobPerformable<TripAdvisorCronJobModel>
{

    private static final TUILogUtils LOG = new TUILogUtils("TripAdvisorJobPerformable");


    @Override
    public PerformResult perform(final TripAdvisorCronJobModel model)
    {
        LOG.info(">> TripAdvisorJobPerformable | Perform << ");
        boolean error = false;

        try
        {
            final TripAdvisorFeedProcessor processor = Registry.getApplicationContext().getBean(TripAdvisorFeedProcessor.class);
            processor.processData(null, model.getOutputDir(), null);
        }
        catch (final Exception ex)
        {
            LOG.error("Failed to import tripadvisor data", ex);
            error = true;
        }

        if (error)
        {
            return new PerformResult(CronJobResult.FAILURE, CronJobStatus.ABORTED);
        }
        return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
    }

}
