package presenter;

import javafx.geometry.Point3D;
import javafx.scene.input.MouseButton;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import view.MainView;
import view.ProteinView;
import view.SequenceView;

/**
 * Created by Caspar on 23.12.2016.
 */
public class Presenter{

    final double SCALE_DELTA = 1.1;
    private double downX;
    private double downY;
    private double zPane = 0;


    MainView view;

    public Presenter (MainView mainView) {

        this.view = mainView;
        setupRotateAndMove();
        setupZoom();
        setupMouseHover();
        createButtonEvents();
    }

    public void setupRotateAndMove() {
        view.viewScene.setOnMousePressed((me) -> {
            downX = me.getSceneX();
            downY = me.getSceneY();
        });

        // Setup Rotate on left mouse button
        view.setOnMouseDragged((me) ->{
            if (me.getButton() == MouseButton.PRIMARY) {
                System.out.println("Mouse dragged left");
                double deltaX = downX-me.getSceneX();
                double deltaY = downY-me.getSceneY();

                Point3D directionVector = new Point3D(deltaX, deltaY, zPane).crossProduct(0, 0, -1);
                Rotate rotate = new Rotate(1.5, directionVector);

                view.woldTransformProperty.setValue(rotate.createConcatenation(view.woldTransformProperty.getValue()));

                downX=me.getSceneX();
                downY=me.getSceneY();
            } else

            // Setup Drag Move on right mouse button
            if (me.getButton() == MouseButton.SECONDARY) {
                System.out.println("Mouse dragged right");
                double deltaX = downX-me.getSceneX();
                double deltaY = downY-me.getSceneY();

                Translate translate = new Translate(-deltaX, -deltaY, 0);

                view.woldTransformProperty.setValue(translate.createConcatenation(view.woldTransformProperty.getValue()));

                downX=me.getSceneX();
                downY=me.getSceneY();
            }

        });
    }

    // Zoom in Protein Scene
    public void setupZoom() {
        view.viewScene.setOnScroll((se) -> {
            System.out.println("Mouse scrolled");
            if (se.getDeltaY() == 0) {
                return;
            }

            double scaleFactor = (se.getDeltaY() > 0) ? SCALE_DELTA : 1/ SCALE_DELTA;
            Scale scale = new Scale(scaleFactor, scaleFactor, scaleFactor);
            view.woldTransformProperty.setValue(scale.createConcatenation(view.woldTransformProperty.getValue()));
        });
    }

    // Hovering Mouse over Atoms displays additional information
    public void setupMouseHover(){
        view.bottomPane.setOnMouseEntered(me -> {
            System.out.println("HOVERING OVER SOMETHING");
        });
    }

    // Button events in Main View
    private void createButtonEvents(){
        // Open File
        view.openFile.setOnAction(e ->{
            view.createNewViewFromPDB();
        });

        // Increase and Decrease Bond and Atom radius
        view.increaseAtomSize.setOnAction(e -> view.getProteinView().increaseAtomRadius());
        view.decreaseAtomSize.setOnAction(e -> view.getProteinView().decreaseAtomRadius());
        view.increaseBondSize.setOnAction(e -> view.getProteinView().increaseBondRadius());
        view.decreaseBondSize.setOnAction(e -> view.getProteinView().decreaseBondRadius());

        // Show bonds and atoms
        view.showAtoms.selectedProperty().set(true);
        view.showBonds.selectedProperty().set(true);
        //view.showAtoms.setOnAction(e -> view.getProteinView().showAtoms(view.showAtoms.selectedProperty().get()));
        //view.showBonds.setOnAction(e -> view.getProteinView().showBonds(view.showBonds.selectedProperty().get()));
    }
}
