package com.github.ethpalser.menu.event;

/**
 * Executable describes an object that has an action that can be performed. This action accepts arguments that can
 * change how its actions are performed.
 * <br><br>
 * Executable can similarly be described as submittable or runnable, as both are forms of actions being performed.
 * Although, it is best to keep these alternative descriptors separate, as they may imply asynchronous execution,
 * whereas executables are synchronous and may be implemented to be asynchronous.
 */
public interface Executable {

    /**
     * Perform an action of the executable.
     *
     * @param args Arguments for customizing execution
     * @return Result of the execution
     */
    Result onExecute(String[] args);

}
