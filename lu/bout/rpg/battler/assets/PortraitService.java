package lu.bout.rpg.battler.assets;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

public class PortraitService {

    public int[] getRandomIds(int count) {
        LinkedList<Integer> ret = new LinkedList<>();
        for (int i=0; i<getPortraitCount(); i++) {
            ret.add(i);
        }
        Collections.shuffle(ret);
        int[] ids = new int[count];
        for (int i=0; i<count; i++) {
            ids[i] = ret.get(i).intValue();
        }
        return ids;
    }

    public HashMap<Integer, Texture> getRandomPortraits(int count) {
        HashMap<Integer, Texture> map = new HashMap<>();
        LinkedList<Integer> ret = new LinkedList<>();
        for (int i=0; i<getPortraitCount(); i++) {
            ret.add(i);
        }
        Collections.shuffle(ret);
        for (int i=0; i<count; i++) {
            Integer id = ret.get(i);
            map.put(id, getPortrait(id.intValue()));
        }
        return map;
    }

    public Texture getPortrait(int id) {
        return new Texture("portrait/generate_0" + id + ".jpg");
    }

    public Pixmap getScaled(int id, int sizeX, int sizeY) {
        Texture texture = getPortrait(id);
        texture.getTextureData().prepare();
        Pixmap source = texture.getTextureData().consumePixmap();
        Pixmap scaled = new Pixmap(sizeX, sizeY, source.getFormat());
        scaled.drawPixmap(source,
                0, 0, source.getWidth(), source.getHeight(),
                0, 0, scaled.getWidth(), scaled.getHeight()
        );
        return scaled;
    }

    public Texture getRoundPortrait(int id, int size) {
        Pixmap source = getScaled(id, size, size);
        Pixmap result = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        result.setBlending(Pixmap.Blending.None);
        result.setColor(1f, 1f, 1f, 1f);
        result.fillCircle(size / 2, size / 2, size/2-1);

        for (int x = 0; x < result.getWidth(); x++) {
            for (int y = 0; y < result.getHeight(); y++) {
                result.drawPixel(x, y, source.getPixel(x, y) & result.getPixel(x, y));
            }
        }
        result.setColor(0f, 0f, 0f, 1f);
        result.drawCircle(size/2, size/2, size/2-1);

        return new Texture(result);
    }

    private int getPortraitCount() {
        return 10;
    }
}
