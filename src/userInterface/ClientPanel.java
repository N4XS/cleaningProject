package userInterface;

import controller.ApplicationController;
import exceptions.list.ListClientsException;
import exceptions.insert.InsertClientException;
import models.Client;
import models.City;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ClientPanel extends EntityModelPanel {

    private JButton addButton;
    private JButton modifyButton;
    private JButton deleteButton;
    private JTextField searchTextField;
    private JButton searchByNameButton;
    private JButton searchByIDButton;


    public ClientPanel(ApplicationController controller) {
        super(controller, new String[]{"Client", "Nom", "Prénom", "Email", "GSM", "Rue", "Numéro", "Boîte", "Ville"});
        initialize();
    }

    private void initialize() {
        searchTextField = new JTextField(20);
        searchByNameButton = new JButton("Rechercher par nom");
        searchByNameButton.addActionListener(e -> {
            String name = searchTextField.getText().trim();
            if (!name.isEmpty()) {
                try {
                    List<Client> clients = controller.getClientsByName(name);
                    updateTableData(clients);
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
                    Client client = controller.getClientByID(id);
                    if (client != null) {
                        updateTableData(List.of(client));
                    } else {
                        JOptionPane.showMessageDialog(this, "Aucun client trouvé pour l'ID : " + id, "Aucun résultat", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la recherche : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        addButton = new JButton("Ajouter un client");
        addButton.addActionListener(e -> addEntity());
        modifyButton = new JButton("Modifier le client séléctionné");
        modifyButton.addActionListener(e -> {
            int selectedRow = entityTable.getSelectedRow();
            if (selectedRow != -1) {
                modifyEntity(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à modifier.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
        deleteButton = new JButton("Supprimer le client sélectionné");
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

    private void updateTableData(List<Client> clients) {
        tableModel.setRowCount(0);
        for (Client client : clients) {
            tableModel.addRow(new Object[]{
                    client.getClientID(),
                    client.getName(),
                    client.getFirstName(),
                    client.getEmail(),
                    client.getGsm(),
                    client.getStreetName(),
                    client.getStreetNumber(),
                    client.getBoxNumber(),
                    client.getCity().getPostalCode(),
                    client.getCity().getLocality()
            });
        }
    }

    @Override
    protected void loadEntities() throws ListClientsException {
        tableModel.setRowCount(0);
        List<Client> clients = controller.listClients();
        for (Client client : clients) {
            tableModel.addRow(new Object[]{
                    client.getClientID(),
                    client.getName(),
                    client.getFirstName(),
                    client.getEmail(),
                    client.getGsm(),
                    client.getStreetName(),
                    client.getStreetNumber(),
                    client.getBoxNumber(),
                    client.getCity()
            });
        }
    }

    @Override
    protected void addEntity() {
        JTextField nameField = new JTextField();
        JTextField firstNameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField gsmField = new JTextField();
        JTextField streetNameField = new JTextField();
        JTextField streetNumberField = new JTextField();
        JTextField numberField = new JTextField();
        JTextField postalCodeField = new JTextField();
        JTextField localityField = new JTextField();

        Object[] fields = {
                "Nom:", nameField,
                "Prénom:", firstNameField,
                "Email:", emailField,
                "GSM:", gsmField,
                "Nom de la rue:", streetNameField,
                "Numéro de rue:", streetNumberField,
                "boîte:", numberField,
                "Code postal:", postalCodeField,
                "Localité:", localityField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Ajouter un client", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText();
                String firstName = firstNameField.getText();
                String email = emailField.getText();
                String gsm = gsmField.getText();
                String streetName = streetNameField.getText();
                int streetNumber = Integer.parseInt(streetNumberField.getText());
                String boxNumber = numberField.getText();
                int postalCode = Integer.parseInt(postalCodeField.getText());
                String locality = localityField.getText();

                Client newClient = new Client(name, firstName, email, gsm, streetName, streetNumber, boxNumber, postalCode, locality);
                controller.addClient(newClient);
                loadEntities();
                JOptionPane.showMessageDialog(this, "Client ajouté avec succès.");
            } catch (InsertClientException ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout du client : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Le code postal et le numéro de rue doivent être des nombres.", "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    protected void modifyEntity(int selectedRow) {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un client à modifier.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String clientID = (String) tableModel.getValueAt(selectedRow, 0);
        String name = (String) tableModel.getValueAt(selectedRow, 1);
        String firstName = (String) tableModel.getValueAt(selectedRow, 2);
        String email = (String) tableModel.getValueAt(selectedRow, 3);
        String gsm = (String) tableModel.getValueAt(selectedRow, 4);
        String streetName = (String) tableModel.getValueAt(selectedRow, 5);
        int streetNumber = (Integer) tableModel.getValueAt(selectedRow, 6);
        String boxNumber = (String) tableModel.getValueAt(selectedRow, 7);
        City city = (City) tableModel.getValueAt(selectedRow, 8);

        int postalCode = city.getPostalCode();
        String locality = city.getLocality();

        JTextField nameField = new JTextField(name);
        JTextField firstNameField = new JTextField(firstName);
        JTextField emailField = new JTextField(email);
        JTextField gsmField = new JTextField(gsm);
        JTextField streetNameField = new JTextField(streetName);
        JSpinner streetNumberSpinner = new JSpinner(new SpinnerNumberModel(streetNumber, 1, Integer.MAX_VALUE, 1));
        JTextField boxNumberField = new JTextField(boxNumber);
        JTextField postalCodeField = new JTextField(String.valueOf(postalCode));
        JTextField localityField = new JTextField(locality);

        Object[] fields = {
                "Nom:", nameField,
                "Prénom:", firstNameField,
                "Email:", emailField,
                "GSM:", gsmField,
                "Rue:", streetNameField,
                "Numéro:", streetNumberSpinner,
                "Boîte:", boxNumberField,
                "Code postal:", postalCodeField,
                "Localité:", localityField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Modifier le client", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                name = nameField.getText();
                firstName = firstNameField.getText();
                email = emailField.getText();
                gsm = gsmField.getText();
                streetName = streetNameField.getText();
                streetNumber = (Integer) streetNumberSpinner.getValue();
                boxNumber = boxNumberField.getText();
                postalCode = Integer.parseInt(postalCodeField.getText());
                locality = localityField.getText();

                City updatedCity = new City(postalCode, locality);
                Client updatedClient = new Client(name, firstName, email, gsm, streetName, streetNumber, boxNumber, updatedCity.getPostalCode(), updatedCity.getLocality());
                updatedClient.setClientID(clientID);

                controller.updateClient(updatedClient);
                loadEntities();
                JOptionPane.showMessageDialog(this, "Client modifié avec succès.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la modification du client : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    protected void deleteEntity(int selectedRow) {
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un client à supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String clientID = (String) tableModel.getValueAt(selectedRow, 0);

        int confirmation = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer ce client ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                controller.deleteClient(clientID);
                loadEntities();
                JOptionPane.showMessageDialog(this, "Client supprimé avec succès.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression du client : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


}
