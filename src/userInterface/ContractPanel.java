package userInterface;

import controller.ApplicationController;
import exceptions.list.ListContractsException;
import exceptions.insert.InsertContractException;
import models.Contract;
import models.Contract.DurationType;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class ContractPanel extends EntityModelPanel {

    private JButton addButton;
    private JButton modifyButton;
    private JButton deleteButton;
    private JTextField searchTextField;
    private JButton searchByIDButton;

    public ContractPanel(ApplicationController controller) {
        super(controller, new String[]{"Contrat", "Site", "Client", "Heures/Semaine", "Début", "Fin", "Durée", "Planning"});
        initialize();
    }

    private void initialize() {
        searchTextField = new JTextField(20);
        searchByIDButton = new JButton("Rechercher par ID");
        searchByIDButton.addActionListener(e -> {
            String id = searchTextField.getText().trim();
            if (!id.isEmpty()) {
                try {
                    Contract contract = controller.getContractByID(id);
                    if (contract != null) {
                        updateTableData(List.of(contract));
                    } else {
                        JOptionPane.showMessageDialog(this, "Aucun contrat trouvé pour l'ID : " + id, "Aucun résultat", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la recherche : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        addButton = new JButton("Ajouter un contrat");
        addButton.addActionListener(e -> addEntity());
        modifyButton = new JButton("Modifier le contrat séléctionné");
        modifyButton.addActionListener(e -> {
            int selectedRow = entityTable.getSelectedRow();
            if (selectedRow != -1) {
                modifyEntity(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à modifier.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
        deleteButton = new JButton("Supprimer le contrat sélectionné");
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
        searchPanel.add(searchByIDButton);

        add(searchPanel, BorderLayout.NORTH);

        add(new JScrollPane(entityTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateTableData(List<Contract> contracts) {
        tableModel.setRowCount(0);
        for (Contract contract : contracts) {
            tableModel.addRow(new Object[]{
                    contract.getContractID(),
                    contract.getSiteID(),
                    contract.getClientID(),
                    contract.getNbHoursPerWeek(),
                    contract.getStartDate(),
                    contract.getEndDate(),
                    contract.getPlanning(),
                    contract.getDurationType().getLabel()
            });
        }
    }

    @Override
    protected void loadEntities() throws ListContractsException {
        tableModel.setRowCount(0);
        List<Contract> contracts = controller.listContracts();
        for (Contract contract : contracts) {
            tableModel.addRow(new Object[]{
                    contract.getContractID(),
                    contract.getClientID(),
                    contract.getSiteID(),
                    contract.getNbHoursPerWeek(),
                    contract.getStartDate(),
                    contract.getEndDate(),
                    contract.getDurationType().getLabel(),
                    contract.getPlanning()
            });
        }
    }

    @Override
    protected void addEntity() {
        JTextField clientIDField = new JTextField();
        JTextField siteIDField = new JTextField();
        JSpinner hoursPerWeekSpinner = new JSpinner(new SpinnerNumberModel(40, 1, 168, 1));
        JSpinner startDateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(startDateSpinner, "dd/MM/yyyy");
        startDateSpinner.setEditor(dateEditor);
        JComboBox<DurationType> durationComboBox = new JComboBox<>(DurationType.values());
        JTextField planningDescField = new JTextField();

        Object[] fields = {
                "ID Client:", clientIDField,
                "ID Site:", siteIDField,
                "Heures par semaine:", hoursPerWeekSpinner,
                "Date de début:", startDateSpinner,
                "Durée du contrat:", durationComboBox,
                "Description du planning:", planningDescField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Ajouter un contrat", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String clientID = clientIDField.getText();
                String siteID = siteIDField.getText();
                int nbHoursPerWeek = (int) hoursPerWeekSpinner.getValue();
                LocalDate startDate = ((SpinnerDateModel) startDateSpinner.getModel()).getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                DurationType duration = (DurationType) durationComboBox.getSelectedItem();
                String planningDesc = planningDescField.getText();

                Contract newContract = new Contract(clientID, siteID, nbHoursPerWeek, startDate, duration, planningDesc);
                controller.addContract(newContract);
                loadEntities();
                JOptionPane.showMessageDialog(this, "Contrat ajouté avec succès.");
            } catch (InsertContractException ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout du contrat : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    protected void modifyEntity(int selectedRow) {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un contrat à modifier.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String contractID = (String) tableModel.getValueAt(selectedRow, 0);
        String siteID = (String) tableModel.getValueAt(selectedRow, 1);
        String clientID = (String) tableModel.getValueAt(selectedRow, 2);
        int nbHoursPerWeek = (Integer) tableModel.getValueAt(selectedRow, 3);
        LocalDate startDate = (LocalDate) tableModel.getValueAt(selectedRow, 4);
        LocalDate endDate = (LocalDate) tableModel.getValueAt(selectedRow, 5);
        String planningDesc = (String) tableModel.getValueAt(selectedRow, 7);
        String durationLabel = (String) tableModel.getValueAt(selectedRow, 6);

        Contract.DurationType durationType = null;
        try {
            durationType = Contract.DurationType.fromLabel(durationLabel);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Durée de contrat invalide: " + durationLabel, "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField siteIDField = new JTextField(siteID);
        JTextField clientIDField = new JTextField(clientID);
        JSpinner nbHoursPerWeekSpinner = new JSpinner(new SpinnerNumberModel(nbHoursPerWeek, 1, Integer.MAX_VALUE, 1));
        JSpinner startDateSpinner = new JSpinner(new SpinnerDateModel());
        startDateSpinner.setEditor(new JSpinner.DateEditor(startDateSpinner, "dd/MM/yyyy"));
        startDateSpinner.setValue(java.sql.Date.valueOf(startDate));
        JSpinner endDateSpinner = new JSpinner(new SpinnerDateModel());
        endDateSpinner.setEditor(new JSpinner.DateEditor(endDateSpinner, "dd/MM/yyyy"));
        endDateSpinner.setValue(java.sql.Date.valueOf(endDate));
        JTextField planningDescField = new JTextField(planningDesc);
        JComboBox<Contract.DurationType> durationTypeComboBox = new JComboBox<>(Contract.DurationType.values());
        durationTypeComboBox.setSelectedItem(durationType);

        Object[] fields = {
                "Site ID:", siteIDField,
                "Client ID:", clientIDField,
                "Heures par semaine:", nbHoursPerWeekSpinner,
                "Date de début:", startDateSpinner,
                "Date de fin:", endDateSpinner,
                "Description de la planification:", planningDescField,
                "Type de durée:", durationTypeComboBox
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Modifier le contrat", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                siteID = siteIDField.getText();
                clientID = clientIDField.getText();
                nbHoursPerWeek = (Integer) nbHoursPerWeekSpinner.getValue();
                startDate = ((java.util.Date) startDateSpinner.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                endDate = ((java.util.Date) endDateSpinner.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                planningDesc = planningDescField.getText();
                durationType = (Contract.DurationType) durationTypeComboBox.getSelectedItem();

                Contract updatedContract = new Contract(clientID, siteID, nbHoursPerWeek, startDate, durationType, planningDesc);
                updatedContract.setContractID(contractID);
                updatedContract.setEndDate(endDate);

                controller.updateContract(updatedContract);
                loadEntities();
                JOptionPane.showMessageDialog(this, "Contrat modifié avec succès.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la modification du contrat : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    protected void deleteEntity(int selectedRow) {
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un contrat à supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String contractID = (String) tableModel.getValueAt(selectedRow, 0);

        int confirmation = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer ce contrat ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                controller.deleteContract(contractID);
                loadEntities();
                JOptionPane.showMessageDialog(this, "Contrat supprimé avec succès.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression du contrat : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
