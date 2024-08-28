package models;

import java.util.regex.Pattern;

public class Client {
    private String clientID;
    private String name;
    private String firstName;
    private String email;
    private String gsm;
    private String streetName;
    private int streetNumber;
    private String boxNumber;
    private City city;

    public Client(String name, String firstName, String email, String gsm, String streetName, int streetNumber, String boxNumber, int postalCode, String localityName) {
        setName(name);
        setFirstName(firstName);
        setEmail(email);
        setGsm(gsm);
        setStreetName(streetName);
        setStreetNumber(streetNumber);
        setBoxNumber(boxNumber);
        setCity(postalCode, localityName);
    }
    public String getClientID() {
        return clientID;
    }

    public String getName() {
        return name;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getEmail() {
        return email;
    }

    public String getGsm() {
        return gsm;
    }

    public String getStreetName() {
        return streetName;
    }

    public int getStreetNumber() {
        return streetNumber;
    }

    public String getBoxNumber() {
        return boxNumber;
    }

    public City getCity() {
        return city;
    }

    public void setClientID(String clientID) {
        if (clientID != null && clientID.trim().length() == 8) {
            this.clientID = clientID;
        } else {
            throw new IllegalArgumentException("L'ID client doit contenir 8 caractères.");
        }
    }

    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Le nom ne peut pas être vide.");
        }
    }

    public void setFirstName(String firstName) {
        if (firstName != null && !firstName.trim().isEmpty()) {
            this.firstName = firstName;
        } else {
            throw new IllegalArgumentException("Le prénom ne peut pas être vide.");
        }
    }

    public void setEmail(String email) {
        String emailPattern = "^[\\w-\\.]+@[\\w-]+\\.[a-z]{2,4}$";
        if (email != null && Pattern.matches(emailPattern, email)) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("L'email doit être valide.");
        }
    }

    public void setGsm(String gsm) {
        if (gsm != null && !gsm.trim().isEmpty()) {
            this.gsm = gsm;
        } else {
            throw new IllegalArgumentException("Le numéro de téléphone ne peut pas être vide.");
        }
    }

    public void setStreetName(String streetName) {
        if (streetName != null && !streetName.trim().isEmpty()) {
            this.streetName = streetName;
        } else {
            throw new IllegalArgumentException("Le nom de rue ne peut pas être vide.");
        }
    }

    public void setStreetNumber(int streetNumber) {
        if (streetNumber > 0) {
            this.streetNumber = streetNumber;
        } else {
            throw new IllegalArgumentException("Le numéro de rue doit être positif.");
        }
    }

    public void setBoxNumber(String boxNumber) {
        this.boxNumber = boxNumber;
    }

    public void setCity(int postalCode, String locality) {
        this.city = new City(postalCode, locality);
    }
}
