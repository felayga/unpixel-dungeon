/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
 *
 * Unpixel Dungeon
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
 */
package com.felayga.unpixeldungeon.windows;

import com.watabou.noosa.BitmapTextMultiline;
import com.felayga.unpixeldungeon.scenes.PixelScene;
import com.felayga.unpixeldungeon.ui.RedButton;
import com.felayga.unpixeldungeon.ui.Window;

import java.util.List;

public class WndOptions extends Window {

	private static final int WIDTH			= 120;
	private static final int MARGIN 		= 2;
	private static final int BUTTON_HEIGHT	= 20;

	private static boolean[] getClicky(int optionsLength)
	{
		boolean[] clicky = new boolean[optionsLength];
		for (int n=0;n<clicky.length;n++) {
			clicky[n] = true;
		}
		return clicky;
	}

	private static boolean[] jesusFuckingChristJava(Boolean[] options)
	{
		boolean[] retval = new boolean[options.length];

		for (int n=0;n<options.length;n++)
		{
			retval[n] = options[n];
		}

		return retval;
	}

	public WndOptions( String title, String message, String... options)
	{
		this(title, message, options, getClicky(options.length));
	}

	public WndOptions(String title, String message, List<String> options) {
		this(title, message, options.toArray(new String[options.size()]));
	}

	public WndOptions(String title, String message, String[] options, Boolean[] optionClickSound) {
		this(title, message, options, jesusFuckingChristJava(optionClickSound));
	}
	
	public WndOptions( String title, String message, String[] options, boolean[] optionClickSound ) {
		super();

		selected = -1;
		
		BitmapTextMultiline tfTitle = PixelScene.createMultiline( title, 9 );
		tfTitle.hardlight( TITLE_COLOR );
		tfTitle.x = tfTitle.y = MARGIN;
		tfTitle.maxWidth = WIDTH - MARGIN * 2;
		tfTitle.measure();
		add( tfTitle );
		
		BitmapTextMultiline tfMesage = PixelScene.createMultiline( message, 8 );
		tfMesage.maxWidth = WIDTH - MARGIN * 2;
		tfMesage.measure();
		tfMesage.x = MARGIN;
		tfMesage.y = tfTitle.y + tfTitle.height() + MARGIN;
		add( tfMesage );
		
		float pos = tfMesage.y + tfMesage.height() + MARGIN;
		
		for (int i=0; i < options.length; i++) {
			final int index = i;
			RedButton btn = new RedButton( options[i], optionClickSound[i] ) {
				@Override
				protected void onClick() {
					selected = index;
					hide();
					onSelect( index );
				}
			};
			btn.setRect( MARGIN, pos, WIDTH - MARGIN * 2, BUTTON_HEIGHT );
			add( btn );
			
			pos += BUTTON_HEIGHT + MARGIN;
		}
		
		resize( WIDTH, (int)pos );
	}

	private int selected;

	protected void onSelect( int index ) {};

	@Override
	public void hide() {
		if (selected == -1) {
			onSelect(-1);
		}

		super.hide();
	}
}
