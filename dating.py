import requests
import json

#Step 2:
#Sends an HTTP Request to the specified endpoint
#Then takes the dictionary that is returned,
#And calculates a new date after adding the given number of seconds.

#Sends http request to an endpoint with expected json dictionary
req = requests.post('http://challenge.code2040.org/api/dating',auth=('username','password'),verify=False,json={"token":"2e7c1620f7482d6a260d512a33bdf5e1"})
headers = {'Content-type':'application/json'}

#Method that takes given date and time and adds the interval to it
def newDate(year,month,day,hour,minute,second,interval):
    #Create new interval in seconds
    interval += (hour*3600)+(minute*60)+second
    
    if(hour == 0):
        hour = 24
        
    #Get new hours,minutes, and seconds
    hour = int(interval/3600)
    interval -= hour*3600
    minute = int(interval/60)
    interval -= minute * 60
    second = interval
    
    if(hour == 24):
        hour = 0
        day += 1
        
    elif(hour > 24):
        day += int(hour/24)
        hour = hour - 24 *int(hour/24)
        
    if(day > 31  and  (month == 1 or month == 3 or month == 5 or month == 7 or month == 8 or month == 10 or month == 12)):
        month += int(day / 31)
        day = day - 31 * int(day / 31)
        
    elif(day > 30  and  (month == 4  or  month == 6  or  month == 9  or  month == 11)):
        month += int(day / 30)
        day = day - 30 * int(day / 30)
    
    elif(year % 400 == 0  and  month == 2  and  day > 29):
        month += int(day / 29)
        day = day - 29 * int(day / 29)
        
    elif(year % 400 != 0  and  month == 2  and  day > 28):
        month += int(day / 28)
        day = day - 28 * int(day / 28)
    
    if(month > 12):
        year += int(month / 12)
        month = month - 12 * int(month / 12)
            
    y = str(year)
    m = str(month)
    d = str(day)
    h = str(hour)
    mn = str(minute)
    s = str(second)

    if(month < 10):
        m = "0"+m
    if(day < 10):
        d = "0" +d
    if(hour < 10):
        h = "0"+h
    if(minute < 10):
        mn = "0" +mn
    if(second < 10):
        s = "0"+s
    
    return  y+ "-" + m + "-" + d + "T" + h + ":" + mn + ":" + s + "Z"


#Gets the text rendered json dictionary
dictionary = json.loads(req.text)

datestamp = dictionary.get("datestamp")
seconds = dictionary.get("interval")

date,time = datestamp.split("T")
years,month,days = date.split("-")

time = time.replace("Z","")
hours,minutes,second = time.split(":")

finalAnswer = newDate(int(years),int(month),int(days),int(hours),int(minutes),int(second),seconds)

#Sends a new http request to an endpoint with expected json dictionary
newReq = requests.post('http://challenge.code2040.org/api/dating/validate',auth=('username','password'),verify=False,json={"token":"2e7c1620f7482d6a260d512a33bdf5e1","datestamp":finalAnswer})
headers = {'Content-type':'application/json'}
#Prints the request response content
print(newReq.content)

