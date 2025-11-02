package dungeonmania.goals;

import org.json.JSONObject;

public interface GoalFactory {
    Goal createGoal(JSONObject jsonGoal, JSONObject config);
}
