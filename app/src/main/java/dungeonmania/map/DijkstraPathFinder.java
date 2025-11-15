package dungeonmania.map;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Portal;
import dungeonmania.util.Position;

public class DijkstraPathFinder {

    public static Position findNext(GameMap map, Position src, Position dest, Entity entity) {
        Map<Position, Integer> dist = new HashMap<>();
        Map<Position, Position> prev = new HashMap<>();
        Map<Position, Boolean> visited = new HashMap<>();

        // invalid inputs → don't move
        if (!map.containsNode(src) || !map.containsNode(dest)) {
            return src;
        }

        dist.put(src, 0);
        prev.put(src, null);

        PriorityQueue<Position> pq = new PriorityQueue<>(
            Comparator.comparingInt(p -> dist.getOrDefault(p, Integer.MAX_VALUE))
        );
        pq.add(src);

        while (!pq.isEmpty()) {
            Position curr = pq.poll();

            if (curr.equals(dest) || dist.get(curr) > 200)
                break;

            // Handle portals:
            if (map.hasPortal(curr)) {
                Portal portal = map.getPortal(curr);
                for (Position p : portal.getDestPositions(map, entity)) {
                    if (!visited.containsKey(p)) {
                        dist.put(p, dist.get(curr));
                        prev.put(p, prev.get(curr));
                        pq.add(p);
                    }
                }
                continue;
            }

            visited.put(curr, true);

            for (Position n : curr.getCardinallyAdjacentPositions()) {
                if (visited.containsKey(n)) continue;
                if (!map.canMoveTo(entity, n)) continue;

                int newDist = dist.get(curr) + map.getTileWeight(n);
                if (newDist < dist.getOrDefault(n, Integer.MAX_VALUE)) {
                    dist.put(n, newDist);
                    prev.put(n, curr);
                    pq.remove(n);
                    pq.add(n);
                }
            }
        }

        // reconstruct first move
        Position ret = dest;
        if (prev.get(ret) == null || ret.equals(src))
            return src;

        while (!prev.get(ret).equals(src)) {
            ret = prev.get(ret);
        }
        return ret;
    }
}
