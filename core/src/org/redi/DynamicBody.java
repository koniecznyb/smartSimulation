package org.redi;

import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by redi on 2015-10-04.
 */

public class DynamicBody {

    private final World world;
    private float initialPositionX = 0, initialPositionY = 0;

    private Body body;

    public DynamicBody(World world) {
        this.world = world;
    }

    public DynamicBody(World world, float initialPositionX, float initialPositionY) {
        this(world);
        this.initialPositionX = initialPositionX;
        this.initialPositionY = initialPositionY;
    }

    public void initBody(){
        // First we create a body definition
        BodyDef bodyDef = new BodyDef();
// We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
        bodyDef.type = BodyDef.BodyType.DynamicBody;
// Set our body's starting position in the world
        bodyDef.position.set(initialPositionX, initialPositionY);

// Create our body in the world using our body definition
        body = world.createBody(bodyDef);

// Create a circle shape and set its radius to 6
        CircleShape circle = new CircleShape();
        circle.setRadius(6f);

// Create a fixture definition to apply our shape to
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.6f; // Make it bounce a little bit

// Create our fixture and attach it to the body
        Fixture fixture = body.createFixture(fixtureDef);

// Remember to dispose of any shapes after you're done with them!
// BodyDef and FixtureDef don't need disposing, but shapes do.
//        circle.dispose();
    }


    public Body getBody() {
        return body;
    }

    public float getInitialPositionX() {
        return initialPositionX;
    }

    public void setInitialPositionX(float initialPositionX) {
        this.initialPositionX = initialPositionX;
    }

    public float getInitialPositionY() {
        return initialPositionY;
    }

    public void setInitialPositionY(float initialPositionY) {
        this.initialPositionY = initialPositionY;
    }
}


