package models;

public class City {
    private int postalCode;
    private String locality;

    public City(int postalCode, String locality) {
        this.postalCode = postalCode;
        this.locality = locality;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public String getLocality() {
        return locality;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    @Override
    public String toString() {
        return locality + " (" + postalCode + ")";
    }
}
