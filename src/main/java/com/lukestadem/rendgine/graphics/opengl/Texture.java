package com.lukestadem.rendgine.graphics.opengl;

import com.lukestadem.rendgine.util.Disposable;
import org.liquidengine.leutil.io.IOUtil;
import org.lwjgl.stb.STBImage;

import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL46.*;

public class Texture implements Disposable {
	
	public final int id;
	public final int width;
	public final int height;
	public final int channels;
	
	public Texture(String filename){
		final int[] tmpWidth = new int[1];
		final int[] tmpHeight = new int[1];
		final int[] tmpChannels = new int[1];
		
		ByteBuffer fileBuf = null;
		try {
			fileBuf = IOUtil.resourceToByteBuffer(filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		STBImage.stbi_set_flip_vertically_on_load(true);
		final ByteBuffer imageBuf = STBImage.stbi_load_from_memory(fileBuf, tmpWidth, tmpHeight, tmpChannels, 4);
		
		if(imageBuf == null){
			throw new RuntimeException("Image file \"" + filename + "\" could not be loaded: " + STBImage.stbi_failure_reason());
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
