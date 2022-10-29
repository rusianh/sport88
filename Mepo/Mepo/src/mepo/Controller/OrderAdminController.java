/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mepo.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.Optional;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import mepo.Components.Order;
import mepo.Components.Product;
import mepo.Helper.OrderHelper;
import mepo.Components.OrderDetail;
import mepo.Helper.OrderDetailHelper;
import mepo.Components.User;

import mepo.Navigator;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author Dell
 */
public class OrderAdminController {

    public static User user = null;
    mepo.Implements.OrderImp OrderImp = new OrderHelper();
    mepo.Implements.OrderDetailImp OrderDetailImp = new OrderDetailHelper();
    //    public static BooleanProperty ACP = new SimpleBooleanProperty();
    public static StringProperty TTMN = new SimpleStringProperty();
    private static DecimalFormat df = new DecimalFormat("#.00");
    private int ided = 0;
    public static ObservableList<OrderDetail> RESULT = FXCollections.observableArrayList();

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
    private Label txtUser;
    @FXML
    private TableView<Order> tvOrder;
    @FXML
    private TableColumn<Order, String> tcUsername;
    @FXML
    private TableColumn<Order, String> tcOrderdate;
    @FXML
    private TableColumn<Order, String> tcAccountAddress;

    @FXML
    private GridPane gridOrderDetail;
    @FXML
    private JFXTextField txtSearch;


    @FXML
    private Label txtPrice;


    @FXML
    private JFXButton bCancel;

    /**
     * Initializes the controller class.
     */
    public void initialize(User username) {
        this.user = username;

        TTMN.set("0");

        search();
        ObservableList<Order> list = tvOrder.getItems();
        if (!list.isEmpty()) {
            Order od = tvOrder.getItems().get(0);

            RESULT = OrderDetailImp.selectOrderDetailByOrderID(od.getOrderID());

            for (OrderDetail o : RESULT) {
                Product pro = OrderDetailImp.selectProductsByOrderProductsID(o.getProductID());
                float prc = Float.parseFloat(pro.getPrice());
                float f = Float.parseFloat(TTMN.get());
                f += prc;
                String s = String.valueOf(df.format(f));
                TTMN.set(s);
                txtPrice.setText(s);
            }

            addOrder(RESULT);
        }

        RESULT.addListener(new ListChangeListener<OrderDetail>() {
            @Override
            public void onChanged(javafx.collections.ListChangeListener.Change<? extends OrderDetail> c) {
                if (c.next()) {
                    addOrder(RESULT);
                }
            }
        });

        OrderItemAdminController.RMV.addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue) {

                        ObservableList<OrderDetail> rs = OrderDetailImp.selectOrderDetailByOrderID(ided);
                        addOrder(rs);
                        OrderItemAdminController.RMV.set(false);
                    }
                }
        );
    }

    @FXML
    void btnActionTuCoins(ActionEvent actionEvent) throws IOException {
        Navigator.getInstance().goToDialogHistoryAdmin(user);
    }

    @FXML
    void btnActionCard(ActionEvent actionEvent) throws IOException {
        Navigator.getInstance().goToCardAdmin(user);
    }

    void search() {
        ObservableList<Order> result = OrderImp.selectAll();

        tvOrder.setItems(result);

        tcUsername.setCellValueFactory((c) -> {
            return c.getValue().getUsernameProperty();
        });
        tcOrderdate.setCellValueFactory((c) -> {
            return c.getValue().getOrderDateProperty();
        });
        tcAccountAddress.setCellValueFactory((c) -> {
            return c.getValue().getAccountAddressProperty();
        });

        FilteredList<Order> fillteredData = new FilteredList<>(result, b -> true);

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            fillteredData.setPredicate((cat) -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lower = newValue.toLowerCase();

                if (cat.getUsername().toLowerCase().indexOf(lower) != -1) {
                    return true;
                } else if (cat.getOrderDate().toLowerCase().indexOf(lower) != -1) {
                    return true;
                } else if (cat.getAccountAddress().toLowerCase().indexOf(lower) != -1) {
                    return true;
                } else {
                    return false;
                }
            });
        });
        SortedList<Order> sortedData = new SortedList<>(fillteredData);
        sortedData.comparatorProperty().bind(tvOrder.comparatorProperty());
        tvOrder.setItems(sortedData);
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
    private void selectFeedback(MouseEvent event) {
        try {
            Order od = tvOrder.getSelectionModel().getSelectedItem();
            ided = od.getOrderID();
            RESULT.clear();
            RESULT = OrderDetailImp.selectOrderDetailByOrderID(od.getOrderID());
            addOrder(RESULT);
            RESULT.addListener(new ListChangeListener<OrderDetail>() {
                @Override
                public void onChanged(javafx.collections.ListChangeListener.Change<? extends OrderDetail> c) {
                    if (c.next()) {
                        addOrder(RESULT);
                    }
                }
            });

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    void addOrder(ObservableList<OrderDetail> rs) {
        TTMN.set("0");
        gridOrderDetail.getChildren().clear();
        int column = 1;
        int row = 0;
        try {
            for (OrderDetail o : rs) {
                Product pro = OrderDetailImp.selectProductsByOrderProductsID(o.getProductID());
                float prc = Float.parseFloat(pro.getPrice());
                float f = Float.parseFloat(TTMN.get());
                f += prc;
                String s = String.valueOf(f);
                TTMN.set(s);

                ++row;
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/mepo/view/orderItemAdmin.fxml"));
                Pane pane = fxmlLoader.load();
                OrderItemAdminController fbCTRL = fxmlLoader.getController();
                fbCTRL.setData(o);
                gridOrderDetail.add(pane, column, row);
                GridPane.setMargin(pane, new Insets(5));
            }
            setTotalMoney();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    void setTotalMoney() {
        TTMN.addListener((observable, oldValue, newValue) -> {
            float f = Float.parseFloat(newValue);
            String s = String.valueOf(df.format(f));
            txtPrice.setText(s);
        });
    }


    @FXML
    private void searchClick(MouseEvent event) {
    }

}
