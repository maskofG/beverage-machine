# beverage-machine
library that implements functionality of beverage machine

**Problem description**

Working code to create a working beverage machine. Here are the desired features
1. It will be serving some beverages.
2. Each beverage will be made using some ingredients.
3. Assume time to prepare a beverage is the same for all cases.
4. The quantity of ingredients used for each beverage can vary. Also, the same ingredient (ex:
water) can be used for multiple beverages.
5. There would be N ( N is an integer ) outlet from which beverages can be served.
7. Maximum N beverages can be served in parallel.
8. Any beverage can be served only if all the ingredients are available in terms of quantity.
9. There would be an indicator that would show which all ingredients are running low. We need
some methods to refill them.
10. Please provide functional integration test cases for maximum coverage.


**Example:**
 
Consider **Chai Point machine** which serves these drinks: 
1. ginger tea 
2. elaichi tea 
3. coffee 
4. hot milk 
5. hot water 

the machine has N outlets for serving these drinks 
Here is the composition for each drink: 

ginger tea: 
- hot water 50 ml 
- hot milk 10 ml 
- tea leaves syrup 10 ml 
- ginger syrup 5 ml 
- sugar syrup 10 ml
 
elaichi tea:

- hot water 50 ml 
- hot milk 10 ml 
- tea leaves syrup 10 ml 
- elaichi syrup 5 ml 
- sugar syrup 10 ml 

hot coffee: 
- hot water 50 ml 
- hot milk 10 ml 
- coffee syrup 10 ml 
- sugar syrup 10 ml 

hot milk: 
- milk 50 ml 

hot water 
- water 50 ml 

Note: Since there are N outlets, N people can take beverages at the same time. 

**Input Test Json** : https://www.npoint.io/docs/e8cd5a9bbd1331de326a
 
**Expected Output** : This input can have multiple outputs.
 
Output 1 
- hot_tea is prepared hot_coffee is prepared 
- green_tea cannot be prepared because green_mixture is not available 
- black_tea cannot be prepared because item hot_water is not sufficient 

Or

Output 2 

- hot_tea is prepared black_tea is prepared 
- green_tea cannot be prepared because green_mixture is not available 
- hot_coffee cannot be prepared because item hot_water is not sufficient

Or 

Output 3 
- hot_coffee is prepared black_tea is prepared 
- green_tea cannot be prepared because green_mixture is not available 
- hot_tea cannot be prepared because item hot_water is not sufficient 


###### Structure of the code:

With code reuse as my primary motive, I started with Beverage Machine interface,
which lays out the functionality of any beverage machine, alongwith an abstract
BaseBeverageMachine class which implements N outlet beverage machine, leaving the
feature of beverage preparation method for concrete beverage machine class. 

Using BaseBeverageMachine, I created basic-beverage machine, which can be used to
build any custom beverage machine. The basic beverage machines built are:

- HotWaterBeverageMachine
- HotMilkBeverageMachine
- GreenTeaBeverageMachine
- GingerTeaBeverageMachine
- ElaichiTeaBeverageMachine
- CoffeMachine

Using the above mentioned rudimentary core beverage machine, we can build any machine
which can any combination beverages - hot water, hot milk, green tea, ginger tea,
elaichi tea, hot coffe.

Hence I reused these core machine components to build a multi-functionality beverage
machine - "chai point machine" which serves - hot water, hot milk, green tea,
ginger tea, elaichi tea, hot coffee.

###### Class diagram:






