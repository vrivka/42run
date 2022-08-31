package edu.school21.game.utils;

import edu.school21.app.Main;
import edu.school21.engine.render.Texture2D;
import edu.school21.game.utils.types.ButtonType;
import edu.school21.game.utils.types.ClusterType;
import edu.school21.game.utils.types.TextureType;
import edu.school21.game.utils.types.TitleType;

import java.util.HashMap;
import java.util.Map;

import static edu.school21.game.utils.types.ButtonType.*;
import static edu.school21.game.utils.types.ClusterType.*;
import static edu.school21.game.utils.types.TextureType.*;
import static edu.school21.game.utils.types.TitleType.*;

public class TextureContainer {
    public static final Map<ButtonType, Texture2D> buttonTextures = new HashMap<>();
    public static final Map<TitleType, Texture2D> titleTextures = new HashMap<>();
    public static final Map<Integer, Texture2D> numberTextures = new HashMap<>();
    public static final Map<ClusterType, Texture2D> clusterTextures = new HashMap<>();
    public static final Map<TextureType, Texture2D> textures = new HashMap<>();

    static {
        try {
            buttonTextures.put(NEW_GAME, new Texture2D("/textures/HUD/NewGameButton.png"));
            buttonTextures.put(CONTINUE, new Texture2D("/textures/HUD/continueButton.png"));
            buttonTextures.put(TRY_AGAIN, new Texture2D("/textures/HUD/TryAgainButton.png"));
            buttonTextures.put(EXIT, new Texture2D("/textures/HUD/ExitButton.png"));

            titleTextures.put(MAIN_MENU, new Texture2D("/textures/HUD/GameTitle.png"));
            titleTextures.put(GAME_OVER, new Texture2D("/textures/HUD/GameOverTitle.png"));

            for (int i = 0; i < 10; i++) {
                numberTextures.put(i, new Texture2D("/textures/HUD/" + i + ".png"));
            }

            clusterTextures.put(UNIVERSE, new Texture2D("/textures/cluster/universe_texture.png"));
            clusterTextures.put(PROGRESS, new Texture2D("/textures/cluster/progress_texture.png"));
            clusterTextures.put(GENOM, new Texture2D("/textures/cluster/genom_texture.png"));
            clusterTextures.put(EVOLUTION, new Texture2D("/textures/cluster/evolution_texture.png"));
            clusterTextures.put(SINGULARITY, new Texture2D("/textures/cluster/singularity_texture.png"));
            clusterTextures.put(ETERNITY, new Texture2D("/textures/cluster/eternity_texture.png"));

            textures.put(SIGN, new Texture2D("/textures/other/signTexture.png"));
            textures.put(BOARD, new Texture2D("/textures/other/boardTexture.png"));
            textures.put(PLAYER, new Texture2D("/textures/other/player_skin.png"));
            textures.put(FENCE, new Texture2D("/textures/other/fence.png"));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(Main.ERROR_EXIT_CODE);
        }
    }

    public static Texture2D getRandomClusterTexture() {
        return clusterTextures.get(ClusterType.getRandom());
    }

    public static void cleanup() {
        buttonTextures.forEach((t, bt) -> bt.cleanup());
        titleTextures.forEach((t, tt) -> tt.cleanup());
        numberTextures.forEach((t, nt) -> nt.cleanup());
        clusterTextures.forEach((t, ct) -> ct.cleanup());
        textures.forEach((type, t) -> t.cleanup());
    }
}
