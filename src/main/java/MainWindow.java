
import com.darkprograms.speech.microphone.Microphone;
import com.darkprograms.speech.microphone.MicrophoneAnalyzer;
import com.darkprograms.speech.recognizer.GSpeechDuplex;
import com.darkprograms.speech.recognizer.GSpeechResponseListener;
import com.darkprograms.speech.recognizer.GoogleResponse;
import com.darkprograms.speech.recognizer.Recognizer;
import com.darkprograms.speech.recognizer.Languages;
import com.darkprograms.speech.synthesiser.Synthesiser;
import com.darkprograms.speech.synthesiser.SynthesiserV2;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import net.sourceforge.javaflacencoder.FLACFileWriter;
import java.io.*;
import com.sun.speech.freetts.*;
import sun.audio.*;
import javazoom.jl.player.Player;
import com.darkprograms.speech.*; 
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import com.sun.syndication.feed.*;
import com.sun.syndication.io.*;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author urcn1_000
 */
public class MainWindow extends javax.swing.JDialog {

Boolean listen = false;

public  void ambientListeningLoop() {
    Chat c = new Chat();
	MicrophoneAnalyzer mic = new MicrophoneAnalyzer(FLACFileWriter.FLAC);
	mic.setAudioFile(new File("AudioTestNow.flac"));
	while(true){
		mic.open();
		final int THRESHOLD =50;
		int volume = mic.getAudioVolume();
                
                //System.out.println(volume);
		boolean isSpeaking = (volume > THRESHOLD);
		if(isSpeaking){
                    voice("Привет");
			try {
				System.out.println("RECORDING...");
                                
				mic.captureAudioToFile(mic.getAudioFile());//Saves audio to file.
                                Thread.sleep(200);
				do{
					Thread.sleep(600);//Updates every second
				}
				while(mic.getAudioVolume() > THRESHOLD);
				System.out.println("Recording Complete!");
				System.out.println("Recognizing...");
				Recognizer rec = new Recognizer("ru-RU","AIzaSyAVtlE8ubqMGn5ZCn5ZTuJlFN0VXkW_NeU");
				GoogleResponse response = rec.getRecognizedDataForFlac(mic.getAudioFile(), 3);
				displayResponse(response);//Displays output in Console
                                if (response.getResponse()!=null){
                                    voice(c.sayInReturn(response.getResponse()));
                                }
                                
				System.out.println("Looping back");//Restarts loops
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Error Occured");
			}
			finally{
				mic.close();//Makes sure microphone closes on exit.
			}
		}
	}
}

private  static void displayResponse(GoogleResponse gr){
	if(gr.getResponse() == null){
		System.out.println((String)null);
		return;
	}
	System.out.println("Google Response: " + gr.getResponse());
	System.out.println("Google is " + Double.parseDouble(gr.getConfidence())*100 + "% confident in"
			+ " the reply");
	System.out.println("Other Possible responses are: ");
	for(String s: gr.getOtherPossibleResponses()){
		System.out.println("\t" + s);
	}
}
    public MainWindow(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        //ambientListeningLoop();
        final Microphone mic = new Microphone(FLACFileWriter.FLAC);
		//Don't use the below google api key , make your own !!! :) 
		GSpeechDuplex duplex = new GSpeechDuplex("AIzaSyAVtlE8ubqMGn5ZCn5ZTuJlFN0VXkW_NeU");
		duplex.setLanguage("ru");
                
                
		new Thread(() -> {
					try {
						duplex.recognize(mic.getTargetDataLine(), mic.getAudioFormat());
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					
				}).start();
                

		duplex.addResponseListener(new GSpeechResponseListener() {
			String old_text = "";
			
			public void onResponse(GoogleResponse gr) {
				String output = "";
				output = gr.getResponse();
//				if (gr.getResponse() == null) {
//					this.old_text = response.getText();
//					if (this.old_text.contains("(")) {
//						this.old_text = this.old_text.substring(0, this.old_text.indexOf('('));
//					}
//					System.out.println("Paragraph Line Added");
//					this.old_text = ( response.getText() + "\n" );
//					this.old_text = this.old_text.replace(")", "").replace("( ", "");
//					response.setText(this.old_text);
//                                        Chat chat = new Chat();
//                                        String s = chat.respond(response.getText());
//                                        voice(s);
//					return;
//				}
//				if (output.contains("(")) {
//					output = output.substring(0, output.indexOf('('));
//				}
//				if (!gr.getOtherPossibleResponses().isEmpty()) {
//					output = output + " (" + (String) gr.getOtherPossibleResponses().get(0) + ")";
//                                        
//				}
                                //System.out.println(gr.isFinalResponse());
                                if(listen == true){
                                System.out.println(output);
				response.setText("");
				response.append(this.old_text);
				response.append(output);
                                if (gr.isFinalResponse()){
                                    Chat c = new Chat();
                                    try {
                                        voice (c.sayInReturn(response.getText()));
                                    } catch (IllegalArgumentException ex) {
                                        Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                                    } catch (FeedException ex) {
                                        Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                                    } catch (IOException ex) {
                                        Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                }
				

                                if(output.contains("Привет") & listen == false)
                                {
                                    System.out.println(output);
                                    //response.setText("Здарова епт");
                                   // voice(response.getText());
                                    listen = true;
                                }
                                
                                if(output.toLowerCase().contains("пока") & listen == true)
                                {
                                    System.out.println(output);
                                   // voice("Пока пидор");
                                    listen = false;
                                }
			}
                        

            
		});
    }
            public void onResponse(GoogleResponse gr) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        response = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        response.setColumns(20);
        response.setRows(5);
        jScrollPane1.setViewportView(response);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 509, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    
    
    private Player player;    
    private void voice(String out)
    {
        Synthesiser  synth = new Synthesiser ("ru");
	try {
		InputStream is = synth.getMP3Data(out);
                BufferedInputStream bis = new BufferedInputStream(is);
                player = new Player(bis);
                player.play();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		System.out.println("Error");
		e.printStackTrace();
		return;
	}
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MainWindow dialog = new MainWindow(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
                
                        
            }
        });
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea response;
    // End of variables declaration//GEN-END:variables
}
