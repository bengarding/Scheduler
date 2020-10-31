package scheduler;

import datasource.Appointment;
import datasource.Customer;
import datasource.Datasource;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Objects;

public class MainController {

    private ObservableList<Customer> customers = FXCollections.observableArrayList(Datasource.getAllCustomers());
    private FilteredList<Appointment> appointmentsFiltered = new FilteredList<>(Objects.requireNonNull(Datasource.getAllAppointments()));

    @FXML
    private TableView<Appointment> appointmentTable;
    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private RadioButton monthRadio;
    @FXML
    private RadioButton weekRadio;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Tab appointmentsTab;
    @FXML
    private Tab customerTab;

    public void initialize() {
        datePicker.setValue(LocalDate.of(2020, 6, 1));
        ObservableList<Customer> customers = FXCollections.observableArrayList(Datasource.getAllCustomers());

        radioSelected();
        customerTable.setItems(customers);
    }

    @FXML
    private void radioSelected() {
        if (monthRadio.isSelected()) {
            appointmentsFiltered.predicateProperty().bind(Bindings.createObjectBinding(() -> {
                        LocalDate monthStart = datePicker.getValue().minusDays(datePicker.getValue().getDayOfMonth() - 1);
                        LocalDate monthEnd = monthStart.plusMonths(1).minusDays(1);

                        return ti -> !monthStart.isAfter(ti.getStart().toLocalDate()) && !monthEnd.isBefore(ti.getStart().toLocalDate());
                    }, datePicker.valueProperty()
            ));
        } else {
            appointmentsFiltered.predicateProperty().bind(Bindings.createObjectBinding(() -> {
                        DayOfWeek dayOfWeek = datePicker.getValue().getDayOfWeek();
                        int dayInt = 0;
                        switch (dayOfWeek) {
                            case MONDAY:
                                dayInt = 1;
                                break;
                            case TUESDAY:
                                dayInt = 2;
                                break;
                            case WEDNESDAY:
                                dayInt = 3;
                                break;
                            case THURSDAY:
                                dayInt = 4;
                                break;
                            case FRIDAY:
                                dayInt = 5;
                                break;
                            case SATURDAY:
                                dayInt = 6;
                                break;
                        }
                        LocalDate weekStart = datePicker.getValue().minusDays(dayInt);
                        LocalDate weekEnd = weekStart.plusDays(6);

                        return ti -> !weekStart.isAfter(ti.getStart().toLocalDate()) && !weekEnd.isBefore(ti.getStart().toLocalDate());
                    }, datePicker.valueProperty()
            ));
        }
        appointmentTable.setItems(appointmentsFiltered);
    }

    @FXML
    private void tabSelected() {
        try {
            if (customerTab.isSelected()) {
                monthRadio.setVisible(false);
                weekRadio.setVisible(false);
                datePicker.setVisible(false);
            } else if (appointmentsTab.isSelected()) {
                monthRadio.setVisible(true);
                weekRadio.setVisible(true);
                datePicker.setVisible(true);
            }
        } catch (NullPointerException e) {
            //Not sure why it's trowing a NullPointerException. Everything works as intended
        }
    }
}
