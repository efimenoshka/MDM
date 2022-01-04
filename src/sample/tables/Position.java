package sample.tables;

public enum Position {
    All("Все"), Admin("Администратор"), Hair("Парикмахер"), Manicure("Мастер по маникюру");

    String name;

    Position(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
