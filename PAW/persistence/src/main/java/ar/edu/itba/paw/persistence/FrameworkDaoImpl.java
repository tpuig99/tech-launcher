package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Framework;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class FrameworkDaoImpl implements FrameworkDao {
    @Override
    public Framework findById(long id) {
        return new Framework(id, "Angular", "WebDevelopment", "Angular is a framework for dynamic websited." );
    }

    @Override
    public List<Framework> getFrameworks(String category) {
        List<Framework> toReturn = new ArrayList<>();
        if (category != null) {
            toReturn.add(new Framework(1, "Angular", category, "Angular is a framework for dynamic websited." ));
        }
        return toReturn;
    }
}
