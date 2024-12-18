package net.nopunintendeds.rbd;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class Sound {
    static Identifier rbd = new Identifier("rbd", "rbd");
    static Identifier ahhyooaaawhoaaa = new Identifier("rbd", "ahhyooaaawhoaaa");
    static Identifier amdead = new Identifier("rbd", "amdead");

    public static final SoundEvent ReturnByDeath = Registry.register(Registries.SOUND_EVENT, rbd, SoundEvent.of(rbd));
    public static final SoundEvent AHHYOOAAAWHOAAA = Registry.register(Registries.SOUND_EVENT, ahhyooaaawhoaaa, SoundEvent.of(ahhyooaaawhoaaa));
    public static final SoundEvent AMDEAD = Registry.register(Registries.SOUND_EVENT, amdead, SoundEvent.of(amdead));



    public static void registerSound(){

    }
}
