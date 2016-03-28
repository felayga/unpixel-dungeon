package com.felayga.unpixeldungeon.mechanics;

/**
 * Created by hello on 3/19/16.
 */
public interface IDecayable {
    long decay();
    void decay(long amount, boolean updateTime, boolean fixTime);
    boolean decayed();
}
