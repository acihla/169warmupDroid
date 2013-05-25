 package com.example.myfirstapp;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.poifs.property.Parent;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Build;
import android.util.Log;
import android.view.View.OnTouchListener;
import android.view.View;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RemoteViews.RemoteView;
import android.widget.TextView;

public class MainActivity extends Activity {

	public int previousXIndex;
	public int previousYIndex;
	public String prevButton;
	public int prevXTouch;
	public int prevYTouch;
	public int prevTouchTime; 
	public String currentSequence = "";
	public Map<String, String[]> keygonSequences; 
	public String textOutput;
	public String lastWord;
	public TextView[] currentPossibilties;
	private MyPoint newMyPoint = null;
	private List<MyPoint> pointsQ = new ArrayList<MyPoint>();
	public boolean gliding = false;
	

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        textOutput = "";
        
     // ** Declare your Bitmap somewhere **      
        final Bitmap nb = BitmapFactory.decodeResource(getResources(), R.drawable.nb);
        final Bitmap sb = BitmapFactory.decodeResource(getResources(), R.drawable.sb);
        final Bitmap eb = BitmapFactory.decodeResource(getResources(), R.drawable.eb);
        final Bitmap wb = BitmapFactory.decodeResource(getResources(), R.drawable.wb);
        final Bitmap ng = BitmapFactory.decodeResource(getResources(), R.drawable.ng);
        final Bitmap neg = BitmapFactory.decodeResource(getResources(), R.drawable.neg);
        final Bitmap eg = BitmapFactory.decodeResource(getResources(), R.drawable.eg);
        final Bitmap seg = BitmapFactory.decodeResource(getResources(), R.drawable.seg);
        final Bitmap sg = BitmapFactory.decodeResource(getResources(), R.drawable.sg);
        final Bitmap swg = BitmapFactory.decodeResource(getResources(), R.drawable.swg);
        final Bitmap wg = BitmapFactory.decodeResource(getResources(), R.drawable.wg);
        final Bitmap nwg = BitmapFactory.decodeResource(getResources(), R.drawable.nwg);
        final Bitmap center = BitmapFactory.decodeResource(getResources(), R.drawable.center);
        
        

        
   //     findViewById(R.id.imageView1).setMinimumHeight(2*((int)findViewById(R.id.imageView1).getHeight()));
     //   findViewById(R.id.imageView1).setMinimumWidth(2*((int)findViewById(R.id.imageView1).getWidth()));
        
        InputStream myInputStream = getResources().openRawResource(R.raw.keygonsequenceoutput);
        try {
			HSSFWorkbook workbook = new HSSFWorkbook(myInputStream);
			HSSFSheet worksheet = workbook.getSheet("keygonSequenceOutput");
			//Log.d("Our sequence is", currentSequence.toString());
			keygonSequences = new HashMap<String, String[]>();
			for(int i = 0; i < worksheet.getLastRowNum(); i++) {
				HSSFRow currentRow = worksheet.getRow(i);
			//	Log.d("ROW NAME",  currentRow.getCell(0).getStringCellValue());
 				//Log.d("ROW SEQ", "" + currentRow.getCell(1).getStringCellValue());
				
				String currentWordSeq = "" + currentRow.getCell(1).getStringCellValue();
				currentWordSeq = currentWordSeq.substring(1);
				String currentWord = currentRow.getCell(0).getStringCellValue();
				String currentWordFreq = "" + currentRow.getCell(2).getStringCellValue();
				currentWordFreq = currentWordFreq.substring(1);
				if(keygonSequences.containsKey(currentWordSeq)) {
					String[] currentArray = keygonSequences.get(currentWordSeq);
					
					for(int j = 0; j < currentArray.length ; j++) {
						
						if((currentArray[j] == null)) {
							currentArray[j] = currentWord;
							j = currentArray.length;
							break;
						}
						if(currentArray[j].equals(currentWord)) {
							j = currentArray.length;
							break;
						}
						
					}
					/*int j = 0;
					while(!(currentArray[j] == null) && j < 4) {
						if(currentArray[j].equals(currentWord)) {
							
						}
						j++;
					}
					
					currentArray[j] = currentWord; 
					/* if (currentWordSeq.equals("01")){
						Log.d("sequencer", currentWord);
					}
					
					for(int j = 0; j < currentArray.length ; j++) {
						if((currentArray[j] == null)) {
							currentArray[j] = currentWord;
							j = currentArray.length;
						}
					} */
				
					keygonSequences.put(currentWordSeq,currentArray);
				}
				else {
					String[] newCurrentArray = new String[5];
					newCurrentArray[0] = currentWord;
					keygonSequences.put(currentWordSeq, newCurrentArray);
				}
				
				
			}
			}
		  catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    	final String fileToRead = "//assets/keygonSequencerOutput.xls";
        final ImageView compass = (ImageView) findViewById(R.id.imageView1); 
        final TextView textOutputView = (TextView) findViewById(R.id.textView2);
        final Button enterButton = (Button) findViewById(R.id.button1);
        final TextView word1 = (TextView) findViewById(R.id.textView3);
        final TextView word2 = (TextView) findViewById(R.id.textView4);
        final TextView word3 = (TextView) findViewById(R.id.textView5);
        final TextView word4 = (TextView) findViewById(R.id.textView6);
        final TextView word5 = (TextView) findViewById(R.id.textView7);
        final TextView word6 = (TextView) findViewById(R.id.textView8);
        final TextView word7 = (TextView) findViewById(R.id.textView9);

        word1.setOnClickListener(new View.OnClickListener() {
        	
        	@Override
        	public void onClick(View v) {
        		textOutputView.append(word1.getText() + " ");
        		textOutput += " " + word1.getText(); 
        		lastWord = (String) word1.getText();
        		currentSequence = "";
        		word1.setText("");
        		word2.setText("");
        		word3.setText("");
        		word4.setText("");
        		word5.setText("");
        		word6.setText("");
        		word7.setText("");
        	}
        });

        word2.setOnClickListener(new View.OnClickListener() {
        			
        	@Override
        	public void onClick(View v) {
        		textOutputView.append(word2.getText() + " ");
        		textOutput += " " + word2.getText(); 
        		lastWord = (String) word2.getText();
        		currentSequence = "";
        		word1.setText("");
        		word2.setText("");
        		word3.setText("");
        		word4.setText("");
        		word5.setText("");
        		word6.setText("");
        		word7.setText("");
        	}
        });

        word3.setOnClickListener(new View.OnClickListener() {
        	
        	@Override
        	public void onClick(View v) {
        		textOutputView.append(word3.getText() + " ");
        		textOutput += " " + word3.getText(); 
        		lastWord = (String)word3.getText();
        		currentSequence = "";
        		word1.setText("");
        		word2.setText("");
        		word3.setText("");
        		word4.setText("");
        		word5.setText("");
        		word6.setText("");
        		word7.setText("");
        	}
        });
        
        word4.setOnClickListener(new View.OnClickListener() {
        	
        	@Override
        	public void onClick(View v) {
        		textOutputView.append(word4.getText() + " ");
        		textOutput += " " + word4.getText(); 
        		lastWord = (String)word4.getText();
        		currentSequence = "";
        		word1.setText("");
        		word2.setText("");
        		word3.setText("");
        		word4.setText("");
        		word5.setText("");
        		word6.setText("");
        		word7.setText("");
        	}
        });
        
        word5.setOnClickListener(new View.OnClickListener() {
        	
        	@Override
        	public void onClick(View v) {
        		textOutputView.append(word5.getText() + " ");
        		textOutput += " " + word5.getText(); 
        		lastWord = (String)word5.getText();
        		currentSequence = "";
        		word1.setText("");
        		word2.setText("");
        		word3.setText("");
        		word4.setText("");
        		word5.setText("");
        		word6.setText("");
        		word7.setText("");
        	}
        });
        
        
        word6.setOnClickListener(new View.OnClickListener() {
        	
        	@Override
        	public void onClick(View v) {
        		textOutputView.append(word6.getText() + " ");
        		textOutput += " " + word6.getText(); 
        		lastWord = (String)word6.getText();
        		currentSequence = "";
        		word1.setText("");
        		word2.setText("");
        		word3.setText("");
        		word4.setText("");
        		word5.setText("");
        		word6.setText("");
        		word7.setText("");
        	}
        });
        
        word7.setOnClickListener(new View.OnClickListener() {
        	
        	@Override
        	public void onClick(View v) {
        		textOutputView.append(word7.getText() + " ");
        		textOutput += " " + word7.getText(); 
        		lastWord = (String)word7.getText();
        		currentSequence = "";
        		word1.setText("");
        		word2.setText("");
        		word3.setText("");
        		word4.setText("");
        		word5.setText("");
        		word6.setText("");
        		word7.setText("");
        	}
        });
        
        enterButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
					 //new FileInputStream(fileToRead);
					/*BufferedReader myReader = new BufferedReader(new InputStreamReader(myInputStream)); 
					String line = "";
					try{line = myReader.readLine();}
					catch(Exception ex) {}
					Log.d("testOUT", line);*/
				Log.d("CURRENT SEQUENCE IS", "" + currentSequence);
				
				if ((keygonSequences.get(currentSequence) != null)){
					String currentWord = keygonSequences.get(currentSequence)[0];
					if(keygonSequences.get(currentSequence)[1] != null)
						Log.d("OTHER POSSIBLE WORDS ARE1", keygonSequences.get(currentSequence)[1]);
					if(keygonSequences.get(currentSequence)[2] != null)
						Log.d("OTHER POSSIBLE WORDS ARE2", keygonSequences.get(currentSequence)[2]);
					if(keygonSequences.get(currentSequence)[3] != null)
						Log.d("OTHER POSSIBLE WORDS ARE3", keygonSequences.get(currentSequence)[3]);
					if(keygonSequences.get(currentSequence)[4] != null)
						Log.d("OTHER POSSIBLE WORDS ARE4", keygonSequences.get(currentSequence)[4]);
					Log.d("Our word is", "" + currentWord);
					textOutput += " " + currentWord;
					lastWord = (String) currentWord;
					textOutputView.setText(textOutput); 
				}
					word1.setText("");
	        		word2.setText("");
	        		word3.setText("");
	        		word4.setText("");
	        		word5.setText("");
	        		word6.setText("");
	        		word7.setText("");
					currentSequence = "";
			}
        });
				
        
        compass.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
                int eventPadTouch = event.getAction();
                int iX = (int) event.getX(); //might have to add 1
                int currY = (int) event.getY(); //might have to add 1
                
                
                boolean isXYPositive = iX>=0 && currY>=0;
                newMyPoint = new MyPoint(iX, currY);
               // if (event.getEventTime() - 100 > prevTouchTime )
                pointsQ.add(newMyPoint);
              //  Log.d("onTouch is continuous" , "" + iX + "  " + currY);
                switch (eventPadTouch) {
                	
                    case MotionEvent.ACTION_DOWN:
	                    if(!gliding) {
	                        if (isXYPositive && iX<nb.getWidth() && currY<nb.getHeight()) { // ** Makes sure that X and Y are not less than 0, and no more than the height and width of the image.                
	                            if (nb.getPixel(iX,currY)!=0) {
	                                // NORTH BUMPER
	                            	prevButton = "northBump";
	                            	//prevTouchTime = (int)event.getEventTime();
	                            	Log.d("NB", "was picked");
	                            	
	                            }               
	                        }
	                        if (isXYPositive && iX<sb.getWidth() && currY<sb.getHeight()) { // ** Makes sure that X and Y are not less than 0, and no more than the height and width of the image.                
	                            if (sb.getPixel(iX,currY)!=0) {
	                                // SOUTH BUMPER
	                            	prevButton = "southBump";
	                            	//prevTouchTime = (int)event.getEventTime();
	                            	Log.d("SB", "was picked");
	                            }               
	                        }
	                        if (isXYPositive && iX<eb.getWidth() && currY<eb.getHeight()) { // ** Makes sure that X and Y are not less than 0, and no more than the height and width of the image.                
	                            if (eb.getPixel(iX,currY)!=0) {
	                                // EAST BUMPER
	                            	prevButton = "eastBump";
	                            	//prevTouchTime = (int)event.getEventTime();
	                            	Log.d("EB", "was picked");
	                            }               
	                        }
	                        if (isXYPositive && iX<wb.getWidth() && currY<wb.getHeight()) { // ** Makes sure that X and Y are not less than 0, and no more than the height and width of the image.                
	                            if (wb.getPixel(iX,currY)!=0) {
	                                // WEST BUMPER
	                            	prevButton = "westBump";
	                            	//prevTouchTime = (int)event.getEventTime();
	                            	Log.d("WB", "was picked");
	                            }               
	                        }
	                        if (isXYPositive && iX<ng.getWidth() && currY<ng.getHeight()) { // ** Makes sure that X and Y are not less than 0, and no more than the height and width of the image.                
	                            if (ng.getPixel(iX,currY)!=0) {
	                                // NORTH GROUPING
	                            	prevButton = "text";
	                            	currentSequence += "0";
	                            	prevTouchTime = (int)event.getEventTime();
	                            	Log.d("NG", "was picked");
	                            }               
	                        }
	                        if (isXYPositive && iX<neg.getWidth() && currY<neg.getHeight()) { // ** Makes sure that X and Y are not less than 0, and no more than the height and width of the image.                
	                            if (neg.getPixel(iX,currY)!=0) {
	                                // NORTH EAST GROUPING
	                            	prevButton = "text";
	                            	currentSequence += "1";
	                            	prevTouchTime = (int)event.getEventTime();
	                            	Log.d("NEG", "was picked");
	                            }               
	                        }
	                        if (isXYPositive && iX<eg.getWidth() && currY<eg.getHeight()) { // ** Makes sure that X and Y are not less than 0, and no more than the height and width of the image.                
	                            if (eg.getPixel(iX,currY)!=0) {
	                                // EAST GROUPING
	                            	prevButton = "text";
	                            	currentSequence += "2";
	                            	prevTouchTime = (int)event.getEventTime();
	                            	Log.d("EG", "was picked");
	                            }               
	                        }
	                        if (isXYPositive && iX<seg.getWidth() && currY<seg.getHeight()) { // ** Makes sure that X and Y are not less than 0, and no more than the height and width of the image.                
	                            if (seg.getPixel(iX,currY)!=0) {
	                                // SOUTH EAST GROUPING
	                            	prevButton = "text";
	                            	currentSequence += "3";
	                            	prevTouchTime = (int)event.getEventTime();
	                            	Log.d("SEG", "was picked");
	                            }               
	                        }
	                        if (isXYPositive && iX<sg.getWidth() && currY<sg.getHeight()) { // ** Makes sure that X and Y are not less than 0, and no more than the height and width of the image.                
	                            if (sg.getPixel(iX,currY)!=0) {
	                                // SOUTH GROUPING
	                            	prevButton = "text";
	                            	currentSequence += "4";
	                            	prevTouchTime = (int)event.getEventTime();
	                            	Log.d("SG", "was picked");
	                            }               
	                        }
	                        if (isXYPositive && iX<swg.getWidth() && currY<swg.getHeight()) { // ** Makes sure that X and Y are not less than 0, and no more than the height and width of the image.                
	                            if (swg.getPixel(iX,currY)!=0) {
	                                // SOUTH WEST GROUPING
	                            	prevButton = "text";
	                            	currentSequence += "5";
	                            	prevTouchTime = (int)event.getEventTime();
	                            	Log.d("SWG", "was picked");
	                            }               
	                        }
	                        if (isXYPositive && iX<wg.getWidth() && currY<wg.getHeight()) { // ** Makes sure that X and Y are not less than 0, and no more than the height and width of the image.                
	                            if (wg.getPixel(iX,currY)!=0) {
	                                // WEST GROUPING
	                            	prevButton = "text";
	                            	currentSequence += "6";
	                            	prevTouchTime = (int)event.getEventTime();
	                            	Log.d("WG", "was picked");
	                            }               
	                        }
	                        if (isXYPositive && iX<nwg.getWidth() && currY<nwg.getHeight()) { // ** Makes sure that X and Y are not less than 0, and no more than the height and width of the image.                
	                            if (nwg.getPixel(iX,currY)!=0) {
	                                // NORTH WEST GROUPING
	                            	prevButton = "text";
	                            	currentSequence += "7";
	                            	prevTouchTime = (int)event.getEventTime();
	                            	Log.d("NWG", "was picked");
	                            }            
	                        }
	                        if (isXYPositive && iX<center.getWidth() && currY<center.getHeight()) {
	                        	if(center.getPixel(iX,currY) != 0) {
	                        		prevButton = "center";
	                        		prevXTouch = iX;
	                        		prevYTouch = currY;
	                        		Log.d("CENTER", "was picked");
	                        	}
	                        }
	                        if (prevButton.equals("text") && keygonSequences.containsKey(currentSequence)) {
	                        	//TextView possibilities = (TextView) findViewById(R.id.textView2);
	                        	//possibilities.setText("");
	                        	for (int k = 0; k < keygonSequences.get(currentSequence).length; k++) {
	                        		TextView tv;
	                        		if (keygonSequences.get(currentSequence)[k] != null) {
	                        			if(k == 0){
	                        				tv = (TextView)findViewById(R.id.textView3);
	                        				tv.setText(keygonSequences.get(currentSequence)[k].toString());
	                        			}
	                        			if(k == 1){
	                        				tv = (TextView)findViewById(R.id.textView4);
	                        				tv.setText(keygonSequences.get(currentSequence)[k].toString());
	                        			}
	                        			if(k == 2){
	                        				tv = (TextView)findViewById(R.id.textView5);
	                        				tv.setText(keygonSequences.get(currentSequence)[k].toString());
	                        			}
	                        			if(k == 3){
	                        				tv = (TextView)findViewById(R.id.textView6);
	                        				tv.setText(keygonSequences.get(currentSequence)[k].toString());
	                        			}
	                        			if(k == 4){
	                        				tv = (TextView)findViewById(R.id.textView7);
	                        				tv.setText(keygonSequences.get(currentSequence)[k].toString());
	                        			}
	                        			if(k == 5){
	                        				tv = (TextView)findViewById(R.id.textView8);
	                        				tv.setText(keygonSequences.get(currentSequence)[k].toString());
	                        			}
	                        			if(k == 6){
	                        				tv = (TextView)findViewById(R.id.textView9);
	                        				tv.setText(keygonSequences.get(currentSequence)[k].toString());
	                        			}
	                        			
	                        		}
	                        	}
	                        	
	                        }
	                    }
                        return true;     
                        
                    case MotionEvent.ACTION_UP:
                    	prevTouchTime = (int) event.getEventTime() - (int) event.getDownTime(); 
                    	if(gliding) {
                    		Log.d("gliding action done", "handle possibilties!");
                    		currentSequence = "";
                    		assessFlow(pointsQ);
                    		pointsQ.clear();
                    		gliding=false;
                    	}
                    	else if(isXYPositive && prevButton.equals("northBump") && iX<sb.getWidth() && currY<sb.getHeight()) {
                    		if (sb.getPixel(iX, currY) !=0){
                    			//Swipe north to south LOWERCASE
                    			if(prevTouchTime > 2000) {
                    				Log.d("SWIPING", "LOWERCASE LONG");
                    			}
                    			else {
                    				Log.d("SWIPING", "LOWERCASE ACTION");
                    			}
                    			prevButton = "lowCase";
                    		}
                    	}
                    	else if(isXYPositive && prevButton.equals("southBump") && iX<nb.getWidth() && currY<nb.getHeight()) {
                    		if (nb.getPixel(iX, currY) !=0){
                    			//Swipe south to north UPPERCASE
                    			if(prevTouchTime > 2000) {
                    				Log.d("SWIPING", "CAPSLOCK");
                    			}
                    			else {
                    				Log.d("SWIPING", "UPPERCASE ACTION");
                    			}
                    			prevButton = "upCase";
                    		}
                    	}
                    	else if(isXYPositive && prevButton.equals("eastBump") && iX<wb.getWidth() && currY<wb.getHeight()) {
                    		if (wb.getPixel(iX, currY) !=0){
                    			//Swipe east to west DELETE TO LEFT
                    			if(prevTouchTime > 2000) {
                    				Log.d("SWIPING", "DELETE ENTIRE WORD TO LEFT");
                    			}
                    			else {
                    				Log.d("SWIPING", "DELETE TO LEFT");
                    				textOutput = textOutput.substring(0, textOutput.length() - lastWord.length());
                    				Log.d("new textoutput", textOutput);
                    				textOutputView.setText(textOutput);
                    			}
                    			prevButton = "lDel";
                    		}
                    	}
                    	else if(isXYPositive && prevButton.equals("westBump") && iX<eb.getWidth() && currY<eb.getHeight()) {
                    		if (eb.getPixel(iX, currY) !=0){
                    			//Swipe west to east DELETE TO RIGHT
                    			if(prevTouchTime > 2000) {
                    				Log.d("SWIPING", "DELETE ENTIRE WORD TO RIGHT");
                    			}
                    			else {
                    				Log.d("SWIPING", "DELETE TO RIGHT");
                    			}
                    			prevButton = "rDel";
                    		}
                    	}
                    	else if (isXYPositive && iX<center.getWidth() && currY<center.getHeight()) {
                        	if(center.getPixel(iX,currY) != 0) {
                        		prevButton = "center";
                        		prevXTouch = iX;
                        		prevYTouch = currY;
                        		Log.d("CENTER", "was picked");
                        	}
                        }
                    	return true;
                    	
                    case MotionEvent.ACTION_MOVE:
                    	if (isXYPositive && prevButton.equals("center")) {
                    		RelativeLayout.LayoutParams layoutCoords = (RelativeLayout.LayoutParams) compass.getLayoutParams();
                    		int leftMarg = layoutCoords.leftMargin + iX - prevXTouch;
                    		int topMarg = layoutCoords.topMargin + currY - prevYTouch;
                    		layoutCoords.leftMargin = leftMarg;
                    		layoutCoords.topMargin = topMarg;
                    		compass.setLayoutParams(layoutCoords);
                    		break;
                    	}
                    	if (isXYPositive && prevButton.equals("text")){
                    		//newMyPoint = new MyPoint(iX, currY);
                           // pointsQ.add(newMyPoint);
                            Log.d("moving", "Swyping");
                            gliding = true;
                            prevButton = "gliding";
                            break;
                    	}
                        return true;   
                }           
                return false;
			}
        }); 
        
        compass.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				if (prevButton == "lowCase"){
					Log.d("SWIPING", "LOWERCASE ACTION");
					prevButton = "";
					return true;
				}
				if (prevButton == "upCase") {
					Log.d("SWIPING", "CAPSLOCK");
					prevButton = "";
					return true; 
				}
				if (prevButton == "lDel") {
					Log.d("SWIPING", "DELETE ENTIRE WORD TO LEFT");
					prevButton = "";
					return true; 
				}
				if (prevButton == "rDel") {
					Log.d("SWIPING", "DELETE ENTIRE WORD TO RIGHT");
					prevButton = "";
					return true; 
				}
				return false;
			}
		});
    }
    
    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }
    
  public void assessFlow (List <MyPoint> touches) {

      final Bitmap ng = BitmapFactory.decodeResource(getResources(), R.drawable.ng);
      final Bitmap neg = BitmapFactory.decodeResource(getResources(), R.drawable.neg);
      final Bitmap eg = BitmapFactory.decodeResource(getResources(), R.drawable.eg);
      final Bitmap seg = BitmapFactory.decodeResource(getResources(), R.drawable.seg);
      final Bitmap sg = BitmapFactory.decodeResource(getResources(), R.drawable.sg);
      final Bitmap swg = BitmapFactory.decodeResource(getResources(), R.drawable.swg);
      final Bitmap wg = BitmapFactory.decodeResource(getResources(), R.drawable.wg);
      final Bitmap nwg = BitmapFactory.decodeResource(getResources(), R.drawable.nwg);
      final Bitmap center = BitmapFactory.decodeResource(getResources(), R.drawable.center);
      
	  int currX, currY;
	  MyPoint currPt;
	  boolean pivot = true;
	  for(int i = 0; i < touches.size(); i++) {
		  
		  currPt = touches.get(i);
		  currX =  (int)currPt.x;
		  currY = (int)currPt.y;
		 // Log.d("entering assess flow loop", "were on our way"+ currX + " " + currY);
		  if(pivot) {

			  if ( currX<ng.getWidth() && currY<ng.getHeight()) { // ** Makes sure that X and Y are not less than 0, and no more than the height and width of the image.                
                  if (ng.getPixel(currX,currY)!=0) {
                      // NORTH GROUPING
                	  pivot = false;
                	prevButton = "text";
                  	currentSequence += "0";
                  	Log.d(currentSequence,"ng");
                  }               
              }
              if ( currX<neg.getWidth() && currY<neg.getHeight()) { // ** Makes sure that X and Y are not less than 0, and no more than the height and width of the image.                
                  if (neg.getPixel(currX,currY)!=0) {
                      // NORTH EAST GROUPING
                	  pivot = false;
                  	prevButton = "text";
                  	currentSequence += "1";
                  	Log.d(currentSequence,"ne");
                  }               
              }
              if ( currX<eg.getWidth() && currY<eg.getHeight()) { // ** Makes sure that X and Y are not less than 0, and no more than the height and width of the image.                
                  if (eg.getPixel(currX,currY)!=0) {
                      // EAST GROUPING
                	  pivot = false;
                  	prevButton = "text";
                  	currentSequence += "2";
                  	Log.d(currentSequence,"eg");
                  }               
              }
              if ( currX<seg.getWidth() && currY<seg.getHeight()) { // ** Makes sure that X and Y are not less than 0, and no more than the height and width of the image.                
                  if (seg.getPixel(currX,currY)!=0) {
                      // SOUTH EAST GROUPING
                	  pivot = false;
                  	prevButton = "text";
                  	currentSequence += "3";
                  	Log.d(currentSequence,"se");
                  }               
              }
              if ( currX<sg.getWidth() && currY<sg.getHeight()) { // ** Makes sure that X and Y are not less than 0, and no more than the height and width of the image.                
                  if (sg.getPixel(currX,currY)!=0) {
                      // SOUTH GROUPING
                	  pivot = false;
                  	prevButton = "text";
                  	currentSequence += "4";
                  	Log.d(currentSequence,"s");
                  }               
              }
              if ( currX<swg.getWidth() && currY<swg.getHeight()) { // ** Makes sure that X and Y are not less than 0, and no more than the height and width of the image.                
                  if (swg.getPixel(currX,currY)!=0) {
                      // SOUTH WEST GROUPING
                	  pivot = false;
                  	prevButton = "text";
                  	currentSequence += "5";
                  	Log.d(currentSequence,"sw");
                  }               
              }
              if ( currX<wg.getWidth() && currY<wg.getHeight()) { // ** Makes sure that X and Y are not less than 0, and no more than the height and width of the image.                
                  if (wg.getPixel(currX,currY)!=0) {
                      // WEST GROUPING
                	pivot = false;
                  	prevButton = "text";
                  	currentSequence += "6";
                  	Log.d(currentSequence,"w");
                  }               
              }
              if ( currX<nwg.getWidth() && currY<nwg.getHeight()) { // ** Makes sure that X and Y are not less than 0, and no more than the height and width of the image.                
                  if (nwg.getPixel(currX,currY)!=0) {
                      // NORTH WEST GROUPING
                	  pivot = false;
                  	prevButton = "text";
                  	currentSequence += "7";
                  	Log.d(currentSequence,"nw");
                  }            
              }
              /*if ( currX<center.getWidth() && currY<center.getHeight()) {
              	if(center.getPixel(currX,currY) != 0) {
              		prevButton = "center";
              		prevXTouch = currX;
              		prevYTouch = currY;
              		pivot = true;
              		              		continue;
              	}
              }*/

		  }
		  else if ( currX<center.getWidth() && currY<center.getHeight()) {
            	if(center.getPixel(currX,currY) != 0) {
            		pivot = true;
            		prevButton = "center";
            		//Log.d("NEW PIVOT","wooo");
            		continue;
            	}
          }
		  
          if(keygonSequences.containsKey(currentSequence)) {
            	//TextView possibilities = (TextView) findViewById(R.id.textView2);
            	//possibilities.setText("");
            	for (int k = 0; k < keygonSequences.get(currentSequence).length; k++) {
            		TextView tv;
            		if (keygonSequences.get(currentSequence)[k] != null) {
            			if(k == 0){
            				tv = (TextView)findViewById(R.id.textView3);
            				tv.setText(keygonSequences.get(currentSequence)[k].toString());
            			}
            			if(k == 1){
            				tv = (TextView)findViewById(R.id.textView4);
            				tv.setText(keygonSequences.get(currentSequence)[k].toString());
            			}
            			if(k == 2){
            				tv = (TextView)findViewById(R.id.textView5);
            				tv.setText(keygonSequences.get(currentSequence)[k].toString());
            			}
            			if(k == 3){
            				tv = (TextView)findViewById(R.id.textView6);
            				tv.setText(keygonSequences.get(currentSequence)[k].toString());
            			}
            			if(k == 4){
            				tv = (TextView)findViewById(R.id.textView7);
            				tv.setText(keygonSequences.get(currentSequence)[k].toString());
            			}
            			if(k == 5){
            				tv = (TextView)findViewById(R.id.textView8);
            				tv.setText(keygonSequences.get(currentSequence)[k].toString());
            			}
            			if(k == 6){
            				tv = (TextView)findViewById(R.id.textView9);
            				tv.setText(keygonSequences.get(currentSequence)[k].toString());
            			}
            			
            		}
            	}
            	
            }
	  }
  }
}



