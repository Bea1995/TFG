package tfg.main.grafica;
 
import ac.essex.graphing.plotting.PlotSettings;

import java.awt.*; 
import java.awt.image.BufferedImage; 
import java.util.Vector; 
 
/** 
 * Graph.java<br /> 
 * Renders graphs using the Java 2D API 
 *
 * All customisable settings are defined in PlotSettings.java 
 * 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version, 
 * provided that any use properly credits the author. 
 * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details at http://www.gnu.org 
 * 
 * @author Olly Oechsle, University of Essex 
 * @see ac.essex.graphing.plotting.PlotSettings 
 * @version 1.1 
 */ 
 
public class Graph2 { 
 
    public static final String VERSION = "Java Plot 1.1"; 
    
    public Color colores[]={Color.BLUE, Color.RED, Color.GREEN, Color.ORANGE, 
    		Color.CYAN, Color.MAGENTA, Color.GRAY, Color.PINK, Color.YELLOW};
 
    /*A graph may plot as many functions as it wants 
     *These may all be of different types. */ 
    public Vector<Plotter2> functions; 
 
    /*The area and general settings of the graph are all defined 
     * by a PlotArea object. */ 
    public PlotSettings plotSettings; 
 
    //Initialises the graph with the plot settings to use.  
    public Graph2(PlotSettings p) { 
        this.functions = new Vector<Plotter2>(5); 
        this.plotSettings = p; 
    } 
 
    protected double plotRangeX, plotRangeY; 
 
    /*How many pixels are there available to use in the graph? 
     *This is the size of the image minus the border size. */ 
    protected int chartWidth, chartHeight; 
 
    protected double unitsPerPixelX, unitsPerPixelY; 
 
    /*Draws the graph using a graphics object.  
     *Note, X axis labels come from the first function (this only applies to discrete functions) 
     * @param g      The graphics context on which to draw 
     * @param width  The width to make the graph 
     * @param height The height to make the graph 
     */ 
    public void draw(Graphics g, int width, int height) { 
 
        //Draw the title  
        if (plotSettings.getTitle() != null) { 
            g.setColor(plotSettings.getFontColor()); 
            //Ensure the border top can accommodate the title 
            if (plotSettings.getMarginTop() < g.getFontMetrics().getHeight() + 20) { 
                plotSettings.setMarginTop(g.getFontMetrics().getHeight() + 20); 
            } 
            int titleXPosition = (width / 2) - ((g.getFontMetrics().stringWidth(plotSettings.getTitle())) / 2); 
            g.drawString(plotSettings.getTitle(), titleXPosition, 10 + g.getFontMetrics().getHeight()); 
        } 
 
        //Calculate the plot range  
        plotRangeX = Math.abs(plotSettings.getMaxX() - plotSettings.getMinX()); 
        plotRangeY = Math.abs(plotSettings.getMaxY() - plotSettings.getMinY()); 
 
        /*First we need to know how many pixels there are across the panel 
         *And we can divide that number between the range that we've been assigned. */ 
        chartWidth = width - (plotSettings.getMarginLeft() + plotSettings.getMarginRight()); 
        chartHeight = height - (plotSettings.getMarginTop() + plotSettings.getMarginBottom()); 
 
        //Calculate the number of units per pixel 
        unitsPerPixelX = plotRangeX / chartWidth; 
        unitsPerPixelY = plotRangeY / chartHeight; 
 
        //Set the background colour 
        g.setColor(plotSettings.getBackgroundColor()); 
        g.fillRect(plotSettings.getMarginLeft(), plotSettings.getMarginTop(), chartWidth - 1, chartHeight - 1); 
 
        int columnIndex = 0; 
 
        //Draw X Axis Notches 
        double firstGridXLocation = ((int) (plotSettings.getMinX() / plotSettings.getGridSpacingX())) * plotSettings.getGridSpacingX(); 
 
        for (double px = firstGridXLocation; px <= plotSettings.getMaxX(); px += plotSettings.getGridSpacingX()) { 
 
            if (px < plotSettings.getMinX()) continue; 
 
            //Find the position of each point and draw a line 
            int plotX = getPlotX(px); 
 
            int plotY = plotSettings.getMarginTop() + chartHeight; 
 
            //Vertical grid lines 
            if (plotSettings.isVerticalGridVisible()) { 
                g.setColor(plotSettings.getGridColor()); 
                g.drawLine(plotX, plotSettings.getMarginTop(), plotX, plotY); 
            } 
 
            //And draw a notch on the X axis. 
            g.setColor(plotSettings.getAxisColor()); 
            g.drawLine(plotX, plotY, plotX, plotY + plotSettings.getNotchLength()); 
 
            //Work out the value at this point and draw 
            String value; 
            int labelXPosition; 
 
            //Note: X Axis labels come from the first function 
            Plotter2 function = functions.elementAt(0); 
 
            if (function instanceof DiscreteFunctionPlotter2) { 
 
                DiscreteFunctionPlotter2 discrete = (DiscreteFunctionPlotter2) function; 
                value = discrete.getLabel(columnIndex); 
 
                int columnWidth = chartWidth / discrete.getColumnCount(); 
                int columnCenterX = (columnIndex * columnWidth) + (columnWidth / 2); 
 
                labelXPosition = columnCenterX - ((g.getFontMetrics().stringWidth(value)) / 2) + plotSettings.getMarginLeft(); 
 
            } else { 
                value = plotSettings.getNumberFormatter().format(px); 
                labelXPosition = plotX - (g.getFontMetrics().stringWidth(value)) / 2; 
            } 
 
            //Draw the value underneath the notch 
            g.setColor(plotSettings.getFontColor()); 
            g.drawString(value, labelXPosition, plotY + plotSettings.getNotchLength() + g.getFontMetrics().getHeight() - 1 + plotSettings.getNotchGap()); 
 
            columnIndex++; 
 
        } 
 
        //Draw Y Axis Notches and Labels  
        double firstGridYLocation = ((int) (plotSettings.getMinY() / plotSettings.getGridSpacingY())) * plotSettings.getGridSpacingY(); 
 
        for (double py = firstGridYLocation; py <= plotSettings.getMaxY(); py += plotSettings.getGridSpacingY()) { 
 
            if (py < plotSettings.getMinY()) continue; 
 
            //Find the position of each point and draw a line 
            int plotX = plotSettings.getMarginLeft(); 
 
            int plotY = getPlotY(py); 
 
            //Horizontal gridColor lines 
            if (plotSettings.isHorizontalGridVisible()) { 
                g.setColor(plotSettings.getGridColor()); 
                g.drawLine(plotSettings.getMarginLeft(), plotY, plotSettings.getMarginLeft() + chartWidth - 1, plotY); 
            } 
 
            //Draw a notch on the Y axis 
            g.setColor(plotSettings.getAxisColor()); 
            g.drawLine(plotX, plotY, plotX - plotSettings.getNotchLength(), plotY); 
 
            //Work out the value at this point and draw 
            String value = plotSettings.getNumberFormatter().format(py); 
 
            //Work out how wide this string is 
            int textXOffset = (g.getFontMetrics().stringWidth(value)); 
 
            g.setColor(plotSettings.getFontColor()); 
            g.drawString(value, plotX - plotSettings.getNotchLength() - textXOffset - plotSettings.getNotchGap(), plotY + (g.getFontMetrics().getHeight() / 2) - 1); 
 
        } 
 
        //Draw a box around the whole graph to delimit the Axes  
        g.setColor(plotSettings.getAxisColor()); 
        g.drawRect(plotSettings.getMarginLeft(), plotSettings.getMarginTop(), chartWidth, chartHeight); 
 
        //Draw the horizontal and vertical axes that go through the point at 0,0. 
        int yEqualsZero = getPlotY(0) + 0; 
        if (0 > plotSettings.getMinY() && 0 < plotSettings.getMaxY()) 
            g.drawLine(plotSettings.getMarginLeft(), yEqualsZero, plotSettings.getMarginLeft() + chartWidth - 1, yEqualsZero); 
 
        int xEqualsZero = getPlotX(0) + 0; 
        if (0 > plotSettings.getMinX() && 0 < plotSettings.getMaxX()) 
            g.drawLine(xEqualsZero, plotSettings.getMarginTop(), xEqualsZero, plotSettings.getMarginTop() + chartHeight); 
 
        //And finally - draw the results of the function onto the chart. 
        for (int i = 0; i <functions.size(); i++) { 
            Plotter2 function = functions.elementAt(i);
            g.setColor(colores[i%colores.length]); 
            if(functions.elementAt(i).getName().equals("Trozo")) g.setColor(Color.BLACK);
            function.plot(this, g, chartWidth, chartHeight); 
        } 
 
    } 
 
    /*Uses the numeric value of Y (as returned by a function) and 
     *figures out which pixel on screen this relates to. */ 
    public int getPlotY(double y) { 
 
    	//Convert Y into pixel coordinates again  
        int pixelY = ((int) ((y - plotSettings.getMinY()) / unitsPerPixelY)); 
 
        /*We also need to flip the Y axis because Y is counted from the top 
         *and not the bottom. Add the various borders */ 
        return ((chartHeight - pixelY) + plotSettings.getMarginTop()); 
 
    } 
 
    /*Uses the numeric value of X, and figures out which pixel on the screen 
     *this relates to. */ 
    public int getPlotX(double x) { 
        return (int) (((x - plotSettings.getMinX()) / unitsPerPixelX) + plotSettings.getMarginLeft()); 
    } 
 
    //Takes a numeric distance and calculates how many actual pixels high that is.  
    public double getActualHeight(double height) { 
        return height / unitsPerPixelY; 
    } 
 
    //Takes a numeric distance and calculates how many actual pixels wide that is.  
    public double getActualWidth(double width) { 
        return width / unitsPerPixelX; 
    } 
 
    /*Takes a set number of actual pixels on the screen (in the Y direction) 
     *And returns how long they are, if plotted on the graph. */ 
    public double getPlotHeight(double height) { 
        return height * unitsPerPixelY; 
    } 
 
    /*Takes a set number of actual pixels on the screen (in the X direction) 
     *And returns how long they are, if plotted on the graph. */ 
    public double getPlotWidth(double width) { 
        return width * unitsPerPixelX; 
    } 
 
    public double getActualX(int pixelX) { 
        return plotSettings.getMinX() + (pixelX * unitsPerPixelX); 
    } 
 
    /*Plots a line between two sets of values. 
     * @param g  Graphics context upon which to write 
     * @param x1 First point X 
     * @param y1 First point Y 
     * @param x2 Second point X 
     * @param y2 Second point Y 
     */ 
    public void drawLine(Graphics g, double x1, double y1, double x2, double y2) { 
        g.drawLine(getPlotX(x1), getPlotY(y1), getPlotX(x2), getPlotY(y2)); 
    } 
 
    /*Draws a bar  
     * @param g            Graphics context upon which to write 
     * @param totalColumns How many columns are there in total? 
     * @param columnIndex  The index of the column, starting at zero (determines which bar to draw) 
     * @param height       How high should the bar be 
     * @param fill         What colour should the bar be? 
     */ 
    public void drawBar(Graphics g, double columnWidth, int columnIndex, double height, Color fill) { 
 
        //The gap on each side of the column  
        final double hgap = 0.1; 
 
        //The actual height of the bar  
        int barHeight = (int) getActualHeight(height); 
 
        //The Y position at the top of the bar.  
        int maxPlotY = getPlotY(height); 
 
        //Where to start drawing the bar in the X direction 
        int columnStartX = getPlotX(columnIndex * columnWidth); 
 
        int gap = (int) +(getActualWidth(columnWidth) * hgap); 
 
        //Draw the bar: 
        g.setColor(fill); 
 
        g.fillRect(columnStartX + gap, maxPlotY, (int) getActualWidth(columnWidth) - (gap * 2), barHeight); 
 
        g.setColor(Color.BLACK); 
 
        g.drawRect(columnStartX + gap, maxPlotY, (int) getActualWidth(columnWidth) - (gap * 2), barHeight); 
 
    } 
 
 
    /*Draws a candlestick plot, which I've chosen to define as a set of three values: high, mean and low. 
     * @param g           The graphics context upon which to draw 
     * @param columnWidth How many units wide is a column? 
     * @param columnIndex The index of the column, starting at zero (determines which bar to draw) 
     * @param high        The highest value 
     * @param mean        The average value 
     * @param low         The lowest value 
     */ 
    public void drawCandleStick(Graphics g, double columnWidth, int columnIndex, double high, double mean, double low, Color lineColor, Color backgroundColor) { 
 
        //Find out how much room there is for each "column"  
        int halfColumnWidth = (int) getActualWidth(columnWidth / 2); 
 
        final int bigNotchWidth = halfColumnWidth / 2; 
        final int smallNotchWidth = halfColumnWidth / 3; 
 
        //Where to start drawing the candlestick in the X direction  
        int columnX = getPlotX(columnIndex * columnWidth) + halfColumnWidth; 
 
        //Get screen coordinates 
        int maxPlotY = getPlotY(high); 
        int meanPlotY = getPlotY(mean); 
        int minPlotY = getPlotY(low); 
 
        //The vertical line 
 
        if (backgroundColor != null) { 
            g.setColor(backgroundColor); 
            g.fillRect(columnX - smallNotchWidth, maxPlotY, smallNotchWidth * 2, (int) getActualHeight(high - low) + 1); 
        } 
 
        g.setColor(lineColor); 
        g.drawLine(columnX, minPlotY, columnX, maxPlotY); 
 
        //Notch at top for the high value 
        g.drawLine(columnX - bigNotchWidth, minPlotY, columnX + bigNotchWidth, minPlotY); 
 
        //Notch at the middle for the mean value 
        g.drawLine(columnX - smallNotchWidth, meanPlotY, columnX + smallNotchWidth, meanPlotY); 
 
        //Notch at the bottom for the low value 
        g.drawLine(columnX - bigNotchWidth, maxPlotY, columnX + bigNotchWidth, maxPlotY); 
    } 
 
    //Returns the graph as an image so that it can be saved.  
    public BufferedImage getImage(int width, int height) { 
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); 
        Graphics g = image.getGraphics(); 
        g.setColor(plotSettings.getBackgroundColor()); 
        g.fillRect(0, 0, width, height); 
        draw(g, width, height); 
        return image; 
    } 
 
}
