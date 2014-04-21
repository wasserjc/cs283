package cs283.baseball;

/**
 * BaseballGame
 * This class will serve as data model for information
 * we need to store about a single baseball game.
 * @author Jared Wasserman
 *
 */
public class BaseballGame {
	
	public BaseballGame() {
		homeTeamName = "Home Team";
		awayTeamName = "Away Team";
		inningNumber = 1;
		halfInning = TOP;
		homeTeamRuns = 0;
		awayTeamRuns = 0;
		homeTeamHits = 0;
		awayTeamHits = 0;
		homeTeamErrors = 0;
		awayTeamErrors = 0;
		outs = strikes = balls = 0;
		firstBase = secondBase = thirdBase = false;
	}
	
	private final int TOP = 0;
	private final int BOTTOM = 1;
	
	
	private String homeTeamName;
	private String awayTeamName;
	// need something for which half of inning (top vs. bottom)
	private int halfInning;
	private int inningNumber;
	private int homeTeamRuns;
	private int awayTeamRuns;
	private int homeTeamHits;
	private int awayTeamHits;
	private int homeTeamErrors;
	private int awayTeamErrors;
	private int outs;
	private int strikes;
	private int balls;
	// need booleans for each base (true --> man on that base)
	private boolean firstBase;
	private boolean secondBase;
	private boolean thirdBase;
	
	public String getHomeTeamName() {
		return this.homeTeamName;
	}
	
	public String getAwayTeamName() {
		return this.awayTeamName;
	}
	
	public int getInningNumber() {
		return this.inningNumber;
	}
	
	public int getHalfInning() {
		return this.halfInning;
	}
	
	public int getHomeTeamRuns() {
		return this.homeTeamRuns;
	}
	
	public int getAwayTeamRuns() {
		return this.awayTeamRuns;
	}
	
	public int getHomeTeamHits() {
		return this.homeTeamHits;
	}
	
	public int getAwayTeamHits() {
		return this.awayTeamHits;
	}
	
	public int getHomeTeamErrors() {
		return this.homeTeamErrors;
	}
	
	public int getAwayTeamErrors() {
		return this.awayTeamErrors;
	}
	
	public int getOuts() {
		return this.outs;
	}
	
	public int getStrikes() {
		return this.strikes;
	}
	
	public int getBalls() {
		return this.balls;
	}
	
	public boolean getFirst() {
		return this.firstBase;
	}
	
	public boolean getSecond() {
		return this.secondBase;
	}
	
	public boolean getThird() {
		return this.thirdBase;
	}
	
	// **************************************************************
	// **************************************************************
	
	// SETTERS (must be synchronized)
	
	public synchronized void resetGame() {
		homeTeamName = "Home Team";
		awayTeamName = "Away Team";
		inningNumber = 1;
		halfInning = TOP;
		homeTeamRuns = 0;
		awayTeamRuns = 0;
		homeTeamHits = 0;
		awayTeamHits = 0;
		homeTeamErrors = 0;
		awayTeamErrors = 0;
		outs = strikes = balls = 0;
		firstBase = secondBase = thirdBase = false;
	}
	
	public synchronized void resetForNewBatter() {
		strikes = balls = 0;
	}
	
	public synchronized void resetForNewInning() {
		outs = strikes = balls = 0;
		firstBase = secondBase = thirdBase = false;
	}
	
	public synchronized void setHomeTeamName(String name) {
		this.homeTeamName = name;
	}
	
	public synchronized void setAwayTeamName(String name) {
		this.awayTeamName = name;
	}
	
	public synchronized void setInningNumber(int inning) {
		this.inningNumber = inning;
	}
	
	public synchronized void setHalfInning(int halfInning) {
		this.halfInning = halfInning;
	}
	
	public synchronized void incrementHalfInning() {
		if (this.halfInning == TOP) halfInning = BOTTOM;
		else {
			halfInning = TOP;
			this.inningNumber++;
		}
		
		outs = strikes = balls = 0;
		firstBase = secondBase = thirdBase = false;
	}
	
	public synchronized void setHomeTeamRuns(int runs) {
		this.homeTeamRuns = runs;
	}
	
	public synchronized void setAwayTeamRuns(int runs) {
		this.awayTeamRuns = runs;
	}
	
	public synchronized void setHomeTeamHits(int hits) {
		this.homeTeamHits = hits;
	}
	
	public synchronized void setAwayTeamHits(int hits) {
		this.awayTeamHits = hits;
	}
	
	public synchronized void setHomeTeamErrors(int errors) {
		this.homeTeamErrors = errors;
	}
	
	public synchronized void setAwayTeamErrors(int errors) {
		this.awayTeamErrors = errors;
	}
	
	public synchronized void incrementOuts() {
		this.outs++;
	}
	
	public synchronized void incrementStrikes() {
		this.strikes++;
	}
	
	public synchronized void incrementBalls() {
		this.balls++;
	}
	
	public synchronized void toggleFirstBase() {
		firstBase = (!firstBase);
	}
	
	public synchronized void toggleSecondBase() {
		secondBase = (!secondBase);
	}
	
	public synchronized void toggleThirdBase() {
		thirdBase = (!thirdBase);
	}
	

	public synchronized void setOuts(int outs) {
		this.outs = outs;
	}
	
	public synchronized void setStrikes(int strikes) {
		this.strikes = strikes;
	}
	
	public synchronized void setBalls(int balls) {
		this.balls = balls;
	}
	
	public synchronized void setFirstBase(boolean onBase) {
		firstBase = onBase;
	}
	
	public synchronized void setSecondBase(boolean onBase) {
		secondBase = onBase;
	}
	
	public synchronized void setThirdBase(boolean onBase) {
		thirdBase = onBase;
	}
}
