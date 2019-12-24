package org.allnix.vtk.prop;

import org.allnix.vtk.VtkRenderer;

import vtk.vtkBoxRepresentation;
import vtk.vtkBoxWidget2;
import vtk.vtkPoints;
import vtk.vtkPolyData;
import vtk.vtkProperty;
import vtk.vtkTransform;

public class BoxWidget2Prop {
    private VtkRenderer renderer; // not used right now
    private vtkBoxWidget2 boxWidget;
    
    public BoxWidget2Prop() {
        boxWidget = new vtkBoxWidget2();
        boxWidget.SetRotationEnabled(0);
        vtkBoxRepresentation rep = this.getBoxRepresentation();
        rep.SetPlaceFactor(1); // Default is 0.5
    }
    
    public void setVtkRenderer(VtkRenderer renderer) {
        this.renderer = renderer;
        boxWidget.SetInteractor(renderer.getRenderWindowInteractor());
        
    }
    
    public void placeWidget(double[] bounds) {
        vtkBoxRepresentation rep = this.getBoxRepresentation();
        rep.PlaceWidget(bounds);
    }
    
    public void setOutlineColor(double[] rgb) {
        vtkBoxRepresentation rep = this.getBoxRepresentation();
        vtkProperty property = rep.GetOutlineProperty();
        property.SetColor(rgb);
    }
    public void translate(double[] xyz) {
        vtkBoxRepresentation rep = this.getBoxRepresentation();
        vtkTransform trans = new vtkTransform();
        rep.GetTransform(trans);
        trans.PostMultiply();
        trans.Translate(xyz);
        rep.SetTransform(trans);
    }
    
    /**
     * 
     * @param theta Degree of rotation
     */
    public void rotateX(double theta) {
        vtkBoxRepresentation rep = this.getBoxRepresentation();
        vtkTransform trans = new vtkTransform(); 
        rep.GetTransform(trans);
        trans.PostMultiply();
        trans.RotateX(theta);
        rep.SetTransform(trans);
    }
    
    public void rotateY(double theta) {
        vtkBoxRepresentation rep = this.getBoxRepresentation();
        vtkTransform trans = new vtkTransform(); 
        rep.GetTransform(trans);
        trans.PostMultiply();
        trans.RotateY(theta);
        rep.SetTransform(trans);
    }
    
    /**
     * Rotate by right-hand rule (counter clockwise) degrees about the 
     * z-axis
     * 
     * @param theta counter clockwise in degrees
     */
    public void rotateZ(double theta) {
        vtkBoxRepresentation rep = this.getBoxRepresentation();
        vtkTransform trans = new vtkTransform(); 
        rep.GetTransform(trans);
        trans.PostMultiply();
        trans.RotateZ(theta);
        rep.SetTransform(trans);
    }
    
    public void setTransformation(vtkTransform trans) {
        vtkBoxRepresentation rep = this.getBoxRepresentation();
        rep.SetTransform(trans);
    }
    
    public void scale(double[] xyz) {
        vtkBoxRepresentation rep = this.getBoxRepresentation();
        vtkTransform trans = new vtkTransform(); 
        rep.GetTransform(trans);
        trans.PostMultiply();
        trans.Scale(xyz);
        rep.SetTransform(trans);
    }
    
    /**
     * v = x[8], y[8], z[8]
     * Points follow VTK order, that is counter-clockwise and bottom layer then
     * top layer.
     * @return array of size 24, the first 8 values are x of each points then y,
     * then z
     */
    public double[] getBoxPoints() {
        vtkBoxRepresentation rep = this.getBoxRepresentation();
        vtkPolyData polyData = new vtkPolyData();
        rep.GetPolyData(polyData);
        //> First 8 points consitutes the box
        //> follow hexahedron order
        vtkPoints points = polyData.GetPoints();
        //System.out.println(points.toString());
        int pointCount = 8; // points.GetNumberOfPoints()
        double[] v = new double[3*pointCount];
        for (int ind = 0; ind < pointCount; ind++) {
            double[] xyz = points.GetPoint(ind);
            v[8*0 + ind] = xyz[0];
            v[8*1 + ind] = xyz[1];
            v[8*2 + ind] = xyz[2];
//            System.out.println(i + " " + xyz[0] + " " + xyz[1] + " " + xyz[2]);
        }
        
        return v;
    }
    
    private vtkBoxRepresentation getBoxRepresentation() {
        vtkBoxRepresentation rep = (vtkBoxRepresentation) boxWidget.GetRepresentation();
        return rep;
    }
    
    public void on() {
        Runnable x = () -> {
            boxWidget.On();
        };
        
        renderer.invokeLater(x);
    }
    public void off() {
        Runnable x = () -> {
            boxWidget.Off();
        };
        
        renderer.invokeLater(x);
    }
    
}
