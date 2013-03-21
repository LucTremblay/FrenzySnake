package com.luccharles.frenzysnake;

import java.util.LinkedList;

import org.andengine.entity.Entity;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;


public class Serpent extends Entity
{
	private final TeteSerpent mHead;
	private final LinkedList<CorpsSerpent> mTail = new LinkedList<CorpsSerpent>();
	private Direction mDirection;
	private boolean mGrow;
	private final ITextureRegion mTailPartTextureRegion;
	private Direction mLastMoveDirection;

	public Serpent(final Direction pInitialDirection, final int pCellX, final int pCellY, final TiledTextureRegion pHeadTextureRegion, final ITextureRegion pTailPartTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager)
	{
		super(0, 0);
		this.mTailPartTextureRegion = pTailPartTextureRegion;
		this.mHead = new TeteSerpent(pCellX, pCellY, pHeadTextureRegion, pVertexBufferObjectManager);
		this.attachChild(this.mHead);
		this.setDirection(pInitialDirection);
	}

	public Direction getDirection()
	{
		return this.mDirection;
	}

	public void setDirection(final Direction pDirection)
	{
		if(this.mLastMoveDirection != getDirectionOppose(pDirection))
		{
			this.mDirection = pDirection;
			this.mHead.setRotation(pDirection);
		}
	}

	public int getTailLength()
	{
		return this.mTail.size();
	}

	public TeteSerpent getHead()
	{
		return this.mHead;
	}

	public void grow()
	{
		this.mGrow = true;
	}

	public int getNextX()
	{
		int pX = this.mHead.getCellX();
		switch(this.mDirection)
		{
			case UP:
			case DOWN:
				return pX;
			case LEFT:
				return pX - 1;
			case RIGHT:
				return pX + 1;
			default:
				throw new IllegalArgumentException();
		}
	}

	public int getNextY()
	{
		int pY = this.mHead.getCellY();
		switch(this.mDirection)
		{
		case LEFT:
		case RIGHT:
			return pY;
		case UP:
			return pY - 1;
		case DOWN:
			return pY + 1;
		default:
			throw new IllegalArgumentException();
		}
	}
		
	public Direction getDirectionOppose(Direction pDirection)
	{
		switch(pDirection)
		{
			case UP:
				return Direction.DOWN;
			case DOWN:
				return Direction.UP;
			case LEFT:
				return Direction.RIGHT;
			case RIGHT:
				return Direction.LEFT;
			default:
				return null;
		}
	}

	public boolean move()
	{
		boolean movementPossible = true;
		this.mLastMoveDirection = this.mDirection;
		if(this.mGrow) {
			this.mGrow = false;
			/* If the snake should grow,
			 * simply add a new part in the front of the tail,
			 * where the head currently is. */
			final CorpsSerpent newTailPart = new CorpsSerpent(this.mHead.mCellX, this.mHead.mCellY, this.mTailPartTextureRegion, this.mHead.getVertexBufferObjectManager());
			this.attachChild(newTailPart);
			this.mTail.addFirst(newTailPart);
		} else {
			if(this.mTail.isEmpty() == false) {
				/* First move the end of the tail to where the head currently is. */
				final CorpsSerpent tailEnd = this.mTail.removeLast();
				tailEnd.setCell(this.mHead.getCellX(), this.mHead.getCellY());
				this.mTail.addFirst(tailEnd);
			}
		}

		/* The move the head into the direction of the snake. */
		this.mHead.setCell(this.getNextX(), this.getNextY());
		
		/* Check if head collides with tail. */
		for(int i = this.mTail.size() - 1; i >= 0; i--) {
			if(this.mHead.isInSameCell(this.mTail.get(i).getCellX(), this.mTail.get(i).getCellY())) {
				movementPossible = false;
			}
		}
		return movementPossible;
	}
}
