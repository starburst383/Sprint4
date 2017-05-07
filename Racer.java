public class Racer {
	public int racerNum;
	public long start;
	public long fin;
	public int index;
	public long time;
	public String name;

	
	public Racer (int rNum, int channel) {
		
		this.racerNum = rNum;
		this.name = "";
		this.start = 0;
		this.fin = 0;
		this.index = channel;
		this.time = 0;
	}
	
	public Racer (int rNum, String alias){
		this.racerNum = rNum;
		this.name = alias;
	}
	
	public void printFileRacers(){
		System.out.println(this.racerNum + " " + this.name);
	}

}
