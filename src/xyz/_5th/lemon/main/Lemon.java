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
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultEditorKit;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

public class Lemon extends JFrame {

	private RSyntaxTextArea area = new RSyntaxTextArea(20, 120);
	private JFileChooser dialog = new JFileChooser(
			System.getProperty("user.dir"));
	private String currentFile = "Untitled";
	private boolean changed = false;

	public Lemon() {
		area.setFont(new Font("Monospaced", Font.PLAIN, 12));
		RTextScrollPane scroll = new RTextScrollPane(area, true);
		add(scroll, BorderLayout.CENTER);
		
		area.setCodeFoldingEnabled(true);
		codeFolded = true;

		JMenuBar menu = new JMenuBar();
		setJMenuBar(menu);
		JMenu file = new JMenu("File");
		JMenu edit = new JMenu("Edit");
		JMenu syntax = new JMenu("Sytax");
		menu.add(file);
		menu.add(edit);
		menu.add(syntax);
		file.add(Open);
		file.add(Save);
		file.add(SaveAs);
		file.add(Quit);
		file.addSeparator();
		file.add(CodeFolding);

		setupSyntax(syntax);

		for (int i = 0; i < 4; i++)
			file.getItem(i).setIcon(null);

		edit.add(Cut);
		edit.add(Copy);
		edit.add(Paste);

		edit.getItem(0).setText("Cut out");
		edit.getItem(1).setText("Copy");
		edit.getItem(2).setText("Paste");

		JToolBar tool = new JToolBar();
		add(tool, BorderLayout.NORTH);
		tool.add(Open);
		tool.add(Save);
		tool.addSeparator();

		JButton cut = tool.add(Cut), copy = tool.add(Copy), paste = tool
				.add(Paste);

		cut.setText(null);
		cut.setIcon(new ImageIcon("cut.gif"));
		copy.setText(null);
		copy.setIcon(new ImageIcon("copy.gif"));
		paste.setText(null);
		paste.setIcon(new ImageIcon("paste.gif"));

		Save.setEnabled(false);
		SaveAs.setEnabled(false);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		area.addKeyListener(k1);
		area.addKeyListener(new Commands());
		setTitle("Lemon | " + currentFile);
	}

	private void setupSyntax(JMenu s) {
		// TODO: Setup Syntax
		addSyntax("None", s, SyntaxConstants.SYNTAX_STYLE_NONE);
		s.addSeparator();
		
		JMenu sA = new JMenu("A");
		addSyntax("ActionScript", sA, SyntaxConstants.SYNTAX_STYLE_ACTIONSCRIPT);
		addSyntax("Assembler x86", sA, SyntaxConstants.SYNTAX_STYLE_ASSEMBLER_X86);
		s.add(sA);
		
		addSyntax("BB Code", s, SyntaxConstants.SYNTAX_STYLE_BBCODE);
		
		JMenu sC = new JMenu("C");
		addSyntax("C", sC, SyntaxConstants.SYNTAX_STYLE_C);
		addSyntax("C++", sC, SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);
		addSyntax("C#", sC, SyntaxConstants.SYNTAX_STYLE_CSHARP);
		addSyntax("Clojure", sC, SyntaxConstants.SYNTAX_STYLE_CLOJURE);
		addSyntax("CSS", sC, SyntaxConstants.SYNTAX_STYLE_CSS);
		s.add(sC);
		
		JMenu sD = new JMenu("D");
		addSyntax("D", sD, SyntaxConstants.SYNTAX_STYLE_D);
		addSyntax("Dart", sD, SyntaxConstants.SYNTAX_STYLE_DART);
		addSyntax("Delphi", sD, SyntaxConstants.SYNTAX_STYLE_DELPHI);
		addSyntax("DTD", sD, SyntaxConstants.SYNTAX_STYLE_DTD);
		s.add(sD);
		
		addSyntax("Fortran", s, SyntaxConstants.SYNTAX_STYLE_FORTRAN);
		
		addSyntax("Groovy", s, SyntaxConstants.SYNTAX_STYLE_GROOVY);
		
		JMenu sH = new JMenu("H");
		addSyntax("Htaccess", sH, SyntaxConstants.SYNTAX_STYLE_HTACCESS);
		addSyntax("HTML", sH, SyntaxConstants.SYNTAX_STYLE_HTML);
		s.add(sH);
		
		JMenu sJ = new JMenu("J");
		addSyntax("Java", sJ, SyntaxConstants.SYNTAX_STYLE_JAVA);
		addSyntax("JavaScript", sJ, SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
		addSyntax("JSON", sJ, SyntaxConstants.SYNTAX_STYLE_JSON);
		addSyntax("JSP", sJ, SyntaxConstants.SYNTAX_STYLE_JSP);
		s.add(sJ);
		
		JMenu sL = new JMenu("L");
		addSyntax("Latex", sL, SyntaxConstants.SYNTAX_STYLE_LATEX);
		addSyntax("Lisp", sL, SyntaxConstants.SYNTAX_STYLE_LISP);
		addSyntax("Lua", sL, SyntaxConstants.SYNTAX_STYLE_LUA);
		s.add(sL);
		
		JMenu sM = new JMenu("M");
		addSyntax("Makefile", sM, SyntaxConstants.SYNTAX_STYLE_MAKEFILE);
		addSyntax("MXML", sM, SyntaxConstants.SYNTAX_STYLE_MXML);
		s.add(sM);
		
		addSyntax("NSIS", s, SyntaxConstants.SYNTAX_STYLE_NSIS);
		
		JMenu sP = new JMenu("P");
		addSyntax("Perl", sP, SyntaxConstants.SYNTAX_STYLE_PERL);
		addSyntax("PHP", sP, SyntaxConstants.SYNTAX_STYLE_PHP);
		addSyntax("Properties File", sP, SyntaxConstants.SYNTAX_STYLE_PROPERTIES_FILE);
		addSyntax("Python", sP, SyntaxConstants.SYNTAX_STYLE_PYTHON);
		s.add(sP);
		
		addSyntax("Ruby", s, SyntaxConstants.SYNTAX_STYLE_RUBY);
		
		JMenu sS = new JMenu("S");
		addSyntax("SAS", sS, SyntaxConstants.SYNTAX_STYLE_SAS);
		addSyntax("Scala", sS, SyntaxConstants.SYNTAX_STYLE_SCALA);
		addSyntax("SQL", sS, SyntaxConstants.SYNTAX_STYLE_SQL);
		s.add(sS);
	}
	
	private void addSyntax(String name, JMenu s, final String syntax){
		s.add(new AbstractAction(name){
			public void actionPerformed(ActionEvent e){
				area.setSyntaxEditingStyle(syntax);
			}
		});
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
	
	
	boolean codeFolded = true;
	Action CodeFolding = new AbstractAction("Toggle Folding"){
		public void actionPerformed(ActionEvent e){
			if(codeFolded){
				area.setCodeFoldingEnabled(false);
				codeFolded = false;
			}else{
				area.setCodeFoldingEnabled(true);
				codeFolded = true;
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
				saveFile(currentFile);
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

	ActionMap m = area.getActionMap();
	Action Cut = m.get(DefaultEditorKit.cutAction);
	Action Copy = m.get(DefaultEditorKit.copyAction);
	Action Paste = m.get(DefaultEditorKit.pasteAction);

	private void saveFileAs() {
		if (dialog.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
			saveFile(dialog.getSelectedFile().getAbsolutePath());
	}

	private void saveOld() {
		if (changed) {
			if (JOptionPane.showConfirmDialog(this, "Would you like to save "
					+ currentFile + "?", "Save", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
				saveFile(currentFile);
		}
	}

	private void readInFile(String fileName) {
		try {
			FileReader r = new FileReader(fileName);
			area.read(r, null);
			r.close();
			currentFile = fileName;
			setTitle("Lemon | " + currentFile);
			changed = false;
		} catch (IOException e) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this,
					"Editor can't find the file called " + fileName);
		}
	}

	private void saveFile(String fileName) {
		try {
			FileWriter w = new FileWriter(fileName);
			area.write(w);
			w.close();
			currentFile = fileName;
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
}
