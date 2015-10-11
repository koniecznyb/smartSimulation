package org.redi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

/**
 * Created by redi on 2015-10-10.
 */
public class Tank extends Sprite{

    private Vector2 position;

    private float movementSpeed = 10;
    private float rotationSpeed = 5;

    private boolean alive = true;
    private Sprite tankSprite;
    private final Camera camera;
    private final World world;
    private Body body;

    public Tank(Camera camera, World world, Vector2 position) {
        tankSprite = Resources.getInstance().tankSprite;

        this.camera = camera;
        this.world = world;
        this.position = position;
    }

    public void init(){
        tankSprite.setRotation(-90f);
        tankSprite.setPosition(position.x, position.y);
        tankSprite.setSize(10, 21.26760f);
        tankSprite.setOriginCenter();
        tankSprite.scale(0.000001f);
        initDynamicBody();
    }

    public void update(){
        position = body.getPosition();

        float hw = tankSprite.getWidth() / 2.0f;
        float hh = tankSprite.getHeight() / 2.0f;
        float a = body.getAngle() * MathUtils.radiansToDegrees;
        float x = position.x - hw;
        float y = position.y - hh;

        tankSprite.setPosition(x, y);
        tankSprite.setRotation(a);
    }


    public void draw(Batch batch){
        if(alive == false) return;
        else{
            batch.draw(tankSprite, tankSprite.getX(), tankSprite.getY());
        }
    }

    private void initDynamicBody(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(tankSprite.getX(), tankSprite.getY());

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.6f;

        body = world.createBody(bodyDef);
        body.setUserData(tankSprite);
    }

    public void handleInput() {
        Vector2 direction = new Vector2(), velocity = new Vector2();
        direction.x = (float) Math.cos(Math.toRadians(tankSprite.getRotation()+90f));
        direction.y = (float) Math.sin(Math.toRadians(tankSprite.getRotation()+90f));
        direction = direction.nor();

        velocity.x = direction.x * movementSpeed;
        velocity.y = direction.y * movementSpeed;

        float timeElapsed = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            tankSprite.rotate(rotationSpeed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            tankSprite.rotate(-rotationSpeed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
//            sprite.setPosition(direction.x, direction.y - speed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
//            tankSprite.setX(tankSprite.getX() + (velocity.x * timeElapsed));
//            tankSprite.setY(tankSprite.getY() + (velocity.y * timeElapsed));

            Vector2 force = new Vector2();
            force.set(MathUtils.cos(body.getAngle()), MathUtils.sin(body.getAngle()));
            System.out.println(force);
            body.applyForce(force, new Vector2(0, 0), false);
        }

    }

    public Sprite getTankSprite() {
        return tankSprite;
    }
}
