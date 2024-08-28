package userInterface;

import controller.ApplicationController;
import exceptions.list.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

public abstract class EntityModelPanel extends JPanel {
    protected ApplicationController controller;
    protected JTable entityTable;
    protected DefaultTableModel tableModel;

    public EntityModelPanel(ApplicationController controller, String[] columnNames) {
        this.controller = controller;
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(columnNames, 0);
        entityTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(entityTable);
        add(scrollPane, BorderLayout.CENTER);

        try {
            loadEntities();
        } catch (ListException | ListAbsencesException | ListCitiesException | ListCleaningServicesException | ListStaffMembersException | ListTeamsException | ListMachineriesException | ListMaterialsOrdersException | ListSitesException | ListWarningsException | ListClientsException | ListProductsException | ListContractsException | ListReplacementsException | SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des entités: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    protected abstract void loadEntities() throws ListException, ListAbsencesException, ListCitiesException, ListCleaningServicesException, ListClientsException, ListContractsException, ListMachineriesException, ListMaterialsOrdersException, ListProductsException, ListReplacementsException, ListSitesException, ListStaffMembersException, ListTeamsException, ListWarningsException, SQLException;

    protected abstract void addEntity();

    protected abstract void deleteEntity(int selectedRow);

    protected abstract void modifyEntity(int selectedRow);
}
