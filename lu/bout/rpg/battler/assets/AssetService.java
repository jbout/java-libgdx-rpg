package lu.bout.rpg.battler.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;

public class AssetService {
    AssetManager manager;
    public AssetService() {
        manager = new AssetManager();
        FileHandleResolver resolver = new InternalFileHandleResolver();
        manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
//        manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

    }

    public void preload(AssetConsumer consumer) {
        for (AssetDescriptor s: consumer.getRequiredFiles()) {
            manager.load(s);
        }
    }

    public synchronized Object get (AssetDescriptor assetDescriptor) {
        manager.finishLoadingAsset(assetDescriptor);
        return manager.get(assetDescriptor);
    }

}
