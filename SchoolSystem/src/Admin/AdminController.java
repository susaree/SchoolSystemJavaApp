package Admin;

import dbUtil.dbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.xml.transform.Result;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    @FXML
    private TextField id;

    @FXML
    private TextField firstname;

    @FXML
    private TextField lastname;

    @FXML
    private TextField email;

    @FXML
    private DatePicker dob;

    @FXML
    private TableView<StudentData> studenttable;

    @FXML
    private TableColumn<StudentData, String> idcolumn;

    @FXML
    private TableColumn<StudentData, String> firstnamecolumn;

    @FXML
    private TableColumn<StudentData, String> lastnamecolumn;

    @FXML
    private TableColumn<StudentData, String> emailcolumn;

    @FXML
    private TableColumn<StudentData, String> dobcolumn;


    private dbConnection dc;
    private ObservableList<StudentData> data;


    private String sql = "SELECT * FROM students";

    public void initialize(URL url, ResourceBundle rb) {
        this.dc = new dbConnection();
    }

    @FXML
    private void loadStudentData(ActionEvent event)throws SQLException {
        try {

            Connection conn = dbConnection.getConnection();
            this.data = FXCollections.observableArrayList();

            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()){
                this.data.add(new StudentData(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4), rs.getString(5)));
            }

        }catch(SQLException e){
            System.err.println("Error " + e);
        }

        this.idcolumn.setCellValueFactory(new PropertyValueFactory<StudentData, String>("ID"));
        this.firstnamecolumn.setCellValueFactory(new PropertyValueFactory<StudentData, String>("firstName"));
        this.lastnamecolumn.setCellValueFactory(new PropertyValueFactory<StudentData, String>("lastName"));
        this.emailcolumn.setCellValueFactory(new PropertyValueFactory<StudentData, String>("email"));
        this.dobcolumn.setCellValueFactory(new PropertyValueFactory<StudentData, String>("DOB"));

        this.studenttable.setItems(null);
        this.studenttable.setItems(this.data);



    }

    @FXML
    private void addStudent(ActionEvent event) {


        Connection conn = null;
        PreparedStatement pStmt = null;
        if(!this.id.getText().equals("")) {
            try {
                conn = dbConnection.getConnection();
                String preQueryStatement = "SELECT * FROM students WHERE id = ?";
                pStmt = conn.prepareStatement(preQueryStatement);
                pStmt.setString(1, this.id.getText());
                ResultSet rs = pStmt.executeQuery();
                if (!rs.next()) {
                    String sqlInsert = "INSERT INTO students(id,fname,lname,email,dob) VALUES (?,?,?,?,?)";
                    PreparedStatement ps = conn.prepareStatement(sqlInsert);

                    ps.setString(1, this.id.getText());
                    ps.setString(2, this.firstname.getText());
                    ps.setString(3, this.lastname.getText());
                    ps.setString(4, this.email.getText());
                    ps.setString(5, this.dob.getEditor().getText());
                    ps.execute();

                }
            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                if (pStmt != null) {
                    try {
                        pStmt.close();
                    } catch (SQLException e) {
                    }
                }
            }

        }

    }






      //  if(!this.id.getText().equals("")){




          /*  try {
                Connection conn = dbConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sqlInsert);


                ResultSet rs = stmt.executeQuery();
                if(!rs.next()){

                }

                stmt.setString(1, this.id.getText());
                stmt.setString(2, this.firstname.getText());
                stmt.setString(3, this.lastname.getText());
                stmt.setString(4, this.email.getText());
                stmt.setString(5, this.dob.getEditor().getText());


                stmt.execute();
                conn.close();

            } catch (SQLException e) {
                e.printStackTrace();

            }
        } */






    @FXML
    private void clearFields(ActionEvent event) {
        this.id.setText("");
        this.firstname.setText("");
        this.lastname.setText("");
        this.email.setText("");
        this.dob.setValue(null);
    }

    @FXML
    private void searchStudentData(ActionEvent event) {
        String sqlSearch = "SELECT * FROM students WHERE id = ?";


        try {

            Connection conn = dbConnection.getConnection();
            this.data = FXCollections.observableArrayList();
            PreparedStatement ps = conn.prepareStatement(sqlSearch);
            ps.setString(1,this.id.getText());

            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                this.data.add(new StudentData(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4), rs.getString(5)));
            }

            rs.close();
            ps.close();

        }catch(SQLException e){
            System.err.println("Error " + e);
        }

        this.idcolumn.setCellValueFactory(new PropertyValueFactory<StudentData, String>("ID"));
        this.firstnamecolumn.setCellValueFactory(new PropertyValueFactory<StudentData, String>("firstName"));
        this.lastnamecolumn.setCellValueFactory(new PropertyValueFactory<StudentData, String>("lastName"));
        this.emailcolumn.setCellValueFactory(new PropertyValueFactory<StudentData, String>("email"));
        this.dobcolumn.setCellValueFactory(new PropertyValueFactory<StudentData, String>("DOB"));

        this.studenttable.setItems(null);
        this.studenttable.setItems(this.data);


    }

    @FXML
    private void removeStudent (ActionEvent event) {
        String sqlDelete = "DELETE FROM students WHERE id = ?";

        try {
            Connection conn = dbConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sqlDelete);

            stmt.setString(1,this.id.getText());
            stmt.executeUpdate();


        }catch(SQLException e){
            System.err.println("Error " + e);
        }


    }

}
