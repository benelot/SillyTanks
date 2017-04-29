package sillytanks;

import sillytanks.entities.Terrain;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.ScreenshotAppState;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import com.jme3.util.SkyFactory;

public class Application extends SimpleApplication implements ActionListener, PhysicsCollisionListener {
	
	/** Physics Application State */
	private BulletAppState bulletAppState;
	
	private Terrain m_terrain;

	public static void main(String args[]) {
		Application app = new Application();
		app.start();
	}
	
	Geometry sphere;

	@Override
	public void simpleInitApp() {
		
		flyCam.setMoveSpeed(50);

		ScreenshotAppState state = new ScreenshotAppState();
		this.stateManager.attach(state);

		this.getCamera().setLocation(new Vector3f(0, 200, 0));
		this.getCamera().lookAt(new Vector3f(100,0,0), Vector3f.UNIT_Y);

		setupKeys(); // setup game control keys

		setupPhysics(); // setup game physics engine
		
		setupEnvironment();
		
		Material matWire = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matWire.getAdditionalRenderState().setWireframe(true);
        matWire.setColor("Color", ColorRGBA.Green);
    
        // Add 5 physics spheres to the world, with random sizes and positions
        // let them drop from the sky
        for (int i = 0; i < 5; i++) {
            float r = (float) (8 * Math.random());
            sphere = new Geometry("cannonball", new Sphere(10, 10, r));
            sphere.setMaterial(matWire);
            float x = (float) (20 * Math.random()) - 40; // random position
            float y = (float) (20 * Math.random()) - 40; // random position
            float z = (float) (20 * Math.random()) - 40; // random position
            sphere.setLocalTranslation(new Vector3f(x, 200 + y, z));
            sphere.addControl(new RigidBodyControl(new SphereCollisionShape(r), 2));
            rootNode.attachChild(sphere);
            bulletAppState.getPhysicsSpace().add(sphere);
            
        }
		
		setupTowers(); // setup towers
		
		setupVehicles(); // setup vehicles
	}

	/**
	 * Map keys to actions.
	 */
	private void setupKeys() {
		// game keys
        inputManager.addMapping("Wireframe", new KeyTrigger(KeyInput.KEY_T));
        inputManager.addMapping("PhysicsDebug", new KeyTrigger(KeyInput.KEY_G));
        inputManager.addListener(this, "Wireframe");
        inputManager.addListener(this, "PhysicsDebug");
        
        // character keys
        inputManager.addMapping("CharLeft", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("CharRight", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("CharForward", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("CharBackward", new KeyTrigger(KeyInput.KEY_S));
//        inputManager.addMapping("CharEscape", new KeyTrigger(KeyInput.KEY_ESCAPE));
        inputManager.addMapping("CharShoot", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(this, "CharLeft"); // Turn character left
        inputManager.addListener(this, "CharRight"); // Turn character right
        inputManager.addListener(this, "CharForward"); // Move character forward
        inputManager.addListener(this, "CharBackward"); // Move character backward
//        inputManager.addListener(this, "CharEscape"); // Make character escape vehicle
        inputManager.addListener(this, "CharShoot"); // Shoot
    }
	
	/**
	 * Setup bullet physics.
	 */
    private void setupPhysics() {
        bulletAppState = new BulletAppState();
        bulletAppState.setThreadingType(BulletAppState.ThreadingType.PARALLEL);
	    stateManager.attach(bulletAppState);
//	    bulletAppState.getPhysicsSpace().enableDebug(assetManager);
	}
    
	/**
	 * Setup terrain.
	 */
	private void setupEnvironment() {
        
		getRootNode().attachChild(SkyFactory.createSky(getAssetManager(), "Textures/Sky/sky.tga", SkyFactory.EnvMapType.EquirectMap));
			    
		m_terrain = new Terrain();
		m_terrain.createTerrain(this);
		
	    DirectionalLight light = new DirectionalLight();
        light.setDirection((new Vector3f(-0.5f, -1f, -0.5f)).normalize());
        rootNode.addLight(light);

        AmbientLight ambLight = new AmbientLight();
        ambLight.setColor(new ColorRGBA(1f, 1f, 0.8f, 0.2f));
        rootNode.addLight(ambLight);
	}
    
    /**
     * Setup towers.
     */
	private void setupTowers() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Setup vehicles.
	 */
	private void setupVehicles() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Setup characters.
	 */
	private void setupCharacters() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void simpleUpdate(float tpf){
		super.simpleUpdate(tpf);
	}

	@Override
	public void onAction(String binding, boolean value, float tpf){
		
	}

	@Override
	public void collision(PhysicsCollisionEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	public BulletAppState getBulletAppState() {
		return bulletAppState;
	}
	
	@Override
	public Node getRootNode(){
		return rootNode;
	}
}
