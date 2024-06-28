package com.github.ethpalser.menu.event;

import com.github.ethpalser.Result;

/**
 * Selectable describes an object that can be chosen, such as from a group of one or more options.
 * <br><br>
 * Selectable can similarly be described as something that can be targeted, highlighted, or registered.
 * <br><br>
 * The intended use for Selectable is to allow interactions with the Selectable object. Additionally, an action
 * may be performed when the object is being treated as selected. A selected object should not be mistaken as an
 * submitted object.
 *
 */
public interface Selectable {

    /**
     * Returns whether this Selectable is available to be selected. It is expected that its availability is checked
     * before performing actions with this object, such as submit, however enforcement depends on its implementation.
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
