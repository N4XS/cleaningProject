package business;

import dataAccess.DAOAccess;
import dataAccess.DBAccess;
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

public class Manager implements DAOAccess {

    private final DAOAccess dao;

    public Manager() throws SingletonConnexionException, SQLException {
        this.dao = new DBAccess();
    }

    @Override
    public void endConnection() throws EndConnectionException {
        dao.endConnection();
    }

    @Override
    public void insertAbsence(Absence absence) throws IllegalArgumentException, SingletonConnexionException {
        dao.insertAbsence(absence);
    }

    @Override
    public void insertCity(City city) throws InsertCityException {
        dao.insertCity(city);
    }

    @Override
    public void insertCleaningService(CleaningService cleaningService) throws InsertCleaningServiceException, SingletonConnexionException {
        dao.insertCleaningService(cleaningService);
    }

    @Override
    public void insertClient(Client client) throws InsertClientException, SQLException, SingletonConnexionException {
        dao.insertClient(client);
    }

    @Override
    public void insertContract(Contract contract) throws InsertContractException, SingletonConnexionException {
        dao.insertContract(contract);
    }

    @Override
    public void insertMachinery(Machinery machinery) throws InsertMachineryException, SingletonConnexionException {
        dao.insertMachinery(machinery);
    }

    @Override
    public void insertMaterialsOrder(MaterialsOrder materialsOrder) throws InsertMaterialsOrderException, SingletonConnexionException {
        dao.insertMaterialsOrder(materialsOrder);
    }

    @Override
    public void insertProduct(Product product) throws InsertProductException, SingletonConnexionException {
        dao.insertProduct(product);
    }

    @Override
    public void insertReplacement(Replacement replacement) throws InsertReplacementException, SingletonConnexionException {
        dao.insertReplacement(replacement);
    }

    @Override
    public void insertSite(Site site) throws InsertSiteException, SingletonConnexionException {
        dao.insertSite(site);
    }

    @Override
    public void insertStaffMember(StaffMember staffMember) throws InsertStaffMemberException {
        dao.insertStaffMember(staffMember);
    }

    @Override
    public void insertTeam(Team team) throws InsertTeamException, SingletonConnexionException {
        dao.insertTeam(team);
    }

    @Override
    public void insertWarning(Warning warning) throws InsertWarningException, SingletonConnexionException {
        dao.insertWarning(warning);
    }

    public void updateAbsence(Absence absence) throws SQLException {
        dao.updateAbsence(absence);
    }

    public void updateCity(City city) throws SQLException {
        dao.updateCity(city);
    }

    public void updateCleaningService(CleaningService cleaningService) throws SQLException {
        dao.updateCleaningService(cleaningService);
    }

    public void updateClient(Client client) throws SQLException {
        dao.updateClient(client);
    }

    public void updateContract(Contract contract) throws SQLException {
        dao.updateContract(contract);
    }

    public void updateMachinery(Machinery machinery) throws SQLException {
        dao.updateMachinery(machinery);
    }

    public void updateMaterialsOrder(MaterialsOrder materialsOrder) throws SQLException {
        dao.updateMaterialsOrder(materialsOrder);
    }

    public void updateProduct(Product product) throws SQLException {
        dao.updateProduct(product);
    }

    public void updateReplacement(Replacement replacement) throws SQLException {
        dao.updateReplacement(replacement);
    }

    public void updateSite(Site site) throws SQLException {
        dao.updateSite(site);
    }

    public void updateStaffMember(StaffMember staffMember) throws SQLException {
        dao.updateStaffMember(staffMember);
    }

    public void updateTeam(Team team) throws SQLException {
        dao.updateTeam(team);
    }

    public void updateWarning(Warning warning) throws SQLException {
        dao.updateWarning(warning);
    }

    @Override
    public List<Absence> listAbsences() throws ListAbsencesException {
        return dao.listAbsences();
    }

    @Override
    public List<City> listCities() throws ListCitiesException {
        return dao.listCities();
    }

    @Override
    public List<CleaningService> listCleaningServices() throws ListCleaningServicesException {
        return dao.listCleaningServices();
    }

    @Override
    public List<Client> listClients() throws ListClientsException {
        return dao.listClients();
    }

    @Override
    public List<Contract> listContracts() throws ListContractsException {
        return dao.listContracts();
    }

    @Override
    public List<Machinery> listMachineries() throws ListMachineriesException {
        return dao.listMachineries();
    }

    @Override
    public List<MaterialsOrder> listMaterialsOrders() throws ListMaterialsOrdersException {
        return dao.listMaterialsOrders();
    }

    @Override
    public List<Product> listProducts() throws ListProductsException {
        return dao.listProducts();
    }

    @Override
    public List<Replacement> listReplacements() throws ListReplacementsException {
        return dao.listReplacements();
    }

    @Override
    public List<Site> listSites() throws ListSitesException {
        return dao.listSites();
    }

    @Override
    public List<StaffMember> listStaffMembers() throws ListStaffMembersException {
        return dao.listStaffMembers();
    }

    @Override
    public List<Team> listTeams() throws ListTeamsException {
        return dao.listTeams();
    }

    @Override
    public List<Warning> listWarnings() throws ListWarningsException {
        return dao.listWarnings();
    }

    public void deleteAbsence(String absenceID) throws DeleteAbsenceException {
        dao.deleteAbsence(absenceID);
    }

    public void deleteCity(int postalCode, String locality) throws DeleteCityException {
        dao.deleteCity(postalCode, locality);
    }

    public void deleteCleaningService(String cleaningServiceID) throws DeleteCleaningServiceException {
        dao.deleteCleaningService(cleaningServiceID);
    }

    public void deleteClient(String clientID) throws DeleteClientException {
        dao.deleteClient(clientID);
    }

    public void deleteContract(String contractID) throws DeleteContractException {
        dao.deleteContract(contractID);
    }

    public void deleteMachinery(String machineryID) throws DeleteMachineryException {
        dao.deleteMachinery(machineryID);
    }

    public void deleteMaterialsOrder(String materialsOrderID) throws DeleteMaterialsOrderException {
        dao.deleteMaterialsOrder(materialsOrderID);
    }

    public void deleteProduct(String productID) throws DeleteProductException {
        dao.deleteProduct(productID);
    }

    public void deleteReplacement(String replacementID) throws DeleteReplacementException {
        dao.deleteReplacement(replacementID);
    }

    public void deleteSite(String siteID) throws DeleteSiteException {
        dao.deleteSite(siteID);
    }

    public void deleteStaffMember(String numONSS) throws DeleteStaffMemberException {
        dao.deleteStaffMember(numONSS);
    }

    public void deleteTeam(String teamID) throws DeleteTeamException {
        dao.deleteTeam(teamID);
    }

    public void deleteWarning(String warningID) throws DeleteWarningException {
        dao.deleteWarning(warningID);
    }

    public List<Absence> getAbsencesByAbsentID(String absentID) throws SQLException {
        return dao.getAbsencesByAbsentID(absentID);
    }

    public Absence getAbsenceByID(String absenceID) throws SQLException, IllegalArgumentException {
        return dao.getAbsenceByID(absenceID);
    }

    public City getCityByLocality(String locality) throws SQLException {
        return dao.getCityByLocality(locality);
    }

    public List<CleaningService> getCleaningServicesByTeam(String teamID) throws SQLException {
        return dao.getCleaningServicesByTeam(teamID);
    }

    public CleaningService getCleaningServiceByID(String cleaningServiceID) throws SQLException {
        return dao.getCleaningServiceByID(cleaningServiceID);
    }

    public List<Client> getClientsByName(String name) throws SQLException {
        return dao.getClientsByName(name);
    }

    public Client getClientByID(String clientID) throws SQLException {
        return dao.getClientByID(clientID);
    }

    public Contract getContractByID(String contractID) throws SQLException {
        return dao.getContractByID(contractID);
    }

    public Machinery getMachineryByName(String name) throws SQLException {
        return dao.getMachineryByName(name);
    }

    public Machinery getMachineryById(String machineryID) throws SQLException {
        return dao.getMachineryById(machineryID);
    }

    public List<MaterialsOrder> getMaterialsOrderByTeam(String teamID) throws SQLException {
        return dao.getMaterialsOrderByTeam(teamID);
    }

    public MaterialsOrder getMaterialsOrderByID(String materialsOrderID) throws SQLException {
        return dao.getMaterialsOrderByID(materialsOrderID);
    }

    public Map<String, Integer> getMaterialsOrder_prod(String materialsOrderID) throws SQLException {
        return dao.getMaterialsOrder_prod(materialsOrderID);
    }

    public Product getProductByName(String productName) throws SQLException {
        return dao.getProductByName(productName);
    }

    public Product getProductByID(String productID) throws SQLException {
        return dao.getProductByID(productID);
    }

    public List<Replacement> getReplacementByStaffReplacing(String staffReplacingID) throws SQLException {
        return dao.getReplacementByStaffReplacing(staffReplacingID);
    }

    public Replacement getReplacementById(String replacementID) throws SQLException {
        return dao.getReplacementById(replacementID);
    }

    public List<Site> getSiteByName(String siteName) throws SQLException {
        return dao.getSiteByName(siteName);
    }

    public Site getSiteByID(String siteID) throws SQLException {
        return dao.getSiteByID(siteID);
    }

    public List<Site> getSiteByCodePostal(int postalCode) throws SQLException {
        return dao.getSiteByCodePostal(postalCode);
    }

    public List<StaffMember> getStaffMembersByName(String name) throws SQLException {
        return dao.getStaffMembersByName(name);
    }

    public StaffMember getStaffMemberByONSS(String numONSS) throws SQLException {
        return dao.getStaffMemberByONSS(numONSS);
    }

    public Team getTeamByTeamID(String teamID) throws SQLException {
        return dao.getTeamByTeamID(teamID);
    }

    public List<Team> getTeamsByMemberONSS(String numONSS) throws SQLException {
        return dao.getTeamsByMemberONSS(numONSS);
    }

    public List<Warning> getWarningByONSS(String staffMemberWarnedID) throws SQLException {
        return dao.getWarningByONSS(staffMemberWarnedID);
    }

    public Warning getWarningByID(String warningID) throws SQLException {
        return dao.getWarningByID(warningID);
    }


    public boolean cityExists(int postalCode, String locality) throws SQLException {
        return dao.cityExists(postalCode, locality);
    }

}
