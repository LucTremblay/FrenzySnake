package com.luccharles.frenzysnake;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;


public class Apple extends CellEntity {
	
	private int  m_noGrow;
	/*private ITextureRegion m_texture;*/
	

	public Apple(final int pCellX, final int pCellY, final ITextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager,int noGrow) {
		super(pCellX, pCellY, CELL_WIDTH, CELL_HEIGHT, pTiledTextureRegion, pVertexBufferObjectManager);
		m_noGrow=noGrow;
		/*m_texture=pTiledTextureRegion;*/
	}
	
	
	public int getNoGrow() {
		return this.m_noGrow;
	}
	
	/*public void setNoGrow(int size) {
		m_noGrow = size;
	}*/
	
	/*public void setTexture(ITextureRegion texture) {
		m_texture = texture;
	}*/
	
	
}
