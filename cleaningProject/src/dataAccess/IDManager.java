package dataAccess;

import exceptions.connexion.SingletonConnexionException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IDManager {

    public static String generateUniqueID(String entityType, Connection connection) throws SingletonConnexionException {
        int nextId = getNextIdFromDatabase(entityType, connection);
        return formatId(entityType, nextId);
    }

    private static String formatId(String entityType, int idNumber) {
        String prefix;
        if (entityType.trim().equals("MaterialsOrder")){
            prefix = "MO";
        } else {
            prefix = entityType.substring(0, 2).toUpperCase();
        }
        return String.format("%s%06d", prefix, idNumber);
    }

    private static int getNextIdFromDatabase(String entityType, Connection connection) throws SingletonConnexionException {
        int nextId = -1;
        String selectSQL = "SELECT next_id FROM EntityID WHERE entity_name = ?";
        String updateSQL = "UPDATE EntityID SET next_id = next_id + 1 WHERE entity_name = ?";

        try (PreparedStatement selectStmt = connection.prepareStatement(selectSQL);
             PreparedStatement updateStmt = connection.prepareStatement(updateSQL)) {

            System.out.println("IDManager - Récupération de l'ID pour : " + entityType);
            selectStmt.setString(1, entityType);
            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                nextId = rs.getInt("next_id");
                System.out.println("IDManager - ID récupéré : " + nextId);
            } else {
                throw new SQLException("Aucune entrée trouvée pour l'entité: " + entityType);
            }

            updateStmt.setString(1, entityType);
            updateStmt.executeUpdate();
            System.out.println("IDManager - ID mis à jour pour : " + entityType);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SingletonConnexionException("Erreur lors de la génération de l'ID pour l'entité: " + entityType, e);
        }

        return nextId;
    }
}
