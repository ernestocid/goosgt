package tests.endtoend;

import auctionsniper.Main;
import auctionsniper.ui.MainWindow;
import auctionsniper.ui.SnipersTableModel;

import java.util.stream.Stream;

import static auctionsniper.SniperState.*;
import static tests.endtoend.FakeAuctionServer.XMPP_HOSTNAME;

public class ApplicationRunner {
	public static final String SNIPER_ID = "sniper";
	public static final String SNIPER_PASSWORD = "sniper";
	public static final String SNIPER_XMPP_ID = "sniper@localhost/Auction";

	private AuctionSniperDriver driver;

	public void startBiddingIn(final FakeAuctionServer... auctions) {
		startSniper();
		Stream.of(auctions).forEach(auction -> {
			String itemId = auction.getItemId();
			driver.startBiddingFor(itemId);
			driver.showsSniperStatus(itemId, 0, 0, SnipersTableModel.textFor(JOINING));
		});
	}

	private void startSniper() {
		Thread thread = new Thread("Test Application") {
			@Override public void run() {
				try {
					Main.main(XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		thread.setDaemon(true);
		thread.start();

		driver = new AuctionSniperDriver(1000);
		driver.hasTitle(MainWindow.APPLICATION_TITLE);
		driver.hasColumnTitles();
	}

	public void showsSniperHasLostAuction(FakeAuctionServer auction) {
		driver.showsSniperStatus(auction.getItemId(), SnipersTableModel.textFor(LOST));
	}

	public void hasShownSniperIsBidding(FakeAuctionServer auction, int lastPrice, int lastBid) {
		driver.showsSniperStatus(auction.getItemId(), lastPrice, lastBid, SnipersTableModel.textFor(BIDDING));
	}

	public void hasShownSniperIsWinning(FakeAuctionServer auction, int winningBid) {
		driver.showsSniperStatus(auction.getItemId(), winningBid, winningBid, SnipersTableModel.textFor(WINNING));
	}

	public void showsSniperHasWonAuction(FakeAuctionServer auction, int lastPrice) {
		driver.showsSniperStatus(auction.getItemId(), lastPrice, lastPrice, SnipersTableModel.textFor(WON));
	}

	public void stop() {
		if (driver != null) {
			driver.dispose();
		}
	}

	private static String[] arguments(FakeAuctionServer... auctions) {
		String[] arguments = new String[auctions.length + 3];
		arguments[0] = XMPP_HOSTNAME;
		arguments[1] = SNIPER_ID;
		arguments[2] = SNIPER_PASSWORD;
		for (int i = 0; i < auctions.length; i++) {
			arguments[i + 3] = auctions[i].getItemId();
		}
		return arguments;
	}
}

