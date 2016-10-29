import requests
import json

#Step 4:
#Sends an HTTP Request to the specified endpoint
#Then takes the returned dictionary value
#And determines which words do not contain the given prefix.

#Sends http request to an endpoint with expected json dictionary
req = requests.post('http://challenge.code2040.org/api/prefix',auth=('username','password'), verify =False, json ={"token":"2e7c1620f7482d6a260d512a33bdf5e1"})
headers = {"Content-type":"applications/json"}
print(req.content)

#Gets the text rendered content of the string that is returned and
#Loads it into a python dictionary
dictionary = json.loads(req.text)
print(dictionary)

#Stores the current prefix
prefix = dictionary.get("prefix")
print("Prefix: "+ prefix)

#Stroes the entire list of words at the value of array in the
#dictionary
wordsList = dictionary.get("array")

#Will hold the new list of words that do not contain the prefix
newWordsList = []

#Gets the length of the prefix
prefixLength = len(prefix)

#Searches through the list for words not beginning with the
#specified prefix.
for index, word in enumerate(wordsList):
    
    #Words that do not contain the prefix are added to the new word
    #list
    if(word[:prefixLength] != prefix):
        newWordsList.insert(index,word)

print(newWordsList[:])

#Sends a new http request to an endpoint with expected json
#dictionary
#newReq = requests.post('http://challenge.code2040.org/api/prefix/validate',auth=('username','password'), verify =False, json ={"token":"2e7c1620f7482d6a260d512a33bdf5e1","array":newWordsList})
#headers = {"Content-type":"applications/json"}

#Prints the request response content
#print(newReq.content)
