package controller;

import business.Manager;
import exceptions.connexion.EndConnectionException;
import exceptions.delete.*;
import exceptions.insert.IllegalArgumentException;
import exceptions.list.*;
import models.*;
import exceptions.connexion.SingletonConnexionException;
import exceptions.insert.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ApplicationController {

    private final Manager manager;

    public ApplicationController() throws SingletonConnexionException, SQLException {
        this.manager = new Manager();
    }

    public void addStaffMember(StaffMember staffMember) throws InsertStaffMemberException {
        manager.insertStaffMember(staffMember);
    }

    public void addAbsence(Absence absence) throws IllegalArgumentException, SingletonConnexionException {
        manager.insertAbsence(absence);
    }

    public void addReplacement(Replacement replacement) throws InsertReplacementException, SingletonConnexionException {
        manager.insertReplacement(replacement);
    }

    public void addClient(Client client) throws InsertClientException, SQLException, SingletonConnexionException {
        manager.insertClient(client);
    }

    public void addContract(Contract contract) throws InsertContractException, SingletonConnexionException {
        manager.insertContract(contract);
    }

    public void addMachinery(Machinery machinery) throws InsertMachineryException, SingletonConnexionException {
        manager.insertMachinery(machinery);
    }

    public void addMaterialsOrder(MaterialsOrder materialsOrder) throws InsertMaterialsOrderException, SingletonConnexionException {
        manager.insertMaterialsOrder(materialsOrder);
    }

    public void addCleaningService(CleaningService cleaningService) throws InsertCleaningServiceException, SingletonConnexionException {
        manager.insertCleaningService(cleaningService);
    }

    public void addProduct(Product product) throws InsertProductException, SingletonConnexionException {
        manager.insertProduct(product);
    }

    public void addTeam(Team team) throws InsertTeamException, SingletonConnexionException {
        manager.insertTeam(team);
    }

    public void addSite(Site site) throws InsertSiteException, SingletonConnexionException {
        manager.insertSite(site);
    }

    public void addCity(City city) throws InsertCityException {
        manager.insertCity(city);
    }

    public void addWarning(Warning warning) throws InsertWarningException, SingletonConnexionException {
        manager.insertWarning(warning);
    }

    public void updateAbsence(Absence absence) throws SQLException {
        manager.updateAbsence(absence);
    }

    public void updateCity(City city) throws SQLException {
        manager.updateCity(city);
    }

    public void updateCleaningService(CleaningService cleaningService) throws SQLException {
        manager.updateCleaningService(cleaningService);
    }

    public void updateClient(Client client) throws SQLException {
        manager.updateClient(client);
    }

    public void updateContract(Contract contract) throws SQLException {
        manager.updateContract(contract);
    }

    public void updateMachinery(Machinery machinery) throws SQLException {
        manager.updateMachinery(machinery);
    }

    public void updateMaterialsOrder(MaterialsOrder materialsOrder) throws SQLException {
        manager.updateMaterialsOrder(materialsOrder);
    }

    public void updateProduct(Product product) throws SQLException {
        manager.updateProduct(product);
    }

    public void updateReplacement(Replacement replacement) throws SQLException {
        manager.updateReplacement(replacement);
    }

    public void updateSite(Site site) throws SQLException {
        manager.updateSite(site);
    }

    public void updateStaffMember(StaffMember staffMember) throws SQLException {
        manager.updateStaffMember(staffMember);
    }

    public void updateTeam(Team team) throws SQLException {
        manager.updateTeam(team);
    }

    public void updateWarning(Warning warning) throws SQLException {
        manager.updateWarning(warning);
    }


    public List<Absence> listAbsences() throws ListAbsencesException {
        return manager.listAbsences();
    }

    public List<City> listCities() throws ListCitiesException {
        return manager.listCities();
    }

    public List<CleaningService> listCleaningServices() throws ListCleaningServicesException {
        return manager.listCleaningServices();
    }

    public List<Client> listClients() throws ListClientsException {
        return manager.listClients();
    }

    public List<Contract> listContracts() throws ListContractsException {
        return manager.listContracts();
    }

    public List<Machinery> listMachineries() throws ListMachineriesException {
        return manager.listMachineries();
    }

    public List<MaterialsOrder> listMaterialsOrders() throws ListMaterialsOrdersException {
        return manager.listMaterialsOrders();
    }

    public List<Product> listProducts() throws ListProductsException {
        return manager.listProducts();
    }

    public List<Replacement> listReplacements() throws ListReplacementsException {
        return manager.listReplacements();
    }

    public List<Site> listSites() throws ListSitesException {
        return manager.listSites();
    }

    public List<StaffMember> listStaffMembers() throws ListStaffMembersException {
        return manager.listStaffMembers();
    }

    public List<Team> listTeams() throws ListTeamsException {
        return manager.listTeams();
    }
    
    public List<Warning> listWarnings() throws ListWarningsException {
        return manager.listWarnings();
    }

    public void deleteAbsence(String absenceID) throws DeleteAbsenceException {
        manager.deleteAbsence(absenceID);
    }

    public void deleteCity(int postalCode, String locality) throws DeleteCityException {
        manager.deleteCity(postalCode, locality);
    }

    public void deleteCleaningService(String cleaningServiceID) throws DeleteCleaningServiceException{
        manager.deleteCleaningService(cleaningServiceID);
    }

    public void deleteClient(String clientID) throws DeleteClientException {
        manager.deleteClient(clientID);
    }

    public void deleteContract(String contractID) throws DeleteContractException {
        manager.deleteContract(contractID);
    }

    public void deleteMachinery(String machineryID) throws DeleteMachineryException {
        manager.deleteMachinery(machineryID);
    }

    public void deleteMaterialsOrder(String materialsOrderID) throws DeleteMaterialsOrderException {
        manager.deleteMaterialsOrder(materialsOrderID);
    }

    public void deleteProduct(String productID) throws DeleteProductException {
        manager.deleteProduct(productID);
    }

    public void deleteReplacement(String replacementID) throws DeleteReplacementException {
        manager.deleteReplacement(replacementID);
    }

    public void deleteSite(String siteID) throws DeleteSiteException {
        manager.deleteSite(siteID);
    }

    public void deleteStaffMember(String numONSS) throws DeleteStaffMemberException {
        manager.deleteStaffMember(numONSS);
    }

    public void deleteTeam(String teamID) throws DeleteTeamException {
        manager.deleteTeam(teamID);
    }

    public void deleteWarning(String warningID) throws DeleteWarningException {
        manager.deleteWarning(warningID);
    }

    public List<Absence> getAbsencesByAbsentID(String absentID) throws SQLException {
        return manager.getAbsencesByAbsentID(absentID);
    }

    public Absence getAbsenceByID(String absenceID) throws SQLException, IllegalArgumentException {
        return manager.getAbsenceByID(absenceID);
    }

    public City getCityByLocality(String locality) throws SQLException {
        return manager.getCityByLocality(locality);
    }

    public List<CleaningService> getCleaningServicesByTeam(String teamID) throws SQLException {
        return manager.getCleaningServicesByTeam(teamID);
    }

    public CleaningService getCleaningServiceByID(String cleaningServiceID) throws SQLException {
        return manager.getCleaningServiceByID(cleaningServiceID);
    }

    public List<Client> getClientsByName(String name) throws SQLException {
        return manager.getClientsByName(name);
    }

    public Client getClientByID(String clientID) throws SQLException {
        return manager.getClientByID(clientID);
    }

    public Contract getContractByID(String contractID) throws SQLException {
        return manager.getContractByID(contractID);
    }

    public Machinery getMachineryByName(String name) throws SQLException {
        return manager.getMachineryByName(name);
    }

    public Machinery getMachineryById(String machineryID) throws SQLException {
        return manager.getMachineryById(machineryID);
    }

    public List<MaterialsOrder> getMaterialsOrderByTeam(String teamID) throws SQLException {
        return manager.getMaterialsOrderByTeam(teamID);
    }

    public MaterialsOrder getMaterialsOrderByID(String materialsOrderID) throws SQLException {
        return manager.getMaterialsOrderByID(materialsOrderID);
    }

    public Product getProductByName(String productName) throws SQLException {
        return manager.getProductByName(productName);
    }

    public Product getProductByID(String productID) throws SQLException {
        return manager.getProductByID(productID);
    }

    public List<Replacement> getReplacementByStaffReplacing(String staffReplacingID) throws SQLException {
        return manager.getReplacementByStaffReplacing(staffReplacingID);
    }

    public Replacement getReplacementById(String replacementID) throws SQLException {
        return manager.getReplacementById(replacementID);
    }

    public List<Site> getSiteByName(String siteName) throws SQLException {
        return manager.getSiteByName(siteName);
    }

    public Site getSiteByID(String siteID) throws SQLException {
        return manager.getSiteByID(siteID);
    }

    public List<Site> getSiteByCodePostal(int postalCode) throws SQLException {
        return manager.getSiteByCodePostal(postalCode);
    }

    public List<StaffMember> getStaffMembersByName(String name) throws SQLException {
        return manager.getStaffMembersByName(name);
    }

    public StaffMember getStaffMemberByONSS(String numONSS) throws SQLException {
        return manager.getStaffMemberByONSS(numONSS);
    }

    public Team getTeamByTeamID(String teamID) throws SQLException {
        return manager.getTeamByTeamID(teamID);
    }

    public List<Team> getTeamsByMemberONSS(String numONSS) throws SQLException {
        return manager.getTeamsByMemberONSS(numONSS);
    }

    public List<Warning> getWarningByONSS(String staffMemberWarnedID) throws SQLException {
        return manager.getWarningByONSS(staffMemberWarnedID);
    }

    public Warning getWarningByID(String warningID) throws SQLException {
        return manager.getWarningByID(warningID);
    }

    public Map<String, Integer> getMaterialsOrder_prod(String materialsOrderID) throws SQLException {
        return manager.getMaterialsOrder_prod(materialsOrderID);
    }

    public void endConnection() throws EndConnectionException {
        manager.endConnection();
    }
}
