package sample.tables;

public enum Status {
    PROCESS("Будущий"), END("Завершен");

    String name;

    Status(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
