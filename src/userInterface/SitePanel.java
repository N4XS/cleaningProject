package userInterface;

import controller.ApplicationController;
import exceptions.list.ListSitesException;
import exceptions.insert.InsertSiteException;
import models.Site;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SitePanel extends EntityModelPanel {

    private JButton addButton;
    private JButton modifyButton;
    private JButton deleteButton;
    private JTextField searchTextField;
    private JButton searchByNameButton;
    private JButton searchByIDButton;
    private JButton searchByPostalCodeButton;

    public SitePanel(ApplicationController controller) {
        super(controller, new String[]{"Site", "Nom du Site", "Rue", "Numéro", "Boîte", "Description", "Code Postal", "Localité", "Propriétaire"});
        initialize();
    }

    private void initialize() {
        searchTextField = new JTextField(20);
        searchByNameButton = new JButton("Rechercher par nom");
        searchByNameButton.addActionListener(e -> {
            String name = searchTextField.getText().trim();
            if (!name.isEmpty()) {
                try {
                    List<Site> sites = controller.getSiteByName(name);
                    updateTableData(sites);
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
                    Site site = controller.getSiteByID(id);
                    if (site != null) {
                        updateTableData(List.of(site));
                    } else {
                        JOptionPane.showMessageDialog(this, "Aucun site trouvé pour l'ID : " + id, "Aucun résultat", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la recherche : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        searchByPostalCodeButton = new JButton("Rechercher par code postal");
        searchByPostalCodeButton.addActionListener(e -> {
            String postalCode = searchTextField.getText().trim();
            if (!postalCode.isEmpty()) {
                try {
                    List<Site> sites = controller.getSiteByCodePostal(Integer.parseInt(postalCode));
                    updateTableData(sites);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la recherche : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        addButton = new JButton("Ajouter un site");
        addButton.addActionListener(e -> addEntity());
        modifyButton = new JButton("Modifier le site séléctionné");
        modifyButton.addActionListener(e -> {
            int selectedRow = entityTable.getSelectedRow();
            if (selectedRow != -1) {
                modifyEntity(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à modifier.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
        deleteButton = new JButton("Supprimer le site sélectionné");
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
        searchPanel.add(searchByPostalCodeButton);

        add(searchPanel, BorderLayout.NORTH);
        add(new JScrollPane(entityTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }



    @Override
    protected void loadEntities() throws ListSitesException {
        tableModel.setRowCount(0);
        List<Site> sites = controller.listSites();
        for (Site site : sites) {
            tableModel.addRow(new Object[]{
                    site.getSiteID(),
                    site.getSiteName(),
                    site.getStreetName(),
                    site.getStreetNumber(),
                    site.getBoxHouse(),
                    site.getDescription(),
                    site.getCity().getPostalCode(),
                    site.getCity().getLocality(),
                    site.getClientOwnerID()
            });
        }
    }

    private void updateTableData(List<Site> sites) {
        tableModel.setRowCount(0);
        for (Site site : sites) {
            tableModel.addRow(new Object[]{
                    site.getSiteID(),
                    site.getSiteName(),
                    site.getStreetName(),
                    site.getStreetNumber(),
                    site.getBoxHouse(),
                    site.getDescription(),
                    site.getCity().getPostalCode(),
                    site.getCity().getLocality(),
                    site.getClientOwnerID()
            });
        }
    }

    @Override
    protected void addEntity() {
        JTextField siteNameField = new JTextField();
        JTextField streetNameField = new JTextField();
        JSpinner streetNumberSpinner = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
        JTextField boxHouseField = new JTextField();
        JTextField descriptionField = new JTextField();
        JTextField postalCodeField = new JTextField();
        JTextField localityField = new JTextField();
        JTextField clientOwnerIDField = new JTextField();

        Object[] fields = {
                "Nom du site:", siteNameField,
                "Nom de la rue:", streetNameField,
                "Numéro de rue:", streetNumberSpinner,
                "Boîte:", boxHouseField,
                "Description:", descriptionField,
                "Code Postal:", postalCodeField,
                "Localité:", localityField,
                "ID Propriétaire:", clientOwnerIDField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Ajouter un site", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String siteName = siteNameField.getText();
                String streetName = streetNameField.getText();
                int streetNumber = (int) streetNumberSpinner.getValue();
                String boxHouse = boxHouseField.getText();
                String description = descriptionField.getText();
                int postalCode = Integer.parseInt(postalCodeField.getText());
                String locality = localityField.getText();
                String clientOwnerID = clientOwnerIDField.getText();

                Site newSite = new Site(siteName, streetName, streetNumber, boxHouse, description, postalCode, locality, clientOwnerID);
                controller.addSite(newSite);
                loadEntities();
                JOptionPane.showMessageDialog(this, "Site ajouté avec succès.");
            } catch (InsertSiteException ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout du site : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    protected void modifyEntity(int selectedRow) {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un site à modifier.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String siteID = (String) tableModel.getValueAt(selectedRow, 0);
        String siteName = (String) tableModel.getValueAt(selectedRow, 1);
        String streetName = (String) tableModel.getValueAt(selectedRow, 2);
        int streetNumber = (Integer) tableModel.getValueAt(selectedRow, 3);
        String boxHouse = (String) tableModel.getValueAt(selectedRow, 4);
        String description = (String) tableModel.getValueAt(selectedRow, 5);
        int postalCode = (Integer) tableModel.getValueAt(selectedRow, 6);
        String locality = (String) tableModel.getValueAt(selectedRow, 7);
        String clientOwnerID = (String) tableModel.getValueAt(selectedRow, 8);

        JTextField siteNameField = new JTextField(siteName);
        JTextField streetNameField = new JTextField(streetName);
        JSpinner streetNumberSpinner = new JSpinner(new SpinnerNumberModel(streetNumber, 1, Integer.MAX_VALUE, 1));
        JTextField boxHouseField = new JTextField(boxHouse);
        JTextArea descriptionArea = new JTextArea(description, 3, 20);
        JTextField postalCodeField = new JTextField(String.valueOf(postalCode));
        JTextField localityField = new JTextField(locality);
        JTextField clientOwnerIDField = new JTextField(clientOwnerID);

        Object[] fields = {
                "Nom du site:", siteNameField,
                "Nom de la rue:", streetNameField,
                "Numéro de rue:", streetNumberSpinner,
                "Boîte:", boxHouseField,
                "Description:", new JScrollPane(descriptionArea),
                "Code postal:", postalCodeField,
                "Localité:", localityField,
                "ID du client propriétaire:", clientOwnerIDField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Modifier le site", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                siteName = siteNameField.getText();
                streetName = streetNameField.getText();
                streetNumber = (Integer) streetNumberSpinner.getValue();
                boxHouse = boxHouseField.getText();
                description = descriptionArea.getText();
                postalCode = Integer.parseInt(postalCodeField.getText());
                locality = localityField.getText();
                clientOwnerID = clientOwnerIDField.getText();

                Site updatedSite = new Site(siteName, streetName, streetNumber, boxHouse, description, postalCode, locality, clientOwnerID);
                updatedSite.setSiteID(siteID);

                controller.updateSite(updatedSite);
                loadEntities();
                JOptionPane.showMessageDialog(this, "Site modifié avec succès.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la modification du site : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    protected void deleteEntity(int selectedRow) {
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un site à supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String siteID = (String) tableModel.getValueAt(selectedRow, 0);

        int confirmation = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer ce site ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                controller.deleteSite(siteID);
                loadEntities();
                JOptionPane.showMessageDialog(this, "Site supprimé avec succès.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression du site : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


}
