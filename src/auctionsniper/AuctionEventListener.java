package auctionsniper;

import org.jivesoftware.smack.MessageListener;

public interface AuctionEventListener extends MessageListener {
	void auctionClosed();
	void currentPrice(int price, int increment);
}
