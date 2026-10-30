package ru.crystallized_dreams.interdimensionallib.common;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.util.Identifier;
import ru.crystallized_dreams.interdimensionallib.InterdimensionalLib;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public final class SkinCache {
    private static final Map<String, Identifier> skinCache=new ConcurrentHashMap<>();
    private static final Set<String> fetching=ConcurrentHashMap.newKeySet();
    private static final HttpClient HTTP=HttpClient.newHttpClient();
    
    public static Identifier get(String nickname) {
        Identifier cached=skinCache.get(nickname);
        if (cached!=null) return cached;

        MinecraftClient client=MinecraftClient.getInstance();
        if (client.getNetworkHandler()!=null) {
            PlayerListEntry entry=client.getNetworkHandler().getPlayerListEntry(nickname);
            if (entry!=null) {
                Identifier tex=entry.getSkinTextures().texture();
                skinCache.put(nickname, tex);
                return tex;
            }
        }

        if (fetching.add(nickname)) {
            CompletableFuture.runAsync(() -> {
                try {
                    HttpRequest req=HttpRequest.newBuilder()
                            .uri(URI.create("https://api.mojang.com/users/profiles/minecraft/"+nickname))
                            .build();
                    HttpResponse<String> resp =
                            HTTP.send(req, HttpResponse.BodyHandlers.ofString());

                    if (resp.statusCode()==200) {
                        JsonObject json=JsonParser.parseString(resp.body()).getAsJsonObject();
                        String rawUuid=json.get("id").getAsString();
                        String formatted=rawUuid.replaceFirst(
                                "(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})",
                                "$1-$2-$3-$4-$5");
                        UUID uuid=UUID.fromString(formatted);
                        GameProfile profile=new GameProfile(uuid, nickname);

                        client.execute(() -> {
                            SkinTextures textures =
                                    client.getSkinProvider().getSkinTextures(profile);
                            skinCache.put(nickname, textures.texture());
                        });
                    }
                } catch (Exception e) {
                    InterdimensionalLib.LOGGER.warn(
                            "[INTERDIM]: Failed to fetch skin for '{}'", nickname);
                } finally {
                    fetching.remove(nickname);
                }
            });
        }

        return DefaultSkinHelper.getTexture();
    }
}
