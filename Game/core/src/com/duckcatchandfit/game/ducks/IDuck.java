package com.duckcatchandfit.game.ducks;

import com.badlogic.gdx.math.Rectangle;

public interface IDuck {

    boolean intersects(Rectangle otherRectangle);
}
