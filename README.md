
# **KLASHA INTERVIEW - JAVA 17 - APACHE MAVEN**

---
## SETUP

### 1. LOCAL

- Run `mvn spring-boot:run` in terminal. Service starts on port 8080

### 2. DOCKER
- cd into this directory and run command below to build docker image:
 ```
docker build -t klasha-test:1.0 .
```
- Enter command below to run docker image:
```
docker run -p 8080:8080 klasha-test:1.0
```


---
## BUILD PROCESS
- You could use rest template or web Client if you using reactive. I chose feign client, I think I prefer it.

- Original idea was to get cities by population in descending order up to N, for each of  the 3 countries,
merge them together and use a comparator to pick the top N populated cities. But desc order type does not work,
so I had to get for all the cities for each of the 3 countries even though its expensive on the DB and to the 
application. Then use a comparator to get top N cities with the most population for each country, merge all 3,
and use the comparator again, and then return

- Population count is an array with population for different years for each city. You can as well inner sort 
that descendingly too, but I just got the first one in the array though

- You can also decide to create a dto for all the responses from the external api, since we know how the
response would look like. This is the best approach. But when you not entirely sure of the structure of the
response, its better you leave it open-ended like using JsonNode, which is what I did. Plus its less stress than
creating a dto for each response

- Later found out that order descending is 'dsc' not 'desc' as expected, so changed implementation for question 1;
also no need of comparator again since its sorted desc from the api by population already

- To take care of states and cities, i.e list of cities for state, it'll be a list of lists. The first order lists
would be the states, the 2nd order list would be cities for each state

- 3rd api has high latency. N<sup>2</sup> time complexity (nested for-loops) is never a good idea for API, It might
even time out on countries with many states and many cities in each of those states. I couldn't quickly think of a 
better approach on the fly that wouldn't take too much time to implement

- No time to run unit & integration tests, but you can check the repo below for previous unit/integration tests for 
a previous project:
```
https://github.com/ionknowmyname/Drone-Service-2
```

- For currency conversion, it could go both ways i.e the source could be interchanged with the target. E.g if
50 naira = $1, then $1 = 50 naira; except that in real life, exchange rate is not the same for backwards and 
forward for currency pairs, so I implemented one-way, source to target


## EXTRAS

- I added my postman collection to the root directory