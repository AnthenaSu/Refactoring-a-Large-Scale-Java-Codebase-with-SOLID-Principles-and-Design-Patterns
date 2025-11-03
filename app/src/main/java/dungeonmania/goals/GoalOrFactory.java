package dungeonmania.goals;

import org.json.JSONArray;
import org.json.JSONObject;

public class GoalOrFactory implements GoalFactory {

    @Override
    public Goal createGoal(JSONObject jsonGoal, JSONObject config) {
        JSONArray subgoals = jsonGoal.getJSONArray("subgoals");
        Goal goal1 = GoalFactoryRegistry.create(subgoals.getJSONObject(0), config);
        Goal goal2 = GoalFactoryRegistry.create(subgoals.getJSONObject(1), config);
        return new OrGoal(goal1, goal2);
    }
}
