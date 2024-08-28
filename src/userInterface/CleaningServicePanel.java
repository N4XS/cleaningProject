package userInterface;

import controller.ApplicationController;
import exceptions.list.ListCleaningServicesException;
import exceptions.insert.InsertCleaningServiceException;
import models.CleaningService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CleaningServicePanel extends EntityModelPanel {

    private JButton addButton;
    private JButton modifyButton;
    private JButton deleteButton;
    private JTextField searchTextField;
    private JButton searchByTeamButton;
    private JButton searchByIDButton;

    public CleaningServicePanel(ApplicationController controller) {
        super(controller, new String[]{"Prestation", "Début de Prestation", "Durée (min)", "Contrat", "Équipe"});
        initialize();
    }

    private void initialize() {
        searchTextField = new JTextField(20);
        searchByTeamButton = new JButton("Rechercher par équipe");
        searchByTeamButton.addActionListener(e -> {
            String teamID = searchTextField.getText().trim();
            if (!teamID.isEmpty()) {
                try {
                    List<CleaningService> services = controller.getCleaningServicesByTeam(teamID);
                    updateTableData(services);
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
                    CleaningService service = controller.getCleaningServiceByID(id);
                    if (service != null) {
                        updateTableData(List.of(service));
                    } else {
                        JOptionPane.showMessageDialog(this, "Aucune prestation de nettoyage trouvée pour l'ID : " + id, "Aucun résultat", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la recherche : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        addButton = new JButton("Ajouter une prestation de nettoyage");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEntity();
            }
        });
        modifyButton = new JButton("Modifier la prestation séléctionnée");
        modifyButton.addActionListener(e -> {
            int selectedRow = entityTable.getSelectedRow();
            if (selectedRow != -1) {
                modifyEntity(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à modifier.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
        deleteButton = new JButton("Supprimer la prestation sélectionnée");
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
        searchPanel.add(searchByTeamButton);
        searchPanel.add(searchByIDButton);

        add(searchPanel, BorderLayout.NORTH);
        add(new JScrollPane(entityTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateTableData(List<CleaningService> services) {
        tableModel.setRowCount(0);
        for (CleaningService service : services) {
            LocalDate dateStart = service.getDateTimeStartPrest().toLocalDateTime().toLocalDate();

            tableModel.addRow(new Object[]{
                    service.getCleaningServiceID(),
                    dateStart,
                    service.getDuration(),
                    service.getContractID(),
                    service.getTeamID()
            });
        }
    }


    @Override
    protected void loadEntities() throws ListCleaningServicesException {
        tableModel.setRowCount(0);
        List<CleaningService> services = controller.listCleaningServices();
        for (CleaningService service : services) {
            tableModel.addRow(new Object[]{
                    service.getCleaningServiceID(),
                    service.getDateTimeStartPrest(),
                    service.getDuration(),
                    service.getContractID(),
                    service.getTeamID()
            });
        }
    }

    @Override
    protected void addEntity() {
        JSpinner startDateTimeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(startDateTimeSpinner, "dd/MM/yyyy");
        startDateTimeSpinner.setEditor(dateEditor);
        JSpinner durationSpinner = new JSpinner(new SpinnerNumberModel(60, 1, 1440, 1));
        JTextField contractIDField = new JTextField();
        JTextField teamIDField = new JTextField();

        Object[] fields = {
                "Début de la prestation:", startDateTimeSpinner,
                "Durée (en minutes):", durationSpinner,
                "ID du contrat:", contractIDField,
                "ID de l'équipe:", teamIDField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Ajouter une prestation de nettoyage", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                Date startDate = ((SpinnerDateModel) startDateTimeSpinner.getModel()).getDate();
                LocalDateTime startDateTime = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                Timestamp startTimestamp = Timestamp.valueOf(startDateTime);

                int duration = (int) durationSpinner.getValue();
                String contractID = contractIDField.getText();
                String teamID = teamIDField.getText();

                CleaningService newService = new CleaningService(startTimestamp, duration, contractID, teamID);
                controller.addCleaningService(newService);
                loadEntities();
                JOptionPane.showMessageDialog(this, "Prestation de nettoyage ajoutée avec succès.");
            } catch (InsertCleaningServiceException ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de la prestation de nettoyage : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    protected void modifyEntity(int selectedRow) {
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un service de nettoyage à modifier.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String cleaningServiceID = (String) tableModel.getValueAt(selectedRow, 0);
        LocalDate dateStart = ((Timestamp) tableModel.getValueAt(selectedRow, 1)).toLocalDateTime().toLocalDate();
        int duration = (int) tableModel.getValueAt(selectedRow, 2);
        String contractID = (String) tableModel.getValueAt(selectedRow, 3);
        String teamID = (String) tableModel.getValueAt(selectedRow, 4);
        JSpinner dateStartSpinner = new JSpinner(new SpinnerDateModel(java.sql.Date.valueOf(dateStart), null, null, Calendar.DAY_OF_MONTH));
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateStartSpinner, "dd/MM/yyyy");
        dateStartSpinner.setEditor(dateEditor);

        JSpinner durationSpinner = new JSpinner(new SpinnerNumberModel(duration, 1, Integer.MAX_VALUE, 1));
        JTextField contractIDField = new JTextField(contractID);
        JTextField teamIDField = new JTextField(teamID);

        Object[] fields = {
                "Date de début:", dateStartSpinner,
                "Durée (en minutes):", durationSpinner,
                "ID du contrat:", contractIDField,
                "ID de l'équipe:", teamIDField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Modifier un service de nettoyage", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                dateStart = ((java.util.Date) dateStartSpinner.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                duration = (int) durationSpinner.getValue();
                contractID = contractIDField.getText().trim();
                teamID = teamIDField.getText().trim();

                CleaningService updatedService = new CleaningService(Timestamp.valueOf(dateStart.atStartOfDay()), duration, contractID, teamID);
                updatedService.setCleaningServiceID(cleaningServiceID);
                controller.updateCleaningService(updatedService);
                loadEntities();
                JOptionPane.showMessageDialog(this, "Service de nettoyage modifié avec succès.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la modification du service de nettoyage : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    protected void deleteEntity(int selectedRow) {
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une prestation de nettoyage à supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String cleaningServiceID = (String) tableModel.getValueAt(selectedRow, 0);

        int confirmation = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer cette prestation de nettoyage ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                controller.deleteCleaningService(cleaningServiceID);
                loadEntities();
                JOptionPane.showMessageDialog(this, "Prestation de nettoyage supprimée avec succès.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression de la prestation de nettoyage : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
