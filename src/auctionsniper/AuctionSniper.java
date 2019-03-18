package auctionsniper;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.packet.Message;

public class AuctionSniper implements AuctionEventListener {
	private final SniperListener sniperListener;
	private final Auction auction;

	public AuctionSniper(Auction auction, SniperListener sniperListener) {
		this.sniperListener = sniperListener;
		this.auction = auction;
	}

	public void auctionClosed() {
		sniperListener.sniperLost();
	}

	public void currentPrice(int price, int increment) {
		auction.bid(price + increment);
		sniperListener.sniperBidding();
	}

	@Override
	public void processMessage(Chat arg0, Message arg1) {
		// TODO Auto-generated method stub

	}
}
