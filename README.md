# beverage-machine
Library that implements functionality of beverage machine.

Problem description can be found in [Link](archive/README.md)


**Structure of the code:**

With code reuse as my primary motive, I started with Beverage Machine interface,
which lays out the functionality of any beverage machine, alongwith an abstract
BaseBeverageMachine class which implements N outlet beverage machine, leaving the
feature of beverage preparation method for concrete beverage machine class. 

Using BaseBeverageMachine, I created basic-beverage machine, which can be used to
build any custom beverage machine. The basic beverage machines built are:

- HotWaterMachine
- HotMilkMachine
- GreenTeaMachine
- GingerTeaMachine
- ElaichiTeaMachine
- CoffeMachine

Using the above mentioned rudimentary core beverage machine, we can build any machine
which can any combination beverages - hot water, hot milk, green tea, ginger tea,
elaichi tea, hot coffe.

Hence I reused these core machine components to build a multi-functionality beverage
machine - "chai point machine" which serves - hot water, hot milk, green tea,
ginger tea, elaichi tea, hot coffee.

**Class diagram:**
![alt text](archive/images/beverage_machine.png?raw=true)

![alt text](archive/images/ingredient_and_beverage_type.png?raw=true)

#### BeverageComposition:
This class is the recipe book of a beverage and tells quantity of ingredients needed
to prepare a particular beverage. So there are will a beverageComposition instance
for green tea, there can be a different beverage composition for hot coffee, a 
different beverage composition for chai point's green tea.

#### BeverageType:
It enumerates all the type of beverages supported by our library. If we want to
add a new beverage, this will be the starting point where we will add an enum
for the corresponding beverage type.

#### IngredientType:
It enumerates all the types of ingredient supported by our library which serves
as the ingredients to prepare a beverage. The ingredient types are -
- water
- milk
- green mixture
- tea leaves syrup
- elaichi syrup
- ginger syrup
- coffee syrup


#### Ingredient:
It is an interface which fixes the functionality an instance of ingredient will 
expose. The functionalities are associated with -
- getType
- get quantity of ingredient
- check if quantity is sufficient or not
- allowing refill of the ingredient
- retrieving a fixed amount of ingredient for beverage preparation

#### ConcreteIngredient:
Its an implementation of Ingredient interface and ingredient will be an instance 
of this class. The ingredient type and amount of the ingredient passed in the 
argument will differ for different ingredient instance created.

#### BaseBeverageMachine:
It implements the basic functionality of allowing N parallel requests(or people) 
to prepare beverage. This has been ensured with "N" semaphore. It also calls 
abstrace function "retrieve(BeverageType type)" to retrive the ingredients required for a beverage type. The
abstract method is not thread-safe, so the classes which inherits this class
can make it thread-safe. 

So the concrete beverage machine can extend this class and depending on which type 
of beverage they are willing to offer they can implement "retrieve(BeverageType type)"
method.

#### HotWaterMachine:
This extends "BaseBeverageMachine" and implements a thread-safe version of
"retrieve(BeverageType type)" using synchronised construct at method level. This
machine only dispenses hot water and quantity is controlled by an associated
beverage composition instance. 

The class is constructed using "Builder pattern"
to simplify the multiple argument injection and to give a feel of component-by-
component building of a beverage machine.

The beverage machine checks the availability of ingredient before starting the 
process of retrieving the ingredients to the complicated scenario of fetch and
rollback( undo partial retrieve) implementation. Taking a lock and then checking
the ingredients and then retrieving the ingredient is a pessimistic approach but 
simplifies the process of parallel brewing of beverage.

We can check the ingredients which are running low, refill the ingredients if we want
to and check the quantity of each ingredient in the machine.

Similarly **HotMilkMachine, GreenTeaMachine, GingerTeachMachine, ElaichiTeaMachine,
CoffeeMachine** is implemented.

#### ChaiPointMachine
This also inherits BaseBeverageMachine class but it is built using components of
already built machine i.e - HotMilkMachine, GreenTeaMachine ... etc.

If it wants to brew hot coffee, it sends the request to coffee machine put
inside it abstracting the complexities of preparing coffee.

Similarly if it wants to preparge elaichi tea, it sends the request to elaichi
tea machine put inside it.

Similarly for all other beverages it sends the request to respective machine 
put inside it.

The ingredient container is common. That means if water is not present, it wont
allow any machine to prepare beverage which needs water.


**Functional Testing "Test-Coverage":**

Testing is done by reading input_test.json and testing all beverage machine classes 
using the data in this file. The file looks like:

```
{
  "machine": {
    "outlets": {
      "count_n": 3
    },
    "total_items_quantity": {
      "hot_water": 500,
      "hot_milk": 500,
      "ginger_syrup": 100,
      "sugar_syrup": 100,
      "tea_leaves_syrup": 100
    },
    "beverages": {
      "ginger_tea": {
        "hot_water": 200,
        "hot_milk": 100,
        "ginger_syrup": 10,
        "sugar_syrup": 10,
        "tea_leaves_syrup": 30
      },
      "hot_coffee": {
        "hot_water": 100,
        "hot_milk": 400,
        "sugar_syrup": 50,
        "coffee_syrup": 30
      },
      "elaichi_tea": {
        "hot_water": 300,
        "hot_milk": 100,
        "sugar_syrup": 50,
        "tea_leaves_syrup": 30,
        "elaichi_syrup": 30
      },
      "green_tea": {
        "hot_water": 100,
        "ginger_syrup": 30,
        "sugar_syrup": 50,
        "green_mixture": 30
      },
      "hot_water" : {
        "water" : 50
      },
      "hot_milk" : {
        "milk" : 50
      }
    }
  }
}
```
The testing is done using [Junit](https://junit.org/junit5/) and coverage report is generated using [Jacoco](https://www.eclemma.org/jacoco/).

Report:

![alt_text](archive/test_coverage_report.png)


















