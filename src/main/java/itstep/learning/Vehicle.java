package itstep.learning;

public abstract class Vehicle {
    private String name;

    public Vehicle() {
    }

    public Vehicle(String name) {
        this.setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract String getInfo();

}