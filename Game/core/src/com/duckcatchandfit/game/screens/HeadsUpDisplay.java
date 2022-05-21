package com.duckcatchandfit.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Align;

public class HeadsUpDisplay {

    //#region Fields

    private final BitmapFont font;
    private final float hudLeftX, hudRightX, hudRow1Y, hudRow2Y, hudSectionWidth;

    //#endregion

    //#region Initializers

    public HeadsUpDisplay(float worldWidth, float worldHeight) {
        //Create a BitmapFont from our font file
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(
                Gdx.files.internal("LowballNeueExtraLight-vmBpL.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        fontParameter.size = 72;
        fontParameter.borderWidth = 3.6f;
        fontParameter.color = new Color(1, 1, 1, 0.5f);
        fontParameter.borderColor = new Color(0, 0, 0, 0.3f);

        font = fontGenerator.generateFont(fontParameter);
        fontGenerator.dispose();

        //scale the font to fit world
        font.getData().setScale(0.1f);

        //calculate hud margins, etc.
        final float hudVerticalMargin = font.getCapHeight() / 2.0f;
        hudLeftX = hudVerticalMargin;
        hudRightX = worldWidth * 2.0f / 3.0f - hudLeftX;
        hudRow1Y = worldHeight - hudVerticalMargin;
        hudRow2Y = hudRow1Y - hudVerticalMargin - font.getCapHeight();
        hudSectionWidth = worldWidth / 3.0f;
    }

    //#endregion

    //#region Public Methods

    public void renderHeadsUpDisplay(int score, SpriteBatch batch) {
        //render top row labels
        font.draw(batch, "Score", hudLeftX, hudRow1Y, hudSectionWidth, Align.left, false);
        font.draw(batch, "Speed", hudRightX, hudRow1Y, hudSectionWidth, Align.right, false);

        //render second row values
        font.draw(batch, String.valueOf(score),
                hudLeftX, hudRow2Y, hudSectionWidth, Align.left, false);
        font.draw(batch, 1 + " ",
                hudRightX, hudRow2Y, hudSectionWidth, Align.right, false);
    }

    public void dispose() {
        font.dispose();
    }

    //#endregion
}
