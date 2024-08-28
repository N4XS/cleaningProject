package userInterface;

import controller.ApplicationController;
import exceptions.list.ListMaterialsOrdersException;
import exceptions.insert.InsertMaterialsOrderException;
import exceptions.list.ListProductsException;
import models.MaterialsOrder;
import models.Product;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class MaterialsOrderPanel extends EntityModelPanel {

    private JButton addButton;
    private JButton modifyButton;
    private JButton deleteButton;
    private JTextField searchTextField;
    private JButton searchByTeamButton;
    private JButton searchByIDButton;

    public MaterialsOrderPanel(ApplicationController controller) {
        super(controller, new String[]{"Commande", "Justification", "Date", "Équipe", "Produits & Quantités"});
        initialize();
    }

    private void initialize() {
        searchTextField = new JTextField(20);
        searchByTeamButton = new JButton("Rechercher par équipe");
        searchByTeamButton.addActionListener(e -> searchByTeam());

        searchByIDButton = new JButton("Rechercher par ID");
        searchByIDButton.addActionListener(e -> searchByID());

        addButton = new JButton("Ajouter une commande");
        addButton.addActionListener(e -> addEntity());

        modifyButton = new JButton("Modifier la commande sélectionnée");
        modifyButton.addActionListener(e -> {
            int selectedRow = entityTable.getSelectedRow();
            if (selectedRow != -1) {
                modifyEntity(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une ligne à modifier.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteButton = new JButton("Supprimer la commande sélectionnée");
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

    private void searchByTeam() {
        String teamID = searchTextField.getText().trim();
        if (!teamID.isEmpty()) {
            try {
                List<MaterialsOrder> orders = controller.getMaterialsOrderByTeam(teamID);
                for (MaterialsOrder order : orders) {
                    Map<String, Integer> productQuantities = controller.getMaterialsOrder_prod(order.getMaterialsOrderID());
                    if (productQuantities == null || productQuantities.isEmpty()) {
                        System.err.println("La carte des quantités est vide pour la commande ID : " + order.getMaterialsOrderID());
                    } else {
                        order.setProductQuantities(productQuantities);
                    }
                }
                updateTableData(orders);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la recherche : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void searchByID() {
        String id = searchTextField.getText().trim();
        if (!id.isEmpty()) {
            try {
                MaterialsOrder order = controller.getMaterialsOrderByID(id);
                if (order != null) {
                    Map<String, Integer> productQuantities = controller.getMaterialsOrder_prod(order.getMaterialsOrderID());
                    if (productQuantities != null && !productQuantities.isEmpty()) {
                        order.setProductQuantities(productQuantities);
                        updateTableData(List.of(order));
                    } else {
                        JOptionPane.showMessageDialog(this, "La carte des quantités est vide pour cette commande.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Aucune commande trouvée pour l'ID : " + id, "Aucun résultat", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la recherche : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateTableData(List<MaterialsOrder> orders) {
        tableModel.setRowCount(0);
        for (MaterialsOrder order : orders) {
            StringBuilder productQuantities = new StringBuilder();
            for (Map.Entry<String, Integer> entry : order.getProductQuantities().entrySet()) {
                productQuantities.append(entry.getKey()).append(": ").append(entry.getValue()).append(", ");
            }
            if (productQuantities.length() > 0) {
                productQuantities.setLength(productQuantities.length() - 2);
            }

            tableModel.addRow(new Object[]{
                    order.getMaterialsOrderID(),
                    order.getJustification(),
                    order.getDateOrder(),
                    order.getTeamID(),
                    productQuantities.toString()
            });
        }
    }

    @Override
    protected void loadEntities() throws ListMaterialsOrdersException, SQLException {
        List<MaterialsOrder> orders = controller.listMaterialsOrders();
        for (MaterialsOrder order : orders) {
            order.setProductQuantities(controller.getMaterialsOrder_prod(order.getMaterialsOrderID()));
        }
        updateTableData(orders);
    }

    @Override
    protected void addEntity() {
        JTextField justificationField = new JTextField();
        JTextField teamIDField = new JTextField();

        JPanel productPanel = new JPanel(new BorderLayout());
        DefaultListModel<Product> productListModel = new DefaultListModel<>();
        JList<Product> productList = new JList<>(productListModel);
        JScrollPane productScrollPane = new JScrollPane(productList);

        try {
            List<Product> products = controller.listProducts();
            for (Product product : products) {
                productListModel.addElement(product);
            }
        } catch (ListProductsException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des produits : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField quantityField = new JTextField();
        JButton addProductButton = new JButton("Ajouter produit avec quantité");
        DefaultListModel<String> selectedProductsModel = new DefaultListModel<>();
        JList<String> selectedProductsList = new JList<>(selectedProductsModel);
        JScrollPane selectedProductsScrollPane = new JScrollPane(selectedProductsList);

        addProductButton.addActionListener(e -> {
            Product selectedProduct = productList.getSelectedValue();
            if (selectedProduct != null && !quantityField.getText().trim().isEmpty()) {
                int quantity;
                try {
                    quantity = Integer.parseInt(quantityField.getText().trim());
                    if (quantity <= 0) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Quantité invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                selectedProductsModel.addElement(selectedProduct.getProductID() + " - " + selectedProduct.getProductName() + ": " + quantity);
            }
        });

        productPanel.add(new JLabel("Produits disponibles"), BorderLayout.NORTH);
        productPanel.add(productScrollPane, BorderLayout.CENTER);
        JPanel quantityPanel = new JPanel(new BorderLayout());
        quantityPanel.add(new JLabel("Quantité:"), BorderLayout.WEST);
        quantityPanel.add(quantityField, BorderLayout.CENTER);
        quantityPanel.add(addProductButton, BorderLayout.EAST);
        productPanel.add(quantityPanel, BorderLayout.SOUTH);

        JPanel selectedProductPanel = new JPanel(new BorderLayout());
        selectedProductPanel.add(new JLabel("Produits sélectionnés"), BorderLayout.NORTH);
        selectedProductPanel.add(selectedProductsScrollPane, BorderLayout.CENTER);

        JPanel mainProductPanel = new JPanel(new GridLayout(1, 2));
        mainProductPanel.add(productPanel);
        mainProductPanel.add(selectedProductPanel);

        Object[] fields = {
                "Justification:", justificationField,
                "ID Équipe:", teamIDField,
                mainProductPanel
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Ajouter une commande de matériaux", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String justification = justificationField.getText();
                LocalDate date = LocalDate.now();
                String teamID = teamIDField.getText();

                Map<String, Integer> productQuantities = new HashMap<>();
                for (int i = 0; i < selectedProductsModel.size(); i++) {
                    String entry = selectedProductsModel.getElementAt(i);
                    String[] parts = entry.split(": ");
                    String productID = parts[0].split(" - ")[0];
                    int quantity = Integer.parseInt(parts[1]);
                    productQuantities.put(productID, quantity);
                }


                MaterialsOrder newOrder = new MaterialsOrder(justification, date, teamID, productQuantities);
                controller.addMaterialsOrder(newOrder);

                for (Map.Entry<String, Integer> entry : productQuantities.entrySet()) {
                    String productID = entry.getKey();
                    int quantity = entry.getValue();

                    Product product = controller.getProductByID(productID);
                    if (product != null) {
                        product.setNbAvailable(product.getNbAvailable() - quantity);
                        controller.updateProduct(product);
                    }
                }

                loadEntities();
                JOptionPane.showMessageDialog(this, "Commande de matériaux ajoutée avec succès.");
            } catch (InsertMaterialsOrderException ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de la commande de matériaux : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    protected void modifyEntity(int selectedRow) {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une commande de matériaux à modifier.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String materialsOrderID = (String) tableModel.getValueAt(selectedRow, 0);
        String justification = (String) tableModel.getValueAt(selectedRow, 1);
        String teamID = (String) tableModel.getValueAt(selectedRow, 3);
        String productQuantitiesString = (String) tableModel.getValueAt(selectedRow, 4);

        JTextField justificationField = new JTextField(justification);
        JTextField teamIDField = new JTextField(teamID);
        JTextArea productQuantitiesArea = new JTextArea(productQuantitiesString, 3, 20);

        Object[] fields = {
                "Justification:", justificationField,
                "ID de l'équipe:", teamIDField,
                "Produits et Quantités:", new JScrollPane(productQuantitiesArea)
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Modifier la commande de matériaux", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                justification = justificationField.getText();
                LocalDate dateOrder = LocalDate.now();
                teamID = teamIDField.getText();
                String[] productEntries = productQuantitiesArea.getText().split(", ");
                Map<String, Integer> productQuantities = new HashMap<>();
                for (String entry : productEntries) {
                    String[] parts = entry.split(": ");
                    String productID = parts[0];
                    int quantity = Integer.parseInt(parts[1]);
                    productQuantities.put(productID, quantity);
                }

                MaterialsOrder updatedOrder = new MaterialsOrder(justification, dateOrder, teamID, productQuantities);
                updatedOrder.setMaterialsOrderID(materialsOrderID);
                controller.updateMaterialsOrder(updatedOrder);

                for (Map.Entry<String, Integer> entry : productQuantities.entrySet()) {
                    String productID = entry.getKey();
                    int quantity = entry.getValue();

                    Product product = controller.getProductByID(productID);
                    if (product != null) {
                        product.setNbAvailable(product.getNbAvailable() - quantity);
                        controller.updateProduct(product);
                    }
                }

                loadEntities();
                JOptionPane.showMessageDialog(this, "Commande de matériaux modifiée avec succès.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la modification de la commande de matériaux : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    protected void deleteEntity(int selectedRow) {
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une commande de matériaux à supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String materialsOrderID = (String) tableModel.getValueAt(selectedRow, 0);

        int confirmation = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer cette commande de matériaux ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                controller.deleteMaterialsOrder(materialsOrderID);
                loadEntities();
                JOptionPane.showMessageDialog(this, "Commande de matériaux supprimée avec succès.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression de la commande de matériaux : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
