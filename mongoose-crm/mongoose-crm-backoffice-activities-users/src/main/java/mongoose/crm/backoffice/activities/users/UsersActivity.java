package mongoose.crm.backoffice.activities.users;

import dev.webfx.framework.client.ui.action.Action;
import dev.webfx.framework.client.ui.action.ActionGroup;
import dev.webfx.framework.client.ui.action.ActionGroupBuilder;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableStringValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import mongoose.base.backoffice.controls.masterslave.ConventionalUiBuilder;
import mongoose.base.backoffice.controls.masterslave.ConventionalUiBuilderMixin;
import mongoose.base.client.activity.eventdependent.EventDependentViewDomainActivity;
import mongoose.base.shared.domainmodel.functions.AbcNames;
import mongoose.base.shared.entities.Person;
import dev.webfx.framework.client.orm.reactive.mapping.entities_to_visual.ReactiveVisualMapper;
import mongoose.ecommerce.backoffice.operations.entities.document.EditUsersPersonalDetailsRequest;

import java.util.Arrays;
import java.util.Collection;

import static dev.webfx.framework.shared.orm.dql.DqlStatement.where;

final class UsersActivity extends EventDependentViewDomainActivity implements
        ConventionalUiBuilderMixin {

    /*==================================================================================================================
    ================================================= Graphical layer ==================================================
    ==================================================================================================================*/

    private final UsersPresentationModel pm = new UsersPresentationModel();

    private Pane pane;

    @Override
    public UsersPresentationModel getPresentationModel() {
        return pm; // eventId and organizationId will then be updated from route
    }

    private ConventionalUiBuilder ui; // Keeping this reference for activity resume

    @Override
    public Node buildUi() {
        ui = createAndBindGroupMasterSlaveViewWithFilterSearchBar(pm, "users", "Person");
        pane = ui.buildUi();
        setUpContextMenu(pane, this::createContextMenuActionGroup);
        return pane;
    }

    private ActionGroup createContextMenuActionGroup() {
        ObservableStringValue textProperty = new SimpleStringProperty("Edit...");
        ObservableObjectValue<Node> graphicProperty = new SimpleObjectProperty<>();
        ObservableBooleanValue disabledProperty = new SimpleBooleanProperty(false);
        ObservableBooleanValue visibleProperty = new SimpleBooleanProperty(true);
        EventHandler<ActionEvent> actionHandler = e -> new EditUsersPersonalDetailsRequest(getPerson(), this, pane);
        Collection<Action> actions = Arrays.asList(
                Action.create(textProperty, graphicProperty, disabledProperty, visibleProperty, actionHandler)
        );
        return new ActionGroupBuilder().setI18nKey(null).setActions(actions).setHasSeparators(false).build();
    }

    private Person getPerson() {
        Person person = pm.selectedMasterProperty().get();
        person.setEvent(getEvent());
        return person;
    }

    @Override
    public void onResume() {
        super.onResume();
        ui.onResume();
    }


    /*==================================================================================================================
    =================================================== Logical layer ==================================================
    ==================================================================================================================*/

    private ReactiveVisualMapper<Person> groupVisualMapper, masterVisualMapper;

    @Override
    protected void startLogic() {
        // Setting up the group mapper that build the content displayed in the group view
        groupVisualMapper = ReactiveVisualMapper.<Person>createGroupReactiveChain(this, pm)
                .always("{class: 'Person', alias: 'p', orderBy: 'id'}")
                .start();

        // Setting up the master mapper that build the content displayed in the master view
        masterVisualMapper = ReactiveVisualMapper.<Person>createMasterPushReactiveChain(this, pm)
                .always("{class: 'Person', alias: 'p', orderBy: 'lastName,firstName,id'}")
                // Applying the user search
                .ifTrimNotEmpty(pm.searchTextProperty(), s ->
                        s.contains("@") ? where("lower(email) like ?", "%" + s.toLowerCase() + "%")
                                : where("abcNames(firstName + ' ' + lastName) like ?", AbcNames.evaluate(s, true)))
                .applyDomainModelRowStyle() // Colorizing the rows
                .start();
    }

    @Override
    protected void refreshDataOnActive() {
        groupVisualMapper.refreshWhenActive();
        masterVisualMapper.refreshWhenActive();
    }
}
