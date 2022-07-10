package org.modality_project.ecommerce.backoffice.activities.income;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.modality_project.base.backoffice.controls.masterslave.group.GroupView;
import org.modality_project.base.client.entities.util.filters.FilterButtonSelectorFactoryMixin;
import org.modality_project.base.client.activity.eventdependent.EventDependentViewDomainActivity;
import org.modality_project.base.shared.entities.Document;
import org.modality_project.base.shared.entities.DocumentLine;
import org.modality_project.base.shared.entities.Filter;
import dev.webfx.extras.visual.controls.grid.VisualGrid;
import dev.webfx.stack.framework.client.ui.action.operation.OperationActionFactoryMixin;
import dev.webfx.stack.framework.client.orm.reactive.mapping.entities_to_visual.ReactiveVisualMapper;
import dev.webfx.stack.framework.client.ui.controls.entity.selector.EntityButtonSelector;

import static dev.webfx.stack.framework.shared.orm.dql.DqlStatement.where;

final class IncomeActivity extends EventDependentViewDomainActivity implements
        OperationActionFactoryMixin,
        FilterButtonSelectorFactoryMixin {

    /*==================================================================================================================
    ================================================= Graphical layer ==================================================
    ==================================================================================================================*/

    private final IncomePresentationModel pm = new IncomePresentationModel();

    @Override
    public IncomePresentationModel getPresentationModel() {
        return pm; // eventId and organizationId will then be updated from route
    }

    @Override
    public Node buildUi() {
        BorderPane container = new BorderPane();

        // Creating the total table that will be on top of the container
        VisualGrid totalTable = new VisualGrid();
        totalTable.setFullHeight(true);
        totalTable.visualResultProperty().bind(pm.genericVisualResultProperty());

        // Also putting the breakdown group selector just below the total table (also on top of the container)
        EntityButtonSelector<Filter> breakdownGroupSelector = createGroupFilterButtonSelectorAndBind("income", "DocumentLine", container, pm);

        container.setTop(new VBox(totalTable, breakdownGroupSelector.getButton()));

        // Creating the breakdown group view and put it in the center of the container
        container.setCenter(GroupView.createAndBind(pm).buildUi());

        return container;
    }


    /*==================================================================================================================
    =================================================== Logical layer ==================================================
    ==================================================================================================================*/

    private ReactiveVisualMapper<Document> totalVisualMapper;
    private ReactiveVisualMapper<DocumentLine> breakdownVisualMapper;

    @Override
    protected void startLogic() {
        totalVisualMapper = ReactiveVisualMapper.<Document>createReactiveChain(this)
                .always("{class: 'Document', alias: 'd'}")
                // Applying the event condition
                .ifNotNullOtherwiseEmpty(pm.eventIdProperty(), eventId -> where("event=?", eventId))
                .always("{columns: `null as Totals,sum(price_deposit) as Deposit,sum(price_net) as Invoiced,sum(price_minDeposit) as MinDeposit,sum(price_nonRefundable) as NonRefundable,sum(price_balance) as Balance,count(1) as Bookings,sum(price_balance!=0 ? 1 : 0) as Unreconciled`, groupBy: `event`}")
                .visualizeResultInto(pm.genericVisualResultProperty())
                .start();

        breakdownVisualMapper = ReactiveVisualMapper.<DocumentLine>createGroupReactiveChain(this, pm)
                .always("{class: 'DocumentLine', alias: 'dl'}")
                // Applying the event condition
                .ifNotNullOtherwiseEmpty(pm.eventIdProperty(), eventId -> where("document.event=?", eventId))
                .start();
    }

    @Override
    protected void refreshDataOnActive() {
        totalVisualMapper.refreshWhenActive();
        breakdownVisualMapper.refreshWhenActive();
    }
}
