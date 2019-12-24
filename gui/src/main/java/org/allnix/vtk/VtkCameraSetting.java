/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.allnix.vtk;

import vtk.vtkCamera;

/**
 *
 * @author ykyang
 */
public class VtkCameraSetting {
     private double[] position;
    private double[] focalPoint;
    private double[] viewUp;
    private double viewAngle;
    //private double[] viewPlaneNormal;
    private double[] clippingRange;
    private int parallelProjection;
    private double parallelScale;
    
    public double[] getPosition() {
        return position;
    }

    public void setPosition(double[] position) {
        this.position = position;
    }

    public double[] getFocalPoint() {
        return focalPoint;
    }

    public void setFocalPoint(double[] focalPoint) {
        this.focalPoint = focalPoint;
    }

    public double[] getViewUp() {
        return viewUp;
    }

    public void setViewUp(double[] viewUp) {
        this.viewUp = viewUp;
    }

    public double getViewAngle() {
        return viewAngle;
    }

    public void setViewAngle(double viewAngle) {
        this.viewAngle = viewAngle;
    }

    public double[] getClippingRange() {
        return clippingRange;
    }

    public void setClippingRange(double[] clippingRange) {
        this.clippingRange = clippingRange;
    }

    public int getParallelProjection() {
        return parallelProjection;
    }

    public void setParallelProjection(int parallelProjection) {
        this.parallelProjection = parallelProjection;
    }

    public double getParallelScale() {
        return parallelScale;
    }

    public void setParallelScale(double parallelScale) {
        this.parallelScale = parallelScale;
    }


    
    /**
     * Copy settings from camera to this settings
     */
    public void fromCameraToSettings(vtkCamera camera) {
        position = camera.GetPosition();
        focalPoint = camera.GetFocalPoint();
        viewUp = camera.GetViewUp();
        viewAngle = camera.GetViewAngle();
        //viewPlaneNormal = camera.GetViewPlaneNormal(); // not necessary???
        clippingRange = camera.GetClippingRange();
        parallelProjection = camera.GetParallelProjection();
        parallelScale = camera.GetParallelScale();
    }
   
    /**
     * Copy settings from this settings to camera
     */
    public void fromSettingsToCamera(vtkCamera camera) {
        camera.SetPosition(position);
        camera.SetFocalPoint(focalPoint);
        camera.SetViewUp(viewUp);
        camera.SetViewAngle(viewAngle);
        //camera.SetViewPlaneNormal: does not exist
        camera.SetClippingRange(clippingRange);
        camera.SetParallelProjection(parallelProjection);
        camera.SetParallelScale(parallelScale);
    }   
}
