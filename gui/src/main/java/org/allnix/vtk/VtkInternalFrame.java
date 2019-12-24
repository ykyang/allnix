package org.allnix.vtk;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JInternalFrame;
import javax.swing.SwingUtilities;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.allnix.gui.ImageSelection;


import vtk.vtkGenericRenderWindowInteractor;
import vtk.vtkInteractorStyleTrackballCamera;
import vtk.vtkLightKit;
import vtk.vtkPNGWriter;
import vtk.vtkRenderWindow;
import vtk.vtkRenderWindowPanel;
import vtk.vtkWindowToImageFilter;

public class VtkInternalFrame {
    static final private Logger logger = LoggerFactory.getLogger(VtkInternalFrame.class);
    
    private VtkRenderWindowPanelRenderer renderer;
    private vtkRenderWindowPanel renPanel;

    private JInternalFrame frame;
//    private KeyListener keyListener;
    private vtkLightKit lightKit;
 
    
    
    public VtkInternalFrame() {
        renPanel = new vtkRenderWindowPanel();
        vtkInteractorStyleTrackballCamera interactorStyle = new vtkInteractorStyleTrackballCamera(); 
        renPanel.setInteractorStyle(interactorStyle);
        renderer = new VtkRenderWindowPanelRenderer(renPanel);
        renderer.setupKeyListener();
        
        
        //> Setup lightkit
        //> https://public.kitware.com/pipermail/vtkusers/2001-March/006088.html
        vtkGenericRenderWindowInteractor interactor =  renPanel.getRenderWindowInteractor();
        interactor.LightFollowCameraOff();
        renderer.getRenderer().LightFollowCameraOn();
        renderer.getRenderer().RemoveAllLights();
        lightKit = new vtkLightKit();
        lightKit.AddLightsToRenderer(renderer.getRenderer());
   
        frame = new JInternalFrame("VTK 3D", true, false, true, true);
        // TODO: review the following 2 lines
        frame.getContentPane().add(renPanel, BorderLayout.CENTER);
        frame.setSize(800, 600);
        frame.add(renPanel);
        //frame.setLocationRelativeTo(null);
        
    }
    
    public VtkInternalFrame(vtkRenderWindowPanel renPanel) {
        //renPanel = new vtkRenderWindowPanel();
        vtkInteractorStyleTrackballCamera interactorStyle = new vtkInteractorStyleTrackballCamera(); 
        renPanel.setInteractorStyle(interactorStyle);
        renderer = new VtkRenderWindowPanelRenderer(renPanel);
        renderer.setupKeyListener();
        
        
        //> Setup lightkit
        //> https://public.kitware.com/pipermail/vtkusers/2001-March/006088.html
        vtkGenericRenderWindowInteractor interactor =  renPanel.getRenderWindowInteractor();
        interactor.LightFollowCameraOff();
        renderer.getRenderer().LightFollowCameraOn();
        renderer.getRenderer().RemoveAllLights();
        lightKit = new vtkLightKit();
        lightKit.AddLightsToRenderer(renderer.getRenderer());
   
        frame = new JInternalFrame("VTK 3D", true, false, true, true);
        // TODO: review the following 2 lines
        frame.getContentPane().add(renPanel, BorderLayout.CENTER);
        frame.setSize(800, 600);
        frame.add(renPanel); // bug???
        //frame.setLocationRelativeTo(null);
        
    }
    
    
    
    public void init() {
        // does nothing for now
    }
    
    /**
     * Not sure about this
     */
    public void dispose() {
        Runnable x = () -> {
            frame.removeAll();
            frame.dispose();
            frame = null;
            

            vtk.vtkJavaMemoryManager jom = vtk.vtkObject.JAVA_OBJECT_MANAGER;
            jom.gc(false);
        };
        
        SwingUtilities.invokeLater(x);
    }
    
   
    /**
     * @deprecated use getJInternalFrame()
     * @return
     */
    public JInternalFrame getInternalFrame() {
        return frame;
    }
    public JInternalFrame getJInternalFrame() {
        return frame;
    }
    
    public vtkLightKit getLightKit() {
        return this.lightKit;
    }
    
    /**
     * @deprecated use getVtkRenderer()
     * @return
     */
    public VtkRenderer getRenderer() {
        return renderer;
    }
    
    public VtkRenderWindowPanelRenderer getVtkRenderer() {
        return renderer;
    }
    public vtkRenderWindowPanel getVtkRenderWindowPanel() {
        return renPanel;
    }
    
    /**
     * Save current render window to a file in Event Dispatch Thread
     * 
     * @param file PNG
     */
    public void saveImageEdt(String file) {
        this.saveImageEdt(file, 1);
    }
    /**
     * Save image in Event Dispatch Thread
     */
    public void saveImageEdt(String file, int magnification) {
        Runnable x = () -> {
            try {
                this.saveImage(file, magnification);
            } catch (IOException e) {
                logger.error(ExceptionUtils.getStackTrace(e));
            }
        };
        renderer.invokeLater(x);
    }
    
    /**
     * Must call in EDT
     * 
     * https://stackoverflow.com/questions/30335787/take-snapshot-of-full-jframe-and-jframe-only
     *
     * 
     * @param file
     * @throws IOException 
     */
    public void saveImage(String file, int magnification) throws IOException {
        vtkRenderWindow renWin = this.renPanel.GetRenderWindow();

        // https://vtk.org/Wiki/VTK/Examples/Java/Miscellaneous/Screenshot
        vtkWindowToImageFilter winToImageFilter = new vtkWindowToImageFilter();
        winToImageFilter.SetInput(renWin);
        
        winToImageFilter.SetScale(magnification); 
        winToImageFilter.Update();

        vtkPNGWriter writer = new vtkPNGWriter();
        writer.SetFileName(file);
        writer.SetInputConnection(winToImageFilter.GetOutputPort());
        writer.Write();
    }

    /**
     * Copy image in Event Dispatch Thread
     */
    public void copyImageEdt() {
        this.copyImageEdt(1);
    }
    /**
     * Copy image in Event Dispatch Thread
     * 
     * @param magnification
     */
    public void copyImageEdt(int magnification) {
        Runnable x = () -> {
            try {
                this.copyImage(magnification);
            } catch (IOException e) {
                logger.error(ExceptionUtils.getStackTrace(e));
            }
        };
        renderer.invokeLater(x);
    }
    /**
     * Copy image
     * 
     * @param magnification
     * @throws IOException
     */
    public void copyImage(int magnification) throws IOException {
        // Crashes VTK
        Path tmpPath = Files.createTempFile("VTK", ".png");
        File tmpFile = tmpPath.toFile();
        
        try {
            this.saveImage(tmpFile.getAbsolutePath(), magnification);
            BufferedImage img = ImageIO.read(tmpFile);
            ImageSelection selection = new ImageSelection(img);
            Toolkit.getDefaultToolkit().getSystemClipboard()
                .setContents(selection, null);
        } catch(Exception e) {
            FileUtils.deleteQuietly(tmpFile);
        }
    }
    
    
    /**
     * Copy current content to system clipboard
     * @throws IOException 
     */
    public void copyImage() throws IOException {
        this.copyImage(1);
    }
    
    /**
     * Set the title of the window
     * 
     * @param title
     */
    public void setTitle(String title) {
        Runnable x = ()->{
            frame.setTitle(title);
        };
        renderer.invokeLater(x);
    }
    
    public void setVisible(boolean value) {
        Runnable x = ()->{
            frame.setVisible(value);
        };
        renderer.invokeLater(x);      
    }
}
