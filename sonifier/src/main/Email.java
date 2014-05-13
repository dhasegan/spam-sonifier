package main;

import main.Constants.Category;

public class Email {

	private Category category;
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
				instrument = 56;
				break;
			case LOST_AND_FOUND:
				instrument = 21;
				break;
			case BUYING_AND_SELLING:
				instrument = 0;
				break;
			case COLLEGE_ACTIVITIES:
				instrument = 24;
				break;
			case USG_AND_PARLY:
				instrument = 60;
				break;
			case PARTY:
				instrument = 115;
				break;
			case ACADEMICS:
				instrument = 40;
				break;
			default:
				instrument = 37;
				break;
		}

		return instrument;
	}	
	
	public int getPitch() {
		int pitch;
		switch(category) {
		case OFFICIAL:
			pitch = 70;
			break;
		case LOST_AND_FOUND:
			pitch = 61;
			break;
		case BUYING_AND_SELLING:
			pitch = 52;
			break;
		case COLLEGE_ACTIVITIES:
			pitch = 43;
			break;
		case USG_AND_PARLY:
			pitch = 34;
			break;
		case PARTY:
			pitch = 88;
			break;
		case ACADEMICS:
			pitch = 79;
			break;
		default:
			pitch = 97;
			break;
		}
		return pitch;
	}
	
	public int getVolume() {
		double v = ((127-70) * (1.0 * (length-1) / 7)) + 70;
		return (int) v;
	}
	
	public long getTime() {
		return 2*timestamp;
	}
}
