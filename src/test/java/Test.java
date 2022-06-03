import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.*;

import java.util.HashMap;

public class Test {
    public static void main(String[] args) throws JsonProcessingException {
        HashMap<String, Integer> deathCount = new HashMap<>(3);
        deathCount.put("colmmurphyxyz", 3);
        deathCount.put("Bob", 9);
        deathCount.put("Arthur", 0);

        String j = JSONStringer.valueToString(deathCount);

        System.out.println(j);

        HashMap<String, Integer> dc = new ObjectMapper().readValue(j, HashMap.class);
    }
}
