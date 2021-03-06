package com.csc.chess.pieces;

import com.csc.chess.board.Alliance;
import com.csc.chess.board.Board;
import com.csc.chess.board.BoardUtils;
import com.csc.chess.board.Move;
import com.csc.chess.board.Move.PawnAttackMove;
import com.csc.chess.board.Move.PawnJump;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Pawn extends Piece {

	private final static int[] CANDIDATE_MOVE_COORDINATE = {8, 16, 7, 9};
	
	public Pawn(final int piecePosition, final Alliance pieceAlliance) {
		super(PieceType.PAWN, piecePosition, pieceAlliance, true);
	}
	public Pawn(final int piecePosition, final Alliance pieceAlliance, final boolean isFirstMove) {
		super(PieceType.PAWN, piecePosition, pieceAlliance, isFirstMove);
	}

	@Override
	public Collection<Move> calculateLegalMoves(final Board board) {
		
		final List<Move> legalMoves = new ArrayList();
		
		for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE) {
			
			final int candidateDestinationCoordinate = this.piecePosition + (this.getPieceAlliance().getDirection() * currentCandidateOffset);
			
			if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
				continue;
			}
			
			if(currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
				//TODO more work here (deal with promotion)
				legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
			} else if (currentCandidateOffset == 16 && this.isFirstMove() && 
					((BoardUtils.SEVENTH_RANK[this.piecePosition] && this.getPieceAlliance().isBlack()) ||
					(BoardUtils.SECOND_RANK[this.piecePosition] && this.getPieceAlliance().isWhite()))){
				final int behindCandidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * 8);
				if(!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() && 
				   !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
					legalMoves.add(new PawnJump(board, this, candidateDestinationCoordinate));
				}
			} else if (currentCandidateOffset == 7 &&
					  !((BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() ||
						(BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())))) {
					if(board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
						final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
						if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
							//TODO more here
							legalMoves.add(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
						}
					}
			} else if (currentCandidateOffset == 9 &&
					  !(BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()) ||
						(BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())) {
					if(board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
						final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
						if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
						//TODO more here
						legalMoves.add(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
						}
					}
			}
		}
		return legalMoves;
	}
	@Override
	public String toString(){
		return PieceType.PAWN.toString();
	}

	@Override
	public Pawn movePiece(Move move) {
		return new Pawn(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
	}
	

}
