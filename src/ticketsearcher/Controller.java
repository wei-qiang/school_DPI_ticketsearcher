package ticketsearcher;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.BookingRequest;
import model.RequestReply;
import model.TicketReply;
import model.TicketRequest;

public class Controller implements Initializable {

  @FXML
  private TextField tbFrom;
  @FXML
  private TextField tbTo;
  @FXML
  private DatePicker dpTravelDate;
  @FXML
  private ListView ticketList;
  @FXML
  private Button btnSearch;
  @FXML
  private Button btnBook;

  private TicketSearcherApplication ticketSearcherApplication;

  public Controller() {
    ticketSearcherApplication = new TicketSearcherApplication(this);
  }

  @FXML
  private void handleButtonActionSearch(ActionEvent event) {
    if (tbFrom.getText().isEmpty() || tbTo.getText().isEmpty()) {
      alert("Please fill in the textboxes");
      return;
    } else if (dpTravelDate.getValue() == null) {
      alert("Please select a date");
      return;
    } else if (!dpTravelDate.getValue().isAfter(LocalDate.now())) {
      alert("Date can't be in the past!");
      return;
    }

    //get date from datepicker
    LocalDate localDate = dpTravelDate.getValue();
    Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
    //create ticket request
    TicketRequest ticketRequest = new TicketRequest(1, tbFrom.getText(), tbTo.getText(),
        Date.from(instant));
    //send request and get correlationId
    String correlationId = ticketSearcherApplication.sendTicketRequest(ticketRequest);
    //store correlationId
    ticketSearcherApplication.getTicketRequestHashMap().put(correlationId, ticketRequest);

    updateTicketList(ticketSearcherApplication.getRequestReplies());
  }

  @FXML
  private void handleButtonActionBook(ActionEvent event) {
    RequestReply<TicketRequest, TicketReply> selectedFlight = (RequestReply<TicketRequest, TicketReply>) ticketList
        .getSelectionModel().getSelectedItem();

    if (selectedFlight.getRequest() == null) {
      alert("No response has been given yet");
      return;
    }

    BookingRequest bookingRequest = new BookingRequest(1, selectedFlight.getReply().getFlightId(),
        selectedFlight.getReply().getAirline());

    String correlationId = ticketSearcherApplication
        .sendBookingRequest(selectedFlight, bookingRequest);

    ticketSearcherApplication.getBookingRequestHashMap().put(correlationId, bookingRequest);

    updateTicketList(ticketSearcherApplication.getRequestReplies());
  }

  public void onRepliesArrived(List<RequestReply> requestReplies) {
    updateTicketList(requestReplies);
  }

  private void alert(String alertText) {
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Information Dialog");
    alert.setHeaderText(null);
    alert.setContentText(alertText);

    alert.showAndWait();
  }

  private void updateTicketList(List<RequestReply> requestReplies) {
    Platform.runLater(() -> {
      ticketList.getItems().clear();
      ticketList.getItems().addAll(FXCollections.observableList(requestReplies));
    });
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

  }
}
