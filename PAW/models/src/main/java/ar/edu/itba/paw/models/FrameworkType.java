package ar.edu.itba.paw.models;

public enum FrameworkType {
    Online_Plataform("Online plataform"),
    Framework("Framework"),
    Service("Service"),
    Database_System("Database System"),
    Programming_Languages("Programming Languages"),
    Operating_System("Operating System");
    private String type;
    FrameworkType(String type) {
        this.type=type;
    }

    FrameworkType() {
    }

    public String getType() {
        return type;
    }

    public static FrameworkType getByName(String name){
        for (FrameworkType ft:FrameworkType.values()) {
            if(ft.type.equals(name)){
                return ft;
            }
        }
        return null;
    }


}
