package models;

import exceptions.insert.IllegalArgumentException;

import java.time.LocalDate;

public class Absence {
    private String absenceID;
    private String justification;
    private Boolean isUnderCertificate;
    private LocalDate startDate;
    private LocalDate endDate;
    private String absentID;

    public Absence(String justification, LocalDate startDate, LocalDate endDate, Boolean isUnderCertificate, String absentID) throws IllegalArgumentException {
        setJustification(justification);
        setIsUnderCertificate(isUnderCertificate);
        setStartDate(startDate);
        setEndDate(endDate);
        setAbsentID(absentID);
    }

    public String getAbsenceID() {
        return absenceID;
    }

    public String getJustification() {
        return justification;
    }

    public Boolean getIsUnderCertificate() {
        return isUnderCertificate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getAbsentID() {
        return absentID;
    }

    @Override
    public String toString() {
        return "Absence ID: " + absenceID + ", Justification: " + (justification != null ? justification : "N/A") +
                ", Start Date: " + startDate + ", End Date: " + (endDate != null ? endDate : "N/A") +
                ", AbsentID: " + absentID;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }


    public void setEndDate(LocalDate endDate) throws IllegalArgumentException {
        if (endDate != null && endDate.isBefore(this.startDate)) {
            throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début.");
        }
        this.endDate = endDate;
    }

    public void setStartDate(LocalDate startDate) throws IllegalArgumentException {
        if (startDate == null || startDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La date de début ne peut pas être dans le futur ou nulle.");
        }
        this.startDate = startDate;
    }

    public void setAbsenceID(String absenceID) throws IllegalArgumentException {
        if (absenceID == null || absenceID.trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID d'absence ne peut pas être vide.");
        }
        this.absenceID = absenceID;
    }

    public void setAbsentID(String absentID) throws IllegalArgumentException {
        if (absentID == null || absentID.trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID de l'absent ne peut pas être vide.");
        }
        this.absentID = absentID;
    }



    public void setIsUnderCertificate(Boolean isUnderCertificate) {
        this.isUnderCertificate = isUnderCertificate;
    }
}
