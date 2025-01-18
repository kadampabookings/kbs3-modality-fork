package one.modality.event.frontoffice.activities.videos;

import dev.webfx.extras.panes.MonoPane;
import dev.webfx.extras.styles.bootstrap.Bootstrap;
import dev.webfx.platform.console.Console;
import dev.webfx.platform.uischeduler.UiScheduler;
import dev.webfx.platform.util.time.Times;
import dev.webfx.platform.windowhistory.spi.BrowsingHistory;
import dev.webfx.stack.i18n.I18nKeys;
import dev.webfx.stack.i18n.controls.I18nControls;
import dev.webfx.stack.orm.entity.EntityStore;
import dev.webfx.stack.orm.entity.UpdateStore;
import dev.webfx.stack.orm.entity.binding.EntityBindings;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import one.modality.base.client.messaging.ModalityMessaging;
import one.modality.base.shared.entities.ScheduledItem;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author Bruno Salmon
 */
final class VideosDayScheduleView {

    private final LocalDate day;
    private final List<ScheduledItem> dayScheduledVideos;
    private final BrowsingHistory browsingHistory;

    private final MonoPane dateMonoPane = new MonoPane();
    private final MonoPane statusMonoPane = new MonoPane();
    private final VBox nameVBox = new VBox();
    private final VBox timeVBox = new VBox();
    private final MonoPane remarkMonoPane = new MonoPane();
    private final MonoPane actionButtonMonoPane = new MonoPane();
    private final MonoPane remarkHeaderMonoPane = new MonoPane();
    private final Separator separator1 = new Separator();
    private final Separator separator2 = new Separator();
    private final HBox mainLine = new HBox();
    private final VBox mainVBox = new VBox();
    private final int DATE_PREF_SIZE = 150;
    private final int STATUS_PREF_SIZE = 150;
    private final int NAME_PREF_SIZE = 200;
    private final int TIME_PREF_SIZE = 130;
    private final int REMARK_PREF_SIZE = 300;


    private final EntityStore entityStore;

    public VideosDayScheduleView(LocalDate day, List<ScheduledItem> dayScheduledVideos, BrowsingHistory browsingHistory, boolean displayHeader, EntityStore entityStore) {
        this.day = day;
        this.dayScheduledVideos = dayScheduledVideos;
        this.browsingHistory = browsingHistory;
        this.entityStore = entityStore;
        buildUi(displayHeader);
    }


    Region getView() {
        return mainVBox;
    }

    private void buildUi(boolean displayHeader) {

        dateMonoPane.setMinWidth(DATE_PREF_SIZE);
        dateMonoPane.setPrefWidth(DATE_PREF_SIZE);
        dateMonoPane.setMaxWidth(DATE_PREF_SIZE);

        statusMonoPane.setMinWidth(STATUS_PREF_SIZE);
        statusMonoPane.setPrefWidth(STATUS_PREF_SIZE);
        statusMonoPane.setMaxWidth(STATUS_PREF_SIZE);

        nameVBox.setMinWidth(NAME_PREF_SIZE);
        nameVBox.setPrefWidth(NAME_PREF_SIZE);
        nameVBox.setMaxWidth(NAME_PREF_SIZE);

        timeVBox.setMinWidth(TIME_PREF_SIZE);
        timeVBox.setPrefWidth(TIME_PREF_SIZE);
        timeVBox.setMaxWidth(TIME_PREF_SIZE);

        remarkMonoPane.setMinWidth(40);
        remarkMonoPane.setPrefWidth(REMARK_PREF_SIZE);
        remarkMonoPane.setMaxWidth(REMARK_PREF_SIZE);

        int BUTTON_PREF_SIZE = 150;
        actionButtonMonoPane.setMinWidth(BUTTON_PREF_SIZE);
        actionButtonMonoPane.setPrefWidth(BUTTON_PREF_SIZE);
        actionButtonMonoPane.setMaxWidth(BUTTON_PREF_SIZE);

        if (displayHeader) {
            addHeaderRow();
        } else {
            if (dayScheduledVideos.get(0).getEvent().getType().getRecurringItem() == null)
                addInvisibleSeparator();
        }

        Label dateLabel = new Label(day.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
        dateLabel.setWrapText(true);

        dateMonoPane.setContent(dateLabel);

        // Add a listener to the width property of the HBox
        mainVBox.widthProperty().addListener((obs, oldWidth, newWidth) -> {
                remarkHeaderMonoPane.setVisible(newWidth.doubleValue() >1000);
                remarkHeaderMonoPane.setManaged(newWidth.doubleValue() >1000);
                remarkMonoPane.setVisible(newWidth.doubleValue() >1000);
                remarkMonoPane.setManaged(newWidth.doubleValue() >1000);
        });


        // Use the inner class to populate the grid
        dayScheduledVideos.forEach((s) -> {
            VideoSchedulePopulator populator = new VideoSchedulePopulator(s);
            // Old code: ModalityMessaging.addFrontOfficeMessageBodyHandler(e -> populator.updateVODButton(e));
            // New code (not yet working):
            ModalityMessaging.getFrontOfficeEntityMessaging().listenEntityChanges(s.getStore());
            populator.populateVideoRow();
        });
        mainVBox.setAlignment(Pos.CENTER);
    }

    private void addHeaderRow() {
        Label dateHeaderLabel = Bootstrap.h4(Bootstrap.textPrimary(I18nControls.newLabel(VideosI18nKeys.Date)));
        dateHeaderLabel.setPadding(new Insets(0,0,0,20));
        Label statusHeaderLabel = Bootstrap.h4(Bootstrap.textPrimary(I18nControls.newLabel(VideosI18nKeys.Status)));
        statusHeaderLabel.setPadding(new Insets(0,0,0,30));
        Label nameHeaderLabel = Bootstrap.h4(Bootstrap.textPrimary(I18nControls.newLabel(VideosI18nKeys.Name)));
        Label timeZoneHeaderLabel = Bootstrap.h4(Bootstrap.textPrimary(I18nControls.newLabel(VideosI18nKeys.TimeZoneUK)));
        Label gmtTimeHeaderLabel = Bootstrap.small(Bootstrap.textPrimary(I18nControls.newLabel(VideosI18nKeys.GMTZoneUK)));
        Label remarksHeaderLabel = Bootstrap.h4(Bootstrap.textPrimary(I18nControls.newLabel(VideosI18nKeys.Remarks)));


        MonoPane dateMonoPane = new MonoPane(dateHeaderLabel);
        dateMonoPane.setMinWidth(DATE_PREF_SIZE);
        dateMonoPane.setAlignment(Pos.CENTER_LEFT);

        MonoPane statusMonoPane = new MonoPane(statusHeaderLabel);
        statusMonoPane.setMinWidth(STATUS_PREF_SIZE);
        statusMonoPane.setAlignment(Pos.CENTER_LEFT);

        MonoPane nameMonoPane = new MonoPane(nameHeaderLabel);
        nameMonoPane.setMinWidth(NAME_PREF_SIZE);
        nameMonoPane.setAlignment(Pos.CENTER_LEFT);

        VBox timeVBox = new VBox(timeZoneHeaderLabel, gmtTimeHeaderLabel);
        timeVBox.setMinWidth(TIME_PREF_SIZE);
        timeVBox.setAlignment(Pos.CENTER_LEFT);

        remarkHeaderMonoPane.setContent(remarksHeaderLabel);
        remarkHeaderMonoPane.setMinWidth(100);
        remarkHeaderMonoPane.setAlignment(Pos.CENTER_LEFT);

        separator1.setPadding(new Insets(5, 0, 15, 0));

        HBox line = new HBox(5,dateMonoPane, statusMonoPane, nameMonoPane, timeVBox,remarkHeaderMonoPane);
        mainVBox.getChildren().addAll(line,separator1);
    }

    private void addInvisibleSeparator() {
        separator2.setVisible(false);
        separator2.setPadding(new Insets(20, 0, 20, 0));
        mainVBox.getChildren().add(separator2);
    }


    // Inner class to handle populating video schedule rows
    private class VideoSchedulePopulator {

        private final Label statusLabel = I18nControls.newLabel(I18nKeys.upperCase(VideosI18nKeys.OnTime));
        private final Button actionButton = Bootstrap.dangerButton(I18nControls.newButton(VideosI18nKeys.Watch));
        private final ScheduledItem scheduledItem;
        //private Attendance attendance;
        private final UpdateStore updateStore;
        private final BooleanProperty attendanceIsAttendedProperty;

        public VideoSchedulePopulator(ScheduledItem s) {
            actionButton.setGraphicTextGap(10);
            actionButton.setCursor(Cursor.HAND);
            actionButton.setMinWidth(130);
            statusLabel.setWrapText(true);
            statusLabel.setPadding(new Insets(0, 10, 0, 0));
            scheduledItem = s;
            updateStore = UpdateStore.createAbove(entityStore);
            //attendance = updateStore.updateEntity(a);
            attendanceIsAttendedProperty = new SimpleBooleanProperty(false);//EntityBindings.getBooleanFieldProperty(attendance,Attendance.attended);
            BooleanProperty scheduledItemPublishedProperty = EntityBindings.getBooleanFieldProperty(scheduledItem, ScheduledItem.published);
            attendanceIsAttendedProperty.addListener(e ->
                UiScheduler.scheduleDelay(3000, () -> {
                    if (attendanceIsAttendedProperty.get()) {
                        I18nControls.bindI18nProperties(actionButton, VideosI18nKeys.WatchAgain);
                    }
                }));
            scheduledItemPublishedProperty.addListener(e -> Platform.runLater(this::computeStatusLabelAndWatchButton));
        }

        public void populateVideoRow() {
            //we initialise statusLabel and actionButton
            computeStatusLabelAndWatchButton();

            if (statusLabel != null) {
                statusMonoPane.setContent(statusLabel);
            }
            // Name label
            //If the name of the video scheduledItem has been overwritten, we use it, otherwise, we use the name of the programScheduledItem
            String name = scheduledItem.getProgramScheduledItem().getName();
            if (scheduledItem.getName() != null && !scheduledItem.getName().isBlank()) {
                name = scheduledItem.getName();
            }
            Label nameLabel = new Label(name);
            nameLabel.setWrapText(true);
            nameLabel.setPadding(new Insets(0, 10, 0, 0));

            // Handle expiration date
            if (scheduledItem.getExpirationDate() != null) {
                String key = scheduledItem.getExpirationDate().isAfter(LocalDateTime.now())
                    ? VideosI18nKeys.VideoAvailableUntil
                    : VideosI18nKeys.VideoExpiredOn;

                Label expirationDateLabel = Bootstrap.small(Bootstrap.textDanger(I18nControls.newLabel(
                    key,
                    scheduledItem.getExpirationDate().format(DateTimeFormatter.ofPattern("d MMMM, uuuu ' - ' HH:mm"))
                )));
                expirationDateLabel.setWrapText(true);

                nameVBox.setAlignment(Pos.TOP_LEFT);
                nameVBox.getChildren().addAll(nameLabel, expirationDateLabel);
            } else {
                nameVBox.getChildren().add(nameLabel);
            }
            nameVBox.setAlignment(Pos.CENTER_LEFT);
            // Time label
            Label timeLabel;
            if (scheduledItem.getEvent().isRecurringWithVideo()) {
                timeLabel = new Label(
                    scheduledItem.getProgramScheduledItem().getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")) + " - " +
                        scheduledItem.getProgramScheduledItem().getEndTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                );
            } else {
                timeLabel = new Label(
                    scheduledItem.getProgramScheduledItem().getTimeline().getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")) + " - " +
                        scheduledItem.getProgramScheduledItem().getTimeline().getEndTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                );
            }
            timeVBox.setAlignment(Pos.CENTER_LEFT);
            timeVBox.getChildren().add(timeLabel);

            // Remarks label
            Label remarkLabel = new Label(scheduledItem.getComment());
            remarkLabel.getStyleClass().add(Bootstrap.TEXT_INFO);
            remarkLabel.setWrapText(true);
            remarkLabel.setPadding(new Insets(0, 10, 0, 0));
            remarkMonoPane.setAlignment(Pos.CENTER_LEFT);
            remarkMonoPane.setMaxWidth(REMARK_PREF_SIZE);
            remarkMonoPane.setContent(remarkLabel);

            // Button
            actionButtonMonoPane.setContent(actionButton);
            mainLine.setAlignment(Pos.CENTER_LEFT);
            mainLine.getChildren().addAll(dateMonoPane,statusMonoPane,nameVBox,timeVBox,remarkMonoPane,actionButtonMonoPane);

            // Separator
            Separator sessionSeparator = new Separator();
            if (scheduledItem.getEvent().getType().getRecurringItem() == null) {
                sessionSeparator.setPadding(new Insets(35, 0, 15, 0));
            } else {
                sessionSeparator.setPadding(new Insets(15, 0, 0, 0));
            }
            mainVBox.getChildren().addAll(mainLine,sessionSeparator);
        }

        private void computeStatusLabelAndWatchButton() {

            //THE STATE
            LocalDateTime sessionStart;
            LocalDateTime sessionEnd;
            if (scheduledItem.getEvent().isRecurringWithVideo()) {
                sessionStart = scheduledItem.getDate().atTime(scheduledItem.getProgramScheduledItem().getStartTime());
                sessionEnd = scheduledItem.getDate().atTime(scheduledItem.getProgramScheduledItem().getStartTime());
            } else {
                sessionStart = scheduledItem.getDate().atTime(scheduledItem.getProgramScheduledItem().getTimeline().getStartTime());
                sessionEnd = scheduledItem.getDate().atTime(scheduledItem.getProgramScheduledItem().getTimeline().getEndTime());
            }
            // For now, we manage the case when the livestream link is unique for the whole event, which is the case with Castr, which is the platform we generally use
            // TODO: manage the case when the livestream link is not global but per session, which happens on platform like youtube, etc.

            //The live is currently playing, we display this 2 minutes before the beginning
            if (LocalDateTime.now().isAfter(sessionStart.minusMinutes(2)) && LocalDateTime.now().isBefore(sessionEnd)) {
                I18nControls.bindI18nProperties(statusLabel, I18nKeys.upperCase(VideosI18nKeys.LiveNow));
                actionButton.setOnAction(e -> {
                    browsingHistory.push(LivestreamPlayerRouting.getLivestreamPath(scheduledItem.getEventId()));
                    //attendance.setAttended(true);
                    updateStore.submitChanges()
                        .onFailure(Console::log)
                        .onSuccess(Console::log);
                });
                actionButton.setVisible(true);
                Duration duration = Duration.between(LocalDateTime.now(), sessionEnd);
                if (duration.getSeconds() > 0)
                    scheduleRefreshUI(duration.getSeconds());
                return;
            }

            //The session has not started yet
            if (LocalDateTime.now().isBefore(sessionStart)) {
                Duration duration = Duration.between(LocalDateTime.now(), sessionStart);

                //We display the countdown 3 hours before the session
                if (duration.getSeconds() > 0 && duration.getSeconds() < 3600 * 3) {
                    I18nControls.bindI18nProperties(statusLabel, I18nKeys.upperCase(VideosI18nKeys.StartingIn), formatDuration(duration));
                    //We refresh every second
                    scheduleRefreshUI(1);
                    //We display the play button 30 minutes before the session
                    if (duration.getSeconds() < 60 * 30) {
                        actionButton.setOnAction(e -> browsingHistory.push(LivestreamPlayerRouting.getLivestreamPath(scheduledItem.getEventId())));
                        actionButton.setVisible(true);
                        //attendance.setAttended(true);
                        updateStore.submitChanges()
                            .onFailure(Console::log)
                            .onSuccess(Console::log);
                    } else {
                        hideActionButton();
                    }
                } else {
                    I18nControls.bindI18nProperties(statusLabel, I18nKeys.upperCase(VideosI18nKeys.OnTime));
                    scheduleRefreshUI(60);
                    hideActionButton();
                }
                return;
            }

            //Case of the video expired
            LocalDateTime expirationDate = scheduledItem.getEvent().getVodExpirationDate();
            //We look if the current video is expired
            if (scheduledItem.getExpirationDate() != null) {
                expirationDate = scheduledItem.getExpirationDate();
            }
            if (expirationDate != null && Times.isPast(expirationDate)) {
                //TODO: when we know how we will manage the timezone, we adapt to take into account the different timezone
                //TODO: when a push notification is sent we have to update this also.
                I18nControls.bindI18nProperties(statusLabel, I18nKeys.upperCase(VideosI18nKeys.Expired));
                hideActionButton();
                return;
            }

            //The recording of the video has been published
            if (scheduledItem.isPublished()) {
                I18nControls.bindI18nProperties(statusLabel, I18nKeys.upperCase(VideosI18nKeys.Available));
                actionButton.setOnAction(e -> {
                    browsingHistory.push(SessionVideoPlayerRouting.getVideoOfSessionPath(scheduledItem.getId()));
                    // attendance.setAttended(true);
                    updateStore.submitChanges()
                        .onFailure(Console::log)
                        .onSuccess(Console::log);
                });
                actionButton.setVisible(true);
                if (expirationDate != null) {
                    //We schedule a refresh so the UI is updated when the expirationDate is reached
                    Duration duration = Duration.between(LocalDateTime.now(), expirationDate);
                    if (duration.getSeconds() > 0) {
                        scheduleRefreshUI(duration.getSeconds());
                    }
                }
                return;
            }

            //case of the video delayed: the video is delayed
            if (scheduledItem.isVodDelayed()) {
                I18nControls.bindI18nProperties(statusLabel, I18nKeys.upperCase(VideosI18nKeys.VideoDelayed));
                hideActionButton();
                scheduleRefreshUI(60);
                return;
            }

            //The live has ended, we're waiting for the video to be published
            if (!scheduledItem.isPublished()) {
                //The default value of the processing time if this parameter has not been entered
                int vodProcessingTimeMinute = getVodProcessingTimeMinute(scheduledItem);
                if (LocalDateTime.now().isAfter(sessionEnd.plusMinutes(vodProcessingTimeMinute))) {
                    I18nControls.bindI18nProperties(statusLabel, I18nKeys.upperCase(VideosI18nKeys.VideoDelayed));
                    hideActionButton();
                    //A push notification will tell us when the video recording will be available
                    return;
                }

                I18nControls.bindI18nProperties(statusLabel, I18nKeys.upperCase(VideosI18nKeys.RecordingSoonAvailable));
                hideActionButton();
                //A push notification will tell us when the video recording will be available
            }
        }

        private void scheduleRefreshUI(long i) {
            long refreshTime = i * 1000;
            if (i > 59) {
                //If we want to refresh more than 1 minutes, we add a second to make sure the calculation has time to proceed before the refresh
                refreshTime = refreshTime + 1000;
            }
            UiScheduler.scheduleDelay(refreshTime, this::computeStatusLabelAndWatchButton);
        }

     /*   private void updateVODButton(Object e) {
            ReadOnlyAstObject message = (ReadOnlyAstObject) e;
            Object updatedScheduledItemId = message.get("id");
            String messageType = message.get("messageType");
            if (Objects.equals(scheduledItem.getPrimaryKey(), updatedScheduledItemId) && "VIDEO_STATE_CHANGED".equals(messageType)) {
                //Here we need to reload the datas from the database to display the button
                String dqlQuery;
                if (scheduledItem.getEvent().isRecurringWithVideo()) {
                    //case sttp, GP, etc.
                    dqlQuery = "select date, expirationDate, event, vodDelayed, published, comment, programScheduledItem.(name, date,startTime, endTime, item.imageUrl)," +
                        " exists(select Media where scheduledItem=si) as " + EventVideosWallActivity.VIDEO_ATTENDANCE_DYNAMIC_BOOLEAN_FIELD_ATTENDED +
                        " from ScheduledItem si where si.id=?" + "and online and exists(select Attendance where scheduledItem=si and documentLine.(!cancelled and document.(event= ? and person=? and price_balance<=0)))" +
                        " order by date, programScheduledItem.date";
                } else {
                    //Case festival, etc.
                    dqlQuery = "select date, expirationDate, event, vodDelayed, published, comment, programScheduledItem.(name, date,timeline.(startTime, endTime), item.imageUrl)," +
                        " exists(select Media where scheduledItem=si) as " + EventVideosWallActivity.VIDEO_ATTENDANCE_DYNAMIC_BOOLEAN_FIELD_ATTENDED +
                        " from ScheduledItem si where si.id=?" + "and online and exists(select Attendance where scheduledItem=si and documentLine.(!cancelled and document.(event= ? and person=? and price_balance<=0)))" +
                        " order by date, programScheduledItem.timeline.startTime";
                }


                entityStore.executeQuery(
                        new EntityStoreQuery(dqlQuery,
                            new Object[]{updatedScheduledItemId, scheduledItem.getEvent(), FXUserPersonId.getUserPersonId()}))
                    .onFailure(Console::log)
                    .onSuccess(entityList ->
                        Platform.runLater(() -> {
                            ScheduledItem si = (ScheduledItem) entityList.get(0);
                            scheduledItem = si;
                            computeStatusLabelAndWatchButton();
                        }));
            }
        }*/

        private void hideActionButton() {
            actionButton.setVisible(false);
            actionButton.setOnAction(null);
        }

        private int getVodProcessingTimeMinute(ScheduledItem currentVideo) {
            int vodProcessingTimeMinute = 60;
            if (currentVideo.getEvent().getVodProcessingTimeMinutes() != null)
                vodProcessingTimeMinute = currentVideo.getEvent().getVodProcessingTimeMinutes();
            return vodProcessingTimeMinute;
        }
    }

    private static String formatDuration(Duration duration) {
        if (duration == null)
            return "xx:xx";
        int hours = (int) duration.toHours();
        int minutes = ((int) duration.toMinutes()) % 60;
        int seconds = ((int) duration.toSeconds()) % 60;
        return (hours < 10 ? "0" : "") + hours + ":" + (minutes < 10 ? "0" : "") + minutes + ":" + (seconds < 10 ? "0" : "") + seconds;
    }
}
