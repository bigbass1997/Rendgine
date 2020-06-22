package com.lukestadem.rendgine.graphics.opengl;

import com.lukestadem.rendgine.util.Disposable;
import org.liquidengine.leutil.io.IOUtil;
import org.lwjgl.stb.STBImage;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL46.*;

public class Texture implements Disposable {
	
	/** OpenGL id for this texture. <b>Do not reassign!</b> */
	public int id;
	/** Width of the texture. <b>Do not reassign!</b> */
	public int width;
	/** Height of the texture. <b>Do not reassign!</b> */
	public int height;
	/** Texture channels. <b>Do not reassign!</b> */
	public int channels;
	
	/**
	 * Creates a new texture from a file located at this path. Useful for loading assets from the classpath.
	 * 
	 * @param filename name/path of the file
	 */
	public Texture(String filename){
		ByteBuffer fileBuf = null;
		try {
			fileBuf = IOUtil.resourceToByteBuffer(filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		init(fileBuf);
	}
	
	/**
	 * Creates a new texture from a file located at the given file. This may not work for files inside the classpath.
	 * 
	 * @param file name/path of the file
	 */
	public Texture(File file){
		ByteBuffer fileBuf = null;
		try {
			fileBuf = IOUtil.resourceToByteBuffer(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		init(fileBuf);
	}
	
	private void init(ByteBuffer fileBuf){
		final int[] tmpWidth = new int[1];
		final int[] tmpHeight = new int[1];
		final int[] tmpChannels = new int[1];
		
		STBImage.stbi_set_flip_vertically_on_load(true);
		final ByteBuffer imageBuf = STBImage.stbi_load_from_memory(fileBuf, tmpWidth, tmpHeight, tmpChannels, 4);
		
		if(imageBuf == null){
			throw new RuntimeException("Image file could not be loaded: " + STBImage.stbi_failure_reason());
		}
		
		width = tmpWidth[0];
		height = tmpHeight[0];
		channels = tmpChannels[0];
		
		id = glGenTextures();
		
		glBindTexture(GL_TEXTURE_2D, id);
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, imageBuf);
		glGenerateMipmap(GL_TEXTURE_2D);
		
		STBImage.stbi_image_free(imageBuf);
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public void bind(){
		glBindTexture(GL_TEXTURE_2D, id);
	}
	
	@Override
	public void dispose(){
		glDeleteTextures(id);
	}
}
