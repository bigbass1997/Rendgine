package com.lukestadem.rendgine.graphics.opengl;

import com.lukestadem.rendgine.util.Disposable;
import org.apache.commons.lang3.ArrayUtils;
import org.liquidengine.leutil.io.IOUtil;
import org.lwjgl.stb.STBImage;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL46.*;

public class Texture implements Disposable {
	
	/**
	 * Pixel data used when this Texture was created. Changing data within the buffer will NOT change the texture.<br><br>
	 * 
	 * This is available for (sub)classes to create (un)modifed clones of the texture. (e.g. creating a pixmap from an existing texture instance)
	 */
	protected ByteBuffer texPixelData;
	
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
		
		genTexture(imageBuf);
		
		STBImage.stbi_image_free(imageBuf);
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
		
		genTexture(imageBuf);
		
		STBImage.stbi_image_free(imageBuf);
	}
	
	public Texture(ByteBuffer pixelBuf, int width, int height){
		this.width = width;
		this.height = height;
		channels = 4;
		genTexture(pixelBuf);
	}
	
	/**
	 * Creates a new texture using a {@link Pixmap}.
	 * 
	 * @param pixmap instance of {@link Pixmap} to create a texture from
	 */
	public Texture(Pixmap pixmap){
		width = pixmap.width;
		height = pixmap.height;
		channels = 4;
		
		genTexture(pixmap.getByteBuffer());
	}
	
	/**
	 * Generates a new OpenGL Texture to correspond with an instance of this class.<br><br>
	 * 
	 * {@link #width} and {@link #height} must be set before calling, and this should only be called once!
	 * 
	 * @param pixelBuf buffer containing pixel data; where each pixel is made up of four bytes, one per color channel (R, G, B, A)
	 */
	protected void genTexture(ByteBuffer pixelBuf){
		texPixelData = ByteBuffer.allocateDirect(pixelBuf.capacity());
		pixelBuf.rewind();
		texPixelData.put(pixelBuf);
		pixelBuf.rewind();
		texPixelData.flip();
		
		id = glGenTextures();
		
		glBindTexture(GL_TEXTURE_2D, id);
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixelBuf);
		
		glGenerateMipmap(GL_TEXTURE_2D);
		
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	/**
	 * Creates a new texture where by all of the pixels are duplicated and then multiplied with the provided color argument.
	 * This is useful for creating multiple textures dynamically based on a greyscale image template.
	 * 
	 * @param color ARGB8888 color to multiply with
	 * @return a new texture of the same size but with multiplied colors
	 */
	public Texture multiply(int color){
		final Pixmap pixmap = new Pixmap(this);
		pixmap.multiply(color);
		
		return new Texture(pixmap);
	}
	
	public void bind(){
		glBindTexture(GL_TEXTURE_2D, id);
	}
	
	@Override
	public void dispose(){
		glDeleteTextures(id);
	}
}
