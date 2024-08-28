package models;

import java.time.LocalDate;

public class Contract {
    public enum DurationType {
        ONE_DAY("1 jour"),
        ONE_WEEK("1 semaine"),
        TWO_WEEKS("2 semaines"),
        ONE_MONTH("1 mois"),
        TWO_MONTHS("2 mois"),
        THREE_MONTHS("3 mois"),
        SIX_MONTHS("6 mois"),
        NINE_MONTHS("9 mois"),
        ONE_YEAR("1 an"),
        UNDETERMINED("indéterminée");

        private final String label;

        DurationType(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        public static DurationType fromLabel(String label) {
            for (DurationType duration : values()) {
                if (duration.getLabel().equals(label)) {
                    return duration;
                }
            }
            throw new IllegalArgumentException("Durée de contrat invalide");
        }
    }

    private String contractID;
    private int nbHoursPerWeek;
    private LocalDate startDate;
    private LocalDate endDate;
    private String planningDesc;
    private String siteID;
    private String clientID;
    private DurationType durationType;

    public Contract(String clientID, String siteID, int nbHoursPerWeek, LocalDate startDate, DurationType duration, String planningDesc) {
        setSiteID(siteID);
        setClientID(clientID);
        setNbHoursPerWeek(nbHoursPerWeek);
        setStartDate(startDate);
        setDurationType(duration);
        setEndDate(calculateEndDate(startDate, duration));
        setPlanning(planningDesc);
    }

    private LocalDate calculateEndDate(LocalDate startDate, DurationType duration) {
        switch (duration) {
            case ONE_DAY:
                return startDate.plusDays(1);
            case ONE_WEEK:
                return startDate.plusWeeks(1);
            case TWO_WEEKS:
                return startDate.plusWeeks(2);
            case ONE_MONTH:
                return startDate.plusMonths(1);
            case TWO_MONTHS:
                return startDate.plusMonths(2);
            case THREE_MONTHS:
                return startDate.plusMonths(3);
            case SIX_MONTHS:
                return startDate.plusMonths(6);
            case NINE_MONTHS:
                return startDate.plusMonths(9);
            case ONE_YEAR:
                return startDate.plusYears(1);
            case UNDETERMINED:
                return null;
            default:
                throw new IllegalArgumentException("Durée de contrat invalide");
        }
    }

    public String getContractID() {
        return contractID;
    }

    public int getNbHoursPerWeek() {
        return nbHoursPerWeek;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getPlanning() {
        return planningDesc;
    }

    public String getSiteID() {
        return siteID;
    }

    public String getClientID() {
        return clientID;
    }

    public DurationType getDurationType() {
        return durationType;
    }

    public void setContractID(String contractID) {
        if (contractID == null || contractID.trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID de contrat ne peut pas être nul ou vide.");
        }
        this.contractID = contractID;
    }

    public void setNbHoursPerWeek(int nbHoursPerWeek) {
        if (nbHoursPerWeek <= 0) {
            throw new IllegalArgumentException("Le nombre d'heures par semaine doit être supérieur à zéro.");
        }
        this.nbHoursPerWeek = nbHoursPerWeek;
    }

    public void setStartDate(LocalDate startDate) throws IllegalArgumentException {
        if (startDate == null) {
            throw new IllegalArgumentException("La date de début ne peut pas être nulle.");
        }
        this.startDate = startDate;
    }


    public void setEndDate(LocalDate endDate) {
        if (endDate != null && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début.");
        }
        this.endDate = endDate;
    }

    public void setPlanning(String planningDesc) {
        if (planningDesc == null || planningDesc.trim().isEmpty()) {
            throw new IllegalArgumentException("La description du planning ne peut pas être nulle ou vide.");
        }
        this.planningDesc = planningDesc;
    }

    public void setSiteID(String siteID) {
        if (siteID == null || siteID.trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID du site ne peut pas être nul ou vide.");
        }
        this.siteID = siteID;
    }

    public void setClientID(String clientID) {
        if (clientID == null || clientID.trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID du client ne peut pas être nul ou vide.");
        }
        this.clientID = clientID;
    }

    public void setDurationType(DurationType durationType) {
        if (durationType == null) {
            throw new IllegalArgumentException("La durée ne peut pas être nulle.");
        }
        this.durationType = durationType;
    }
}
