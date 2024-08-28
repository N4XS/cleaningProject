package userInterface;

import controller.ApplicationController;
import exceptions.list.ListReplacementsException;
import exceptions.insert.InsertReplacementException;
import models.Replacement;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class ReplacementPanel extends EntityModelPanel {

    private JButton addButton;
    private JButton modifyButton;
    private JButton deleteButton;
    private JTextField searchTextField;
    private JButton searchByStaffReplacingButton;
    private JButton searchByIDButton;

    public ReplacementPanel(ApplicationController controller) {
        super(controller, new String[]{"Remplacement", "Date de Début", "Date de Fin", "Remplaçant", "Absence Remplacée"});
        initialize();
    }

    private void initialize() {
        searchTextField = new JTextField(20);
        searchByStaffReplacingButton = new JButton("Rechercher par Staff Replacing");
        searchByStaffReplacingButton.addActionListener(e -> {
            String staffReplacingID = searchTextField.getText().trim();
            if (!staffReplacingID.isEmpty()) {
                try {
                    List<Replacement> replacements = controller.getReplacementByStaffReplacing(staffReplacingID);
                    updateTableData(replacements);
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
                    Replacement replacement = controller.getReplacementById(id);
                    if (replacement != null) {
                        updateTableData(List.of(replacement));
                    } else {
                        JOptionPane.showMessageDialog(this, "Aucun remplacement trouvé pour l'ID : " + id, "Aucun résultat", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la recherche : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        addButton = new JButton("Ajouter un remplacement");
        addButton.addActionListener(e -> addEntity());
        modifyButton = new JButton("Modifier le replacement séléctionné");
        modifyButton.addActionListener(e -> {
            int selectedRow = entityTable.getSelectedRow();
            if (selectedRow != -1) {
                modifyEntity(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à modifier.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
        deleteButton = new JButton("Supprimer le remplacement sélectionné");
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
        searchPanel.add(searchByStaffReplacingButton);
        searchPanel.add(searchByIDButton);

        add(searchPanel, BorderLayout.NORTH);

        add(new JScrollPane(entityTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateTableData(List<Replacement> replacements) {
        tableModel.setRowCount(0);
        for (Replacement replacement : replacements) {
            tableModel.addRow(new Object[]{
                    replacement.getReplacementID(),
                    replacement.getStartDate(),
                    replacement.getEndDate(),
                    replacement.getStaffReplacingID(),
                    replacement.getAbsenceReplacedID()
            });
        }
    }

    @Override
    protected void loadEntities() throws ListReplacementsException {
        tableModel.setRowCount(0);
        List<Replacement> replacements = controller.listReplacements();
        for (Replacement replacement : replacements) {
            tableModel.addRow(new Object[]{
                    replacement.getReplacementID(),
                    replacement.getStartDate(),
                    replacement.getEndDate(),
                    replacement.getStaffReplacingID(),
                    replacement.getAbsenceReplacedID()
            });
        }
    }

    @Override
    protected void addEntity() {
        JSpinner startDateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(startDateSpinner, "dd/MM/yyyy");
        startDateSpinner.setEditor(dateEditor);
        JSpinner endDateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor endEditor = new JSpinner.DateEditor(endDateSpinner, "dd/MM/yyyy");
        endDateSpinner.setEditor(endEditor);
        JTextField staffReplacingIDField = new JTextField();
        JTextField absenceReplacedIDField = new JTextField();

        Object[] fields = {
                "Date de début:", startDateSpinner,
                "Date de fin:", endDateSpinner,
                "ID du remplaçant:", staffReplacingIDField,
                "ID de l'absence remplacée:", absenceReplacedIDField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Ajouter un remplacement", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                LocalDate startDate = ((SpinnerDateModel) startDateSpinner.getModel()).getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate endDate = ((SpinnerDateModel) endDateSpinner.getModel()).getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                String staffReplacingID = staffReplacingIDField.getText();
                String absenceReplacedID = absenceReplacedIDField.getText();

                Replacement newReplacement = new Replacement(startDate, endDate, staffReplacingID, absenceReplacedID);
                controller.addReplacement(newReplacement);
                loadEntities();
                JOptionPane.showMessageDialog(this, "Remplacement ajouté avec succès.");
            } catch (InsertReplacementException ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout du remplacement : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    protected void modifyEntity(int selectedRow) {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un remplacement à modifier.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String replacementID = (String) tableModel.getValueAt(selectedRow, 0);
        LocalDate startDate = (LocalDate) tableModel.getValueAt(selectedRow, 1);
        LocalDate endDate = (LocalDate) tableModel.getValueAt(selectedRow, 2);
        String staffReplacingID = (String) tableModel.getValueAt(selectedRow, 3);
        String absenceReplacedID = (String) tableModel.getValueAt(selectedRow, 4);

        JSpinner startDateSpinner = new JSpinner(new SpinnerDateModel());
        startDateSpinner.setEditor(new JSpinner.DateEditor(startDateSpinner, "dd/MM/yyyy"));
        startDateSpinner.setValue(java.sql.Date.valueOf(startDate));
        JSpinner endDateSpinner = new JSpinner(new SpinnerDateModel());
        endDateSpinner.setEditor(new JSpinner.DateEditor(endDateSpinner, "dd/MM/yyyy"));
        endDateSpinner.setValue(java.sql.Date.valueOf(endDate));
        JTextField staffReplacingIDField = new JTextField(staffReplacingID);
        JTextField absenceReplacedIDField = new JTextField(absenceReplacedID);

        Object[] fields = {
                "Date de début:", startDateSpinner,
                "Date de fin:", endDateSpinner,
                "ID de l'employé remplaçant:", staffReplacingIDField,
                "ID de l'absence remplacée:", absenceReplacedIDField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Modifier le remplacement", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                startDate = ((java.util.Date) startDateSpinner.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                endDate = ((java.util.Date) endDateSpinner.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                staffReplacingID = staffReplacingIDField.getText();
                absenceReplacedID = absenceReplacedIDField.getText();

                Replacement updatedReplacement = new Replacement(startDate, endDate, staffReplacingID, absenceReplacedID);
                updatedReplacement.setReplacementID(replacementID);

                controller.updateReplacement(updatedReplacement);
                loadEntities();
                JOptionPane.showMessageDialog(this, "Remplacement modifié avec succès.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la modification du remplacement : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    protected void deleteEntity(int selectedRow) {
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un remplacement à supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String replacementID = (String) tableModel.getValueAt(selectedRow, 0);

        int confirmation = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer ce remplacement ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                controller.deleteReplacement(replacementID);
                loadEntities();
                JOptionPane.showMessageDialog(this, "Remplacement supprimé avec succès.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression du remplacement : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


}
