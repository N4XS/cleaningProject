package userInterface;

import controller.ApplicationController;
import exceptions.list.ListProductsException;
import exceptions.insert.InsertProductException;
import models.Product;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ProductPanel extends EntityModelPanel {

    private JButton addButton;
    private JButton modifyButton;
    private JButton deleteButton;
    private JTextField searchTextField;
    private JButton searchByNameButton;
    private JButton searchByIDButton;

    public ProductPanel(ApplicationController controller) {
        super(controller, new String[]{"Produit", "Nom", "Quantité Disponible", "Description"});
        initialize();
    }

    private void initialize()
    {
        searchTextField = new JTextField(20);
        searchByNameButton = new JButton("Rechercher par nom");
        searchByNameButton.addActionListener(e -> {
            String name = searchTextField.getText().trim();
            if (!name.isEmpty()) {
                try {
                    Product product = controller.getProductByName(name);
                    updateTableData(List.of(product));
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
                    Product product = controller.getProductByID(id);
                    if (product != null) {
                        updateTableData(List.of(product));
                    } else {
                        JOptionPane.showMessageDialog(this, "Aucun produit trouvé pour l'ID : " + id, "Aucun résultat", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la recherche : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        addButton = new JButton("Ajouter un produit");
        addButton.addActionListener(e -> addEntity());
        modifyButton = new JButton("Modifier le produit séléctionné");
        modifyButton.addActionListener(e -> {
            int selectedRow = entityTable.getSelectedRow();
            if (selectedRow != -1) {
                modifyEntity(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à modifier.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
        deleteButton = new JButton("Supprimer le produit sélectionné");
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

    private void updateTableData(List<Product> products) {
        tableModel.setRowCount(0);
        for (Product product : products) {
            tableModel.addRow(new Object[]{
                    product.getProductID(),
                    product.getProductName(),
                    product.getNbAvailable(),
                    product.getDescription()
            });
        }
    }

    @Override
    protected void loadEntities() throws ListProductsException {
        tableModel.setRowCount(0);
        List<Product> products = controller.listProducts();
        for (Product product : products) {
            tableModel.addRow(new Object[]{
                    product.getProductID(),
                    product.getProductName(),
                    product.getNbAvailable(),
                    product.getDescription() != null ? product.getDescription() : "Pas de description"
            });
        }
    }

    @Override
    protected void addEntity() {
        JTextField productNameField = new JTextField();
        JTextField nbAvailableField = new JTextField();
        JTextArea descriptionArea = new JTextArea(3, 20);

        Object[] fields = {
                "Nom du produit:", productNameField,
                "Quantité disponible:", nbAvailableField,
                "Description:", new JScrollPane(descriptionArea)
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Ajouter un produit", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String productName = productNameField.getText();
                int nbAvailable;
                try {
                    nbAvailable = Integer.parseInt(nbAvailableField.getText().trim());
                    if (nbAvailable < 0) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Quantité invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String description = descriptionArea.getText();

                Product newProduct = new Product(productName, nbAvailable, description);
                controller.addProduct(newProduct);
                loadEntities();
                JOptionPane.showMessageDialog(this, "Produit ajouté avec succès.");
            } catch (InsertProductException ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout du produit : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    protected void modifyEntity(int selectedRow) {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un produit à modifier.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String productID = (String) tableModel.getValueAt(selectedRow, 0);
        String productName = (String) tableModel.getValueAt(selectedRow, 1);
        int nbAvailable = (int) tableModel.getValueAt(selectedRow, 2);
        String description = (String) tableModel.getValueAt(selectedRow, 3);

        JTextField productNameField = new JTextField(productName);
        JSpinner nbAvailableSpinner = new JSpinner(new SpinnerNumberModel(nbAvailable, 0, Integer.MAX_VALUE, 1));
        JTextArea descriptionArea = new JTextArea(description, 3, 20);
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);

        Object[] fields = {
                "Nom du produit:", productNameField,
                "Quantité disponible:", nbAvailableSpinner,
                "Description:", descriptionScrollPane
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Modifier le produit", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                productName = productNameField.getText();
                nbAvailable = (int) nbAvailableSpinner.getValue();
                description = descriptionArea.getText();

                Product updatedProduct = new Product(productName, nbAvailable, description);
                updatedProduct.setProductID(productID);

                controller.updateProduct(updatedProduct);
                loadEntities();
                JOptionPane.showMessageDialog(this, "Produit modifié avec succès.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la modification du produit : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    protected void deleteEntity(int selectedRow) {
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un produit à supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String productID = (String) tableModel.getValueAt(selectedRow, 0);

        int confirmation = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer ce produit ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                controller.deleteProduct(productID);
                loadEntities();
                JOptionPane.showMessageDialog(this, "Produit supprimé avec succès.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression du produit : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
