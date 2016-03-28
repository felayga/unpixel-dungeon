package com.felayga.unpixeldungeon.mechanics;

import com.watabou.noosa.ColorBlock;

/**
 * Created by hello on 3/13/16.
 */
public enum BUCStatus {
    Unknown,
    Cursed,
    Uncursed,
    Blessed;

    public static BUCStatus FromInt(int value)
    {
        switch(value)
        {
            case -1:
                return BUCStatus.Cursed;
            case 0:
                return BUCStatus.Uncursed;
            case 1:
                return BUCStatus.Blessed;
            default:
                return BUCStatus.Unknown;
        }
    }

    public static int ToInt(BUCStatus status)
    {
        switch(status)
        {
            case Cursed:
                return -1;
            case Uncursed:
                return 0;
            case Blessed:
                return 1;
            default:
                return -2;
        }
    }

    public static void colorizeBackground(ColorBlock background, BUCStatus status)
    {
        switch(status)
        {
            case Cursed:
                background.ra += 0.2f;
                background.ga -= 0.1f;
                background.ba -= 0.1f;
                break;
            case Blessed:
                background.ra -= 0.1f;
                background.ga += 0.2f;
                background.ba -= 0.1f;
                break;
            case Unknown:
                background.ra += 0.15f;
                background.ga += 0.15f;
        }
    }

    public static String getName(BUCStatus status)
    {
        switch(status) {
            case Cursed:
                return "cursed";
            case Uncursed:
                return "uncursed";
            case Blessed:
                return "blessed";
        }

        return "";
    }
}
