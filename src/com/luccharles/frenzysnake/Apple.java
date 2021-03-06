package com.luccharles.frenzysnake;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;


public class Apple extends Sprite implements Constantes {
	private int  m_noGrow;
	protected int mCellX;
	protected int mCellY;
	

	public Apple(final int pCellX, final int pCellY, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, int nGrow)
	{
		super(pCellX * CELL_WIDTH, pCellY * CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT, pTextureRegion, pVertexBufferObjectManager);
		this.mCellX = pCellX;
		this.mCellY = pCellY;
		m_noGrow = nGrow;
	}
	
	
	public int getNoGrow() {
		return this.m_noGrow;
	}
	

	public int getCellX()
	{
		return this.mCellX;
	}

	public int getCellY()
	{
		return this.mCellY;
	}

	public void setCell(final int pCellX, final int pCellY)
	{
		this.mCellX = pCellX;
		this.mCellY = pCellY;
		this.setPosition(this.mCellX * CELL_WIDTH, this.mCellY * CELL_HEIGHT);
	}
	
}
