package dungeonmania;

/**
 * ComparableCallback is a wrapper around a Runnable
 * For every runnable r, we associate a value v to it
 * higher v means lower priority
 * the callback is performed based on one entity,
 * whose entityId is attached to the callback
 */
public class ComparableCallback implements Comparable<ComparableCallback>, Runnable {
    /** Inner callback */
    private Runnable r;
    /** Priority (higher means lower priority) */
    private int v;
    /**
     * ID of this callback, used to look it up and cancel it.
     *
     * Multiple callbacks can have the same ID, meaning that attempting to cancel one will cancel all others with the
     * same ID. Useful for making callbacks cancel when an entity is destroyed.
     */
    private String id;
    /** used to invalidate the current callback */
    private boolean isValid = true;
    /** some callbacks can only be used once */
    private boolean once = false;

    /**
     * Construct a ComparableCallback
     * @param r runnable to be executed
     * @param v priority. Higher `v` means lower priority
     * @param id ID of this callback
     * @param once whether to only run this callback once (default is `false`
     * meaning it will run repeatedly)
     */
    public ComparableCallback(Runnable r, int v, String id, boolean once) {
        this.r = r;
        this.v = v;
        this.id = id;
        this.once = once;
    }

    /**
     * Construct a ComparableCallback that will run infinitely
     * @param r runnable to be executed
     * @param v priority. Higher `v` means lower priority
     * @param entityId ID of this callback
     */
    public ComparableCallback(Runnable r, int v, String entityId) {
        this(r, v, entityId, false);
    }

    @Override
    public void run() {
        if (isValid)
            r.run();
        if (once)
            invalidate();
    }

    @Override
    public int compareTo(ComparableCallback arg0) {
        return Integer.compare(v, arg0.v);
    }

    /** Return the callback's ID */
    public String getId() {
        return this.id;
    }

    /** Invalidate the callback so it will not be run anymore */
    public void invalidate() {
        this.isValid = false;
    }

    /**
     * Return whether the callback is valid (meaning it should be run in future)
     */
    public boolean isValid() {
        return isValid;
    }
}
