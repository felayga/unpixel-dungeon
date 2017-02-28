/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
 *
 * unPixel Dungeon
 * Copyright (C) 2015-2016 Randall Foudray
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 *
 */
package com.felayga.unpixeldungeon.items.wands;

import com.felayga.unpixeldungeon.actors.Actor;
import com.felayga.unpixeldungeon.actors.Char;
import com.felayga.unpixeldungeon.actors.blobs.Blob;
import com.felayga.unpixeldungeon.actors.blobs.VenomGas;
import com.felayga.unpixeldungeon.effects.MagicMissile;
import com.felayga.unpixeldungeon.items.weapon.melee.simple.MagesStaff;
import com.felayga.unpixeldungeon.mechanics.Ballistica;
import com.felayga.unpixeldungeon.scenes.GameScene;
import com.watabou.utils.Callback;

public class WandOfVenom extends Wand {

    public WandOfVenom()
	{
        super(20);
		name = "Wand of Venom";

		collisionProperties = Ballistica.Mode.SplasherBolt;
	}

	@Override
	protected void onZap(Ballistica bolt) {
		Blob venomGas = Blob.seed(curUser, bolt.collisionPos, 50 + 10 * level(), VenomGas.class);
		((VenomGas)venomGas).setStrength(level()+1);
		GameScene.add(venomGas);

		Char ch = Actor.findChar(bolt.collisionPos);
		if (ch != null){
			processSoulMark(ch, curUser);
		}
	}

	@Override
	protected void fxEffect(Ballistica bolt, Callback callback) {
		MagicMissile.poison(curUser.sprite.parent, bolt.sourcePos, bolt.collisionPos, callback);
	}

    /*
	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
		new Poison().proc(staff, attacker, defender, damage);
	}
	*/

	@Override
	public void staffFx(MagesStaff.StaffParticle particle) {
		particle.color( 0x8844FF ); particle.am = 0.6f;
		particle.setLifespan(0.6f);
		particle.acc.set(0, 40);
		particle.setSize( 0f, 3f);
		particle.shuffleXY(2f);
	}

	@Override
	public String desc() {
		return
			"This wand has a purple body which opens to a brilliant green gem. " +
			"A small amount of foul smelling gas leaks from the gem.\n\n" +
			"This wand shoots a bolt which explodes into a cloud of vile venomous gas at a targeted location. " +
			"Anything caught inside this cloud will take continual damage, increasing with time.";
	}
}
