package me.coolaid.enhancedkeybinds.event;

public interface InterKeyConflictContext {

    boolean isActive();

    boolean conflicts(InterKeyConflictContext other);
}