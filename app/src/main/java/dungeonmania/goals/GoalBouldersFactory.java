package dungeonmania.goals;

import org.json.JSONObject;

public class GoalBouldersFactory implements GoalFactory {
    @Override
    public Goal createGoal(JSONObject jsonGoal, JSONObject config) {
        return new Goal("boulders");
    }
}
