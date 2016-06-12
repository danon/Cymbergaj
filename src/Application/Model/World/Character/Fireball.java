package Application.Model.World.Character;

import Application.Geometry.Angle;
import Application.Geometry.Point;
import Application.Model.World.Control;
import Application.Model.World.FireballControl;
import Application.View.Renderer.FireballRenderer;
import Application.View.Renderer.ImagesRepository;
import Application.View.Renderer.Renderer;

public class Fireball {
    private final Point position;
    private final Angle direction;

    public Fireball(Point position, Angle direction) {
        this.position = position;
        this.direction = direction;
    }

    public Point getPosition() {
        return position;
    }

    public Angle getAngle() {
        return direction;
    }

    public Control getControl() {
        return new FireballControl(this);
    }

    public Renderer getRenderer(ImagesRepository images) {
        return new FireballRenderer(this, images);
    }
}