package lu.bout.rpg.battler.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;

public class FieldSprite extends Sprite {

    private Field field;
    private Texture swords;
    private Texture portal;

    public FieldSprite(Field field, Texture dot, Texture swords, Texture portal) {
        super(dot);
        this.field = field;
        this.swords = swords;
        this.portal = portal;
    }

    public Circle getBoundaries() {
        return new Circle(this.getX() + this.getWidth() / 2, this.getY() + this.getWidth() / 2, this.getWidth() / 2);
    }

    public Field getField() {
        return field;
    }

    @Override
    public void draw (Batch batch) {
        super.draw(batch);
        if (!this.getField().isOpen() && this.getField().getType() == Field.TYPE_MONSTER) {
            batch.draw(swords, getVertices(), 0, getVertices().length);
        }
        if (this.getField().getType() == Field.TYPE_RETURN_FIELD) {
            batch.draw(portal, getVertices(), 0, getVertices().length);
        }
    }

}
