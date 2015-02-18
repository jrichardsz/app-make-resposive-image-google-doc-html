package org.jrichardsz.apps.googledocresponsive.controler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.jrichardsz.apps.googledocresponsive.view.MainView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.linet.api.swing.filechooser.FileChooserUtil;
import com.linet.util.file.FileUtil;

public class MakeResposiveImageController implements ActionListener{

	private MainView window;
	private JTextField textFieldPathHtml;
	private JButton openButton;
	private JButton executeButton;
	private String pathHtml=null;	
	
	public MakeResposiveImageController(MainView window) {
		this.window = window;
		textFieldPathHtml=this.window.getTextFieldPathHtml();
		openButton=this.window.getOpenButton();
		executeButton=this.window.getExecuteButton();
		// add listener
		openButton.addActionListener(this);
		executeButton.addActionListener(this);		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource().equals(openButton)){
			// get valid path
			getPathForHtmlFile();

		} else if(e.getSource().equals(executeButton)){

			if(pathHtml == null){
				JOptionPane.showMessageDialog(null,"Select a valid path.");
				return;
			}
			
			File input = new File(pathHtml);
			Document doc = null;
			try {
				doc = Jsoup.parse(input, "UTF-8", "http://example.com/");
			} catch (IOException e2) {
				e2.printStackTrace();
				return;
			}
			
			Elements img = doc.getElementsByTag("img");

			String fileName = FileUtil.getNameFromPathFile(pathHtml);
			
			 for (Element imgElement : img){
				 
				 imgElement.attr("style", "max-width:100% !important; height:auto; display:block;");
				 
				 //save html sstring
				 String htmlOfImage = imgElement.toString();
				
				 Element span = imgElement.parent();
				
				 //get parent of span
				 Element parestOfSpan = span.parent();
				 
				 span.remove();	
				 
				 parestOfSpan.append(htmlOfImage);
				 
			 }

			String pathFinal=null;
			try{
				pathFinal=FileChooserUtil.getFilePathToSave("save resposive image html",fileName+"-ready","html");
			}
			catch(Exception e1){
				e1.printStackTrace();
				return;
			}

			
			BufferedWriter htmlWriter = null;
			
			try{
				htmlWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pathFinal), "UTF-8"));
				htmlWriter.write(doc.toString());
			}
			catch(Exception e1){
				e1.printStackTrace();
				return;
			}finally{
				if(htmlWriter!=null){
					try {
						htmlWriter.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
			
		}
		
	}
	
	public void getPathForHtmlFile(){
		try{

			if(textFieldPathHtml.getText() != null && !textFieldPathHtml.getText().equals("")){
				pathHtml=FileChooserUtil.getFilePathToOpen("Select path of html to parse",textFieldPathHtml.getText(),null);
			}
			else{
				pathHtml=FileChooserUtil.getFilePathToOpen("Select path of html to parse");
			}
			textFieldPathHtml.setText(pathHtml);
		}
		catch(Exception exception){
			JOptionPane.showMessageDialog(null,exception.toString());
		}
	}	

}
