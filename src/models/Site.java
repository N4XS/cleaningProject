package models;

public class Site {
    private String siteID;
    private String siteName;
    private String streetName;
    private int streetNumber;
    private String boxHouse;
    private String description;
    private City city;
    private String clientOwnerID;

    public Site(String siteName, String streetName, int streetNumber, String boxHouse, String description, int postalCode, String locality, String clientOwnerID)  {
        setSiteName(siteName);
        setStreetName(streetName);
        setStreetNumber(streetNumber);
        setBoxHouse(boxHouse);
        setDescription(description);
        setCity(postalCode, locality);
        setClientOwnerID(clientOwnerID);
    }

    public String getSiteID() {
        return siteID;
    }

    public String getSiteName() {
        return siteName;
    }

    public String getStreetName() {
        return streetName;
    }

    public int getStreetNumber() {
        return streetNumber;
    }

    public String getBoxHouse() {
        return boxHouse;
    }

    public String getDescription() {
        return description;
    }

    public String getClientOwnerID() {
        return clientOwnerID;
    }

    public City getCity() {
        return city;
    }

    public void setSiteID(String siteID) {
        if (siteID != null && siteID.length() == 8) {
            this.siteID = siteID;
        } else {
            throw new IllegalArgumentException("L'ID du site doit avoir une longueur de 8 caractères.");
        }
    }

    public void setSiteName(String siteName) {
        if (siteName != null && !siteName.trim().isEmpty()) {
            this.siteName = siteName;
        } else {
            throw new IllegalArgumentException("Le nom du site ne peut pas être vide.");
        }
    }

    public void setStreetName(String streetName) {
        if (streetName != null && !streetName.trim().isEmpty()) {
            this.streetName = streetName;
        } else {
            throw new IllegalArgumentException("Le nom de la rue ne peut pas être vide.");
        }
    }

    public void setStreetNumber(int streetNumber) {
        if (streetNumber > 0) {
            this.streetNumber = streetNumber;
        } else {
            throw new IllegalArgumentException("Le numéro de rue doit être positif.");
        }
    }

    public void setBoxHouse(String boxHouse) {
        this.boxHouse = boxHouse;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCity(int postalCode, String locality) {
        this.city = new City(postalCode, locality);
    }

    public void setClientOwnerID(String clientOwnerID) {
        if (clientOwnerID != null && clientOwnerID.length() == 8) {
            this.clientOwnerID = clientOwnerID;
        } else {
            throw new IllegalArgumentException("L'ID du client propriétaire doit avoir une longueur de 8 caractères.");
        }
    }
}
