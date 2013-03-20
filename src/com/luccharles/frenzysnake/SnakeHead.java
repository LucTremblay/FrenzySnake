package com.luccharles.frenzysnake;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;


public class SnakeHead extends AnimatedCellEntity {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public SnakeHead(final int pCellX, final int pCellY, final TiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pCellX, pCellY, CELL_WIDTH, CELL_HEIGHT, pTiledTextureRegion, pVertexBufferObjectManager);

		this.setRotationCenterY(CELL_HEIGHT / 2);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void setRotation(final Direction pDirection) {
		switch(pDirection) {
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

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
