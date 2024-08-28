package models;

import java.util.ArrayList;
import java.util.List;

public class Team {
    private String teamID;
    private List<String> staffMemberIDs = new ArrayList<>();
    private String leaderID;

    public Team(List<String> staffMemberIDs){
        addMembers(staffMemberIDs);
    }

    public void addMembers(List<String> staffMemberIDs) {
        if (staffMemberIDs == null || staffMemberIDs.isEmpty()) {
            throw new IllegalArgumentException("La liste des IDs des membres du personnel ne peut pas être nulle ou vide.");
        }

        for (String memberID : staffMemberIDs) {
            addMember(memberID);
        }

        if (!this.staffMemberIDs.isEmpty()) {
            setLeader(this.staffMemberIDs.get(0));
        }
    }

    public List<String> getStaffMemberIDs() {
        return staffMemberIDs;
    }

    public String getLeaderID() {
        return leaderID;
    }

    public String getTeamID() {
        return teamID;
    }

    private void setLeader(String leaderID) {
        this.leaderID = leaderID;
        if (!this.staffMemberIDs.contains(leaderID)) {
            this.staffMemberIDs.add(0, leaderID);
        }
    }

    public void addMember(String staffMemberID) {
        if (staffMemberID != null && !staffMemberIDs.contains(staffMemberID)) {
            this.staffMemberIDs.add(staffMemberID);
            if (this.staffMemberIDs.size() == 1) {
                setLeader(staffMemberID);
            }
        }
    }

    public void setTeamID(String teamID) {
        this.teamID = teamID;
    }
}
