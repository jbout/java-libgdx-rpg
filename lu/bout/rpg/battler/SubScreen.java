package lu.bout.rpg.battler;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public interface SubScreen {

    /**
     * Renders a subcomponent on a self defined spot of the screen
     * @param batch batch to render to
     * @param delta time elapsed in seconds
     * @param inputVector position of the input device if touched, else null
     */
    public void render(SpriteBatch batch, float delta, Vector2 inputVector);
}
