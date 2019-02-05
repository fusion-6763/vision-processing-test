/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.shuffleboard.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  //DifferentialDrive myRobot = new DifferentialDrive(new Spark(0), new Spark(2));

  //SerialPort pixyPort = new SerialPort(19200, SerialPort.Port.kUSB);

  AnalogInput pixyAnalog = new AnalogInput(0);
  AnalogInput widthInput = new AnalogInput(1);
  AnalogInput heightInput = new AnalogInput(2);
  DigitalInput pixyDigital = new DigitalInput(0);

  String data;
  String dataArray[];

  double value;

  DifferentialDrive myRobot = new DifferentialDrive(new Spark(0), new Spark(2));

  Joystick stick = new Joystick(0);

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    //System.out.println(pixyAnalog.getVoltage() * 30.30);
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    //m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    //System.out.println("Auto selected: " + m_autoSelected);
  }
  int x;
  double finalXSpeed;
  boolean seesBall = false;

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    value = pixyAnalog.getVoltage() * 30.30;
    
    double difference = value - 50;
    double cookie = difference / 50;
    double pineapple = cookie * 0.6;

    if(pineapple < 0){
      finalXSpeed = pineapple - 0.4;
    }
    else if(pineapple > 0){
      finalXSpeed = pineapple + 0.4;
    }
      if(pixyDigital.get()){
        myRobot.tankDrive(finalXSpeed, -finalXSpeed);
      }
      else{
        myRobot.tankDrive(0.5, -0.5);
      }

    seesBall = pixyDigital.get();
    //Shuffleboard.getTab("Tab 1").add("Sees ball?", seesBall);
    SmartDashboard.putBoolean("Sees Ball?", seesBall);
  } 

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    /*data = pixyPort.readString();
    if ("".equals(data) == false) {
      dataArray = data.split(",");
      System.out.println(data);
      try{
        x = Integer.parseInt(dataArray[0]);
      }catch(NumberFormatException e){
        x = 159;
      }
    }*/
    //System.out.println(data);

    value = pixyAnalog.getVoltage() * 30.30;
    
    double difference = value - 50;
    double rawXSpeed = difference / 50;
    double scaledXSpeed = rawXSpeed * 0.6;

    if(scaledXSpeed < 0){
      finalXSpeed = scaledXSpeed - 0.4;
    }
    else if(scaledXSpeed > 0){
      finalXSpeed = scaledXSpeed + 0.4;
    }
    if(stick.getRawButton(1)){
      if(pixyDigital.get()){
        myRobot.arcadeDrive(-stick.getY(), finalXSpeed);
      }
      else{
        myRobot.arcadeDrive(-stick.getY(), stick.getX());
      }
    }
    else{
      myRobot.arcadeDrive(-stick.getY(), stick.getX());
    }

    seesBall = pixyDigital.get();
    //Shuffleboard.getTab("Tab 1").add("Sees ball?", seesBall);
    SmartDashboard.putBoolean("Sees Ball?", seesBall);
    //System.out.println(value);
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
