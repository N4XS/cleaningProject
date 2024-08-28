package userInterface;

import controller.ApplicationController;
import exceptions.list.ListCitiesException;
import exceptions.insert.InsertCityException;
import models.City;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class CityPanel extends EntityModelPanel {

    private JButton addButton;
    private JButton modifyButton;
    private JButton deleteButton;
    private JTextField searchTextField;
    private JButton searchByLocalityButton;


    public CityPanel(ApplicationController controller) {
        super(controller, new String[]{"Code Postal", "Localité"});
        initialize();
    }

    private void initialize() {
        searchTextField = new JTextField(20);
        searchByLocalityButton = new JButton("Rechercher par localité");
        searchByLocalityButton.addActionListener(e -> {
            String locality = searchTextField.getText().trim();
            if (!locality.isEmpty()) {
                try {
                    City city = controller.getCityByLocality(locality);
                    if (city != null) {
                        updateTableData(List.of(city));
                    } else {
                        JOptionPane.showMessageDialog(this, "Aucune ville trouvée pour la localité : " + locality, "Aucun résultat", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la recherche : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        addButton = new JButton("Ajouter une ville");
        addButton.addActionListener(e -> addEntity());
        modifyButton = new JButton("Modifier la ville séléctionnée");
        modifyButton.addActionListener(e -> {
            int selectedRow = entityTable.getSelectedRow();
            if (selectedRow != -1) {
                modifyEntity(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à modifier.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
        deleteButton = new JButton("Supprimer la ville sélectionnée");
        deleteButton.addActionListener(e -> {
            int selectedRow = entityTable.getSelectedRow();
            if (selectedRow != -1) {
                deleteEntity(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à supprimer", "Erreur", JOptionPane.ERROR_MESSAGE);
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
        searchPanel.add(searchByLocalityButton);

        add(searchPanel, BorderLayout.NORTH);
        add(new JScrollPane(entityTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateTableData(List<City> cities) {
        tableModel.setRowCount(0);
        for (City city : cities) {
            tableModel.addRow(new Object[]{
                    city.getPostalCode(),
                    city.getLocality()
            });
        }
    }

    @Override
    protected void loadEntities() throws ListCitiesException {
        tableModel.setRowCount(0);
        List<City> cities = controller.listCities();
        for (City city : cities) {
            tableModel.addRow(new Object[]{
                    city.getPostalCode(),
                    city.getLocality()
            });
        }
    }

    @Override
    protected void addEntity() {
        JTextField postalCodeField = new JTextField();
        JTextField localityField = new JTextField();

        Object[] fields = {
                "Code Postal:", postalCodeField,
                "Localité:", localityField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Ajouter une ville", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int postalCode = Integer.parseInt(postalCodeField.getText());
                String locality = localityField.getText();

                City newCity = new City(postalCode, locality);
                controller.addCity(newCity);
                loadEntities();
                JOptionPane.showMessageDialog(this, "Ville ajoutée avec succès.");
            } catch (InsertCityException | ListCitiesException ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de la ville : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    protected void modifyEntity(int selectedRow) {
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ville à modifier.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int postalCode = (int) tableModel.getValueAt(selectedRow, 0);
        String locality = (String) tableModel.getValueAt(selectedRow, 1);

        JTextField postalCodeField = new JTextField(String.valueOf(postalCode));
        JTextField localityField = new JTextField(locality);

        Object[] fields = {
                "Code Postal:", postalCodeField,
                "Localité:", localityField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Modifier une ville", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                postalCode = Integer.parseInt(postalCodeField.getText().trim());
                locality = localityField.getText().trim();

                City updatedCity = new City(postalCode, locality);
                controller.updateCity(updatedCity);
                loadEntities();
                JOptionPane.showMessageDialog(this, "Ville modifiée avec succès.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Erreur : le code postal doit être un nombre.", "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la modification de la ville : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    protected void deleteEntity(int selectedRow) {
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ville à supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int postalCode = (int) tableModel.getValueAt(selectedRow, 0);
        String locality = (String) tableModel.getValueAt(selectedRow, 1);

        int confirmation = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer cette ville ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                controller.deleteCity(postalCode, locality);
                loadEntities();
                JOptionPane.showMessageDialog(this, "Ville supprimée avec succès.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression de la ville : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
