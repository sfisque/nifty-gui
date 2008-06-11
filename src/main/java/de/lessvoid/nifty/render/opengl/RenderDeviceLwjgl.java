package de.lessvoid.nifty.render.opengl;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import de.lessvoid.nifty.render.RenderDevice;
import de.lessvoid.nifty.render.RenderFont;
import de.lessvoid.nifty.render.RenderImage;

/**
 * Lwjgl RenderDevice Implementation.
 * @author void
 */
public class RenderDeviceLwjgl implements RenderDevice {

  /**
   * Buffersize.
   */
  private static final int INTERNAL_BUFFERSIZE_IN_BYTES = 1024;

  /**
   * ByteBuffer.
   */
  private static ByteBuffer byteBuffer = BufferUtils.createByteBuffer(INTERNAL_BUFFERSIZE_IN_BYTES);

  /**
   * DoubleBuffer.
   */
  private static DoubleBuffer doubleBuffer = byteBuffer.asDoubleBuffer();

  /**
   * FloatBuffer.
   */
  private static FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();

  /**
   * Get Width.
   * @return width of display mode
   */
  public int getWidth() {
    return Display.getDisplayMode().getWidth();
  }

  /**
   * Get Height.
   * @return height of display mode
   */
  public int getHeight() {
    return Display.getDisplayMode().getHeight();
  }

  /**
   * Clear Screen.
   */
  public void clear() {
    GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
  }

  /**
   * Create a new RenderImage.
   * @param filename filename
   * @param filterLinear linear filter the image
   * @return RenderImage
   */
  public RenderImage createImage(final String filename, final boolean filterLinear) {
    return new RenderImageLwjgl(filename, filterLinear);
  }

  /**
   * Create a new RenderFont.
   * @param filename filename
   * @return RenderFont
   */
  public RenderFont createFont(final String filename) {
    return new RenderFontLwjgl(filename);
  }

  /**
   * Render a quad.
   * @param x x
   * @param y y
   * @param width width
   * @param height height
   */
  public void renderQuad(final int x, final int y, final int width, final int height) {
    GL11.glBegin(GL11.GL_QUADS);
      GL11.glVertex2i(x,         y);
      GL11.glVertex2i(x + width, y);
      GL11.glVertex2i(x + width, y + height);
      GL11.glVertex2i(x,         y + height);
    GL11.glEnd();
  }

  /**
   * Enable Blendmode.
   */
  public void enableBlend() {
    GL11.glEnable(GL11.GL_BLEND);
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
  }

  /**
   * Enable clipping to the given region.
   * @param x0 x0
   * @param y0 y0
   * @param x1 x1
   * @param y1 y1
   */
  public void enableClip(final int x0, final int y0, final int x1, final int y1) {
    GL11.glEnable(GL11.GL_CLIP_PLANE0);
    GL11.glEnable(GL11.GL_CLIP_PLANE1);
    GL11.glEnable(GL11.GL_CLIP_PLANE2);
    GL11.glEnable(GL11.GL_CLIP_PLANE3);

    doubleBuffer.clear();
    doubleBuffer.put(1).put(0).put(0).put(-x0).flip();
    GL11.glClipPlane(GL11.GL_CLIP_PLANE0, doubleBuffer);
    doubleBuffer.clear();
    doubleBuffer.put(-1).put(0).put(0).put(x1).flip();
    GL11.glClipPlane(GL11.GL_CLIP_PLANE1, doubleBuffer);
    doubleBuffer.clear();
    doubleBuffer.put(0).put(1).put(0).put(-y0).flip();
    GL11.glClipPlane(GL11.GL_CLIP_PLANE2, doubleBuffer);
    doubleBuffer.clear();
    doubleBuffer.put(0).put(-1).put(0).put(y1).flip();
    GL11.glClipPlane(GL11.GL_CLIP_PLANE3, doubleBuffer);
  }

  /**
   * Disable Clip.
   */
  public void disableClip() {
    GL11.glDisable(GL11.GL_CLIP_PLANE0);
    GL11.glDisable(GL11.GL_CLIP_PLANE1);
    GL11.glDisable(GL11.GL_CLIP_PLANE2);
    GL11.glDisable(GL11.GL_CLIP_PLANE3);
  }

}
