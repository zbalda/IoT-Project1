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

For our implementation, we seperated the logic of button presses, from the "state" of the system, from the LEDs. That is, we set it up such that Buttons changed state variables, and LEDs operated based on the state variables.

To do this we used Pi4J Gpio Pin Listeners to listen for button presses and update our set of state variables after these presses. Each LED ran on its own thread and blinked / slept based on the state variables.

...

#### Challenges

During the initial stage of wiring up the breadboard, we first took the approach of using three digital outputs with various resistors. This approach did not work because when a output pin was not pulsing, it was still grounded and pulled the current away from the diode (LED). After researching how to control the brightness of an LED with a digital output, we found it best to use the PWM(pulse width modulation) method. This fixed our problem because instead of trying to use three outputs, we only need to use one output.

#### Outcome

As the final product, the node run as intended. One button would change the functionality of the second button and the second button either stopped/started the increasing blinking rate of first LED or cycling through brightness levels of the second LED.


## Step 3: Temperature Sensor and LED

#### Goal

The goal of Step 3 is to learn how to use the DS18B20 digital temperature sensor. For this step, our goal was to change the brightness of the LED based on its temperature readings.

#### Implementation

The [DS18B20](https://datasheets.maximintegrated.com/en/ds/DS18B20-PAR.pdf) has three pins and a unique one-wire interface. The pins are:

GND - Ground

DQ - Data In/Out

NC - No Connect

**describe hardware setup**

For getting the temperature from the sensor we use the Pi4J TemperatureSensor, W1Master, and TemperatureScale classes. We initialize our Gpio controller, initialize our LED pin, initialize our W1Master object for getting our temperature sensor device, and save the TemperatureSensor device we get from the W1Master. From a loop in main we repeatedly collect the temperature from our TemperatureSensor device and update the brightness of our LED based on the temperature. We use the same method to update the brightness as we did in Step 2, and we also use the same LED.

The equation we use for setting the pwm brightness of our LED  based on the temperature is:

pwm = 50 * Temperature Celsius - 750

  15°C => 0pwm

  25°C => 500pwm (room temperature => half brightness)

  35°C => 1000pwm

pwm values are "capped" between 0 and 1000. That is, a calculated pwm of -50 would be set to 0 and a calculated pwm of 1050 would be set to 1000.

#### Challenges
Figuring out how to read from the temperature sensor was the most difficult part of this step. Once we figured out how to use the Pi4J TemperatureSensor and W1Master classes we were able to successfully get temperature readings from the sensor. We then used what we learned about LED brightness from Step 2 to change the brightness of the LED based on the temperature.

#### Outcome

In the end the Step 3 application ran exactly as intended. The LED goes to half brightness at room temperature (25°C), is completely off at 10 degrees celsius below room temperature (15°C), and is at full brightness 10 degrees above room temperature (35°C).


## Conclusion

Concluding thoughts.
