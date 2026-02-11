# Refactoring a Large-Scale Java Codebase with SOLID Principles and Design Patterns
This project focused on analysing &amp; refactoring a large Java game codebase using OOP principles to improve maintainability, extensibility, and design quality. It involved identifying design smells, fixing flawed inheritance structures, and applying appropriate object-oriented design patterns, while keeping the system fully functional and CI-clean.


## Overview: Project Goals & Learning Outcomes
This project was designed to simulate real-world software engineering challenges and focused on five key areas of object-oriented development:
#### Working with an existing system
- I acclimatised to a large, unfamiliar Java codebase, learning to understand, navigate, and modify software I did not build from scratch—an essential industry skill.
#### Refactoring techniques
- I identified design smells and intentional flaws in the system and refactored them incrementally, ensuring the codebase was left in a cleaner, more maintainable state after each change.
#### Applying design patterns
- I recognised recurring structural and behavioural problems in the code and applied appropriate object-oriented design patterns to improve code quality and reduce coupling.
#### Evolving requirements
The system was extended and reshaped to support new behaviours.
#### Dealing with uncertainty
- I regularly encountered incomplete understanding, ambiguous behaviour, and undocumented logic, and addressed these by investigating the codebase, reasoning about intent, and refining solutions through iteration.

---

## 1. Removing Code Duplication with the Strategy Pattern
### Problem
In `src/main/java/dungeonmania/entities/enemies`, I identified duplicated movement logic:
- `ZombieToast.runAway()` (lines 41–71)
- `Mercenary.invincible()` (lines 116–146)

Both contained identical “flee from player” behaviour, violating DRY and increasing maintenance risk.

### Solution: Strategy Pattern
Enemy movement behaviours vary based on state (random, hostile, fleeing, path-finding), but were previously hard-coded inside enemy classes.

I refactored this using the **Strategy Pattern**, encapsulating movement algorithms into separate classes.

### Final Design
- Introduced a `MoveStrategy` interface
- Implemented concrete strategies:
  - `RandomMovement`
  - `DijkstraMovement`
  - `FleeMovement`
- Replaced a dedicated context class with a static `Map<String, MoveStrategy>`
- Enemies select behaviour by key (e.g. `"random"`, `"runAway"`)

**Benefits**
- Eliminates duplicated code
- Fully open for extension
- No enemy class changes needed to add new movement behaviour
- Cleaner responsibilities and reduced coupling

---

## 2. Fixing Inheritance Design in Collectable Items

### Problem
The original `InventoryItem` abstraction forced all collectables to implement:
- `applyBuff()`
- `getDurability()`

This included non-combat items such as `Wood`, `Treasure`, and `Bomb`, which do not participate in battle logic.

### Principle Violated
**Liskov Substitution Principle (LSP)**  
Subclasses were forced to provide meaningless implementations, making them invalid substitutes for their parent type.

### Refactor
- Introduced `InventoryBattle extends InventoryItem`
- Battle items (`Sword`, `Shield`, `Potion`) extend `InventoryBattle`
- Non-battle collectables extend `InventoryItem` directly
- Updated battle logic to process only `InventoryBattle` instances

This restored logical correctness and removed irrelevant behaviour from non-combat items.

---

## 3. Refactoring the Goals System (Open–Closed Principle)

### Problem
The goals system relied on multiple `switch` statements across:
- `Goal.achieved()`
- `Goal.toString()`
- `GoalFactory.createGoal()`

Adding a new goal required modifying several existing classes, violating:
- Open–Closed Principle (OCP)
- Single Responsibility Principle (SRP)

### Solution: Factory Method Pattern
- Created a `GoalFactory` interface
- Implemented one concrete factory per goal type (AND, OR, exit, boulders, treasure)
- Added a `GoalFactoryRegistry` mapping goal types to factories
- Updated `GameBuilder` to use the registry

**Result**
- New goals can be added without modifying existing code
- Clear separation between goal creation, logic, and representation
- Improved extensibility and maintainability

---

## 4. Additional Refactoring Improvements

### Law of Demeter
Removed deep method chaining by introducing helper methods such as:
- `Player.getHealth()`
- `Player.getBattleItemsList()`
- `Game.getEntities()`, `getMercenaryList()`, `getBuildables()`

This improved encapsulation and reduced coupling.

---

### Liskov Substitution Principle (Entities)
Previously, all entities inherited methods like:
- `onMovedAway()`
- `onDestroy()`

Many subclasses were forced to override them with empty bodies.

**Fix**
- Removed meaningless overrides
- Updated `GameMap.destroyEntity()` to call `onDestroy()` only where applicable

Subclasses now only implement behaviour that applies to them.

---

### Deprecated API Warnings
- Addressed compiler warnings using:
  ```java
  @SuppressWarnings("removal")

## Additional Notes
For more detailed explanations, design rationale, and refactoring discussions, please refer to Blog.md included in this repository.
