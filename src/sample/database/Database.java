package sample.database;

import sample.tables.Customer;
import sample.tables.Cheque;
import sample.tables.TypeOfService;
import sample.tables.Worker;

import java.sql.*;
import java.util.ArrayList;
import java.util.stream.Stream;

import static sample.database.Tables.*;

public class Database {
    protected String dbHost = "localhost\\SQLEXPRESS";
    protected String dbName = "IS_Hairdresser";
    protected String dbUser = "admin";
    protected String dbPass = "1234";

    protected String dbConnectString = "jdbc:sqlserver://" + dbHost + ";database=" + dbName;

    public Connection dbConnection;

    public Connection getDbConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dbConnection = DriverManager.getConnection(dbConnectString, dbUser, dbPass);
        return dbConnection;
    }

    public Worker getWorker(String login, String password) {
        Worker worker = null;
        try {
            String request = "SELECT * FROM " + WORKER_TABLE + " WHERE " + WORKER_LOGIN + "=? AND " + WORKER_PASSWORD + "=?";
            PreparedStatement prSt = getDbConnection().prepareStatement(request);
            prSt.setString(1, login);
            prSt.setString(2, password);

            ResultSet pox = prSt.executeQuery();
            if (pox.next()) {
                worker = new Worker(
                        pox.getInt(1),
                        pox.getString(2),
                        pox.getString(3),
                        pox.getString(4),
                        pox.getString(5),
                        pox.getString(6),
                        pox.getString(7)
                );
            }
        }
        catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return worker;
    }

    public TypeOfService getTypeServiceById(int id) {
        TypeOfService typeOfService = null;
        String request = "SELECT * FROM " + TYPE_OF_SERVICE_TABLE + " WHERE " + TYPE_OF_SERVICE_ID + "=?";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(request);
            prSt.setInt(1, id);
            ResultSet pox = prSt.executeQuery();
            if (pox.next()) {
                typeOfService = new TypeOfService(
                        pox.getInt(1),
                        pox.getString(2),
                        pox.getString(3),
                        pox.getString(4),
                        pox.getString(5)
                );
            }
        }
        catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return typeOfService;
    }

    public Customer getClientById(int id) {
        Customer client = null;
        String request = "SELECT * FROM " + CUSTOMER_TABLE + " WHERE " + CUSTOMER_ID + "=?";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(request);
            prSt.setInt(1, id);
            ResultSet pox = prSt.executeQuery();
            if (pox.next()) {
                client = new Customer(
                        pox.getInt(1),
                        pox.getString(2),
                        pox.getString(3)
                );
            }
        }
        catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return client;
    }

    public Worker getWorkerById(int id) {
        Worker worker = null;
        String request = "SELECT * FROM " + WORKER_TABLE + " WHERE " + WORKER_ID + "=?";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(request);
            prSt.setInt(1, id);
            ResultSet pox = prSt.executeQuery();
            if (pox.next()) {
                worker = new Worker(
                        pox.getInt(1),
                        pox.getString(2),
                        pox.getString(3),
                        pox.getString(4),
                        pox.getString(5),
                        pox.getString(6),
                        pox.getString(7)
                );
            }
        }
        catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return worker;
    }

    public ArrayList<Cheque> getListCheque(){
        ArrayList<Cheque> cheques = new ArrayList<>();
        String request = "SELECT * FROM " + CHEQUE_TABLE;
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(request);
            ResultSet pox = prSt.executeQuery();
            while (pox.next()){
                cheques.add(new Cheque(
                        pox.getInt(1),
                        getTypeServiceById(pox.getInt(2)),
                        getClientById(pox.getInt(3)),
                        getWorkerById(pox.getInt(4)),
                        pox.getDate(5),
                        pox.getString(6)
                ));
            }
        }
        catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return cheques;
    }

    public ArrayList<TypeOfService> getListTypeOfService(){
        ArrayList<TypeOfService> nameService = new ArrayList<>();
        String request = "SELECT * FROM " + TYPE_OF_SERVICE_TABLE;
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(request);
            ResultSet pox = prSt.executeQuery();
            while (pox.next()){
                nameService.add(new TypeOfService(
                        pox.getInt(1),
                        pox.getString(2),
                        pox.getString(3),
                        pox.getString(4),
                        pox.getString(5)
                ));
            }
        }
        catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return nameService;
    }

    public ArrayList<Worker> getListWorkersByPosition(String position){
        ArrayList<Worker> workerPosition = new ArrayList<>();
        String request = "SELECT * FROM " + WORKER_TABLE + " WHERE " + WORKER_POSITION + "=?";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(request);
            prSt.setString(1, position);
            ResultSet pox = prSt.executeQuery();
            while (pox.next()){
                workerPosition.add(new Worker(
                        pox.getInt(1),
                        pox.getString(2),
                        pox.getString(3),
                        pox.getString(4),
                        pox.getString(5),
                        pox.getString(6),
                        pox.getString(7)
                ));
            }
        }
        catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return workerPosition;
    }

    public ArrayList<Worker> getListWorkers(){
        ArrayList<Worker> workerPosition = new ArrayList<>();
        String request = "SELECT * FROM " + WORKER_TABLE;
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(request);
            ResultSet pox = prSt.executeQuery();
            while (pox.next()){
                workerPosition.add(new Worker(
                        pox.getInt(1),
                        pox.getString(2),
                        pox.getString(3),
                        pox.getString(4),
                        pox.getString(5),
                        pox.getString(6),
                        pox.getString(7)
                ));
            }
        }
        catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return workerPosition;
    }

    public ArrayList<Customer> getListCustomers(){
        ArrayList<Customer> customers = new ArrayList<>();
        String request = "SELECT * FROM " + CUSTOMER_TABLE;
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(request);
            ResultSet pox = prSt.executeQuery();
            while (pox.next()){
                customers.add(new Customer(
                        pox.getInt(1),
                        pox.getString(2),
                        pox.getString(3)
                ));
            }
        }
        catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return customers;
    }

    //добавление заказа в БД
    public void addCheque(Cheque cheque){
        String request = "INSERT INTO " + CHEQUE_TABLE + "(" + CHEQUE_NAME_SERVICE_ID + "," + CHEQUE_CUSTOMER_ID + "," +
                CHEQUE_WORKER_ID + "," + CHEQUE_DATE + "," + CHEQUE_TIME + ")" + "VALUES(?,?,?,?,?)";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(request);
            prSt.setInt(1, cheque.getNameService().getId());
            prSt.setInt(2, cheque.getCustomer().getId());
            prSt.setInt(3, cheque.getWorker().getId());
            prSt.setObject(4, cheque.getDate(), Types.DATE);
            prSt.setObject(5, cheque.getTime(), Types.TIME);
            prSt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //add new client
    public Customer addGetNewCustomer(Customer customer){
        Customer newCustomer = null;
        String request1 = "SELECT * FROM " + CUSTOMER_TABLE + " WHERE " +
                CUSTOMER_NAME + "=? AND " + CUSTOMER_TELEPHONE + "=?";
        String request = "INSERT INTO " + CUSTOMER_TABLE +
                "(" + CUSTOMER_NAME + "," + CUSTOMER_TELEPHONE + ")" + "VALUES(?,?)";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(request);
            prSt.setString(1, customer.getName());
            prSt.setString(2, customer.getTelephone());
            prSt.executeUpdate();

            PreparedStatement prSt1 = getDbConnection().prepareStatement(request1);
            prSt1.setString(1, customer.getName());
            prSt1.setString(2, customer.getTelephone());
            ResultSet resultSet = prSt1.executeQuery();
            if (resultSet.next()) {
                newCustomer = new Customer(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3)
                );
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return newCustomer;
    }
}
