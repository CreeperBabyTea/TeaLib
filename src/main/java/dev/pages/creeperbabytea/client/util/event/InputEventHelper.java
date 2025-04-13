package dev.pages.creeperbabytea.client.util.event;

import net.neoforged.neoforge.client.event.InputEvent;
import org.lwjgl.glfw.GLFW;

public class InputEventHelper {
    /*---------- mouse events ----------*/

    public static boolean isPress(InputEvent.MouseButton event) {
        return event.getAction() == GLFW.GLFW_PRESS;
    }

    public static boolean isRelease(InputEvent.MouseButton event) {
        return event.getAction() == GLFW.GLFW_RELEASE;
    }

    public static boolean isRightButton(InputEvent.MouseButton event) {
        return event.getButton() == GLFW.GLFW_MOUSE_BUTTON_RIGHT;
    }

    public static boolean isLeftButton(InputEvent.MouseButton event) {
        return event.getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT;
    }

    public static boolean isShiftHeld(InputEvent.MouseButton event) {
        return (event.getModifiers() & GLFW.GLFW_MOD_SHIFT) != 0;
    }

    public static boolean isAltHeld(InputEvent.MouseButton event) {
        return (event.getModifiers() & GLFW.GLFW_MOD_ALT) != 0;
    }

    public static boolean isCtrlHeld(InputEvent.MouseButton event) {
        return (event.getModifiers() & GLFW.GLFW_MOD_CONTROL) != 0;
    }
}
