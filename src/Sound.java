import javax.sound.midi.*;

public class Sound
{
   protected static MidiChannel[] channels=null;		//MIDI channels
   protected static Instrument[] instr;					//MIDI instrument bank
   protected static final int PIANO = 0;              //here is the list of all instrument sounds 
   protected static final int ORGAN = 20;
   protected static final int FRET_NOISE = 120;       //             https://www.midi.org/specifications-old/item/gm-level-1-sound-set

   public static void initialize()
   {
      try 
      {
         Synthesizer synth = MidiSystem.getSynthesizer();
         synth.open();
         Sound.channels = synth.getChannels();
         Sound.instr = synth.getDefaultSoundbank().getInstruments();
      }
      catch (Exception ignored) 
      {}
      channels[0].programChange(instr[PIANO].getPatch().getProgram());
      silence();		
   }

   //turn sounds off 
   public static void silence()
   {
      channels[0].allNotesOff();		
   }

   //sound for hitting the enter key
   public static void click()
   {
      channels[0].programChange(instr[FRET_NOISE].getPatch().getProgram());
      int pitch = 100;                    //pitch is how low or high the note is (1-127)  
      int velocity = 60;                  //velocity is how loud the sound is (0-127)
      channels[0].noteOn(pitch, velocity);
   }
   
   //sound for hitting a key
   public static void randomNote()
   {
      channels[0].programChange(instr[ORGAN].getPatch().getProgram());
      int pitch = 52;          
      int velocity = 100;
      channels[0].noteOn(55, 1);
      channels[0].noteOn(58, 100);
      channels[0].noteOn(pitch, velocity);
   }
   
   public static void test()
   {
      channels[0].programChange(instr[ORGAN].getPatch().getProgram());
      int pitch = 69;          
      int velocity = 100;
      channels[0].noteOn(45, 1);
      channels[0].noteOn(61, 100);
      channels[0].noteOn(44, 100);
      channels[0].noteOn(pitch, velocity); //F Sharp diminished, d sharp minor 7 flat 5, minor 6, diminished chords,
      //E diminished: 52 55, 58
   }
   
}