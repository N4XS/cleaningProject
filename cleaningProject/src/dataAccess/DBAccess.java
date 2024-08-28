package dataAccess;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import exceptions.delete.*;
import exceptions.insert.*;
import exceptions.insert.IllegalArgumentException;
import exceptions.list.*;
import exceptions.connexion.*;
import models.*;

public class DBAccess implements DAOAccess {
    private Connection connection;

    public DBAccess() throws SingletonConnexionException, SQLException {
        this.connection = SingletonConnection.getInstance();
        if (this.connection == null || this.connection.isClosed()) {
            throw new SingletonConnexionException("La connexion à la base de données n'a pas pu être établie.");
        }
    }

        public List<Absence> listAbsences() throws ListAbsencesException {
        List<Absence> absences = new ArrayList<>();
        String sql = "SELECT absenceID, justification, startDate, endDate, isUnderCertificate, absentID FROM absence";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                String absenceID = resultSet.getString("absenceID");
                String justification = resultSet.getString("justification");
                LocalDate startDate = resultSet.getDate("startDate").toLocalDate();
                LocalDate endDate = resultSet.getDate("endDate") != null ? resultSet.getDate("endDate").toLocalDate() : null;
                boolean isUnderCertificate = resultSet.getBoolean("isUnderCertificate");
                String absentID = resultSet.getString("absentID");

                Absence absence = new Absence(justification, startDate, endDate, isUnderCertificate, absentID);
                absence.setAbsenceID(absenceID);
                absences.add(absence);
            }
        } catch (SQLException e) {
            throw new ListAbsencesException("Erreur lors de la récupération des absences : " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
            return absences;
    }

    public List<City> listCities() throws ListCitiesException {
        List<City> cities = new ArrayList<>();
        String sql = "SELECT postalCode, locality FROM City";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int postalCode = resultSet.getInt("postalCode");
                String locality = resultSet.getString("locality");
                City city = new City(postalCode, locality);
                cities.add(city);
            }
        } catch (SQLException e) {
            throw new ListCitiesException("Erreur lors de la récupération des villes : " + e.getMessage(), e);
        }
        return cities;
    }

    public List<CleaningService> listCleaningServices() throws ListCleaningServicesException {
        List<CleaningService> services = new ArrayList<>();
        String sql = "SELECT cleaningServiceID, dateTimeStartPrest, duration, contractID, teamID FROM cleaningService";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                String cleaningServiceID = resultSet.getString("cleaningServiceID");
                Timestamp dateTimeStartPrest = resultSet.getTimestamp("dateTimeStartPrest");
                int duration = resultSet.getInt("duration");
                String contractID = resultSet.getString("contractID");
                String teamID = resultSet.getString("teamID");
                CleaningService service = new CleaningService(dateTimeStartPrest, duration, contractID, teamID);
                service.setCleaningServiceID(cleaningServiceID);
                services.add(service);
            }
        } catch (SQLException e) {
            throw new ListCleaningServicesException("Erreur lors de la récupération des services de nettoyage : " + e.getMessage(), e);
        }
        return services;
    }

    public List<Client> listClients() throws ListClientsException {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT clientID, name, firstName, email, gsm, streetName, streetNumber, boxNumber, postalCode, locality FROM client";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                String clientID = resultSet.getString("clientID");
                String name = resultSet.getString("name");
                String firstName = resultSet.getString("firstName");
                String email = resultSet.getString("email");
                String gsm = resultSet.getString("gsm");
                String streetName = resultSet.getString("streetName");
                int streetNumber = resultSet.getInt("streetNumber");
                String boxNumber = resultSet.getString("boxNumber");
                int postalCode = resultSet.getInt("postalCode");
                String locality = resultSet.getString("locality");

                Client client = new Client(name, firstName, email, gsm, streetName, streetNumber, boxNumber, postalCode, locality);
                client.setClientID(clientID);
                clients.add(client);
            }
        } catch (SQLException e) {
            throw new ListClientsException("Erreur lors de la récupération des clients : " + e.getMessage(), e);
        }
        return clients;
    }

    public List<Contract> listContracts() throws ListContractsException {
        List<Contract> contracts = new ArrayList<>();
        String sql = "SELECT contractID, siteID, clientID, nbHoursPerWeek, startDate, endDate, planningDesc, durationType FROM contract";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String contractID = resultSet.getString("contractID");
                String siteID = resultSet.getString("siteID");
                String clientID = resultSet.getString("clientID");
                int nbHoursPerWeek = resultSet.getInt("nbHoursPerWeek");
                LocalDate startDate = resultSet.getDate("startDate").toLocalDate();
                LocalDate endDate = resultSet.getDate("endDate") != null ? resultSet.getDate("endDate").toLocalDate() : null;
                String planningDesc = resultSet.getString("planningDesc");
                String durationLabel = resultSet.getString("durationType");
                Contract.DurationType durationType = Contract.DurationType.fromLabel(durationLabel);
                Contract contract = new Contract(siteID, clientID, nbHoursPerWeek, startDate, durationType, planningDesc);
                contract.setContractID(contractID);
                contract.setEndDate(endDate);
                contracts.add(contract);
            }
        } catch (SQLException e) {
            throw new ListContractsException("Erreur lors de la récupération des contrats : " + e.getMessage(), e);
        }

        return contracts;
    }

    public List<Machinery> listMachineries() throws ListMachineriesException {
        List<Machinery> machineries = new ArrayList<>();
        String sql = "SELECT machineryID, name, isAvailable, siteID FROM machinery";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String machineryID = resultSet.getString("machineryID");
                String name = resultSet.getString("name");
                boolean isAvailable = resultSet.getBoolean("isAvailable");
                String siteID = resultSet.getString("siteID");

                Machinery machinery = new Machinery(name, isAvailable, siteID);
                machinery.setMachineryID(machineryID);
                machineries.add(machinery);
            }
        } catch (SQLException e) {
            throw new ListMachineriesException("Erreur lors de la récupération des machines : " + e.getMessage(), e);
        }

        return machineries;
    }

    public List<MaterialsOrder> listMaterialsOrders() throws ListMaterialsOrdersException {
        List<MaterialsOrder> orders = new ArrayList<>();
        String sqlOrder = "SELECT materialsOrderID, justification, dateOrder, teamID FROM MaterialsOrder";
        String sqlOrderProduct = "SELECT productID, quantity FROM MaterialsOrder_Product WHERE materialsOrderID = ?";

        try (PreparedStatement preparedStatementOrder = connection.prepareStatement(sqlOrder);
             ResultSet resultSetOrder = preparedStatementOrder.executeQuery()) {

            while (resultSetOrder.next()) {
                String materialsOrderID = resultSetOrder.getString("materialsOrderID");
                String justification = resultSetOrder.getString("justification");
                LocalDate date = resultSetOrder.getDate("dateOrder").toLocalDate();
                String teamID = resultSetOrder.getString("teamID");

                Map<String, Integer> productQuantities = new HashMap<>();

                try (PreparedStatement preparedStatementOrderProduct = connection.prepareStatement(sqlOrderProduct)) {
                    preparedStatementOrderProduct.setString(1, materialsOrderID);
                    try (ResultSet resultSetOrderProduct = preparedStatementOrderProduct.executeQuery()) {
                        while (resultSetOrderProduct.next()) {
                            String productID = resultSetOrderProduct.getString("productID");
                            int quantity = resultSetOrderProduct.getInt("quantity");
                            productQuantities.put(productID, quantity);
                        }
                    }
                }

                if (productQuantities.isEmpty()) {
                    continue;
                }

                MaterialsOrder order = new MaterialsOrder(justification, date, teamID, productQuantities);
                order.setMaterialsOrderID(materialsOrderID);
                orders.add(order);
            }
        } catch (SQLException e) {
            throw new ListMaterialsOrdersException("Erreur lors de la récupération des commandes de matériaux : " + e.getMessage(), e);
        }

        return orders;
    }


    public List<Product> listProducts() throws ListProductsException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT productID, productName, nbAvailable, description FROM product";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String productID = resultSet.getString("productID");
                String productName = resultSet.getString("productName");
                int nbAvailable = resultSet.getInt("nbAvailable");
                String description = resultSet.getString("description");

                Product product = new Product(productName, nbAvailable, description);
                product.setProductID(productID);
                products.add(product);
            }
        } catch (SQLException e) {
            throw new ListProductsException("Erreur lors de la récupération des produits : " + e.getMessage(), e);
        }

        return products;
    }

    public List<Replacement> listReplacements() throws ListReplacementsException {
        List<Replacement> replacements = new ArrayList<>();
        String sql = "SELECT replacementID, startDate, endDate, staffReplacingID, absenceReplacedID FROM replacement";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String replacementID = resultSet.getString("replacementID");
                LocalDate startDate = resultSet.getDate("startDate").toLocalDate();
                LocalDate endDate = resultSet.getDate("endDate") != null ? resultSet.getDate("endDate").toLocalDate() : null;
                String staffReplacingID = resultSet.getString("staffReplacingID");
                String absenceReplacedID = resultSet.getString("absenceReplacedID");

                Replacement replacement = new Replacement(startDate, endDate, staffReplacingID, absenceReplacedID);
                replacement.setReplacementID(replacementID);
                replacements.add(replacement);
            }
        } catch (SQLException e) {
            throw new ListReplacementsException("Erreur lors de la récupération des remplacements : " + e.getMessage(), e);
        }

        return replacements;
    }

    public List<Site> listSites() throws ListSitesException {
        List<Site> sites = new ArrayList<>();
        String sql = "SELECT siteID, siteName, streetName, streetNumber, boxHouse, description, postalCode, locality, clientOwnerID FROM site";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String siteID = resultSet.getString("siteID");
                String siteName = resultSet.getString("siteName");
                String streetName = resultSet.getString("streetName");
                int streetNumber = resultSet.getInt("streetNumber");
                String boxHouse = resultSet.getString("boxHouse");
                String description = resultSet.getString("description");
                int postalCode = resultSet.getInt("postalCode");
                String locality = resultSet.getString("locality");
                String clientOwnerID = resultSet.getString("clientOwnerID");

                Site site = new Site(siteName, streetName, streetNumber, boxHouse, description, postalCode, locality, clientOwnerID);
                site.setSiteID(siteID);
                sites.add(site);
            }
        } catch (SQLException e) {
            throw new ListSitesException("Erreur lors de la récupération des sites : " + e.getMessage(), e);
        }

        return sites;
    }

    public List<StaffMember> listStaffMembers() throws ListStaffMembersException {
        List<StaffMember> staffMembers = new ArrayList<>();
        String sql = "SELECT numONSS, birthday, firstName, lastName, email, streetName, streetNumber, boxNumber, cellphoneNumber, startDate, isCleaner, graduate, postalCode, locality FROM staffmember";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String numONSS = resultSet.getString("numONSS");
                LocalDate birthday = resultSet.getDate("birthday").toLocalDate();
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String email = resultSet.getString("email");
                String streetName = resultSet.getString("streetName");
                int streetNumber = resultSet.getInt("streetNumber");
                String boxNumber = resultSet.getString("boxNumber");
                String cellphoneNumber = resultSet.getString("cellphoneNumber");
                LocalDate startDate = resultSet.getDate("startDate").toLocalDate();
                boolean isCleaner = resultSet.getBoolean("isCleaner");
                String graduate = resultSet.getString("graduate");
                int postalCode = resultSet.getInt("postalCode");
                String locality = resultSet.getString("locality");

                City city = new City(postalCode, locality);
                StaffMember staffMember = new StaffMember(numONSS, birthday, firstName, lastName, email, streetName, streetNumber, boxNumber, cellphoneNumber, startDate, isCleaner, graduate, city);
                staffMembers.add(staffMember);
            }
        } catch (SQLException e) {
            throw new ListStaffMembersException("Erreur lors de la récupération des membres du personnel : " + e.getMessage(), e);
        }

        return staffMembers;
    }

    public List<Team> listTeams() throws ListTeamsException {
        List<Team> teams = new ArrayList<>();
        String sql = "SELECT teamID, leaderID, secondMemberID, thirdMemberID FROM team";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String teamID = resultSet.getString("teamID");
                String leaderID = resultSet.getString("leaderID");
                String secondMemberID = resultSet.getString("secondMemberID");
                String thirdMemberID = resultSet.getString("thirdMemberID");

                List<String> staffMemberIDs = new ArrayList<>();
                if (leaderID != null && !leaderID.trim().isEmpty()) {
                    staffMemberIDs.add(leaderID);
                }
                if (secondMemberID != null && !secondMemberID.trim().isEmpty()) {
                    staffMemberIDs.add(secondMemberID);
                }
                if (thirdMemberID != null && !thirdMemberID.trim().isEmpty()) {
                    staffMemberIDs.add(thirdMemberID);
                }

                Team team = new Team(staffMemberIDs);
                team.setTeamID(teamID);

                if (leaderID != null && !staffMemberIDs.isEmpty() && !staffMemberIDs.get(0).equals(leaderID)) {
                    staffMemberIDs.remove(leaderID);
                    staffMemberIDs.add(0, leaderID);
                }

                teams.add(team);
            }
        } catch (SQLException e) {
            throw new ListTeamsException("Erreur lors de la récupération des équipes : " + e.getMessage(), e);
        }

        return teams;
    }


    public List<Warning> listWarnings() throws ListWarningsException {
        List<Warning> warnings = new ArrayList<>();
        String sql = "SELECT warningID, description, dateFault, isSeriousFault, staffMemberWarnedID FROM warning";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String warningID = resultSet.getString("warningID");
                String description = resultSet.getString("description");
                LocalDate date = resultSet.getDate("dateFault").toLocalDate();
                boolean isSeriousFault = resultSet.getBoolean("isSeriousFault");
                String staffMemberWarnedID = resultSet.getString("staffMemberWarnedID");

                Warning warning = new Warning(description, date, isSeriousFault, staffMemberWarnedID);
                warning.setWarningID(warningID);
                warnings.add(warning);
            }
        } catch (SQLException e) {
            throw new ListWarningsException("Erreur lors de la récupération des avertissements : " + e.getMessage(), e);
        }

        return warnings;
    }

    public void insertAbsence(Absence absence) throws IllegalArgumentException, SingletonConnexionException {
        String absenceId = IDManager.generateUniqueID("Absence", this.connection);
        absence.setAbsenceID(absenceId);
        String sql = "INSERT INTO absence (absenceID, justification, startDate, endDate, isUnderCertificate, absentID) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, absence.getAbsenceID());
            preparedStatement.setString(2, absence.getJustification());
            preparedStatement.setDate(3, Date.valueOf(absence.getStartDate()));
            if (absence.getEndDate() == null) {
                preparedStatement.setNull(4, Types.DATE);
            } else {
                preparedStatement.setDate(4, Date.valueOf(absence.getEndDate()));
            }
            preparedStatement.setBoolean(5, absence.getIsUnderCertificate());
            preparedStatement.setString(6, absence.getAbsentID());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException("Erreur lors de l'ajout de l'absence: " + e.getMessage(), e);
        }
    }

    public void insertCity(City city) throws InsertCityException {
        city.setLocality(normalizeLocality(city.getLocality()));
        try {
            if (!cityExists(city.getPostalCode(), city.getLocality())) {
                String sql = "INSERT INTO City (postalCode, locality) VALUES (?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setInt(1, city.getPostalCode());
                    preparedStatement.setString(2, city.getLocality());
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new InsertCityException("Erreur lors de l'insertion de la ville: " + e.getMessage(), e);
        }

    }

    public String normalizeLocality(String locality) {
        return locality.trim().toLowerCase();
    }

    public boolean cityExists(int postalCode, String locality) throws SQLException {
        String sql = "SELECT COUNT(*) FROM City WHERE postalCode = ? AND locality = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, postalCode);
            preparedStatement.setString(2, locality);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public void insertCleaningService(CleaningService cleaningService) throws InsertCleaningServiceException, SingletonConnexionException {
        String cleaningServiceId = IDManager.generateUniqueID("CleaningService", this.connection);
        cleaningService.setCleaningServiceID(cleaningServiceId);
        String sql = "INSERT INTO cleaningService (cleaningServiceID, dateTimeStartPrest, duration, contractID, teamID) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, cleaningService.getCleaningServiceID());
            preparedStatement.setTimestamp(2, cleaningService.getDateTimeStartPrest());
            preparedStatement.setInt(3, cleaningService.getDuration());
            preparedStatement.setString(4, cleaningService.getContractID());
            preparedStatement.setString(5, cleaningService.getTeamID());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new InsertCleaningServiceException("Erreur lors de l'ajout de la prestation de nettoyage: " + e.getMessage(), e);
        }
    }

    public void insertClient(Client client) throws SQLException, SingletonConnexionException {
        String clientId = IDManager.generateUniqueID("Client", this.connection);
        client.setClientID(clientId);
        String sql = "INSERT INTO client (clientID, name, firstName, email, gsm, streetName, streetNumber, boxNumber, postalCode, locality) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
       
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, client.getClientID());
            pstmt.setString(2, client.getName());
            pstmt.setString(3, client.getFirstName());
            pstmt.setString(4, client.getEmail());
            pstmt.setString(5, client.getGsm());
            pstmt.setString(6, client.getStreetName());
            pstmt.setInt(7, client.getStreetNumber());
            pstmt.setString(8, client.getBoxNumber());
            pstmt.setInt(9, client.getCity().getPostalCode());
            pstmt.setString(10, client.getCity().getLocality());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Erreur lors de l'insertion du client : " + e.getMessage());
        }
    }


    public void insertContract(Contract contract) throws InsertContractException, SingletonConnexionException {
        String contractId = IDManager.generateUniqueID("contract", this.connection);
        contract.setContractID(contractId);
        String sql = "INSERT INTO contract (contractID, siteID, clientID, nbHoursPerWeek, startDate, endDate, planningDesc, durationType) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, contract.getContractID());
            preparedStatement.setString(2, contract.getClientID());
            preparedStatement.setString(3, contract.getSiteID());
            preparedStatement.setInt(4, contract.getNbHoursPerWeek());
            preparedStatement.setDate(5, Date.valueOf(contract.getStartDate()));
            preparedStatement.setDate(6, contract.getEndDate() != null ? Date.valueOf(contract.getEndDate()) : null);
            preparedStatement.setString(7, contract.getPlanning());
            preparedStatement.setString(8, contract.getDurationType().getLabel());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new InsertContractException("Erreur lors de l'ajout du contrat: " + e.getMessage(), e);
        }
    }

    public void insertMachinery(Machinery machinery) throws InsertMachineryException, SingletonConnexionException {
        String machineryId = IDManager.generateUniqueID("machinery", this.connection);
        machinery.setMachineryID(machineryId);
        String sql = "INSERT INTO machinery (machineryID, name, isAvailable, siteID) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, machinery.getMachineryID());
            preparedStatement.setString(2, machinery.getName());
            preparedStatement.setBoolean(3, machinery.getAvailable());
            preparedStatement.setString(4, machinery.getSiteID());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new InsertMachineryException("Erreur lors de l'ajout de la machine: " + e.getMessage(), e);
        }
    }

    public void insertMaterialsOrder(MaterialsOrder materialsOrder) throws InsertMaterialsOrderException, SingletonConnexionException {
        String mateialsOrderId = IDManager.generateUniqueID("MaterialsOrder", this.connection);
        materialsOrder.setMaterialsOrderID(mateialsOrderId);
        String sqlOrder = "INSERT INTO MaterialsOrder (materialsOrderID, justification, dateOrder, teamID) VALUES (?, ?, ?, ?)";
        String sqlOrderProduct = "INSERT INTO MaterialsOrder_Product (materialsOrderID, productID, quantity) VALUES (?, ?, ?)";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatementOrder = connection.prepareStatement(sqlOrder)) {
                preparedStatementOrder.setString(1, materialsOrder.getMaterialsOrderID());
                preparedStatementOrder.setString(2, materialsOrder.getJustification());
                preparedStatementOrder.setDate(3, Date.valueOf(materialsOrder.getDateOrder()));
                preparedStatementOrder.setString(4, materialsOrder.getTeamID());
                preparedStatementOrder.executeUpdate();
            }

            try (PreparedStatement preparedStatementOrderProduct = connection.prepareStatement(sqlOrderProduct)) {
                for (Map.Entry<String, Integer> entry : materialsOrder.getProductQuantities().entrySet()) {
                    preparedStatementOrderProduct.setString(1, materialsOrder.getMaterialsOrderID());
                    preparedStatementOrderProduct.setString(2, entry.getKey());
                    preparedStatementOrderProduct.setInt(3, entry.getValue());
                    preparedStatementOrderProduct.executeUpdate();
                }
            }

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                throw new InsertMaterialsOrderException("Erreur lors du rollback de la transaction: " + rollbackException.getMessage(), rollbackException);
            }
            throw new InsertMaterialsOrderException("Erreur lors de l'ajout de la commande de matériaux: " + e.getMessage(), e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public void insertReplacement(Replacement replacement) throws InsertReplacementException, SingletonConnexionException {
        String replacementId = IDManager.generateUniqueID("replacement", this.connection);
        replacement.setReplacementID(replacementId);
        String sql = "INSERT INTO replacement (replacementID, startDate, endDate, staffReplacingID, absenceReplacedID) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, replacement.getReplacementID());
            preparedStatement.setDate(2, Date.valueOf(replacement.getStartDate()));
            preparedStatement.setDate(3, Date.valueOf(replacement.getEndDate()));
            preparedStatement.setString(4, replacement.getStaffReplacingID());
            preparedStatement.setString(5, replacement.getAbsenceReplacedID());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new InsertReplacementException("Erreur lors de l'ajout du remplacement: " + e.getMessage(), e);
        }
    }

    public void insertSite(Site site) throws InsertSiteException, SingletonConnexionException {
        String siteId = IDManager.generateUniqueID("site", this.connection);
        site.setSiteID(siteId);
        String sql = "INSERT INTO site (siteID, siteName, streetName, streetNumber, boxHouse, description, postalCode, locality, clientOwnerID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, site.getSiteID());
            preparedStatement.setString(2, site.getSiteName());
            preparedStatement.setString(3, site.getStreetName());
            preparedStatement.setInt(4, site.getStreetNumber());
            preparedStatement.setString(5, site.getBoxHouse());
            preparedStatement.setString(6, site.getDescription());
            preparedStatement.setString(9, site.getClientOwnerID());
            preparedStatement.setInt(7, site.getCity().getPostalCode());
            preparedStatement.setString(8, site.getCity().getLocality());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new InsertSiteException("Erreur lors de l'ajout du site: " + e.getMessage(), e);
        }
    }

    public void insertStaffMember(StaffMember staffMember) throws InsertStaffMemberException {
        String sql = "INSERT INTO staffmember (numONSS, birthday, firstName, lastName, email, streetName, streetNumber, boxNumber, cellphoneNumber, startDate, isCleaner, graduate, postalCode, locality) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, staffMember.getNumONSS());
            preparedStatement.setDate(2, Date.valueOf(staffMember.getBirthday()));
            preparedStatement.setString(3, staffMember.getFirstName());
            preparedStatement.setString(4, staffMember.getLastName());
            preparedStatement.setString(5, staffMember.getEmail());
            preparedStatement.setString(6, staffMember.getStreetName());
            preparedStatement.setInt(7, staffMember.getStreetNumber());
            preparedStatement.setString(8, staffMember.getBoxNumber());
            preparedStatement.setString(9, staffMember.getCellphoneNumber());
            preparedStatement.setDate(10, Date.valueOf(staffMember.getStartDate()));
            preparedStatement.setBoolean(11, staffMember.getIsCleaner());
            preparedStatement.setString(12, staffMember.getGraduate());
            preparedStatement.setInt(13, staffMember.getCity().getPostalCode());
            preparedStatement.setString(14, staffMember.getCity().getLocality());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new InsertStaffMemberException("Erreur lors de l'ajout de l'employé: " + e.getMessage(), e);
        }
    }

    public void insertProduct(Product product) throws InsertProductException, SingletonConnexionException {
        String productId = IDManager.generateUniqueID("product", this.connection);
        product.setProductID(productId);
        String sql = "INSERT INTO product (productID, productName, nbAvailable, description) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, product.getProductID());
            preparedStatement.setString(2, product.getProductName());
            preparedStatement.setInt(3, product.getNbAvailable());
            preparedStatement.setString(4, product.getDescription());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new InsertProductException("Erreur lors de l'ajout du produit: " + e.getMessage(), e);
        }
    }

    public void insertTeam(Team team) throws InsertTeamException, SingletonConnexionException {
        String teamId = IDManager.generateUniqueID("team", this.connection);
        team.setTeamID(teamId);
        String sql = "INSERT INTO team (teamID, leaderID, secondMemberID, thirdMemberID) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, team.getTeamID());
            List<String> members = team.getStaffMemberIDs();
            for (int i = 0; i < 3; i++) {
                if (i < members.size()) {
                    preparedStatement.setString(i + 2, members.get(i));
                } else {
                    preparedStatement.setNull(i + 2, Types.VARCHAR);
                }
            }
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new InsertTeamException("Erreur lors de l'ajout de l'équipe: " + e.getMessage(), e);
        }
    }

    public void insertWarning(Warning warning) throws InsertWarningException, SingletonConnexionException {
        String warningId = IDManager.generateUniqueID("warning", this.connection);
        warning.setWarningID(warningId);
        String sql = "INSERT INTO warning (warningID, description, dateFault, isSeriousFault, staffMemberWarnedID) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, warning.getWarningID());
            preparedStatement.setString(2, warning.getDescription());
            preparedStatement.setDate(3, Date.valueOf(warning.getDateFault()));
            preparedStatement.setBoolean(4, warning.getSeriousFault());
            preparedStatement.setString(5, warning.getStaffMemberWarnedID());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new InsertWarningException("Erreur lors de l'ajout de l'avertissement: " + e.getMessage(), e);
        }
    }

    public void updateAbsence(Absence absence) throws SQLException {
        String sql = "UPDATE absence SET justification = ?, startDate = ?, endDate = ?, isUnderCertificate = ?, absentID = ? WHERE absenceID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, absence.getJustification());
            preparedStatement.setDate(2, Date.valueOf(absence.getStartDate()));
            if (absence.getEndDate() == null) {
                preparedStatement.setNull(3, Types.DATE);
            } else {
                preparedStatement.setDate(3, Date.valueOf(absence.getEndDate()));
            }
            preparedStatement.setBoolean(4, absence.getIsUnderCertificate());
            preparedStatement.setString(5, absence.getAbsentID());
            preparedStatement.setString(6, absence.getAbsenceID());
            preparedStatement.executeUpdate();
        }
    }

    public void updateCity(City city) throws SQLException {
        String sql = "UPDATE City SET locality = ? WHERE postalCode = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, city.getLocality());
            preparedStatement.setInt(2, city.getPostalCode());
            preparedStatement.executeUpdate();
        }
    }

    public void updateCleaningService(CleaningService cleaningService) throws SQLException {
        String sql = "UPDATE cleaningService SET dateTimeStartPrest = ?, duration = ?, contractID = ?, teamID = ? WHERE cleaningServiceID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setTimestamp(1, cleaningService.getDateTimeStartPrest());
            preparedStatement.setInt(2, cleaningService.getDuration());
            preparedStatement.setString(3, cleaningService.getContractID());
            preparedStatement.setString(4, cleaningService.getTeamID());
            preparedStatement.setString(5, cleaningService.getCleaningServiceID());
            preparedStatement.executeUpdate();
        }
    }

    public void updateClient(Client client) throws SQLException {
        String sql = "UPDATE client SET name = ?, firstName = ?, email = ?, gsm = ?, streetName = ?, streetNumber = ?, boxNumber = ?, postalCode = ?, locality = ? WHERE clientID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, client.getName());
            preparedStatement.setString(2, client.getFirstName());
            preparedStatement.setString(3, client.getEmail());
            preparedStatement.setString(4, client.getGsm());
            preparedStatement.setString(5, client.getStreetName());
            preparedStatement.setInt(6, client.getStreetNumber());
            preparedStatement.setString(7, client.getBoxNumber());
            preparedStatement.setInt(8, client.getCity().getPostalCode());
            preparedStatement.setString(9, client.getCity().getLocality());
            preparedStatement.setString(10, client.getClientID());
            preparedStatement.executeUpdate();
        }
    }

    public void updateContract(Contract contract) throws SQLException {
        String sql = "UPDATE contract SET siteID = ?, clientID = ?, nbHoursPerWeek = ?, startDate = ?, endDate = ?, planningDesc = ?, durationType = ? WHERE contractID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, contract.getSiteID());
            preparedStatement.setString(2, contract.getClientID());
            preparedStatement.setInt(3, contract.getNbHoursPerWeek());
            preparedStatement.setDate(4, Date.valueOf(contract.getStartDate()));
            if (contract.getEndDate() == null) {
                preparedStatement.setNull(5, Types.DATE);
            } else {
                preparedStatement.setDate(5, Date.valueOf(contract.getEndDate()));
            }
            preparedStatement.setString(6, contract.getPlanning());
            preparedStatement.setString(7, contract.getDurationType().getLabel());
            preparedStatement.setString(8, contract.getContractID());
            preparedStatement.executeUpdate();
        }
    }

    public void updateMachinery(Machinery machinery) throws SQLException {
        String sql = "UPDATE machinery SET name = ?, isAvailable = ?, siteID = ? WHERE machineryID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, machinery.getName());
            preparedStatement.setBoolean(2, machinery.getAvailable());
            preparedStatement.setString(3, machinery.getSiteID());
            preparedStatement.setString(4, machinery.getMachineryID());
            preparedStatement.executeUpdate();
        }
    }

    public void updateMaterialsOrder(MaterialsOrder materialsOrder) throws SQLException {
        String sqlOrder = "UPDATE MaterialsOrder SET justification = ?, dateOrder = ?, teamID = ? WHERE materialsOrderID = ?";
        String sqlDeleteOrderProducts = "DELETE FROM MaterialsOrder_Product WHERE materialsOrderID = ?";
        String sqlInsertOrderProduct = "INSERT INTO MaterialsOrder_Product (materialsOrderID, productID, quantity) VALUES (?, ?, ?)";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatementOrder = connection.prepareStatement(sqlOrder)) {
                preparedStatementOrder.setString(1, materialsOrder.getJustification());
                preparedStatementOrder.setDate(2, Date.valueOf(materialsOrder.getDateOrder()));
                preparedStatementOrder.setString(3, materialsOrder.getTeamID());
                preparedStatementOrder.setString(4, materialsOrder.getMaterialsOrderID());
                preparedStatementOrder.executeUpdate();
            }

            try (PreparedStatement preparedStatementDelete = connection.prepareStatement(sqlDeleteOrderProducts)) {
                preparedStatementDelete.setString(1, materialsOrder.getMaterialsOrderID());
                preparedStatementDelete.executeUpdate();
            }

            try (PreparedStatement preparedStatementInsert = connection.prepareStatement(sqlInsertOrderProduct)) {
                for (Map.Entry<String, Integer> entry : materialsOrder.getProductQuantities().entrySet()) {
                    preparedStatementInsert.setString(1, materialsOrder.getMaterialsOrderID());
                    preparedStatementInsert.setString(2, entry.getKey());
                    preparedStatementInsert.setInt(3, entry.getValue());
                    preparedStatementInsert.executeUpdate();
                }
            }

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                throw new SQLException("Erreur lors du rollback de la transaction: " + rollbackException.getMessage(), rollbackException);
            }
            throw new SQLException("Erreur lors de la mise à jour de la commande de matériaux: " + e.getMessage(), e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateProduct(Product product) throws SQLException {
        String sql = "UPDATE product SET productName = ?, nbAvailable = ?, description = ? WHERE productID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, product.getProductName());
            preparedStatement.setInt(2, product.getNbAvailable());
            preparedStatement.setString(3, product.getDescription());
            preparedStatement.setString(4, product.getProductID());
            preparedStatement.executeUpdate();
        }
    }

    public void updateReplacement(Replacement replacement) throws SQLException {
        String sql = "UPDATE replacement SET startDate = ?, endDate = ?, staffReplacingID = ?, absenceReplacedID = ? WHERE replacementID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDate(1, Date.valueOf(replacement.getStartDate()));
            preparedStatement.setDate(2, Date.valueOf(replacement.getEndDate()));
            preparedStatement.setString(3, replacement.getStaffReplacingID());
            preparedStatement.setString(4, replacement.getAbsenceReplacedID());
            preparedStatement.setString(5, replacement.getReplacementID());
            preparedStatement.executeUpdate();
        }
    }

    public void updateSite(Site site) throws SQLException {
        String sql = "UPDATE site SET siteName = ?, streetName = ?, streetNumber = ?, boxHouse = ?, description = ?, postalCode = ?, locality = ?, clientOwnerID = ? WHERE siteID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, site.getSiteName());
            preparedStatement.setString(2, site.getStreetName());
            preparedStatement.setInt(3, site.getStreetNumber());
            preparedStatement.setString(4, site.getBoxHouse());
            preparedStatement.setString(5, site.getDescription());
            preparedStatement.setInt(6, site.getCity().getPostalCode());
            preparedStatement.setString(7, site.getCity().getLocality());
            preparedStatement.setString(8, site.getClientOwnerID());
            preparedStatement.setString(9, site.getSiteID());
            preparedStatement.executeUpdate();
        }
    }

    public void updateStaffMember(StaffMember staffMember) throws SQLException {
        String sql = "UPDATE staffmember SET birthday = ?, firstName = ?, lastName = ?, email = ?, streetName = ?, streetNumber = ?, boxNumber = ?, cellphoneNumber = ?, startDate = ?, isCleaner = ?, graduate = ?, postalCode = ?, locality = ? WHERE numONSS = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDate(1, Date.valueOf(staffMember.getBirthday()));
            preparedStatement.setString(2, staffMember.getFirstName());
            preparedStatement.setString(3, staffMember.getLastName());
            preparedStatement.setString(4, staffMember.getEmail());
            preparedStatement.setString(5, staffMember.getStreetName());
            preparedStatement.setInt(6, staffMember.getStreetNumber());
            preparedStatement.setString(7, staffMember.getBoxNumber());
            preparedStatement.setString(8, staffMember.getCellphoneNumber());
            preparedStatement.setDate(9, Date.valueOf(staffMember.getStartDate()));
            preparedStatement.setBoolean(10, staffMember.getIsCleaner());
            preparedStatement.setString(11, staffMember.getGraduate());
            preparedStatement.setInt(12, staffMember.getCity().getPostalCode());
            preparedStatement.setString(13, staffMember.getCity().getLocality());
            preparedStatement.setString(14, staffMember.getNumONSS());
            preparedStatement.executeUpdate();
        }
    }

    public void updateWarning(Warning warning) throws SQLException {
        String sql = "UPDATE warning SET description = ?, dateFault = ?, isSeriousFault = ?, staffMemberWarnedID = ? WHERE warningID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, warning.getDescription());
            preparedStatement.setDate(2, Date.valueOf(warning.getDateFault()));
            preparedStatement.setBoolean(3, warning.getSeriousFault());
            preparedStatement.setString(4, warning.getStaffMemberWarnedID());
            preparedStatement.setString(5, warning.getWarningID());
            preparedStatement.executeUpdate();
        }
    }


    public void updateTeam(Team team) throws SQLException {
        String sql = "UPDATE team SET leaderID = ?, secondMemberID = ?, thirdMemberID = ? WHERE teamID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            List<String> members = team.getStaffMemberIDs();
            for (int i = 0; i < 3; i++) {
                if (i < members.size()) {
                    preparedStatement.setString(i + 1, members.get(i));
                } else {
                    preparedStatement.setNull(i + 1, Types.VARCHAR);
                }
            }
            preparedStatement.setString(4, team.getTeamID());
            preparedStatement.executeUpdate();
        }
    }

    public void deleteAbsence(String absenceID) throws DeleteAbsenceException {
        try {
            deleteReplacementsByAbsenceID(absenceID);
            String sql = "DELETE FROM absence WHERE absenceID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, absenceID);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DeleteAbsenceException("Erreur lors de la suppression de l'absence: " + e.getMessage());
        }
    }

    private void deleteReplacementsByAbsenceID(String absenceID) throws SQLException {
        String sql = "DELETE FROM replacement WHERE absenceReplacedID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, absenceID);
            preparedStatement.executeUpdate();
        }
    }

    public void deleteCity(int postalCode, String locality) throws DeleteCityException {
        String sql = "DELETE FROM City WHERE postalCode = ? AND locality = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, postalCode);
            preparedStatement.setString(2, locality);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DeleteCityException("Erreur lors de la suppression de la ville: " + e.getMessage());
        }
    }

    public void deleteCleaningService(String cleaningServiceID) throws DeleteCleaningServiceException {
        String sql = "DELETE FROM cleaningService WHERE cleaningServiceID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, cleaningServiceID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DeleteCleaningServiceException("Erreur lors de la suppression de la prestation de nettoyage: " + e.getMessage());
        }
    }

    public void deleteClient(String clientID) throws DeleteClientException {
        String sql = "DELETE FROM client WHERE clientID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, clientID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DeleteClientException("Erreur lors de la suppression du client: " + e.getMessage());
        }
    }

    public void deleteContract(String contractID) throws DeleteContractException {
        String sql = "DELETE FROM contract WHERE contractID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, contractID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DeleteContractException("Erreur lors de la suppression du contrat: " + e.getMessage());
        }
    }

    public void deleteMachinery(String machineryID) throws DeleteMachineryException {
        String sql = "DELETE FROM machinery WHERE machineryID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, machineryID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DeleteMachineryException("Erreur lors de la suppression de la machine: " + e.getMessage());
        }
    }

    public void deleteMaterialsOrder(String materialsOrderID) throws DeleteMaterialsOrderException {
        String sqlOrder = "DELETE FROM MaterialsOrder WHERE materialsOrderID = ?";
        String sqlOrderProduct = "DELETE FROM MaterialsOrder_Product WHERE materialsOrderID = ?";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatementOrderProduct = connection.prepareStatement(sqlOrderProduct)) {
                preparedStatementOrderProduct.setString(1, materialsOrderID);
                preparedStatementOrderProduct.executeUpdate();
            }

            try (PreparedStatement preparedStatementOrder = connection.prepareStatement(sqlOrder)) {
                preparedStatementOrder.setString(1, materialsOrderID);
                preparedStatementOrder.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                throw new DeleteMaterialsOrderException("Erreur lors du rollback de la transaction: " + rollbackException.getMessage());
            }
            throw new DeleteMaterialsOrderException("Erreur lors de la suppression de la commande de matériaux: " + e.getMessage());
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteProduct(String productID) throws DeleteProductException {
        try {
            deleteProductReferencesInMaterialsOrderProduct(productID);
            String sql = "DELETE FROM product WHERE productID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, productID);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DeleteProductException("Erreur lors de la suppression du produit: " + e.getMessage());
        }
    }

    private void deleteProductReferencesInMaterialsOrderProduct(String productID) throws SQLException {
        String sql = "DELETE FROM materialsorder_product WHERE productID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, productID);
            preparedStatement.executeUpdate();
        }
    }

    public void deleteReplacement(String replacementID) throws DeleteReplacementException {
        String sql = "DELETE FROM replacement WHERE replacementID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, replacementID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DeleteReplacementException("Erreur lors de la suppression du remplacement: " + e.getMessage());
        }
    }

    public void deleteSite(String siteID) throws DeleteSiteException {
        String sql = "DELETE FROM site WHERE siteID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, siteID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DeleteSiteException("Erreur lors de la suppression du site: " + e.getMessage());
        }
    }

    public void deleteStaffMember(String numONSS) throws DeleteStaffMemberException {
        try {
            deleteWarningsByStaffMember(numONSS);
            String sql = "DELETE FROM staffmember WHERE numONSS = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, numONSS);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DeleteStaffMemberException("Erreur lors de la suppression du membre du personnel : " + e.getMessage());
        }
    }

    private void deleteWarningsByStaffMember(String staffMemberWarnedID) throws SQLException {
        String sql = "DELETE FROM warning WHERE staffMemberWarnedID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, staffMemberWarnedID);
            preparedStatement.executeUpdate();
        }
    }

    public void deleteTeam(String teamID) throws DeleteTeamException {
        String sql = "DELETE FROM team WHERE teamID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, teamID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DeleteTeamException("Erreur lors de la suppression de l'équipe: " + e.getMessage());
        }
    }

    public void deleteWarning(String warningID) throws DeleteWarningException {
        String sql = "DELETE FROM warning WHERE warningID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, warningID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DeleteWarningException("Erreur lors de la suppression de l'avertissement: " + e.getMessage());
        }
    }

    public List<Absence> getAbsencesByAbsentID(String absentID) throws SQLException {
        List<Absence> absences = new ArrayList<>();
        String sql = "SELECT absenceID, justification, startDate, endDate, isUnderCertificate, absentID FROM absence WHERE absentID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, absentID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String absenceID = resultSet.getString("absenceID");
                    String justification = resultSet.getString("justification");
                    LocalDate startDate = resultSet.getDate("startDate").toLocalDate();
                    LocalDate endDate = resultSet.getDate("endDate") != null ? resultSet.getDate("endDate").toLocalDate() : null;
                    boolean isUnderCertificate = resultSet.getBoolean("isUnderCertificate");

                    Absence absence = new Absence(justification, startDate, endDate, isUnderCertificate, absentID);
                    absence.setAbsenceID(absenceID);
                    absences.add(absence);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        return absences;
    }

    public Absence getAbsenceByID(String absenceID) throws SQLException, IllegalArgumentException {
        String sql = "SELECT absenceID, justification, startDate, endDate, isUnderCertificate, absentID FROM absence WHERE absenceID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, absenceID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String justification = resultSet.getString("justification");
                    LocalDate startDate = resultSet.getDate("startDate").toLocalDate();
                    LocalDate endDate = resultSet.getDate("endDate") != null ? resultSet.getDate("endDate").toLocalDate() : null;
                    boolean isUnderCertificate = resultSet.getBoolean("isUnderCertificate");
                    String absentID = resultSet.getString("absentID");

                    Absence absence = new Absence(justification, startDate, endDate, isUnderCertificate, absentID);
                    absence.setAbsenceID(absenceID);
                    return absence;
                }
            }
        }
        return null;
    }

    public City getCityByLocality(String locality) throws SQLException {
        String sql = "SELECT postalCode, locality FROM City WHERE locality = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, locality);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int postalCode = resultSet.getInt("postalCode");
                    return new City(postalCode, locality);
                }
            }
        }
        return null;
    }

    public List<CleaningService> getCleaningServicesByTeam(String teamID) throws SQLException {
        List<CleaningService> services = new ArrayList<>();
        String sql = "SELECT cleaningServiceID, dateTimeStartPrest, duration, contractID, teamID FROM cleaningService WHERE teamID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, teamID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String cleaningServiceID = resultSet.getString("cleaningServiceID");
                    Timestamp dateTimeStartPrest = resultSet.getTimestamp("dateTimeStartPrest");
                    int duration = resultSet.getInt("duration");
                    String contractID = resultSet.getString("contractID");

                    CleaningService service = new CleaningService(dateTimeStartPrest, duration, contractID, teamID);
                    service.setCleaningServiceID(cleaningServiceID);
                    services.add(service);
                }
            }
        }
        return services;
    }

    public CleaningService getCleaningServiceByID(String cleaningServiceID) throws SQLException {
        String sql = "SELECT cleaningServiceID, dateTimeStartPrest, duration, contractID, teamID FROM cleaningService WHERE cleaningServiceID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, cleaningServiceID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Timestamp dateTimeStartPrest = resultSet.getTimestamp("dateTimeStartPrest");
                    int duration = resultSet.getInt("duration");
                    String contractID = resultSet.getString("contractID");
                    String teamID = resultSet.getString("teamID");

                    CleaningService service = new CleaningService(dateTimeStartPrest, duration, contractID, teamID);
                    service.setCleaningServiceID(cleaningServiceID);
                    return service;
                }
            }
        }
        return null;
    }

    public List<Client> getClientsByName(String name) throws SQLException {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT clientID, name, firstName, email, gsm, streetName, streetNumber, boxNumber, postalCode, locality FROM client WHERE name LIKE ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, "%" + name + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String clientID = resultSet.getString("clientID");
                    String firstName = resultSet.getString("firstName");
                    String email = resultSet.getString("email");
                    String gsm = resultSet.getString("gsm");
                    String streetName = resultSet.getString("streetName");
                    int streetNumber = resultSet.getInt("streetNumber");
                    String boxNumber = resultSet.getString("boxNumber");
                    int postalCode = resultSet.getInt("postalCode");
                    String locality = resultSet.getString("locality");

                    Client client = new Client(name, firstName, email, gsm, streetName, streetNumber, boxNumber, postalCode, locality);
                    client.setClientID(clientID);
                    clients.add(client);
                }
            }
        }
        return clients;
    }

    public Client getClientByID(String clientID) throws SQLException {
        String sql = "SELECT clientID, name, firstName, email, gsm, streetName, streetNumber, boxNumber, postalCode, locality FROM client WHERE clientID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, clientID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String firstName = resultSet.getString("firstName");
                    String email = resultSet.getString("email");
                    String gsm = resultSet.getString("gsm");
                    String streetName = resultSet.getString("streetName");
                    int streetNumber = resultSet.getInt("streetNumber");
                    String boxNumber = resultSet.getString("boxNumber");
                    int postalCode = resultSet.getInt("postalCode");
                    String locality = resultSet.getString("locality");

                    Client client = new Client(name, firstName, email, gsm, streetName, streetNumber, boxNumber, postalCode, locality);
                    client.setClientID(clientID);
                    return client;
                }
            }
        }
        return null;
    }

    public Contract getContractByID(String contractID) throws SQLException {
        String sql = "SELECT contractID, siteID, clientID, nbHoursPerWeek, startDate, endDate, planningDesc, durationType FROM contract WHERE contractID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, contractID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String siteID = resultSet.getString("siteID");
                    String clientID = resultSet.getString("clientID");
                    int nbHoursPerWeek = resultSet.getInt("nbHoursPerWeek");
                    LocalDate startDate = resultSet.getDate("startDate").toLocalDate();
                    LocalDate endDate = resultSet.getDate("endDate") != null ? resultSet.getDate("endDate").toLocalDate() : null;
                    String planningDesc = resultSet.getString("planningDesc");
                    String durationLabel = resultSet.getString("durationType");
                    Contract.DurationType durationType = Contract.DurationType.fromLabel(durationLabel);

                    Contract contract = new Contract(siteID, clientID, nbHoursPerWeek, startDate, durationType, planningDesc);
                    contract.setContractID(contractID);
                    contract.setEndDate(endDate);
                    return contract;
                }
            }
        }
        return null;
    }

    public Machinery getMachineryByName(String name) throws SQLException {
        String sql = "SELECT machineryID, name, isAvailable, siteID FROM machinery WHERE name = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String machineryID = resultSet.getString("machineryID");
                    boolean isAvailable = resultSet.getBoolean("isAvailable");
                    String siteID = resultSet.getString("siteID");

                    Machinery machinery = new Machinery(name, isAvailable, siteID);
                    machinery.setMachineryID(machineryID);
                    return machinery;
                }
            }
        }
        return null;
    }

    public Machinery getMachineryById(String machineryID) throws SQLException {
        String sql = "SELECT machineryID, name, isAvailable, siteID FROM machinery WHERE machineryID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, machineryID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    boolean isAvailable = resultSet.getBoolean("isAvailable");
                    String siteID = resultSet.getString("siteID");

                    Machinery machinery = new Machinery(name, isAvailable, siteID);
                    machinery.setMachineryID(machineryID);
                    return machinery;
                }
            }
        }
        return null;
    }

    public List<MaterialsOrder> getMaterialsOrderByTeam(String teamID) throws SQLException {
        List<MaterialsOrder> orders = new ArrayList<>();
        String sql = "SELECT materialsOrderID, justification, dateOrder, teamID FROM MaterialsOrder WHERE teamID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, teamID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String materialsOrderID = resultSet.getString("materialsOrderID");
                    String justification = resultSet.getString("justification");
                    LocalDate dateOrder = resultSet.getDate("dateOrder").toLocalDate();
                    String teamIDFromDB = resultSet.getString("teamID");

                    MaterialsOrder order = new MaterialsOrder(justification, dateOrder, teamIDFromDB, getMaterialsOrder_prod(materialsOrderID));
                    order.setMaterialsOrderID(materialsOrderID);
                    orders.add(order);
                }
            }
        }
        return orders;
    }



    public MaterialsOrder getMaterialsOrderByID(String materialsOrderID) throws SQLException {
        MaterialsOrder order = null;
        String sql = "SELECT justification, dateOrder, teamID FROM MaterialsOrder WHERE materialsOrderID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, materialsOrderID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String justification = resultSet.getString("justification");
                    LocalDate dateOrder = resultSet.getDate("dateOrder").toLocalDate();
                    String teamID = resultSet.getString("teamID");

                    Map<String, Integer> productQuantities = getMaterialsOrder_prod(materialsOrderID);

                    order = new MaterialsOrder(justification, dateOrder, teamID, productQuantities);
                    order.setMaterialsOrderID(materialsOrderID);
                }
            }
        }
        return order;
    }


    public Map<String, Integer> getMaterialsOrder_prod(String materialsOrderID) throws SQLException {
        Map<String, Integer> productQuantities = new HashMap<>();
        String sql = "SELECT productID, quantity FROM materialsorder_product WHERE materialsOrderID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, materialsOrderID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String productID = resultSet.getString("productID");
                    int quantity = resultSet.getInt("quantity");
                    productQuantities.put(productID, quantity);
                }
            }
        }

        if (productQuantities.isEmpty()) {
            System.err.println("Pas de produit dans cette commande : " + materialsOrderID);
        }

        return productQuantities;
    }




    public Product getProductByName(String productName) throws SQLException {
        String sql = "SELECT productID, productName, nbAvailable, description FROM product WHERE productName = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, productName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String productID = resultSet.getString("productID");
                    int nbAvailable = resultSet.getInt("nbAvailable");
                    String description = resultSet.getString("description");

                    Product product = new Product(productName, nbAvailable, description);
                    product.setProductID(productID);
                    return product;
                }
            }
        }
        return null;
    }

    public Product getProductByID(String productID) throws SQLException {
        String sql = "SELECT productID, productName, nbAvailable, description FROM product WHERE productID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, productID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String productName = resultSet.getString("productName");
                    int nbAvailable = resultSet.getInt("nbAvailable");
                    String description = resultSet.getString("description");

                    Product product = new Product(productName, nbAvailable, description);
                    product.setProductID(productID);
                    return product;
                }
            }
        }
        return null;
    }

    public List<Replacement> getReplacementByStaffReplacing(String staffReplacingID) throws SQLException {
        List<Replacement> replacements = new ArrayList<>();
        String sql = "SELECT replacementID, startDate, endDate, staffReplacingID, absenceReplacedID FROM replacement WHERE staffReplacingID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, staffReplacingID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String replacementID = resultSet.getString("replacementID");
                    LocalDate startDate = resultSet.getDate("startDate").toLocalDate();
                    LocalDate endDate = resultSet.getDate("endDate") != null ? resultSet.getDate("endDate").toLocalDate() : null;
                    String absenceReplacedID = resultSet.getString("absenceReplacedID");

                    Replacement replacement = new Replacement(startDate, endDate, staffReplacingID, absenceReplacedID);
                    replacement.setReplacementID(replacementID);
                    replacements.add(replacement);
                }
            }
        }
        return replacements;
    }

    public Replacement getReplacementById(String replacementID) throws SQLException {
        String sql = "SELECT replacementID, startDate, endDate, staffReplacingID, absenceReplacedID FROM replacement WHERE replacementID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, replacementID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    LocalDate startDate = resultSet.getDate("startDate").toLocalDate();
                    LocalDate endDate = resultSet.getDate("endDate") != null ? resultSet.getDate("endDate").toLocalDate() : null;
                    String staffReplacingID = resultSet.getString("staffReplacingID");
                    String absenceReplacedID = resultSet.getString("absenceReplacedID");

                    Replacement replacement = new Replacement(startDate, endDate, staffReplacingID, absenceReplacedID);
                    replacement.setReplacementID(replacementID);
                    return replacement;
                }
            }
        }
        return null;
    }

    public List<Site> getSiteByName(String siteName) throws SQLException {
        List<Site> sites = new ArrayList<>();
        String sql = "SELECT siteID, siteName, streetName, streetNumber, boxHouse, description, postalCode, locality, clientOwnerID FROM site WHERE siteName LIKE ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, "%" + siteName + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String siteID = resultSet.getString("siteID");
                    String streetName = resultSet.getString("streetName");
                    int streetNumber = resultSet.getInt("streetNumber");
                    String boxHouse = resultSet.getString("boxHouse");
                    String description = resultSet.getString("description");
                    int postalCode = resultSet.getInt("postalCode");
                    String locality = resultSet.getString("locality");
                    String clientOwnerID = resultSet.getString("clientOwnerID");

                    Site site = new Site(siteName, streetName, streetNumber, boxHouse, description, postalCode, locality, clientOwnerID);
                    site.setSiteID(siteID);
                    sites.add(site);
                }
            }
        }
        return sites;
    }


    public Site getSiteByID(String siteID) throws SQLException {
        String sql = "SELECT siteID, siteName, streetName, streetNumber, boxHouse, description, postalCode, locality, clientOwnerID FROM site WHERE siteID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, siteID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String siteName = resultSet.getString("siteName");
                    String streetName = resultSet.getString("streetName");
                    int streetNumber = resultSet.getInt("streetNumber");
                    String boxHouse = resultSet.getString("boxHouse");
                    String description = resultSet.getString("description");
                    int postalCode = resultSet.getInt("postalCode");
                    String locality = resultSet.getString("locality");
                    String clientOwnerID = resultSet.getString("clientOwnerID");

                    Site site = new Site(siteName, streetName, streetNumber, boxHouse, description, postalCode, locality, clientOwnerID);
                    site.setSiteID(siteID);
                    return site;
                }
            }
        }
        return null;
    }

    public List<Site> getSiteByCodePostal(int postalCode) throws SQLException {
        List<Site> sites = new ArrayList<>();
        String sql = "SELECT siteID, siteName, streetName, streetNumber, boxHouse, description, postalCode, locality, clientOwnerID FROM site WHERE postalCode = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, postalCode);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String siteID = resultSet.getString("siteID");
                    String siteName = resultSet.getString("siteName");
                    String streetName = resultSet.getString("streetName");
                    int streetNumber = resultSet.getInt("streetNumber");
                    String boxHouse = resultSet.getString("boxHouse");
                    String description = resultSet.getString("description");
                    String locality = resultSet.getString("locality");
                    String clientOwnerID = resultSet.getString("clientOwnerID");

                    Site site = new Site(siteName, streetName, streetNumber, boxHouse, description, postalCode, locality, clientOwnerID);
                    site.setSiteID(siteID);
                    sites.add(site);
                }
            }
        }
        return sites;
    }

    public List<StaffMember> getStaffMembersByName(String name) throws SQLException {
        List<StaffMember> staffMembers = new ArrayList<>();
        String sql = "SELECT numONSS, birthday, firstName, lastName, email, streetName, streetNumber, boxNumber, cellphoneNumber, startDate, isCleaner, graduate, postalCode, locality FROM staffmember WHERE firstName LIKE ? OR lastName LIKE ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, "%" + name + "%");
            preparedStatement.setString(2, "%" + name + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String numONSS = resultSet.getString("numONSS");
                    LocalDate birthday = resultSet.getDate("birthday").toLocalDate();
                    String firstName = resultSet.getString("firstName");
                    String lastName = resultSet.getString("lastName");
                    String email = resultSet.getString("email");
                    String streetName = resultSet.getString("streetName");
                    int streetNumber = resultSet.getInt("streetNumber");
                    String boxNumber = resultSet.getString("boxNumber");
                    String cellphoneNumber = resultSet.getString("cellphoneNumber");
                    LocalDate startDate = resultSet.getDate("startDate").toLocalDate();
                    boolean isCleaner = resultSet.getBoolean("isCleaner");
                    String graduate = resultSet.getString("graduate");
                    int postalCode = resultSet.getInt("postalCode");
                    String locality = resultSet.getString("locality");

                    City city = new City(postalCode, locality);
                    StaffMember staffMember = new StaffMember(numONSS, birthday, firstName, lastName, email, streetName, streetNumber, boxNumber, cellphoneNumber, startDate, isCleaner, graduate, city);
                    staffMembers.add(staffMember);
                }
            }
        }
        return staffMembers;
    }

    public StaffMember getStaffMemberByONSS(String numONSS) throws SQLException {
        String sql = "SELECT numONSS, birthday, firstName, lastName, email, streetName, streetNumber, boxNumber, cellphoneNumber, startDate, isCleaner, graduate, postalCode, locality FROM staffmember WHERE numONSS = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, numONSS);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    LocalDate birthday = resultSet.getDate("birthday").toLocalDate();
                    String firstName = resultSet.getString("firstName");
                    String lastName = resultSet.getString("lastName");
                    String email = resultSet.getString("email");
                    String streetName = resultSet.getString("streetName");
                    int streetNumber = resultSet.getInt("streetNumber");
                    String boxNumber = resultSet.getString("boxNumber");
                    String cellphoneNumber = resultSet.getString("cellphoneNumber");
                    LocalDate startDate = resultSet.getDate("startDate").toLocalDate();
                    boolean isCleaner = resultSet.getBoolean("isCleaner");
                    String graduate = resultSet.getString("graduate");
                    int postalCode = resultSet.getInt("postalCode");
                    String locality = resultSet.getString("locality");

                    City city = new City(postalCode, locality);
                    StaffMember staffMember = new StaffMember(numONSS, birthday, firstName, lastName, email, streetName, streetNumber, boxNumber, cellphoneNumber, startDate, isCleaner, graduate, city);
                    return staffMember;
                }
            }
        }
        return null;
    }

    public Team getTeamByTeamID(String teamID) throws SQLException {
        String sql = "SELECT teamID, leaderID, secondMemberID, thirdMemberID FROM team WHERE teamID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, teamID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String leaderID = resultSet.getString("leaderID");
                    String secondMemberID = resultSet.getString("secondMemberID");
                    String thirdMemberID = resultSet.getString("thirdMemberID");

                    List<String> staffMemberIDs = new ArrayList<>();
                    if (leaderID != null && !leaderID.trim().isEmpty()) {
                        staffMemberIDs.add(leaderID);
                    }
                    if (secondMemberID != null && !secondMemberID.trim().isEmpty()) {
                        staffMemberIDs.add(secondMemberID);
                    }
                    if (thirdMemberID != null && !thirdMemberID.trim().isEmpty()) {
                        staffMemberIDs.add(thirdMemberID);
                    }

                    Team team = new Team(staffMemberIDs);
                    team.setTeamID(teamID);
                    return team;
                }
            }
        }
        return null;
    }

    public List<Team> getTeamsByMemberONSS(String numONSS) throws SQLException {
        List<Team> teams = new ArrayList<>();
        String sql = "SELECT teamID, leaderID, secondMemberID, thirdMemberID FROM team WHERE leaderID = ? OR secondMemberID = ? OR thirdMemberID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, numONSS);
            preparedStatement.setString(2, numONSS);
            preparedStatement.setString(3, numONSS);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String teamID = resultSet.getString("teamID");
                    String leaderID = resultSet.getString("leaderID");
                    String secondMemberID = resultSet.getString("secondMemberID");
                    String thirdMemberID = resultSet.getString("thirdMemberID");

                    List<String> staffMemberIDs = new ArrayList<>();
                    if (leaderID != null && !leaderID.trim().isEmpty()) {
                        staffMemberIDs.add(leaderID);
                    }
                    if (secondMemberID != null && !secondMemberID.trim().isEmpty()) {
                        staffMemberIDs.add(secondMemberID);
                    }
                    if (thirdMemberID != null && !thirdMemberID.trim().isEmpty()) {
                        staffMemberIDs.add(thirdMemberID);
                    }

                    Team team = new Team(staffMemberIDs);
                    team.setTeamID(teamID);
                    teams.add(team);
                }
            }
        }
        return teams;
    }

    public List<Warning> getWarningByONSS(String staffMemberWarnedID) throws SQLException {
        List<Warning> warnings = new ArrayList<>();
        String sql = "SELECT warningID, description, dateFault, isSeriousFault, staffMemberWarnedID FROM warning WHERE staffMemberWarnedID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, staffMemberWarnedID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String warningID = resultSet.getString("warningID");
                    String description = resultSet.getString("description");
                    LocalDate dateFault = resultSet.getDate("dateFault").toLocalDate();
                    boolean isSeriousFault = resultSet.getBoolean("isSeriousFault");

                    Warning warning = new Warning(description, dateFault, isSeriousFault, staffMemberWarnedID);
                    warning.setWarningID(warningID);
                    warnings.add(warning);
                }
            }
        }
        return warnings;
    }

    public Warning getWarningByID(String warningID) throws SQLException {
        String sql = "SELECT warningID, description, dateFault, isSeriousFault, staffMemberWarnedID FROM warning WHERE warningID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, warningID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String description = resultSet.getString("description");
                    LocalDate dateFault = resultSet.getDate("dateFault").toLocalDate();
                    boolean isSeriousFault = resultSet.getBoolean("isSeriousFault");
                    String staffMemberWarnedID = resultSet.getString("staffMemberWarnedID");

                    Warning warning = new Warning(description, dateFault, isSeriousFault, staffMemberWarnedID);
                    warning.setWarningID(warningID);
                    return warning;
                }
            }
        }
        return null;
    }


    public void endConnection() throws EndConnectionException {
        try {
            connection.close();
        }
        catch (SQLException exception) {
            String message = "Impossible de fermer la connection";
            throw new EndConnectionException(message);
        }
    }

}
