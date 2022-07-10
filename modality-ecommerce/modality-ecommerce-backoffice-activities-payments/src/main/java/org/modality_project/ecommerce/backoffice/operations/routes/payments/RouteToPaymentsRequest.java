package org.modality_project.ecommerce.backoffice.operations.routes.payments;

import org.modality_project.ecommerce.backoffice.activities.payments.routing.PaymentsRouting;
import dev.webfx.stack.framework.client.operations.route.RoutePushRequest;
import dev.webfx.stack.framework.shared.operation.HasOperationCode;
import dev.webfx.stack.platform.windowhistory.spi.BrowsingHistory;

/**
 * @author Bruno Salmon
 */
public final class RouteToPaymentsRequest extends RoutePushRequest implements HasOperationCode {

    private final static String OPERATION_CODE = "RouteToPayments";

    public RouteToPaymentsRequest(Object eventId, BrowsingHistory history) {
        super(PaymentsRouting.getPaymentsPath(eventId), history);
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }

}
