package Application.View.Renderer;

import Application.Geometry.Point;
import Application.Geometry.Rotation;
import Application.Model.World.Character.Fireball;
import Application.View.AnimatedSpriteSheet;

import java.awt.*;

public class FireballRenderer implements Renderer {
    private final Fireball fireball;
    private final AnimatedSpriteSheet spriteSheet;
    private int refreshesToNextFrame = 0;

    public FireballRenderer(Fireball fireball, ImagesRepository images) {
        this.fireball = fireball;
        this.spriteSheet = images.fireball;
    }

    @Override
    public void renderOn(Graphics2D canvas) {
        Rotation rotation = new Rotation(fireball.getAngle(), new Point(8, 32));
        spriteSheet.drawOn(canvas, fireball.getPosition(), rotation);
        refreshesToNextFrame++;
        if (refreshesToNextFrame == 3) {
            refreshesToNextFrame = 0;
            spriteSheet.next();
        }
    }
}
