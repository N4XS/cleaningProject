package userInterface;

import controller.ApplicationController;
import exceptions.list.ListWarningsException;
import exceptions.insert.InsertWarningException;
import models.Warning;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class WarningPanel extends EntityModelPanel {

    private JButton addButton;
    private JButton modifyButton;
    private JButton deleteButton;
    private JTextField searchTextField;
    private JButton searchByONSSButton;
    private JButton searchByIDButton;

    public WarningPanel(ApplicationController controller) {
        super(controller, new String[]{"ID Avertissement", "Description", "Date", "Faute grave", "ID Employé averti"});
        initialize();
    }

    private void initialize() {
        searchTextField = new JTextField(20);
        searchByONSSButton = new JButton("Rechercher par ONSS");
        searchByIDButton = new JButton("Rechercher par ID");
        searchByONSSButton.addActionListener(e -> searchWarningsByONSS());
        searchByIDButton.addActionListener(e -> searchWarningsByID());

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.LINE_AXIS));
        searchPanel.add(searchTextField);
        searchPanel.add(Box.createHorizontalStrut(10));
        searchPanel.add(searchByONSSButton);
        searchPanel.add(searchByIDButton);

        addButton = new JButton("Ajouter un avertissement");
        addButton.addActionListener(e -> addEntity());
        modifyButton = new JButton("Modifier l'avertissement sélectionné");
        modifyButton.addActionListener(e -> modifySelectedEntity());
        deleteButton = new JButton("Supprimer l'avertissement sélectionné");
        deleteButton.addActionListener(e -> deleteSelectedEntity());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(addButton);
        buttonPanel.add(modifyButton);
        buttonPanel.add(deleteButton);

        add(searchPanel, BorderLayout.NORTH);
        add(new JScrollPane(entityTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void searchWarningsByONSS() {
        String onss = searchTextField.getText().trim();
        if (!onss.isEmpty()) {
            try {
                List<Warning> warnings = controller.getWarningByONSS(onss);
                if (warnings.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Aucun avertissement trouvé pour ce numéro ONSS.", "Aucun résultat", JOptionPane.INFORMATION_MESSAGE);
                }
                updateTableData(warnings);
            } catch (Exception ex) {
                showErrorDialog("Erreur lors de la recherche par ONSS : " + ex.getMessage());
            }
        }
    }

    private void searchWarningsByID() {
        String id = searchTextField.getText().trim();
        if (!id.isEmpty()) {
            try {
                Warning warning = controller.getWarningByID(id);
                if (warning != null) {
                    updateTableData(List.of(warning));
                } else {
                    JOptionPane.showMessageDialog(this, "Aucun avertissement trouvé pour l'ID : " + id, "Aucun résultat", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                showErrorDialog("Erreur lors de la recherche par ID : " + ex.getMessage());
            }
        }
    }

    private void updateTableData(List<Warning> warnings) {
        tableModel.setRowCount(0);
        for (Warning warning : warnings) {
            tableModel.addRow(new Object[]{
                    warning.getWarningID(),
                    warning.getDescription(),
                    warning.getDateFault(),
                    warning.getSeriousFault() ? "Oui" : "Non",
                    warning.getStaffMemberWarnedID()
            });
        }
    }

    @Override
    protected void loadEntities() throws ListWarningsException {
        List<Warning> warnings = controller.listWarnings();
        updateTableData(warnings);
    }

    @Override
    protected void addEntity() {
        JTextField descriptionField = new JTextField();
        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
        dateSpinner.setEditor(dateEditor);
        JCheckBox seriousFaultCheckBox = new JCheckBox();
        JTextField staffMemberWarnedIDField = new JTextField();

        Object[] fields = {
                "Description:", descriptionField,
                "Date:", dateSpinner,
                "Faute grave:", seriousFaultCheckBox,
                "ID Employé averti:", staffMemberWarnedIDField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Ajouter un avertissement", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String description = descriptionField.getText();
                LocalDate date = ((SpinnerDateModel) dateSpinner.getModel()).getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                Boolean isSeriousFault = seriousFaultCheckBox.isSelected();
                String staffMemberWarnedID = staffMemberWarnedIDField.getText();

                Warning newWarning = new Warning(description, date, isSeriousFault, staffMemberWarnedID);
                controller.addWarning(newWarning);
                loadEntities();
                JOptionPane.showMessageDialog(this, "Avertissement ajouté avec succès.");
            } catch (InsertWarningException ex) {
                showErrorDialog("Erreur lors de l'ajout de l'avertissement : " + ex.getMessage());
            } catch (Exception ex) {
                showErrorDialog("Erreur : " + ex.getMessage());
            }
        }
    }

    @Override
    protected void modifyEntity(int selectedRow) {
        if (selectedRow < 0) {
            showErrorDialog("Veuillez sélectionner un avertissement à modifier.");
            return;
        }

        String warningID = (String) tableModel.getValueAt(selectedRow, 0);
        String description = (String) tableModel.getValueAt(selectedRow, 1);
        LocalDate dateFault = (LocalDate) tableModel.getValueAt(selectedRow, 2);
        boolean isSeriousFault = "Oui".equals(tableModel.getValueAt(selectedRow, 3));
        String staffMemberWarnedID = (String) tableModel.getValueAt(selectedRow, 4);

        JTextField descriptionField = new JTextField(description);
        JSpinner dateFaultSpinner = new JSpinner(new SpinnerDateModel());
        dateFaultSpinner.setEditor(new JSpinner.DateEditor(dateFaultSpinner, "dd/MM/yyyy"));
        dateFaultSpinner.setValue(java.sql.Date.valueOf(dateFault));
        JCheckBox isSeriousFaultCheckBox = new JCheckBox("Faute grave", isSeriousFault);
        JTextField staffMemberWarnedIDField = new JTextField(staffMemberWarnedID);

        Object[] fields = {
                "Description:", descriptionField,
                "Date de la faute:", dateFaultSpinner,
                "Faute grave:", isSeriousFaultCheckBox,
                "ID de l'employé averti:", staffMemberWarnedIDField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Modifier l'avertissement", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                description = descriptionField.getText();
                dateFault = ((java.util.Date) dateFaultSpinner.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                isSeriousFault = isSeriousFaultCheckBox.isSelected();
                staffMemberWarnedID = staffMemberWarnedIDField.getText();

                Warning updatedWarning = new Warning(description, dateFault, isSeriousFault, staffMemberWarnedID);
                updatedWarning.setWarningID(warningID);

                controller.updateWarning(updatedWarning);
                loadEntities();
                JOptionPane.showMessageDialog(this, "Avertissement modifié avec succès.");
            } catch (Exception ex) {
                showErrorDialog("Erreur lors de la modification de l'avertissement : " + ex.getMessage());
            }
        }
    }

    @Override
    protected void deleteEntity(int selectedRow) {
        if (selectedRow < 0) {
            showErrorDialog("Veuillez sélectionner un avertissement à supprimer.");
            return;
        }

        String warningID = (String) tableModel.getValueAt(selectedRow, 0);

        int confirmation = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer cet avertissement ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                controller.deleteWarning(warningID);
                loadEntities();
                JOptionPane.showMessageDialog(this, "Avertissement supprimé avec succès.");
            } catch (Exception ex) {
                showErrorDialog("Erreur lors de la suppression de l'avertissement : " + ex.getMessage());
            }
        }
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    private void modifySelectedEntity() {
        int selectedRow = entityTable.getSelectedRow();
        if (selectedRow != -1) {
            modifyEntity(selectedRow);
        } else {
            showErrorDialog("Veuillez sélectionner une ligne à modifier.");
        }
    }

    private void deleteSelectedEntity() {
        int selectedRow = entityTable.getSelectedRow();
        if (selectedRow != -1) {
            deleteEntity(selectedRow);
        } else {
            showErrorDialog("Veuillez sélectionner une ligne à supprimer.");
        }
    }
}
