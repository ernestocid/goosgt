package tests.endtoend;

import auctionsniper.Main;
import auctionsniper.ui.MainWindow;

import static tests.endtoend.FakeAuctionServer.XMPP_HOSTNAME;

public class ApplicationRunner {

	public static final String SNIPER_ID = "sniper";
	public static final String SNIPER_PASSWORD = "sniper";
	public static final String SNIPER_XMPP_ID = "sniper@localhost/Auction";
	private AuctionSniperDriver driver;

	public void startBiddingIn(final FakeAuctionServer auction) {
		Thread thread = new Thread("Test Application") {
			@Override
			public void run() {
				try {
					Main.main(XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.getItemId());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		thread.setDaemon(true);
		thread.start();
		driver = new AuctionSniperDriver(1000);
		driver.showsSniperStatus(MainWindow.STATUS_JOINING);
	}

	public void showsSniperHasLostAuction() {
		driver.showsSniperStatus(MainWindow.STATUS_LOST);
	}

	public void hasShownSniperIsBidding() {
		driver.showsSniperStatus(MainWindow.STATUS_BIDDING);
	}

	public void stop() {
		if (driver != null) {
			driver.dispose();
		}
	}
}
