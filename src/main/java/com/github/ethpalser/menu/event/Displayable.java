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
public interface Displayable {

    /**
     * Perform an action before this is displayed.
     *
     * @return Result of the action
     */
    Result preRender();

    /**
     * Perform an action when this is displayed. This may be before, during or after depending on implementation.
     *
     * @return Result of the action
     */
    Result onRender();

    /**
     * Perform an action after this is displayed.
     *
     * @return Result of the action
     */
    Result postRender();


}
