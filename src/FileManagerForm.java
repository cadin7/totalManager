import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class FileManagerForm {
	// frames
	private JFrame frame = new JFrame("TotalManager ");
	private JPanel panel = new JPanel();
	private JScrollPane scrollpane1, scrollpane2;
	private JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollpane1, scrollpane2);
	// file trees

	private JTree leftTree = new JTree();
	private JTree rightTree = new JTree();
	private JTree currentTree = leftTree;

	private JLabel infoLabel = new JLabel();

	// right mouse click menus
	private JPopupMenu fileMenu = new JPopupMenu();
	private JPopupMenu standardMenu = new JPopupMenu();

	private Properties settings = new Properties();

	private Listeners listeners = new Listeners();

	private NutPad nutpad = new NutPad();

	private File selectedFile;

	private Border selectBorder, nullBorder;

	private JMenuBar menu = new JMenuBar();

	JPanel btnPnl = new JPanel(new FlowLayout(FlowLayout.CENTER));
	JButton btnf1 = new JButton(ButtonNames.ftpName);
	JButton btnf2 = new JButton(ButtonNames.driveName);
	JButton btnf3 = new JButton(ButtonNames.openName);
	JButton btnf4 = new JButton(ButtonNames.editName);
	JButton btnf5 = new JButton(ButtonNames.copyName);
	JButton btnf6 = new JButton(ButtonNames.moveName);
	JButton btnf7 = new JButton(ButtonNames.newFolderName);
	JButton btnf8 = new JButton(ButtonNames.deleteName);
	JButton btnf9 = new JButton(ButtonNames.renameName);

	FileManagerForm() {
		listeners.setProcessForm(this);
		nutpad.setProcessForm(this);

		File settingsFile = new File("settings.ini");
		if (settingsFile.exists())
			readSettingsFromFile();
		else
			loadDefaults();

		applySettings();

		buildMainForm();
		buildFileMenu();
		buildStandardMenu();

		frame.setVisible(true);

		saveSettings();

	}

	public void buildMainForm() {
		frame.setLayout(new BorderLayout());

		panel.setLayout(new GridLayout(1, 2));
		panel.add(scrollpane1);
		panel.add(scrollpane2);

		btnPnl.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		btnPnl.add(btnf1, BorderLayout.SOUTH);
		btnPnl.add(btnf2, BorderLayout.SOUTH);
		btnf2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listeners.changeDrive(listeners.getpForm().getCurrentTree());
			}
		});
		
		btnPnl.add(btnf3, BorderLayout.SOUTH);
		btnf3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (listeners.getpForm().getCurrentTree() != null) {
					listeners.openFile(listeners.getpForm().getCurrentTree());
				} else {
					listeners.openFile();
				}
			}
		});
		
		btnPnl.add(btnf4, BorderLayout.SOUTH);
		btnf4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listeners.editFile(listeners.getpForm().getCurrentTree());
			}
		});
		
		btnPnl.add(btnf5, BorderLayout.SOUTH);
		btnf5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listeners.copyDir(listeners.getpForm().getCurrentTree());
			}
		});
		
		btnPnl.add(btnf6, BorderLayout.SOUTH);
		btnf6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listeners.moveDir(listeners.getpForm().getCurrentTree());
			}
		});
		
		btnPnl.add(btnf7, BorderLayout.SOUTH);
		btnf7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listeners.makeDir();
			}
		});
		
		btnPnl.add(btnf8, BorderLayout.SOUTH);
		btnf8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listeners.deleteDir(listeners.getpForm().getCurrentTree());
			}
		});
		
		btnPnl.add(btnf9, BorderLayout.SOUTH);
		frame.add(btnPnl, BorderLayout.SOUTH);

		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollpane1, scrollpane2);
		splitPane.setDividerLocation((int) (0.5 * this.frame.getWidth()));

		frame.add(panel, BorderLayout.CENTER);
		// frame.add(infoLabel, BorderLayout.SOUTH);
		frame.add(splitPane, BorderLayout.CENTER);

		leftTree.addMouseListener(listeners.currentTreeListener);
		rightTree.addMouseListener(listeners.currentTreeListener);

		leftTree.addMouseListener(listeners.mouseRightMenu);
		leftTree.addKeyListener(listeners.hotKeys);
		rightTree.addMouseListener(listeners.mouseRightMenu);
		rightTree.addKeyListener(listeners.hotKeys);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addComponentListener(new ComponentListener() {

			@Override
			public void componentShown(ComponentEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void componentResized(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				splitPane.setDividerLocation((int) (0.5 * frame.getWidth()));
			}

			@Override
			public void componentMoved(ComponentEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void componentHidden(ComponentEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		frame.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				// TODO Auto-generated method stub
				saveSettings();

			}

			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		JMenu help = new JMenu("Help");
		help.setMnemonic(KeyEvent.VK_H);
		this.menu.add(help);

		JMenuItem hotkeys = new JMenuItem("Hot Keys");
		help.add(hotkeys);

		hotkeys.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				nutpad.setWorkFile(new File("hotkeys.txt"));
				nutpad.openWorkFile(false);
			}
		});

		help.addSeparator();

		JMenuItem about = new JMenuItem("About", KeyEvent.VK_A);
		help.add(about);
		frame.setJMenuBar(this.menu);
		about.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				nutpad.setWorkFile(new File("about.txt"));
				nutpad.openWorkFile(false);
			}
		});

	}

	public void buildFileMenu() {
		final JMenuItem open = new JMenuItem("Open");
		this.fileMenu.add(open);
		fileMenu.addSeparator();
		final JMenuItem copy = new JMenuItem("Copy");
		this.fileMenu.add(copy);
		copy.setEnabled(false);

		fileMenu.addComponentListener(new ComponentListener() {

			@Override
			public void componentShown(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				listeners.getCopyPaths();
			}

			@Override
			public void componentResized(ComponentEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void componentMoved(ComponentEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void componentHidden(ComponentEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		open.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				listeners.openFile();
			}
		});

		copy.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					listeners.copyFolder(new File(listeners.getTransferFrom()), new File(listeners.getTransferTo()));
					System.out.println("all files successfully copied");

				} catch (IOException ioe) {
					// TODO Auto-generated catch block
					System.out.println("can't copy files");
				}

			}
		});
	}

	public void buildStandardMenu() {
		final JMenuItem itemNewFile = new JMenuItem("Create New File");
		this.standardMenu.add(itemNewFile);
		final JMenuItem itemNewDirectory = new JMenuItem("Create New Directory");
		this.standardMenu.add(itemNewDirectory);

		itemNewFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				listeners.makeFile();
			}
		});

		itemNewDirectory.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				listeners.makeDir();
			}
		});

	}

	public void readSettingsFromFile() {
		try {
			FileInputStream inStream = new FileInputStream("settings.ini");
			settings.load(inStream);

			inStream.close();
		} catch (IOException e) {
			System.out.println("I/0 exception in reading\nsettings from file");
		}

	}

	/**
	 * defaults list: (int)"width" - width of the main window (int)"height" -
	 * height of the main window (string)left_cat - start catalog of left tree
	 * menu (string)right_cat - start catalog of right tree menu
	 * (double)size_coeff - size of the window (double)offset_coeff- offset of
	 * the window from top and left
	 */

	public void loadDefaults() {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		Integer height = (int) dim.getHeight();
		Integer width = (int) dim.getWidth();

		Double size_coeff = 0.66;
		Double offset_coeff = 0.17;

		File[] roots = File.listRoots();

		int index = Math.min(roots.length - 1, 1);
		String left_cat = roots[index].getAbsolutePath();
		String right_cat = roots[index].getAbsolutePath();

		settings.setProperty("width", width.toString());
		settings.setProperty("height", height.toString());
		settings.setProperty("size_coeff", size_coeff.toString());
		settings.setProperty("offset_coeff", offset_coeff.toString());
		settings.setProperty("left_cat", left_cat);
		settings.setProperty("right_cat", right_cat);

		System.out.println("left_cat is " + left_cat + " right_cat is " + right_cat);

		settings.put("width", width.toString());
		settings.put("height", height.toString());

		settings.put("size_coeff", size_coeff.toString());
		settings.put("offset_coeff", offset_coeff.toString());

	}

	public void applySettings() {
		String leftRoot, rightRoot;
		int width, height;
		double size_coeff, offset_coeff;
		// getting width, height
		width = Integer.valueOf(settings.getProperty("width").toString());
		height = Integer.valueOf(settings.getProperty("height").toString());
		size_coeff = Double.valueOf(settings.getProperty("size_coeff").toString());
		offset_coeff = Double.valueOf(settings.getProperty("offset_coeff").toString());

		// getting left and right catalogs
		try {
			leftRoot = settings.getProperty("left_cat");
		} catch (Exception e) {
			leftRoot = (File.listRoots())[0].getAbsolutePath();
		}

		try {
			rightRoot = settings.getProperty("right_cat");
		} catch (Exception e) {
			rightRoot = (File.listRoots())[0].getAbsolutePath();
		}
		// setting left and right catalogs
		listeners.selectDrive(leftRoot, leftTree);
		listeners.selectDrive(rightRoot, rightTree);
		// setting
		if (scrollpane1 == null)
			scrollpane1 = new JScrollPane();
		if (scrollpane2 == null)
			scrollpane2 = new JScrollPane();

		scrollpane1.setViewportView(leftTree);
		scrollpane1.revalidate();
		scrollpane1.repaint();

		scrollpane2.setViewportView(rightTree);
		scrollpane2.revalidate();
		scrollpane2.repaint();

		frame.setSize((int) (width * size_coeff), (int) (height * size_coeff));
		frame.setLocation((int) (width * offset_coeff), (int) (height * offset_coeff));

		java.net.URL imgURL = this.getClass().getResource("/resources/maika.jpg");
		// ImageIcon icon = new ImageIcon(imgURL, "");
		// selectBorder = BorderFactory.createMatteBorder(20, 20, 20, 20, icon);
		nullBorder = BorderFactory.createMatteBorder(20, 20, 20, 20, Color.WHITE);

		scrollpane1.setBorder(nullBorder);
		scrollpane2.setBorder(nullBorder);
	}

	public void saveSettings() {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		settings.setProperty("width", String.valueOf((int) dim.width));
		settings.setProperty("height", String.valueOf((int) dim.height));
		File tmp1 = (File) leftTree.getModel().getRoot();
		File tmp2 = (File) rightTree.getModel().getRoot();
		settings.setProperty("left_cat", tmp1.getAbsolutePath());
		settings.setProperty("right_cat", tmp2.getAbsolutePath());
		try {

			FileOutputStream outStream = new FileOutputStream("settings.ini");
			settings.store(outStream, null);
			outStream.close();

		} catch (FileNotFoundException e) {
			System.out.println("can't find settings.ini to save settings");
		} catch (IOException e) {
			System.out.println("cannot write a settings file");
		}

	}

	/*
	 * 
	 * The getters and setters section
	 */

	public JTree getCurrentTree() {
		return currentTree;
	}

	public JPopupMenu getFileMenu() {
		return fileMenu;
	}

	public JLabel getInfoLabel() {
		return infoLabel;
	}

	public JTree getLeftTree() {
		return leftTree;
	}

	public Border getNullBorder() {
		return nullBorder;
	}

	public NutPad getNutPad() {
		return nutpad;
	}

	public JTree getRightTree() {
		return rightTree;
	}

	public JScrollPane getScrollpane1() {
		return scrollpane1;
	}

	public JScrollPane getScrollpane2() {
		return scrollpane2;
	}

	public File getSelectedFile() {
		return selectedFile;
	}

	public JPopupMenu getStandardMenu() {
		return standardMenu;
	}

	public Border getSelectBorder() {
		return selectBorder;
	}

	public void setCurrentTree(JTree currentTree) {
		this.currentTree = currentTree;
	}

	public void setInfoLabel(JLabel infoLabel) {
		this.infoLabel = infoLabel;
	}

	public void setInfoLabelText(String text) {
		this.infoLabel.setText(text);
	}

	public void setNutPad(NutPad nutpad) {
		this.nutpad = nutpad;
	}

	public void setSelectBorder(EmptyBorder selectBorder) {
		this.selectBorder = selectBorder;
	}

	public void setSelectedFile(File selectedFile) {
		this.selectedFile = selectedFile;
	}
}