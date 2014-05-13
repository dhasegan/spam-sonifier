package main;

import java.io.File;
import java.util.List;

import javax.sound.midi.*;

public class EmailPlayer {
	
	private Sequence sequence = null;

	public void addMessage(Track t, int status, int pitch, int volume, long tick) {
		ShortMessage mm = new ShortMessage();
		try {
			mm.setMessage(status, pitch,volume);
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
			addMessage(t,0xB0, 0x7F,0x00,(long)0);
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
		addMessage(t, 0xC0, email.getInstrument(), 0, (long) email.getTime());
		// note on - middle C
		addMessage(t, 0x90, email.getPitch(), email.getVolume(), (long) email.getTime());
		// note off - middle C - interval ticks later  ****
		addMessage(t, 0x80, email.getPitch(),email.getVolume(), (long) email.getTime() + interval);
	}
	
	public void sonifyEmails(List<Email> emails) {
		Track t = setMetaData();
		for (Email email : emails) {
//			System.out.println("instrument : " + email.getInstrument());
//			System.out.println("volume : " + email.getVolume());
//			System.out.println("time : " + email.getTime());
			addEmail(t, email, 2);
		}
		finalize(this.sequence, t, emails.size());
	}
	
	public static void main(String args[]) {
		List<Email> emails = Reader.readEmails("/home/caroline/spam-sonifier/times.csv");
		System.out.println("sonifying " + emails.size() + " emails");
		EmailPlayer player = new EmailPlayer();
		player.sonifyEmails(emails);
	}
}
