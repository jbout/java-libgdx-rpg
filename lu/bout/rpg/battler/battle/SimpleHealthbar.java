package lu.bout.rpg.battler.battle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class SimpleHealthbar extends Rectangle {

    TextureRegionDrawable background;
    TextureRegionDrawable health;
    float border;

    public SimpleHealthbar(float width, float height) {
        super(0,0,width, height);
        border = height / 10;
        buildRedOnBlack();
    }

    public void buildRedOnBlack() {
        int pixHeight = 10;
        int pixWidth = (int)(10f * width / height );
        // making the border exactly 1 pixel
        Pixmap pixmap = new Pixmap(pixWidth, pixHeight, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.fill();
        background = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        pixmap.setColor(Color.RED);
        pixmap.fillRectangle(0,1, pixWidth, pixHeight-2);
        health = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
    }

    public void buildGreenOnRed() {
        int pixHeight = 10;
        int pixWidth = (int)(10f * width / height );
        // making the border exactly 1 pixel
        Pixmap pixmap = new Pixmap(pixWidth, pixHeight, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.fill();
        pixmap.setColor(Color.RED);
        pixmap.fillRectangle(1,1, pixWidth-2, pixHeight-2);
        background = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        pixmap.setColor(Color.BLACK);
        pixmap.fill();
        pixmap.setColor(0x6c9806ff);
        pixmap.fillRectangle(0,1, pixWidth, pixHeight-2);
        health = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
    }


    public void draw(Batch batch, float x, float y, float percent) {
        background.draw(batch, x, y, this.width, this.height);
        health.draw(batch, x + border, y, (this.width - (2 * border)) * percent , this.height);
    }
}
