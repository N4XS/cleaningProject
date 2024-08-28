package models;

import java.time.LocalDate;

public class Replacement {
        private String replacementID;
        private LocalDate startDate;
        private LocalDate endDate;
        private String staffReplacingID;
        private String absenceReplacedID;

        public Replacement(LocalDate startDate, LocalDate endDate, String staffReplacingID, String absenceReplacedID) {
            this.setStartDate(startDate);
            this.setEndDate(endDate);
            this.staffReplacingID = staffReplacingID;
            this.absenceReplacedID = absenceReplacedID;
        }

    public LocalDate getEndDate() {
        return endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public String getAbsenceReplacedID() {
        return absenceReplacedID;
    }

    public String getReplacementID() {
        return replacementID;
    }

    public String getStaffReplacingID() {
        return staffReplacingID;
    }

    public void setStartDate(LocalDate startDate) {
        if (startDate == null) {
            throw new IllegalArgumentException("La date de début ne peut pas être nulle.");
        }
        if (endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("La date de début ne peut pas être après la date de fin.");
        }
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        if (endDate != null && startDate != null && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("La date de fin ne peut pas être avant la date de début.");
        }
        this.endDate = endDate;
    }

    public void setAbsenceReplacedID(String absenceReplacedID) {
        if (absenceReplacedID == null || absenceReplacedID.trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID de l'absence remplacée ne peut pas être nul ou vide.");
        }
        this.absenceReplacedID = absenceReplacedID;
    }

    public void setReplacementID(String replacementID) {
        if (replacementID == null || replacementID.trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID de remplacement ne peut pas être nul ou vide.");
        }
        this.replacementID = replacementID;
    }

    public void setStaffReplacingID(String staffReplacingID) {
        if (staffReplacingID == null || staffReplacingID.trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID du membre du personnel remplaçant ne peut pas être nul ou vide.");
        }
        this.staffReplacingID = staffReplacingID;
    }
}
