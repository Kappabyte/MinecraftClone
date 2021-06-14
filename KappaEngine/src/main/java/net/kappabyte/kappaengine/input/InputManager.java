package net.kappabyte.kappaengine.input;

import java.util.ArrayList;
import java.util.HashMap;

import org.joml.Vector2d;
import org.lwjgl.glfw.GLFW;

import net.kappabyte.kappaengine.util.Log;
import net.kappabyte.kappaengine.window.Window;

public class InputManager {

    private Window window;

    private Vector2d mouseLocation;
    private Vector2d mouseDelta;

    private boolean focused = true;

    private ArrayList<Input> down = new ArrayList<>();
    private ArrayList<Input> held = new ArrayList<>();
    private ArrayList<Input> up = new ArrayList<>();

    public InputManager(Window window) {
        this.window = window;

        mouseLocation = new Vector2d(0.5, 0.5);

        GLFW.glfwSetCursorPosCallback(window.getHandle(), (handle, x, y) -> {
            mouseLocation.x = x;
            mouseLocation.y = y;
        });
        GLFW.glfwSetWindowFocusCallback(window.getHandle(), (handle, focused) -> {
            this.focused = focused;
        });
        GLFW.glfwSetCursorEnterCallback(window.getHandle(), (handle, entered) -> {
            if(entered) {
                down.remove(Input.MOUSE_INSIDE_WINDOW);
                held.remove(Input.MOUSE_INSIDE_WINDOW);
                up.remove(Input.MOUSE_INSIDE_WINDOW);

                down.add(Input.MOUSE_INSIDE_WINDOW);
            } else {
                down.remove(Input.MOUSE_INSIDE_WINDOW);
                held.remove(Input.MOUSE_INSIDE_WINDOW);
                up.remove(Input.MOUSE_INSIDE_WINDOW);

                up.add(Input.MOUSE_INSIDE_WINDOW);
            }
        });
        GLFW.glfwSetMouseButtonCallback(window.getHandle(), (handle, button, action, mode) -> {
            Input input = Input.getFromIntCode(button);
            if(action == GLFW.GLFW_PRESS) {
                down.remove(input);
                held.remove(input);
                up.remove(input);

                down.add(input);
            } else if(action == GLFW.GLFW_RELEASE) {
                down.remove(input);
                held.remove(input);
                up.remove(input);

                up.add(input);
            }
        });
        GLFW.glfwSetKeyCallback(window.getHandle(), (handle, key, scancode, action, mods) -> {
            Input input = Input.getFromIntCode(key);
            if(action == GLFW.GLFW_PRESS) {
                down.remove(input);
                held.remove(input);
                up.remove(input);

                down.add(input);
            } else if(action == GLFW.GLFW_RELEASE) {
                down.remove(input);
                held.remove(input);
                up.remove(input);

                up.add(input);
            }
        });
    }

    public boolean down(Input input) {
        return down.contains(input);
    }
    public boolean held(Input input) {
        return held.contains(input);
    }
    public boolean up(Input input) {
        return up.contains(input);
    }

    public Vector2d getMouseDelta() {
        return mouseDelta;
    }

    public void update() {
        for(Input down : this.down) {
            held.add(down);
        }
        down.removeAll(down);
        up.removeAll(up);

        double mouseDeltaX, mouseDeltaY;

        mouseDeltaX = (mouseLocation.x - window.getWidth() / 2) / (window.getWidth() / 2);
        mouseDeltaY = (mouseLocation.y - window.getHeight() / 2) / (window.getHeight() / 2);

        if(focused) {
            GLFW.glfwSetCursorPos(window.getHandle(), window.getWidth() / 2, window.getHeight() / 2);
        }

        mouseDelta = new Vector2d(mouseDeltaX, mouseDeltaY);
    }

    public enum Input {
        MOUSE_LEFT_BUTTON(GLFW.GLFW_MOUSE_BUTTON_LEFT),
        MOUSE_RIGHT_BUTTON(GLFW.GLFW_MOUSE_BUTTON_RIGHT),
        MOUSE_MIDDLE_BUTTON(GLFW.GLFW_MOUSE_BUTTON_MIDDLE),
        MOUSE_SCROLLWHEEL_UP(-1),
        MOUSE_SCROLLWHEEL_DOWN(-1),
        MOUSE_FORWARD_BUTTON(GLFW.GLFW_MOUSE_BUTTON_4),
        MOUSE_BACK_BUTTON(GLFW.GLFW_MOUSE_BUTTON_5),
        MOUSE_6_BUTTON(GLFW.GLFW_MOUSE_BUTTON_6),
        MOUSE_7_BUTTON(GLFW.GLFW_MOUSE_BUTTON_7),
        MOUSE_8_BUTTON(GLFW.GLFW_MOUSE_BUTTON_8),
        MOUSE_INSIDE_WINDOW(-1),

        /* Common keys, such as any shift key, or any enter key */
        KEYBOARD_COMMON_ENTER(-1),
        KEYBOARD_COMMON_SHIFT(-1),
        KEYBOARD_COMMON_CONTROL(-1),
        KEYBOARD_COMMON_ALT(-1),
        KEYBOARD_COMMON_WINDOWS(-1),

        KEYBOARD_A(GLFW.GLFW_KEY_A),
        KEYBOARD_B(GLFW.GLFW_KEY_B),
        KEYBOARD_C(GLFW.GLFW_KEY_C),
        KEYBOARD_D(GLFW.GLFW_KEY_D),
        KEYBOARD_E(GLFW.GLFW_KEY_E),
        KEYBOARD_F(GLFW.GLFW_KEY_F),
        KEYBOARD_G(GLFW.GLFW_KEY_G),
        KEYBOARD_H(GLFW.GLFW_KEY_H),
        KEYBOARD_I(GLFW.GLFW_KEY_I),
        KEYBOARD_J(GLFW.GLFW_KEY_J),
        KEYBOARD_K(GLFW.GLFW_KEY_K),
        KEYBOARD_L(GLFW.GLFW_KEY_L),
        KEYBOARD_M(GLFW.GLFW_KEY_M),
        KEYBOARD_N(GLFW.GLFW_KEY_N),
        KEYBOARD_O(GLFW.GLFW_KEY_O),
        KEYBOARD_P(GLFW.GLFW_KEY_P),
        KEYBOARD_Q(GLFW.GLFW_KEY_Q),
        KEYBOARD_R(GLFW.GLFW_KEY_R),
        KEYBOARD_S(GLFW.GLFW_KEY_S),
        KEYBOARD_T(GLFW.GLFW_KEY_T),
        KEYBOARD_U(GLFW.GLFW_KEY_U),
        KEYBOARD_V(GLFW.GLFW_KEY_V),
        KEYBOARD_W(GLFW.GLFW_KEY_W),
        KEYBOARD_X(GLFW.GLFW_KEY_X),
        KEYBOARD_Y(GLFW.GLFW_KEY_Y),
        KEYBOARD_Z(GLFW.GLFW_KEY_Z),

        KEYBOARD_0(GLFW.GLFW_KEY_0),
        KEYBOARD_1(GLFW.GLFW_KEY_1),
        KEYBOARD_2(GLFW.GLFW_KEY_2),
        KEYBOARD_3(GLFW.GLFW_KEY_3),
        KEYBOARD_4(GLFW.GLFW_KEY_4),
        KEYBOARD_5(GLFW.GLFW_KEY_5),
        KEYBOARD_6(GLFW.GLFW_KEY_6),
        KEYBOARD_7(GLFW.GLFW_KEY_7),
        KEYBOARD_8(GLFW.GLFW_KEY_8),
        KEYBOARD_9(GLFW.GLFW_KEY_9),

        KEYBOARD_F1(GLFW.GLFW_KEY_F1),
        KEYBOARD_F2(GLFW.GLFW_KEY_F2),
        KEYBOARD_F3(GLFW.GLFW_KEY_F3),
        KEYBOARD_F4(GLFW.GLFW_KEY_F4),
        KEYBOARD_F5(GLFW.GLFW_KEY_F5),
        KEYBOARD_F6(GLFW.GLFW_KEY_F6),
        KEYBOARD_F7(GLFW.GLFW_KEY_F7),
        KEYBOARD_F8(GLFW.GLFW_KEY_F8),
        KEYBOARD_F9(GLFW.GLFW_KEY_F9),
        KEYBOARD_F10(GLFW.GLFW_KEY_F10),
        KEYBOARD_F11(GLFW.GLFW_KEY_F11),
        KEYBOARD_F12(GLFW.GLFW_KEY_F12),
        KEYBOARD_F13(GLFW.GLFW_KEY_F13),
        KEYBOARD_F14(GLFW.GLFW_KEY_F14),
        KEYBOARD_F15(GLFW.GLFW_KEY_F15),
        KEYBOARD_F16(GLFW.GLFW_KEY_F16),
        KEYBOARD_F17(GLFW.GLFW_KEY_F17),
        KEYBOARD_F18(GLFW.GLFW_KEY_F18),
        KEYBOARD_F19(GLFW.GLFW_KEY_F19),
        KEYBOARD_F20(GLFW.GLFW_KEY_F20),
        KEYBOARD_F21(GLFW.GLFW_KEY_F21),
        KEYBOARD_F22(GLFW.GLFW_KEY_F22),
        KEYBOARD_F23(GLFW.GLFW_KEY_F23),
        KEYBOARD_F24(GLFW.GLFW_KEY_F24),
        KEYBOARD_F25(GLFW.GLFW_KEY_F25),

        KEYBOARD_NUMPAD_0(GLFW.GLFW_KEY_KP_0),
        KEYBOARD_NUMPAD_1(GLFW.GLFW_KEY_KP_1),
        KEYBOARD_NUMPAD_2(GLFW.GLFW_KEY_KP_2),
        KEYBOARD_NUMPAD_3(GLFW.GLFW_KEY_KP_3),
        KEYBOARD_NUMPAD_4(GLFW.GLFW_KEY_KP_4),
        KEYBOARD_NUMPAD_5(GLFW.GLFW_KEY_KP_5),
        KEYBOARD_NUMPAD_6(GLFW.GLFW_KEY_KP_6),
        KEYBOARD_NUMPAD_7(GLFW.GLFW_KEY_KP_7),
        KEYBOARD_NUMPAD_8(GLFW.GLFW_KEY_KP_8),
        KEYBOARD_NUMPAD_9(GLFW.GLFW_KEY_KP_9),

        KEYBOARD_NUMPAD_PERIOD(GLFW.GLFW_KEY_KP_DECIMAL),
        KEYBOARD_NUMPAD_DIVIDE(GLFW.GLFW_KEY_KP_DIVIDE),
        KEYBOARD_NUMPAD_MULTIPLY(GLFW.GLFW_KEY_KP_MULTIPLY),
        KEYBOARD_NUMPAD_SUBTRACT(GLFW.GLFW_KEY_KP_SUBTRACT),
        KEYBOARD_NUMPAD_ADD(GLFW.GLFW_KEY_KP_ADD),
        KEYBOARD_NUMPAD_EQUAL(GLFW.GLFW_KEY_KP_EQUAL),
        KEYBOARD_NUMPAD_ENTER(GLFW.GLFW_KEY_KP_ENTER),

        KEYBOARD_SPACE(GLFW.GLFW_KEY_SPACE),
        KEYBOARD_TAB(GLFW.GLFW_KEY_TAB),
        KEYBOARD_ESCAPE(GLFW.GLFW_KEY_ESCAPE),
        KEYBOARD_ENTER(GLFW.GLFW_KEY_ENTER),
        KEYBOARD_BACKSPACE(GLFW.GLFW_KEY_BACKSPACE),

        KEYBOARD_UP(GLFW.GLFW_KEY_UP),
        KEYBOARD_DOWN(GLFW.GLFW_KEY_DOWN),
        KEYBOARD_LEFT(GLFW.GLFW_KEY_LEFT),
        KEYBOARD_RIGHT(GLFW.GLFW_KEY_RIGHT),

        KEYBOARD_INSERT(GLFW.GLFW_KEY_INSERT),
        KEYBOARD_DELETE(GLFW.GLFW_KEY_DELETE),
        KEYBOARD_HOME(GLFW.GLFW_KEY_HOME),
        KEYBOARD_END(GLFW.GLFW_KEY_END),
        KEYBOARD_PAGE_UP(GLFW.GLFW_KEY_PAGE_UP),
        KEYBOARD_PAGE_DOWN(GLFW.GLFW_KEY_PAGE_DOWN),

        KEYBOARD_APOSTROPHE(GLFW.GLFW_KEY_APOSTROPHE),
        KEYBOARD_COMMA(GLFW.GLFW_KEY_COMMA),
        KEYBOARD_PERIOD(GLFW.GLFW_KEY_PERIOD),
        KEYBOARD_SEMICOLON(GLFW.GLFW_KEY_SEMICOLON),
        KEYBOARD_SLASH(GLFW.GLFW_KEY_SLASH),
        KEYBOARD_BACKSLASH(GLFW.GLFW_KEY_BACKSLASH),

        KEYBOARD_LEFT_BRACKET(GLFW.GLFW_KEY_LEFT_BRACKET),
        KEYBOARD_RIGHT_BRACKET(GLFW.GLFW_KEY_RIGHT_BRACKET),
        KEYBOARD_GRAVE(GLFW.GLFW_KEY_GRAVE_ACCENT),

        KEYBOARD_MINUS(GLFW.GLFW_KEY_MINUS),
        KEYBOARD_EQUAL(GLFW.GLFW_KEY_EQUAL),

        KEYBOARD_LOCK_CAPS(GLFW.GLFW_KEY_CAPS_LOCK),
        KEYBOARD_LOCK_NUMPAD(GLFW.GLFW_KEY_NUM_LOCK),
        KEYBOARD_LOCK_SCROLL(GLFW.GLFW_KEY_SCROLL_LOCK),

        KEYBOARD_PAUSE(GLFW.GLFW_KEY_PAUSE),
        KEYBOARD_PRINT_SCREEN(GLFW.GLFW_KEY_PRINT_SCREEN),

        KEYBOARD_NON_US_1(GLFW.GLFW_KEY_WORLD_1),
        KEYBOARD_NON_US_2(GLFW.GLFW_KEY_WORLD_2),

        KEYBOARD_LEFT_SHIFT(GLFW.GLFW_KEY_LEFT_SHIFT),
        KEYBOARD_LEFT_CONTROL(GLFW.GLFW_KEY_LEFT_CONTROL),
        KEYBOARD_LEFT_ALT(GLFW.GLFW_KEY_LEFT_ALT),
        KEYBOARD_LEFT_WINDOWS(GLFW.GLFW_KEY_LEFT_SUPER),

        KEYBOARD_RIGHT_SHIFT(GLFW.GLFW_KEY_RIGHT_SHIFT),
        KEYBOARD_RIGHT_CONTROL(GLFW.GLFW_KEY_RIGHT_CONTROL),
        KEYBOARD_RIGHT_ALT(GLFW.GLFW_KEY_RIGHT_ALT),
        KEYBOARD_RIGHT_WINDOWS(GLFW.GLFW_KEY_RIGHT_SUPER),

        KEYBOARD_MENU(GLFW.GLFW_KEY_MENU);

        private int keyID;
        private static HashMap<Integer, Input> mappings = new HashMap<>();

        static {
            for(Input input : Input.values()) {
                mappings.put(input.keyID, input);
            }
        }

        private Input(int keyID) {
            this.keyID = keyID;
        }

        private static Input getFromIntCode(int code) {
            return mappings.get(code);
        }
    }
}
