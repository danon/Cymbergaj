package danon.Cymbergaj.View.Renderer;

import danon.Cymbergaj.Character;
import danon.Cymbergaj.Geometry.Angle;
import danon.Cymbergaj.Geometry.Point;
import danon.Cymbergaj.Geometry.Rotation;
import danon.Cymbergaj.Geometry.Size;
import danon.Cymbergaj.Model.Updatable;

import java.awt.*;

public class CharacterRenderer extends BodyRenderer implements Updatable {
    private final ImagesRepository images;

    private double totalElapsedTime = 0;

    public CharacterRenderer(Character character, ImagesRepository imagesRepository) {
        super(character);
        this.images = imagesRepository;
    }

    @Override
    protected void renderBody(Graphics2D canvas) {
        Size size = images.handgunIdle.getSpriteSize();
        Point center = new Point(size.getWidth() / 2, size.getHeight() / 2);

        canvas.draw(new Rectangle(-10, -10, 20, 20));

        images.handgunIdle.drawOn(canvas,
                new Point(0, 0),
                new Rotation(Angle.fromDegrees(totalElapsedTime * 100), center)
        );
    }

    @Override
    public void update(double elapsedTime) {
        totalElapsedTime += elapsedTime;
        int frame = (int) Math.round(totalElapsedTime * 20.0);
        images.handgunIdle.setFrame(frame);
    }
}
