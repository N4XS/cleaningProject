package models;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

public class StaffMember {
    private String numONSS;
    private LocalDate birthday;
    private String firstName;
    private String lastName;
    private String email;
    private String streetName;
    private Integer streetNumber;
    private String boxNumber;
    private String cellphoneNumber;
    private LocalDate startDate;
    private Boolean isCleaner;
    private String graduate;
    private City city;

    private static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private static final String PHONE_REGEX = "^\\d{10}$";
    private static final int MINIMUM_AGE = 18;

    public StaffMember(String numONSS, LocalDate birthday, String firstName, String lastName, String email,
                       String streetName, Integer streetNumber, String boxNumber,
                       String cellphoneNumber, LocalDate startDate, Boolean isCleaner,
                       String graduate, City city) {
        setNumONSS(numONSS);
        setBirthday(birthday);
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setStreetName(streetName);
        setStreetNumber(streetNumber);
        setBoxNumber(boxNumber);
        setCellphoneNumber(cellphoneNumber);
        setStartDate(startDate);
        setIsCleaner(isCleaner);
        setGraduate(graduate);
        setCity(city);
    }

    public void setNumONSS(String numONSS) {
        if (!numONSS.matches("^\\d{8}$")) {
            throw new IllegalArgumentException("Le numéro ONSS doit contenir exactement 8 chiffres.");
        }
        this.numONSS = numONSS;
    }

    public void setBirthday(LocalDate birthday) {
        if (Period.between(birthday, LocalDate.now()).getYears() < MINIMUM_AGE) {
            throw new IllegalArgumentException("Le membre du personnel doit avoir au moins 18 ans.");
        }
        this.birthday = birthday;
    }

    public void setFirstName(String firstName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom ne peut pas être vide.");
        }
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide.");
        }
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        if (!Pattern.matches(EMAIL_REGEX, email)) {
            throw new IllegalArgumentException("Format de l'adresse email invalide.");
        }
        this.email = email;
    }


    public void setStreetName(String streetName) {
        if (streetName == null || streetName.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de la rue ne peut pas être vide.");
        }
        this.streetName = streetName;
    }

    public void setStreetNumber(Integer streetNumber) {
        if (streetNumber == null || streetNumber <= 0) {
            throw new IllegalArgumentException("Le numéro de la rue doit être un entier positif.");
        }
        this.streetNumber = streetNumber;
    }

    public void setBoxNumber(String boxNumber) {
        this.boxNumber = boxNumber;
    }

    public void setCellphoneNumber(String cellphoneNumber) {
        if (cellphoneNumber != null && !Pattern.matches(PHONE_REGEX, cellphoneNumber)) {
            throw new IllegalArgumentException("Format du numéro de téléphone portable invalide.");
        }
        this.cellphoneNumber = cellphoneNumber;
    }

    public void setStartDate(LocalDate startDate) {
        if (startDate == null || startDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La date de début ne peut pas être dans le futur.");
        }
        this.startDate = startDate;
    }

    public void setIsCleaner(Boolean isCleaner) {
        this.isCleaner = isCleaner;
    }

    public void setGraduate(String graduate) {
        this.graduate = graduate;
    }

    public void setCity(City city) {
        if (city == null) {
            throw new IllegalArgumentException("La ville ne peut pas être nulle.");
        }
        this.city = city;
    }

    public String getNumONSS() {
        return numONSS;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getStreetName() {
        return streetName;
    }

    public Integer getStreetNumber() {
        return streetNumber;
    }

    public String getBoxNumber() {
        return boxNumber;
    }

    public String getCellphoneNumber() {
        return cellphoneNumber;
    }

    public LocalDate getStartDate() {
        return startDate;
    }


    public Boolean getIsCleaner() {
        return isCleaner;
    }

    public String getGraduate() {
        return graduate;
    }

    public City getCity() {
        return city;
    }

}
