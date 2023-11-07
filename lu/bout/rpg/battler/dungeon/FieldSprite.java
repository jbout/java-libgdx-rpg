package lu.bout.rpg.battler.dungeon;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;

public class FieldSprite extends Sprite {

    private Field field;
    private TextureAtlas atlas;

    public FieldSprite(Field field, TextureAtlas mapTextures) {
        super();
        setSize(50,50);
        this.field = field;
        atlas = mapTextures;
        updateSprite();
    }

    public void updateSprite() {
        int type = field.isOpen() ? Field.TYPE_EMPTY : field.getType();
        switch (type) {
            case Field.TYPE_RETURN_FIELD:
                setRegion(atlas.findRegion("dot_trapdoor"));
                break;
            case Field.TYPE_MONSTER:
                setRegion(atlas.findRegion("dot_swords"));
                break;
            case Field.TYPE_TREASURE:
                setRegion(atlas.findRegion("dot_heal"));
                break;
            case Field.TYPE_FINISH:
                setRegion(atlas.findRegion("gate"));
                break;
            default:
                setRegion(atlas.findRegion("dot"));
        }
    }

    public Circle getBoundaries() {
        // making the circle double sized
        return new Circle(this.getX() + this.getWidth() / 2, this.getY() + this.getWidth() / 2, this.getWidth());
    }

    public Field getField() {
        return field;
    }
}
