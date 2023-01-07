package com.example.hafta11deneme;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;
import java.sql.*;

public class HelloController implements Initializable {

    @FXML
    private TableColumn<Musteri, String> sutunAd;

    @FXML
    private TableColumn<Musteri, String> sutunMail;

    @FXML
    private TableColumn<Musteri, String> sutunSoyad;

    @FXML
    private TableView<Musteri> tblMusteriler;

    @FXML
    private TextField txtAd;

    @FXML
    private TextField txtMail;

    @FXML
    private TextField txtSoyad;

    private ObservableList<Musteri>musteriler= FXCollections.observableArrayList();

    private Connection baglanti;
    private PreparedStatement sorgu;
    private Alert hata=new Alert(Alert.AlertType.ERROR);
    private ResultSet rs=null;

    @FXML
    void btnKaydet(ActionEvent event) {

        String mail=txtMail.getText();

        for (int i = 0; i < musteriler.size() ; i++) {
            if (mail.equals(musteriler.get(i).getMail())){
                hata.setTitle("Hata!");
                hata.setHeaderText("Bu e-mail adresi daha önce kullanılmış...");
                hata.show();
                return;
            }
        }

        String ad=txtAd.getText();
        String soyad=txtSoyad.getText();

        Musteri musteri=new Musteri(ad,soyad,mail);
        musteriler.add(musteri);

        try {
            sorgu=baglanti.prepareStatement("insert into Tablo(Adı,Soyadı,Maili) values(?,?,?)");
            sorgu.setString(1,ad); //soru işaretleri yerine ad,soyad,mail yazılabilirdi ancak
            sorgu.setString(2,soyad);//daha kısa olması için bu şekilde yazıldı.
            sorgu.setString(3,mail);
            sorgu.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @FXML
    void btnKayitGüncelle(ActionEvent event) {


        int index=tblMusteriler.getSelectionModel().getSelectedIndex();

        if (index != -1) {

            String mail=txtMail.getText();

            for (int i = 0; i <musteriler.size(); i++) {

                if (mail.equals(musteriler.get(i).getMail()) && !mail.equals(musteriler.get(index).getMail()) ) {
                    hata.setTitle("Hata!");
                    hata.setHeaderText("Bu e-mail adresi daha önce kullanılmış!");
                    hata.show();
                    return;
                }
            }
            String ad=txtAd.getText();
            String soyad=txtSoyad.getText();

            Musteri musteri=new Musteri(ad,soyad,mail);
            musteriler.set(index,musteri);

            try {
                sorgu=baglanti.prepareStatement("update Tablo set Adı=?,Soyadı=?,Maili=? where Maili="+"'"+mail+"'");
                sorgu.setString(1,ad);
                sorgu.setString(2,soyad);
                sorgu.setString(3,mail);
                sorgu.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        else{
            hata.setTitle("Hata!");
            hata.setHeaderText("Güncellenecek müşteri kaydı yok veya seçilmedi...");
            hata.show();

        }

    }

    @FXML
    void btnTemizle(){
        txtAd.clear();
        txtSoyad.clear();
        txtMail.clear();
    }

    @FXML
    void btnKayıtSil(ActionEvent event) {
        int index=tblMusteriler.getSelectionModel().getSelectedIndex();
        if (index >=0) {
            try {
                sorgu=baglanti.prepareStatement("delete from Tablo where Maili="+"'"+musteriler.get(index).getMail()+"'");
                musteriler.remove(index);
                sorgu.executeUpdate();


            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else{
            hata.setTitle("Hata!");
            hata.setHeaderText("Silme işlemi yapmak için önce seçim yapmalısınız");
            hata.show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        sutunAd.setCellValueFactory(new PropertyValueFactory<>("ad"));
        sutunSoyad.setCellValueFactory(new PropertyValueFactory<>("soyad"));
        sutunMail.setCellValueFactory(new PropertyValueFactory<>("mail"));

        tblMusteriler.setItems(musteriler);

        //Driver tanımlama

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            baglanti=DriverManager.getConnection("jdbc:sqlserver://OFT-PC;database=MusteriBilgileri2;integratedSecurity=true");
            System.out.println(baglanti.isValid(0));//Databaseyle bağlantı olup olmadığını gösterir
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        try {
            sorgu=baglanti.prepareStatement("select * from Tablo");
            rs=sorgu.executeQuery();

            String ad,soyad,mail;

            while (rs.next()){
                ad=rs.getString(1);
                soyad=rs.getString(2);
                mail=rs.getString(3);

                Musteri musteri=new Musteri(ad,soyad,mail);
                musteriler.add(musteri);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        tblMusteriler.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {

                int index=t1.intValue();

                if (index != -1) {

                txtAd.setText(musteriler.get(index).getAd());
                txtSoyad.setText(musteriler.get(index).getSoyad());
                txtMail.setText(musteriler.get(index).getMail());
                }
                else{

                }
            }
        });

    }
}
