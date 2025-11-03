# Assignment II Part 1: Blog Template

## Task 1) Code Analysis and Refactoring ⚙️

### a) From DRY to Design Patterns (6 marks)

[Links to your merge requests](https://nw-syd-gitlab.cseunsw.tech/COMP2511/25T3/students/z5640267/assignment-ii/-/merge_requests/1)

> i. Look inside src/main/java/dungeonmania/entities/enemies. Where can you notice an instance of repeated code? Note down the particular offending lines/methods/fields.

[Answer]
In ZombieToast case "runAway" (line 41-71) is exactly same as Mercenary case "invincible" (line 116-146). The entire “fleeing away from player” logic is copy-pasted. One way to avoid this is to add a method for fleeing in superclass Enemy, calling this methods in "runAway" and "invincible" cases.

> ii. What Design Pattern could be used to improve the quality of the code and avoid repetition? Justify your choice by relating the scenario to the key characteristics of your chosen Design Pattern.

[Answer]
The Strategy Pattern addresses this issue of repetition by encapsulating algorithms (movement behaviours) into separate, interchangeable classes. 
Inside src/main/java/dungeonmania/entities/enemies, each enemy has its own movement and their logics are hard-coded, making the program harder to maintain or extend.
Therefore, I can use strategy patterns to do "move" in a lot of different ways and extract all of these algorithms into separate classes. 

> iii. Using your chosen Design Pattern, refactor the code to remove the repetition.

My refactoring: 
| Strategy Interface  | MoveStrategy |
| Contax class        | DoMovement   |
| Concrete strategies | RandomMovement, DijkstraMovement, FleeMovement |
Step1: The context (DoMovement) holds a strategy. Each enemy object has a field of type DoMovement. This class doesn’t know how to move — it only knows that it has a MoveStrategy object to handle movement.
Step2: The enemy sets the desired strategy. Before moving, the enemy selects which algorithm (strategy) it wants to use. For example, based on potion effects or state.
Step 3: The context delegates the call to the chosen strategy
Step 4: The concrete strategy executes its algorithm. Depending on which strategy was set, one of the following move() implementations runs.

### b) Inheritance Design (6 marks)

[Links to your merge requests](https://nw-syd-gitlab.cseunsw.tech/COMP2511/25T3/students/z5640267/assignment-ii/-/merge_requests/2)

> i. List one design principle that is violated by collectable objects based on the description above. Briefly justify your answer.

[Answer]
Liskov Substitution Principle (LSP) is violated.
Before refactoring, the abstract class InventoryItem declared methods such as applyBuff() and getDurability(), which are only relevant to battle-related items. Non-battle collectables like Wood, Treasure, or Bomb were still forced to override these methods even though they did not logically require them. This breaks the LSP, since not all subclasses of InventoryItem can be substituted for their parent without changing the program’s intended behaviour.

> ii. Refactor the inheritance structure of the code, and in the process remove the design principle violation you identified.

To restore proper inheritance and satisfy LSP, the hierarchy was refactored by introducing a new abstract class InventoryBattle that extends InventoryItem.
    - Battle-related items (e.g. Sword, Shield, Potion) extend InventoryBattle, which defines applyBuff() and getDurability().
    - Non-battle collectables (e.g. Wood, Treasure, Bomb) simply extend InventoryItem, which now only handles collection behaviour and no longer enforces irrelevant methods.
Moreover, in the battle() method within BattleFacade, the loop originally iterating over all InventoryItem objects was updated to only process instances of InventoryBattle. This ensures that only battle-capable items contribute to combat logic while preserving a clean, logically consistent inheritance structure.

### c) Open-Closed Goals (6 marks)

[Links to your merge requests](https://nw-syd-gitlab.cseunsw.tech/COMP2511/25T3/students/z5640267/assignment-ii/-/merge_requests/3)

> i. Do you think the design is of good quality here? Do you think it complies with the open-closed principle? Do you think the design should be changed?

[Answer]
This code works functionally, but it has poor quality as it does not follow good design principles, especially Open–Closed Principle (OCP) and Single Responsibility Principle (SRP).
OCP says "classes should be open for extension, but closed for modification.” In this design, every time a new goal type (e.g. "defeatEnemies", "collectKeys") is added, you must:
    1. Edit the switch in Goal.achieved()
    2. Edit the switch in Goal.toString()
    3. Edit the switch in GoalFactory.createGoal()
That means the code is not closed for modification. Adding a new goal requires editing existing classes.
Moreover, the Goal class currently stores goal data, handles goal logi (checking completion), and Handles string formatting for display. It’s doing too many things

> ii. If you think the design is sufficient as it is, justify your decision. If you think the answer is no, pick a suitable Design Pattern that would improve the quality of the code and refactor the code accordingly.

Factory Method Pattern.
Since there are five distinct goal types, I defined an interface called GoalFactory containing a single method createGoal(). Each goal type (e.g., AND, OR, exit, boulders, treasure) has its own concrete factory class that implements GoalFactory and overrides the createGoal() method to construct the corresponding goal object.

I also created a GoalFactoryRegistry class that serves as a central coordinator. It maintains a mapping between goal type strings and their respective factory instances, allowing the system to distinguish and call the correct factory based on the goal type specified in the JSON configuration.

Finally, the buildGoals() method inside GameBuilder was updated to call GoalFactoryRegistry.create(...) instead of the original GoalFactory.createGoal(...). 

### d) Open Refactoring (12 marks)

[Merge Request 1](https://nw-syd-gitlab.cseunsw.tech/COMP2511/25T3/students/z5640267/assignment-ii/-/merge_requests/5)

Law of Demeter

[BattleFacade line 30]
Before | player.getBattleStatistics().getHealth()         
After  | create a mehtod called getHealth() inside Player. Call it direcetly: double initialPlayerHealth = player.getHealth();

[BattleFacade line 43]
Before | player.getInventory().getEntities(InventoryBattle.class)
After  | create a mehtod called getBattleItemsList() inside Player, returning list of battle items.

Since there are multiple Law of Demeter violations, I created several new helper methods across different classes to improve encapsulation and remove method chaining: 

- public List<Mercenary> getMercenaryList() in Game
- public List<Entity> getEntities() in Game
- public List<BattleResponse> getBattleResponses() in Game
- public boolean achieved(Game game) in Game
- public List<String> getBuildables() in Game
- public <T extends InventoryItem> int count(Class<T> itemType) in Player
- public void removePotionListener(PotionListener listener) in GameMap
- public void battle(Player player, Enemy enemy) in GameMap

These changes ensure that classes interact only with their immediate collaborators (e.g. Player, GameMap, Game) rather than navigating through multiple object layers.

[Merge Request 2](https://nw-syd-gitlab.cseunsw.tech/COMP2511/25T3/students/z5640267/assignment-ii/-/merge_requests/6)

Liskov Substitution Principle (LSP)

Previously, the Entity superclass declared methods such as onMovedAway() and onDestroy() that were inherited by all subclasses, even though only specific entity types (like Switch and Enemy) needed them. For exmpale:
    @Override
    public void onMovedAway(GameMap map, Entity entity) {
        return;
    }
    @Override
    public void onDestroy(GameMap gameMap) {
        return;
    }
This violated the Liskov Substitution Principle (LSP) because subclasses were forced to provide meaningless or empty implementations of methods that did not apply to their behaviour.

To refactor, I removed unnecessary overrides from unrelated subclasses (e.g. those returning immediately in onMovedAway() and onDestroy()). Then, I updated the GameMap.destroyEntity() method to safely call onDestroy() only when the entity is an instance of Enemy.

As a reuslt, subclasses are no longer required to override irrelevant methods. Each entity type now correctly represents its behaviour without empty or redundant method bodies.

[Merge Request 3](https://nw-syd-gitlab.cseunsw.tech/COMP2511/25T3/students/z5640267/assignment-ii/-/merge_requests/7)
Added @SuppressWarnings("removal") above calls to Entity.translate(...) to silence compiler warnings. 



[Merge Request 4](link)

In the original implementation, the constructEntity() method in EntityFactory relied on a long switch statement to determine which type of entity to create. Each case explicitly handled the construction logic for different entities such as Player, Wall, Bomb, Door, and so on. While this approach worked functionally, it violated the Open–Closed Principle of object-oriented design. Every time a new entity type needed to be added or an existing creation logic modified, developers were required to open and modify this switch statement, making the code difficult to maintain and prone to errors.

To address this issue, I refactored the design by introducing a Factory Pattern using an EntityBuilder interface and individual factory classes for each entity type. Instead of hardcoding every creation rule inside one large method, each entity now has its own dedicated factory class (e.g., BombFactory, PortalFactory, DoorFactory, etc.) inside the file EntityFactories responsible for creating that entity based on the given JSON data and configuration file. 

This change not only satisfies the Open–Closed Principle but also greatly improves Single Responsibility. Since each entity’s creation logic now resides in its own dedicated factory class, the EntityFactory class itself no longer contains entity-specific logic or numerous buildXYZ() methods.



## Task 2) Evolution of Requirements 🔧

[DELETE ONE OF THESE!]

### Sun Stone & More Buildables (20 marks)

[Links to your merge requests](/put/links/here)

**Assumptions**

[Any assumptions made]

**Design**

[Design]

**Changes after review**

[Design review/Changes made]

**Test list**

[Test List]

**Other notes**

[Any other notes]

### Logic Switches (30 marks)

[Links to your merge requests](/put/links/here)

**Assumptions**

[Any assumptions made]

**Design**

[Design]

**Changes after review**

[Design review/Changes made]

**Test list**

[Test List]

**Other notes**

[Any other notes]
