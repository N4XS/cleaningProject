package models;

import java.time.LocalDate;

public class Warning {
    private String warningID;
    private String description;
    private LocalDate dateFault;
    private Boolean isSeriousFault;
    private String staffMemberWarnedID;

    public Warning(String description, LocalDate dateFault, Boolean isSeriousFault, String attributedStaffMemberID)  {
        setDescription(description);
        setDateFault(dateFault);
        setSeriousFault(isSeriousFault);
        setStaffMemberWarnedID(attributedStaffMemberID);
    }

    public Boolean getSeriousFault() {
        return isSeriousFault;
    }

    public String getStaffMemberWarnedID() {
        return staffMemberWarnedID;
    }

    public LocalDate getDateFault() {
        return dateFault;
    }

    public String getDescription() {
        return description;
    }

    public String getWarningID() {
        return warningID;
    }

    public void setDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("La description ne peut pas être vide.");
        }
        this.description = description;
    }

    public void setDateFault(LocalDate dateFault) {
        if (dateFault == null) {
            throw new IllegalArgumentException("La date ne peut pas être nulle.");
        }
        if (dateFault.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La date ne peut pas être dans le futur.");
        }
        this.dateFault = dateFault;
    }

    public void setStaffMemberWarnedID(String staffMemberWarnedID) {
        if (staffMemberWarnedID == null || staffMemberWarnedID.trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID du membre du personnel ne peut pas être vide.");
        }
        this.staffMemberWarnedID = staffMemberWarnedID;
    }

    public void setSeriousFault(Boolean seriousFault) {
        if (seriousFault == null) {
            throw new IllegalArgumentException("Le statut de faute grave ne peut pas être nul.");
        }
        this.isSeriousFault = seriousFault;
    }

    public void setWarningID(String warningID) {
        if (warningID == null || warningID.trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID de l'avertissement ne peut pas être vide.");
        }
        this.warningID = warningID;
    }
}
