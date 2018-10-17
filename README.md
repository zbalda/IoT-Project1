# IoT Project 1
CSCI 43300 

Introduction to Internet of Things

Zachary Balda

Corey Stockton


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

To test that our Pi4J library and breadboard worked we ran the [Pi4J Blink Application](http://pi4j.com/example/control.html) with an LED connected to GPIO_01. The application ran as designed so we moved on to step 2.


## Step 2: Buttons and LEDs

#### Goal

Description of Step 2

#### Implementation

Steps took to complete Step 2

#### Challenges

Difficulties faced during implementation.

#### Outcome

Conclusion.


## Step 3: Temperature Sensor and LED

#### Goal

Description of Step 3


#### Implementation

Steps took to complete Step 3.

#### Challenges

Difficulties faced during implementation.

#### Outcome

Conclusion.


## Conclusion

Concluding thoughts.






