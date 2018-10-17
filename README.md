# IoT Project 1
CSCI 43300 

Introduction to Internet of Things

Zachary Balda

Corey Stockton

## Project Goal

Learn to use a single node system (Raspberry Pi) to build a simple application.


## Step 1: Setup

For our implementation we used **Java** with the **Pi4J library**.

#### How to Install Pi4J

`apt install pi4j`

#### How to Run with Pi4J
`javac -classpath .:classes:/opt/pi4j/lib/'*' StepX.java`

`sudo java -classpath .:classes:/opt/pi4j/lib/'*' StepX`

#### Challenges Running with Pi4J

After installing Pi4J we encountered a conflict between the Pi4J library and our Raspberry Pi kernel version. We resolved this issue by installing a Pi4J snapshot that did not conflict with our kernel version:

`sudo dpkg -i pi4j-1.2-SNAPSHOT.deb`

#### Running the Blink Application

To test that our Pi4J library and breadboard worked we ran the [Pi4J Blink Application](http://pi4j.com/example/control.html) with an LED connected to GPIO_01. The application ran as designed so we moved on to Step 2.


## Step 2: Buttons and LEDs

#### Goal

The goal of Step 2 is to learn how to use buttons to control LEDs. For this step, we will use two LEDs, two resistors, and two buttons. 

One LED should initially blink once a second. It should automatically blink faster after every few seconds. Once it reaches its fastest frequency, the LED should go back to its slowest frequency. A `primary button` should turn on/off this blink rate increase feature. That is, when the `primary button` is pressed, the blink frequency should stop increasing. When the `primary button` is pressed again, the blink frequency should start increasing again. 4 to 6 frequency levels are recommended. 

The other LED should blink the same way as the first LED. This LED, however, should always have its blink frequency increasing. There is no need for an on/off blink rate increase feature. Instead, a resistor and the `primary button` should be used to control its brightness. When the `primary button` is pressed, the brightness of the LED should change. Once the max brightness is reached, the brightness should be set back to the lowest brightness. At least two levels of brightness are required.

The above two functionalities should share one button, the `primary button`. Another button, the `toggle button`, should be used to switch the behavior of the `primary button`. For example, suppose the `primary button` is controlling the first LED's frequency. When the `toggle button` is pressed, the `primary button` should switch to control the second LED’s brightness.

#### Implementation

Steps took to complete Step 2

#### Challenges

Difficulties faced during implementation.

#### Outcome

Conclusion.


## Step 3: Temperature Sensor and LED

#### Goal

The goal of Step 3 is to learn how to use the DS18B20 digital temperature sensor. For this step, our goal was to change the brightness of the LED based on its temperature readings.

#### Implementation

Steps took to complete Step 3.

#### Challenges

Difficulties faced during implementation.

#### Outcome

Conclusion.


## Conclusion

Concluding thoughts.






