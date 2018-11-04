package com.risk.model;

/**
 * Model for CardTrade View
 * 
 * @author <a href="mailto:apoorv.semwal20@gmail.com">Apoorv Semwal</a>
 * @version 0.0.1
 */
public class CardTrade {

	/**
	 * Represents Card1 to be traded during ReInforcement.
	 */
	private Card card1;
	
	/**
	 * Represents Card2 to be traded during ReInforcement.
	 */
	private Card card2;
	
	/**
	 * Represents Card3 to be traded during ReInforcement.
	 */
	private Card card3;

	public Card getCard1() {
		return card1;
	}

	public void setCard1(Card card1) {
		this.card1 = card1;
	}

	public Card getCard2() {
		return card2;
	}

	public void setCard2(Card card2) {
		this.card2 = card2;
	}

	public Card getCard3() {
		return card3;
	}

	public void setCard3(Card card3) {
		this.card3 = card3;
	}
		
}
