package com.github.ethpalser;

/**
 * Selectable describes an object that can be chosen, such as from a group of one or more selectable options.
 * The expected use of Selectable objects are within a user interface, where every element that can be interacted with
 * is Selectable.
 *
 */
public interface Selectable {

    /**
     * Returns whether this Selectable is available to be selected. It is expected that its availability is checked
     * before rendering the Selectable or executing onSelect(), however its enforcement depends on the implementation.
     *
     * @return boolean (true/false)
     */
    boolean isAvailable();

    /**
     * Perform an action once this Selectable has been chosen.
     *
     * @return Result containing whether the onSelect had an error and message that should be displayed
     * @see Result
     */
    Result onSelect(String[] args);

}
