package com.felayga.unpixeldungeon.items.spells.inventory;

import com.felayga.unpixeldungeon.Badges;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.effects.Identification;
import com.felayga.unpixeldungeon.items.Item;
import com.felayga.unpixeldungeon.mechanics.GameTime;
import com.felayga.unpixeldungeon.sprites.ItemSpriteSheet;
import com.felayga.unpixeldungeon.utils.GLog;
import com.felayga.unpixeldungeon.windows.WndBackpack;

/**
 * Created by hello on 3/18/16.
 */
public class IdentifySpell extends InventorySpell {
    public IdentifySpell() {
        super(1, GameTime.TICK);

        name = "Identify";
        image = ItemSpriteSheet.SPELL_IDENTIFY;
        mode = WndBackpack.Mode.UNIDENTIFED;
    }

    @Override
    protected void onItemSelected(Item item)
    {
        spellEffect(curUser, item, true);
    }

    public static void spellEffect(Char user, Item item, boolean fx) {
        if (fx) {
            user.sprite.parent.add(new Identification(user.sprite.center().offset(0, -16)));
        }

        if (item != null) {
            item.identify();
            GLog.i("It is " + item.getDisplayName());
            Badges.validateItemLevelAquired(item);
        }
    }
}
