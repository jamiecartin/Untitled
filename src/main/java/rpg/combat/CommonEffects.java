package rpg.combat;

import rpg.entities.Character;

public class CommonEffects {
    public static StatusEffect POISON = new StatusEffect("Poison", StatusEffect.EffectType.DOT, 3, 5)
        .onApply(target -> 
            System.out.println(target.getName() + " is poisoned!"))
        .onRemove(target -> 
            System.out.println(target.getName() + " is no longer poisoned"));

    public static StatusEffect STRENGTH_BUFF = new StatusEffect("Strength Boost", StatusEffect.EffectType.BUFF, 3, 5)
        .onApply(target -> {
            target.modifyAttribute("Strength", 5);
            System.out.println(target.getName() + " feels stronger!");
        })
        .onRemove(target -> {
            target.modifyAttribute("Strength", -5);
            System.out.println(target.getName() + "'s strength returns to normal");
        });

    public static StatusEffect STUN = new StatusEffect("Stun", StatusEffect.EffectType.CONTROL, 1, 0)
        .onApply(target -> 
            System.out.println(target.getName() + " is stunned and can't move!"))
        .onRemove(target -> 
            System.out.println(target.getName() + " recovers from the stun"));

    public static StatusEffect REGENERATION = new StatusEffect("Regeneration", StatusEffect.EffectType.HOT, 3, 10)
        .onApply(target -> 
            System.out.println(target.getName() + " starts regenerating health"));
}
