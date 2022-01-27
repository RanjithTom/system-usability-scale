###Application for SUS Calculation and Statistics.
[System Usability Scale](https://www.usability.gov/how-to-and-tools/methods/system-usability-scale.html)

Application have 2 APIs

1. /sus/compute -> API for calculating the SUS score based on the submitted answers(POST method).

API expects answer points in Integer array of size 10 and of values between 1-5, otherwise API will throw BadRequest error

Sample Request Body 
```json
{
    "answerPoints" : [5,1,5,4,2,3,3,4,5,3]
}
```
Response
```json
{
  "usabilityScore": 62.5
}
```

2. /sus/statistics -> API for getting statistics of average SUS score based on the requested period type(GET method).

API accepts following optional query parameters 
    1. 'period' -> to define the period type, possible values (ALL, HOUR, DAY, WEEK, MONTH, YEAR). By default consider type as ALL
    2. 'start' -> From which date we need to calculate(expecting in LocalDate format - 2001-01-01), by default it will take 6 months
    3. 'end' -> If we need results in particular period we can give end date also, by default consider today's date as end date

Sample Request -> /sus/statistics?period=WEEK

Response 
```json
{
  "periodType": "WEEK",
  "periodScores": [
    {
      "period": "2022-01-24T00:00:00",
      "responseCount": 4,
      "susScore": 62.5
    }
  ]
}
```

Application also dockerized in a simple manner by running the jar file from target.

Sample commands 
1. docker build -t susdocker:1 .
2. docker run -d --name susdocker -p 8080:8080 susdocker:1
3. docker logs -f susdocker
4. docker stop dockerID