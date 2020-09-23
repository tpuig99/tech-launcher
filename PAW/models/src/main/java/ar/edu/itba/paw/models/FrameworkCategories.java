package ar.edu.itba.paw.models;

public enum FrameworkCategories {
    Platforms("Platforms"),
    Front_End_Development("Front-End Development"),
    Back_End_Development("Back-End Development"),
    Computer_Science("Computer Science"),
    Big_Data("Big Data"),
    Artificial_Intelligence("Artificial Intelligence"),
    Editors("Editors"),
    Gaming("Gaming"),
    Development_Environment("Development Environment"),
    Entertainment("Entertainment"),
    Databases("Databases"),
    Media("Media"),
    Security("Security"),
    Business("Business"),
    Networking("Networking"),
    Decentralized_Systems("Decentralized Systems"),
    Higher_Education("Higher Education"),
    Events("Events"),
    Testing("Testing"),
    Miscellaneous("Miscellaneous"),
    Related("Related"),
    Functional("Functional"),
    OO("Object Oriented"),
    Imperative("Imperative");
    private String nameCat;
    FrameworkCategories(String nameCat) {
        this.nameCat=nameCat;
    }

    FrameworkCategories() {
    }

    public String getNameCat() {
        return nameCat;
    }

    public static FrameworkCategories getByName(String name){
        for (FrameworkCategories fc:FrameworkCategories.values()) {
            if(fc.nameCat.equals(name)){
                return fc;
            }
        }
        return null;
    }

    
}
