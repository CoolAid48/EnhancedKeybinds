package me.coolaid.enhancedkeybinds.event;

/**
 * Defines the context that a {@link net.minecraft.client.option.KeyBinding} is used.
 * Key conflicts occur when a {@link net.minecraft.client.option.KeyBinding} has the same {@link InterKeyConflictContext} and has conflicting modifiers and keyCodes.
 */
public interface InterKeyConflictContext {
    /**
     * @return true if conditions are met to activate {@link net.minecraft.client.option.KeyBinding}s with this context
     */
    boolean isActive();

    /**
     * @return true if the other context can have {@link net.minecraft.client.option.KeyBinding} conflicts with this one.
     * This will be called on both contexts to check for conflicts.
     */
    boolean conflicts(InterKeyConflictContext other);
}