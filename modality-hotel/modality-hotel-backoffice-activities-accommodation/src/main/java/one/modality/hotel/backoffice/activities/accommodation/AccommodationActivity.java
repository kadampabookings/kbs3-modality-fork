package one.modality.hotel.backoffice.activities.accommodation;

import dev.webfx.stack.ui.operation.action.OperationActionFactoryMixin;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import one.modality.base.client.activity.organizationdependent.OrganizationDependentViewDomainActivity;
import one.modality.base.client.gantt.fx.visibility.FXGanttVisibility;
import one.modality.base.client.gantt.fx.visibility.GanttVisibility;
import one.modality.base.shared.entities.Attendance;
import one.modality.base.shared.entities.Event;

import java.util.List;
import java.util.stream.Collectors;

final class AccommodationActivity extends OrganizationDependentViewDomainActivity implements
        AccommodationController,
        OperationActionFactoryMixin {

    private final AccommodationGanttCanvas accommodationGanttCanvas = new AccommodationGanttCanvas(this);
    private final AccommodationKeyPane accommodationKeyPane = new AccommodationKeyPane();
    private final AccommodationSummaryPane accommodationSummaryPane = new AccommodationSummaryPane();

    @Override
    public Node buildUi() {
        BorderPane borderPane = new BorderPane(accommodationGanttCanvas.buildCanvasContainer());
        borderPane.setBottom(accommodationSummaryPane);
        borderPane.setLeft(accommodationKeyPane);

        CheckBox showKeyCheckBox = new CheckBox("Show key");
        showKeyCheckBox.setSelected(true);
        showKeyCheckBox.setOnAction(e -> {
            borderPane.setLeft(showKeyCheckBox.isSelected() ? accommodationKeyPane : null);
        });
        borderPane.setTop(showKeyCheckBox);

        return borderPane;
    }

    @Override
    public void setEntities(List<Attendance> attendances) {
        List<Event> events = attendances.stream()
                .map(Attendance::getEvent)
                .distinct()
                .collect(Collectors.toList());
        accommodationKeyPane.setEvents(events);

        accommodationSummaryPane.setEntities(attendances);
    }

    @Override
    public Color getEventColor(Event event) {
        return accommodationKeyPane.getEventColor(event);
    }

    @Override
    public void onResume() {
        super.onResume();
        FXGanttVisibility.setGanttVisibility(GanttVisibility.EVENTS);
    }

    @Override
    public void onPause() {
        FXGanttVisibility.setGanttVisibility(GanttVisibility.HIDDEN);
        super.onPause();
    }

    /*==================================================================================================================
    =================================================== Logical layer ==================================================
    ==================================================================================================================*/

    @Override
    protected void startLogic() {
        accommodationGanttCanvas.startLogic(this);
    }

    private final AccommodationPresentationModel pm = new AccommodationPresentationModel();

    @Override
    public AccommodationPresentationModel getPresentationModel() {
        super.getPresentationModel();
        return pm; // eventId and organizationId will then be updated from route
    }

}
