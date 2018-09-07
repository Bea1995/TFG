package tfg.main.grafica;
import ac.essex.graphing.plotting.Graph; 
import ac.essex.graphing.plotting.PlotSettings; 
 
import ac.essex.graphing.swing.SettingsUpdateListener;

import javax.swing.*; 
import javax.swing.filechooser.FileFilter; 

import java.awt.*;  
import java.awt.event.ActionListener; 
import java.awt.event.ActionEvent; 
import java.io.File; 
import java.io.IOException; 
import java.util.Vector;
  
@SuppressWarnings("serial")
/** 
 * @author Olly Oechsle, University of Essex 
 * @see ac.essex.graphing.plotting.PlotSettings 
 * @version 1.1 
 */ 
public class GraphApplication2 extends JFrame implements ActionListener, SettingsUpdateListener { 
 
    protected JButton save, exit, update; 
    protected JTextField minX, minY, maxX, maxY; 
 
    protected GraphPanel2 graphPanel; 
 
    public GraphApplication2(Graph2 graph) { 
    	
    	Vector<Plotter2> funciones=graph.functions;
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT)); 
 
        save = new JButton("Save"); 
        save.addActionListener(this); 
 
    	for(int i=0; i<funciones.size(); i++){
    		if(!funciones.get(i).getName().equals("Trozo")){
    			JLabel aux=new JLabel(funciones.get(i).getName());
    			aux.setForeground(graph.colores[i%graph.colores.length]);
    			toolbar.add(aux);
    		}
    	}
 
        minX = new JTextField(String.valueOf((int) graph.plotSettings.getMinX())); 
        minX.setPreferredSize(new Dimension(50,20));
        minX.setMinimumSize(minX.getPreferredSize());
        minY = new JTextField(String.valueOf((int) graph.plotSettings.getMinY())); 
        minY.setPreferredSize(new Dimension(50,20));
        minY.setMinimumSize(minY.getPreferredSize());
        maxX = new JTextField(String.valueOf((int) graph.plotSettings.getMaxX()));
        maxX.setPreferredSize(new Dimension(50,20));
        maxX.setMinimumSize(maxX.getPreferredSize());
        maxY = new JTextField(String.valueOf((int) graph.plotSettings.getMaxY())); 
        maxY.setPreferredSize(new Dimension(50,20));
        maxY.setMinimumSize(maxY.getPreferredSize());
 
        toolbar.add(new JLabel("X: ")); 
        toolbar.add(minX); 
        toolbar.add(new JLabel("-")); 
        toolbar.add(maxX); 
        toolbar.add(new JLabel(", Y:")); 
        toolbar.add(minY); 
        toolbar.add(new JLabel("-")); 
        toolbar.add(maxY); 
 
        update = new JButton("Update"); 
        update.addActionListener(this); 
        toolbar.add(update); 
 
        toolbar.add(save); 
 
        //Add the panel to the middle of the BorderLayout, it will fill the window. 
        graphPanel = new InteractiveGraphPanel2(this); 
 
        //Add the toolbar and graph to the frame 
        Container c = getContentPane();
        c.add(graphPanel, BorderLayout.CENTER); 
        c.add(toolbar, BorderLayout.SOUTH); 
 
        //Default size of the window, the Graph Panel will be slightly smaller. 
        setSize(700, 500);       
 
        //Window title 
        setTitle(Graph.VERSION); 
 
        //Show the Window 
        setVisible(true); 
 
        graphPanel.setGraph(graph); 
 
    } 
 
    public void graphUpdated(PlotSettings settings) { 
        minX.setText(String.valueOf((int) settings.getMinX())); 
        minY.setText(String.valueOf((int) settings.getMinY())); 
        maxX.setText(String.valueOf((int) settings.getMaxX())); 
        maxY.setText(String.valueOf((int) settings.getMaxY())); 
    } 
 
    public void actionPerformed(ActionEvent e) { 
 
        if (e.getSource() == update) { 
            Graph2 g = graphPanel.getGraph(); 
            g.plotSettings.setMinX(Double.parseDouble(minX.getText())); 
            g.plotSettings.setMaxX(Double.parseDouble(maxX.getText())); 
            g.plotSettings.setMinY(Double.parseDouble(minY.getText())); 
            g.plotSettings.setMaxY(Double.parseDouble(maxY.getText())); 
            graphPanel.repaint(); 
        } 
 
        //Saves an image of the graph to disk. 
        if (e.getSource() == save) { 
            JFileChooser filechooser = new JFileChooser(System.getProperty("user.home")); 
            filechooser.setDialogTitle("Save Graph Image"); 
            filechooser.setSelectedFile(new File(filechooser.getCurrentDirectory(), "graph.png")); 
            filechooser.setFileFilter(new FileFilter() { 
                public boolean accept(File f) { 
                    String extension = f.getName().substring(f.getName().lastIndexOf('.') + 1).toLowerCase(); 
                    if (f.isDirectory()) return true; 
                    if (extension.equals("bmp")) return true; 
                    if (extension.equals("jpg")) return true; 
                    if (extension.equals("png")) return true; 
                    return false; 
                } 
 
                public String getDescription() { 
                    return "Image Files: jpg, png, bmp"; 
                } 
            }); 
            int action = filechooser.showSaveDialog(this); 
            if (action == JFileChooser.APPROVE_OPTION) { 
                File f = filechooser.getSelectedFile(); 
                try { 
                    String extension = f.getName().substring(f.getName().lastIndexOf(".") + 1); 
                    javax.imageio.ImageIO.write(graphPanel.getImage(), extension, f); 
                } catch (IOException err) { 
                    JOptionPane.showMessageDialog(this, "Could not save image: " + err.getMessage()); 
                } 
            } 
        } 
 
    } 
 
} 