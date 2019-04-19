
import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import chrriis.common.UIUtils;
import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;

import java.util.*;
import java.util.regex.*;
import java.text.MessageFormat;

public class map extends JPanel {
    private JPanel webBrowserPanel;
    private JWebBrowser webBrowser;

    public static String processTemplate(String template, Map<String, Object> params){
        StringBuffer sb = new StringBuffer();
        Matcher m = Pattern.compile("\\$\\{\\w+\\}").matcher(template);
        while (m.find()) {
            String param = m.group();
            Object value = params.get(param.substring(2, param.length() - 1));
            m.appendReplacement(sb, value==null ? "" : value.toString());
        }
        m.appendTail(sb);
        return sb.toString();
    }

    public static String readToString(String fileName) {
        String encoding = "UTF-8";
        File file = new File(fileName);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }

    public map(String html) {
        super(new BorderLayout());
        webBrowserPanel = new JPanel(new BorderLayout());
        webBrowser = new JWebBrowser();

        webBrowser.setButtonBarVisible(false);
        webBrowser.setMenuBarVisible(false);
        webBrowser.setBarsVisible(false);
        webBrowser.setStatusBarVisible(false);
        webBrowserPanel.add(webBrowser, BorderLayout.CENTER);
        add(webBrowserPanel, BorderLayout.CENTER);

        webBrowser.setHTMLContent(html);
//        webBrowser.executeJavascript("alert('hello swing')");
    }

    public void draw() {
        webBrowser.executeJavascript("alert('123')");
    }

    public static void paintMap(String html) {
        UIUtils.setPreferredLookAndFeel();
        NativeInterface.open();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//                frame.setUndecorated(true);

                frame.getContentPane().add(new map(html), BorderLayout.CENTER);
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                frame.setLocationByPlatform(true);
                frame.setVisible(true);
                frame.setResizable(true);
                frame.setSize(1000, 700);
                frame.setLocationRelativeTo(frame.getOwner());

            }
        });
//        NativeInterface.runEventPump();
    }

    public static void main(String[] args) {
        String points = String.format("[%f, %f],", 116.478935, 39.997761); // source points

        points += String.format("[%f, %f], ", 116.478939, 39.997825);
        points += String.format("[%f, %f], ", 116.478912, 39.998549);
        points += String.format("[%f, %f], ", 116.478912, 39.998549);
        points += String.format("[%f, %f], ", 116.478998, 39.998555);

        HashMap map = new HashMap<String, Object>();
        map.put("points", points);

        String message = processTemplate(readToString("template.html"), map);
        System.out.println(message);
        paintMap(message);
    }
}