package danon.Cymbergaj.Debug.dyn;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Capsule;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Polygon;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Slice;
import org.dyn4j.geometry.Triangle;
import org.dyn4j.geometry.Vector2;

/**
 * Class used to show a simple example of using the dyn4j project using
 * Java2D for rendering.
 * <p>
 * This class can be used as a starting point for projects.
 * @author William Bittle
 * @version 3.2.0
 * @since 3.0.0
 */
public class ExampleGraphics2D extends JFrame {
    /** The serial version id */
    private static final long serialVersionUID = 5663760293144882635L;

    /** The scale 45 pixels per meter */
    public static final double SCALE = 45.0;
    public static final double NANO_TO_BASE = 1.0e9;

    public static class GameObject extends Body {
        /** The color of the object */
        protected Color color;

        /**
         * Default constructor.
         */
        public GameObject() {
            // randomly generate the color
            this.color = new Color(
                    (float)Math.random() * 0.5f + 0.5f,
                    (float)Math.random() * 0.5f + 0.5f,
                    (float)Math.random() * 0.5f + 0.5f);
        }

        public void render(Graphics2D g) {
            // save the original transform
            AffineTransform ot = g.getTransform();

            // transform the coordinate system from world coordinates to local coordinates
            AffineTransform lt = new AffineTransform();
            lt.translate(this.transform.getTranslationX() * SCALE, this.transform.getTranslationY() * SCALE);
            lt.rotate(this.transform.getRotation());

            // apply the transform
            g.transform(lt);

            // loop over all the body fixtures for this body
            for (BodyFixture fixture : this.fixtures) {
                // get the shape on the fixture
                Convex convex = fixture.getShape();
                Graphics2DRenderer.render(g, convex, SCALE, color);
            }

            // set the original transform
            g.setTransform(ot);
        }
    }

    protected Canvas canvas;
    protected World world;
    protected boolean stopped;
    protected long last;

    public ExampleGraphics2D() {
        super("Graphics2D Example");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                stop();
                super.windowClosing(e);
            }
        });

        Dimension size = new Dimension(800, 600);

        this.canvas = new Canvas();
        this.canvas.setPreferredSize(size);
        this.canvas.setMinimumSize(size);
        this.canvas.setMaximumSize(size);

        this.add(this.canvas);
        this.setResizable(false);
        this.pack();
        this.stopped = false;

        this.initializeWorld();
    }

    protected void initializeWorld() {
        this.world = new World();

        // create the floor
        Rectangle floorRect = new Rectangle(15.0, 1.0);
        GameObject floor = new GameObject();
        floor.addFixture(new BodyFixture(floorRect));
        floor.setMass(MassType.INFINITE);
        // move the floor down a bit
        floor.translate(0.0, -4.0);
        this.world.addBody(floor);

        // create a triangle object
        Triangle triShape = new Triangle(
                new Vector2(0.0, 0.5),
                new Vector2(-0.5, -0.5),
                new Vector2(0.5, -0.5));
        GameObject triangle = new GameObject();
        triangle.addFixture(triShape);
        triangle.setMass(MassType.NORMAL);
        triangle.translate(-1.0, 2.0);
        triangle.getLinearVelocity().set(5.0, 0.0);
        this.world.addBody(triangle);

        // create a circle
        Circle cirShape = new Circle(0.5);
        GameObject circle = new GameObject();
        circle.addFixture(cirShape);
        circle.setMass(MassType.NORMAL);
        circle.translate(2.0, 2.0);
        // test adding some force
        circle.applyForce(new Vector2(-100.0, 0.0));
        // set some linear damping to simulate rolling friction
        circle.setLinearDamping(0.05);
        this.world.addBody(circle);

        // try a rectangle
        Rectangle rectShape = new Rectangle(1.0, 1.0);
        GameObject rectangle = new GameObject();
        rectangle.addFixture(rectShape);
        rectangle.setMass(MassType.NORMAL);
        rectangle.translate(0.0, 2.0);
        rectangle.getLinearVelocity().set(-5.0, 0.0);
        this.world.addBody(rectangle);

        // try a polygon with lots of vertices
        Polygon polyShape = Geometry.createUnitCirclePolygon(10, 1.0);
        GameObject polygon = new GameObject();
        polygon.addFixture(polyShape);
        polygon.setMass(MassType.NORMAL);
        polygon.translate(-2.5, 2.0);
        // set the angular velocity
        polygon.setAngularVelocity(Math.toRadians(-20.0));
        this.world.addBody(polygon);

        // try a compound object
        Circle c1 = new Circle(0.5);
        BodyFixture c1Fixture = new BodyFixture(c1);
        c1Fixture.setDensity(0.5);
        Circle c2 = new Circle(0.5);
        BodyFixture c2Fixture = new BodyFixture(c2);
        c2Fixture.setDensity(0.5);
        Rectangle rm = new Rectangle(2.0, 1.0);
        // translate the circles in local coordinates
        c1.translate(-1.0, 0.0);
        c2.translate(1.0, 0.0);
        GameObject capsule = new GameObject();
        capsule.addFixture(c1Fixture);
        capsule.addFixture(c2Fixture);
        capsule.addFixture(rm);
        capsule.setMass(MassType.NORMAL);
        capsule.translate(0.0, 4.0);
        this.world.addBody(capsule);

        GameObject issTri = new GameObject();
        issTri.addFixture(Geometry.createIsoscelesTriangle(1.0, 3.0));
        issTri.setMass(MassType.NORMAL);
        issTri.translate(2.0, 3.0);
        this.world.addBody(issTri);

        GameObject equTri = new GameObject();
        equTri.addFixture(Geometry.createEquilateralTriangle(2.0));
        equTri.setMass(MassType.NORMAL);
        equTri.translate(3.0, 3.0);
        this.world.addBody(equTri);

        GameObject rightTri = new GameObject();
        rightTri.addFixture(Geometry.createRightTriangle(2.0, 1.0));
        rightTri.setMass(MassType.NORMAL);
        rightTri.translate(4.0, 3.0);
        this.world.addBody(rightTri);

        GameObject cap = new GameObject();
        cap.addFixture(new Capsule(1.0, 0.5));
        cap.setMass(MassType.NORMAL);
        cap.translate(-3.0, 3.0);
        this.world.addBody(cap);

        GameObject slice = new GameObject();
        slice.addFixture(new Slice(0.5, Math.toRadians(120)));
        slice.setMass(MassType.NORMAL);
        slice.translate(-3.0, 3.0);
        this.world.addBody(slice);
    }


    public void start() {
        this.last = System.nanoTime();
        this.canvas.setIgnoreRepaint(true);
        this.canvas.createBufferStrategy(2);
        Thread thread = new Thread() {
            public void run() {
                while (!isStopped()) {
                    gameLoop();
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    protected void gameLoop() {
        Graphics2D g = (Graphics2D)this.canvas.getBufferStrategy().getDrawGraphics();

        // before we render everything im going to flip the y axis and move the
        // origin to the center (instead of it being in the top left corner)
        AffineTransform yFlip = AffineTransform.getScaleInstance(1, -1);
        AffineTransform move = AffineTransform.getTranslateInstance(400, -300);
        g.transform(yFlip);
        g.transform(move);

        // now (0, 0) is in the center of the screen with the positive x axis
        // pointing right and the positive y axis pointing up

        // render anything about the Example (will render the World objects)
        this.render(g);

        g.dispose();

        BufferStrategy strategy = this.canvas.getBufferStrategy();
        if (!strategy.contentsLost()) {
            strategy.show();
        }

        // Sync the display on some systems.
        // (on Linux, this fixes event queue problems)
        Toolkit.getDefaultToolkit().sync();

        // update the World

        // get the current time
        long time = System.nanoTime();
        // get the elapsed time from the last iteration
        long diff = time - this.last;
        // set the last time
        this.last = time;
        // convert from nanoseconds to seconds
        double elapsedTime = diff / NANO_TO_BASE;
        // update the world with the elapsed time
        this.world.update(elapsedTime);
    }

    protected void render(Graphics2D g) {
        // lets draw over everything with a white background
        g.setColor(Color.WHITE);
        g.fillRect(-400, -300, 800, 600);

        // lets move the view up some
        g.translate(0.0, -1.0 * SCALE);

        // draw all the objects in the world
        for (int i = 0; i < this.world.getBodyCount(); i++) {
            // get the object
            GameObject go = (GameObject) this.world.getBody(i);
            // draw the object
            go.render(g);
        }
    }

    public synchronized void stop() {
        this.stopped = true;
    }

    public synchronized boolean isStopped() {
        return this.stopped;
    }

    public static void main(String[] args) throws IllegalAccessException {
        // set the look and feel to the system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // create the example JFrame
        ExampleGraphics2D window = new ExampleGraphics2D();

        // show it
        window.setVisible(true);

        // start it
        window.start();
    }
}