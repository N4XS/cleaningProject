package userInterface;

import controller.ApplicationController;
import exceptions.list.ListTeamsException;
import exceptions.insert.InsertTeamException;
import models.Team;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TeamPanel extends EntityModelPanel {

    private JButton addButton;
    private JButton modifyButton;
    private JButton deleteButton;
    private JTextField searchTextField;
    private JButton searchByIDButton;
    private JButton searchByMemberONSSButton;

    public TeamPanel(ApplicationController controller) {
        super(controller, new String[]{"ID de l'équipe", "Leader", "Membres"});
        initialize();
    }

    private void initialize() {
        searchTextField = new JTextField(20);
        searchByIDButton = new JButton("Rechercher par ID");
        searchByIDButton.addActionListener(e -> {
            String id = searchTextField.getText().trim();
            if (!id.isEmpty()) {
                try {
                    Team team = controller.getTeamByTeamID(id);
                    if (team != null) {
                        updateTableData(List.of(team));
                    } else {
                        JOptionPane.showMessageDialog(this, "Aucune équipe trouvée pour l'ID : " + id, "Aucun résultat", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la recherche : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        searchByMemberONSSButton = new JButton("Rechercher par membre ONSS");
        searchByMemberONSSButton.addActionListener(e -> {
            String onss = searchTextField.getText().trim();
            if (!onss.isEmpty()) {
                try {
                    List<Team> teams = controller.getTeamsByMemberONSS(onss);
                    updateTableData(teams);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la recherche : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        addButton = new JButton("Ajouter une équipe");
        addButton.addActionListener(e -> addEntity());
        modifyButton = new JButton("Modifier l'équipe séléctionnée");
        modifyButton.addActionListener(e -> {
            int selectedRow = entityTable.getSelectedRow();
            if (selectedRow != -1) {
                modifyEntity(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à modifier.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
        deleteButton = new JButton("Supprimer l'équipe sélectionnée");
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
        searchPanel.add(searchByMemberONSSButton);


        add(searchPanel, BorderLayout.NORTH);
        add(new JScrollPane(entityTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

    }

    private void updateTableData(List<Team> teams) {
        tableModel.setRowCount(0);
        for (Team team : teams) {
            tableModel.addRow(new Object[]{
                    team.getTeamID(),
                    String.join(", ", team.getStaffMemberIDs())
            });
        }
    }

    @Override
    protected void loadEntities() throws ListTeamsException {
        tableModel.setRowCount(0);
        List<Team> teams = controller.listTeams();
        for (Team team : teams) {
            tableModel.addRow(new Object[]{
                    team.getTeamID(),
                    team.getLeaderID(),
                    String.join(", ", team.getStaffMemberIDs())
            });
        }
    }

    @Override
    protected void addEntity() {
        JTextField leaderIDField = new JTextField();
        JTextField membersField = new JTextField();

        Object[] fields = {
                "ID du leader:", leaderIDField,
                "Membres (séparés par des virgules):", membersField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Ajouter une équipe", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String leaderID = leaderIDField.getText();
                String[] membersArray = membersField.getText().split(",");
                List<String> members = List.of(membersArray).stream().map(String::trim).collect(Collectors.toList());

                Team newTeam = new Team(members);
                controller.addTeam(newTeam);
                loadEntities();
                JOptionPane.showMessageDialog(this, "Équipe ajoutée avec succès.");
            } catch (InsertTeamException ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de l'équipe : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    protected void modifyEntity(int selectedRow) {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une équipe à modifier.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String teamID = (String) tableModel.getValueAt(selectedRow, 0);
        String leaderID = (String) tableModel.getValueAt(selectedRow, 1);
        String secondMemberID = (String) tableModel.getValueAt(selectedRow, 2);
        String thirdMemberID = (String) tableModel.getValueAt(selectedRow, 3);

        JTextField leaderIDField = new JTextField(leaderID);
        JTextField secondMemberIDField = new JTextField(secondMemberID);
        JTextField thirdMemberIDField = new JTextField(thirdMemberID);

        Object[] fields = {
                "ID du leader:", leaderIDField,
                "ID du deuxième membre:", secondMemberIDField,
                "ID du troisième membre:", thirdMemberIDField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Modifier l'équipe", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                leaderID = leaderIDField.getText();
                secondMemberID = secondMemberIDField.getText();
                thirdMemberID = thirdMemberIDField.getText();

                List<String> staffMemberIDs = new ArrayList<>();
                if (!leaderID.trim().isEmpty()) {
                    staffMemberIDs.add(leaderID);
                }
                if (!secondMemberID.trim().isEmpty()) {
                    staffMemberIDs.add(secondMemberID);
                }
                if (!thirdMemberID.trim().isEmpty()) {
                    staffMemberIDs.add(thirdMemberID);
                }

                Team updatedTeam = new Team(staffMemberIDs);
                updatedTeam.setTeamID(teamID);

                controller.updateTeam(updatedTeam);
                loadEntities();
                JOptionPane.showMessageDialog(this, "Équipe modifiée avec succès.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la modification de l'équipe : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    protected void deleteEntity(int selectedRow) {
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une équipe à supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String teamID = (String) tableModel.getValueAt(selectedRow, 0);

        int confirmation = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer cette équipe ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                controller.deleteTeam(teamID);
                loadEntities();
                JOptionPane.showMessageDialog(this, "Équipe supprimée avec succès.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression de l'équipe : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
