
package jmri.jmrit.operations.locations;

import java.awt.GraphicsEnvironment;
import java.text.MessageFormat;
import jmri.InstanceManager;
import jmri.jmrit.operations.OperationsSwingTestCase;
import jmri.jmrit.operations.routes.Route;
import jmri.jmrit.operations.routes.RouteLocation;
import jmri.jmrit.operations.routes.RouteManager;
import jmri.jmrit.operations.trains.Train;
import jmri.jmrit.operations.trains.TrainManager;
import jmri.util.JUnitUtil;
import jmri.util.JmriJFrame;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the Operations Locations GUI class
 *
 * @author Dan Boudreau Copyright (C) 2009
 */
public class SidingEditFrameTest extends OperationsSwingTestCase {

    final static int ALL = Track.EAST + Track.WEST + Track.NORTH + Track.SOUTH;
    private LocationManager lManager = null;
    private Location l = null;
    private Train trainA = null;

    @Test
    public void testAddSidingDefaults() {
        if (GraphicsEnvironment.isHeadless()) {
            return; // can't use Assume in TestCase subclasses
        }
        LocationManager lManager = InstanceManager.getDefault(LocationManager.class);
        Location l = lManager.getLocationByName("Test Loc C");
        SpurEditFrame f = new SpurEditFrame();
        f.setTitle("Test Siding Add Frame");
        f.setLocation(0, 0); // entire panel must be visible for tests to work properly
        f.initComponents(l, null);

        // create one siding tracks
        f.trackNameTextField.setText("new siding track");
        f.trackLengthTextField.setText("1223");
        enterClickAndLeave(f.addTrackButton);

        Track t = l.getTrackByName("new siding track", null);
        Assert.assertNotNull("new siding track", t);
        Assert.assertEquals("siding track length", 1223, t.getLength());
        // check that the defaults are correct
        Assert.assertEquals("all directions", ALL, t.getTrainDirections());
        Assert.assertEquals("all roads", Track.ALL_ROADS, t.getRoadOption());

        // create a second siding
        f.trackNameTextField.setText("2nd siding track");
        f.trackLengthTextField.setText("9999");
        enterClickAndLeave(f.addTrackButton);

        t = l.getTrackByName("2nd siding track", null);
        Assert.assertNotNull("2nd siding track", t);
        Assert.assertEquals("2nd siding track length", 9999, t.getLength());
        // check that the defaults are correct
        Assert.assertEquals("all directions", ALL, t.getTrainDirections());
        Assert.assertEquals("all roads", Track.ALL_ROADS, t.getRoadOption());

        // test error, try to create track with same name
        enterClickAndLeave(f.addTrackButton);

        // error dialogue should have appeared
        pressDialogButton(f, MessageFormat.format(Bundle
                .getMessage("CanNotTrack"),
                new Object[]{Bundle
                        .getMessage("add")}),
                Bundle.getMessage("ButtonOK"));

        // kill all frames
        JUnitUtil.dispose(f);
    }

    @Test
    public void testSetDirectionUsingCheckbox() {
        if (GraphicsEnvironment.isHeadless()) {
            return; // can't use Assume in TestCase subclasses
        }
        SpurEditFrame f = new SpurEditFrame();
        f.setTitle("Test Siding Add Frame");
        f.setLocation(0, 0); // entire panel must be visible for tests to work properly
        f.initComponents(l, null);

        f.trackNameTextField.setText("3rd siding track");
        f.trackLengthTextField.setText("1010");
        enterClickAndLeave(f.addTrackButton);

        Track t = l.getTrackByName("3rd siding track", null);
        Assert.assertNotNull("3rd siding track", t);
        Assert.assertEquals("3rd siding track length", 1010, t.getLength());
        Assert.assertEquals("Direction All before change", ALL, t.getTrainDirections());

        // deselect east, west and north check boxes
        enterClickAndLeave(f.eastCheckBox);
        enterClickAndLeave(f.westCheckBox);
        enterClickAndLeave(f.northCheckBox);

        enterClickAndLeave(f.saveTrackButton);

        Assert.assertEquals("only south", Track.SOUTH, t.getTrainDirections());

        // kill all frames
        JUnitUtil.dispose(f);
    }

    @Test
    public void testAddScheduleButton() {
        if (GraphicsEnvironment.isHeadless()) {
            return; // can't use Assume in TestCase subclasses
        }
        SpurEditFrame f = new SpurEditFrame();
        f.setTitle("Test Siding Add Frame");
        f.setLocation(0, 0); // entire panel must be visible for tests to work properly
        f.initComponents(l, null);

        f.trackNameTextField.setText("3rd siding track");
        f.trackLengthTextField.setText("1010");
        enterClickAndLeave(f.addTrackButton);

        // create the schedule edit frame
        enterClickAndLeave(f.editScheduleButton);

        // confirm schedule add frame creation
        JmriJFrame sef = JmriJFrame.getFrame(Bundle.getMessage("TitleScheduleAdd", "3rd siding track"));
        Assert.assertNotNull(sef);

        // kill all frames
        JUnitUtil.dispose(f);
        JUnitUtil.dispose(sef);
    }

    @Test
    public void testAddCloseAndRestore() {
        if (GraphicsEnvironment.isHeadless()) {
            return; // can't use Assume in TestCase subclasses
        }
        SpurEditFrame f = new SpurEditFrame();
        f.setTitle("Test Siding Add Frame");
        f.setLocation(0, 0); // entire panel must be visible for tests to work properly
        f.initComponents(l, null);

        // create three siding tracks
        f.trackNameTextField.setText("new siding track");
        f.trackLengthTextField.setText("1223");
        enterClickAndLeave(f.addTrackButton);

        f.trackNameTextField.setText("2nd siding track");
        f.trackLengthTextField.setText("9999");
        enterClickAndLeave(f.addTrackButton);

        f.trackNameTextField.setText("3rd siding track");
        f.trackLengthTextField.setText("1010");
        enterClickAndLeave(f.addTrackButton);

        // deselect east, west and north check boxes
        enterClickAndLeave(f.eastCheckBox);
        enterClickAndLeave(f.westCheckBox);
        enterClickAndLeave(f.northCheckBox);

        enterClickAndLeave(f.saveTrackButton);

        // create the schedule edit frame
        enterClickAndLeave(f.editScheduleButton);

        // confirm schedule add frame creation
        JmriJFrame sef = JmriJFrame.getFrame(Bundle.getMessage("TitleScheduleAdd", "3rd siding track"));
        Assert.assertNotNull(sef);

        // kill all frames
        JUnitUtil.dispose(f);
        JUnitUtil.dispose(sef);

        // now reload
        Location l2 = lManager.getLocationByName("Test Loc C");
        Assert.assertNotNull("Location Test Loc C", l2);

        LocationEditFrame fl = new LocationEditFrame(l2);
        fl.setTitle("Test Edit Location Frame");

        // check location name
        Assert.assertEquals("name", "Test Loc C", fl.locationNameTextField.getText());

        Assert.assertEquals("number of sidings", 3, fl.spurModel.getRowCount());
        Assert.assertEquals("number of staging tracks", 0, fl.stagingModel.getRowCount());

        JUnitUtil.dispose(fl);
    }

    @Test
    public void testTrainServicesTrack() {
        if (GraphicsEnvironment.isHeadless()) {
            return; // can't use Assume in TestCase subclasses
        }
        SpurEditFrame f = new SpurEditFrame();
        f.setTitle("Test Spur Frame");
        f.setLocation(0, 0); // entire panel must be visible for tests to work properly    
        f.initComponents(l, null);
        f.setSize(650, 800); // need to see save button

        // create track
        f.trackNameTextField.setText("Train test siding track");
        f.trackLengthTextField.setText("1234");
        enterClickAndLeave(f.addTrackButton);

        // Don't allow train to service car type "Boxcar"
        trainA.deleteTypeName("Boxcar");

        // save button
        enterClickAndLeave(f.saveTrackButton);
        
        // confirm no error dialogue
        Assert.assertTrue(f.isActive());
        
        // specify train pickups using the exclude option
        enterClickAndLeave(f.excludeTrainPickup);
        enterClickAndLeave(f.saveTrackButton);

        // error dialogue should have appeared
        pressDialogButton(f, Bundle.getMessage("ErrorStrandedCar"), Bundle.getMessage("ButtonOK"));

        trainA.addTypeName("Boxcar");

        // save button
        enterClickAndLeave(f.saveTrackButton);

        // confirm no error dialogue
        Assert.assertTrue(f.isActive());

        // disable pick ups by train
        Route route = trainA.getRoute();
        RouteLocation rloc = route.getLastLocationByName(l.getName());
        rloc.setPickUpAllowed(false);

        // save button
        enterClickAndLeave(f.saveTrackButton);

        // error dialogue should have appeared
        pressDialogButton(f, Bundle.getMessage("ErrorStrandedCar"), Bundle.getMessage("ButtonOK"));

        // restore pick ups
        rloc.setPickUpAllowed(true);

        // deselect east, west, north check boxes
        enterClickAndLeave(f.eastCheckBox);
        enterClickAndLeave(f.westCheckBox);
        enterClickAndLeave(f.northCheckBox);
        enterClickAndLeave(f.southCheckBox);

        // save button
        enterClickAndLeave(f.saveTrackButton);

        // confirm no error dialogue
        Assert.assertTrue(f.isActive());

        // Train had only one location in its route, a switcher, now make it a train with two locations
        route.addLocation(lManager.getLocationByName("Test Loc A"));

        // save button
        enterClickAndLeave(f.saveTrackButton);

        // error dialogue should have appeared
        pressDialogButton(f, Bundle.getMessage("ErrorStrandedCar"), Bundle.getMessage("ButtonOK"));

        // train direction default when creating a route is north 
        enterClickAndLeave(f.northCheckBox);
        enterClickAndLeave(f.saveTrackButton);

        // confirm no error dialogue
        Assert.assertTrue(f.isActive());

        // try 0 moves
        rloc.setMaxCarMoves(0);
        enterClickAndLeave(f.saveTrackButton);

        // error dialogue should have appeared
        pressDialogButton(f, Bundle.getMessage("ErrorStrandedCar"), Bundle.getMessage("ButtonOK"));

        // restore move count
        rloc.setMaxCarMoves(5);
        enterClickAndLeave(f.saveTrackButton);
        Assert.assertTrue(f.isActive());

        // try having the train skip the location
        trainA.addTrainSkipsLocation(rloc.getId());

        enterClickAndLeave(f.saveTrackButton);

        // error dialogue should have appeared
        pressDialogButton(f, Bundle.getMessage("ErrorStrandedCar"), Bundle.getMessage("ButtonOK"));

        // kill all frames
        JUnitUtil.dispose(f);
    }

    private void loadLocations() {
        // create 5 locations
        LocationManager lManager = InstanceManager.getDefault(LocationManager.class);
        Location l1 = lManager.newLocation("Test Loc E");
        l1.setLength(1001);
        Location l2 = lManager.newLocation("Test Loc D");
        l2.setLength(1002);
        Location l3 = lManager.newLocation("Test Loc C");
        l3.setLength(1003);
        Location l4 = lManager.newLocation("Test Loc B");
        l4.setLength(1004);
        Location l5 = lManager.newLocation("Test Loc A");
        l5.setLength(1005);

    }

    private void loadTrains() {
        TrainManager trainManager = InstanceManager.getDefault(TrainManager.class);
        trainA = trainManager.newTrain("Test Train A");
        // train needs to service location "l" or error message when saving track edit frame
        RouteManager routeManager = InstanceManager.getDefault(RouteManager.class);
        Route route = routeManager.newRoute("Route Train A");
        route.addLocation(l);
        trainA.setRoute(route);
    }

    // Ensure minimal setup for log4J
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        loadLocations();
        lManager = InstanceManager.getDefault(LocationManager.class);
        l = lManager.getLocationByName("Test Loc C");

        loadTrains();
    }

    @Override
    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }
}
