
public class MessageTime {
	
    private static final MessageTime instance = new MessageTime();
    
    private long pToC = 0, cToP = 0, fullCircle = 0, totalTime = 0;
	
	private MessageTime() {}
	
	public static MessageTime getInstance(){
        return instance;
    }
	
	public void calculateTime(long timeStampP1, long timeStampC, long timeStampP2) {
		pToC = pToC + Math.abs(timeStampC - timeStampP1);
		cToP = cToP + Math.abs(timeStampP2 - timeStampC);
		fullCircle = fullCircle + Math.abs(timeStampP2 - timeStampP1);
		totalTime = totalTime + fullCircle;
	}
	
	public synchronized void resetTime() {
		pToC = 0;
		cToP = 0;
		fullCircle = 0;
	}
	
	public long getPToC() {
		return pToC;
	}
	
	public long getCToP() {
		return cToP;
	}
	
	public long getFullCircle() {
		return fullCircle;
	}
	
	public long getTotalTime() {
		return totalTime;
	}

}
