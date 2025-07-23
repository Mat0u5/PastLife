package net.mat0u5.pastlife.mixin;

import net.mat0u5.pastlife.interfaces.IKeybinds;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(GameOptions.class)
public class GameOptionsMixin implements IKeybinds {
    @Unique
    private KeyBinding zoomKey = new KeyBinding("Zoom", Keyboard.KEY_C);

    @Override
    public KeyBinding zoomKey() {
        return zoomKey;
    }

    @Inject(method = "load", at = @At(value = "HEAD"))
    public void init1(CallbackInfo ci) {
        GameOptions options = (GameOptions) (Object) this;
        addNewKeys(options);
    }

    @Unique
    private void addNewKeys(GameOptions options) {
        KeyBinding[] currentKeybindings = options.keyBindings;
        for (KeyBinding key : currentKeybindings) {
            if (key.name.equals(zoomKey.name)) {
                return;
            }
        }
        KeyBinding[] newKeybindings = Arrays.copyOf(currentKeybindings, options.keyBindings.length+1);
        newKeybindings[options.keyBindings.length] = zoomKey;
        options.keyBindings = newKeybindings;
    }
}
