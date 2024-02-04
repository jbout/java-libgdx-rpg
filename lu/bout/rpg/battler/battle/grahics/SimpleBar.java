package lu.bout.rpg.battler.battle.grahics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class SimpleBar extends Rectangle {

    public final static Color[] styleRedOnBlack = new Color[] {Color.RED, Color.BLACK};
    public final static Color[] styleGreenOnRed = new Color[] {Color.GREEN, Color.BLACK, Color.RED};

    public final static Color[] styleGrayOnGray = new Color[] {Color.GRAY, Color.BLACK, Color.DARK_GRAY};

    TextureRegionDrawable background;
    TextureRegionDrawable health;
    int border;

    Color[] style;

    public SimpleBar(float width, float height) {
        this(width, height, styleRedOnBlack);
    }

    public SimpleBar(float width, float height, Color[] style) {
        super(0,0,width, height);
        border = Math.max(1,(int) (height / 10));
        int pixHeight = (int) height;
        int pixWidth = (int)(10f * width / height );
        // making the border exactly 1 pixel
        Pixmap pixmap = new Pixmap(pixWidth, pixHeight, Pixmap.Format.RGBA8888);
        pixmap.setColor(style[1]);
        pixmap.fill();
        if (style.length == 3) {
            pixmap.setColor(style[2]);
            pixmap.fillRectangle(border,border, pixWidth-(2*border), pixHeight-(2*border));
        }
        background = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        pixmap.setColor(style[0]);
        pixmap.fillRectangle(0,border, pixWidth, pixHeight-(2*border));
        health = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
    }

    public void draw(Batch batch, float x, float y, float percent) {
        background.draw(batch, x, y, this.width, this.height);
        health.draw(batch, x + border, y, (this.width - (2 * border)) * percent , this.height);
    }
}
