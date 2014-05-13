package main;

import main.Constants.Category;

public class Email {
	
	static public int delay = 16;

	public Category category;
	private int length;
	private long timestamp;
	private int bin;
	
	public Email(Category category, int length, long timestamp, int bin) {
		this.category = category;
		this.length = length;
		this.timestamp = timestamp;
		this.bin = bin;
	}


	public int getInstrument() {
		int instrument;
		switch(category) {
			case OFFICIAL:
				// Trumpet
				instrument = 57; 
				break;
			case LOST_AND_FOUND:
				// French Horn
				instrument = 61;
				break;
			case BUYING_AND_SELLING:
				// Steel Drums
				instrument = 115;
				break;
			case COLLEGE_ACTIVITIES:
				// Acoustic Guitar
				instrument = 25;
				break;
			case USG_AND_PARLY:
				// Trombone
				instrument = 58;
				break;
			case PARTY:
				// Piano
				instrument = 1;
				break;
			case ACADEMICS:
				// Bass
				instrument = 35;
				break;
			default:
				// Viola
				instrument = 42;
				break;
		}

		return instrument;
//		return 61;
	}	
	
	public int getPitch() {
		int pitch;
		switch(category) {
		case OFFICIAL:
			pitch = 72;
			break;
		case LOST_AND_FOUND:
			pitch = 60;
			break;
		case BUYING_AND_SELLING:
			pitch = 48;
			break;
		case COLLEGE_ACTIVITIES:
			pitch = 65;
			break;
		case USG_AND_PARLY:
			pitch = 64;
			break;
		case PARTY:
			pitch = 62;
			break;
		case ACADEMICS:
			pitch = 48;
			break;
		default:
			pitch = 69;
			break;
		}
		return pitch;
//		return 62;
	}
	
	public int getVolume() {
		double v = ((127-90) * (1.0 * (length-1) / 7)) + 90;
		return (int) v;
	}
	
	public long getTime() {
		return Email.delay * timestamp;
	}
}
