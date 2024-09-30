package one.modality.event.frontoffice.activities.audiorecordings;

import dev.webfx.stack.routing.router.auth.authz.RouteRequest;
import dev.webfx.stack.routing.uirouter.activity.uiroute.UiRouteActivityContext;
import dev.webfx.stack.routing.uirouter.operations.RouteRequestEmitter;
import one.modality.event.frontoffice.operations.routes.audiorecordings.RouteToAudioRecordingsRequest;

public final class RouteToAudioRecordingsRequestEmitter implements RouteRequestEmitter {

    @Override
    public RouteRequest instantiateRouteRequest(UiRouteActivityContext context) {
        return new RouteToAudioRecordingsRequest(context.getHistory());
    }
}