import requests

#Step 3:
#Sends an HTTP Request to the specified endpoint
#Then takes the returned dictionary value
#And determines where the needle is in the haystack.


#Sends http request to an endpoint with expected json dictionary
req = requests.post('http://challenge.code2040.org/api/haystack', auth=('username','password'),verify=False,json={"token":"2e7c1620f7482d6a260d512a33bdf5e1"})
headers = {'Content-type':'application/json'}

#Gets the text rendered content of the string that is returned and
#Loads it into a python dictionary
dictionary = json.loads(req.text)
print(dictionary)

#Stores the current value of the "needle"
needle = dictionary.get("needle")
print("Needle : " + needle)

#Stores a listed value of all words in the "haystack"
haystackArray = dictionary.get("haystack")

#Will hold the positon of the needle in the haystack
index = 0

#Searches through the haystack for the word equivalent to current "needle"
for i, s in enumerate(haystackArray):
    #Once the word is found that positio is stored as the index
    if(s == needle):
        index = i

        
print(index)

#Sends a new http request to an endpoint with expected json
#dictionary
newReq = requests.post('http://challenge.code2040.org/api/haystack/validate', auth=('username','password'),verify=False,json={"token":"2e7c1620f7482d6a260d512a33bdf5e1","needle":index})
headers = {'Content-type':'application/json'}

#Prints the request response content
print(newReq.content)
