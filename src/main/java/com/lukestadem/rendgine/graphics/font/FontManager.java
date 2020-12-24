package com.lukestadem.rendgine.graphics.font;

import com.lukestadem.rendgine.graphics.opengl.Texture;
import org.liquidengine.leutil.io.IOUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FontManager {
	
	private static Map<String, BitmapFont> fontCache;
	
	private FontManager(){
		fontCache = new HashMap<String, BitmapFont>();
	}
	
	private static class LazyHolder {
		private static final FontManager INSTANCE = new FontManager();
	}
	
	public static FontManager getInstance(){
		return LazyHolder.INSTANCE;
	}
	
	public BitmapFont loadFont(String name, String pngPath, String fntPath){
		BitmapFont font;
		try {
			final Texture tex = new Texture(pngPath);
			final String fntData = IOUtil.resourceToString(fntPath);
			font = new BitmapFont(name, tex, fntData);
			fontCache.put(name, font);
		} catch(RuntimeException | IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return font;
	}
	
	public BitmapFont getFont(String name){
		if(name == null){
			return null;
		}
		
		return fontCache.get(name);
	}
}
