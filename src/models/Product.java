package models;

public class Product {
    private String productID;
    private String productName;
    private int nbAvailable;
    private String description;

    public Product(String productName, int nbAvailable, String description){
        this.setProductName(productName);
        this.setNbAvailable(nbAvailable);
        this.setDescription(description);
    }

    public String getDescription() {
        return description;
    }

    public int getNbAvailable() {
        return nbAvailable;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductName(String productName) {
        if (productName == null || productName.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du produit ne peut pas être nul ou vide.");
        }
        this.productName = productName;
    }

    public void setNbAvailable(int nbAvailable) {
        if (nbAvailable < 0) {
            throw new IllegalArgumentException("Le nombre de produits disponibles ne peut pas être négatif.");
        }
        this.nbAvailable = nbAvailable;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setProductID(String productID) {
        if (productID == null || productID.trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID du produit ne peut pas être nul ou vide.");
        }
        this.productID = productID;
    }

    @Override
    public String toString() {
        return getProductID() + " - " + getProductName() + " - " + getNbAvailable() + " - " + (getDescription() != null ? getDescription() : "Pas de description");
    }
}
