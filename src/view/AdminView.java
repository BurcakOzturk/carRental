package view;

import business.BookManager;
import business.BrandManager;
import business.CarManager;
import business.ModelManager;
import core.ComboItem;
import core.Helper;
import entity.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.util.ArrayList;

public class AdminView extends Layout {
    private JPanel container;
    private JLabel lbl_greeting;
    private JPanel pnl_top;
    private JTabbedPane tab_menu;
    private JButton btn_logout;
    private JPanel pnl_brand;
    private JScrollPane scrl_brand;
    private JTable tbl_brand;
    private JPanel pnl_model;
    private JScrollPane scrl_model;
    private JTable tbl_model;
    private JComboBox<ComboItem> cmb_s_model_brand;
    private JComboBox<Model.Transmission> cmb_s_model_transmission;
    private JComboBox<Model.Fuel> cmb_s_model_fuel;
    private JComboBox<Model.Type> cmb_s_model_type;
    private JButton btn_search_model;
    private JButton btn_cncl_model;
    private JPanel pnl_car;
    private JScrollPane scrl_car;
    private JTable tbl_car;
    private JPanel pnl_booking;
    private JScrollPane scrl_booking;
    private JTable tbl_booking;
    private JPanel pnl_booking_search;
    private JComboBox<Model.Type> cmb_booking_type;
    private JComboBox<Model.Fuel> cmb_booking_fuel;
    private JComboBox<Model.Transmission> cmb_booking_transmission;
    private JFormattedTextField fld_strt_date;
    private JFormattedTextField fld_fnsh_date;
    private JButton btn_booking_search;
    private JButton btn_model_clear;
    private JPanel pnl_book;
    private JScrollPane scrl_book;
    private JTable tbl_book;
    private JFormattedTextField fld_book_strt_date;
    private JFormattedTextField fld_book_fnsh_date;
    private JComboBox<ComboItem> cmb_book_car;
    private JButton btn_book_search;
    private JButton btn_cncl_book;
    private JPanel pnl_book_search;
    private User user;
    private DefaultTableModel tmdl_brand = new DefaultTableModel();
    private DefaultTableModel tmdl_model = new DefaultTableModel();
    private DefaultTableModel tmdl_car = new DefaultTableModel();
    private DefaultTableModel tmdl_booking = new DefaultTableModel();
    private DefaultTableModel tmdl_book = new DefaultTableModel();
    private BrandManager brandManager;
    private ModelManager modelManager;
    private BookManager bookManager;
    private CarManager carManager;
    private JPopupMenu brand_menu;
    private JPopupMenu model_menu;
    private JPopupMenu car_menu;
    private JPopupMenu booking_menu;
    private JPopupMenu book_menu;
    private Object[] col_model;
    private Object[] col_car;
    private Object[] col_book;


    public AdminView(User user) {

        this.modelManager = new ModelManager();
        this.brandManager = new BrandManager();
        this.carManager = new CarManager();
        this.bookManager = new BookManager();
        this.add(container);
        this.guiInitialize(1000, 500);
        this.user = user;

        if (this.user == null) {
            dispose();
        }

        this.lbl_greeting.setText("User: " + this.user.getUsername());

        loadComponent();

        //Tabs: Brand
        loadBrandTable();
        loadBrandComponent();

        //Tabs: Car Models
        loadModelTable(null);
        loadModelComponent();
        loadModelFilter();

        //Tabs: Cars
        loadCarTable(null);
        loadCarComponent();

        //Tabs: Reservation Creation
        loadBookingTable(null);
        loadBookingComponent();
        loadBookingFilter();

        //Tabs: Existing Reservations
        loadBookTable(null);
        loadBookComponent();
        loadBookFilterCar();
    }

    private void loadComponent() {
        this.btn_logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                LoginView loginView = new LoginView();
            }
        });
    }
    private void loadBookComponent() {
        tableRowSelect(this.tbl_book);
        this.book_menu = new JPopupMenu();
        this.book_menu.add("Cancel").addActionListener(e -> {
            if (Helper.confirm("sure")) {
                int selectBookId = this.getTableSelectedRow(this.tbl_book, 0);
                if (this.bookManager.delete(selectBookId)) {
                    Helper.showMessage("done");
                    loadBookTable(null);
                } else {
                    Helper.showMessage("error");
                }
            }
        });
        this.tbl_book.setComponentPopupMenu(book_menu);

        btn_book_search.addActionListener(e -> {
            ComboItem selectedCar = (ComboItem) this.cmb_book_car.getSelectedItem();
            int carId = 0;
            if (selectedCar != null) {
                carId = selectedCar.getKey();
            }

            ArrayList<Book> bookListBySearch = this.bookManager.searchForTable(carId);
            ArrayList<Object[]> bookRowListBySearch = this.bookManager.getForTable(this.col_book.length, bookListBySearch);
            loadBookTable(bookRowListBySearch);
        });

        this.btn_cncl_book.addActionListener(e -> {
            loadBookFilterCar();
        });
    }

    private void loadBookTable(ArrayList<Object[]> bookList) {
        col_book = new Object[]{"ID", "Plate", "Brand", "Model", "Client", "Phone", "E-mail", "TC ID", "Start Date", "End Date", "Total Cost"};
        if (bookList == null) {
            bookList = this.bookManager.getForTable(col_book.length, this.bookManager.findAll());
        }
        createTable(this.tmdl_book, this.tbl_book, col_book, bookList);
    }

    public void loadBookFilterCar() {
        this.cmb_book_car.removeAllItems();
        for (Car obj : this.carManager.findAll()) {
            this.cmb_book_car.addItem(new ComboItem(obj.getId(), obj.getPlate()));
        }
        this.cmb_book_car.setSelectedItem(null);
    }

    private void loadBookingComponent() {
        tableRowSelect(this.tbl_booking);
        this.booking_menu = new JPopupMenu();
        this.booking_menu.add("Create Reservation").addActionListener(e -> {
            int selectCarId = this.getTableSelectedRow(this.tbl_booking, 0);
            BookingView bookingView = new BookingView(
                    this.carManager.getById(selectCarId),
                    this.fld_strt_date.getText(),
                    this.fld_fnsh_date.getText()
            );
            bookingView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadBookingTable(null);
                    loadBookingFilter();
                    loadBookTable(null);
                }
            });
        });
        this.tbl_booking.setComponentPopupMenu(booking_menu);

        btn_booking_search.addActionListener(e -> {
            ArrayList<Car> carList = this.carManager.searchForBooking(
                    fld_strt_date.getText(),
                    fld_fnsh_date.getText(),
                    (Model.Type) cmb_booking_type.getSelectedItem(),
                    (Model.Transmission) cmb_booking_transmission.getSelectedItem(),
                    (Model.Fuel) cmb_booking_fuel.getSelectedItem()
            );

            ArrayList<Object[]> carBookingRow = this.carManager.getForTable(this.col_car.length, carList);
            loadBookingTable(carBookingRow);
        });
        btn_model_clear.addActionListener(e -> {
            loadBookingFilter();
        });
    }

    private void loadBookingTable(ArrayList<Object[]> carList) {
        Object[] col_booking_list = {"ID", "Brand", "Model", "Plate", "Color", "KM", "Year", "Type", "Fuel", "Transmission"};
        createTable(this.tmdl_booking, this.tbl_booking, col_booking_list, carList);
    }

    public void loadBookingFilter() {
        this.cmb_booking_type.setModel(new DefaultComboBoxModel<>(Model.Type.values()));
        this.cmb_booking_type.setSelectedItem(null);
        this.cmb_booking_transmission.setModel(new DefaultComboBoxModel<>(Model.Transmission.values()));
        this.cmb_booking_transmission.setSelectedItem(null);
        this.cmb_booking_fuel.setModel(new DefaultComboBoxModel<>(Model.Fuel.values()));
        this.cmb_booking_fuel.setSelectedItem(null);
    }

    private void loadModelComponent() {
        tableRowSelect(this.tbl_model);
        this.model_menu = new JPopupMenu();
        this.model_menu.add("Add").addActionListener(e -> {
            ModelView modelView = new ModelView(new Model());
            modelView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadModelTable(null);
                }
            });
        });
        this.model_menu.add("Update").addActionListener(e -> {
            int selectModelId = this.getTableSelectedRow(tbl_model, 0);
            ModelView modelView = new ModelView(this.modelManager.getById(selectModelId));
            modelView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadModelTable(null);
                    loadCarTable(null);
                    loadBookTable(null);
                }
            });
        });
        this.model_menu.add("Remove").addActionListener(e -> {
            if (Helper.confirm("confirm")) {
                int selectModelId = this.getTableSelectedRow(tbl_model, 0);
                if (this.modelManager.delete(selectModelId)) {
                    Helper.showMessage("done");
                    loadModelTable(null);
                } else {
                    Helper.showMessage("error");
                }
            }
        });

        this.tbl_model.setComponentPopupMenu(model_menu);
    }

    public void loadModelTable(ArrayList<Object[]> modelList) {
        this.col_model = new Object[]{"Model ID", "Brand", "Model", "Type", "Year", "Fuel", "Transmission"};
        if (modelList == null) {
            modelList = this.modelManager.getForTable(col_model.length, this.modelManager.findAll());
        }

        createTable(this.tmdl_model, this.tbl_model, col_model, modelList);
    }

    public void loadBrandTable() {
        Object[] col_brand = {"Brand ID", "Brand name"};
        ArrayList<Object[]> brandList = this.brandManager.getForTable(col_brand.length);

        this.createTable(this.tmdl_brand, this.tbl_brand, col_brand, brandList);
    }

    public void loadCarTable(ArrayList<Object[]> carList) {
        this.col_car = new Object[]{"ID", "Brand", "Model", "Plate", "Color", "KM", "Year", "Type", "Fuel", "Transmission"};
        if(carList == null){
            carList = this.carManager.getForTable(col_car.length,this.carManager.findAll());
        }
        createTable(this.tmdl_car,this.tbl_car,col_car,carList);
    }

    public void loadCarComponent() {
        tableRowSelect(this.tbl_car);
        this.car_menu = new JPopupMenu();
        this.car_menu.add("Add").addActionListener(e -> {
            CarView carView = new CarView(new Car());
            carView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadCarTable(null);
                }
            });
        });
        this.car_menu.add("Update").addActionListener(e -> {
            int selectModelId = this.getTableSelectedRow(tbl_car, 0);
            CarView carView = new CarView(this.carManager.getById(selectModelId));
            carView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadCarTable(null);
                }
            });
        });
        this.car_menu.add("Remove").addActionListener(e -> {
            if (Helper.confirm("confirm")) {
                int selectedCarId = this.getTableSelectedRow(tbl_car, 0);
                if (this.carManager.delete(selectedCarId)) {
                    Helper.showMessage("done");
                    loadCarTable(null);
                } else {
                    Helper.showMessage("error");
                }
            }
        });
        this.tbl_car.setComponentPopupMenu(car_menu);
    }

    public void loadModelFilter() {
        this.cmb_s_model_type.setModel(new DefaultComboBoxModel<>(Model.Type.values()));
        this.cmb_s_model_type.setSelectedItem(null);
        this.cmb_s_model_transmission.setModel(new DefaultComboBoxModel<>(Model.Transmission.values()));
        this.cmb_s_model_transmission.setSelectedItem(null);
        this.cmb_s_model_fuel.setModel(new DefaultComboBoxModel<>(Model.Fuel.values()));
        this.cmb_s_model_fuel.setSelectedItem(null);
        loadModelFilterBrand();
    }

    public void loadModelFilterBrand() {
        this.cmb_s_model_brand.removeAllItems();
        for (Brand obj : brandManager.findAll()) {
            this.cmb_s_model_brand.addItem(new ComboItem(obj.getId(), obj.getName()));
        }
        this.cmb_s_model_brand.setSelectedItem(null);
    }

    public void loadBrandComponent() {
        tableRowSelect(this.tbl_brand);
        this.brand_menu = new JPopupMenu();
        this.brand_menu.add("Add").addActionListener(e -> {
            BrandView brandView = new BrandView(null);
            brandView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadBrandTable();
                    loadModelTable(null);
                    loadModelFilterBrand();
                }
            });
        });
        this.brand_menu.add("Update").addActionListener(e -> {
            int selectBrandId = this.getTableSelectedRow(tbl_brand, 0);
            BrandView brandView = new BrandView(this.brandManager.getById(selectBrandId));
            brandView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadBrandTable();
                    loadModelTable(null);
                    loadModelFilterBrand();
                    loadCarTable(null);
                    loadBookTable(null);
                }
            });
        });
        this.brand_menu.add("Remove").addActionListener(e -> {
            if (Helper.confirm("sure")) {
                int selectBrandId = this.getTableSelectedRow(tbl_brand, 0);
                if (this.brandManager.delete(selectBrandId)) {
                    Helper.showMessage("done");
                    loadBrandTable();
                    loadModelTable(null);
                    loadModelFilterBrand();
                    loadCarTable(null);
                } else {
                    Helper.showMessage("error");
                }
            }
        });
        this.tbl_brand.setComponentPopupMenu(brand_menu);
    }


    private void createUIComponents() throws ParseException {
        this.fld_strt_date = new JFormattedTextField(new MaskFormatter("##/##/####"));
        this.fld_strt_date.setText("10/10/2023");
        this.fld_fnsh_date = new JFormattedTextField(new MaskFormatter("##/##/####"));
        this.fld_fnsh_date.setText("16/10/2023");

        this.fld_book_strt_date = new JFormattedTextField(new MaskFormatter("##/##/####"));
        this.fld_book_strt_date.setText("10/10/2023");
        this.fld_book_fnsh_date = new JFormattedTextField(new MaskFormatter("##/##/####"));
        this.fld_book_fnsh_date.setText("16/10/2023");
    }
}
