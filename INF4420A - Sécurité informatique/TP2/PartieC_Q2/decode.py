file_object  = open("code.txt", "r")
code = file_object.read();
newString = code

oldValues= ['Z', 'Q','G','L','B','Y','S','E','U','T','R','P','O','@','V','I','C','J','K','H','M','N']
newValue = [' ', 'e','s','o','t','a','h','r','d','n','i','l','m','k','b','f','w','y','p','u','c','g']

withoutZ = code.replace('Z', '')
for i in ['Q','G','L','B','Y','S','E','U','T','R','P','O','@','V','I','C','J','K','H','M','N']:
	occurence = withoutZ.count(i)
	print("Lettre: ", i, "\tpourcentage: ", occurence/len(withoutZ) * 100)

for pos, char in enumerate(oldValues):
	nombreOccurence = code.count(char)
	newString = newString.replace(char, newValue[pos])
	
print("Ancienne chaine: ", code.replace('Z', ' '))
print("Nouvelle chaine: ", newString.replace('  ', '.'))