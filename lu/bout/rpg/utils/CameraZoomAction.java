package lu.bout.rpg.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.utils.Null;

public class CameraZoomAction extends TemporalAction {
    private OrthographicCamera camera;
    private Vector2 startPos;
    private float startZoom;
    private float deltaX, deltaY, deltaZoom;

    public CameraZoomAction(OrthographicCamera camera, Rectangle rectangle, float duration) {
        super(duration);
        this.camera = camera;
        startPos = new Vector2(camera.position.x, camera.position.y);
        startZoom = camera.zoom;
        float ratioX = rectangle.width / camera.viewportWidth;
        float ratioY = rectangle.height / camera.viewportHeight;
        deltaZoom = Math.max(ratioX, ratioY) - startZoom;
        Vector2 endPos = rectangle.getCenter(new Vector2(0f,0f));
        deltaX = endPos.x - startPos.x;
        deltaY = endPos.y - startPos.y;

    }

    protected void update (float percent) {
        camera.position.set(startPos.x + (deltaX * percent), startPos.y + (deltaY * percent),0);
        camera.zoom = startZoom + (deltaZoom * percent);
    }
}