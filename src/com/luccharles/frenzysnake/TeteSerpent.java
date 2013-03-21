package com.luccharles.frenzysnake;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;


public class TeteSerpent extends AnimatedSprite implements Constantes
{
	protected int mCellX;
	protected int mCellY;

	public TeteSerpent(final int pCellX, final int pCellY, final TiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager)
	{
		super(pCellX * CELL_WIDTH, pCellY * CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT, pTiledTextureRegion, pVertexBufferObjectManager);
		this.mCellX = pCellX;
		this.mCellY = pCellY;
		this.setRotationCenterY(CELL_HEIGHT / 2);
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
	
	public boolean isInSameCell(final int pCellX, final int pCellY)
	{
		return this.mCellX == pCellX && this.mCellY ==pCellY;
	}

	public void setRotation(final Direction pDirection)
	{
		switch(pDirection)
		{
			case UP:
				this.setRotation(180);
				break;
			case DOWN:
				this.setRotation(0);
				break;
			case LEFT:
				this.setRotation(90);
				break;
			case RIGHT:
				this.setRotation(270);
				break;
		}
	}
}
