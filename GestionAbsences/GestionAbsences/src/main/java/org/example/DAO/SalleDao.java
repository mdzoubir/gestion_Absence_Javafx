package org.example.DAO;

import org.example.Model.Salle;

import java.sql.SQLException;
import java.util.List;

public interface SalleDao {

    List<Salle> getAll() throws ClassNotFoundException, SQLException;

    public Salle getById(int id_salle) throws ClassNotFoundException, SQLException;

    public void SaveSalle(String nomDeSalle) throws ClassNotFoundException, SQLException;

    public void updateSalle(int id, String nomDeSalle)throws ClassNotFoundException, SQLException;

    public void deleteById(int id_salle) throws ClassNotFoundException, SQLException;

}
