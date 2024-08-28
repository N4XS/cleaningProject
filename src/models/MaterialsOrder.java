package models;

import java.time.LocalDate;
import java.util.Map;

public class MaterialsOrder {
    private String materialsOrderID;
    private String justification;
    private LocalDate dateOrder;
    private String teamID;
    private Map<String, Integer> productQuantities;

    public MaterialsOrder(String justification, LocalDate dateOrder, String teamID, Map<String, Integer> quantities)  {
        this.justification = justification;
        this.dateOrder = dateOrder;
        this.teamID = teamID;
        setProductQuantities(quantities);
    }


    public String getMaterialsOrderID() {
        return materialsOrderID;
    }

    public String getJustification() {
        return justification;
    }

    public LocalDate getDateOrder() {
        return dateOrder;
    }

    public String getTeamID() {
        return teamID;
    }

    public Map<String, Integer> getProductQuantities() {
        return productQuantities;
    }

    public void setJustification(String justification) {
        if (justification == null || justification.trim().isEmpty()) {
            throw new IllegalArgumentException("La justification ne peut pas être vide.");
        }
        this.justification = justification;
    }

    public void setDateOrder(LocalDate dateOrder) {
        if (dateOrder == null || dateOrder.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La date de la commande ne peut pas être dans le futur.");
        }
        this.dateOrder = dateOrder;
    }

    public void setMaterialsOrderID(String materialsOrderID) {
        if (materialsOrderID == null || materialsOrderID.trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID de la commande ne peut pas être vide.");
        }
        this.materialsOrderID = materialsOrderID;
    }

    public void setProductQuantities(Map<String, Integer> quantities) {
        if (quantities == null || quantities.isEmpty()) {
            throw new IllegalArgumentException("La carte des quantités ne peut pas être vide.");
        }

        for (Map.Entry<String, Integer> entry : quantities.entrySet()) {
            if (entry.getValue() <= 0) {
                throw new IllegalArgumentException("La quantité pour le produit ID " + entry.getKey() + " doit être positive.");
            }
        }
        this.productQuantities = quantities;
    }

    public void setTeamID(String teamID) {
        if (teamID == null || teamID.trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID de l'équipe ne peut pas être vide.");
        }
        this.teamID = teamID;
    }
}
