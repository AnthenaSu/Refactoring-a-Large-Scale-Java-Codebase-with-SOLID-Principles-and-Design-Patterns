package dungeonmania.goals;

import org.json.JSONObject;

public class GoalExitFactory implements GoalFactory {
    @Override
    public Goal createGoal(JSONObject jsonGoal, JSONObject config) {
        return new ExitGoal();
    }
}
