package sample.tables;

public class TypeOfService {
    private int id;
    private String name;
    private String time;
    private String price;
    private String position;

    private String priceStr;

    public TypeOfService(int id, String name, String time, String price, String position) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.price = price;
        this.position = position;

        priceStr = price.split("\\.")[0];
    }

    public TypeOfService(String name, String time, String price, String position) {
        this.name = name;
        this.time = time;
        this.price = price;
        this.position = position;
    }

    public TypeOfService(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPriceStr() {
        return priceStr;
    }

    public void setPriceStr(String priceStr) {
        this.priceStr = priceStr;
    }

    @Override
    public String toString() {
        return name;
    }
}
