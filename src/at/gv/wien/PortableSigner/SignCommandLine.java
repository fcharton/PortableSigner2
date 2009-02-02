/*
 * CommandLine.java
 *
 * Created on 25. Oktober 2006, 10:03
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package at.gv.wien.PortableSigner;

import org.apache.commons.cli.*;
import java.io.FileInputStream;
import java.util.Arrays;


/**
 *
 * @author pfp
 */
public class SignCommandLine {
    public String input = "", output = "", signature = "", password = "",
            sigblock = "", sigimage = "", comment = "", reason = "", location = "",
            pwdFile = "";
    public Boolean nogui = false, finalize = true;
    private Boolean help = false;
    
    /** Creates a new instance of CommandLine */
    public SignCommandLine(String args[]) {
       CommandLine cmd;
       Options options = new Options();
       options.addOption("t", true, 
               java.util.ResourceBundle.getBundle("at/gv/wien/PortableSigner/i18n").getString("CLI-InputFile"));
       options.addOption("o", true, 
               java.util.ResourceBundle.getBundle("at/gv/wien/PortableSigner/i18n").getString("CLI-OutputFile"));
       options.addOption("s", true, 
               java.util.ResourceBundle.getBundle("at/gv/wien/PortableSigner/i18n").getString("CLI-SignatureFile"));
       options.addOption("p", true, 
               java.util.ResourceBundle.getBundle("at/gv/wien/PortableSigner/i18n").getString("CLI-Password"));
       options.addOption("n", false, 
               java.util.ResourceBundle.getBundle("at/gv/wien/PortableSigner/i18n").getString("CLI-WithoutGUI"));
       options.addOption("f", false, 
               java.util.ResourceBundle.getBundle("at/gv/wien/PortableSigner/i18n").getString("CLI-Finalize"));
       options.addOption("h", false, 
               java.util.ResourceBundle.getBundle("at/gv/wien/PortableSigner/i18n").getString("CLI-Help"));
       options.addOption("b", true, 
               java.util.ResourceBundle.getBundle("at/gv/wien/PortableSigner/i18n").getString("CLI-SigBlock"));
       options.addOption("i", true, 
               java.util.ResourceBundle.getBundle("at/gv/wien/PortableSigner/i18n").getString("CLI-SigImage"));
       options.addOption("c", true, 
               java.util.ResourceBundle.getBundle("at/gv/wien/PortableSigner/i18n").getString("CLI-SigComment"));
       options.addOption("r", true, 
               java.util.ResourceBundle.getBundle("at/gv/wien/PortableSigner/i18n").getString("CLI-SigReason"));
       options.addOption("l", true, 
               java.util.ResourceBundle.getBundle("at/gv/wien/PortableSigner/i18n").getString("CLI-SigLocation"));
       options.addOption("pwdfile", true,
               java.util.ResourceBundle.getBundle("at/gv/wien/PortableSigner/i18n").getString("CLI-PasswdFile"));

       CommandLineParser parser = new PosixParser();
       HelpFormatter usage = new HelpFormatter();
       try {
            cmd = parser.parse(options, args);
            input = cmd.getOptionValue("t"); 
            output = cmd.getOptionValue("o");
            signature = cmd.getOptionValue("s");
            password = cmd.getOptionValue("p");
            nogui = cmd.hasOption("n");
            help = cmd.hasOption("h");
            finalize = !cmd.hasOption("f");
            sigblock = cmd.getOptionValue("b");
            sigimage = cmd.getOptionValue("i");
            comment = cmd.getOptionValue("c");
            reason = cmd.getOptionValue("r");
            location = cmd.getOptionValue("l");
            pwdFile = cmd.getOptionValue("pwdfile");
            if (sigblock == null) { sigblock = ""; comment = ""; }
            if (sigimage == null) { sigimage = ""; }
            
            if (cmd.getArgs().length != 0) {
                throw new ParseException(java.util.ResourceBundle
                        .getBundle("at/gv/wien/PortableSigner/i18n").getString("CLI-UnknownArguments"));
            }
       } catch(ParseException e) {
           System.err.println(java.util.ResourceBundle
                   .getBundle("at/gv/wien/PortableSigner/i18n").getString("CLI-WrongArguments"));
           usage.printHelp("PortableSigner", options);
           System.exit(3);
       }
       if (nogui) {
           if (input == null || output == null || signature == null ) {
               System.err.println(java.util.ResourceBundle
                       .getBundle("at/gv/wien/PortableSigner/i18n").getString("CLI-MissingArguments"));
               usage.printHelp("PortableSigner", options);
               System.exit(2);
           }
           if (password == null) {
			   // password missing
			   if (pwdFile != null) {
				   // read the password from the given file
				   try {
					   FileInputStream pwdfis = new FileInputStream (pwdFile);
					   byte[] pwd = new byte[1024];
					   password = "";
					   try {
						   do {
								int r = pwdfis.read (pwd);
								if (r < 0) break;
								password += new String(Arrays.copyOfRange(pwd, 0, r));
								password = password.trim ();
						   } while (pwdfis.available() > 0);
						   pwdfis.close ();
					   } catch (java.io.IOException ex) {}
				   } catch (java.io.FileNotFoundException fnfex) {}
			   }
			   else if (nogui)
			   {
				   // no password file given, read from standard input
				   System.out.print(java.util.ResourceBundle
						   .getBundle("at/gv/wien/PortableSigner/i18n").getString("CLI-MissingPassword"));
				   byte[] pwd = new byte[1024];
				   password = "";
				   try {
					   do {
							int r = System.in.read (pwd);
							if (r < 0) break;
							password += new String(Arrays.copyOfRange(pwd, 0, r));
							password = password.trim ();
                          } while (System.in.available() > 0);
                   } catch (java.io.IOException ex) {}
			   }
		   }
       }
       
       if (!(sigblock.equals("german") || sigblock.equals("english") ||  sigblock.equals("polish") || sigblock.equals(""))) {
           System.err.println(java.util.ResourceBundle
                   .getBundle("at/gv/wien/PortableSigner/i18n").getString("CLI-Only-german-english"));
               usage.printHelp("PortableSigner", options);
               System.exit(4);
       }
       if (help) {
           usage.printHelp("PortableSigner", options);
           System.exit(1);
       }
    }
}
