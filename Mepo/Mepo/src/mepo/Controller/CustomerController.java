/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mepo.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.util.Optional;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import mepo.Helper.ClientHelper;
import mepo.Components.User;
import mepo.Implements.UserImp;
import mepo.Navigator;

/**
 * FXML Controller class
 *
 * @author Dell
 */
public class CustomerController {

    private UserImp UserImp = new ClientHelper();
    private User user = null;

    @FXML
    private JFXButton btnHome;
    @FXML
    private JFXButton btnAdmin;
    @FXML
    private JFXButton btnCustomer;
    @FXML
    private JFXButton btnCategory;
    @FXML
    private JFXButton btnProduct;
    @FXML
    private JFXButton btnOrder;
    @FXML
    private JFXButton btnFeedback;
    @FXML
    private JFXButton btnLogout;
    @FXML
    private TableView<User> tvAdmin;
    @FXML
    private TableColumn<User, String> tcUsername;
    @FXML
    private TableColumn<User, String> tcPassHash;
    @FXML
    private TableColumn<User, String> tcFullName;
    @FXML
    private TableColumn<User, String> tcContact;
    @FXML
    private Button btnCreate;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnEdit;
    @FXML
    private Label txtUser;

    @FXML
    private JFXTextField txtSearch;

    /**
     * Initializes the controller class.
     */
    public void initialize(User username) {
        this.user = username;
        txtUser.setText("User: " + this.user.getUsername());
        search();
    }
    
    void search() {
        ObservableList<User> result = UserImp.selectAll();
        tvAdmin.setItems(result);

        tcUsername.setCellValueFactory((c) -> {
            return c.getValue().getUsernameProperty();
        });
        tcPassHash.setCellValueFactory((c) -> {
            return c.getValue().getPasswordProperty();
        });
        tcFullName.setCellValueFactory((c) -> {
            return c.getValue().getFullnameProperty();
        });
        tcContact.setCellValueFactory((c) -> {
            return c.getValue().getContactProperty();
        });

        FilteredList<User> fillteredData = new FilteredList<>(result, b -> true);

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            fillteredData.setPredicate((cat) -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lower = newValue.toLowerCase();
                if (cat.getUsername().toLowerCase().indexOf(lower) != -1) {
                    return true;
                } else if (cat.getFullName().toLowerCase().indexOf(lower) != -1) {
                    return true;
                } else {
                    return false;
                }
            });
        });
        SortedList<User> sortedData = new SortedList<>(fillteredData);
        sortedData.comparatorProperty().bind(tvAdmin.comparatorProperty());
        tvAdmin.setItems(sortedData);
    }

    @FXML
    void logoClicked(MouseEvent event) throws IOException {
        Navigator.getInstance().goToHomeAdmin(user);
    }

    @FXML
    private void btnHomeClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToHomeAdmin(user);
    }

    @FXML
    private void btnAdminClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToAdmin(user);
    }

    @FXML
    private void btnCustomerClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToCustomer(user);
    }

    @FXML
    private void btnCategoryClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToCategory(user);
    }

    @FXML
    private void btnProductClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToProducts(user);
    }

    @FXML
    private void btnOrderClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToOrderAdmin(user);
    }

    @FXML
    private void btnFeedbackClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToFeedbackAdmin(user);
    }

    @FXML
    private void btnLogoutClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToLoginAdmin();
    }

    @FXML
    private void btnCreateClick(ActionEvent event) throws IOException {
        Navigator.getInstance().goToCustomerCreate(user);
    }

    @FXML
    private void btnDeleteClick(ActionEvent event) {
        User u = tvAdmin.getSelectionModel().getSelectedItem();
        if (u == null) {
            alert("Warning", "Please select a Customer");
        } else {
            String title = "Delete";
            String text = "Are you sure you want to delete this Customer?";
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(title);
            alert.setHeaderText(text);
            Optional<ButtonType> confirmationResponse
                    = alert.showAndWait();
            if (confirmationResponse.get() == ButtonType.OK) {
                User us = u;
                boolean result = UserImp.delete(us);
                search();
                if (result) {
                    tvAdmin.getItems().remove(us);
                    System.out.println("A Customer deleted");
                } else {
                    System.out.println("No Customer is deleted");
                }
            }
        }
    }

    @FXML
    private void btnEditClick(ActionEvent event) throws IOException {
        User editUser = tvAdmin.getSelectionModel().getSelectedItem();
        if (editUser == null) {
            alert("Warning", "Please select the Customer you want to edit");
        } else {
            Navigator.getInstance().goToCustomerEdit(user, editUser);
        }
    }

    private void alert(String title, String text) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(text);
        alert.showAndWait();
    }
    @FXML
     void btnActionTuCoins(ActionEvent actionEvent) throws IOException {
        Navigator.getInstance().goToDialogHistoryAdmin(user);
    }
    @FXML
    void btnActionCard(ActionEvent actionEvent) throws IOException {
        Navigator.getInstance().goToCardAdmin(user);
    }
}
