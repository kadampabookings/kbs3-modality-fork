package one.modality.hotel.backoffice.activities.accommodation;

import dev.webfx.stack.ui.action.Action;
import dev.webfx.stack.ui.operation.action.OperationActionFactoryMixin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import one.modality.base.backoffice.activities.mainframe.fx.FXMainFrameHeaderTabsBar;
import one.modality.base.backoffice.tile.Tile;
import one.modality.base.client.activity.organizationdependent.OrganizationDependentViewDomainActivity;
import one.modality.base.client.gantt.fx.visibility.FXGanttVisibility;
import one.modality.base.client.gantt.fx.visibility.GanttVisibility;
import one.modality.hotel.backoffice.accommodation.AccommodationBorderPane;
import one.modality.hotel.backoffice.accommodation.AccommodationPresentationModel;
import one.modality.hotel.backoffice.accommodation.TodayAccommodationStatus;

import java.util.function.Supplier;

final class AccommodationActivity extends OrganizationDependentViewDomainActivity implements
        OperationActionFactoryMixin {

    private final ObservableList<Tile> tabsButtons = FXCollections.observableArrayList();
    private final AccommodationPresentationModel pm = new AccommodationPresentationModel();
    private final RoomView roomView = new RoomView(pm);
    private final GuestView guestView = new GuestView(pm);

    private final RoomsAlterationView roomsAlterationView = new RoomsAlterationView(pm, this);
    private final TodayAccommodationStatus todayAccommodationStatus = new TodayAccommodationStatus(pm);

    final BorderPane container = new BorderPane();
    public AccommodationActivity() {
        pm.doFXBindings();
    }

    @Override
    public Node buildUi() {
        // Creating the tabs buttons that will appear in the main frame header tabs bar (see onResume())
        tabsButtons.setAll(
                createTabButton("Rooms", this::buildRoomView),
                createTabButton("Guests", this::buildGuestView),
                createTabButton("Rooms alteration", this::buildRoomsAlterationView)
        );
        // Firing the first tab (= Rooms)
        tabsButtons.get(0).fireAction(); // Will show the rooms view in the container
        // returning the container
        return container;
    }

    private Tile selectedTile;

    private Tile createTabButton(String text, Supplier<Node> nodeSupplier) {
        Tile[] tabTile = { null };
        Node[] tabContent = { null };
        Action action = newAction(text, () -> {
            if (tabContent[0] == null)
                tabContent[0] = nodeSupplier.get();
            container.setCenter(tabContent[0]);
            if (selectedTile != null)
                selectedTile.setSelected(false);
            selectedTile = tabTile[0].setSelected(true);
        });
        tabTile[0] = new Tile(action).setFontSize(14).setSelected(false).setTransparentBackground(true);
        tabTile[0].setPadding(new Insets(5, 30, 5, 30));
        return tabTile[0];
    }

    private Node buildRoomView() {
        BorderPane borderPane = new BorderPane(roomView.buildCanvasContainer());
        CheckBox groupBlocksCheckBox = new CheckBox("Group blocks");
        roomView.blocksGroupingProperty().bind(groupBlocksCheckBox.selectedProperty());
        borderPane.setBottom(groupBlocksCheckBox);
        return borderPane;
    }

    private Node buildGuestView() {
        return AccommodationBorderPane.createAccommodationBorderPane(guestView.getAttendanceGantt(), todayAccommodationStatus);
    }

    private Node buildRoomsAlterationView() {
        return RoomsAlterationBorderPane.createAccommodationBorderPane(roomsAlterationView, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        FXMainFrameHeaderTabsBar.setTabsBarButtons(tabsButtons);
        FXGanttVisibility.setGanttVisibility(GanttVisibility.EVENTS);
    }

    @Override
    public void onPause() {
        FXMainFrameHeaderTabsBar.clearTabsBarButtons();
        FXGanttVisibility.setGanttVisibility(GanttVisibility.HIDDEN);
        super.onPause();
    }

    /*==================================================================================================================
    =================================================== Logical layer ==================================================
    ==================================================================================================================*/

    @Override
    protected void startLogic() {
        roomView.startLogic(this);
        guestView.startLogic(this);
        todayAccommodationStatus.startLogic(this);
        roomsAlterationView.startLogic(this);
    }

    @Override
    public AccommodationPresentationModel getPresentationModel() {
        return pm;
    }

}
