package dungeonmania.goals;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class GoalFactoryRegistry {
    private static Map<String, GoalFactory> factories = new HashMap<>();

    static {
        factories.put("AND", new GoalAndFactory());
        factories.put("OR", new GoalOrFactory());
        factories.put("exit", new GoalExitFactory());
        factories.put("boulders", new GoalBouldersFactory());
        factories.put("treasure", new GoalTreasureFactory());
    }

    public static Goal create(JSONObject jsonGoal, JSONObject config) {
        String type = jsonGoal.getString("goal");
        GoalFactory factory = factories.get(type);
        if (factory == null)
            throw new IllegalArgumentException("Unknown goal type: " + type);
        return factory.createGoal(jsonGoal, config);
    }
}
