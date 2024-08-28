package dataAccess;

import exceptions.connexion.EndConnectionException;
import exceptions.connexion.SingletonConnexionException;
import exceptions.delete.*;
import exceptions.insert.*;
import exceptions.insert.IllegalArgumentException;
import exceptions.list.*;
import models.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface DAOAccess {
    void endConnection() throws EndConnectionException;

    void insertAbsence(Absence absence) throws IllegalArgumentException, SingletonConnexionException;

    void insertCity(City city) throws InsertCityException;

    void insertCleaningService(CleaningService cleaningService) throws InsertCleaningServiceException, SingletonConnexionException;

    void insertClient(Client client) throws InsertClientException, SQLException, SingletonConnexionException;

    void insertContract(Contract contract) throws InsertContractException, SingletonConnexionException;

    void insertMachinery(Machinery machinery) throws InsertMachineryException, SingletonConnexionException;

    void insertMaterialsOrder(MaterialsOrder materialsOrder) throws InsertMaterialsOrderException, SingletonConnexionException;

    void insertProduct(Product product) throws InsertProductException, SingletonConnexionException;

    void insertReplacement(Replacement replacement) throws InsertReplacementException, SingletonConnexionException;

    void insertSite(Site site) throws InsertSiteException, SingletonConnexionException;

    void insertStaffMember(StaffMember staffMember) throws InsertStaffMemberException;

    void insertTeam(Team team) throws InsertTeamException, SingletonConnexionException;

    void insertWarning(Warning warning) throws InsertWarningException, SingletonConnexionException;

    void updateAbsence(Absence absence) throws SQLException;

    void updateCity(City city) throws SQLException;

    void updateCleaningService(CleaningService cleaningService) throws SQLException;

    void updateClient(Client client) throws SQLException;

    void updateContract(Contract contract) throws SQLException;

    void updateMachinery(Machinery machinery) throws SQLException;

    void updateMaterialsOrder(MaterialsOrder materialsOrder) throws SQLException;

    void updateProduct(Product product) throws SQLException;

    void updateReplacement(Replacement replacement) throws SQLException;

    void updateSite(Site site) throws SQLException;

    void updateStaffMember(StaffMember staffMember) throws SQLException;

    void updateTeam(Team team) throws SQLException;

    void updateWarning(Warning warning) throws SQLException;

     void deleteAbsence(String absenceID) throws DeleteAbsenceException;

     void deleteCity(int postalCode, String locality) throws DeleteCityException;

     void deleteCleaningService(String cleaningServiceID) throws DeleteCleaningServiceException;

     void deleteClient(String clientID) throws DeleteClientException;

     void deleteContract(String contractID) throws DeleteContractException;

     void deleteMachinery(String machineryID) throws DeleteMachineryException;

     void deleteMaterialsOrder(String materialsOrderID) throws DeleteMaterialsOrderException;

     void deleteProduct(String productID) throws DeleteProductException;

     void deleteReplacement(String replacementID) throws DeleteReplacementException;

     void deleteSite(String siteID) throws DeleteSiteException;

     void deleteStaffMember(String numONSS) throws DeleteStaffMemberException;

     void deleteTeam(String teamID) throws DeleteTeamException;

     void deleteWarning(String warningID) throws DeleteWarningException;


    List<Absence> listAbsences() throws ListAbsencesException;
    List<City> listCities() throws ListCitiesException;
    List<CleaningService> listCleaningServices() throws ListCleaningServicesException;
    List<Client> listClients() throws ListClientsException;
    List<Contract> listContracts() throws ListContractsException;
    List<Machinery> listMachineries() throws ListMachineriesException;
    List<MaterialsOrder> listMaterialsOrders() throws ListMaterialsOrdersException;
    List<Product> listProducts() throws ListProductsException;
    List<Replacement> listReplacements() throws ListReplacementsException;
    List<Site> listSites() throws ListSitesException;
    List<StaffMember> listStaffMembers() throws ListStaffMembersException;
    List<Team> listTeams() throws ListTeamsException;
    List<Warning> listWarnings() throws ListWarningsException;

     List<Absence> getAbsencesByAbsentID(String absentID) throws SQLException;
     Absence getAbsenceByID(String absenceID) throws SQLException, IllegalArgumentException;
     City getCityByLocality(String locality) throws SQLException;
     List<CleaningService> getCleaningServicesByTeam(String teamID) throws SQLException;
     CleaningService getCleaningServiceByID(String cleaningServiceID) throws SQLException;
     List<Client> getClientsByName(String name) throws SQLException;
     Client getClientByID(String clientID) throws SQLException;
     Contract getContractByID(String contractID) throws SQLException;
     Machinery getMachineryByName(String name) throws SQLException;
     Machinery getMachineryById(String machineryID) throws SQLException;
     List<MaterialsOrder> getMaterialsOrderByTeam(String teamID) throws SQLException;
     MaterialsOrder getMaterialsOrderByID(String materialsOrderID) throws SQLException;
     Product getProductByName(String productName) throws SQLException;
     Product getProductByID(String productID) throws SQLException;
     List<Replacement> getReplacementByStaffReplacing(String staffReplacingID) throws SQLException;
     Replacement getReplacementById(String replacementID) throws SQLException;
     List<Site> getSiteByName(String siteName) throws SQLException;
     Site getSiteByID(String siteID) throws SQLException;
     List<Site> getSiteByCodePostal(int postalCode) throws SQLException;
     List<StaffMember> getStaffMembersByName(String name) throws SQLException;
     StaffMember getStaffMemberByONSS(String numONSS) throws SQLException;
     Team getTeamByTeamID(String teamID) throws SQLException;
     List<Team> getTeamsByMemberONSS(String numONSS) throws SQLException;
     List<Warning> getWarningByONSS(String staffMemberWarnedID) throws SQLException;
     Warning getWarningByID(String warningID) throws SQLException;
    Map<String, Integer> getMaterialsOrder_prod(String materialsOrderID) throws SQLException;


    boolean cityExists(int postalCode, String locality) throws SQLException;
}

