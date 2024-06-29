package com.github.ethpalser.menu.event;

/**
 * Displayable describes objects that are expected to be rendered on an interface. A displayable object is a container
 * for interface information, and is not expected to handle rendering its information. Instead, it performs actions
 * when its information is displayed, and the renderer is responsible for calling these actions.
 * <br><br>
 * Displayable objects should be solely responsible for managing its information, and altering its information
 * by another class is not recommended. The methods preRender() and postRender() can be used to perform actions
 * to modify its content. It is not recommended to use onRender() to modify its content, as it can be unclear whether
 * onRender() occurs before, during or after the renderer displays the content.
 */
public interface Displayable<T> {

    /**
     * Determines if this displayable object should be rendered.
     *
     * @return boolean (true/false)
     */
    boolean isVisible();

    /**
     * Updates the visibility of the object. Can be used to toggle it visible/invisible.
     *
     * @param isVisible boolean (true/false)
     */
    void setVisible(boolean isVisible);

    /**
     * Fetches the display that will be rendered.
     *
     * @return Object that will be consumed by a renderer.
     */
    T getDisplay();

    /**
     * Updates the display to be rendered.
     *
     * @param display Object with type consumed by a renderer.
     */
    void setDisplay(T display);

    /**
     * Perform an action before this is displayed.
     *
     * @return Result of the action
     */
    default Result preRender() {
        return new Result();
    }

    /**
     * Perform an action when this is displayed. This may be before, during or after depending on implementation.
     *
     * @return Result of the action
     */
    default Result onRender() {
        return new Result();
    }

    /**
     * Perform an action after this is displayed.
     *
     * @return Result of the action
     */
    default Result postRender() {
        return new Result();
    }

}
