package simulator;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import simulator.WorldLoader.WorldLoaderObject.WorldSceneObject;
import simulator.WorldLoader.WorldLoaderObject.WorldSceneObject.WorldSceneObjectArgs;
import simulator.sceneobjects.*;
import simulator.sceneobjects.SceneObject;


public class WorldLoader {
	
	public class WorldLoaderObject {
		
		public WorldSceneObject camera;
		@SerializedName("static")
		public WorldSceneObject[] staticObjects;
		@SerializedName("dynamic")
		public WorldSceneObject[] dynamicObjects;
		
		public class WorldSceneObject {
			
			public class WorldSceneObjectArgs {
				public double x;
				public double y;
				public double z;
				
				public double xscale;
				public double yscale;
				public double zscale;
				
				public double yaw;
				public double pitch;
				public double roll;
				
				public WorldSceneObjectArgs() {
					
				}
				
			}
			

			public String classname;
			public WorldSceneObjectArgs args;

		}
		
	}
	
	
	
	FileSystem fs = FileSystems.getDefault();
	
	public WorldLoader(World world, String filename) {
		
		try {			
			Path file = Paths.get(filename);
			InputStream input = Files.newInputStream(file);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			String text = null;
			String contents = new String();
			
			while ((text = reader.readLine()) != null) {
				contents = contents.concat(text);
			}
			
			WorldLoaderObject obj = (new Gson()).fromJson(contents, WorldLoaderObject.class);
			
			// instantiate static scene objects
			for(WorldSceneObject staticObj : obj.staticObjects) {
				try {
					Class<?> myClass = Class.forName("simulator.sceneobjects." + staticObj.classname);
					Constructor<?> myConstructor = myClass.getConstructor(WorldSceneObjectArgs.class);
					SceneObject sceneObj = (SceneObject) myConstructor.newInstance(staticObj.args);
					sceneObj.dynamic = false;
					world.addSceneObject( sceneObj );
				}
				
				catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
				
				catch (SecurityException e) {
					e.printStackTrace();
				}
				
				catch (InstantiationException e) {
					e.printStackTrace();
				}
				
				catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				
				catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
				
				catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				
				catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				
			}
			// instantiate dynamic scene objects
			for(WorldSceneObject dynamicObj : obj.dynamicObjects) {
				try {
					Class<?> myClass = Class.forName(dynamicObj.classname);
					Constructor<?> myConstructor = myClass.getConstructor();
					SceneObject sceneObj = (SceneObject) myConstructor.newInstance();				
					world.addSceneObject( sceneObj );
				}
				
				catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
				
				catch (SecurityException e) {
					e.printStackTrace();
				}
				
				catch (InstantiationException e) {
					e.printStackTrace();
				}
				
				catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				
				catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
				
				catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				
				catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				
			}
		} 
		
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	/*public static void main(String[] args) {
		String filename = "E:\\Development\\Projects\\Web\\WSClient\\bin\\client\\data\\map.dat";
		WorldLoader loader = new WorldLoader(null, filename);		
		//loader.load(world);
	
	}*/
}

