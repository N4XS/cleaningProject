package models;

import java.sql.Timestamp;

public class CleaningService {
    private String cleaningServiceID;
    private Timestamp dateTimeStartPrest;
    private int duration;
    private String contractID;
    private String teamID;

    public CleaningService(Timestamp dateTimeStartPrest, int duration, String contractID, String teamID){
        setDateTimeStartPrest(dateTimeStartPrest);
        setDuration(duration);
        setContractID(contractID);
        setTeamID(teamID);
    }

    public String getCleaningServiceID() {
        return cleaningServiceID;
    }

    public Timestamp getDateTimeStartPrest() {
        return dateTimeStartPrest;
    }

    public int getDuration() {
        return duration;
    }

    public String getContractID() {
        return contractID;
    }

    public String getTeamID() {
        return teamID;
    }

    public void setCleaningServiceID(String cleaningServiceID) {
        if (cleaningServiceID != null && !cleaningServiceID.trim().isEmpty()) {
            this.cleaningServiceID = cleaningServiceID;
        } else {
            throw new IllegalArgumentException("Cleaning Service ID cannot be null or empty.");
        }
    }

    public void setDateTimeStartPrest(Timestamp dateTimeStartPrest) {
        if (dateTimeStartPrest != null) {
            this.dateTimeStartPrest = dateTimeStartPrest;
        } else {
            throw new IllegalArgumentException("Start Date/Time cannot be null.");
        }
    }

    public void setDuration(int duration) {
        if (duration > 0) {
            this.duration = duration;
        } else {
            throw new IllegalArgumentException("Duration must be positive.");
        }
    }

    public void setContractID(String contractID) {
        if (contractID == null || contractID.trim().isEmpty()) {
            throw new IllegalArgumentException("Contract ID cannot be null or empty.");
        }
        this.contractID = contractID;
    }

    public void setTeamID(String teamID) {
        if (teamID == null || teamID.trim().isEmpty()) {
            throw new IllegalArgumentException("Team ID cannot be null or empty.");
        }
        this.teamID = teamID;
    }
}
