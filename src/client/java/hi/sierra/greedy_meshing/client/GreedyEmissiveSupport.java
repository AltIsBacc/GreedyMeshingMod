package hi.sierra.greedy_meshing.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
//? if >=1.21.11 {
/*import net.minecraft.resources.Identifier;
*///?} else {
import net.minecraft.resources.ResourceLocation;
//?}

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/** Resolves the conventional OptiFine/Iris {@code _e} companion texture for a block sprite. */
public final class GreedyEmissiveSupport {
    /**
     * Per-sprite verdict cache (empty = no {@code _e} companion). The lookup behind it walks every
     * resource pack (zip entry probes, path formatting, a thrown NoSuchFileException per miss) and
     * was the single hottest call in chunk meshing, yet the answer only changes on a resource
     * reload. Keyed by sprite identity: a reload stitches new sprite objects, so entries for the
     * old atlas simply become unreachable and are dropped by {@link #clearCache()}.
     */
    private static final Map<TextureAtlasSprite, Optional<TextureAtlasSprite>> CACHE = new ConcurrentHashMap<>();

    private GreedyEmissiveSupport() {
    }

    public static void clearCache() {
        CACHE.clear();
    }

    public static TextureAtlasSprite find(TextureAtlasSprite base) {
        Optional<TextureAtlasSprite> cached = CACHE.get(base);
        if (cached == null) {
            // Resolve outside computeIfAbsent so pack I/O never runs under a map bin lock.
            cached = Optional.ofNullable(resolve(base));
            Optional<TextureAtlasSprite> raced = CACHE.putIfAbsent(base, cached);
            if (raced != null) {
                cached = raced;
            }
        }
        return cached.orElse(null);
    }

    private static TextureAtlasSprite resolve(TextureAtlasSprite base) {
        //? if UNOBFUSCATED {
        /*Minecraft mc = Minecraft.getInstance();
        Identifier baseId = base.contents().name();
        Identifier emissiveId = Identifier.fromNamespaceAndPath(baseId.getNamespace(), baseId.getPath() + "_e");
        Identifier textureId = emissiveId.withPath(path -> "textures/" + path + ".png");
        if (mc.getResourceManager().getResource(textureId).isEmpty()) {
            return null;
        }
        return mc.getAtlasManager().getAtlasOrThrow(Identifier.fromNamespaceAndPath("minecraft", "blocks")).getSprite(emissiveId);
*///?} else if >=1.21.11 {
        /*var mc = Minecraft.getInstance();
        Identifier baseId = base.contents().name();
        Identifier emissiveId = Identifier.fromNamespaceAndPath(baseId.getNamespace(), baseId.getPath() + "_e");
        Identifier textureId = emissiveId.withPath(path -> "textures/" + path + ".png");
        if (mc.getResourceManager().getResource(textureId).isEmpty()) {
            return null;
        }
        return mc.getAtlasManager().getAtlasOrThrow(Identifier.fromNamespaceAndPath("minecraft", "blocks")).getSprite(emissiveId);
        *///?} else if >=1.21.9 {
        /*var mc = Minecraft.getInstance();
        ResourceLocation baseId = base.contents().name();
        ResourceLocation emissiveId = ResourceLocation.fromNamespaceAndPath(baseId.getNamespace(), baseId.getPath() + "_e");
        ResourceLocation textureId = emissiveId.withPath(path -> "textures/" + path + ".png");
        if (mc.getResourceManager().getResource(textureId).isEmpty()) {
            return null;
        }
        return mc.getAtlasManager().getAtlasOrThrow(ResourceLocation.fromNamespaceAndPath("minecraft", "blocks")).getSprite(emissiveId);
        *///?} else {
        Minecraft mc = Minecraft.getInstance();
        ResourceLocation baseId = base.contents().name();
        ResourceLocation emissiveId = ResourceLocation.fromNamespaceAndPath(baseId.getNamespace(), baseId.getPath() + "_e");
        ResourceLocation textureId = emissiveId.withPath(path -> "textures/" + path + ".png");
        if (mc.getResourceManager().getResource(textureId).isEmpty()) {
            return null;
        }
        return mc.getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(emissiveId);
        //?}
    }
}
