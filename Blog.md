# Assignment II Part 1: Blog Template

## Task 1) Code Analysis and Refactoring ⚙️

### a) From DRY to Design Patterns (6 marks)

[Links to your merge requests](/put/links/here)

> i. Look inside src/main/java/dungeonmania/entities/enemies. Where can you notice an instance of repeated code? Note down the particular offending lines/methods/fields.

[Answer]
In ZombieToast case "runAway" (line 41-71) is exactly same as Mercenary case "invincible" (line 116-146). The entire “fleeing away from player” logic is copy-pasted. One way to avoid this is to add a method for fleeing in superclass Enemy, calling this methods in "runAway" and "invincible" cases.

> ii. What Design Pattern could be used to improve the quality of the code and avoid repetition? Justify your choice by relating the scenario to the key characteristics of your chosen Design Pattern.

[Answer]
The Strategy Pattern addresses this issue of repetition by encapsulating algorithms (movement behaviours) into separate, interchangeable classes. 
Inside src/main/java/dungeonmania/entities/enemies, each enemy has its own movement and their logics are hard-coded, making the program harder to maintain or extend.
Therefore, we can use strategy patterns to do "move" in a lot of different ways and extract all of these algorithms into separate classes. 

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

[Links to your merge requests](/put/links/here)

> i. List one design principle that is violated by collectable objects based on the description above. Briefly justify your answer.

[Answer]

> ii. Refactor the inheritance structure of the code, and in the process remove the design principle violation you identified.

[Briefly explain what you did]

### c) Open-Closed Goals (6 marks)

[Links to your merge requests](/put/links/here)

> i. Do you think the design is of good quality here? Do you think it complies with the open-closed principle? Do you think the design should be changed?

[Answer]

> ii. If you think the design is sufficient as it is, justify your decision. If you think the answer is no, pick a suitable Design Pattern that would improve the quality of the code and refactor the code accordingly.

[Briefly explain what you did]

### d) Open Refactoring (12 marks)

[Merge Request 1](/put/links/here)

[Briefly explain what you did]

[Merge Request 2](/put/links/here)

[Briefly explain what you did]

Add all other changes you made in the same format here:

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
