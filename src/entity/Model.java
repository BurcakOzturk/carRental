package entity;

import core.ComboItem;

public class Model {
    private int id;
    private int brand_id;
    private String name;
    private Type type;
    private String year;
    private Fuel fuel;
    private Transmission transmission;
    private Brand brand;

    public enum Fuel {
        Gasoline,
        Lpg,
        Electric,
        Diesel
    }

    public enum Transmission {
        Manual,
        Auto
    }

    public enum Type {
        Sedan,
        Hatchback
    }

    public Model() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(int brand_id) {
        this.brand_id = brand_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Fuel getFuel() {
        return fuel;
    }

    public void setFuel(Fuel fuel) {
        this.fuel = fuel;
    }

    public Transmission getTransmission() {
        return transmission;
    }

    public void setTransmission(Transmission transmission) {
        this.transmission = transmission;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public ComboItem getComboItem() {
        return new ComboItem(this.getId(), this.getBrand().getName() + " - " + this.getName() + " - " + this.getYear() + " - " + this.getTransmission());
    }

    @Override
    public String toString() {
        return "Model{" +
                "id=" + id +
                ", brand_id=" + brand_id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", year='" + year + '\'' +
                ", fuel=" + fuel +
                ", transmission=" + transmission +
                ", brand=" + brand +
                '}';
    }
}
