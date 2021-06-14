package net.kappabyte.kappaengine.physics;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.joml.Vector3f;

import net.kappabyte.kappaengine.math.Time;
import net.kappabyte.kappaengine.util.Log;

public class Rigidbody extends AABBCollider {

    public ArrayList<Force> forces = new ArrayList<Force>();
    Supplier<List<Collider>> colliderSupplier = Collider::getAllColliders;
    public Vector3f velocity = new Vector3f();
    public float mass = 1.0f;

    public float terminalVelocity = 15f;

    public float gravity = -9.81f;

    public boolean onGround = false;

    public Rigidbody(float mass) {
        super(new Vector3f(-0.4f, 0, -0.4f), new Vector3f(0.4f, 1.9f, 0.4f));
        this.mass = mass;
        //debugEnabled = true;
    }

    public void applyForce(Vector3f force) {
        forces.add(new Force(force));
    }

    public void setColliderSupplier(Supplier<List<Collider>> colliderSupplier) {
        this.colliderSupplier = colliderSupplier;
    }

    public void applyForce(Vector3f force, float time) {
        forces.add(new Force(force, time));
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public List<Collider> getCollisions() {
        return super.getCollisions(colliderSupplier.get());
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        float time = Time.deltaTime();

        // Get net force acting on object
        Vector3f netForce = new Vector3f();
        for (Force force : forces) {
            netForce.add(force.force);
            force.time -= time;
        }
        forces.removeIf(force -> force.time <= 0);

        netForce.add(0, gravity * mass, 0);

        // Get acceleration of the object
        // F = ma
        Vector3f acceleration = netForce.div(mass);

        // Get final velocity
        // a = (dv/dt)
        // a = (v2 - v1)t
        // at + v1 = v2
        velocity.add(new Vector3f(acceleration).mul(time));

        if(velocity.length() > terminalVelocity) {
            velocity.normalize(terminalVelocity);
        }

        // Calculate displacement
        Vector3f displacement = new Vector3f(velocity).mul(time)
                .sub(new Vector3f(acceleration).mul(0.5f).mul(time * time));
        if(displacement.length() > terminalVelocity) {
            displacement.normalize(terminalVelocity);
        }
        //Collision Detection
        List<Collider> colliders = colliderSupplier.get();
        for(Collider collider : getCollisions(colliders)) {
            if(!(collider instanceof AABBCollider)) return;
            AABBCollider other = (AABBCollider) collider;
            reactToCollision(other, displacement, velocity);
        }
        getTransform().addPosition(displacement);
    }
    public void reactToCollision(AABBCollider other, Vector3f displacement) {
        reactToCollision(other, displacement, new Vector3f());
    }
    public void reactToCollision(AABBCollider other, Vector3f displacement, Vector3f velocity) {
        //+x
        onGround = false;
        Vector3f thisMin = getMinAbsolute();
        Vector3f thisMax = getMaxAbsolute();
        Vector3f otherMin = other.getMinAbsolute();
        Vector3f otherMax = other.getMaxAbsolute();
        if((thisMin.x + displacement.x < otherMax.x && thisMax.x + displacement.x > otherMax.x)
                && (thisMin.y + displacement.y < otherMax.y - getSize().y/2 && thisMax.y + displacement.y > otherMin.y + getSize().y/2)
                && (thisMin.z + displacement.z < otherMax.z - getSize().z/2 && thisMax.z + displacement.z > otherMin.z + getSize().z/2)) {
                    displacement.x += otherMax.x - (thisMin.x + displacement.x);
                    velocity.x = 0;
            }
            //+z
            if((thisMin.x + displacement.x < otherMax.x - getSize().x/2 && thisMax.x + displacement.x > otherMin.x + getSize().x/2)
                && (thisMin.y + displacement.y < otherMax.y - getSize().y/2 && thisMax.y + displacement.y > otherMin.y + getSize().y/2)
                && (thisMin.z + displacement.z < otherMax.z && thisMax.z + displacement.z > otherMax.z)) {
                    displacement.z += otherMax.z - (thisMin.z + displacement.z);
                    velocity.z = 0;
            }
            // -x face
            if((thisMin.x + displacement.x < otherMin.x && thisMax.x + displacement.x > otherMin.x)
                && (thisMin.y + displacement.y < otherMax.y - getSize().y/2 && thisMax.y + displacement.y > otherMin.y + getSize().y/2)
                && (thisMin.z + displacement.z < otherMax.z - getSize().z/2 && thisMax.z + displacement.z > otherMin.z + getSize().z/2)) {
                    displacement.x += otherMin.x - (thisMax.x + displacement.x);
                    velocity.x = 0;
            }
            //-z
            if((thisMin.x + displacement.x < otherMax.x - getSize().x/2 && thisMax.x + displacement.x > otherMin.x - getSize().x/2)
                && (thisMin.y + displacement.y < otherMax.y - getSize().y/2 && thisMax.y + displacement.y > otherMin.y - getSize().y/2)
                && (thisMin.z + displacement.z < otherMin.z && thisMax.z + displacement.z > otherMin.z)) {
                    displacement.z += otherMin.z - (thisMax.z + displacement.z);
                    velocity.z = 0;
            }
            //+y
            if((thisMin.x + displacement.x < otherMax.x - getSize().x/2 && thisMax.x + displacement.x > otherMin.x + getSize().x/2)
                && (thisMin.y + displacement.y < otherMax.y && thisMax.y + displacement.y > otherMax.y)
                && (thisMin.z + displacement.z < otherMax.z - getSize().z/2 && thisMax.z + displacement.z > otherMin.z + getSize().z/2)) {
                    displacement.y += otherMax.y - (thisMin.y + displacement.y);
                    velocity.y = 0;
                    onGround = true;
            }
            //-y
            if((thisMin.x + displacement.x < otherMax.x - getSize().x/2 && thisMax.x + displacement.x > otherMin.x + getSize().x/2)
                && (thisMin.y + displacement.y < otherMin.y && thisMax.y + displacement.y > otherMin.y)
                && (thisMin.z + displacement.z < otherMax.z - getSize().z/2 && thisMax.z + displacement.z > otherMin.z + getSize().z/2)) {
                    displacement.y += otherMin.y - (thisMax.y + displacement.y);
                    velocity.y = 0;
            }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
