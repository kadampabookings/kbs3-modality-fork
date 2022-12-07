package one.modality.catering.backoffice.activities.kitchen;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;

public class AttendanceMonthPanel extends GridPane {

    private static final List<String> DAY_NAMES = Arrays.asList("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN");
    private static final String DAY_NAME_TEXT_BOX_STYLE = "-fx-border-color: lightgray; -fx-border-radius: 5;";
    private static final Color DAY_NAME_TEXT_COLOR = Color.web("#0096d6");
    private static final double HORIZONTAL_GAP = 16;

    public AttendanceMonthPanel(AttendanceCounts attendanceCounts, LocalDate month) {
        if (attendanceCounts != null) {
            addDayNames();
            addDayPanels(attendanceCounts, month);
            setHgap(HORIZONTAL_GAP);
        }
    }

    private void addDayNames() {
        int columnIndex = 0;
        for (String dayName : DAY_NAMES) {
            Text dayNameText = new Text(dayName);
            dayNameText.setFill(DAY_NAME_TEXT_COLOR);
            VBox dayNameTextBox = new VBox(dayNameText);
            dayNameTextBox.setStyle(DAY_NAME_TEXT_BOX_STYLE);
            dayNameTextBox.prefWidthProperty().bind(widthProperty().subtract(HORIZONTAL_GAP * 6).divide(7));
            dayNameTextBox.setAlignment(Pos.CENTER);
            dayNameTextBox.setPadding(new Insets(10, 0, 10, 0));
            add(dayNameTextBox, columnIndex, 0);
            columnIndex++;
        }
    }

    private void addDayPanels(AttendanceCounts attendanceCounts, LocalDate month) {
        int numDaysInMonth = YearMonth.of(month.getYear(), month.getMonth()).lengthOfMonth();
        int rowIndex = 1;
        for (int day = 0; day < numDaysInMonth; day++) {
            LocalDate dayDate = LocalDate.of(month.getYear(), month.getMonth(), day + 1);
            int columnIndex = dayDate.getDayOfWeek().ordinal();
            AttendanceDayPanel dayPanel = new AttendanceDayPanel(attendanceCounts, dayDate);
            add(dayPanel, columnIndex, rowIndex);
            if (columnIndex == 6) {
                rowIndex++;
            }
        }
    }
}
