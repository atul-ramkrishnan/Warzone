package org.warzone.logging;

/**
 * The {@code Observer} interface is part of the Observer design pattern.
 * It should be implemented by any class that needs to be notified of changes in an {@code Observable} object.
 */
public interface Observer {
    /**
     * This method is called whenever the observed object is changed.
     * An application calls an {@code Observable} object's
     * {@code notifyObservers} method to have all the object's
     * observers notified of the change.
     *
     * @param p_observable the observable object.
     */
    void update(Observable p_observable);
}
