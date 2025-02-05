package rpg.combat;

import java.util.function.Consumer;
import rpg.entities.Character;

public class StatusEffect {
    public enum EffectType {
        BUFF, DEBUFF, DOT, HOT, CONTROL, SPECIAL
    }

    private String name;
    private EffectType type;
    private int duration;
    private int potency;
    private Consumer<Character> applyEffect;
    private Consumer<Character> removeEffect;
    private boolean isExpired;

    public StatusEffect(String name, EffectType type, int duration, int potency) {
        this.name = name;
        this.type = type;
        this.duration = duration;
        this.potency = potency;
        this.isExpired = false;
    }

    // Fluent setters for effect behavior
    public StatusEffect onApply(Consumer<Character> applyEffect) {
        this.applyEffect = applyEffect;
        return this;
    }

    public StatusEffect onRemove(Consumer<Character> removeEffect) {
        this.removeEffect = removeEffect;
        return this;
    }

    public void processTurn(Character target) {
        if (isExpired) return;

        duration--;
        if (duration <= 0) {
            remove(target);
            return;
        }

        // Default behavior based on type
        switch(type) {
            case DOT:
                target.takeDamage(potency);
                System.out.println(target.getName() + " takes " + potency + " damage from " + name);
                break;
            case HOT:
                target.heal(potency);
                System.out.println(target.getName() + " heals " + potency + " HP from " + name);
                break;
        }

        if (applyEffect != null) {
            applyEffect.accept(target);
        }
    }

    public void remove(Character target) {
        isExpired = true;
        if (removeEffect != null) {
            removeEffect.accept(target);
        }
        System.out.println(target.getName() + " is no longer affected by " + name);
    }

    // Getters
    public String getName() { return name; }
    public EffectType getType() { return type; }
    public int getDuration() { return duration; }
    public int getPotency() { return potency; }
    public boolean isExpired() { return isExpired; }
}
