package xyz._5th.lemon.main;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultEditorKit;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

public class Lemon extends JFrame {
	
	private JFileChooser dialog = new JFileChooser(System.getProperty("user.dir"));
	
	static File currentFile;
	static List<File> openFiles = new ArrayList<File>();
	
	private boolean changed = false;
	private JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);

	public Lemon() {
		
		currentFile = new File();
		openFiles.add(currentFile);
		
		tabs.add(currentFile.getName(), currentFile.getTextArea());
		add(tabs);

		JMenuBar menu = new JMenuBar();
		setJMenuBar(menu);
		JMenu file = new JMenu("File");
		JMenu edit = new JMenu("Edit");
		JMenu syntax = new JMenu("Syntax");
		menu.add(file);
		menu.add(edit);
		menu.add(syntax);
		file.add(Open);
		file.add(Save);
		file.add(SaveAs);
		file.add(Quit);
		file.addSeparator();
		file.add(CodeFolding);

		for (int i = 0; i < 4; i++)
			file.getItem(i).setIcon(null);

//		edit.add(Cut);
//		edit.add(Copy);
//		edit.add(Paste);

//		edit.getItem(0).setText("Cut out");
//		edit.getItem(1).setText("Copy");
//		edit.getItem(2).setText("Paste");

		JToolBar tool = new JToolBar();
		add(tool, BorderLayout.NORTH);
		tool.add(Open);
		tool.add(Save);
		tool.addSeparator();

//		JButton cut = tool.add(Cut), copy = tool.add(Copy), paste = tool
//				.add(Paste);

//		cut.setText(null);
//		cut.setIcon(new ImageIcon("cut.gif"));
//		copy.setText(null);
//		copy.setIcon(new ImageIcon("copy.gif"));
//		paste.setText(null);
//		paste.setIcon(new ImageIcon("paste.gif"));

		Save.setEnabled(false);
		SaveAs.setEnabled(false);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		addKeyListener(k1);
		addKeyListener(new Commands());
		setTitle("Lemon | " + currentFile);
	}

	
	
	
	
	private KeyListener k1 = new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
			changed = true;
			Save.setEnabled(true);
			SaveAs.setEnabled(true);
			if(!getTitle().contains("*") && !currentFile.equals("Untitled"))
				setTitle(getTitle() + " *");
		}
	};
	
	
	Action CodeFolding = new AbstractAction("Toggle Folding"){
		public void actionPerformed(ActionEvent e){
			if(currentFile.codeFolded){
				currentFile.getTextArea().setCodeFoldingEnabled(false);
				currentFile.codeFolded = false;
			}else{
				currentFile.getTextArea().setCodeFoldingEnabled(true);
				currentFile.codeFolded = true;
			}
		}
	};
	
	Action Open = new AbstractAction("Open", new ImageIcon("open.gif")) {
		public void actionPerformed(ActionEvent e) {
			saveOld();
			if (dialog.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				readInFile(dialog.getSelectedFile().getAbsolutePath());
			}
			SaveAs.setEnabled(true);
		}
	};

	Action Save = new AbstractAction("Save", new ImageIcon("save.gif")) {
		public void actionPerformed(ActionEvent e) {
			if (!currentFile.equals("Untitled"))
				saveFile(currentFile.getPath());
			else
				saveFileAs();
		}
	};

	Action SaveAs = new AbstractAction("Save as...") {
		public void actionPerformed(ActionEvent e) {
			saveFileAs();
		}
	};

	Action Quit = new AbstractAction("Quit") {
		public void actionPerformed(ActionEvent e) {
			saveOld();
			System.exit(0);
		}
	};

//	ActionMap m = currentFile.getTextArea().getActionMap();
//	Action Cut = m.get(DefaultEditorKit.cutAction);
//	Action Copy = m.get(DefaultEditorKit.copyAction);
//	Action Paste = m.get(DefaultEditorKit.pasteAction);

	private void saveFileAs() {
		if (dialog.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
			saveFile(dialog.getSelectedFile().getAbsolutePath());
	}

	private void saveOld() {
		if (changed) {
			if (JOptionPane.showConfirmDialog(this, "Would you like to save "
					+ currentFile + "?", "Save", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
				saveFile(currentFile.getPath());
		}
	}

	private void readInFile(String path) {
		try {
			FileReader r = new FileReader(path);
			File f = new File(getFile(path), path);
			tabs.add(getFile(path), f.getTextArea());
			openFiles.add(f);
			tabs.setSelectedComponent(f.getTextArea());
			currentFile = f;
			r.close();
		} catch (IOException e) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this,
					"Editor can't find the file called " + path);
		}
	}

	private void saveFile(String path) {
		try {
			FileWriter w = new FileWriter(path);
			currentFile.getTextArea().write(w);
			w.close();
			currentFile.setPath(path);
			setTitle("Lemon | " + currentFile);
			changed = false;
			Save.setEnabled(false);
		} catch (IOException e) {
		}
	}
	
	public static void main(String[] arg) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Lemon().setVisible(true);
			}
		});
	}
	
	public String getFile(String path){
		String[] a = path.split("/");
		return a[a.length-1];
	}
	
}
