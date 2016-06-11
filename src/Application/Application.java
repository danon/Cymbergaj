package Application;

import Application.Geometry.Point;
import Application.Geometry.Size;
import Application.Model.Engine;
import Application.Model.WindowClosingListener;
import Application.Model.World.Control;
import Application.Model.World.Spaceship;
import Application.Model.World.World;
import Application.View.Images;
import Application.View.Window;

public class Application {

    private final Engine engine = new Engine();
    private final Window window;
    private final World world;

    private Application() {
        Settings settings = new Settings();
        settings.windowTitle = "Tadeosz | Best 2D game jk";
        settings.size = new Size(1080, 620);

        Images images = new Images();
        images.load();

        world = new World();
        window = new Window(settings, images, new WindowClosingListener(engine), world);

        engine.addGameEventListener(world);
        engine.addGameEventListener(window);


        Spaceship ship = new Spaceship(new Point(50, 200));

        Control control = ship.getControl();
        window.addKeyListener(control);
        window.addRenderer(ship.getRenderer(images));

        engine.addGameEventListener(control);
    }

    private void start() {
        window.show();
        engine.start();
    }

    public static void main(String[] args) {
        new Application().start();
    }
}
