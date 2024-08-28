package models;

public class Machinery {
    private String machineryID;
    private String name;
    private Boolean isAvailable;
    private String siteID;

    public Machinery(String name, Boolean isAvailable, String siteID) {
        this.setName(name);
        this.setAvailable(isAvailable);
        this.setSiteID(siteID);
    }

    public String getName() {
        return name;
    }

    public String getSiteID() {
        return siteID;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public String getMachineryID() {
        return machineryID;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de la machine ne peut pas être nul ou vide.");
        }
        this.name = name;
    }

    public void setAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public void setMachineryID(String machineryID) {
        if (machineryID == null || machineryID.trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID de la machine ne peut pas être nul ou vide.");
        }
        this.machineryID = machineryID;
    }

    public void setSiteID(String siteID) {
        if (siteID == null || siteID.trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID du site ne peut pas être nul ou vide.");
        }
        this.siteID = siteID;
    }
}
