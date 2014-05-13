package main;

import java.io.File;
import java.util.List;

import javax.sound.midi.*;

public class EmailPlayer {
	
	private Sequence sequence = null;

	public void addMessage(Track t, int status, int channel, int pitch, int volume, long tick) {
		ShortMessage mm = new ShortMessage();
		try {
			mm.setMessage(status, channel, pitch,volume);
			MidiEvent me = new MidiEvent(mm,tick);
			t.add(me);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public Track setMetaData() {
		//Obtain a MIDI track from the sequence  ****
		Track t = null;
		try {
			Sequence s = new Sequence(javax.sound.midi.Sequence.PPQ,24);
			this.sequence = s;
			t = s.createTrack();
			//General MIDI sysex -- turn on General MIDI sound set  ****
			byte[] b = {(byte)0xF0, 0x7E, 0x7F, 0x09, 0x01, (byte)0xF7};
			SysexMessage sm = new SysexMessage();
			sm.setMessage(b, 6);
			MidiEvent me = new MidiEvent(sm,(long)0);
			t.add(me);
	
			//set tempo (meta event)
			MetaMessage mt = new MetaMessage();
	        byte[] bt = {0x01, (byte)0x00, 0x00};
			mt.setMessage(0x51 ,bt, 3);
			me = new MidiEvent(mt,(long)0);
			t.add(me);
	
			//set track name (meta event)
			mt = new MetaMessage();
			String TrackName = new String("email_sonification");
			mt.setMessage(0x03 ,TrackName.getBytes(), TrackName.length());
			me = new MidiEvent(mt,(long)0);
			t.add(me);
	
			//set omni on
			ShortMessage mm = new ShortMessage();
			mm.setMessage(0xB0, 0x7D,0x00);
			me = new MidiEvent(mm,(long)0);
			t.add(me);
	
			//set poly on
			addMessage(t,0xB0, 1, 0x7F,0x00,(long)0);
			addMessage(t,0xB0, 2, 0x7F,0x00,(long)0);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return t;
	}
	
	public void finalize(Sequence s, Track t, long timestamp) {
		File f = new File("midifile.mid");
		try {
			//****  set end of track (meta event) 19 ticks later  ****
			MetaMessage mt = new MetaMessage();
	        byte[] bet = {}; // empty array
			mt.setMessage(0x4F,bet,0);
			MidiEvent me = new MidiEvent(mt, timestamp);
			t.add(me);
	
			//write the MIDI sequence to a MIDI file  ****
			MidiSystem.write(s,1,f);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void addEmail(Track t, Email email, long interval) {
		// add the instrument
		addMessage(t, 0xC0, 1, email.getInstrument(), 0, (long) email.getTime());
		// note on - middle C
		addMessage(t, 0x90, 1, email.getPitch(), email.getVolume(), (long) email.getTime());
		// note off - middle C - interval ticks later  ****
		addMessage(t, 0x80, 1, email.getPitch(),email.getVolume(), (long) email.getTime() + interval);
	}
	
	public void sonifyEmails(Track t, List<Email> emails) {
		for (Email email : emails) {
//			System.out.println("instrument : " + email.getInstrument());
//			System.out.println("volume : " + email.getVolume());
//			System.out.println("time : " + email.getTime());
			if (email.getTime() >= Email.start_time) {
				addEmail(t, email, Email.delay);
			}
		}
//		System.out.println("maxTime " + (maxTime * 8 + 100));
	}

	public void addDay(Track t, long start, long interval, int pitch, int instrument, int volume) {
		// add the instrument
		addMessage(t, 0xC0, 2, instrument, 0, (long) start);
		// note on - middle C
		addMessage(t, 0x90, 2, pitch, volume, (long) start);
		// note off - middle C - interval ticks later  ****
		addMessage(t, 0x80, 2, pitch, volume, (long) start + interval);
	}
	
	public void sonifyBackground(Track t, long maxTime) {
		int j = 0;
		long day_time = 144 * Email.delay;
		
		int pitch = 48;
		int instrument = 2;
		int volume = 50;
		
		while( j < maxTime ) {
			for (int i = 0; i < 7; ++i) {
				int semitones = 0;
				if (i > 2) {
					semitones ++;
				} 
				if (i > 5) { 
					semitones ++;
				}
				for (int k=0;k < 3; ++k) {
					if (j + k * day_time / 3 >= Email.start_time) {
						this.addDay(t, j + k * day_time / 3 , day_time / 3 - 1, pitch + i * 2 - semitones, instrument, volume );
					}
				}
				j += day_time;
			}
		}
	}
	
	public void sonifyEverything(List<Email> emails) {
		Track t = setMetaData();

		long maxTime = 0;
		for (Email email : emails) {
			long emailtime = email.getTime();
			if (maxTime < emailtime) {
				maxTime = emailtime;
			}
		}
		
		this.sonifyEmails(t, emails);
		this.sonifyBackground(t, maxTime );
		
		finalize(this.sequence, t, maxTime + 1000 );
	}
	
	public static void main(String args[]) {
		List<Email> emails = Reader.readEmails("/home/daniel/sem6/letthedataspeak/dataset/times.csv");
		System.out.println("sonifying " + emails.size() + " emails");
		EmailPlayer player = new EmailPlayer();
		
		player.sonifyEverything(emails);
	}
}
