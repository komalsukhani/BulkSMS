
import javax.microedition.io.*;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import javax.wireless.messaging.*;
public class HugeSMS extends MIDlet implements CommandListener {
      Display display;
      private TextField toWhom;
      private TextField message;
      private TextField count;    
      
      private Alert alert;
      int n;
      private Command send,exit;
      MessageConnection clientConn;
      private Form compose;
      public HugeSMS() {
            display=Display.getDisplay(this);
            compose=new Form("Bulk SMS!!!!");
            toWhom=new TextField("To","",10,TextField.PHONENUMBER);
            message=new TextField("Message","",600,TextField.ANY);
            count= new TextField("Count","",600,TextField.NUMERIC);
            count.insert("1", 0);
            send=new Command("Send",Command.BACK,0);
            exit=new Command("Exit",Command.SCREEN,5);
            compose.append(toWhom);
            compose.append(message);
            compose.append(count);
            compose.addCommand(send);
            compose.addCommand(exit);
            compose.setCommandListener(this);
      }
      public void startApp() {
            display.setCurrent(compose);
      }
      public void pauseApp() {
      }
      public void destroyApp(boolean unconditional) {
            notifyDestroyed();
      }
      public void commandAction(Command cmd,Displayable disp) {
            if(cmd==exit) {
                  destroyApp(false);
            }
            if(cmd==send) {
                  String mno=toWhom.getString();
                  String msg=message.getString();
                  if(mno.equals("")) {
                        alert = new Alert("Alert");
                        alert.setString("Enter Mobile Number!!!");
                        alert.setTimeout(2000);
                        display.setCurrent(alert);
                  }
                  else {
                        try {
                              clientConn=(MessageConnection)Connector.open("sms://"+mno);
                        }
                        catch(Exception e) {
                              alert = new Alert("Alert");
                              alert.setString("Unable to connect to Station because of network problem");
                              alert.setTimeout(2000);
                              display.setCurrent(alert);
                        }
                        try {
                              TextMessage textmessage = (TextMessage) clientConn.newMessage(MessageConnection.TEXT_MESSAGE);
                              textmessage.setAddress("sms://"+mno);
                              textmessage.setPayloadText(msg);
                              if(Integer.parseInt(count.getString())>0)
                                  n=Integer.parseInt(count.getString());
                              else
                                  n=1;
                              for(int i=0;i<n ;i++)
                              {
                                  clientConn.send(textmessage);
                                  System.out.print("Send: "+ i +"\n");
                              }
                        }
                        catch(Exception e)
                        {
                              Alert alert=new Alert("Alert","",null,AlertType.INFO);
                              alert.setTimeout(Alert.FOREVER);
                              alert.setString("Unable to send");
                              display.setCurrent(alert);
                        }
                  }
            }
      }
}