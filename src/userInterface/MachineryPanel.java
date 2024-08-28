package userInterface;

import controller.ApplicationController;
import exceptions.list.ListMachineriesException;
import exceptions.insert.InsertMachineryException;
import models.Machinery;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class MachineryPanel extends EntityModelPanel {

    private JButton addButton;
    private JButton modifyButton;
    private JButton deleteButton;
    private JTextField searchTextField;
    private JButton searchByNameButton;
    private JButton searchByIDButton;

    public MachineryPanel(ApplicationController controller) {
        super(controller, new String[]{"Machine", "Nom", "Disponibilité", "Site"});
        initialize();
    }

    private void initialize() {
        searchTextField = new JTextField(20);
        searchByNameButton = new JButton("Rechercher par nom");

        searchByNameButton.addActionListener(e -> {
            String name = searchTextField.getText().trim();
            if (!name.isEmpty()) {
                try {
                    Machinery machinery = controller.getMachineryByName(name);
                    updateTableData(List.of(machinery));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la recherche : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        searchByIDButton = new JButton("Rechercher par ID");
        searchByIDButton.addActionListener(e -> {
            String id = searchTextField.getText().trim();
            if (!id.isEmpty()) {
                try {
                    Machinery machinery = controller.getMachineryById(id);
                    if (machinery != null) {
                        updateTableData(List.of(machinery));
                    } else {
                        JOptionPane.showMessageDialog(this, "Aucune machine trouvée pour l'ID : " + id, "Aucun résultat", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la recherche : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        addButton = new JButton("Ajouter une machine");
        addButton.addActionListener(e -> addEntity());
        modifyButton = new JButton("Modifier la machine séléctionnée");
        modifyButton.addActionListener(e -> {
            int selectedRow = entityTable.getSelectedRow();
            if (selectedRow != -1) {
                modifyEntity(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à modifier.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
        deleteButton = new JButton("Supprimer la machine sélectionnée");
        deleteButton.addActionListener(e -> {
            int selectedRow = entityTable.getSelectedRow();
            if (selectedRow != -1) {
                deleteEntity(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(addButton);
        buttonPanel.add(modifyButton);
        buttonPanel.add(deleteButton);

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.LINE_AXIS));
        searchPanel.add(searchTextField);
        searchPanel.add(Box.createHorizontalStrut(10));
        searchPanel.add(searchByNameButton);
        searchPanel.add(searchByIDButton);

        add(searchPanel, BorderLayout.NORTH);

        add(new JScrollPane(entityTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateTableData(List<Machinery> machineries) throws SQLException {
        tableModel.setRowCount(0);
        for (Machinery machinery : machineries) {
            tableModel.addRow(new Object[]{
                    machinery.getMachineryID(),
                    machinery.getName(),
                    machinery.getAvailable() ? "Oui" : "Non",
                    controller.getSiteByID(machinery.getSiteID()).getSiteName()
            });
        }
    }

    @Override
    protected void loadEntities() throws ListMachineriesException, SQLException {
        tableModel.setRowCount(0);
        List<Machinery> machineryList = controller.listMachineries();
        for (Machinery machinery : machineryList) {
            tableModel.addRow(new Object[]{
                    machinery.getMachineryID(),
                    machinery.getName(),
                    machinery.getAvailable() ? "Oui" : "Non",
                    controller.getSiteByID(machinery.getSiteID()).getSiteName()
            });
        }
    }


    @Override
    protected void addEntity() {
        JTextField nameField = new JTextField();
        JCheckBox isAvailableCheckBox = new JCheckBox();
        JTextField siteIDField = new JTextField();

        Object[] fields = {
                "Nom de la machine:", nameField,
                "Disponible:", isAvailableCheckBox,
                "ID du site:", siteIDField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Ajouter une machine", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText();
                Boolean isAvailable = isAvailableCheckBox.isSelected();
                String siteID = siteIDField.getText();

                Machinery newMachinery = new Machinery(name, isAvailable, siteID);
                controller.addMachinery(newMachinery);
                loadEntities();
                JOptionPane.showMessageDialog(this, "Machine ajoutée avec succès.");
            } catch (InsertMachineryException ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de la machine : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    protected void modifyEntity(int selectedRow) {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une machine à modifier.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String machineryID = (String) tableModel.getValueAt(selectedRow, 0);
        String name = (String) tableModel.getValueAt(selectedRow, 1);
        Boolean isAvailable = (Boolean) tableModel.getValueAt(selectedRow, 2);
        String siteID = (String) tableModel.getValueAt(selectedRow, 3);

        JTextField nameField = new JTextField(name);
        JCheckBox isAvailableCheckBox = new JCheckBox("Disponible", isAvailable);
        JTextField siteIDField = new JTextField(siteID);

        Object[] fields = {
                "Nom de la machine:", nameField,
                "Disponible:", isAvailableCheckBox,
                "ID du site:", siteIDField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Modifier la machine", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                name = nameField.getText();
                isAvailable = isAvailableCheckBox.isSelected();
                siteID = siteIDField.getText();

                Machinery updatedMachinery = new Machinery(name, isAvailable, siteID);
                updatedMachinery.setMachineryID(machineryID);

                controller.updateMachinery(updatedMachinery);
                loadEntities();
                JOptionPane.showMessageDialog(this, "Machine modifiée avec succès.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la modification de la machine : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    protected void deleteEntity(int selectedRow) {
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une machine à supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String machineryID = (String) tableModel.getValueAt(selectedRow, 0);

        int confirmation = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer cette machine ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                controller.deleteMachinery(machineryID);
                loadEntities();
                JOptionPane.showMessageDialog(this, "Machine supprimée avec succès.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression de la machine : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }



}
