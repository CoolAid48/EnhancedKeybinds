package me.coolaid.enhancedkeybinds.multiKey;

import me.coolaid.enhancedkeybinds.event.InterKeyBinding;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

import java.io.*;
import java.nio.file.Path;
import java.util.Objects;

public class multiKeyBindingOptions {
    private static final Path optParent = FabricLoader.getInstance().getConfigDir().resolve("mkb");
    private static final Path optPath = optParent.resolve("keys-options.txt");

    public static boolean isDirReady() {
        if (optParent.toFile().isDirectory()) return true;
        return makeDir();
    }

    public static boolean isFileReady() {
        if (optPath.toFile().isFile()) return true;
        return makeFile();
    }

    public static boolean makeDir() {
        return optParent.toFile().mkdir();
    }

    public static boolean makeFile() {
        try {
            return optPath.toFile().createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void write(KeyBinding[] bindings) {
        if (!isDirReady() || !isFileReady()) return;
        try (final BufferedWriter w = new BufferedWriter(new FileWriter(optPath.toFile()))) {
            for (final KeyBinding k : bindings) {
                final InterKeyBinding extended = (InterKeyBinding) k;
                w.write("key_" + k.getTranslationKey() + ":" + k.getBoundKeyTranslationKey() + ":" + extended.getKeyModifier().name());
                w.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void read(KeyBinding[] bindings) {
        if (!isDirReady() || !isFileReady()) return;
        try (final BufferedReader r = new BufferedReader(new FileReader(optPath.toFile()))) {
            String l;
            while ((l = r.readLine()) != null) {
                final String[] s = l.split(":");
                for (final KeyBinding k : bindings) {
                    if (Objects.equals(s[0], "key_" + k.getTranslationKey())) {
                        final multiKeyModifier m = s.length > 2 ? multiKeyModifier.valueFromString(s[2]) : multiKeyModifier.NONE;
                        final InterKeyBinding e = (InterKeyBinding) k;
                        e.setKeyModifierAndCode(m, InputUtil.fromTranslationKey(s[1]));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}