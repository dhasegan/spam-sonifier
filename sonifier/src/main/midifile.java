package main;

/**
 * midifile.java
 *
 * A very short program which builds and writes
 * a one-note Midi file.
 *
 * author  Karl Brown
 * last updated 2/24/2003
 */

import java.io.*;
import java.util.*;
import javax.sound.midi.*; // package for all midi classes
public class midifile
{
	static void addMessage(Track t, int status, int pitch, int volume, long tick) {
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
  public static void main(String argv[]) {
    System.out.println("midifile begin ");
	File f = new File("midifile.mid");
	    try {
	//****  Create a new MIDI sequence with 24 ticks per beat  ****
			Sequence s = new Sequence(javax.sound.midi.Sequence.PPQ,24);
	
	//****  Obtain a MIDI track from the sequence  ****
			Track t = s.createTrack();
	
	//****  General MIDI sysex -- turn on General MIDI sound set  ****
			byte[] b = {(byte)0xF0, 0x7E, 0x7F, 0x09, 0x01, (byte)0xF7};
			SysexMessage sm = new SysexMessage();
			sm.setMessage(b, 6);
			MidiEvent me = new MidiEvent(sm,(long)0);
			t.add(me);
	
	//****  set tempo (meta event)  ****
			MetaMessage mt = new MetaMessage();
	        byte[] bt = {0x02, (byte)0x00, 0x00};
			mt.setMessage(0x51 ,bt, 3);
			me = new MidiEvent(mt,(long)0);
			t.add(me);
	
	//****  set track name (meta event)  ****
			mt = new MetaMessage();
			String TrackName = new String("midifile track");
			mt.setMessage(0x03 ,TrackName.getBytes(), TrackName.length());
			me = new MidiEvent(mt,(long)0);
			t.add(me);
	
	//****  set omni on  ****
			ShortMessage mm = new ShortMessage();
			mm.setMessage(0xB0, 0x7D,0x00);
			me = new MidiEvent(mm,(long)0);
			t.add(me);
	
	//****  set poly on  ****
			addMessage(t,0xB0, 0x7F,0x00,(long)0);
	
			
//	//****  set instrument to Piano  ****
////			addMessage(t, 0xC0, 127, 0x00, (long) 50);
//	//****  note on - middle C  ****
//			addMessage(t, 0x90,127,0x60, (long)1);
//	//****  note off - middle C - 120 ticks later  ****
//			addMessage(t, 0x80,127,0x40, (long) 121);
//
//			
//			//****  set instrument to Piano  ****
////			addMessage(t, 0xE0, 0x00, 0x00, (long) 50);
//			//****  note on - middle C  ****
//			addMessage(t,0x90,100,0x60, (long) 122);
//	//****  note off - middle C - 120 ticks later  ****
//			addMessage(t,0x80,100,0x40, (long) 221);
//
//			//****  set instrument to Piano  ****
////			addMessage(t, 0xE0, 0x00, 0x00, (long) 50);
//			//****  note on - middle C  ****
//			addMessage(t,0x90,127,0x60, (long) 222);
//	//****  note off - middle C - 120 ticks later  ****
//			addMessage(t, 0x80,127,0x40, (long) 321);
//
			int offset = 0, i = 0;
			// add piano
			addMessage(t, 0xC0, 0x00, 0x00, (long) 0);
			for (i = 0; i <= 127; i++) {
//			addMessage(t, 0xE0, 0x00, 0x00, (long) 50);
			//****  note on - middle C  ****
				int val = i*70 + offset;
				//addMessage(t,0x90,i,0x60, (long) val);
	//****  note off - middle C - 120 ticks later  ****
				//addMessage(t, 0x80,i,0x40, (long) val+70);
				//****  set instrument to Piano  ****
				// add piano
				if (true) {
					addMessage(t, 0xC0, 60, 0, (long) val);
				}
				
				addMessage(t,0x90,70,127, (long) val);
	//****  note off - middle C - 120 ticks later  ****
				addMessage(t, 0x80,70,127, (long) val+70);

				offset += 1;
			}
			//****  set end of track (meta event) 19 ticks later  ****
			mt = new MetaMessage();
	        byte[] bet = {}; // empty array
			mt.setMessage(0x4F,bet,0);
			me = new MidiEvent(mt, (long)i*70);
			t.add(me);
	
	//****  write the MIDI sequence to a MIDI file  ****
			MidiSystem.write(s,1,f);
		} //try
			catch(Exception e)
		{
			System.out.println("Exception caught " + e.toString());
		} //catch
	 
	System.out.println("midifile end ");
  } //main
} //midifile