package org.modality_project.event.backoffice.activities.events;

import org.modality_project.event.backoffice.operations.routes.events.RouteToEventsRequest;
import dev.webfx.stack.framework.client.activity.impl.elementals.uiroute.UiRouteActivityContext;
import dev.webfx.stack.framework.client.operations.route.RouteRequestEmitter;
import dev.webfx.stack.framework.shared.router.auth.authz.RouteRequest;

/**
 * @author Bruno Salmon
 */
public final class RouteToEventsRequestEmitter implements RouteRequestEmitter {

    @Override
    public RouteRequest instantiateRouteRequest(UiRouteActivityContext context) {
        return new RouteToEventsRequest(context.getHistory());
    }
}
