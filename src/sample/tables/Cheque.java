package sample.tables;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Cheque {
    private int id;
    private TypeOfService nameService;
    private Customer customer;
    private Worker worker;
    private String cost;
    private Date date;
    private String time;
    private Status status;

    private SimpleDateFormat simpleDate = new SimpleDateFormat("dd.MM.yy");
    private String dateStr;
    private String timeStr;
    private String costStr;

    public Cheque(int id, TypeOfService nameService, Customer customer, Worker worker, Date date, String time, int status) {
        this.id = id;
        this.nameService = nameService;
        this.customer = customer;
        this.worker = worker;
        this.date = date;
        this.time = time;
        this.status = Status.values()[status];

        this.cost = nameService.getPrice();
        this.cost = cost.split("\\.")[0];
        dateStr = simpleDate.format(date);
        timeStr = time.split("\\.")[0];
    }

    public Cheque(TypeOfService nameService, Customer customer, Worker worker, Date date, String time) {
        this.nameService = nameService;
        this.customer = customer;
        this.worker = worker;
        this.date = date;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TypeOfService getNameService() {
        return nameService;
    }

    public void setNameService(TypeOfService nameService) {
        this.nameService = nameService;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
