import requests
import json

#Step 2:
#Sends an HTTP Request to the specified endpoint
#Then takes the string that is returned,
#And reverses it then send it back to be validated.

#Sends http request to an endpoint with expected json dictionary
req = requests.post('http://challenge.code2040.org/api/reverse',auth=('username','password'),verify=False,json={"token":"2e7c1620f7482d6a260d512a33bdf5e1"})
headers = {'Content-type':'application/json'}

#Gets the text rendered content of the string that is returned
s = req.text
print(s)

#Uses substring method in order to reverse the string
reverse = s[::-1]
print(reverse)

#Sends a new http request to an endpoint with expected json dictionary
newReq = requests.post('http://challenge.code2040.org/api/reverse/validate',auth=('username','password'),verify=False,json={"token":"2e7c1620f7482d6a260d512a33bdf5e1","string":reverse})
headers = {'Content-type':'application/json'}

#Prints the request response content
print(newReq.content)
