package com.luccharles.frenzysnake;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;


public class CorpsSerpent extends Sprite implements Constantes {
	protected int mCellX;
	protected int mCellY;
	
	public CorpsSerpent(final int pCellX, final int pCellY, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager)
	{
		super(pCellX * CELL_WIDTH, pCellY * CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT, pTextureRegion, pVertexBufferObjectManager);
		this.mCellX = pCellX;
		this.mCellY = pCellY;
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
