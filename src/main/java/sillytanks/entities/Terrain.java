package sillytanks.entities;

import java.util.ArrayList;
import java.util.List;

import sillytanks.Application;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;

public class Terrain extends PhysicsEntity{

    private TerrainQuad terrain;
    private Material mat_terrain;

    public void createTerrain(Application app) {
    	/** 1. Create terrain material and load four textures into it. */
    	mat_terrain = new Material(app.getAssetManager(),
    			"Common/MatDefs/Terrain/Terrain.j3md");

    	/** 1.1) Add ALPHA map (for red-blue-green coded splat textures) */
    	mat_terrain.setTexture("Alpha", app.getAssetManager().loadTexture(
    			"Textures/Terrain/levels/sand_tex.tga"));

    	//    /** 1.2) Add GRASS texture into the red layer (Tex1). */
    	//    Texture grass = assetManager.loadTexture(
    	//            "Textures/Terrain/splat/grass.jpg");
    	//    grass.setWrap(WrapMode.Repeat);
    	//    mat_terrain.setTexture("Tex1", grass);
    	//    mat_terrain.setFloat("Tex1Scale", 64f);
    	//
    	//    /** 1.3) Add DIRT texture into the green layer (Tex2) */
    	//    Texture dirt = assetManager.loadTexture(
    	//            "Textures/Terrain/splat/dirt.jpg");
    	//    dirt.setWrap(WrapMode.Repeat);
    	//    mat_terrain.setTexture("Tex2", dirt);
    	//    mat_terrain.setFloat("Tex2Scale", 32f);
    	//
    	//    /** 1.4) Add ROAD texture into the blue layer (Tex3) */
    	//    Texture rock = assetManager.loadTexture(
    	//            "Textures/Terrain/splat/road.jpg");
    	//    rock.setWrap(WrapMode.Repeat);
    	//    mat_terrain.setTexture("Tex3", rock);
    	//    mat_terrain.setFloat("Tex3Scale", 128f);

    	/** 2. Create the height map */
    	AbstractHeightMap heightmap = null;
    	Texture heightMapImage = app.getAssetManager().loadTexture(
    			"Textures/Terrain/levels/sand_hf.tga");
    	heightmap = new ImageBasedHeightMap(heightMapImage.getImage());
    	heightmap.load();

    	int patchSize = 65;
    	terrain = new TerrainQuad("terrain", patchSize, 513, heightmap.getHeightMap());

    	/** 4. We give the terrain its material, position & scale it, and attach it. */
    	terrain.setMaterial(mat_terrain);
    	terrain.setLocalTranslation(250, -100, 250);
    	terrain.setLocalScale(2f, 1f, 2f);
    	app.getRootNode().attachChild(terrain);

    	/** 5. The LOD (level of detail) depends on were the camera is: */
    	TerrainLodControl control = new TerrainLodControl(terrain, app.getCamera());
    	terrain.addControl(control);
    	
    	terrain.addControl(new RigidBodyControl(0));
        app.getBulletAppState().getPhysicsSpace().addAll(terrain);
    }
    
    private void adjustHeight(Vector3f loc, float radius, float height) {

        // offset it by radius because in the loop we iterate through 2 radii
        int radiusStepsX = (int) (radius / terrain.getLocalScale().x);
        int radiusStepsZ = (int) (radius / terrain.getLocalScale().z);

        float xStepAmount = terrain.getLocalScale().x;
        float zStepAmount = terrain.getLocalScale().z;
        long start = System.currentTimeMillis();
        List<Vector2f> locs = new ArrayList<Vector2f>();
        List<Float> heights = new ArrayList<Float>();
        
        for (int z = -radiusStepsZ; z < radiusStepsZ; z++) {
            for (int x = -radiusStepsX; x < radiusStepsX; x++) {

                float locX = loc.x + (x * xStepAmount);
                float locZ = loc.z + (z * zStepAmount);

                if (isInRadius(locX - loc.x, locZ - loc.z, radius)) {
                    // see if it is in the radius of the tool
                    float h = calculateHeight(radius, height, locX - loc.x, locZ - loc.z);
                    locs.add(new Vector2f(locX, locZ));
                    heights.add(h);
                }
            }
        }

        terrain.adjustHeight(locs, heights);
        //System.out.println("Modified "+locs.size()+" points, took: " + (System.currentTimeMillis() - start)+" ms");
        terrain.updateModelBound();
    }
    
    private boolean isInRadius(float x, float y, float radius) {
        Vector2f point = new Vector2f(x, y);
        // return true if the distance is less than equal to the radius
        return point.length() <= radius;
    }

    private float calculateHeight(float radius, float heightFactor, float x, float z) {
        // find percentage for each 'unit' in radius
        Vector2f point = new Vector2f(x, z);
        float val = point.length() / radius;
        val = 1 - val;
        if (val <= 0) {
            val = 0;
        }
        return heightFactor * val;
    }

}
