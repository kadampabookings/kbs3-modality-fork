package org.modality_project.base.backoffice.activities.filters;

import org.modality_project.base.backoffice.operations.routes.filters.RouteToFiltersRequest;
import dev.webfx.stack.framework.client.activity.impl.elementals.uiroute.UiRouteActivityContext;
import dev.webfx.stack.framework.client.operations.route.RouteRequestEmitter;
import dev.webfx.stack.framework.shared.router.auth.authz.RouteRequest;

public final class RouteToFiltersRequestEmitter implements RouteRequestEmitter {

    @Override
    public RouteRequest instantiateRouteRequest(UiRouteActivityContext context) {
        return new RouteToFiltersRequest(context.getHistory());
    }
}
