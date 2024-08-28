package userInterface;

import controller.ApplicationController;
import exceptions.list.ListStaffMembersException;
import exceptions.insert.InsertStaffMemberException;
import models.City;
import models.StaffMember;
import models.Warning;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class StaffMemberPanel extends EntityModelPanel {

    private JButton addButton;
    private JButton modifyButton;
    public JButton deleteButton;
    private JTextField searchTextField;
    private JButton searchByNameButton;
    private JButton searchByONSSButton;

    public StaffMemberPanel(ApplicationController controller) {
        super(controller, new String[]{"Num ONSS", "Prénom", "Nom", "Date de Naissance", "Email", "Rue", "Numéro", "Boîte", "Téléphone", "Date de Début", "Nettoyeur", "Diplômé", "Code Postal", "Localité"});
        initialize();
    }

    private void initialize() {
        addButton = new JButton("Ajouter un membre du personnel");
        addButton.addActionListener(e -> addEntity());
        modifyButton = new JButton("Modifier le personnel séléctionné");
        modifyButton.addActionListener(e -> {
            int selectedRow = entityTable.getSelectedRow();
            if (selectedRow != -1) {
                modifyEntity(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à modifier.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
        deleteButton = new JButton("Supprimer le personnel sélectionné");
        deleteButton.addActionListener(e -> {
            int selectedRow = entityTable.getSelectedRow();
            if (selectedRow != -1) {
                deleteEntity(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
        searchTextField = new JTextField(20);
        searchByNameButton = new JButton("Rechercher par nom");
        searchByONSSButton = new JButton("Rechercher par ONSS");
        searchByNameButton.addActionListener(e -> {
            String name = searchTextField.getText().trim();
            if (!name.isEmpty()) {
                try {
                    List<StaffMember> staffMembers = controller.getStaffMembersByName(name);
                    if (staffMembers.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Aucun résultat pour ce nom.", "Aucun résultat", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        updateTableData(staffMembers);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la recherche : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez entrer un nom pour la recherche.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        searchByONSSButton.addActionListener(e -> {
            try {
                controller.getStaffMembersByName(searchTextField.getText());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Aucun résultat pour ce numéro ONSS.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.LINE_AXIS));
        searchPanel.add(searchTextField);
        searchPanel.add(Box.createHorizontalStrut(10));
        searchPanel.add(searchByNameButton);
        searchPanel.add(searchByONSSButton);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(addButton);
        buttonPanel.add(modifyButton);
        buttonPanel.add(deleteButton);

        add(searchPanel, BorderLayout.NORTH);
        add(new JScrollPane(entityTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateTableData(List<StaffMember> staffMembers) {
        tableModel.setRowCount(0);
        for (StaffMember member : staffMembers) {
            tableModel.addRow(new Object[]{
                    member.getNumONSS(),
                    member.getFirstName(),
                    member.getLastName(),
                    member.getBirthday(),
                    member.getEmail(),
                    member.getStreetName(),
                    member.getStreetNumber(),
                    member.getBoxNumber(),
                    member.getCellphoneNumber(),
                    member.getStartDate(),
                    member.getIsCleaner() ? "Oui" : "Non",
                    member.getGraduate(),
                    member.getCity().getPostalCode(),
                    member.getCity().getLocality()
            });
        }
    }

    @Override
    protected void loadEntities() throws ListStaffMembersException {
        tableModel.setRowCount(0);
        List<StaffMember> staffMembers = controller.listStaffMembers();
        for (StaffMember member : staffMembers) {
            tableModel.addRow(new Object[]{
                    member.getNumONSS(),
                    member.getFirstName(),
                    member.getLastName(),
                    member.getBirthday(),
                    member.getEmail(),
                    member.getStreetName(),
                    member.getStreetNumber(),
                    member.getBoxNumber(),
                    member.getCellphoneNumber(),
                    member.getStartDate(),
                    member.getIsCleaner() ? "Oui" : "Non",
                    member.getGraduate(),
                    member.getCity().getPostalCode(),
                    member.getCity().getLocality()
            });
        }
    }



    @Override
    protected void addEntity() {
        JTextField numONSSField = new JTextField();
        JTextField firstNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JSpinner birthdaySpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor birthdayEditor = new JSpinner.DateEditor(birthdaySpinner, "dd/MM/yyyy");
        birthdaySpinner.setEditor(birthdayEditor);
        JTextField emailField = new JTextField();
        JTextField streetNameField = new JTextField();
        JSpinner streetNumberSpinner = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
        JTextField boxNumberField = new JTextField();
        JTextField cellphoneNumberField = new JTextField();
        JSpinner startDateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor startEditor = new JSpinner.DateEditor(startDateSpinner, "dd/MM/yyyy");
        startDateSpinner.setEditor(startEditor);
        JCheckBox isCleanerCheckBox = new JCheckBox();
        JTextField graduateField = new JTextField();
        JTextField postalCodeField = new JTextField();
        JTextField localityField = new JTextField();

        Object[] fields = {
                "Num ONSS:", numONSSField,
                "Prénom:", firstNameField,
                "Nom:", lastNameField,
                "Date de Naissance:", birthdaySpinner,
                "Email:", emailField,
                "Rue:", streetNameField,
                "Numéro de rue:", streetNumberSpinner,
                "Boîte:", boxNumberField,
                "Téléphone:", cellphoneNumberField,
                "Date de Début:", startDateSpinner,
                "Nettoyeur:", isCleanerCheckBox,
                "Diplômé:", graduateField,
                "Code Postal:", postalCodeField,
                "Localité:", localityField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Ajouter un membre du personnel", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String numONSS = numONSSField.getText();
                LocalDate birthday = ((SpinnerDateModel) birthdaySpinner.getModel()).getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String email = emailField.getText();
                String streetName = streetNameField.getText();
                int streetNumber = (int) streetNumberSpinner.getValue();
                String boxNumber = boxNumberField.getText();
                String cellphoneNumber = cellphoneNumberField.getText();
                LocalDate startDate = ((SpinnerDateModel) startDateSpinner.getModel()).getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                Boolean isCleaner = isCleanerCheckBox.isSelected();
                String graduate = graduateField.getText();
                int postalCode = Integer.parseInt(postalCodeField.getText());
                String locality = localityField.getText();

                City city = new City(postalCode, locality);
                StaffMember newStaffMember = new StaffMember(numONSS, birthday, firstName, lastName, email, streetName, streetNumber, boxNumber, cellphoneNumber, startDate, isCleaner, graduate, city);
                controller.addStaffMember(newStaffMember);
                loadEntities();
                JOptionPane.showMessageDialog(this, "Membre du personnel ajouté avec succès.");
            } catch (InsertStaffMemberException ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout du membre du personnel : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    protected void modifyEntity(int selectedRow) {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un membre du personnel à modifier.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String numONSS = (String) tableModel.getValueAt(selectedRow, 0);
        String firstName = (String) tableModel.getValueAt(selectedRow, 1);
        String lastName = (String) tableModel.getValueAt(selectedRow, 2);
        LocalDate birthday = (LocalDate) tableModel.getValueAt(selectedRow, 3);
        String email = (String) tableModel.getValueAt(selectedRow, 4);
        String streetName = (String) tableModel.getValueAt(selectedRow, 5);
        int streetNumber = (Integer) tableModel.getValueAt(selectedRow, 6);
        String boxNumber = (String) tableModel.getValueAt(selectedRow, 7);
        String cellphoneNumber = (String) tableModel.getValueAt(selectedRow, 8);
        LocalDate startDate = (LocalDate) tableModel.getValueAt(selectedRow, 9);
        boolean isCleaner = "Oui".equals(tableModel.getValueAt(selectedRow, 10));
        String graduate = (String) tableModel.getValueAt(selectedRow, 11);
        int postalCode = (Integer) tableModel.getValueAt(selectedRow, 12);
        String locality = (String) tableModel.getValueAt(selectedRow, 13);

        JTextField firstNameField = new JTextField(firstName);
        JTextField lastNameField = new JTextField(lastName);
        JSpinner birthdaySpinner = new JSpinner(new SpinnerDateModel());
        birthdaySpinner.setEditor(new JSpinner.DateEditor(birthdaySpinner, "dd/MM/yyyy"));
        birthdaySpinner.setValue(java.sql.Date.valueOf(birthday));
        JTextField emailField = new JTextField(email);
        JTextField streetNameField = new JTextField(streetName);
        JSpinner streetNumberSpinner = new JSpinner(new SpinnerNumberModel(streetNumber, 1, Integer.MAX_VALUE, 1));
        JTextField boxNumberField = new JTextField(boxNumber);
        JTextField cellphoneNumberField = new JTextField(cellphoneNumber);
        JSpinner startDateSpinner = new JSpinner(new SpinnerDateModel());
        startDateSpinner.setEditor(new JSpinner.DateEditor(startDateSpinner, "dd/MM/yyyy"));
        startDateSpinner.setValue(java.sql.Date.valueOf(startDate));
        JCheckBox isCleanerCheckBox = new JCheckBox("Nettoyeur", isCleaner);
        JTextField graduateField = new JTextField(graduate);
        JTextField postalCodeField = new JTextField(String.valueOf(postalCode));
        JTextField localityField = new JTextField(locality);

        Object[] fields = {
                "Prénom:", firstNameField,
                "Nom:", lastNameField,
                "Date de Naissance:", birthdaySpinner,
                "Email:", emailField,
                "Rue:", streetNameField,
                "Numéro de rue:", streetNumberSpinner,
                "Boîte:", boxNumberField,
                "Téléphone:", cellphoneNumberField,
                "Date de Début:", startDateSpinner,
                "Nettoyeur:", isCleanerCheckBox,
                "Diplômé:", graduateField,
                "Code Postal:", postalCodeField,
                "Localité:", localityField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Modifier le membre du personnel", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                firstName = firstNameField.getText();
                lastName = lastNameField.getText();
                birthday = ((java.util.Date) birthdaySpinner.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                email = emailField.getText();
                streetName = streetNameField.getText();
                streetNumber = (Integer) streetNumberSpinner.getValue();
                boxNumber = boxNumberField.getText();
                cellphoneNumber = cellphoneNumberField.getText();
                startDate = ((java.util.Date) startDateSpinner.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                isCleaner = isCleanerCheckBox.isSelected();
                graduate = graduateField.getText();
                postalCode = Integer.parseInt(postalCodeField.getText());
                locality = localityField.getText();

                City city = new City(postalCode, locality);
                StaffMember updatedStaffMember = new StaffMember(numONSS, birthday, firstName, lastName, email, streetName, streetNumber, boxNumber, cellphoneNumber, startDate, isCleaner, graduate, city);

                controller.updateStaffMember(updatedStaffMember);
                loadEntities();
                JOptionPane.showMessageDialog(this, "Membre du personnel modifié avec succès.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la modification du membre du personnel : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    protected void deleteEntity(int selectedRow) {
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un membre du personnel à supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String numONSS = (String) tableModel.getValueAt(selectedRow, 0);

        try {
            List<Warning> warnings = controller.getWarningByONSS(numONSS);
            if (!warnings.isEmpty()) {
                int confirmation = JOptionPane.showConfirmDialog(this, "Ce membre du personnel est associé à un ou plusieurs avertissements. Voulez-vous vraiment le supprimer ainsi que tous les avertissements associés ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirmation != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            controller.deleteStaffMember(numONSS);
            loadEntities();
            JOptionPane.showMessageDialog(this, "Membre du personnel supprimé avec succès.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la suppression du membre du personnel : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }


}
