package org.warzone.logging;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an Observable object as part of the Observer design pattern.
 * This interface allows objects to register as observers and be notified of changes.
 */
public interface Observable {

    /**
     * List to hold all the observers interested in notifications from this observable.
     */
    List<Observer> d_observers = new ArrayList<>();

    /**
     * Adds an observer to the list of observers.
     *
     * @param p_observer The observer to be added.
     */
    default void addObserver(Observer p_observer) {
        d_observers.add(p_observer);
    }

    /**
     * Removes an observer from the list of observers.
     *
     * @param p_observer The observer to be removed.
     */
    default void removeObserver(Observer p_observer) {
        d_observers.remove(p_observer);
    }

    /**
     * Notifies all registered observers of a change.
     */
    default void notifyObservers() {
        for (Observer l_observer : d_observers) {
            l_observer.update(this);
        }
    }
}
