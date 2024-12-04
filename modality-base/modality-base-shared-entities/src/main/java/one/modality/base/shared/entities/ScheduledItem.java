package one.modality.base.shared.entities;

import dev.webfx.stack.orm.entity.Entity;
import dev.webfx.stack.orm.entity.EntityId;
import one.modality.base.shared.entities.markers.*;

import java.time.LocalDateTime;

/**
 * @author Bruno Salmon
 */
public interface ScheduledItem extends Entity,
    EntityHasName,
    EntityHasLabel,
    EntityHasEvent,
    EntityHasTeacher,
    EntityHasLocalDate,
    EntityHasSiteAndItem,
    EntityHasStartAndEndTime {

    default void setProgramScheduledItem(Object programScheduledItem) {
        setForeignField("programScheduledItem", programScheduledItem);
    }

    default EntityId getProgramScheduledItemId() {
        return getForeignEntityId("programScheduledItem");
    }

    default ScheduledItem getProgramScheduledItem() {
        return getForeignEntity("programScheduledItem");
    }

    default void setBookableScheduledItem(Object bookableScheduledItem) {
        setForeignField("bookableScheduledItem", bookableScheduledItem);
    }

    default EntityId getBookableScheduledItemId() {
        return getForeignEntityId("bookableScheduledItem");
    }

    default ScheduledItem getBookableScheduledItem() {
        return getForeignEntity("bookableScheduledItem");
    }

    default void setTimeLine(Object timeline) {
        setForeignField("timeline", timeline);
    }

    default EntityId getTimelineId() {
        return getForeignEntityId("timeline");
    }

    default Timeline getTimeline() {
        return getForeignEntity("timeline");
    }

    default void setExpirationDate(LocalDateTime expirationDate) {
        setFieldValue("expirationDate", expirationDate);
    }

    default LocalDateTime getExpirationDate() {
        return getLocalDateTimeFieldValue("expirationDate");
    }

    default void setComment(String comment) {
        setFieldValue("comment", comment);
    }

    default String getComment() {
        return getStringFieldValue("comment");
    }

    default void setCommentLabel(Object label) {
        setForeignField("commentLabel", label);
    }

    default EntityId getCommentLabelId() {
        return getForeignEntityId("commentLabel");
    }

    default Label getCommentLabel() {
        return getForeignEntity("commentLabel");
    }

    default void setVodDelayed(Boolean vodDelayed) {
        setFieldValue("vodDelayed", vodDelayed);
    }

    default Boolean isVodDelayed() {
        return getBooleanFieldValue("vodDelayed");
    }

    default void setPublished(Boolean published) {
        setFieldValue("published", published);
    }

    default Boolean isPublished() {
        return getBooleanFieldValue("published");
    }

}
