package ar.edu.itba.paw.models;

import java.util.ArrayList;
import java.util.List;

public enum FrameworkType {
    Online_Plataform("Online Platform"),
    Framework("Framework"),
    Service("Service"),
    Database_System("Database System"),
    Programming_Languages("Programming Language"),
    Operating_System("Operating System"),
    Runtime_Plataform("Runtime Platform"),
    Libraries("Libraries"),
    Engine("Engine"),
    Shell("Shell"),
    Terminal("Terminal"),
    Application("Application"),
    Text_Editor("Text Editor"),
    CSS_Modifier("CSS Modifier"),
    API("API"),
    Toolkit("Toolkit"),
    IDE("IDE");
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

    public static List<String> getAllTypes(){
        List<String> types = new ArrayList<>();
        for (FrameworkType ft:FrameworkType.values()) {
            types.add(ft.type);
        }
        return types;
    }


}
