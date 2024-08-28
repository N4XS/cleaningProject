package userInterface;

import controller.ApplicationController;
import exceptions.list.ListAbsencesException;
import exceptions.insert.IllegalArgumentException;
import models.Absence;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;

public class AbsencePanel extends EntityModelPanel {

    private JButton addButton;
    private JButton modifyButton;
    private JButton deleteButton;
    private JTextField searchTextField;
    private JButton searchByAbsentIDButton;
    private JButton searchByIDButton;

    public AbsencePanel(ApplicationController controller) {
        super(controller, new String[]{"Absence", "Justification", "Date de début", "Date de fin", "Certificat", "ONSS Absent"});
        initialize();
    }

    private void initialize() {
        searchTextField = new JTextField(20);
        searchByAbsentIDButton = new JButton("Rechercher par ID absent");
        searchByAbsentIDButton.addActionListener(e -> {
            String absentID = searchTextField.getText().trim();
            if (!absentID.isEmpty()) {
                try {
                    List<Absence> absences = controller.getAbsencesByAbsentID(absentID);
                    updateTableData(absences);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la recherche : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        searchByIDButton = new JButton("Rechercher par ID absence");
        searchByIDButton.addActionListener(e -> {
            String id = searchTextField.getText().trim();
            if (!id.isEmpty()) {
                try {
                    Absence absence = controller.getAbsenceByID(id);
                    if (absence != null) {
                        updateTableData(List.of(absence));
                    } else {
                        JOptionPane.showMessageDialog(this, "Aucune absence trouvée pour l'ID : " + id, "Aucun résultat", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la recherche : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        addButton = new JButton("Ajouter une absence");
        addButton.addActionListener(e -> addEntity());
        modifyButton = new JButton("Modifier l'absence séléctionnée");
        modifyButton.addActionListener(e -> {
            int selectedRow = entityTable.getSelectedRow();
            if (selectedRow != -1) {
                modifyEntity(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à modifier.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
        deleteButton = new JButton("Supprimer l'absence séléctionnée");
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
        searchPanel.add(searchByAbsentIDButton);
        searchPanel.add(searchByIDButton);

        add(searchPanel, BorderLayout.NORTH);

        add(new JScrollPane(entityTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateTableData(List<Absence> absences) {
        tableModel.setRowCount(0);
        for (Absence absence : absences) {
            tableModel.addRow(new Object[]{
                    absence.getAbsenceID(),
                    absence.getJustification(),
                    absence.getStartDate(),
                    absence.getEndDate(),
                    absence.getIsUnderCertificate() ? "Oui" : "Non",
                    absence.getAbsentID()
            });
        }
    }

    @Override
    protected void loadEntities() throws ListAbsencesException {
        tableModel.setRowCount(0);
        List<Absence> absences = controller.listAbsences();
        for (Absence absence : absences) {
            tableModel.addRow(new Object[]{
                    absence.getAbsenceID(),
                    absence.getJustification(),
                    absence.getStartDate(),
                    absence.getEndDate(),
                    absence.getIsUnderCertificate() ? "Oui" : "Non",
                    absence.getAbsentID()
            });
        }
    }

    @Override
    protected void addEntity() {
        JTextField justificationField = new JTextField();
        JSpinner startDateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(startDateSpinner, "dd/MM/yyyy");
        startDateSpinner.setEditor(dateEditor);
        JSpinner endDateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor endEditor = new JSpinner.DateEditor(endDateSpinner, "dd/MM/yyyy");
        endDateSpinner.setEditor(endEditor);
        JCheckBox underCertificateCheckBox = new JCheckBox();
        JTextField absentIDField = new JTextField();

        Object[] fields = {
                "Justification:", justificationField,
                "Date de début:", startDateSpinner,
                "Date de fin:", endDateSpinner,
                "Certificat médical:", underCertificateCheckBox,
                "ID Absent:", absentIDField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Ajouter une absence", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String justification = justificationField.getText();
                LocalDate startDate = ((SpinnerDateModel) startDateSpinner.getModel()).getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate endDate = ((SpinnerDateModel) endDateSpinner.getModel()).getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                Boolean isUnderCertificate = underCertificateCheckBox.isSelected();
                String absentID = absentIDField.getText();

                Absence newAbsence = new Absence(justification, startDate, endDate, isUnderCertificate, absentID);
                controller.addAbsence(newAbsence);
                loadEntities();
                JOptionPane.showMessageDialog(this, "Absence ajoutée avec succès.");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de l'absence : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    protected void modifyEntity(int selectedRow) {
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une absence à modifier.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String absenceID = (String) tableModel.getValueAt(selectedRow, 0);
        String justification = (String) tableModel.getValueAt(selectedRow, 1);
        LocalDate startDate = (LocalDate) tableModel.getValueAt(selectedRow, 2);
        LocalDate endDate = (LocalDate) tableModel.getValueAt(selectedRow, 3);
        Boolean isUnderCertificate = "Oui".equals(tableModel.getValueAt(selectedRow, 4));
        String absentID = (String) tableModel.getValueAt(selectedRow, 5);

        JTextField justificationField = new JTextField(justification);
        JSpinner startDateSpinner = new JSpinner(new SpinnerDateModel(java.sql.Date.valueOf(startDate), null, null, Calendar.DAY_OF_MONTH));
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(startDateSpinner, "dd/MM/yyyy");
        startDateSpinner.setEditor(dateEditor);
        JSpinner endDateSpinner = new JSpinner(new SpinnerDateModel(endDate != null ? java.sql.Date.valueOf(endDate) : null, null, null, Calendar.DAY_OF_MONTH));
        JSpinner.DateEditor endEditor = new JSpinner.DateEditor(endDateSpinner, "dd/MM/yyyy");
        endDateSpinner.setEditor(endEditor);
        JCheckBox underCertificateCheckBox = new JCheckBox();
        underCertificateCheckBox.setSelected(isUnderCertificate);
        JTextField absentIDField = new JTextField(absentID);

        Object[] fields = {
                "Justification:", justificationField,
                "Date de début:", startDateSpinner,
                "Date de fin:", endDateSpinner,
                "Certificat médical:", underCertificateCheckBox,
                "ID Absent:", absentIDField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Modifier une absence", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                justification = justificationField.getText();
                startDate = ((SpinnerDateModel) startDateSpinner.getModel()).getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                endDate = ((SpinnerDateModel) endDateSpinner.getModel()).getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                isUnderCertificate = underCertificateCheckBox.isSelected();
                absentID = absentIDField.getText();

                Absence updatedAbsence = new Absence(justification, startDate, endDate, isUnderCertificate, absentID);
                updatedAbsence.setAbsenceID(absenceID);
                controller.updateAbsence(updatedAbsence);
                loadEntities();
                JOptionPane.showMessageDialog(this, "Absence modifiée avec succès.");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la modification de l'absence : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    protected void deleteEntity(int selectedRow) {
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une absence à supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String absenceID = (String) tableModel.getValueAt(selectedRow, 0);

        int confirmation = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer cette absence ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                controller.deleteAbsence(absenceID);
                loadEntities();
                JOptionPane.showMessageDialog(this, "Absence supprimée avec succès.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression de l'absence : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
