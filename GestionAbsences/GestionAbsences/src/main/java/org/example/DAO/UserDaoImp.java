package org.example.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.Model.Promo;
import org.example.Model.Salle;
import org.example.Model.Specialite;
import org.example.Model.User;
import org.example.MysqlConnect.ConnectionClass;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImp implements UserDao{
    Connection conn;
    User users = new User();
    Salle salle = new Salle();
    Promo promos = new Promo();
    Specialite specialite = new Specialite();

    public void creatUser(String nom, String prenom, String password, String Email, String Tele, LocalDate date, Object role, String cin, String Specialite, String Sale, String promo) throws SQLException
    {
        //hash password
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt(12));

        try {
            conn = ConnectionClass.getMyConnexion();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        String query = "INSERT INTO user (nom, prenom, password, mail, tele, dateDeNaissance, role) VALUES (?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, nom);
        preparedStatement.setString(2, prenom);
        preparedStatement.setString(3, hashed);
        preparedStatement.setString(4, Email);
        preparedStatement.setString(5, Tele);
        preparedStatement.setString(6, String.valueOf(date));
        preparedStatement.setString(7, String.valueOf(role));
        preparedStatement.executeUpdate();
        ResultSet rs = preparedStatement.getGeneratedKeys();
        JOptionPane.showMessageDialog(null , "Ajouté avec succès");
        System.out.println("User cree");

        specialite.setNom_sp(Specialite);
        for (int b = 0; b < getAllSpecialites().size(); b++) {
            if (getAllSpecialites().get(b).getNom_sp().equals(Specialite)) {
                specialite.setId(getAllSpecialites().get(b).getId());
                specialite.setNom_sp(getAllSpecialites().get(b).getNom_sp());
            }
        }

        promos.setNomPromo(promo);
        for (int b = 0; b < getAllPromos().size(); b++) {
            if (getAllPromos().get(b).getNomPromo().equals(promo)) {
                promos.setId_promo(getAllPromos().get(b).getId_promo());
                promos.setNomPromo(getAllPromos().get(b).getNomPromo());
            }
        }

        salle.setNomDeSalle(Sale);
        for (int b = 0; b < getAllSalles().size(); b++) {
            if (getAllSalles().get(b).getNomDeSalle().equals(Sale)) {
                salle.setId_salle(getAllSalles().get(b).getId_salle());
                salle.setNomDeSalle(getAllSalles().get(b).getNomDeSalle());
            }
        }
        if (role == "Apprenant" && rs.next()) {
            users.setId_user(rs.getInt(1));
            String qr = "INSERT INTO apprenant (cin,id_sp,id_salle,id_user,id_prom) VALUES (?,?,?,?,?)";
            PreparedStatement preparedStatement1 = conn.prepareStatement(qr);
            preparedStatement1.setString(1, cin);
            preparedStatement1.setInt(2, specialite.getId());
            preparedStatement1.setInt(3, salle.getId_salle());
            preparedStatement1.setInt(4, users.getId_user());
            preparedStatement1.setInt(5, promos.getId_promo());
            preparedStatement1.executeUpdate();
            System.out.println("Inser appronant");
        }

        if (role == "Formateur" && rs.next()) {
            users.setId_user(rs.getInt(1));
            String qr = "INSERT INTO formateur (id_sp,id_salle,id_user) VALUES (?,?,?)";
            PreparedStatement preparedStatement1 = conn.prepareStatement(qr);
            preparedStatement1.setInt(1, specialite.getId());
            preparedStatement1.setInt(2, salle.getId_salle());
            preparedStatement1.setInt(3, users.getId_user());
            preparedStatement1.executeUpdate();
            System.out.println("Inser Formateur");
        }

    }

    @Override
    public void updateUser(int id_user, String nom, String prenom, String password, String email,String tele, LocalDate dateNaissance, Object role, String cin, String Specialite, String Sale, String promo) throws SQLException {
        try {
            conn = ConnectionClass.getMyConnexion();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt(12));
        String query = "UPDATE user SET nom =?, prenom =?, password=?, mail=?, tele=?, dateDeNaissance=?, role=? WHERE id_user=?";
        PreparedStatement preparedStatement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1,nom);
        preparedStatement.setString(2,prenom);
        preparedStatement.setString(3, hashed);
        preparedStatement.setString(4, email);
        preparedStatement.setString(5,tele);
        preparedStatement.setString(6, String.valueOf(dateNaissance));
        preparedStatement.setString(7, String.valueOf(role));
        preparedStatement.setInt(8, id_user);

        preparedStatement.executeUpdate();
        JOptionPane.showMessageDialog(null , "Update avec succès");
        System.out.println("Users Update");

        specialite.setNom_sp(Specialite);
        for (int b = 0; b < getAllSpecialites().size(); b++) {
            if (getAllSpecialites().get(b).getNom_sp().equals(Specialite)) {
                specialite.setId(getAllSpecialites().get(b).getId());
            }
        }

        promos.setNomPromo(promo);
        for (int b = 0; b < getAllPromos().size(); b++) {
            if (getAllPromos().get(b).getNomPromo().equals(promo)) {
                promos.setId_promo(getAllPromos().get(b).getId_promo());
            }
        }

        salle.setNomDeSalle(Sale);
        for (int b = 0; b < getAllSalles().size(); b++) {
            if (getAllSalles().get(b).getNomDeSalle().equals(Sale)) {
                salle.setId_salle(getAllSalles().get(b).getId_salle());
            }
        }

        if(role.equals("Apprenant")){
            String qr = "UPDATE apprenant SET cin=?, id_sp=?, id_salle=?, id_prom=? WHERE id_user=?";
            PreparedStatement preparedStatement1 = conn.prepareStatement(qr);
            preparedStatement1.setString(1, cin);
            preparedStatement1.setInt(2, specialite.getId());
            preparedStatement1.setInt(3, salle.getId_salle());
            preparedStatement1.setInt(4, promos.getId_promo());
            preparedStatement1.setInt(5, id_user);
            preparedStatement1.executeUpdate();
            System.out.println("update appronant");
        }

        if(role.equals("Formateur")){
            String qr = "UPDATE formateur SET id_sp=?, id_salle=? WHERE id_user=?";
            PreparedStatement preparedStatement1 = conn.prepareStatement(qr);
            preparedStatement1.setInt(1, specialite.getId());
            preparedStatement1.setInt(2, salle.getId_salle());
            preparedStatement1.setInt(3, id_user);
            preparedStatement1.executeUpdate();
            System.out.println("update formateur");
        }

    }

    @Override
    public void deleteUser(int id, Object role) throws SQLException {

        try {
            conn = ConnectionClass.getMyConnexion();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if(role== "Apprenant"){
            String qr = "DELETE FROM apprenant WHERE id_user=?";
            PreparedStatement preparedStatement1 = conn.prepareStatement(qr);
            preparedStatement1.setInt(1,id);
            preparedStatement1.executeUpdate();
            System.out.println("delete appronant");
        }else if (role== "Formateur"){
            String qr = "DELETE FROM formateur WHERE id_user=?";
            PreparedStatement preparedStatement1 = conn.prepareStatement(qr);
            preparedStatement1.setInt(1,id);
            preparedStatement1.executeUpdate();
            System.out.println("delete formateur");
        }

        String query = "DELETE FROM user WHERE id_user=?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setInt(1,id);

        preparedStatement.executeUpdate();
        JOptionPane.showMessageDialog(null , "Delete avec succès");
        System.out.println("Users Delete");

    }

    @Override
    public ObservableList<User> getUsersList()  {
        ObservableList<User> usersList = FXCollections.observableArrayList();
        try {
            conn = ConnectionClass.getMyConnexion();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        String query = "SELECT * FROM user";

        Statement st;
        ResultSet rs;
        try{
            st = conn.createStatement();
            rs = st.executeQuery(query);
            User users;
            while (rs.next()){
                users = new User(rs.getInt("id_user"),rs.getString("nom"),rs.getString("prenom"),rs.getString("password"), rs.getString("mail"), rs.getString("tele"), rs.getString("dateDeNaissance"), rs.getString("role"));
                usersList.add(users);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return usersList;
    }

    @Override
    public List<Salle> getAllSalles() throws SQLException {
        try {
            conn = ConnectionClass.getMyConnexion();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        List<Salle> listSalle = new ArrayList<>();
        String query = "SELECT * FROM salle";
        PreparedStatement preparedStatement = conn.prepareStatement(query);


        ResultSet rs = preparedStatement.executeQuery();
        Salle salle;
        while(rs.next()){
            salle = new Salle(rs.getInt("id_salle"),rs.getString("nomDeSalle"));
            listSalle.add(salle);
        }

        return listSalle;
    }

    @Override
    public List<Promo> getAllPromos() throws SQLException {
        try {
            conn = ConnectionClass.getMyConnexion();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        List<Promo> listPromo = new ArrayList<>();
        String query = "SELECT * FROM promo";
        PreparedStatement preparedStatement = conn.prepareStatement(query);


        ResultSet rs = preparedStatement.executeQuery();
        Promo promo;
        while(rs.next()){
            promo = new Promo(rs.getInt("id_promo"),rs.getString("nomPromo"), rs.getString("anneeDePromo"));
            listPromo.add(promo);
        }

        return listPromo;
    }

    @Override
    public List<Specialite> getAllSpecialites() throws SQLException {
        try {
            conn = ConnectionClass.getMyConnexion();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        List<Specialite> listSpecialite = new ArrayList<>();
        String query = "SELECT * FROM specialite";
        PreparedStatement preparedStatement = conn.prepareStatement(query);


        ResultSet rs = preparedStatement.executeQuery();
        Specialite specialite;
        while(rs.next()){
            specialite = new Specialite(rs.getInt("id_specialite"),rs.getString("nom_sp"), rs.getInt("numbreDeModule"));
            listSpecialite.add(specialite);
        }
        return listSpecialite;
    }



}
