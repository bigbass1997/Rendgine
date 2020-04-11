package com.lukestadem.rendgine.graphics.font;

import com.lukestadem.rendgine.graphics.Color;
import com.lukestadem.rendgine.graphics.TextureRenderer;
import com.lukestadem.rendgine.graphics.opengl.Texture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BitmapFont {
	
	private final Texture tex;
	
	private Map<Character, Glyph> glyphMap;
	
	private String name;
	private String face;
	private int size;
	private boolean bold;
	private boolean italic;
	
	public float letterSpacing;
	
	/**
	 * See {@link #BitmapFont(String, Texture, String)}
	 */
	public BitmapFont(Texture tex, String fntData){
		this(null, tex, fntData);
	}
	
	/**
	 * Creates a new BitmapFont given the texture and font data provided. <b>FNT file data must only have 1 page, or
	 * this constructor will throw a {@link RuntimeException}!</b>
	 * 
	 * @param name name to identify this font
	 * @param tex texture that this font's glyphs come from
	 * @param fntData .fnt file contents, with {@code \r\n} formated carriage returns
	 */
	public BitmapFont(String name, Texture tex, String fntData){
		this.tex = tex;
		
		glyphMap = new HashMap<Character, Glyph>();
		
		for(String line : fntData.split("\r\n")){
			final String[] frags = line.split(" ");
			final Map<String, String> keyValPairs = new HashMap<String, String>();
			for(String frag : frags){
				if(frag.trim().isEmpty()){
					continue;
				}
				
				final String[] split = frag.split("=", 2);
				if(split.length != 2){
					continue;
				}
				keyValPairs.put(split[0].trim(), split[1].replace("\"", "").replace("'", "").trim());
			}
			
			if(line.startsWith("info")){
				if(name == null){
					this.name = keyValPairs.getOrDefault("face", "");
				} else {
					this.name = name;
				}
				
				face = keyValPairs.getOrDefault("face", "");
				
				try {
					size = Integer.parseInt(keyValPairs.getOrDefault("size", "0"));
				} catch(NumberFormatException e) { size = 0; }
				
				try {
					bold = ( 1 == Integer.parseInt(keyValPairs.getOrDefault("bold", "0")) );
				} catch(NumberFormatException e) { bold = false; }
				
				try {
					italic = ( 1 == Integer.parseInt(keyValPairs.getOrDefault("italic", "0")) );
				} catch(NumberFormatException e) { italic = false; }
			}
			
			if(line.startsWith("common") && !keyValPairs.getOrDefault("pages", "0").equals("1")){
				throw new RuntimeException("Provided fnt file must contain only 1 page!");
			}
			
			if(line.startsWith("char ")){
				try {
					final Glyph glyph = new Glyph();
					glyph.charid = Integer.parseInt(keyValPairs.getOrDefault("id", "-1"));
					glyph.xOffset = Integer.parseInt(keyValPairs.getOrDefault("xoffset", "0"));
					glyph.yOffset = Integer.parseInt(keyValPairs.getOrDefault("yoffset", "0"));
					glyph.xAdvance = Integer.parseInt(keyValPairs.getOrDefault("xadvance", "0"));
					
					final int x = Integer.parseInt(keyValPairs.getOrDefault("x", "0"));
					final int y = Integer.parseInt(keyValPairs.getOrDefault("y", "0"));
					final int width = Integer.parseInt(keyValPairs.getOrDefault("width", "0"));
					final int height = Integer.parseInt(keyValPairs.getOrDefault("height", "0"));
					
					final int tW = tex.width;
					final int tH = tex.height;
					
					glyph.u = (x / (float) tW);
					glyph.v = (y / (float) tH);
					glyph.u2 = ((x + width) / (float) tW);
					glyph.v2 = ((y + height) / (float) tH);
					
					glyph.v = -glyph.v + tex.height;
					glyph.v2 = -glyph.v2 + tex.height;
					
					glyph.width = width;
					glyph.height = height;
					
					glyphMap.put((char) glyph.charid, glyph);
				} catch(NumberFormatException e) {
					e.printStackTrace();
				}
			}
		}
		
		if(this.name == null){
			this.name = "";
		}
		
		letterSpacing = 0;
	}
	
	public void render(TextureRenderer tr, CharSequence text, float xOrigin, float yOrigin){
		render(tr, text, xOrigin, yOrigin, 1, 1, 1, 1);
	}
	
	public void render(TextureRenderer tr, CharSequence text, float xOrigin, float yOrigin, Color color){
		render(tr, text, xOrigin, yOrigin, color.r, color.g, color.b, color.a);
	}
	
	/**
	 * Draws the series of textures for each character given by the {@code text} parameter. This method
	 * only works if {@link TextureRenderer#isDrawing()} is true.
	 * 
	 * @param tr TextureRenderer to use for rendering
	 * @param text the text to be rendered using this font
	 */
	public void render(TextureRenderer tr, CharSequence text, float xOrigin, float yOrigin, float r, float g, float b, float a){
		if(tr.isDrawing()){
			float curX = xOrigin;
			for(int i = 0; i < text.length(); i++){
				final Glyph glyph = glyphMap.get(text.charAt(i));
				if(glyph != null){
					tr.texture(tex, curX + glyph.xOffset, yOrigin - glyph.yOffset, glyph.width, glyph.height, glyph.u, glyph.v2, glyph.u2, glyph.v, r, g, b, a);
					curX += glyph.xAdvance + letterSpacing;
				}
			}
		}
	}
	
	public Texture getTexture(){
		return tex;
	}
	
	public String getName(){
		return name;
	}
	
	public String getFace(){
		return face;
	}
	
	public int getSize(){
		return size;
	}
	
	public boolean isBold(){
		return bold;
	}
	
	public boolean isItalic(){
		return italic;
	}
}
